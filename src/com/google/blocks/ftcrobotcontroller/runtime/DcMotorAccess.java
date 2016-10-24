/*     */ package com.google.blocks.ftcrobotcontroller.runtime;
/*     */ 
/*     */ import android.webkit.JavascriptInterface;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DcMotorAccess
/*     */   extends HardwareAccess<DcMotor>
/*     */ {
/*     */   private final DcMotor dcMotor;
/*     */   
/*     */   DcMotorAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<DcMotor> deviceMapping)
/*     */   {
/*  23 */     super(hardwareItem, deviceMapping);
/*  24 */     this.dcMotor = ((DcMotor)this.hardwareDevice);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JavascriptInterface
/*     */   public void setDirection(String directionString)
/*     */   {
/*  32 */     if (this.dcMotor != null)
/*     */     {
/*     */       try {
/*  35 */         direction = DcMotorSimple.Direction.valueOf(directionString.toUpperCase(Locale.ENGLISH));
/*     */       } catch (Exception e) { DcMotorSimple.Direction direction;
/*     */         return; }
/*     */       DcMotorSimple.Direction direction;
/*  39 */       this.dcMotor.setDirection(direction);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public String getDirection()
/*     */   {
/*  46 */     if (this.dcMotor != null) {
/*  47 */       DcMotorSimple.Direction direction = this.dcMotor.getDirection();
/*  48 */       if (direction != null) {
/*  49 */         return direction.toString();
/*     */       }
/*     */     }
/*  52 */     return "";
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setPower(double power)
/*     */   {
/*  58 */     if (this.dcMotor != null) {
/*  59 */       this.dcMotor.setPower(power);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public double getPower()
/*     */   {
/*  66 */     if (this.dcMotor != null) {
/*  67 */       return this.dcMotor.getPower();
/*     */     }
/*  69 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JavascriptInterface
/*     */   public void setMaxSpeed(double maxSpeed)
/*     */   {
/*  77 */     if (this.dcMotor != null) {
/*  78 */       this.dcMotor.setMaxSpeed((int)maxSpeed);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getMaxSpeed()
/*     */   {
/*  85 */     if (this.dcMotor != null) {
/*  86 */       return this.dcMotor.getMaxSpeed();
/*     */     }
/*  88 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setZeroPowerBehavior(String zeroPowerBehaviorString)
/*     */   {
/*  94 */     if (this.dcMotor != null) {
/*  95 */       zeroPowerBehaviorString = zeroPowerBehaviorString.toUpperCase(Locale.ENGLISH);
/*     */       try
/*     */       {
/*  98 */         zeroPowerBehavior = DcMotor.ZeroPowerBehavior.valueOf(zeroPowerBehaviorString);
/*     */       } catch (Exception e) { DcMotor.ZeroPowerBehavior zeroPowerBehavior;
/*     */         return; }
/*     */       DcMotor.ZeroPowerBehavior zeroPowerBehavior;
/* 102 */       this.dcMotor.setZeroPowerBehavior(zeroPowerBehavior);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public String getZeroPowerBehavior()
/*     */   {
/* 109 */     if (this.dcMotor != null) {
/* 110 */       DcMotor.ZeroPowerBehavior zeroPowerBehavior = this.dcMotor.getZeroPowerBehavior();
/* 111 */       if (zeroPowerBehavior != null) {
/* 112 */         return zeroPowerBehavior.toString();
/*     */       }
/*     */     }
/* 115 */     return "";
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean getPowerFloat()
/*     */   {
/* 121 */     if (this.dcMotor != null) {
/* 122 */       return this.dcMotor.getPowerFloat();
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setTargetPosition(double position)
/*     */   {
/* 130 */     if (this.dcMotor != null) {
/* 131 */       this.dcMotor.setTargetPosition((int)position);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getTargetPosition()
/*     */   {
/* 138 */     if (this.dcMotor != null) {
/* 139 */       return this.dcMotor.getTargetPosition();
/*     */     }
/* 141 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public boolean isBusy()
/*     */   {
/* 147 */     if (this.dcMotor != null) {
/* 148 */       return this.dcMotor.isBusy();
/*     */     }
/* 150 */     return false;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int getCurrentPosition()
/*     */   {
/* 156 */     if (this.dcMotor != null) {
/* 157 */       return this.dcMotor.getCurrentPosition();
/*     */     }
/* 159 */     return 0;
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setMode(String runModeString)
/*     */   {
/* 165 */     if (this.dcMotor != null) {
/* 166 */       runModeString = runModeString.toUpperCase(Locale.ENGLISH);
/*     */       
/*     */       DcMotor.RunMode runMode;
/* 169 */       if (runModeString.equals("RUN_USING_ENCODERS")) {
/* 170 */         runMode = DcMotor.RunMode.RUN_USING_ENCODER; } else { DcMotor.RunMode runMode;
/* 171 */         if (runModeString.equals("RUN_WITHOUT_ENCODERS")) {
/* 172 */           runMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER; } else { DcMotor.RunMode runMode;
/* 173 */           if (runModeString.equals("RESET_ENCODERS")) {
/* 174 */             runMode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
/*     */           } else
/*     */             try {
/* 177 */               runMode = DcMotor.RunMode.valueOf(runModeString);
/*     */             } catch (Exception e) { DcMotor.RunMode runMode;
/*     */               return;
/*     */             } } }
/*     */       DcMotor.RunMode runMode;
/* 182 */       this.dcMotor.setMode(runMode);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public String getMode()
/*     */   {
/* 189 */     if (this.dcMotor != null) {
/* 190 */       DcMotor.RunMode runMode = this.dcMotor.getMode();
/* 191 */       if (runMode != null) {
/* 192 */         return runMode.toString();
/*     */       }
/*     */     }
/* 195 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JavascriptInterface
/*     */   public void setDualMaxSpeed(double maxSpeed1, Object other, double maxSpeed2)
/*     */   {
/* 203 */     if ((other instanceof DcMotorAccess)) {
/* 204 */       setMaxSpeed(maxSpeed1);
/* 205 */       ((DcMotorAccess)other).setMaxSpeed(maxSpeed2);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setDualMode(String runMode1, Object other, String runMode2)
/*     */   {
/* 212 */     if ((other instanceof DcMotorAccess)) {
/* 213 */       setMode(runMode1);
/* 214 */       ((DcMotorAccess)other).setMode(runMode2);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setDualPower(double power1, Object other, double power2)
/*     */   {
/* 221 */     if ((other instanceof DcMotorAccess)) {
/* 222 */       setPower(power1);
/* 223 */       ((DcMotorAccess)other).setPower(power2);
/*     */     }
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public void setDualTargetPosition(double position1, Object other, double position2)
/*     */   {
/* 230 */     if ((other instanceof DcMotorAccess)) {
/* 231 */       setTargetPosition(position1);
/* 232 */       ((DcMotorAccess)other).setTargetPosition(position2);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JavascriptInterface
/*     */   public void setDualZeroPowerBehavior(String zeroPowerBehaviorString1, Object other, String zeroPowerBehaviorString2)
/*     */   {
/* 240 */     if ((other instanceof DcMotorAccess)) {
/* 241 */       setZeroPowerBehavior(zeroPowerBehaviorString1);
/* 242 */       ((DcMotorAccess)other).setZeroPowerBehavior(zeroPowerBehaviorString2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\DcMotorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */