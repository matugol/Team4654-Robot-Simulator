/*    */ package com.google.blocks.ftcrobotcontroller;
/*    */ 
/*    */ import android.app.Activity;
/*    */ import android.widget.TextView;
/*    */ import com.google.blocks.ProgrammingModeConnectionInfo;
/*    */ import com.qualcomm.ftccommon.FtcEventLoopHandler;
/*    */ import com.qualcomm.ftccommon.ProgrammingModeController;
/*    */ import com.qualcomm.robotcore.robocol.Command;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ import com.qualcomm.robotcore.wifi.NetworkType;
/*    */ import java.io.IOException;
/*    */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
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
/*    */ public class ProgrammingModeControllerImpl
/*    */   implements ProgrammingModeController
/*    */ {
/*    */   private final Activity activity;
/*    */   private final TextView textRemoteProgrammingMode;
/*    */   private volatile NetworkType networkType;
/*    */   private volatile ProgrammingModeServer programmingModeServer;
/* 30 */   private NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
/*    */   
/*    */   public ProgrammingModeControllerImpl(Activity activity, TextView textRemoteProgrammingMode) {
/* 33 */     this.activity = activity;
/* 34 */     this.textRemoteProgrammingMode = textRemoteProgrammingMode;
/*    */   }
/*    */   
/*    */   public void setCurrentNetworkType(NetworkType networkType)
/*    */   {
/* 39 */     this.networkType = networkType;
/*    */   }
/*    */   
/*    */   public boolean isActive()
/*    */   {
/* 44 */     return this.programmingModeServer != null;
/*    */   }
/*    */   
/*    */ 
/*    */   public void startProgrammingMode(FtcEventLoopHandler ftcEventLoopHandler)
/*    */   {
/* 50 */     ProgrammingModeLog programmingModeLog = new ProgrammingModeLog()
/*    */     {
/*    */       public void addToLog(String msg) {
/* 53 */         ProgrammingModeControllerImpl.this.networkConnectionHandler.sendCommand(new Command("CMD_PROGRAMMING_MODE_LOG_NOTIFICATION", msg));
/*    */       }
/*    */       
/*    */ 
/* 57 */     };
/* 58 */     this.programmingModeServer = new ProgrammingModeServer(this.activity, this.networkType, programmingModeLog);
/*    */     
/* 60 */     this.activity.runOnUiThread(new Runnable()
/*    */     {
/*    */       public void run() {
/* 63 */         ProgrammingModeControllerImpl.this.textRemoteProgrammingMode.setText("Programming Mode is Active");
/* 64 */         ProgrammingModeControllerImpl.this.textRemoteProgrammingMode.setVisibility(0);
/*    */       }
/*    */     });
/*    */     try
/*    */     {
/* 69 */       this.programmingModeServer.start();
/*    */     } catch (IOException e) {
/* 71 */       RobotLog.logStacktrace(e);
/*    */     }
/*    */     
/*    */ 
/* 75 */     String extra = this.programmingModeServer.getConnectionInformation().toJson();
/* 76 */     this.networkConnectionHandler.sendCommand(new Command("CMD_START_PROGRAMMING_MODE_RESP", extra));
/*    */   }
/*    */   
/*    */ 
/*    */   public void stopProgrammingMode()
/*    */   {
/* 82 */     if (this.programmingModeServer != null) {
/* 83 */       this.programmingModeServer.stop();
/* 84 */       this.programmingModeServer = null;
/*    */     }
/*    */     
/* 87 */     this.activity.runOnUiThread(new Runnable()
/*    */     {
/*    */       public void run() {
/* 90 */         ProgrammingModeControllerImpl.this.textRemoteProgrammingMode.setVisibility(4);
/* 91 */         ProgrammingModeControllerImpl.this.textRemoteProgrammingMode.setText("");
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\ProgrammingModeControllerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */