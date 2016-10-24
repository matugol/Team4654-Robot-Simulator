/*     */ package com.qualcomm.hardware.logitech;
/*     */ 
/*     */ import android.os.Build.VERSION;
/*     */ import android.view.MotionEvent;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad.GamepadCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogitechGamepadF310
/*     */   extends Gamepad
/*     */ {
/*     */   public LogitechGamepadF310()
/*     */   {
/*  46 */     this(null);
/*     */   }
/*     */   
/*     */   public LogitechGamepadF310(Gamepad.GamepadCallback callback) {
/*  50 */     super(callback);
/*     */     
/*     */ 
/*  53 */     this.joystickDeadzone = 0.06F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update(MotionEvent event)
/*     */   {
/*  64 */     this.id = event.getDeviceId();
/*  65 */     this.timestamp = event.getEventTime();
/*     */     
/*  67 */     if (Build.VERSION.SDK_INT < 21)
/*     */     {
/*  69 */       update_4_dot_ex(event);
/*     */     }
/*     */     else {
/*  72 */       update_5_dot_oh(event);
/*     */     }
/*     */   }
/*     */   
/*     */   private void update_4_dot_ex(MotionEvent event)
/*     */   {
/*  78 */     this.left_stick_x = cleanMotionValues(event.getAxisValue(0));
/*  79 */     this.left_stick_y = cleanMotionValues(event.getAxisValue(1));
/*  80 */     this.right_stick_x = cleanMotionValues(event.getAxisValue(12));
/*  81 */     this.right_stick_y = cleanMotionValues(event.getAxisValue(13));
/*  82 */     this.left_trigger = ((event.getAxisValue(11) + 1.0F) / 2.0F);
/*  83 */     this.right_trigger = ((event.getAxisValue(14) + 1.0F) / 2.0F);
/*  84 */     this.dpad_down = (event.getAxisValue(16) > this.dpadThreshold);
/*  85 */     this.dpad_up = (event.getAxisValue(16) < -this.dpadThreshold);
/*  86 */     this.dpad_right = (event.getAxisValue(15) > this.dpadThreshold);
/*  87 */     this.dpad_left = (event.getAxisValue(15) < -this.dpadThreshold);
/*     */     
/*  89 */     callCallback();
/*     */   }
/*     */   
/*     */   private void update_5_dot_oh(MotionEvent event)
/*     */   {
/*  94 */     this.left_stick_x = cleanMotionValues(event.getAxisValue(0));
/*  95 */     this.left_stick_y = cleanMotionValues(event.getAxisValue(1));
/*     */     
/*     */ 
/*  98 */     this.right_stick_x = cleanMotionValues(event.getAxisValue(11));
/*     */     
/*     */ 
/* 101 */     this.right_stick_y = cleanMotionValues(event.getAxisValue(14));
/*     */     
/*     */ 
/* 104 */     this.left_trigger = event.getAxisValue(23);
/*     */     
/*     */ 
/* 107 */     this.right_trigger = event.getAxisValue(22);
/*     */     
/* 109 */     this.dpad_down = (event.getAxisValue(16) > this.dpadThreshold);
/* 110 */     this.dpad_up = (event.getAxisValue(16) < -this.dpadThreshold);
/* 111 */     this.dpad_right = (event.getAxisValue(15) > this.dpadThreshold);
/* 112 */     this.dpad_left = (event.getAxisValue(15) < -this.dpadThreshold);
/*     */     
/* 114 */     callCallback();
/*     */   }
/*     */   
/*     */   public String type()
/*     */   {
/* 119 */     return "F310";
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\logitech\LogitechGamepadF310.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */