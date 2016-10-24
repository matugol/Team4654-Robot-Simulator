/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class OpticalDistanceSensorAccess
/*    */   extends HardwareAccess<OpticalDistanceSensor>
/*    */ {
/*    */   private final OpticalDistanceSensor opticalDistanceSensor;
/*    */   
/*    */   OpticalDistanceSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<OpticalDistanceSensor> deviceMapping)
/*    */   {
/* 20 */     super(hardwareItem, deviceMapping);
/* 21 */     this.opticalDistanceSensor = ((OpticalDistanceSensor)this.hardwareDevice);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @JavascriptInterface
/*    */   public double getLightDetected()
/*    */   {
/* 29 */     if (this.opticalDistanceSensor != null) {
/* 30 */       return this.opticalDistanceSensor.getLightDetected();
/*    */     }
/* 32 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getRawLightDetected()
/*    */   {
/* 38 */     if (this.opticalDistanceSensor != null) {
/* 39 */       return this.opticalDistanceSensor.getRawLightDetected();
/*    */     }
/* 41 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getRawLightDetectedMax()
/*    */   {
/* 47 */     if (this.opticalDistanceSensor != null) {
/* 48 */       return this.opticalDistanceSensor.getRawLightDetectedMax();
/*    */     }
/* 50 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void enableLed(boolean enable)
/*    */   {
/* 56 */     if (this.opticalDistanceSensor != null) {
/*    */       try {
/* 58 */         this.opticalDistanceSensor.enableLed(enable);
/*    */       }
/*    */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\OpticalDistanceSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */