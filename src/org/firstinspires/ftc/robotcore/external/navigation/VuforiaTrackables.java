package org.firstinspires.ftc.robotcore.external.navigation;

import java.util.List;

public abstract interface VuforiaTrackables
  extends List<VuforiaTrackable>
{
  public abstract void setName(String paramString);
  
  public abstract String getName();
  
  public abstract void activate();
  
  public abstract void deactivate();
  
  public abstract VuforiaLocalizer getLocalizer();
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaTrackables.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */