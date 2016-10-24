/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.UltrasonicSensor;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.util.concurrent.locks.Lock;
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
/*     */ public class HiTechnicNxtUltrasonicSensor
/*     */   extends LegacyModulePortDeviceImpl
/*     */   implements UltrasonicSensor, I2cController.I2cPortReadyCallback
/*     */ {
/*  51 */   public static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
/*     */   
/*     */ 
/*     */   public static final int ADDRESS_DISTANCE = 66;
/*     */   
/*     */ 
/*     */   public static final int MAX_PORT = 5;
/*     */   
/*     */   public static final int MIN_PORT = 4;
/*     */   
/*     */   Lock readLock;
/*     */   
/*     */   byte[] readBuffer;
/*     */   
/*     */ 
/*     */   public HiTechnicNxtUltrasonicSensor(ModernRoboticsUsbLegacyModule legacyModule, int physicalPort)
/*     */   {
/*  68 */     super(legacyModule, physicalPort);
/*  69 */     throwIfPortIsInvalid(physicalPort);
/*  70 */     finishConstruction();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void moduleNowArmedOrPretending()
/*     */   {
/*  76 */     this.readLock = this.module.getI2cReadCacheLock(this.physicalPort);
/*  77 */     this.readBuffer = this.module.getI2cReadCache(this.physicalPort);
/*     */     
/*  79 */     this.module.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 66, 1);
/*  80 */     this.module.enable9v(this.physicalPort, true);
/*  81 */     this.module.setI2cPortActionFlag(this.physicalPort);
/*  82 */     this.module.readI2cCacheFromController(this.physicalPort);
/*     */     
/*  84 */     this.module.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  93 */     return String.format("Ultrasonic: %6.1f", new Object[] { Double.valueOf(getUltrasonicLevel()) });
/*     */   }
/*     */   
/*     */ 
/*     */   public double getUltrasonicLevel()
/*     */   {
/*     */     try
/*     */     {
/* 101 */       this.readLock.lock();
/* 102 */       distance = this.readBuffer[4];
/*     */     } finally { byte distance;
/* 104 */       this.readLock.unlock(); }
/*     */     byte distance;
/* 106 */     return TypeConversion.unsignedByteToDouble(distance);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void portIsReady(int port)
/*     */   {
/* 115 */     this.module.setI2cPortActionFlag(this.physicalPort);
/* 116 */     this.module.writeI2cCacheToController(this.physicalPort);
/* 117 */     this.module.readI2cCacheFromController(this.physicalPort);
/*     */   }
/*     */   
/*     */   public String status()
/*     */   {
/* 122 */     return String.format("NXT Ultrasonic Sensor, connected via device %s, port %d", new Object[] { this.module.getSerialNumber().toString(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/* 126 */     return HardwareDevice.Manufacturer.Lego;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 131 */     return "NXT Ultrasonic Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 136 */     return this.module.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 141 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */   private void throwIfPortIsInvalid(int port)
/*     */   {
/* 154 */     if ((port < 4) || (port > 5)) {
/* 155 */       throw new IllegalArgumentException(String.format("Port %d is invalid for " + getDeviceName() + "; valid ports are %d or %d", new Object[] { Integer.valueOf(port), Integer.valueOf(4), Integer.valueOf(5) }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtUltrasonicSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */