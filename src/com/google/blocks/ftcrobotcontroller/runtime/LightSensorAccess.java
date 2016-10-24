/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import com.qualcomm.robotcore.hardware.LightSensor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LightSensorAccess
/*    */   extends HardwareAccess<LightSensor>
/*    */ {
/*    */   private final LightSensor lightSensor;
/*    */   
/*    */   LightSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<LightSensor> deviceMapping)
/*    */   {
/* 20 */     super(hardwareItem, deviceMapping);
/* 21 */     this.lightSensor = ((LightSensor)this.hardwareDevice);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @JavascriptInterface
/*    */   public double getLightDetected()
/*    */   {
/* 29 */     if (this.lightSensor != null) {
/* 30 */       return this.lightSensor.getLightDetected();
/*    */     }
/* 32 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getRawLightDetected()
/*    */   {
/* 38 */     if (this.lightSensor != null) {
/* 39 */       return this.lightSensor.getRawLightDetected();
/*    */     }
/* 41 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getRawLightDetectedMax()
/*    */   {
/* 47 */     if (this.lightSensor != null) {
/* 48 */       return this.lightSensor.getRawLightDetectedMax();
/*    */     }
/* 50 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void enableLed(boolean enable)
/*    */   {
/* 56 */     if (this.lightSensor != null) {
/*    */       try {
/* 58 */         this.lightSensor.enableLed(enable);
/*    */       }
/*    */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\LightSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */