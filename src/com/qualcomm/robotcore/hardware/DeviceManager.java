/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ import android.support.annotation.Nullable;
/*    */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*    */ import com.qualcomm.robotcore.hardware.configuration.UserSensorType;
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
/*    */ import java.util.Map;
/*    */ 
/*    */ public abstract interface DeviceManager
/*    */ {
/*    */   public abstract Map<SerialNumber, DeviceType> scanForUsbDevices()
/*    */     throws RobotCoreException;
/*    */   
/*    */   public abstract DcMotorController createUsbDcMotorController(SerialNumber paramSerialNumber) throws RobotCoreException, InterruptedException;
/*    */   
/*    */   public abstract DcMotor createDcMotor(DcMotorController paramDcMotorController, int paramInt);
/*    */   
/*    */   public abstract DcMotor createDcMotorEx(DcMotorController paramDcMotorController, int paramInt);
/*    */   
/*    */   public abstract ServoController createUsbServoController(SerialNumber paramSerialNumber) throws RobotCoreException, InterruptedException;
/*    */   
/*    */   public abstract Servo createServo(ServoController paramServoController, int paramInt);
/*    */   
/*    */   public abstract Servo createServoEx(ServoController paramServoController, int paramInt);
/*    */   
/*    */   public abstract CRServo createCRServo(ServoController paramServoController, int paramInt);
/*    */   
/*    */   public abstract LegacyModule createUsbLegacyModule(SerialNumber paramSerialNumber) throws RobotCoreException, InterruptedException;
/*    */   
/*    */   public abstract DeviceInterfaceModule createDeviceInterfaceModule(SerialNumber paramSerialNumber) throws RobotCoreException, InterruptedException;
/*    */   
/*    */   public abstract TouchSensor createNxtTouchSensor(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract TouchSensorMultiplexer createNxtTouchSensorMultiplexer(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract AnalogInput createAnalogInputDevice(AnalogInputController paramAnalogInputController, int paramInt);
/*    */   
/*    */   public abstract AnalogOutput createAnalogOutputDevice(AnalogOutputController paramAnalogOutputController, int paramInt);
/*    */   
/*    */   public abstract DigitalChannel createDigitalChannelDevice(DigitalChannelController paramDigitalChannelController, int paramInt);
/*    */   
/*    */   public abstract PWMOutput createPwmOutputDevice(PWMOutputController paramPWMOutputController, int paramInt);
/*    */   
/*    */   public abstract PWMOutput createPwmOutputDeviceEx(PWMOutputController paramPWMOutputController, int paramInt);
/*    */   
/*    */   public static enum DeviceType
/*    */   {
/* 48 */     FTDI_USB_UNKNOWN_DEVICE, 
/* 49 */     MODERN_ROBOTICS_USB_UNKNOWN_DEVICE, 
/* 50 */     MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER, 
/* 51 */     MODERN_ROBOTICS_USB_SERVO_CONTROLLER, 
/* 52 */     MODERN_ROBOTICS_USB_LEGACY_MODULE, 
/* 53 */     MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE, 
/* 54 */     MODERN_ROBOTICS_USB_SENSOR_MUX, 
/* 55 */     UNKNOWN_DEVICE;
/*    */     
/*    */     private DeviceType() {}
/*    */   }
/*    */   
/*    */   public abstract I2cDevice createI2cDevice(I2cController paramI2cController, int paramInt);
/*    */   
/*    */   @Nullable
/*    */   public abstract HardwareDevice createUserI2cDevice(I2cController paramI2cController, int paramInt, UserSensorType paramUserSensorType);
/*    */   
/*    */   public abstract DcMotorController createNxtDcMotorController(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract ServoController createNxtServoController(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract CompassSensor createNxtCompassSensor(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract TouchSensor createDigitalTouchSensor(DeviceInterfaceModule paramDeviceInterfaceModule, int paramInt);
/*    */   
/*    */   public abstract AccelerationSensor createNxtAccelerationSensor(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract LightSensor createNxtLightSensor(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract IrSeekerSensor createNxtIrSeekerSensor(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract IrSeekerSensor createI2cIrSeekerSensorV3(I2cController paramI2cController, int paramInt);
/*    */   
/*    */   public abstract UltrasonicSensor createNxtUltrasonicSensor(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract GyroSensor createNxtGyroSensor(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract GyroSensor createModernRoboticsI2cGyroSensor(I2cController paramI2cController, int paramInt);
/*    */   
/*    */   public abstract OpticalDistanceSensor createAnalogOpticalDistanceSensor(AnalogInputController paramAnalogInputController, int paramInt);
/*    */   
/*    */   public abstract ColorSensor createAdafruitI2cColorSensor(I2cController paramI2cController, int paramInt);
/*    */   
/*    */   public abstract ColorSensor createNxtColorSensor(LegacyModule paramLegacyModule, int paramInt);
/*    */   
/*    */   public abstract ColorSensor createModernRoboticsI2cColorSensor(I2cController paramI2cController, int paramInt);
/*    */   
/*    */   public abstract LED createLED(DigitalChannelController paramDigitalChannelController, int paramInt);
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DeviceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */