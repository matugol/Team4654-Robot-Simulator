/*     */ package com.qualcomm.hardware.matrix;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.DcMotor;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorController;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MatrixDcMotorController
/*     */   implements DcMotorController
/*     */ {
/*     */   private class MotorProperties
/*     */   {
/*     */     public int target;
/*     */     public int position;
/*     */     public byte mode;
/*     */     public boolean floating;
/*     */     public double power;
/*     */     public DcMotor.RunMode runMode;
/*     */     public int maxSpeed;
/*     */     public DcMotor.ZeroPowerBehavior zeroPowerBehavior;
/*     */     
/*     */     public MotorProperties(int motor)
/*     */     {
/*  52 */       this.target = 0;
/*  53 */       this.position = 0;
/*  54 */       this.mode = 0;
/*  55 */       this.power = 0.0D;
/*  56 */       this.floating = true;
/*  57 */       this.runMode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
/*  58 */       this.maxSpeed = MatrixDcMotorController.this.getDefaultMaxMotorSpeed(motor);
/*  59 */       this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   private MotorProperties[] motorCache = { new MotorProperties(1), new MotorProperties(1), new MotorProperties(2), new MotorProperties(3), new MotorProperties(4) };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  80 */   private boolean pendMotorPowerChanges = false;
/*     */   
/*     */ 
/*     */   public static final byte POWER_MAX = 100;
/*     */   
/*     */   public static final byte POWER_MIN = -100;
/*     */   
/*     */   protected static final double apiPowerMin = -1.0D;
/*     */   
/*     */   protected static final double apiPowerMax = 1.0D;
/*     */   
/*     */   private static final byte CHANNEL_MODE_FLAG_SELECT_FLOAT = 0;
/*     */   
/*     */   private static final byte CHANNEL_MODE_FLAG_SELECT_POWER_CONTROL = 1;
/*     */   
/*     */   private static final byte CHANNEL_MODE_FLAG_SELECT_SPEED_CONTROL = 2;
/*     */   
/*     */   private static final byte CHANNEL_MODE_FLAG_SELECT_RTP_CONTROL = 3;
/*     */   
/*     */   private static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 4;
/*     */   
/*     */   private static final byte I2C_DATA_OFFSET = 4;
/*     */   
/*     */   private static final byte MODE_PENDING_BIT = 8;
/*     */   
/*     */   private static final byte SPEED_STOPPED = 0;
/*     */   
/*     */   private static final int MAX_NUM_MOTORS = 4;
/*     */   
/*     */   private static final int NO_TARGET = 0;
/*     */   
/*     */   private static final int BATTERY_UNITS = 40;
/*     */   
/*     */   private static final int POSITION_DATA_SIZE = 4;
/*     */   
/*     */   private static final int TARGET_DATA_SIZE = 4;
/*     */   
/*     */   protected MatrixMasterController master;
/*     */   
/*     */   private int batteryVal;
/*     */   
/*     */ 
/*     */   public MatrixDcMotorController(MatrixMasterController master)
/*     */   {
/* 124 */     this.master = master;
/* 125 */     this.batteryVal = 0;
/*     */     
/* 127 */     master.registerMotorController(this);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */     for (byte i = 0; i < 4; i = (byte)(i + 1)) {
/* 136 */       MatrixI2cTransaction transaction = new MatrixI2cTransaction(i, (byte)0, 0, (byte)0);
/* 137 */       master.queueTransaction(transaction);
/* 138 */       this.motorCache[i].runMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
/* 139 */       this.motorCache[i].floating = true;
/* 140 */       this.motorCache[i].power = 0.0D;
/*     */     }
/*     */     
/* 143 */     this.pendMotorPowerChanges = false;
/*     */   }
/*     */   
/*     */   protected byte runModeToFlagMatrix(DcMotor.RunMode mode)
/*     */   {
/* 148 */     switch (mode.migrate()) {
/*     */     case RUN_USING_ENCODER: 
/* 150 */       return 2;
/*     */     case RUN_WITHOUT_ENCODER: 
/* 152 */       return 1;
/*     */     case RUN_TO_POSITION: 
/* 154 */       return 3;
/*     */     
/*     */ 
/*     */ 
/*     */     case STOP_AND_RESET_ENCODER: 
/* 159 */       return 4;
/*     */     }
/* 161 */     return 4;
/*     */   }
/*     */   
/*     */   protected DcMotor.RunMode flagMatrixToRunMode(byte flag)
/*     */   {
/* 166 */     switch (flag) {
/*     */     case 2: 
/* 168 */       return DcMotor.RunMode.RUN_USING_ENCODER;
/*     */     case 1: 
/* 170 */       return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
/*     */     case 3: 
/* 172 */       return DcMotor.RunMode.RUN_TO_POSITION;
/*     */     
/*     */ 
/*     */ 
/*     */     case 4: 
/* 177 */       return DcMotor.RunMode.STOP_AND_RESET_ENCODER;
/*     */     }
/*     */     
/* 180 */     RobotLog.e("Invalid run mode flag " + flag);
/* 181 */     return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
/*     */   }
/*     */   
/*     */   public boolean isBusy(int motor)
/*     */   {
/* 186 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_MODE);
/* 187 */     this.master.queueTransaction(transaction);
/*     */     
/* 189 */     this.master.waitOnRead();
/*     */     
/* 191 */     if ((this.motorCache[transaction.motor].mode & 0x80) != 0) {
/* 192 */       return true;
/*     */     }
/* 194 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected int getDefaultMaxMotorSpeed(int motor)
/*     */   {
/* 200 */     double encoderTicksPerRevolution = 757.12D;
/* 201 */     double maxNoLoadRpm = 265.0D;
/* 202 */     double maxNoLoadRps = maxNoLoadRpm / 60.0D;
/* 203 */     double maxLoadRps = maxNoLoadRps * 0.8D;
/* 204 */     double maxPidRps = maxLoadRps * 0.8D;
/* 205 */     int degreesPerRevolution = 360;
/* 206 */     double maxPidDegPerS = maxPidRps * degreesPerRevolution;
/* 207 */     return (int)(encoderTicksPerRevolution * maxPidDegPerS / degreesPerRevolution + 0.5D);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMotorMaxSpeed(int motor, int encoderTicksPerSecond)
/*     */   {
/* 213 */     throwIfMotorIsInvalid(motor);
/* 214 */     encoderTicksPerSecond = validateEncoderTicksPerSecond(motor, encoderTicksPerSecond);
/* 215 */     if (this.motorCache[motor].maxSpeed != encoderTicksPerSecond)
/*     */     {
/* 217 */       double power = this.motorCache[motor].runMode.isPIDMode() ? getMotorPower(motor) : 0.0D;
/* 218 */       this.motorCache[motor].maxSpeed = encoderTicksPerSecond;
/* 219 */       if (this.motorCache[motor].runMode.isPIDMode()) {
/* 220 */         setMotorPower(motor, power);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMotorMaxSpeed(int motor)
/*     */   {
/* 228 */     throwIfMotorIsInvalid(motor);
/* 229 */     return this.motorCache[motor].maxSpeed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMotorMode(int motor, DcMotor.RunMode mode)
/*     */   {
/* 240 */     mode = mode.migrate();
/* 241 */     throwIfMotorIsInvalid(motor);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 247 */     if ((!this.motorCache[motor].floating) && (this.motorCache[motor].runMode == mode)) {
/* 248 */       return;
/*     */     }
/*     */     
/* 251 */     byte flag = runModeToFlagMatrix(mode);
/*     */     
/* 253 */     DcMotor.RunMode prevMode = this.motorCache[motor].runMode;
/* 254 */     double prevPower = getMotorPower(motor);
/*     */     
/* 256 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_MODE, flag);
/*     */     
/* 258 */     this.master.queueTransaction(transaction);
/*     */     
/* 260 */     this.motorCache[motor].runMode = mode;
/*     */     
/* 262 */     if ((mode.isPIDMode()) && (!prevMode.isPIDMode())) {
/* 263 */       setMotorPower(motor, prevPower);
/*     */     }
/*     */     
/* 266 */     setFloatingFromMode(motor);
/*     */   }
/*     */   
/*     */   void setFloatingFromMode(int motor)
/*     */   {
/* 271 */     if (this.motorCache[motor].runMode == DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
/* 272 */       this.motorCache[motor].floating = true;
/*     */     } else {
/* 274 */       this.motorCache[motor].floating = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public DcMotor.RunMode getMotorMode(int motor)
/*     */   {
/* 281 */     throwIfMotorIsInvalid(motor);
/*     */     
/* 283 */     return this.motorCache[motor].runMode;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void setMotorZeroPowerBehavior(int motor, DcMotor.ZeroPowerBehavior zeroPowerBehavior)
/*     */   {
/* 289 */     throwIfMotorIsInvalid(motor);
/* 290 */     if (zeroPowerBehavior == DcMotor.ZeroPowerBehavior.UNKNOWN) { throw new IllegalArgumentException("zeroPowerBehavior may not be UNKNOWN");
/*     */     }
/* 292 */     this.motorCache[motor].zeroPowerBehavior = zeroPowerBehavior;
/*     */     
/*     */ 
/* 295 */     if (this.motorCache[motor].power == 0.0D) {
/* 296 */       setMotorPower(motor, this.motorCache[motor].power);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized DcMotor.ZeroPowerBehavior getMotorZeroPowerBehavior(int motor)
/*     */   {
/* 303 */     throwIfMotorIsInvalid(motor);
/* 304 */     return this.motorCache[motor].zeroPowerBehavior;
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
/*     */   protected void setMotorPowerFloat(int motor)
/*     */   {
/* 317 */     throwIfMotorIsInvalid(motor);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 324 */     if (!this.motorCache[motor].floating) {
/* 325 */       MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_MODE, 4);
/* 326 */       this.master.queueTransaction(transaction);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 332 */     this.motorCache[motor].floating = true;
/* 333 */     this.motorCache[motor].power = 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean getMotorPowerFloat(int motor)
/*     */   {
/* 339 */     throwIfMotorIsInvalid(motor);
/*     */     
/* 341 */     return this.motorCache[motor].floating;
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
/*     */   public synchronized void setMotorPower(Set<DcMotor> motors, double power)
/*     */   {
/* 354 */     this.pendMotorPowerChanges = true;
/*     */     try {
/* 356 */       for (DcMotor motor : motors) {
/* 357 */         motor.setPower(power);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 363 */       MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_START, 1);
/* 364 */       this.master.queueTransaction(transaction);
/*     */     } finally {
/* 366 */       this.pendMotorPowerChanges = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void setMotorPower(int motor, double power)
/*     */   {
/* 373 */     throwIfMotorIsInvalid(motor);
/* 374 */     power = Range.clip(power, -1.0D, 1.0D);
/*     */     
/* 376 */     if ((this.motorCache[motor].zeroPowerBehavior == DcMotor.ZeroPowerBehavior.FLOAT) && (power == 0.0D))
/*     */     {
/* 378 */       setMotorPowerFloat(motor);
/*     */     }
/*     */     else
/*     */     {
/* 382 */       if (getMotorMode(motor).isPIDMode()) {
/* 383 */         double defMaxSpeed = getDefaultMaxMotorSpeed(motor);
/* 384 */         power = Math.signum(power) * Range.scale(Math.abs(power), 0.0D, 1.0D, 0.0D, this.motorCache[motor].maxSpeed / defMaxSpeed);
/* 385 */         power = Range.clip(power, -1.0D, 1.0D);
/*     */       }
/*     */       
/* 388 */       byte p = (byte)(int)(power * 100.0D);
/* 389 */       byte bit = this.pendMotorPowerChanges ? 8 : 0;
/*     */       
/* 391 */       MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)motor, p, this.motorCache[motor].target, (byte)(runModeToFlagMatrix(this.motorCache[motor].runMode) | bit));
/* 392 */       this.master.queueTransaction(transaction);
/*     */       
/* 394 */       setFloatingFromMode(motor);
/* 395 */       this.motorCache[motor].power = power;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double getMotorPower(int motor)
/*     */   {
/* 402 */     throwIfMotorIsInvalid(motor);
/*     */     
/* 404 */     double power = this.motorCache[motor].power;
/* 405 */     if (getMotorMode(motor).isPIDMode())
/*     */     {
/* 407 */       double defMaxSpeed = getDefaultMaxMotorSpeed(motor);
/* 408 */       power = Math.signum(power) * Range.scale(Math.abs(power), 0.0D, this.motorCache[motor].maxSpeed / defMaxSpeed, 0.0D, 1.0D);
/* 409 */       power = Range.clip(power, -1.0D, 1.0D);
/*     */     }
/* 411 */     return power;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMotorTargetPosition(int motor, int position)
/*     */   {
/* 417 */     throwIfMotorIsInvalid(motor);
/*     */     
/* 419 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_TARGET, position);
/*     */     
/* 421 */     this.master.queueTransaction(transaction);
/* 422 */     this.motorCache[motor].target = position;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMotorTargetPosition(int motor)
/*     */   {
/* 428 */     throwIfMotorIsInvalid(motor);
/*     */     
/* 430 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_TARGET);
/*     */     
/*     */ 
/* 433 */     if (this.master.queueTransaction(transaction)) {
/* 434 */       this.master.waitOnRead();
/*     */     }
/*     */     
/* 437 */     return this.motorCache[motor].target;
/*     */   }
/*     */   
/*     */   public int getMotorCurrentPosition(int motor)
/*     */   {
/* 442 */     throwIfMotorIsInvalid(motor);
/*     */     
/* 444 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_POSITION);
/*     */     
/*     */ 
/* 447 */     if (this.master.queueTransaction(transaction)) {
/* 448 */       this.master.waitOnRead();
/*     */     }
/*     */     
/* 451 */     return this.motorCache[motor].position;
/*     */   }
/*     */   
/*     */   public int getBattery()
/*     */   {
/* 456 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_BATTERY);
/*     */     
/*     */ 
/* 459 */     if (this.master.queueTransaction(transaction)) {
/* 460 */       this.master.waitOnRead();
/*     */     }
/*     */     
/* 463 */     return this.batteryVal;
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 468 */     return HardwareDevice.Manufacturer.Matrix;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 474 */     return "Matrix Motor Controller";
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 480 */     return this.master.getConnectionInfo();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getVersion()
/*     */   {
/* 486 */     return 1;
/*     */   }
/*     */   
/*     */   void brakeAllAtZero()
/*     */   {
/* 491 */     for (int motor = 0; motor < 4; motor++)
/*     */     {
/* 493 */       this.motorCache[motor].zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode()
/*     */   {
/* 501 */     brakeAllAtZero();
/*     */   }
/*     */   
/*     */ 
/*     */   public void close()
/*     */   {
/* 507 */     setMotorPowerFloat(1);
/* 508 */     setMotorPowerFloat(2);
/* 509 */     setMotorPowerFloat(3);
/* 510 */     setMotorPowerFloat(4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleReadBattery(byte[] buffer)
/*     */   {
/* 521 */     this.batteryVal = (40 * TypeConversion.unsignedByteToInt(buffer[4]));
/* 522 */     RobotLog.v("Battery voltage: " + this.batteryVal + "mV");
/*     */   }
/*     */   
/*     */   public void handleReadPosition(MatrixI2cTransaction transaction, byte[] buffer)
/*     */   {
/* 527 */     this.motorCache[transaction.motor].position = TypeConversion.byteArrayToInt(Arrays.copyOfRange(buffer, 4, 8));
/* 528 */     RobotLog.v("Position motor: " + transaction.motor + " " + this.motorCache[transaction.motor].position);
/*     */   }
/*     */   
/*     */   public void handleReadTargetPosition(MatrixI2cTransaction transaction, byte[] buffer)
/*     */   {
/* 533 */     this.motorCache[transaction.motor].target = TypeConversion.byteArrayToInt(Arrays.copyOfRange(buffer, 4, 8));
/* 534 */     RobotLog.v("Target motor: " + transaction.motor + " " + this.motorCache[transaction.motor].target);
/*     */   }
/*     */   
/*     */   public void handleReadMode(MatrixI2cTransaction transaction, byte[] buffer)
/*     */   {
/* 539 */     this.motorCache[transaction.motor].mode = buffer[4];
/* 540 */     RobotLog.v("Mode: " + this.motorCache[transaction.motor].mode);
/*     */   }
/*     */   
/*     */   private void throwIfMotorIsInvalid(int motor) {
/* 544 */     if ((motor < 1) || (motor > 4)) {
/* 545 */       throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", new Object[] { Integer.valueOf(motor), Integer.valueOf(4) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int validateEncoderTicksPerSecond(int motor, int encoderTicksPerSecond)
/*     */   {
/* 554 */     encoderTicksPerSecond = Range.clip(encoderTicksPerSecond, 1, getDefaultMaxMotorSpeed(motor));
/* 555 */     return encoderTicksPerSecond;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\matrix\MatrixDcMotorController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */