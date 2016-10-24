package com.qualcomm.robotcore.hardware;

public abstract interface UltrasonicSensor
  extends HardwareDevice
{
  public abstract double getUltrasonicLevel();
  
  public abstract String status();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\UltrasonicSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */