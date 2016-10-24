package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.wifi.NetworkType;

public abstract interface ProgrammingModeController
{
  public abstract void setCurrentNetworkType(NetworkType paramNetworkType);
  
  public abstract boolean isActive();
  
  public abstract void startProgrammingMode(FtcEventLoopHandler paramFtcEventLoopHandler);
  
  public abstract void stopProgrammingMode();
}


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\ProgrammingModeController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */