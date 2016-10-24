/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.ArmableUsbDevice.OpenRobotUsbDevice;
/*     */ import com.qualcomm.hardware.HardwareFactory;
/*     */ import com.qualcomm.hardware.R.string;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorController;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.VoltageSensor;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.FirmwareVersion;
/*     */ import com.qualcomm.robotcore.util.DifferentialControlLoopCoefficients;
/*     */ import com.qualcomm.robotcore.util.LastKnown;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ModernRoboticsUsbDcMotorController
/*     */   extends ModernRoboticsUsbController
/*     */   implements DcMotorController, VoltageSensor
/*     */ {
/*     */   public static final String TAG = "MRMotorController";
/*     */   protected static final boolean DEBUG_LOGGING = false;
/*     */   protected static final int MONITOR_LENGTH = 30;
/*     */   protected static final int MOTOR_FIRST = 1;
/*     */   protected static final int MOTOR_LAST = 2;
/*     */   protected static final int MOTOR_MAX = 3;
/*     */   protected static final byte bPowerMax = 100;
/*     */   protected static final byte bPowerBrake = 0;
/*     */   protected static final byte bPowerMin = -100;
/*     */   protected static final byte bPowerFloat = -128;
/*     */   protected static final byte RATIO_MIN = -128;
/*     */   protected static final byte RATIO_MAX = 127;
/*     */   protected static final double apiPowerMin = -1.0D;
/*     */   protected static final double apiPowerMax = 1.0D;
/*     */   protected static final int DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAX = 255;
/*     */   protected static final int BATTERY_MAX_MEASURABLE_VOLTAGE_INT = 1023;
/*     */   protected static final double BATTERY_MAX_MEASURABLE_VOLTAGE = 20.4D;
/*     */   protected static final byte DEFAULT_P_COEFFICIENT = -128;
/*     */   protected static final byte DEFAULT_I_COEFFICIENT = 64;
/*     */   protected static final byte DEFAULT_D_COEFFICIENT = -72;
/*     */   protected static final byte START_ADDRESS = 64;
/*     */   protected static final int CHANNEL_MODE_MASK_SELECTION = 3;
/*     */   protected static final int CHANNEL_MODE_MASK_LOCK = 4;
/*     */   protected static final int CHANNEL_MODE_MASK_REVERSE = 8;
/*     */   protected static final int CHANNEL_MODE_MASK_NO_TIMEOUT = 16;
/*     */   protected static final int CHANNEL_MODE_MASK_EMPTY_D5 = 32;
/*     */   protected static final int CHANNEL_MODE_MASK_ERROR = 64;
/*     */   protected static final int CHANNEL_MODE_MASK_BUSY = 128;
/*     */   protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_POWER_CONTROL_ONLY = 0;
/*     */   protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_CONSTANT_SPEED = 1;
/*     */   protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_TO_POSITION = 2;
/*     */   protected static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 3;
/*     */   protected static final byte CHANNEL_MODE_FLAG_LOCK = 4;
/*     */   protected static final byte CHANNEL_MODE_FLAG_REVERSE = 8;
/*     */   protected static final byte CHANNEL_MODE_FLAG_NO_TIMEOUT = 16;
/*     */   protected static final byte CHANNEL_MODE_FLAG_UNUSED = 32;
/*     */   protected static final byte CHANNEL_MODE_FLAG_ERROR = 64;
/*     */   protected static final byte CHANNEL_MODE_FLAG_BUSY = -128;
/*     */   protected static final int ADDRESS_MOTOR1_TARGET_ENCODER_VALUE = 64;
/*     */   protected static final int ADDRESS_MOTOR1_MODE = 68;
/*     */   protected static final int ADDRESS_MOTOR1_POWER = 69;
/*     */   protected static final int ADDRESS_MOTOR2_POWER = 70;
/*     */   protected static final int ADDRESS_MOTOR2_MODE = 71;
/*     */   protected static final int ADDRESS_MOTOR2_TARGET_ENCODER_VALUE = 72;
/*     */   protected static final int ADDRESS_MOTOR1_CURRENT_ENCODER_VALUE = 76;
/*     */   protected static final int ADDRESS_MOTOR2_CURRENT_ENCODER_VALUE = 80;
/*     */   protected static final int ADDRESS_BATTERY_VOLTAGE = 84;
/*     */   protected static final int ADDRESS_MOTOR1_GEAR_RATIO = 86;
/*     */   protected static final int ADDRESS_MOTOR1_P_COEFFICIENT = 87;
/*     */   protected static final int ADDRESS_MOTOR1_I_COEFFICIENT = 88;
/*     */   protected static final int ADDRESS_MOTOR1_D_COEFFICIENT = 89;
/*     */   protected static final int ADDRESS_MOTOR2_GEAR_RATIO = 90;
/*     */   protected static final int ADDRESS_MOTOR2_P_COEFFICIENT = 91;
/*     */   protected static final int ADDRESS_MOTOR2_I_COEFFICIENT = 92;
/*     */   protected static final int ADDRESS_MOTOR2_D_COEFFICIENT = 93;
/*     */   protected static final int ADDRESS_UNUSED = 255;
/* 192 */   protected static final int[] ADDRESS_MOTOR_POWER_MAP = { 255, 69, 70 };
/* 193 */   protected static final int[] ADDRESS_MOTOR_MODE_MAP = { 255, 68, 71 };
/* 194 */   protected static final int[] ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP = { 255, 64, 72 };
/* 195 */   protected static final int[] ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP = { 255, 76, 80 };
/* 196 */   protected static final int[] ADDRESS_MOTOR_GEAR_RATIO_MAP = { 255, 86, 90 };
/* 197 */   protected static final int[] ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP = { 255, 87, 91 };
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int BUSY_THRESHOLD = 5;
/*     */   
/*     */ 
/*     */   protected static final byte cbEncoder = 4;
/*     */   
/*     */ 
/*     */ 
/*     */   static class MotorProperties
/*     */   {
/* 210 */     LastKnown<Byte> lastKnownPowerByte = new LastKnown();
/* 211 */     LastKnown<Integer> lastKnownTargetPosition = new LastKnown();
/* 212 */     LastKnown<DcMotor.RunMode> lastKnownMode = new LastKnown();
/*     */     
/*     */ 
/* 215 */     DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
/* 216 */     boolean modeSwitchCompletionNeeded = false;
/* 217 */     int modeSwitchWaitCount = 0;
/* 218 */     int modeSwitchWaitCountMax = 4;
/* 219 */     DcMotor.RunMode prevRunMode = null;
/*     */     double prevPower;
/*     */     int maxSpeed;
/*     */   }
/*     */   
/* 224 */   final MotorProperties[] motors = new MotorProperties[3];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModernRoboticsUsbDcMotorController(Context context, final SerialNumber serialNumber, ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice, EventLoopManager manager)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 236 */     super(context, serialNumber, manager, openRobotUsbDevice, new ModernRoboticsUsbDevice.CreateReadWriteRunnable()
/*     */     {
/*     */       public ReadWriteRunnable create(RobotUsbDevice device)
/*     */       {
/* 240 */         return new ReadWriteRunnableStandard(ModernRoboticsUsbDcMotorController.this, serialNumber, device, 30, 64, false);
/*     */       }
/*     */     });
/* 243 */     for (int motor = 0; motor < this.motors.length; motor++)
/*     */     {
/* 245 */       this.motors[motor] = new MotorProperties();
/*     */     }
/* 247 */     resetMaxMotorSpeeds();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initializeHardware()
/*     */   {
/* 254 */     floatHardware();
/* 255 */     setDifferentialControlLoopCoefficientsToDefault();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void resetMaxMotorSpeeds()
/*     */   {
/* 264 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 266 */       this.motors[motor].maxSpeed = getDefaultMaxMotorSpeed(motor);
/*     */     }
/*     */   }
/*     */   
/*     */   void brakeAllAtZero()
/*     */   {
/* 272 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 274 */       this.motors[motor].zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
/*     */     }
/*     */   }
/*     */   
/*     */   void forgetLastKnown()
/*     */   {
/* 280 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 282 */       this.motors[motor].lastKnownPowerByte.invalidate();
/* 283 */       this.motors[motor].lastKnownMode.invalidate();
/* 284 */       this.motors[motor].lastKnownTargetPosition.invalidate();
/*     */     }
/*     */   }
/*     */   
/*     */   void forgetLastKnownPowers()
/*     */   {
/* 290 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 292 */       this.motors[motor].lastKnownPowerByte.invalidate();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doArm() throws RobotCoreException, InterruptedException
/*     */   {
/* 298 */     doArmOrPretend(true);
/*     */   }
/*     */   
/*     */   protected void doPretend() throws RobotCoreException, InterruptedException {
/* 302 */     doArmOrPretend(false);
/*     */   }
/*     */   
/*     */   private void doArmOrPretend(boolean isArm) throws RobotCoreException, InterruptedException
/*     */   {
/* 307 */     RobotLog.d("arming modern motor controller %s%s...", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber), isArm ? "" : " (pretend)" });
/*     */     
/* 309 */     forgetLastKnown();
/* 310 */     if (isArm) {
/* 311 */       armDevice();
/*     */     } else {
/* 313 */       pretendDevice();
/*     */     }
/* 315 */     RobotLog.d("...arming modern motor controller %s complete", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
/*     */   }
/*     */   
/*     */   protected void doDisarm() throws RobotCoreException, InterruptedException
/*     */   {
/* 320 */     RobotLog.d("disarming modern motor controller %s...", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
/*     */     
/* 322 */     disarmDevice();
/* 323 */     forgetLastKnown();
/*     */     
/* 325 */     RobotLog.d("...disarming modern motor controller %s complete", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
/*     */   }
/*     */   
/*     */   protected void doCloseFromArmed()
/*     */   {
/* 330 */     floatHardware();
/* 331 */     doCloseFromOther();
/*     */   }
/*     */   
/*     */   protected void doCloseFromOther()
/*     */   {
/*     */     try {
/* 337 */       doDisarm();
/*     */     }
/*     */     catch (InterruptedException e)
/*     */     {
/* 341 */       Thread.currentThread().interrupt();
/*     */     }
/*     */     catch (RobotCoreException localRobotCoreException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 355 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 366 */     RobotUsbDevice.FirmwareVersion version = this.robotUsbDevice.getFirmwareVersion();
/* 367 */     if (version != null)
/*     */     {
/* 369 */       return String.format("%s v%d.%d", new Object[] { this.context.getString(R.string.moduleDisplayNameMotorController), Integer.valueOf(version.majorVersion), Integer.valueOf(version.minorVersion) });
/*     */     }
/*     */     
/*     */ 
/* 373 */     return this.context.getString(R.string.moduleDisplayNameMotorController);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 380 */     return "USB " + getSerialNumber();
/*     */   }
/*     */   
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode()
/*     */   {
/* 386 */     resetMaxMotorSpeeds();
/* 387 */     floatHardware();
/* 388 */     runWithoutEncoders();
/* 389 */     brakeAllAtZero();
/* 390 */     forgetLastKnown();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 398 */     floatHardware();
/* 399 */     super.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getDefaultMaxMotorSpeed(int motor)
/*     */   {
/* 408 */     int encoderTicksPerRevolution = 1440;
/* 409 */     int maxDegreesPerSecond = 1000;
/* 410 */     int degreesPerRevolution = 360;
/*     */     
/* 412 */     return 4000;
/*     */   }
/*     */   
/*     */   public synchronized int getMotorMaxSpeed(int motor)
/*     */   {
/* 417 */     validateMotor(motor);
/* 418 */     return this.motors[motor].maxSpeed;
/*     */   }
/*     */   
/*     */   public synchronized void setMotorMaxSpeed(int motor, int encoderTicksPerSecond)
/*     */   {
/* 423 */     validateMotor(motor);
/* 424 */     encoderTicksPerSecond = validateEncoderTicksPerSecond(motor, encoderTicksPerSecond);
/* 425 */     if (this.motors[motor].maxSpeed != encoderTicksPerSecond)
/*     */     {
/*     */ 
/* 428 */       DcMotor.RunMode mode = internalGetCachedOrQueriedRunMode(motor);
/* 429 */       if (mode.isPIDMode())
/*     */       {
/* 431 */         double power = internalGetCachedOrQueriedMotorPower(motor);
/* 432 */         this.motors[motor].maxSpeed = encoderTicksPerSecond;
/* 433 */         internalSetMotorPower(motor, power);
/*     */       }
/*     */       else
/*     */       {
/* 437 */         this.motors[motor].maxSpeed = encoderTicksPerSecond;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void setMotorMode(int motor, DcMotor.RunMode mode)
/*     */   {
/* 444 */     mode = mode.migrate();
/* 445 */     validateMotor(motor);
/* 446 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 448 */     DcMotor.RunMode prevMode = (DcMotor.RunMode)this.motors[motor].lastKnownMode.getNonTimedValue();
/* 449 */     if (this.motors[motor].lastKnownMode.updateValue(mode))
/*     */     {
/*     */ 
/*     */ 
/* 453 */       this.motors[motor].modeSwitchCompletionNeeded = true;
/* 454 */       this.motors[motor].modeSwitchWaitCount = 0;
/* 455 */       this.motors[motor].prevRunMode = prevMode;
/* 456 */       this.motors[motor].prevPower = internalGetCachedOrQueriedMotorPower(motor);
/*     */       
/* 458 */       byte bNewMode = modeToByte(mode);
/* 459 */       write8(ADDRESS_MOTOR_MODE_MAP[motor], bNewMode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void finishModeSwitchIfNecessary(int motor)
/*     */   {
/* 470 */     if (!this.motors[motor].modeSwitchCompletionNeeded)
/*     */     {
/* 472 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 476 */       DcMotor.RunMode mode = internalGetCachedOrQueriedRunMode(motor);
/* 477 */       DcMotor.RunMode prevMode = this.motors[motor].prevRunMode;
/* 478 */       byte bNewMode = modeToByte(mode);
/* 479 */       byte bRunWithoutEncoderMode = modeToByte(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 486 */       while (isArmed())
/*     */       {
/* 488 */         byte bCurrentMode = (byte)(read8(ADDRESS_MOTOR_MODE_MAP[motor]) & 0x3);
/* 489 */         if (bCurrentMode == bNewMode) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 498 */         if ((mode == DcMotor.RunMode.STOP_AND_RESET_ENCODER) && (bCurrentMode == bRunWithoutEncoderMode)) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 504 */         if (this.motors[motor].modeSwitchWaitCount++ >= this.motors[motor].modeSwitchWaitCountMax)
/*     */         {
/* 506 */           RobotLog.dd("MRMotorController", "mode resend: motor=[%s,%d] wait=%d from=%d to=%d cur=%d", new Object[] { getSerialNumber(), Integer.valueOf(motor), Integer.valueOf(this.motors[motor].modeSwitchWaitCount - 1), Byte.valueOf(modeToByte(prevMode)), Byte.valueOf(bNewMode), Byte.valueOf(bCurrentMode) });
/*     */           
/*     */ 
/* 509 */           write8(ADDRESS_MOTOR_MODE_MAP[motor], bNewMode);
/* 510 */           this.motors[motor].modeSwitchWaitCount = 0;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 516 */         if (!waitForNextReadComplete())
/*     */           break;
/*     */       }
/* 519 */       if ((mode.isPIDMode()) && ((prevMode == null) || (!prevMode.isPIDMode())))
/*     */       {
/* 521 */         double power = this.motors[motor].prevPower;
/* 522 */         if (mode == DcMotor.RunMode.RUN_TO_POSITION)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 530 */           power = Math.abs(power);
/*     */         }
/* 532 */         internalSetMotorPower(motor, power);
/*     */ 
/*     */       }
/* 535 */       else if (mode == DcMotor.RunMode.RUN_TO_POSITION)
/*     */       {
/* 537 */         double power = internalQueryMotorPower(motor);
/* 538 */         if (power < 0.0D) {
/* 539 */           internalSetMotorPower(motor, Math.abs(power));
/*     */         }
/*     */       }
/* 542 */       if (mode == DcMotor.RunMode.STOP_AND_RESET_ENCODER)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 551 */         long nsSendInterval = 100000000L;
/* 552 */         long nsResendDeadline = System.nanoTime() + nsSendInterval;
/* 553 */         while (internalQueryMotorCurrentPosition(motor) != 0)
/*     */         {
/*     */ 
/* 556 */           long nsNow = System.nanoTime();
/* 557 */           if (nsNow > nsResendDeadline)
/*     */           {
/* 559 */             RobotLog.dd("MRMotorController", "mode resend: motor=[%s,%d] mode=%s", new Object[] { getSerialNumber(), Integer.valueOf(motor), mode });
/* 560 */             write8(ADDRESS_MOTOR_MODE_MAP[motor], bNewMode);
/* 561 */             nsResendDeadline = nsNow + nsSendInterval;
/*     */           }
/*     */           
/* 564 */           if ((!isArmed()) || 
/* 565 */             (!waitForNextReadComplete()))
/*     */           {
/*     */             break;
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 578 */       forgetLastKnown();
/*     */       
/*     */ 
/* 581 */       this.motors[motor].modeSwitchCompletionNeeded = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized DcMotor.RunMode getMotorMode(int motor)
/*     */   {
/* 587 */     validateMotor(motor);
/* 588 */     finishModeSwitchIfNecessary(motor);
/* 589 */     return internalQueryRunMode(motor);
/*     */   }
/*     */   
/*     */   DcMotor.RunMode internalQueryRunMode(int motor)
/*     */   {
/* 594 */     byte b = read8(ADDRESS_MOTOR_MODE_MAP[motor]);
/* 595 */     DcMotor.RunMode result = modeFromByte(b);
/* 596 */     this.motors[motor].lastKnownMode.setValue(result);
/* 597 */     return result;
/*     */   }
/*     */   
/*     */   DcMotor.RunMode internalGetCachedOrQueriedRunMode(int motor)
/*     */   {
/* 602 */     DcMotor.RunMode mode = (DcMotor.RunMode)this.motors[motor].lastKnownMode.getNonTimedValue();
/* 603 */     if (mode == null)
/*     */     {
/* 605 */       mode = internalQueryRunMode(motor);
/*     */     }
/* 607 */     return mode;
/*     */   }
/*     */   
/*     */   public synchronized void setMotorPower(int motor, double power)
/*     */   {
/* 612 */     validateMotor(motor);
/* 613 */     finishModeSwitchIfNecessary(motor);
/* 614 */     internalSetMotorPower(motor, power);
/*     */   }
/*     */   
/*     */   void internalSetMotorPower(int motor, double power)
/*     */   {
/* 619 */     power = Range.clip(power, -1.0D, 1.0D);
/* 620 */     validateApiMotorPower(power);
/*     */     
/* 622 */     DcMotor.RunMode mode = internalGetCachedOrQueriedRunMode(motor);
/* 623 */     if (mode.isPIDMode())
/*     */     {
/* 625 */       double defMaxSpeed = getDefaultMaxMotorSpeed(motor);
/* 626 */       power = Math.signum(power) * Range.scale(Math.abs(power), 0.0D, 1.0D, 0.0D, this.motors[motor].maxSpeed / defMaxSpeed);
/* 627 */       power = Range.clip(power, -1.0D, 1.0D);
/*     */     }
/*     */     
/* 630 */     byte bPower = (power == 0.0D) && (this.motors[motor].zeroPowerBehavior == DcMotor.ZeroPowerBehavior.FLOAT) ? Byte.MIN_VALUE : (byte)(int)Range.scale(power, -1.0D, 1.0D, -100.0D, 100.0D);
/*     */     
/*     */ 
/* 633 */     internalSetMotorPower(motor, bPower);
/*     */   }
/*     */   
/*     */   void internalSetMotorPower(int motor, byte bPower)
/*     */   {
/* 638 */     if (this.motors[motor].lastKnownPowerByte.updateValue(Byte.valueOf(bPower)))
/*     */     {
/* 640 */       write8(ADDRESS_MOTOR_POWER_MAP[motor], bPower);
/*     */     }
/*     */   }
/*     */   
/*     */   double internalQueryMotorPower(int motor)
/*     */   {
/* 646 */     byte bPower = read8(ADDRESS_MOTOR_POWER_MAP[motor]);
/* 647 */     this.motors[motor].lastKnownPowerByte.setValue(Byte.valueOf(bPower));
/* 648 */     return internalMotorPowerFromByte(motor, bPower);
/*     */   }
/*     */   
/*     */   double internalGetCachedOrQueriedMotorPower(int motor)
/*     */   {
/* 653 */     Byte bPower = (Byte)this.motors[motor].lastKnownPowerByte.getNonTimedValue();
/* 654 */     if (bPower != null) {
/* 655 */       return internalMotorPowerFromByte(motor, bPower.byteValue());
/*     */     }
/* 657 */     return internalQueryMotorPower(motor);
/*     */   }
/*     */   
/*     */   double internalMotorPowerFromByte(int motor, byte bPower)
/*     */   {
/* 662 */     if (bPower == Byte.MIN_VALUE) {
/* 663 */       return 0.0D;
/*     */     }
/*     */     
/* 666 */     double power = Range.scale(bPower, -100.0D, 100.0D, -1.0D, 1.0D);
/* 667 */     DcMotor.RunMode mode = internalGetCachedOrQueriedRunMode(motor);
/* 668 */     if (mode.isPIDMode())
/*     */     {
/*     */ 
/* 671 */       double defMaxSpeed = getDefaultMaxMotorSpeed(motor);
/* 672 */       power = Math.signum(power) * Range.scale(Math.abs(power), 0.0D, this.motors[motor].maxSpeed / defMaxSpeed, 0.0D, 1.0D);
/*     */     }
/* 674 */     return Range.clip(power, -1.0D, 1.0D);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized double getMotorPower(int motor)
/*     */   {
/* 680 */     validateMotor(motor);
/* 681 */     finishModeSwitchIfNecessary(motor);
/* 682 */     return internalQueryMotorPower(motor);
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
/*     */ 
/*     */ 
/*     */   public boolean isBusy(int motor)
/*     */   {
/* 701 */     validateMotor(motor);
/* 702 */     finishModeSwitchIfNecessary(motor);
/*     */     
/*     */ 
/* 705 */     return Math.abs(getMotorTargetPosition(motor) - getMotorCurrentPosition(motor)) > 5;
/*     */   }
/*     */   
/*     */   public synchronized void setMotorZeroPowerBehavior(int motor, DcMotor.ZeroPowerBehavior zeroPowerBehavior)
/*     */   {
/* 710 */     validateMotor(motor);
/* 711 */     if (zeroPowerBehavior == DcMotor.ZeroPowerBehavior.UNKNOWN) throw new IllegalArgumentException("zeroPowerBehavior may not be UNKNOWN");
/* 712 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 714 */     if (this.motors[motor].zeroPowerBehavior != zeroPowerBehavior)
/*     */     {
/* 716 */       this.motors[motor].zeroPowerBehavior = zeroPowerBehavior;
/*     */       
/*     */ 
/* 719 */       if (internalGetCachedOrQueriedMotorPower(motor) == 0.0D)
/*     */       {
/* 721 */         this.motors[motor].lastKnownPowerByte.invalidate();
/* 722 */         internalSetMotorPower(motor, 0.0D);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized DcMotor.ZeroPowerBehavior getMotorZeroPowerBehavior(int motor)
/*     */   {
/* 729 */     validateMotor(motor);
/* 730 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 732 */     return this.motors[motor].zeroPowerBehavior;
/*     */   }
/*     */   
/*     */   protected synchronized void setMotorPowerFloat(int motor)
/*     */   {
/* 737 */     validateMotor(motor);
/* 738 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 740 */     write8(ADDRESS_MOTOR_POWER_MAP[motor], (byte)Byte.MIN_VALUE);
/*     */   }
/*     */   
/*     */   public synchronized boolean getMotorPowerFloat(int motor)
/*     */   {
/* 745 */     validateMotor(motor);
/* 746 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 748 */     byte bPower = read8(ADDRESS_MOTOR_POWER_MAP[motor]);
/* 749 */     return bPower == Byte.MIN_VALUE;
/*     */   }
/*     */   
/*     */   public synchronized void setMotorTargetPosition(int motor, int position)
/*     */   {
/* 754 */     validateMotor(motor);
/* 755 */     finishModeSwitchIfNecessary(motor);
/*     */     
/* 757 */     if (this.motors[motor].lastKnownTargetPosition.updateValue(Integer.valueOf(position)))
/*     */     {
/*     */ 
/* 760 */       write(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], TypeConversion.intToByteArray(position, ByteOrder.BIG_ENDIAN));
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized int getMotorTargetPosition(int motor)
/*     */   {
/* 766 */     validateMotor(motor);
/* 767 */     finishModeSwitchIfNecessary(motor);
/* 768 */     return internalQueryMotorTargetPosition(motor);
/*     */   }
/*     */   
/*     */   int internalQueryMotorTargetPosition(int motor)
/*     */   {
/* 773 */     byte[] rgbPosition = read(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], 4);
/* 774 */     int result = TypeConversion.byteArrayToInt(rgbPosition, ByteOrder.BIG_ENDIAN);
/* 775 */     this.motors[motor].lastKnownTargetPosition.setValue(Integer.valueOf(result));
/* 776 */     return result;
/*     */   }
/*     */   
/*     */   public synchronized int getMotorCurrentPosition(int motor)
/*     */   {
/* 781 */     validateMotor(motor);
/* 782 */     finishModeSwitchIfNecessary(motor);
/* 783 */     return internalQueryMotorCurrentPosition(motor);
/*     */   }
/*     */   
/*     */   int internalQueryMotorCurrentPosition(int motor)
/*     */   {
/* 788 */     byte[] bytes = read(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[motor], 4);
/* 789 */     return TypeConversion.byteArrayToInt(bytes, ByteOrder.BIG_ENDIAN);
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
/*     */   public double getVoltage()
/*     */   {
/* 805 */     byte[] data = read(84, 2);
/*     */     
/*     */ 
/* 808 */     int voltage = TypeConversion.unsignedByteToInt(data[0]);
/* 809 */     voltage <<= 2;
/* 810 */     voltage += (TypeConversion.unsignedByteToInt(data[1]) & 0x3);
/* 811 */     voltage &= 0x3FF;
/*     */     
/*     */ 
/* 814 */     double percent = voltage / 1023.0D;
/*     */     
/*     */ 
/* 817 */     return percent * 20.4D;
/*     */   }
/*     */   
/*     */   public void setGearRatio(int motor, double ratio)
/*     */   {
/* 822 */     validateMotor(motor);
/* 823 */     Range.throwIfRangeIsInvalid(ratio, -1.0D, 1.0D);
/*     */     
/* 825 */     write(ADDRESS_MOTOR_GEAR_RATIO_MAP[motor], new byte[] { (byte)(int)(ratio * 127.0D) });
/*     */   }
/*     */   
/*     */   public double getGearRatio(int motor)
/*     */   {
/* 830 */     validateMotor(motor);
/*     */     
/* 832 */     byte[] data = read(ADDRESS_MOTOR_GEAR_RATIO_MAP[motor], 1);
/* 833 */     return data[0] / 127.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setDifferentialControlLoopCoefficients(int motor, DifferentialControlLoopCoefficients pid)
/*     */   {
/* 839 */     validateMotor(motor);
/*     */     
/* 841 */     if (pid.p > 255.0D)
/*     */     {
/* 843 */       pid.p = 255.0D;
/*     */     }
/*     */     
/* 846 */     if (pid.i > 255.0D)
/*     */     {
/* 848 */       pid.i = 255.0D;
/*     */     }
/*     */     
/* 851 */     if (pid.d > 255.0D)
/*     */     {
/* 853 */       pid.d = 255.0D;
/*     */     }
/*     */     
/* 856 */     write(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], new byte[] { (byte)(int)pid.p, (byte)(int)pid.i, (byte)(int)pid.d });
/*     */   }
/*     */   
/*     */ 
/*     */   public DifferentialControlLoopCoefficients getDifferentialControlLoopCoefficients(int motor)
/*     */   {
/* 862 */     validateMotor(motor);
/*     */     
/* 864 */     DifferentialControlLoopCoefficients pid = new DifferentialControlLoopCoefficients();
/* 865 */     byte[] data = read(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], 3);
/* 866 */     pid.p = data[0];
/* 867 */     pid.i = data[1];
/* 868 */     pid.d = data[2];
/*     */     
/* 870 */     return pid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte modeToByte(DcMotor.RunMode mode)
/*     */   {
/* 879 */     switch (mode.migrate()) {
/*     */     case RUN_USING_ENCODER: 
/* 881 */       return 1;
/* 882 */     case RUN_WITHOUT_ENCODER:  return 0;
/* 883 */     case RUN_TO_POSITION:  return 2;
/* 884 */     case STOP_AND_RESET_ENCODER:  return 3;
/*     */     }
/* 886 */     return 1;
/*     */   }
/*     */   
/*     */   public static DcMotor.RunMode modeFromByte(byte flag)
/*     */   {
/* 891 */     switch (flag & 0x3) {
/*     */     case 1: 
/* 893 */       return DcMotor.RunMode.RUN_USING_ENCODER;
/* 894 */     case 0:  return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
/* 895 */     case 2:  return DcMotor.RunMode.RUN_TO_POSITION;
/* 896 */     case 3:  return DcMotor.RunMode.STOP_AND_RESET_ENCODER;
/*     */     }
/* 898 */     return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
/*     */   }
/*     */   
/*     */   private void floatHardware()
/*     */   {
/* 903 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 905 */       setMotorPowerFloat(motor);
/*     */     }
/*     */   }
/*     */   
/*     */   private void runWithoutEncoders()
/*     */   {
/* 911 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 913 */       setMotorMode(motor, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setDifferentialControlLoopCoefficientsToDefault()
/*     */   {
/* 919 */     for (int motor = 1; motor <= 2; motor++)
/*     */     {
/* 921 */       write(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], new byte[] { Byte.MIN_VALUE, 64, -72 });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void validateMotor(int motor)
/*     */   {
/* 928 */     if ((motor < 1) || (motor > 2))
/*     */     {
/* 930 */       throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are %d..%d", new Object[] { Integer.valueOf(motor), Integer.valueOf(1), Integer.valueOf(2) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int validateEncoderTicksPerSecond(int motor, int encoderTicksPerSecond)
/*     */   {
/* 939 */     encoderTicksPerSecond = Range.clip(encoderTicksPerSecond, 1, getDefaultMaxMotorSpeed(motor));
/* 940 */     return encoderTicksPerSecond;
/*     */   }
/*     */   
/*     */   private void validateApiMotorPower(double power)
/*     */   {
/* 945 */     if ((-1.0D > power) || (power > 1.0D))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 950 */       throw new IllegalArgumentException(String.format("illegal motor power %f; must be in interval [%f,%f]", new Object[] { Double.valueOf(power), Double.valueOf(-1.0D), Double.valueOf(1.0D) }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbDcMotorController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */