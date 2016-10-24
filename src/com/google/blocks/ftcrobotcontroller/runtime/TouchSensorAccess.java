/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import com.qualcomm.robotcore.hardware.TouchSensor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class TouchSensorAccess
/*    */   extends HardwareAccess<TouchSensor>
/*    */ {
/*    */   private final TouchSensor touchSensor;
/*    */   
/*    */   TouchSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<TouchSensor> deviceMapping)
/*    */   {
/* 19 */     super(hardwareItem, deviceMapping);
/* 20 */     this.touchSensor = ((TouchSensor)this.hardwareDevice);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public boolean getIsPressed()
/*    */   {
/* 26 */     if (this.touchSensor != null) {
/* 27 */       return this.touchSensor.isPressed();
/*    */     }
/* 29 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\TouchSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */