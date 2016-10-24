/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.AnalogOutput;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class AnalogOutputAccess
/*    */   extends HardwareAccess<AnalogOutput>
/*    */ {
/*    */   private final AnalogOutput analogOutput;
/*    */   
/*    */   AnalogOutputAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<AnalogOutput> deviceMapping)
/*    */   {
/* 20 */     super(hardwareItem, deviceMapping);
/* 21 */     this.analogOutput = ((AnalogOutput)this.hardwareDevice);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void setAnalogOutputVoltage(int voltage)
/*    */   {
/* 27 */     if (this.analogOutput != null) {
/*    */       try {
/* 29 */         this.analogOutput.setAnalogOutputVoltage(voltage);
/*    */       }
/*    */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*    */     }
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void setAnalogOutputFrequency(int frequency)
/*    */   {
/* 38 */     if (this.analogOutput != null) {
/*    */       try {
/* 40 */         this.analogOutput.setAnalogOutputFrequency(frequency);
/*    */       }
/*    */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*    */     }
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void setAnalogOutputMode(int mode)
/*    */   {
/* 49 */     if (this.analogOutput != null) {
/*    */       try {
/* 51 */         this.analogOutput.setAnalogOutputMode((byte)mode);
/*    */       }
/*    */       catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\AnalogOutputAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */