package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;

public abstract interface SyncdDevice
{
  public abstract void blockUntilReady()
    throws RobotCoreException, InterruptedException;
  
  public abstract void startBlockingWork();
  
  public abstract boolean hasShutdownAbnormally();
  
  public abstract void setOwner(RobotUsbModule paramRobotUsbModule);
  
  public abstract RobotUsbModule getOwner();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\SyncdDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */