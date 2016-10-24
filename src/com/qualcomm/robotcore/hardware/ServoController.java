/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface ServoController
/*    */   extends HardwareDevice
/*    */ {
/*    */   public abstract void pwmEnable();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void pwmDisable();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract PwmStatus getPwmStatus();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void setServoPosition(int paramInt, double paramDouble);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract double getServoPosition(int paramInt);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static enum PwmStatus
/*    */   {
/* 45 */     ENABLED,  DISABLED,  MIXED;
/*    */     
/*    */     private PwmStatus() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\ServoController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */