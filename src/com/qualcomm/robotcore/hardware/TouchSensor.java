package com.qualcomm.robotcore.hardware;

public abstract interface TouchSensor
  extends HardwareDevice
{
  public abstract double getValue();
  
  public abstract boolean isPressed();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\TouchSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */