/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import com.qualcomm.robotcore.hardware.VoltageSensor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class VoltageSensorAccess
/*    */   extends HardwareAccess<VoltageSensor>
/*    */ {
/*    */   private final VoltageSensor voltageSensor;
/*    */   
/*    */   VoltageSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<VoltageSensor> deviceMapping)
/*    */   {
/* 19 */     super(hardwareItem, deviceMapping);
/* 20 */     this.voltageSensor = ((VoltageSensor)this.hardwareDevice);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getVoltage()
/*    */   {
/* 26 */     if (this.voltageSensor != null) {
/* 27 */       return this.voltageSensor.getVoltage();
/*    */     }
/* 29 */     return 0.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\VoltageSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */