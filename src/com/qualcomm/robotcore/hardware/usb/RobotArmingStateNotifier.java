/*    */ package com.qualcomm.robotcore.hardware.usb;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
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
/*    */ public abstract interface RobotArmingStateNotifier
/*    */ {
/*    */   public abstract SerialNumber getSerialNumber();
/*    */   
/*    */   public abstract ARMINGSTATE getArmingState();
/*    */   
/*    */   public abstract void registerCallback(Callback paramCallback);
/*    */   
/*    */   public abstract void unregisterCallback(Callback paramCallback);
/*    */   
/*    */   public static abstract interface Callback
/*    */   {
/*    */     public abstract void onModuleStateChange(RobotArmingStateNotifier paramRobotArmingStateNotifier, RobotArmingStateNotifier.ARMINGSTATE paramARMINGSTATE);
/*    */   }
/*    */   
/*    */   public static enum ARMINGSTATE
/*    */   {
/* 43 */     ARMED,  PRETENDING,  DISARMED,  CLOSED,  TO_ARMED,  TO_PRETENDING,  TO_DISARMED;
/*    */     
/*    */     private ARMINGSTATE() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\usb\RobotArmingStateNotifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */