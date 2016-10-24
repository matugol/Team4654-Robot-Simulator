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
/*    */ 
/*    */ public class ServoConfiguration
/*    */   extends DeviceConfiguration
/*    */ {
/*    */   public ServoConfiguration(int port, ConfigurationType type, String name, boolean enabled)
/*    */   {
/* 38 */     super(port, type, name, enabled);
/* 39 */     if ((type == BuiltInConfigurationType.SERVO) || (type == BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO)) return;
/* 40 */     throw new IllegalArgumentException(String.format("ServoConfiguration: illegal servo type: %s", new Object[] { type.toString() }));
/*    */   }
/*    */   
/*    */   public ServoConfiguration(int port, String name, boolean enabled) {
/* 44 */     super(port, BuiltInConfigurationType.SERVO, name, enabled);
/*    */   }
/*    */   
/*    */   public ServoConfiguration(int port) {
/* 48 */     super(port, BuiltInConfigurationType.SERVO, "NO$DEVICE$ATTACHED", false);
/*    */   }
/*    */   
/*    */   public ServoConfiguration(String name) {
/* 52 */     super(BuiltInConfigurationType.SERVO);
/* 53 */     super.setName(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\ServoConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */