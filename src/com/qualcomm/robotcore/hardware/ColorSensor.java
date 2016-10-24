package com.qualcomm.robotcore.hardware;

public abstract interface ColorSensor
  extends HardwareDevice
{
  public abstract int red();
  
  public abstract int green();
  
  public abstract int blue();
  
  public abstract int alpha();
  
  public abstract int argb();
  
  public abstract void enableLed(boolean paramBoolean);
  
  public abstract void setI2cAddress(I2cAddr paramI2cAddr);
  
  public abstract I2cAddr getI2cAddress();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\ColorSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */