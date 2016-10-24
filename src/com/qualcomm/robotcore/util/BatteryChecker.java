/*    */ package com.qualcomm.robotcore.util;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
/*    */ import android.content.IntentFilter;
/*    */ import android.os.Handler;
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
/*    */ public class BatteryChecker
/*    */ {
/*    */   private Context context;
/*    */   private long delay;
/* 49 */   private long SHORT_DELAY = 5000L;
/*    */   private BatteryWatcher watcher;
/*    */   protected Handler batteryHandler;
/*    */   
/*    */   public BatteryChecker(Context context, BatteryWatcher watcher, long delay) {
/* 54 */     this.context = context;
/* 55 */     this.watcher = watcher;
/* 56 */     this.delay = delay;
/* 57 */     this.batteryHandler = new Handler();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 66 */   Runnable batteryLevelChecker = new Runnable()
/*    */   {
/*    */     public void run() {
/* 69 */       float percent = BatteryChecker.this.getBatteryLevel();
/*    */       
/* 71 */       BatteryChecker.this.watcher.updateBatteryLevel(percent);
/* 72 */       RobotLog.i("Battery Checker, Level Remaining: " + percent);
/*    */       
/*    */ 
/* 75 */       BatteryChecker.this.batteryHandler.postDelayed(BatteryChecker.this.batteryLevelChecker, BatteryChecker.this.delay);
/*    */     }
/*    */   };
/*    */   
/*    */   public float getBatteryLevel() {
/* 80 */     IntentFilter batteryLevelFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
/* 81 */     Intent intent = this.context.registerReceiver(null, batteryLevelFilter);
/*    */     
/* 83 */     int currentLevel = intent.getIntExtra("level", -1);
/* 84 */     int scale = intent.getIntExtra("scale", -1);
/* 85 */     int percent = -1;
/* 86 */     if ((currentLevel >= 0) && (scale > 0)) {
/* 87 */       percent = currentLevel * 100 / scale;
/*    */     }
/* 89 */     return percent;
/*    */   }
/*    */   
/*    */   public void startBatteryMonitoring()
/*    */   {
/* 94 */     this.batteryHandler.postDelayed(this.batteryLevelChecker, this.SHORT_DELAY);
/*    */   }
/*    */   
/*    */   public void endBatteryMonitoring() {
/* 98 */     this.batteryHandler.removeCallbacks(this.batteryLevelChecker);
/*    */   }
/*    */   
/*    */   public static abstract interface BatteryWatcher
/*    */   {
/*    */     public abstract void updateBatteryLevel(float paramFloat);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\BatteryChecker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */