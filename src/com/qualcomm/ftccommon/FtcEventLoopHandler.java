/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.ArmableUsbDevice;
/*     */ import com.qualcomm.hardware.HardwareFactory;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorController;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.hardware.ServoController;
/*     */ import com.qualcomm.robotcore.hardware.VoltageSensor;
/*     */ import com.qualcomm.robotcore.robocol.TelemetryMessage;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ import com.qualcomm.robotcore.util.BatteryChecker;
/*     */ import com.qualcomm.robotcore.util.BatteryChecker.BatteryWatcher;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import com.qualcomm.robotcore.util.MovingStatistics;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcEventLoopHandler
/*     */   implements BatteryChecker.BatteryWatcher
/*     */ {
/*     */   public static final String NO_VOLTAGE_SENSOR = "$no$voltage$sensor$";
/*     */   private Context robotControllerContext;
/*     */   private EventLoopManager eventLoopManager;
/*     */   private BatteryChecker robotControllerBatteryChecker;
/*  71 */   private double robotControllerBatteryCheckerInterval = 180.0D;
/*     */   
/*  73 */   private ElapsedTime robotBatteryTimer = new ElapsedTime();
/*  74 */   private double robotBatteryInterval = 3.0D;
/*  75 */   private MovingStatistics robotBatteryStatistics = new MovingStatistics(10);
/*  76 */   private ElapsedTime robotBatteryLoggingTimer = new ElapsedTime(0L);
/*  77 */   private double robotBatteryLoggingInterval = this.robotControllerBatteryCheckerInterval;
/*     */   
/*  79 */   private ElapsedTime userTelemetryTimer = new ElapsedTime(0L);
/*  80 */   private double userTelemetryInterval = 0.25D;
/*  81 */   private final Object refreshUserTelemetryLock = new Object();
/*     */   
/*  83 */   private ElapsedTime updateUITimer = new ElapsedTime();
/*  84 */   private double updateUIInterval = 0.25D;
/*     */   
/*     */   private UpdateUI.Callback callback;
/*  87 */   private HardwareFactory hardwareFactory = null;
/*  88 */   private HardwareMap hardwareMap = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FtcEventLoopHandler(HardwareFactory hardwareFactory, UpdateUI.Callback callback, Context robotControllerContext)
/*     */   {
/*  95 */     this.hardwareFactory = hardwareFactory;
/*  96 */     this.callback = callback;
/*  97 */     this.robotControllerContext = robotControllerContext;
/*     */     
/*  99 */     long milliseconds = (this.robotControllerBatteryCheckerInterval * 1000.0D);
/* 100 */     this.robotControllerBatteryChecker = new BatteryChecker(robotControllerContext, this, milliseconds);
/* 101 */     this.robotControllerBatteryChecker.startBatteryMonitoring();
/*     */   }
/*     */   
/*     */   public void init(EventLoopManager eventLoopManager) {
/* 105 */     this.eventLoopManager = eventLoopManager;
/*     */   }
/*     */   
/*     */   public EventLoopManager getEventLoopManager() {
/* 109 */     return this.eventLoopManager;
/*     */   }
/*     */   
/*     */   public HardwareMap getHardwareMap() throws RobotCoreException, InterruptedException {
/* 113 */     if (this.hardwareMap == null) {
/* 114 */       this.hardwareMap = this.hardwareFactory.createHardwareMap(this.eventLoopManager);
/*     */     }
/* 116 */     return this.hardwareMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void displayGamePadInfo(String activeOpModeName)
/*     */   {
/* 124 */     if (this.updateUITimer.time() > this.updateUIInterval) {
/* 125 */       this.updateUITimer.reset();
/*     */       
/*     */ 
/* 128 */       Gamepad[] gamepads = this.eventLoopManager.getGamepads();
/* 129 */       this.callback.updateUi(activeOpModeName, gamepads);
/*     */     }
/*     */   }
/*     */   
/*     */   public Gamepad[] getGamepads() {
/* 134 */     return this.eventLoopManager.getGamepads();
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
/*     */ 
/*     */ 
/*     */   public void refreshUserTelemetry(TelemetryMessage telemetry, double requestedInterval)
/*     */   {
/* 150 */     synchronized (this.refreshUserTelemetryLock)
/*     */     {
/*     */ 
/*     */ 
/* 154 */       if (Double.isNaN(requestedInterval)) {
/* 155 */         requestedInterval = this.userTelemetryInterval;
/*     */       }
/*     */       
/*     */ 
/* 159 */       boolean transmitBecauseOfUser = this.userTelemetryTimer.seconds() >= requestedInterval;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 176 */       double msThreshold = 2.0D;
/* 177 */       boolean transmitBecauseOfBattery = (this.robotBatteryTimer.seconds() >= this.robotBatteryInterval) || ((transmitBecauseOfUser) && (this.robotBatteryStatistics.getMean() < msThreshold));
/*     */       
/*     */ 
/* 180 */       if ((transmitBecauseOfUser) || (transmitBecauseOfBattery))
/*     */       {
/* 182 */         if (transmitBecauseOfUser) {
/* 183 */           this.userTelemetryTimer.reset();
/*     */         }
/*     */         
/* 186 */         if (transmitBecauseOfBattery) {
/* 187 */           telemetry.addData("$Robot$Battery$Level$", buildRobotBatteryMsg());
/* 188 */           this.robotBatteryTimer.reset();
/* 189 */           if (this.robotBatteryLoggingTimer.seconds() > this.robotBatteryLoggingInterval) {
/* 190 */             RobotLog.i("robot battery read duration: n=%d, mean=%.3fms sd=%.3fms", new Object[] { Integer.valueOf(this.robotBatteryStatistics.getCount()), Double.valueOf(this.robotBatteryStatistics.getMean()), Double.valueOf(this.robotBatteryStatistics.getStandardDeviation()) });
/* 191 */             this.robotBatteryLoggingTimer.reset();
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 197 */         if (telemetry.hasData()) {
/* 198 */           this.eventLoopManager.sendTelemetryData(telemetry);
/* 199 */           telemetry.clearData();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void sendBatteryInfo()
/*     */   {
/* 209 */     float percent = this.robotControllerBatteryChecker.getBatteryLevel();
/* 210 */     String batteryMessage = buildRobotBatteryMsg();
/* 211 */     if (batteryMessage != null) {
/* 212 */       sendTelemetry("$RobotController$Battery$Level$", String.valueOf(percent));
/* 213 */       sendTelemetry("$Robot$Battery$Level$", batteryMessage);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String buildRobotBatteryMsg()
/*     */   {
/* 224 */     if (this.hardwareMap == null) { return null;
/*     */     }
/* 226 */     double minBatteryLevel = Double.POSITIVE_INFINITY;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 235 */     for (VoltageSensor sensor : this.hardwareMap.voltageSensor)
/*     */     {
/*     */ 
/* 238 */       long nanoBefore = System.nanoTime();
/* 239 */       double sensorVoltage = sensor.getVoltage();
/* 240 */       long nanoAfter = System.nanoTime();
/*     */       
/* 242 */       if (sensorVoltage >= 1.0D)
/*     */       {
/* 244 */         this.robotBatteryStatistics.add((nanoAfter - nanoBefore) / 1000000.0D);
/*     */         
/*     */ 
/* 247 */         if (sensorVoltage < minBatteryLevel) {
/* 248 */           minBatteryLevel = sensorVoltage;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     String msg;
/*     */     String msg;
/* 255 */     if (minBatteryLevel == Double.POSITIVE_INFINITY) {
/* 256 */       msg = "$no$voltage$sensor$";
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 261 */       msg = Integer.toString((int)(minBatteryLevel * 100.0D));
/* 262 */       msg = new StringBuilder(msg).insert(msg.length() - 2, ".").toString();
/*     */     }
/*     */     
/* 265 */     return msg;
/*     */   }
/*     */   
/*     */   public void sendTelemetry(String tag, String msg) {
/* 269 */     TelemetryMessage telemetry = new TelemetryMessage();
/* 270 */     telemetry.setTag(tag);
/* 271 */     telemetry.addData(tag, msg);
/* 272 */     if (this.eventLoopManager != null) {
/* 273 */       this.eventLoopManager.sendTelemetryData(telemetry);
/* 274 */       telemetry.clearData();
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeMotorControllers() {
/* 279 */     for (DcMotorController controller : this.hardwareMap.getAll(DcMotorController.class)) {
/* 280 */       controller.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeServoControllers() {
/* 285 */     for (ServoController controller : this.hardwareMap.getAll(ServoController.class)) {
/* 286 */       controller.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeAllUsbDevices() {
/* 291 */     for (ArmableUsbDevice device : this.hardwareMap.getAll(ArmableUsbDevice.class)) {
/* 292 */       device.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void restartRobot() {
/* 297 */     DbgLog.error("restarting robot...");
/* 298 */     this.robotControllerBatteryChecker.endBatteryMonitoring();
/* 299 */     this.callback.restartRobot();
/*     */   }
/*     */   
/*     */   public String getOpMode(String extra) {
/* 303 */     if (this.eventLoopManager.state != RobotState.RUNNING) {
/* 304 */       return "$Stop$Robot$";
/*     */     }
/* 306 */     return extra;
/*     */   }
/*     */   
/*     */   public void updateBatteryLevel(float percent) {
/* 310 */     sendTelemetry("$RobotController$Battery$Level$", String.valueOf(percent));
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\FtcEventLoopHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */