/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.CRServo;
/*    */ import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CRServoAccess
/*    */   extends HardwareAccess<CRServo>
/*    */ {
/*    */   private final CRServo crServo;
/*    */   
/*    */   CRServoAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<CRServo> deviceMapping)
/*    */   {
/* 21 */     super(hardwareItem, deviceMapping);
/* 22 */     this.crServo = ((CRServo)this.hardwareDevice);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @JavascriptInterface
/*    */   public void setDirection(String directionString)
/*    */   {
/* 30 */     if (this.crServo != null)
/*    */     {
/*    */       try {
/* 33 */         direction = DcMotorSimple.Direction.valueOf(directionString.toUpperCase(Locale.ENGLISH));
/*    */       } catch (Exception e) { DcMotorSimple.Direction direction;
/*    */         return; }
/*    */       DcMotorSimple.Direction direction;
/* 37 */       this.crServo.setDirection(direction);
/*    */     }
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public String getDirection()
/*    */   {
/* 44 */     if (this.crServo != null) {
/* 45 */       DcMotorSimple.Direction direction = this.crServo.getDirection();
/* 46 */       if (direction != null) {
/* 47 */         return direction.toString();
/*    */       }
/*    */     }
/* 50 */     return "";
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void setPower(double power)
/*    */   {
/* 56 */     if (this.crServo != null) {
/* 57 */       this.crServo.setPower(power);
/*    */     }
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getPower()
/*    */   {
/* 64 */     if (this.crServo != null) {
/* 65 */       return this.crServo.getPower();
/*    */     }
/* 67 */     return 0.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\CRServoAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */