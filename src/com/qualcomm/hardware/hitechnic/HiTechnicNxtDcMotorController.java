/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.R.string;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorController;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.HeartbeatAction;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadMode;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadWindow;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.VoltageSensor;
/*     */ import com.qualcomm.robotcore.util.LastKnown;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HiTechnicNxtDcMotorController
/*     */   extends HiTechnicNxtController
/*     */   implements DcMotorController, VoltageSensor
/*     */ {
/*     */   protected static final int MOTOR_FIRST = 1;
/*     */   protected static final int MOTOR_LAST = 2;
/*     */   protected static final int MOTOR_MAX = 3;
/*  95 */   protected static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
/*     */   
/*     */   protected static final int OFFSET_UNUSED = -1;
/*     */   
/*     */   protected static final int CHANNEL_MODE_MASK_SELECTION = 3;
/*     */   
/*     */   protected static final int CHANNEL_MODE_MASK_LOCK = 4;
/*     */   
/*     */   protected static final int CHANNEL_MODE_MASK_REVERSE = 8;
/*     */   protected static final int CHANNEL_MODE_MASK_NO_TIMEOUT = 16;
/*     */   protected static final int CHANNEL_MODE_MASK_EMPTY_D5 = 32;
/*     */   protected static final int CHANNEL_MODE_MASK_ERROR = 64;
/*     */   protected static final int CHANNEL_MODE_MASK_BUSY = 128;
/*     */   protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_POWER_CONTROL_ONLY_NXT = 0;
/*     */   protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_CONSTANT_SPEED_NXT = 1;
/*     */   protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_TO_POSITION = 2;
/*     */   protected static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 3;
/* 112 */   protected static final byte[] ADDRESS_MOTOR_POWER_MAP = { -1, 69, 70 };
/* 113 */   protected static final byte[] ADDRESS_MOTOR_MODE_MAP = { -1, 68, 71 };
/* 114 */   protected static final byte[] ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP = { -1, 64, 72 };
/* 115 */   protected static final byte[] ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP = { -1, 76, 80 };
/*     */   
/*     */   protected static final int iRegWindowFirst = 64;
/*     */   
/*     */   protected static final int iRegWindowMax = 86;
/*     */   
/*     */   protected static final byte bPowerBrake = 0;
/*     */   
/*     */   protected static final byte bPowerFloat = -128;
/*     */   
/*     */   protected static final byte bPowerMax = 100;
/*     */   
/*     */   protected static final byte bPowerMin = -100;
/*     */   
/*     */   protected static final byte cbEncoder = 4;
/*     */   
/*     */   protected static final double apiPowerMin = -1.0D;
/*     */   
/*     */   protected static final double apiPowerMax = 1.0D;
/*     */   
/*     */   public static final int BUSY_THRESHOLD = 5;
/*     */   
/*     */   static class MotorProperties
/*     */   {
/* 139 */     LastKnown<Byte> lastKnownPowerByte = new LastKnown();
/* 140 */     LastKnown<Integer> lastKnownTargetPosition = new LastKnown();
/* 141 */     LastKnown<DcMotor.RunMode> lastKnownMode = new LastKnown();
/*     */     
/*     */ 
/* 144 */     DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
/* 145 */     boolean modeSwitchCompletionNeeded = false;
/* 146 */     DcMotor.RunMode prevRunMode = null;
/*     */     double prevPower;
/*     */     int maxSpeed;
/*     */   }
/*     */   
/* 151 */   final MotorProperties[] motors = new MotorProperties[3];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HiTechnicNxtDcMotorController(Context context, LegacyModule module, int physicalPort)
/*     */   {
/* 159 */     super(context, module, physicalPort, I2C_ADDRESS);
/* 160 */     for (int motor = 0; motor < this.motors.length; motor++)
/*     */     {
/* 162 */       this.motors[motor] = new MotorProperties();
/*     */     }
/* 164 */     resetMaxMotorSpeeds();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */     I2cDeviceSynch.HeartbeatAction heartbeatAction = new I2cDeviceSynch.HeartbeatAction(true, true, new I2cDeviceSynch.ReadWindow(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[1], 1, I2cDeviceSynch.ReadMode.ONLY_ONCE));
/*     */     
/*     */ 
/* 174 */     this.i2cDeviceSynch.setHeartbeatAction(heartbeatAction);
/* 175 */     this.i2cDeviceSynch.setHeartbeatInterval(2000);
/* 176 */     this.i2cDeviceSynch.enableWriteCoalescing(true);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */     this.i2cDeviceSynch.setReadWindow(new I2cDeviceSynch.ReadWindow(64, 22, I2cDeviceSynch.ReadMode.BALANCED));
/*     */     
/* 186 */     finishConstruction();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void resetMaxMotorSpeeds()
/*     */   {
/* 195 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 197 */       this.motors[motor].maxSpeed = getDefaultMaxMotorSpeed(motor);
/*     */     }
/*     */   }
/*     */   
/*     */   void brakeAllAtZero()
/*     */   {
/* 203 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 205 */       this.motors[motor].zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
/*     */     }
/*     */   }
/*     */   
/*     */   void forgetLastKnown()
/*     */   {
/* 211 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 213 */       this.motors[motor].lastKnownMode.invalidate();
/* 214 */       this.motors[motor].lastKnownPowerByte.invalidate();
/* 215 */       this.motors[motor].lastKnownTargetPosition.invalidate();
/*     */     }
/*     */   }
/*     */   
/*     */   void forgetLastKnownPowers()
/*     */   {
/* 221 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 223 */       this.motors[motor].lastKnownPowerByte.invalidate();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/* 232 */     adjustHookingToMatchEngagement();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doHook()
/*     */   {
/* 238 */     forgetLastKnown();
/* 239 */     this.i2cDeviceSynch.engage();
/*     */   }
/*     */   
/*     */ 
/*     */   public void initializeHardware()
/*     */   {
/* 245 */     initPID();
/* 246 */     floatHardware();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doUnhook()
/*     */   {
/* 252 */     this.i2cDeviceSynch.disengage();
/* 253 */     forgetLastKnown();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 263 */     return this.context.getString(R.string.nxtDcMotorControllerName);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 269 */     return String.format(this.context.getString(R.string.controllerPortConnectionInfoFormat), new Object[] { this.controller.getConnectionInfo(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */ 
/*     */   public int getVersion()
/*     */   {
/* 275 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode()
/*     */   {
/* 281 */     resetMaxMotorSpeeds();
/* 282 */     floatHardware();
/* 283 */     runWithoutEncoders();
/* 284 */     brakeAllAtZero();
/* 285 */     forgetLastKnown();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getDefaultMaxMotorSpeed(int motor)
/*     */   {
/* 294 */     int encoderTicksPerRevolution = 1440;
/* 295 */     int maxDegreesPerSecond = 1000;
/* 296 */     int degreesPerRevolution = 360;
/*     */     
/* 298 */     return 4000;
/*     */   }
/*     */   
/*     */   public synchronized int getMotorMaxSpeed(int motor)
/*     */   {
/* 303 */     validateMotor(motor);
/* 304 */     return this.motors[motor].maxSpeed;
/*     */   }
/*     */   
/*     */   public synchronized void setMotorMaxSpeed(int motor, int encoderTicksPerSecond)
/*     */   {
/* 309 */     validateMotor(motor);
/* 310 */     encoderTicksPerSecond = validateEncoderTicksPerSecond(motor, encoderTicksPerSecond);
/* 311 */     if (this.motors[motor].maxSpeed != encoderTicksPerSecond)
/*     */     {
/*     */ 
/* 314 */       DcMotor.RunMode mode = internalGetCachedOrQueriedRunMode(motor);
/* 315 */       if (mode.isPIDMode())
/*     */       {
/* 317 */         double power = internalGetCachedOrQueriedMotorPower(motor);
/* 318 */         this.motors[motor].maxSpeed = encoderTicksPerSecond;
/* 319 */         internalSetMotorPower(motor, power);
/*     */       }
/*     */       else
/*     */       {
/* 323 */         this.motors[motor].maxSpeed = encoderTicksPerSecond;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void setMotorMode(int motor, DcMotor.RunMode mode)
/*     */   {
/* 330 */     mode = mode.migrate();
/* 331 */     validateMotor(motor);
/* 332 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 334 */     DcMotor.RunMode prevMode = (DcMotor.RunMode)this.motors[motor].lastKnownMode.getNonTimedValue();
/* 335 */     if (this.motors[motor].lastKnownMode.updateValue(mode))
/*     */     {
/* 337 */       byte bNewMode = modeToByte(mode);
/*     */       
/*     */ 
/*     */ 
/* 341 */       this.motors[motor].modeSwitchCompletionNeeded = true;
/* 342 */       this.motors[motor].prevRunMode = prevMode;
/* 343 */       this.motors[motor].prevPower = internalGetCachedOrQueriedMotorPower(motor);
/*     */       
/*     */ 
/*     */ 
/* 347 */       write8(ADDRESS_MOTOR_MODE_MAP[motor], bNewMode);
/*     */     }
/*     */   }
/*     */   
/*     */   DcMotor.RunMode internalQueryRunMode(int motor)
/*     */   {
/* 353 */     byte b = this.i2cDeviceSynch.read8(ADDRESS_MOTOR_MODE_MAP[motor]);
/* 354 */     DcMotor.RunMode result = modeFromByte(b);
/* 355 */     this.motors[motor].lastKnownMode.setValue(result);
/* 356 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void finishModeSwitchIfNecessary(int motor)
/*     */   {
/* 366 */     if (!this.motors[motor].modeSwitchCompletionNeeded)
/*     */     {
/* 368 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 373 */     DcMotor.RunMode mode = internalGetCachedOrQueriedRunMode(motor);
/* 374 */     DcMotor.RunMode prevMode = this.motors[motor].prevRunMode;
/*     */     
/* 376 */     byte bNewMode = modeToByte(mode);
/* 377 */     byte bRunWithoutEncoderMode = modeToByte(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 384 */     while (isArmed())
/*     */     {
/* 386 */       byte bCurrentMode = (byte)(this.i2cDeviceSynch.read8(ADDRESS_MOTOR_MODE_MAP[motor]) & 0x3);
/* 387 */       if (bCurrentMode == bNewMode) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 395 */       if ((mode == DcMotor.RunMode.STOP_AND_RESET_ENCODER) && (bCurrentMode == bRunWithoutEncoderMode)) {
/*     */         break;
/*     */       }
/* 398 */       Thread.yield();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 406 */     if (mode == DcMotor.RunMode.STOP_AND_RESET_ENCODER)
/*     */     {
/*     */ 
/* 409 */       while (internalQueryMotorCurrentPosition(motor) != 0)
/*     */       {
/* 411 */         if (!isArmed()) break;
/* 412 */         Thread.yield();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 417 */     if ((mode.isPIDMode()) && ((prevMode == null) || (!prevMode.isPIDMode())))
/*     */     {
/* 419 */       double prevPower = this.motors[motor].prevPower;
/* 420 */       if (mode == DcMotor.RunMode.RUN_TO_POSITION)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 428 */         prevPower = Math.abs(prevPower);
/*     */       }
/* 430 */       internalSetMotorPower(motor, prevPower);
/*     */ 
/*     */     }
/* 433 */     else if (mode == DcMotor.RunMode.RUN_TO_POSITION)
/*     */     {
/* 435 */       double power = internalGetCachedOrQueriedMotorPower(motor);
/* 436 */       if (power < 0.0D) {
/* 437 */         internalSetMotorPower(motor, Math.abs(power));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 443 */     if (DcMotor.RunMode.STOP_AND_RESET_ENCODER.equals(prevMode))
/*     */     {
/* 445 */       Byte bPower = (Byte)this.motors[motor].lastKnownPowerByte.getRawValue();
/*     */       
/* 447 */       if (bPower != null)
/*     */       {
/* 449 */         double power = internalMotorPowerFromByte(motor, bPower.byteValue(), prevMode);
/* 450 */         internalSetMotorPower(motor, power);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 459 */     forgetLastKnownPowers();
/*     */     
/*     */ 
/* 462 */     this.motors[motor].modeSwitchCompletionNeeded = false;
/*     */   }
/*     */   
/*     */   public synchronized DcMotor.RunMode getMotorMode(int motor)
/*     */   {
/* 467 */     validateMotor(motor);
/* 468 */     finishModeSwitchIfNecessary(motor);
/* 469 */     return internalQueryRunMode(motor);
/*     */   }
/*     */   
/*     */   DcMotor.RunMode internalGetCachedOrQueriedRunMode(int motor)
/*     */   {
/* 474 */     DcMotor.RunMode mode = (DcMotor.RunMode)this.motors[motor].lastKnownMode.getNonTimedValue();
/* 475 */     if (mode == null)
/*     */     {
/* 477 */       mode = internalQueryRunMode(motor);
/*     */     }
/* 479 */     return mode;
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
/*     */ 
/*     */ 
/*     */   public synchronized boolean isBusy(int motor)
/*     */   {
/* 496 */     validateMotor(motor);
/* 497 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 499 */     return Math.abs(getMotorTargetPosition(motor) - getMotorCurrentPosition(motor)) > 5;
/*     */   }
/*     */   
/*     */   public synchronized void setMotorPower(int motor, double power)
/*     */   {
/* 504 */     validateMotor(motor);
/* 505 */     finishModeSwitchIfNecessary(motor);
/* 506 */     internalSetMotorPower(motor, power);
/*     */   }
/*     */   
/*     */   void internalSetMotorPower(int motor, double power)
/*     */   {
/* 511 */     power = Range.clip(power, -1.0D, 1.0D);
/* 512 */     validateApiMotorPower(power);
/*     */     
/* 514 */     DcMotor.RunMode mode = internalGetCachedOrQueriedRunMode(motor);
/* 515 */     if (mode.isPIDMode())
/*     */     {
/* 517 */       double defMaxSpeed = getDefaultMaxMotorSpeed(motor);
/* 518 */       power = Math.signum(power) * Range.scale(Math.abs(power), 0.0D, 1.0D, 0.0D, this.motors[motor].maxSpeed / defMaxSpeed);
/* 519 */       power = Range.clip(power, -1.0D, 1.0D);
/*     */     }
/*     */     
/* 522 */     byte bPower = (power == 0.0D) && (this.motors[motor].zeroPowerBehavior == DcMotor.ZeroPowerBehavior.FLOAT) ? Byte.MIN_VALUE : (byte)(int)Range.scale(power, -1.0D, 1.0D, -100.0D, 100.0D);
/*     */     
/*     */ 
/* 525 */     internalSetMotorPower(motor, bPower);
/*     */   }
/*     */   
/*     */   void internalSetMotorPower(int motor, byte bPower)
/*     */   {
/* 530 */     if (this.motors[motor].lastKnownPowerByte.updateValue(Byte.valueOf(bPower)))
/*     */     {
/* 532 */       write8(ADDRESS_MOTOR_POWER_MAP[motor], bPower);
/*     */     }
/*     */   }
/*     */   
/*     */   double internalQueryMotorPower(int motor)
/*     */   {
/* 538 */     byte bPower = this.i2cDeviceSynch.read8(ADDRESS_MOTOR_POWER_MAP[motor]);
/* 539 */     this.motors[motor].lastKnownPowerByte.setValue(Byte.valueOf(bPower));
/* 540 */     return internalMotorPowerFromByte(motor, bPower);
/*     */   }
/*     */   
/*     */   double internalGetCachedOrQueriedMotorPower(int motor)
/*     */   {
/* 545 */     Byte bPower = (Byte)this.motors[motor].lastKnownPowerByte.getNonTimedValue();
/* 546 */     if (bPower != null) {
/* 547 */       return internalMotorPowerFromByte(motor, bPower.byteValue());
/*     */     }
/* 549 */     return internalQueryMotorPower(motor);
/*     */   }
/*     */   
/*     */   double internalMotorPowerFromByte(int motor, byte bPower)
/*     */   {
/* 554 */     if (bPower == Byte.MIN_VALUE) {
/* 555 */       return 0.0D;
/*     */     }
/*     */     
/* 558 */     DcMotor.RunMode mode = internalGetCachedOrQueriedRunMode(motor);
/* 559 */     return internalMotorPowerFromByte(motor, bPower, mode);
/*     */   }
/*     */   
/*     */ 
/*     */   double internalMotorPowerFromByte(int motor, byte bPower, DcMotor.RunMode mode)
/*     */   {
/* 565 */     if (bPower == Byte.MIN_VALUE) {
/* 566 */       return 0.0D;
/*     */     }
/*     */     
/* 569 */     double power = Range.scale(bPower, -100.0D, 100.0D, -1.0D, 1.0D);
/* 570 */     if (mode.isPIDMode())
/*     */     {
/*     */ 
/* 573 */       double defMaxSpeed = getDefaultMaxMotorSpeed(motor);
/* 574 */       power = Math.signum(power) * Range.scale(Math.abs(power), 0.0D, this.motors[motor].maxSpeed / defMaxSpeed, 0.0D, 1.0D);
/*     */     }
/* 576 */     return Range.clip(power, -1.0D, 1.0D);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized double getMotorPower(int motor)
/*     */   {
/* 582 */     validateMotor(motor);
/* 583 */     finishModeSwitchIfNecessary(motor);
/* 584 */     return internalGetCachedOrQueriedMotorPower(motor);
/*     */   }
/*     */   
/*     */   public synchronized void setMotorZeroPowerBehavior(int motor, DcMotor.ZeroPowerBehavior zeroPowerBehavior)
/*     */   {
/* 589 */     validateMotor(motor);
/* 590 */     if (zeroPowerBehavior == DcMotor.ZeroPowerBehavior.UNKNOWN) throw new IllegalArgumentException("zeroPowerBehavior may not be UNKNOWN");
/* 591 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 593 */     if (this.motors[motor].zeroPowerBehavior != zeroPowerBehavior)
/*     */     {
/* 595 */       this.motors[motor].zeroPowerBehavior = zeroPowerBehavior;
/*     */       
/*     */ 
/* 598 */       if (internalGetCachedOrQueriedMotorPower(motor) == 0.0D)
/*     */       {
/* 600 */         this.motors[motor].lastKnownPowerByte.invalidate();
/* 601 */         internalSetMotorPower(motor, 0.0D);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized DcMotor.ZeroPowerBehavior getMotorZeroPowerBehavior(int motor)
/*     */   {
/* 608 */     validateMotor(motor);
/* 609 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 611 */     return this.motors[motor].zeroPowerBehavior;
/*     */   }
/*     */   
/*     */   protected synchronized void setMotorPowerFloat(int motor)
/*     */   {
/* 616 */     validateMotor(motor);
/* 617 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 619 */     write8(ADDRESS_MOTOR_POWER_MAP[motor], (byte)Byte.MIN_VALUE);
/*     */   }
/*     */   
/*     */   public synchronized boolean getMotorPowerFloat(int motor)
/*     */   {
/* 624 */     validateMotor(motor);
/* 625 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 627 */     byte bPower = this.i2cDeviceSynch.read8(ADDRESS_MOTOR_POWER_MAP[motor]);
/* 628 */     return bPower == Byte.MIN_VALUE;
/*     */   }
/*     */   
/*     */   public synchronized void setMotorTargetPosition(int motor, int position)
/*     */   {
/* 633 */     validateMotor(motor);
/* 634 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 636 */     if (this.motors[motor].lastKnownTargetPosition.updateValue(Integer.valueOf(position)))
/*     */     {
/*     */ 
/* 639 */       byte[] bytes = TypeConversion.intToByteArray(position, ByteOrder.BIG_ENDIAN);
/* 640 */       write(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], bytes);
/*     */     }
/*     */   }
/*     */   
/*     */   int internalQueryMotorTargetPosition(int motor)
/*     */   {
/* 646 */     byte[] bytes = this.i2cDeviceSynch.read(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], 4);
/* 647 */     int result = TypeConversion.byteArrayToInt(bytes, ByteOrder.BIG_ENDIAN);
/* 648 */     this.motors[motor].lastKnownTargetPosition.setValue(Integer.valueOf(result));
/* 649 */     return result;
/*     */   }
/*     */   
/*     */   public synchronized int getMotorTargetPosition(int motor)
/*     */   {
/* 654 */     validateMotor(motor);
/* 655 */     finishModeSwitchIfNecessary(motor);
/* 656 */     return internalQueryMotorTargetPosition(motor);
/*     */   }
/*     */   
/*     */   public synchronized int getMotorCurrentPosition(int motor)
/*     */   {
/* 661 */     validateMotor(motor);
/* 662 */     finishModeSwitchIfNecessary(motor);
/* 663 */     return internalQueryMotorCurrentPosition(motor);
/*     */   }
/*     */   
/*     */   int internalQueryMotorCurrentPosition(int motor)
/*     */   {
/* 668 */     byte[] bytes = this.i2cDeviceSynch.read(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[motor], 4);
/* 669 */     return TypeConversion.byteArrayToInt(bytes, ByteOrder.BIG_ENDIAN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void validateMotor(int motor)
/*     */   {
/* 678 */     if ((motor < 1) || (motor > 2))
/*     */     {
/* 680 */       throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are %d..%d", new Object[] { Integer.valueOf(motor), Integer.valueOf(1), Integer.valueOf(2) }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateApiMotorPower(double power)
/*     */   {
/* 686 */     if ((-1.0D > power) || (power > 1.0D))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 691 */       throw new IllegalArgumentException(String.format("illegal motor power %f; must be in interval [%f,%f]", new Object[] { Double.valueOf(power), Double.valueOf(-1.0D), Double.valueOf(1.0D) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private int validateEncoderTicksPerSecond(int motor, int encoderTicksPerSecond)
/*     */   {
/* 698 */     encoderTicksPerSecond = Range.clip(encoderTicksPerSecond, 1, getDefaultMaxMotorSpeed(motor));
/* 699 */     return encoderTicksPerSecond;
/*     */   }
/*     */   
/*     */   public static DcMotor.RunMode modeFromByte(byte flag)
/*     */   {
/* 704 */     switch (flag & 0x3) {
/*     */     case 0: 
/* 706 */       return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
/* 707 */     case 1:  return DcMotor.RunMode.RUN_USING_ENCODER;
/* 708 */     case 2:  return DcMotor.RunMode.RUN_TO_POSITION;
/* 709 */     case 3:  return DcMotor.RunMode.STOP_AND_RESET_ENCODER;
/*     */     }
/* 711 */     return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
/*     */   }
/*     */   
/*     */   public static byte modeToByte(DcMotor.RunMode mode)
/*     */   {
/* 716 */     switch (mode.migrate()) {
/*     */     case RUN_USING_ENCODER: 
/* 718 */       return 1;
/* 719 */     case RUN_WITHOUT_ENCODER:  return 0;
/* 720 */     case RUN_TO_POSITION:  return 2;
/* 721 */     case STOP_AND_RESET_ENCODER:  return 3;
/*     */     }
/* 723 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void initPID() {}
/*     */   
/*     */ 
/*     */   protected void floatHardware()
/*     */   {
/* 733 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 735 */       setMotorPowerFloat(motor);
/*     */     }
/* 737 */     this.i2cDeviceSynch.waitForWriteCompletions();
/*     */   }
/*     */   
/*     */   private void runWithoutEncoders()
/*     */   {
/* 742 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 744 */       setMotorMode(motor, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getVoltage()
/*     */   {
/*     */     try
/*     */     {
/* 758 */       byte[] bytes = this.i2cDeviceSynch.read(84, 2);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 764 */       bytes[1] = ((byte)(bytes[1] << 6));
/* 765 */       ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
/* 766 */       int tenBits = buffer.getShort() >> 6 & 0x3FF;
/* 767 */       return tenBits * 0.02D;
/*     */     }
/*     */     catch (RuntimeException e) {}
/*     */     
/*     */ 
/*     */ 
/* 773 */     return 0.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtDcMotorController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */