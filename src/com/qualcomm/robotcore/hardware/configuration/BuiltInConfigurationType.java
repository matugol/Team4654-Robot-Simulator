/*     */ package com.qualcomm.robotcore.hardware.configuration;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.support.annotation.NonNull;
/*     */ import com.qualcomm.robotcore.R.string;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
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
/*     */ public enum BuiltInConfigurationType
/*     */   implements ConfigurationType
/*     */ {
/*  78 */   MOTOR("Motor"), 
/*  79 */   SERVO("Servo"), 
/*  80 */   CONTINUOUS_ROTATION_SERVO("ContinuousRotationServo"), 
/*  81 */   GYRO("Gyro"), 
/*  82 */   COMPASS("Compass"), 
/*  83 */   IR_SEEKER("IrSeeker"), 
/*  84 */   LIGHT_SENSOR("LightSensor"), 
/*  85 */   ACCELEROMETER("Accelerometer"), 
/*  86 */   MOTOR_CONTROLLER("MotorController"), 
/*  87 */   SERVO_CONTROLLER("ServoController"), 
/*  88 */   LEGACY_MODULE_CONTROLLER("LegacyModuleController"), 
/*  89 */   DEVICE_INTERFACE_MODULE("DeviceInterfaceModule"), 
/*  90 */   I2C_DEVICE("I2cDevice"), 
/*  91 */   I2C_DEVICE_SYNCH("I2cDeviceSynch"), 
/*  92 */   ANALOG_INPUT("AnalogInput"), 
/*  93 */   TOUCH_SENSOR("TouchSensor"), 
/*  94 */   OPTICAL_DISTANCE_SENSOR("OpticalDistanceSensor"), 
/*  95 */   ANALOG_OUTPUT("AnalogOutput"), 
/*  96 */   DIGITAL_DEVICE("DigitalDevice"), 
/*  97 */   PULSE_WIDTH_DEVICE("PulseWidthDevice"), 
/*  98 */   IR_SEEKER_V3("IrSeekerV3"), 
/*  99 */   TOUCH_SENSOR_MULTIPLEXER("TouchSensorMultiplexer"), 
/* 100 */   MATRIX_CONTROLLER("MatrixController"), 
/* 101 */   ULTRASONIC_SENSOR("UltrasonicSensor"), 
/* 102 */   ADAFRUIT_COLOR_SENSOR("AdafruitColorSensor"), 
/* 103 */   COLOR_SENSOR("ColorSensor"), 
/* 104 */   LED("Led"), 
/* 105 */   NOTHING("Nothing"), 
/* 106 */   UNKNOWN("<unknown>");
/*     */   
/*     */   public final String xmlTag;
/*     */   
/*     */   private BuiltInConfigurationType(String xmlTag)
/*     */   {
/* 112 */     this.xmlTag = xmlTag;
/*     */   }
/*     */   
/*     */   public static ConfigurationType fromXmlTag(String xmlTag)
/*     */   {
/* 117 */     if (xmlTag.equalsIgnoreCase(MOTOR.xmlTag)) return MOTOR;
/* 118 */     if (xmlTag.equalsIgnoreCase(SERVO.xmlTag)) return SERVO;
/* 119 */     if (xmlTag.equalsIgnoreCase(CONTINUOUS_ROTATION_SERVO.xmlTag)) return CONTINUOUS_ROTATION_SERVO;
/* 120 */     if (xmlTag.equalsIgnoreCase(GYRO.xmlTag)) return GYRO;
/* 121 */     if (xmlTag.equalsIgnoreCase(COMPASS.xmlTag)) return COMPASS;
/* 122 */     if (xmlTag.equalsIgnoreCase(IR_SEEKER.xmlTag)) return IR_SEEKER;
/* 123 */     if (xmlTag.equalsIgnoreCase(LIGHT_SENSOR.xmlTag)) return LIGHT_SENSOR;
/* 124 */     if (xmlTag.equalsIgnoreCase(ACCELEROMETER.xmlTag)) return ACCELEROMETER;
/* 125 */     if (xmlTag.equalsIgnoreCase(MOTOR_CONTROLLER.xmlTag)) return MOTOR_CONTROLLER;
/* 126 */     if (xmlTag.equalsIgnoreCase(SERVO_CONTROLLER.xmlTag)) return SERVO_CONTROLLER;
/* 127 */     if (xmlTag.equalsIgnoreCase(LEGACY_MODULE_CONTROLLER.xmlTag)) return LEGACY_MODULE_CONTROLLER;
/* 128 */     if (xmlTag.equalsIgnoreCase(DEVICE_INTERFACE_MODULE.xmlTag)) return DEVICE_INTERFACE_MODULE;
/* 129 */     if (xmlTag.equalsIgnoreCase(I2C_DEVICE.xmlTag)) return I2C_DEVICE;
/* 130 */     if (xmlTag.equalsIgnoreCase(I2C_DEVICE_SYNCH.xmlTag)) return I2C_DEVICE_SYNCH;
/* 131 */     if (xmlTag.equalsIgnoreCase(ANALOG_INPUT.xmlTag)) return ANALOG_INPUT;
/* 132 */     if (xmlTag.equalsIgnoreCase(TOUCH_SENSOR.xmlTag)) return TOUCH_SENSOR;
/* 133 */     if (xmlTag.equalsIgnoreCase(OPTICAL_DISTANCE_SENSOR.xmlTag)) return OPTICAL_DISTANCE_SENSOR;
/* 134 */     if (xmlTag.equalsIgnoreCase(ANALOG_OUTPUT.xmlTag)) return ANALOG_OUTPUT;
/* 135 */     if (xmlTag.equalsIgnoreCase(DIGITAL_DEVICE.xmlTag)) return DIGITAL_DEVICE;
/* 136 */     if (xmlTag.equalsIgnoreCase(PULSE_WIDTH_DEVICE.xmlTag)) return PULSE_WIDTH_DEVICE;
/* 137 */     if (xmlTag.equalsIgnoreCase(IR_SEEKER_V3.xmlTag)) return IR_SEEKER_V3;
/* 138 */     if (xmlTag.equalsIgnoreCase(TOUCH_SENSOR_MULTIPLEXER.xmlTag)) return TOUCH_SENSOR_MULTIPLEXER;
/* 139 */     if (xmlTag.equalsIgnoreCase(MATRIX_CONTROLLER.xmlTag)) return MATRIX_CONTROLLER;
/* 140 */     if (xmlTag.equalsIgnoreCase(ULTRASONIC_SENSOR.xmlTag)) return ULTRASONIC_SENSOR;
/* 141 */     if (xmlTag.equalsIgnoreCase(ADAFRUIT_COLOR_SENSOR.xmlTag)) return ADAFRUIT_COLOR_SENSOR;
/* 142 */     if (xmlTag.equalsIgnoreCase(COLOR_SENSOR.xmlTag)) return COLOR_SENSOR;
/* 143 */     if (xmlTag.equalsIgnoreCase(LED.xmlTag)) return LED;
/* 144 */     if (xmlTag.equalsIgnoreCase(NOTHING.xmlTag)) return NOTHING;
/* 145 */     return UNKNOWN;
/*     */   }
/*     */   
/*     */   public static ConfigurationType fromString(String toString)
/*     */   {
/* 150 */     for (ConfigurationType configType : )
/*     */     {
/* 152 */       if (toString.equalsIgnoreCase(configType.toString()))
/*     */       {
/* 154 */         return configType;
/*     */       }
/*     */     }
/* 157 */     return UNKNOWN;
/*     */   }
/*     */   
/*     */   public static ConfigurationType fromUSBDeviceType(DeviceManager.DeviceType type)
/*     */   {
/* 162 */     switch (type) {
/*     */     case MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER: 
/* 164 */       return MOTOR_CONTROLLER;
/* 165 */     case MODERN_ROBOTICS_USB_SERVO_CONTROLLER:  return SERVO_CONTROLLER;
/* 166 */     case MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE:  return DEVICE_INTERFACE_MODULE;
/* 167 */     case MODERN_ROBOTICS_USB_LEGACY_MODULE:  return LEGACY_MODULE_CONTROLLER; }
/* 168 */     return UNKNOWN;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isI2cDevice()
/*     */   {
/* 174 */     switch (this)
/*     */     {
/*     */     case I2C_DEVICE: 
/*     */     case I2C_DEVICE_SYNCH: 
/*     */     case IR_SEEKER_V3: 
/*     */     case ADAFRUIT_COLOR_SENSOR: 
/*     */     case COLOR_SENSOR: 
/*     */     case GYRO: 
/* 182 */       return true;
/*     */     }
/* 184 */     return false;
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public DeviceManager.DeviceType toUSBDeviceType()
/*     */   {
/* 190 */     switch (this) {
/*     */     case MOTOR_CONTROLLER: 
/* 192 */       return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
/* 193 */     case SERVO_CONTROLLER:  return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
/* 194 */     case DEVICE_INTERFACE_MODULE:  return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE;
/* 195 */     case LEGACY_MODULE_CONTROLLER:  return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE; }
/* 196 */     return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
/*     */   }
/*     */   
/*     */ 
/*     */   @NonNull
/*     */   public String getDisplayName(Context context)
/*     */   {
/* 203 */     switch (this) {
/*     */     case MOTOR: 
/* 205 */       return context.getString(R.string.configTypeMotor);
/* 206 */     case SERVO:  return context.getString(R.string.configTypeServo);
/* 207 */     case CONTINUOUS_ROTATION_SERVO:  return context.getString(R.string.configTypeContinuousRotationServo);
/* 208 */     case GYRO:  return context.getString(R.string.configTypeGyro);
/* 209 */     case COMPASS:  return context.getString(R.string.configTypeCompass);
/* 210 */     case IR_SEEKER:  return context.getString(R.string.configTypeIrSeeker);
/* 211 */     case LIGHT_SENSOR:  return context.getString(R.string.configTypeLightSensor);
/* 212 */     case ACCELEROMETER:  return context.getString(R.string.configTypeAccelerometer);
/* 213 */     case MOTOR_CONTROLLER:  return context.getString(R.string.configTypeMotorController);
/* 214 */     case SERVO_CONTROLLER:  return context.getString(R.string.configTypeServoController);
/* 215 */     case LEGACY_MODULE_CONTROLLER:  return context.getString(R.string.configTypeLegacyModuleController);
/* 216 */     case DEVICE_INTERFACE_MODULE:  return context.getString(R.string.configTypeDeviceInterfaceModule);
/* 217 */     case I2C_DEVICE:  return context.getString(R.string.configTypeI2cDevice);
/* 218 */     case I2C_DEVICE_SYNCH:  return context.getString(R.string.configTypeI2cDeviceSynch);
/* 219 */     case ANALOG_INPUT:  return context.getString(R.string.configTypeAnalogInput);
/* 220 */     case TOUCH_SENSOR:  return context.getString(R.string.configTypeTouchSensor);
/* 221 */     case OPTICAL_DISTANCE_SENSOR:  return context.getString(R.string.configTypeOpticalDistanceSensor);
/* 222 */     case ANALOG_OUTPUT:  return context.getString(R.string.configTypeAnalogOutput);
/* 223 */     case DIGITAL_DEVICE:  return context.getString(R.string.configTypeDigitalDevice);
/* 224 */     case PULSE_WIDTH_DEVICE:  return context.getString(R.string.configTypePulseWidthDevice);
/* 225 */     case IR_SEEKER_V3:  return context.getString(R.string.configTypeIrSeekerV3);
/* 226 */     case TOUCH_SENSOR_MULTIPLEXER:  return context.getString(R.string.configTypeTouchSensorMultiplexer);
/* 227 */     case MATRIX_CONTROLLER:  return context.getString(R.string.configTypeMatrixController);
/* 228 */     case ULTRASONIC_SENSOR:  return context.getString(R.string.configTypeUltrasonicSensor);
/* 229 */     case ADAFRUIT_COLOR_SENSOR:  return context.getString(R.string.configTypeAdafruitColorSensor);
/* 230 */     case COLOR_SENSOR:  return context.getString(R.string.configTypeColorSensor);
/* 231 */     case LED:  return context.getString(R.string.configTypeLED);
/* 232 */     case NOTHING:  return context.getString(R.string.configTypeNothing);
/*     */     }
/*     */     
/* 235 */     return context.getString(R.string.configTypeUnknown);
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public String getXmlTag()
/*     */   {
/* 241 */     return this.xmlTag;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\BuiltInConfigurationType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */