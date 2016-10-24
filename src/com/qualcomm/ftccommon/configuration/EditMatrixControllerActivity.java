/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.os.Bundle;
/*     */ import android.text.Editable;
/*     */ import android.text.TextWatcher;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.CheckBox;
/*     */ import android.widget.EditText;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
/*     */ import java.util.ArrayList;
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
/*     */ public class EditMatrixControllerActivity
/*     */   extends EditActivity
/*     */ {
/*  51 */   public static final RequestCode requestCode = RequestCode.EDIT_MATRIX_CONTROLLER;
/*     */   
/*     */   private MatrixControllerConfiguration matrixControllerConfigurationConfig;
/*     */   private ArrayList<DeviceConfiguration> motors;
/*     */   private ArrayList<DeviceConfiguration> servos;
/*     */   private EditText controller_name;
/*     */   private View info_port1;
/*     */   private View info_port2;
/*     */   private View info_port3;
/*     */   private View info_port4;
/*     */   private View info_port5;
/*     */   private View info_port6;
/*     */   private View info_port7;
/*     */   private View info_port8;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  68 */     super.onCreate(savedInstanceState);
/*  69 */     setContentView(R.layout.matrices);
/*     */     
/*  71 */     this.controller_name = ((EditText)findViewById(R.id.matrixcontroller_name));
/*     */     
/*     */ 
/*  74 */     LinearLayout layout_port1 = (LinearLayout)findViewById(R.id.linearLayout_matrix1);
/*  75 */     this.info_port1 = getLayoutInflater().inflate(R.layout.matrix_devices, layout_port1, true);
/*  76 */     TextView port1 = (TextView)this.info_port1.findViewById(R.id.port_number);
/*  77 */     port1.setText("1");
/*     */     
/*  79 */     LinearLayout layout_port2 = (LinearLayout)findViewById(R.id.linearLayout_matrix2);
/*  80 */     this.info_port2 = getLayoutInflater().inflate(R.layout.matrix_devices, layout_port2, true);
/*  81 */     TextView port2 = (TextView)this.info_port2.findViewById(R.id.port_number);
/*  82 */     port2.setText("2");
/*     */     
/*  84 */     LinearLayout layout_port3 = (LinearLayout)findViewById(R.id.linearLayout_matrix3);
/*  85 */     this.info_port3 = getLayoutInflater().inflate(R.layout.matrix_devices, layout_port3, true);
/*  86 */     TextView port3 = (TextView)this.info_port3.findViewById(R.id.port_number);
/*  87 */     port3.setText("3");
/*     */     
/*  89 */     LinearLayout layout_port4 = (LinearLayout)findViewById(R.id.linearLayout_matrix4);
/*  90 */     this.info_port4 = getLayoutInflater().inflate(R.layout.matrix_devices, layout_port4, true);
/*  91 */     TextView port4 = (TextView)this.info_port4.findViewById(R.id.port_number);
/*  92 */     port4.setText("4");
/*     */     
/*     */ 
/*  95 */     LinearLayout layout_port5 = (LinearLayout)findViewById(R.id.linearLayout_matrix5);
/*  96 */     this.info_port5 = getLayoutInflater().inflate(R.layout.matrix_devices, layout_port5, true);
/*  97 */     TextView port5 = (TextView)this.info_port5.findViewById(R.id.port_number);
/*  98 */     port5.setText("1");
/*     */     
/* 100 */     LinearLayout layout_port6 = (LinearLayout)findViewById(R.id.linearLayout_matrix6);
/* 101 */     this.info_port6 = getLayoutInflater().inflate(R.layout.matrix_devices, layout_port6, true);
/* 102 */     TextView port6 = (TextView)this.info_port6.findViewById(R.id.port_number);
/* 103 */     port6.setText("2");
/*     */     
/* 105 */     LinearLayout layout_port7 = (LinearLayout)findViewById(R.id.linearLayout_matrix7);
/* 106 */     this.info_port7 = getLayoutInflater().inflate(R.layout.matrix_devices, layout_port7, true);
/* 107 */     TextView port7 = (TextView)this.info_port7.findViewById(R.id.port_number);
/* 108 */     port7.setText("3");
/*     */     
/* 110 */     LinearLayout layout_port8 = (LinearLayout)findViewById(R.id.linearLayout_matrix8);
/* 111 */     this.info_port8 = getLayoutInflater().inflate(R.layout.matrix_devices, layout_port8, true);
/* 112 */     TextView port8 = (TextView)this.info_port8.findViewById(R.id.port_number);
/* 113 */     port8.setText("4");
/*     */     
/* 115 */     EditParameters parameters = EditParameters.fromIntent(this, getIntent());
/* 116 */     if (parameters != null) {
/* 117 */       this.matrixControllerConfigurationConfig = ((MatrixControllerConfiguration)parameters.getConfiguration());
/* 118 */       this.motors = ((ArrayList)this.matrixControllerConfigurationConfig.getMotors());
/* 119 */       this.servos = ((ArrayList)this.matrixControllerConfigurationConfig.getServos());
/*     */     }
/*     */     
/* 122 */     this.controller_name.setText(this.matrixControllerConfigurationConfig.getName());
/*     */     
/* 124 */     for (int i = 0; i < this.motors.size(); i++) {
/* 125 */       View info_port = findMotorViewByPort(i + 1);
/* 126 */       addCheckBoxListenerOnPort(i + 1, info_port, this.motors);
/* 127 */       addNameTextChangeWatcherOnPort(info_port, (DeviceConfiguration)this.motors.get(i));
/* 128 */       handleDisabledDevice(i + 1, info_port, this.motors);
/*     */     }
/*     */     
/* 131 */     for (int i = 0; i < this.servos.size(); i++) {
/* 132 */       View info_port = findServoViewByPort(i + 1);
/* 133 */       addCheckBoxListenerOnPort(i + 1, info_port, this.servos);
/* 134 */       addNameTextChangeWatcherOnPort(info_port, (DeviceConfiguration)this.servos.get(i));
/* 135 */       handleDisabledDevice(i + 1, info_port, this.servos);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 141 */     super.onStart();
/*     */   }
/*     */   
/*     */   private void addNameTextChangeWatcherOnPort(View info_port, DeviceConfiguration module) {
/* 145 */     EditText name = (EditText)info_port.findViewById(R.id.editTextResult);
/*     */     
/* 147 */     name.addTextChangedListener(new UsefulTextWatcher(module, null));
/*     */   }
/*     */   
/*     */   private void handleDisabledDevice(int port, View info_port, ArrayList<DeviceConfiguration> list) {
/* 151 */     CheckBox checkbox = (CheckBox)info_port.findViewById(R.id.checkbox_port);
/* 152 */     DeviceConfiguration device = (DeviceConfiguration)list.get(port - 1);
/* 153 */     if (device.isEnabled()) {
/* 154 */       checkbox.setChecked(true);
/* 155 */       EditText name = (EditText)info_port.findViewById(R.id.editTextResult);
/* 156 */       name.setText(device.getName());
/*     */     } else {
/* 158 */       checkbox.setChecked(true);
/*     */       
/*     */ 
/* 161 */       checkbox.performClick();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void addCheckBoxListenerOnPort(int port, View info_port, ArrayList<DeviceConfiguration> list)
/*     */   {
/* 168 */     final EditText name = (EditText)info_port.findViewById(R.id.editTextResult);
/*     */     
/*     */ 
/* 171 */     final DeviceConfiguration device = (DeviceConfiguration)list.get(port - 1);
/*     */     
/* 173 */     CheckBox checkbox = (CheckBox)info_port.findViewById(R.id.checkbox_port);
/* 174 */     checkbox.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 177 */         if (((CheckBox)view).isChecked()) {
/* 178 */           name.setEnabled(true);
/* 179 */           name.setText("");
/* 180 */           device.setEnabled(true);
/* 181 */           device.setName("");
/*     */         } else {
/* 183 */           name.setEnabled(false);
/* 184 */           device.setEnabled(false);
/* 185 */           device.setName(EditMatrixControllerActivity.this.disabledDeviceName());
/* 186 */           name.setText(EditMatrixControllerActivity.this.disabledDeviceName());
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private View findServoViewByPort(int port) {
/* 193 */     switch (port) {
/*     */     case 1: 
/* 195 */       return this.info_port1;
/*     */     case 2: 
/* 197 */       return this.info_port2;
/*     */     case 3: 
/* 199 */       return this.info_port3;
/*     */     case 4: 
/* 201 */       return this.info_port4;
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */   
/*     */   private View findMotorViewByPort(int port)
/*     */   {
/* 208 */     switch (port) {
/*     */     case 1: 
/* 210 */       return this.info_port5;
/*     */     case 2: 
/* 212 */       return this.info_port6;
/*     */     case 3: 
/* 214 */       return this.info_port7;
/*     */     case 4: 
/* 216 */       return this.info_port8;
/*     */     }
/* 218 */     return null;
/*     */   }
/*     */   
/*     */   public void onDoneButtonPressed(View v)
/*     */   {
/* 223 */     finishOk();
/*     */   }
/*     */   
/*     */   protected void finishOk()
/*     */   {
/* 228 */     this.matrixControllerConfigurationConfig.setServos(this.servos);
/* 229 */     this.matrixControllerConfigurationConfig.setMotors(this.motors);
/* 230 */     this.matrixControllerConfigurationConfig.setName(this.controller_name.getText().toString());
/*     */     
/* 232 */     finishOk(new EditParameters(this, this.matrixControllerConfigurationConfig));
/*     */   }
/*     */   
/*     */   public void onCancelButtonPressed(View v) {
/* 236 */     finishCancel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class UsefulTextWatcher
/*     */     implements TextWatcher
/*     */   {
/*     */     private DeviceConfiguration module;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 251 */     private UsefulTextWatcher(DeviceConfiguration module) { this.module = module; }
/*     */     
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void afterTextChanged(Editable editable) {
/* 258 */       String text = editable.toString();
/* 259 */       this.module.setName(text);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditMatrixControllerActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */