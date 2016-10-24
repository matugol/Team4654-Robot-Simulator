/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.AnalogSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.LightSensor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HiTechnicNxtLightSensor
/*     */   extends LegacyModulePortDeviceImpl
/*     */   implements LightSensor, AnalogSensor
/*     */ {
/*     */   public static final byte LED_DIGITAL_LINE_NUMBER = 0;
/*     */   public static final double MIN_LIGHT_FRACTION = 0.11730205278592376D;
/*     */   public static final double MAX_LIGHT_FRACTION = 0.8504398826979472D;
/*     */   protected static final double apiLevelMin = 0.0D;
/*     */   protected static final double apiLevelMax = 1.0D;
/*     */   
/*     */   public HiTechnicNxtLightSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/*  58 */     super(legacyModule, physicalPort);
/*  59 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void moduleNowArmedOrPretending()
/*     */   {
/*  64 */     this.module.enableAnalogReadMode(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  73 */     return String.format("Light Level: %1.2f", new Object[] { Double.valueOf(getLightDetected()) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getLightDetected()
/*     */   {
/*  86 */     double max = getRawLightDetectedMax();
/*  87 */     return Range.clip(Range.scale(getRawLightDetected(), 0.11730205278592376D * max, 0.8504398826979472D * max, 0.0D, 1.0D), 0.0D, 1.0D);
/*     */   }
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
/*     */   public double getRawLightDetected()
/*     */   {
/* 106 */     double max = getRawLightDetectedMax();
/* 107 */     return Range.clip(max - readRawVoltage(), 0.0D, max);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getRawLightDetectedMax()
/*     */   {
/* 114 */     double sensorMaxVoltage = 5.0D;
/* 115 */     return Math.min(5.0D, this.module.getMaxAnalogInputVoltage());
/*     */   }
/*     */   
/*     */ 
/*     */   public double readRawVoltage()
/*     */   {
/* 121 */     return this.module.readAnalogVoltage(this.physicalPort);
/*     */   }
/*     */   
/*     */   public void enableLed(boolean enable)
/*     */   {
/* 126 */     this.module.setDigitalLine(this.physicalPort, 0, enable);
/*     */   }
/*     */   
/*     */   public String status()
/*     */   {
/* 131 */     return String.format("NXT Light Sensor, connected via device %s, port %d", new Object[] { this.module.getSerialNumber().toString(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 136 */     return HardwareDevice.Manufacturer.HiTechnic;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 141 */     return "NXT Light Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 146 */     return this.module.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 151 */     return 1;
/*     */   }
/*     */   
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtLightSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */