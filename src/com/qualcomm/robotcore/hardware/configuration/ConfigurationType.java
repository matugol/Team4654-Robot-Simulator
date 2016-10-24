package com.qualcomm.robotcore.hardware.configuration;

import android.content.Context;
import android.support.annotation.NonNull;
import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;

public abstract interface ConfigurationType
{
  @NonNull
  public abstract String getDisplayName(Context paramContext);
  
  @NonNull
  public abstract String getXmlTag();
  
  @NonNull
  public abstract DeviceManager.DeviceType toUSBDeviceType();
  
  public abstract boolean isI2cDevice();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\ConfigurationType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */