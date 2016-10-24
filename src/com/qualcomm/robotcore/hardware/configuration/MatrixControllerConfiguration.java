/*    */ package com.qualcomm.robotcore.hardware.configuration;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class MatrixControllerConfiguration extends ControllerConfiguration
/*    */ {
/*    */   private List<DeviceConfiguration> servos;
/*    */   private List<DeviceConfiguration> motors;
/*    */   
/*    */   public MatrixControllerConfiguration(String name, List<DeviceConfiguration> motors, List<DeviceConfiguration> servos, SerialNumber serialNumber)
/*    */   {
/* 14 */     super(name, serialNumber, BuiltInConfigurationType.MATRIX_CONTROLLER);
/* 15 */     this.servos = servos;
/* 16 */     this.motors = motors;
/*    */   }
/*    */   
/*    */   public List<DeviceConfiguration> getServos() {
/* 20 */     return this.servos;
/*    */   }
/*    */   
/*    */   public void setServos(ArrayList<DeviceConfiguration> servos) {
/* 24 */     this.servos = servos;
/*    */   }
/*    */   
/*    */   public List<DeviceConfiguration> getMotors() {
/* 28 */     return this.motors;
/*    */   }
/*    */   
/*    */   public void setMotors(ArrayList<DeviceConfiguration> motors) {
/* 32 */     this.motors = motors;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\MatrixControllerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */