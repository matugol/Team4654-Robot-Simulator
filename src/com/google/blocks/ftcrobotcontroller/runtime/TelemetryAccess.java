/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import android.webkit.JavascriptInterface;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ import org.firstinspires.ftc.robotcore.external.Telemetry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class TelemetryAccess
/*    */   extends Access
/*    */ {
/*    */   private final Telemetry telemetry;
/*    */   
/*    */   TelemetryAccess(String identifier, Telemetry telemetry)
/*    */   {
/* 19 */     super(identifier);
/* 20 */     this.telemetry = telemetry;
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void addNumericData(String key, double msg)
/*    */   {
/*    */     try {
/* 27 */       this.telemetry.addData(key, Double.valueOf(msg));
/*    */     } catch (Exception e) {
/* 29 */       RobotLog.e("TelemetryAccess.addNumericData - caught " + e);
/*    */     }
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void addTextData(String key, String msg)
/*    */   {
/*    */     try {
/* 37 */       this.telemetry.addData(key, msg);
/*    */     } catch (Exception e) {
/* 39 */       RobotLog.e("TelemetryAccess.addTextData - caught " + e);
/*    */     }
/*    */   }
/*    */   
/*    */   @JavascriptInterface
/*    */   public void update()
/*    */   {
/*    */     try {
/* 47 */       this.telemetry.update();
/*    */     } catch (Exception e) {
/* 49 */       RobotLog.e("TelemetryAccess.update - caught " + e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\TelemetryAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */