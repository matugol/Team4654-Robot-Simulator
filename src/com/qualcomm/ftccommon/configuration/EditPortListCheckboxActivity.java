/*    */ package com.qualcomm.ftccommon.configuration;
/*    */ 
/*    */ import android.view.View;
/*    */ import android.view.View.OnClickListener;
/*    */ import android.widget.CheckBox;
/*    */ import android.widget.EditText;
/*    */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*    */ import java.util.List;
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
/*    */ public abstract class EditPortListCheckboxActivity
/*    */   extends EditPortListActivity
/*    */ {
/*    */   protected int idItemCheckbox;
/*    */   
/*    */   protected void addViewListenersOnIndex(int index)
/*    */   {
/* 64 */     addCheckBoxListenerOnIndex(index);
/* 65 */     addNameTextChangeWatcherOnIndex(index);
/* 66 */     handleDisabledDeviceByIndex(index);
/*    */   }
/*    */   
/*    */   protected void handleDisabledDeviceByIndex(int index)
/*    */   {
/* 71 */     View itemView = findViewByIndex(index);
/* 72 */     CheckBox checkbox = (CheckBox)itemView.findViewById(this.idItemCheckbox);
/* 73 */     DeviceConfiguration device = (DeviceConfiguration)this.configList.get(index);
/* 74 */     if (device.isEnabled())
/*    */     {
/* 76 */       checkbox.setChecked(true);
/* 77 */       EditText name = (EditText)itemView.findViewById(this.idItemEditTextResult);
/* 78 */       name.setText(device.getName());
/*    */     }
/*    */     else
/*    */     {
/* 82 */       checkbox.setChecked(true);
/*    */       
/*    */ 
/* 85 */       checkbox.performClick();
/*    */     }
/*    */   }
/*    */   
/*    */   protected void addCheckBoxListenerOnIndex(int index)
/*    */   {
/* 91 */     View itemView = findViewByIndex(index);
/*    */     
/* 93 */     final EditText name = (EditText)itemView.findViewById(this.idItemEditTextResult);
/*    */     
/*    */ 
/* 96 */     final DeviceConfiguration device = (DeviceConfiguration)this.configList.get(index);
/*    */     
/* 98 */     CheckBox checkbox = (CheckBox)itemView.findViewById(this.idItemCheckbox);
/* 99 */     checkbox.setOnClickListener(new View.OnClickListener()
/*    */     {
/*    */ 
/*    */       public void onClick(View view)
/*    */       {
/* :4 */         if (((CheckBox)view).isChecked())
/*    */         {
/* :6 */           name.setEnabled(true);
/* :7 */           name.setText("");
/* :8 */           device.setEnabled(true);
/* :9 */           device.setName("");
/*    */         }
/*    */         else
/*    */         {
/* ;3 */           name.setEnabled(false);
/* ;4 */           name.setText(EditPortListCheckboxActivity.this.disabledDeviceName());
/* ;5 */           device.setEnabled(false);
/* ;6 */           device.setName(EditPortListCheckboxActivity.this.disabledDeviceName());
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditPortListCheckboxActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */