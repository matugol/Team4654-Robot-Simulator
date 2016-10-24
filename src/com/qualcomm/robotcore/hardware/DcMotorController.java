package com.qualcomm.robotcore.hardware;

public abstract interface DcMotorController
  extends HardwareDevice
{
  public abstract void setMotorMode(int paramInt, DcMotor.RunMode paramRunMode);
  
  public abstract DcMotor.RunMode getMotorMode(int paramInt);
  
  public abstract void setMotorPower(int paramInt, double paramDouble);
  
  public abstract double getMotorPower(int paramInt);
  
  public abstract void setMotorMaxSpeed(int paramInt1, int paramInt2);
  
  public abstract int getMotorMaxSpeed(int paramInt);
  
  public abstract boolean isBusy(int paramInt);
  
  public abstract void setMotorZeroPowerBehavior(int paramInt, DcMotor.ZeroPowerBehavior paramZeroPowerBehavior);
  
  public abstract DcMotor.ZeroPowerBehavior getMotorZeroPowerBehavior(int paramInt);
  
  public abstract boolean getMotorPowerFloat(int paramInt);
  
  public abstract void setMotorTargetPosition(int paramInt1, int paramInt2);
  
  public abstract int getMotorTargetPosition(int paramInt);
  
  public abstract int getMotorCurrentPosition(int paramInt);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DcMotorController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */