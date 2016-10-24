/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface CompassSensor
/*    */   extends HardwareDevice
/*    */ {
/*    */   public abstract double getDirection();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract String status();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void setMode(CompassMode paramCompassMode);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract boolean calibrationFailed();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static enum CompassMode
/*    */   {
/* 40 */     MEASUREMENT_MODE, 
/* 41 */     CALIBRATION_MODE;
/*    */     
/*    */     private CompassMode() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\CompassSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */