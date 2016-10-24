package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public abstract interface AnalogInputController
  extends HardwareDevice
{
  public abstract double getAnalogInputVoltage(int paramInt);
  
  public abstract double getMaxAnalogInputVoltage();
  
  public abstract SerialNumber getSerialNumber();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\AnalogInputController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */