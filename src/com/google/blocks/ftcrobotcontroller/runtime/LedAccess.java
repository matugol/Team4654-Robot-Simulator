/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import com.qualcomm.robotcore.hardware.LED;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LedAccess
/*    */   extends HardwareAccess<LED>
/*    */ {
/*    */   private final LED led;
/*    */   
/*    */   LedAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<LED> deviceMapping)
/*    */   {
/* 20 */     super(hardwareItem, deviceMapping);
/* 21 */     this.led = ((LED)this.hardwareDevice);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @JavascriptInterface
/*    */   public void enableLed(boolean enable)
/*    */   {
/* 29 */     if (this.led != null) {
/*    */       try {
/* 31 */         this.led.enable(enable);
/*    */       }
/*    */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\LedAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */