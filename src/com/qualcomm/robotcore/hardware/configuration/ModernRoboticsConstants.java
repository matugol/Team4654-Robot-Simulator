/*    */ package com.qualcomm.robotcore.hardware.configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModernRoboticsConstants
/*    */ {
/*    */   public static final int NUMBER_OF_MOTORS = 2;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int NUMBER_OF_SERVOS = 6;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int NUMBER_OF_I2C_CHANNELS = 6;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int NUMBER_OF_LEGACY_MODULE_PORTS = 6;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int NUMBER_OF_PWM_CHANNELS = 2;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int NUMBER_OF_ANALOG_INPUTS = 8;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int NUMBER_OF_DIGITAL_IOS = 8;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int NUMBER_OF_ANALOG_OUTPUTS = 2;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int TETRIX_MOTOR_TICKS_PER_REVOLUTION = 1440;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int MAX_PID_DEGREES_PER_SECOND = 1000;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void validateMotorZ(int motorZ)
/*    */   {
/* 55 */     if ((motorZ < 0) || (motorZ >= 2))
/* 56 */       throw new IllegalArgumentException(String.format("invalid motor: %d", new Object[] { Integer.valueOf(motorZ) }));
/*    */   }
/*    */   
/*    */   public static void validatePwmChannelZ(int channelZ) {
/* 60 */     if ((channelZ < 0) || (channelZ >= 2))
/* 61 */       throw new IllegalArgumentException(String.format("invalid pwm channel: %d", new Object[] { Integer.valueOf(channelZ) }));
/*    */   }
/*    */   
/*    */   public static void validateServoChannelZ(int channelZ) {
/* 65 */     if ((channelZ < 0) || (channelZ >= 6))
/* 66 */       throw new IllegalArgumentException(String.format("invalid servo channel: %d", new Object[] { Integer.valueOf(channelZ) }));
/*    */   }
/*    */   
/*    */   public static void validateI2cChannelZ(int channelZ) {
/* 70 */     if ((channelZ < 0) || (channelZ >= 6))
/* 71 */       throw new IllegalArgumentException(String.format("invalid i2c channel: %d", new Object[] { Integer.valueOf(channelZ) }));
/*    */   }
/*    */   
/*    */   public static void validateAnalogInputZ(int analogInputZ) {
/* 75 */     if ((analogInputZ < 0) || (analogInputZ >= 8))
/* 76 */       throw new IllegalArgumentException(String.format("invalid analog input: %d", new Object[] { Integer.valueOf(analogInputZ) }));
/*    */   }
/*    */   
/*    */   public static void validateDigitalIOZ(int digitalIOZ) {
/* 80 */     if ((digitalIOZ < 0) || (digitalIOZ >= 8)) {
/* 81 */       throw new IllegalArgumentException(String.format("invalid digital pin: %d", new Object[] { Integer.valueOf(digitalIOZ) }));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\ModernRoboticsConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */