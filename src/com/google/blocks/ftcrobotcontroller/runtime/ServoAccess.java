/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import com.qualcomm.robotcore.hardware.Servo;
/*    */ import com.qualcomm.robotcore.hardware.Servo.Direction;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ServoAccess
/*    */   extends HardwareAccess<Servo>
/*    */ {
/*    */   private final Servo servo;
/*    */   
/*    */   ServoAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<Servo> deviceMapping)
/*    */   {
/* 21 */     super(hardwareItem, deviceMapping);
/* 22 */     this.servo = ((Servo)this.hardwareDevice);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @JavascriptInterface
/*    */   public void setDirection(String directionString)
/*    */   {
/* 30 */     if (this.servo != null)
/*    */     {
/*    */       try {
/* 33 */         direction = Servo.Direction.valueOf(directionString.toUpperCase(Locale.ENGLISH));
/*    */       } catch (Exception e) { Servo.Direction direction;
/*    */         return; }
/*    */       Servo.Direction direction;
/* 37 */       this.servo.setDirection(direction);
/*    */     }
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public String getDirection()
/*    */   {
/* 44 */     if (this.servo != null) {
/* 45 */       Servo.Direction direction = this.servo.getDirection();
/* 46 */       if (direction != null) {
/* 47 */         return direction.toString();
/*    */       }
/*    */     }
/* 50 */     return "";
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void setPosition(double position)
/*    */   {
/* 56 */     if (this.servo != null) {
/* 57 */       this.servo.setPosition(position);
/*    */     }
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getPosition()
/*    */   {
/* 64 */     if (this.servo != null) {
/* 65 */       return this.servo.getPosition();
/*    */     }
/* 67 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void scaleRange(double min, double max)
/*    */   {
/* 73 */     if (this.servo != null) {
/* 74 */       this.servo.scaleRange(min, max);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\ServoAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */