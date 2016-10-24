/*    */ package com.qualcomm.robotcore.util;
/*    */ 
/*    */ import android.os.Build.VERSION;
/*    */ import android.os.Handler;
/*    */ import android.os.Message;
/*    */ import android.view.View;
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
/*    */ public class ImmersiveMode
/*    */ {
/*    */   View decorView;
/*    */   
/*    */   public ImmersiveMode(View decorView)
/*    */   {
/* 49 */     this.decorView = decorView;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 55 */   Handler hideSystemUiHandler = new Handler()
/*    */   {
/*    */     public void handleMessage(Message msg) {
/* 58 */       ImmersiveMode.this.hideSystemUI();
/*    */     }
/*    */   };
/*    */   
/*    */   public void cancelSystemUIHide() {
/* 63 */     this.hideSystemUiHandler.removeMessages(0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void hideSystemUI()
/*    */   {
/* 71 */     this.decorView.setSystemUiVisibility(4098);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static boolean apiOver19()
/*    */   {
/* 78 */     int currentApiVersion = Build.VERSION.SDK_INT;
/* 79 */     return currentApiVersion >= 19;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\ImmersiveMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */