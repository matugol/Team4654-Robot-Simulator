/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.DistanceSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddrConfig;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadMode;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadWindow;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
/*     */ import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
/*     */ import com.qualcomm.robotcore.hardware.configuration.I2cSensor;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.FirmwareVersion;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.util.Locale;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @I2cSensor(name="MR Range Sensor", description="a MR range sensor", xmlTag="ModernRoboticsI2cRangeSensor")
/*     */ public class ModernRoboticsI2cRangeSensor
/*     */   extends I2cDeviceSynchDevice<I2cDeviceSynch>
/*     */   implements DistanceSensor, OpticalDistanceSensor, I2cAddrConfig
/*     */ {
/*  63 */   public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(40);
/*     */   protected static final double apiLevelMin = 0.0D;
/*     */   protected static final double apiLevelMax = 1.0D;
/*     */   
/*  67 */   public static enum Register { FIRST(0), 
/*  68 */     FIRMWARE_REV(0), 
/*  69 */     MANUFACTURE_CODE(1), 
/*  70 */     SENSOR_ID(2), 
/*  71 */     ULTRASONIC(4), 
/*  72 */     OPTICAL(5), 
/*  73 */     LAST(OPTICAL.bVal), 
/*  74 */     UNKNOWN(-1);
/*     */     
/*     */     private Register(int bVal) {
/*  77 */       this.bVal = ((byte)bVal);
/*     */     }
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
/*  89 */   public double pParam = -1.02001D;
/*  90 */   public double qParam = 0.00311326D;
/*  91 */   public double rParam = -8.39366D;
/*  92 */   public int sParam = 10;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModernRoboticsI2cRangeSensor(I2cDeviceSynch deviceClient)
/*     */   {
/* 100 */     super(deviceClient, true);
/*     */     
/* 102 */     setOptimalReadWindow();
/* 103 */     ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C_DEFAULT);
/* 104 */     ((I2cDeviceSynch)this.deviceClient).engage();
/*     */     
/* 106 */     super.registerArmingStateCallback();
/*     */   }
/*     */   
/*     */   public void setOptimalReadWindow()
/*     */   {
/* 111 */     I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(Register.FIRST.bVal, Register.LAST.bVal - Register.FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
/*     */     
/*     */ 
/*     */ 
/* 115 */     ((I2cDeviceSynch)this.deviceClient).setReadWindow(readWindow);
/*     */   }
/*     */   
/*     */ 
/*     */   protected synchronized boolean doInitialize()
/*     */   {
/* 121 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 127 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 132 */     return String.format(Locale.getDefault(), "Modern Robotics Range Sensor %s", new Object[] { new RobotUsbDevice.FirmwareVersion(read8(Register.FIRMWARE_REV)) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte read8(Register reg)
/*     */   {
/* 142 */     return ((I2cDeviceSynch)this.deviceClient).read8(reg.bVal);
/*     */   }
/*     */   
/*     */   public void write8(Register reg, byte value)
/*     */   {
/* 147 */     write8(reg, value, false);
/*     */   }
/*     */   
/*     */   public void write8(Register reg, byte value, boolean waitForCompletion) {
/* 151 */     ((I2cDeviceSynch)this.deviceClient).write8(reg.bVal, value, waitForCompletion);
/*     */   }
/*     */   
/*     */   protected int readUnsignedByte(Register reg)
/*     */   {
/* 156 */     return TypeConversion.unsignedByteToInt(read8(reg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setI2cAddress(I2cAddr newAddress)
/*     */   {
/* 165 */     ((I2cDeviceSynch)this.deviceClient).setI2cAddress(newAddress);
/*     */   }
/*     */   
/*     */   public I2cAddr getI2cAddress()
/*     */   {
/* 170 */     return ((I2cDeviceSynch)this.deviceClient).getI2cAddress();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int rawUltrasonic()
/*     */   {
/* 179 */     return readUnsignedByte(Register.ULTRASONIC);
/*     */   }
/*     */   
/*     */   public int rawOptical()
/*     */   {
/* 184 */     return readUnsignedByte(Register.OPTICAL);
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
/*     */ 
/*     */ 
/*     */   protected double cmFromOptical(int opticalReading)
/*     */   {
/* 247 */     if (opticalReading < this.sParam) {
/* 248 */       return 0.0D;
/*     */     }
/* 250 */     return this.pParam * Math.log(this.qParam * (this.rParam + opticalReading));
/*     */   }
/*     */   
/*     */   public double cmUltrasonic()
/*     */   {
/* 255 */     return rawUltrasonic();
/*     */   }
/*     */   
/*     */   public double cmOptical()
/*     */   {
/* 260 */     return cmFromOptical(rawOptical());
/*     */   }
/*     */   
/*     */   public double getDistance(DistanceUnit unit)
/*     */   {
/* 265 */     double cmOptical = cmOptical();
/* 266 */     double cm = cmOptical > 0.0D ? cmOptical : cmUltrasonic();
/* 267 */     return unit.fromUnit(DistanceUnit.CM, cm);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getLightDetected()
/*     */   {
/* 276 */     return Range.clip(Range.scale(getRawLightDetected(), 0.0D, getRawLightDetectedMax(), 0.0D, 1.0D), 0.0D, 1.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getRawLightDetected()
/*     */   {
/* 283 */     return rawOptical();
/*     */   }
/*     */   
/*     */   public double getRawLightDetectedMax()
/*     */   {
/* 288 */     return 255.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void enableLed(boolean enable) {}
/*     */   
/*     */ 
/*     */   public String status()
/*     */   {
/* 298 */     return String.format(Locale.getDefault(), "%s on %s", new Object[] { getDeviceName(), getConnectionInfo() });
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cRangeSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */