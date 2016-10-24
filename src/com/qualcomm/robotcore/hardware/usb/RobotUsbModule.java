package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;

public abstract interface RobotUsbModule
  extends RobotArmingStateNotifier
{
  public abstract void arm()
    throws RobotCoreException, InterruptedException;
  
  public abstract void pretend()
    throws RobotCoreException, InterruptedException;
  
  public abstract void armOrPretend()
    throws RobotCoreException, InterruptedException;
  
  public abstract void disarm()
    throws RobotCoreException, InterruptedException;
  
  public abstract void close();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\usb\RobotUsbModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */