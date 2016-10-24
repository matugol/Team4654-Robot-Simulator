/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.hardware.usb.UsbDevice;
/*     */ import com.ftdi.j2xx.D2xxManager;
/*     */ import com.ftdi.j2xx.D2xxManager.D2xxException;
/*     */ import com.ftdi.j2xx.D2xxManager.FtDeviceInfoListNode;
/*     */ import com.ftdi.j2xx.FT_Device;
/*     */ import com.google.gson.Gson;
/*     */ import com.qualcomm.ftccommon.configuration.ScannedDevices;
/*     */ import com.qualcomm.ftccommon.configuration.USBScanManager;
/*     */ import com.qualcomm.hardware.HardwareFactory;
/*     */ import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.UserSensorTypeManager;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.TelemetryMessage;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.ThreadPool.SingletonResult;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcEventLoop
/*     */   extends FtcEventLoopBase
/*     */ {
/*     */   protected Utility utility;
/*     */   protected USBScanManager usbScanManager;
/*     */   protected OpModeManagerImpl opModeManager;
/*     */   protected OpModeRegister register;
/*     */   protected UsbModuleAttachmentHandler usbModuleAttachmentHandler;
/*     */   protected final Object attachedUsbDevicesLock;
/*     */   protected Set<String> attachedUsbDevices;
/*     */   protected AtomicReference<OpMode> opModeStopRequested;
/* 123 */   protected Map<SerialNumber, ControllerConfiguration> deviceControllers = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FtcEventLoop(HardwareFactory hardwareFactory, OpModeRegister register, UpdateUI.Callback callback, Activity activityContext, ProgrammingModeController programmingModeController)
/*     */   {
/* 130 */     super(hardwareFactory, callback, activityContext, programmingModeController);
/* 131 */     this.opModeManager = createOpModeManager();
/* 132 */     this.register = register;
/* 133 */     this.usbModuleAttachmentHandler = new DefaultUsbModuleAttachmentHandler();
/* 134 */     this.attachedUsbDevicesLock = new Object();
/* 135 */     this.attachedUsbDevices = new HashSet();
/* 136 */     this.opModeStopRequested = new AtomicReference();
/* 137 */     this.utility = new Utility(activityContext);
/* 138 */     this.usbScanManager = null;
/*     */   }
/*     */   
/*     */   protected OpModeManagerImpl createOpModeManager() {
/* 142 */     return new OpModeManagerImpl(this.activityContext, new HardwareMap(this.activityContext));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpModeManagerImpl getOpModeManager()
/*     */   {
/* 150 */     return this.opModeManager;
/*     */   }
/*     */   
/*     */   public UsbModuleAttachmentHandler getUsbModuleAttachmentHandler() {
/* 154 */     return this.usbModuleAttachmentHandler;
/*     */   }
/*     */   
/*     */   public void setUsbModuleAttachmentHandler(UsbModuleAttachmentHandler handler) {
/* 158 */     this.usbModuleAttachmentHandler = handler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init(EventLoopManager eventLoopManager)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 180 */     DbgLog.msg("======= INIT START =======");
/*     */     
/* 182 */     this.opModeManager.init(eventLoopManager);
/* 183 */     this.opModeManager.registerOpModes(this.register);
/*     */     
/*     */ 
/* 186 */     this.networkConnectionHandler.sendCommand(new Command("CMD_SUGGEST_OP_MODE_LIST_REFRESH"));
/*     */     
/* 188 */     this.ftcEventLoopHandler.init(eventLoopManager);
/* 189 */     HardwareMap hardwareMap = this.ftcEventLoopHandler.getHardwareMap();
/*     */     
/* 191 */     ModernRoboticsUsbUtil.init(hardwareMap.appContext, hardwareMap);
/*     */     
/* 193 */     this.opModeManager.setHardwareMap(hardwareMap);
/* 194 */     hardwareMap.logDevices();
/*     */     
/* 196 */     DbgLog.msg("======= INIT FINISH =======");
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
/*     */   public void loop()
/*     */     throws RobotCoreException
/*     */   {
/* 210 */     OpMode opModeToStop = (OpMode)this.opModeStopRequested.getAndSet(null);
/* 211 */     if (opModeToStop != null) {
/* 212 */       processOpModeStopRequest(opModeToStop);
/*     */     }
/*     */     
/* 215 */     this.ftcEventLoopHandler.displayGamePadInfo(this.opModeManager.getActiveOpModeName());
/* 216 */     Gamepad[] gamepads = this.ftcEventLoopHandler.getGamepads();
/*     */     
/* 218 */     this.opModeManager.runActiveOpMode(gamepads);
/*     */   }
/*     */   
/*     */   public void refreshUserTelemetry(TelemetryMessage telemetry, double sInterval)
/*     */   {
/* 223 */     this.ftcEventLoopHandler.refreshUserTelemetry(telemetry, sInterval);
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
/*     */ 
/*     */   public void teardown()
/*     */     throws RobotCoreException
/*     */   {
/* 241 */     DbgLog.msg("======= TEARDOWN =======");
/*     */     
/* 243 */     if (this.usbScanManager != null) {
/* 244 */       this.usbScanManager.stopExecutorService();
/* 245 */       this.usbScanManager = null;
/*     */     }
/*     */     
/* 248 */     this.opModeManager.stopActiveOpMode();
/* 249 */     this.opModeManager.teardown();
/*     */     
/*     */ 
/*     */ 
/* 253 */     this.ftcEventLoopHandler.closeMotorControllers();
/* 254 */     this.ftcEventLoopHandler.closeServoControllers();
/*     */     
/*     */ 
/*     */ 
/* 258 */     this.ftcEventLoopHandler.closeAllUsbDevices();
/*     */     
/* 260 */     DbgLog.msg("======= TEARDOWN COMPLETE =======");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallbackResult processCommand(Command command)
/*     */     throws InterruptedException, RobotCoreException
/*     */   {
/* 271 */     this.ftcEventLoopHandler.sendBatteryInfo();
/*     */     
/* 273 */     CallbackResult result = super.processCommand(command);
/* 274 */     if (!result.stopDispatch()) {
/* 275 */       CallbackResult localResult = CallbackResult.HANDLED;
/*     */       
/* 277 */       String name = command.getName();
/* 278 */       String extra = command.getExtra();
/*     */       
/* 280 */       if (name.equals("CMD_REQUEST_OP_MODE_LIST")) {
/* 281 */         handleCommandRequestOpModeList();
/* 282 */       } else if (name.equals("CMD_INIT_OP_MODE")) {
/* 283 */         handleCommandInitOpMode(extra);
/* 284 */       } else if (name.equals("CMD_RUN_OP_MODE")) {
/* 285 */         handleCommandRunOpMode(extra);
/* 286 */       } else if (name.equals("CMD_SCAN")) {
/* 287 */         handleCommandScan(extra);
/*     */       } else {
/* 289 */         localResult = CallbackResult.NOT_HANDLED;
/*     */       }
/* 291 */       if (localResult == CallbackResult.HANDLED) {
/* 292 */         result = localResult;
/*     */       }
/*     */     }
/* 295 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void handleCommandScan(String extra)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 302 */     RobotLog.vv("FtcConfigTag", "handling command SCAN");
/*     */     
/*     */ 
/* 305 */     if (this.usbScanManager == null) {
/* 306 */       this.usbScanManager = new USBScanManager(this.activityContext, false);
/* 307 */       this.usbScanManager.startExecutorService(0);
/*     */     }
/*     */     
/*     */ 
/* 311 */     ThreadPool.SingletonResult<ScannedDevices> future = this.usbScanManager.startDeviceScanIfNecessary();
/* 312 */     ScannedDevices scannedDevices = (ScannedDevices)future.await();
/*     */     
/*     */ 
/* 315 */     String data = this.usbScanManager.packageCommandResponse(scannedDevices);
/* 316 */     RobotLog.vv("FtcConfigTag", "handleCommandScan data='%s'", new Object[] { data });
/* 317 */     this.networkConnectionHandler.sendCommand(new Command("CMD_SCAN_RESP", data));
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
/*     */   protected void handleCommandRequestOpModeList()
/*     */   {
/* 331 */     while (!this.opModeManager.areOpModesRegistered()) {
/* 332 */       Thread.yield();
/*     */     }
/*     */     
/*     */ 
/* 336 */     String opModeList = new Gson().toJson(this.opModeManager.getOpModes());
/* 337 */     this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_OP_MODE_LIST_RESP", opModeList));
/*     */     
/*     */ 
/* 340 */     UserSensorTypeManager.getInstance().sendUserSensorTypes();
/*     */     
/* 342 */     EventLoopManager manager = this.ftcEventLoopHandler.getEventLoopManager();
/* 343 */     if (manager != null) manager.refreshSystemTelemetryNow();
/*     */   }
/*     */   
/*     */   protected void handleCommandInitOpMode(String extra) {
/* 347 */     String newOpMode = this.ftcEventLoopHandler.getOpMode(extra);
/* 348 */     this.opModeManager.initActiveOpMode(newOpMode);
/*     */   }
/*     */   
/*     */   protected void handleCommandRunOpMode(String extra)
/*     */   {
/* 353 */     String newOpMode = this.ftcEventLoopHandler.getOpMode(extra);
/* 354 */     if (!this.opModeManager.getActiveOpModeName().equals(newOpMode)) {
/* 355 */       this.opModeManager.initActiveOpMode(newOpMode);
/*     */     }
/* 357 */     this.opModeManager.startActiveOpMode();
/*     */   }
/*     */   
/*     */ 
/*     */   public void requestOpModeStop(OpMode opModeToStopIfActive)
/*     */   {
/* 363 */     this.opModeStopRequested.set(opModeToStopIfActive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void processOpModeStopRequest(OpMode opModeToStop)
/*     */   {
/* 370 */     if ((opModeToStop != null) && (this.opModeManager.getActiveOpMode() == opModeToStop))
/*     */     {
/* 372 */       DbgLog.msg("auto-stopping OpMode '%s'", new Object[] { this.opModeManager.getActiveOpModeName() });
/*     */       
/*     */ 
/* 375 */       this.opModeManager.stopActiveOpMode();
/*     */     }
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
/*     */ 
/*     */   public void onUsbDeviceAttached(UsbDevice usbDevice)
/*     */   {
/* 393 */     String serialNumber = getSerialNumberOfUsbDevice(usbDevice);
/* 394 */     if (serialNumber == null) {
/* 395 */       serialNumber = getSerialNumberOfUsbDevice(usbDevice);
/*     */     }
/*     */     
/* 398 */     if (serialNumber != null) {
/* 399 */       synchronized (this.attachedUsbDevicesLock) {
/* 400 */         this.attachedUsbDevices.add(serialNumber);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 408 */       DbgLog.msg("ignoring: unable get serial number of attached UsbDevice vendor=0x%04x, product=0x%04x device=0x%04x name=%s", new Object[] { Integer.valueOf(usbDevice.getVendorId()), Integer.valueOf(usbDevice.getProductId()), Integer.valueOf(usbDevice.getDeviceId()), usbDevice.getDeviceName() });
/*     */     }
/*     */   }
/*     */   
/*     */   protected String getSerialNumberOfUsbDevice(UsbDevice usbDevice)
/*     */   {
/* 414 */     FT_Device ftDevice = null;
/* 415 */     String serialNumber = null;
/*     */     try {
/* 417 */       D2xxManager manager = D2xxManager.getInstance(this.activityContext);
/* 418 */       ftDevice = manager.openByUsbDevice(this.activityContext, usbDevice);
/* 419 */       if (ftDevice != null) {
/* 420 */         serialNumber = ftDevice.getDeviceInfo().serialNumber;
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (NullPointerException localNullPointerException) {}catch (D2xxManager.D2xxException localD2xxException) {}finally
/*     */     {
/* 427 */       if (ftDevice != null)
/* 428 */         ftDevice.close();
/*     */     }
/* 430 */     return serialNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processedRecentlyAttachedUsbDevices()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 441 */     synchronized (this.attachedUsbDevicesLock) {
/* 442 */       Set<String> serialNumbersToProcess = this.attachedUsbDevices;
/* 443 */       this.attachedUsbDevices = new HashSet();
/*     */     }
/*     */     
/*     */     Set<String> serialNumbersToProcess;
/* 447 */     UsbModuleAttachmentHandler handler = this.usbModuleAttachmentHandler;
/*     */     Object modules;
/*     */     Iterator i$;
/* 450 */     if ((handler != null) && (!serialNumbersToProcess.isEmpty()))
/*     */     {
/*     */ 
/* 453 */       modules = this.ftcEventLoopHandler.getHardwareMap().getAll(RobotUsbModule.class);
/*     */       
/*     */ 
/* 456 */       for (i$ = serialNumbersToProcess.iterator(); i$.hasNext();) { serialNumber = (String)i$.next();
/* 457 */         for (RobotUsbModule module : (List)modules) {
/* 458 */           if (module.getSerialNumber().toString().equals(serialNumber)) {
/* 459 */             handler.handleUsbModuleAttach(module);
/* 460 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     String serialNumber;
/*     */   }
/*     */   
/*     */   public void handleUsbModuleDetach(RobotUsbModule module) throws RobotCoreException, InterruptedException
/*     */   {
/* 470 */     UsbModuleAttachmentHandler handler = this.usbModuleAttachmentHandler;
/* 471 */     if (handler != null) {
/* 472 */       handler.handleUsbModuleDetach(module);
/*     */     }
/*     */   }
/*     */   
/*     */   public class DefaultUsbModuleAttachmentHandler implements UsbModuleAttachmentHandler {
/*     */     public DefaultUsbModuleAttachmentHandler() {}
/*     */     
/*     */     public void handleUsbModuleAttach(RobotUsbModule module) throws RobotCoreException, InterruptedException {
/* 480 */       String id = nameOfUsbModule(module);
/*     */       
/* 482 */       DbgLog.msg("======= MODULE ATTACH: disarm %s=======", new Object[] { id });
/* 483 */       module.disarm();
/*     */       
/* 485 */       DbgLog.msg("======= MODULE ATTACH: arm or pretend %s=======", new Object[] { id });
/* 486 */       module.armOrPretend();
/*     */       
/* 488 */       DbgLog.msg("======= MODULE ATTACH: complete %s=======", new Object[] { id });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void handleUsbModuleDetach(RobotUsbModule module)
/*     */       throws RobotCoreException, InterruptedException
/*     */     {
/* 496 */       String id = nameOfUsbModule(module);
/*     */       
/* 498 */       DbgLog.msg("======= MODULE DETACH RECOVERY: disarm %s=======", new Object[] { id });
/*     */       
/*     */ 
/* 501 */       module.disarm();
/*     */       
/* 503 */       DbgLog.msg("======= MODULE DETACH RECOVERY: pretend %s=======", new Object[] { id });
/*     */       
/*     */ 
/* 506 */       module.pretend();
/*     */       
/* 508 */       DbgLog.msg("======= MODULE DETACH RECOVERY: complete %s=======", new Object[] { id });
/*     */     }
/*     */     
/*     */     String nameOfUsbModule(RobotUsbModule module) {
/* 512 */       return HardwareFactory.getDeviceDisplayName(FtcEventLoop.this.activityContext, module.getSerialNumber());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\FtcEventLoop.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */