/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.CompassSensor;
/*    */ import com.qualcomm.robotcore.hardware.CompassSensor.CompassMode;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CompassSensorAccess
/*    */   extends HardwareAccess<CompassSensor>
/*    */ {
/*    */   private final CompassSensor compassSensor;
/*    */   
/*    */   CompassSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<CompassSensor> deviceMapping)
/*    */   {
/* 21 */     super(hardwareItem, deviceMapping);
/* 22 */     this.compassSensor = ((CompassSensor)this.hardwareDevice);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getDirection()
/*    */   {
/* 28 */     if (this.compassSensor != null) {
/* 29 */       return this.compassSensor.getDirection();
/*    */     }
/* 31 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public boolean getCalibrationFailed()
/*    */   {
/* 37 */     if (this.compassSensor != null) {
/* 38 */       return this.compassSensor.calibrationFailed();
/*    */     }
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void setMode(String compassModeString)
/*    */   {
/* 46 */     if (this.compassSensor != null)
/*    */     {
/*    */       try {
/* 49 */         compassMode = CompassSensor.CompassMode.valueOf(compassModeString.toUpperCase(Locale.ENGLISH));
/*    */       } catch (Exception e) { CompassSensor.CompassMode compassMode;
/*    */         return; }
/*    */       CompassSensor.CompassMode compassMode;
/* 53 */       this.compassSensor.setMode(compassMode);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\CompassSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */