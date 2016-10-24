/*     */ package com.google.blocks.ftcrobotcontroller.runtime;
/*     */ 
/*     */ import android.webkit.JavascriptInterface;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor.Mode;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class IrSeekerSensorAccess
/*     */   extends HardwareAccess<IrSeekerSensor>
/*     */ {
/*     */   private final IrSeekerSensor irSeekerSensor;
/*     */   
/*     */   IrSeekerSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<IrSeekerSensor> deviceMapping)
/*     */   {
/*  22 */     super(hardwareItem, deviceMapping);
/*  23 */     this.irSeekerSensor = ((IrSeekerSensor)this.hardwareDevice);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JavascriptInterface
/*     */   public void setSignalDetectedThreshold(double threshold)
/*     */   {
/*  31 */     if (this.irSeekerSensor != null) {
/*  32 */       this.irSeekerSensor.setSignalDetectedThreshold(threshold);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public double getSignalDetectedThreshold()
/*     */   {
/*  39 */     if (this.irSeekerSensor != null) {
/*  40 */       return this.irSeekerSensor.getSignalDetectedThreshold();
/*     */     }
/*  42 */     return 0.0D;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setMode(String modeString)
/*     */   {
/*  48 */     if (this.irSeekerSensor != null) {
/*  49 */       modeString = modeString.toUpperCase(Locale.ENGLISH);
/*     */       try
/*     */       {
/*  52 */         mode = IrSeekerSensor.Mode.valueOf(modeString);
/*     */       } catch (Exception e) { IrSeekerSensor.Mode mode;
/*     */         return; }
/*     */       IrSeekerSensor.Mode mode;
/*  56 */       this.irSeekerSensor.setMode(mode);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public String getMode()
/*     */   {
/*  63 */     if (this.irSeekerSensor != null) {
/*  64 */       IrSeekerSensor.Mode mode = this.irSeekerSensor.getMode();
/*  65 */       if (mode != null) {
/*  66 */         return mode.toString();
/*     */       }
/*     */     }
/*  69 */     return "";
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getIsSignalDetected()
/*     */   {
/*  75 */     if (this.irSeekerSensor != null) {
/*  76 */       return this.irSeekerSensor.signalDetected();
/*     */     }
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public double getAngle()
/*     */   {
/*  84 */     if (this.irSeekerSensor != null) {
/*  85 */       return this.irSeekerSensor.getAngle();
/*     */     }
/*  87 */     return 0.0D;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public double getStrength()
/*     */   {
/*  93 */     if (this.irSeekerSensor != null) {
/*  94 */       return this.irSeekerSensor.getStrength();
/*     */     }
/*  96 */     return 0.0D;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setI2cAddress7Bit(int i2cAddr7Bit)
/*     */   {
/* 102 */     if (this.irSeekerSensor != null) {
/*     */       try {
/* 104 */         this.irSeekerSensor.setI2cAddress(I2cAddr.create7bit(i2cAddr7Bit));
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getI2cAddress7Bit()
/*     */   {
/* 113 */     if (this.irSeekerSensor != null) {
/* 114 */       I2cAddr i2cAddr = this.irSeekerSensor.getI2cAddress();
/* 115 */       if (i2cAddr != null) {
/* 116 */         return i2cAddr.get7Bit();
/*     */       }
/*     */     }
/* 119 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setI2cAddress8Bit(int i2cAddr8Bit)
/*     */   {
/* 125 */     if (this.irSeekerSensor != null) {
/*     */       try {
/* 127 */         this.irSeekerSensor.setI2cAddress(I2cAddr.create8bit(i2cAddr8Bit));
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getI2cAddress8Bit()
/*     */   {
/* 136 */     if (this.irSeekerSensor != null) {
/* 137 */       I2cAddr i2cAddr = this.irSeekerSensor.getI2cAddress();
/* 138 */       if (i2cAddr != null) {
/* 139 */         return i2cAddr.get8Bit();
/*     */       }
/*     */     }
/* 142 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\IrSeekerSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */