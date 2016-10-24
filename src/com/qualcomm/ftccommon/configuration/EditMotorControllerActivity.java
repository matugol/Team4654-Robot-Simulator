/*    */ package com.qualcomm.ftccommon.configuration;
/*    */ 
/*    */ import com.qualcomm.ftccommon.R.layout;
/*    */ import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
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
/*    */ public class EditMotorControllerActivity
/*    */   extends EditMotorListActivity
/*    */ {
/* 38 */   public static final RequestCode requestCode = RequestCode.EDIT_MOTOR_CONTROLLER;
/*    */   
/*    */ 
/*    */   public EditMotorControllerActivity()
/*    */   {
/* 43 */     this.layoutControllerNameBanner = R.layout.motor_controller_banner;
/*    */   }
/*    */   
/*    */ 
/*    */   public void finishOk()
/*    */   {
/* 49 */     ((MotorControllerConfiguration)this.controllerConfiguration).setMotors(this.configList);
/* 50 */     super.finishOk();
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditMotorControllerActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */