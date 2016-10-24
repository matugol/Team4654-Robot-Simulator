/*     */ package com.google.blocks.ftcrobotcontroller.runtime;
/*     */ 
/*     */ import android.webkit.JavascriptInterface;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GamepadAccess
/*     */   extends Access
/*     */ {
/*     */   private final Gamepad gamepad;
/*     */   
/*     */   GamepadAccess(String identifier, Gamepad gamepad)
/*     */   {
/*  18 */     super(identifier);
/*  19 */     this.gamepad = gamepad;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public float getLeftStickX()
/*     */   {
/*  25 */     if (this.gamepad != null) {
/*  26 */       return this.gamepad.left_stick_x;
/*     */     }
/*  28 */     return 0.0F;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public float getLeftStickY()
/*     */   {
/*  34 */     if (this.gamepad != null) {
/*  35 */       return this.gamepad.left_stick_y;
/*     */     }
/*  37 */     return 0.0F;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public float getRightStickX()
/*     */   {
/*  43 */     if (this.gamepad != null) {
/*  44 */       return this.gamepad.right_stick_x;
/*     */     }
/*  46 */     return 0.0F;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public float getRightStickY()
/*     */   {
/*  52 */     if (this.gamepad != null) {
/*  53 */       return this.gamepad.right_stick_y;
/*     */     }
/*  55 */     return 0.0F;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getDpadUp()
/*     */   {
/*  61 */     if (this.gamepad != null) {
/*  62 */       return this.gamepad.dpad_up;
/*     */     }
/*  64 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getDpadDown()
/*     */   {
/*  70 */     if (this.gamepad != null) {
/*  71 */       return this.gamepad.dpad_down;
/*     */     }
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getDpadLeft()
/*     */   {
/*  79 */     if (this.gamepad != null) {
/*  80 */       return this.gamepad.dpad_left;
/*     */     }
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getDpadRight()
/*     */   {
/*  88 */     if (this.gamepad != null) {
/*  89 */       return this.gamepad.dpad_right;
/*     */     }
/*  91 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getA()
/*     */   {
/*  97 */     if (this.gamepad != null) {
/*  98 */       return this.gamepad.a;
/*     */     }
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getB()
/*     */   {
/* 106 */     if (this.gamepad != null) {
/* 107 */       return this.gamepad.b;
/*     */     }
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getX()
/*     */   {
/* 115 */     if (this.gamepad != null) {
/* 116 */       return this.gamepad.x;
/*     */     }
/* 118 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getY()
/*     */   {
/* 124 */     if (this.gamepad != null) {
/* 125 */       return this.gamepad.y;
/*     */     }
/* 127 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getGuide()
/*     */   {
/* 133 */     if (this.gamepad != null) {
/* 134 */       return this.gamepad.guide;
/*     */     }
/* 136 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getStart()
/*     */   {
/* 142 */     if (this.gamepad != null) {
/* 143 */       return this.gamepad.start;
/*     */     }
/* 145 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getBack()
/*     */   {
/* 151 */     if (this.gamepad != null) {
/* 152 */       return this.gamepad.back;
/*     */     }
/* 154 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getLeftBumper()
/*     */   {
/* 160 */     if (this.gamepad != null) {
/* 161 */       return this.gamepad.left_bumper;
/*     */     }
/* 163 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getRightBumper()
/*     */   {
/* 169 */     if (this.gamepad != null) {
/* 170 */       return this.gamepad.right_bumper;
/*     */     }
/* 172 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getLeftStickButton()
/*     */   {
/* 178 */     if (this.gamepad != null) {
/* 179 */       return this.gamepad.left_stick_button;
/*     */     }
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getRightStickButton()
/*     */   {
/* 187 */     if (this.gamepad != null) {
/* 188 */       return this.gamepad.right_stick_button;
/*     */     }
/* 190 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public float getLeftTrigger()
/*     */   {
/* 196 */     if (this.gamepad != null) {
/* 197 */       return this.gamepad.left_trigger;
/*     */     }
/* 199 */     return 0.0F;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public float getRightTrigger()
/*     */   {
/* 205 */     if (this.gamepad != null) {
/* 206 */       return this.gamepad.right_trigger;
/*     */     }
/* 208 */     return 0.0F;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getAtRest()
/*     */   {
/* 214 */     if (this.gamepad != null) {
/* 215 */       return this.gamepad.atRest();
/*     */     }
/* 217 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\GamepadAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */