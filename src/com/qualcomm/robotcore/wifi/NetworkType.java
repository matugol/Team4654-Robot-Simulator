/*    */ package com.qualcomm.robotcore.wifi;
/*    */ 
/*    */ import android.support.annotation.NonNull;
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
/*    */ public enum NetworkType
/*    */ {
/* 36 */   WIFIDIRECT, 
/* 37 */   LOOPBACK, 
/* 38 */   SOFTAP, 
/* 39 */   UNKNOWN_NETWORK_TYPE;
/*    */   
/*    */   private NetworkType() {}
/*    */   @NonNull
/* 43 */   public static NetworkType fromString(String type) { try { return valueOf(type.toUpperCase());
/*    */     } catch (Exception e) {}
/* 45 */     return UNKNOWN_NETWORK_TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\wifi\NetworkType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */