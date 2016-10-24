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
/*    */ public class EditServoListActivity
/*    */   extends EditPortListSpinnerActivity
/*    */ {
/*    */   public EditServoListActivity()
/*    */   {
/* 41 */     this.layoutMain = R.layout.servo_list;
/* 42 */     this.idListParentLayout = R.id.item_list_parent;
/* 43 */     this.layoutItem = R.layout.servo;
/* 44 */     this.idItemRowPort = R.id.row_port;
/* 45 */     this.idItemSpinner = R.id.choiceSpinner;
/* 46 */     this.idItemEditTextResult = R.id.editTextResult;
/* 47 */     this.idItemPortNumber = R.id.port_number;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditServoListActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */