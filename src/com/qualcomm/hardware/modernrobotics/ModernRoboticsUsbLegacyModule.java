/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.ArmableUsbDevice.OpenRobotUsbDevice;
/*     */ import com.qualcomm.hardware.R.string;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.FirmwareVersion;
/*     */ import com.qualcomm.robotcore.util.Range;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModernRoboticsUsbLegacyModule
/*     */   extends ModernRoboticsUsbI2cController
/*     */   implements LegacyModule
/*     */ {
/*     */   public static final double MAX_ANALOG_INPUT_VOLTAGE = 5.0D;
/*     */   public static final boolean DEBUG_LOGGING = false;
/*     */   public static final int MONITOR_LENGTH = 13;
/*     */   public static final byte START_ADDRESS = 3;
/*     */   public static final byte MIN_PORT_NUMBER = 0;
/*     */   public static final byte MAX_PORT_NUMBER = 5;
/*     */   public static final byte NUMBER_OF_PORTS = 6;
/*     */   public static final byte I2C_ACTION_FLAG = -1;
/*     */   public static final byte I2C_NO_ACTION_FLAG = 0;
/*     */   public static final byte SIZE_ANALOG_BUFFER = 2;
/*     */   public static final byte SIZE_I2C_BUFFER = 27;
/*     */   public static final byte SIZE_OF_PORT_BUFFER = 32;
/*     */   public static final byte NXT_MODE_ANALOG = 0;
/*     */   public static final byte NXT_MODE_I2C = 1;
/*     */   public static final byte NXT_MODE_9V_ENABLED = 2;
/*     */   public static final byte NXT_MODE_DIGITAL_0 = 4;
/*     */   public static final byte NXT_MODE_DIGITAL_1 = 8;
/*     */   public static final byte NXT_MODE_READ = -128;
/*     */   public static final byte NXT_MODE_WRITE = 0;
/*     */   public static final byte BUFFER_FLAG_S0 = 1;
/*     */   public static final byte BUFFER_FLAG_S1 = 2;
/*     */   public static final byte BUFFER_FLAG_S2 = 4;
/*     */   public static final byte BUFFER_FLAG_S3 = 8;
/*     */   public static final byte BUFFER_FLAG_S4 = 16;
/*     */   public static final byte BUFFER_FLAG_S5 = 32;
/*     */   public static final int ADDRESS_BUFFER_STATUS = 3;
/*     */   public static final int ADDRESS_ANALOG_PORT_S0 = 4;
/*     */   public static final int ADDRESS_ANALOG_PORT_S1 = 6;
/*     */   public static final int ADDRESS_ANALOG_PORT_S2 = 8;
/*     */   public static final int ADDRESS_ANALOG_PORT_S3 = 10;
/*     */   public static final int ADDRESS_ANALOG_PORT_S4 = 12;
/*     */   public static final int ADDRESS_ANALOG_PORT_S5 = 14;
/*     */   public static final int ADDRESS_I2C_PORT_SO = 16;
/*     */   public static final int ADDRESS_I2C_PORT_S1 = 48;
/*     */   public static final int ADDRESS_I2C_PORT_S2 = 80;
/*     */   public static final int ADDRESS_I2C_PORT_S3 = 112;
/*     */   public static final int ADDRESS_I2C_PORT_S4 = 144;
/*     */   public static final int ADDRESS_I2C_PORT_S5 = 176;
/*     */   public static final byte OFFSET_I2C_PORT_MODE = 0;
/*     */   public static final byte OFFSET_I2C_PORT_I2C_ADDRESS = 1;
/*     */   public static final byte OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
/*     */   public static final byte OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
/*     */   public static final byte OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
/*     */   public static final byte OFFSET_I2C_PORT_FLAG = 31;
/* 132 */   public static final int[] ADDRESS_ANALOG_PORT_MAP = { 4, 6, 8, 10, 12, 14 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */   public static final int[] ADDRESS_I2C_PORT_MAP = { 16, 48, 80, 112, 144, 176 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */   public static final int[] BUFFER_FLAG_MAP = { 1, 2, 4, 8, 16, 32 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 168 */   public static final int[] DIGITAL_LINE = { 4, 8 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 176 */   public static final int[] PORT_9V_CAPABLE = { 4, 5 };
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int SEGMENT_OFFSET_PORT_FLAG_ONLY = 6;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final double MIN_I2C_WRITE_RATE = 2.0D;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int LEGACY_MODULE_FORCE_IO_DELAY = 15;
/*     */   
/*     */ 
/*     */ 
/* 192 */   private final ReadWriteRunnableSegment[] segments = new ReadWriteRunnableSegment[12];
/*     */   
/*     */ 
/* 195 */   private final I2cController.I2cPortReadyCallback[] portReadyCallback = new I2cController.I2cPortReadyCallback[6];
/* 196 */   protected final byte[] lastI2cPortModes = new byte[6];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModernRoboticsUsbLegacyModule(Context context, final SerialNumber serialNumber, ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice, EventLoopManager manager)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 208 */     super(6, context, serialNumber, manager, openRobotUsbDevice, new ModernRoboticsUsbDevice.CreateReadWriteRunnable() {
/*     */       public ReadWriteRunnable create(RobotUsbDevice device) {
/* 210 */         return new ReadWriteRunnableStandard(ModernRoboticsUsbLegacyModule.this, serialNumber, device, 13, 3, false);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   protected void doArm() throws RobotCoreException, InterruptedException {
/* 216 */     super.doArm();
/* 217 */     createSegments();
/*     */   }
/*     */   
/*     */   protected void doPretend() throws RobotCoreException, InterruptedException
/*     */   {
/* 222 */     super.doPretend();
/* 223 */     createSegments();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void createSegments()
/*     */   {
/* 229 */     for (int i = 0; i < 6; i++)
/*     */     {
/* 231 */       this.segments[i] = this.readWriteRunnable.createSegment(i, ADDRESS_I2C_PORT_MAP[i], 32);
/*     */       
/*     */ 
/* 234 */       this.segments[(i + 6)] = this.readWriteRunnable.createSegment(i + 6, ADDRESS_I2C_PORT_MAP[i] + 31, 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void initializeHardware()
/*     */   {
/* 241 */     for (int i = 0; i < 6; i++)
/*     */     {
/* 243 */       enableAnalogReadMode(i);
/* 244 */       this.readWriteRunnable.queueSegmentWrite(i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 253 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 263 */     RobotUsbDevice.FirmwareVersion version = this.robotUsbDevice.getFirmwareVersion();
/* 264 */     if (version != null) {
/* 265 */       return String.format("%s v%d.%d", new Object[] { this.context.getString(R.string.moduleDisplayNameLegacyModule), Integer.valueOf(version.majorVersion), Integer.valueOf(version.minorVersion) });
/*     */     }
/*     */     
/* 268 */     return this.context.getString(R.string.moduleDisplayNameLegacyModule);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 274 */     return "USB " + getSerialNumber();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 286 */     super.close();
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
/*     */   public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback callback, int port)
/*     */   {
/* 301 */     throwIfI2cPortIsInvalid(port);
/* 302 */     this.portReadyCallback[port] = callback;
/*     */   }
/*     */   
/*     */   public I2cController.I2cPortReadyCallback getI2cPortReadyCallback(int port)
/*     */   {
/* 307 */     throwIfI2cPortIsInvalid(port);
/* 308 */     return this.portReadyCallback[port];
/*     */   }
/*     */   
/*     */   public void deregisterForPortReadyCallback(int port)
/*     */   {
/* 313 */     throwIfI2cPortIsInvalid(port);
/* 314 */     this.portReadyCallback[port] = null;
/*     */   }
/*     */   
/*     */   public void enableI2cReadMode(int physicalPort, I2cAddr i2cAddress, int memAddress, int length)
/*     */   {
/* 319 */     throwIfI2cPortIsInvalid(physicalPort);
/* 320 */     throwIfBufferLengthIsInvalid(length);
/*     */     try
/*     */     {
/* 323 */       this.segments[physicalPort].getWriteLock().lock();
/* 324 */       byte[] buffer = this.segments[physicalPort].getWriteBuffer();
/* 325 */       this.lastI2cPortModes[physicalPort] = (buffer[0] = -127);
/*     */       
/* 327 */       buffer[1] = ((byte)i2cAddress.get8Bit());
/* 328 */       buffer[2] = ((byte)memAddress);
/* 329 */       buffer[3] = ((byte)length);
/*     */     } finally {
/* 331 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void enableI2cWriteMode(int physicalPort, I2cAddr i2cAddress, int memAddress, int length)
/*     */   {
/* 337 */     throwIfI2cPortIsInvalid(physicalPort);
/* 338 */     throwIfBufferLengthIsInvalid(length);
/*     */     try
/*     */     {
/* 341 */       this.segments[physicalPort].getWriteLock().lock();
/* 342 */       byte[] buffer = this.segments[physicalPort].getWriteBuffer();
/* 343 */       this.lastI2cPortModes[physicalPort] = (buffer[0] = 1);
/*     */       
/* 345 */       buffer[1] = ((byte)i2cAddress.get8Bit());
/* 346 */       buffer[2] = ((byte)memAddress);
/* 347 */       buffer[3] = ((byte)length);
/*     */     } finally {
/* 349 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void enableAnalogReadMode(int physicalPort)
/*     */   {
/* 355 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     try
/*     */     {
/* 358 */       this.segments[physicalPort].getWriteLock().lock();
/* 359 */       byte[] buffer = this.segments[physicalPort].getWriteBuffer();
/* 360 */       this.lastI2cPortModes[physicalPort] = (buffer[0] = 0);
/*     */     }
/*     */     finally {
/* 363 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/* 365 */     writeI2cCacheToController(physicalPort);
/*     */   }
/*     */   
/*     */   public void enable9v(int physicalPort, boolean enable)
/*     */   {
/* 370 */     if (Arrays.binarySearch(PORT_9V_CAPABLE, physicalPort) < 0) {
/* 371 */       throw new IllegalArgumentException("9v is only available on the following ports: " + Arrays.toString(PORT_9V_CAPABLE));
/*     */     }
/*     */     try
/*     */     {
/* 375 */       this.segments[physicalPort].getWriteLock().lock();
/* 376 */       byte mode = this.segments[physicalPort].getWriteBuffer()[0];
/* 377 */       if (enable) {
/* 378 */         mode = (byte)(mode | 0x2);
/*     */       } else {
/* 380 */         mode = (byte)(mode & 0xFFFFFFFD);
/*     */       }
/* 382 */       this.segments[physicalPort].getWriteBuffer()[0] = mode;
/*     */     } finally {
/* 384 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/* 386 */     writeI2cCacheToController(physicalPort);
/*     */   }
/*     */   
/*     */   public void setReadMode(int physicalPort, int i2cAddr, int memAddr, int memLen) {
/* 390 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     try
/*     */     {
/* 393 */       this.segments[physicalPort].getWriteLock().lock();
/* 394 */       byte[] buffer = this.segments[physicalPort].getWriteBuffer();
/* 395 */       this.lastI2cPortModes[physicalPort] = (buffer[0] = -127);
/*     */       
/* 397 */       buffer[1] = ((byte)i2cAddr);
/* 398 */       buffer[2] = ((byte)memAddr);
/* 399 */       buffer[3] = ((byte)memLen);
/*     */     } finally {
/* 401 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setWriteMode(int physicalPort, int i2cAddress, int memAddress) {
/* 406 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     try
/*     */     {
/* 409 */       this.segments[physicalPort].getWriteLock().lock();
/* 410 */       byte[] buffer = this.segments[physicalPort].getWriteBuffer();
/* 411 */       this.lastI2cPortModes[physicalPort] = (buffer[0] = 1);
/*     */       
/* 413 */       buffer[1] = ((byte)i2cAddress);
/* 414 */       buffer[2] = ((byte)memAddress);
/*     */     } finally {
/* 416 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setData(int physicalPort, byte[] data, int length) {
/* 421 */     throwIfI2cPortIsInvalid(physicalPort);
/* 422 */     throwIfBufferLengthIsInvalid(length);
/*     */     try
/*     */     {
/* 425 */       this.segments[physicalPort].getWriteLock().lock();
/* 426 */       byte[] buffer = this.segments[physicalPort].getWriteBuffer();
/* 427 */       System.arraycopy(data, 0, buffer, 4, length);
/* 428 */       buffer[3] = ((byte)length);
/*     */     } finally {
/* 430 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDigitalLine(int physicalPort, int line, boolean set)
/*     */   {
/* 436 */     throwIfI2cPortIsInvalid(physicalPort);
/* 437 */     throwIfDigitalLineIsInvalid(line);
/*     */     try
/*     */     {
/* 440 */       this.segments[physicalPort].getWriteLock().lock();
/* 441 */       byte mode = this.segments[physicalPort].getWriteBuffer()[0];
/*     */       
/* 443 */       if (set) {
/* 444 */         mode = (byte)(mode | DIGITAL_LINE[line]);
/*     */       } else {
/* 446 */         mode = (byte)(mode & (DIGITAL_LINE[line] ^ 0xFFFFFFFF));
/*     */       }
/*     */       
/* 449 */       this.segments[physicalPort].getWriteBuffer()[0] = mode;
/*     */     } finally {
/* 451 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/* 453 */     writeI2cCacheToController(physicalPort);
/*     */   }
/*     */   
/*     */   public byte[] readAnalogRaw(int physicalPort)
/*     */   {
/* 458 */     throwIfI2cPortIsInvalid(physicalPort);
/* 459 */     return read(ADDRESS_ANALOG_PORT_MAP[physicalPort], 2);
/*     */   }
/*     */   
/*     */   public double readAnalogVoltage(int physicalPort)
/*     */   {
/* 464 */     byte[] rawBytes = readAnalogRaw(physicalPort);
/* 465 */     int tenBits = TypeConversion.byteArrayToShort(rawBytes, ByteOrder.LITTLE_ENDIAN) & 0x3FF;
/* 466 */     return Range.scale(tenBits, 0.0D, 1023.0D, 0.0D, getMaxAnalogInputVoltage());
/*     */   }
/*     */   
/*     */   public double getMaxAnalogInputVoltage()
/*     */   {
/* 471 */     return 5.0D;
/*     */   }
/*     */   
/*     */   public byte[] getCopyOfReadBuffer(int physicalPort)
/*     */   {
/* 476 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 478 */     byte[] bufferCopy = null;
/*     */     try {
/* 480 */       this.segments[physicalPort].getReadLock().lock();
/* 481 */       byte[] buffer = this.segments[physicalPort].getReadBuffer();
/* 482 */       int length = buffer[3];
/* 483 */       bufferCopy = new byte[length];
/* 484 */       System.arraycopy(buffer, 4, bufferCopy, 0, bufferCopy.length);
/*     */     } finally {
/* 486 */       this.segments[physicalPort].getReadLock().unlock();
/*     */     }
/* 488 */     return bufferCopy;
/*     */   }
/*     */   
/*     */   public byte[] getCopyOfWriteBuffer(int physicalPort)
/*     */   {
/* 493 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 495 */     byte[] bufferCopy = null;
/*     */     try {
/* 497 */       this.segments[physicalPort].getWriteLock().lock();
/* 498 */       byte[] buffer = this.segments[physicalPort].getWriteBuffer();
/* 499 */       int length = buffer[3];
/* 500 */       bufferCopy = new byte[length];
/* 501 */       System.arraycopy(buffer, 4, bufferCopy, 0, bufferCopy.length);
/*     */     } finally {
/* 503 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/* 505 */     return bufferCopy;
/*     */   }
/*     */   
/*     */   public void copyBufferIntoWriteBuffer(int physicalPort, byte[] buffer)
/*     */   {
/* 510 */     throwIfI2cPortIsInvalid(physicalPort);
/* 511 */     throwIfBufferLengthIsInvalid(buffer.length);
/*     */     try
/*     */     {
/* 514 */       this.segments[physicalPort].getWriteLock().lock();
/* 515 */       byte[] writeBuffer = this.segments[physicalPort].getWriteBuffer();
/* 516 */       System.arraycopy(buffer, 0, writeBuffer, 4, buffer.length);
/*     */     } finally {
/* 518 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setI2cPortActionFlag(int physicalPort)
/*     */   {
/* 524 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     try
/*     */     {
/* 527 */       this.segments[physicalPort].getWriteLock().lock();
/* 528 */       byte[] buffer = this.segments[physicalPort].getWriteBuffer();
/* 529 */       buffer[31] = -1;
/*     */     } finally {
/* 531 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearI2cPortActionFlag(int physicalPort)
/*     */   {
/* 537 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     try
/*     */     {
/* 540 */       this.segments[physicalPort].getWriteLock().lock();
/* 541 */       byte[] buffer = this.segments[physicalPort].getWriteBuffer();
/* 542 */       buffer[31] = 0;
/*     */     } finally {
/* 544 */       this.segments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isI2cPortActionFlagSet(int physicalPort)
/*     */   {
/* 550 */     throwIfI2cPortIsInvalid(physicalPort);
/* 551 */     boolean isSet = false;
/*     */     try {
/* 553 */       this.segments[physicalPort].getReadLock().lock();
/* 554 */       byte[] buffer = this.segments[physicalPort].getReadBuffer();
/* 555 */       isSet = buffer[31] == -1;
/*     */     } finally {
/* 557 */       this.segments[physicalPort].getReadLock().unlock();
/*     */     }
/* 559 */     return isSet;
/*     */   }
/*     */   
/*     */   public void readI2cCacheFromController(int physicalPort)
/*     */   {
/* 564 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 566 */     this.readWriteRunnable.queueSegmentRead(physicalPort);
/*     */   }
/*     */   
/*     */   public void writeI2cCacheToController(int physicalPort)
/*     */   {
/* 571 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 573 */     this.readWriteRunnable.queueSegmentWrite(physicalPort);
/*     */   }
/*     */   
/*     */   public void writeI2cPortFlagOnlyToController(int physicalPort)
/*     */   {
/* 578 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 580 */     ReadWriteRunnableSegment full = this.segments[physicalPort];
/* 581 */     ReadWriteRunnableSegment flagOnly = this.segments[(physicalPort + 6)];
/*     */     try
/*     */     {
/* 584 */       full.getWriteLock().lock();
/* 585 */       flagOnly.getWriteLock().lock();
/* 586 */       flagOnly.getWriteBuffer()[0] = full.getWriteBuffer()[31];
/*     */     } finally {
/* 588 */       full.getWriteLock().unlock();
/* 589 */       flagOnly.getWriteLock().unlock();
/*     */     }
/*     */     
/* 592 */     this.readWriteRunnable.queueSegmentWrite(physicalPort + 6);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isI2cPortInReadMode(int physicalPort)
/*     */   {
/* 598 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 600 */     boolean inReadMode = false;
/*     */     try
/*     */     {
/* 603 */       this.segments[physicalPort].getReadLock().lock();
/* 604 */       byte[] buffer = this.segments[physicalPort].getReadBuffer();
/* 605 */       inReadMode = (isArmed() ? buffer[0] : this.lastI2cPortModes[physicalPort]) == -127;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 610 */       this.segments[physicalPort].getReadLock().unlock();
/*     */     }
/*     */     
/* 613 */     return inReadMode;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isI2cPortInWriteMode(int physicalPort)
/*     */   {
/* 619 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 621 */     boolean inWriteMode = false;
/*     */     try
/*     */     {
/* 624 */       this.segments[physicalPort].getReadLock().lock();
/* 625 */       byte[] buffer = this.segments[physicalPort].getReadBuffer();
/* 626 */       inWriteMode = (isArmed() ? buffer[0] : this.lastI2cPortModes[physicalPort]) == 1;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 631 */       this.segments[physicalPort].getReadLock().unlock();
/*     */     }
/*     */     
/* 634 */     return inWriteMode;
/*     */   }
/*     */   
/*     */   public boolean isI2cPortReady(int physicalPort)
/*     */   {
/* 639 */     byte bufferStatusByte = read8(3);
/* 640 */     return isPortReady(physicalPort, bufferStatusByte);
/*     */   }
/*     */   
/*     */   private void throwIfBufferLengthIsInvalid(int length) {
/* 644 */     if ((length < 0) || (length > 27)) {
/* 645 */       throw new IllegalArgumentException(String.format("buffer length of %d is invalid; max value is %d", new Object[] { Integer.valueOf(length), Byte.valueOf(27) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void throwIfDigitalLineIsInvalid(int line)
/*     */   {
/* 652 */     if ((line != 0) && (line != 1)) {
/* 653 */       throw new IllegalArgumentException("line is invalid, valid lines are 0 and 1");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readComplete()
/*     */     throws InterruptedException
/*     */   {
/* 662 */     if (this.portReadyCallback == null) { return;
/*     */     }
/* 664 */     byte bufferStatusByte = read8(3);
/* 665 */     for (int i = 0; i < 6; i++)
/*     */     {
/* 667 */       if ((this.portReadyCallback[i] != null) && (isPortReady(i, bufferStatusByte))) {
/* 668 */         this.portReadyCallback[i].portIsReady(i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isPortReady(int physicalPort, byte bufferStatusByte)
/*     */   {
/* 675 */     if (isArmed()) {
/* 676 */       return (bufferStatusByte & BUFFER_FLAG_MAP[physicalPort]) == 0;
/*     */     }
/* 678 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public Lock getI2cReadCacheLock(int physicalPort)
/*     */   {
/* 684 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 686 */     return this.segments[physicalPort].getReadLock();
/*     */   }
/*     */   
/*     */   public Lock getI2cWriteCacheLock(int physicalPort)
/*     */   {
/* 691 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 693 */     return this.segments[physicalPort].getWriteLock();
/*     */   }
/*     */   
/*     */   public byte[] getI2cReadCache(int physicalPort)
/*     */   {
/* 698 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 700 */     return this.segments[physicalPort].getReadBuffer();
/*     */   }
/*     */   
/*     */   public byte[] getI2cWriteCache(int physicalPort)
/*     */   {
/* 705 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 707 */     return this.segments[physicalPort].getWriteBuffer();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void readI2cCacheFromModule(int port)
/*     */   {
/* 713 */     readI2cCacheFromController(port);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void writeI2cCacheToModule(int port)
/*     */   {
/* 719 */     writeI2cCacheToController(port);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void writeI2cPortFlagOnlyToModule(int port)
/*     */   {
/* 725 */     writeI2cPortFlagOnlyToController(port);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbLegacyModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */