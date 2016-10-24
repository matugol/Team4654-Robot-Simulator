/*    */ package org.firstinspires.ftc.robotcore.internal.network;
/*    */ 
/*    */ import android.content.Context;
/*    */ import com.qualcomm.robotcore.R.string;
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
/*    */ public enum NetworkStatus
/*    */ {
/* 44 */   UNKNOWN, 
/* 45 */   INACTIVE, 
/* 46 */   ACTIVE, 
/* 47 */   ENABLED, 
/* 48 */   ERROR, 
/* 49 */   CREATED_AP_CONNECTION;
/*    */   
/*    */   private NetworkStatus() {}
/*    */   
/* 53 */   public String toString(Context context, Object... args) { switch (this) {
/*    */     case UNKNOWN: 
/* 55 */       return context.getString(R.string.networkStatusUnknown);
/* 56 */     case ACTIVE:  return context.getString(R.string.networkStatusActive);
/* 57 */     case INACTIVE:  return context.getString(R.string.networkStatusInactive);
/* 58 */     case ENABLED:  return context.getString(R.string.networkStatusEnabled);
/* 59 */     case ERROR:  return context.getString(R.string.networkStatusError);
/* 60 */     case CREATED_AP_CONNECTION:  return String.format(context.getString(R.string.networkStatusCreatedAPConnection), args); }
/* 61 */     return context.getString(R.string.networkStatusInternalError);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\network\NetworkStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */