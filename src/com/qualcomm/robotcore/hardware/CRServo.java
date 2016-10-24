package com.qualcomm.robotcore.hardware;

public abstract interface CRServo
  extends DcMotorSimple
{
  public abstract ServoController getController();
  
  public abstract int getPortNumber();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\CRServo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */