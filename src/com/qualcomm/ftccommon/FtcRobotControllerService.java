/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Service;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.net.wifi.WifiManager;
/*     */ import android.os.Binder;
/*     */ import android.os.Build;
/*     */ import android.os.IBinder;
/*     */ import android.support.annotation.NonNull;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoop;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager.EventLoopMonitor;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.factory.RobotFactory;
/*     */ import com.qualcomm.robotcore.robot.Robot;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ import com.qualcomm.robotcore.robot.RobotStatus;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.Event;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.NetworkConnectionCallback;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
/*     */ import com.qualcomm.robotcore.wifi.NetworkType;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.PeerStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcRobotControllerService
/*     */   extends Service
/*     */   implements NetworkConnection.NetworkConnectionCallback
/*     */ {
/*     */   public static final String TAG = "FTCService";
/*     */   private static final double NETWORK_MAX_WAIT = 120.0D;
/*     */   private static final int NETWORK_WAIT = 1000;
/*     */   private static final int USB_WAIT = 5000;
/*     */   private final IBinder binder;
/*     */   private NetworkConnection networkConnection;
/*     */   private EventLoopManager eventLoopManager;
/*     */   private Robot robot;
/*     */   private EventLoop eventLoop;
/*     */   private EventLoop idleEventLoop;
/*     */   private NetworkConnection.Event networkConnectionStatus;
/*     */   private RobotStatus robotStatus;
/*     */   private UpdateUI.Callback callback;
/*     */   private final EventLoopMonitor eventLoopMonitor;
/*     */   private final ElapsedTime networkTimer;
/*     */   private ExecutorService robotSetupExecutor;
/*     */   
/*     */   public FtcRobotControllerService()
/*     */   {
/*  73 */     this.binder = new FtcRobotControllerBinder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */     this.networkConnectionStatus = NetworkConnection.Event.UNKNOWN;
/*  82 */     this.robotStatus = RobotStatus.NONE;
/*     */     
/*  84 */     this.callback = null;
/*  85 */     this.eventLoopMonitor = new EventLoopMonitor(null);
/*     */     
/*  87 */     this.networkTimer = new ElapsedTime();
/*     */     
/*  89 */     this.robotSetupExecutor = null; }
/*     */   
/*     */   public class FtcRobotControllerBinder extends Binder { public FtcRobotControllerBinder() {}
/*     */     
/*  93 */     public FtcRobotControllerService getService() { return FtcRobotControllerService.this; }
/*     */   }
/*     */   
/*     */   private class EventLoopMonitor implements EventLoopManager.EventLoopMonitor
/*     */   {
/*     */     private EventLoopMonitor() {}
/*     */     
/*     */     public void onStateChange(@NonNull RobotState state) {
/* 101 */       if (FtcRobotControllerService.this.callback == null) return;
/* 102 */       FtcRobotControllerService.this.callback.updateRobotState(state);
/* 103 */       if (state == RobotState.RUNNING) {
/* 104 */         FtcRobotControllerService.this.callback.updateRobotStatus(RobotStatus.NONE);
/*     */       }
/*     */     }
/*     */     
/*     */     public void onPeerConnected(boolean peerLikelyChanged) {
/* 109 */       if (FtcRobotControllerService.this.callback == null) return;
/* 110 */       FtcRobotControllerService.this.callback.updatePeerStatus(PeerStatus.CONNECTED);
/*     */     }
/*     */     
/*     */     public void onPeerDisconnected() {
/* 114 */       if (FtcRobotControllerService.this.callback == null) return;
/* 115 */       FtcRobotControllerService.this.callback.updatePeerStatus(PeerStatus.DISCONNECTED);
/*     */     }
/*     */     
/*     */     public void onTelemetryTransmitted()
/*     */     {
/* 120 */       if (FtcRobotControllerService.this.callback == null) return;
/* 121 */       FtcRobotControllerService.this.callback.refreshErrorTextOnUiThread();
/*     */     }
/*     */   }
/*     */   
/*     */   private class RobotSetupRunnable implements Runnable {
/*     */     private RobotSetupRunnable() {}
/*     */     
/* 128 */     public void run() { ThreadPool.logThreadLifeCycle("RobotSetupRunnable.run()", new Runnable()
/*     */       {
/*     */         public void run() {
/*     */           try {
/* 132 */             if (FtcRobotControllerService.this.robot != null) {
/* 133 */               FtcRobotControllerService.this.robot.shutdown();
/* 134 */               FtcRobotControllerService.this.robot = null;
/*     */             }
/*     */             
/* 137 */             FtcRobotControllerService.this.updateRobotStatus(RobotStatus.SCANNING_USB);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             try
/*     */             {
/* 149 */               Thread.sleep(5000L);
/*     */             }
/*     */             catch (InterruptedException e) {
/* 152 */               FtcRobotControllerService.this.updateRobotStatus(RobotStatus.ABORT_DUE_TO_INTERRUPT);
/* 153 */               return;
/*     */             }
/*     */             
/* 156 */             if (FtcRobotControllerService.this.eventLoopManager == null) {
/* 157 */               FtcRobotControllerService.this.eventLoopManager = new EventLoopManager(FtcRobotControllerService.this);
/* 158 */               FtcRobotControllerService.this.eventLoopManager.setIdleEventLoop(FtcRobotControllerService.this.idleEventLoop);
/*     */             }
/*     */             
/* 161 */             FtcRobotControllerService.this.robot = RobotFactory.createRobot(FtcRobotControllerService.this.eventLoopManager);
/*     */             
/* 163 */             FtcRobotControllerService.this.updateRobotStatus(RobotStatus.WAITING_ON_NETWORK);
/*     */             
/*     */ 
/* 166 */             FtcRobotControllerService.this.networkTimer.reset();
/* 167 */             while ((FtcRobotControllerService.this.networkConnection.getNetworkType() != NetworkType.SOFTAP) && 
/* 168 */               (!FtcRobotControllerService.this.networkConnection.isConnected())) {
/*     */               try {
/* 170 */                 Thread.sleep(1000L);
/* 171 */                 if (FtcRobotControllerService.this.networkTimer.time() > 120.0D) {
/* 172 */                   FtcRobotControllerService.this.updateRobotStatus(RobotStatus.NETWORK_TIMED_OUT);
/* 173 */                   return;
/*     */                 }
/*     */               } catch (InterruptedException e) {
/* 176 */                 RobotLog.vv("FTCService", "interrupt waiting for network; aborting setup");
/* 177 */                 return;
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 183 */             FtcRobotControllerService.this.updateRobotStatus(RobotStatus.STARTING_ROBOT);
/*     */             try {
/* 185 */               FtcRobotControllerService.this.robot.eventLoopManager.setMonitor(FtcRobotControllerService.this.eventLoopMonitor);
/* 186 */               FtcRobotControllerService.this.robot.start(FtcRobotControllerService.this.eventLoop);
/*     */             } catch (RobotCoreException e) {
/* 188 */               FtcRobotControllerService.this.updateRobotStatus(RobotStatus.FAILED_TO_START_ROBOT);
/* 189 */               RobotLog.setGlobalErrorMsg(e, FtcRobotControllerService.this.getString(R.string.globalErrorFailedToStartRobot));
/*     */             }
/*     */           } catch (RobotCoreException e) {
/* 192 */             FtcRobotControllerService.this.updateRobotStatus(RobotStatus.UNABLE_TO_CREATE_ROBOT);
/* 193 */             RobotLog.setGlobalErrorMsg(e, FtcRobotControllerService.this.getString(R.string.globalErrorFailedToCreateRobot));
/*     */           }
/*     */         }
/*     */       }); }
/*     */   }
/*     */   
/*     */   public NetworkConnection getNetworkConnection() {
/* 200 */     return this.networkConnection;
/*     */   }
/*     */   
/*     */   public NetworkConnection.Event getNetworkConnectionStatus() {
/* 204 */     return this.networkConnectionStatus;
/*     */   }
/*     */   
/*     */   public RobotStatus getRobotStatus() {
/* 208 */     return this.robotStatus;
/*     */   }
/*     */   
/*     */   public Robot getRobot() {
/* 212 */     return this.robot;
/*     */   }
/*     */   
/*     */   public void onCreate() {
/* 216 */     super.onCreate();
/* 217 */     RobotLog.vv("FTCService", "onCreate()");
/*     */   }
/*     */   
/*     */   public int onStartCommand(Intent intent, int flags, int startId) {
/* 221 */     RobotLog.vv("FTCService", "onStartCommand()");
/* 222 */     return super.onStartCommand(intent, flags, startId);
/*     */   }
/*     */   
/*     */   public IBinder onBind(Intent intent)
/*     */   {
/* 227 */     RobotLog.vv("FTCService", "onBind()");
/* 228 */     RobotLog.vv("FTCService", "Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);
/*     */     
/* 230 */     NetworkType networkType = (NetworkType)intent.getSerializableExtra("NETWORK_CONNECTION_TYPE");
/* 231 */     this.networkConnection = NetworkConnectionFactory.getNetworkConnection(networkType, getBaseContext());
/* 232 */     this.networkConnection.setCallback(this);
/*     */     
/* 234 */     this.networkConnection.enable();
/* 235 */     if (Build.MODEL.equals("FL7007"))
/*     */     {
/* 237 */       this.networkConnection.discoverPotentialConnections();
/*     */     } else {
/* 239 */       WifiManager wifiManager = (WifiManager)getBaseContext().getSystemService("wifi");
/* 240 */       this.networkConnection.createConnection();
/*     */     }
/*     */     
/* 243 */     return this.binder;
/*     */   }
/*     */   
/*     */   public boolean onUnbind(Intent intent)
/*     */   {
/* 248 */     RobotLog.vv("FTCService", "onUnbind()");
/*     */     
/* 250 */     this.networkConnection.disable();
/* 251 */     shutdownRobot();
/*     */     
/* 253 */     if (this.eventLoopManager != null) {
/* 254 */       this.eventLoopManager.close();
/* 255 */       this.eventLoopManager = null;
/*     */     }
/*     */     
/* 258 */     return false;
/*     */   }
/*     */   
/*     */   public void onDestroy() {
/* 262 */     super.onDestroy();
/* 263 */     RobotLog.vv("FTCService", "onDestroy()");
/*     */   }
/*     */   
/*     */   public synchronized void setCallback(UpdateUI.Callback callback) {
/* 267 */     this.callback = callback;
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
/*     */   public synchronized void setupRobot(EventLoop eventLoop, EventLoop idleEventLoop)
/*     */   {
/* 280 */     stopRobotSetupThread();
/*     */     
/* 282 */     RobotLog.clearGlobalErrorMsg();
/* 283 */     RobotLog.clearGlobalWarningMsg();
/* 284 */     RobotLog.vv("FTCService", "Processing robot setup");
/*     */     
/* 286 */     this.eventLoop = eventLoop;
/* 287 */     this.idleEventLoop = idleEventLoop;
/*     */     
/*     */ 
/* 290 */     this.robotSetupExecutor = ThreadPool.newSingleThreadExecutor();
/* 291 */     this.robotSetupExecutor.execute(new RobotSetupRunnable(null));
/*     */     
/* 293 */     this.robotSetupExecutor.shutdown();
/*     */   }
/*     */   
/*     */   void stopRobotSetupThread() {
/* 297 */     if (this.robotSetupExecutor != null) {
/* 298 */       this.robotSetupExecutor.shutdownNow();
/* 299 */       ThreadPool.awaitTerminationOrExitApplication(this.robotSetupExecutor, 60L, TimeUnit.SECONDS, "robot setup", "internal error");
/* 300 */       this.robotSetupExecutor = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void shutdownRobot()
/*     */   {
/* 306 */     stopRobotSetupThread();
/*     */     
/*     */ 
/* 309 */     if (this.robot != null) this.robot.shutdown();
/* 310 */     this.robot = null;
/* 311 */     updateRobotStatus(RobotStatus.NONE);
/*     */   }
/*     */   
/*     */   public CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event)
/*     */   {
/* 316 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/* 317 */     switch (event) {
/*     */     case CONNECTED_AS_GROUP_OWNER: 
/* 319 */       RobotLog.ii("FTCService", "Wifi Direct - connected as group owner");
/* 320 */       this.networkConnection.cancelPotentialConnections();
/* 321 */       if (!NetworkConnection.isDeviceNameValid(this.networkConnection.getDeviceName())) {
/* 322 */         RobotLog.ee("FTCService", "Network Connection device name contains non-printable characters");
/* 323 */         ConfigWifiDirectActivity.launch(getBaseContext(), ConfigWifiDirectActivity.Flag.WIFI_DIRECT_DEVICE_NAME_INVALID);
/* 324 */         result = CallbackResult.HANDLED;
/*     */       }
/*     */       break;
/*     */     case CONNECTED_AS_PEER: 
/* 328 */       RobotLog.ee("FTCService", "Wifi Direct - connected as peer, was expecting Group Owner");
/* 329 */       ConfigWifiDirectActivity.launch(getBaseContext(), ConfigWifiDirectActivity.Flag.WIFI_DIRECT_FIX_CONFIG);
/* 330 */       result = CallbackResult.HANDLED;
/* 331 */       break;
/*     */     case CONNECTION_INFO_AVAILABLE: 
/* 333 */       RobotLog.ii("FTCService", "Network Connection Passphrase: " + this.networkConnection.getPassphrase());
/* 334 */       break;
/*     */     case ERROR: 
/* 336 */       RobotLog.ee("FTCService", "Network Connection Error: " + this.networkConnection.getFailureReason());
/* 337 */       break;
/*     */     case AP_CREATED: 
/* 339 */       RobotLog.ii("FTCService", "Network Connection created: " + this.networkConnection.getConnectionOwnerName());
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 344 */     updateNetworkConnectionStatus(event);
/* 345 */     return result;
/*     */   }
/*     */   
/*     */   private void updateNetworkConnectionStatus(NetworkConnection.Event event) {
/* 349 */     this.networkConnectionStatus = event;
/* 350 */     if (this.callback != null) this.callback.networkConnectionUpdate(this.networkConnectionStatus);
/*     */   }
/*     */   
/*     */   private void updateRobotStatus(@NonNull RobotStatus status) {
/* 354 */     this.robotStatus = status;
/* 355 */     if (this.callback != null) {
/* 356 */       this.callback.updateRobotStatus(status);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\FtcRobotControllerService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */