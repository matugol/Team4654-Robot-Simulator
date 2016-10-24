/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import com.qualcomm.robotcore.hardware.UltrasonicSensor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class UltrasonicSensorAccess
/*    */   extends HardwareAccess<UltrasonicSensor>
/*    */ {
/*    */   private final UltrasonicSensor ultrasonicSensor;
/*    */   
/*    */   UltrasonicSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<UltrasonicSensor> deviceMapping)
/*    */   {
/* 19 */     super(hardwareItem, deviceMapping);
/* 20 */     this.ultrasonicSensor = ((UltrasonicSensor)this.hardwareDevice);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getUltrasonicLevel()
/*    */   {
/* 26 */     if (this.ultrasonicSensor != null) {
/* 27 */       return this.ultrasonicSensor.getUltrasonicLevel();
/*    */     }
/* 29 */     return 0.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\UltrasonicSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */