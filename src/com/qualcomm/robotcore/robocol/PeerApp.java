package com.qualcomm.robotcore.robocol;

import android.content.Context;
import android.support.annotation.StringRes;

public abstract interface PeerApp
{
  public abstract Context getContext();
  
  @StringRes
  public abstract int getIdThisApp();
  
  @StringRes
  public abstract int getIdRemoteApp();
  
  public abstract boolean isRobotController();
  
  public abstract boolean isDriverStation();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\PeerApp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */