/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.AnalogInputController;
/*     */ import com.qualcomm.robotcore.hardware.AnalogSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModernRoboticsAnalogOpticalDistanceSensor
/*     */   implements OpticalDistanceSensor, AnalogSensor
/*     */ {
/*     */   private final AnalogInputController analogInputController;
/*     */   private final int physicalPort;
/*     */   protected static final double apiLevelMin = 0.0D;
/*     */   protected static final double apiLevelMax = 1.0D;
/*     */   
/*     */   public ModernRoboticsAnalogOpticalDistanceSensor(AnalogInputController analogInputController, int physicalPort)
/*     */   {
/*  50 */     this.analogInputController = analogInputController;
/*  51 */     this.physicalPort = physicalPort;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  56 */     return String.format("OpticalDistanceSensor: %1.2f", new Object[] { Double.valueOf(getLightDetected()) });
/*     */   }
/*     */   
/*     */   public double getLightDetected()
/*     */   {
/*  61 */     return Range.clip(Range.scale(getRawLightDetected(), 0.0D, getRawLightDetectedMax(), 0.0D, 1.0D), 0.0D, 1.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getMaxVoltage()
/*     */   {
/*  72 */     double sensorMaxVoltage = 5.0D;
/*  73 */     return Math.min(5.0D, this.analogInputController.getMaxAnalogInputVoltage());
/*     */   }
/*     */   
/*     */   public double getRawLightDetected()
/*     */   {
/*  78 */     return Range.clip(readRawVoltage(), 0.0D, getMaxVoltage());
/*     */   }
/*     */   
/*     */   public double getRawLightDetectedMax()
/*     */   {
/*  83 */     return getMaxVoltage();
/*     */   }
/*     */   
/*     */   public double readRawVoltage()
/*     */   {
/*  88 */     return this.analogInputController.getAnalogInputVoltage(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */   public void enableLed(boolean enable) {}
/*     */   
/*     */ 
/*     */   public String status()
/*     */   {
/*  97 */     return String.format("Optical Distance Sensor, connected via device %s, port %d", new Object[] { this.analogInputController.getSerialNumber().toString(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/* 101 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 106 */     return "Modern Robotics Analog Optical Distance Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 111 */     return this.analogInputController.getConnectionInfo() + "; analog port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 116 */     return 0;
/*     */   }
/*     */   
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsAnalogOpticalDistanceSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */