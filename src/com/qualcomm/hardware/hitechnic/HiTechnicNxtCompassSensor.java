/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.CompassSensor;
/*     */ import com.qualcomm.robotcore.hardware.CompassSensor.CompassMode;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Arrays;
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
/*     */ public class HiTechnicNxtCompassSensor
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements CompassSensor, I2cController.I2cPortReadyCallback
/*     */ {
/*  50 */   public static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
/*     */   
/*     */   public static final byte MODE_CONTROL_ADDRESS = 65;
/*     */   
/*     */   public static final byte CALIBRATION = 67;
/*     */   
/*     */   public static final byte MEASUREMENT = 0;
/*     */   
/*     */   public static final byte HEADING_IN_TWO_DEGREE_INCREMENTS = 66;
/*     */   
/*     */   public static final byte ONE_DEGREE_HEADING_ADDER = 67;
/*     */   
/*     */   public static final byte CALIBRATION_FAILURE = 70;
/*     */   
/*     */   public static final byte DIRECTION_START = 7;
/*     */   
/*     */   public static final byte DIRECTION_END = 9;
/*     */   public static final double INVALID_DIRECTION = -1.0D;
/*     */   public static final int HEADING_WORD_LENGTH = 2;
/*     */   public static final int COMPASS_BUFFER = 65;
/*     */   public static final int COMPASS_BUFFER_SIZE = 5;
/*     */   private byte[] readBuffer;
/*     */   private Lock readBufferLock;
/*     */   private byte[] writeBuffer;
/*     */   private Lock writeBufferLock;
/*  75 */   private CompassSensor.CompassMode mode = CompassSensor.CompassMode.MEASUREMENT_MODE;
/*  76 */   private boolean switchingModes = false;
/*     */   
/*     */   private double direction;
/*  79 */   private boolean calibrationFailed = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HiTechnicNxtCompassSensor(I2cController module, int physicalPort)
/*     */   {
/*  86 */     super(module, physicalPort);
/*  87 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/*  92 */     this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 65, 5);
/*     */     
/*  94 */     this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
/*  95 */     this.readBufferLock = this.controller.getI2cReadCacheLock(this.physicalPort);
/*  96 */     this.writeBuffer = this.controller.getI2cWriteCache(this.physicalPort);
/*  97 */     this.writeBufferLock = this.controller.getI2cWriteCacheLock(this.physicalPort);
/*     */     
/*  99 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 108 */     return String.format("Compass: %3.1f", new Object[] { Double.valueOf(getDirection()) });
/*     */   }
/*     */   
/*     */   public synchronized double getDirection()
/*     */   {
/* 113 */     if (this.switchingModes) return -1.0D;
/* 114 */     if (this.mode == CompassSensor.CompassMode.CALIBRATION_MODE) { return -1.0D;
/*     */     }
/* 116 */     byte[] heading = null;
/*     */     try
/*     */     {
/* 119 */       this.readBufferLock.lock();
/* 120 */       heading = Arrays.copyOfRange(this.readBuffer, 7, 9);
/*     */     } finally {
/* 122 */       this.readBufferLock.unlock();
/*     */     }
/*     */     
/* 125 */     return TypeConversion.byteArrayToShort(heading, ByteOrder.LITTLE_ENDIAN);
/*     */   }
/*     */   
/*     */   public String status()
/*     */   {
/* 130 */     return String.format("NXT Compass Sensor, connected via device %s, port %d", new Object[] { this.controller.getSerialNumber().toString(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void setMode(CompassSensor.CompassMode mode)
/*     */   {
/* 136 */     if (this.mode == mode) { return;
/*     */     }
/* 138 */     this.mode = mode;
/* 139 */     writeModeSwitch();
/*     */   }
/*     */   
/*     */   private void writeModeSwitch() {
/* 143 */     this.switchingModes = true;
/* 144 */     byte modeAsByte = this.mode == CompassSensor.CompassMode.CALIBRATION_MODE ? 67 : 0;
/*     */     
/*     */ 
/*     */ 
/* 148 */     this.controller.enableI2cWriteMode(this.physicalPort, I2C_ADDRESS, 65, 1);
/*     */     try {
/* 150 */       this.writeBufferLock.lock();
/* 151 */       this.writeBuffer[3] = modeAsByte;
/*     */     } finally {
/* 153 */       this.writeBufferLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   private void readModeSwitch() {
/* 158 */     if (this.mode == CompassSensor.CompassMode.MEASUREMENT_MODE) {
/* 159 */       this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 65, 5);
/*     */     }
/*     */     
/* 162 */     this.switchingModes = false;
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
/*     */   public synchronized boolean calibrationFailed()
/*     */   {
/* 175 */     if ((this.mode == CompassSensor.CompassMode.CALIBRATION_MODE) || (this.switchingModes)) { return false;
/*     */     }
/* 177 */     boolean failed = false;
/*     */     try {
/* 179 */       this.readBufferLock.lock();
/* 180 */       failed = this.readBuffer[3] == 70;
/*     */     } finally {
/* 182 */       this.readBufferLock.unlock();
/*     */     }
/* 184 */     return failed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void portIsReady(int port)
/*     */   {
/* 192 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 193 */     this.controller.readI2cCacheFromController(this.physicalPort);
/*     */     
/* 195 */     if (this.switchingModes) {
/* 196 */       readModeSwitch();
/* 197 */       this.controller.writeI2cCacheToController(this.physicalPort);
/*     */     } else {
/* 199 */       this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
/*     */     }
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/* 204 */     return HardwareDevice.Manufacturer.HiTechnic;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 209 */     return "NXT Compass Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 214 */     return this.controller.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 219 */     return 1;
/*     */   }
/*     */   
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtCompassSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */