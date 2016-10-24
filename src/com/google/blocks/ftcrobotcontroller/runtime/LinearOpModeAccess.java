/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LinearOpModeAccess
/*    */   extends Access
/*    */ {
/*    */   private final LinearOpMode linearOpMode;
/*    */   
/*    */   LinearOpModeAccess(String identifier, LinearOpMode linearOpMode)
/*    */   {
/* 18 */     super(identifier);
/* 19 */     this.linearOpMode = linearOpMode;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void waitForStart() throws InterruptedException
/*    */   {
/* 25 */     this.linearOpMode.waitForStart();
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void idle() throws InterruptedException
/*    */   {
/* 31 */     this.linearOpMode.idle();
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void sleep(double millis) throws InterruptedException
/*    */   {
/* 37 */     this.linearOpMode.sleep(millis);
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public boolean opModeIsActive()
/*    */   {
/* 43 */     return this.linearOpMode.opModeIsActive();
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public boolean isStarted()
/*    */   {
/* 49 */     return this.linearOpMode.isStarted();
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public boolean isStopRequested()
/*    */   {
/* 55 */     return this.linearOpMode.isStopRequested();
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\LinearOpModeAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */