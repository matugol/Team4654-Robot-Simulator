/*     */ package com.qualcomm.robotcore.hardware.configuration;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlPullParserFactory;
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
/*     */ public class ReadXMLFileHandler
/*     */ {
/*  49 */   private static boolean DEBUG = false;
/*     */   
/*     */   List<ControllerConfiguration> deviceControllers;
/*     */   private static final int NUMBER_OF_PWM_DEVICES = 2;
/*     */   private static final int NUMBER_OF_DIGITAL_DEVICES = 8;
/*     */   private static final int NUMBER_OF_ANALOG_INPUT_DEVICES = 8;
/*     */   private static final int NUMBER_OF_ANALOG_OUTPUT_DEVICES = 2;
/*     */   private static final int NUMBER_OF_I2C_DEVICES = 6;
/*     */   private static final int NUMBER_OF_LEGACY_MODULE_PORTS = 6;
/*     */   private static final int NUMBER_OF_SERVOS = 6;
/*     */   private static final int NUMBER_OF_MOTORS = 2;
/*     */   private static final int NUMBER_OF_MOTORS_MATRIX = 4;
/*     */   private static final int NUMBER_OF_SERVOS_MATRIX = 4;
/*     */   private XmlPullParser parser;
/*     */   
/*     */   public ReadXMLFileHandler()
/*     */   {
/*  66 */     this.deviceControllers = new ArrayList();
/*     */   }
/*     */   
/*     */   public List<ControllerConfiguration> getDeviceControllers() {
/*  70 */     return this.deviceControllers;
/*     */   }
/*     */   
/*     */   public static XmlPullParser xmlPullParserFromReader(Reader reader)
/*     */   {
/*  75 */     XmlPullParser parser = null;
/*     */     try {
/*  77 */       XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
/*  78 */       factory.setNamespaceAware(true);
/*  79 */       parser = factory.newPullParser();
/*  80 */       parser.setInput(reader);
/*     */     } catch (XmlPullParserException e) {
/*  82 */       e.printStackTrace();
/*     */     }
/*  84 */     return parser;
/*     */   }
/*     */   
/*     */   public List<ControllerConfiguration> parse(Reader reader) throws RobotCoreException {
/*  88 */     this.parser = xmlPullParserFromReader(reader);
/*  89 */     return performParse();
/*     */   }
/*     */   
/*     */   public List<ControllerConfiguration> parse(XmlPullParser parser) throws RobotCoreException {
/*  93 */     this.parser = parser;
/*  94 */     return performParse();
/*     */   }
/*     */   
/*     */   private List<ControllerConfiguration> performParse() throws RobotCoreException {
/*     */     try {
/*  99 */       int eventType = this.parser.getEventType();
/* 100 */       while (eventType != 1) {
/* 101 */         ConfigurationType configurationType = deform(this.parser.getName());
/* 102 */         if (eventType == 2) {
/* 103 */           if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
/* 104 */             this.deviceControllers.add(handleMotorController(true));
/*     */           }
/* 106 */           else if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
/* 107 */             this.deviceControllers.add(handleServoController(true));
/*     */           }
/* 109 */           else if (configurationType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
/* 110 */             this.deviceControllers.add(handleLegacyModule());
/*     */           }
/* 112 */           else if (configurationType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
/* 113 */             this.deviceControllers.add(handleDeviceInterfaceModule());
/*     */           }
/*     */         }
/* 116 */         eventType = this.parser.next();
/*     */       }
/*     */     }
/*     */     catch (XmlPullParserException e) {
/* 120 */       RobotLog.w("XmlPullParserException");
/* 121 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/* 123 */       RobotLog.w("IOException");
/* 124 */       e.printStackTrace();
/*     */     }
/*     */     
/* 127 */     return this.deviceControllers;
/*     */   }
/*     */   
/*     */   public List<ControllerConfiguration> parse(InputStream is) throws RobotCoreException
/*     */   {
/* 132 */     this.parser = null;
/*     */     try {
/* 134 */       XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
/* 135 */       factory.setNamespaceAware(true);
/* 136 */       this.parser = factory.newPullParser();
/* 137 */       this.parser.setInput(is, null);
/*     */     } catch (XmlPullParserException e) {
/* 139 */       e.printStackTrace();
/*     */     }
/* 141 */     return performParse();
/*     */   }
/*     */   
/*     */   private ControllerConfiguration handleDeviceInterfaceModule() throws IOException, XmlPullParserException, RobotCoreException {
/* 145 */     String name = this.parser.getAttributeValue(null, "name");
/* 146 */     String serialNumber = this.parser.getAttributeValue(null, "serialNumber");
/*     */     
/*     */ 
/* 149 */     ArrayList<DeviceConfiguration> pwmDevices = buildEmptyDevices(2, BuiltInConfigurationType.PULSE_WIDTH_DEVICE);
/* 150 */     ArrayList<DeviceConfiguration> i2cDevices = buildEmptyDevices(6, BuiltInConfigurationType.I2C_DEVICE);
/* 151 */     ArrayList<DeviceConfiguration> analogInputDevices = buildEmptyDevices(8, BuiltInConfigurationType.ANALOG_INPUT);
/* 152 */     ArrayList<DeviceConfiguration> digitalDevices = buildEmptyDevices(8, BuiltInConfigurationType.DIGITAL_DEVICE);
/* 153 */     ArrayList<DeviceConfiguration> analogOutputDevices = buildEmptyDevices(2, BuiltInConfigurationType.ANALOG_OUTPUT);
/*     */     
/* 155 */     int eventType = this.parser.next();
/* 156 */     ConfigurationType configurationType = deform(this.parser.getName());
/*     */     
/* 158 */     while (eventType != 1) {
/* 159 */       if (eventType == 3) {
/* 160 */         if (configurationType == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 164 */         if (DEBUG) RobotLog.e("[handleDeviceInterfaceModule] tagname: " + configurationType);
/* 165 */         if (configurationType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE)
/*     */         {
/* 167 */           DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = new DeviceInterfaceModuleConfiguration(name, new SerialNumber(serialNumber));
/*     */           
/* 169 */           deviceInterfaceModuleConfiguration.setPwmOutputs(pwmDevices);
/* 170 */           deviceInterfaceModuleConfiguration.setI2cDevices(i2cDevices);
/* 171 */           deviceInterfaceModuleConfiguration.setAnalogInputDevices(analogInputDevices);
/* 172 */           deviceInterfaceModuleConfiguration.setDigitalDevices(digitalDevices);
/* 173 */           deviceInterfaceModuleConfiguration.setAnalogOutputDevices(analogOutputDevices);
/* 174 */           deviceInterfaceModuleConfiguration.setEnabled(true);
/* 175 */           return deviceInterfaceModuleConfiguration;
/*     */         }
/*     */       }
/* 178 */       if (eventType == 2) {
/* 179 */         if ((configurationType == BuiltInConfigurationType.ANALOG_INPUT) || (configurationType == BuiltInConfigurationType.OPTICAL_DISTANCE_SENSOR))
/*     */         {
/* 181 */           DeviceConfiguration dev = handleDevice();
/* 182 */           analogInputDevices.set(dev.getPort(), dev);
/*     */         }
/* 184 */         if (configurationType == BuiltInConfigurationType.PULSE_WIDTH_DEVICE) {
/* 185 */           DeviceConfiguration dev = handleDevice();
/* 186 */           pwmDevices.set(dev.getPort(), dev);
/*     */         }
/* 188 */         if (configurationType.isI2cDevice()) {
/* 189 */           DeviceConfiguration dev = handleDevice();
/* 190 */           i2cDevices.set(dev.getPort(), dev);
/*     */         }
/* 192 */         if (configurationType == BuiltInConfigurationType.ANALOG_OUTPUT) {
/* 193 */           DeviceConfiguration dev = handleDevice();
/* 194 */           analogOutputDevices.set(dev.getPort(), dev);
/*     */         }
/* 196 */         if ((configurationType == BuiltInConfigurationType.DIGITAL_DEVICE) || (configurationType == BuiltInConfigurationType.TOUCH_SENSOR) || (configurationType == BuiltInConfigurationType.LED))
/*     */         {
/*     */ 
/* 199 */           DeviceConfiguration dev = handleDevice();
/* 200 */           digitalDevices.set(dev.getPort(), dev);
/*     */         }
/*     */       }
/* 203 */       eventType = this.parser.next();
/* 204 */       configurationType = deform(this.parser.getName());
/*     */     }
/*     */     
/* 207 */     RobotLog.logAndThrow("Reached the end of the XML file while parsing.");
/* 208 */     return null;
/*     */   }
/*     */   
/*     */   private ControllerConfiguration handleLegacyModule() throws IOException, XmlPullParserException, RobotCoreException {
/* 212 */     String name = this.parser.getAttributeValue(null, "name");
/* 213 */     String serialNumber = this.parser.getAttributeValue(null, "serialNumber");
/*     */     
/* 215 */     ArrayList<DeviceConfiguration> modules = buildEmptyDevices(6, BuiltInConfigurationType.NOTHING);
/*     */     
/* 217 */     int eventType = this.parser.next();
/* 218 */     ConfigurationType configurationType = deform(this.parser.getName());
/*     */     
/* 220 */     while (eventType != 1) {
/* 221 */       if (eventType == 3) {
/* 222 */         if (configurationType == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 226 */         if (configurationType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER)
/*     */         {
/* 228 */           LegacyModuleControllerConfiguration legacyModule = new LegacyModuleControllerConfiguration(name, modules, new SerialNumber(serialNumber));
/* 229 */           legacyModule.setEnabled(true);
/* 230 */           return legacyModule;
/*     */         }
/*     */       }
/* 233 */       if (eventType == 2) {
/* 234 */         if (DEBUG) RobotLog.e("[handleLegacyModule] tagname: " + configurationType);
/* 235 */         if ((configurationType == BuiltInConfigurationType.COMPASS) || (configurationType == BuiltInConfigurationType.LIGHT_SENSOR) || (configurationType == BuiltInConfigurationType.IR_SEEKER) || (configurationType == BuiltInConfigurationType.ACCELEROMETER) || (configurationType == BuiltInConfigurationType.GYRO) || (configurationType == BuiltInConfigurationType.TOUCH_SENSOR) || (configurationType == BuiltInConfigurationType.TOUCH_SENSOR_MULTIPLEXER) || (configurationType == BuiltInConfigurationType.ULTRASONIC_SENSOR) || (configurationType == BuiltInConfigurationType.COLOR_SENSOR) || (configurationType == BuiltInConfigurationType.NOTHING))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 245 */           DeviceConfiguration dev = handleDevice();
/* 246 */           modules.set(dev.getPort(), dev);
/*     */         }
/* 248 */         else if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
/* 249 */           ControllerConfiguration mc = handleMotorController(false);
/* 250 */           modules.set(mc.getPort(), mc);
/*     */         }
/* 252 */         else if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
/* 253 */           ControllerConfiguration sc = handleServoController(false);
/* 254 */           modules.set(sc.getPort(), sc);
/*     */         }
/* 256 */         else if (configurationType == BuiltInConfigurationType.MATRIX_CONTROLLER) {
/* 257 */           ControllerConfiguration mc = handleMatrixController();
/* 258 */           modules.set(mc.getPort(), mc);
/*     */         }
/*     */       }
/* 261 */       eventType = this.parser.next();
/* 262 */       configurationType = deform(this.parser.getName());
/*     */     }
/*     */     
/* 265 */     return new LegacyModuleControllerConfiguration(name, modules, new SerialNumber(serialNumber));
/*     */   }
/*     */   
/*     */   private DeviceConfiguration handleDevice() {
/* 269 */     ConfigurationType configurationType = deform(this.parser.getName());
/*     */     
/* 271 */     int port = Integer.parseInt(this.parser.getAttributeValue(null, "port"));
/* 272 */     DeviceConfiguration device = new DeviceConfiguration(port);
/* 273 */     device.setType(configurationType);
/*     */     
/* 275 */     device.setName(this.parser.getAttributeValue(null, "name"));
/*     */     
/*     */ 
/* 278 */     device.setEnabled(true);
/*     */     
/* 280 */     if (DEBUG) RobotLog.e("[handleDevice] name: " + device.getName() + ", port: " + device.getPort() + ", type: " + device.getType());
/* 281 */     return device;
/*     */   }
/*     */   
/*     */   private ArrayList<DeviceConfiguration> buildEmptyDevices(int size, ConfigurationType type)
/*     */   {
/* 286 */     ArrayList<DeviceConfiguration> list = new ArrayList();
/* 287 */     for (int i = 0; i < size; i++) {
/* 288 */       if ((type == BuiltInConfigurationType.SERVO) || (type == BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO)) {
/* 289 */         list.add(new ServoConfiguration(i + 1, type, "NO$DEVICE$ATTACHED", false));
/* 290 */       } else if (type == BuiltInConfigurationType.MOTOR) {
/* 291 */         list.add(new MotorConfiguration(i + 1, "NO$DEVICE$ATTACHED", false));
/*     */       } else {
/* 293 */         list.add(new DeviceConfiguration(i, type, "NO$DEVICE$ATTACHED", false));
/*     */       }
/*     */     }
/* 296 */     return list;
/*     */   }
/*     */   
/*     */   private MotorConfiguration handleMotor() {
/* 300 */     int port = Integer.parseInt(this.parser.getAttributeValue(null, "port"));
/* 301 */     String motorName = this.parser.getAttributeValue(null, "name");
/* 302 */     MotorConfiguration motor = new MotorConfiguration(port, motorName, true);
/* 303 */     return motor;
/*     */   }
/*     */   
/*     */   private ServoConfiguration handleServo(ConfigurationType type) {
/* 307 */     int port = Integer.parseInt(this.parser.getAttributeValue(null, "port"));
/* 308 */     String servoName = this.parser.getAttributeValue(null, "name");
/* 309 */     ServoConfiguration servo = new ServoConfiguration(port, type, servoName, true);
/* 310 */     return servo;
/*     */   }
/*     */   
/*     */   private ControllerConfiguration handleMatrixController() throws IOException, XmlPullParserException, RobotCoreException {
/* 314 */     String name = this.parser.getAttributeValue(null, "name");
/*     */     
/*     */ 
/* 317 */     String serialNumber = new SerialNumber().toString();
/* 318 */     int controllerPort = Integer.parseInt(this.parser.getAttributeValue(null, "port"));
/*     */     
/* 320 */     ArrayList<DeviceConfiguration> servos = buildEmptyDevices(4, BuiltInConfigurationType.SERVO);
/* 321 */     ArrayList<DeviceConfiguration> motors = buildEmptyDevices(4, BuiltInConfigurationType.MOTOR);
/*     */     
/* 323 */     int eventType = this.parser.next();
/* 324 */     ConfigurationType configurationType = deform(this.parser.getName());
/*     */     
/* 326 */     while (eventType != 1) {
/* 327 */       if (eventType == 3) {
/* 328 */         if (configurationType == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 332 */         if (configurationType == BuiltInConfigurationType.MATRIX_CONTROLLER)
/*     */         {
/* 334 */           MatrixControllerConfiguration newController = new MatrixControllerConfiguration(name, motors, servos, new SerialNumber(serialNumber));
/* 335 */           newController.setPort(controllerPort);
/* 336 */           newController.setEnabled(true);
/* 337 */           return newController;
/*     */         }
/*     */       }
/* 340 */       if (eventType == 2) {
/* 341 */         if ((configurationType == BuiltInConfigurationType.SERVO) || (configurationType == BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO)) {
/* 342 */           ServoConfiguration servo = handleServo(configurationType);
/*     */           
/* 344 */           servos.set(servo.getPort() - 1, servo);
/*     */         }
/* 346 */         else if (configurationType == BuiltInConfigurationType.MOTOR) {
/* 347 */           MotorConfiguration motor = handleMotor();
/*     */           
/* 349 */           motors.set(motor.getPort() - 1, motor);
/*     */         }
/*     */       }
/* 352 */       eventType = this.parser.next();
/* 353 */       configurationType = deform(this.parser.getName());
/*     */     }
/*     */     
/* 356 */     RobotLog.logAndThrow("Reached the end of the XML file while parsing.");
/* 357 */     return null;
/*     */   }
/*     */   
/*     */   private ControllerConfiguration handleServoController(boolean isUsbDevice) throws IOException, XmlPullParserException {
/* 361 */     String name = this.parser.getAttributeValue(null, "name");
/*     */     
/*     */ 
/* 364 */     int controllerPort = -1;
/* 365 */     String serialNumber = new SerialNumber().toString();
/* 366 */     if (isUsbDevice) {
/* 367 */       serialNumber = this.parser.getAttributeValue(null, "serialNumber");
/*     */     } else {
/* 369 */       controllerPort = Integer.parseInt(this.parser.getAttributeValue(null, "port"));
/*     */     }
/*     */     
/* 372 */     ArrayList<DeviceConfiguration> servos = buildEmptyDevices(6, BuiltInConfigurationType.SERVO);
/*     */     
/* 374 */     int eventType = this.parser.next();
/* 375 */     ConfigurationType configurationType = deform(this.parser.getName());
/*     */     
/* 377 */     while (eventType != 1) {
/* 378 */       if (eventType == 3) {
/* 379 */         if (configurationType == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 383 */         if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER)
/*     */         {
/* 385 */           ServoControllerConfiguration newController = new ServoControllerConfiguration(name, servos, new SerialNumber(serialNumber));
/* 386 */           newController.setPort(controllerPort);
/* 387 */           newController.setEnabled(true);
/* 388 */           return newController;
/*     */         }
/*     */       }
/* 391 */       if ((eventType == 2) && (
/* 392 */         (configurationType == BuiltInConfigurationType.SERVO) || (configurationType == BuiltInConfigurationType.CONTINUOUS_ROTATION_SERVO))) {
/* 393 */         ServoConfiguration servo = handleServo(configurationType);
/*     */         
/* 395 */         servos.set(servo.getPort() - 1, servo);
/*     */       }
/*     */       
/* 398 */       eventType = this.parser.next();
/* 399 */       configurationType = deform(this.parser.getName());
/*     */     }
/* 401 */     ServoControllerConfiguration newController = new ServoControllerConfiguration(name, servos, new SerialNumber(serialNumber));
/* 402 */     newController.setPort(controllerPort);
/* 403 */     return newController;
/*     */   }
/*     */   
/*     */   private ControllerConfiguration handleMotorController(boolean isUsbDevice) throws IOException, XmlPullParserException {
/* 407 */     String name = this.parser.getAttributeValue(null, "name");
/*     */     
/*     */ 
/* 410 */     int controllerPort = -1;
/* 411 */     String serialNumber = new SerialNumber().toString();
/* 412 */     if (isUsbDevice) {
/* 413 */       serialNumber = this.parser.getAttributeValue(null, "serialNumber");
/*     */     } else {
/* 415 */       controllerPort = Integer.parseInt(this.parser.getAttributeValue(null, "port"));
/*     */     }
/*     */     
/* 418 */     ArrayList<DeviceConfiguration> motors = buildEmptyDevices(2, BuiltInConfigurationType.MOTOR);
/*     */     
/* 420 */     int eventType = this.parser.next();
/* 421 */     ConfigurationType configurationType = deform(this.parser.getName());
/*     */     
/* 423 */     while (eventType != 1) {
/* 424 */       if (eventType == 3) {
/* 425 */         if (configurationType == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 429 */         if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER)
/*     */         {
/* 431 */           MotorControllerConfiguration newController = new MotorControllerConfiguration(name, motors, new SerialNumber(serialNumber));
/* 432 */           newController.setPort(controllerPort);
/* 433 */           newController.setEnabled(true);
/* 434 */           return newController;
/*     */         }
/*     */       }
/* 437 */       if ((eventType == 2) && 
/* 438 */         (configurationType == BuiltInConfigurationType.MOTOR)) {
/* 439 */         MotorConfiguration motor = handleMotor();
/*     */         
/* 441 */         motors.set(motor.getPort() - 1, motor);
/*     */       }
/*     */       
/* 444 */       eventType = this.parser.next();
/* 445 */       configurationType = deform(this.parser.getName());
/*     */     }
/*     */     
/* 448 */     MotorControllerConfiguration newController = new MotorControllerConfiguration(name, motors, new SerialNumber(serialNumber));
/* 449 */     newController.setPort(controllerPort);
/* 450 */     return newController;
/*     */   }
/*     */   
/*     */   private ConfigurationType deform(String xmlTag) {
/* 454 */     ConfigurationType result = null;
/* 455 */     if (xmlTag != null) {
/* 456 */       result = UserSensorTypeManager.getInstance().typeFromTag(xmlTag);
/*     */     }
/* 458 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\ReadXMLFileHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */