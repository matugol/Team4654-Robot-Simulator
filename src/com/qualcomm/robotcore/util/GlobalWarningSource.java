package com.qualcomm.robotcore.util;

public abstract interface GlobalWarningSource
{
  public abstract String getGlobalWarning();
  
  public abstract void suppressGlobalWarning(boolean paramBoolean);
  
  public abstract boolean setGlobalWarning(String paramString);
  
  public abstract void clearGlobalWarning();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\GlobalWarningSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */