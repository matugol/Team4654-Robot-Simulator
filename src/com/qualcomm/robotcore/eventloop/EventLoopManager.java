/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 * 
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.robotcore.eventloop;

import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Event Loop Manager
 * <p>
 * Takes RobocolDatagram messages, converts them into the appropriate data type, and then passes it to the current EventLoop.
 */
@SuppressWarnings("unused")
public class EventLoopManager implements RecvLoopRunnable.RecvLoopCallback, NetworkConnection.NetworkConnectionCallback, SendOnceRunnable.ClientCallback {

	// ------------------------------------------------------------------------------------------------
	// Types
	// ------------------------------------------------------------------------------------------------

	/**
	 * Callback to monitor when event loop changes state
	 */
	public interface EventLoopMonitor {
		void onStateChange(@NonNull RobotState state);

		void onTelemetryTransmitted();

		void onPeerConnected(boolean peerLikelyChanged);

		void onPeerDisconnected();
	}

	// ------------------------------------------------------------------------------------------------
	// State
	// ------------------------------------------------------------------------------------------------

	public static final String TAG = "EventLoopManager";
	private static final boolean DEBUG = false;
	private static final int HEARTBEAT_WAIT_DELAY = 500; // in milliseconds
	private static final int MAX_COMMAND_CACHE = 8;

	// We use strings that are unlikely to inadvertently collide with user-specified telemetry keys
	public static final String SYSTEM_NONE_KEY = "$System$None$";
	public static final String SYSTEM_ERROR_KEY = "$System$Error$";
	public static final String SYSTEM_WARNING_KEY = "$System$Warning$";
	public static final String ROBOT_BATTERY_LEVEL_KEY = "$Robot$Battery$Level$";
	public static final String RC_BATTERY_LEVEL_KEY = "$RobotController$Battery$Level$";

	/** If no heartbeat is received in this amount of time, forceable shut down the robot */
	private static final double SECONDS_UNTIL_FORCED_SHUTDOWN = 2.0;
	private EventLoop idleEventLoop = new EmptyEventLoop();
	public RobotState state = RobotState.NOT_STARTED;
	protected boolean isPeerConnected = false;
	private ExecutorService executorEventLoop = ThreadPool.newSingleThreadExecutor();
	private ElapsedTime lastHeartbeatReceived = new ElapsedTime();
	private EventLoop eventLoop = idleEventLoop;
	private final Gamepad gamepad[] = {new Gamepad(), new Gamepad()};
	private Heartbeat heartbeat = new Heartbeat();
	private EventLoopMonitor callback = null;
	private final Set<SyncdDevice> syncdDevices = new CopyOnWriteArraySet<SyncdDevice>();
	private final Command[] commandRecvCache = new Command[MAX_COMMAND_CACHE];
	private int commandRecvCachePosition = 0;
	private InetAddress remoteAddr;
	private final Object refreshSystemTelemetryLock = new Object();
	private String lastSystemTelemetryMessage = null;
	private String lastSystemTelemetryKey = null;
	private long lastSystemTelemetryNanoTime = 0;
	private final @NonNull Context context;
	private final AppUtil appUtil = AppUtil.getInstance();
	private final NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();

	// ------------------------------------------------------------------------------------------------
	// Construction
	// ------------------------------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public EventLoopManager(@NonNull final Context context) {
		this.context = context;
		changeState(RobotState.NOT_STARTED);
	}

	// ------------------------------------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------------------------------------

	public void setIdleEventLoop(final EventLoop idleEventLoop) {
		this.idleEventLoop = idleEventLoop;
	}

	/**
	 * Set a monitor for this event loop
	 *
	 * @param monitor event loop monitor
	 */
	public void setMonitor(final EventLoopMonitor monitor) {
		callback = monitor;
	}

	/** return any event loop monitor previously set */
	public EventLoopMonitor getMonitor() {
		return callback;
	}

	/**
	 * Get the current event loop
	 *
	 * @return current event loop
	 */
	public EventLoop getEventLoop() {
		return eventLoop;
	}

	/**
	 * Get the current gamepad state
	 * <p>
	 * Port 0 is assumed
	 *
	 * @see EventLoopManager#getGamepad(int)
	 * @return gamepad
	 */
	public Gamepad getGamepad() {
		return getGamepad(0);
	}

	/**
	 * Get the gamepad connected to a particular user
	 * 
	 * @param port user 0 and 1 are valid
	 * @return gamepad
	 */
	public Gamepad getGamepad(final int port) {
		Range.throwIfRangeIsInvalid(port, 0, 1);
		return gamepad[port];
	}

	/**
	 * Get the gamepads
	 * <p>
	 * Array index will match the user number
	 * 
	 * @return gamepad
	 */
	public Gamepad[] getGamepads() {
		return gamepad;
	}

	/**
	 * Get the current heartbeat state
	 *
	 * @return heartbeat
	 */
	public Heartbeat getHeartbeat() {
		return heartbeat;
	}

	// ------------------------------------------------------------------------------------------------
	// Runnables
	// ------------------------------------------------------------------------------------------------

	@Override
	public CallbackResult telemetryEvent(final RobocolDatagram packet) {
		return CallbackResult.NOT_HANDLED;
	}

	@Override
	public CallbackResult reportGlobalError(final String error, final boolean recoverable) {
		RobotLog.setGlobalErrorMsg(error);
		return CallbackResult.HANDLED;
	}

	@Override
	public CallbackResult packetReceived(final RobocolDatagram packet) {
		EventLoopManager.this.refreshSystemTelemetry();
		return CallbackResult.NOT_HANDLED; // if we said 'handled', that would supress dispatch by packet type, always, which is
// pointless
	}

	/*
	 * Responsible for calling loop on the assigned event loop
	 */
	private class EventLoopRunnable implements Runnable {
		@Override
		public void run() {
			ThreadPool.logThreadLifeCycle("opmode loop()", new Runnable() {
				@Override
				public void run() {

					try {
						final ElapsedTime loopTime = new ElapsedTime();
						final double MIN_THROTTLE = 0.0010; // in seconds
						final long THROTTLE_RESOLUTION = 5; // in milliseconds

						while (!Thread.interrupted()) {

							while (loopTime.time() < MIN_THROTTLE) {
								// don't go faster than throttle allows
								Thread.sleep(THROTTLE_RESOLUTION);
							}
							loopTime.reset();

							// Send any pending errors or warnings to other apps
							refreshSystemTelemetry();

							if (lastHeartbeatReceived.startTime() == 0.0) {
								// We haven't received a heartbeat so slow the whole thing down
								// Note that the actual disconnect is detected in the lower network layer
								Thread.sleep(HEARTBEAT_WAIT_DELAY);
							}

							// wait for all sync'd devices to be ready
							for (final SyncdDevice device : syncdDevices) {
								device.blockUntilReady();
							}

							// see if any devices have abnormally shutdown and deal with them if so
							for (final SyncdDevice device : syncdDevices) {
								if (device.hasShutdownAbnormally()) {
									RobotLog.v("event loop: device has shutdown abnormally");
									final RobotUsbModule owner = device.getOwner();
									if (owner != null) {
										RobotLog.v("event loop: detaching device owner");
										eventLoop.handleUsbModuleDetach(owner);
									}
								}
							}

							// conversely, if any devices have attached, now is a good time for the eventLoop to process them
							eventLoop.processedRecentlyAttachedUsbDevices();

							// run the event loop
							try {
								eventLoop.loop();
							} catch (final Exception e) {
								// we should catch everything, since we don't know what the event loop might throw
								RobotLog.e("Event loop threw an exception");
								RobotLog.logStacktrace(e);

								// display error message. it will get reported to DS in the RobotCoreException handler below
								final String errorMsg = e.getClass().getSimpleName() + (e.getMessage() != null ? " - " + e.getMessage() : "");
								RobotLog.setGlobalErrorMsg("User code threw an uncaught exception: " + errorMsg);
								throw new RobotCoreException("EventLoop Exception in loop(): %s", errorMsg);
							} finally {
								// notify sync'd devices that the event loop is complete
								for (final SyncdDevice device : syncdDevices) {
									device.startBlockingWork();
								}
							}
						}
					} catch (final InterruptedException e) {
						// interrupted: exit this loop
						RobotLog.v("EventLoopRunnable interrupted");
						changeState(RobotState.STOPPED);
					} catch (final CancellationException e) {
						// interrupted, then cancel thrown: exit this loop
						RobotLog.v("EventLoopRunnable cancelled");
						changeState(RobotState.STOPPED);
					} catch (final RobotCoreException e) {
						RobotLog.v("RobotCoreException in EventLoopManager: " + e.getMessage());
						changeState(RobotState.EMERGENCY_STOP);

						refreshSystemTelemetry();
					}

					// after loop finishes, close all the devices and tear down the event loop.
					try {
						eventLoop.teardown();
					} catch (final Exception e) {
						RobotLog.w("Caught exception during looper teardown: " + e.toString());
						RobotLog.logStacktrace(e);

						refreshSystemTelemetry();
					}
				}
			});
		}
	}

	// ------------------------------------------------------------------------------------------------
	// Misc
	// ------------------------------------------------------------------------------------------------

	/**
	 * Forces an immediate refresh of the system telemetry
	 */
	public void refreshSystemTelemetryNow() {
		lastSystemTelemetryNanoTime = 0;
		refreshSystemTelemetry();
	}

	/**
	 * Do our best to maintain synchrony of the system error / warning state between applications
	 * without incurring undo overhead.
	 */
	public void refreshSystemTelemetry() {

		// As this method can be called from multiple or various threads, we enforce a
		// one-at-a-time policy in the execution
		synchronized (refreshSystemTelemetryLock) {

			String message;
			String key;
			final long now = System.nanoTime();

			final String errorMessage = RobotLog.getGlobalErrorMsg();
			final String warningMessage = RobotLog.getGlobalWarningMessage();

			// Figure out what things *should* like
			if (!errorMessage.isEmpty()) {
				message = errorMessage;
				key = SYSTEM_ERROR_KEY;
			} else if (!warningMessage.isEmpty()) {
				message = warningMessage;
				key = SYSTEM_WARNING_KEY;
			} else {
				message = "";
				key = SYSTEM_NONE_KEY;
			}

			// If nothing has changed, we only retransmit periodically. As this is only to repair
			// the error/warning display on dropped-then-reestablished connections, we don't need to
			// do this very often.
			final long nanoTimeRetransmitInterval = 5000 * ElapsedTime.MILLIS_IN_NANO;

			// If things don't look like they should, or it's been a while (perhaps the driver station
			// is just connected?) then retransmit. Log on the transition of those so we can better correlate
			// log entries with what the user sees.
			final boolean shouldLog = !message.equals(lastSystemTelemetryMessage) || !key.equals(lastSystemTelemetryKey);
			final boolean shouldTransmit = shouldLog || now - lastSystemTelemetryNanoTime > nanoTimeRetransmitInterval;

			if (shouldLog) {
				RobotLog.d("system telemetry: key=%s msg=\"%s\"", key, message);
			}

			if (shouldTransmit) {
				lastSystemTelemetryMessage = message;
				lastSystemTelemetryKey = key;
				lastSystemTelemetryNanoTime = now;
				//
				buildAndSendTelemetry(key, message);
				if (callback != null) callback.onTelemetryTransmitted();
			}
		}
	}

	/*
	 * Empty event loop, used as a sane default
	 */
	private static class EmptyEventLoop implements EventLoop {
		@Override
		public void init(final EventLoopManager eventProcessor) {} // take no action

		@Override
		public void loop() {} // take no action

		@Override
		public void refreshUserTelemetry(final TelemetryMessage telemetry, final double sInterval) {} // take no action

		@Override
		public void teardown() {} // take no action

		@Override
		public void onUsbDeviceAttached(final UsbDevice usbDevice) {} // take no action

		@Override
		public void processedRecentlyAttachedUsbDevices() throws RobotCoreException, InterruptedException {} // take no action

		@Override
		public void handleUsbModuleDetach(final RobotUsbModule module) throws RobotCoreException, InterruptedException {} // take
// no action

		@Override
		public CallbackResult processCommand(final Command command) {
			RobotLog.ww(RobocolDatagram.TAG, "Dropping command " + command.getName() + ", no active event loop");
			return CallbackResult.NOT_HANDLED;
		}

		@Override
		public OpModeManagerImpl getOpModeManager() {
			return null;
		}

		@Override
		public void requestOpModeStop(final OpMode opModeToStopIfActive) {}
	}

	@Override
	public CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event) {
		CallbackResult result = CallbackResult.NOT_HANDLED;
		switch (event) {
			case PEERS_AVAILABLE:
				result = networkConnectionHandler.handlePeersAvailable();
				break;
			case CONNECTION_INFO_AVAILABLE:
				result = networkConnectionHandler.handleConnectionInfoAvailable(SocketConnect.DEFER);
				break;
		}
		return result;
	}

	/**
	 * Starts up the {@link EventLoopManager}. This mostly involves setting up the network
	 * connections and listeners and senders, then getting the event loop thread going.
	 *
	 * Note that shutting down the {@link EventLoopManager} does <em>not</em> do a full
	 * complete inverse. Rather, it leaves the underlying network connection alive and
	 * running, as this, among other things, helps remote toasts to continue to function
	 * correctly. Thus, we must be aware of that possibility here as we start.
	 *
	 * @see #shutdown()
	 */
	public void start(@NonNull final EventLoop eventLoop) throws RobotCoreException {
		RobotLog.vv(RobocolDatagram.TAG, "EventLoopManager.start()");

		networkConnectionHandler.pushNetworkConnectionCallback(this);
		networkConnectionHandler.pushReceiveLoopCallback(this);

		final NetworkType networkType = networkConnectionHandler.getDefaultNetworkType(context);
		networkConnectionHandler.init(networkType, context); // idempotent

		// see also similar code in the driver station startup logic
		if (networkConnectionHandler.isNetworkConnected()) {
			// spoof a wifi direct event. Some devices won't send this event out,
			// so to complete our setup, we will spoof it to get all the necessary information.
			RobotLog.vv(RobocolDatagram.TAG, "Spoofing a Network Connection event...");
			onNetworkConnectionEvent(WifiDirectAssistant.Event.CONNECTION_INFO_AVAILABLE);
		}

		// setEventLoop() will throw if there's a hardware configuration issue. So
		// we do that *after* we start the threads we use to talk to the driver station.
		setEventLoop(eventLoop);
	}

	/**
	 * Performs the logical inverse of {@link #start(EventLoop)}.
	 * 
	 * @see #start(EventLoop)
	 */
	public void shutdown() {
		RobotLog.vv(RobocolDatagram.TAG, "EventLoopManager.shutdown()");
		stopEventLoop();
	}

	public void close() {
		RobotLog.vv(RobocolDatagram.TAG, "EventLoopManager.close()");
		networkConnectionHandler.shutdown();
		networkConnectionHandler.removeNetworkConnectionCallback(this);
		networkConnectionHandler.removeReceiveLoopCallback(this);
	}

	/**
	 * Register a sync'd device
	 *
	 * @param device sync'd device
	 * @see #unregisterSyncdDevice(SyncdDevice)
	 */
	public void registerSyncdDevice(final SyncdDevice device) {
		syncdDevices.add(device);
	}

	/**
	 * Unregisters a device from this event loop. It is specifically permitted to unregister
	 * a device which is not currently registered; such an operation has no effect.
	 *
	 * @param device the device to be unregistered. May not be null.
	 * @see #registerSyncdDevice(SyncdDevice)
	 */
	public void unregisterSyncdDevice(final SyncdDevice device) {
		syncdDevices.remove(device);
	}

	/**
	 * Replace the current event loop with a new event loop
	 *
	 * @param eventLoop new event loop
	 * @throws RobotCoreException if event loop fails to init
	 */
	public void setEventLoop(@NonNull final EventLoop eventLoop) throws RobotCoreException {

		// cancel the old event loop
		stopEventLoop();

		// assign the new event loop
		this.eventLoop = eventLoop;
		RobotLog.vv(RobocolDatagram.TAG, "eventLoop=%s", this.eventLoop.getClass().getSimpleName());

		// start the new event loop
		startEventLoop();
	}

	/**
	 * Send telemetry data
	 * <p>
	 * Send the telemetry data, and then clear the sent data
	 * 
	 * @param telemetry telemetry data
	 */
	public void sendTelemetryData(final TelemetryMessage telemetry) {
		try {
			telemetry.setRobotState(state); // conveying state here helps global errors always be portrayed as in EMERGENCY_STOP
// state rather than waiting until next heartbeat
			networkConnectionHandler.sendDatagram(new RobocolDatagram(telemetry.toByteArrayForTransmission()));
		} catch (final RobotCoreException e) {
			RobotLog.w("Failed to send telemetry data");
			RobotLog.logStacktrace(e);
		}

		// clear the stale telemetry data
		telemetry.clearData();
	}

	private void startEventLoop() throws RobotCoreException {
		// call the init method
		try {
			changeState(RobotState.INIT);
			eventLoop.init(this);

			// notify sync'd devices that the event loop init is complete
			for (final SyncdDevice device : syncdDevices) {
				device.startBlockingWork();
			}
		} catch (final Exception e) {
			RobotLog.w("Caught exception during looper init: " + e.toString());
			RobotLog.logStacktrace(e);
			changeState(RobotState.EMERGENCY_STOP);

			refreshSystemTelemetry();

			throw new RobotCoreException("Robot failed to start: " + e.getMessage());
		}

		// reset the heartbeat timer
		lastHeartbeatReceived = new ElapsedTime(0);

		// start the new event loop
		changeState(RobotState.RUNNING);

		executorEventLoop = ThreadPool.newSingleThreadExecutor();
		executorEventLoop.execute(new EventLoopRunnable());
	}

	private void stopEventLoop() {

		executorEventLoop.shutdownNow();
		ThreadPool.awaitTerminationOrExitApplication(executorEventLoop, 10, TimeUnit.SECONDS, "EventLoop", "possible infinite loop in user code?");

		// inform callback that event loop has been stopped
		changeState(RobotState.STOPPED);

		eventLoop = idleEventLoop;

		// unregister all sync'd devices
		syncdDevices.clear();
	}

	private void changeState(@NonNull final RobotState state) {
		this.state = state;
		RobotLog.v("EventLoopManager state is " + state.toString());
		if (callback != null) callback.onStateChange(state);
	}

	/*
	 * Event processing methods
	 */

	@Override
	public CallbackResult gamepadEvent(final RobocolDatagram packet) throws RobotCoreException {
		if (DEBUG) RobotLog.vv(RobocolDatagram.TAG, "processing gamepad event");
		final Gamepad incomingGamepad = new Gamepad();
		incomingGamepad.fromByteArray(packet.getData());

		if (incomingGamepad.user < 1 || incomingGamepad.user > 2) {
			// this gamepad user is invalid, we cannot use
			RobotLog.d("Gamepad with user %d received. Only users 1 and 2 are valid");
			return CallbackResult.HANDLED;
		}

		final int position = incomingGamepad.user - 1;

		// swap out state of the old gamepad state for the current gamepad state
		gamepad[position].copy(incomingGamepad);

		if (gamepad[0].id == gamepad[1].id) {
			// a gamepad was moved, reset the old gamepad
			RobotLog.v("Gamepad moved position, removing stale gamepad");
			if (position == 0) gamepad[1].copy(new Gamepad());
			if (position == 1) gamepad[0].copy(new Gamepad());
		}
		return CallbackResult.HANDLED;
	}

	@Override
	public CallbackResult heartbeatEvent(final RobocolDatagram packet, final long tReceived) throws RobotCoreException {
		if (SendOnceRunnable.DEBUG) RobotLog.vv(SendOnceRunnable.TAG, "processing received heartbeat");

		final Heartbeat currentHeartbeat = new Heartbeat();
		currentHeartbeat.fromByteArray(packet.getData());
		currentHeartbeat.setRobotState(state);

		currentHeartbeat.t1 = tReceived;
		currentHeartbeat.t2 = Heartbeat.getMsTimeSyncTime();

		packet.setData(currentHeartbeat.toByteArrayForTransmission());
		networkConnectionHandler.sendDatagram(packet);

		lastHeartbeatReceived.reset();
		heartbeat = currentHeartbeat;
		return CallbackResult.HANDLED;
	}

	@Override
	public void peerConnected(final boolean peerLikelyChanged) {
		isPeerConnected = true;
		if (callback != null) {
			callback.onPeerConnected(peerLikelyChanged);
		}
		// The first time we connect, send some useful state to the DS
		if (peerLikelyChanged) {
			UserSensorTypeManager.getInstance().sendUserSensorTypes();
		}
	}

	@Override
	public void peerDisconnected() {
		if (callback != null) {
			callback.onPeerDisconnected();
		}

		if (isPeerConnected) {
			// If we lose contact with the DS, then we auto-stop the robot
			final OpModeManagerImpl opModeManager = eventLoop.getOpModeManager();

			// opModeManager will be null if not running FtcEventLoop right now
			if (opModeManager != null) {
				final String msg = "Lost connection while running op mode: " + opModeManager.getActiveOpModeName();
				opModeManager.initActiveOpMode(OpModeManager.DEFAULT_OP_MODE_NAME);
				RobotLog.i(msg);
			} else {
				RobotLog.i("Lost connection while main event loop not active");
			}

			isPeerConnected = false;
		}
		remoteAddr = null;
		lastHeartbeatReceived = new ElapsedTime(0);
	}

	public CallbackResult peerDiscoveryEvent(final RobocolDatagram packet) throws RobotCoreException {

		networkConnectionHandler.updateConnection(packet, null, this);

		// Send a second PeerDiscovery() packet in response. That will inform the fellow
		// who sent the incoming PeerDiscovery() who *we* are.
		//
		// We should still send a peer discovery packet even if *we* already know the client,
		// because it could be that the connection dropped (e.g., while changing other settings)
		// and the other guy (ie: DS) needs to reconnect. If we don't send this, the connection will
		// never complete. These only get sent about once per second so it's not a huge load on the network.

		final PeerDiscovery outgoing = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
		final RobocolDatagram outgoingDatagram = new RobocolDatagram(outgoing);
		networkConnectionHandler.sendDatagram(outgoingDatagram);

		return CallbackResult.HANDLED;
	}

	@Override
	public CallbackResult commandEvent(final Command command) throws RobotCoreException {
		// called on RecvRunnable.run() thread

		CallbackResult result = CallbackResult.NOT_HANDLED;

		// check if it's in the cache to avoid duplicate executions
		for (final Command c : commandRecvCache) {
			if (c != null && c.equals(command)) {
				// this command is in the cache, which means we've already handled it
				// no need to continue, just return now
				return CallbackResult.HANDLED;
			}
		}

		// cache the command
		commandRecvCache[commandRecvCachePosition++ % commandRecvCache.length] = command;

		// process the command
		try {
			result = eventLoop.processCommand(command);
		} catch (final Exception e) {
			// we should catch everything, since we don't know what the event loop might throw
			RobotLog.e("Event loop threw an exception while processing a command");
			RobotLog.logStacktrace(e);
		}

		return result;
	}

	@Override
	public CallbackResult emptyEvent(final RobocolDatagram packet) {
		return CallbackResult.NOT_HANDLED;
	}

	public void buildAndSendTelemetry(final String tag, final String msg) {
		final TelemetryMessage telemetry = new TelemetryMessage();
		telemetry.setTag(tag);
		telemetry.addData(tag, msg);
		sendTelemetryData(telemetry);
	}
}
