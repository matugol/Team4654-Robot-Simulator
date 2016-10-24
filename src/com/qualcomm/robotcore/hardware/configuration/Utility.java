/*     */ package com.qualcomm.robotcore.hardware.configuration;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.AlertDialog.Builder;
/*     */ import android.support.annotation.IdRes;
/*     */ import android.support.annotation.LayoutRes;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.Button;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import junit.framework.Assert;
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
/*     */ public class Utility
/*     */ {
/*     */   private Activity activity;
/*  57 */   private static int count = 1;
/*     */   
/*     */   public Utility(Activity activity) {
/*  60 */     this.activity = activity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createLists(Set<Map.Entry<SerialNumber, DeviceManager.DeviceType>> entries, Map<SerialNumber, ControllerConfiguration> deviceControllers)
/*     */   {
/*  71 */     for (Map.Entry<SerialNumber, DeviceManager.DeviceType> entry : entries) {
/*  72 */       SerialNumber serialNumber = (SerialNumber)entry.getKey();
/*  73 */       DeviceManager.DeviceType enumVal = (DeviceManager.DeviceType)entry.getValue();
/*  74 */       switch (enumVal) {
/*     */       case MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER: 
/*  76 */         deviceControllers.put(serialNumber, buildModernMotorController(serialNumber));
/*  77 */         break;
/*     */       case MODERN_ROBOTICS_USB_SERVO_CONTROLLER: 
/*  79 */         deviceControllers.put(serialNumber, buildModernServoController(serialNumber));
/*  80 */         break;
/*     */       case MODERN_ROBOTICS_USB_LEGACY_MODULE: 
/*  82 */         deviceControllers.put(serialNumber, buildLegacyModule(serialNumber));
/*  83 */         break;
/*     */       case MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE: 
/*  85 */         deviceControllers.put(serialNumber, buildDeviceInterfaceModule(serialNumber));
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public DeviceInterfaceModuleConfiguration buildDeviceInterfaceModule(SerialNumber serialNumber)
/*     */   {
/*  94 */     DeviceInterfaceModuleConfiguration deviceInterfaceModule = new DeviceInterfaceModuleConfiguration("Device Interface Module " + count, serialNumber);
/*  95 */     deviceInterfaceModule.setPwmOutputs(createPWMList(2));
/*  96 */     deviceInterfaceModule.setI2cDevices(createI2CList(6));
/*  97 */     deviceInterfaceModule.setAnalogInputDevices(createAnalogInputList(8));
/*  98 */     deviceInterfaceModule.setDigitalDevices(createDigitalList(8));
/*  99 */     deviceInterfaceModule.setAnalogOutputDevices(createAnalogOutputList(2));
/* 100 */     count += 1;
/* 101 */     return deviceInterfaceModule;
/*     */   }
/*     */   
/*     */   public LegacyModuleControllerConfiguration buildLegacyModule(SerialNumber serialNumber)
/*     */   {
/* 106 */     List<DeviceConfiguration> legacies = createLegacyModuleList();
/* 107 */     LegacyModuleControllerConfiguration legacyModule = new LegacyModuleControllerConfiguration("Legacy Module " + count, legacies, serialNumber);
/* 108 */     count += 1;
/* 109 */     return legacyModule;
/*     */   }
/*     */   
/*     */   public ServoControllerConfiguration buildModernServoController(SerialNumber serialNumber) {
/* 113 */     ArrayList<DeviceConfiguration> servos = createServoList(6);
/* 114 */     ServoControllerConfiguration servoController = new ServoControllerConfiguration("Servo Controller " + count, servos, serialNumber);
/* 115 */     count += 1;
/* 116 */     return servoController;
/*     */   }
/*     */   
/*     */   public MotorControllerConfiguration buildModernMotorController(SerialNumber serialNumber) {
/* 120 */     ArrayList<DeviceConfiguration> motors = createMotorList(2);
/* 121 */     MotorControllerConfiguration motorController = new MotorControllerConfiguration("Motor Controller " + count, motors, serialNumber);
/* 122 */     count += 1;
/* 123 */     return motorController;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<DeviceConfiguration> createMotorList(int count)
/*     */   {
/* 131 */     ArrayList<DeviceConfiguration> motors = new ArrayList();
/* 132 */     for (int motor = 1; motor <= count; motor++) {
/* 133 */       MotorConfiguration conf = new MotorConfiguration(motor);
/* 134 */       motors.add(conf);
/*     */     }
/* 136 */     return motors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<DeviceConfiguration> createServoList(int count)
/*     */   {
/* 144 */     ArrayList<DeviceConfiguration> servos = new ArrayList();
/*     */     
/* 146 */     for (int i = 1; i <= count; i++) {
/* 147 */       ServoConfiguration servo = new ServoConfiguration(i);
/* 148 */       servos.add(servo);
/*     */     }
/* 150 */     return servos;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<DeviceConfiguration> createLegacyModuleList()
/*     */   {
/* 158 */     ArrayList<DeviceConfiguration> modules = new ArrayList();
/*     */     
/* 160 */     for (int i = 0; i < 6; i++) {
/* 161 */       DeviceConfiguration module = new DeviceConfiguration(i, BuiltInConfigurationType.NOTHING);
/* 162 */       modules.add(module);
/*     */     }
/* 164 */     return modules;
/*     */   }
/*     */   
/*     */   private ArrayList<DeviceConfiguration> createDeviceList(int count, ConfigurationType type) {
/* 168 */     ArrayList<DeviceConfiguration> result = new ArrayList();
/*     */     
/* 170 */     for (int i = 0; i < count; i++) {
/* 171 */       DeviceConfiguration module = new DeviceConfiguration(i, type);
/* 172 */       result.add(module);
/*     */     }
/* 174 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<DeviceConfiguration> createPWMList(int count)
/*     */   {
/* 182 */     return createDeviceList(count, BuiltInConfigurationType.PULSE_WIDTH_DEVICE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<DeviceConfiguration> createI2CList(int count)
/*     */   {
/* 190 */     return createDeviceList(count, BuiltInConfigurationType.I2C_DEVICE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<DeviceConfiguration> createAnalogInputList(int count)
/*     */   {
/* 198 */     return createDeviceList(count, BuiltInConfigurationType.ANALOG_INPUT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<DeviceConfiguration> createDigitalList(int count)
/*     */   {
/* 206 */     return createDeviceList(count, BuiltInConfigurationType.DIGITAL_DEVICE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<DeviceConfiguration> createAnalogOutputList(int count)
/*     */   {
/* 214 */     return createDeviceList(count, BuiltInConfigurationType.ANALOG_OUTPUT);
/*     */   }
/*     */   
/*     */   public void resetCount() {
/* 218 */     count = 1;
/*     */   }
/*     */   
/*     */   public Activity getActivity() {
/* 222 */     return this.activity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOrangeText(CharSequence[] array, int info_id, int layout_id, int orange0, int orange1)
/*     */   {
/* 230 */     setOrangeText(array[0], array[1], info_id, layout_id, orange0, orange1);
/*     */   }
/*     */   
/*     */   public void setOrangeText(CharSequence title, CharSequence message, @IdRes int idParent, @LayoutRes int layout_id, @IdRes int idTitle, @IdRes int idMessage) {
/* 234 */     setOrangeText(title, message, idParent, layout_id, idTitle, idMessage, 0);
/*     */   }
/*     */   
/*     */   public void setOrangeText(CharSequence title, CharSequence message, @IdRes final int idParent, @LayoutRes int layout_id, @IdRes int idTitle, @IdRes int idMessage, @IdRes int idButtonDismiss) {
/* 238 */     LinearLayout parent = (LinearLayout)this.activity.findViewById(idParent);
/* 239 */     parent.setVisibility(0);
/* 240 */     parent.removeAllViews();
/* 241 */     this.activity.getLayoutInflater().inflate(layout_id, parent, true);
/* 242 */     TextView text0 = (TextView)parent.findViewById(idTitle);
/* 243 */     TextView text1 = (TextView)parent.findViewById(idMessage);
/* 244 */     Button buttonDismiss = (Button)parent.findViewById(idButtonDismiss);
/*     */     
/* 246 */     if (text0 != null) text0.setText(title);
/* 247 */     if (text1 != null) text1.setText(message);
/* 248 */     if (buttonDismiss != null) {
/* 249 */       buttonDismiss.setOnClickListener(new View.OnClickListener() {
/*     */         public void onClick(View v) {
/* 251 */           Utility.this.hideOrangeText(idParent);
/*     */         }
/* 253 */       });
/* 254 */       buttonDismiss.setVisibility(0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void hideOrangeText(int idParent) {
/* 259 */     LinearLayout parent = (LinearLayout)this.activity.findViewById(idParent);
/* 260 */     parent.removeAllViews();
/* 261 */     parent.setVisibility(8);
/*     */   }
/*     */   
/*     */   @Nullable
/* 265 */   public CharSequence[] getOrangeText(@IdRes int info_id, @LayoutRes int layout_id, @IdRes int orange0, @IdRes int orange1) { LinearLayout layout = (LinearLayout)this.activity.findViewById(info_id);
/* 266 */     Assert.assertTrue(layout != null);
/* 267 */     TextView text0 = (TextView)layout.findViewById(orange0);
/* 268 */     TextView text1 = (TextView)layout.findViewById(orange1);
/*     */     
/* 270 */     boolean text0Null = (text0 == null) || (text0.getText().length() == 0);
/* 271 */     boolean text1Null = (text1 == null) || (text1.getText().length() == 0);
/* 272 */     if ((text0Null) && (text1Null)) { return null;
/*     */     }
/* 274 */     return new CharSequence[] { text0 == null ? "" : text0.getText(), text1 == null ? "" : text1.getText() };
/*     */   }
/*     */   
/*     */ 
/*     */   public AlertDialog.Builder buildBuilder(String title, String message)
/*     */   {
/* 280 */     AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
/* 281 */     builder.setTitle(title).setMessage(message);
/*     */     
/*     */ 
/* 284 */     return builder;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\Utility.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */