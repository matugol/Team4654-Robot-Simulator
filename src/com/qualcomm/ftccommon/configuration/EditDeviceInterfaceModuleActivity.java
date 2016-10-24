/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Intent;
/*     */ import android.content.res.Resources;
/*     */ import android.os.Bundle;
/*     */ import android.view.View;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemClickListener;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.EditText;
/*     */ import android.widget.ListView;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.array;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.UserSensorType.Flavor;
/*     */ import com.qualcomm.robotcore.hardware.configuration.UserSensorTypeManager;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public class EditDeviceInterfaceModuleActivity
/*     */   extends EditUSBDeviceActivity
/*     */ {
/*  59 */   public static final RequestCode requestCode = RequestCode.EDIT_DEVICE_INTERFACE_MODULE;
/*     */   
/*     */ 
/*     */   private DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration;
/*     */   
/*     */ 
/*     */   private EditText device_interface_module_name;
/*     */   
/*     */ 
/*     */   private EditActivity.DisplayNameAndRequestCode[] listKeys;
/*     */   
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  73 */     super.onCreate(savedInstanceState);
/*  74 */     setContentView(R.layout.device_interface_module);
/*     */     
/*  76 */     String[] strings = getResources().getStringArray(R.array.device_interface_module_options_array);
/*  77 */     this.listKeys = EditActivity.DisplayNameAndRequestCode.fromArray(strings);
/*     */     
/*  79 */     ListView listView = (ListView)findViewById(R.id.listView_devices);
/*  80 */     listView.setAdapter(new ArrayAdapter(this, 17367043, this.listKeys));
/*  81 */     listView.setOnItemClickListener(this.editLaunchListener);
/*     */     
/*  83 */     this.device_interface_module_name = ((EditText)findViewById(R.id.device_interface_module_name));
/*     */     
/*  85 */     EditParameters parameters = EditParameters.fromIntent(this, getIntent());
/*  86 */     deserialize(parameters);
/*     */     
/*  88 */     this.device_interface_module_name.addTextChangedListener(new EditActivity.SetNameTextWatcher(this, this.controllerConfiguration));
/*  89 */     this.device_interface_module_name.setText(this.controllerConfiguration.getName());
/*     */     
/*  91 */     showFixSwapButtons();
/*  92 */     this.deviceInterfaceModuleConfiguration = ((DeviceInterfaceModuleConfiguration)this.controllerConfiguration);
/*     */   }
/*     */   
/*     */   protected void refreshSerialNumber() {
/*  96 */     TextView serialNumberView = (TextView)findViewById(R.id.serialNumber);
/*  97 */     serialNumberView.setText(formatSerialNumber(this, this.controllerConfiguration));
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 102 */     super.onStart();
/*     */   }
/*     */   
/* 105 */   private AdapterView.OnItemClickListener editLaunchListener = new AdapterView.OnItemClickListener()
/*     */   {
/*     */     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/* 108 */       EditActivity.DisplayNameAndRequestCode key = EditDeviceInterfaceModuleActivity.this.listKeys[position];
/*     */       
/* 110 */       if (key.requestCode == EditPWMDevicesActivity.requestCode) {
/* 111 */         EditDeviceInterfaceModuleActivity.this.handleLaunchEdit(key.requestCode, EditPWMDevicesActivity.class, EditDeviceInterfaceModuleActivity.this.deviceInterfaceModuleConfiguration.getPwmOutputs());
/* 112 */       } else if (key.requestCode == EditI2cDevicesActivity.requestCode) {
/* 113 */         EditParameters parameters = new EditParameters(EditDeviceInterfaceModuleActivity.this, EditDeviceInterfaceModuleActivity.this.deviceInterfaceModuleConfiguration.getI2cDevices(), 6);
/*     */         
/* 115 */         List<ConfigurationType> list = new LinkedList();
/* 116 */         list.add(BuiltInConfigurationType.NOTHING);
/* 117 */         list.add(BuiltInConfigurationType.IR_SEEKER_V3);
/* 118 */         list.add(BuiltInConfigurationType.COLOR_SENSOR);
/* 119 */         list.add(BuiltInConfigurationType.ADAFRUIT_COLOR_SENSOR);
/* 120 */         list.add(BuiltInConfigurationType.GYRO);
/* 121 */         list.add(BuiltInConfigurationType.I2C_DEVICE);
/* 122 */         list.add(BuiltInConfigurationType.I2C_DEVICE_SYNCH);
/* 123 */         list.addAll(UserSensorTypeManager.getInstance().allUserTypes(UserSensorType.Flavor.I2C));
/* 124 */         parameters.setConfigurationTypes((ConfigurationType[])list.toArray(new ConfigurationType[list.size()]));
/*     */         
/* 126 */         EditDeviceInterfaceModuleActivity.this.handleLaunchEdit(key.requestCode, EditI2cDevicesActivity.class, parameters);
/* 127 */       } else if (key.requestCode == EditAnalogInputDevicesActivity.requestCode) {
/* 128 */         EditDeviceInterfaceModuleActivity.this.handleLaunchEdit(key.requestCode, EditAnalogInputDevicesActivity.class, EditDeviceInterfaceModuleActivity.this.deviceInterfaceModuleConfiguration.getAnalogInputDevices());
/* 129 */       } else if (key.requestCode == EditDigitalDevicesActivity.requestCode) {
/* 130 */         EditDeviceInterfaceModuleActivity.this.handleLaunchEdit(key.requestCode, EditDigitalDevicesActivity.class, EditDeviceInterfaceModuleActivity.this.deviceInterfaceModuleConfiguration.getDigitalDevices());
/* 131 */       } else if (key.requestCode == EditAnalogOutputDevicesActivity.requestCode) {
/* 132 */         EditDeviceInterfaceModuleActivity.this.handleLaunchEdit(key.requestCode, EditAnalogOutputDevicesActivity.class, EditDeviceInterfaceModuleActivity.this.deviceInterfaceModuleConfiguration.getAnalogOutputDevices());
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   protected void onActivityResult(int requestCodeValue, int resultCode, Intent data)
/*     */   {
/* 139 */     logActivityResult(requestCodeValue, resultCode, data);
/*     */     
/* 141 */     RequestCode requestCode = RequestCode.fromValue(requestCodeValue);
/* 142 */     if (resultCode == -1) {
/* 143 */       EditParameters parameters = EditParameters.fromIntent(this, data);
/* 144 */       if (requestCode == EditSwapUsbDevices.requestCode) {
/* 145 */         completeSwapConfiguration(requestCodeValue, resultCode, data);
/* 146 */       } else if (requestCode == EditPWMDevicesActivity.requestCode) {
/* 147 */         this.deviceInterfaceModuleConfiguration.setPwmOutputs(parameters.getCurrentItems());
/* 148 */       } else if (requestCode == EditAnalogInputDevicesActivity.requestCode) {
/* 149 */         this.deviceInterfaceModuleConfiguration.setAnalogInputDevices(parameters.getCurrentItems());
/* 150 */       } else if (requestCode == EditDigitalDevicesActivity.requestCode) {
/* 151 */         this.deviceInterfaceModuleConfiguration.setDigitalDevices(parameters.getCurrentItems());
/* 152 */       } else if (requestCode == EditI2cDevicesActivity.requestCode) {
/* 153 */         this.deviceInterfaceModuleConfiguration.setI2cDevices(parameters.getCurrentItems());
/* 154 */       } else if (requestCode == EditAnalogOutputDevicesActivity.requestCode) {
/* 155 */         this.deviceInterfaceModuleConfiguration.setAnalogOutputDevices(parameters.getCurrentItems());
/*     */       }
/* 157 */       this.currentCfgFile.markDirty();
/* 158 */       this.robotConfigFileManager.setActiveConfigAndUpdateUI(this.currentCfgFile);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onDoneButtonPressed(View v) {
/* 163 */     finishOk();
/*     */   }
/*     */   
/*     */   protected void finishOk()
/*     */   {
/* 168 */     this.controllerConfiguration.setName(this.device_interface_module_name.getText().toString());
/* 169 */     finishOk(new EditParameters(this, this.controllerConfiguration, getRobotConfigMap()));
/*     */   }
/*     */   
/*     */   public void onCancelButtonPressed(View v) {
/* 173 */     finishCancel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onFixButtonPressed(View v)
/*     */   {
/* 181 */     fixConfiguration();
/*     */   }
/*     */   
/*     */   public void onSwapButtonPressed(View view) {
/* 185 */     swapConfiguration();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditDeviceInterfaceModuleActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */