/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor.IrSeekerIndividualSensor;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor.Mode;
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
/*     */ public class HiTechnicNxtIrSeekerSensor
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements IrSeekerSensor, I2cController.I2cPortReadyCallback
/*     */ {
/*  51 */   public static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(16);
/*     */   
/*     */   public static final int MEM_MODE_ADDRESS = 65;
/*     */   
/*     */   public static final int MEM_DC_START_ADDRESS = 66;
/*     */   
/*     */   public static final int MEM_AC_START_ADDRESS = 73;
/*     */   
/*     */   public static final int MEM_READ_LENGTH = 6;
/*     */   
/*     */   public static final byte MODE_AC = 0;
/*     */   public static final byte MODE_DC = 2;
/*     */   public static final byte DIRECTION = 4;
/*     */   public static final byte SENSOR_FIRST = 5;
/*     */   public static final byte SENSOR_COUNT = 9;
/*     */   public static final double MAX_SENSOR_STRENGTH = 256.0D;
/*     */   public static final byte INVALID_ANGLE = 0;
/*     */   public static final byte MIN_ANGLE = 1;
/*     */   public static final byte MAX_ANGLE = 9;
/*  70 */   public static final double[] DIRECTION_TO_ANGLE = { 0.0D, -120.0D, -90.0D, -60.0D, -30.0D, 0.0D, 30.0D, 60.0D, 90.0D, 120.0D };
/*     */   
/*     */ 
/*     */   public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625D;
/*     */   
/*     */ 
/*     */   private byte[] readBuffer;
/*     */   
/*     */   private Lock readBufferLock;
/*     */   
/*     */   private byte[] writeBuffer;
/*     */   
/*     */   private Lock writeBufferLock;
/*     */   
/*     */   private IrSeekerSensor.Mode mode;
/*     */   
/*  86 */   private double signalDetectedThreshold = 0.00390625D;
/*     */   
/*     */ 
/*     */   private volatile boolean switchingModes;
/*     */   
/*     */ 
/*     */ 
/*     */   public HiTechnicNxtIrSeekerSensor(I2cController module, int physicalPort)
/*     */   {
/*  95 */     super(module, physicalPort);
/*     */     
/*     */ 
/*  98 */     this.mode = IrSeekerSensor.Mode.MODE_1200HZ;
/*     */     
/* 100 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/* 105 */     this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
/* 106 */     this.readBufferLock = this.controller.getI2cReadCacheLock(this.physicalPort);
/* 107 */     this.writeBuffer = this.controller.getI2cWriteCache(this.physicalPort);
/* 108 */     this.writeBufferLock = this.controller.getI2cWriteCacheLock(this.physicalPort);
/*     */     
/* 110 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */     
/* 112 */     this.switchingModes = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 121 */     if (signalDetected()) {
/* 122 */       return String.format("IR Seeker: %3.0f%% signal at %6.1f degrees", new Object[] { Double.valueOf(getStrength() * 100.0D), Double.valueOf(getAngle()) });
/*     */     }
/*     */     
/* 125 */     return "IR Seeker:  --% signal at  ---.- degrees";
/*     */   }
/*     */   
/*     */ 
/*     */   public void setSignalDetectedThreshold(double threshold)
/*     */   {
/* 131 */     this.signalDetectedThreshold = threshold;
/*     */   }
/*     */   
/*     */   public double getSignalDetectedThreshold()
/*     */   {
/* 136 */     return this.signalDetectedThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void setMode(IrSeekerSensor.Mode mode)
/*     */   {
/* 142 */     if (this.mode == mode) { return;
/*     */     }
/* 144 */     this.mode = mode;
/* 145 */     writeModeSwitch();
/*     */   }
/*     */   
/*     */   public IrSeekerSensor.Mode getMode()
/*     */   {
/* 150 */     return this.mode;
/*     */   }
/*     */   
/*     */   public boolean signalDetected()
/*     */   {
/* 155 */     if (this.switchingModes) { return false;
/*     */     }
/* 157 */     boolean detected = false;
/*     */     try
/*     */     {
/* 160 */       this.readBufferLock.lock();
/*     */       
/* 162 */       detected = this.readBuffer[4] != 0;
/*     */     } finally {
/* 164 */       this.readBufferLock.unlock();
/*     */     }
/*     */     
/* 167 */     detected = (detected) && (getStrength() > this.signalDetectedThreshold);
/*     */     
/* 169 */     return detected;
/*     */   }
/*     */   
/*     */   public double getAngle()
/*     */   {
/* 174 */     if (this.switchingModes) { return 0.0D;
/*     */     }
/* 176 */     double angle = 0.0D;
/*     */     try
/*     */     {
/* 179 */       this.readBufferLock.lock();
/* 180 */       if ((this.readBuffer[4] < 1) || (this.readBuffer[4] > 9)) {
/* 181 */         angle = 0.0D;
/*     */       } else {
/* 183 */         angle = DIRECTION_TO_ANGLE[this.readBuffer[4]];
/*     */       }
/*     */     } finally {
/* 186 */       this.readBufferLock.unlock();
/*     */     }
/*     */     
/* 189 */     return angle;
/*     */   }
/*     */   
/*     */   public double getStrength()
/*     */   {
/* 194 */     if (this.switchingModes) { return 0.0D;
/*     */     }
/* 196 */     double strength = 0.0D;
/*     */     try
/*     */     {
/* 199 */       this.readBufferLock.lock();
/* 200 */       for (int i = 0; i < 9; i++) {
/* 201 */         strength = Math.max(strength, getSensorStrength(this.readBuffer, i));
/*     */       }
/*     */     } finally {
/* 204 */       this.readBufferLock.unlock();
/*     */     }
/*     */     
/* 207 */     return strength;
/*     */   }
/*     */   
/*     */   public IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors()
/*     */   {
/* 212 */     IrSeekerSensor.IrSeekerIndividualSensor[] sensors = new IrSeekerSensor.IrSeekerIndividualSensor[9];
/* 213 */     if (this.switchingModes) return sensors;
/*     */     try
/*     */     {
/* 216 */       this.readBufferLock.lock();
/* 217 */       for (int i = 0; i < 9; i++) {
/* 218 */         double angle = DIRECTION_TO_ANGLE[(i * 2 + 1)];
/* 219 */         double strength = getSensorStrength(this.readBuffer, i);
/* 220 */         sensors[i] = new IrSeekerSensor.IrSeekerIndividualSensor(angle, strength);
/*     */       }
/*     */     } finally {
/* 223 */       this.readBufferLock.unlock();
/*     */     }
/*     */     
/* 226 */     return sensors;
/*     */   }
/*     */   
/*     */   public void setI2cAddress(I2cAddr newAddress)
/*     */   {
/* 231 */     throw new UnsupportedOperationException("This method is not supported.");
/*     */   }
/*     */   
/*     */   public I2cAddr getI2cAddress()
/*     */   {
/* 236 */     return I2C_ADDRESS;
/*     */   }
/*     */   
/*     */   private void writeModeSwitch() {
/* 240 */     this.switchingModes = true;
/* 241 */     byte modeAsByte = this.mode == IrSeekerSensor.Mode.MODE_600HZ ? 2 : 0;
/*     */     
/* 243 */     this.controller.enableI2cWriteMode(this.physicalPort, I2C_ADDRESS, 65, 1);
/*     */     try {
/* 245 */       this.writeBufferLock.lock();
/* 246 */       this.writeBuffer[4] = modeAsByte;
/*     */     } finally {
/* 248 */       this.writeBufferLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private double getSensorStrength(byte[] buffer, int sensor)
/*     */   {
/* 259 */     return TypeConversion.unsignedByteToDouble(buffer[(sensor + 5)]) / 256.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void portIsReady(int port)
/*     */   {
/* 268 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 269 */     this.controller.readI2cCacheFromController(this.physicalPort);
/*     */     
/* 271 */     if (this.switchingModes) {
/* 272 */       if (this.mode == IrSeekerSensor.Mode.MODE_600HZ) {
/* 273 */         this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 66, 6);
/*     */       } else {
/* 275 */         this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 73, 6);
/*     */       }
/* 277 */       this.controller.writeI2cCacheToController(this.physicalPort);
/* 278 */       this.switchingModes = false;
/*     */     } else {
/* 280 */       this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
/*     */     }
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/* 285 */     return HardwareDevice.Manufacturer.HiTechnic;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 290 */     return "NXT IR Seeker Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 295 */     return this.controller.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 300 */     return 2;
/*     */   }
/*     */   
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtIrSeekerSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */