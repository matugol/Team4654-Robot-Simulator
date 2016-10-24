package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;

public abstract interface AccelerationSensor
  extends HardwareDevice
{
  public abstract Acceleration getAcceleration();
  
  public abstract String status();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\AccelerationSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */