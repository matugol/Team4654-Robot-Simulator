/*      */ package com.qualcomm.hardware.adafruit;
/*      */ 
/*      */ import android.support.annotation.NonNull;
/*      */ import android.util.Log;
/*      */ import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/*      */ import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier.Notifications;
/*      */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*      */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
/*      */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadMode;
/*      */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadWindow;
/*      */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
/*      */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple.TimestampedData;
/*      */ import com.qualcomm.robotcore.hardware.configuration.I2cSensor;
/*      */ import com.qualcomm.robotcore.util.ElapsedTime;
/*      */ import com.qualcomm.robotcore.util.ReadWriteFile;
/*      */ import com.qualcomm.robotcore.util.ThreadPool;
/*      */ import com.qualcomm.robotcore.util.TypeConversion;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import org.firstinspires.ftc.robotcore.external.Func;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.Position;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
/*      */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @I2cSensor(name="Adafruit IMU", xmlTag="AdafruitBNO055IMU", description="an Adafruit IMU")
/*      */ public class AdafruitBNO055IMU
/*      */   extends I2cDeviceSynchDevice<I2cDeviceSynch>
/*      */   implements BNO055IMU, OpModeManagerNotifier.Notifications
/*      */ {
/*      */   protected BNO055IMU.Parameters parameters;
/*      */   protected BNO055IMU.SensorMode currentMode;
/*   86 */   protected final Object dataLock = new Object();
/*      */   
/*      */   protected BNO055IMU.AccelerationIntegrator accelerationAlgorithm;
/*   89 */   protected final Object startStopLock = new Object();
/*      */   
/*      */ 
/*      */   protected ExecutorService accelerationMananger;
/*      */   
/*      */ 
/*      */   protected static final int msAwaitChipId = 2000;
/*      */   
/*      */ 
/*      */   protected static final int msAwaitSelfTest = 2000;
/*      */   
/*  100 */   protected static final I2cDeviceSynch.ReadMode readMode = I2cDeviceSynch.ReadMode.REPEAT;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  113 */   protected static final I2cDeviceSynch.ReadWindow lowerWindow = newWindow(BNO055IMU.Register.CHIP_ID, BNO055IMU.Register.EUL_H_LSB);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  121 */   protected static final I2cDeviceSynch.ReadWindow upperWindow = newWindow(BNO055IMU.Register.EUL_H_LSB, BNO055IMU.Register.TEMP);
/*      */   protected static final int msExtra = 50;
/*      */   static final byte bCHIP_ID_VALUE = -96;
/*      */   
/*  125 */   protected static I2cDeviceSynch.ReadWindow newWindow(BNO055IMU.Register regFirst, BNO055IMU.Register regMax) { return new I2cDeviceSynch.ReadWindow(regFirst.bVal, regMax.bVal - regFirst.bVal, readMode); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AdafruitBNO055IMU(I2cDeviceSynch deviceClient)
/*      */   {
/*  139 */     super(deviceClient, true);
/*      */     
/*  141 */     ((I2cDeviceSynch)this.deviceClient).setReadWindow(lowerWindow);
/*  142 */     ((I2cDeviceSynch)this.deviceClient).engage();
/*      */     
/*  144 */     this.parameters = disabledParameters();
/*  145 */     this.currentMode = null;
/*  146 */     this.accelerationAlgorithm = new NaiveAccelerationIntegrator();
/*  147 */     this.accelerationMananger = null;
/*      */     
/*  149 */     super.registerArmingStateCallback();
/*      */   }
/*      */   
/*      */ 
/*      */   protected BNO055IMU.Parameters disabledParameters()
/*      */   {
/*  155 */     BNO055IMU.Parameters result = new BNO055IMU.Parameters();
/*  156 */     result.mode = BNO055IMU.SensorMode.DISABLED;
/*  157 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetDeviceConfigurationForOpMode()
/*      */   {
/*  169 */     stopAccelerationIntegration();
/*  170 */     this.parameters = disabledParameters();
/*  171 */     super.resetDeviceConfigurationForOpMode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void onOpModePreInit(OpMode opMode) {}
/*      */   
/*      */ 
/*      */   public void onOpModePreStart(OpMode opMode) {}
/*      */   
/*      */ 
/*      */   public void onOpModePostStop(OpMode opMode)
/*      */   {
/*  184 */     stopAccelerationIntegration();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @NonNull
/*      */   public BNO055IMU.Parameters getParameters()
/*      */   {
/*  193 */     return this.parameters;
/*      */   }
/*      */   
/*      */ 
/*      */   protected synchronized boolean doInitialize()
/*      */   {
/*  199 */     return internalInitialize(this.parameters);
/*      */   }
/*      */   
/*      */   public boolean initialize(@NonNull BNO055IMU.Parameters parameters)
/*      */   {
/*  204 */     if (internalInitialize(parameters))
/*      */     {
/*  206 */       this.isInitialized = true;
/*  207 */       return true;
/*      */     }
/*  209 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean internalInitialize(BNO055IMU.Parameters parameters)
/*      */   {
/*  217 */     if ((parameters == null) || (parameters.mode == BNO055IMU.SensorMode.DISABLED)) {
/*  218 */       return false;
/*      */     }
/*      */     
/*  221 */     BNO055IMU.Parameters prevParameters = this.parameters;
/*  222 */     this.parameters = parameters;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  229 */     ((I2cDeviceSynch)this.deviceClient).setI2cAddr(parameters.i2cAddr);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  236 */     BNO055IMU.SystemStatus expectedStatus = parameters.mode.isFusionMode() ? BNO055IMU.SystemStatus.RUNNING_FUSION : BNO055IMU.SystemStatus.RUNNING_NO_FUSION;
/*      */     
/*  238 */     for (int attempt = 0; attempt < 4; attempt++)
/*      */     {
/*  240 */       if (internalInitializeOnce())
/*      */       {
/*      */ 
/*      */ 
/*  244 */         BNO055IMU.SystemStatus status = getSystemStatus();
/*  245 */         if (status == expectedStatus)
/*      */         {
/*  247 */           this.isInitialized = true;
/*  248 */           return true;
/*      */         }
/*  250 */         log_w("retrying IMU initialization: unexpected system status %s; expected %s", new Object[] { status, expectedStatus });
/*      */       }
/*      */       else
/*      */       {
/*  254 */         log_w("retrying IMU initialization: initializeOnce() failed", new Object[0]);
/*      */       }
/*      */     }
/*      */     
/*  258 */     log_e("IMU initialization failed", new Object[0]);
/*  259 */     this.parameters = prevParameters;
/*  260 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean internalInitializeOnce()
/*      */   {
/*  269 */     if (BNO055IMU.SensorMode.CONFIG == this.parameters.mode) {
/*  270 */       throw new IllegalArgumentException("SensorMode.CONFIG illegal for use as initialization mode");
/*      */     }
/*  272 */     ElapsedTime elapsed = new ElapsedTime();
/*  273 */     if (this.parameters.accelerationIntegrationAlgorithm != null)
/*      */     {
/*  275 */       this.accelerationAlgorithm = this.parameters.accelerationIntegrationAlgorithm;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  280 */     write8(BNO055IMU.Register.PAGE_ID, 0);
/*      */     
/*      */ 
/*  283 */     byte chipId = read8(BNO055IMU.Register.CHIP_ID);
/*  284 */     if (chipId != -96)
/*      */     {
/*  286 */       delayExtra(650);
/*  287 */       chipId = read8(BNO055IMU.Register.CHIP_ID);
/*  288 */       if (chipId != -96)
/*      */       {
/*  290 */         log_e("unexpected chip: expected=%d found=%d", new Object[] { Byte.valueOf(-96), Byte.valueOf(chipId) });
/*  291 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  296 */     setSensorMode(BNO055IMU.SensorMode.CONFIG);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  301 */     elapsed.reset();
/*  302 */     write8(BNO055IMU.Register.SYS_TRIGGER, 32);
/*      */     do
/*      */     {
/*  305 */       chipId = read8(BNO055IMU.Register.CHIP_ID);
/*  306 */       if (chipId == -96)
/*      */         break;
/*  308 */       delayExtra(10);
/*  309 */     } while (elapsed.milliseconds() <= 2000.0D);
/*      */     
/*  311 */     log_e("failed to retrieve chip id", new Object[0]);
/*  312 */     return false;
/*      */     
/*      */ 
/*  315 */     delayLoreExtra(50);
/*      */     
/*      */ 
/*  318 */     write8(BNO055IMU.Register.PWR_MODE, POWER_MODE.NORMAL.getValue());
/*  319 */     delayLoreExtra(10);
/*      */     
/*      */ 
/*      */ 
/*  323 */     write8(BNO055IMU.Register.PAGE_ID, 0);
/*      */     
/*      */ 
/*  326 */     int unitsel = this.parameters.pitchMode.bVal << 7 | this.parameters.temperatureUnit.bVal << 4 | this.parameters.angleUnit.bVal << 2 | this.parameters.angleUnit.bVal << 1 | this.parameters.accelUnit.bVal;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  331 */     write8(BNO055IMU.Register.UNIT_SEL, unitsel);
/*      */     
/*      */ 
/*      */ 
/*  335 */     write8(BNO055IMU.Register.SYS_TRIGGER, this.parameters.useExternalCrystal ? 128 : 0);
/*  336 */     delayLoreExtra(50);
/*      */     
/*      */ 
/*  339 */     write8(BNO055IMU.Register.PAGE_ID, 1);
/*      */     
/*      */ 
/*  342 */     write8(BNO055IMU.Register.ACC_CONFIG, this.parameters.accelPowerMode.bVal | this.parameters.accelBandwidth.bVal | this.parameters.accelRange.bVal);
/*  343 */     write8(BNO055IMU.Register.MAG_CONFIG, this.parameters.magPowerMode.bVal | this.parameters.magOpMode.bVal | this.parameters.magRate.bVal);
/*  344 */     write8(BNO055IMU.Register.GYR_CONFIG_0, this.parameters.gyroBandwidth.bVal | this.parameters.gyroRange.bVal);
/*  345 */     write8(BNO055IMU.Register.GYR_CONFIG_1, this.parameters.gyroPowerMode.bVal);
/*      */     
/*      */ 
/*  348 */     write8(BNO055IMU.Register.PAGE_ID, 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  353 */     write8(BNO055IMU.Register.SYS_TRIGGER, read8(BNO055IMU.Register.SYS_TRIGGER) | 0x1);
/*  354 */     elapsed.reset();
/*  355 */     boolean selfTestSuccessful = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  360 */     int successfulResult = 7;
/*  361 */     int successfulResultMask = 7;
/*      */     
/*  363 */     while ((!selfTestSuccessful) && (elapsed.milliseconds() < 2000.0D))
/*      */     {
/*  365 */       selfTestSuccessful = (read8(BNO055IMU.Register.SELFTEST_RESULT) & 0x7) == 7;
/*      */     }
/*  367 */     if (!selfTestSuccessful)
/*      */     {
/*  369 */       int result = read8(BNO055IMU.Register.SELFTEST_RESULT);
/*  370 */       log_e("self test failed: 0x%02x", new Object[] { Integer.valueOf(result) });
/*  371 */       return false;
/*      */     }
/*      */     
/*  374 */     if (this.parameters.calibrationData != null)
/*      */     {
/*  376 */       writeCalibrationData(this.parameters.calibrationData);
/*      */     }
/*  378 */     else if (this.parameters.calibrationDataFile != null) {
/*      */       try
/*      */       {
/*  381 */         File file = AppUtil.getInstance().getSettingsFile(this.parameters.calibrationDataFile);
/*  382 */         String serialized = ReadWriteFile.readFileOrThrow(file);
/*  383 */         BNO055IMU.CalibrationData data = BNO055IMU.CalibrationData.deserialize(serialized);
/*  384 */         writeCalibrationData(data);
/*      */       }
/*      */       catch (IOException localIOException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  393 */     setSensorMode(this.parameters.mode);
/*  394 */     delayLoreExtra(200);
/*      */     
/*  396 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setSensorMode(BNO055IMU.SensorMode mode)
/*      */   {
/*  405 */     this.currentMode = mode;
/*      */     
/*      */ 
/*  408 */     write8(BNO055IMU.Register.OPR_MODE, mode.bVal & 0xF);
/*      */     
/*      */ 
/*  411 */     if (mode == BNO055IMU.SensorMode.CONFIG) {
/*  412 */       delayExtra(19);
/*      */     } else {
/*  414 */       delayExtra(7);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized BNO055IMU.SystemStatus getSystemStatus() {
/*  419 */     byte bVal = read8(BNO055IMU.Register.SYS_STAT);
/*  420 */     return BNO055IMU.SystemStatus.from(bVal);
/*      */   }
/*      */   
/*      */   public synchronized BNO055IMU.SystemError getSystemError()
/*      */   {
/*  425 */     byte bVal = read8(BNO055IMU.Register.SYS_ERR);
/*  426 */     return BNO055IMU.SystemError.from(bVal);
/*      */   }
/*      */   
/*      */   public synchronized BNO055IMU.CalibrationStatus getCalibrationStatus()
/*      */   {
/*  431 */     byte bVal = read8(BNO055IMU.Register.CALIB_STAT);
/*  432 */     return new BNO055IMU.CalibrationStatus(bVal);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void close()
/*      */   {
/*  442 */     stopAccelerationIntegration();
/*  443 */     if (this.deviceClientIsOwned)
/*      */     {
/*  445 */       ((I2cDeviceSynch)this.deviceClient).close();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public String getDeviceName()
/*      */   {
/*  452 */     return "Adafruit BNO055 IMU";
/*      */   }
/*      */   
/*      */ 
/*      */   public HardwareDevice.Manufacturer getManufacturer()
/*      */   {
/*  458 */     return HardwareDevice.Manufacturer.Adafruit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean isSystemCalibrated()
/*      */   {
/*  467 */     byte b = read8(BNO055IMU.Register.CALIB_STAT);
/*  468 */     return (b >> 6 & 0x3) == 3;
/*      */   }
/*      */   
/*      */   public synchronized boolean isGyroCalibrated()
/*      */   {
/*  473 */     byte b = read8(BNO055IMU.Register.CALIB_STAT);
/*  474 */     return (b >> 4 & 0x3) == 3;
/*      */   }
/*      */   
/*      */   public synchronized boolean isAccelerometerCalibrated()
/*      */   {
/*  479 */     byte b = read8(BNO055IMU.Register.CALIB_STAT);
/*  480 */     return (b >> 2 & 0x3) == 3;
/*      */   }
/*      */   
/*      */   public synchronized boolean isMagnetometerCalibrated()
/*      */   {
/*  485 */     byte b = read8(BNO055IMU.Register.CALIB_STAT);
/*  486 */     return (b & 0x3) == 3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BNO055IMU.CalibrationData readCalibrationData()
/*      */   {
/*  502 */     BNO055IMU.SensorMode prevMode = this.currentMode;
/*  503 */     if (prevMode != BNO055IMU.SensorMode.CONFIG) { setSensorMode(BNO055IMU.SensorMode.CONFIG);
/*      */     }
/*  505 */     BNO055IMU.CalibrationData result = new BNO055IMU.CalibrationData();
/*  506 */     result.dxAccel = readShort(BNO055IMU.Register.ACC_OFFSET_X_LSB);
/*  507 */     result.dyAccel = readShort(BNO055IMU.Register.ACC_OFFSET_Y_LSB);
/*  508 */     result.dzAccel = readShort(BNO055IMU.Register.ACC_OFFSET_Z_LSB);
/*  509 */     result.dxMag = readShort(BNO055IMU.Register.MAG_OFFSET_X_LSB);
/*  510 */     result.dyMag = readShort(BNO055IMU.Register.MAG_OFFSET_Y_LSB);
/*  511 */     result.dzMag = readShort(BNO055IMU.Register.MAG_OFFSET_Z_LSB);
/*  512 */     result.dxGyro = readShort(BNO055IMU.Register.GYR_OFFSET_X_LSB);
/*  513 */     result.dyGyro = readShort(BNO055IMU.Register.GYR_OFFSET_Y_LSB);
/*  514 */     result.dzGyro = readShort(BNO055IMU.Register.GYR_OFFSET_Z_LSB);
/*  515 */     result.radiusAccel = readShort(BNO055IMU.Register.ACC_RADIUS_LSB);
/*  516 */     result.radiusMag = readShort(BNO055IMU.Register.MAG_RADIUS_LSB);
/*      */     
/*      */ 
/*  519 */     if (prevMode != BNO055IMU.SensorMode.CONFIG) setSensorMode(prevMode);
/*  520 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeCalibrationData(BNO055IMU.CalibrationData data)
/*      */   {
/*  535 */     BNO055IMU.SensorMode prevMode = this.currentMode;
/*  536 */     if (prevMode != BNO055IMU.SensorMode.CONFIG) { setSensorMode(BNO055IMU.SensorMode.CONFIG);
/*      */     }
/*  538 */     writeShort(BNO055IMU.Register.ACC_OFFSET_X_LSB, data.dxAccel);
/*  539 */     writeShort(BNO055IMU.Register.ACC_OFFSET_Y_LSB, data.dyAccel);
/*  540 */     writeShort(BNO055IMU.Register.ACC_OFFSET_Z_LSB, data.dzAccel);
/*  541 */     writeShort(BNO055IMU.Register.MAG_OFFSET_X_LSB, data.dxMag);
/*  542 */     writeShort(BNO055IMU.Register.MAG_OFFSET_Y_LSB, data.dyMag);
/*  543 */     writeShort(BNO055IMU.Register.MAG_OFFSET_Z_LSB, data.dzMag);
/*  544 */     writeShort(BNO055IMU.Register.GYR_OFFSET_X_LSB, data.dxGyro);
/*  545 */     writeShort(BNO055IMU.Register.GYR_OFFSET_Y_LSB, data.dyGyro);
/*  546 */     writeShort(BNO055IMU.Register.GYR_OFFSET_Z_LSB, data.dzGyro);
/*  547 */     writeShort(BNO055IMU.Register.ACC_RADIUS_LSB, data.radiusAccel);
/*  548 */     writeShort(BNO055IMU.Register.MAG_RADIUS_LSB, data.radiusMag);
/*      */     
/*      */ 
/*  551 */     if (prevMode != BNO055IMU.SensorMode.CONFIG) { setSensorMode(prevMode);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized Temperature getTemperature()
/*      */   {
/*  560 */     byte b = read8(BNO055IMU.Register.TEMP);
/*  561 */     return new Temperature(this.parameters.temperatureUnit.toTempUnit(), b, System.nanoTime());
/*      */   }
/*      */   
/*      */   public synchronized MagneticFlux getMagneticFieldStrength()
/*      */   {
/*  566 */     VectorData vector = getVector(VECTOR.MAGNETOMETER, getFluxScale());
/*  567 */     return new MagneticFlux(vector.next(), vector.next(), vector.next(), vector.data.nanoTime);
/*      */   }
/*      */   
/*      */   public synchronized Acceleration getOverallAcceleration() {
/*  571 */     VectorData vector = getVector(VECTOR.ACCELEROMETER, getMetersAccelerationScale());
/*  572 */     return new Acceleration(DistanceUnit.METER, vector.next(), vector.next(), vector.next(), vector.data.nanoTime);
/*      */   }
/*      */   
/*      */   public synchronized Acceleration getLinearAcceleration() {
/*  576 */     VectorData vector = getVector(VECTOR.LINEARACCEL, getMetersAccelerationScale());
/*  577 */     return new Acceleration(DistanceUnit.METER, vector.next(), vector.next(), vector.next(), vector.data.nanoTime);
/*      */   }
/*      */   
/*      */   public synchronized Acceleration getGravity() {
/*  581 */     VectorData vector = getVector(VECTOR.GRAVITY, getMetersAccelerationScale());
/*  582 */     return new Acceleration(DistanceUnit.METER, vector.next(), vector.next(), vector.next(), vector.data.nanoTime);
/*      */   }
/*      */   
/*      */   public synchronized AngularVelocity getAngularVelocity() {
/*  586 */     VectorData vector = getVector(VECTOR.GYROSCOPE, getAngularScale());
/*  587 */     return new AngularVelocity(AxesReference.INTRINSIC, AxesOrder.ZYX, this.parameters.angleUnit.toAngleUnit(), -vector.next(), vector.next(), vector.next(), vector.data.nanoTime);
/*      */   }
/*      */   
/*      */   public synchronized Orientation getAngularOrientation()
/*      */   {
/*  592 */     VectorData vector = getVector(VECTOR.EULER, getAngularScale());
/*  593 */     return new Orientation(AxesReference.INTRINSIC, AxesOrder.ZYX, this.parameters.angleUnit.toAngleUnit(), -vector.next(), vector.next(), vector.next(), vector.data.nanoTime);
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized Quaternion getQuaternionOrientation()
/*      */   {
/*  599 */     ((I2cDeviceSynch)this.deviceClient).ensureReadWindow(new I2cDeviceSynch.ReadWindow(BNO055IMU.Register.QUA_DATA_W_LSB.bVal, 8, readMode), upperWindow);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  604 */     I2cDeviceSynchSimple.TimestampedData ts = ((I2cDeviceSynch)this.deviceClient).readTimeStamped(BNO055IMU.Register.QUA_DATA_W_LSB.bVal, 8);
/*  605 */     VectorData vector = new VectorData(ts, 16384.0F);
/*  606 */     return new Quaternion(vector.next(), vector.next(), vector.next(), vector.next(), vector.data.nanoTime);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected float getAngularScale()
/*      */   {
/*  615 */     return this.parameters.angleUnit == BNO055IMU.AngleUnit.DEGREES ? 16.0F : 900.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected float getAccelerationScale()
/*      */   {
/*  624 */     return this.parameters.accelUnit == BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC ? 100.0F : 1.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected float getMetersAccelerationScale()
/*      */   {
/*  638 */     float scaleConversionFactor = 100.0F;
/*      */     
/*  640 */     return this.parameters.accelUnit == BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC ? getAccelerationScale() : getAccelerationScale() * scaleConversionFactor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected float getFluxScale()
/*      */   {
/*  652 */     return 1.6E7F;
/*      */   }
/*      */   
/*      */ 
/*      */   protected VectorData getVector(VECTOR vector, float scale)
/*      */   {
/*  658 */     ensureReadWindow(new I2cDeviceSynch.ReadWindow(vector.getValue(), 6, readMode));
/*      */     
/*      */ 
/*  661 */     return new VectorData(((I2cDeviceSynch)this.deviceClient).readTimeStamped(vector.getValue(), 6), scale);
/*      */   }
/*      */   
/*      */   protected static class VectorData
/*      */   {
/*      */     public I2cDeviceSynchSimple.TimestampedData data;
/*      */     public float scale;
/*      */     protected ByteBuffer buffer;
/*      */     
/*      */     public VectorData(I2cDeviceSynchSimple.TimestampedData data, float scale)
/*      */     {
/*  672 */       this.data = data;
/*  673 */       this.scale = scale;
/*  674 */       this.buffer = ByteBuffer.wrap(data.data).order(ByteOrder.LITTLE_ENDIAN);
/*      */     }
/*      */     
/*      */     public float next()
/*      */     {
/*  679 */       return this.buffer.getShort() / this.scale;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Acceleration getAcceleration()
/*      */   {
/*  689 */     synchronized (this.dataLock)
/*      */     {
/*  691 */       Acceleration result = this.accelerationAlgorithm.getAcceleration();
/*  692 */       if (result == null) result = new Acceleration();
/*  693 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   public Velocity getVelocity() {
/*  698 */     synchronized (this.dataLock)
/*      */     {
/*  700 */       Velocity result = this.accelerationAlgorithm.getVelocity();
/*  701 */       if (result == null) result = new Velocity();
/*  702 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   public Position getPosition() {
/*  707 */     synchronized (this.dataLock)
/*      */     {
/*  709 */       Position result = this.accelerationAlgorithm.getPosition();
/*  710 */       if (result == null) result = new Position();
/*  711 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void startAccelerationIntegration(Position initalPosition, Velocity initialVelocity, int msPollInterval)
/*      */   {
/*  718 */     synchronized (this.startStopLock)
/*      */     {
/*      */ 
/*  721 */       stopAccelerationIntegration();
/*      */       
/*      */ 
/*  724 */       this.accelerationAlgorithm.initialize(this.parameters, initalPosition, initialVelocity);
/*      */       
/*      */ 
/*  727 */       this.accelerationMananger = ThreadPool.newSingleThreadExecutor();
/*      */       
/*      */ 
/*  730 */       this.accelerationMananger.execute(new AccelerationManager(msPollInterval));
/*      */     }
/*      */   }
/*      */   
/*      */   public void stopAccelerationIntegration()
/*      */   {
/*  736 */     synchronized (this.startStopLock)
/*      */     {
/*      */ 
/*  739 */       if (this.accelerationMananger != null)
/*      */       {
/*  741 */         this.accelerationMananger.shutdownNow();
/*  742 */         ThreadPool.awaitTerminationOrExitApplication(this.accelerationMananger, 10L, TimeUnit.SECONDS, "IMU acceleration", "unresponsive user acceleration code");
/*  743 */         this.accelerationMananger = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   class AccelerationManager
/*      */     implements Runnable
/*      */   {
/*      */     protected final int msPollInterval;
/*      */     protected static final long nsPerMs = 1000000L;
/*      */     
/*      */     AccelerationManager(int msPollInterval)
/*      */     {
/*  756 */       this.msPollInterval = msPollInterval;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void run()
/*      */     {
/*      */       try
/*      */       {
/*  765 */         while (!AdafruitBNO055IMU.this.isStopRequested())
/*      */         {
/*      */ 
/*  768 */           Acceleration linearAcceleration = AdafruitBNO055IMU.this.getLinearAcceleration();
/*      */           
/*      */ 
/*  771 */           synchronized (AdafruitBNO055IMU.this.dataLock)
/*      */           {
/*  773 */             AdafruitBNO055IMU.this.accelerationAlgorithm.update(linearAcceleration);
/*      */           }
/*      */           
/*      */ 
/*  777 */           if (this.msPollInterval > 0)
/*      */           {
/*  779 */             long msSoFar = (System.nanoTime() - linearAcceleration.acquisitionTime) / 1000000L;
/*  780 */             long msReadFudge = 5L;
/*  781 */             Thread.sleep(Math.max(0L, this.msPollInterval - msSoFar - msReadFudge));
/*      */           }
/*      */           else {
/*  784 */             Thread.yield();
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (InterruptedException|CancellationException e) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   boolean isStopRequested()
/*      */   {
/*  796 */     return Thread.currentThread().isInterrupted();
/*      */   }
/*      */   
/*      */   public synchronized byte read8(BNO055IMU.Register reg)
/*      */   {
/*  801 */     return ((I2cDeviceSynch)this.deviceClient).read8(reg.bVal);
/*      */   }
/*      */   
/*      */   public synchronized byte[] read(BNO055IMU.Register reg, int cb)
/*      */   {
/*  806 */     return ((I2cDeviceSynch)this.deviceClient).read(reg.bVal, cb);
/*      */   }
/*      */   
/*      */   protected short readShort(BNO055IMU.Register reg)
/*      */   {
/*  811 */     byte[] data = read(reg, 2);
/*  812 */     return TypeConversion.byteArrayToShort(data, ByteOrder.LITTLE_ENDIAN);
/*      */   }
/*      */   
/*      */ 
/*      */   public void write8(BNO055IMU.Register reg, int data)
/*      */   {
/*  818 */     ((I2cDeviceSynch)this.deviceClient).write8(reg.bVal, data);
/*      */   }
/*      */   
/*      */   public void write(BNO055IMU.Register reg, byte[] data) {
/*  822 */     ((I2cDeviceSynch)this.deviceClient).write(reg.bVal, data);
/*      */   }
/*      */   
/*      */   protected void writeShort(BNO055IMU.Register reg, short value)
/*      */   {
/*  827 */     byte[] data = TypeConversion.shortToByteArray(value, ByteOrder.LITTLE_ENDIAN);
/*  828 */     write(reg, data);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getLoggingTag()
/*      */   {
/*  837 */     return this.parameters.loggingTag;
/*      */   }
/*      */   
/*      */   protected void log_v(String format, Object... args)
/*      */   {
/*  842 */     if (this.parameters.loggingEnabled)
/*      */     {
/*  844 */       String message = String.format(format, args);
/*  845 */       Log.v(getLoggingTag(), message);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void log_d(String format, Object... args)
/*      */   {
/*  851 */     if (this.parameters.loggingEnabled)
/*      */     {
/*  853 */       String message = String.format(format, args);
/*  854 */       Log.d(getLoggingTag(), message);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void log_w(String format, Object... args)
/*      */   {
/*  860 */     if (this.parameters.loggingEnabled)
/*      */     {
/*  862 */       String message = String.format(format, args);
/*  863 */       Log.w(getLoggingTag(), message);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void log_e(String format, Object... args)
/*      */   {
/*  869 */     if (this.parameters.loggingEnabled)
/*      */     {
/*  871 */       String message = String.format(format, args);
/*  872 */       Log.e(getLoggingTag(), message);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void ensureReadWindow(I2cDeviceSynch.ReadWindow needed)
/*      */   {
/*  879 */     I2cDeviceSynch.ReadWindow windowToSet = upperWindow.containsWithSameMode(needed) ? upperWindow : lowerWindow.containsWithSameMode(needed) ? lowerWindow : needed;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  884 */     ((I2cDeviceSynch)this.deviceClient).ensureReadWindow(needed, windowToSet);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void delayExtra(int ms)
/*      */   {
/*  899 */     delay(ms + 50);
/*      */   }
/*      */   
/*      */   protected void delayLoreExtra(int ms) {
/*  903 */     delayLore(ms + 50);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void delayLore(int ms)
/*      */   {
/*      */     try
/*      */     {
/*  915 */       Thread.sleep(ms);
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*  919 */       Thread.currentThread().interrupt();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void delay(int ms)
/*      */   {
/*      */     try
/*      */     {
/*  932 */       Thread.sleep(ms);
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*  936 */       Thread.currentThread().interrupt();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void enterConfigModeFor(Runnable action)
/*      */   {
/*  942 */     BNO055IMU.SensorMode modePrev = this.currentMode;
/*  943 */     setSensorMode(BNO055IMU.SensorMode.CONFIG);
/*  944 */     delayLoreExtra(25);
/*      */     try
/*      */     {
/*  947 */       action.run();
/*      */     }
/*      */     finally
/*      */     {
/*  951 */       setSensorMode(modePrev);
/*  952 */       delayLoreExtra(20);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected <T> T enterConfigModeFor(Func<T> lambda)
/*      */   {
/*  960 */     BNO055IMU.SensorMode modePrev = this.currentMode;
/*  961 */     setSensorMode(BNO055IMU.SensorMode.CONFIG);
/*  962 */     delayLoreExtra(25);
/*      */     try
/*      */     {
/*  965 */       result = lambda.value();
/*      */     }
/*      */     finally {
/*      */       T result;
/*  969 */       setSensorMode(modePrev);
/*  970 */       delayLoreExtra(20);
/*      */     }
/*      */     T result;
/*  973 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static enum VECTOR
/*      */   {
/*  984 */     ACCELEROMETER(BNO055IMU.Register.ACC_DATA_X_LSB), 
/*  985 */     MAGNETOMETER(BNO055IMU.Register.MAG_DATA_X_LSB), 
/*  986 */     GYROSCOPE(BNO055IMU.Register.GYR_DATA_X_LSB), 
/*  987 */     EULER(BNO055IMU.Register.EUL_H_LSB), 
/*  988 */     LINEARACCEL(BNO055IMU.Register.LIA_DATA_X_LSB), 
/*  989 */     GRAVITY(BNO055IMU.Register.GRV_DATA_X_LSB);
/*      */     
/*      */ 
/*  992 */     private VECTOR(int value) { this.value = ((byte)value); }
/*  993 */     private VECTOR(BNO055IMU.Register register) { this(register.bVal); }
/*  994 */     public byte getValue() { return this.value; }
/*      */     
/*      */     protected byte value;
/*      */   }
/*      */   
/*  999 */   static enum POWER_MODE { NORMAL(0), 
/* 1000 */     LOWPOWER(1), 
/* 1001 */     SUSPEND(2);
/*      */     
/*      */ 
/* 1004 */     private POWER_MODE(int value) { this.value = ((byte)value); }
/* 1005 */     public byte getValue() { return this.value; }
/*      */     
/*      */     protected byte value;
/*      */   }
/*      */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\adafruit\AdafruitBNO055IMU.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */