/*    */ package com.qualcomm.robotcore.hardware.configuration;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
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
/*    */ public class DeviceInterfaceModuleConfiguration
/*    */   extends ControllerConfiguration
/*    */ {
/*    */   private List<DeviceConfiguration> pwmOutputs;
/*    */   private List<DeviceConfiguration> i2cDevices;
/*    */   private List<DeviceConfiguration> analogInputDevices;
/*    */   private List<DeviceConfiguration> digitalDevices;
/*    */   private List<DeviceConfiguration> analogOutputDevices;
/*    */   
/*    */   public DeviceInterfaceModuleConfiguration(String name, SerialNumber serialNumber)
/*    */   {
/* 47 */     super(name, serialNumber, BuiltInConfigurationType.DEVICE_INTERFACE_MODULE);
/*    */   }
/*    */   
/*    */   public void setPwmOutputs(List<DeviceConfiguration> pwmDevices) {
/* 51 */     this.pwmOutputs = pwmDevices;
/*    */   }
/*    */   
/*    */   public List<DeviceConfiguration> getPwmOutputs() {
/* 55 */     return this.pwmOutputs;
/*    */   }
/*    */   
/*    */   public List<DeviceConfiguration> getI2cDevices() {
/* 59 */     return this.i2cDevices;
/*    */   }
/*    */   
/*    */   public void setI2cDevices(List<DeviceConfiguration> i2cDevices) {
/* 63 */     this.i2cDevices = i2cDevices;
/*    */   }
/*    */   
/*    */   public List<DeviceConfiguration> getAnalogInputDevices() {
/* 67 */     return this.analogInputDevices;
/*    */   }
/*    */   
/*    */   public void setAnalogInputDevices(List<DeviceConfiguration> analogInputDevices) {
/* 71 */     this.analogInputDevices = analogInputDevices;
/*    */   }
/*    */   
/*    */   public List<DeviceConfiguration> getDigitalDevices() {
/* 75 */     return this.digitalDevices;
/*    */   }
/*    */   
/*    */   public void setDigitalDevices(List<DeviceConfiguration> digitalDevices) {
/* 79 */     this.digitalDevices = digitalDevices;
/*    */   }
/*    */   
/*    */   public List<DeviceConfiguration> getAnalogOutputDevices() {
/* 83 */     return this.analogOutputDevices;
/*    */   }
/*    */   
/*    */   public void setAnalogOutputDevices(List<DeviceConfiguration> analogOutputDevices) {
/* 87 */     this.analogOutputDevices = analogOutputDevices;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\DeviceInterfaceModuleConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */