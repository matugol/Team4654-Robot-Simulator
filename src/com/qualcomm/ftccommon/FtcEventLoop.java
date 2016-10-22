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

/*
 * Copyright (c) 2016 Molly Nicholas
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
 * Neither the name of Molly Nicholas nor the names of its contributors may be used to
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

package com.qualcomm.ftccommon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

/**
 * Main event loop to control the robot
 * <p>
 * Modify this class with your own code, or create your own event loop by implementing EventLoop.
 */
public class FtcEventLoop extends FtcEventLoopBase {

	// ------------------------------------------------------------------------------------------------
	// State
	// ------------------------------------------------------------------------------------------------

	protected Utility utility;
	protected USBScanManager usbScanManager;
	protected OpModeManagerImpl opModeManager;
	protected OpModeRegister register;
	protected UsbModuleAttachmentHandler usbModuleAttachmentHandler;
	protected final Object attachedUsbDevicesLock;
	protected Set<String> attachedUsbDevices;
	protected AtomicReference<OpMode> opModeStopRequested;
	protected Map<SerialNumber, ControllerConfiguration> deviceControllers = new HashMap<SerialNumber, ControllerConfiguration>();

	// ------------------------------------------------------------------------------------------------
	// Construction
	// ------------------------------------------------------------------------------------------------

	public FtcEventLoop(final HardwareFactory hardwareFactory, final OpModeRegister register, final UpdateUI.Callback callback, final Activity activityContext, final ProgrammingModeController programmingModeController) {
		super(hardwareFactory, callback, activityContext, programmingModeController);
		opModeManager = createOpModeManager();
		this.register = register;
		usbModuleAttachmentHandler = new DefaultUsbModuleAttachmentHandler();
		attachedUsbDevicesLock = new Object();
		attachedUsbDevices = new HashSet<String>();
		opModeStopRequested = new AtomicReference<OpMode>();
		utility = new Utility(activityContext);
		usbScanManager = null;
	}

	protected OpModeManagerImpl createOpModeManager() {
		return new OpModeManagerImpl(activityContext, new HardwareMap(activityContext));
	}

	// ------------------------------------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------------------------------------

	@Override
	public OpModeManagerImpl getOpModeManager() {
		return opModeManager;
	}

	public UsbModuleAttachmentHandler getUsbModuleAttachmentHandler() {
		return usbModuleAttachmentHandler;
	}

	public void setUsbModuleAttachmentHandler(final UsbModuleAttachmentHandler handler) {
		usbModuleAttachmentHandler = handler;
	}

	// ------------------------------------------------------------------------------------------------
	// Core Event Loop
	// ------------------------------------------------------------------------------------------------

	/**
	 * Init method
	 * <p>
	 * This code will run when the robot first starts up. Place any initialization code in this method.
	 * <p>
	 * It is important to save a copy of the event loop manager from this method, as that is how you'll get access to the
	 * gamepad.
	 * <p>
	 * If an Exception is thrown then the event loop manager will not start the robot.
	 * <p>
	 * Caller synchronizes: called on RobotSetupRunnable.run() thread
	 */
	@Override
	public void init(final EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
		DbgLog.msg("======= INIT START =======");

		opModeManager.init(eventLoopManager);
		opModeManager.registerOpModes(register);

		// Now that we have what is maybe a new set of opmodes, let the DS know that he might want to take a peek
		networkConnectionHandler.sendCommand(new Command(CommandList.CMD_SUGGEST_OP_MODE_LIST_REFRESH));

		ftcEventLoopHandler.init(eventLoopManager);
		final HardwareMap hardwareMap = ftcEventLoopHandler.getHardwareMap();

		ModernRoboticsUsbUtil.init(hardwareMap.appContext, hardwareMap);

		opModeManager.setHardwareMap(hardwareMap);
		hardwareMap.logDevices();

		DbgLog.msg("======= INIT FINISH =======");
	}

	/**
	 * Loop method, this will be called repeatedly while the robot is running.
	 * <p>
	 *
	 * @see com.qualcomm.robotcore.eventloop.EventLoop#loop()
	 *      <p>
	 *      Caller synchronizes: called on EventLoopRunnable.run() thread.
	 */
	@Override
	public void loop() throws RobotCoreException {

		// Atomically capture the OpMode to stop, if any
		final OpMode opModeToStop = opModeStopRequested.getAndSet(null);
		if (opModeToStop != null) {
			processOpModeStopRequest(opModeToStop);
		}

		ftcEventLoopHandler.displayGamePadInfo(opModeManager.getActiveOpModeName());
		final Gamepad gamepads[] = ftcEventLoopHandler.getGamepads();

		opModeManager.runActiveOpMode(gamepads);
	}

	@Override
	public void refreshUserTelemetry(final TelemetryMessage telemetry, final double sInterval) {
		ftcEventLoopHandler.refreshUserTelemetry(telemetry, sInterval);
	}

	/**
	 * Teardown method
	 * <p>
	 * This method will be called when the robot is being shut down. This method should stop the robot. There will be no more
	 * changes to write to the hardware after this method is called.
	 * <p>
	 * If an exception is thrown, then the event loop manager will attempt to shut down the robot without the benefit of this
	 * method.
	 * <p>
	 *
	 * @see com.qualcomm.robotcore.eventloop.EventLoop#teardown()
	 *      <p>
	 *      Caller synchronizes: called on EventLoopRunnable.run() thread.
	 */
	@Override
	public void teardown() throws RobotCoreException {
		DbgLog.msg("======= TEARDOWN =======");

		if (usbScanManager != null) {
			usbScanManager.stopExecutorService();
			usbScanManager = null;
		}

		opModeManager.stopActiveOpMode();
		opModeManager.teardown();

		// Close motor and servo controllers first, since some of them may reside on top
		// of legacy modules: closing first just keeps things more graceful
		ftcEventLoopHandler.closeMotorControllers();
		ftcEventLoopHandler.closeServoControllers();

		// Now close everything that's USB-connected (yes that might re-close a motor or servo
		// controller, but that's ok
		ftcEventLoopHandler.closeAllUsbDevices();

		DbgLog.msg("======= TEARDOWN COMPLETE =======");
	}

	/**
	 * If the driver station sends over a command, it will be routed to this method. You can choose
	 * what to do with this command, or you can just ignore it completely.
	 * <p>
	 * Called on RecvRunnable.run() thread. Method is thread safem, non-interfering
	 */
	@Override
	public CallbackResult processCommand(final Command command) throws InterruptedException, RobotCoreException {
		ftcEventLoopHandler.sendBatteryInfo();

		CallbackResult result = super.processCommand(command);
		if (!result.stopDispatch()) {
			CallbackResult localResult = CallbackResult.HANDLED;

			final String name = command.getName();
			final String extra = command.getExtra();

			if (name.equals(CommandList.CMD_REQUEST_OP_MODE_LIST)) {
				handleCommandRequestOpModeList();
			} else if (name.equals(CommandList.CMD_INIT_OP_MODE)) {
				handleCommandInitOpMode(extra);
			} else if (name.equals(CommandList.CMD_RUN_OP_MODE)) {
				handleCommandRunOpMode(extra);
			} else if (name.equals(CommandList.CMD_SCAN)) {
				handleCommandScan(extra);
			} else {
				localResult = CallbackResult.NOT_HANDLED;
			}
			if (localResult == CallbackResult.HANDLED) {
				result = localResult;
			}
		}
		return result;
	}

	/**
	 * @see FtcConfigurationActivity#doUSBScanAndUpdateUI()
	 */
	protected void handleCommandScan(final String extra) throws RobotCoreException, InterruptedException {
		RobotLog.vv(FtcConfigurationActivity.TAG, "handling command SCAN");

		// Demand-start our local USB scanner in order to save resources.
		if (usbScanManager == null) {
			usbScanManager = new USBScanManager(activityContext, false);
			usbScanManager.startExecutorService(0);
		}

		// Start a scan and wait for it to complete, but if a scan is already in progress, then just wait for that one to finish
		ThreadPool.SingletonResult<ScannedDevices> future = usbScanManager.startDeviceScanIfNecessary();
		final ScannedDevices scannedDevices = future.await();

		// Package up the raw scanned device info and send that back to the DS
		final String data = usbScanManager.packageCommandResponse(scannedDevices);
		RobotLog.vv(FtcConfigurationActivity.TAG, "handleCommandScan data='%s'", data);
		networkConnectionHandler.sendCommand(new Command(CommandList.CMD_SCAN_RESP, data));
	}

	/**
	 * The driver station is requesting our opmode list. Build up an appropriately-delimited
	 * list and send it back to them. Also take this opportunity to forcibly refresh their error/warning
	 * state: the opmode list is only infrequently requested (so sending another datagram isn't
	 * a traffic burden) and it's requested just after a driver station reconnects after a disconnect
	 * (so doing the refresh now is probably an opportune thing to do).
	 */
	protected void handleCommandRequestOpModeList() {

		// We might get a request in really soon, before we're fully together. Wait: the driver
		// station doesn't retry if we were to ignore
		while (!opModeManager.areOpModesRegistered()) {
			Thread.yield();
		}

		// Send the opmode list
		final String opModeList = new Gson().toJson(opModeManager.getOpModes());
		networkConnectionHandler.sendCommand(new Command(CommandList.CMD_REQUEST_OP_MODE_LIST_RESP, opModeList));

		// Send the user sensor type list for robustness (we should have sent it earlier on connect)
		UserSensorTypeManager.getInstance().sendUserSensorTypes();

		final EventLoopManager manager = ftcEventLoopHandler.getEventLoopManager();
		if (manager != null) manager.refreshSystemTelemetryNow(); // null check is paranoia, need isn't verified
	}

	protected void handleCommandInitOpMode(final String extra) {
		final String newOpMode = ftcEventLoopHandler.getOpMode(extra);
		opModeManager.initActiveOpMode(newOpMode);
	}

	protected void handleCommandRunOpMode(final String extra) {
		// Make sure we're in the opmode that the DS thinks we are
		final String newOpMode = ftcEventLoopHandler.getOpMode(extra);
		if (!opModeManager.getActiveOpModeName().equals(newOpMode)) {
			opModeManager.initActiveOpMode(newOpMode);
		}
		opModeManager.startActiveOpMode();
	}

	@Override
	public void requestOpModeStop(final OpMode opModeToStopIfActive) {
		// Note that this may be called from any thread, including an OpMode's loop() thread.
		opModeStopRequested.set(opModeToStopIfActive);
	}

	private void processOpModeStopRequest(final OpMode opModeToStop) {
		// Called on the event loop thread. If the currently active OpMode is the one indicated,
		// we are to stop it and set up the default.

		if (opModeToStop != null && opModeManager.getActiveOpMode() == opModeToStop) {

			DbgLog.msg("auto-stopping OpMode '%s'", opModeManager.getActiveOpModeName());

			// Call stop on the currently active OpMode and init the default one
			opModeManager.stopActiveOpMode();
		}
	}

	// ------------------------------------------------------------------------------------------------
	// Usb connection management
	// ------------------------------------------------------------------------------------------------

	/**
	 * Deal with the fact that a UsbDevice has recently attached to the system
	 *
	 * @param usbDevice
	 */
	@Override
	public void onUsbDeviceAttached(final UsbDevice usbDevice) {
		// Find out who it is. Try twice (for good luck, no, really, to try to work around as-yet-understood
		// situations where we're not getting the data back) if we need to. Note that it's unclear that's
		// ALWAYS a bug (though it clearly is, sometimes): one way we'll fail to get the serial number is that
		// the device might not be an FTDI device. So trying too hard, more than twice, isn't probably good.
		String serialNumber = getSerialNumberOfUsbDevice(usbDevice);
		if (serialNumber == null) serialNumber = getSerialNumberOfUsbDevice(usbDevice);

		// Remember whoever it was for later
		if (serialNumber != null) {
			synchronized (attachedUsbDevicesLock) {
				attachedUsbDevices.add(serialNumber);
			}
		} else {
			// We don't actually understand under what conditions we'll be unable to open an
			// FT_Device for which we're actually receiving a change notification: who else
			// would have it open (for example)? We'd like to do something more, but don't
			// have an idea of what that would look like.
			DbgLog.msg("ignoring: unable get serial number of attached UsbDevice vendor=0x%04x, product=0x%04x device=0x%04x name=%s", usbDevice.getVendorId(), usbDevice.getProductId(), usbDevice.getDeviceId(), usbDevice.getDeviceName());
		}
	}

	protected String getSerialNumberOfUsbDevice(final UsbDevice usbDevice) {
		FT_Device ftDevice = null;
		String serialNumber = null;
		try {
			final D2xxManager manager = D2xxManager.getInstance(activityContext); // note: we're not supposed to close this
			ftDevice = manager.openByUsbDevice(activityContext, usbDevice);
			if (ftDevice != null) {
				serialNumber = ftDevice.getDeviceInfo().serialNumber;
			}
		} catch (final NullPointerException e) {
			// ignored. Not sure if this will be thrown, but the FTDI layer is notorious for doing so
		} catch (final D2xxManager.D2xxException e) {
			// ignored
		} finally {
			if (ftDevice != null) ftDevice.close();
		}
		return serialNumber;
	}

	/**
	 * Process any usb devices that might have recently attached.
	 * Called on the event loop thread.
	 */
	@Override
	public void processedRecentlyAttachedUsbDevices() throws RobotCoreException, InterruptedException {

		// Snarf a copy of the set of serial numbers
		Set<String> serialNumbersToProcess;
		synchronized (attachedUsbDevicesLock) {
			serialNumbersToProcess = attachedUsbDevices;
			attachedUsbDevices = new HashSet<String>();
		}

		// Do we have anyone who can deal with these?
		final UsbModuleAttachmentHandler handler = usbModuleAttachmentHandler;

		// If we have a handler, and there's something to handle, then handle it
		if (handler != null && !serialNumbersToProcess.isEmpty()) {

			// Find all the UsbModules in the current hardware map
			final List<RobotUsbModule> modules = ftcEventLoopHandler.getHardwareMap().getAll(RobotUsbModule.class);

			// For each serial number, find the module with that serial number and ask the handler to deal with it
			for (final String serialNumber : serialNumbersToProcess) {
				for (final RobotUsbModule module : modules) {
					if (module.getSerialNumber().toString().equals(serialNumber)) {
						handler.handleUsbModuleAttach(module);
						break;
					}
				}
			}
		}
	}

	@Override
	public void handleUsbModuleDetach(final RobotUsbModule module) throws RobotCoreException, InterruptedException {
		// Called on the event loop thread
		final UsbModuleAttachmentHandler handler = usbModuleAttachmentHandler;
		if (handler != null) handler.handleUsbModuleDetach(module);
	}

	public class DefaultUsbModuleAttachmentHandler implements UsbModuleAttachmentHandler {

		@Override
		public void handleUsbModuleAttach(final RobotUsbModule module) throws RobotCoreException, InterruptedException {

			final String id = nameOfUsbModule(module);

			DbgLog.msg("======= MODULE ATTACH: disarm %s=======", id);
			module.disarm();

			DbgLog.msg("======= MODULE ATTACH: arm or pretend %s=======", id);
			module.armOrPretend();

			DbgLog.msg("======= MODULE ATTACH: complete %s=======", id);
		}

		@Override
		public void handleUsbModuleDetach(final RobotUsbModule module) throws RobotCoreException, InterruptedException {
			// This provides the default policy for dealing with hardware modules that have experienced
			// abnormal termination because they, e.g., had their USB cable disconnected.

			final String id = nameOfUsbModule(module);

			DbgLog.msg("======= MODULE DETACH RECOVERY: disarm %s=======", id);

			// First thing we do is disarm the module. That will put it in a nice clean state.
			module.disarm();

			DbgLog.msg("======= MODULE DETACH RECOVERY: pretend %s=======", id);

			// Next, to make it appear more normal and functional to its clients, we make it pretend
			module.pretend();

			DbgLog.msg("======= MODULE DETACH RECOVERY: complete %s=======", id);
		}

		String nameOfUsbModule(final RobotUsbModule module) {
			return HardwareFactory.getDeviceDisplayName(activityContext, module.getSerialNumber());
		}
	}
}
