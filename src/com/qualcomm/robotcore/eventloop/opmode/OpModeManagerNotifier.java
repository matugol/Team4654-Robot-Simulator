package com.qualcomm.robotcore.eventloop.opmode;

public abstract interface OpModeManagerNotifier
{
  public abstract OpMode registerListener(Notifications paramNotifications);
  
  public abstract void unregisterListener(Notifications paramNotifications);
  
  public static abstract interface Notifications
  {
    public abstract void onOpModePreInit(OpMode paramOpMode);
    
    public abstract void onOpModePreStart(OpMode paramOpMode);
    
    public abstract void onOpModePostStop(OpMode paramOpMode);
  }
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\opmode\OpModeManagerNotifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */