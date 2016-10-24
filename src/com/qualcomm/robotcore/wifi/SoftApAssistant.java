/*     */ package com.qualcomm.robotcore.wifi;
/*     */ 
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.net.wifi.ScanResult;
/*     */ import android.net.wifi.SupplicantState;
/*     */ import android.net.wifi.WifiConfiguration;
/*     */ import android.net.wifi.WifiInfo;
/*     */ import android.net.wifi.WifiManager;
/*     */ import com.qualcomm.robotcore.util.ReadWriteFile;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SoftApAssistant
/*     */   extends NetworkConnection
/*     */ {
/*  58 */   private static SoftApAssistant softApAssistant = null;
/*     */   
/*  60 */   private final List<ScanResult> scanResults = new ArrayList();
/*     */   
/*     */   private final WifiManager wifiManager;
/*  63 */   private Context context = null;
/*  64 */   private NetworkConnection.Event lastEvent = null;
/*     */   
/*     */   private static IntentFilter intentFilter;
/*     */   
/*     */   private BroadcastReceiver receiver;
/*  69 */   private static String DEFAULT_PASSWORD = "password";
/*  70 */   private static String DEFAULT_SSID = "FTC-1234";
/*     */   
/*  72 */   String ssid = DEFAULT_SSID;
/*  73 */   String password = DEFAULT_PASSWORD;
/*     */   
/*     */   private static final String NETWORK_SSID_FILE = "FTC_RobotController_SSID.txt";
/*     */   
/*     */   private static final String NETWORK_PASSWORD_FILE = "FTC_RobotController_password.txt";
/*  78 */   private NetworkConnection.NetworkConnectionCallback callback = null;
/*     */   
/*     */   public static synchronized SoftApAssistant getSoftApAssistant(Context context)
/*     */   {
/*  82 */     if (softApAssistant == null) { softApAssistant = new SoftApAssistant(context);
/*     */     }
/*     */     
/*  85 */     intentFilter = new IntentFilter();
/*  86 */     intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
/*  87 */     intentFilter.addAction("android.net.wifi.NETWORK_IDS_CHANGED");
/*  88 */     intentFilter.addAction("android.net.wifi.STATE_CHANGE");
/*  89 */     intentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
/*  90 */     intentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
/*  91 */     intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
/*     */     
/*  93 */     return softApAssistant;
/*     */   }
/*     */   
/*     */   private SoftApAssistant(Context context)
/*     */   {
/*  98 */     this.context = context;
/*     */     
/* 100 */     this.wifiManager = ((WifiManager)context.getSystemService("wifi"));
/*     */   }
/*     */   
/*     */   public List<ScanResult> getScanResults()
/*     */   {
/* 105 */     return this.scanResults;
/*     */   }
/*     */   
/*     */   public NetworkType getNetworkType()
/*     */   {
/* 110 */     return NetworkType.SOFTAP;
/*     */   }
/*     */   
/*     */ 
/*     */   public void enable()
/*     */   {
/* 116 */     if (this.receiver == null) { this.receiver = new BroadcastReceiver()
/*     */       {
/*     */         public void onReceive(Context context, Intent intent) {
/* 119 */           String action = intent.getAction();
/* 120 */           WifiInfo wifiInfo = SoftApAssistant.this.wifiManager.getConnectionInfo();
/* 121 */           RobotLog.v("onReceive(), action: " + action + ", wifiInfo: " + wifiInfo);
/*     */           
/* 123 */           if ((wifiInfo.getSSID().equals(SoftApAssistant.this.ssid)) && (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED)) {
/* 124 */             SoftApAssistant.this.sendEvent(NetworkConnection.Event.CONNECTION_INFO_AVAILABLE);
/*     */           }
/* 126 */           if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
/* 127 */             SoftApAssistant.this.scanResults.clear();
/* 128 */             SoftApAssistant.this.scanResults.addAll(SoftApAssistant.this.wifiManager.getScanResults());
/*     */             
/* 130 */             RobotLog.v("Soft AP scanResults found: " + SoftApAssistant.this.scanResults.size());
/* 131 */             for (ScanResult scanResult : SoftApAssistant.this.scanResults)
/*     */             {
/* 133 */               String s = "    scanResult: " + scanResult.SSID;
/* 134 */               RobotLog.v(s);
/*     */             }
/* 136 */             SoftApAssistant.this.sendEvent(NetworkConnection.Event.PEERS_AVAILABLE);
/*     */           }
/* 138 */           if (("android.net.wifi.supplicant.STATE_CHANGE".equals(action)) && 
/* 139 */             (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED)) {
/* 140 */             SoftApAssistant.this.sendEvent(NetworkConnection.Event.CONNECTION_INFO_AVAILABLE);
/*     */           }
/*     */         }
/*     */       };
/*     */     }
/*     */     
/* 146 */     this.context.registerReceiver(this.receiver, intentFilter);
/*     */   }
/*     */   
/*     */   public void disable()
/*     */   {
/*     */     try {
/* 152 */       this.context.unregisterReceiver(this.receiver);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {}
/*     */     
/* 156 */     this.lastEvent = null;
/*     */   }
/*     */   
/*     */   public void setCallback(NetworkConnection.NetworkConnectionCallback callback)
/*     */   {
/* 161 */     RobotLog.v("setting NetworkConnection callback: " + callback);
/* 162 */     this.callback = callback;
/*     */   }
/*     */   
/*     */   public void discoverPotentialConnections()
/*     */   {
/* 167 */     this.wifiManager.startScan();
/*     */   }
/*     */   
/*     */ 
/*     */   public void cancelPotentialConnections() {}
/*     */   
/*     */ 
/*     */   private WifiConfiguration buildConfig(String ssid, String pass)
/*     */   {
/* 176 */     WifiConfiguration myConfig = new WifiConfiguration();
/* 177 */     myConfig.SSID = ssid;
/* 178 */     myConfig.preSharedKey = pass;
/* 179 */     RobotLog.v("Setting up network, myConfig.SSID: " + myConfig.SSID + ", password: " + myConfig.preSharedKey);
/* 180 */     myConfig.status = 2;
/* 181 */     myConfig.allowedAuthAlgorithms.set(0);
/* 182 */     myConfig.allowedKeyManagement.set(1);
/* 183 */     myConfig.allowedProtocols.set(1);
/* 184 */     myConfig.allowedProtocols.set(0);
/* 185 */     myConfig.allowedGroupCiphers.set(2);
/* 186 */     myConfig.allowedGroupCiphers.set(3);
/* 187 */     myConfig.allowedPairwiseCiphers.set(1);
/* 188 */     myConfig.allowedPairwiseCiphers.set(2);
/*     */     
/* 190 */     return myConfig;
/*     */   }
/*     */   
/*     */ 
/*     */   public void createConnection()
/*     */   {
/* 196 */     if (this.wifiManager.isWifiEnabled()) {
/* 197 */       this.wifiManager.setWifiEnabled(false);
/*     */     }
/*     */     
/* 200 */     File directory = AppUtil.FIRST_FOLDER;
/* 201 */     File fileSSID = new File(directory, "FTC_RobotController_SSID.txt");
/* 202 */     if (!fileSSID.exists()) {
/* 203 */       ReadWriteFile.writeFile(directory, "FTC_RobotController_SSID.txt", DEFAULT_SSID);
/*     */     }
/*     */     
/* 206 */     File filePassword = new File(directory, "FTC_RobotController_password.txt");
/* 207 */     if (!filePassword.exists()) {
/* 208 */       ReadWriteFile.writeFile(directory, "FTC_RobotController_password.txt", DEFAULT_PASSWORD);
/*     */     }
/*     */     
/* 211 */     String userSSID = ReadWriteFile.readFile(fileSSID);
/* 212 */     String userPass = ReadWriteFile.readFile(filePassword);
/*     */     
/* 214 */     if ((userSSID.isEmpty()) || (userSSID.length() >= 15)) {
/* 215 */       ReadWriteFile.writeFile(directory, "FTC_RobotController_SSID.txt", DEFAULT_SSID);
/*     */     }
/*     */     
/* 218 */     if (userPass.isEmpty()) {
/* 219 */       ReadWriteFile.writeFile(directory, "FTC_RobotController_password.txt", DEFAULT_PASSWORD);
/*     */     }
/* 221 */     this.ssid = ReadWriteFile.readFile(fileSSID);
/* 222 */     this.password = ReadWriteFile.readFile(filePassword);
/*     */     
/* 224 */     WifiConfiguration wifiConfig = buildConfig(this.ssid, this.password);
/*     */     
/*     */ 
/* 227 */     RobotLog.v("Advertising SSID: " + this.ssid + ", password: " + this.password);
/*     */     try
/*     */     {
/* 230 */       Method setApConfig = this.wifiManager.getClass().getMethod("setWifiApConfiguration", new Class[] { WifiConfiguration.class });
/* 231 */       setApConfig.invoke(this.wifiManager, new Object[] { wifiConfig });
/*     */       
/* 233 */       Method enableAp = this.wifiManager.getClass().getMethod("setWifiApEnabled", new Class[] { WifiConfiguration.class, Boolean.TYPE });
/* 234 */       enableAp.invoke(this.wifiManager, new Object[] { null, Boolean.valueOf(false) });
/* 235 */       Boolean success = (Boolean)enableAp.invoke(this.wifiManager, new Object[] { wifiConfig, Boolean.valueOf(true) });
/* 236 */       if (success.booleanValue()) {
/* 237 */         sendEvent(NetworkConnection.Event.AP_CREATED);
/*     */       }
/*     */     } catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
/* 240 */       RobotLog.e(e.getMessage());
/* 241 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void connect(String ssid, String password)
/*     */   {
/* 247 */     this.ssid = ssid;
/* 248 */     this.password = password;
/*     */     
/* 250 */     WifiConfiguration wifiConfig = buildConfig(String.format("\"%s\"", new Object[] { ssid }), String.format("\"%s\"", new Object[] { password }));
/*     */     
/* 252 */     WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
/*     */     
/* 254 */     RobotLog.v("Connecting to SoftAP, SSID: " + wifiConfig.SSID + ", supplicant state: " + wifiInfo.getSupplicantState());
/* 255 */     if ((wifiInfo.getSSID().equals(wifiConfig.SSID)) && (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED)) {
/* 256 */       sendEvent(NetworkConnection.Event.CONNECTION_INFO_AVAILABLE);
/*     */     }
/* 258 */     if ((!wifiInfo.getSSID().equals(wifiConfig.SSID)) || (wifiInfo.getSupplicantState() != SupplicantState.COMPLETED))
/*     */     {
/* 260 */       int netId = this.wifiManager.addNetwork(wifiConfig);
/* 261 */       this.wifiManager.saveConfiguration();
/* 262 */       if (netId != -1)
/*     */       {
/* 264 */         List<WifiConfiguration> list = this.wifiManager.getConfiguredNetworks();
/* 265 */         for (WifiConfiguration i : list) {
/* 266 */           if ((i.SSID != null) && (i.SSID.equals("\"" + ssid + "\""))) {
/* 267 */             this.wifiManager.disconnect();
/* 268 */             this.wifiManager.enableNetwork(i.networkId, true);
/* 269 */             this.wifiManager.reconnect();
/* 270 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void connect(String ssid)
/*     */   {
/* 279 */     connect(ssid, DEFAULT_PASSWORD);
/*     */   }
/*     */   
/*     */   public InetAddress getConnectionOwnerAddress()
/*     */   {
/* 284 */     InetAddress address = null;
/*     */     try {
/* 286 */       address = InetAddress.getByName("192.168.43.1");
/*     */     } catch (UnknownHostException e) {
/* 288 */       e.printStackTrace();
/*     */     }
/*     */     
/* 291 */     return address;
/*     */   }
/*     */   
/*     */   public String getConnectionOwnerName()
/*     */   {
/* 296 */     RobotLog.e("ssid in softap assistant: " + this.ssid);
/* 297 */     return this.ssid.replace("\"", "");
/*     */   }
/*     */   
/*     */   public String getConnectionOwnerMacAddress()
/*     */   {
/* 302 */     return this.ssid.replace("\"", "");
/*     */   }
/*     */   
/*     */   public boolean isConnected()
/*     */   {
/* 307 */     WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
/* 308 */     RobotLog.v("isConnected(), current supplicant state: " + wifiInfo.getSupplicantState().toString());
/* 309 */     return wifiInfo.getSupplicantState() == SupplicantState.COMPLETED;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 314 */     return this.ssid;
/*     */   }
/*     */   
/*     */   private boolean isSoftAccessPoint()
/*     */   {
/* 319 */     Method isWifiApEnabled = null;
/*     */     try {
/* 321 */       isWifiApEnabled = this.wifiManager.getClass().getMethod("isWifiApEnabled", new Class[0]);
/* 322 */       return ((Boolean)isWifiApEnabled.invoke(this.wifiManager, new Object[0])).booleanValue();
/*     */     } catch (NoSuchMethodException e) {
/* 324 */       e.printStackTrace();
/*     */     } catch (InvocationTargetException e) {
/* 326 */       e.printStackTrace();
/*     */     } catch (IllegalAccessException e) {
/* 328 */       e.printStackTrace();
/*     */     }
/* 330 */     return false;
/*     */   }
/*     */   
/*     */   public String getInfo()
/*     */   {
/* 335 */     StringBuilder s = new StringBuilder();
/*     */     
/* 337 */     WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
/*     */     
/* 339 */     s.append("Name: ").append(getDeviceName());
/* 340 */     if (isSoftAccessPoint()) {
/* 341 */       s.append("\nAccess Point SSID: ").append(getConnectionOwnerName());
/* 342 */       s.append("\nPassphrase: ").append(getPassphrase());
/* 343 */       s.append("\nAdvertising");
/* 344 */     } else if (isConnected()) {
/* 345 */       s.append("\nIP Address: ").append(getIpAddressAsString(wifiInfo.getIpAddress()));
/* 346 */       s.append("\nAccess Point SSID: ").append(getConnectionOwnerName());
/* 347 */       s.append("\nPassphrase: ").append(getPassphrase());
/*     */     } else {
/* 349 */       s.append("\nNo connection information");
/*     */     }
/*     */     
/* 352 */     return s.toString();
/*     */   }
/*     */   
/*     */   private String getIpAddressAsString(int ipAddress) {
/* 356 */     String address = String.format("%d.%d.%d.%d", new Object[] { Integer.valueOf(ipAddress & 0xFF), Integer.valueOf(ipAddress >> 8 & 0xFF), Integer.valueOf(ipAddress >> 16 & 0xFF), Integer.valueOf(ipAddress >> 24 & 0xFF) });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 362 */     return address;
/*     */   }
/*     */   
/*     */   public String getFailureReason()
/*     */   {
/* 367 */     return null;
/*     */   }
/*     */   
/*     */   public String getPassphrase()
/*     */   {
/* 372 */     return this.password;
/*     */   }
/*     */   
/*     */   public NetworkConnection.ConnectStatus getConnectStatus()
/*     */   {
/* 377 */     WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
/* 378 */     switch (wifiInfo.getSupplicantState()) {
/*     */     case ASSOCIATING: 
/* 380 */       return NetworkConnection.ConnectStatus.CONNECTING;
/*     */     case COMPLETED: 
/* 382 */       return NetworkConnection.ConnectStatus.CONNECTED;
/*     */     case SCANNING: 
/* 384 */       return NetworkConnection.ConnectStatus.NOT_CONNECTED;
/*     */     }
/* 386 */     return NetworkConnection.ConnectStatus.NOT_CONNECTED;
/*     */   }
/*     */   
/*     */ 
/*     */   private void sendEvent(NetworkConnection.Event event)
/*     */   {
/* 392 */     if (this.lastEvent == event) return;
/* 393 */     this.lastEvent = event;
/*     */     
/* 395 */     if (this.callback != null) this.callback.onNetworkConnectionEvent(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\wifi\SoftApAssistant.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */