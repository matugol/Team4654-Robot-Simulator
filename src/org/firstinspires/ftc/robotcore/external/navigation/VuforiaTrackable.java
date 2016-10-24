package org.firstinspires.ftc.robotcore.external.navigation;

import android.support.annotation.Nullable;
import com.vuforia.TrackableResult;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;

public abstract interface VuforiaTrackable
{
  public abstract void setListener(@Nullable Listener paramListener);
  
  public abstract Listener getListener();
  
  public abstract void setLocation(OpenGLMatrix paramOpenGLMatrix);
  
  public abstract OpenGLMatrix getLocation();
  
  public abstract void setUserData(Object paramObject);
  
  public abstract Object getUserData();
  
  public abstract VuforiaTrackables getTrackables();
  
  public abstract void setName(String paramString);
  
  public abstract String getName();
  
  public static abstract interface Listener
  {
    public abstract void onTracked(TrackableResult paramTrackableResult);
    
    public abstract void onNotTracked();
  }
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaTrackable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */