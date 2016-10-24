/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.os.Handler;
/*     */ import android.view.Window;
/*     */ import android.view.WindowManager.LayoutParams;
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
/*     */ public class Dimmer
/*     */ {
/*     */   public static final int DEFAULT_DIM_TIME = 30000;
/*     */   public static final int LONG_BRIGHT_TIME = 60000;
/*     */   public static final float MAXIMUM_BRIGHTNESS = 1.0F;
/*     */   public static final float MINIMUM_BRIGHTNESS = 0.05F;
/*  47 */   Handler handler = new Handler();
/*     */   
/*     */   Activity activity;
/*     */   final WindowManager.LayoutParams layoutParams;
/*     */   long waitTime;
/*  52 */   float userBrightness = 1.0F;
/*     */   
/*     */   public Dimmer(Activity activity) {
/*  55 */     this(30000L, activity);
/*     */   }
/*     */   
/*     */   public Dimmer(long waitTime, Activity activity) {
/*  59 */     this.waitTime = waitTime;
/*  60 */     this.activity = activity;
/*  61 */     this.layoutParams = activity.getWindow().getAttributes();
/*  62 */     this.userBrightness = this.layoutParams.screenBrightness;
/*     */   }
/*     */   
/*     */   private float percentageDim() {
/*  66 */     float newBrightness = 0.05F * this.userBrightness;
/*  67 */     if (newBrightness < 0.05F) {
/*  68 */       return 0.05F;
/*     */     }
/*  70 */     return newBrightness;
/*     */   }
/*     */   
/*     */   public void handleDimTimer() {
/*  74 */     sendToUIThread(this.userBrightness);
/*  75 */     this.handler.removeCallbacks(null);
/*  76 */     this.handler.postDelayed(new Runnable()
/*     */     {
/*     */ 
/*  79 */       public void run() { Dimmer.this.sendToUIThread(Dimmer.access$000(Dimmer.this)); } }, this.waitTime);
/*     */   }
/*     */   
/*     */ 
/*     */   private void sendToUIThread(float brightness)
/*     */   {
/*  85 */     this.layoutParams.screenBrightness = brightness;
/*  86 */     this.activity.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/*  89 */         Dimmer.this.activity.getWindow().setAttributes(Dimmer.this.layoutParams);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void longBright()
/*     */   {
/* 101 */     sendToUIThread(this.userBrightness);
/* 102 */     Runnable runnable = new Runnable()
/*     */     {
/*     */       public void run() {
/* 105 */         Dimmer.this.sendToUIThread(Dimmer.access$000(Dimmer.this));
/*     */       }
/* 107 */     };
/* 108 */     this.handler.removeCallbacksAndMessages(null);
/* 109 */     this.handler.postDelayed(runnable, 60000L);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\Dimmer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */