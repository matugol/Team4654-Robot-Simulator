/*     */ package com.qualcomm.hardware.adafruit;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import com.google.gson.Gson;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import java.util.Locale;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Position;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.TempUnit;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface BNO055IMU
/*     */ {
/*     */   public abstract boolean initialize(@NonNull Parameters paramParameters);
/*     */   
/*     */   public abstract boolean initialize();
/*     */   
/*     */   @NonNull
/*     */   public abstract Parameters getParameters();
/*     */   
/*     */   public abstract void close();
/*     */   
/*     */   public abstract Orientation getAngularOrientation();
/*     */   
/*     */   public abstract Acceleration getOverallAcceleration();
/*     */   
/*     */   public abstract AngularVelocity getAngularVelocity();
/*     */   
/*     */   public abstract Acceleration getLinearAcceleration();
/*     */   
/*     */   public abstract Acceleration getGravity();
/*     */   
/*     */   public abstract Temperature getTemperature();
/*     */   
/*     */   public abstract MagneticFlux getMagneticFieldStrength();
/*     */   
/*     */   public abstract Quaternion getQuaternionOrientation();
/*     */   
/*     */   public abstract Position getPosition();
/*     */   
/*     */   public abstract Velocity getVelocity();
/*     */   
/*     */   public abstract Acceleration getAcceleration();
/*     */   
/*     */   public abstract void startAccelerationIntegration(Position paramPosition, Velocity paramVelocity, int paramInt);
/*     */   
/*     */   public abstract void stopAccelerationIntegration();
/*     */   
/*     */   public abstract SystemStatus getSystemStatus();
/*     */   
/*     */   public abstract SystemError getSystemError();
/*     */   
/*     */   public abstract CalibrationStatus getCalibrationStatus();
/*     */   
/*     */   public abstract boolean isSystemCalibrated();
/*     */   
/*     */   public abstract boolean isGyroCalibrated();
/*     */   
/*     */   public abstract boolean isAccelerometerCalibrated();
/*     */   
/*     */   public abstract boolean isMagnetometerCalibrated();
/*     */   
/*     */   public abstract CalibrationData readCalibrationData();
/*     */   
/*     */   public abstract void writeCalibrationData(CalibrationData paramCalibrationData);
/*     */   
/*     */   public abstract byte read8(Register paramRegister);
/*     */   
/*     */   public abstract byte[] read(Register paramRegister, int paramInt);
/*     */   
/*     */   public abstract void write8(Register paramRegister, int paramInt);
/*     */   
/*     */   public abstract void write(Register paramRegister, byte[] paramArrayOfByte);
/*     */   
/*     */   public static class Parameters
/*     */   {
/* 111 */     public I2cAddr i2cAddr = BNO055IMU.I2CADDR_DEFAULT;
/*     */     
/*     */ 
/* 114 */     public BNO055IMU.SensorMode mode = BNO055IMU.SensorMode.IMU;
/*     */     
/*     */ 
/*     */ 
/* 118 */     public boolean useExternalCrystal = true;
/*     */     
/*     */ 
/* 121 */     public BNO055IMU.TempUnit temperatureUnit = BNO055IMU.TempUnit.CELSIUS;
/*     */     
/* 123 */     public BNO055IMU.AngleUnit angleUnit = BNO055IMU.AngleUnit.RADIANS;
/*     */     
/* 125 */     public BNO055IMU.AccelUnit accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
/*     */     
/* 127 */     public BNO055IMU.PitchMode pitchMode = BNO055IMU.PitchMode.ANDROID;
/*     */     
/*     */ 
/* 130 */     public BNO055IMU.AccelRange accelRange = BNO055IMU.AccelRange.G4;
/*     */     
/* 132 */     public BNO055IMU.AccelBandwidth accelBandwidth = BNO055IMU.AccelBandwidth.HZ62_5;
/*     */     
/* 134 */     public BNO055IMU.AccelPowerMode accelPowerMode = BNO055IMU.AccelPowerMode.NORMAL;
/*     */     
/*     */ 
/* 137 */     public BNO055IMU.GyroRange gyroRange = BNO055IMU.GyroRange.DPS2000;
/*     */     
/* 139 */     public BNO055IMU.GyroBandwidth gyroBandwidth = BNO055IMU.GyroBandwidth.HZ32;
/*     */     
/* 141 */     public BNO055IMU.GyroPowerMode gyroPowerMode = BNO055IMU.GyroPowerMode.NORMAL;
/*     */     
/*     */ 
/* 144 */     public BNO055IMU.MagRate magRate = BNO055IMU.MagRate.HZ10;
/*     */     
/* 146 */     public BNO055IMU.MagOpMode magOpMode = BNO055IMU.MagOpMode.REGULAR;
/*     */     
/* 148 */     public BNO055IMU.MagPowerMode magPowerMode = BNO055IMU.MagPowerMode.NORMAL;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 153 */     public BNO055IMU.CalibrationData calibrationData = null;
/* 154 */     public String calibrationDataFile = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 159 */     public BNO055IMU.AccelerationIntegrator accelerationIntegrationAlgorithm = null;
/*     */     
/*     */ 
/* 162 */     public boolean loggingEnabled = false;
/*     */     
/* 164 */     public String loggingTag = "AdaFruitIMU";
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
/*     */   public static abstract interface AccelerationIntegrator
/*     */   {
/*     */     public abstract void initialize(BNO055IMU.Parameters paramParameters, Position paramPosition, Velocity paramVelocity);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Position getPosition();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Velocity getVelocity();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Acceleration getAcceleration();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract void update(Acceleration paramAcceleration);
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
/*     */   public static class CalibrationData
/*     */   {
/*     */     public short dxAccel;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short dyAccel;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short dzAccel;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short dxMag;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short dyMag;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short dzMag;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short dxGyro;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short dyGyro;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short dzGyro;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short radiusAccel;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public short radiusMag;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String serialize()
/*     */     {
/* 420 */       return new Gson().toJson(this);
/*     */     }
/*     */     
/* 423 */     public static CalibrationData deserialize(String data) { return (CalibrationData)new Gson().fromJson(data, CalibrationData.class); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 482 */   public static final I2cAddr I2CADDR_UNSPECIFIED = ;
/* 483 */   public static final I2cAddr I2CADDR_DEFAULT = I2cAddr.create7bit(40);
/* 484 */   public static final I2cAddr I2CADDR_ALTERNATE = I2cAddr.create7bit(41);
/*     */   
/* 486 */   public static enum TempUnit { CELSIUS(0),  FARENHEIT(1); private TempUnit(int i) { this.bVal = ((byte)i); }
/*     */     
/*     */     public final byte bVal;
/* 489 */     TempUnit toTempUnit() { if (this == CELSIUS) {
/* 490 */         return TempUnit.CELSIUS;
/*     */       }
/* 492 */       return TempUnit.FARENHEIT;
/*     */     } }
/*     */   
/* 495 */   public static enum AngleUnit { DEGREES(0),  RADIANS(1); private AngleUnit(int i) { this.bVal = ((byte)i); }
/*     */     
/*     */     public final byte bVal;
/* 498 */     AngleUnit toAngleUnit() { if (this == DEGREES) {
/* 499 */         return AngleUnit.DEGREES;
/*     */       }
/* 501 */       return AngleUnit.RADIANS; } }
/*     */   
/*     */   public static enum AccelUnit { public final byte bVal;
/* 504 */     METERS_PERSEC_PERSEC(0),  MILLI_EARTH_GRAVITY(1); private AccelUnit(int i) { this.bVal = ((byte)i); }
/* 505 */      } public static enum PitchMode { public final byte bVal; WINDOWS(0),  ANDROID(1); private PitchMode(int i) { this.bVal = ((byte)i); }
/*     */      }
/* 507 */   public static enum GyroRange { public final byte bVal; DPS2000(0),  DPS1000(1),  DPS500(2),  DPS250(3),  DPS125(4); private GyroRange(int i) { this.bVal = ((byte)(i << 0)); }
/* 508 */      } public static enum GyroBandwidth { public final byte bVal; HZ523(0),  HZ230(1),  HZ116(2),  HZ47(3),  HZ23(4),  HZ12(5),  HZ64(6),  HZ32(7); private GyroBandwidth(int i) { this.bVal = ((byte)(i << 3)); }
/* 509 */      } public static enum GyroPowerMode { public final byte bVal; NORMAL(0),  FAST(1),  DEEP(2),  SUSPEND(3),  ADVANCED(4); private GyroPowerMode(int i) { this.bVal = ((byte)(i << 0)); }
/* 510 */      } public static enum AccelRange { public final byte bVal; G2(0),  G4(1),  G8(2),  G16(3); private AccelRange(int i) { this.bVal = ((byte)(i << 0)); }
/* 511 */      } public static enum AccelBandwidth { public final byte bVal; HZ7_81(0),  HZ15_63(1),  HZ31_25(2),  HZ62_5(3),  HZ125(4),  HZ250(5),  HZ500(6),  HZ1000(7); private AccelBandwidth(int i) { this.bVal = ((byte)(i << 2)); }
/* 512 */      } public static enum AccelPowerMode { public final byte bVal; NORMAL(0),  SUSPEND(1),  LOW1(2),  STANDBY(3),  LOW2(4),  DEEP(5); private AccelPowerMode(int i) { this.bVal = ((byte)(i << 5)); }
/*     */      }
/* 514 */   public static enum MagRate { public final byte bVal; HZ2(0),  HZ6(1),  HZ8(2),  HZ10(3),  HZ15(4),  HZ20(5),  HZ25(6),  HZ30(7); private MagRate(int i) { this.bVal = ((byte)(i << 0)); }
/* 515 */      } public static enum MagOpMode { LOW(0),  REGULAR(1),  ENHANCED(2),  HIGH(3); private MagOpMode(int i) { this.bVal = ((byte)(i << 3)); }
/* 516 */     public final byte bVal; } public static enum MagPowerMode { NORMAL(0),  SLEEP(1),  SUSPEND(2),  FORCE(3); private MagPowerMode(int i) { this.bVal = ((byte)(i << 5)); }
/*     */     
/*     */     public final byte bVal; }
/* 519 */   public static enum SystemStatus { UNKNOWN(-1),  IDLE(0),  SYSTEM_ERROR(1),  INITIALIZING_PERIPHERALS(2),  SYSTEM_INITIALIZATION(3), 
/* 520 */     SELF_TEST(4),  RUNNING_FUSION(5),  RUNNING_NO_FUSION(6); private SystemStatus(int value) { this.bVal = ((byte)value); }
/*     */     
/*     */     public final byte bVal;
/*     */     public static SystemStatus from(int value) {
/* 524 */       for (SystemStatus systemStatus : )
/*     */       {
/* 526 */         if (systemStatus.bVal == value) return systemStatus;
/*     */       }
/* 528 */       return UNKNOWN;
/*     */     }
/*     */     
/*     */     public String toShortString()
/*     */     {
/* 533 */       switch (BNO055IMU.1.$SwitchMap$com$qualcomm$hardware$adafruit$BNO055IMU$SystemStatus[ordinal()]) {
/*     */       case 1: 
/* 535 */         return "idle";
/* 536 */       case 2:  return "syserr";
/* 537 */       case 3:  return "periph";
/* 538 */       case 4:  return "sysinit";
/* 539 */       case 5:  return "selftest";
/* 540 */       case 6:  return "fusion";
/* 541 */       case 7:  return "running";
/*     */       }
/* 543 */       return "unk";
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum SystemError {
/* 548 */     UNKNOWN(-1),  NO_ERROR(0),  PERIPHERAL_INITIALIZATION_ERROR(1),  SYSTEM_INITIALIZATION_ERROR(2), 
/* 549 */     SELF_TEST_FAILED(3),  REGISTER_MAP_OUT_OF_RANGE(4),  REGISTER_MAP_ADDRESS_OUT_OF_RANGE(5), 
/* 550 */     REGISTER_MAP_WRITE_ERROR(6),  LOW_POWER_MODE_NOT_AVAILABLE(7),  ACCELEROMETER_POWER_MODE_NOT_AVAILABLE(8), 
/* 551 */     FUSION_CONFIGURATION_ERROR(9),  SENSOR_CONFIGURATION_ERROR(10); private SystemError(int value) { this.bVal = ((byte)value); }
/*     */     
/*     */     public final byte bVal;
/*     */     public static SystemError from(int value) {
/* 555 */       for (SystemError systemError : )
/*     */       {
/* 557 */         if (systemError.bVal == value) return systemError;
/*     */       }
/* 559 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CalibrationStatus
/*     */   {
/*     */     public final byte calibrationStatus;
/*     */     
/*     */     public CalibrationStatus(int calibrationStatus)
/*     */     {
/* 569 */       this.calibrationStatus = ((byte)calibrationStatus);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 574 */       StringBuilder result = new StringBuilder();
/* 575 */       result.append(String.format(Locale.getDefault(), "s%d", new Object[] { Integer.valueOf(this.calibrationStatus >> 6 & 0x3) }));
/* 576 */       result.append(" ");
/* 577 */       result.append(String.format(Locale.getDefault(), "g%d", new Object[] { Integer.valueOf(this.calibrationStatus >> 4 & 0x3) }));
/* 578 */       result.append(" ");
/* 579 */       result.append(String.format(Locale.getDefault(), "a%d", new Object[] { Integer.valueOf(this.calibrationStatus >> 2 & 0x3) }));
/* 580 */       result.append(" ");
/* 581 */       result.append(String.format(Locale.getDefault(), "m%d", new Object[] { Integer.valueOf(this.calibrationStatus >> 0 & 0x3) }));
/* 582 */       return result.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum SensorMode
/*     */   {
/* 592 */     CONFIG(0),  ACCONLY(1),  MAGONLY(2), 
/* 593 */     GYRONLY(3),  ACCMAG(4),  ACCGYRO(5), 
/* 594 */     MAGGYRO(6),  AMG(7),  IMU(8), 
/* 595 */     COMPASS(9),  M4G(10),  NDOF_FMC_OFF(11), 
/* 596 */     NDOF(12), 
/* 597 */     DISABLED(-1);
/*     */     
/*     */     private SensorMode(int i) {
/* 600 */       this.bVal = ((byte)i);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isFusionMode()
/*     */     {
/* 606 */       switch (BNO055IMU.1.$SwitchMap$com$qualcomm$hardware$adafruit$BNO055IMU$SensorMode[ordinal()])
/*     */       {
/*     */       case 1: 
/*     */       case 2: 
/*     */       case 3: 
/*     */       case 4: 
/*     */       case 5: 
/* 613 */         return true;
/*     */       }
/* 615 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final byte bVal;
/*     */   }
/*     */   
/*     */ 
/*     */   public static enum Register
/*     */   {
/* 626 */     PAGE_ID(7), 
/*     */     
/* 628 */     CHIP_ID(0), 
/* 629 */     ACC_ID(1), 
/* 630 */     MAG_ID(2), 
/* 631 */     GYR_ID(3), 
/* 632 */     SW_REV_ID_LSB(4), 
/* 633 */     SW_REV_ID_MSB(5), 
/* 634 */     BL_REV_ID(6), 
/*     */     
/*     */ 
/* 637 */     ACC_DATA_X_LSB(8), 
/* 638 */     ACC_DATA_X_MSB(9), 
/* 639 */     ACC_DATA_Y_LSB(10), 
/* 640 */     ACC_DATA_Y_MSB(11), 
/* 641 */     ACC_DATA_Z_LSB(12), 
/* 642 */     ACC_DATA_Z_MSB(13), 
/*     */     
/*     */ 
/* 645 */     MAG_DATA_X_LSB(14), 
/* 646 */     MAG_DATA_X_MSB(15), 
/* 647 */     MAG_DATA_Y_LSB(16), 
/* 648 */     MAG_DATA_Y_MSB(17), 
/* 649 */     MAG_DATA_Z_LSB(18), 
/* 650 */     MAG_DATA_Z_MSB(19), 
/*     */     
/*     */ 
/* 653 */     GYR_DATA_X_LSB(20), 
/* 654 */     GYR_DATA_X_MSB(21), 
/* 655 */     GYR_DATA_Y_LSB(22), 
/* 656 */     GYR_DATA_Y_MSB(23), 
/* 657 */     GYR_DATA_Z_LSB(24), 
/* 658 */     GYR_DATA_Z_MSB(25), 
/*     */     
/*     */ 
/* 661 */     EUL_H_LSB(26), 
/* 662 */     EUL_H_MSB(27), 
/* 663 */     EUL_R_LSB(28), 
/* 664 */     EUL_R_MSB(29), 
/* 665 */     EUL_P_LSB(30), 
/* 666 */     EUL_P_MSB(31), 
/*     */     
/*     */ 
/* 669 */     QUA_DATA_W_LSB(32), 
/* 670 */     QUA_DATA_W_MSB(33), 
/* 671 */     QUA_DATA_X_LSB(34), 
/* 672 */     QUA_DATA_X_MSB(35), 
/* 673 */     QUA_DATA_Y_LSB(36), 
/* 674 */     QUA_DATA_Y_MSB(37), 
/* 675 */     QUA_DATA_Z_LSB(38), 
/* 676 */     QUA_DATA_Z_MSB(39), 
/*     */     
/*     */ 
/* 679 */     LIA_DATA_X_LSB(40), 
/* 680 */     LIA_DATA_X_MSB(41), 
/* 681 */     LIA_DATA_Y_LSB(42), 
/* 682 */     LIA_DATA_Y_MSB(43), 
/* 683 */     LIA_DATA_Z_LSB(44), 
/* 684 */     LIA_DATA_Z_MSB(45), 
/*     */     
/*     */ 
/* 687 */     GRV_DATA_X_LSB(46), 
/* 688 */     GRV_DATA_X_MSB(47), 
/* 689 */     GRV_DATA_Y_LSB(48), 
/* 690 */     GRV_DATA_Y_MSB(49), 
/* 691 */     GRV_DATA_Z_LSB(50), 
/* 692 */     GRV_DATA_Z_MSB(51), 
/*     */     
/*     */ 
/* 695 */     TEMP(52), 
/*     */     
/*     */ 
/* 698 */     CALIB_STAT(53), 
/* 699 */     SELFTEST_RESULT(54), 
/* 700 */     INTR_STAT(55), 
/*     */     
/* 702 */     SYS_CLK_STAT(56), 
/* 703 */     SYS_STAT(57), 
/* 704 */     SYS_ERR(58), 
/*     */     
/*     */ 
/* 707 */     UNIT_SEL(59), 
/* 708 */     DATA_SELECT(60), 
/*     */     
/*     */ 
/* 711 */     OPR_MODE(61), 
/* 712 */     PWR_MODE(62), 
/*     */     
/* 714 */     SYS_TRIGGER(63), 
/* 715 */     TEMP_SOURCE(64), 
/*     */     
/*     */ 
/* 718 */     AXIS_MAP_CONFIG(65), 
/* 719 */     AXIS_MAP_SIGN(66), 
/*     */     
/*     */ 
/* 722 */     SIC_MATRIX_0_LSB(67), 
/* 723 */     SIC_MATRIX_0_MSB(68), 
/* 724 */     SIC_MATRIX_1_LSB(69), 
/* 725 */     SIC_MATRIX_1_MSB(70), 
/* 726 */     SIC_MATRIX_2_LSB(71), 
/* 727 */     SIC_MATRIX_2_MSB(72), 
/* 728 */     SIC_MATRIX_3_LSB(73), 
/* 729 */     SIC_MATRIX_3_MSB(74), 
/* 730 */     SIC_MATRIX_4_LSB(75), 
/* 731 */     SIC_MATRIX_4_MSB(76), 
/* 732 */     SIC_MATRIX_5_LSB(77), 
/* 733 */     SIC_MATRIX_5_MSB(78), 
/* 734 */     SIC_MATRIX_6_LSB(79), 
/* 735 */     SIC_MATRIX_6_MSB(80), 
/* 736 */     SIC_MATRIX_7_LSB(81), 
/* 737 */     SIC_MATRIX_7_MSB(82), 
/* 738 */     SIC_MATRIX_8_LSB(83), 
/* 739 */     SIC_MATRIX_8_MSB(84), 
/*     */     
/*     */ 
/* 742 */     ACC_OFFSET_X_LSB(85), 
/* 743 */     ACC_OFFSET_X_MSB(86), 
/* 744 */     ACC_OFFSET_Y_LSB(87), 
/* 745 */     ACC_OFFSET_Y_MSB(88), 
/* 746 */     ACC_OFFSET_Z_LSB(89), 
/* 747 */     ACC_OFFSET_Z_MSB(90), 
/*     */     
/*     */ 
/* 750 */     MAG_OFFSET_X_LSB(91), 
/* 751 */     MAG_OFFSET_X_MSB(92), 
/* 752 */     MAG_OFFSET_Y_LSB(93), 
/* 753 */     MAG_OFFSET_Y_MSB(94), 
/* 754 */     MAG_OFFSET_Z_LSB(95), 
/* 755 */     MAG_OFFSET_Z_MSB(96), 
/*     */     
/*     */ 
/* 758 */     GYR_OFFSET_X_LSB(97), 
/* 759 */     GYR_OFFSET_X_MSB(98), 
/* 760 */     GYR_OFFSET_Y_LSB(99), 
/* 761 */     GYR_OFFSET_Y_MSB(100), 
/* 762 */     GYR_OFFSET_Z_LSB(101), 
/* 763 */     GYR_OFFSET_Z_MSB(102), 
/*     */     
/*     */ 
/* 766 */     ACC_RADIUS_LSB(103), 
/* 767 */     ACC_RADIUS_MSB(104), 
/* 768 */     MAG_RADIUS_LSB(105), 
/* 769 */     MAG_RADIUS_MSB(106), 
/*     */     
/*     */ 
/* 772 */     ACC_CONFIG(8), 
/* 773 */     MAG_CONFIG(9), 
/* 774 */     GYR_CONFIG_0(10), 
/* 775 */     GYR_CONFIG_1(11), 
/* 776 */     ACC_SLEEP_CONFIG(12), 
/* 777 */     GYR_SLEEP_CONFIG(13), 
/* 778 */     INT_MSK(15), 
/* 779 */     INT_EN(16), 
/* 780 */     ACC_AM_THRES(17), 
/* 781 */     ACC_INT_SETTINGS(18), 
/* 782 */     ACC_HG_DURATION(19), 
/* 783 */     ACC_HG_THRES(20), 
/* 784 */     ACC_NM_THRES(21), 
/* 785 */     ACC_NM_SET(22), 
/* 786 */     GRYO_INT_SETTING(23), 
/* 787 */     GRYO_HR_X_SET(24), 
/* 788 */     GRYO_DUR_X(25), 
/* 789 */     GRYO_HR_Y_SET(26), 
/* 790 */     GRYO_DUR_Y(27), 
/* 791 */     GRYO_HR_Z_SET(28), 
/* 792 */     GRYO_DUR_Z(29), 
/* 793 */     GRYO_AM_THRES(30), 
/* 794 */     GRYO_AM_SET(31), 
/*     */     
/* 796 */     UNIQUE_ID_FIRST(80), 
/* 797 */     UNIQUE_ID_LAST(95);
/*     */     
/*     */ 
/*     */     public final byte bVal;
/*     */     
/*     */     private Register(int i)
/*     */     {
/* 804 */       this.bVal = ((byte)i);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\adafruit\BNO055IMU.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */