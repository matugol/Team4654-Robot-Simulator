/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.graphics.Color;
/*     */ import com.qualcomm.robotcore.hardware.ColorSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
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
/*     */ public class ModernRoboticsI2cColorSensor
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements ColorSensor, I2cController.I2cPortReadyCallback
/*     */ {
/*  50 */   public static final I2cAddr DEFAULT_I2C_ADDRESS = I2cAddr.create8bit(60);
/*     */   
/*     */   public static final int ADDRESS_COMMAND = 3;
/*     */   
/*     */   public static final int ADDRESS_COLOR_NUMBER = 4;
/*     */   
/*     */   public static final int OFFSET_COMMAND = 4;
/*     */   
/*     */   public static final int OFFSET_COLOR_NUMBER = 5;
/*     */   
/*     */   public static final int OFFSET_RED_READING = 6;
/*     */   public static final int OFFSET_GREEN_READING = 7;
/*     */   public static final int OFFSET_BLUE_READING = 8;
/*     */   public static final int OFFSET_ALPHA_VALUE = 9;
/*     */   public static final int BUFFER_LENGTH = 6;
/*     */   public static final int COMMAND_PASSIVE_LED = 1;
/*     */   public static final int COMMAND_ACTIVE_LED = 0;
/*  67 */   private volatile I2cAddr i2cAddr = DEFAULT_I2C_ADDRESS;
/*     */   private byte[] readBuffer;
/*     */   private Lock readLock;
/*     */   private byte[] writeBuffer;
/*     */   private Lock writeLock;
/*     */   
/*  73 */   private static enum State { READING_ONLY,  PERFORMING_WRITE,  SWITCHING_TO_READ;
/*  74 */     private State() {} } private State state = State.READING_ONLY;
/*  75 */   private int lastCommand = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModernRoboticsI2cColorSensor(I2cController module, int physicalPort)
/*     */   {
/*  82 */     super(module, physicalPort);
/*  83 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/*  88 */     this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
/*  89 */     this.readLock = this.controller.getI2cReadCacheLock(this.physicalPort);
/*  90 */     this.writeBuffer = this.controller.getI2cWriteCache(this.physicalPort);
/*  91 */     this.writeLock = this.controller.getI2cWriteCacheLock(this.physicalPort);
/*     */     
/*  93 */     this.controller.enableI2cReadMode(this.physicalPort, this.i2cAddr, 3, 6);
/*  94 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/*  95 */     this.controller.writeI2cCacheToController(this.physicalPort);
/*     */     
/*  97 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 106 */     return String.format("argb: %d", new Object[] { Integer.valueOf(argb()) });
/*     */   }
/*     */   
/*     */   public int red()
/*     */   {
/* 111 */     return getColor(6);
/*     */   }
/*     */   
/*     */   public int green()
/*     */   {
/* 116 */     return getColor(7);
/*     */   }
/*     */   
/*     */   public int blue()
/*     */   {
/* 121 */     return getColor(8);
/*     */   }
/*     */   
/*     */   public int alpha()
/*     */   {
/* 126 */     return getColor(9);
/*     */   }
/*     */   
/*     */   public int argb()
/*     */   {
/* 131 */     return Color.argb(alpha(), red(), green(), blue());
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void enableLed(boolean enable)
/*     */   {
/* 137 */     byte command = 1;
/* 138 */     if (enable) {
/* 139 */       command = 0;
/*     */     }
/*     */     
/* 142 */     if (this.lastCommand == command) {
/* 143 */       return;
/*     */     }
/*     */     
/* 146 */     this.lastCommand = command;
/* 147 */     this.state = State.PERFORMING_WRITE;
/*     */     try
/*     */     {
/* 150 */       this.writeLock.lock();
/* 151 */       this.writeBuffer[4] = command;
/*     */     } finally {
/* 153 */       this.writeLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   private int getColor(int OFFSET)
/*     */   {
/*     */     try {
/* 160 */       this.readLock.lock();
/* 161 */       color = this.readBuffer[OFFSET];
/*     */     } finally { byte color;
/* 163 */       this.readLock.unlock(); }
/*     */     byte color;
/* 165 */     return TypeConversion.unsignedByteToInt(color);
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/* 169 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 174 */     return "Modern Robotics I2C Color Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 179 */     return this.controller.getConnectionInfo() + "; I2C port: " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 184 */     return 1;
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
/*     */   public synchronized void portIsReady(int port)
/*     */   {
/* 198 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 199 */     this.controller.readI2cCacheFromController(this.physicalPort);
/*     */     
/* 201 */     if (this.state == State.PERFORMING_WRITE) {
/* 202 */       this.controller.enableI2cWriteMode(this.physicalPort, this.i2cAddr, 3, 6);
/* 203 */       this.controller.writeI2cCacheToController(this.physicalPort);
/* 204 */       this.state = State.SWITCHING_TO_READ;
/* 205 */     } else if (this.state == State.SWITCHING_TO_READ) {
/* 206 */       this.controller.enableI2cReadMode(this.physicalPort, this.i2cAddr, 3, 6);
/* 207 */       this.controller.writeI2cCacheToController(this.physicalPort);
/* 208 */       this.state = State.READING_ONLY;
/*     */     } else {
/* 210 */       this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setI2cAddress(I2cAddr newAddress)
/*     */   {
/* 216 */     ModernRoboticsUsbDeviceInterfaceModule.throwIfModernRoboticsI2cAddressIsInvalid(newAddress);
/* 217 */     RobotLog.i(getDeviceName() + ", just changed I2C address. Original address: " + this.i2cAddr.get8Bit() + ", new address: " + newAddress.get8Bit());
/*     */     
/* 219 */     this.i2cAddr = newAddress;
/*     */     
/* 221 */     this.controller.enableI2cReadMode(this.physicalPort, this.i2cAddr, 3, 6);
/* 222 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 223 */     this.controller.writeI2cCacheToController(this.physicalPort);
/*     */     
/* 225 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */   public I2cAddr getI2cAddress()
/*     */   {
/* 230 */     return this.i2cAddr;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cColorSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */