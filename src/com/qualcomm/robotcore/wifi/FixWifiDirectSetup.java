/*    */ package com.qualcomm.robotcore.wifi;
/*    */ 
/*    */ import android.net.wifi.WifiManager;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
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
/*    */ public class FixWifiDirectSetup
/*    */ {
/*    */   public static final int WIFI_TOGGLE_DELAY = 2000;
/*    */   
/*    */   public static void fixWifiDirectSetup(WifiManager wifiManager)
/*    */     throws InterruptedException
/*    */   {
/* 45 */     toggleWifi(false, wifiManager);
/* 46 */     toggleWifi(true, wifiManager);
/*    */   }
/*    */   
/*    */   public static void disableWifiDirect(WifiManager wifiManager) throws InterruptedException {
/* 50 */     toggleWifi(false, wifiManager);
/*    */   }
/*    */   
/*    */   private static void toggleWifi(boolean enabled, WifiManager wifiManager) throws InterruptedException {
/* 54 */     String toggle = enabled ? "on" : "off";
/* 55 */     RobotLog.i("Toggling Wifi " + toggle);
/* 56 */     wifiManager.setWifiEnabled(enabled);
/* 57 */     Thread.sleep(2000L);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\wifi\FixWifiDirectSetup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */