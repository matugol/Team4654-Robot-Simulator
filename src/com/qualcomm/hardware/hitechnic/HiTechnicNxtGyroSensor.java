/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.AnalogSensor;
/*     */ import com.qualcomm.robotcore.hardware.GyroSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
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
/*     */ 
/*     */ 
/*     */ public class HiTechnicNxtGyroSensor
/*     */   extends LegacyModulePortDeviceImpl
/*     */   implements GyroSensor, AnalogSensor
/*     */ {
/*     */   public HiTechnicNxtGyroSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/*  48 */     super(legacyModule, physicalPort);
/*  49 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void moduleNowArmedOrPretending()
/*     */   {
/*  54 */     this.module.enableAnalogReadMode(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  63 */     return String.format("Gyro: %3.1f", new Object[] { Double.valueOf(getRotationFraction()) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void calibrate() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCalibrating()
/*     */   {
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   public double getRotationFraction()
/*     */   {
/*  88 */     return readRawVoltage() / getMaxVoltage();
/*     */   }
/*     */   
/*     */   public double readRawVoltage()
/*     */   {
/*  93 */     return this.module.readAnalogVoltage(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getMaxVoltage()
/*     */   {
/* 101 */     double sensorMaxVoltage = 5.0D;
/* 102 */     return Math.min(5.0D, this.module.getMaxAnalogInputVoltage());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHeading()
/*     */   {
/* 112 */     notSupported();
/* 113 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int rawX()
/*     */   {
/* 123 */     notSupported();
/* 124 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int rawY()
/*     */   {
/* 134 */     notSupported();
/* 135 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int rawZ()
/*     */   {
/* 145 */     notSupported();
/* 146 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetZAxisIntegrator() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String status()
/*     */   {
/* 161 */     return String.format("NXT Gyro Sensor, connected via device %s, port %d", new Object[] { this.module.getSerialNumber().toString(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 166 */     return HardwareDevice.Manufacturer.HiTechnic;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 171 */     return "NXT Gyro Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 176 */     return this.module.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 181 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */   protected void notSupported()
/*     */   {
/* 195 */     throw new UnsupportedOperationException("This method is not supported for " + getDeviceName());
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtGyroSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */