/*    */ package com.qualcomm.robotcore.hardware.configuration;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class ServoControllerConfiguration
/*    */   extends ControllerConfiguration
/*    */ {
/*    */   public ServoControllerConfiguration()
/*    */   {
/* 42 */     super("", new ArrayList(), new SerialNumber(), BuiltInConfigurationType.SERVO_CONTROLLER);
/*    */   }
/*    */   
/*    */   public ServoControllerConfiguration(String name, List<DeviceConfiguration> servos, SerialNumber serialNumber) {
/* 46 */     super(name, servos, serialNumber, BuiltInConfigurationType.SERVO_CONTROLLER);
/*    */   }
/*    */   
/*    */   public List<DeviceConfiguration> getServos() {
/* 50 */     return super.getDevices();
/*    */   }
/*    */   
/*    */   public void setServos(List<DeviceConfiguration> servos) {
/* 54 */     super.setDevices(servos);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\ServoControllerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */