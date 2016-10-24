/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface HardwareDevice
/*    */ {
/*    */   public abstract Manufacturer getManufacturer();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract String getDeviceName();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract String getConnectionInfo();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract int getVersion();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void resetDeviceConfigurationForOpMode();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void close();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static enum Manufacturer
/*    */   {
/* 40 */     Unknown,  Other,  Lego,  HiTechnic,  ModernRobotics,  Adafruit,  Matrix,  AMS;
/*    */     
/*    */     private Manufacturer() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\HardwareDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */