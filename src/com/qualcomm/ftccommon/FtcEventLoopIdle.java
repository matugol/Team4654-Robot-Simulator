/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.app.Activity;
/*    */ import android.hardware.usb.UsbDevice;
/*    */ import com.qualcomm.hardware.HardwareFactory;
/*    */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*    */ import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/*    */ import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
/*    */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
/*    */ import com.qualcomm.robotcore.robocol.TelemetryMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FtcEventLoopIdle
/*    */   extends FtcEventLoopBase
/*    */ {
/*    */   public FtcEventLoopIdle(HardwareFactory hardwareFactory, UpdateUI.Callback callback, Activity activityContext, ProgrammingModeController programmingModeController)
/*    */   {
/* 22 */     super(hardwareFactory, callback, activityContext, programmingModeController);
/*    */   }
/*    */   
/*    */ 
/*    */   public void init(EventLoopManager eventLoopManager)
/*    */     throws RobotCoreException, InterruptedException
/*    */   {}
/*    */   
/*    */ 
/*    */   public void loop()
/*    */     throws RobotCoreException, InterruptedException
/*    */   {}
/*    */   
/*    */ 
/*    */   public void refreshUserTelemetry(TelemetryMessage telemetry, double sInterval) {}
/*    */   
/*    */ 
/*    */   public void teardown()
/*    */     throws RobotCoreException, InterruptedException
/*    */   {}
/*    */   
/*    */ 
/*    */   public void onUsbDeviceAttached(UsbDevice usbDevice) {}
/*    */   
/*    */ 
/*    */   public void processedRecentlyAttachedUsbDevices()
/*    */     throws RobotCoreException, InterruptedException
/*    */   {}
/*    */   
/*    */ 
/*    */   public void handleUsbModuleDetach(RobotUsbModule module)
/*    */     throws RobotCoreException, InterruptedException
/*    */   {}
/*    */   
/*    */   public OpModeManagerImpl getOpModeManager()
/*    */   {
/* 58 */     return null;
/*    */   }
/*    */   
/*    */   public void requestOpModeStop(OpMode opModeToStopIfActive) {}
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\FtcEventLoopIdle.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */