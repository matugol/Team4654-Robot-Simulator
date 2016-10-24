/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.GyroSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
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
/*     */ public class ModernRoboticsI2cGyro
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements GyroSensor, I2cController.I2cPortReadyCallback
/*     */ {
/*     */   protected static enum I2cTransactionState
/*     */   {
/*  53 */     QUEUED, 
/*  54 */     PENDING_I2C_READ, 
/*  55 */     PENDING_I2C_WRITE, 
/*  56 */     PENDING_READ_DONE, 
/*  57 */     DONE;
/*     */     
/*     */ 
/*     */     private I2cTransactionState() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public class GyroI2cTransaction
/*     */   {
/*     */     ModernRoboticsI2cGyro.I2cTransactionState state;
/*     */     byte[] buffer;
/*     */     byte offset;
/*     */     byte len;
/*     */     boolean write;
/*     */     boolean blocking;
/*     */     
/*     */     public GyroI2cTransaction(boolean blocking)
/*     */     {
/*  75 */       this.offset = 0;
/*  76 */       this.len = 18;
/*  77 */       this.write = false;
/*  78 */       this.blocking = blocking;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public GyroI2cTransaction(byte data)
/*     */     {
/*  86 */       this.offset = 3;
/*  87 */       this.buffer = new byte[1];
/*  88 */       this.buffer[0] = data;
/*  89 */       this.len = ((byte)this.buffer.length);
/*  90 */       this.write = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private GyroI2cTransaction(byte fsb, byte lsb)
/*     */     {
/*  99 */       this.offset = 16;
/* 100 */       this.buffer = new byte[2];
/* 101 */       this.buffer[0] = fsb;
/* 102 */       this.buffer[1] = lsb;
/* 103 */       this.len = ((byte)this.buffer.length);
/* 104 */       this.write = true;
/*     */     }
/*     */     
/*     */     public boolean isEqual(GyroI2cTransaction transaction)
/*     */     {
/* 109 */       if (this.offset != transaction.offset) {
/* 110 */         return false;
/*     */       }
/* 112 */       switch (this.offset) {
/*     */       case 3: 
/*     */       case 16: 
/* 115 */         if (Arrays.equals(this.buffer, transaction.buffer)) {
/* 116 */           return true;
/*     */         }
/*     */         break;
/*     */       default: 
/* 120 */         return false;
/*     */       }
/*     */       
/* 123 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */   public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(32);
/*     */   
/*     */   protected static final int OFFSET_FIRMWARE_REV = 0;
/*     */   
/*     */   protected static final int OFFSET_MANUFACTURE_CODE = 1;
/*     */   
/*     */   protected static final int OFFSET_SENSOR_ID = 2;
/*     */   
/*     */   protected static final int OFFSET_COMMAND = 3;
/*     */   
/*     */   protected static final int OFFSET_HEADING_DATA = 4;
/*     */   
/*     */   protected static final int OFFSET_INTEGRATED_Z_VAL = 6;
/*     */   
/*     */   protected static final int OFFSET_RAW_X_VAL = 8;
/*     */   
/*     */   protected static final int OFFSET_RAW_Y_VAL = 10;
/*     */   
/*     */   protected static final int OFFSET_RAW_Z_VAL = 12;
/*     */   
/*     */   protected static final int OFFSET_Z_AXIS_OFFSET = 14;
/*     */   
/*     */   protected static final int OFFSET_Z_AXIS_SCALE_COEF = 16;
/*     */   protected static final int OFFSET_NEW_I2C_ADDRESS = 112;
/*     */   protected static final int OFFSET_TRIGGER_1 = 113;
/*     */   protected static final int OFFSET_TRIGGER_2 = 114;
/*     */   protected static final int TRIGGER_1_VAL = 85;
/*     */   protected static final int TRIGGER_2_VAL = 170;
/*     */   protected static final byte COMMAND_NORMAL = 0;
/*     */   protected static final byte COMMAND_NULL = 78;
/*     */   protected static final byte COMMAND_RESET_Z_AXIS = 82;
/*     */   protected static final byte COMMAND_WRITE_EEPROM = 87;
/*     */   protected static final int BUFFER_LENGTH = 18;
/*     */   protected ConcurrentLinkedQueue<GyroI2cTransaction> transactionQueue;
/*     */   private I2cAddr i2cAddress;
/*     */   private byte[] readBuffer;
/*     */   private Lock readLock;
/*     */   private byte[] writeBuffer;
/*     */   private Lock writeLock;
/*     */   private static final boolean debug = false;
/* 171 */   private volatile boolean waitingForGodot = false;
/*     */   private HeadingMode headingMode;
/*     */   private MeasurementMode measurementMode;
/*     */   private GyroMemoryMap memoryMap;
/*     */   
/*     */   public static enum HeadingMode {
/* 177 */     HEADING_CARTESIAN, 
/* 178 */     HEADING_CARDINAL;
/*     */     
/*     */     private HeadingMode() {} }
/*     */   
/* 182 */   public static enum MeasurementMode { GYRO_CALIBRATION_PENDING, 
/* 183 */     GYRO_CALIBRATING, 
/* 184 */     GYRO_NORMAL;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private MeasurementMode() {}
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
/*     */   public ModernRoboticsI2cGyro(I2cController module, int physicalPort, I2cAddr i2cAddress)
/*     */   {
/* 211 */     super(module, physicalPort);
/*     */     
/* 213 */     this.headingMode = HeadingMode.HEADING_CARDINAL;
/* 214 */     this.i2cAddress = i2cAddress;
/*     */     
/* 216 */     this.transactionQueue = new ConcurrentLinkedQueue();
/* 217 */     this.memoryMap = new GyroMemoryMap(null);
/*     */     
/* 219 */     this.measurementMode = MeasurementMode.GYRO_NORMAL;
/*     */     
/* 221 */     finishConstruction();
/*     */   }
/*     */   
/*     */   public ModernRoboticsI2cGyro(I2cController module, int physicalPort)
/*     */   {
/* 226 */     this(module, physicalPort, ADDRESS_I2C_DEFAULT);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/* 232 */     this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
/* 233 */     this.readLock = this.controller.getI2cReadCacheLock(this.physicalPort);
/* 234 */     this.writeBuffer = this.controller.getI2cWriteCache(this.physicalPort);
/* 235 */     this.writeLock = this.controller.getI2cWriteCacheLock(this.physicalPort);
/*     */     
/* 237 */     this.controller.enableI2cReadMode(this.physicalPort, this.i2cAddress, 0, 18);
/* 238 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/* 239 */     this.controller.writeI2cCacheToController(this.physicalPort);
/*     */     
/* 241 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setI2cAddress(I2cAddr newAddress)
/*     */   {
/* 249 */     ModernRoboticsUsbDeviceInterfaceModule.throwIfModernRoboticsI2cAddressIsInvalid(newAddress);
/* 250 */     RobotLog.i(getDeviceName() + ", just changed I2C address. Original address: " + this.i2cAddress + ", new address: " + newAddress);
/*     */     
/* 252 */     this.i2cAddress = newAddress;
/*     */   }
/*     */   
/*     */   public I2cAddr getI2cAddress() {
/* 256 */     return this.i2cAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean queueTransaction(GyroI2cTransaction transaction, boolean force)
/*     */   {
/* 267 */     if (!force) {
/* 268 */       Iterator<GyroI2cTransaction> it = this.transactionQueue.iterator();
/* 269 */       while (it.hasNext()) {
/* 270 */         GyroI2cTransaction t = (GyroI2cTransaction)it.next();
/* 271 */         if (t.isEqual(transaction)) {
/* 272 */           buginf("NO Queue transaction " + transaction.toString());
/* 273 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 286 */     buginf("YES Queue transaction " + transaction.toString());
/* 287 */     this.transactionQueue.add(transaction);
/* 288 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean queueTransaction(GyroI2cTransaction transaction)
/*     */   {
/* 293 */     return queueTransaction(transaction, false);
/*     */   }
/*     */   
/*     */   public void calibrate()
/*     */   {
/* 298 */     GyroI2cTransaction transaction = new GyroI2cTransaction((byte)78);
/*     */     
/* 300 */     queueTransaction(transaction);
/*     */     
/* 302 */     this.measurementMode = MeasurementMode.GYRO_CALIBRATION_PENDING;
/*     */   }
/*     */   
/*     */   public boolean isCalibrating()
/*     */   {
/* 307 */     if (this.measurementMode == MeasurementMode.GYRO_NORMAL) {
/* 308 */       return false;
/*     */     }
/* 310 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HeadingMode getHeadingMode()
/*     */   {
/* 320 */     return this.headingMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeadingMode(HeadingMode headingMode)
/*     */   {
/* 329 */     this.headingMode = headingMode;
/*     */   }
/*     */   
/*     */   public MeasurementMode getMeasurementMode()
/*     */   {
/* 334 */     return this.measurementMode;
/*     */   }
/*     */   
/*     */   public int getHeading()
/*     */   {
/* 339 */     if (this.headingMode == HeadingMode.HEADING_CARDINAL) {
/* 340 */       if (this.memoryMap.heading == 0) {
/* 341 */         return this.memoryMap.heading;
/*     */       }
/* 343 */       return Math.abs(this.memoryMap.heading - 360);
/*     */     }
/*     */     
/* 346 */     return this.memoryMap.heading;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getRotationFraction()
/*     */   {
/* 358 */     notSupported();
/* 359 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public int getIntegratedZValue()
/*     */   {
/* 364 */     return this.memoryMap.integratedZ;
/*     */   }
/*     */   
/*     */   public int rawX()
/*     */   {
/* 369 */     return this.memoryMap.rawX;
/*     */   }
/*     */   
/*     */   public int rawY()
/*     */   {
/* 374 */     return this.memoryMap.rawY;
/*     */   }
/*     */   
/*     */   public int rawZ()
/*     */   {
/* 379 */     return this.memoryMap.rawZ;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetZAxisIntegrator()
/*     */   {
/* 386 */     GyroI2cTransaction transaction = new GyroI2cTransaction((byte)82);
/* 387 */     queueTransaction(transaction);
/* 388 */     transaction = new GyroI2cTransaction(true);
/* 389 */     queueTransaction(transaction);
/* 390 */     if (queueTransaction(transaction)) {
/* 391 */       waitOnRead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setZAxisScalingCoefficient(double coefficient)
/*     */   {
/* 404 */     byte fsb = (byte)(int)coefficient;
/* 405 */     byte lsb = (byte)(int)Math.abs((fsb - coefficient) * 100.0D);
/*     */     
/* 407 */     GyroI2cTransaction transaction = new GyroI2cTransaction(fsb, lsb, null);
/*     */     
/* 409 */     queueTransaction(transaction);
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 414 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 420 */     return "Modern Robotics Gyro";
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 426 */     return this.controller.getConnectionInfo() + "; I2C port: " + this.physicalPort;
/*     */   }
/*     */   
/*     */ 
/*     */   public String status()
/*     */   {
/* 432 */     return String.format("Modern Robotics Gyro, connected via device %s, port %d", new Object[] { this.controller.getSerialNumber().toString(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getVersion()
/*     */   {
/* 439 */     return 1;
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
/*     */ 
/*     */   private void populateGyroMemoryMap(boolean blocking)
/*     */   {
/*     */     try
/*     */     {
/* 456 */       this.readLock.lock();
/* 457 */       ByteBuffer buf = ByteBuffer.wrap(this.readBuffer);
/* 458 */       buf.order(ByteOrder.LITTLE_ENDIAN);
/* 459 */       this.memoryMap.firmwareRev = this.readBuffer[4];
/* 460 */       this.memoryMap.manfacturerCode = this.readBuffer[5];
/* 461 */       this.memoryMap.sensorId = this.readBuffer[6];
/* 462 */       this.memoryMap.command = this.readBuffer[7];
/* 463 */       this.memoryMap.heading = buf.getShort(8);
/* 464 */       this.memoryMap.integratedZ = buf.getShort(10);
/* 465 */       this.memoryMap.rawX = buf.getShort(12);
/* 466 */       this.memoryMap.rawY = buf.getShort(14);
/* 467 */       this.memoryMap.rawZ = buf.getShort(16);
/* 468 */       this.memoryMap.ZOffset = buf.getShort(18);
/* 469 */       this.memoryMap.ZScalingCoef = buf.getShort(20);
/*     */     } finally {
/* 471 */       this.readLock.unlock();
/*     */     }
/* 473 */     if (blocking) {
/* 474 */       synchronized (this) {
/* 475 */         if (this.waitingForGodot) {
/* 476 */           this.waitingForGodot = false;
/* 477 */           notify();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void doRead()
/*     */   {
/* 487 */     GyroI2cTransaction transaction = new GyroI2cTransaction(false);
/* 488 */     queueTransaction(transaction);
/*     */   }
/*     */   
/*     */ 
/*     */   public void portIsReady(int port)
/*     */   {
/* 494 */     if (this.transactionQueue.isEmpty()) {
/* 495 */       doRead();
/* 496 */       return;
/*     */     }
/*     */     
/* 499 */     GyroI2cTransaction transaction = (GyroI2cTransaction)this.transactionQueue.peek();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 509 */     if (transaction.state == I2cTransactionState.PENDING_I2C_READ)
/*     */     {
/*     */ 
/*     */ 
/* 513 */       this.controller.readI2cCacheFromModule(this.physicalPort);
/* 514 */       transaction.state = I2cTransactionState.PENDING_READ_DONE;
/* 515 */       return; }
/* 516 */     if (transaction.state == I2cTransactionState.PENDING_I2C_WRITE)
/*     */     {
/*     */ 
/*     */ 
/* 520 */       transaction = (GyroI2cTransaction)this.transactionQueue.poll();
/*     */       
/*     */ 
/*     */ 
/* 524 */       if (this.transactionQueue.isEmpty()) {
/* 525 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 530 */       transaction = (GyroI2cTransaction)this.transactionQueue.peek();
/* 531 */     } else if (transaction.state == I2cTransactionState.PENDING_READ_DONE)
/*     */     {
/*     */ 
/*     */ 
/* 535 */       populateGyroMemoryMap(transaction.blocking);
/*     */       
/* 537 */       transaction = (GyroI2cTransaction)this.transactionQueue.poll();
/* 538 */       if (this.transactionQueue.isEmpty()) {
/* 539 */         return;
/*     */       }
/* 541 */       transaction = (GyroI2cTransaction)this.transactionQueue.peek();
/*     */     }
/*     */     try
/*     */     {
/* 545 */       if (transaction.write) {
/* 546 */         if (transaction.offset == 3) {
/* 547 */           this.memoryMap.command = transaction.buffer[0];
/* 548 */           this.measurementMode = MeasurementMode.GYRO_CALIBRATING;
/*     */         }
/* 550 */         this.controller.enableI2cWriteMode(port, this.i2cAddress, transaction.offset, transaction.len);
/* 551 */         this.controller.copyBufferIntoWriteBuffer(port, transaction.buffer);
/* 552 */         transaction.state = I2cTransactionState.PENDING_I2C_WRITE;
/*     */       } else {
/* 554 */         this.controller.enableI2cReadMode(port, this.i2cAddress, transaction.offset, transaction.len);
/* 555 */         transaction.state = I2cTransactionState.PENDING_I2C_READ;
/*     */       }
/* 557 */       this.controller.writeI2cCacheToController(port);
/*     */     } catch (IllegalArgumentException e) {
/* 559 */       RobotLog.e(e.getMessage());
/*     */     }
/*     */     
/* 562 */     if (this.memoryMap.command == 0) {
/* 563 */       this.measurementMode = MeasurementMode.GYRO_NORMAL;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void buginf(String s) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void notSupported()
/*     */   {
/* 579 */     throw new UnsupportedOperationException("This method is not supported for " + getDeviceName());
/*     */   }
/*     */   
/*     */   private void waitOnRead()
/*     */   {
/* 584 */     synchronized (this) {
/* 585 */       this.waitingForGodot = true;
/*     */       try {
/* 587 */         while (this.waitingForGodot) {
/* 588 */           wait(0L);
/*     */         }
/*     */       } catch (InterruptedException e) {
/* 591 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class GyroMemoryMap
/*     */   {
/*     */     byte firmwareRev;
/*     */     byte manfacturerCode;
/*     */     byte sensorId;
/*     */     byte command;
/*     */     short heading;
/*     */     short integratedZ;
/*     */     short rawX;
/*     */     short rawY;
/*     */     short rawZ;
/*     */     short ZOffset;
/*     */     short ZScalingCoef;
/*     */     
/*     */     private GyroMemoryMap() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cGyro.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */