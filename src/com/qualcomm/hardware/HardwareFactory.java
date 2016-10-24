/*     */ package com.qualcomm.hardware;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtDcMotorController;
/*     */ import com.qualcomm.hardware.matrix.MatrixDcMotorController;
/*     */ import com.qualcomm.hardware.matrix.MatrixMasterController;
/*     */ import com.qualcomm.hardware.matrix.MatrixServoController;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDcMotorController;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.AccelerationSensor;
/*     */ import com.qualcomm.robotcore.hardware.AnalogInput;
/*     */ import com.qualcomm.robotcore.hardware.AnalogOutput;
/*     */ import com.qualcomm.robotcore.hardware.CRServo;
/*     */ import com.qualcomm.robotcore.hardware.ColorSensor;
/*     */ import com.qualcomm.robotcore.hardware.CompassSensor;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorController;
/*     */ import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager;
/*     */ import com.qualcomm.robotcore.hardware.DigitalChannel;
/*     */ import com.qualcomm.robotcore.hardware.DigitalChannelController;
/*     */ import com.qualcomm.robotcore.hardware.GyroSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cDevice;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor;
/*     */ import com.qualcomm.robotcore.hardware.LED;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.LightSensor;
/*     */ import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
/*     */ import com.qualcomm.robotcore.hardware.PWMOutput;
/*     */ import com.qualcomm.robotcore.hardware.PWMOutputController;
/*     */ import com.qualcomm.robotcore.hardware.Servo;
/*     */ import com.qualcomm.robotcore.hardware.ServoController;
/*     */ import com.qualcomm.robotcore.hardware.TouchSensor;
/*     */ import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
/*     */ import com.qualcomm.robotcore.hardware.UltrasonicSensor;
/*     */ import com.qualcomm.robotcore.hardware.VoltageSensor;
/*     */ import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.UserSensorType;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.xmlpull.v1.XmlPullParser;
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
/*     */ public class HardwareFactory
/*     */ {
/*     */   private Context context;
/* 106 */   private XmlPullParser xmlPullParser = null;
/*     */   
/* 108 */   protected static final HashMap<String, String> deviceDisplayNames = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareFactory(Context context)
/*     */   {
/* 115 */     this.context = context;
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
/*     */   public HardwareMap createHardwareMap(EventLoopManager manager)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 130 */     synchronized (HardwareDeviceManager.scanDevicesLock) {
/* 131 */       HardwareMap map = new HardwareMap(this.context);
/*     */       DeviceManager deviceMgr;
/* 133 */       if (this.xmlPullParser != null) {
/* 134 */         deviceMgr = new HardwareDeviceManager(this.context, manager);
/*     */         
/* 136 */         ReadXMLFileHandler readXmlFileHandler = new ReadXMLFileHandler();
/*     */         
/* 138 */         List<ControllerConfiguration> ctrlConfList = readXmlFileHandler.parse(this.xmlPullParser);
/*     */         
/* 140 */         for (ControllerConfiguration ctrlConf : ctrlConfList) {
/* 141 */           ConfigurationType type = ctrlConf.getType();
/* 142 */           if (type == BuiltInConfigurationType.MOTOR_CONTROLLER) {
/* 143 */             mapUsbMotorController(map, deviceMgr, ctrlConf);
/*     */           }
/* 145 */           else if (type == BuiltInConfigurationType.SERVO_CONTROLLER) {
/* 146 */             mapUsbServoController(map, deviceMgr, ctrlConf);
/*     */           }
/* 148 */           else if (type == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
/* 149 */             mapUsbLegacyModule(map, deviceMgr, ctrlConf);
/*     */           }
/* 151 */           else if (type == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
/* 152 */             mapCoreInterfaceDeviceModule(map, deviceMgr, ctrlConf);
/*     */           }
/*     */           else {
/* 155 */             RobotLog.w("Unexpected controller type while parsing XML: " + type.toString());
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 161 */       return map;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setXmlPullParser(XmlPullParser xmlPullParser) {
/* 166 */     this.xmlPullParser = xmlPullParser;
/*     */   }
/*     */   
/*     */   public XmlPullParser getXmlPullParser() {
/* 170 */     return this.xmlPullParser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void enableDeviceEmulation() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void disableDeviceEmulation() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void mapUsbMotorController(HardwareMap map, DeviceManager deviceMgr, ControllerConfiguration ctrlConf)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 190 */     if (!ctrlConf.isEnabled()) return;
/* 191 */     ModernRoboticsUsbDcMotorController dcMotorController = (ModernRoboticsUsbDcMotorController)deviceMgr.createUsbDcMotorController(ctrlConf.getSerialNumber());
/* 192 */     map.dcMotorController.put(ctrlConf.getName(), dcMotorController);
/* 193 */     for (DeviceConfiguration devConf : ctrlConf.getDevices()) {
/* 194 */       mapMotor(map, deviceMgr, devConf, dcMotorController);
/*     */     }
/*     */     
/* 197 */     VoltageSensor voltageSensor = dcMotorController;
/* 198 */     map.voltageSensor.put(ctrlConf.getName(), voltageSensor);
/*     */   }
/*     */   
/*     */   private void mapUsbServoController(HardwareMap map, DeviceManager deviceMgr, ControllerConfiguration ctrlConf) throws RobotCoreException, InterruptedException {
/* 202 */     if (!ctrlConf.isEnabled()) return;
/* 203 */     ServoController servoController = deviceMgr.createUsbServoController(ctrlConf.getSerialNumber());
/* 204 */     map.servoController.put(ctrlConf.getName(), servoController);
/* 205 */     for (DeviceConfiguration servoConf : ctrlConf.getDevices()) {
/* 206 */       mapServo(map, deviceMgr, servoConf, servoController);
/*     */     }
/*     */   }
/*     */   
/*     */   private void mapMotor(HardwareMap map, DeviceManager deviceMgr, DeviceConfiguration motorConf, DcMotorController dcMotorController) {
/* 211 */     if (!motorConf.isEnabled()) return;
/* 212 */     DcMotor dcMotor = deviceMgr.createDcMotor(dcMotorController, motorConf.getPort());
/* 213 */     map.dcMotor.put(motorConf.getName(), dcMotor);
/*     */   }
/*     */   
/*     */   private void mapServo(HardwareMap map, DeviceManager deviceMgr, DeviceConfiguration servoConf, ServoController servoController) {
/* 217 */     if (!servoConf.isEnabled()) return;
/* 218 */     if (servoConf.getType() == BuiltInConfigurationType.SERVO) {
/* 219 */       Servo s = deviceMgr.createServo(servoController, servoConf.getPort());
/* 220 */       map.servo.put(servoConf.getName(), s);
/* 221 */     } else if (servoConf.getType() == BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO) {
/* 222 */       CRServo s = deviceMgr.createCRServo(servoController, servoConf.getPort());
/* 223 */       map.crservo.put(servoConf.getName(), s);
/*     */     }
/*     */   }
/*     */   
/*     */   private void mapCoreInterfaceDeviceModule(HardwareMap map, DeviceManager deviceMgr, ControllerConfiguration ctrlConf) throws RobotCoreException, InterruptedException {
/* 228 */     if (!ctrlConf.isEnabled()) return;
/* 229 */     DeviceInterfaceModule deviceInterfaceModule = deviceMgr.createDeviceInterfaceModule(ctrlConf.getSerialNumber());
/* 230 */     map.deviceInterfaceModule.put(ctrlConf.getName(), deviceInterfaceModule);
/*     */     
/* 232 */     List<DeviceConfiguration> pwmDevices = ((DeviceInterfaceModuleConfiguration)ctrlConf).getPwmOutputs();
/* 233 */     buildDevices(pwmDevices, map, deviceMgr, deviceInterfaceModule);
/*     */     
/* 235 */     List<DeviceConfiguration> i2cDevices = ((DeviceInterfaceModuleConfiguration)ctrlConf).getI2cDevices();
/* 236 */     buildI2cDevices(i2cDevices, map, deviceMgr, deviceInterfaceModule);
/*     */     
/* 238 */     List<DeviceConfiguration> analogInputDevices = ((DeviceInterfaceModuleConfiguration)ctrlConf).getAnalogInputDevices();
/* 239 */     buildDevices(analogInputDevices, map, deviceMgr, deviceInterfaceModule);
/*     */     
/* 241 */     List<DeviceConfiguration> digitalDevices = ((DeviceInterfaceModuleConfiguration)ctrlConf).getDigitalDevices();
/* 242 */     buildDevices(digitalDevices, map, deviceMgr, deviceInterfaceModule);
/*     */     
/* 244 */     List<DeviceConfiguration> analogOutputDevices = ((DeviceInterfaceModuleConfiguration)ctrlConf).getAnalogOutputDevices();
/* 245 */     buildDevices(analogOutputDevices, map, deviceMgr, deviceInterfaceModule);
/*     */   }
/*     */   
/*     */   private void buildDevices(List<DeviceConfiguration> list, HardwareMap map, DeviceManager deviceMgr, DeviceInterfaceModule deviceInterfaceModule) {
/* 249 */     for (DeviceConfiguration deviceConfiguration : list) {
/* 250 */       ConfigurationType devType = deviceConfiguration.getType();
/* 251 */       if (devType == BuiltInConfigurationType.OPTICAL_DISTANCE_SENSOR) {
/* 252 */         mapOpticalDistanceSensor(map, deviceMgr, deviceInterfaceModule, deviceConfiguration);
/*     */       }
/* 254 */       else if (devType == BuiltInConfigurationType.ANALOG_INPUT) {
/* 255 */         mapAnalogInputDevice(map, deviceMgr, deviceInterfaceModule, deviceConfiguration);
/*     */       }
/* 257 */       else if (devType == BuiltInConfigurationType.TOUCH_SENSOR) {
/* 258 */         mapTouchSensor(map, deviceMgr, deviceInterfaceModule, deviceConfiguration);
/*     */       }
/* 260 */       else if (devType == BuiltInConfigurationType.DIGITAL_DEVICE) {
/* 261 */         mapDigitalDevice(map, deviceMgr, deviceInterfaceModule, deviceConfiguration);
/*     */       }
/* 263 */       else if (devType == BuiltInConfigurationType.PULSE_WIDTH_DEVICE) {
/* 264 */         mapPwmOutputDevice(map, deviceMgr, deviceInterfaceModule, deviceConfiguration);
/*     */       }
/* 266 */       else if (devType == BuiltInConfigurationType.ANALOG_OUTPUT) {
/* 267 */         mapAnalogOutputDevice(map, deviceMgr, deviceInterfaceModule, deviceConfiguration);
/*     */       }
/* 269 */       else if (devType == BuiltInConfigurationType.LED) {
/* 270 */         mapLED(map, deviceMgr, deviceInterfaceModule, deviceConfiguration);
/*     */       }
/* 272 */       else if (devType != BuiltInConfigurationType.NOTHING)
/*     */       {
/*     */ 
/*     */ 
/* 276 */         RobotLog.w("Unexpected device type connected to Device Interface Module while parsing XML: " + devType.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void buildI2cDevices(List<DeviceConfiguration> list, HardwareMap map, DeviceManager deviceMgr, I2cController i2cController) {
/* 282 */     for (DeviceConfiguration deviceConfiguration : list) {
/* 283 */       ConfigurationType devType = deviceConfiguration.getType();
/* 284 */       if (devType == BuiltInConfigurationType.I2C_DEVICE) {
/* 285 */         mapI2cDevice(map, deviceMgr, i2cController, deviceConfiguration);
/*     */ 
/*     */       }
/* 288 */       else if (devType == BuiltInConfigurationType.I2C_DEVICE_SYNCH) {
/* 289 */         mapI2cDeviceSynch(map, deviceMgr, i2cController, deviceConfiguration);
/*     */ 
/*     */       }
/* 292 */       else if (devType == BuiltInConfigurationType.IR_SEEKER_V3) {
/* 293 */         mapIrSeekerV3Device(map, deviceMgr, i2cController, deviceConfiguration);
/*     */ 
/*     */       }
/* 296 */       else if (devType == BuiltInConfigurationType.ADAFRUIT_COLOR_SENSOR) {
/* 297 */         mapAdafruitColorSensor(map, deviceMgr, i2cController, deviceConfiguration);
/*     */ 
/*     */       }
/* 300 */       else if (devType == BuiltInConfigurationType.COLOR_SENSOR) {
/* 301 */         mapModernRoboticsColorSensor(map, deviceMgr, i2cController, deviceConfiguration);
/*     */ 
/*     */       }
/* 304 */       else if (devType == BuiltInConfigurationType.GYRO) {
/* 305 */         mapModernRoboticsGyro(map, deviceMgr, i2cController, deviceConfiguration);
/*     */ 
/*     */       }
/* 308 */       else if (devType != BuiltInConfigurationType.NOTHING)
/*     */       {
/*     */ 
/*     */ 
/* 312 */         if ((devType.isI2cDevice()) && 
/* 313 */           ((devType instanceof UserSensorType))) {
/* 314 */           mapUserI2cDevice(map, deviceMgr, i2cController, deviceConfiguration);
/*     */         }
/*     */         else
/*     */         {
/* 318 */           RobotLog.w("Unexpected device type connected to I2c Controller while parsing XML: " + devType.toString()); } }
/*     */     }
/*     */   }
/*     */   
/*     */   private void mapUsbLegacyModule(HardwareMap map, DeviceManager deviceMgr, ControllerConfiguration ctrlConf) throws RobotCoreException, InterruptedException {
/* 323 */     if (!ctrlConf.isEnabled()) return;
/* 324 */     LegacyModule legacyModule = deviceMgr.createUsbLegacyModule(ctrlConf.getSerialNumber());
/* 325 */     map.legacyModule.put(ctrlConf.getName(), legacyModule);
/*     */     
/* 327 */     for (DeviceConfiguration devConf : ctrlConf.getDevices()) {
/* 328 */       ConfigurationType devType = devConf.getType();
/* 329 */       if (devType == BuiltInConfigurationType.GYRO) {
/* 330 */         mapNxtGyroSensor(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 332 */       else if (devType == BuiltInConfigurationType.COMPASS) {
/* 333 */         mapNxtCompassSensor(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 335 */       else if (devType == BuiltInConfigurationType.IR_SEEKER) {
/* 336 */         mapNxtIrSeekerSensor(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 338 */       else if (devType == BuiltInConfigurationType.LIGHT_SENSOR) {
/* 339 */         mapNxtLightSensor(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 341 */       else if (devType == BuiltInConfigurationType.ACCELEROMETER) {
/* 342 */         mapNxtAccelerationSensor(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 344 */       else if (devType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
/* 345 */         mapNxtDcMotorController(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 347 */       else if (devType == BuiltInConfigurationType.SERVO_CONTROLLER) {
/* 348 */         mapNxtServoController(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 350 */       else if (devType == BuiltInConfigurationType.TOUCH_SENSOR) {
/* 351 */         mapNxtTouchSensor(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 353 */       else if (devType == BuiltInConfigurationType.TOUCH_SENSOR_MULTIPLEXER) {
/* 354 */         mapNxtTouchSensorMultiplexer(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 356 */       else if (devType == BuiltInConfigurationType.ULTRASONIC_SENSOR) {
/* 357 */         mapSonarSensor(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 359 */       else if (devType == BuiltInConfigurationType.COLOR_SENSOR) {
/* 360 */         mapNxtColorSensor(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 362 */       else if (devType == BuiltInConfigurationType.MATRIX_CONTROLLER) {
/* 363 */         mapMatrixController(map, deviceMgr, legacyModule, devConf);
/*     */       }
/* 365 */       else if (devType != BuiltInConfigurationType.NOTHING)
/*     */       {
/*     */ 
/*     */ 
/* 369 */         RobotLog.w("Unexpected device type connected to Legacy Module while parsing XML: " + devType.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void mapIrSeekerV3Device(HardwareMap map, DeviceManager deviceMgr, I2cController i2cController, DeviceConfiguration devConf) {
/* 375 */     if (!devConf.isEnabled()) return;
/* 376 */     IrSeekerSensor irSeekerSensor = deviceMgr.createI2cIrSeekerSensorV3(i2cController, devConf.getPort());
/* 377 */     map.irSeekerSensor.put(devConf.getName(), irSeekerSensor);
/*     */   }
/*     */   
/*     */   private void mapDigitalDevice(HardwareMap map, DeviceManager deviceMgr, DeviceInterfaceModule deviceInterfaceModule, DeviceConfiguration devConf) {
/* 381 */     if (!devConf.isEnabled()) return;
/* 382 */     DigitalChannel digitalChannel = deviceMgr.createDigitalChannelDevice(deviceInterfaceModule, devConf.getPort());
/* 383 */     map.digitalChannel.put(devConf.getName(), digitalChannel);
/*     */   }
/*     */   
/*     */   private void mapTouchSensor(HardwareMap map, DeviceManager deviceMgr, DeviceInterfaceModule deviceInterfaceModule, DeviceConfiguration devConf) {
/* 387 */     if (!devConf.isEnabled()) return;
/* 388 */     TouchSensor touchSensor = deviceMgr.createDigitalTouchSensor(deviceInterfaceModule, devConf.getPort());
/* 389 */     map.touchSensor.put(devConf.getName(), touchSensor);
/*     */   }
/*     */   
/*     */   private void mapAnalogInputDevice(HardwareMap map, DeviceManager deviceMgr, DeviceInterfaceModule deviceInterfaceModule, DeviceConfiguration devConf) {
/* 393 */     if (!devConf.isEnabled()) return;
/* 394 */     AnalogInput analogInput = deviceMgr.createAnalogInputDevice(deviceInterfaceModule, devConf.getPort());
/* 395 */     map.analogInput.put(devConf.getName(), analogInput);
/*     */   }
/*     */   
/*     */   private void mapPwmOutputDevice(HardwareMap map, DeviceManager deviceMgr, PWMOutputController pwmOutputController, DeviceConfiguration devConf) {
/* 399 */     if (!devConf.isEnabled()) return;
/* 400 */     PWMOutput pwmOutput = deviceMgr.createPwmOutputDevice(pwmOutputController, devConf.getPort());
/* 401 */     map.pwmOutput.put(devConf.getName(), pwmOutput);
/*     */   }
/*     */   
/*     */   private void mapI2cDevice(HardwareMap map, DeviceManager deviceMgr, I2cController i2cController, DeviceConfiguration devConf) {
/* 405 */     if (!devConf.isEnabled()) return;
/* 406 */     I2cDevice i2cDevice = deviceMgr.createI2cDevice(i2cController, devConf.getPort());
/* 407 */     map.i2cDevice.put(devConf.getName(), i2cDevice);
/*     */   }
/*     */   
/*     */   private void mapI2cDeviceSynch(HardwareMap map, DeviceManager deviceMgr, I2cController i2cController, DeviceConfiguration devConf) {
/* 411 */     if (!devConf.isEnabled()) return;
/* 412 */     I2cDevice i2cDevice = deviceMgr.createI2cDevice(i2cController, devConf.getPort());
/* 413 */     I2cDeviceSynch i2cDeviceSynch = new I2cDeviceSynchImpl(i2cDevice, true);
/* 414 */     map.i2cDeviceSynch.put(devConf.getName(), i2cDeviceSynch);
/*     */   }
/*     */   
/*     */   private void mapUserI2cDevice(HardwareMap map, DeviceManager deviceMgr, I2cController i2cController, DeviceConfiguration devConf) {
/* 418 */     if (!devConf.isEnabled()) return;
/* 419 */     UserSensorType userType = (UserSensorType)devConf.getType();
/* 420 */     HardwareDevice hardwareDevice = deviceMgr.createUserI2cDevice(i2cController, devConf.getPort(), userType);
/* 421 */     if (hardwareDevice != null)
/*     */     {
/* 423 */       map.put(devConf.getName(), hardwareDevice);
/*     */     }
/*     */   }
/*     */   
/*     */   private void mapAnalogOutputDevice(HardwareMap map, DeviceManager deviceMgr, DeviceInterfaceModule deviceInterfaceModule, DeviceConfiguration devConf) {
/* 428 */     if (!devConf.isEnabled()) return;
/* 429 */     AnalogOutput analogOutput = deviceMgr.createAnalogOutputDevice(deviceInterfaceModule, devConf.getPort());
/* 430 */     map.analogOutput.put(devConf.getName(), analogOutput);
/*     */   }
/*     */   
/*     */   private void mapOpticalDistanceSensor(HardwareMap map, DeviceManager deviceMgr, DeviceInterfaceModule deviceInterfaceModule, DeviceConfiguration devConf) {
/* 434 */     if (!devConf.isEnabled()) return;
/* 435 */     OpticalDistanceSensor opticalDistanceSensor = deviceMgr.createAnalogOpticalDistanceSensor(deviceInterfaceModule, devConf.getPort());
/* 436 */     map.opticalDistanceSensor.put(devConf.getName(), opticalDistanceSensor);
/*     */   }
/*     */   
/*     */   private void mapNxtTouchSensor(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 440 */     if (!devConf.isEnabled()) return;
/* 441 */     TouchSensor nxtTouchSensor = deviceMgr.createNxtTouchSensor(legacyModule, devConf.getPort());
/* 442 */     map.touchSensor.put(devConf.getName(), nxtTouchSensor);
/*     */   }
/*     */   
/*     */   private void mapNxtTouchSensorMultiplexer(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 446 */     if (!devConf.isEnabled()) return;
/* 447 */     TouchSensorMultiplexer nxtTouchSensorMultiplexer = deviceMgr.createNxtTouchSensorMultiplexer(legacyModule, devConf.getPort());
/* 448 */     map.touchSensorMultiplexer.put(devConf.getName(), nxtTouchSensorMultiplexer);
/*     */   }
/*     */   
/*     */   private void mapSonarSensor(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 452 */     if (!devConf.isEnabled()) return;
/* 453 */     UltrasonicSensor sonarSensor = deviceMgr.createNxtUltrasonicSensor(legacyModule, devConf.getPort());
/* 454 */     map.ultrasonicSensor.put(devConf.getName(), sonarSensor);
/*     */   }
/*     */   
/*     */   private void mapNxtColorSensor(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 458 */     if (!devConf.isEnabled()) return;
/* 459 */     ColorSensor colorSensor = deviceMgr.createNxtColorSensor(legacyModule, devConf.getPort());
/* 460 */     map.colorSensor.put(devConf.getName(), colorSensor);
/*     */   }
/*     */   
/*     */   private void mapNxtGyroSensor(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 464 */     if (!devConf.isEnabled()) return;
/* 465 */     GyroSensor gyro = deviceMgr.createNxtGyroSensor(legacyModule, devConf.getPort());
/* 466 */     map.gyroSensor.put(devConf.getName(), gyro);
/*     */   }
/*     */   
/*     */   private void mapNxtCompassSensor(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 470 */     if (!devConf.isEnabled()) return;
/* 471 */     CompassSensor compass = deviceMgr.createNxtCompassSensor(legacyModule, devConf.getPort());
/* 472 */     map.compassSensor.put(devConf.getName(), compass);
/*     */   }
/*     */   
/*     */   private void mapNxtIrSeekerSensor(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 476 */     if (!devConf.isEnabled()) return;
/* 477 */     IrSeekerSensor irSeeker = deviceMgr.createNxtIrSeekerSensor(legacyModule, devConf.getPort());
/* 478 */     map.irSeekerSensor.put(devConf.getName(), irSeeker);
/*     */   }
/*     */   
/*     */   private void mapNxtLightSensor(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 482 */     if (!devConf.isEnabled()) return;
/* 483 */     LightSensor light = deviceMgr.createNxtLightSensor(legacyModule, devConf.getPort());
/* 484 */     map.lightSensor.put(devConf.getName(), light);
/*     */   }
/*     */   
/*     */   private void mapNxtAccelerationSensor(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 488 */     if (!devConf.isEnabled()) return;
/* 489 */     AccelerationSensor accel = deviceMgr.createNxtAccelerationSensor(legacyModule, devConf.getPort());
/* 490 */     map.accelerationSensor.put(devConf.getName(), accel);
/*     */   }
/*     */   
/*     */   private void mapNxtDcMotorController(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration ctrlConf) {
/* 494 */     if (!ctrlConf.isEnabled()) return;
/* 495 */     HiTechnicNxtDcMotorController dcMotorController = (HiTechnicNxtDcMotorController)deviceMgr.createNxtDcMotorController(legacyModule, ctrlConf.getPort());
/* 496 */     map.dcMotorController.put(ctrlConf.getName(), dcMotorController);
/* 497 */     for (DeviceConfiguration motorConf : ((MotorControllerConfiguration)ctrlConf).getMotors()) {
/* 498 */       mapMotor(map, deviceMgr, motorConf, dcMotorController);
/*     */     }
/*     */     
/* 501 */     VoltageSensor voltageSensor = dcMotorController;
/* 502 */     map.voltageSensor.put(ctrlConf.getName(), voltageSensor);
/*     */   }
/*     */   
/*     */   private void mapNxtServoController(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 506 */     if (!devConf.isEnabled()) return;
/* 507 */     ServoController sc = deviceMgr.createNxtServoController(legacyModule, devConf.getPort());
/* 508 */     map.servoController.put(devConf.getName(), sc);
/* 509 */     for (DeviceConfiguration servoConf : ((ServoControllerConfiguration)devConf).getServos()) {
/* 510 */       mapServo(map, deviceMgr, servoConf, sc);
/*     */     }
/*     */   }
/*     */   
/*     */   private void mapMatrixController(HardwareMap map, DeviceManager deviceMgr, LegacyModule legacyModule, DeviceConfiguration devConf) {
/* 515 */     if (!devConf.isEnabled()) return;
/* 516 */     MatrixMasterController master = new MatrixMasterController((ModernRoboticsUsbLegacyModule)legacyModule, devConf.getPort());
/*     */     
/* 518 */     DcMotorController mc = new MatrixDcMotorController(master);
/* 519 */     map.dcMotorController.put(devConf.getName() + "Motor", mc);
/* 520 */     map.dcMotorController.put(devConf.getName(), mc);
/* 521 */     for (DeviceConfiguration motorConf : ((MatrixControllerConfiguration)devConf).getMotors()) {
/* 522 */       mapMotor(map, deviceMgr, motorConf, mc);
/*     */     }
/*     */     
/* 525 */     ServoController sc = new MatrixServoController(master);
/* 526 */     map.servoController.put(devConf.getName() + "Servo", sc);
/* 527 */     map.servoController.put(devConf.getName(), sc);
/* 528 */     for (DeviceConfiguration servoConf : ((MatrixControllerConfiguration)devConf).getServos()) {
/* 529 */       mapServo(map, deviceMgr, servoConf, sc);
/*     */     }
/*     */   }
/*     */   
/*     */   private void mapAdafruitColorSensor(HardwareMap map, DeviceManager deviceMgr, I2cController i2cController, DeviceConfiguration devConf) {
/* 534 */     if (!devConf.isEnabled()) return;
/* 535 */     ColorSensor colorSensor = deviceMgr.createAdafruitI2cColorSensor(i2cController, devConf.getPort());
/* 536 */     map.colorSensor.put(devConf.getName(), colorSensor);
/*     */   }
/*     */   
/*     */   private void mapLED(HardwareMap map, DeviceManager deviceMgr, DigitalChannelController digitalChannelController, DeviceConfiguration devConf) {
/* 540 */     if (!devConf.isEnabled()) return;
/* 541 */     LED led = deviceMgr.createLED(digitalChannelController, devConf.getPort());
/* 542 */     map.led.put(devConf.getName(), led);
/*     */   }
/*     */   
/*     */   private void mapModernRoboticsColorSensor(HardwareMap map, DeviceManager deviceMgr, I2cController i2cController, DeviceConfiguration devConf) {
/* 546 */     if (!devConf.isEnabled()) return;
/* 547 */     ColorSensor colorSensor = deviceMgr.createModernRoboticsI2cColorSensor(i2cController, devConf.getPort());
/* 548 */     map.colorSensor.put(devConf.getName(), colorSensor);
/*     */   }
/*     */   
/*     */   private void mapModernRoboticsGyro(HardwareMap map, DeviceManager deviceMgr, I2cController i2cController, DeviceConfiguration devConf) {
/* 552 */     if (!devConf.isEnabled()) return;
/* 553 */     GyroSensor gyroSensor = deviceMgr.createModernRoboticsI2cGyroSensor(i2cController, devConf.getPort());
/* 554 */     map.gyroSensor.put(devConf.getName(), gyroSensor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void noteSerialNumberType(Context context, SerialNumber serialNumber, String typeName)
/*     */   {
/* 562 */     synchronized (deviceDisplayNames) {
/* 563 */       deviceDisplayNames.put(serialNumber.toString(), String.format("%s [%s]", new Object[] { typeName, serialNumber.toString(context) }));
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getDeviceDisplayName(Context context, SerialNumber serialNumber) {
/* 568 */     synchronized (deviceDisplayNames) {
/* 569 */       String result = (String)deviceDisplayNames.get(serialNumber.toString());
/* 570 */       if (result == null) {
/* 571 */         result = String.format(context.getString(R.string.deviceDisplayNameUnknownUSBDevice), new Object[] { serialNumber.toString(context) });
/*     */       }
/* 573 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\HardwareFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */