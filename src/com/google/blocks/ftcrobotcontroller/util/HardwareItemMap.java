/*     */ package com.google.blocks.ftcrobotcontroller.util;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import com.qualcomm.ftccommon.configuration.RobotConfigFile;
/*     */ import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ public class HardwareItemMap
/*     */ {
/*  39 */   private final Map<HardwareType, List<HardwareItem>> map = new TreeMap();
/*     */   
/*     */ 
/*  42 */   private final Set<DeviceConfiguration> devices = new HashSet();
/*     */   
/*     */ 
/*     */   public static HardwareItemMap newHardwareItemMap(Activity activity)
/*     */   {
/*     */     try
/*     */     {
/*  49 */       RobotConfigFileManager robotConfigFileManager = new RobotConfigFileManager(activity);
/*  50 */       RobotConfigFile activeConfig = robotConfigFileManager.getActiveConfig();
/*  51 */       XmlPullParser pullParser = activeConfig.getXml();
/*     */       try {
/*  53 */         HardwareItemMap localHardwareItemMap = new HardwareItemMap(pullParser);return localHardwareItemMap;
/*     */       }
/*     */       finally {}
/*     */       
/*     */ 
/*     */ 
/*  59 */       return new HardwareItemMap();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  58 */       RobotLog.logStacktrace(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HardwareItemMap newHardwareItemMap(HardwareMap hardwareMap)
/*     */   {
/*  68 */     return new HardwareItemMap(hardwareMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   HardwareItemMap() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private HardwareItemMap(XmlPullParser pullParser)
/*     */   {
/*     */     try
/*     */     {
/*  84 */       ReadXMLFileHandler readXMLFileHandler = new ReadXMLFileHandler();
/*  85 */       for (ControllerConfiguration controllerConfiguration : readXMLFileHandler.parse(pullParser)) {
/*  86 */         addDevice(controllerConfiguration);
/*     */       }
/*     */     } catch (RobotCoreException e) {
/*  89 */       RobotLog.logStacktrace(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   HardwareItemMap(Reader reader)
/*     */   {
/*     */     try
/*     */     {
/* 100 */       ReadXMLFileHandler readXMLFileHandler = new ReadXMLFileHandler();
/* 101 */       for (ControllerConfiguration controllerConfiguration : readXMLFileHandler.parse(reader)) {
/* 102 */         addDevice(controllerConfiguration);
/*     */       }
/*     */     } catch (RobotCoreException e) {
/* 105 */       RobotLog.logStacktrace(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private HardwareItemMap(HardwareMap hardwareMap)
/*     */   {
/*     */     HardwareType hardwareType;
/*     */     
/* 114 */     for (hardwareType : HardwareType.values()) {
/* 115 */       HardwareMap.DeviceMapping<? extends HardwareDevice> deviceMapping = getDeviceMapping(hardwareType, hardwareMap);
/*     */       
/* 117 */       for (Map.Entry<String, ? extends HardwareDevice> entry : deviceMapping.entrySet()) {
/* 118 */         addHardwareItem(hardwareType, (String)entry.getKey());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addController(ControllerConfiguration controllerConfiguration)
/*     */   {
/* 128 */     for (DeviceConfiguration deviceConfiguration : controllerConfiguration.getDevices()) {
/* 129 */       addDevice(deviceConfiguration);
/*     */     }
/* 131 */     if ((controllerConfiguration instanceof DeviceInterfaceModuleConfiguration)) {
/* 132 */       DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = (DeviceInterfaceModuleConfiguration)controllerConfiguration;
/*     */       
/*     */ 
/* 135 */       for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getPwmOutputs()) {
/* 136 */         addDevice(deviceConfiguration);
/*     */       }
/*     */       
/* 139 */       for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getI2cDevices()) {
/* 140 */         addDevice(deviceConfiguration);
/*     */       }
/*     */       
/* 143 */       for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getAnalogInputDevices()) {
/* 144 */         addDevice(deviceConfiguration);
/*     */       }
/*     */       
/* 147 */       for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getDigitalDevices()) {
/* 148 */         addDevice(deviceConfiguration);
/*     */       }
/*     */       
/* 151 */       for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getAnalogOutputDevices()) {
/* 152 */         addDevice(deviceConfiguration);
/*     */       }
/*     */     }
/* 155 */     if ((controllerConfiguration instanceof MatrixControllerConfiguration)) {
/* 156 */       MatrixControllerConfiguration matrixControllerConfiguration = (MatrixControllerConfiguration)controllerConfiguration;
/*     */       
/* 158 */       for (DeviceConfiguration deviceConfiguration : matrixControllerConfiguration.getServos()) {
/* 159 */         addDevice(deviceConfiguration);
/*     */       }
/* 161 */       for (DeviceConfiguration deviceConfiguration : matrixControllerConfiguration.getMotors()) {
/* 162 */         addDevice(deviceConfiguration);
/*     */       }
/*     */     }
/* 165 */     if ((controllerConfiguration instanceof MotorControllerConfiguration)) {
/* 166 */       MotorControllerConfiguration motorControllerConfiguration = (MotorControllerConfiguration)controllerConfiguration;
/*     */       
/* 168 */       for (DeviceConfiguration deviceConfiguration : motorControllerConfiguration.getMotors()) {
/* 169 */         addDevice(deviceConfiguration);
/*     */       }
/*     */     }
/* 172 */     if ((controllerConfiguration instanceof ServoControllerConfiguration)) {
/* 173 */       ServoControllerConfiguration servoControllerConfiguration = (ServoControllerConfiguration)controllerConfiguration;
/*     */       
/* 175 */       for (DeviceConfiguration deviceConfiguration : servoControllerConfiguration.getServos()) {
/* 176 */         addDevice(deviceConfiguration);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addDevice(DeviceConfiguration deviceConfiguration)
/*     */   {
/* 187 */     if ((this.devices.add(deviceConfiguration)) && 
/* 188 */       (deviceConfiguration.isEnabled())) {
/* 189 */       if (HardwareUtil.isSupported(deviceConfiguration)) {
/* 190 */         HardwareType hardwareType = HardwareUtil.getHardwareType(deviceConfiguration);
/* 191 */         addHardwareItem(hardwareType, deviceConfiguration.getName());
/*     */       }
/* 193 */       if ((deviceConfiguration instanceof ControllerConfiguration)) {
/* 194 */         addController((ControllerConfiguration)deviceConfiguration);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static HardwareMap.DeviceMapping<? extends HardwareDevice> getDeviceMapping(HardwareType hardwareType, HardwareMap hardwareMap)
/*     */   {
/* 206 */     switch (hardwareType) {
/*     */     case ACCELERATION_SENSOR: 
/* 208 */       return hardwareMap.accelerationSensor;
/*     */     case ANALOG_INPUT: 
/* 210 */       return hardwareMap.analogInput;
/*     */     case ANALOG_OUTPUT: 
/* 212 */       return hardwareMap.analogOutput;
/*     */     case COLOR_SENSOR: 
/* 214 */       return hardwareMap.colorSensor;
/*     */     case COMPASS_SENSOR: 
/* 216 */       return hardwareMap.compassSensor;
/*     */     case CR_SERVO: 
/* 218 */       return hardwareMap.crservo;
/*     */     case DC_MOTOR: 
/* 220 */       return hardwareMap.dcMotor;
/*     */     case GYRO_SENSOR: 
/* 222 */       return hardwareMap.gyroSensor;
/*     */     case IR_SEEKER_SENSOR: 
/* 224 */       return hardwareMap.irSeekerSensor;
/*     */     case LED: 
/* 226 */       return hardwareMap.led;
/*     */     case LIGHT_SENSOR: 
/* 228 */       return hardwareMap.lightSensor;
/*     */     case OPTICAL_DISTANCE_SENSOR: 
/* 230 */       return hardwareMap.opticalDistanceSensor;
/*     */     case SERVO: 
/* 232 */       return hardwareMap.servo;
/*     */     case SERVO_CONTROLLER: 
/* 234 */       return hardwareMap.servoController;
/*     */     case TOUCH_SENSOR: 
/* 236 */       return hardwareMap.touchSensor;
/*     */     case ULTRASONIC_SENSOR: 
/* 238 */       return hardwareMap.ultrasonicSensor;
/*     */     case VOLTAGE_SENSOR: 
/* 240 */       return hardwareMap.voltageSensor;
/*     */     }
/* 242 */     throw new IllegalArgumentException("Unknown hardware type " + hardwareType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void addHardwareItem(HardwareType hardwareType, String deviceName)
/*     */   {
/* 251 */     List<HardwareItem> hardwareItemList = (List)this.map.get(hardwareType);
/* 252 */     if (hardwareItemList == null) {
/* 253 */       hardwareItemList = new ArrayList();
/* 254 */       this.map.put(hardwareType, hardwareItemList);
/*     */     }
/* 256 */     hardwareItemList.add(new HardwareItem(hardwareType, deviceName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getHardwareTypeCount()
/*     */   {
/* 263 */     return this.map.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(HardwareType hardwareType)
/*     */   {
/* 270 */     return this.map.containsKey(hardwareType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<HardwareItem> getHardwareItems(HardwareType hardwareType)
/*     */   {
/* 277 */     return this.map.containsKey(hardwareType) ? Collections.unmodifiableList((List)this.map.get(hardwareType)) : Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HardwareItem> getAllHardwareItems()
/*     */   {
/* 286 */     List<HardwareItem> list = new ArrayList();
/* 287 */     for (List<HardwareItem> hardwareItems : this.map.values()) {
/* 288 */       list.addAll(hardwareItems);
/*     */     }
/* 290 */     return Collections.unmodifiableList(list);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<HardwareType> getHardwareTypes()
/*     */   {
/* 297 */     return Collections.unmodifiableSet(this.map.keySet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 304 */     if ((o instanceof HardwareItemMap)) {
/* 305 */       HardwareItemMap that = (HardwareItemMap)o;
/* 306 */       return this.map.equals(that.map);
/*     */     }
/* 308 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 313 */     return this.map.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\util\HardwareItemMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */