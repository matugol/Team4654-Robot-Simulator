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
/*    */ public enum PeerStatus
/*    */ {
/* 44 */   UNKNOWN(-1), 
/* 45 */   DISCONNECTED(0), 
/* 46 */   CONNECTED(1);
/*    */   
/*    */   public final byte bVal;
/*    */   
/*    */   private PeerStatus(int bVal)
/*    */   {
/* 52 */     this.bVal = ((byte)bVal);
/*    */   }
/*    */   
/*    */   public static PeerStatus fromByte(byte b)
/*    */   {
/* 57 */     for (PeerStatus status : )
/*    */     {
/* 59 */       if (status.bVal == b)
/*    */       {
/* 61 */         return status;
/*    */       }
/*    */     }
/* 64 */     return UNKNOWN;
/*    */   }
/*    */   
/*    */   public String toString(Context context)
/*    */   {
/* 69 */     switch (this) {
/*    */     case UNKNOWN: 
/* 71 */       return context.getString(R.string.networkStatusUnknown);
/* 72 */     case CONNECTED:  return context.getString(R.string.peerStatusConnected);
/* 73 */     case DISCONNECTED:  return context.getString(R.string.peerStatusDisconnected); }
/* 74 */     return context.getString(R.string.networkStatusInternalError);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\network\PeerStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */