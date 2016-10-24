/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.AccelerationSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
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
/*     */ public class HiTechnicNxtAccelerationSensor
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements AccelerationSensor, I2cController.I2cPortReadyCallback
/*     */ {
/*  49 */   public static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
/*     */   
/*     */   public static final int ADDRESS_ACCEL_START = 66;
/*     */   
/*     */   public static final int ACCEL_LENGTH = 6;
/*     */   
/*     */   private static final double ONE_G = 200.0D;
/*     */   
/*     */   private static final double HIGH_BYTE_SCALING_VALUE = 4.0D;
/*     */   
/*     */   private static final int X_HIGH_BYTE = 4;
/*     */   
/*     */   private static final int Y_HIGH_BYTE = 5;
/*     */   
/*     */   private static final int Z_HIGH_BYTE = 6;
/*     */   
/*     */   private static final int X_LOW_BYTE = 7;
/*     */   
/*     */   private static final int Y_LOW_BYTE = 8;
/*     */   
/*     */   private static final int Z_LOW_BYTE = 9;
/*     */   private byte[] readBuffer;
/*     */   private Lock readBufferLock;
/*     */   
/*     */   public HiTechnicNxtAccelerationSensor(I2cController module, int physicalPort)
/*     */   {
/*  75 */     super(module, physicalPort);
/*  76 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/*  81 */     this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 66, 6);
/*     */     
/*  83 */     this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
/*  84 */     this.readBufferLock = this.controller.getI2cReadCacheLock(this.physicalPort);
/*     */     
/*  86 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  95 */     return getAcceleration().toString();
/*     */   }
/*     */   
/*     */   public Acceleration getAcceleration()
/*     */   {
/*     */     try
/*     */     {
/* 102 */       this.readBufferLock.lock();
/* 103 */       double gx = rawToG(this.readBuffer[4], this.readBuffer[7]);
/* 104 */       double gy = rawToG(this.readBuffer[5], this.readBuffer[8]);
/* 105 */       double gz = rawToG(this.readBuffer[6], this.readBuffer[9]);
/* 106 */       return Acceleration.fromGravity(gx, gy, gz, System.nanoTime());
/*     */     } finally {
/* 108 */       this.readBufferLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public String status()
/*     */   {
/* 114 */     return String.format("NXT Acceleration Sensor, connected via device %s, port %d", new Object[] { this.controller.getSerialNumber().toString(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */   private double rawToG(double high, double low)
/*     */   {
/* 119 */     return (high * 4.0D + low) / 200.0D;
/*     */   }
/*     */   
/*     */   public void portIsReady(int port)
/*     */   {
/* 124 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 125 */     this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
/* 126 */     this.controller.readI2cCacheFromController(this.physicalPort);
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/* 130 */     return HardwareDevice.Manufacturer.HiTechnic;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 135 */     return "NXT Acceleration Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 140 */     return this.controller.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 145 */     return 1;
/*     */   }
/*     */   
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtAccelerationSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */