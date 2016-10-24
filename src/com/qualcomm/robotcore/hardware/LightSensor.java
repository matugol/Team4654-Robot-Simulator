package com.qualcomm.robotcore.hardware;

public abstract interface LightSensor
  extends HardwareDevice
{
  public abstract double getLightDetected();
  
  public abstract double getRawLightDetected();
  
  public abstract double getRawLightDetectedMax();
  
  public abstract void enableLed(boolean paramBoolean);
  
  public abstract String status();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\LightSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */