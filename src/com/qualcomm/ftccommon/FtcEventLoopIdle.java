package com.qualcomm.ftccommon;

import android.app.Activity;
import android.hardware.usb.UsbDevice;

import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.TelemetryMessage;

/**
 * {@link FtcEventLoopIdle} is an eventloop that runs whenever a full {@link FtcEventLoop}
 * is inappropriate.
 */
public class FtcEventLoopIdle extends FtcEventLoopBase
    {
    public FtcEventLoopIdle(HardwareFactory hardwareFactory, UpdateUI.Callback callback, Activity activityContext, ProgrammingModeController programmingModeController)
        {
        super(hardwareFactory, callback, activityContext, programmingModeController);
        }

    @Override
    public void init(EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException
        {
        }

    @Override public void loop() throws RobotCoreException, InterruptedException
        {
        }

    @Override public void refreshUserTelemetry(TelemetryMessage telemetry, double sInterval)
        {
        }

    @Override public void teardown() throws RobotCoreException, InterruptedException
        {
        }

    @Override public void onUsbDeviceAttached(UsbDevice usbDevice)
        {
        }

    @Override
    public void processedRecentlyAttachedUsbDevices() throws RobotCoreException, InterruptedException
        {
        }

    @Override
    public void handleUsbModuleDetach(RobotUsbModule module) throws RobotCoreException, InterruptedException
        {
        }

    @Override public OpModeManagerImpl getOpModeManager()
        {
        return null;
        }

    @Override public void requestOpModeStop(OpMode opModeToStopIfActive)
        {
        }
    }
