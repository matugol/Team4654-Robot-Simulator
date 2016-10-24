package com.qualcomm.robotcore.hardware;

public abstract interface I2cDeviceSynchSimple
  extends HardwareDevice, I2cAddrConfig
{
  public abstract byte read8(int paramInt);
  
  public abstract byte[] read(int paramInt1, int paramInt2);
  
  public abstract TimestampedData readTimeStamped(int paramInt1, int paramInt2);
  
  public abstract void write8(int paramInt1, int paramInt2);
  
  public abstract void write(int paramInt, byte[] paramArrayOfByte);
  
  public abstract void write8(int paramInt1, int paramInt2, boolean paramBoolean);
  
  public abstract void write(int paramInt, byte[] paramArrayOfByte, boolean paramBoolean);
  
  public abstract void waitForWriteCompletions();
  
  public abstract void enableWriteCoalescing(boolean paramBoolean);
  
  public abstract boolean isWriteCoalescingEnabled();
  
  public abstract boolean isArmed();
  
  @Deprecated
  public abstract void setI2cAddr(I2cAddr paramI2cAddr);
  
  @Deprecated
  public abstract I2cAddr getI2cAddr();
  
  public abstract void setLogging(boolean paramBoolean);
  
  public abstract void setLoggingTag(String paramString);
  
  public static class TimestampedData
  {
    public byte[] data;
    public long nanoTime;
  }
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchSimple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */