package com.qualcomm.robotcore.hardware;

import java.util.concurrent.locks.Lock;

public abstract interface I2cDevice
  extends I2cControllerPortDevice, HardwareDevice
{
  public abstract void enableI2cReadMode(I2cAddr paramI2cAddr, int paramInt1, int paramInt2);
  
  public abstract void enableI2cWriteMode(I2cAddr paramI2cAddr, int paramInt1, int paramInt2);
  
  public abstract boolean isI2cPortInReadMode();
  
  public abstract boolean isI2cPortInWriteMode();
  
  public abstract void readI2cCacheFromController();
  
  public abstract void writeI2cCacheToController();
  
  public abstract void writeI2cPortFlagOnlyToController();
  
  public abstract void setI2cPortActionFlag();
  
  @Deprecated
  public abstract boolean isI2cPortActionFlagSet();
  
  public abstract void clearI2cPortActionFlag();
  
  public abstract byte[] getI2cReadCache();
  
  public abstract Lock getI2cReadCacheLock();
  
  public abstract byte[] getI2cWriteCache();
  
  public abstract Lock getI2cWriteCacheLock();
  
  public abstract byte[] getCopyOfReadBuffer();
  
  public abstract byte[] getCopyOfWriteBuffer();
  
  public abstract void copyBufferIntoWriteBuffer(byte[] paramArrayOfByte);
  
  public abstract void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback paramI2cPortReadyCallback);
  
  public abstract I2cController.I2cPortReadyCallback getI2cPortReadyCallback();
  
  public abstract void deregisterForPortReadyCallback();
  
  public abstract int getCallbackCount();
  
  public abstract boolean isI2cPortReady();
  
  public abstract void registerForPortReadyBeginEndCallback(I2cController.I2cPortReadyBeginEndNotifications paramI2cPortReadyBeginEndNotifications);
  
  public abstract I2cController.I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback();
  
  public abstract void deregisterForPortReadyBeginEndCallback();
  
  public abstract boolean isArmed();
  
  @Deprecated
  public abstract I2cController getController();
  
  @Deprecated
  public abstract void readI2cCacheFromModule();
  
  @Deprecated
  public abstract void writeI2cCacheToModule();
  
  @Deprecated
  public abstract void writeI2cPortFlagOnlyToModule();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */