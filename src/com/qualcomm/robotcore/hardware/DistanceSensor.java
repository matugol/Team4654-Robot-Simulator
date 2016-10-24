package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public abstract interface DistanceSensor
  extends HardwareDevice
{
  public abstract double getDistance(DistanceUnit paramDistanceUnit);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DistanceSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */