/*    */ package com.qualcomm.hardware.modernrobotics;
/*    */ 
/*    */ import com.qualcomm.robotcore.hardware.DigitalChannelController;
/*    */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*    */ import com.qualcomm.robotcore.hardware.TouchSensor;
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
/*    */ 
/*    */ 
/*    */ public class ModernRoboticsDigitalTouchSensor
/*    */   implements TouchSensor
/*    */ {
/* 42 */   private DigitalChannelController module = null;
/* 43 */   private int physicalPort = -1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ModernRoboticsDigitalTouchSensor(DigitalChannelController module, int physicalPort)
/*    */   {
/* 52 */     this.module = module;
/* 53 */     this.physicalPort = physicalPort;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 58 */     return String.format("Touch Sensor: %1.2f", new Object[] { Double.valueOf(getValue()) });
/*    */   }
/*    */   
/*    */   public double getValue()
/*    */   {
/* 63 */     return isPressed() ? 1.0D : 0.0D;
/*    */   }
/*    */   
/*    */   public boolean isPressed()
/*    */   {
/* 68 */     return this.module.getDigitalChannelState(this.physicalPort);
/*    */   }
/*    */   
/*    */   public HardwareDevice.Manufacturer getManufacturer() {
/* 72 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*    */   }
/*    */   
/*    */   public String getDeviceName()
/*    */   {
/* 77 */     return "Modern Robotics Digital Touch Sensor";
/*    */   }
/*    */   
/*    */   public String getConnectionInfo()
/*    */   {
/* 82 */     return this.module.getConnectionInfo() + "; digital port " + this.physicalPort;
/*    */   }
/*    */   
/*    */   public int getVersion()
/*    */   {
/* 87 */     return 1;
/*    */   }
/*    */   
/*    */   public void resetDeviceConfigurationForOpMode() {}
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsDigitalTouchSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */