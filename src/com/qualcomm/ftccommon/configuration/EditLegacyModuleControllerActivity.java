/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.text.Editable;
/*     */ import android.text.TextWatcher;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewParent;
/*     */ import android.widget.Button;
/*     */ import android.widget.EditText;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.Spinner;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
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
/*     */ 
/*     */ 
/*     */ public class EditLegacyModuleControllerActivity
/*     */   extends EditUSBDeviceActivity
/*     */ {
/*  64 */   public static final RequestCode requestCode = RequestCode.EDIT_LEGACY_MODULE;
/*     */   
/*  66 */   private static boolean DEBUG = false;
/*     */   private EditText controller_name;
/*     */   
/*  69 */   public EditLegacyModuleControllerActivity() { this.devices = new ArrayList(); }
/*     */   
/*     */ 
/*     */   private ArrayList<DeviceConfiguration> devices;
/*     */   
/*     */   private View info_port0;
/*     */   
/*     */   private View info_port1;
/*     */   
/*     */   private View info_port2;
/*     */   
/*     */   private View info_port3;
/*     */   
/*     */   private View info_port4;
/*     */   private View info_port5;
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  86 */     super.onCreate(savedInstanceState);
/*  87 */     setContentView(R.layout.legacy);
/*     */     
/*  89 */     this.info_port0 = createPortView(R.id.linearLayout0, 0);
/*  90 */     this.info_port1 = createPortView(R.id.linearLayout1, 1);
/*  91 */     this.info_port2 = createPortView(R.id.linearLayout2, 2);
/*  92 */     this.info_port3 = createPortView(R.id.linearLayout3, 3);
/*  93 */     this.info_port4 = createPortView(R.id.linearLayout4, 4);
/*  94 */     this.info_port5 = createPortView(R.id.linearLayout5, 5);
/*     */     
/*  96 */     this.controller_name = ((EditText)findViewById(R.id.device_interface_module_name));
/*     */     
/*  98 */     EditParameters parameters = EditParameters.fromIntent(this, getIntent());
/*  99 */     deserialize(parameters);
/*     */     
/* 101 */     this.devices = ((ArrayList)this.controllerConfiguration.getDevices());
/*     */     
/* 103 */     this.controller_name.setText(this.controllerConfiguration.getName());
/* 104 */     this.controller_name.addTextChangedListener(new UsefulTextWatcher(null));
/*     */     
/* 106 */     showFixSwapButtons();
/*     */     
/* 108 */     for (int i = 0; i < this.devices.size(); i++) {
/* 109 */       DeviceConfiguration device = (DeviceConfiguration)this.devices.get(i);
/* 110 */       if (DEBUG) RobotLog.e("[onStart] device name: " + device.getName() + ", port: " + device.getPort() + ", type: " + device.getType());
/* 111 */       populatePort(findViewByPort(i), device);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void refreshSerialNumber() {
/* 116 */     TextView serialNumberView = (TextView)findViewById(R.id.serialNumber);
/* 117 */     serialNumberView.setText(formatSerialNumber(this, this.controllerConfiguration));
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 122 */     super.onStart();
/*     */   }
/*     */   
/*     */   private View createPortView(int id, int portNumber) {
/* 126 */     LinearLayout layout = (LinearLayout)findViewById(id);
/* 127 */     View result = getLayoutInflater().inflate(R.layout.simple_device, layout, true);
/* 128 */     TextView port = (TextView)result.findViewById(R.id.portNumber);
/* 129 */     port.setText(String.format("%d", new Object[] { Integer.valueOf(portNumber) }));
/*     */     
/* 131 */     Spinner spinner = (Spinner)result.findViewById(R.id.choiceSpinner);
/* 132 */     localizeConfigTypeSpinner(spinner);
/*     */     
/* 134 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onActivityResult(int requestCodeValue, int resultCode, Intent data)
/*     */   {
/* 140 */     logActivityResult(requestCodeValue, resultCode, data);
/*     */     
/* 142 */     if (resultCode == -1) {
/* 143 */       EditParameters parameters = EditParameters.fromIntent(this, data);
/* 144 */       RequestCode requestCode = RequestCode.fromValue(requestCodeValue);
/*     */       
/* 146 */       if (requestCode == EditSwapUsbDevices.requestCode) {
/* 147 */         completeSwapConfiguration(requestCodeValue, resultCode, data);
/*     */       } else {
/* 149 */         ControllerConfiguration newC = (ControllerConfiguration)parameters.getConfiguration();
/* 150 */         setModule(newC);
/* 151 */         populatePort(findViewByPort(newC.getPort()), (DeviceConfiguration)this.devices.get(newC.getPort()));
/*     */       }
/*     */       
/* 154 */       this.currentCfgFile.markDirty();
/* 155 */       this.robotConfigFileManager.setActiveConfigAndUpdateUI(this.currentCfgFile);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void editController_general(DeviceConfiguration controller)
/*     */   {
/* 165 */     LinearLayout layout = (LinearLayout)findViewByPort(controller.getPort());
/* 166 */     EditText nameText = (EditText)layout.findViewById(R.id.editTextResult);
/* 167 */     controller.setName(nameText.getText().toString());
/*     */     
/* 169 */     if (controller.getType() == BuiltInConfigurationType.MOTOR_CONTROLLER) {
/* 170 */       EditParameters parameters = new EditParameters(this, controller, ((MotorControllerConfiguration)controller).getMotors());
/* 171 */       parameters.setInitialPortNumber(1);
/* 172 */       handleLaunchEdit(EditMotorControllerActivity.requestCode, EditMotorControllerActivity.class, parameters);
/*     */     }
/* 174 */     else if (controller.getType() == BuiltInConfigurationType.SERVO_CONTROLLER) {
/* 175 */       EditParameters parameters = new EditParameters(this, controller, ((ServoControllerConfiguration)controller).getServos());
/* 176 */       parameters.setInitialPortNumber(1);
/* 177 */       handleLaunchEdit(EditServoControllerActivity.requestCode, EditServoControllerActivity.class, parameters);
/*     */     }
/* 179 */     else if (controller.getType() == BuiltInConfigurationType.MATRIX_CONTROLLER) {
/* 180 */       handleLaunchEdit(EditMatrixControllerActivity.requestCode, EditMatrixControllerActivity.class, controller);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onDoneButtonPressed(View v) {
/* 185 */     finishOk();
/*     */   }
/*     */   
/*     */   protected void finishOk()
/*     */   {
/* 190 */     this.controllerConfiguration.setName(this.controller_name.getText().toString());
/* 191 */     finishOk(new EditParameters(this, this.controllerConfiguration, getRobotConfigMap()));
/*     */   }
/*     */   
/*     */   public void onCancelButtonPressed(View v) {
/* 195 */     finishCancel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onFixButtonPressed(View v)
/*     */   {
/* 203 */     fixConfiguration();
/*     */   }
/*     */   
/*     */   public void onSwapButtonPressed(View view) {
/* 207 */     swapConfiguration();
/*     */   }
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
/*     */   private void populatePort(View v, DeviceConfiguration device)
/*     */   {
/* 222 */     handleSpinner(v, R.id.choiceSpinner, device, true);
/*     */     
/* 224 */     String name = device.getName();
/* 225 */     EditText nameText = (EditText)v.findViewById(R.id.editTextResult);
/*     */     
/* 227 */     TextView portNumber = (TextView)v.findViewById(R.id.portNumber);
/* 228 */     int port = Integer.parseInt(portNumber.getText().toString());
/* 229 */     nameText.addTextChangedListener(new UsefulTextWatcher(findViewByPort(port), null));
/* 230 */     nameText.setText(name);
/*     */     
/* 232 */     if (DEBUG) { RobotLog.e("[populatePort] name: " + name + ", port: " + port + ", type: " + device.getType());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void clearDevice(View itemView)
/*     */   {
/* 241 */     TextView portNumber = (TextView)itemView.findViewById(R.id.portNumber);
/* 242 */     int port = Integer.parseInt(portNumber.getText().toString());
/* 243 */     EditText nameText = (EditText)itemView.findViewById(R.id.editTextResult);
/*     */     
/* 245 */     nameText.setEnabled(false);
/* 246 */     nameText.setText(disabledDeviceName());
/*     */     
/* 248 */     DeviceConfiguration newModule = new DeviceConfiguration(BuiltInConfigurationType.NOTHING);
/* 249 */     newModule.setPort(port);
/* 250 */     setModule(newModule);
/*     */     
/* 252 */     setButtonVisibility(port, 8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void changeDevice(View itemView, ConfigurationType type)
/*     */   {
/* 261 */     TextView portNumber = (TextView)itemView.findViewById(R.id.portNumber);
/* 262 */     int port = Integer.parseInt(portNumber.getText().toString());
/* 263 */     EditText nameText = (EditText)itemView.findViewById(R.id.editTextResult);
/* 264 */     DeviceConfiguration currentModule = (DeviceConfiguration)this.devices.get(port);
/*     */     
/* 266 */     nameText.setEnabled(true);
/* 267 */     clearNameIfNecessary(nameText, currentModule);
/*     */     
/* 269 */     if ((type == BuiltInConfigurationType.MOTOR_CONTROLLER) || (type == BuiltInConfigurationType.SERVO_CONTROLLER) || (type == BuiltInConfigurationType.MATRIX_CONTROLLER))
/*     */     {
/*     */ 
/* 272 */       createController(port, type);
/* 273 */       setButtonVisibility(port, 0);
/*     */     } else {
/* 275 */       currentModule.setType(type);
/* 276 */       if (type == BuiltInConfigurationType.NOTHING) {
/* 277 */         currentModule.setEnabled(false);
/*     */       } else {
/* 279 */         currentModule.setEnabled(true);
/*     */       }
/* 281 */       setButtonVisibility(port, 8);
/*     */     }
/*     */     
/* 284 */     if (DEBUG) {
/* 285 */       DeviceConfiguration module = (DeviceConfiguration)this.devices.get(port);
/* 286 */       RobotLog.e("[changeDevice] modules.get(port) name: " + module.getName() + ", port: " + module.getPort() + ", type: " + module.getType());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void createController(int port, ConfigurationType newType)
/*     */   {
/* 300 */     DeviceConfiguration currentModule = (DeviceConfiguration)this.devices.get(port);
/*     */     
/* 302 */     String name = currentModule.getName();
/* 303 */     ArrayList<DeviceConfiguration> devices = new ArrayList();
/* 304 */     SerialNumber serialNumber = new SerialNumber();
/*     */     
/* 306 */     ConfigurationType currentType = currentModule.getType();
/* 307 */     if (currentType != newType) {
/*     */       ControllerConfiguration newModule;
/* 309 */       if (newType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
/* 310 */         for (int motorPortNumber = 1; motorPortNumber <= 2; motorPortNumber++) {
/* 311 */           devices.add(new MotorConfiguration(motorPortNumber));
/*     */         }
/* 313 */         ControllerConfiguration newModule = new MotorControllerConfiguration(name, devices, serialNumber);
/* 314 */         newModule.setPort(port);
/*     */       }
/* 316 */       else if (newType == BuiltInConfigurationType.SERVO_CONTROLLER) {
/* 317 */         for (int servoPortNumber = 1; servoPortNumber <= 6; servoPortNumber++) {
/* 318 */           devices.add(new ServoConfiguration(servoPortNumber));
/*     */         }
/* 320 */         ControllerConfiguration newModule = new ServoControllerConfiguration(name, devices, serialNumber);
/* 321 */         newModule.setPort(port);
/*     */       }
/* 323 */       else if (newType == BuiltInConfigurationType.MATRIX_CONTROLLER) {
/* 324 */         ArrayList<DeviceConfiguration> motors = new ArrayList();
/* 325 */         for (int motorPortNumber = 1; motorPortNumber <= 4; motorPortNumber++) {
/* 326 */           motors.add(new MotorConfiguration(motorPortNumber));
/*     */         }
/*     */         
/* 329 */         ArrayList<DeviceConfiguration> servos = new ArrayList();
/* 330 */         for (int servoPortNumber = 1; servoPortNumber <= 4; servoPortNumber++) {
/* 331 */           servos.add(new ServoConfiguration(servoPortNumber));
/*     */         }
/* 333 */         ControllerConfiguration newModule = new MatrixControllerConfiguration(name, motors, servos, serialNumber);
/* 334 */         newModule.setPort(port);
/*     */       }
/*     */       else {
/* 337 */         newModule = null;
/*     */       }
/* 339 */       if (newModule != null) {
/* 340 */         newModule.setEnabled(true);
/* 341 */         setModule(newModule);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void editController_portALL(View v)
/*     */   {
/* 355 */     LinearLayout layout = (LinearLayout)v.getParent().getParent();
/* 356 */     TextView portNumber = (TextView)layout.findViewById(R.id.portNumber);
/* 357 */     int port = Integer.parseInt(portNumber.getText().toString());
/* 358 */     DeviceConfiguration currentModule = (DeviceConfiguration)this.devices.get(port);
/*     */     
/* 360 */     if (!isController(currentModule)) {
/* 361 */       Spinner choiceSpinner = (Spinner)layout.findViewById(R.id.choiceSpinner);
/* 362 */       EditActivity.ConfigurationTypeAndDisplayName pair = (EditActivity.ConfigurationTypeAndDisplayName)choiceSpinner.getSelectedItem();
/* 363 */       createController(port, pair.configurationType);
/*     */     }
/* 365 */     editController_general(currentModule);
/*     */   }
/*     */   
/*     */   private void setModule(DeviceConfiguration dev) {
/* 369 */     int port = dev.getPort();
/* 370 */     this.devices.set(port, dev);
/*     */   }
/*     */   
/*     */ 
/*     */   private View findViewByPort(int port)
/*     */   {
/* 376 */     switch (port) {
/* 377 */     case 0:  return this.info_port0;
/* 378 */     case 1:  return this.info_port1;
/* 379 */     case 2:  return this.info_port2;
/* 380 */     case 3:  return this.info_port3;
/* 381 */     case 4:  return this.info_port4;
/* 382 */     case 5:  return this.info_port5; }
/* 383 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setButtonVisibility(int port, int visibility)
/*     */   {
/* 393 */     View layout = findViewByPort(port);
/* 394 */     Button button = (Button)layout.findViewById(R.id.edit_controller_btn);
/* 395 */     button.setVisibility(visibility);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isController(DeviceConfiguration module)
/*     */   {
/* 404 */     return (module.getType() == BuiltInConfigurationType.MOTOR_CONTROLLER) || (module.getType() == BuiltInConfigurationType.SERVO_CONTROLLER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class UsefulTextWatcher
/*     */     implements TextWatcher
/*     */   {
/*     */     private int port;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 419 */     private boolean isController = false;
/*     */     
/*     */ 
/* 422 */     private UsefulTextWatcher() { this.isController = true; }
/*     */     
/*     */     private UsefulTextWatcher(View layout) {
/* 425 */       TextView portNumber = (TextView)layout.findViewById(R.id.portNumber);
/* 426 */       this.port = Integer.parseInt(portNumber.getText().toString());
/*     */     }
/*     */     
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/* 433 */     public void afterTextChanged(Editable editable) { String text = editable.toString();
/* 434 */       if (this.isController) {
/* 435 */         EditLegacyModuleControllerActivity.this.controllerConfiguration.setName(text);
/*     */       } else {
/* 437 */         DeviceConfiguration dev = (DeviceConfiguration)EditLegacyModuleControllerActivity.this.devices.get(this.port);
/* 438 */         dev.setName(text);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditLegacyModuleControllerActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */