/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface DcMotor
/*     */   extends DcMotorSimple
/*     */ {
/*     */   public abstract void setMaxSpeed(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int getMaxSpeed();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract DcMotorController getController();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int getPortNumber();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void setZeroPowerBehavior(ZeroPowerBehavior paramZeroPowerBehavior);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ZeroPowerBehavior getZeroPowerBehavior();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public abstract void setPowerFloat();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean getPowerFloat();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void setTargetPosition(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int getTargetPosition();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isBusy();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int getCurrentPosition();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void setMode(RunMode paramRunMode);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract RunMode getMode();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum ZeroPowerBehavior
/*     */   {
/*  91 */     UNKNOWN, 
/*     */     
/*     */ 
/*  94 */     BRAKE, 
/*     */     
/*     */ 
/*  97 */     FLOAT;
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
/*     */     private ZeroPowerBehavior() {}
/*     */   }
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
/*     */   public static enum RunMode
/*     */   {
/* 205 */     RUN_WITHOUT_ENCODER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 210 */     RUN_USING_ENCODER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */     RUN_TO_POSITION, 
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
/* 232 */     STOP_AND_RESET_ENCODER, 
/*     */     
/*     */ 
/* 235 */     RUN_WITHOUT_ENCODERS, 
/*     */     
/*     */ 
/* 238 */     RUN_USING_ENCODERS, 
/*     */     
/*     */ 
/* 241 */     RESET_ENCODERS;
/*     */     
/*     */     private RunMode() {}
/*     */     
/*     */     @Deprecated
/*     */     public RunMode migrate()
/*     */     {
/* 248 */       switch (DcMotor.1.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[ordinal()]) {
/*     */       case 1: 
/* 250 */         return RUN_WITHOUT_ENCODER;
/* 251 */       case 2:  return RUN_USING_ENCODER;
/* 252 */       case 3:  return STOP_AND_RESET_ENCODER; }
/* 253 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isPIDMode()
/*     */     {
/* 263 */       return (this == RUN_USING_ENCODER) || (this == RUN_USING_ENCODERS) || (this == RUN_TO_POSITION);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DcMotor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */