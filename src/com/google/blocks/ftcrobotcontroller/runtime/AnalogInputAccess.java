/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.AnalogInput;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class AnalogInputAccess
/*    */   extends HardwareAccess<AnalogInput>
/*    */ {
/*    */   private final AnalogInput analogInput;
/*    */   
/*    */   AnalogInputAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<AnalogInput> deviceMapping)
/*    */   {
/* 21 */     super(hardwareItem, deviceMapping);
/* 22 */     this.analogInput = ((AnalogInput)this.hardwareDevice);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getVoltage()
/*    */   {
/* 28 */     if (this.analogInput != null) {
/* 29 */       return this.analogInput.getVoltage();
/*    */     }
/* 31 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getMaxVoltage()
/*    */   {
/* 37 */     if (this.analogInput != null) {
/* 38 */       return this.analogInput.getMaxVoltage();
/*    */     }
/* 40 */     return 0.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\AnalogInputAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */