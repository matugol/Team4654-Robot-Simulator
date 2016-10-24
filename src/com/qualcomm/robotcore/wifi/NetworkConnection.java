/*     */ package com.qualcomm.robotcore.wifi;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ 
/*     */ public abstract class NetworkConnection {
/*     */   public abstract NetworkType getNetworkType();
/*     */   
/*     */   public abstract void enable();
/*     */   
/*     */   public abstract void disable();
/*     */   
/*     */   public abstract void setCallback(@NonNull NetworkConnectionCallback paramNetworkConnectionCallback);
/*     */   
/*     */   public abstract void discoverPotentialConnections();
/*     */   
/*     */   public abstract void cancelPotentialConnections();
/*     */   
/*     */   public abstract void createConnection();
/*     */   
/*     */   public abstract void connect(String paramString);
/*     */   
/*     */   public abstract void connect(String paramString1, String paramString2);
/*     */   
/*     */   public abstract java.net.InetAddress getConnectionOwnerAddress();
/*     */   
/*     */   public abstract String getConnectionOwnerName();
/*     */   
/*     */   public abstract String getConnectionOwnerMacAddress();
/*     */   
/*     */   public abstract boolean isConnected();
/*     */   
/*     */   public abstract String getDeviceName();
/*     */   
/*     */   public abstract String getInfo();
/*     */   
/*     */   public abstract String getFailureReason();
/*     */   
/*     */   public abstract String getPassphrase();
/*     */   
/*     */   public abstract ConnectStatus getConnectStatus();
/*     */   
/*  42 */   public static enum Event { DISCOVERING_PEERS, 
/*  43 */     PEERS_AVAILABLE, 
/*  44 */     GROUP_CREATED, 
/*  45 */     CONNECTING, 
/*  46 */     CONNECTED_AS_PEER, 
/*  47 */     CONNECTED_AS_GROUP_OWNER, 
/*  48 */     DISCONNECTED, 
/*  49 */     CONNECTION_INFO_AVAILABLE, 
/*  50 */     AP_CREATED, 
/*  51 */     ERROR, 
/*  52 */     UNKNOWN;
/*     */     
/*     */     private Event() {} }
/*     */   
/*  56 */   public static enum ConnectStatus { NOT_CONNECTED, 
/*  57 */     CONNECTING, 
/*  58 */     CONNECTED, 
/*  59 */     GROUP_OWNER, 
/*  60 */     ERROR;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private ConnectStatus() {}
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isDeviceNameValid(String deviceName)
/*     */   {
/* 109 */     return deviceName.matches("^\\p{Print}+$");
/*     */   }
/*     */   
/*     */   public static abstract interface NetworkConnectionCallback
/*     */   {
/*     */     public abstract org.firstinspires.ftc.robotcore.internal.network.CallbackResult onNetworkConnectionEvent(NetworkConnection.Event paramEvent);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\wifi\NetworkConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */