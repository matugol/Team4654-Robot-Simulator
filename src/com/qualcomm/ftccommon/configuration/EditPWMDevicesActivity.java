/*    */ package com.qualcomm.ftccommon.configuration;
/*    */ 
/*    */ import com.qualcomm.ftccommon.R.id;
/*    */ import com.qualcomm.ftccommon.R.layout;
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
/*    */ public class EditPWMDevicesActivity
/*    */   extends EditPortListCheckboxActivity
/*    */ {
/* 70 */   public static final RequestCode requestCode = RequestCode.EDIT_PWM_PORT;
/*    */   
/*    */   public EditPWMDevicesActivity()
/*    */   {
/* 74 */     this.layoutMain = R.layout.pwms;
/* 75 */     this.idListParentLayout = R.id.item_list_parent;
/* 76 */     this.layoutItem = R.layout.pwm_device;
/* 77 */     this.idItemRowPort = R.id.row_port;
/* 78 */     this.idItemCheckbox = R.id.checkbox_port;
/* 79 */     this.idItemEditTextResult = R.id.editTextResult;
/* 80 */     this.idItemPortNumber = R.id.port_number;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditPWMDevicesActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */