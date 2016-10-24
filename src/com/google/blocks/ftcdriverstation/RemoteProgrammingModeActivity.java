/*     */ package com.google.blocks.ftcdriverstation;
/*     */ 
/*     */ import android.content.Intent;
/*     */ import com.google.blocks.AbstractProgrammingModeActivity;
/*     */ import com.google.blocks.ProgrammingModeConnectionInfo;
/*     */ import com.qualcomm.ftccommon.DbgLog;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.RobocolDatagram;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable.RecvLoopCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RemoteProgrammingModeActivity
/*     */   extends AbstractProgrammingModeActivity
/*     */   implements RecvLoopRunnable.RecvLoopCallback
/*     */ {
/*     */   public static final String launchIntent = "com.google.blocks.ftcdriverstation.RemoteProgrammingModeActivity.intent.action.Launch";
/*     */   private static final boolean DEBUG = false;
/*  32 */   private NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
/*     */   
/*     */   public void onStart()
/*     */   {
/*  36 */     super.onStart();
/*     */     
/*  38 */     this.networkConnectionHandler.pushReceiveLoopCallback(this);
/*     */     
/*  40 */     String extra = getIntent().getStringExtra("PROGRAMMING_MODE_CONNECTION_INFO");
/*     */     
/*  42 */     updateDisplay(ProgrammingModeConnectionInfo.fromJson(extra));
/*     */   }
/*     */   
/*     */   public void onPause()
/*     */   {
/*  47 */     super.onPause();
/*     */   }
/*     */   
/*     */   protected void onStop()
/*     */   {
/*  52 */     super.onStop();
/*  53 */     this.networkConnectionHandler.removeReceiveLoopCallback(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CallbackResult commandEvent(Command command)
/*     */   {
/*  60 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/*     */     try {
/*  62 */       String name = command.getName();
/*  63 */       String extra = command.getExtra();
/*     */       
/*  65 */       if (name.equals("CMD_PROGRAMMING_MODE_LOG_NOTIFICATION")) {
/*  66 */         addMessageToTextViewLog(extra);
/*  67 */         result = CallbackResult.HANDLED;
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  71 */       DbgLog.logStacktrace(e);
/*     */     }
/*  73 */     return result;
/*     */   }
/*     */   
/*     */   public CallbackResult telemetryEvent(RobocolDatagram packet)
/*     */   {
/*  78 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult reportGlobalError(String error, boolean recoverable)
/*     */   {
/*  83 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult packetReceived(RobocolDatagram packet)
/*     */   {
/*  88 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult peerDiscoveryEvent(RobocolDatagram packet)
/*     */   {
/*  93 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult heartbeatEvent(RobocolDatagram packet, long tReceived)
/*     */   {
/*  98 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult gamepadEvent(RobocolDatagram packet)
/*     */   {
/* 103 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult emptyEvent(RobocolDatagram packet)
/*     */   {
/* 108 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcdriverstation\RemoteProgrammingModeActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */