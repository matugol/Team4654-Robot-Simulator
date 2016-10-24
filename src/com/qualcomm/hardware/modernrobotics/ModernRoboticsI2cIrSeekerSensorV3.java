/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor.IrSeekerIndividualSensor;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor.Mode;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.nio.ByteOrder;
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
/*     */ public class ModernRoboticsI2cIrSeekerSensorV3
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements IrSeekerSensor, I2cController.I2cPortReadyCallback
/*     */ {
/*  53 */   public static final I2cAddr DEFAULT_I2C_ADDRESS = I2cAddr.create8bit(56);
/*     */   
/*     */   public static final int ADDRESS_MEM_START = 4;
/*     */   
/*     */   public static final int MEM_LENGTH = 12;
/*     */   
/*     */   public static final int OFFSET_1200HZ_HEADING_DATA = 4;
/*     */   
/*     */   public static final int OFFSET_1200HZ_SIGNAL_STRENGTH = 5;
/*     */   
/*     */   public static final int OFFSET_600HZ_HEADING_DATA = 6;
/*     */   
/*     */   public static final int OFFSET_600HZ_SIGNAL_STRENGTH = 7;
/*     */   
/*     */   public static final int OFFSET_1200HZ_LEFT_SIDE_RAW_DATA = 8;
/*     */   
/*     */   public static final int OFFSET_1200HZ_RIGHT_SIDE_RAW_DATA = 10;
/*     */   
/*     */   public static final int OFFSET_600HZ_LEFT_SIDE_RAW_DATA = 12;
/*     */   
/*     */   public static final int OFFSET_600HZ_RIGHT_SIDE_RAW_DATA = 14;
/*     */   public static final byte SENSOR_COUNT = 2;
/*     */   public static final double MAX_SENSOR_STRENGTH = 256.0D;
/*     */   public static final byte INVALID_ANGLE = 0;
/*     */   public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625D;
/*  78 */   private volatile I2cAddr i2cAddr = DEFAULT_I2C_ADDRESS;
/*     */   private IrSeekerSensor.Mode mode;
/*     */   private byte[] readCacheBuffer;
/*     */   private Lock readCacheLock;
/*  82 */   private double signalDetectedThreshold = 0.00390625D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModernRoboticsI2cIrSeekerSensorV3(I2cController module, int physicalPort)
/*     */   {
/*  89 */     super(module, physicalPort);
/*     */     
/*  91 */     this.mode = IrSeekerSensor.Mode.MODE_1200HZ;
/*     */     
/*  93 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/*  98 */     this.readCacheBuffer = this.controller.getI2cReadCache(this.physicalPort);
/*  99 */     this.readCacheLock = this.controller.getI2cReadCacheLock(this.physicalPort);
/*     */     
/* 101 */     this.controller.enableI2cReadMode(this.physicalPort, this.i2cAddr, 4, 12);
/* 102 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 103 */     this.controller.writeI2cCacheToController(this.physicalPort);
/*     */     
/* 105 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 114 */     if (signalDetected()) {
/* 115 */       return String.format("IR Seeker: %3.0f%% signal at %6.1f degrees", new Object[] { Double.valueOf(getStrength() * 100.0D), Double.valueOf(getAngle()) });
/*     */     }
/*     */     
/* 118 */     return "IR Seeker:  --% signal at  ---.- degrees";
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void setSignalDetectedThreshold(double threshold)
/*     */   {
/* 124 */     this.signalDetectedThreshold = threshold;
/*     */   }
/*     */   
/*     */   public double getSignalDetectedThreshold()
/*     */   {
/* 129 */     return this.signalDetectedThreshold;
/*     */   }
/*     */   
/*     */   public synchronized void setMode(IrSeekerSensor.Mode mode)
/*     */   {
/* 134 */     this.mode = mode;
/*     */   }
/*     */   
/*     */   public IrSeekerSensor.Mode getMode()
/*     */   {
/* 139 */     return this.mode;
/*     */   }
/*     */   
/*     */   public boolean signalDetected()
/*     */   {
/* 144 */     return getStrength() > this.signalDetectedThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized double getAngle()
/*     */   {
/* 150 */     double angle = 0.0D;
/* 151 */     int headingOffset = this.mode == IrSeekerSensor.Mode.MODE_1200HZ ? 4 : 6;
/*     */     try
/*     */     {
/* 154 */       this.readCacheLock.lock();
/* 155 */       angle = this.readCacheBuffer[headingOffset];
/*     */     } finally {
/* 157 */       this.readCacheLock.unlock();
/*     */     }
/*     */     
/* 160 */     return angle;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized double getStrength()
/*     */   {
/* 166 */     double strength = 0.0D;
/* 167 */     int strengthOffset = this.mode == IrSeekerSensor.Mode.MODE_1200HZ ? 5 : 7;
/*     */     try
/*     */     {
/* 170 */       this.readCacheLock.lock();
/* 171 */       strength = TypeConversion.unsignedByteToDouble(this.readCacheBuffer[strengthOffset]) / 256.0D;
/*     */     } finally {
/* 173 */       this.readCacheLock.unlock();
/*     */     }
/*     */     
/* 176 */     return strength;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors()
/*     */   {
/* 182 */     IrSeekerSensor.IrSeekerIndividualSensor[] sensors = new IrSeekerSensor.IrSeekerIndividualSensor[2];
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 187 */       this.readCacheLock.lock();
/*     */       
/* 189 */       int leftSideRawOffset = this.mode == IrSeekerSensor.Mode.MODE_1200HZ ? 8 : 12;
/* 190 */       byte[] rawLeftValues = new byte[2];
/* 191 */       System.arraycopy(this.readCacheBuffer, leftSideRawOffset, rawLeftValues, 0, rawLeftValues.length);
/* 192 */       double strengthLeft = TypeConversion.byteArrayToShort(rawLeftValues, ByteOrder.LITTLE_ENDIAN) / 256.0D;
/* 193 */       sensors[0] = new IrSeekerSensor.IrSeekerIndividualSensor(-1.0D, strengthLeft);
/*     */       
/* 195 */       int rightSideRawOffset = this.mode == IrSeekerSensor.Mode.MODE_1200HZ ? 10 : 14;
/* 196 */       byte[] rawRightValues = new byte[2];
/* 197 */       System.arraycopy(this.readCacheBuffer, rightSideRawOffset, rawRightValues, 0, rawRightValues.length);
/* 198 */       double strengthRight = TypeConversion.byteArrayToShort(rawRightValues, ByteOrder.LITTLE_ENDIAN) / 256.0D;
/* 199 */       sensors[1] = new IrSeekerSensor.IrSeekerIndividualSensor(1.0D, strengthRight);
/*     */     } finally {
/* 201 */       this.readCacheLock.unlock();
/*     */     }
/*     */     
/* 204 */     return sensors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void portIsReady(int port)
/*     */   {
/* 212 */     this.controller.setI2cPortActionFlag(port);
/* 213 */     this.controller.readI2cCacheFromController(port);
/* 214 */     this.controller.writeI2cPortFlagOnlyToController(port);
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/* 218 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 223 */     return "Modern Robotics I2C IR Seeker Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 228 */     return this.controller.getConnectionInfo() + "; I2C port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 233 */     return 3;
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
/*     */   public synchronized void setI2cAddress(I2cAddr newAddress)
/*     */   {
/* 247 */     ModernRoboticsUsbDeviceInterfaceModule.throwIfModernRoboticsI2cAddressIsInvalid(newAddress);
/* 248 */     RobotLog.i(getDeviceName() + ", just changed the I2C address. Original address: " + this.i2cAddr + ", new address: " + newAddress);
/*     */     
/* 250 */     this.i2cAddr = newAddress;
/*     */     
/* 252 */     this.controller.enableI2cReadMode(this.physicalPort, this.i2cAddr, 4, 12);
/* 253 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 254 */     this.controller.writeI2cCacheToController(this.physicalPort);
/*     */     
/* 256 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */   public I2cAddr getI2cAddress()
/*     */   {
/* 261 */     return this.i2cAddr;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cIrSeekerSensorV3.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */