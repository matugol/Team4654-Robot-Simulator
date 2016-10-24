package com.qualcomm.robotcore.eventloop;

import android.hardware.usb.UsbDevice;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;

public abstract interface EventLoop
{
  public static final double TELEMETRY_DEFAULT_INTERVAL = NaN.0D;
  
  public abstract void init(EventLoopManager paramEventLoopManager)
    throws RobotCoreException, InterruptedException;
  
  public abstract void loop()
    throws RobotCoreException, InterruptedException;
  
  public abstract void refreshUserTelemetry(TelemetryMessage paramTelemetryMessage, double paramDouble);
  
  public abstract void teardown()
    throws RobotCoreException, InterruptedException;
  
  public abstract void onUsbDeviceAttached(UsbDevice paramUsbDevice);
  
  public abstract void processedRecentlyAttachedUsbDevices()
    throws RobotCoreException, InterruptedException;
  
  public abstract void handleUsbModuleDetach(RobotUsbModule paramRobotUsbModule)
    throws RobotCoreException, InterruptedException;
  
  public abstract CallbackResult processCommand(Command paramCommand)
    throws InterruptedException, RobotCoreException;
  
  public abstract OpModeManagerImpl getOpModeManager();
  
  public abstract void requestOpModeStop(OpMode paramOpMode);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\EventLoop.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */