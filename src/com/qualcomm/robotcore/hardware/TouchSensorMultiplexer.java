package com.qualcomm.robotcore.hardware;

public abstract interface TouchSensorMultiplexer
  extends HardwareDevice
{
  public abstract boolean isTouchSensorPressed(int paramInt);
  
  public abstract int getSwitches();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\TouchSensorMultiplexer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */