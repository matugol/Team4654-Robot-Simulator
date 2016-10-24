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
/*    */ public enum RobotState
/*    */ {
/* 39 */   UNKNOWN(-1), 
/* 40 */   NOT_STARTED(0), 
/* 41 */   INIT(1), 
/* 42 */   RUNNING(2), 
/* 43 */   STOPPED(3), 
/* 44 */   EMERGENCY_STOP(4);
/*    */   
/*    */   private int robotState;
/*    */   
/*    */   private RobotState(int state) {
/* 49 */     this.robotState = ((byte)state);
/*    */   }
/*    */   
/*    */   public byte asByte() {
/* 53 */     return (byte)this.robotState;
/*    */   }
/*    */   
/*    */   public static RobotState fromByte(byte b) {
/* 57 */     for (RobotState robotState : ) {
/* 58 */       if (robotState.robotState == b) {
/* 59 */         return robotState;
/*    */       }
/*    */     }
/* 62 */     return UNKNOWN;
/*    */   }
/*    */   
/*    */   public String toString(Context context) {
/* 66 */     switch (this) {
/* 67 */     case UNKNOWN:  return context.getString(R.string.robotStateUnknown);
/* 68 */     case NOT_STARTED:  return context.getString(R.string.robotStateNotStarted);
/* 69 */     case INIT:  return context.getString(R.string.robotStateInit);
/* 70 */     case RUNNING:  return context.getString(R.string.robotStateRunning);
/* 71 */     case STOPPED:  return context.getString(R.string.robotStateStopped);
/* 72 */     case EMERGENCY_STOP:  return context.getString(R.string.robotStateEmergencyStop); }
/* 73 */     return context.getString(R.string.robotStateInternalError);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robot\RobotState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */