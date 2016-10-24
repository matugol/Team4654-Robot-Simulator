/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.util.Log;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DbgLog
/*    */ {
/*    */   public static final String TAG = "FIRST";
/*    */   public static final String ERROR_PREPEND = "### ERROR: ";
/*    */   
/*    */   public static void msg(String message)
/*    */   {
/* 52 */     Log.i("FIRST", message);
/*    */   }
/*    */   
/*    */   public static void msg(String format, Object... args) {
/* 56 */     msg(String.format(format, args));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void error(String message)
/*    */   {
/* 67 */     Log.e("FIRST", "### ERROR: " + message);
/*    */   }
/*    */   
/*    */   public static void error(String format, Object... args) {
/* 71 */     error(String.format(format, args));
/*    */   }
/*    */   
/*    */   public static void logStacktrace(Exception e) {
/* 75 */     msg(e.toString());
/* 76 */     for (StackTraceElement el : e.getStackTrace()) {
/* 77 */       msg(el.toString());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\DbgLog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */