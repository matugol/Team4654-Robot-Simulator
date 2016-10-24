/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import com.qualcomm.robotcore.hardware.ServoController;
/*    */ import com.qualcomm.robotcore.hardware.ServoController.PwmStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ServoControllerAccess
/*    */   extends HardwareAccess<ServoController>
/*    */ {
/*    */   private final ServoController servoController;
/*    */   
/*    */   ServoControllerAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<ServoController> deviceMapping)
/*    */   {
/* 21 */     super(hardwareItem, deviceMapping);
/* 22 */     this.servoController = ((ServoController)this.hardwareDevice);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public String getPwmStatus()
/*    */   {
/* 28 */     if (this.servoController != null) {
/* 29 */       ServoController.PwmStatus pwmStatus = this.servoController.getPwmStatus();
/* 30 */       if (pwmStatus != null) {
/* 31 */         return pwmStatus.toString();
/*    */       }
/*    */     }
/* 34 */     return "";
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void pwmEnable()
/*    */   {
/* 40 */     if (this.servoController != null) {
/* 41 */       this.servoController.pwmEnable();
/*    */     }
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void pwmDisable()
/*    */   {
/* 48 */     if (this.servoController != null) {
/* 49 */       this.servoController.pwmDisable();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\ServoControllerAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */