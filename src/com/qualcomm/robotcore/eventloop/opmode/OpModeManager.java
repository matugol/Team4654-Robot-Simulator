package com.qualcomm.robotcore.eventloop.opmode;

public abstract interface OpModeManager
{
  public static final String DEFAULT_OP_MODE_NAME = "$Stop$Robot$";
  
  public abstract void register(String paramString, Class<? extends OpMode> paramClass);
  
  public abstract void register(OpModeMeta paramOpModeMeta, Class<? extends OpMode> paramClass);
  
  public abstract void register(String paramString, OpMode paramOpMode);
  
  public abstract void register(OpModeMeta paramOpModeMeta, OpMode paramOpMode);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\opmode\OpModeManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */