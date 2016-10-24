/*    */ package com.qualcomm.ftccommon.configuration;
/*    */ 
/*    */ import com.qualcomm.ftccommon.R.layout;
/*    */ import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
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
/*    */ public class EditServoControllerActivity
/*    */   extends EditServoListActivity
/*    */ {
/* 70 */   public static final RequestCode requestCode = RequestCode.EDIT_SERVO_CONTROLLER;
/*    */   
/*    */ 
/*    */   public EditServoControllerActivity()
/*    */   {
/* 75 */     this.layoutControllerNameBanner = R.layout.servo_controller_banner;
/*    */   }
/*    */   
/*    */ 
/*    */   public void finishOk()
/*    */   {
/* 81 */     ((ServoControllerConfiguration)this.controllerConfiguration).setServos(this.configList);
/* 82 */     super.finishOk();
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditServoControllerActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */