/*    */ package com.qualcomm.ftccommon.configuration;
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
/*    */ public enum RequestCode
/*    */ {
/* 43 */   NOTHING(0), 
/* 44 */   EDIT_MOTOR_CONTROLLER(1),  EDIT_SERVO_CONTROLLER(2),  EDIT_LEGACY_MODULE(3), 
/* 45 */   EDIT_DEVICE_INTERFACE_MODULE(4),  EDIT_MATRIX_CONTROLLER(5), 
/* 46 */   EDIT_PWM_PORT(6),  EDIT_I2C_PORT(7),  EDIT_ANALOG_INPUT(8), 
/* 47 */   EDIT_DIGITAL(9),  EDIT_ANALOG_OUTPUT(10), 
/* 48 */   EDIT_I2C_CHANNEL0(13),  EDIT_I2C_CHANNEL1(14),  EDIT_I2C_CHANNEL2(15),  EDIT_I2C_CHANNEL3(16), 
/* 49 */   EDIT_MOTOR_LIST(17),  EDIT_SERVO_LIST(18),  EDIT_SWAP_USB_DEVICES(19), 
/* 50 */   EDIT_FILE(20),  NEW_FILE(21),  AUTO_CONFIGURE(22),  CONFIG_FROM_TEMPLATE(23);
/*    */   
/*    */   public final int value;
/*    */   
/*    */   private RequestCode(int value)
/*    */   {
/* 56 */     this.value = value;
/*    */   }
/*    */   
/*    */   public static RequestCode fromString(String string)
/*    */   {
/* 61 */     for (RequestCode requestCode : )
/*    */     {
/* 63 */       if (requestCode.toString().equals(string))
/*    */       {
/* 65 */         return requestCode;
/*    */       }
/*    */     }
/* 68 */     return NOTHING;
/*    */   }
/*    */   
/*    */   public static RequestCode fromValue(int value)
/*    */   {
/* 73 */     for (RequestCode requestCode : )
/*    */     {
/* 75 */       if (requestCode.value == value)
/*    */       {
/* 77 */         return requestCode;
/*    */       }
/*    */     }
/* 80 */     return NOTHING;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\RequestCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */