package com.qualcomm.robotcore.hardware;

public abstract interface LegacyModule
  extends HardwareDevice, I2cController
{
  public abstract void enableAnalogReadMode(int paramInt);
  
  public abstract byte[] readAnalogRaw(int paramInt);
  
  public abstract double readAnalogVoltage(int paramInt);
  
  public abstract double getMaxAnalogInputVoltage();
  
  public abstract void enable9v(int paramInt, boolean paramBoolean);
  
  public abstract void setDigitalLine(int paramInt1, int paramInt2, boolean paramBoolean);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\LegacyModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */