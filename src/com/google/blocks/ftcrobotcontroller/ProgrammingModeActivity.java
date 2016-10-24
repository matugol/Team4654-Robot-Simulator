/*    */ package com.google.blocks.ftcrobotcontroller;
/*    */ 
/*    */ import android.content.Intent;
/*    */ import com.google.blocks.AbstractProgrammingModeActivity;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareUtilDeviceTest;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ import com.qualcomm.robotcore.wifi.NetworkType;
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
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
/*    */ public class ProgrammingModeActivity
/*    */   extends AbstractProgrammingModeActivity
/*    */ {
/*    */   public static final String launchIntent = "com.google.blocks.ftcrobotcontroller.ProgrammingModeActivity.intent.action.Launch";
/*    */   private NetworkType networkType;
/*    */   private ProgrammingModeServer programmingModeServer;
/*    */   
/*    */   protected void onStart()
/*    */   {
/* 29 */     super.onStart();
/*    */     
/* 31 */     Intent intent = getIntent();
/* 32 */     Serializable extra = intent.getSerializableExtra("PROGRAMMING_MODE_ACTIVITY_NETWORK_TYPE");
/*    */     
/* 34 */     if (extra != null) {
/* 35 */       this.networkType = ((NetworkType)extra);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onResume()
/*    */   {
/* 41 */     super.onResume();
/*    */     
/*    */ 
/*    */ 
/* 45 */     ProgrammingModeLog programmingModeLog = new ProgrammingModeLog()
/*    */     {
/*    */       public void addToLog(String msg) {
/* 48 */         ProgrammingModeActivity.this.addMessageToTextViewLog(msg);
/*    */       }
/*    */       
/* 51 */     };
/* 52 */     this.programmingModeServer = new ProgrammingModeServer(this, this.networkType, programmingModeLog);
/*    */     try
/*    */     {
/* 55 */       this.programmingModeServer.start();
/*    */     } catch (IOException e) {
/* 57 */       RobotLog.logStacktrace(e);
/*    */     }
/*    */     
/*    */ 
/* 61 */     updateDisplay(this.programmingModeServer.getConnectionInformation());
/*    */   }
/*    */   
/*    */   public void onPause()
/*    */   {
/* 66 */     super.onPause();
/*    */     
/* 68 */     if (this.programmingModeServer != null) {
/* 69 */       this.programmingModeServer.stop();
/* 70 */       this.programmingModeServer = null;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private void runTestsOnDevice()
/*    */   {
/*    */     try
/*    */     {
/* 82 */       HardwareUtilDeviceTest.testAll();
/* 83 */       addMessageToTextViewLog("HardwareUtilDeviceTest passed!");
/*    */     } catch (Exception e) {
/* 85 */       addMessageToTextViewLog("HardwareUtilDeviceTest failed!");
/* 86 */       addMessageToTextViewLog(e.toString());
/* 87 */       for (StackTraceElement el : e.getStackTrace()) {
/* 88 */         addMessageToTextViewLog(el.toString());
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\ProgrammingModeActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */