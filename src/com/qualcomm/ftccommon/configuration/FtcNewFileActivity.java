/*    */ package com.qualcomm.ftccommon.configuration;
/*    */ 
/*    */ import android.os.Bundle;
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
/*    */ public class FtcNewFileActivity
/*    */   extends FtcConfigurationActivity
/*    */ {
/* 44 */   public static final RequestCode requestCode = RequestCode.NEW_FILE;
/*    */   
/*    */   protected void onCreate(Bundle savedInstanceState)
/*    */   {
/* 48 */     super.onCreate(savedInstanceState);
/*    */     
/*    */ 
/* 51 */     dirtyCheckThenSingletonUSBScanAndUpdateUI(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\FtcNewFileActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */