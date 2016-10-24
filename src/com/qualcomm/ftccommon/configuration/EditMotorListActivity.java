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
/*    */ public class EditMotorListActivity
/*    */   extends EditPortListCheckboxActivity
/*    */ {
/*    */   public EditMotorListActivity()
/*    */   {
/* 41 */     this.layoutMain = R.layout.motor_list;
/* 42 */     this.idListParentLayout = R.id.item_list_parent;
/* 43 */     this.layoutItem = R.layout.motor;
/* 44 */     this.idItemRowPort = R.id.row_port;
/* 45 */     this.idItemCheckbox = R.id.checkbox;
/* 46 */     this.idItemEditTextResult = R.id.editTextResult;
/* 47 */     this.idItemPortNumber = R.id.port_number;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditMotorListActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */