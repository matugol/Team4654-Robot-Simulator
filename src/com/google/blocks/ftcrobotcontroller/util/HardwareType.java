/*     */ package com.google.blocks.ftcrobotcontroller.util;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum HardwareType
/*     */ {
/*  14 */   ACCELERATION_SENSOR("createAccelerationSensorDropdown", "accelerationSensor", "AccelerationSensor", "", new ConfigurationType[] { BuiltInConfigurationType.ACCELEROMETER }), 
/*     */   
/*     */ 
/*  17 */   ANALOG_INPUT("createAnalogInputDropdown", "analogInput", "AnalogInput", "AsAnalogInput", new ConfigurationType[] { BuiltInConfigurationType.ANALOG_INPUT }), 
/*     */   
/*     */ 
/*  20 */   ANALOG_OUTPUT("createAnalogOutputDropdown", "analogOutput", "AnalogOutput", "AsAnalogOutput", new ConfigurationType[] { BuiltInConfigurationType.ANALOG_OUTPUT }), 
/*     */   
/*     */ 
/*  23 */   COLOR_SENSOR("createColorSensorDropdown", "colorSensor", "ColorSensor", "", new ConfigurationType[] { BuiltInConfigurationType.COLOR_SENSOR, BuiltInConfigurationType.ADAFRUIT_COLOR_SENSOR }), 
/*     */   
/*     */ 
/*  26 */   COMPASS_SENSOR("createCompassSensorDropdown", "compassSensor", "CompassSensor", "", new ConfigurationType[] { BuiltInConfigurationType.COMPASS }), 
/*     */   
/*     */ 
/*  29 */   CR_SERVO("createCRServoDropdown", "crServo", "CRServo", "", new ConfigurationType[] { BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO }), 
/*     */   
/*     */ 
/*  32 */   DC_MOTOR("createDcMotorDropdown", "dcMotor", "DcMotor", "", new ConfigurationType[] { BuiltInConfigurationType.MOTOR }), 
/*     */   
/*     */ 
/*  35 */   GYRO_SENSOR("createGyroSensorDropdown", "gyroSensor", "GyroSensor", "", new ConfigurationType[] { BuiltInConfigurationType.GYRO }), 
/*     */   
/*     */ 
/*  38 */   IR_SEEKER_SENSOR("createIrSeekerSensorDropdown", "irSeekerSensor", "IrSeekerSensor", "", new ConfigurationType[] { BuiltInConfigurationType.IR_SEEKER }), 
/*     */   
/*     */ 
/*  41 */   LED("createLedDropdown", "led", "LED", "", new ConfigurationType[] { BuiltInConfigurationType.LED }), 
/*     */   
/*     */ 
/*  44 */   LIGHT_SENSOR("createLightSensorDropdown", "lightSensor", "LightSensor", "", new ConfigurationType[] { BuiltInConfigurationType.LIGHT_SENSOR }), 
/*     */   
/*     */ 
/*  47 */   OPTICAL_DISTANCE_SENSOR("createOpticalDistanceSensorDropdown", "opticalDistanceSensor", "OpticalDistanceSensor", "", new ConfigurationType[] { BuiltInConfigurationType.OPTICAL_DISTANCE_SENSOR }), 
/*     */   
/*     */ 
/*  50 */   SERVO("createServoDropdown", "servo", "Servo", "", new ConfigurationType[] { BuiltInConfigurationType.SERVO }), 
/*     */   
/*     */ 
/*  53 */   SERVO_CONTROLLER("createServoControllerDropdown", "servoController", "ServoController", "AsServoController", new ConfigurationType[] { BuiltInConfigurationType.SERVO_CONTROLLER, BuiltInConfigurationType.MATRIX_CONTROLLER }), 
/*     */   
/*     */ 
/*  56 */   TOUCH_SENSOR("createTouchSensorDropdown", "touchSensor", "TouchSensor", "", new ConfigurationType[] { BuiltInConfigurationType.TOUCH_SENSOR }), 
/*     */   
/*     */ 
/*  59 */   ULTRASONIC_SENSOR("createUltrasonicSensorDropdown", "ultrasonicSensor", "UltrasonicSensor", "", new ConfigurationType[] { BuiltInConfigurationType.ULTRASONIC_SENSOR }), 
/*     */   
/*     */ 
/*  62 */   VOLTAGE_SENSOR("createVoltageSensorDropdown", "voltageSensor", "VoltageSensor", "AsVoltageSensor", new ConfigurationType[] { BuiltInConfigurationType.MOTOR_CONTROLLER });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ConfigurationType[] configurationTypes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String createDropdownFunctionName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String blockTypePrefix;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String toolboxCategoryName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String identifierSuffix;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HardwareType(String createDropdownFunctionName, String blockTypePrefix, String toolboxCategoryName, String identifierSuffix, ConfigurationType... configurationTypes)
/*     */   {
/*  98 */     this.createDropdownFunctionName = createDropdownFunctionName;
/*  99 */     this.blockTypePrefix = blockTypePrefix;
/* 100 */     this.toolboxCategoryName = toolboxCategoryName;
/* 101 */     this.identifierSuffix = identifierSuffix;
/* 102 */     this.configurationTypes = configurationTypes;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\util\HardwareType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */