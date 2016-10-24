/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.view.View;
/*     */ import android.widget.EditText;
/*     */ import android.widget.Spinner;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditPortListSpinnerActivity
/*     */   extends EditPortListActivity
/*     */ {
/*     */   protected int idItemSpinner;
/*     */   
/*     */   protected View createItemViewForPort(int portNumber)
/*     */   {
/*  65 */     View itemView = super.createItemViewForPort(portNumber);
/*  66 */     localizeSpinner(itemView);
/*  67 */     return itemView;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void localizeSpinner(View itemView)
/*     */   {
/*  74 */     Spinner spinner = (Spinner)itemView.findViewById(this.idItemSpinner);
/*  75 */     localizeConfigTypeSpinner(spinner);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void addViewListenersOnIndex(int index)
/*     */   {
/*  82 */     View itemView = findViewByIndex(index);
/*  83 */     DeviceConfiguration config = findConfigByIndex(index);
/*     */     
/*  85 */     addNameTextChangeWatcherOnIndex(index);
/*  86 */     handleDisabledDevice(itemView, config);
/*  87 */     handleSpinner(itemView, this.idItemSpinner, config);
/*     */   }
/*     */   
/*     */   private void handleDisabledDevice(View itemView, DeviceConfiguration deviceConfiguration)
/*     */   {
/*  92 */     EditText name = (EditText)itemView.findViewById(this.idItemEditTextResult);
/*  93 */     if (deviceConfiguration.isEnabled())
/*     */     {
/*  95 */       name.setText(deviceConfiguration.getName());
/*  96 */       name.setEnabled(true);
/*     */     }
/*     */     else
/*     */     {
/* 100 */       name.setText(disabledDeviceName());
/* 101 */       name.setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void clearDevice(View itemView)
/*     */   {
/* 108 */     TextView textViewPortNumber = (TextView)itemView.findViewById(this.idItemPortNumber);
/* 109 */     int portNumber = Integer.parseInt(textViewPortNumber.getText().toString());
/* 110 */     EditText nameText = (EditText)itemView.findViewById(this.idItemEditTextResult);
/*     */     
/* 112 */     nameText.setEnabled(false);
/* 113 */     nameText.setText(disabledDeviceName());
/*     */     
/* 115 */     DeviceConfiguration config = findConfigByPort(portNumber);
/* 116 */     config.setEnabled(false);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void changeDevice(View itemView, ConfigurationType type)
/*     */   {
/* 122 */     TextView textViewPortNumber = (TextView)itemView.findViewById(this.idItemPortNumber);
/* 123 */     int portNumber = Integer.parseInt(textViewPortNumber.getText().toString());
/* 124 */     EditText nameText = (EditText)itemView.findViewById(this.idItemEditTextResult);
/*     */     
/* 126 */     nameText.setEnabled(true);
/*     */     
/* 128 */     DeviceConfiguration config = findConfigByPort(portNumber);
/* 129 */     clearNameIfNecessary(nameText, config);
/* 130 */     config.setType(type);
/* 131 */     config.setEnabled(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditPortListSpinnerActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */