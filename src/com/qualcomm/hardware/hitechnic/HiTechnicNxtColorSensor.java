/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import android.graphics.Color;
/*     */ import com.qualcomm.robotcore.hardware.ColorSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
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
/*     */ public class HiTechnicNxtColorSensor
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements ColorSensor, I2cController.I2cPortReadyCallback
/*     */ {
/*  50 */   public static final I2cAddr ADDRESS_I2C = I2cAddr.create8bit(2);
/*     */   
/*     */   public static final int ADDRESS_COMMAND = 65;
/*     */   
/*     */   public static final int ADDRESS_COLOR_NUMBER = 66;
/*     */   
/*     */   public static final int OFFSET_COMMAND = 4;
/*     */   public static final int OFFSET_COLOR_NUMBER = 5;
/*     */   public static final int OFFSET_RED_READING = 6;
/*     */   public static final int OFFSET_GREEN_READING = 7;
/*     */   public static final int OFFSET_BLUE_READING = 8;
/*     */   public static final int BUFFER_LENGTH = 5;
/*     */   public static final int COMMAND_PASSIVE_LED = 1;
/*     */   public static final int COMMAND_ACTIVE_LED = 0;
/*     */   private byte[] readBuffer;
/*     */   private Lock readLock;
/*     */   private byte[] writeBuffer;
/*     */   private Lock writeLock;
/*     */   
/*     */   private static enum State
/*     */   {
/*  71 */     READING_ONLY,  PERFORMING_WRITE,  SWITCHING_TO_READ;
/*  72 */     private State() {} } private State state = State.READING_ONLY;
/*  73 */   private int lastCommand = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HiTechnicNxtColorSensor(I2cController module, int physicalPort)
/*     */   {
/*  80 */     super(module, physicalPort);
/*  81 */     finishConstruction();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/*  87 */     this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
/*  88 */     this.readLock = this.controller.getI2cReadCacheLock(this.physicalPort);
/*  89 */     this.writeBuffer = this.controller.getI2cWriteCache(this.physicalPort);
/*  90 */     this.writeLock = this.controller.getI2cWriteCacheLock(this.physicalPort);
/*     */     
/*  92 */     this.controller.enableI2cReadMode(this.physicalPort, ADDRESS_I2C, 65, 5);
/*  93 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/*  94 */     this.controller.writeI2cCacheToController(this.physicalPort);
/*     */     
/*  96 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 105 */     return String.format("argb: %d", new Object[] { Integer.valueOf(argb()) });
/*     */   }
/*     */   
/*     */   public int red()
/*     */   {
/* 110 */     return getColor(6);
/*     */   }
/*     */   
/*     */   public int green()
/*     */   {
/* 115 */     return getColor(7);
/*     */   }
/*     */   
/*     */   public int blue()
/*     */   {
/* 120 */     return getColor(8);
/*     */   }
/*     */   
/*     */   public int alpha()
/*     */   {
/* 125 */     return 0;
/*     */   }
/*     */   
/*     */   public int argb()
/*     */   {
/* 130 */     return Color.argb(alpha(), red(), green(), blue());
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void enableLed(boolean enable)
/*     */   {
/* 136 */     byte command = 1;
/* 137 */     if (enable) {
/* 138 */       command = 0;
/*     */     }
/*     */     
/* 141 */     if (this.lastCommand == command) {
/* 142 */       return;
/*     */     }
/*     */     
/* 145 */     this.lastCommand = command;
/* 146 */     this.state = State.PERFORMING_WRITE;
/*     */     try
/*     */     {
/* 149 */       this.writeLock.lock();
/* 150 */       this.writeBuffer[4] = command;
/*     */     } finally {
/* 152 */       this.writeLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setI2cAddress(I2cAddr newAddress)
/*     */   {
/* 158 */     throw new UnsupportedOperationException("setI2cAddress is not supported.");
/*     */   }
/*     */   
/*     */   public I2cAddr getI2cAddress()
/*     */   {
/* 163 */     return ADDRESS_I2C;
/*     */   }
/*     */   
/*     */   private int getColor(int OFFSET)
/*     */   {
/*     */     try {
/* 169 */       this.readLock.lock();
/* 170 */       color = this.readBuffer[OFFSET];
/*     */     } finally { byte color;
/* 172 */       this.readLock.unlock(); }
/*     */     byte color;
/* 174 */     return TypeConversion.unsignedByteToInt(color);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 182 */     return HardwareDevice.Manufacturer.HiTechnic;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 187 */     return "NXT Color Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 192 */     return this.controller.getConnectionInfo() + "; I2C port: " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 197 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void portIsReady(int port)
/*     */   {
/* 215 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 216 */     this.controller.readI2cCacheFromController(this.physicalPort);
/*     */     
/* 218 */     if (this.state == State.PERFORMING_WRITE) {
/* 219 */       this.controller.enableI2cWriteMode(this.physicalPort, ADDRESS_I2C, 65, 5);
/* 220 */       this.controller.writeI2cCacheToController(this.physicalPort);
/* 221 */       this.state = State.SWITCHING_TO_READ;
/* 222 */     } else if (this.state == State.SWITCHING_TO_READ) {
/* 223 */       this.controller.enableI2cReadMode(this.physicalPort, ADDRESS_I2C, 65, 5);
/* 224 */       this.controller.writeI2cCacheToController(this.physicalPort);
/* 225 */       this.state = State.READING_ONLY;
/*     */     } else {
/* 227 */       this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtColorSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */