package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.TelemetryMessage;

/**
 * {@link FtcEventLoopIdle} is an eventloop that runs whenever a full {@link FtcEventLoop} is inappropriate.
 */
public class FtcEventLoopIdle extends FtcEventLoopBase {

	public FtcEventLoopIdle(final HardwareFactory hardwareFactory, final UpdateUI.Callback callback, final Activity activityContext, final ProgrammingModeController programmingModeController) {
		super(hardwareFactory, callback, activityContext, programmingModeController);
	}

	@Override
	public void init(final EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {}

	@Override
	public void loop() throws RobotCoreException, InterruptedException {}

	@Override
	public void refreshUserTelemetry(final TelemetryMessage telemetry, final double sInterval) {}

	@Override
	public void teardown() throws RobotCoreException, InterruptedException {}

	@Override
	public void onUsbDeviceAttached(final UsbDevice usbDevice) {}

	@Override
	public void processedRecentlyAttachedUsbDevices() throws RobotCoreException, InterruptedException {}

	@Override
	public void handleUsbModuleDetach(final RobotUsbModule module) throws RobotCoreException, InterruptedException {}

	@Override
	public OpModeManagerImpl getOpModeManager() {
		return null;
	}

	@Override
	public void requestOpModeStop(final OpMode opModeToStopIfActive) {}
}
