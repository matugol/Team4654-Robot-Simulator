package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.robocol.TelemetryMessage;

abstract interface OpModeServices
{
  public abstract void refreshUserTelemetry(TelemetryMessage paramTelemetryMessage, double paramDouble);
  
  public abstract void requestOpModeStop(OpMode paramOpMode);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\opmode\OpModeServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */