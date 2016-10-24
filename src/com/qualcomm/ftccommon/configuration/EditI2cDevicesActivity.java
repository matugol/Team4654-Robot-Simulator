/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.view.View;
/*     */ import android.widget.Spinner;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import java.util.Arrays;
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
/*     */ public class EditI2cDevicesActivity
/*     */   extends EditPortListSpinnerActivity
/*     */ {
/*  80 */   public static final RequestCode requestCode = RequestCode.EDIT_I2C_PORT;
/*     */   
/*  82 */   ConfigurationType[] configurationTypes = new ConfigurationType[0];
/*     */   
/*     */   public EditI2cDevicesActivity()
/*     */   {
/*  86 */     this.layoutMain = R.layout.i2cs;
/*  87 */     this.idListParentLayout = R.id.item_list_parent;
/*  88 */     this.layoutItem = R.layout.i2c_device;
/*  89 */     this.idItemRowPort = R.id.row_port_i2c;
/*  90 */     this.idItemSpinner = R.id.choiceSpinner;
/*  91 */     this.idItemEditTextResult = R.id.editTextResult;
/*  92 */     this.idItemPortNumber = R.id.port_number;
/*     */   }
/*     */   
/*     */   protected void deserialize(EditParameters parameters)
/*     */   {
/*  97 */     super.deserialize(parameters);
/*  98 */     if (parameters.getConfigurationTypes() != null)
/*     */     {
/* 100 */       this.configurationTypes = parameters.getConfigurationTypes();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void localizeSpinner(View itemView)
/*     */   {
/* 107 */     Spinner spinner = (Spinner)itemView.findViewById(this.idItemSpinner);
/* 108 */     localizeConfigTypeSpinnerTypes(spinner, Arrays.asList(this.configurationTypes));
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditI2cDevicesActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */