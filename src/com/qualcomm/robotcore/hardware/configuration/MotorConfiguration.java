/*    */ package com.qualcomm.robotcore.hardware.configuration;
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
/*    */ public class MotorConfiguration
/*    */   extends DeviceConfiguration
/*    */ {
/*    */   public MotorConfiguration(int port, String name, boolean enabled)
/*    */   {
/* 37 */     super(port, BuiltInConfigurationType.MOTOR, name, enabled);
/*    */   }
/*    */   
/*    */   public MotorConfiguration(int port) {
/* 41 */     this(port, "NO$DEVICE$ATTACHED", false);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\MotorConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */