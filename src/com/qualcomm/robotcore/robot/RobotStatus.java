/*    */ package com.qualcomm.robotcore.robot;
/*    */ 
/*    */ import android.content.Context;
/*    */ import com.qualcomm.robotcore.R.string;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum RobotStatus
/*    */ {
/* 45 */   UNKNOWN,  NONE,  SCANNING_USB,  ABORT_DUE_TO_INTERRUPT,  WAITING_ON_NETWORK,  NETWORK_TIMED_OUT, 
/* 46 */   STARTING_ROBOT,  FAILED_TO_START_ROBOT,  UNABLE_TO_CREATE_ROBOT;
/*    */   
/*    */   private RobotStatus() {}
/*    */   
/* 50 */   public String toString(Context context) { switch (this) {
/*    */     case UNKNOWN: 
/* 52 */       return context.getString(R.string.robotStatusUnknown);
/* 53 */     case NONE:  return "";
/* 54 */     case SCANNING_USB:  return context.getString(R.string.robotStatusScanningUSB);
/* 55 */     case WAITING_ON_NETWORK:  return context.getString(R.string.robotStatusWaitingOnNetwork);
/* 56 */     case NETWORK_TIMED_OUT:  return context.getString(R.string.robotStatusNetworkTimedOut);
/* 57 */     case STARTING_ROBOT:  return context.getString(R.string.robotStatusStartingRobot);
/* 58 */     case FAILED_TO_START_ROBOT:  return context.getString(R.string.robotStatusFailedToStartRobot);
/* 59 */     case UNABLE_TO_CREATE_ROBOT:  return context.getString(R.string.robotStatusUnableToCreateRobot); }
/* 60 */     return context.getString(R.string.robotStatusInternalError);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robot\RobotStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */