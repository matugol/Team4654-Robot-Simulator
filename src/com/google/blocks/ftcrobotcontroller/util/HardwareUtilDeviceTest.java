/*     */ package com.google.blocks.ftcrobotcontroller.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HardwareUtilDeviceTest
/*     */ {
/*     */   public static void testAll()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   private static void test_getHardwareItemMap_fromXml()
/*     */     throws Exception
/*     */   {
/*  21 */     String xml = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><Robot>    <LegacyModuleController name=\"Legacy Module 1\" serialNumber=\"A1010I84\">        <Accelerometer name=\"accelerometer\" port=\"0\" />        <ColorSensor name=\"color_sensor\" port=\"1\" />        <Compass name=\"compass\" port=\"2\" />        <IrSeeker name=\"ir_seeker\" port=\"3\" />        <LightSensor name=\"light_sensor\" port=\"4\" />        <UltrasonicSensor name=\"ultrasonic\" port=\"5\" />    </LegacyModuleController>    <DeviceInterfaceModule name=\"Device Interface Module 1\" serialNumber=\"AL00VCXT\">        <Gyro name=\"gyro\" port=\"0\" />        <I2cDevice name=\"i2c_device\" port=\"1\" />        <AdafruitColorSensor name=\"ada_fruit\" port=\"2\" />        <OpticalDistanceSensor name=\"sensor_EOPD\" port=\"0\" />        <AnalogInput name=\"analog_input\" port=\"1\" />        <TouchSensor name=\"touch_sensor\" port=\"0\" />        <DigitalDevice name=\"digital_device\" port=\"1\" />        <Led name=\"led\" port=\"2\" />        <AnalogOutput name=\"analog_output\" port=\"0\" />    </DeviceInterfaceModule>    <MotorController name=\"Motor Controller 1\" serialNumber=\"AL00VVNK\">        <Motor name=\"left_drive\" port=\"1\" />        <Motor name=\"right_drive\" port=\"2\" />    </MotorController>    <ServoController name=\"Servo Controller 1\" serialNumber=\"AL00VEM5\">        <Servo name=\"arm\" port=\"1\" />        <ContinuousRotationServo name=\"cr servo\" port=\"2\" />    </ServoController></Robot>";
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
/*  52 */     BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
/*     */     
/*  54 */     HardwareItemMap hardwareItemMap = new HardwareItemMap(reader);
/*     */     
/*  56 */     int expectedCount = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  61 */     expectedCount++;
/*  62 */     List<HardwareItem> list = hardwareItemMap.getHardwareItems(HardwareType.ACCELERATION_SENSOR);
/*  63 */     if (list.size() != 1) {
/*  64 */       throw new Exception("Failure - AccelerationSensor list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/*  67 */     HardwareItem hardwareItem = (HardwareItem)list.get(0);
/*  68 */     if (hardwareItem.hardwareType != HardwareType.ACCELERATION_SENSOR) {
/*  69 */       throw new Exception("Failure - hardwareItem.hardwareType - expected ACCELERATION_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/*  73 */     if (!hardwareItem.deviceName.equals("accelerometer")) {
/*  74 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"accelerometer\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/*  78 */     if (!hardwareItem.identifier.equals("accelerometer")) {
/*  79 */       throw new Exception("Failure - hardwareItem.identifier - expected \"accelerometer\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/*  83 */     if (!hardwareItem.visibleName.equals("accelerometer")) {
/*  84 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"accelerometer\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  89 */     expectedCount++;
/*  90 */     list = hardwareItemMap.getHardwareItems(HardwareType.ANALOG_INPUT);
/*  91 */     if (list.size() != 1) {
/*  92 */       throw new Exception("Failure - AnalogInput list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/*  95 */     hardwareItem = (HardwareItem)list.get(0);
/*  96 */     if (hardwareItem.hardwareType != HardwareType.ANALOG_INPUT) {
/*  97 */       throw new Exception("Failure - hardwareItem.hardwareType - expected ANALOG_INPUT, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 101 */     if (!hardwareItem.deviceName.equals("analog_input")) {
/* 102 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"analog_input\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 106 */     if (!hardwareItem.identifier.equals("analog_inputAsAnalogInput")) {
/* 107 */       throw new Exception("Failure - hardwareItem.identifier - expected \"analog_inputAsAnalogInput\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 111 */     if (!hardwareItem.visibleName.equals("analog_input")) {
/* 112 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"analog_input\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 117 */     expectedCount++;
/* 118 */     list = hardwareItemMap.getHardwareItems(HardwareType.ANALOG_OUTPUT);
/* 119 */     if (list.size() != 1) {
/* 120 */       throw new Exception("Failure - AnalogOutput list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 123 */     hardwareItem = (HardwareItem)list.get(0);
/* 124 */     if (hardwareItem.hardwareType != HardwareType.ANALOG_OUTPUT) {
/* 125 */       throw new Exception("Failure - hardwareItem.hardwareType - expected ANALOG_OUTPUT, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 129 */     if (!hardwareItem.deviceName.equals("analog_output")) {
/* 130 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"analog_output\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 134 */     if (!hardwareItem.identifier.equals("analog_outputAsAnalogOutput")) {
/* 135 */       throw new Exception("Failure - hardwareItem.identifier - expected \"analog_outputAsAnalogOutput\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 139 */     if (!hardwareItem.visibleName.equals("analog_output")) {
/* 140 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"analog_output\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 145 */     expectedCount++;
/* 146 */     list = hardwareItemMap.getHardwareItems(HardwareType.COLOR_SENSOR);
/* 147 */     if (list.size() != 2) {
/* 148 */       throw new Exception("Failure - ColorSensor list.size() - expected 2, actual " + list.size());
/*     */     }
/*     */     
/* 151 */     hardwareItem = (HardwareItem)list.get(0);
/* 152 */     if (hardwareItem.hardwareType != HardwareType.COLOR_SENSOR) {
/* 153 */       throw new Exception("Failure - hardwareItem.hardwareType - expected COLOR_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 157 */     if (!hardwareItem.deviceName.equals("color_sensor")) {
/* 158 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"color_sensor\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 162 */     if (!hardwareItem.identifier.equals("color_sensor")) {
/* 163 */       throw new Exception("Failure - hardwareItem.identifier - expected \"color_sensor\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 167 */     if (!hardwareItem.visibleName.equals("color_sensor")) {
/* 168 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"color_sensor\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 172 */     hardwareItem = (HardwareItem)list.get(1);
/* 173 */     if (hardwareItem.hardwareType != HardwareType.COLOR_SENSOR) {
/* 174 */       throw new Exception("Failure - hardwareItem.hardwareType - expected COLOR_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 178 */     if (!hardwareItem.deviceName.equals("ada_fruit")) {
/* 179 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"ada_fruit\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 183 */     if (!hardwareItem.identifier.equals("ada_fruit")) {
/* 184 */       throw new Exception("Failure - hardwareItem.identifier - expected \"ada_fruit\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 188 */     if (!hardwareItem.visibleName.equals("ada_fruit")) {
/* 189 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"ada_fruit\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 194 */     expectedCount++;
/* 195 */     list = hardwareItemMap.getHardwareItems(HardwareType.COMPASS_SENSOR);
/* 196 */     if (list.size() != 1) {
/* 197 */       throw new Exception("Failure - CompassSensor list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 200 */     hardwareItem = (HardwareItem)list.get(0);
/* 201 */     if (hardwareItem.hardwareType != HardwareType.COMPASS_SENSOR) {
/* 202 */       throw new Exception("Failure - hardwareItem.hardwareType - expected COMPASS_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 206 */     if (!hardwareItem.deviceName.equals("compass")) {
/* 207 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"compass\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 211 */     if (!hardwareItem.identifier.equals("compass")) {
/* 212 */       throw new Exception("Failure - hardwareItem.identifier - expected \"compass\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 216 */     if (!hardwareItem.visibleName.equals("compass")) {
/* 217 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"compass\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 222 */     expectedCount++;
/* 223 */     list = hardwareItemMap.getHardwareItems(HardwareType.CR_SERVO);
/* 224 */     if (list.size() != 1) {
/* 225 */       throw new Exception("Failure - Servo list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 228 */     hardwareItem = (HardwareItem)list.get(0);
/* 229 */     if (hardwareItem.hardwareType != HardwareType.CR_SERVO) {
/* 230 */       throw new Exception("Failure - hardwareItem.hardwareType - expected CR_SERVO, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 234 */     if (!hardwareItem.deviceName.equals("cr servo")) {
/* 235 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"cr servo\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 239 */     if (!hardwareItem.identifier.equals("crservo")) {
/* 240 */       throw new Exception("Failure - hardwareItem.identifier - expected \"crservo\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 244 */     if (!hardwareItem.visibleName.equals("cr servo")) {
/* 245 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"cr servo\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 250 */     expectedCount++;
/* 251 */     list = hardwareItemMap.getHardwareItems(HardwareType.DC_MOTOR);
/* 252 */     if (list.size() != 2) {
/* 253 */       throw new Exception("Failure - DcMotor list.size() - expected 2, actual " + list.size());
/*     */     }
/*     */     
/* 256 */     hardwareItem = (HardwareItem)list.get(0);
/* 257 */     if (hardwareItem.hardwareType != HardwareType.DC_MOTOR) {
/* 258 */       throw new Exception("Failure - hardwareItem.hardwareType - expected DC_MOTOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 262 */     if (!hardwareItem.deviceName.equals("left_drive")) {
/* 263 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"left_drive\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 267 */     if (!hardwareItem.identifier.equals("left_drive")) {
/* 268 */       throw new Exception("Failure - hardwareItem.identifier - expected \"left_drive\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 272 */     if (!hardwareItem.visibleName.equals("left_drive")) {
/* 273 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"left_drive\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 277 */     hardwareItem = (HardwareItem)list.get(1);
/* 278 */     if (hardwareItem.hardwareType != HardwareType.DC_MOTOR) {
/* 279 */       throw new Exception("Failure - hardwareItem.hardwareType - expected DC_MOTOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 283 */     if (!hardwareItem.deviceName.equals("right_drive")) {
/* 284 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"right_drive\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 288 */     if (!hardwareItem.identifier.equals("right_drive")) {
/* 289 */       throw new Exception("Failure - hardwareItem.identifier - expected \"right_drive\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 293 */     if (!hardwareItem.visibleName.equals("right_drive")) {
/* 294 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"right_drive\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
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
/* 386 */     expectedCount++;
/* 387 */     list = hardwareItemMap.getHardwareItems(HardwareType.GYRO_SENSOR);
/* 388 */     if (list.size() != 1) {
/* 389 */       throw new Exception("Failure - GyroSensor list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 392 */     hardwareItem = (HardwareItem)list.get(0);
/* 393 */     if (hardwareItem.hardwareType != HardwareType.GYRO_SENSOR) {
/* 394 */       throw new Exception("Failure - hardwareItem.hardwareType - expected GYRO_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 398 */     if (!hardwareItem.deviceName.equals("gyro")) {
/* 399 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"gyro\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 403 */     if (!hardwareItem.identifier.equals("gyro")) {
/* 404 */       throw new Exception("Failure - hardwareItem.identifier - expected \"gyro\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 408 */     if (!hardwareItem.visibleName.equals("gyro")) {
/* 409 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"gyro\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
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
/* 443 */     expectedCount++;
/* 444 */     list = hardwareItemMap.getHardwareItems(HardwareType.IR_SEEKER_SENSOR);
/* 445 */     if (list.size() != 1) {
/* 446 */       throw new Exception("Failure - IrSeekerSensor list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 449 */     hardwareItem = (HardwareItem)list.get(0);
/* 450 */     if (hardwareItem.hardwareType != HardwareType.IR_SEEKER_SENSOR) {
/* 451 */       throw new Exception("Failure - hardwareItem.hardwareType - expected IR_SEEKER_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 455 */     if (!hardwareItem.deviceName.equals("ir_seeker")) {
/* 456 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"ir_seeker\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 460 */     if (!hardwareItem.identifier.equals("ir_seeker")) {
/* 461 */       throw new Exception("Failure - hardwareItem.identifier - expected \"ir_seeker\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 465 */     if (!hardwareItem.visibleName.equals("ir_seeker")) {
/* 466 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"ir_seeker\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 471 */     expectedCount++;
/* 472 */     list = hardwareItemMap.getHardwareItems(HardwareType.LED);
/* 473 */     if (list.size() != 1) {
/* 474 */       throw new Exception("Failure - Led list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 477 */     hardwareItem = (HardwareItem)list.get(0);
/* 478 */     if (hardwareItem.hardwareType != HardwareType.LED) {
/* 479 */       throw new Exception("Failure - hardwareItem.hardwareType - expected LED, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 483 */     if (!hardwareItem.deviceName.equals("led")) {
/* 484 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"led\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 488 */     if (!hardwareItem.identifier.equals("led")) {
/* 489 */       throw new Exception("Failure - hardwareItem.identifier - expected \"led\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 493 */     if (!hardwareItem.visibleName.equals("led")) {
/* 494 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"led\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
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
/* 528 */     expectedCount++;
/* 529 */     list = hardwareItemMap.getHardwareItems(HardwareType.LIGHT_SENSOR);
/* 530 */     if (list.size() != 1) {
/* 531 */       throw new Exception("Failure - LightSensor list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 534 */     hardwareItem = (HardwareItem)list.get(0);
/* 535 */     if (hardwareItem.hardwareType != HardwareType.LIGHT_SENSOR) {
/* 536 */       throw new Exception("Failure - hardwareItem.hardwareType - expected LIGHT_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 540 */     if (!hardwareItem.deviceName.equals("light_sensor")) {
/* 541 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"light_sensor\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 545 */     if (!hardwareItem.identifier.equals("light_sensor")) {
/* 546 */       throw new Exception("Failure - hardwareItem.identifier - expected \"light_sensor\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 550 */     if (!hardwareItem.visibleName.equals("light_sensor")) {
/* 551 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"light_sensor\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 556 */     expectedCount++;
/* 557 */     list = hardwareItemMap.getHardwareItems(HardwareType.OPTICAL_DISTANCE_SENSOR);
/* 558 */     if (list.size() != 1) {
/* 559 */       throw new Exception("Failure - OpticalDistanceSensor list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 562 */     hardwareItem = (HardwareItem)list.get(0);
/* 563 */     if (hardwareItem.hardwareType != HardwareType.OPTICAL_DISTANCE_SENSOR) {
/* 564 */       throw new Exception("Failure - hardwareItem.hardwareType - expected OPTICAL_DISTANCE_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 568 */     if (!hardwareItem.deviceName.equals("sensor_EOPD")) {
/* 569 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"sensor_EOPD\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 573 */     if (!hardwareItem.identifier.equals("sensor_EOPD")) {
/* 574 */       throw new Exception("Failure - hardwareItem.identifier - expected \"sensor_EOPD\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 578 */     if (!hardwareItem.visibleName.equals("sensor_EOPD")) {
/* 579 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"sensor_EOPD\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 584 */     expectedCount++;
/* 585 */     list = hardwareItemMap.getHardwareItems(HardwareType.SERVO);
/* 586 */     if (list.size() != 1) {
/* 587 */       throw new Exception("Failure - Servo list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 590 */     hardwareItem = (HardwareItem)list.get(0);
/* 591 */     if (hardwareItem.hardwareType != HardwareType.SERVO) {
/* 592 */       throw new Exception("Failure - hardwareItem.hardwareType - expected SERVO, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 596 */     if (!hardwareItem.deviceName.equals("arm")) {
/* 597 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"arm\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 601 */     if (!hardwareItem.identifier.equals("arm")) {
/* 602 */       throw new Exception("Failure - hardwareItem.identifier - expected \"arm\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 606 */     if (!hardwareItem.visibleName.equals("arm")) {
/* 607 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"arm\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 612 */     expectedCount++;
/* 613 */     list = hardwareItemMap.getHardwareItems(HardwareType.SERVO_CONTROLLER);
/* 614 */     if (list.size() != 1) {
/* 615 */       throw new Exception("Failure - ServoController list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 618 */     hardwareItem = (HardwareItem)list.get(0);
/* 619 */     if (hardwareItem.hardwareType != HardwareType.SERVO_CONTROLLER) {
/* 620 */       throw new Exception("Failure - hardwareItem.hardwareType - expected SERVO_CONTROLLER, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 624 */     if (!hardwareItem.deviceName.equals("Servo Controller 1")) {
/* 625 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"Servo Controller 1\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 629 */     if (!hardwareItem.identifier.equals("ServoController1AsServoController")) {
/* 630 */       throw new Exception("Failure - hardwareItem.identifier - expected \"ServoController1AsServoController\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 634 */     if (!hardwareItem.visibleName.equals("Servo Controller 1")) {
/* 635 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"Servo Controller 1\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 640 */     expectedCount++;
/* 641 */     list = hardwareItemMap.getHardwareItems(HardwareType.TOUCH_SENSOR);
/* 642 */     if (list.size() != 1) {
/* 643 */       throw new Exception("Failure - TouchSensor list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 646 */     hardwareItem = (HardwareItem)list.get(0);
/* 647 */     if (hardwareItem.hardwareType != HardwareType.TOUCH_SENSOR) {
/* 648 */       throw new Exception("Failure - hardwareItem.hardwareType - expected TOUCH_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 652 */     if (!hardwareItem.deviceName.equals("touch_sensor")) {
/* 653 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"touch_sensor\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 657 */     if (!hardwareItem.identifier.equals("touch_sensor")) {
/* 658 */       throw new Exception("Failure - hardwareItem.identifier - expected \"touch_sensor\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 662 */     if (!hardwareItem.visibleName.equals("touch_sensor")) {
/* 663 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"touch_sensor\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 668 */     expectedCount++;
/* 669 */     list = hardwareItemMap.getHardwareItems(HardwareType.ULTRASONIC_SENSOR);
/* 670 */     if (list.size() != 1) {
/* 671 */       throw new Exception("Failure - UltrasonicSensor list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 674 */     hardwareItem = (HardwareItem)list.get(0);
/* 675 */     if (hardwareItem.hardwareType != HardwareType.ULTRASONIC_SENSOR) {
/* 676 */       throw new Exception("Failure - hardwareItem.hardwareType - expected ULTRASONIC_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 680 */     if (!hardwareItem.deviceName.equals("ultrasonic")) {
/* 681 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"ultrasonic\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 685 */     if (!hardwareItem.identifier.equals("ultrasonic")) {
/* 686 */       throw new Exception("Failure - hardwareItem.identifier - expected \"ultrasonic\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 690 */     if (!hardwareItem.visibleName.equals("ultrasonic")) {
/* 691 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"ultrasonic\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 696 */     expectedCount++;
/* 697 */     list = hardwareItemMap.getHardwareItems(HardwareType.VOLTAGE_SENSOR);
/* 698 */     if (list.size() != 1) {
/* 699 */       throw new Exception("Failure - VoltageSensor list.size() - expected 1, actual " + list.size());
/*     */     }
/*     */     
/* 702 */     hardwareItem = (HardwareItem)list.get(0);
/* 703 */     if (hardwareItem.hardwareType != HardwareType.VOLTAGE_SENSOR) {
/* 704 */       throw new Exception("Failure - hardwareItem.hardwareType - expected VOLTAGE_SENSOR, actual " + hardwareItem.hardwareType);
/*     */     }
/*     */     
/*     */ 
/* 708 */     if (!hardwareItem.deviceName.equals("Motor Controller 1")) {
/* 709 */       throw new Exception("Failure - hardwareItem.deviceName - expected \"Motor Controller 1\", actual \"" + hardwareItem.deviceName + "\"");
/*     */     }
/*     */     
/*     */ 
/* 713 */     if (!hardwareItem.identifier.equals("MotorController1AsVoltageSensor")) {
/* 714 */       throw new Exception("Failure - hardwareItem.identifier - expected \"MotorController1AsVoltageSensor\", actual \"" + hardwareItem.identifier + "\"");
/*     */     }
/*     */     
/*     */ 
/* 718 */     if (!hardwareItem.visibleName.equals("Motor Controller 1")) {
/* 719 */       throw new Exception("Failure - hardwareItem.visibleName - expected \"Motor Controller 1\", actual \"" + hardwareItem.visibleName + "\"");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 724 */     if (hardwareItemMap.getHardwareTypeCount() != expectedCount) {
/* 725 */       throw new Exception("Failure - hardwareItemMap.getHardwareTypeCount() - expected " + expectedCount + ", actual " + hardwareItemMap.getHardwareTypeCount());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\util\HardwareUtilDeviceTest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */