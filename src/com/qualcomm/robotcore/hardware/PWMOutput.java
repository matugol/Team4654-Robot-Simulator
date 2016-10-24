package com.qualcomm.robotcore.hardware;

public abstract interface PWMOutput
  extends HardwareDevice
{
  public abstract void setPulseWidthOutputTime(int paramInt);
  
  public abstract int getPulseWidthOutputTime();
  
  public abstract void setPulseWidthPeriod(int paramInt);
  
  public abstract int getPulseWidthPeriod();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\PWMOutput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */