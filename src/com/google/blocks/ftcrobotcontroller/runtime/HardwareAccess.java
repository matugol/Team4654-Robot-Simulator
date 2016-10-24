/*    */ package com.google.blocks.ftcrobotcontroller.runtime;
/*    */ 
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*    */ import com.google.blocks.ftcrobotcontroller.util.HardwareType;
/*    */ import com.qualcomm.robotcore.hardware.HardwareDevice;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*    */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class HardwareAccess<DEVICE_TYPE extends HardwareDevice>
/*    */   extends Access
/*    */ {
/*    */   protected final DEVICE_TYPE hardwareDevice;
/*    */   
/*    */   protected HardwareAccess(HardwareItem hardwareItem, HardwareMap.DeviceMapping<DEVICE_TYPE> deviceMapping)
/*    */   {
/* 24 */     super(hardwareItem.identifier);
/*    */     
/* 26 */     DEVICE_TYPE hardwareDevice = null;
/*    */     try {
/* 28 */       hardwareDevice = deviceMapping.get(hardwareItem.deviceName);
/*    */     } catch (Exception e) {
/* 30 */       RobotLog.e("HardwareAcccess - caught " + e);
/*    */     }
/* 32 */     this.hardwareDevice = hardwareDevice;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static HardwareAccess newHardwareAccess(HardwareType hardwareType, HardwareMap hardwareMap, HardwareItem hardwareItem)
/*    */   {
/* 41 */     switch (hardwareType) {
/*    */     case ACCELERATION_SENSOR: 
/* 43 */       return new AccelerationSensorAccess(hardwareItem, hardwareMap.accelerationSensor);
/*    */     case ANALOG_INPUT: 
/* 45 */       return new AnalogInputAccess(hardwareItem, hardwareMap.analogInput);
/*    */     case ANALOG_OUTPUT: 
/* 47 */       return new AnalogOutputAccess(hardwareItem, hardwareMap.analogOutput);
/*    */     case COLOR_SENSOR: 
/* 49 */       return new ColorSensorAccess(hardwareItem, hardwareMap.colorSensor);
/*    */     case COMPASS_SENSOR: 
/* 51 */       return new CompassSensorAccess(hardwareItem, hardwareMap.compassSensor);
/*    */     case CR_SERVO: 
/* 53 */       return new CRServoAccess(hardwareItem, hardwareMap.crservo);
/*    */     case DC_MOTOR: 
/* 55 */       return new DcMotorAccess(hardwareItem, hardwareMap.dcMotor);
/*    */     case GYRO_SENSOR: 
/* 57 */       return new GyroSensorAccess(hardwareItem, hardwareMap.gyroSensor);
/*    */     case IR_SEEKER_SENSOR: 
/* 59 */       return new IrSeekerSensorAccess(hardwareItem, hardwareMap.irSeekerSensor);
/*    */     case LED: 
/* 61 */       return new LedAccess(hardwareItem, hardwareMap.led);
/*    */     case LIGHT_SENSOR: 
/* 63 */       return new LightSensorAccess(hardwareItem, hardwareMap.lightSensor);
/*    */     case OPTICAL_DISTANCE_SENSOR: 
/* 65 */       return new OpticalDistanceSensorAccess(hardwareItem, hardwareMap.opticalDistanceSensor);
/*    */     case SERVO: 
/* 67 */       return new ServoAccess(hardwareItem, hardwareMap.servo);
/*    */     case SERVO_CONTROLLER: 
/* 69 */       return new ServoControllerAccess(hardwareItem, hardwareMap.servoController);
/*    */     case TOUCH_SENSOR: 
/* 71 */       return new TouchSensorAccess(hardwareItem, hardwareMap.touchSensor);
/*    */     case ULTRASONIC_SENSOR: 
/* 73 */       return new UltrasonicSensorAccess(hardwareItem, hardwareMap.ultrasonicSensor);
/*    */     case VOLTAGE_SENSOR: 
/* 75 */       return new VoltageSensorAccess(hardwareItem, hardwareMap.voltageSensor);
/*    */     }
/* 77 */     throw new IllegalArgumentException("Unknown hardware type " + hardwareType);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\HardwareAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */