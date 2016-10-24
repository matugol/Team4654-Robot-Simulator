/*    */ package com.qualcomm.robotcore.wifi;
/*    */ 
/*    */ import android.content.Context;
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
/*    */ public class NetworkConnectionFactory
/*    */ {
/*    */   public static final String NETWORK_CONNECTION_TYPE = "NETWORK_CONNECTION_TYPE";
/*    */   
/*    */   public static NetworkConnection getNetworkConnection(NetworkType type, Context context)
/*    */   {
/* 43 */     RobotLog.v("Starting network of type: " + type);
/* 44 */     switch (type) {
/*    */     case WIFIDIRECT: 
/* 46 */       return WifiDirectAssistant.getWifiDirectAssistant(context);
/*    */     case LOOPBACK: 
/* 48 */       return null;
/*    */     case SOFTAP: 
/* 50 */       return SoftApAssistant.getSoftApAssistant(context);
/*    */     }
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public static NetworkType getTypeFromString(String type)
/*    */   {
/* 57 */     return NetworkType.fromString(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\wifi\NetworkConnectionFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */