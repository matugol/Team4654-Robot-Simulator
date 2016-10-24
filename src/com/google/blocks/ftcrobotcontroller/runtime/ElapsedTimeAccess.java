/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.qualcomm.robotcore.util.ElapsedTime;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ElapsedTimeAccess
/*    */   extends Access
/*    */ {
/*    */   ElapsedTimeAccess(String identifier)
/*    */   {
/* 17 */     super(identifier);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public Object create()
/*    */   {
/* 23 */     return new ElapsedTime();
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getStartTime(Object elapsedTime)
/*    */   {
/* 29 */     if ((elapsedTime instanceof ElapsedTime)) {
/*    */       try {
/* 31 */         return ((ElapsedTime)elapsedTime).startTime();
/*    */       } catch (Exception e) {
/* 33 */         RobotLog.e("ElapsedTimeAccess.getStartTime - caught " + e);
/*    */       }
/*    */     }
/* 36 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getSeconds(Object elapsedTime)
/*    */   {
/* 42 */     if ((elapsedTime instanceof ElapsedTime)) {
/*    */       try {
/* 44 */         return ((ElapsedTime)elapsedTime).seconds();
/*    */       } catch (Exception e) {
/* 46 */         RobotLog.e("ElapsedTimeAccess.getSeconds - caught " + e);
/*    */       }
/*    */     }
/* 49 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public double getMilliseconds(Object elapsedTime)
/*    */   {
/* 55 */     if ((elapsedTime instanceof ElapsedTime)) {
/*    */       try {
/* 57 */         return ((ElapsedTime)elapsedTime).milliseconds();
/*    */       } catch (Exception e) {
/* 59 */         RobotLog.e("ElapsedTimeAccess.getMilliseconds - caught " + e);
/*    */       }
/*    */     }
/* 62 */     return 0.0D;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public String getAsText(Object elapsedTime)
/*    */   {
/* 68 */     if ((elapsedTime instanceof ElapsedTime)) {
/*    */       try {
/* 70 */         return ((ElapsedTime)elapsedTime).toString();
/*    */       } catch (Exception e) {
/* 72 */         RobotLog.e("ElapsedTimeAccess.getAsText - caught " + e);
/*    */       }
/*    */     }
/* 75 */     return "";
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void reset(Object elapsedTime)
/*    */   {
/* 81 */     if ((elapsedTime instanceof ElapsedTime)) {
/*    */       try {
/* 83 */         ((ElapsedTime)elapsedTime).reset();
/*    */       } catch (Exception e) {
/* 85 */         RobotLog.e("ElapsedTimeAccess.reset - caught " + e);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\ElapsedTimeAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */