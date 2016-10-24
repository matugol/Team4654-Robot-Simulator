package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;

public abstract interface RobotUsbManager
{
  public abstract int scanForDevices()
    throws RobotCoreException;
  
  public abstract void freezeScanForDevices();
  
  public abstract void thawScanForDevices();
  
  public abstract SerialNumber getDeviceSerialNumberByIndex(int paramInt)
    throws RobotCoreException;
  
  public abstract String getDeviceDescriptionByIndex(int paramInt)
    throws RobotCoreException;
  
  public abstract RobotUsbDevice openBySerialNumber(SerialNumber paramSerialNumber)
    throws RobotCoreException;
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\usb\RobotUsbManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */