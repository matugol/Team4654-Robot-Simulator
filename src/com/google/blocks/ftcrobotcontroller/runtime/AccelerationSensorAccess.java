/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.AccelerationSensor;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class AccelerationSensorAccess
/*    */   extends HardwareAccess<AccelerationSensor>
/*    */ {
/*    */   private final AccelerationSensor accelerationSensor;
/*    */   
/*    */   AccelerationSensorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<AccelerationSensor> deviceMapping)
/*    */   {
/* 21 */     super(hardwareItem, deviceMapping);
/* 22 */     this.accelerationSensor = ((AccelerationSensor)this.hardwareDevice);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getXAccel()
/*    */   {
/* 28 */     if (this.accelerationSensor != null) {
/* 29 */       Acceleration acceleration = this.accelerationSensor.getAcceleration();
/* 30 */       if (acceleration != null) {
/* 31 */         return acceleration.xAccel;
/*    */       }
/*    */     }
/* 34 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getYAccel()
/*    */   {
/* 40 */     if (this.accelerationSensor != null) {
/* 41 */       Acceleration acceleration = this.accelerationSensor.getAcceleration();
/* 42 */       if (acceleration != null) {
/* 43 */         return acceleration.yAccel;
/*    */       }
/*    */     }
/* 46 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getZAccel()
/*    */   {
/* 52 */     if (this.accelerationSensor != null) {
/* 53 */       Acceleration acceleration = this.accelerationSensor.getAcceleration();
/* 54 */       if (acceleration != null) {
/* 55 */         return acceleration.zAccel;
/*    */       }
/*    */     }
/* 58 */     return 0.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\AccelerationSensorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */