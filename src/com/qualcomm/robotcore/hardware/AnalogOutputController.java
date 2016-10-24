package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public abstract interface AnalogOutputController
  extends HardwareDevice
{
  public abstract SerialNumber getSerialNumber();
  
  public abstract void setAnalogOutputVoltage(int paramInt1, int paramInt2);
  
  public abstract void setAnalogOutputFrequency(int paramInt1, int paramInt2);
  
  public abstract void setAnalogOutputMode(int paramInt, byte paramByte);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\AnalogOutputController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */