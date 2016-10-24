/*     */ package com.qualcomm.hardware.adafruit;
/*     */ 
/*     */ import android.graphics.Color;
/*     */ import com.qualcomm.robotcore.hardware.ColorSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class AdafruitI2cColorSensor
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements ColorSensor, I2cController.I2cPortReadyCallback
/*     */ {
/*     */   public static final int I2C_ADDRESS_TCS34725 = 41;
/*     */   public static final int TCS34725_COMMAND_BIT = 128;
/*     */   public static final int TCS34725_ID = 18;
/*     */   public static final int ADDRESS_TCS34725_ENABLE = 0;
/*     */   public static final int TCS34725_ENABLE_AIEN = 16;
/*     */   public static final int TCS34725_ENABLE_AEN = 2;
/*     */   public static final int TCS34725_ENABLE_PON = 1;
/*     */   public static final int TCS34725_CDATAL = 20;
/*     */   public static final int TCS34725_RDATAL = 22;
/*     */   public static final int TCS34725_GDATAL = 24;
/*     */   public static final int TCS34725_BDATAL = 26;
/*     */   public static final int OFFSET_ALPHA_LOW_BYTE = 4;
/*     */   public static final int OFFSET_ALPHA_HIGH_BYTE = 5;
/*     */   public static final int OFFSET_RED_LOW_BYTE = 6;
/*     */   public static final int OFFSET_RED_HIGH_BYTE = 7;
/*     */   public static final int OFFSET_GREEN_LOW_BYTE = 8;
/*     */   public static final int OFFSET_GREEN_HIGH_BYTE = 9;
/*     */   public static final int OFFSET_BLUE_LOW_BYTE = 10;
/*     */   public static final int OFFSET_BLUE_HIGH_BYTE = 11;
/*     */   private byte[] readBuffer;
/*     */   private Lock readLock;
/*     */   private byte[] writeBuffer;
/*     */   private Lock writeLock;
/*  81 */   private boolean enableWrite = false;
/*  82 */   private boolean enableRead = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AdafruitI2cColorSensor(I2cController module, int physicalPort)
/*     */   {
/*  89 */     super(module, physicalPort);
/*     */     
/*  91 */     this.enableWrite = true;
/*     */     
/*  93 */     finishConstruction();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/*  99 */     this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
/* 100 */     this.readLock = this.controller.getI2cReadCacheLock(this.physicalPort);
/* 101 */     this.writeBuffer = this.controller.getI2cWriteCache(this.physicalPort);
/* 102 */     this.writeLock = this.controller.getI2cWriteCacheLock(this.physicalPort);
/*     */     
/* 104 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 113 */     return String.format("argb: %d", new Object[] { Integer.valueOf(argb()) });
/*     */   }
/*     */   
/*     */   public int red()
/*     */   {
/* 118 */     return getColor(7, 6);
/*     */   }
/*     */   
/*     */   public int green()
/*     */   {
/* 123 */     return getColor(9, 8);
/*     */   }
/*     */   
/*     */   public int blue()
/*     */   {
/* 128 */     return getColor(11, 10);
/*     */   }
/*     */   
/*     */   public int alpha()
/*     */   {
/* 133 */     return getColor(5, 4);
/*     */   }
/*     */   
/*     */   private int getColor(int HIGH_OFFSET, int LOW_OFFSET)
/*     */   {
/*     */     try {
/* 139 */       this.readLock.lock();
/* 140 */       color = this.readBuffer[HIGH_OFFSET] << 8 | this.readBuffer[LOW_OFFSET] & 0xFF;
/*     */     } finally { int color;
/* 142 */       this.readLock.unlock(); }
/*     */     int color;
/* 144 */     return color;
/*     */   }
/*     */   
/*     */   public int argb()
/*     */   {
/* 149 */     return Color.argb(alpha(), red(), green(), blue());
/*     */   }
/*     */   
/*     */   public void enableLed(boolean enable)
/*     */   {
/* 154 */     throw new UnsupportedOperationException("enableLed is not implemented.");
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 159 */     return HardwareDevice.Manufacturer.Adafruit;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 164 */     return "Adafruit I2C Color Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 169 */     return this.controller.getConnectionInfo() + "; I2C port: " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 174 */     return 1;
/*     */   }
/*     */   
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
/*     */ 
/*     */   public void portIsReady(int port)
/*     */   {
/* 190 */     if (this.enableWrite) {
/* 191 */       enableWrite();
/* 192 */       this.enableWrite = false;
/* 193 */       this.enableRead = true;
/* 194 */     } else if (this.enableRead) {
/* 195 */       enableRead();
/* 196 */       this.enableRead = false;
/*     */     }
/* 198 */     this.controller.readI2cCacheFromController(this.physicalPort);
/* 199 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 200 */     this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
/*     */   }
/*     */   
/*     */   private void enableRead()
/*     */   {
/* 205 */     this.controller.enableI2cReadMode(this.physicalPort, I2cAddr.create7bit(41), 148, 8);
/* 206 */     this.controller.writeI2cCacheToController(this.physicalPort);
/*     */   }
/*     */   
/*     */   private void enableWrite()
/*     */   {
/* 211 */     this.controller.enableI2cWriteMode(this.physicalPort, I2cAddr.create7bit(41), 128, 1);
/*     */     try {
/* 213 */       this.writeLock.lock();
/*     */       
/* 215 */       this.writeBuffer[4] = 3;
/*     */     } finally {
/* 217 */       this.writeLock.unlock();
/*     */     }
/* 219 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 220 */     this.controller.writeI2cCacheToController(this.physicalPort);
/*     */   }
/*     */   
/*     */   public void setI2cAddress(I2cAddr newAddress)
/*     */   {
/* 225 */     throw new UnsupportedOperationException("setI2cAddress is not supported.");
/*     */   }
/*     */   
/*     */   public I2cAddr getI2cAddress()
/*     */   {
/* 230 */     return I2cAddr.create7bit(41);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\adafruit\AdafruitI2cColorSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */