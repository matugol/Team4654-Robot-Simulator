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

package com.qualcomm.robotcore.eventloop.opmode;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Manages Op Modes
 *
 * Able to switch between op modes
 */
@SuppressWarnings("unused")
public class OpModeManagerImpl implements OpModeServices, OpModeManager, OpModeManagerNotifier {

	// ------------------------------------------------------------------------------------------------
	// Types
	// ------------------------------------------------------------------------------------------------

	class OpModeStateTransition {
		String queuedOpModeName = null;
		Boolean opModeSwapNeeded = null;
		Boolean callToInitNeeded = null;
		Boolean gamepadResetNeeded = null;
		Boolean telemetryClearNeeded = null;
		Boolean callToStartNeeded = null;

		void apply() {
			if (queuedOpModeName != null) OpModeManagerImpl.this.queuedOpModeName = queuedOpModeName;

			// We never clear state here; that's done in runActiveOpMode()
			if (opModeSwapNeeded != null) OpModeManagerImpl.this.opModeSwapNeeded = opModeSwapNeeded;
			if (callToInitNeeded != null) OpModeManagerImpl.this.callToInitNeeded = callToInitNeeded;
			if (gamepadResetNeeded != null) OpModeManagerImpl.this.gamepadResetNeeded = gamepadResetNeeded;
			if (telemetryClearNeeded != null) OpModeManagerImpl.this.telemetryClearNeeded = telemetryClearNeeded;
			if (callToStartNeeded != null) OpModeManagerImpl.this.callToStartNeeded = callToStartNeeded;
		}

		OpModeStateTransition copy() {
			final OpModeStateTransition result = new OpModeStateTransition();
			result.queuedOpModeName = queuedOpModeName;
			result.opModeSwapNeeded = opModeSwapNeeded;
			result.callToInitNeeded = callToInitNeeded;
			result.gamepadResetNeeded = gamepadResetNeeded;
			result.telemetryClearNeeded = telemetryClearNeeded;
			result.callToStartNeeded = callToStartNeeded;
			return result;
		}
	}

	// ------------------------------------------------------------------------------------------------
	// State
	// ------------------------------------------------------------------------------------------------

	public static final String DEFAULT_OP_MODE_NAME = OpModeManager.DEFAULT_OP_MODE_NAME;
	public static final OpMode DEFAULT_OP_MODE = new DefaultOpMode();

	protected enum OpModeState {
		INIT, LOOPING
	}

	protected static class OpModeObjectAndMeta {
		public final OpModeMeta meta;
		public final OpMode opMode;

		public OpModeObjectAndMeta(final OpModeMeta meta, final OpMode opMode) {
			this.meta = meta;
			this.opMode = opMode;
		}
	}

	protected Context context;
	protected Map<String, OpModeAndMeta> opModeClasses = new LinkedHashMap<String, OpModeAndMeta>();
	protected Map<String, OpModeObjectAndMeta> opModeObjects = new LinkedHashMap<String, OpModeObjectAndMeta>();
	protected volatile boolean opmodesAreRegistered = false;
	protected String activeOpModeName = DEFAULT_OP_MODE_NAME;
	protected OpMode activeOpMode = DEFAULT_OP_MODE;
	protected String queuedOpModeName = DEFAULT_OP_MODE_NAME;
	protected HardwareMap hardwareMap = null;
	protected EventLoopManager eventLoopManager = null;
	protected final WeakReferenceSet<OpModeManagerNotifier.Notifications> listeners = new WeakReferenceSet<OpModeManagerNotifier.Notifications>();
	protected OpModeStuckCodeMonitor stuckMonitor = null;

	protected OpModeState opModeState = OpModeState.INIT;
	protected boolean opModeSwapNeeded = false;
	protected boolean callToInitNeeded = false;
	protected boolean callToStartNeeded = false;
	protected boolean gamepadResetNeeded = false;
	protected boolean telemetryClearNeeded = false;
	protected AtomicReference<OpModeStateTransition> nextOpModeState = new AtomicReference<OpModeStateTransition>(null);

	protected static final WeakHashMap<Activity, OpModeManagerImpl> mapActivityToOpModeManager = new WeakHashMap<Activity, OpModeManagerImpl>();

	// ------------------------------------------------------------------------------------------------
	// Construction
	// ------------------------------------------------------------------------------------------------

	// Called on FtcRobotControllerService thread
	public OpModeManagerImpl(final Activity activity, final HardwareMap hardwareMap) {
		this.hardwareMap = hardwareMap;

		// register our default op mode first, that way the user can override it (eh?)
		register(DEFAULT_OP_MODE_NAME, DefaultOpMode.class);

		// switch to the default op mode
		initActiveOpMode(DEFAULT_OP_MODE_NAME);

		context = activity;
		synchronized (mapActivityToOpModeManager) {
			mapActivityToOpModeManager.put(activity, this);
		}

	}

	public static OpModeManagerImpl getOpModeManagerOfActivity(final Activity activity) {
		synchronized (mapActivityToOpModeManager) {
			return mapActivityToOpModeManager.get(activity);
		}
	}

	// called from the RobotSetupRunnable.run thread
	public void init(final EventLoopManager eventLoopManager) {
		stuckMonitor = new OpModeStuckCodeMonitor();
		this.eventLoopManager = eventLoopManager;
	}

	public void teardown() {
		stuckMonitor.shutdown();
	}

	// ------------------------------------------------------------------------------------------------
	// Notifications
	// ------------------------------------------------------------------------------------------------

	@Override
	public OpMode registerListener(final OpModeManagerNotifier.Notifications listener) {
		synchronized (listeners) {
			this.listeners.add(listener);
			return this.activeOpMode;
		}
	}

	@Override
	public void unregisterListener(final OpModeManagerNotifier.Notifications listener) {
		synchronized (listeners) {
			this.listeners.remove(listener);
		}
	}

	protected void setActiveOpMode(final OpMode opMode, final String activeOpModeName) {
		synchronized (listeners) {
			this.activeOpMode = opMode;
			this.activeOpModeName = activeOpModeName;
		}
	}

	// ------------------------------------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------------------------------------

	// called from the RobotSetupRunnable.run thread
	public void registerOpModes(final OpModeRegister register) {
		// Call user's code to learn the OpModes
		register.register(this);
		opmodesAreRegistered = true;
	}

	public boolean areOpModesRegistered() {
		return opmodesAreRegistered;
	}

	// called from the RobotSetupRunnable.run thread
	public void setHardwareMap(final HardwareMap hardwareMap) {
		this.hardwareMap = hardwareMap;
	}

	public HardwareMap getHardwareMap() {
		return hardwareMap;
	}

	// ------------------------------------------------------------------------------------------------
	// OpMode management
	// ------------------------------------------------------------------------------------------------

	// called on DS receive thread
	public List<OpModeMeta> getOpModes() {
		Assert.assertTrue(opmodesAreRegistered);
		final List<OpModeMeta> result = new LinkedList<OpModeMeta>();
		for (final OpModeAndMeta opModeAndMetaData : opModeClasses.values()) {
			if (!opModeAndMetaData.meta.name.equals(DEFAULT_OP_MODE_NAME)) {
				result.add(opModeAndMetaData.meta);
			}
		}
		for (final OpModeObjectAndMeta opModeObjectAndMeta : opModeObjects.values()) {
			result.add(opModeObjectAndMeta.meta);
		}
		return result;
	}

	private boolean isOpModeRegistered(final String name) {
		return opModeClasses.containsKey(name) || opModeObjects.containsKey(name);
	}

	// called on DS receive thread, event loop thread
	public String getActiveOpModeName() {
		return activeOpModeName;
	}

	// called on the event loop thread
	public OpMode getActiveOpMode() {
		return activeOpMode;
	}

	// called on DS receive thread
	// initActiveOpMode(DEFAULT_OP_MODE_NAME) is called from event loop thread, FtcRobotControllerService thread
	public void initActiveOpMode(final String name) {

		final OpModeStateTransition newState = new OpModeStateTransition();
		newState.queuedOpModeName = name;
		newState.opModeSwapNeeded = true;
		newState.callToInitNeeded = true;
		newState.gamepadResetNeeded = true;
		newState.telemetryClearNeeded = !name.equals(DEFAULT_OP_MODE_NAME); // no semantic need to clear if we're just stopping
		newState.callToStartNeeded = false;

		// We *insist* on becoming the new state
		nextOpModeState.set(newState);
	}

	// called on DS receive thread
	public void startActiveOpMode() {
		// We're happy to modify an existing (init?) state to then do a start
		OpModeStateTransition existingState = null;
		for (;;) {
			OpModeStateTransition newState;
			if (existingState != null) {
				newState = existingState.copy();
			} else {
				newState = new OpModeStateTransition();
			}
			newState.callToStartNeeded = true;
			if (nextOpModeState.compareAndSet(existingState, newState)) break;
			Thread.yield();
			existingState = nextOpModeState.get();
		}
	}

	// called on the event loop thread
	public void stopActiveOpMode() {
		callActiveOpModeStop();
		initActiveOpMode(DEFAULT_OP_MODE_NAME);
	}

	// called on the event loop thread
	public void runActiveOpMode(final Gamepad[] gamepads) {

		// Apply a state transition if one is pending
		final OpModeStateTransition transition = nextOpModeState.getAndSet(null);
		if (transition != null) transition.apply();

		activeOpMode.time = activeOpMode.getRuntime();
		activeOpMode.gamepad1 = gamepads[0];
		activeOpMode.gamepad2 = gamepads[1];

		// Robustly ensure that gamepad state from previous opmodes doesn't
		// leak into new opmodes.
		if (gamepadResetNeeded) {
			activeOpMode.gamepad1.reset();
			activeOpMode.gamepad2.reset();
			gamepadResetNeeded = false;
		}

		if (telemetryClearNeeded && eventLoopManager != null) {
			// We clear telemetry once 'init' is pressed in order to
			// ensure that stale telemetry from previous OpMode runs is
			// no longer (confusingly) on the screen.
			final TelemetryMessage telemetry = new TelemetryMessage();
			telemetry.addData("\0", "");
			eventLoopManager.sendTelemetryData(telemetry);
			telemetryClearNeeded = false;
		}

		if (opModeSwapNeeded) {
			callActiveOpModeStop();
			performOpModeSwap();
			opModeSwapNeeded = false;
		}

		if (callToInitNeeded) {
			activeOpMode.gamepad1 = gamepads[0];
			activeOpMode.gamepad2 = gamepads[1];
			activeOpMode.hardwareMap = hardwareMap;
			activeOpMode.opModeServices = this;

			// The point about resetting the hardware is to have it in the same state
			// every time for the *user's* code so that they can simplify their initialization
			// logic. There's no point in bothering / spending the time for the default opmode.
			if (!activeOpModeName.equals(DEFAULT_OP_MODE_NAME)) {
				resetHardwareForOpMode();
			}

			activeOpMode.resetStartTime();
			callActiveOpModeInit();
			opModeState = OpModeState.INIT;
			callToInitNeeded = false;
			NetworkConnectionHandler.getInstance().sendCommand(new Command(RobotCoreCommandList.CMD_INIT_OP_MODE_RESP, activeOpModeName)); // send
// *truth* to DS
		}

		if (callToStartNeeded) {
			callActiveOpModeStart();
			opModeState = OpModeState.LOOPING;
			callToStartNeeded = false;
			NetworkConnectionHandler.getInstance().sendCommand(new Command(RobotCoreCommandList.CMD_RUN_OP_MODE_RESP, activeOpModeName)); // send
// *truth* to DS
		}

		if (opModeState == OpModeState.INIT)
			callActiveOpModeInitLoop();
		else if (opModeState == OpModeState.LOOPING) callActiveOpModeLoop();
	}

	// resets the hardware to the state expected at the start of an opmode
	protected void resetHardwareForOpMode() {
		for (final HardwareDevice device : hardwareMap) {
			device.resetDeviceConfigurationForOpMode();
		}
	}

	// not called
	public void logOpModes() {
		final int opModeCount = opModeClasses.size() + opModeObjects.size();
		final String format = "   Op Mode: name=\"%s\" flavor=%s group=\"%s\"";
		RobotLog.i("There are " + opModeCount + " Op Modes");
		for (final Map.Entry<String, OpModeAndMeta> entry : opModeClasses.entrySet()) {
			RobotLog.i(format, entry.getKey(), entry.getValue().meta.flavor, entry.getValue().meta.group);
		}
		for (final Map.Entry<String, OpModeObjectAndMeta> entry : opModeObjects.entrySet()) {
			RobotLog.i(format, entry.getKey(), entry.getValue().meta.flavor, entry.getValue().meta.group);
		}
	}

	// Called on FtcRobotControllerService thread
	/**
	 * Registers an OpMode <em>class</em> with the name by which it should be known in the driver station.
	 *
	 * @param name the name of the OpMode in the driver station
	 * @param opMode the OpMode class to instantiate when that OpMode is selected
	 */
	@Override
	public void register(final String name, final Class<? extends OpMode> opMode) {
		register(new OpModeMeta(name), opMode);
	}

	@Override
	public void register(final OpModeMeta meta, final Class<? extends OpMode> opMode) {
		if (isOpModeRegistered(meta.name)) {
			throw new IllegalArgumentException(String.format("Can't register OpMode name twice: '%s'", meta.name));
		}
		opModeClasses.put(meta.name, new OpModeAndMeta(meta, opMode));
		RobotLog.dd(AnnotatedOpModeRegistrar.TAG, String.format("registered {%s} as {%s}", opMode.getSimpleName(), meta.name));
	}

	// Called on FtcRobotControllerService thread
	/**
	 * Registers an OpMode <em>instance</em> with the name by which it should be known in the driver station.
	 * This should only be used in environments where it is not possible to pass Class objects. In particular, it
	 * should not be used with OpModes authored (in Java) using Android Studio.
	 *
	 * @param name the name of the OpMode in the driver station
	 * @param opMode the OpMode instance to use when that OpMode is selected
	 */
	@Override
	public void register(final String name, final OpMode opMode) {
		register(new OpModeMeta(name), opMode);
	}

	@Override
	public void register(final OpModeMeta meta, final OpMode opMode) {
		if (isOpModeRegistered(meta.name)) {
			throw new IllegalArgumentException(String.format("Can't register OpMode name twice: '%s'", meta.name));
		}
		opModeObjects.put(meta.name, new OpModeObjectAndMeta(meta, opMode));
		RobotLog.dd(AnnotatedOpModeRegistrar.TAG, String.format("registered instance as {%s}", meta));
	}

	private void performOpModeSwap() {
		RobotLog.i("Attempting to switch to op mode " + queuedOpModeName);

		try {
			if (opModeObjects.containsKey(queuedOpModeName)) {
				setActiveOpMode(opModeObjects.get(queuedOpModeName).opMode, queuedOpModeName);
			} else {
				setActiveOpMode(opModeClasses.get(queuedOpModeName).clazz.newInstance(), queuedOpModeName);
			}
		} catch (final InstantiationException e) {
			failedToSwapOpMode(e);
		} catch (final IllegalAccessException e) {
			failedToSwapOpMode(e);
		}
	}

	private void failedToSwapOpMode(final Exception e) {
		RobotLog.e("Unable to start op mode " + activeOpModeName);
		RobotLog.logStacktrace(e);
		setActiveOpMode(DEFAULT_OP_MODE, DEFAULT_OP_MODE_NAME);
	}

	protected void callActiveOpModeStop() {
		detectStuck("stop()", () -> activeOpMode.stop());
		synchronized (listeners) {
			for (OpModeManagerNotifier.Notifications listener : this.listeners) {
				listener.onOpModePostStop(activeOpMode);
			}
		}
		for (final HardwareDevice device : hardwareMap) {
			if (device instanceof OpModeManagerNotifier.Notifications) {
				((OpModeManagerNotifier.Notifications) device).onOpModePostStop(activeOpMode);
			}
		}
	}

	protected void detectStuck(final String method, final Runnable runnable) {
		detectStuck(method, runnable, false);
	}

	protected void detectStuck(final String method, final Runnable runnable, final boolean resetDebuggerCheck) {
		stuckMonitor.startMonitoring(method, resetDebuggerCheck);
		try {
			runnable.run();
		} finally {
			stuckMonitor.stopMonitoring();
		}
	}

	/** A utility class that detects infinite loops in user code */
	protected class OpModeStuckCodeMonitor {
		ExecutorService executorService = ThreadPool.newSingleThreadExecutor();
		Semaphore stopped = new Semaphore(0);
		CountDownLatch acquired = null;
		boolean debuggerDetected = false;
		String method;

		public void startMonitoring(final String method, final boolean resetDebuggerCheck) {
			// Wait for any previous monitoring to drain
			if (acquired != null) {
				try {
					acquired.await();
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			this.method = method;
			stopped.drainPermits();
			acquired = new CountDownLatch(1);
			executorService.execute(new Runner());
			if (resetDebuggerCheck) {
				debuggerDetected = false;
			}
		}

		public void stopMonitoring() {
			stopped.release();
		}

		public void shutdown() {
			executorService.shutdownNow();
		}

		protected boolean checkForDebugger() {
			// Once we see a debugger, we disable timeout checking for the remainder of the OpMode
			// in order to be sure to avoid premature termination of the app.
			debuggerDetected = debuggerDetected || Debug.isDebuggerConnected();
			return debuggerDetected;
		}

		protected class Runner implements Runnable {
			@Override
			public void run() {
				boolean errorWasSet = false;
				try {
					// We won't bother timing if a debugger is attached because single stepping
					// etc in a debugger can take an arbitrarily long amount of time.
					if (checkForDebugger()) return;

					if (!stopped.tryAcquire(5, TimeUnit.SECONDS)) {
						// Timeout hit waiting for opmode to stop. Inform user, then restart app.
						if (checkForDebugger()) return;

						final String message = String.format(context.getString(R.string.errorOpModeStuck), activeOpModeName, method);
						errorWasSet = RobotLog.setGlobalErrorMsg(message);
						RobotLog.e(message);
						AppUtil.getInstance().showToast(context, String.format(context.getString(R.string.toastOpModeStuck), method));
						// Wait a touch for message to be seen
						Thread.sleep(1000);
						// Restart
						AppUtil.getInstance().restartApp(-1);
					}
				} catch (final InterruptedException e) {
					// Shutdown complete, return
					if (errorWasSet) RobotLog.clearGlobalErrorMsg();
				} finally {
					acquired.countDown();
				}
			}
		}
	}

	protected void callActiveOpModeInit() {
		synchronized (listeners) {
			for (OpModeManagerNotifier.Notifications listener : this.listeners) {
				listener.onOpModePreInit(activeOpMode);
			}
		}
		for (final HardwareDevice device : hardwareMap) {
			if (device instanceof OpModeManagerNotifier.Notifications) {
				((OpModeManagerNotifier.Notifications) device).onOpModePreInit(activeOpMode);
			}
		}

		activeOpMode.preInit();
		detectStuck("init()", () -> activeOpMode.init(), true);
	}

	protected void callActiveOpModeStart() {
		synchronized (listeners) {
			for (OpModeManagerNotifier.Notifications listener : this.listeners) {
				listener.onOpModePreStart(activeOpMode);
			}
		}
		for (final HardwareDevice device : hardwareMap) {
			if (device instanceof OpModeManagerNotifier.Notifications) {
				((OpModeManagerNotifier.Notifications) device).onOpModePreStart(activeOpMode);
			}
		}
		detectStuck("start()", () -> activeOpMode.start());
	}

	protected void callActiveOpModeInitLoop() {
		detectStuck("init_loop()", () -> activeOpMode.init_loop());
		activeOpMode.postInitLoop();
	}

	protected void callActiveOpModeLoop() {
		detectStuck("loop()", () -> activeOpMode.loop());
		activeOpMode.postLoop();
	}

	// ------------------------------------------------------------------------------------------------
	// OpModeServices
	// ------------------------------------------------------------------------------------------------

	/** For the use of {@link TelemetryImpl}. */
	public static void updateTelemetryNow(final OpMode opMode, final TelemetryMessage telemetry) {
		opMode.updateTelemetryNow(telemetry);
	}

	@Override
	public void refreshUserTelemetry(final TelemetryMessage telemetry, final double sInterval) {
		eventLoopManager.getEventLoop().refreshUserTelemetry(telemetry, sInterval);
	}

	/**
	 * Requests that an OpMode be stopped.
	 *
	 * @see OpMode#requestOpModeStop()
	 */
	@Override
	public void requestOpModeStop(final OpMode opModeToStopIfActive) {
		// We have two basic concerns: (a) is the indicated opMode the active one, and (b) we might
		// here be running on literally any thread, including the loop() thread or a linear OpMode's
		// thread.
		eventLoopManager.getEventLoop().requestOpModeStop(opModeToStopIfActive);
	}

	// ------------------------------------------------------------------------------------------------
	// Default OpMode
	// ------------------------------------------------------------------------------------------------

	/**
	 * {@link DefaultOpMode} is the opmode that the system runs when no user opmode is active.
	 * Note that it's not necessarily the case that this opmode runs when a user opmode stops: there
	 * are situations in which we can transition directly for one user opmode to another.
	 */
	public static class DefaultOpMode extends OpMode {

		// ----------------------------------------------------------------------------------------------
		// State
		// ----------------------------------------------------------------------------------------------

		private long nanoFirstSafe;

		// ----------------------------------------------------------------------------------------------
		// Construction
		// ----------------------------------------------------------------------------------------------

		public DefaultOpMode() {
			nanoFirstSafe = 0;
		}

		// ----------------------------------------------------------------------------------------------
		// Loop operations
		// ----------------------------------------------------------------------------------------------

		@Override
		public void init() {
			moveToSafeState();
		}

		@Override
		public void init_loop() {
			moveToSafeState();
			telemetry.addData("Status", "Robot is stopped");
		}

		@Override
		public void loop() {
			moveToSafeState();
			telemetry.addData("Status", "Robot is stopped");
		}

		@Override
		public void stop() {
			// take no action
		}

		private void moveToSafeState() {

			if (nanoFirstSafe == 0) nanoFirstSafe = System.nanoTime();

			final long now = System.nanoTime();

			// Set all motor powers to zero. The implementation here will also stop any CRServos.
			for (final DcMotorSimple motor : hardwareMap.getAll(DcMotorSimple.class)) {
				motor.setPower(0);
			}

			// set their run mode to something reasonable, and safe. we do this *after*
			// zeroing the powers as switching modes, on the legacy controller at least,
			// is not especially zippy. separating out like this is probably paranoia, but
			// it's harmless
			for (final DcMotor dcMotor : hardwareMap.getAll(DcMotor.class)) {
				if (true) {
					dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
				}
			}

			// power down the servos
			for (final ServoController servoController : hardwareMap.getAll(ServoController.class)) {
				if (true) {
					servoController.pwmDisable();
				}
			}

			// turn of light sensors
			for (final LightSensor light : hardwareMap.getAll(LightSensor.class)) {
				if (true) {
					light.enableLed(false);
				}
			}
		}
	}
}
