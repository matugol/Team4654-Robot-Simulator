package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public abstract interface PWMOutputController
  extends HardwareDevice
{
  public abstract SerialNumber getSerialNumber();
  
  public abstract void setPulseWidthOutputTime(int paramInt1, int paramInt2);
  
  public abstract void setPulseWidthPeriod(int paramInt1, int paramInt2);
  
  public abstract int getPulseWidthOutputTime(int paramInt);
  
  public abstract int getPulseWidthPeriod(int paramInt);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\PWMOutputController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */