/*     */ package com.google.blocks.ftcrobotcontroller.runtime;
/*     */ 
/*     */ import android.webkit.JavascriptInterface;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro.HeadingMode;
/*     */ import com.qualcomm.robotcore.hardware.GyroSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GyroSensorAccess
/*     */   extends HardwareAccess<GyroSensor>
/*     */ {
/*     */   private final GyroSensor gyroSensor;
/*     */   
/*     */   GyroSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<GyroSensor> deviceMapping)
/*     */   {
/*  23 */     super(hardwareItem, deviceMapping);
/*  24 */     this.gyroSensor = ((GyroSensor)this.hardwareDevice);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JavascriptInterface
/*     */   public int getHeading()
/*     */   {
/*  32 */     if (this.gyroSensor != null) {
/*     */       try {
/*  34 */         return this.gyroSensor.getHeading();
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/*  38 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setHeadingMode(String headingModeString)
/*     */   {
/*  44 */     if ((this.gyroSensor != null) && 
/*  45 */       ((this.gyroSensor instanceof ModernRoboticsI2cGyro))) {
/*  46 */       headingModeString = headingModeString.toUpperCase(Locale.ENGLISH);
/*     */       try
/*     */       {
/*  49 */         headingMode = ModernRoboticsI2cGyro.HeadingMode.valueOf(headingModeString);
/*     */       } catch (Exception e) { ModernRoboticsI2cGyro.HeadingMode headingMode;
/*     */         return; }
/*     */       ModernRoboticsI2cGyro.HeadingMode headingMode;
/*  53 */       ((ModernRoboticsI2cGyro)this.gyroSensor).setHeadingMode(headingMode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JavascriptInterface
/*     */   public String getHeadingMode()
/*     */   {
/*  61 */     if ((this.gyroSensor != null) && 
/*  62 */       ((this.gyroSensor instanceof ModernRoboticsI2cGyro))) {
/*  63 */       ModernRoboticsI2cGyro.HeadingMode headingMode = ((ModernRoboticsI2cGyro)this.gyroSensor).getHeadingMode();
/*  64 */       if (headingMode != null) {
/*  65 */         return headingMode.toString();
/*     */       }
/*     */     }
/*     */     
/*  69 */     return "";
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setI2cAddress7Bit(int i2cAddr7Bit)
/*     */   {
/*  75 */     if ((this.gyroSensor != null) && 
/*  76 */       ((this.gyroSensor instanceof ModernRoboticsI2cGyro))) {
/*  77 */       ((ModernRoboticsI2cGyro)this.gyroSensor).setI2cAddress(I2cAddr.create7bit(i2cAddr7Bit));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JavascriptInterface
/*     */   public int getI2cAddress7Bit()
/*     */   {
/*  85 */     if ((this.gyroSensor != null) && 
/*  86 */       ((this.gyroSensor instanceof ModernRoboticsI2cGyro))) {
/*  87 */       I2cAddr i2cAddr = ((ModernRoboticsI2cGyro)this.gyroSensor).getI2cAddress();
/*  88 */       if (i2cAddr != null) {
/*  89 */         return i2cAddr.get7Bit();
/*     */       }
/*     */     }
/*     */     
/*  93 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setI2cAddress8Bit(int i2cAddr8Bit)
/*     */   {
/*  99 */     if ((this.gyroSensor != null) && 
/* 100 */       ((this.gyroSensor instanceof ModernRoboticsI2cGyro))) {
/* 101 */       ((ModernRoboticsI2cGyro)this.gyroSensor).setI2cAddress(I2cAddr.create8bit(i2cAddr8Bit));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JavascriptInterface
/*     */   public int getI2cAddress8Bit()
/*     */   {
/* 109 */     if ((this.gyroSensor != null) && 
/* 110 */       ((this.gyroSensor instanceof ModernRoboticsI2cGyro))) {
/* 111 */       I2cAddr i2cAddr = ((ModernRoboticsI2cGyro)this.gyroSensor).getI2cAddress();
/* 112 */       if (i2cAddr != null) {
/* 113 */         return i2cAddr.get8Bit();
/*     */       }
/*     */     }
/*     */     
/* 117 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getRawX()
/*     */   {
/* 123 */     if (this.gyroSensor != null) {
/*     */       try {
/* 125 */         return this.gyroSensor.rawX();
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/* 129 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getRawY()
/*     */   {
/* 135 */     if (this.gyroSensor != null) {
/*     */       try {
/* 137 */         return this.gyroSensor.rawY();
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/* 141 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getRawZ()
/*     */   {
/* 147 */     if (this.gyroSensor != null) {
/*     */       try {
/* 149 */         return this.gyroSensor.rawZ();
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/* 153 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public double getRotationFraction()
/*     */   {
/* 159 */     if (this.gyroSensor != null) {
/*     */       try {
/* 161 */         return this.gyroSensor.getRotationFraction();
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/* 165 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JavascriptInterface
/*     */   public void calibrate()
/*     */   {
/* 173 */     if (this.gyroSensor != null) {
/* 174 */       this.gyroSensor.calibrate();
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean isCalibrating()
/*     */   {
/* 181 */     if (this.gyroSensor != null) {
/* 182 */       return this.gyroSensor.isCalibrating();
/*     */     }
/* 184 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void resetZAxisIntegrator()
/*     */   {
/* 190 */     if (this.gyroSensor != null) {
/* 191 */       this.gyroSensor.resetZAxisIntegrator();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\GyroSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */