/*     */ package org.firstinspires.ftc.robotcore.internal.network;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.SharedPreferences;
/*     */ import android.net.wifi.ScanResult;
/*     */ import android.net.wifi.WifiManager;
/*     */ import android.net.wifi.WifiManager.WifiLock;
/*     */ import android.net.wifi.p2p.WifiP2pDevice;
/*     */ import android.preference.PreferenceManager;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.PeerDiscovery;
/*     */ import com.qualcomm.robotcore.robocol.RobocolDatagram;
/*     */ import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.ConnectStatus;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.Event;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.NetworkConnectionCallback;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
/*     */ import com.qualcomm.robotcore.wifi.NetworkType;
/*     */ import com.qualcomm.robotcore.wifi.SoftApAssistant;
/*     */ import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NetworkConnectionHandler
/*     */ {
/*     */   public static final String TAG = "NetworkConnectionHandler";
/*     */   public static final boolean DEBUG = false;
/*  75 */   private static final NetworkConnectionHandler theInstance = new NetworkConnectionHandler();
/*     */   
/*     */ 
/*  78 */   public static NetworkConnectionHandler getInstance() { return theInstance; }
/*     */   
/*     */   @Nullable
/*     */   protected WifiManager.WifiLock wifiLock;
/*     */   protected boolean setupNeeded;
/*     */   protected Context context;
/*     */   protected ElapsedTime lastRecvPacket;
/*     */   
/*  86 */   public NetworkConnectionHandler() { this.setupNeeded = true;
/*     */     
/*     */ 
/*  89 */     this.lastRecvPacket = new ElapsedTime();
/*     */     
/*     */ 
/*  92 */     this.sendLoopService = Executors.newSingleThreadScheduledExecutor();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */     this.networkConnection = null;
/* 100 */     this.theNetworkConnectionCallback = new NetworkConnectionCallbackChainer();
/*     */     
/* 102 */     this.theRecvLoopCallback = new RecvLoopCallbackChainer();
/* 103 */     this.callbackLock = new Object();
/*     */   }
/*     */   
/*     */   protected InetAddress remoteAddr;
/*     */   protected RobocolDatagramSocket socket;
/*     */   protected ScheduledExecutorService sendLoopService;
/*     */   protected ScheduledFuture<?> sendLoopFuture;
/* 110 */   public static WifiManager.WifiLock newWifiLock(Context context) { WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
/* 111 */     return wifiManager.createWifiLock(3, "");
/*     */   }
/*     */   
/*     */   public NetworkType getDefaultNetworkType(Context context) {
/* 115 */     SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
/* 116 */     return NetworkType.fromString(preferences.getString("NETWORK_CONNECTION_TYPE", NetworkType.WIFIDIRECT.toString()));
/*     */   }
/*     */   
/*     */   public void init(@NonNull WifiManager.WifiLock wifiLock, @NonNull NetworkType networkType, @NonNull String owner, @NonNull String password, @NonNull Context context)
/*     */   {
/* 121 */     this.wifiLock = wifiLock;
/* 122 */     this.connectionOwner = owner;
/* 123 */     this.connectionOwnerPassword = password;
/* 124 */     this.context = context;
/*     */     
/* 126 */     shutdown();
/* 127 */     this.networkConnection = null;
/* 128 */     initNetworkConnection(networkType);
/*     */     
/* 130 */     startWifiAndDiscoverConnections();
/*     */   }
/*     */   
/*     */   public void init(@NonNull NetworkType networkType, @NonNull Context context)
/*     */   {
/* 135 */     this.context = context;
/* 136 */     initNetworkConnection(networkType);
/*     */   }
/*     */   
/*     */   private void initNetworkConnection(NetworkType networkType) {
/* 140 */     if ((this.networkConnection != null) && (this.networkConnection.getNetworkType() != networkType))
/*     */     {
/* 142 */       shutdown();
/* 143 */       this.networkConnection = null;
/*     */     }
/* 145 */     if (this.networkConnection == null) {
/* 146 */       this.networkConnection = NetworkConnectionFactory.getNetworkConnection(networkType, this.context);
/* 147 */       synchronized (this.callbackLock) {
/* 148 */         this.networkConnection.setCallback(this.theNetworkConnectionCallback);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setRecvLoopRunnable(RecvLoopRunnable recvLoopRunnable) {
/* 154 */     synchronized (this.callbackLock) {
/* 155 */       this.recvLoopRunnable = recvLoopRunnable;
/* 156 */       this.recvLoopRunnable.setCallback(this.theRecvLoopCallback);
/*     */     }
/*     */   }
/*     */   
/*     */   public NetworkType getNetworkType() {
/* 161 */     return this.networkConnection.getNetworkType();
/*     */   }
/*     */   
/*     */   public void startWifiAndDiscoverConnections() {
/* 165 */     acquireWifiLock();
/* 166 */     this.networkConnection.enable();
/* 167 */     if (!this.networkConnection.isConnected()) this.networkConnection.discoverPotentialConnections();
/*     */   }
/*     */   
/*     */   public void startConnection(@NonNull String owner, @NonNull String password) {
/* 171 */     this.connectionOwner = owner;
/* 172 */     this.connectionOwnerPassword = password;
/* 173 */     this.networkConnection.connect(this.connectionOwner, this.connectionOwnerPassword);
/*     */   }
/*     */   
/*     */   public boolean connectedWithUnexpectedDevice() {
/* 177 */     if ((this.connectionOwner != null) && 
/* 178 */       (!this.connectionOwner.equals(this.networkConnection.getConnectionOwnerMacAddress()))) {
/* 179 */       RobotLog.ee("NetworkConnectionHandler", "Network Connection - connected to " + this.networkConnection.getConnectionOwnerMacAddress() + ", expected " + this.connectionOwner);
/* 180 */       return true;
/*     */     }
/*     */     
/* 183 */     return false;
/*     */   }
/*     */   
/*     */   public void acquireWifiLock() {
/* 187 */     if (this.wifiLock != null) this.wifiLock.acquire();
/*     */   }
/*     */   
/*     */   public boolean isNetworkConnected() {
/* 191 */     return this.networkConnection.isConnected();
/*     */   }
/*     */   
/*     */   public boolean isWifiDirect() {
/* 195 */     return this.networkConnection.getNetworkType().equals(NetworkType.WIFIDIRECT);
/*     */   }
/*     */   
/*     */   public void discoverPotentialConnections() {
/* 199 */     this.networkConnection.discoverPotentialConnections();
/*     */   }
/*     */   
/*     */   public void cancelConnectionSearch() {
/* 203 */     this.networkConnection.cancelPotentialConnections();
/*     */   }
/*     */   
/*     */   public String getFailureReason() {
/* 207 */     return this.networkConnection.getFailureReason();
/*     */   }
/*     */   
/*     */   public String getConnectionOwnerName() {
/* 211 */     return this.networkConnection.getConnectionOwnerName();
/*     */   }
/*     */   
/*     */   public String getDeviceName() {
/* 215 */     return this.networkConnection.getDeviceName();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 219 */     this.networkConnection.disable();
/* 220 */     if ((this.wifiLock != null) && (this.wifiLock.isHeld())) this.wifiLock.release();
/*     */   }
/*     */   
/*     */   public boolean connectingOrConnected() {
/* 224 */     NetworkConnection.ConnectStatus status = this.networkConnection.getConnectStatus();
/* 225 */     return (status == NetworkConnection.ConnectStatus.CONNECTED) || (status == NetworkConnection.ConnectStatus.CONNECTING);
/*     */   }
/*     */   
/*     */   public boolean connectionMatches(String name)
/*     */   {
/* 230 */     return (this.connectionOwner != null) && (this.connectionOwner.equals(name));
/*     */   }
/*     */   
/*     */   public synchronized CallbackResult handleConnectionInfoAvailable(SocketConnect socketConnect) {
/* 234 */     CallbackResult result = CallbackResult.HANDLED;
/* 235 */     if ((this.networkConnection.isConnected()) && (this.setupNeeded)) {
/* 236 */       this.setupNeeded = false;
/*     */       
/* 238 */       if (this.networkConnection.getNetworkType() == NetworkType.SOFTAP) {
/*     */         try {
/* 240 */           Thread.sleep(2000L);
/*     */         } catch (InterruptedException e) {
/* 242 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       }
/* 245 */       synchronized (this.callbackLock) {
/* 246 */         this.setupRunnable = new SetupRunnable(this.theRecvLoopCallback, this.networkConnection, this.lastRecvPacket, socketConnect);
/*     */       }
/* 248 */       new Thread(this.setupRunnable).start();
/*     */     }
/* 250 */     return result;
/*     */   }
/*     */   
/*     */   public synchronized CallbackResult handlePeersAvailable() {
/* 254 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/* 255 */     NetworkType networkType = this.networkConnection.getNetworkType();
/* 256 */     switch (networkType) {
/*     */     case WIFIDIRECT: 
/* 258 */       result = handleWifiDirectPeersAvailable();
/* 259 */       break;
/*     */     case SOFTAP: 
/* 261 */       result = handleSoftAPPeersAvailable();
/* 262 */       break;
/*     */     case LOOPBACK: 
/*     */     case UNKNOWN_NETWORK_TYPE: 
/* 265 */       RobotLog.e("Unhandled peers available event: " + networkType.toString());
/*     */     }
/*     */     
/* 268 */     return result;
/*     */   }
/*     */   
/*     */   private CallbackResult handleSoftAPPeersAvailable() {
/* 272 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/*     */     
/* 274 */     List<ScanResult> scanResults = ((SoftApAssistant)this.networkConnection).getScanResults();
/* 275 */     for (ScanResult scanResult : scanResults) {
/* 276 */       RobotLog.v(scanResult.SSID);
/* 277 */       if (scanResult.SSID.equalsIgnoreCase(this.connectionOwner))
/*     */       {
/* 279 */         this.networkConnection.connect(this.connectionOwner, this.connectionOwnerPassword);
/* 280 */         result = CallbackResult.HANDLED;
/* 281 */         break;
/*     */       }
/*     */     }
/* 284 */     return result;
/*     */   }
/*     */   
/*     */   private CallbackResult handleWifiDirectPeersAvailable() {
/* 288 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/* 289 */     List<WifiP2pDevice> peers = ((WifiDirectAssistant)this.networkConnection).getPeers();
/* 290 */     for (WifiP2pDevice peer : peers) {
/* 291 */       if (peer.deviceAddress.equalsIgnoreCase(this.connectionOwner))
/*     */       {
/* 293 */         this.networkConnection.connect(peer.deviceAddress);
/* 294 */         result = CallbackResult.HANDLED;
/* 295 */         break;
/*     */       }
/*     */     }
/* 298 */     return result; }
/*     */   
/*     */   protected SendOnceRunnable sendOnceRunnable;
/*     */   protected SetupRunnable setupRunnable;
/*     */   @Nullable
/*     */   protected String connectionOwner;
/*     */   @Nullable
/*     */   protected String connectionOwnerPassword;
/*     */   protected NetworkConnection networkConnection;
/*     */   protected final NetworkConnectionCallbackChainer theNetworkConnectionCallback;
/*     */   protected RecvLoopRunnable recvLoopRunnable;
/*     */   protected final RecvLoopCallbackChainer theRecvLoopCallback;
/*     */   protected final Object callbackLock;
/* 311 */   public synchronized void updateConnection(@NonNull RobocolDatagram packet, @Nullable SendOnceRunnable.Parameters parameters, SendOnceRunnable.ClientCallback clientCallback) throws RobotCoreException { if (packet.getAddress().equals(this.remoteAddr)) {
/* 312 */       if (clientCallback != null) {
/* 313 */         clientCallback.peerConnected(false);
/*     */       }
/* 315 */       return;
/*     */     }
/*     */     
/* 318 */     if (parameters == null) { parameters = new SendOnceRunnable.Parameters();
/*     */     }
/*     */     
/* 321 */     PeerDiscovery peerDiscovery = PeerDiscovery.forReceive();
/* 322 */     peerDiscovery.fromByteArray(packet.getData());
/*     */     
/*     */ 
/* 325 */     this.remoteAddr = packet.getAddress();
/* 326 */     RobotLog.vv("PeerDiscovery", "new remote peer discovered: " + this.remoteAddr.getHostAddress());
/*     */     
/* 328 */     if ((this.socket == null) && (this.setupRunnable != null)) {
/* 329 */       this.socket = this.setupRunnable.getSocket();
/*     */     }
/*     */     
/* 332 */     if (this.socket != null) {
/*     */       try {
/* 334 */         this.socket.connect(this.remoteAddr);
/*     */       } catch (SocketException e) {
/* 336 */         throw RobotCoreException.createChained(e, "unable to connect to %s", new Object[] { this.remoteAddr.toString() });
/*     */       }
/*     */       
/*     */ 
/* 340 */       if ((this.sendLoopFuture == null) || (this.sendLoopFuture.isDone())) {
/* 341 */         RobotLog.vv("NetworkConnectionHandler", "starting sending loop");
/* 342 */         this.sendOnceRunnable = new SendOnceRunnable(this.context, clientCallback, this.socket, this.lastRecvPacket, parameters);
/* 343 */         this.sendLoopFuture = this.sendLoopService.scheduleAtFixedRate(this.sendOnceRunnable, 0L, 40L, TimeUnit.MILLISECONDS);
/*     */       }
/*     */       
/* 346 */       if (clientCallback != null) {
/* 347 */         clientCallback.peerConnected(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized boolean removeCommand(Command cmd)
/*     */   {
/* 354 */     return (this.sendOnceRunnable != null) && (this.sendOnceRunnable.removeCommand(cmd));
/*     */   }
/*     */   
/*     */   public synchronized void sendCommand(Command cmd)
/*     */   {
/* 359 */     if (this.sendOnceRunnable != null) this.sendOnceRunnable.sendCommand(cmd);
/*     */   }
/*     */   
/*     */   public CallbackResult processAcknowledgments(Command command) throws RobotCoreException {
/* 363 */     if (command.isAcknowledged())
/*     */     {
/* 365 */       removeCommand(command);
/* 366 */       return CallbackResult.HANDLED;
/*     */     }
/*     */     
/* 369 */     command.acknowledge();
/* 370 */     sendCommand(command);
/* 371 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public synchronized void sendDatagram(RobocolDatagram datagram) {
/* 375 */     if ((this.socket != null) && (this.socket.getInetAddress() != null)) this.socket.send(datagram);
/*     */   }
/*     */   
/*     */   public synchronized void clientDisconnect() {
/* 379 */     if (this.sendOnceRunnable != null) this.sendOnceRunnable.clearCommands();
/* 380 */     this.remoteAddr = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void shutdown()
/*     */   {
/* 386 */     if (this.setupRunnable != null) {
/* 387 */       this.setupRunnable.shutdown();
/* 388 */       this.setupRunnable = null;
/*     */     }
/*     */     
/* 391 */     if (this.sendLoopFuture != null) {
/* 392 */       this.sendLoopFuture.cancel(true);
/* 393 */       this.sendOnceRunnable = null;
/* 394 */       this.sendLoopFuture = null;
/*     */     }
/*     */     
/*     */ 
/* 398 */     if (this.socket != null) {
/* 399 */       this.socket.close();
/* 400 */       this.socket = null;
/*     */     }
/*     */     
/*     */ 
/* 404 */     this.remoteAddr = null;
/*     */     
/*     */ 
/* 407 */     this.setupNeeded = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void pushNetworkConnectionCallback(@Nullable NetworkConnection.NetworkConnectionCallback callback)
/*     */   {
/* 418 */     synchronized (this.callbackLock) {
/* 419 */       this.theNetworkConnectionCallback.push(callback);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeNetworkConnectionCallback(@Nullable NetworkConnection.NetworkConnectionCallback callback) {
/* 424 */     synchronized (this.callbackLock) {
/* 425 */       this.theNetworkConnectionCallback.remove(callback);
/*     */     } }
/*     */   
/*     */   protected class NetworkConnectionCallbackChainer implements NetworkConnection.NetworkConnectionCallback { protected NetworkConnectionCallbackChainer() {}
/*     */     
/* 430 */     protected final Object lock = new Object();
/* 431 */     protected final LinkedList<NetworkConnection.NetworkConnectionCallback> callbacks = new LinkedList();
/*     */     
/*     */     void push(@Nullable NetworkConnection.NetworkConnectionCallback callback) {
/* 434 */       synchronized (this.lock) {
/* 435 */         remove(callback);
/* 436 */         if ((callback != null) && (!this.callbacks.contains(callback))) {
/* 437 */           this.callbacks.push(callback);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     void remove(@Nullable NetworkConnection.NetworkConnectionCallback callback) {
/* 443 */       synchronized (this.lock) {
/* 444 */         if (callback != null) this.callbacks.remove(callback);
/*     */       }
/*     */     }
/*     */     
/*     */     public CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event) {
/* 449 */       synchronized (this.lock) {
/* 450 */         for (NetworkConnection.NetworkConnectionCallback callback : this.callbacks) {
/* 451 */           CallbackResult result = callback.onNetworkConnectionEvent(event);
/* 452 */           if (result.stopDispatch()) {
/* 453 */             return CallbackResult.HANDLED;
/*     */           }
/*     */         }
/* 456 */         return CallbackResult.NOT_HANDLED;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void pushReceiveLoopCallback(@Nullable RecvLoopRunnable.RecvLoopCallback callback) {
/* 462 */     synchronized (this.callbackLock) {
/* 463 */       this.theRecvLoopCallback.push(callback);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeReceiveLoopCallback(@Nullable RecvLoopRunnable.RecvLoopCallback callback) {
/* 468 */     synchronized (this.callbackLock) {
/* 469 */       this.theRecvLoopCallback.remove(callback);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RecvLoopCallbackChainer implements RecvLoopRunnable.RecvLoopCallback { protected RecvLoopCallbackChainer() {}
/*     */     
/* 475 */     protected final Object lock = new Object();
/* 476 */     protected final LinkedList<RecvLoopRunnable.RecvLoopCallback> callbacks = new LinkedList();
/*     */     
/*     */     void push(@Nullable RecvLoopRunnable.RecvLoopCallback callback) {
/* 479 */       synchronized (this.lock) {
/* 480 */         remove(callback);
/* 481 */         if ((callback != null) && (!this.callbacks.contains(callback))) {
/* 482 */           this.callbacks.push(callback);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     void remove(@Nullable RecvLoopRunnable.RecvLoopCallback callback) {
/* 488 */       synchronized (this.lock) {
/* 489 */         if (callback != null) this.callbacks.remove(callback);
/*     */       }
/*     */     }
/*     */     
/*     */     public CallbackResult packetReceived(RobocolDatagram packet) throws RobotCoreException {
/* 494 */       synchronized (this.lock) {
/* 495 */         for (RecvLoopRunnable.RecvLoopCallback callback : this.callbacks) {
/* 496 */           CallbackResult result = callback.packetReceived(packet);
/* 497 */           if (result.stopDispatch()) {
/* 498 */             return CallbackResult.HANDLED;
/*     */           }
/*     */         }
/* 501 */         return CallbackResult.NOT_HANDLED;
/*     */       }
/*     */     }
/*     */     
/*     */     public CallbackResult peerDiscoveryEvent(RobocolDatagram packet) throws RobotCoreException {
/* 506 */       synchronized (this.lock) {
/* 507 */         for (RecvLoopRunnable.RecvLoopCallback callback : this.callbacks) {
/* 508 */           CallbackResult result = callback.peerDiscoveryEvent(packet);
/* 509 */           if (result.stopDispatch()) {
/* 510 */             return CallbackResult.HANDLED;
/*     */           }
/*     */         }
/* 513 */         return CallbackResult.NOT_HANDLED;
/*     */       }
/*     */     }
/*     */     
/*     */     public CallbackResult heartbeatEvent(RobocolDatagram packet, long tReceived) throws RobotCoreException {
/* 518 */       synchronized (this.lock) {
/* 519 */         for (RecvLoopRunnable.RecvLoopCallback callback : this.callbacks) {
/* 520 */           CallbackResult result = callback.heartbeatEvent(packet, tReceived);
/* 521 */           if (result.stopDispatch()) {
/* 522 */             return CallbackResult.HANDLED;
/*     */           }
/*     */         }
/* 525 */         return CallbackResult.NOT_HANDLED;
/*     */       }
/*     */     }
/*     */     
/*     */     public CallbackResult commandEvent(Command command) throws RobotCoreException
/*     */     {
/* 531 */       synchronized (this.lock) {
/* 532 */         boolean handled = false;
/* 533 */         for (RecvLoopRunnable.RecvLoopCallback callback : this.callbacks) {
/* 534 */           CallbackResult result = callback.commandEvent(command);
/* 535 */           handled = (handled) || (result.isHandled());
/* 536 */           if (result.stopDispatch()) {
/* 537 */             return CallbackResult.HANDLED;
/*     */           }
/*     */         }
/*     */         
/* 541 */         if (!handled)
/*     */         {
/* 543 */           StringBuilder callbackNames = new StringBuilder();
/* 544 */           for (RecvLoopRunnable.RecvLoopCallback callback : this.callbacks) {
/* 545 */             if (callbackNames.length() > 0) callbackNames.append(",");
/* 546 */             callbackNames.append(callback.getClass().getSimpleName());
/*     */           }
/* 548 */           RobotLog.vv("Robocol", "unable to process command %s callbacks=%s", new Object[] { command.getName(), callbackNames.toString() });
/*     */         }
/* 550 */         return handled ? CallbackResult.HANDLED : CallbackResult.NOT_HANDLED;
/*     */       }
/*     */     }
/*     */     
/*     */     public CallbackResult telemetryEvent(RobocolDatagram packet) throws RobotCoreException {
/* 555 */       synchronized (this.lock) {
/* 556 */         for (RecvLoopRunnable.RecvLoopCallback callback : this.callbacks) {
/* 557 */           CallbackResult result = callback.telemetryEvent(packet);
/* 558 */           if (result.stopDispatch()) {
/* 559 */             return CallbackResult.HANDLED;
/*     */           }
/*     */         }
/* 562 */         return CallbackResult.NOT_HANDLED;
/*     */       }
/*     */     }
/*     */     
/*     */     public CallbackResult gamepadEvent(RobocolDatagram packet) throws RobotCoreException {
/* 567 */       synchronized (this.lock) {
/* 568 */         for (RecvLoopRunnable.RecvLoopCallback callback : this.callbacks) {
/* 569 */           CallbackResult result = callback.gamepadEvent(packet);
/* 570 */           if (result.stopDispatch()) {
/* 571 */             return CallbackResult.HANDLED;
/*     */           }
/*     */         }
/* 574 */         return CallbackResult.NOT_HANDLED;
/*     */       }
/*     */     }
/*     */     
/*     */     public CallbackResult emptyEvent(RobocolDatagram packet) throws RobotCoreException {
/* 579 */       synchronized (this.lock) {
/* 580 */         for (RecvLoopRunnable.RecvLoopCallback callback : this.callbacks) {
/* 581 */           CallbackResult result = callback.emptyEvent(packet);
/* 582 */           if (result.stopDispatch()) {
/* 583 */             return CallbackResult.HANDLED;
/*     */           }
/*     */         }
/* 586 */         return CallbackResult.NOT_HANDLED;
/*     */       }
/*     */     }
/*     */     
/*     */     public CallbackResult reportGlobalError(String error, boolean recoverable) {
/* 591 */       synchronized (this.lock) {
/* 592 */         for (RecvLoopRunnable.RecvLoopCallback callback : this.callbacks) {
/* 593 */           CallbackResult result = callback.reportGlobalError(error, recoverable);
/* 594 */           if (result.stopDispatch()) {
/* 595 */             return CallbackResult.HANDLED;
/*     */           }
/*     */         }
/* 598 */         return CallbackResult.NOT_HANDLED;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\network\NetworkConnectionHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */