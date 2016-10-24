/*     */ package com.qualcomm.robotcore.wifi;
/*     */ 
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.net.NetworkInfo;
/*     */ import android.net.wifi.p2p.WifiP2pConfig;
/*     */ import android.net.wifi.p2p.WifiP2pDevice;
/*     */ import android.net.wifi.p2p.WifiP2pDeviceList;
/*     */ import android.net.wifi.p2p.WifiP2pGroup;
/*     */ import android.net.wifi.p2p.WifiP2pInfo;
/*     */ import android.net.wifi.p2p.WifiP2pManager;
/*     */ import android.net.wifi.p2p.WifiP2pManager.ActionListener;
/*     */ import android.net.wifi.p2p.WifiP2pManager.Channel;
/*     */ import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
/*     */ import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
/*     */ import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
/*     */ import android.os.Looper;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.net.InetAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import junit.framework.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WifiDirectAssistant
/*     */   extends NetworkConnection
/*     */ {
/*     */   public static final String TAG = "WifiDirect";
/*  63 */   private static WifiDirectAssistant wifiDirectAssistant = null;
/*     */   
/*  65 */   private final List<WifiP2pDevice> peers = new ArrayList();
/*     */   
/*     */   private final IntentFilter intentFilter;
/*     */   
/*     */   private final WifiP2pManager.Channel wifiP2pChannel;
/*     */   
/*     */   private final WifiP2pManager wifiP2pManager;
/*     */   private final WifiDirectConnectionInfoListener connectionListener;
/*     */   private final WifiDirectPeerListListener peerListListener;
/*     */   private final WifiDirectGroupInfoListener groupInfoListener;
/*  75 */   private Context context = null;
/*  76 */   private boolean isWifiP2pEnabled = false;
/*     */   private WifiP2pBroadcastReceiver receiver;
/*  78 */   private int failureReason = 0;
/*  79 */   private NetworkConnection.ConnectStatus connectStatus = NetworkConnection.ConnectStatus.NOT_CONNECTED;
/*  80 */   private NetworkConnection.Event lastEvent = null;
/*     */   
/*  82 */   private String deviceMacAddress = "";
/*  83 */   private String deviceName = "";
/*  84 */   private InetAddress groupOwnerAddress = null;
/*  85 */   private String groupOwnerMacAddress = "";
/*  86 */   private String groupOwnerName = "";
/*  87 */   private String groupInterface = "";
/*  88 */   private String groupNetworkName = "";
/*  89 */   private String passphrase = "";
/*  90 */   private boolean groupFormed = false;
/*     */   
/*     */ 
/*  93 */   private int clients = 0;
/*     */   
/*  95 */   private NetworkConnection.NetworkConnectionCallback callback = null;
/*     */   
/*     */ 
/*     */ 
/*     */   private class WifiDirectPeerListListener
/*     */     implements WifiP2pManager.PeerListListener
/*     */   {
/*     */     private WifiDirectPeerListListener() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void onPeersAvailable(WifiP2pDeviceList peerList)
/*     */     {
/* 108 */       WifiDirectAssistant.this.peers.clear();
/* 109 */       WifiDirectAssistant.this.peers.addAll(peerList.getDeviceList());
/*     */       
/* 111 */       RobotLog.vv("WifiDirect", "peers found: " + WifiDirectAssistant.this.peers.size());
/* 112 */       for (WifiP2pDevice peer : WifiDirectAssistant.this.peers)
/*     */       {
/* 114 */         String s = "    peer: " + peer.deviceAddress + " " + peer.deviceName;
/* 115 */         RobotLog.vv("WifiDirect", s);
/*     */       }
/*     */       
/* 118 */       WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.PEERS_AVAILABLE);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class WifiDirectConnectionInfoListener
/*     */     implements WifiP2pManager.ConnectionInfoListener
/*     */   {
/*     */     private WifiDirectConnectionInfoListener() {}
/*     */     
/*     */     public void onConnectionInfoAvailable(WifiP2pInfo info)
/*     */     {
/* 130 */       WifiDirectAssistant.this.wifiP2pManager.requestGroupInfo(WifiDirectAssistant.this.wifiP2pChannel, WifiDirectAssistant.this.groupInfoListener);
/* 131 */       WifiDirectAssistant.this.groupOwnerAddress = info.groupOwnerAddress;
/* 132 */       RobotLog.dd("WifiDirect", "group owners address: " + WifiDirectAssistant.this.groupOwnerAddress.toString());
/*     */       
/* 134 */       if ((info.groupFormed) && (info.isGroupOwner)) {
/* 135 */         RobotLog.dd("WifiDirect", "group formed, this device is the group owner (GO)");
/* 136 */         WifiDirectAssistant.this.connectStatus = NetworkConnection.ConnectStatus.GROUP_OWNER;
/* 137 */         WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.CONNECTED_AS_GROUP_OWNER);
/* 138 */       } else if (info.groupFormed) {
/* 139 */         RobotLog.dd("WifiDirect", "group formed, this device is a client");
/* 140 */         WifiDirectAssistant.this.connectStatus = NetworkConnection.ConnectStatus.CONNECTED;
/* 141 */         WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.CONNECTED_AS_PEER);
/*     */       } else {
/* 143 */         RobotLog.dd("WifiDirect", "group NOT formed, ERROR: " + info.toString());
/* 144 */         WifiDirectAssistant.this.failureReason = 0;
/* 145 */         WifiDirectAssistant.this.connectStatus = NetworkConnection.ConnectStatus.ERROR;
/* 146 */         WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.ERROR);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class WifiDirectGroupInfoListener implements WifiP2pManager.GroupInfoListener {
/*     */     private WifiDirectGroupInfoListener() {}
/*     */     
/*     */     public void onGroupInfoAvailable(WifiP2pGroup group) {
/* 155 */       if (group == null) { return;
/*     */       }
/* 157 */       if (group.isGroupOwner()) {
/* 158 */         WifiDirectAssistant.this.groupOwnerMacAddress = WifiDirectAssistant.this.deviceMacAddress;
/* 159 */         WifiDirectAssistant.this.groupOwnerName = WifiDirectAssistant.this.deviceName;
/*     */       } else {
/* 161 */         WifiP2pDevice go = group.getOwner();
/* 162 */         WifiDirectAssistant.this.groupOwnerMacAddress = go.deviceAddress;
/* 163 */         WifiDirectAssistant.this.groupOwnerName = go.deviceName;
/*     */       }
/*     */       
/* 166 */       WifiDirectAssistant.this.groupInterface = group.getInterface();
/* 167 */       WifiDirectAssistant.this.groupNetworkName = group.getNetworkName();
/*     */       
/* 169 */       WifiDirectAssistant.this.passphrase = group.getPassphrase();
/*     */       
/*     */ 
/* 172 */       WifiDirectAssistant.this.passphrase = (WifiDirectAssistant.this.passphrase != null ? WifiDirectAssistant.this.passphrase : "");
/*     */       
/* 174 */       RobotLog.vv("WifiDirect", "connection information available");
/* 175 */       RobotLog.vv("WifiDirect", "connection information - groupOwnerName = " + WifiDirectAssistant.this.groupOwnerName);
/* 176 */       RobotLog.vv("WifiDirect", "connection information - groupOwnerMacAddress = " + WifiDirectAssistant.this.groupOwnerMacAddress);
/* 177 */       RobotLog.vv("WifiDirect", "connection information - groupInterface = " + WifiDirectAssistant.this.groupInterface);
/* 178 */       RobotLog.vv("WifiDirect", "connection information - groupNetworkName = " + WifiDirectAssistant.this.groupNetworkName);
/*     */       
/* 180 */       WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.CONNECTION_INFO_AVAILABLE);
/*     */     }
/*     */   }
/*     */   
/*     */   private class WifiP2pBroadcastReceiver extends BroadcastReceiver {
/*     */     private WifiP2pBroadcastReceiver() {}
/*     */     
/*     */     public void onReceive(Context context, Intent intent) {
/* 188 */       String action = intent.getAction();
/* 189 */       if ("android.net.wifi.p2p.STATE_CHANGED".equals(action)) {
/* 190 */         int state = intent.getIntExtra("wifi_p2p_state", -1);
/* 191 */         WifiDirectAssistant.this.isWifiP2pEnabled = (state == 2);
/* 192 */         RobotLog.dd("WifiDirect", "broadcast: state - enabled: " + WifiDirectAssistant.this.isWifiP2pEnabled);
/*     */       }
/* 194 */       else if ("android.net.wifi.p2p.PEERS_CHANGED".equals(action)) {
/* 195 */         RobotLog.dd("WifiDirect", "broadcast: peers changed");
/* 196 */         WifiDirectAssistant.this.wifiP2pManager.requestPeers(WifiDirectAssistant.this.wifiP2pChannel, WifiDirectAssistant.this.peerListListener);
/*     */       }
/* 198 */       else if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(action))
/*     */       {
/* 200 */         NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
/* 201 */         WifiP2pInfo wifip2pinfo = (WifiP2pInfo)intent.getParcelableExtra("wifiP2pInfo");
/* 202 */         WifiP2pGroup wifiP2pGroup = (WifiP2pGroup)intent.getParcelableExtra("p2pGroupInfo");
/*     */         
/* 204 */         RobotLog.dd("WifiDirect", "broadcast: connection changed: connectStatus=%s networkInfo.state=%s", new Object[] { WifiDirectAssistant.this.connectStatus, networkInfo.getState() });
/* 205 */         WifiDirectAssistant.dump(networkInfo);
/* 206 */         WifiDirectAssistant.dump(wifip2pinfo);
/* 207 */         WifiDirectAssistant.dump(wifiP2pGroup);
/*     */         
/* 209 */         if (networkInfo.isConnected()) {
/* 210 */           if (!WifiDirectAssistant.this.isConnected()) {
/* 211 */             WifiDirectAssistant.this.wifiP2pManager.requestConnectionInfo(WifiDirectAssistant.this.wifiP2pChannel, WifiDirectAssistant.this.connectionListener);
/* 212 */             WifiDirectAssistant.this.wifiP2pManager.stopPeerDiscovery(WifiDirectAssistant.this.wifiP2pChannel, null);
/*     */           }
/*     */         } else {
/* 215 */           WifiDirectAssistant.this.connectStatus = NetworkConnection.ConnectStatus.NOT_CONNECTED;
/* 216 */           if (!WifiDirectAssistant.this.groupFormed) {
/* 217 */             WifiDirectAssistant.this.discoverPeers();
/*     */           }
/*     */           
/* 220 */           if (WifiDirectAssistant.this.isConnected()) {
/* 221 */             RobotLog.vv("WifiDirect", "disconnecting");
/* 222 */             WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.DISCONNECTED);
/*     */           }
/* 224 */           WifiDirectAssistant.this.groupFormed = wifip2pinfo.groupFormed;
/*     */         }
/*     */       }
/* 227 */       else if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(action)) {
/* 228 */         RobotLog.dd("WifiDirect", "broadcast: this device changed");
/* 229 */         WifiDirectAssistant.this.onWifiP2pThisDeviceChanged((WifiP2pDevice)intent.getParcelableExtra("wifiP2pDevice"));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void dump(NetworkInfo info) {
/* 235 */     Assert.assertNotNull(info);
/* 236 */     RobotLog.vv("WifiDirect", "NetworkInfo: %s", new Object[] { info.toString() });
/*     */   }
/*     */   
/*     */   public static void dump(WifiP2pInfo info) {
/* 240 */     Assert.assertNotNull(info);
/* 241 */     RobotLog.vv("WifiDirect", "WifiP2pInfo: %s", new Object[] { info.toString() });
/*     */   }
/*     */   
/*     */   public static void dump(WifiP2pGroup info) {
/* 245 */     Assert.assertNotNull(info);
/* 246 */     RobotLog.vv("WifiDirect", "WifiP2pGroup: %s", new Object[] { info.toString().replace("\n ", ", ") });
/*     */   }
/*     */   
/*     */   public static synchronized WifiDirectAssistant getWifiDirectAssistant(Context context) {
/* 250 */     if (wifiDirectAssistant == null) wifiDirectAssistant = new WifiDirectAssistant(context);
/* 251 */     return wifiDirectAssistant;
/*     */   }
/*     */   
/*     */   private WifiDirectAssistant(Context context) {
/* 255 */     this.context = context;
/*     */     
/*     */ 
/* 258 */     this.intentFilter = new IntentFilter();
/* 259 */     this.intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
/* 260 */     this.intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
/* 261 */     this.intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
/* 262 */     this.intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
/*     */     
/* 264 */     this.wifiP2pManager = ((WifiP2pManager)context.getSystemService("wifip2p"));
/* 265 */     this.wifiP2pChannel = this.wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
/* 266 */     this.receiver = new WifiP2pBroadcastReceiver(null);
/* 267 */     this.connectionListener = new WifiDirectConnectionInfoListener(null);
/* 268 */     this.peerListListener = new WifiDirectPeerListListener(null);
/* 269 */     this.groupInfoListener = new WifiDirectGroupInfoListener(null);
/*     */   }
/*     */   
/*     */   public NetworkType getNetworkType()
/*     */   {
/* 274 */     return NetworkType.WIFIDIRECT;
/*     */   }
/*     */   
/*     */   public synchronized void enable() {
/* 278 */     this.clients += 1;
/* 279 */     RobotLog.vv("WifiDirect", "There are " + this.clients + " Wifi Direct Assistant Clients (+)");
/*     */     
/* 281 */     if (this.clients == 1) {
/* 282 */       RobotLog.vv("WifiDirect", "Enabling Wifi Direct Assistant");
/* 283 */       if (this.receiver == null) this.receiver = new WifiP2pBroadcastReceiver(null);
/* 284 */       this.context.registerReceiver(this.receiver, this.intentFilter);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void disable() {
/* 289 */     this.clients -= 1;
/* 290 */     RobotLog.vv("WifiDirect", "There are " + this.clients + " Wifi Direct Assistant Clients (-)");
/*     */     
/* 292 */     if (this.clients == 0) {
/* 293 */       RobotLog.vv("WifiDirect", "Disabling Wifi Direct Assistant");
/* 294 */       this.wifiP2pManager.stopPeerDiscovery(this.wifiP2pChannel, null);
/* 295 */       this.wifiP2pManager.cancelConnect(this.wifiP2pChannel, null);
/*     */       try
/*     */       {
/* 298 */         this.context.unregisterReceiver(this.receiver);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*     */       
/* 302 */       this.lastEvent = null;
/* 303 */       this.connectStatus = NetworkConnection.ConnectStatus.NOT_CONNECTED;
/*     */     }
/*     */   }
/*     */   
/*     */   public void discoverPotentialConnections()
/*     */   {
/* 309 */     discoverPeers();
/*     */   }
/*     */   
/*     */   public void createConnection()
/*     */   {
/* 314 */     createGroup();
/*     */   }
/*     */   
/*     */   public void cancelPotentialConnections()
/*     */   {
/* 319 */     cancelDiscoverPeers();
/*     */   }
/*     */   
/*     */   public String getInfo()
/*     */   {
/* 324 */     StringBuilder s = new StringBuilder();
/*     */     
/* 326 */     if (isEnabled()) {
/* 327 */       s.append("Name: ").append(getDeviceName());
/* 328 */       if (isGroupOwner()) {
/* 329 */         s.append("\nIP Address").append(getGroupOwnerAddress().getHostAddress());
/* 330 */         s.append("\nPassphrase: ").append(getPassphrase());
/* 331 */         s.append("\nGroup Owner");
/* 332 */       } else if (isConnected()) {
/* 333 */         s.append("\nGroup Owner: ").append(getGroupOwnerName());
/* 334 */         s.append("\nConnected");
/*     */       } else {
/* 336 */         s.append("\nNo connection information");
/*     */       }
/*     */     }
/*     */     
/* 340 */     return s.toString();
/*     */   }
/*     */   
/*     */   public synchronized boolean isEnabled() {
/* 344 */     return this.clients > 0;
/*     */   }
/*     */   
/*     */   public NetworkConnection.ConnectStatus getConnectStatus() {
/* 348 */     return this.connectStatus;
/*     */   }
/*     */   
/*     */   public List<WifiP2pDevice> getPeers() {
/* 352 */     return new ArrayList(this.peers);
/*     */   }
/*     */   
/*     */   public NetworkConnection.NetworkConnectionCallback getCallback() {
/* 356 */     return this.callback;
/*     */   }
/*     */   
/*     */   public void setCallback(NetworkConnection.NetworkConnectionCallback callback) {
/* 360 */     this.callback = callback;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeviceMacAddress()
/*     */   {
/* 368 */     return this.deviceMacAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 376 */     return this.deviceName;
/*     */   }
/*     */   
/* 379 */   public InetAddress getConnectionOwnerAddress() { return getGroupOwnerAddress(); }
/*     */   
/*     */ 
/*     */ 
/*     */   public InetAddress getGroupOwnerAddress()
/*     */   {
/* 385 */     return this.groupOwnerAddress;
/*     */   }
/*     */   
/*     */   public String getConnectionOwnerMacAddress() {
/* 389 */     return getGroupOwnerMacAddress();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String getGroupOwnerMacAddress()
/*     */   {
/* 396 */     return this.groupOwnerMacAddress;
/*     */   }
/*     */   
/*     */   public String getConnectionOwnerName() {
/* 400 */     return getGroupOwnerName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGroupOwnerName()
/*     */   {
/* 408 */     return this.groupOwnerName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPassphrase()
/*     */   {
/* 416 */     return this.passphrase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGroupInterface()
/*     */   {
/* 424 */     return this.groupInterface;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGroupNetworkName()
/*     */   {
/* 432 */     return this.groupNetworkName;
/*     */   }
/*     */   
/*     */   public boolean isWifiP2pEnabled() {
/* 436 */     return this.isWifiP2pEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isConnected()
/*     */   {
/* 444 */     return (this.connectStatus == NetworkConnection.ConnectStatus.CONNECTED) || (this.connectStatus == NetworkConnection.ConnectStatus.GROUP_OWNER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isGroupOwner()
/*     */   {
/* 453 */     return this.connectStatus == NetworkConnection.ConnectStatus.GROUP_OWNER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void discoverPeers()
/*     */   {
/* 460 */     this.wifiP2pManager.discoverPeers(this.wifiP2pChannel, new WifiP2pManager.ActionListener()
/*     */     {
/*     */       public void onSuccess()
/*     */       {
/* 464 */         WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.DISCOVERING_PEERS);
/* 465 */         RobotLog.dd("WifiDirect", "discovering peers");
/*     */       }
/*     */       
/*     */       public void onFailure(int reason)
/*     */       {
/* 470 */         String reasonStr = WifiDirectAssistant.failureReasonToString(reason);
/* 471 */         WifiDirectAssistant.this.failureReason = reason;
/* 472 */         RobotLog.w("Wifi Direct failure while trying to discover peers - reason: " + reasonStr);
/* 473 */         WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.ERROR);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void cancelDiscoverPeers()
/*     */   {
/* 482 */     RobotLog.dd("WifiDirect", "stop discovering peers");
/* 483 */     this.wifiP2pManager.stopPeerDiscovery(this.wifiP2pChannel, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createGroup()
/*     */   {
/* 495 */     this.wifiP2pManager.createGroup(this.wifiP2pChannel, new WifiP2pManager.ActionListener()
/*     */     {
/*     */       public void onSuccess()
/*     */       {
/* 499 */         WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.GROUP_CREATED);
/* 500 */         RobotLog.dd("WifiDirect", "created group");
/*     */       }
/*     */       
/*     */       public void onFailure(int reason)
/*     */       {
/* 505 */         if (reason == 2)
/*     */         {
/* 507 */           RobotLog.dd("WifiDirect", "cannot create group, does group already exist?");
/*     */         } else {
/* 509 */           String reasonStr = WifiDirectAssistant.failureReasonToString(reason);
/* 510 */           WifiDirectAssistant.this.failureReason = reason;
/* 511 */           RobotLog.w("Wifi Direct failure while trying to create group - reason: " + reasonStr);
/* 512 */           WifiDirectAssistant.this.connectStatus = NetworkConnection.ConnectStatus.ERROR;
/* 513 */           WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.ERROR);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeGroup()
/*     */   {
/* 523 */     this.wifiP2pManager.removeGroup(this.wifiP2pChannel, null);
/*     */   }
/*     */   
/*     */   public void connect(String deviceAddress, String notSupported)
/*     */   {
/* 528 */     throw new UnsupportedOperationException("This method is not supported for this class");
/*     */   }
/*     */   
/*     */   public void connect(String deviceAddress)
/*     */   {
/* 533 */     if ((this.connectStatus == NetworkConnection.ConnectStatus.CONNECTING) || (this.connectStatus == NetworkConnection.ConnectStatus.CONNECTED)) {
/* 534 */       RobotLog.dd("WifiDirect", "connection request to " + deviceAddress + " ignored, already connected");
/* 535 */       return;
/*     */     }
/*     */     
/* 538 */     RobotLog.dd("WifiDirect", "connecting to " + deviceAddress);
/* 539 */     this.connectStatus = NetworkConnection.ConnectStatus.CONNECTING;
/*     */     
/* 541 */     WifiP2pConfig config = new WifiP2pConfig();
/* 542 */     config.deviceAddress = deviceAddress;
/* 543 */     config.wps.setup = 0;
/* 544 */     config.groupOwnerIntent = 1;
/*     */     
/* 546 */     this.wifiP2pManager.connect(this.wifiP2pChannel, config, new WifiP2pManager.ActionListener()
/*     */     {
/*     */       public void onSuccess()
/*     */       {
/* 550 */         RobotLog.dd("WifiDirect", "connect started");
/* 551 */         WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.CONNECTING);
/*     */       }
/*     */       
/*     */       public void onFailure(int reason)
/*     */       {
/* 556 */         String reasonStr = WifiDirectAssistant.failureReasonToString(reason);
/* 557 */         WifiDirectAssistant.this.failureReason = reason;
/* 558 */         RobotLog.dd("WifiDirect", "connect cannot start - reason: " + reasonStr);
/* 559 */         WifiDirectAssistant.this.sendEvent(NetworkConnection.Event.ERROR);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void onWifiP2pThisDeviceChanged(WifiP2pDevice wifiP2pDevice) {
/* 565 */     this.deviceName = wifiP2pDevice.deviceName;
/* 566 */     this.deviceMacAddress = wifiP2pDevice.deviceAddress;
/* 567 */     RobotLog.vv("WifiDirect", "device information: " + this.deviceName + " " + this.deviceMacAddress);
/*     */   }
/*     */   
/*     */   public String getFailureReason() {
/* 571 */     return failureReasonToString(this.failureReason);
/*     */   }
/*     */   
/*     */   public static String failureReasonToString(int reason) {
/* 575 */     switch (reason) {
/*     */     case 1: 
/* 577 */       return "P2P_UNSUPPORTED";
/*     */     case 0: 
/* 579 */       return "ERROR";
/*     */     case 2: 
/* 581 */       return "BUSY";
/*     */     }
/* 583 */     return "UNKNOWN (reason " + reason + ")";
/*     */   }
/*     */   
/*     */ 
/*     */   private void sendEvent(NetworkConnection.Event event)
/*     */   {
/* 589 */     if ((this.lastEvent == event) && (this.lastEvent != NetworkConnection.Event.PEERS_AVAILABLE)) return;
/* 590 */     this.lastEvent = event;
/*     */     
/* 592 */     if (this.callback != null) this.callback.onNetworkConnectionEvent(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\wifi\WifiDirectAssistant.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */