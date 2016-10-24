/*     */ package com.google.blocks.ftcrobotcontroller.util;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.res.AssetManager;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
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
/*     */ public class HardwareUtil
/*     */ {
/*     */   private static final String ELAPSED_TIME_DEFAULT_VAR_NAME = "timer";
/*  42 */   private static final Map<ConfigurationType, HardwareType> CONFIGURATION_TYPE_TO_HARDWARE_TYPE = new HashMap();
/*     */   
/*     */   static {
/*  45 */     for (HardwareType hardwareType : HardwareType.values()) {
/*  46 */       for (ConfigurationType configurationType : hardwareType.configurationTypes) {
/*  47 */         CONFIGURATION_TYPE_TO_HARDWARE_TYPE.put(configurationType, hardwareType);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Iterable<HardwareType> getSupportedHardwareTypes()
/*     */   {
/*  60 */     return CONFIGURATION_TYPE_TO_HARDWARE_TYPE.values();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static boolean isSupported(ConfigurationType configurationType)
/*     */   {
/*  68 */     return CONFIGURATION_TYPE_TO_HARDWARE_TYPE.containsKey(configurationType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static boolean isSupported(DeviceConfiguration deviceConfiguration)
/*     */   {
/*  75 */     return isSupported(deviceConfiguration.getType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static HardwareType getHardwareType(ConfigurationType configurationType)
/*     */   {
/*  84 */     return (HardwareType)CONFIGURATION_TYPE_TO_HARDWARE_TYPE.get(configurationType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static HardwareType getHardwareType(DeviceConfiguration deviceConfiguration)
/*     */   {
/*  92 */     return (HardwareType)CONFIGURATION_TYPE_TO_HARDWARE_TYPE.get(deviceConfiguration.getType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String fetchJavaScriptForHardware(Activity activity)
/*     */   {
/*  99 */     return fetchJavaScriptForHardware(HardwareItemMap.newHardwareItemMap(activity));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String fetchJavaScriptForHardware(HardwareItemMap hardwareItemMap)
/*     */   {
/* 107 */     StringBuilder jsHardware = new StringBuilder().append("\n");
/*     */     
/*     */ 
/* 110 */     jsHardware.append("var linearOpModeIdentifier = '").append("linearOpMode").append("';\n\n");
/*     */     
/*     */ 
/*     */ 
/* 114 */     jsHardware.append("var telemetryIdentifier = '").append("telemetry").append("';\n\n");
/*     */     
/*     */ 
/*     */ 
/* 118 */     jsHardware.append("function createGamepadDropdown() {\n").append("  var CHOICES = [\n").append("      ['gamepad1', '").append("gamepad1").append("'],\n").append("      ['gamepad2', '").append("gamepad2").append("'],\n").append("  ];\n").append("  return new Blockly.FieldDropdown(CHOICES);\n").append("}\n\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */     jsHardware.append("var colorIdentifier = '").append("colorAccess").append("';\n\n");
/*     */     
/*     */ 
/*     */ 
/* 132 */     jsHardware.append("var elapsedTimeIdentifier = '").append("elapsedTimeAccess").append("';\n").append("var elapsedTimeDefaultVarName = '").append("timer").append("';\n\n");
/*     */     
/*     */ 
/*     */ 
/* 136 */     for (HardwareType hardwareType : HardwareType.values()) {
/* 137 */       jsHardware.append("function ").append(hardwareType.createDropdownFunctionName).append("() {\n").append("  var CHOICES = [\n");
/*     */       
/*     */ 
/* 140 */       if (hardwareItemMap.contains(hardwareType)) {
/* 141 */         for (HardwareItem hardwareItem : hardwareItemMap.getHardwareItems(hardwareType)) {
/* 142 */           jsHardware.append("      ['").append(hardwareItem.visibleName).append("', '").append(hardwareItem.identifier).append("'],\n");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 147 */       jsHardware.append("  ];\n").append("  return new Blockly.FieldDropdown(CHOICES);\n").append("}\n\n");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 152 */     jsHardware.append("function addReservedWordsForHardware() {\n");
/* 153 */     for (HardwareItem hardwareItem : hardwareItemMap.getAllHardwareItems()) {
/* 154 */       jsHardware.append("  Blockly.JavaScript.addReservedWords('").append(hardwareItem.identifier).append("');\n");
/*     */     }
/*     */     
/* 157 */     jsHardware.append("  Blockly.JavaScript.addReservedWords('").append("blocksOpMode").append("');\n");
/*     */     
/* 159 */     jsHardware.append("  Blockly.JavaScript.addReservedWords('").append("colorAccess").append("');\n");
/*     */     
/* 161 */     jsHardware.append("  Blockly.JavaScript.addReservedWords('").append("elapsedTimeAccess").append("');\n");
/*     */     
/* 163 */     jsHardware.append("  Blockly.JavaScript.addReservedWords('").append("gamepad1").append("');\n");
/*     */     
/* 165 */     jsHardware.append("  Blockly.JavaScript.addReservedWords('").append("gamepad2").append("');\n");
/*     */     
/* 167 */     jsHardware.append("  Blockly.JavaScript.addReservedWords('").append("linearOpMode").append("');\n");
/*     */     
/* 169 */     jsHardware.append("  Blockly.JavaScript.addReservedWords('").append("telemetry").append("');\n");
/*     */     
/* 171 */     jsHardware.append("}\n\n");
/*     */     
/* 173 */     return jsHardware.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String fetchToolbox(Activity activity, AssetManager assetManager)
/*     */     throws IOException
/*     */   {
/* 182 */     return fetchToolbox(HardwareItemMap.newHardwareItemMap(activity), assetManager);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String fetchToolbox(HardwareItemMap hardwareItemMap, AssetManager assetManager)
/*     */     throws IOException
/*     */   {
/* 192 */     StringBuilder xmlToolbox = new StringBuilder();
/* 193 */     xmlToolbox.append("<xml id=\"toolbox\" style=\"display: none\">\n");
/*     */     
/*     */ 
/* 196 */     if (assetManager != null) {
/* 197 */       ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/linear_op_mode.xml");
/* 198 */       ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/gamepad.xml");
/* 199 */       ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/telemetry.xml");
/*     */     }
/*     */     
/* 202 */     for (HardwareType hardwareType : hardwareItemMap.getHardwareTypes()) {
/* 203 */       addHardwareCategoryToToolbox(xmlToolbox, hardwareType, hardwareItemMap.getHardwareItems(hardwareType));
/*     */     }
/*     */     
/*     */ 
/* 207 */     addElapsedTimeCategoryToToolbox(xmlToolbox, assetManager);
/*     */     
/* 209 */     if (assetManager != null) {
/* 210 */       ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/color.xml");
/* 211 */       ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/misc.xml");
/*     */     }
/*     */     
/* 214 */     xmlToolbox.append("</xml>\n");
/* 215 */     return xmlToolbox.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addHardwareCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 225 */     if ((hardwareItems != null) && (hardwareItems.size() > 0)) {
/* 226 */       xmlToolbox.append("  <category name=\"").append(hardwareType.toolboxCategoryName).append("\">\n");
/*     */       
/*     */ 
/* 229 */       switch (hardwareType) {
/*     */       case ACCELERATION_SENSOR: 
/* 231 */         addAccelerationSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 232 */         break;
/*     */       case ANALOG_INPUT: 
/* 234 */         addAnalogInputCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 235 */         break;
/*     */       case ANALOG_OUTPUT: 
/* 237 */         addAnalogOutputCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 238 */         break;
/*     */       case COLOR_SENSOR: 
/* 240 */         addColorSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 241 */         break;
/*     */       case COMPASS_SENSOR: 
/* 243 */         addCompassSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 244 */         break;
/*     */       case CR_SERVO: 
/* 246 */         addCRServoCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 247 */         break;
/*     */       case DC_MOTOR: 
/* 249 */         addDcMotorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 250 */         break;
/*     */       case GYRO_SENSOR: 
/* 252 */         addGyroSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 253 */         break;
/*     */       case IR_SEEKER_SENSOR: 
/* 255 */         addIrSeekerSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 256 */         break;
/*     */       case LED: 
/* 258 */         addLedCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 259 */         break;
/*     */       case LIGHT_SENSOR: 
/* 261 */         addLightSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 262 */         break;
/*     */       case OPTICAL_DISTANCE_SENSOR: 
/* 264 */         addOpticalDistanceSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 265 */         break;
/*     */       case SERVO: 
/* 267 */         addServoCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 268 */         break;
/*     */       case SERVO_CONTROLLER: 
/* 270 */         addServoControllerCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 271 */         break;
/*     */       case TOUCH_SENSOR: 
/* 273 */         addTouchSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 274 */         break;
/*     */       case ULTRASONIC_SENSOR: 
/* 276 */         addUltrasonicSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 277 */         break;
/*     */       case VOLTAGE_SENSOR: 
/* 279 */         addVoltageSensorCategoryToToolbox(xmlToolbox, hardwareType, hardwareItems);
/* 280 */         break;
/*     */       default: 
/* 282 */         throw new IllegalArgumentException("Unknown hardware type " + hardwareType);
/*     */       }
/*     */       
/* 285 */       xmlToolbox.append("  </category>\n");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addAccelerationSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 294 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 297 */     SortedSet<String> properties = new TreeSet();
/* 298 */     properties.add("XAccel");
/* 299 */     properties.add("YAccel");
/* 300 */     properties.add("ZAccel");
/* 301 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addAnalogInputCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 310 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 313 */     SortedSet<String> properties = new TreeSet();
/* 314 */     properties.add("Voltage");
/* 315 */     properties.add("MaxVoltage");
/* 316 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addAnalogOutputCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 325 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 328 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 329 */     Map<String, String> setAnalogOutputVoltageArgs = new HashMap();
/* 330 */     setAnalogOutputVoltageArgs.put("VOLTAGE", ToolboxUtil.makeNumberShadow(512));
/* 331 */     functions.put("setAnalogOutputVoltage", setAnalogOutputVoltageArgs);
/* 332 */     Map<String, String> setAnalogOutputFrequencyArgs = new HashMap();
/* 333 */     setAnalogOutputFrequencyArgs.put("FREQUENCY", ToolboxUtil.makeNumberShadow(100));
/* 334 */     functions.put("setAnalogOutputFrequency", setAnalogOutputFrequencyArgs);
/* 335 */     Map<String, String> setAnalogOutputModeArgs = new HashMap();
/* 336 */     setAnalogOutputModeArgs.put("MODE", ToolboxUtil.makeNumberShadow(0));
/* 337 */     functions.put("setAnalogOutputMode", setAnalogOutputModeArgs);
/* 338 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addColorSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 346 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 349 */     SortedSet<String> properties = new TreeSet();
/* 350 */     properties.add("Red");
/* 351 */     properties.add("Green");
/* 352 */     properties.add("Blue");
/* 353 */     properties.add("Alpha");
/* 354 */     properties.add("Argb");
/* 355 */     properties.add("I2cAddress7Bit");
/* 356 */     properties.add("I2cAddress8Bit");
/* 357 */     Map<String, String[]> setterValues = new HashMap();
/* 358 */     setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
/* 359 */     setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
/* 360 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);
/*     */     
/*     */ 
/* 363 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 364 */     Map<String, String> enableLedArgs = new HashMap();
/* 365 */     enableLedArgs.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
/* 366 */     functions.put("enableLed", enableLedArgs);
/* 367 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addCompassSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 375 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 378 */     SortedSet<String> properties = new TreeSet();
/* 379 */     properties.add("Direction");
/* 380 */     properties.add("CalibrationFailed");
/* 381 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, null);
/*     */     
/*     */ 
/*     */ 
/* 385 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 386 */     Map<String, String> setModeArgs = new HashMap();
/* 387 */     setModeArgs.put("COMPASS_MODE", ToolboxUtil.makeEnumShadow(hardwareType, "compassMode"));
/* 388 */     functions.put("setMode", setModeArgs);
/* 389 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addCRServoCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 397 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 400 */     SortedSet<String> properties = new TreeSet();
/* 401 */     properties.add("Direction");
/* 402 */     properties.add("Power");
/* 403 */     Map<String, String[]> setterValues = new HashMap();
/* 404 */     setterValues.put("Direction", new String[] { ToolboxUtil.makeEnumShadow(hardwareType, "direction") });
/*     */     
/* 406 */     setterValues.put("Power", new String[] { ToolboxUtil.makeNumberShadow(1), ToolboxUtil.makeNumberShadow(0) });
/*     */     
/*     */ 
/*     */ 
/* 410 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addDcMotorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 418 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/* 419 */     String zero = ToolboxUtil.makeNumberShadow(0);
/* 420 */     String one = ToolboxUtil.makeNumberShadow(1);
/* 421 */     String ten = ToolboxUtil.makeNumberShadow(10);
/* 422 */     String runMode = ToolboxUtil.makeEnumShadow(hardwareType, "runMode");
/* 423 */     String zeroPowerBehavior = ToolboxUtil.makeEnumShadow(hardwareType, "zeroPowerBehavior");
/*     */     
/* 425 */     if (hardwareItems.size() > 1) {
/* 426 */       HardwareItem hardwareItem1 = (HardwareItem)hardwareItems.get(1);
/*     */       
/* 428 */       xmlToolbox.append("    <category name=\"Dual\">\n");
/*     */       
/* 430 */       ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "Power", hardwareItem, one, hardwareItem1, one);
/*     */       
/*     */ 
/*     */ 
/* 434 */       ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "Power", hardwareItem, zero, hardwareItem1, zero);
/*     */       
/*     */ 
/*     */ 
/* 438 */       ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "MaxSpeed", hardwareItem, ten, hardwareItem1, ten);
/*     */       
/*     */ 
/*     */ 
/* 442 */       ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "Mode", hardwareItem, runMode, hardwareItem1, runMode);
/*     */       
/*     */ 
/*     */ 
/* 446 */       ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "TargetPosition", hardwareItem, zero, hardwareItem1, zero);
/*     */       
/*     */ 
/*     */ 
/* 450 */       ToolboxUtil.addDualPropertySetters(xmlToolbox, hardwareType, "ZeroPowerBehavior", hardwareItem, zeroPowerBehavior, hardwareItem1, zeroPowerBehavior);
/*     */       
/*     */ 
/* 453 */       xmlToolbox.append("    </category>\n").append("    <category name=\"Individual\">\n");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 459 */     SortedSet<String> properties = new TreeSet();
/* 460 */     properties.add("CurrentPosition");
/* 461 */     properties.add("Direction");
/* 462 */     properties.add("MaxSpeed");
/* 463 */     properties.add("Mode");
/* 464 */     properties.add("Power");
/* 465 */     properties.add("PowerFloat");
/* 466 */     properties.add("TargetPosition");
/* 467 */     properties.add("ZeroPowerBehavior");
/* 468 */     Map<String, String[]> setterValues = new HashMap();
/* 469 */     setterValues.put("Direction", new String[] { ToolboxUtil.makeEnumShadow(hardwareType, "direction") });
/*     */     
/* 471 */     setterValues.put("MaxSpeed", new String[] { ten });
/* 472 */     setterValues.put("Mode", new String[] { runMode });
/* 473 */     setterValues.put("Power", new String[] { one, zero });
/*     */     
/*     */ 
/*     */ 
/* 477 */     setterValues.put("TargetPosition", new String[] { zero });
/* 478 */     setterValues.put("ZeroPowerBehavior", new String[] { zeroPowerBehavior });
/* 479 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);
/*     */     
/*     */ 
/* 482 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 483 */     functions.put("isBusy", new HashMap());
/* 484 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */     
/* 486 */     if (hardwareItems.size() > 1) {
/* 487 */       xmlToolbox.append("    </category>\n");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addGyroSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 496 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 499 */     SortedSet<String> properties = new TreeSet();
/* 500 */     properties.add("Heading");
/* 501 */     properties.add("HeadingMode");
/* 502 */     properties.add("I2cAddress7Bit");
/* 503 */     properties.add("I2cAddress8Bit");
/* 504 */     properties.add("RawX");
/* 505 */     properties.add("RawY");
/* 506 */     properties.add("RawZ");
/* 507 */     properties.add("RotationFraction");
/* 508 */     Map<String, String[]> setterValues = new HashMap();
/* 509 */     setterValues.put("HeadingMode", new String[] { ToolboxUtil.makeEnumShadow(hardwareType, "headingMode") });
/*     */     
/* 511 */     setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
/* 512 */     setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
/* 513 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);
/*     */     
/*     */ 
/* 516 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 517 */     functions.put("calibrate", new HashMap());
/* 518 */     functions.put("isCalibrating", new HashMap());
/* 519 */     functions.put("resetZAxisIntegrator", new HashMap());
/* 520 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addIrSeekerSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 528 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 531 */     SortedSet<String> properties = new TreeSet();
/* 532 */     properties.add("SignalDetectedThreshold");
/* 533 */     properties.add("Mode");
/* 534 */     properties.add("IsSignalDetected");
/* 535 */     properties.add("Angle");
/* 536 */     properties.add("Strength");
/* 537 */     properties.add("I2cAddress7Bit");
/* 538 */     properties.add("I2cAddress8Bit");
/* 539 */     Map<String, String[]> setterValues = new HashMap();
/* 540 */     setterValues.put("SignalDetectedThreshold", new String[] { ToolboxUtil.makeNumberShadow(0.003D) });
/*     */     
/* 542 */     setterValues.put("Mode", new String[] { ToolboxUtil.makeEnumShadow(hardwareType, "mode") });
/*     */     
/* 544 */     setterValues.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
/* 545 */     setterValues.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
/* 546 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addLedCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 554 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 557 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 558 */     Map<String, String> enableLedArgs = new HashMap();
/* 559 */     enableLedArgs.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
/* 560 */     functions.put("enableLed", enableLedArgs);
/* 561 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addLightSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 569 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 572 */     SortedSet<String> properties = new TreeSet();
/* 573 */     properties.add("LightDetected");
/* 574 */     properties.add("RawLightDetected");
/* 575 */     properties.add("RawLightDetectedMax");
/* 576 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, null);
/*     */     
/*     */ 
/*     */ 
/* 580 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 581 */     Map<String, String> enableLedArgs = new HashMap();
/* 582 */     enableLedArgs.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
/* 583 */     functions.put("enableLed", enableLedArgs);
/* 584 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addOpticalDistanceSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 592 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 595 */     SortedSet<String> properties = new TreeSet();
/* 596 */     properties.add("LightDetected");
/* 597 */     properties.add("RawLightDetected");
/* 598 */     properties.add("RawLightDetectedMax");
/* 599 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, null);
/*     */     
/*     */ 
/*     */ 
/* 603 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 604 */     Map<String, String> enableLedArgs = new HashMap();
/* 605 */     enableLedArgs.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
/* 606 */     functions.put("enableLed", enableLedArgs);
/* 607 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addServoCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 615 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 618 */     SortedSet<String> properties = new TreeSet();
/* 619 */     properties.add("Direction");
/* 620 */     properties.add("Position");
/* 621 */     Map<String, String[]> setterValues = new HashMap();
/* 622 */     setterValues.put("Direction", new String[] { ToolboxUtil.makeEnumShadow(hardwareType, "direction") });
/*     */     
/* 624 */     setterValues.put("Position", new String[] { ToolboxUtil.makeNumberShadow(0) });
/* 625 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, setterValues);
/*     */     
/*     */ 
/* 628 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 629 */     functions.put("scaleRange", new HashMap());
/* 630 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addServoControllerCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 638 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 641 */     SortedSet<String> properties = new TreeSet();
/* 642 */     properties.add("PwmStatus");
/* 643 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, null);
/*     */     
/*     */ 
/*     */ 
/* 647 */     Map<String, Map<String, String>> functions = new TreeMap();
/* 648 */     functions.put("pwmEnable", new HashMap());
/* 649 */     functions.put("pwmDisable", new HashMap());
/* 650 */     ToolboxUtil.addFunctions(xmlToolbox, hardwareType, hardwareItem, functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addTouchSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 658 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 661 */     SortedSet<String> properties = new TreeSet();
/* 662 */     properties.add("IsPressed");
/* 663 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addUltrasonicSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 672 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 675 */     SortedSet<String> properties = new TreeSet();
/* 676 */     properties.add("UltrasonicLevel");
/* 677 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addVoltageSensorCategoryToToolbox(StringBuilder xmlToolbox, HardwareType hardwareType, List<HardwareItem> hardwareItems)
/*     */   {
/* 686 */     HardwareItem hardwareItem = (HardwareItem)hardwareItems.get(0);
/*     */     
/*     */ 
/* 689 */     SortedSet<String> properties = new TreeSet();
/* 690 */     properties.add("Voltage");
/* 691 */     ToolboxUtil.addProperties(xmlToolbox, hardwareType, hardwareItem, properties, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addElapsedTimeCategoryToToolbox(StringBuilder xmlToolbox, AssetManager assetManager)
/*     */     throws IOException
/*     */   {
/* 700 */     xmlToolbox.append("  <category name=\"ElapsedTime\">\n").append("    <block type=\"variables_set\">\n").append("      <field name=\"VAR\">").append("timer").append("</field>\n").append("      <value name=\"VALUE\">\n").append("        <block type=\"elapsedTime_create\"></block>\n").append("      </value>\n").append("    </block>\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 708 */     if (assetManager != null) {
/* 709 */       ToolboxUtil.addAssetToToolbox(xmlToolbox, assetManager, "toolbox/elapsed_time.xml");
/*     */     }
/* 711 */     xmlToolbox.append("  </category>\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static String upgradeBlocks(String blkContent, Activity activity)
/*     */   {
/* 718 */     return upgradeBlocks(blkContent, HardwareItemMap.newHardwareItemMap(activity));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String upgradeBlocks(String blkContent, HardwareItemMap hardwareItemMap)
/*     */   {
/* 730 */     blkContent = replaceIdentifierIfNotFound(blkContent, hardwareItemMap.getHardwareItems(HardwareType.DC_MOTOR), 0, "motorL", "MOTOR");
/*     */     
/*     */ 
/*     */ 
/* 734 */     blkContent = replaceIdentifierIfNotFound(blkContent, hardwareItemMap.getHardwareItems(HardwareType.DC_MOTOR), 1, "motorR", "MOTOR");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 739 */     blkContent = replaceIdentifierIfNotFound(blkContent, hardwareItemMap.getHardwareItems(HardwareType.OPTICAL_DISTANCE_SENSOR), 0, "sensorOpticalDistance", "OPTICAL_DISTANCE_SENSOR");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 745 */     blkContent = replaceIdentifierIfNotFound(blkContent, hardwareItemMap.getHardwareItems(HardwareType.TOUCH_SENSOR), 0, "sensorTouch", "TOUCH_SENSOR");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 751 */     blkContent = blkContent.replace("<block type=\"robotController_telemetryAddNumericData\"><field name=\"ROBOT_CONTROLLER\">robotController</field>", "<block type=\"telemetry_addNumericData\"><field name=\"IDENTIFIER\">telemetry</field>");
/*     */     
/*     */ 
/* 754 */     blkContent = blkContent.replace("<block type=\"robotController_telemetryAddTextData\"><field name=\"ROBOT_CONTROLLER\">robotController</field>", "<block type=\"telemetry_addTextData\"><field name=\"IDENTIFIER\">telemetry</field>");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 763 */     blkContent = blkContent.replaceAll("\\<block type\\=\\\"dcMotor_constant_runMode\\\"\\>\\<field name\\=\\\"MOTOR\\\"\\>[^<]*\\</field\\>", "<block type=\"dcMotor_enum_runMode\">");
/*     */     
/*     */ 
/*     */ 
/* 767 */     blkContent = blkContent.replaceAll("\\<block type\\=\\\"dcMotor_constant_runMode_V2\\\"\\>\\<field name\\=\\\"MOTOR\\\"\\>[^<]*\\</field\\>", "<block type=\"dcMotor_enum_runMode\">");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 772 */     blkContent = blkContent.replace("<field name=\"RUN_MODE\">RUN_USING_ENCODERS</field>", "<field name=\"RUN_MODE\">RUN_USING_ENCODER</field>");
/*     */     
/*     */ 
/* 775 */     blkContent = blkContent.replace("<field name=\"RUN_MODE\">RUN_WITHOUT_ENCODERS</field>", "<field name=\"RUN_MODE\">RUN_WITHOUT_ENCODER</field>");
/*     */     
/*     */ 
/* 778 */     blkContent = blkContent.replace("<field name=\"RUN_MODE\">RESET_ENCODERS</field\\>", "<field name=\"RUN_MODE\">STOP_AND_RESET_ENCODER</field>");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 787 */     blkContent = blkContent.replaceAll("\\<block type\\=\\\"dcMotor_constant_direction\\\"\\>\\<field name\\=\\\"MOTOR\\\"\\>[^<]*\\</field\\>", "<block type=\"dcMotor_enum_direction\">");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 794 */     blkContent = blkContent.replace("<field name=\"GAMEPAD\">", "<field name=\"IDENTIFIER\">");
/*     */     
/*     */ 
/* 797 */     blkContent = blkContent.replace("<field name=\"LINEAR_OP_MODE\">", "<field name=\"IDENTIFIER\">");
/*     */     
/*     */ 
/* 800 */     blkContent = blkContent.replace("<field name=\"MOTOR\">", "<field name=\"IDENTIFIER\">");
/*     */     
/*     */ 
/* 803 */     blkContent = blkContent.replace("<field name=\"OPTICAL_DISTANCE_SENSOR\">", "<field name=\"IDENTIFIER\">");
/*     */     
/*     */ 
/* 806 */     blkContent = blkContent.replace("<field name=\"TOUCH_SENSOR\">", "<field name=\"IDENTIFIER\">");
/*     */     
/*     */ 
/*     */ 
/* 810 */     return blkContent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String replaceIdentifierIfNotFound(String blkContent, List<HardwareItem> hardwareItemList, int index, String hardcodedIdentifier, String identifierFieldName)
/*     */   {
/* 822 */     if ((hardwareItemList != null) && (hardwareItemList.size() > index)) {
/* 823 */       boolean found = false;
/* 824 */       for (HardwareItem hardwareItem : hardwareItemList) {
/* 825 */         if (hardwareItem.identifier.equals(hardcodedIdentifier)) {
/* 826 */           found = true;
/* 827 */           break;
/*     */         }
/*     */       }
/* 830 */       if (!found) {
/* 831 */         String oldTag = "<field name=\"" + identifierFieldName + "\">" + hardcodedIdentifier + "</field>";
/*     */         
/* 833 */         String newIdentifier = ((HardwareItem)hardwareItemList.get(index)).identifier;
/* 834 */         String newTag = "<field name=\"" + identifierFieldName + "\">" + newIdentifier + "</field>";
/* 835 */         blkContent = blkContent.replace(oldTag, newTag);
/*     */       }
/*     */     }
/* 838 */     return blkContent;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\util\HardwareUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */