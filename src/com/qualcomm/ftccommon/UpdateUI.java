/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ import com.qualcomm.robotcore.robot.RobotStatus;
/*     */ import com.qualcomm.robotcore.util.Dimmer;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.Event;
/*     */ import org.firstinspires.ftc.ftccommon.external.RobotStateMonitor;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkStatus;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpdateUI
/*     */ {
/*     */   public static final boolean DEBUG = false;
/*     */   private static final int NUM_GAMEPADS = 2;
/*     */   protected TextView textDeviceName;
/*     */   protected TextView textNetworkConnectionStatus;
/*     */   protected TextView textRobotStatus;
/*     */   
/*     */   public class Callback
/*     */   {
/*     */     public Callback() {}
/*     */     
/*  61 */     RobotStateMonitor stateMonitor = null;
/*     */     
/*     */     public RobotStateMonitor getStateMonitor() {
/*  64 */       return this.stateMonitor;
/*     */     }
/*     */     
/*     */     public void setStateMonitor(RobotStateMonitor stateMonitor) {
/*  68 */       this.stateMonitor = stateMonitor;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void restartRobot()
/*     */     {
/*  75 */       AppUtil.getInstance().showToast(UpdateUI.this.activity.getString(R.string.toastRestartingRobot));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  80 */       Thread t = new Thread(new Runnable() {
/*  81 */         public void run() { ThreadPool.logThreadLifeCycle("restart robot UI thunker", new Runnable() {
/*     */             public void run() {
/*     */               try {
/*  84 */                 Thread.sleep(1500L);
/*     */               }
/*     */               catch (InterruptedException localInterruptedException) {}
/*     */               
/*  88 */               RobotLog.v("UpdateUI restarting robot..");
/*  89 */               UpdateUI.this.activity.runOnUiThread(new Runnable() {
/*  90 */                 public void run() { UpdateUI.this.requestRobotRestart(); }
/*     */               });
/*     */             }
/*     */           }); } });
/*  94 */       t.start();
/*     */     }
/*     */     
/*     */     public void updateUi(final String opModeName, final Gamepad[] gamepads) {
/*  98 */       UpdateUI.this.activity.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/* 101 */           if (UpdateUI.this.textGamepad != null) {
/* 102 */             for (int i = 0; (i < UpdateUI.this.textGamepad.length) && (i < gamepads.length); i++) {
/* 103 */               if (gamepads[i].id == -1) {
/* 104 */                 UpdateUI.this.setText(UpdateUI.this.textGamepad[i], "");
/*     */               } else {
/* 106 */                 UpdateUI.this.setText(UpdateUI.this.textGamepad[i], gamepads[i].toString());
/*     */               }
/*     */             }
/*     */           }
/*     */           String opModeShow;
/*     */           String opModeShow;
/* 112 */           if (opModeName.equals("$Stop$Robot$")) {
/* 113 */             opModeShow = UpdateUI.this.activity.getString(R.string.defaultOpModeName);
/*     */           } else {
/* 115 */             opModeShow = opModeName;
/*     */           }
/* 117 */           UpdateUI.this.setText(UpdateUI.this.textOpMode, "Op Mode: " + opModeShow);
/*     */           
/* 119 */           UpdateUI.Callback.this.refreshTextErrorMessage();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public void networkConnectionUpdate(NetworkConnection.Event event)
/*     */     {
/* 126 */       switch (UpdateUI.1.$SwitchMap$com$qualcomm$robotcore$wifi$NetworkConnection$Event[event.ordinal()]) {
/*     */       case 1: 
/* 128 */         updateNetworkConnectionStatus(NetworkStatus.UNKNOWN);
/* 129 */         break;
/*     */       case 2: 
/* 131 */         updateNetworkConnectionStatus(NetworkStatus.INACTIVE);
/* 132 */         break;
/*     */       case 3: 
/* 134 */         updateNetworkConnectionStatus(NetworkStatus.ENABLED);
/* 135 */         break;
/*     */       case 4: 
/* 137 */         updateNetworkConnectionStatus(NetworkStatus.ERROR);
/* 138 */         break;
/*     */       case 5: 
/* 140 */         NetworkConnection networkConnection = UpdateUI.this.controllerService.getNetworkConnection();
/* 141 */         displayDeviceName(networkConnection.getDeviceName());
/* 142 */         updateNetworkConnectionStatus(NetworkStatus.ACTIVE);
/* 143 */         break;
/*     */       case 6: 
/* 145 */         NetworkConnection networkConnection = UpdateUI.this.controllerService.getNetworkConnection();
/* 146 */         updateNetworkConnectionStatus(NetworkStatus.CREATED_AP_CONNECTION, networkConnection.getConnectionOwnerName());
/* 147 */         break;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */     protected void displayDeviceName(final String name)
/*     */     {
/* 154 */       UpdateUI.this.activity.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/* 157 */           UpdateUI.this.textDeviceName.setText(name);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public void updateNetworkConnectionStatus(NetworkStatus networkStatus) {
/* 163 */       if (UpdateUI.this.networkStatus != networkStatus) {
/* 164 */         UpdateUI.this.networkStatus = networkStatus;
/* 165 */         UpdateUI.this.networkStatusExtra = null;
/* 166 */         if (this.stateMonitor != null) this.stateMonitor.updateNetworkStatus(networkStatus, null);
/* 167 */         refreshNetworkStatus();
/*     */       }
/*     */     }
/*     */     
/*     */     public void updateNetworkConnectionStatus(NetworkStatus networkStatus, @NonNull String extra) {
/* 172 */       if ((UpdateUI.this.networkStatus != networkStatus) || (!extra.equals(UpdateUI.this.networkStatusExtra))) {
/* 173 */         UpdateUI.this.networkStatus = networkStatus;
/* 174 */         UpdateUI.this.networkStatusExtra = extra;
/* 175 */         if (this.stateMonitor != null) this.stateMonitor.updateNetworkStatus(networkStatus, extra);
/* 176 */         refreshNetworkStatus();
/*     */       }
/*     */     }
/*     */     
/*     */     public void updatePeerStatus(PeerStatus peerStatus) {
/* 181 */       if (UpdateUI.this.peerStatus != peerStatus) {
/* 182 */         UpdateUI.this.peerStatus = peerStatus;
/* 183 */         if (this.stateMonitor != null) this.stateMonitor.updatePeerStatus(peerStatus);
/* 184 */         refreshNetworkStatus();
/*     */       }
/*     */     }
/*     */     
/*     */     void refreshNetworkStatus() {
/* 189 */       String format = UpdateUI.this.activity.getString(R.string.networkStatusFormat);
/* 190 */       String strNetworkStatus = UpdateUI.this.networkStatus.toString(UpdateUI.this.activity, new Object[] { UpdateUI.this.networkStatusExtra });
/* 191 */       String strPeerStatus = UpdateUI.this.peerStatus == PeerStatus.UNKNOWN ? "" : String.format(", %s", new Object[] { UpdateUI.this.peerStatus.toString(UpdateUI.this.activity) });
/* 192 */       final String message = String.format(format, new Object[] { strNetworkStatus, strPeerStatus });
/*     */       
/*     */ 
/* 195 */       if (!message.equals(UpdateUI.this.networkStatusMessage)) RobotLog.v(message);
/* 196 */       UpdateUI.this.networkStatusMessage = message;
/*     */       
/* 198 */       UpdateUI.this.activity.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/* 201 */           UpdateUI.this.setText(UpdateUI.this.textNetworkConnectionStatus, message);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public void updateRobotStatus(@NonNull RobotStatus status) {
/* 207 */       UpdateUI.this.robotStatus = status;
/* 208 */       if (this.stateMonitor != null) this.stateMonitor.updateRobotStatus(UpdateUI.this.robotStatus);
/* 209 */       refreshStateStatus();
/*     */     }
/*     */     
/*     */     public void updateRobotState(@NonNull RobotState state) {
/* 213 */       UpdateUI.this.robotState = state;
/* 214 */       if (this.stateMonitor != null) this.stateMonitor.updateRobotState(UpdateUI.this.robotState);
/* 215 */       refreshStateStatus();
/*     */     }
/*     */     
/*     */     protected void refreshStateStatus() {
/* 219 */       String format = UpdateUI.this.activity.getString(R.string.robotStatusFormat);
/* 220 */       String state = UpdateUI.this.robotState.toString(UpdateUI.this.activity);
/* 221 */       String status = UpdateUI.this.robotStatus == RobotStatus.NONE ? "" : String.format(", %s", new Object[] { UpdateUI.this.robotStatus.toString(UpdateUI.this.activity) });
/* 222 */       final String message = String.format(format, new Object[] { state, status });
/*     */       
/*     */ 
/* 225 */       if (!message.equals(UpdateUI.this.stateStatusMessage)) RobotLog.v(message);
/* 226 */       UpdateUI.this.stateStatusMessage = message;
/*     */       
/* 228 */       UpdateUI.this.activity.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/* 231 */           UpdateUI.this.setText(UpdateUI.this.textRobotStatus, message);
/* 232 */           UpdateUI.Callback.this.refreshTextErrorMessage();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public void refreshErrorTextOnUiThread() {
/* 238 */       UpdateUI.this.activity.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/* 241 */           UpdateUI.Callback.this.refreshTextErrorMessage();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     void refreshTextErrorMessage()
/*     */     {
/* 248 */       String errorMessage = RobotLog.getGlobalErrorMsg();
/* 249 */       String warningMessage = RobotLog.getGlobalWarningMessage();
/*     */       
/* 251 */       if ((!errorMessage.isEmpty()) || (!warningMessage.isEmpty())) {
/* 252 */         if (!errorMessage.isEmpty()) {
/* 253 */           String message = UpdateUI.this.activity.getString(R.string.error_text_error, new Object[] { trimTextErrorMessage(errorMessage) });
/* 254 */           UpdateUI.this.setText(UpdateUI.this.textErrorMessage, message);
/* 255 */           if (this.stateMonitor != null) this.stateMonitor.updateErrorMessage(message);
/*     */         } else {
/* 257 */           String message = UpdateUI.this.activity.getString(R.string.error_text_warning, new Object[] { trimTextErrorMessage(warningMessage) });
/* 258 */           UpdateUI.this.setText(UpdateUI.this.textErrorMessage, message);
/* 259 */           if (this.stateMonitor != null) this.stateMonitor.updateWarningMessage(message);
/*     */         }
/* 261 */         UpdateUI.this.dimmer.longBright();
/*     */       } else {
/* 263 */         UpdateUI.this.setText(UpdateUI.this.textErrorMessage, "");
/* 264 */         if (this.stateMonitor != null) {
/* 265 */           this.stateMonitor.updateErrorMessage(null);
/* 266 */           this.stateMonitor.updateWarningMessage(null);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     String trimTextErrorMessage(String message)
/*     */     {
/* 273 */       return message;
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
/* 284 */   protected TextView[] textGamepad = new TextView[2];
/*     */   protected TextView textOpMode;
/*     */   protected TextView textErrorMessage;
/* 287 */   protected RobotState robotState = RobotState.NOT_STARTED;
/* 288 */   protected RobotStatus robotStatus = RobotStatus.NONE;
/* 289 */   protected NetworkStatus networkStatus = NetworkStatus.UNKNOWN;
/* 290 */   protected String networkStatusExtra = null;
/* 291 */   protected PeerStatus peerStatus = PeerStatus.DISCONNECTED;
/* 292 */   protected String networkStatusMessage = null;
/* 293 */   protected String stateStatusMessage = null;
/*     */   
/*     */   Restarter restarter;
/*     */   FtcRobotControllerService controllerService;
/*     */   Activity activity;
/*     */   Dimmer dimmer;
/*     */   
/*     */   public UpdateUI(Activity activity, Dimmer dimmer)
/*     */   {
/* 302 */     this.activity = activity;
/* 303 */     this.dimmer = dimmer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTextViews(TextView textWifiDirectStatus, TextView textRobotStatus, TextView[] textGamepad, TextView textOpMode, TextView textErrorMessage, TextView textDeviceName)
/*     */   {
/* 310 */     this.textNetworkConnectionStatus = textWifiDirectStatus;
/* 311 */     this.textRobotStatus = textRobotStatus;
/* 312 */     this.textGamepad = textGamepad;
/* 313 */     this.textOpMode = textOpMode;
/* 314 */     this.textErrorMessage = textErrorMessage;
/* 315 */     this.textDeviceName = textDeviceName;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void setText(TextView textView, String message)
/*     */   {
/* 321 */     if ((textView != null) && (message != null)) {
/* 322 */       message = message.trim();
/* 323 */       if (message.length() > 0) {
/* 324 */         textView.setText(message);
/* 325 */         textView.setVisibility(0);
/*     */       } else {
/* 327 */         textView.setVisibility(4);
/* 328 */         textView.setText(" ");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setControllerService(FtcRobotControllerService controllerService) {
/* 334 */     this.controllerService = controllerService;
/*     */   }
/*     */   
/*     */   public void setRestarter(Restarter restarter) {
/* 338 */     this.restarter = restarter;
/*     */   }
/*     */   
/*     */   private void requestRobotRestart() {
/* 342 */     this.restarter.requestRestart();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\UpdateUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */