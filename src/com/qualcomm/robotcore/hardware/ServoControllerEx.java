package com.qualcomm.robotcore.hardware;

public abstract interface ServoControllerEx
{
  public abstract void setServoPwmRange(int paramInt, ServoEx.ServoPwmRange paramServoPwmRange);
  
  public abstract ServoEx.ServoPwmRange getServoPwmRange(int paramInt);
  
  public abstract void setServoPwmEnable(int paramInt);
  
  public abstract void setServoPwmDisable(int paramInt);
  
  public abstract boolean isSwervoPwmEnabled(int paramInt);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\ServoControllerEx.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */