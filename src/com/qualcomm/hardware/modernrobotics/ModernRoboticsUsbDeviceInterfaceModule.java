/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.ArmableUsbDevice.OpenRobotUsbDevice;
/*     */ import com.qualcomm.hardware.R.string;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
/*     */ import com.qualcomm.robotcore.hardware.DigitalChannelController.Mode;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.FirmwareVersion;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModernRoboticsUsbDeviceInterfaceModule
/*     */   extends ModernRoboticsUsbI2cController
/*     */   implements DeviceInterfaceModule
/*     */ {
/*     */   public static final boolean DEBUG_LOGGING = false;
/*     */   public static final int MIN_I2C_PORT_NUMBER = 0;
/*     */   public static final int MAX_I2C_PORT_NUMBER = 5;
/*     */   public static final int MAX_ANALOG_PORT_NUMBER = 7;
/*     */   public static final int MIN_ANALOG_PORT_NUMBER = 0;
/*     */   public static final int NUMBER_OF_PORTS = 6;
/*     */   public static final int START_ADDRESS = 3;
/*     */   public static final int MONITOR_LENGTH = 21;
/*     */   public static final int SIZE_I2C_BUFFER = 27;
/*     */   public static final int SIZE_ANALOG_BUFFER = 2;
/*     */   public static final int WORD_SIZE = 2;
/*     */   public static final double MAX_ANALOG_INPUT_VOLTAGE = 5.0D;
/*     */   public static final int ADDRESS_BUFFER_STATUS = 3;
/*     */   public static final int ADDRESS_ANALOG_PORT_A0 = 4;
/*     */   public static final int ADDRESS_ANALOG_PORT_A1 = 6;
/*     */   public static final int ADDRESS_ANALOG_PORT_A2 = 8;
/*     */   public static final int ADDRESS_ANALOG_PORT_A3 = 10;
/*     */   public static final int ADDRESS_ANALOG_PORT_A4 = 12;
/*     */   public static final int ADDRESS_ANALOG_PORT_A5 = 14;
/*     */   public static final int ADDRESS_ANALOG_PORT_A6 = 16;
/*     */   public static final int ADDRESS_ANALOG_PORT_A7 = 18;
/*     */   public static final int ADDRESS_DIGITAL_INPUT_STATE = 20;
/*     */   public static final int ADDRESS_DIGITAL_IO_CONTROL = 21;
/*     */   public static final int ADDRESS_DIGITAL_OUTPUT_STATE = 22;
/*     */   public static final int ADDRESS_LED_SET = 23;
/*     */   public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_0 = 24;
/*     */   public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_1 = 30;
/*     */   public static final int ADDRESS_PULSE_OUTPUT_PORT_0 = 36;
/*     */   public static final int ADDRESS_PULSE_OUTPUT_PORT_1 = 40;
/*     */   public static final int ADDRESS_I2C0 = 48;
/*     */   public static final int ADDRESS_I2C1 = 80;
/*     */   public static final int ADDRESS_I2C2 = 112;
/*     */   public static final int ADDRESS_I2C3 = 144;
/*     */   public static final int ADDRESS_I2C4 = 176;
/*     */   public static final int ADDRESS_I2C5 = 208;
/*     */   public static final byte BUFFER_FLAG_I2C0 = 1;
/*     */   public static final byte BUFFER_FLAG_I2C1 = 2;
/*     */   public static final byte BUFFER_FLAG_I2C2 = 4;
/*     */   public static final byte BUFFER_FLAG_I2C3 = 8;
/*     */   public static final byte BUFFER_FLAG_I2C4 = 16;
/*     */   public static final byte BUFFER_FLAG_I2C5 = 32;
/*     */   public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_VOLTAGE = 0;
/*     */   public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_FREQ = 2;
/*     */   public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_MODE = 4;
/*     */   public static final int ANALOG_VOLTAGE_OUTPUT_BUFFER_SIZE = 5;
/*     */   public static final int OFFSET_PULSE_OUTPUT_TIME = 0;
/*     */   public static final int OFFSET_PULSE_OUTPUT_PERIOD = 2;
/*     */   public static final int PULSE_OUTPUT_BUFFER_SIZE = 4;
/*     */   public static final int OFFSET_I2C_PORT_MODE = 0;
/*     */   public static final int OFFSET_I2C_PORT_I2C_ADDRESS = 1;
/*     */   public static final int OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
/*     */   public static final int OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
/*     */   public static final int OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
/*     */   public static final int OFFSET_I2C_PORT_FLAG = 31;
/*     */   public static final int I2C_PORT_BUFFER_SIZE = 32;
/*     */   public static final byte I2C_MODE_READ = -128;
/*     */   public static final byte I2C_MODE_WRITE = 0;
/*     */   public static final byte I2C_ACTION_FLAG = -1;
/*     */   public static final byte I2C_NO_ACTION_FLAG = 0;
/*     */   public static final int LED_0_BIT_MASK = 1;
/*     */   public static final int LED_1_BIT_MASK = 2;
/* 145 */   public static final int[] LED_BIT_MASK_MAP = { 1, 2 };
/*     */   
/*     */ 
/*     */   public static final int D0_MASK = 1;
/*     */   
/*     */ 
/*     */   public static final int D1_MASK = 2;
/*     */   
/*     */   public static final int D2_MASK = 4;
/*     */   
/*     */   public static final int D3_MASK = 8;
/*     */   
/*     */   public static final int D4_MASK = 16;
/*     */   
/*     */   public static final int D5_MASK = 32;
/*     */   
/*     */   public static final int D6_MASK = 64;
/*     */   
/*     */   public static final int D7_MASK = 128;
/*     */   
/* 165 */   public static final int[] ADDRESS_DIGITAL_BIT_MASK = { 1, 2, 4, 8, 16, 32, 64, 128 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */   public static final int[] ADDRESS_ANALOG_PORT_MAP = { 4, 6, 8, 10, 12, 14, 16, 18 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */   public static final int[] ADDRESS_VOLTAGE_OUTPUT_PORT_MAP = { 24, 30 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 201 */   public static final int[] ADDRESS_PULSE_OUTPUT_PORT_MAP = { 36, 40 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */   public static final int[] ADDRESS_I2C_PORT_MAP = { 48, 80, 112, 144, 176, 208 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 221 */   public static final int[] BUFFER_FLAG_MAP = { 1, 2, 4, 8, 16, 32 };
/*     */   
/*     */   private static final int SEGMENT_KEY_ANALOG_VOLTAGE_OUTPUT_PORT_0 = 0;
/*     */   
/*     */   private static final int SEGMENT_KEY_ANALOG_VOLTAGE_OUTPUT_PORT_1 = 1;
/*     */   
/*     */   private static final int SEGMENT_KEY_PULSE_OUTPUT_PORT_0 = 2;
/*     */   
/*     */   private static final int SEGMENT_KEY_PULSE_OUTPUT_PORT_1 = 3;
/*     */   
/*     */   private static final int SEGMENT_KEY_I2C_PORT_0 = 4;
/*     */   
/*     */   private static final int SEGMENT_KEY_I2C_PORT_1 = 5;
/*     */   
/*     */   private static final int SEGMENT_KEY_I2C_PORT_2 = 6;
/*     */   
/*     */   private static final int SEGMENT_KEY_I2C_PORT_3 = 7;
/*     */   
/*     */   private static final int SEGMENT_KEY_I2C_PORT_4 = 8;
/*     */   
/*     */   private static final int SEGMENT_KEY_I2C_PORT_5 = 9;
/*     */   
/*     */   private static final int SEGMENT_KEY_I2C_PORT_0_FLAG_ONLY = 10;
/*     */   
/*     */   private static final int SEGMENT_KEY_I2C_PORT_1_FLAG_ONLY = 11;
/*     */   private static final int SEGMENT_KEY_I2C_PORT_2_FLAG_ONLY = 12;
/*     */   private static final int SEGMENT_KEY_I2C_PORT_3_FLAG_ONLY = 13;
/*     */   private static final int SEGMENT_KEY_I2C_PORT_4_FLAG_ONLY = 14;
/*     */   private static final int SEGMENT_KEY_I2C_PORT_5_FLAG_ONLY = 15;
/* 250 */   private static final int[] SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP = { 0, 1 };
/*     */   
/*     */ 
/*     */ 
/* 254 */   private static final int[] SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP = { 2, 3 };
/*     */   
/*     */ 
/*     */ 
/* 258 */   private static final int[] SEGMENT_KEY_I2C_PORT_MAP = { 4, 5, 6, 7, 8, 9 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 263 */   private static final int[] SEGMENT_KEY_I2C_PORT_FLAG_ONLY_MAP = { 10, 11, 12, 13, 14, 15 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 269 */   private final I2cController.I2cPortReadyCallback[] i2cPortReadyCallback = new I2cController.I2cPortReadyCallback[6];
/*     */   
/* 271 */   private ReadWriteRunnableSegment[] analogVoltagePortSegments = new ReadWriteRunnableSegment[SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP.length];
/* 272 */   private ReadWriteRunnableSegment[] pulseOutputPortSegments = new ReadWriteRunnableSegment[SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP.length];
/* 273 */   private ReadWriteRunnableSegment[] i2cPortSegments = new ReadWriteRunnableSegment[SEGMENT_KEY_I2C_PORT_MAP.length];
/* 274 */   private ReadWriteRunnableSegment[] i2cPortFlagOnlySegments = new ReadWriteRunnableSegment[SEGMENT_KEY_I2C_PORT_FLAG_ONLY_MAP.length];
/*     */   
/* 276 */   protected final byte[] lastI2cPortModes = new byte[6];
/*     */   
/*     */ 
/*     */   public static final int MAX_NEW_I2C_ADDRESS = 126;
/*     */   
/*     */ 
/*     */   public static final int MIN_NEW_I2C_ADDRESS = 16;
/*     */   
/*     */ 
/*     */ 
/*     */   public ModernRoboticsUsbDeviceInterfaceModule(Context context, final SerialNumber serialNumber, ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice, EventLoopManager manager)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 289 */     super(6, context, serialNumber, manager, openRobotUsbDevice, new ModernRoboticsUsbDevice.CreateReadWriteRunnable() {
/*     */       public ReadWriteRunnable create(RobotUsbDevice device) {
/* 291 */         return new ReadWriteRunnableStandard(ModernRoboticsUsbDeviceInterfaceModule.this, serialNumber, device, 21, 3, false);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   protected void doArm() throws RobotCoreException, InterruptedException {
/* 297 */     super.doArm();
/* 298 */     createSegments();
/*     */   }
/*     */   
/*     */   protected void doPretend() throws RobotCoreException, InterruptedException
/*     */   {
/* 303 */     super.doPretend();
/* 304 */     createSegments();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void createSegments()
/*     */   {
/* 310 */     for (int i = 0; i < SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP.length; i++) {
/* 311 */       this.analogVoltagePortSegments[i] = this.readWriteRunnable.createSegment(SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP[i], ADDRESS_VOLTAGE_OUTPUT_PORT_MAP[i], 5);
/*     */     }
/*     */     
/* 314 */     for (int i = 0; i < SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP.length; i++) {
/* 315 */       this.pulseOutputPortSegments[i] = this.readWriteRunnable.createSegment(SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP[i], ADDRESS_PULSE_OUTPUT_PORT_MAP[i], 4);
/*     */     }
/*     */     
/* 318 */     for (int i = 0; i < SEGMENT_KEY_I2C_PORT_MAP.length; i++) {
/* 319 */       this.i2cPortSegments[i] = this.readWriteRunnable.createSegment(SEGMENT_KEY_I2C_PORT_MAP[i], ADDRESS_I2C_PORT_MAP[i], 32);
/* 320 */       this.i2cPortFlagOnlySegments[i] = this.readWriteRunnable.createSegment(SEGMENT_KEY_I2C_PORT_FLAG_ONLY_MAP[i], ADDRESS_I2C_PORT_MAP[i] + 31, 1);
/* 321 */       this.lastI2cPortModes[i] = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initializeHardware() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 335 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 345 */     RobotUsbDevice.FirmwareVersion version = this.robotUsbDevice.getFirmwareVersion();
/* 346 */     if (version != null) {
/* 347 */       return String.format("%s v%d.%d", new Object[] { this.context.getString(R.string.moduleDisplayNameCDIM), Integer.valueOf(version.majorVersion), Integer.valueOf(version.minorVersion) });
/*     */     }
/*     */     
/* 350 */     return this.context.getString(R.string.moduleDisplayNameCDIM);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 356 */     return "USB " + getSerialNumber();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public double getAnalogInputVoltage(int channel)
/*     */   {
/* 367 */     throwIfAnalogPortIsInvalid(channel);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 372 */     byte[] buffer = read(ADDRESS_ANALOG_PORT_MAP[channel], 2);
/* 373 */     int tenBits = TypeConversion.byteArrayToShort(buffer, ByteOrder.LITTLE_ENDIAN) & 0x3FF;
/* 374 */     return Range.scale(tenBits, 0.0D, 1023.0D, 0.0D, getMaxAnalogInputVoltage());
/*     */   }
/*     */   
/*     */   public double getMaxAnalogInputVoltage()
/*     */   {
/* 379 */     return 5.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public DigitalChannelController.Mode getDigitalChannelMode(int channel)
/*     */   {
/* 385 */     return byteToRunMode(channel, getDigitalIOControlByte());
/*     */   }
/*     */   
/*     */   public void setDigitalChannelMode(int channel, DigitalChannelController.Mode mode)
/*     */   {
/* 390 */     int mask = runModeToBitMask(channel, mode);
/* 391 */     int oldMode = readFromWriteCache(21);
/*     */     
/*     */     int newMode;
/*     */     
/*     */     int newMode;
/* 396 */     if (mode == DigitalChannelController.Mode.OUTPUT) {
/* 397 */       newMode = oldMode | mask;
/*     */     }
/*     */     else
/*     */     {
/* 401 */       newMode = oldMode & mask;
/*     */     }
/* 403 */     write8(21, newMode);
/*     */   }
/*     */   
/*     */   public boolean getDigitalChannelState(int channel) {
/*     */     int val;
/*     */     int val;
/* 409 */     if (DigitalChannelController.Mode.OUTPUT == getDigitalChannelMode(channel)) {
/* 410 */       val = getDigitalOutputStateByte();
/*     */     } else {
/* 412 */       val = getDigitalInputStateByte();
/*     */     }
/* 414 */     val &= ADDRESS_DIGITAL_BIT_MASK[channel];
/* 415 */     return val > 0;
/*     */   }
/*     */   
/*     */   public void setDigitalChannelState(int channel, boolean state)
/*     */   {
/* 420 */     if (DigitalChannelController.Mode.OUTPUT == getDigitalChannelMode(channel)) {
/* 421 */       int outputState = readFromWriteCache(22);
/* 422 */       if (state)
/*     */       {
/* 424 */         outputState |= ADDRESS_DIGITAL_BIT_MASK[channel];
/*     */       }
/*     */       else {
/* 427 */         outputState &= (ADDRESS_DIGITAL_BIT_MASK[channel] ^ 0xFFFFFFFF);
/*     */       }
/* 429 */       setDigitalOutputByte((byte)outputState);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getDigitalInputStateByte()
/*     */   {
/* 435 */     byte buff = read8(20);
/* 436 */     return TypeConversion.unsignedByteToInt(buff);
/*     */   }
/*     */   
/*     */   public byte getDigitalIOControlByte()
/*     */   {
/* 441 */     return read8(21);
/*     */   }
/*     */   
/*     */   public void setDigitalIOControlByte(byte input)
/*     */   {
/* 446 */     write8(21, input);
/*     */   }
/*     */   
/*     */   public byte getDigitalOutputStateByte()
/*     */   {
/* 451 */     return read8(22);
/*     */   }
/*     */   
/*     */   public void setDigitalOutputByte(byte input)
/*     */   {
/* 456 */     write8(22, input);
/*     */   }
/*     */   
/*     */   private int runModeToBitMask(int channel, DigitalChannelController.Mode mode) {
/* 460 */     if (mode == DigitalChannelController.Mode.OUTPUT)
/*     */     {
/* 462 */       return ADDRESS_DIGITAL_BIT_MASK[channel];
/*     */     }
/*     */     
/*     */ 
/* 466 */     return ADDRESS_DIGITAL_BIT_MASK[channel] ^ 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   private DigitalChannelController.Mode byteToRunMode(int channel, int input)
/*     */   {
/* 471 */     int masked = ADDRESS_DIGITAL_BIT_MASK[channel] & input;
/* 472 */     if (masked > 0) {
/* 473 */       return DigitalChannelController.Mode.OUTPUT;
/*     */     }
/* 475 */     return DigitalChannelController.Mode.INPUT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean getLEDState(int channel)
/*     */   {
/* 482 */     throwIfLEDChannelIsInvalid(channel);
/*     */     
/* 484 */     byte buff = read8(23);
/* 485 */     int val = buff & LED_BIT_MASK_MAP[channel];
/* 486 */     return val > 0;
/*     */   }
/*     */   
/*     */   public void setLED(int channel, boolean set)
/*     */   {
/* 491 */     throwIfLEDChannelIsInvalid(channel);
/*     */     
/*     */ 
/* 494 */     byte buff = readFromWriteCache(23);
/* 495 */     int val; int val; if (set) {
/* 496 */       val = buff | LED_BIT_MASK_MAP[channel];
/*     */     }
/*     */     else {
/* 499 */       val = buff & (LED_BIT_MASK_MAP[channel] ^ 0xFFFFFFFF);
/*     */     }
/* 501 */     write8(23, val);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAnalogOutputVoltage(int port, int voltage)
/*     */   {
/* 507 */     throwIfAnalogOutputPortIsInvalid(port);
/*     */     
/* 509 */     Lock lock = this.analogVoltagePortSegments[port].getWriteLock();
/* 510 */     byte[] buffer = this.analogVoltagePortSegments[port].getWriteBuffer();
/* 511 */     byte[] newValue = TypeConversion.shortToByteArray((short)voltage, ByteOrder.LITTLE_ENDIAN);
/*     */     try
/*     */     {
/* 514 */       lock.lock();
/* 515 */       System.arraycopy(newValue, 0, buffer, 0, newValue.length);
/*     */     } finally {
/* 517 */       lock.unlock();
/*     */     }
/*     */     
/* 520 */     this.readWriteRunnable.queueSegmentWrite(SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP[port]);
/*     */   }
/*     */   
/*     */   public void setAnalogOutputFrequency(int port, int freq)
/*     */   {
/* 525 */     throwIfAnalogOutputPortIsInvalid(port);
/*     */     
/* 527 */     Lock lock = this.analogVoltagePortSegments[port].getWriteLock();
/* 528 */     byte[] buffer = this.analogVoltagePortSegments[port].getWriteBuffer();
/* 529 */     byte[] newValue = TypeConversion.shortToByteArray((short)freq, ByteOrder.LITTLE_ENDIAN);
/*     */     try
/*     */     {
/* 532 */       lock.lock();
/* 533 */       System.arraycopy(newValue, 0, buffer, 2, newValue.length);
/*     */     } finally {
/* 535 */       lock.unlock();
/*     */     }
/*     */     
/* 538 */     this.readWriteRunnable.queueSegmentWrite(SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP[port]);
/*     */   }
/*     */   
/*     */   public void setAnalogOutputMode(int port, byte mode)
/*     */   {
/* 543 */     throwIfAnalogOutputPortIsInvalid(port);
/*     */     
/* 545 */     Lock lock = this.analogVoltagePortSegments[port].getWriteLock();
/* 546 */     byte[] buffer = this.analogVoltagePortSegments[port].getWriteBuffer();
/*     */     try
/*     */     {
/* 549 */       lock.lock();
/* 550 */       buffer[4] = mode;
/*     */     } finally {
/* 552 */       lock.unlock();
/*     */     }
/*     */     
/* 555 */     this.readWriteRunnable.queueSegmentWrite(SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP[port]);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPulseWidthOutputTime(int port, int usDuration)
/*     */   {
/* 561 */     throwIfPulseWidthPortIsInvalid(port);
/*     */     
/* 563 */     Lock lock = this.pulseOutputPortSegments[port].getWriteLock();
/* 564 */     byte[] buffer = this.pulseOutputPortSegments[port].getWriteBuffer();
/* 565 */     byte[] newValue = TypeConversion.shortToByteArray((short)usDuration, ByteOrder.LITTLE_ENDIAN);
/*     */     try
/*     */     {
/* 568 */       lock.lock();
/* 569 */       System.arraycopy(newValue, 0, buffer, 0, newValue.length);
/*     */     } finally {
/* 571 */       lock.unlock();
/*     */     }
/*     */     
/* 574 */     this.readWriteRunnable.queueSegmentWrite(SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP[port]);
/*     */   }
/*     */   
/*     */   public void setPulseWidthPeriod(int port, int usPeriod)
/*     */   {
/* 579 */     throwIfI2cPortIsInvalid(port);
/*     */     
/* 581 */     Lock lock = this.pulseOutputPortSegments[port].getWriteLock();
/* 582 */     byte[] buffer = this.pulseOutputPortSegments[port].getWriteBuffer();
/* 583 */     byte[] newValue = TypeConversion.shortToByteArray((short)usPeriod, ByteOrder.LITTLE_ENDIAN);
/*     */     try
/*     */     {
/* 586 */       lock.lock();
/* 587 */       System.arraycopy(newValue, 0, buffer, 2, newValue.length);
/*     */     } finally {
/* 589 */       lock.unlock();
/*     */     }
/*     */     
/* 592 */     this.readWriteRunnable.queueSegmentWrite(SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP[port]);
/*     */   }
/*     */   
/*     */   public int getPulseWidthOutputTime(int port)
/*     */   {
/* 597 */     throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
/*     */   }
/*     */   
/*     */   public int getPulseWidthPeriod(int port)
/*     */   {
/* 602 */     throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enableI2cReadMode(int physicalPort, I2cAddr i2cAddress, int memAddress, int length)
/*     */   {
/* 610 */     throwIfI2cPortIsInvalid(physicalPort);
/* 611 */     throwIfBufferIsTooLarge(length);
/*     */     try
/*     */     {
/* 614 */       this.i2cPortSegments[physicalPort].getWriteLock().lock();
/* 615 */       byte[] buffer = this.i2cPortSegments[physicalPort].getWriteBuffer();
/* 616 */       this.lastI2cPortModes[physicalPort] = (buffer[0] = Byte.MIN_VALUE);
/*     */       
/* 618 */       buffer[1] = ((byte)i2cAddress.get8Bit());
/* 619 */       buffer[2] = ((byte)memAddress);
/* 620 */       buffer[3] = ((byte)length);
/*     */     } finally {
/* 622 */       this.i2cPortSegments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void enableI2cWriteMode(int physicalPort, I2cAddr i2cAddress, int memAddress, int length)
/*     */   {
/* 628 */     throwIfI2cPortIsInvalid(physicalPort);
/* 629 */     throwIfBufferIsTooLarge(length);
/*     */     try
/*     */     {
/* 632 */       this.i2cPortSegments[physicalPort].getWriteLock().lock();
/* 633 */       byte[] buffer = this.i2cPortSegments[physicalPort].getWriteBuffer();
/* 634 */       this.lastI2cPortModes[physicalPort] = (buffer[0] = 0);
/*     */       
/* 636 */       buffer[1] = ((byte)i2cAddress.get8Bit());
/* 637 */       buffer[2] = ((byte)memAddress);
/* 638 */       buffer[3] = ((byte)length);
/*     */     } finally {
/* 640 */       this.i2cPortSegments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public byte[] getCopyOfReadBuffer(int physicalPort)
/*     */   {
/* 646 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 648 */     byte[] bufferCopy = null;
/*     */     try {
/* 650 */       this.i2cPortSegments[physicalPort].getReadLock().lock();
/* 651 */       byte[] buffer = this.i2cPortSegments[physicalPort].getReadBuffer();
/* 652 */       int length = buffer[3];
/* 653 */       bufferCopy = new byte[length];
/* 654 */       System.arraycopy(buffer, 4, bufferCopy, 0, bufferCopy.length);
/*     */     } finally {
/* 656 */       this.i2cPortSegments[physicalPort].getReadLock().unlock();
/*     */     }
/* 658 */     return bufferCopy;
/*     */   }
/*     */   
/*     */   public byte[] getCopyOfWriteBuffer(int physicalPort)
/*     */   {
/* 663 */     throwIfI2cPortIsInvalid(physicalPort);
/*     */     
/* 665 */     byte[] bufferCopy = null;
/*     */     try {
/* 667 */       this.i2cPortSegments[physicalPort].getWriteLock().lock();
/* 668 */       byte[] buffer = this.i2cPortSegments[physicalPort].getWriteBuffer();
/* 669 */       int length = buffer[3];
/* 670 */       bufferCopy = new byte[length];
/* 671 */       System.arraycopy(buffer, 4, bufferCopy, 0, bufferCopy.length);
/*     */     } finally {
/* 673 */       this.i2cPortSegments[physicalPort].getWriteLock().unlock();
/*     */     }
/* 675 */     return bufferCopy;
/*     */   }
/*     */   
/*     */   public void copyBufferIntoWriteBuffer(int physicalPort, byte[] buffer)
/*     */   {
/* 680 */     throwIfI2cPortIsInvalid(physicalPort);
/* 681 */     throwIfBufferIsTooLarge(buffer.length);
/*     */     try
/*     */     {
/* 684 */       this.i2cPortSegments[physicalPort].getWriteLock().lock();
/* 685 */       byte[] writeBuffer = this.i2cPortSegments[physicalPort].getWriteBuffer();
/* 686 */       System.arraycopy(buffer, 0, writeBuffer, 4, buffer.length);
/*     */     } finally {
/* 688 */       this.i2cPortSegments[physicalPort].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setI2cPortActionFlag(int port)
/*     */   {
/* 694 */     throwIfI2cPortIsInvalid(port);
/*     */     try
/*     */     {
/* 697 */       this.i2cPortSegments[port].getWriteLock().lock();
/* 698 */       byte[] buffer = this.i2cPortSegments[port].getWriteBuffer();
/* 699 */       buffer[31] = -1;
/*     */     } finally {
/* 701 */       this.i2cPortSegments[port].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearI2cPortActionFlag(int port)
/*     */   {
/* 707 */     throwIfI2cPortIsInvalid(port);
/*     */     try
/*     */     {
/* 710 */       this.i2cPortSegments[port].getWriteLock().lock();
/* 711 */       byte[] buffer = this.i2cPortSegments[port].getWriteBuffer();
/* 712 */       buffer[31] = 0;
/*     */     } finally {
/* 714 */       this.i2cPortSegments[port].getWriteLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isI2cPortActionFlagSet(int port)
/*     */   {
/* 720 */     throwIfI2cPortIsInvalid(port);
/* 721 */     boolean isSet = false;
/*     */     try {
/* 723 */       this.i2cPortSegments[port].getReadLock().lock();
/* 724 */       byte[] buffer = this.i2cPortSegments[port].getReadBuffer();
/* 725 */       isSet = buffer[31] == -1;
/*     */     } finally {
/* 727 */       this.i2cPortSegments[port].getReadLock().unlock();
/*     */     }
/* 729 */     return isSet;
/*     */   }
/*     */   
/*     */   public void readI2cCacheFromController(int port)
/*     */   {
/* 734 */     throwIfI2cPortIsInvalid(port);
/*     */     
/* 736 */     this.readWriteRunnable.queueSegmentRead(SEGMENT_KEY_I2C_PORT_MAP[port]);
/*     */   }
/*     */   
/*     */   public void writeI2cCacheToController(int port)
/*     */   {
/* 741 */     throwIfI2cPortIsInvalid(port);
/*     */     
/* 743 */     this.readWriteRunnable.queueSegmentWrite(SEGMENT_KEY_I2C_PORT_MAP[port]);
/*     */   }
/*     */   
/*     */   public void writeI2cPortFlagOnlyToController(int port)
/*     */   {
/* 748 */     throwIfI2cPortIsInvalid(port);
/*     */     
/* 750 */     ReadWriteRunnableSegment full = this.i2cPortSegments[port];
/* 751 */     ReadWriteRunnableSegment flagOnly = this.i2cPortFlagOnlySegments[port];
/*     */     try
/*     */     {
/* 754 */       full.getWriteLock().lock();
/* 755 */       flagOnly.getWriteLock().lock();
/* 756 */       flagOnly.getWriteBuffer()[0] = full.getWriteBuffer()[31];
/*     */     } finally {
/* 758 */       full.getWriteLock().unlock();
/* 759 */       flagOnly.getWriteLock().unlock();
/*     */     }
/*     */     
/* 762 */     this.readWriteRunnable.queueSegmentWrite(SEGMENT_KEY_I2C_PORT_FLAG_ONLY_MAP[port]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isI2cPortInReadMode(int port)
/*     */   {
/* 769 */     throwIfI2cPortIsInvalid(port);
/*     */     
/* 771 */     boolean inReadMode = false;
/*     */     try
/*     */     {
/* 774 */       this.i2cPortSegments[port].getReadLock().lock();
/* 775 */       byte[] buffer = this.i2cPortSegments[port].getReadBuffer();
/* 776 */       inReadMode = (isArmed() ? buffer[0] : this.lastI2cPortModes[port]) == Byte.MIN_VALUE;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 781 */       this.i2cPortSegments[port].getReadLock().unlock();
/*     */     }
/*     */     
/* 784 */     return inReadMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isI2cPortInWriteMode(int port)
/*     */   {
/* 791 */     throwIfI2cPortIsInvalid(port);
/*     */     
/* 793 */     boolean inWriteMode = false;
/*     */     try
/*     */     {
/* 796 */       this.i2cPortSegments[port].getReadLock().lock();
/* 797 */       byte[] buffer = this.i2cPortSegments[port].getReadBuffer();
/* 798 */       inWriteMode = (isArmed() ? buffer[0] : this.lastI2cPortModes[port]) == 0;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 803 */       this.i2cPortSegments[port].getReadLock().unlock();
/*     */     }
/*     */     
/* 806 */     return inWriteMode;
/*     */   }
/*     */   
/*     */   public boolean isI2cPortReady(int port)
/*     */   {
/* 811 */     return isI2cPortReady(port, read8(3));
/*     */   }
/*     */   
/*     */   public Lock getI2cReadCacheLock(int port)
/*     */   {
/* 816 */     return this.i2cPortSegments[port].getReadLock();
/*     */   }
/*     */   
/*     */   public Lock getI2cWriteCacheLock(int port)
/*     */   {
/* 821 */     return this.i2cPortSegments[port].getWriteLock();
/*     */   }
/*     */   
/*     */   public byte[] getI2cReadCache(int port)
/*     */   {
/* 826 */     return this.i2cPortSegments[port].getReadBuffer();
/*     */   }
/*     */   
/*     */   public byte[] getI2cWriteCache(int port)
/*     */   {
/* 831 */     return this.i2cPortSegments[port].getWriteBuffer();
/*     */   }
/*     */   
/*     */   public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback callback, int port)
/*     */   {
/* 836 */     throwIfI2cPortIsInvalid(port);
/* 837 */     this.i2cPortReadyCallback[port] = callback;
/*     */   }
/*     */   
/*     */   public I2cController.I2cPortReadyCallback getI2cPortReadyCallback(int port)
/*     */   {
/* 842 */     throwIfI2cPortIsInvalid(port);
/* 843 */     return this.i2cPortReadyCallback[port];
/*     */   }
/*     */   
/*     */   public void deregisterForPortReadyCallback(int port)
/*     */   {
/* 848 */     throwIfI2cPortIsInvalid(port);
/* 849 */     this.i2cPortReadyCallback[port] = null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void readI2cCacheFromModule(int port)
/*     */   {
/* 855 */     readI2cCacheFromController(port);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void writeI2cCacheToModule(int port)
/*     */   {
/* 861 */     writeI2cCacheToController(port);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void writeI2cPortFlagOnlyToModule(int port)
/*     */   {
/* 867 */     writeI2cPortFlagOnlyToController(port);
/*     */   }
/*     */   
/*     */ 
/*     */   private void throwIfLEDChannelIsInvalid(int port)
/*     */   {
/* 873 */     if ((port != 0) && (port != 1)) {
/* 874 */       throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", new Object[] { Integer.valueOf(port) }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void throwIfAnalogOutputPortIsInvalid(int port) {
/* 879 */     if ((port != 0) && (port != 1)) {
/* 880 */       throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", new Object[] { Integer.valueOf(port) }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void throwIfPulseWidthPortIsInvalid(int port) {
/* 885 */     if ((port != 0) && (port != 1)) {
/* 886 */       throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", new Object[] { Integer.valueOf(port) }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void throwIfAnalogPortIsInvalid(int port) {
/* 891 */     if ((port < 0) || (port > 7)) {
/* 892 */       throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", new Object[] { Integer.valueOf(port), Integer.valueOf(0), Integer.valueOf(7) }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void throwIfDigitalLineIsInvalid(int line)
/*     */   {
/* 898 */     if ((line != 0) && (line != 1)) {
/* 899 */       throw new IllegalArgumentException("line is invalid, valid lines are 0 and 1");
/*     */     }
/*     */   }
/*     */   
/*     */   private void throwIfBufferIsTooLarge(int length) {
/* 904 */     if (length > 27) {
/* 905 */       throw new IllegalArgumentException(String.format("buffer is too large (%d byte), max size is %d bytes", new Object[] { Integer.valueOf(length), Integer.valueOf(27) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isI2cPortReady(int port, byte flags)
/*     */   {
/* 912 */     if (isArmed()) {
/* 913 */       return (flags & BUFFER_FLAG_MAP[port]) == 0;
/*     */     }
/* 915 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readComplete()
/*     */     throws InterruptedException
/*     */   {
/* 923 */     if (this.i2cPortReadyCallback == null) { return;
/*     */     }
/* 925 */     byte bufferStatusByte = read8(3);
/* 926 */     for (int i = 0; i < 6; i++)
/*     */     {
/* 928 */       if ((this.i2cPortReadyCallback[i] != null) && (isI2cPortReady(i, bufferStatusByte))) {
/* 929 */         this.i2cPortReadyCallback[i].portIsReady(i);
/*     */       }
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
/*     */   public static void throwIfModernRoboticsI2cAddressIsInvalid(I2cAddr newAddress)
/*     */   {
/* 943 */     if ((newAddress.get8Bit() < 16) || (newAddress.get8Bit() > 126)) {
/* 944 */       throw new IllegalArgumentException(String.format("New I2C address %d is invalid; valid range is: %d..%d", new Object[] { Integer.valueOf(newAddress.get8Bit()), Integer.valueOf(16), Integer.valueOf(126) }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbDeviceInterfaceModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */