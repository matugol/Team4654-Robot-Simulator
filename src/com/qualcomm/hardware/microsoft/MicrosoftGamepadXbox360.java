/*    */ package com.qualcomm.hardware.microsoft;
/*    */ 
/*    */ import com.qualcomm.robotcore.hardware.Gamepad;
/*    */ import com.qualcomm.robotcore.hardware.Gamepad.GamepadCallback;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MicrosoftGamepadXbox360
/*    */   extends Gamepad
/*    */ {
/*    */   public MicrosoftGamepadXbox360()
/*    */   {
/* 38 */     this(null);
/*    */   }
/*    */   
/*    */   public MicrosoftGamepadXbox360(Gamepad.GamepadCallback callback) {
/* 42 */     super(callback);
/*    */     
/*    */ 
/* 45 */     this.joystickDeadzone = 0.15F;
/*    */   }
/*    */   
/*    */   public String type()
/*    */   {
/* 50 */     return "Xbox 360";
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\microsoft\MicrosoftGamepadXbox360.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */