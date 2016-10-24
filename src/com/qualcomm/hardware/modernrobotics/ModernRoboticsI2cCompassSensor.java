/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.CompassSensor;
/*     */ import com.qualcomm.robotcore.hardware.CompassSensor.CompassMode;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddrConfig;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadMode;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadWindow;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple.TimestampedData;
/*     */ import com.qualcomm.robotcore.hardware.configuration.I2cSensor;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.FirmwareVersion;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Locale;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @I2cSensor(name="MR Compass Sensor", description="a MR compass sensor", xmlTag="ModernRoboticsI2cCompassSensor")
/*     */ public class ModernRoboticsI2cCompassSensor
/*     */   extends I2cDeviceSynchDevice<I2cDeviceSynch>
/*     */   implements CompassSensor, I2cAddrConfig
/*     */ {
/*  69 */   public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(36);
/*     */   
/*     */   public static enum Register
/*     */   {
/*  73 */     FIRST(0), 
/*  74 */     FIRMWARE_REV(0), 
/*  75 */     MANUFACTURE_CODE(1), 
/*  76 */     SENSOR_ID(2), 
/*  77 */     COMMAND(3), 
/*  78 */     HEADING(4), 
/*  79 */     ACCELX(6), 
/*  80 */     ACCELY(8), 
/*  81 */     ACCELZ(10), 
/*  82 */     MAGX(12), 
/*  83 */     MAGY(14), 
/*  84 */     MAGZ(16), 
/*  85 */     LAST(17), 
/*  86 */     ACCELX_OFFSET(18), 
/*  87 */     ACCELY_OFFSET(20), 
/*  88 */     ACCELZ_OFFSET(22), 
/*  89 */     MAGX_OFFSET(24), 
/*  90 */     MAGY_OFFSET(26), 
/*  91 */     MAGZ_OFFSET(28), 
/*  92 */     MAG_TILT_COEFF(30), 
/*  93 */     ACCEL_SCALE_COEFF(32), 
/*  94 */     MAG_SCALE_COEFF_X(34), 
/*  95 */     MAG_SCALE_COEFF_Y(36), 
/*  96 */     UNKNOWN(-1);
/*     */     
/*     */ 
/*  99 */     private Register(int bVal) { this.bVal = ((byte)bVal); }
/*     */     
/*     */     public byte bVal;
/*     */   }
/*     */   
/* 104 */   public static enum Command { NORMAL(0), 
/* 105 */     CALIBRATE_IRON(67), 
/* 106 */     ACCEL_NULL_X(88), 
/* 107 */     ACCEL_NULL_Y(89), 
/* 108 */     ACCEL_NULL_Z(90), 
/* 109 */     ACCEL_GAIN_ADJUST(71), 
/* 110 */     MEASURE_TILT_UP(85), 
/* 111 */     MEASURE_TILT_DOWN(68), 
/* 112 */     WRITE_EEPROM(87), 
/* 113 */     CALIBRATION_FAILED(70), 
/* 114 */     UNKNOWN(-1);
/*     */     
/*     */ 
/* 117 */     private Command(int bVal) { this.bVal = ((byte)bVal); }
/*     */     
/* 119 */     public static Command fromByte(byte b) { for (Command command : ) {
/* 120 */         if (command.bVal == b) return command;
/*     */       }
/* 122 */       return UNKNOWN;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public byte bVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModernRoboticsI2cCompassSensor(I2cDeviceSynch deviceClient)
/*     */   {
/* 138 */     super(deviceClient, true);
/*     */     
/* 140 */     setOptimalReadWindow();
/* 141 */     ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C_DEFAULT);
/* 142 */     ((I2cDeviceSynch)this.deviceClient).engage();
/*     */     
/* 144 */     super.registerArmingStateCallback();
/*     */   }
/*     */   
/*     */   public void setOptimalReadWindow()
/*     */   {
/* 149 */     I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(Register.FIRST.bVal, Register.LAST.bVal - Register.FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
/*     */     
/*     */ 
/*     */ 
/* 153 */     ((I2cDeviceSynch)this.deviceClient).setReadWindow(readWindow);
/*     */   }
/*     */   
/*     */ 
/*     */   protected synchronized boolean doInitialize()
/*     */   {
/* 159 */     setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);
/* 160 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 166 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 171 */     RobotUsbDevice.FirmwareVersion firmwareVersion = new RobotUsbDevice.FirmwareVersion(read8(Register.FIRMWARE_REV));
/* 172 */     return String.format(Locale.getDefault(), "Modern Robotics Compass Sensor %s", new Object[] { firmwareVersion });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte read8(Register reg)
/*     */   {
/* 181 */     return ((I2cDeviceSynch)this.deviceClient).read8(reg.bVal);
/*     */   }
/*     */   
/*     */   public void write8(Register reg, byte value)
/*     */   {
/* 186 */     write8(reg, value, false);
/*     */   }
/*     */   
/*     */   public void write8(Register reg, byte value, boolean waitForCompletion) {
/* 190 */     ((I2cDeviceSynch)this.deviceClient).write8(reg.bVal, value, waitForCompletion);
/*     */   }
/*     */   
/*     */   public int readShort(Register reg)
/*     */   {
/* 195 */     return TypeConversion.byteArrayToShort(((I2cDeviceSynch)this.deviceClient).read(reg.bVal, 2), ByteOrder.LITTLE_ENDIAN);
/*     */   }
/*     */   
/*     */   public void writeShort(Register reg, short value)
/*     */   {
/* 200 */     ((I2cDeviceSynch)this.deviceClient).write(reg.bVal, TypeConversion.shortToByteArray(value, ByteOrder.LITTLE_ENDIAN));
/*     */   }
/*     */   
/*     */   public void writeCommand(Command command)
/*     */   {
/* 205 */     write8(Register.COMMAND, command.bVal, true);
/*     */   }
/*     */   
/*     */   public Command readCommand()
/*     */   {
/* 210 */     return Command.fromByte(read8(Register.COMMAND));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Acceleration getAcceleration()
/*     */   {
/* 220 */     I2cDeviceSynchSimple.TimestampedData ts = ((I2cDeviceSynch)this.deviceClient).readTimeStamped(Register.ACCELX.bVal, 6);
/* 221 */     ByteBuffer buffer = ByteBuffer.wrap(ts.data).order(ByteOrder.LITTLE_ENDIAN);
/*     */     
/* 223 */     int mgX = buffer.getShort();
/* 224 */     int mgY = buffer.getShort();
/* 225 */     int mgZ = buffer.getShort();
/* 226 */     double scale = 0.001D;
/* 227 */     return Acceleration.fromGravity(mgX * scale, mgY * scale, mgZ * scale, ts.nanoTime);
/*     */   }
/*     */   
/*     */ 
/*     */   public MagneticFlux getMagneticFlux()
/*     */   {
/* 233 */     I2cDeviceSynchSimple.TimestampedData ts = ((I2cDeviceSynch)this.deviceClient).readTimeStamped(Register.MAGX.bVal, 6);
/* 234 */     ByteBuffer buffer = ByteBuffer.wrap(ts.data).order(ByteOrder.LITTLE_ENDIAN);
/*     */     
/* 236 */     int magX = buffer.getShort();
/* 237 */     int magY = buffer.getShort();
/* 238 */     int magZ = buffer.getShort();
/* 239 */     double scale = 1.0E-4D;
/* 240 */     return new MagneticFlux(magX * scale, magY * scale, magZ * scale, ts.nanoTime);
/*     */   }
/*     */   
/*     */   public double getDirection()
/*     */   {
/* 245 */     return readShort(Register.HEADING);
/*     */   }
/*     */   
/*     */   public String status()
/*     */   {
/* 250 */     return String.format(Locale.getDefault(), "%s on %s", new Object[] { getDeviceName(), getConnectionInfo() });
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
/*     */   public boolean isCalibrating()
/*     */   {
/* 269 */     return readCommand() == Command.CALIBRATE_IRON;
/*     */   }
/*     */   
/*     */   public boolean calibrationFailed()
/*     */   {
/* 274 */     return readCommand() == Command.CALIBRATION_FAILED;
/*     */   }
/*     */   
/*     */   public void setMode(CompassSensor.CompassMode mode)
/*     */   {
/* 279 */     writeCommand(mode == CompassSensor.CompassMode.CALIBRATION_MODE ? Command.CALIBRATE_IRON : Command.NORMAL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setI2cAddress(I2cAddr newAddress)
/*     */   {
/* 290 */     ((I2cDeviceSynch)this.deviceClient).setI2cAddress(newAddress);
/*     */   }
/*     */   
/*     */ 
/*     */   public I2cAddr getI2cAddress()
/*     */   {
/* 296 */     return ((I2cDeviceSynch)this.deviceClient).getI2cAddress();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cCompassSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */