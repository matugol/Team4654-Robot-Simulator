/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.ftccommon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.Binder;

import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

public class FtcRobotControllerService extends Service implements NetworkConnection.NetworkConnectionCallback {

	public final static String TAG = "FTCService";

	private final static double NETWORK_MAX_WAIT = 120.0; // in seconds
	private final static int NETWORK_WAIT = 1000; // in milliseconds
	private final static int USB_WAIT = 5000; // in milliseconds

	private final IBinder binder = new FtcRobotControllerBinder();

	private NetworkConnection networkConnection;
	private EventLoopManager eventLoopManager;
	private Robot robot;
	private EventLoop eventLoop;
	private EventLoop idleEventLoop;

	private WifiDirectAssistant.Event networkConnectionStatus = WifiDirectAssistant.Event.UNKNOWN;
	private RobotStatus robotStatus = RobotStatus.NONE;

	private UpdateUI.Callback callback = null;
	private final EventLoopMonitor eventLoopMonitor = new EventLoopMonitor();

	private final ElapsedTime networkTimer = new ElapsedTime();

	private ExecutorService robotSetupExecutor = null;

	public class FtcRobotControllerBinder extends Binder {
		public FtcRobotControllerService getService() {
			return FtcRobotControllerService.this;
		}
	}

	private class EventLoopMonitor implements EventLoopManager.EventLoopMonitor {

		@Override
		public void onStateChange(@NonNull final RobotState state) {
			if (callback == null) return;
			callback.updateRobotState(state);
			if (state == RobotState.RUNNING) {
				callback.updateRobotStatus(RobotStatus.NONE);
			}
		}

		@Override
		public void onPeerConnected(final boolean peerLikelyChanged) {
			if (callback == null) return;
			callback.updatePeerStatus(PeerStatus.CONNECTED);
		}

		@Override
		public void onPeerDisconnected() {
			if (callback == null) return;
			callback.updatePeerStatus(PeerStatus.DISCONNECTED);
		}

		@Override
		public void onTelemetryTransmitted() {
			if (callback == null) return;
			callback.refreshErrorTextOnUiThread();
		}
	}

	private class RobotSetupRunnable implements Runnable {
		@Override
		public void run() {
			ThreadPool.logThreadLifeCycle("RobotSetupRunnable.run()", new Runnable() {
				@Override
				public void run() {

					try {
						// if an old robot is around, shut it down
						if (robot != null) {
							robot.shutdown();
							robot = null;
						}

						updateRobotStatus(RobotStatus.SCANNING_USB);

						/*
						 * Give android a chance to finish scanning for USB devices before
						 * we create our robot object.
						 * 
						 * It takes Android some time per USB device plugged into a hub.
						 * Higher quality hubs take less time.
						 * 
						 * If USB hubs are chained this can take much longer.
						 */
						try {
							Thread.sleep(USB_WAIT);
						} catch (final InterruptedException e) {
							// we received an interrupt, abort
							updateRobotStatus(RobotStatus.ABORT_DUE_TO_INTERRUPT);
							return;
						}

						if (eventLoopManager == null) {
							eventLoopManager = new EventLoopManager(FtcRobotControllerService.this);
							eventLoopManager.setIdleEventLoop(idleEventLoop);
						}

						robot = RobotFactory.createRobot(eventLoopManager);

						updateRobotStatus(RobotStatus.WAITING_ON_NETWORK);

						// wait for network to come up
						networkTimer.reset();
						if (networkConnection.getNetworkType() != NetworkType.SOFTAP) {
							while (!networkConnection.isConnected()) {
								try {
									Thread.sleep(NETWORK_WAIT);
									if (networkTimer.time() > NETWORK_MAX_WAIT) {
										updateRobotStatus(RobotStatus.NETWORK_TIMED_OUT);
										return;
									}
								} catch (final InterruptedException e) {
									RobotLog.vv(TAG, "interrupt waiting for network; aborting setup");
									return;
								}
							}
						}

						// now that we have network, start up the robot
						updateRobotStatus(RobotStatus.STARTING_ROBOT);
						try {
							robot.eventLoopManager.setMonitor(eventLoopMonitor);
							robot.start(eventLoop);
						} catch (final RobotCoreException e) {
							updateRobotStatus(RobotStatus.FAILED_TO_START_ROBOT);
							RobotLog.setGlobalErrorMsg(e, getString(R.string.globalErrorFailedToStartRobot));
						}
					} catch (final RobotCoreException e) {
						updateRobotStatus(RobotStatus.UNABLE_TO_CREATE_ROBOT);
						RobotLog.setGlobalErrorMsg(e, getString(R.string.globalErrorFailedToCreateRobot));
					}
				}
			});
		}
	}

	public NetworkConnection getNetworkConnection() {
		return networkConnection;
	}

	public NetworkConnection.Event getNetworkConnectionStatus() {
		return networkConnectionStatus;
	}

	public RobotStatus getRobotStatus() {
		return robotStatus;
	}

	public Robot getRobot() {
		return robot;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		RobotLog.vv(TAG, "onCreate()");
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		RobotLog.vv(TAG, "onStartCommand()");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(final Intent intent) {
		RobotLog.vv(TAG, "onBind()");
		RobotLog.vv(TAG, "Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);

		final NetworkType networkType = (NetworkType) intent.getSerializableExtra(NetworkConnectionFactory.NETWORK_CONNECTION_TYPE);
		networkConnection = NetworkConnectionFactory.getNetworkConnection(networkType, getBaseContext());
		networkConnection.setCallback(this);

		networkConnection.enable();
		if (Build.MODEL.equals(Device.MODEL_FOXDA_FL7007)) {
			// wifi channel selection does not work on the FOXDA when running as group owner
			networkConnection.discoverPotentialConnections();
		} else {
			final WifiManager wifiManager = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
			networkConnection.createConnection();
		}

		return binder;
	}

	@Override
	public boolean onUnbind(final Intent intent) {
		RobotLog.vv(TAG, "onUnbind()");

		networkConnection.disable();
		shutdownRobot();

		if (eventLoopManager != null) {
			eventLoopManager.close();
			eventLoopManager = null;
		}

		return false; // don't have new clients call onRebind()
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RobotLog.vv(TAG, "onDestroy()");
	}

	public synchronized void setCallback(final UpdateUI.Callback callback) {
		this.callback = callback;
	}

	public synchronized void setupRobot(final EventLoop eventLoop, final EventLoop idleEventLoop) {

		/*
		 * There is a bug in the Android activity life cycle with regards to apps
		 * launched via USB. To work around this bug we will only honor this
		 * method if setup is not currently running
		 * 
		 * See: https://code.google.com/p/android/issues/detail?id=25701
		 */

		stopRobotSetupThread();

		RobotLog.clearGlobalErrorMsg();
		RobotLog.clearGlobalWarningMsg();
		RobotLog.vv(TAG, "Processing robot setup");

		this.eventLoop = eventLoop;
		this.idleEventLoop = idleEventLoop;

		// setup the robot
		robotSetupExecutor = ThreadPool.newSingleThreadExecutor();
		robotSetupExecutor.execute(new RobotSetupRunnable());
		// once that guy has finished, we don't need him any more
		robotSetupExecutor.shutdown();
	}

	void stopRobotSetupThread() {
		if (robotSetupExecutor != null) {
			robotSetupExecutor.shutdownNow();
			ThreadPool.awaitTerminationOrExitApplication(robotSetupExecutor, 60, TimeUnit.SECONDS, "robot setup", "internal error");
			robotSetupExecutor = null;
		}
	}

	public synchronized void shutdownRobot() {

		stopRobotSetupThread();

		// shut down the robot
		if (robot != null) robot.shutdown();
		robot = null; // need to set robot to null
		updateRobotStatus(RobotStatus.NONE);
	}

	@Override
	public CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event) {
		CallbackResult result = CallbackResult.NOT_HANDLED;
		switch (event) {
			case CONNECTED_AS_GROUP_OWNER:
				RobotLog.ii(TAG, "Wifi Direct - connected as group owner");
				networkConnection.cancelPotentialConnections();
				if (!NetworkConnection.isDeviceNameValid(networkConnection.getDeviceName())) {
					RobotLog.ee(TAG, "Network Connection device name contains non-printable characters");
					ConfigWifiDirectActivity.launch(getBaseContext(), ConfigWifiDirectActivity.Flag.WIFI_DIRECT_DEVICE_NAME_INVALID);
					result = CallbackResult.HANDLED;
				}
				break;
			case CONNECTED_AS_PEER:
				RobotLog.ee(TAG, "Wifi Direct - connected as peer, was expecting Group Owner");
				ConfigWifiDirectActivity.launch(getBaseContext(), ConfigWifiDirectActivity.Flag.WIFI_DIRECT_FIX_CONFIG);
				result = CallbackResult.HANDLED;
				break;
			case CONNECTION_INFO_AVAILABLE:
				RobotLog.ii(TAG, "Network Connection Passphrase: " + networkConnection.getPassphrase());
				break;
			case ERROR:
				RobotLog.ee(TAG, "Network Connection Error: " + networkConnection.getFailureReason());
				break;
			case AP_CREATED:
				RobotLog.ii(TAG, "Network Connection created: " + networkConnection.getConnectionOwnerName());
			default:
				break;
		}

		updateNetworkConnectionStatus(event);
		return result;
	}

	private void updateNetworkConnectionStatus(final WifiDirectAssistant.Event event) {
		networkConnectionStatus = event;
		if (callback != null) callback.networkConnectionUpdate(networkConnectionStatus);
	}

	private void updateRobotStatus(@NonNull final RobotStatus status) {
		robotStatus = status;
		if (callback != null) {
			callback.updateRobotStatus(status);
		}
	}
}
