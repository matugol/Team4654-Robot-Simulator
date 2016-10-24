package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.locks.Lock;

public abstract interface I2cController
  extends HardwareDevice
{
  public static final byte I2C_BUFFER_START_ADDRESS = 4;
  
  public abstract SerialNumber getSerialNumber();
  
  public abstract void enableI2cReadMode(int paramInt1, I2cAddr paramI2cAddr, int paramInt2, int paramInt3);
  
  public abstract void enableI2cWriteMode(int paramInt1, I2cAddr paramI2cAddr, int paramInt2, int paramInt3);
  
  public abstract byte[] getCopyOfReadBuffer(int paramInt);
  
  public abstract byte[] getCopyOfWriteBuffer(int paramInt);
  
  public abstract void copyBufferIntoWriteBuffer(int paramInt, byte[] paramArrayOfByte);
  
  public abstract void setI2cPortActionFlag(int paramInt);
  
  public abstract void clearI2cPortActionFlag(int paramInt);
  
  public abstract boolean isI2cPortActionFlagSet(int paramInt);
  
  public abstract void readI2cCacheFromController(int paramInt);
  
  public abstract void writeI2cCacheToController(int paramInt);
  
  public abstract void writeI2cPortFlagOnlyToController(int paramInt);
  
  public abstract boolean isI2cPortInReadMode(int paramInt);
  
  public abstract boolean isI2cPortInWriteMode(int paramInt);
  
  public abstract boolean isI2cPortReady(int paramInt);
  
  public abstract Lock getI2cReadCacheLock(int paramInt);
  
  public abstract Lock getI2cWriteCacheLock(int paramInt);
  
  public abstract byte[] getI2cReadCache(int paramInt);
  
  public abstract byte[] getI2cWriteCache(int paramInt);
  
  public abstract void registerForI2cPortReadyCallback(I2cPortReadyCallback paramI2cPortReadyCallback, int paramInt);
  
  public abstract I2cPortReadyCallback getI2cPortReadyCallback(int paramInt);
  
  public abstract void deregisterForPortReadyCallback(int paramInt);
  
  public abstract void registerForPortReadyBeginEndCallback(I2cPortReadyBeginEndNotifications paramI2cPortReadyBeginEndNotifications, int paramInt);
  
  public abstract I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback(int paramInt);
  
  public abstract void deregisterForPortReadyBeginEndCallback(int paramInt);
  
  public abstract boolean isArmed();
  
  @Deprecated
  public abstract void readI2cCacheFromModule(int paramInt);
  
  @Deprecated
  public abstract void writeI2cCacheToModule(int paramInt);
  
  @Deprecated
  public abstract void writeI2cPortFlagOnlyToModule(int paramInt);
  
  public static abstract interface I2cPortReadyBeginEndNotifications
  {
    public abstract void onPortIsReadyCallbacksBegin(int paramInt)
      throws InterruptedException;
    
    public abstract void onPortIsReadyCallbacksEnd(int paramInt)
      throws InterruptedException;
  }
  
  public static abstract interface I2cPortReadyCallback
  {
    public abstract void portIsReady(int paramInt);
  }
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */