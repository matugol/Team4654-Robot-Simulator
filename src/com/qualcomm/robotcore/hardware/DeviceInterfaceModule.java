package com.qualcomm.robotcore.hardware;

public abstract interface DeviceInterfaceModule
  extends DigitalChannelController, AnalogInputController, PWMOutputController, I2cController, AnalogOutputController
{
  public abstract int getDigitalInputStateByte();
  
  public abstract void setDigitalIOControlByte(byte paramByte);
  
  public abstract byte getDigitalIOControlByte();
  
  public abstract void setDigitalOutputByte(byte paramByte);
  
  public abstract byte getDigitalOutputStateByte();
  
  public abstract boolean getLEDState(int paramInt);
  
  public abstract void setLED(int paramInt, boolean paramBoolean);
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DeviceInterfaceModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */