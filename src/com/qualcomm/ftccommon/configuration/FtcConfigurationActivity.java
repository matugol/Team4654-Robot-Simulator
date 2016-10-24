/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.AlertDialog;
/*     */ import android.app.AlertDialog.Builder;
/*     */ import android.content.DialogInterface;
/*     */ import android.content.DialogInterface.OnClickListener;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.view.View;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemClickListener;
/*     */ import android.widget.Button;
/*     */ import android.widget.EditText;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.ListView;
/*     */ import android.widget.TextView;
/*     */ import android.widget.Toast;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.exception.DuplicateNameException;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.RobocolDatagram;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import com.qualcomm.robotcore.util.ThreadPool.Singleton;
/*     */ import com.qualcomm.robotcore.util.ThreadPool.SingletonResult;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.Event;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.NetworkConnectionCallback;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.ToastLocation;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable.RecvLoopCallback;
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
/*     */ public class FtcConfigurationActivity
/*     */   extends EditActivity
/*     */   implements RecvLoopRunnable.RecvLoopCallback, NetworkConnection.NetworkConnectionCallback
/*     */ {
/*     */   protected static final boolean DEBUG = false;
/*     */   public static final String TAG = "FtcConfigTag";
/*  93 */   public static final RequestCode requestCode = RequestCode.EDIT_FILE;
/*     */   
/*  95 */   protected USBScanManager usbScanManager = null;
/*  96 */   protected ThreadPool.Singleton scanButtonSingleton = new ThreadPool.Singleton();
/*  97 */   protected final Object robotConfigMapLock = new Object();
/*  98 */   protected int idOrangeToastAnchor = R.id.orangeTextAnchor;
/*  99 */   protected Semaphore orangeTextPosted = new Semaphore(0);
/* 100 */   protected long msScanWait = 4000L;
/* 101 */   protected long msSaveSplashDelay = 1000L;
/* 102 */   protected NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/* 110 */     super.onCreate(savedInstanceState);
/* 111 */     RobotLog.vv("FtcConfigTag", "onCreate()");
/* 112 */     setContentView(R.layout.activity_ftc_configuration);
/*     */     try
/*     */     {
/* 115 */       EditParameters parameters = EditParameters.fromIntent(this, getIntent());
/* 116 */       deserialize(parameters);
/*     */       
/* 118 */       Button scanButton = (Button)findViewById(R.id.scanButton);
/* 119 */       scanButton.setVisibility(0);
/*     */       
/* 121 */       Button doneButton = (Button)findViewById(R.id.doneButton);
/* 122 */       doneButton.setText(R.string.buttonNameSave);
/*     */       
/* 124 */       startExecutorService();
/*     */     }
/*     */     catch (RobotCoreException e) {
/* 127 */       RobotLog.ee("FtcConfigTag", "exception thrown during FtcConfigurationActivity.onCreate()");
/* 128 */       finishCancel();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 134 */     super.onStart();
/*     */     
/* 136 */     if (this.remoteConfigure) {
/* 137 */       this.networkConnectionHandler.pushNetworkConnectionCallback(this);
/* 138 */       this.networkConnectionHandler.pushReceiveLoopCallback(this);
/*     */     }
/*     */     
/* 141 */     if (!this.remoteConfigure) {
/* 142 */       this.robotConfigFileManager.createConfigFolder();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 148 */     if (!this.currentCfgFile.isDirty()) {
/* 149 */       if (this.haveRobotConfigMapParameter)
/*     */       {
/* 151 */         populateListAndWarnDevices();
/* 152 */       } else if (this.remoteConfigure)
/*     */       {
/* 154 */         this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_PARTICULAR_CONFIGURATION", this.currentCfgFile.toString()));
/*     */       }
/*     */       else {
/* 157 */         readFile();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onResume()
/*     */   {
/* 164 */     super.onResume();
/*     */   }
/*     */   
/*     */   public void onPause()
/*     */   {
/* 169 */     super.onPause();
/*     */   }
/*     */   
/*     */   protected void onStop()
/*     */   {
/* 174 */     super.onStop();
/* 175 */     if (this.remoteConfigure) {
/* 176 */       this.networkConnectionHandler.removeNetworkConnectionCallback(this);
/* 177 */       this.networkConnectionHandler.removeReceiveLoopCallback(this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onDestroy()
/*     */   {
/* 183 */     RobotLog.vv("FtcConfigTag", "FtcConfigurationActivity.onDestroy()");
/* 184 */     super.onDestroy();
/* 185 */     stopExecutorService();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */   DialogInterface.OnClickListener doNothingAndCloseListener = new DialogInterface.OnClickListener()
/*     */   {
/*     */     public void onClick(DialogInterface dialog, int button) {}
/*     */   };
/*     */   
/*     */   public void onDevicesInfoButtonPressed(View v) {
/* 198 */     RobotLog.vv("FtcConfigTag", "onDevicesInfoButtonPressed()");
/* 199 */     AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.titleDevices), getString(R.string.msgInfoHowToUse));
/* 200 */     builder.setPositiveButton(getString(R.string.buttonNameOK), this.doNothingAndCloseListener);
/* 201 */     AlertDialog alert = builder.create();
/* 202 */     alert.show();
/* 203 */     TextView textView = (TextView)alert.findViewById(16908299);
/* 204 */     textView.setTextSize(14.0F);
/*     */   }
/*     */   
/*     */   public void onDoneInfoButtonPressed(View v) {
/* 208 */     RobotLog.vv("FtcConfigTag", "onDoneInfoButtonPressed()");
/* 209 */     AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.titleSaveConfiguration), getString(R.string.msgInfoSave));
/* 210 */     builder.setPositiveButton(getString(R.string.buttonNameOK), this.doNothingAndCloseListener);
/* 211 */     AlertDialog alert = builder.create();
/* 212 */     alert.show();
/* 213 */     TextView textView = (TextView)alert.findViewById(16908299);
/* 214 */     textView.setTextSize(14.0F);
/*     */   }
/*     */   
/*     */   public void onScanButtonPressed(View v) {
/* 218 */     dirtyCheckThenSingletonUSBScanAndUpdateUI(true);
/*     */   }
/*     */   
/*     */   void dirtyCheckThenSingletonUSBScanAndUpdateUI(final boolean showOrangeText) {
/* 222 */     final Runnable runnable = new Runnable() {
/*     */       public void run() {
/* 224 */         ThreadPool.logThreadLifeCycle("USB bus scan handler", new Runnable() {
/*     */           public void run() {
/* 226 */             if (FtcConfigurationActivity.2.this.val$showOrangeText) {
/* 227 */               FtcConfigurationActivity.this.synchronouslySetOrangeTextWhile(FtcConfigurationActivity.this.getString(R.string.ftcConfigScanning), "", new Runnable() {
/*     */                 public void run() {
/* 229 */                   FtcConfigurationActivity.this.doUSBScanAndUpdateUI();
/*     */                 }
/*     */               });
/*     */             } else {
/* 233 */               FtcConfigurationActivity.this.doUSBScanAndUpdateUI();
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */     };
/* 239 */     if (this.currentCfgFile.isDirty()) {
/* 240 */       AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.titleUnsavedChanges), getString(R.string.msgAlertBeforeScan));
/* 241 */       DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
/*     */         public void onClick(DialogInterface dialog, int button) {
/* 243 */           FtcConfigurationActivity.this.scanButtonSingleton.submit(runnable);
/*     */         }
/* 245 */       };
/* 246 */       builder.setPositiveButton(R.string.buttonNameOK, okListener);
/* 247 */       builder.setNegativeButton(R.string.buttonNameCancel, this.doNothingAndCloseListener);
/* 248 */       builder.show();
/*     */     } else {
/* 250 */       this.scanButtonSingleton.submit(runnable);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doUSBScanAndUpdateUI()
/*     */   {
/*     */     try
/*     */     {
/* 264 */       ThreadPool.SingletonResult<ScannedDevices> future = this.usbScanManager.startDeviceScanIfNecessary();
/* 265 */       ScannedDevices devices = (ScannedDevices)future.await(this.msScanWait);
/*     */       
/* 267 */       if (devices != null)
/*     */       {
/*     */ 
/* 270 */         buildRobotConfigMapFromScanned(devices);
/*     */         
/*     */ 
/* 273 */         this.appUtil.synchronousRunOnUiThread(new Runnable() {
/*     */           public void run() {
/* 275 */             FtcConfigurationActivity.this.clearDuplicateWarning();
/* 276 */             FtcConfigurationActivity.this.currentCfgFile.markDirty();
/* 277 */             FtcConfigurationActivity.this.robotConfigFileManager.updateActiveConfigHeader(FtcConfigurationActivity.this.currentCfgFile);
/* 278 */             FtcConfigurationActivity.this.populateListAndWarnDevices();
/*     */           }
/*     */         });
/*     */       }
/*     */       else {
/* 283 */         this.appUtil.showToast(ToastLocation.ONLY_LOCAL, this.context, getString(R.string.ftcConfigScanningFailed));
/*     */       }
/*     */     }
/*     */     catch (InterruptedException e) {
/* 287 */       Thread.currentThread().interrupt();
/*     */     }
/*     */   }
/*     */   
/*     */   private void startExecutorService() throws RobotCoreException {
/* 292 */     this.usbScanManager = new USBScanManager(this, this.remoteConfigure);
/* 293 */     this.usbScanManager.startExecutorService(1);
/*     */     
/*     */ 
/*     */ 
/* 297 */     this.scanButtonSingleton.reset();
/* 298 */     this.scanButtonSingleton.setService(this.usbScanManager.getExecutorService());
/*     */     
/*     */ 
/*     */ 
/* 302 */     this.usbScanManager.startDeviceScanIfNecessary();
/*     */   }
/*     */   
/*     */   private void stopExecutorService() {
/* 306 */     this.usbScanManager.stopExecutorService();
/* 307 */     this.usbScanManager = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static RobotConfigMap buildRobotConfigMapFromScanned(Utility utility, RobotConfigMap existingControllers, ScannedDevices scannedDevices)
/*     */   {
/* 316 */     RobotConfigMap newRobotConfigMap = new RobotConfigMap();
/*     */     
/* 318 */     utility.resetCount();
/* 319 */     for (Map.Entry<SerialNumber, DeviceManager.DeviceType> entry : scannedDevices.entrySet()) {
/* 320 */       SerialNumber serialNumber = (SerialNumber)entry.getKey();
/* 321 */       ControllerConfiguration controllerConfiguration = null;
/* 322 */       if ((existingControllers != null) && (existingControllers.contains(serialNumber))) {
/* 323 */         controllerConfiguration = existingControllers.get(serialNumber);
/*     */       } else {
/* 325 */         switch ((DeviceManager.DeviceType)entry.getValue()) {
/*     */         case MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER: 
/* 327 */           controllerConfiguration = utility.buildModernMotorController(serialNumber);
/* 328 */           break;
/*     */         case MODERN_ROBOTICS_USB_SERVO_CONTROLLER: 
/* 330 */           controllerConfiguration = utility.buildModernServoController(serialNumber);
/* 331 */           break;
/*     */         case MODERN_ROBOTICS_USB_LEGACY_MODULE: 
/* 333 */           controllerConfiguration = utility.buildLegacyModule(serialNumber);
/* 334 */           break;
/*     */         case MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE: 
/* 336 */           controllerConfiguration = utility.buildDeviceInterfaceModule(serialNumber);
/*     */         }
/*     */         
/*     */       }
/* 340 */       if (controllerConfiguration != null) {
/* 341 */         controllerConfiguration.setKnownToBeAttached(true);
/* 342 */         newRobotConfigMap.put(serialNumber, controllerConfiguration);
/*     */       }
/*     */     }
/* 345 */     return newRobotConfigMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readFile()
/*     */   {
/* 355 */     if (this.currentCfgFile.isNoConfig())
/*     */     {
/* 357 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 361 */       XmlPullParser xmlPullParser = this.currentCfgFile.getXml();
/* 362 */       if (xmlPullParser == null) { throw new RobotCoreException("can't access configuration");
/*     */       }
/* 364 */       ReadXMLFileHandler parser = new ReadXMLFileHandler();
/* 365 */       List<ControllerConfiguration> controllerList = parser.parse(xmlPullParser);
/* 366 */       buildControllersFromXMLResults(controllerList);
/* 367 */       populateListAndWarnDevices();
/*     */     }
/*     */     catch (Exception e) {
/* 370 */       String message = String.format(getString(R.string.errorParsingConfiguration), new Object[] { this.currentCfgFile.getName() });
/* 371 */       RobotLog.ee("FtcConfigTag", message);
/* 372 */       RobotLog.logStacktrace(e);
/* 373 */       this.appUtil.showToast(ToastLocation.ONLY_LOCAL, this.context, message);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void populateListAndWarnDevices()
/*     */   {
/* 380 */     this.appUtil.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 383 */         FtcConfigurationActivity.this.populateList();
/* 384 */         FtcConfigurationActivity.this.warnIncompleteDevices();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void warnIncompleteDevices()
/*     */   {
/* 391 */     if (!getRobotConfigMap().allControllersAreBound()) {
/* 392 */       String title = getString(R.string.notAllDevicesFoundTitle);
/* 393 */       String message = String.format(getString(R.string.notAllDevicesFoundMessage), new Object[] { getString(R.string.noSerialNumber) });
/* 394 */       this.utility.setOrangeText(title, message, this.idOrangeToastAnchor, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1, R.id.orangeTextOKButton);
/* 395 */     } else if (getRobotConfigMap().size() == 0) {
/* 396 */       String title = getString(R.string.noDevicesFoundTitle);
/* 397 */       String message = getString(R.string.noDevicesFoundMessage);
/* 398 */       this.utility.setOrangeText(title, message, this.idOrangeToastAnchor, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1, R.id.orangeTextOKButton);
/* 399 */       clearDuplicateWarning();
/*     */     } else {
/* 401 */       this.utility.hideOrangeText(this.idOrangeToastAnchor);
/*     */     }
/*     */   }
/*     */   
/*     */   private void synchronouslySetOrangeTextWhile(final String title, final String message, Runnable runnable)
/*     */   {
/* 407 */     final CharSequence[] prev = this.utility.getOrangeText(this.idOrangeToastAnchor, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */     try
/*     */     {
/* 410 */       this.appUtil.synchronousRunOnUiThread(new Runnable() {
/* 411 */         public void run() { FtcConfigurationActivity.this.utility.setOrangeText(title, message, FtcConfigurationActivity.this.idOrangeToastAnchor, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/* 412 */           FtcConfigurationActivity.this.orangeTextPosted.release();
/*     */         } });
/* 414 */       try { this.orangeTextPosted.acquire(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
/* 415 */       runnable.run();
/*     */     }
/*     */     finally {
/* 418 */       this.appUtil.runOnUiThread(new Runnable() {
/* 419 */         public void run() { if (prev != null) {
/* 420 */             FtcConfigurationActivity.this.utility.setOrangeText(prev, FtcConfigurationActivity.this.idOrangeToastAnchor, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */           } else
/* 422 */             FtcConfigurationActivity.this.utility.hideOrangeText(FtcConfigurationActivity.this.idOrangeToastAnchor);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private void warnDuplicateNames(String dupeMsg) {
/* 429 */     String msg0 = "Found " + dupeMsg;
/* 430 */     String msg1 = "Please fix and re-save.";
/* 431 */     this.utility.setOrangeText(msg0, msg1, R.id.orange_warning_anchor, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */   }
/*     */   
/*     */   private void clearDuplicateWarning() {
/* 435 */     LinearLayout warning_layout = (LinearLayout)findViewById(R.id.orange_warning_anchor);
/* 436 */     warning_layout.removeAllViews();
/* 437 */     warning_layout.setVisibility(8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void populateList()
/*     */   {
/* 445 */     ListView controllerListView = (ListView)findViewById(R.id.controllersList);
/*     */     
/*     */     try
/*     */     {
/* 449 */       this.scannedDevices = this.usbScanManager.await(this.msScanWait);
/*     */     } catch (InterruptedException e) {
/* 451 */       Thread.currentThread().interrupt();
/*     */     }
/*     */     
/*     */ 
/* 455 */     tellControllersAboutAttachment();
/*     */     
/* 457 */     DeviceInfoAdapter adapter = new DeviceInfoAdapter(this, 17367044, new LinkedList(getRobotConfigMap().controllerConfigurations()));
/* 458 */     controllerListView.setAdapter(adapter);
/*     */     
/* 460 */     controllerListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
/*     */     {
/*     */       public void onItemClick(AdapterView<?> adapterView, View v, int pos, long arg3)
/*     */       {
/* 464 */         ControllerConfiguration controllerConfiguration = (ControllerConfiguration)adapterView.getItemAtPosition(pos);
/* 465 */         ConfigurationType itemType = controllerConfiguration.getType();
/* 466 */         if (itemType == BuiltInConfigurationType.MOTOR_CONTROLLER)
/*     */         {
/* 468 */           EditParameters parameters = FtcConfigurationActivity.this.initParameters(1, new EditParameters(FtcConfigurationActivity.this, controllerConfiguration, ((MotorControllerConfiguration)controllerConfiguration).getMotors()));
/* 469 */           FtcConfigurationActivity.this.handleLaunchEdit(EditMotorControllerActivity.requestCode, EditMotorControllerActivity.class, parameters);
/*     */         }
/* 471 */         else if (itemType == BuiltInConfigurationType.SERVO_CONTROLLER)
/*     */         {
/* 473 */           EditParameters parameters = FtcConfigurationActivity.this.initParameters(1, new EditParameters(FtcConfigurationActivity.this, controllerConfiguration, ((ServoControllerConfiguration)controllerConfiguration).getServos()));
/* 474 */           FtcConfigurationActivity.this.handleLaunchEdit(EditServoControllerActivity.requestCode, EditServoControllerActivity.class, parameters);
/*     */         }
/* 476 */         else if (itemType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER)
/*     */         {
/* 478 */           EditParameters parameters = FtcConfigurationActivity.this.initParameters(0, new EditParameters(FtcConfigurationActivity.this, controllerConfiguration, ((LegacyModuleControllerConfiguration)controllerConfiguration).getDevices()));
/* 479 */           FtcConfigurationActivity.this.handleLaunchEdit(EditLegacyModuleControllerActivity.requestCode, EditLegacyModuleControllerActivity.class, parameters);
/*     */         }
/* 481 */         else if (itemType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE)
/*     */         {
/* 483 */           EditParameters parameters = FtcConfigurationActivity.this.initParameters(0, new EditParameters(FtcConfigurationActivity.this, controllerConfiguration, ((DeviceInterfaceModuleConfiguration)controllerConfiguration).getDevices()));
/* 484 */           FtcConfigurationActivity.this.handleLaunchEdit(EditDeviceInterfaceModuleActivity.requestCode, EditDeviceInterfaceModuleActivity.class, parameters);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   EditParameters initParameters(int initialPortNumber, EditParameters parameters)
/*     */   {
/* 492 */     parameters.setInitialPortNumber(initialPortNumber);
/* 493 */     parameters.setScannedDevices(this.scannedDevices);
/* 494 */     parameters.setRobotConfigMap(getRobotConfigMap());
/* 495 */     return parameters;
/*     */   }
/*     */   
/*     */   protected void onActivityResult(int requestCodeValue, int resultCode, Intent data)
/*     */   {
/*     */     try {
/* 501 */       logActivityResult(requestCodeValue, resultCode, data);
/* 502 */       if (resultCode == 0) {
/* 503 */         return;
/*     */       }
/* 505 */       RequestCode requestCode = RequestCode.fromValue(requestCodeValue);
/* 506 */       EditParameters parameters = EditParameters.fromIntent(this, data);
/*     */       
/* 508 */       RobotLog.vv("FtcConfigTag", "onActivityResult(%s)", new Object[] { requestCode.toString() });
/*     */       
/*     */ 
/*     */ 
/* 512 */       synchronized (this.robotConfigMapLock)
/*     */       {
/* 514 */         deserializeConfigMap(parameters);
/*     */       }
/*     */       
/*     */ 
/* 518 */       this.scannedDevices = this.usbScanManager.await(this.msScanWait);
/*     */       
/*     */ 
/* 521 */       this.appUtil.runOnUiThread(new Runnable() {
/*     */         public void run() {
/* 523 */           FtcConfigurationActivity.this.currentCfgFile.markDirty();
/* 524 */           FtcConfigurationActivity.this.robotConfigFileManager.updateActiveConfigHeader(FtcConfigurationActivity.this.currentCfgFile);
/* 525 */           FtcConfigurationActivity.this.populateList();
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (InterruptedException e) {
/* 530 */       Thread.currentThread().interrupt();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onBackPressed()
/*     */   {
/* 536 */     RobotLog.vv("FtcConfigTag", "onBackPressed()");
/* 537 */     doBackOrCancel();
/*     */   }
/*     */   
/*     */   public void onCancelButtonPressed(View view) {
/* 541 */     RobotLog.vv("FtcConfigTag", "onCancelButtonPressed()");
/* 542 */     doBackOrCancel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void doBackOrCancel()
/*     */   {
/* 551 */     if (this.currentCfgFile.isDirty())
/*     */     {
/* 553 */       DialogInterface.OnClickListener exitWithoutSavingButtonListener = new DialogInterface.OnClickListener() {
/*     */         public void onClick(DialogInterface dialog, int which) {
/* 555 */           FtcConfigurationActivity.this.finishCancel();
/*     */         }
/*     */         
/* 558 */       };
/* 559 */       AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.saveChangesTitle), getString(R.string.saveChangesMessage));
/* 560 */       builder.setPositiveButton(R.string.buttonExitWithoutSaving, exitWithoutSavingButtonListener);
/* 561 */       builder.setNegativeButton(R.string.buttonNameCancel, this.doNothingAndCloseListener);
/* 562 */       builder.show();
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 567 */       finishCancel();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDoneButtonPressed(View v)
/*     */   {
/* 578 */     RobotLog.vv("FtcConfigTag", "onDoneButtonPressed()");
/*     */     
/*     */ 
/* 581 */     final String data = this.robotConfigFileManager.toXml(getRobotConfigMap());
/* 582 */     if (data == null) {
/* 583 */       return;
/*     */     }
/*     */     
/* 586 */     String message = getString(R.string.configNamePromptBanter);
/* 587 */     final EditText input = new EditText(this);
/* 588 */     input.setText(this.currentCfgFile.isNoConfig() ? "" : this.currentCfgFile.getName());
/*     */     
/* 590 */     AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.configNamePromptTitle), message);
/* 591 */     builder.setView(input);
/*     */     
/* 593 */     DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
/*     */       public void onClick(DialogInterface dialog, int button) {
/* 595 */         String newConfigurationName = input.getText().toString();
/*     */         
/* 597 */         RobotConfigFileManager.ConfigNameCheckResult checkResult = FtcConfigurationActivity.this.robotConfigFileManager.isPlausibleConfigName(FtcConfigurationActivity.this.currentCfgFile, newConfigurationName, FtcConfigurationActivity.this.extantRobotConfigurations);
/* 598 */         if (!checkResult.success)
/*     */         {
/* 600 */           String message = String.format(checkResult.errorFormat, new Object[] { newConfigurationName });
/* 601 */           FtcConfigurationActivity.this.appUtil.showToast(ToastLocation.ONLY_LOCAL, FtcConfigurationActivity.this.context, String.format("%s %s", new Object[] { message, FtcConfigurationActivity.this.getString(R.string.configurationNotSaved) }));
/* 602 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/* 610 */           if (!FtcConfigurationActivity.this.currentCfgFile.getName().equals(newConfigurationName)) {
/* 611 */             FtcConfigurationActivity.this.currentCfgFile = new RobotConfigFile(FtcConfigurationActivity.this.robotConfigFileManager, newConfigurationName);
/*     */           }
/* 613 */           FtcConfigurationActivity.this.robotConfigFileManager.writeToFile(FtcConfigurationActivity.this.currentCfgFile, FtcConfigurationActivity.this.remoteConfigure, data);
/* 614 */           FtcConfigurationActivity.this.robotConfigFileManager.setActiveConfigAndUpdateUI(FtcConfigurationActivity.this.remoteConfigure, FtcConfigurationActivity.this.currentCfgFile);
/*     */         }
/*     */         catch (DuplicateNameException e) {
/* 617 */           FtcConfigurationActivity.this.warnDuplicateNames(e.getMessage());
/* 618 */           RobotLog.ee("FtcConfigTag", e.getMessage());
/* 619 */           return;
/*     */         } catch (RobotCoreException|IOException e) {
/* 621 */           FtcConfigurationActivity.this.appUtil.showToast(ToastLocation.ONLY_LOCAL, FtcConfigurationActivity.this.context, e.getMessage());
/* 622 */           RobotLog.ee("FtcConfigTag", e.getMessage());
/* 623 */           return;
/*     */         }
/* 625 */         FtcConfigurationActivity.this.clearDuplicateWarning();
/* 626 */         FtcConfigurationActivity.this.confirmSave();
/* 627 */         FtcConfigurationActivity.this.pauseAfterSave();
/* 628 */         FtcConfigurationActivity.this.finishOk();
/*     */       }
/*     */       
/* 631 */     };
/* 632 */     builder.setPositiveButton(getString(R.string.buttonNameOK), okListener);
/* 633 */     builder.setNegativeButton(getString(R.string.buttonNameCancel), this.doNothingAndCloseListener);
/* 634 */     builder.show();
/*     */   }
/*     */   
/*     */   private void confirmSave() {
/* 638 */     Toast confirmation = Toast.makeText(this, R.string.toastSaved, 0);
/* 639 */     confirmation.setGravity(80, 0, 50);
/* 640 */     confirmation.show();
/*     */   }
/*     */   
/*     */   private void pauseAfterSave() {
/* 644 */     try { Thread.sleep(this.msSaveSplashDelay); } catch (InterruptedException e) { Thread.currentThread().interrupt();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void buildControllersFromXMLResults(List<ControllerConfiguration> deviceList)
/*     */   {
/* 654 */     synchronized (this.robotConfigMapLock) {
/* 655 */       this.robotConfigMap = new RobotConfigMap(deviceList);
/*     */     }
/*     */   }
/*     */   
/*     */   private void buildRobotConfigMapFromScanned(ScannedDevices scannedDevices) {
/* 660 */     synchronized (this.robotConfigMapLock) {
/* 661 */       this.robotConfigMap = buildRobotConfigMapFromScanned(this.utility, getRobotConfigMap(), scannedDevices);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected RobotConfigMap getRobotConfigMap()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 16	com/qualcomm/ftccommon/configuration/FtcConfigurationActivity:robotConfigMapLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: invokespecial 253	com/qualcomm/ftccommon/configuration/EditActivity:getRobotConfigMap	()Lcom/qualcomm/ftccommon/configuration/RobotConfigMap;
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: areturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #666	-> byte code offset #0
/*     */     //   Java source line #667	-> byte code offset #7
/*     */     //   Java source line #668	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	FtcConfigurationActivity
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   protected void tellControllersAboutAttachment()
/*     */   {
/* 672 */     for (ControllerConfiguration controllerConfiguration : getRobotConfigMap().controllerConfigurations()) {
/* 673 */       controllerConfiguration.setKnownToBeAttached(this.scannedDevices.containsKey(controllerConfiguration.getSerialNumber()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected CallbackResult handleCommandScanResp(String extra)
/*     */     throws RobotCoreException
/*     */   {
/* 682 */     if (this.usbScanManager != null) {
/* 683 */       this.usbScanManager.handleCommandScanResponse(extra);
/*     */       
/*     */ 
/* 686 */       populateListAndWarnDevices();
/*     */     }
/* 688 */     return CallbackResult.HANDLED_CONTINUE;
/*     */   }
/*     */   
/*     */   protected CallbackResult handleCommandRequestParticularConfigurationResp(String extra) throws RobotCoreException {
/* 692 */     ReadXMLFileHandler readXMLFileHandler = new ReadXMLFileHandler();
/* 693 */     List<ControllerConfiguration> controllerList = readXMLFileHandler.parse(new StringReader(extra));
/* 694 */     buildControllersFromXMLResults(controllerList);
/* 695 */     populateListAndWarnDevices();
/* 696 */     return CallbackResult.HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult commandEvent(Command command)
/*     */   {
/* 701 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/*     */     try {
/* 703 */       String name = command.getName();
/* 704 */       String extra = command.getExtra();
/*     */       
/* 706 */       if (name.equals("CMD_SCAN_RESP")) {
/* 707 */         result = handleCommandScanResp(extra);
/* 708 */       } else if (name.equals("CMD_REQUEST_PARTICULAR_CONFIGURATION_RESP")) {
/* 709 */         result = handleCommandRequestParticularConfigurationResp(extra);
/*     */       }
/*     */     } catch (RobotCoreException e) {
/* 712 */       RobotLog.logStacktrace(e);
/*     */     }
/* 714 */     return result;
/*     */   }
/*     */   
/*     */   public CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event)
/*     */   {
/* 719 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult packetReceived(RobocolDatagram packet)
/*     */   {
/* 724 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult peerDiscoveryEvent(RobocolDatagram packet)
/*     */   {
/* 729 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult heartbeatEvent(RobocolDatagram packet, long tReceived)
/*     */   {
/* 734 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult telemetryEvent(RobocolDatagram packet)
/*     */   {
/* 739 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult gamepadEvent(RobocolDatagram packet)
/*     */   {
/* 744 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult emptyEvent(RobocolDatagram packet)
/*     */   {
/* 749 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult reportGlobalError(String error, boolean recoverable)
/*     */   {
/* 754 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\FtcConfigurationActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */