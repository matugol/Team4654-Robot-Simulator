package com.qualcomm.robotcore.hardware;

public abstract interface GyroSensor
  extends HardwareDevice
{
  public abstract void calibrate();
  
  public abstract boolean isCalibrating();
  
  public abstract int getHeading();
  
  public abstract double getRotationFraction();
  
  public abstract int rawX();
  
  public abstract int rawY();
  
  public abstract int rawZ();
  
  public abstract void resetZAxisIntegrator();
  
  public abstract String status();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\GyroSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */