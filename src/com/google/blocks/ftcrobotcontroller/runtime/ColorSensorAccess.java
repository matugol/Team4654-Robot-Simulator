/*     */ package com.google.blocks.ftcrobotcontroller.runtime;
/*     */ 
/*     */ import android.webkit.JavascriptInterface;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*     */ import com.qualcomm.robotcore.hardware.ColorSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ColorSensorAccess
/*     */   extends HardwareAccess<ColorSensor>
/*     */ {
/*     */   private final ColorSensor colorSensor;
/*     */   
/*     */   ColorSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<ColorSensor> deviceMapping)
/*     */   {
/*  21 */     super(hardwareItem, deviceMapping);
/*  22 */     this.colorSensor = ((ColorSensor)this.hardwareDevice);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JavascriptInterface
/*     */   public int getRed()
/*     */   {
/*  30 */     if (this.colorSensor != null) {
/*  31 */       return this.colorSensor.red();
/*     */     }
/*  33 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getGreen()
/*     */   {
/*  39 */     if (this.colorSensor != null) {
/*  40 */       return this.colorSensor.green();
/*     */     }
/*  42 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getBlue()
/*     */   {
/*  48 */     if (this.colorSensor != null) {
/*  49 */       return this.colorSensor.blue();
/*     */     }
/*  51 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getAlpha()
/*     */   {
/*  57 */     if (this.colorSensor != null) {
/*  58 */       return this.colorSensor.alpha();
/*     */     }
/*  60 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getArgb()
/*     */   {
/*  66 */     if (this.colorSensor != null) {
/*  67 */       return this.colorSensor.argb();
/*     */     }
/*  69 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void enableLed(boolean enable)
/*     */   {
/*  75 */     if (this.colorSensor != null) {
/*     */       try {
/*  77 */         this.colorSensor.enableLed(enable);
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setI2cAddress7Bit(int i2cAddr7Bit)
/*     */   {
/*  86 */     if (this.colorSensor != null) {
/*     */       try {
/*  88 */         this.colorSensor.setI2cAddress(I2cAddr.create7bit(i2cAddr7Bit));
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getI2cAddress7Bit()
/*     */   {
/*  97 */     if (this.colorSensor != null) {
/*  98 */       I2cAddr i2cAddr = this.colorSensor.getI2cAddress();
/*  99 */       if (i2cAddr != null) {
/* 100 */         return i2cAddr.get7Bit();
/*     */       }
/*     */     }
/* 103 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setI2cAddress8Bit(int i2cAddr8Bit)
/*     */   {
/* 109 */     if (this.colorSensor != null) {
/*     */       try {
/* 111 */         this.colorSensor.setI2cAddress(I2cAddr.create8bit(i2cAddr8Bit));
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getI2cAddress8Bit()
/*     */   {
/* 120 */     if (this.colorSensor != null) {
/* 121 */       I2cAddr i2cAddr = this.colorSensor.getI2cAddress();
/* 122 */       if (i2cAddr != null) {
/* 123 */         return i2cAddr.get8Bit();
/*     */       }
/*     */     }
/* 126 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\ColorSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */