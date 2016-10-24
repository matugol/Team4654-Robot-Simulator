/*    */ package com.qualcomm.robotcore.hardware.configuration;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
/*    */ import java.io.Serializable;
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
/*    */ public class MotorControllerConfiguration
/*    */   extends ControllerConfiguration
/*    */   implements Serializable
/*    */ {
/*    */   public MotorControllerConfiguration()
/*    */   {
/* 43 */     super("", new ArrayList(), new SerialNumber(), BuiltInConfigurationType.MOTOR_CONTROLLER);
/*    */   }
/*    */   
/*    */   public MotorControllerConfiguration(String name, List<DeviceConfiguration> motors, SerialNumber serialNumber) {
/* 47 */     super(name, motors, serialNumber, BuiltInConfigurationType.MOTOR_CONTROLLER);
/*    */   }
/*    */   
/*    */   public List<DeviceConfiguration> getMotors() {
/* 51 */     return super.getDevices();
/*    */   }
/*    */   
/*    */   public void setMotors(List<DeviceConfiguration> motors) {
/* 55 */     super.setDevices(motors);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\MotorControllerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */