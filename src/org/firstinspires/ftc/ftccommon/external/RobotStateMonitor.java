package org.firstinspires.ftc.ftccommon.external;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.robot.RobotStatus;
import org.firstinspires.ftc.robotcore.internal.network.NetworkStatus;
import org.firstinspires.ftc.robotcore.internal.network.PeerStatus;

public abstract interface RobotStateMonitor
{
  public abstract void updateRobotState(@NonNull RobotState paramRobotState);
  
  public abstract void updateRobotStatus(@NonNull RobotStatus paramRobotStatus);
  
  public abstract void updatePeerStatus(@NonNull PeerStatus paramPeerStatus);
  
  public abstract void updateNetworkStatus(@NonNull NetworkStatus paramNetworkStatus, @Nullable String paramString);
  
  public abstract void updateErrorMessage(@Nullable String paramString);
  
  public abstract void updateWarningMessage(@Nullable String paramString);
}


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\org\firstinspires\ftc\ftccommon\external\RobotStateMonitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */