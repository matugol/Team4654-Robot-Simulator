package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;

public abstract interface UsbModuleAttachmentHandler
{
  public abstract void handleUsbModuleAttach(RobotUsbModule paramRobotUsbModule)
    throws RobotCoreException, InterruptedException;
  
  public abstract void handleUsbModuleDetach(RobotUsbModule paramRobotUsbModule)
    throws RobotCoreException, InterruptedException;
}


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\UsbModuleAttachmentHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */