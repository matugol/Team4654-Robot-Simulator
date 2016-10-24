/*     */ package com.qualcomm.robotcore.wifi;
/*     */ 
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.net.NetworkInfo;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
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
/*     */ public class WifiAssistant
/*     */ {
/*     */   private final IntentFilter intentFilter;
/*     */   private final Context context;
/*     */   private final WifiStateBroadcastReceiver receiver;
/*     */   
/*     */   public static enum WifiState
/*     */   {
/*  51 */     CONNECTED,  NOT_CONNECTED;
/*     */     
/*     */ 
/*     */     private WifiState() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract interface WifiAssistantCallback
/*     */   {
/*     */     public abstract void wifiEventCallback(WifiAssistant.WifiState paramWifiState);
/*     */   }
/*     */   
/*     */   private static class WifiStateBroadcastReceiver
/*     */     extends BroadcastReceiver
/*     */   {
/*  66 */     private WifiAssistant.WifiState state = null;
/*     */     private final WifiAssistant.WifiAssistantCallback callback;
/*     */     
/*     */     public WifiStateBroadcastReceiver(WifiAssistant.WifiAssistantCallback callback) {
/*  70 */       this.callback = callback;
/*     */     }
/*     */     
/*     */     public void onReceive(Context context, Intent intent)
/*     */     {
/*  75 */       String action = intent.getAction();
/*  76 */       if (action.equals("android.net.wifi.STATE_CHANGE")) {
/*  77 */         NetworkInfo info = (NetworkInfo)intent.getParcelableExtra("networkInfo");
/*  78 */         if (info.isConnected()) {
/*  79 */           notify(WifiAssistant.WifiState.CONNECTED);
/*     */         } else {
/*  81 */           notify(WifiAssistant.WifiState.NOT_CONNECTED);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void notify(WifiAssistant.WifiState newState) {
/*  87 */       if (this.state == newState) { return;
/*     */       }
/*  89 */       this.state = newState;
/*  90 */       if (this.callback != null) { this.callback.wifiEventCallback(this.state);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WifiAssistant(Context context, WifiAssistantCallback callback)
/*     */   {
/* 104 */     this.context = context;
/*     */     
/* 106 */     if (callback == null) RobotLog.v("WifiAssistantCallback is null");
/* 107 */     this.receiver = new WifiStateBroadcastReceiver(callback);
/*     */     
/* 109 */     this.intentFilter = new IntentFilter();
/* 110 */     this.intentFilter.addAction("android.net.wifi.STATE_CHANGE");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enable()
/*     */   {
/* 119 */     this.context.registerReceiver(this.receiver, this.intentFilter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disable()
/*     */   {
/* 128 */     this.context.unregisterReceiver(this.receiver);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\wifi\WifiAssistant.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */