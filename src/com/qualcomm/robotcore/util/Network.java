/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Network
/*     */ {
/*     */   public static InetAddress getLoopbackAddress()
/*     */   {
/*     */     try
/*     */     {
/*  54 */       return InetAddress.getByAddress(new byte[] { Byte.MAX_VALUE, 0, 0, 1 });
/*     */     }
/*     */     catch (UnknownHostException e) {}
/*  57 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ArrayList<InetAddress> getLocalIpAddresses()
/*     */   {
/*  67 */     ArrayList<InetAddress> addresses = new ArrayList();
/*     */     try
/*     */     {
/*  70 */       for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
/*  71 */         addresses.addAll(Collections.list(iface.getInetAddresses()));
/*     */       }
/*     */     }
/*     */     catch (SocketException localSocketException) {}
/*     */     
/*     */ 
/*     */ 
/*  78 */     return addresses;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ArrayList<InetAddress> getLocalIpAddress(String networkInterface)
/*     */   {
/*  88 */     ArrayList<InetAddress> addresses = new ArrayList();
/*     */     try
/*     */     {
/*  91 */       for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
/*  92 */         if (iface.getName() == networkInterface) {
/*  93 */           addresses.addAll(Collections.list(iface.getInetAddresses()));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SocketException localSocketException) {}
/*     */     
/*     */ 
/*     */ 
/* 101 */     return addresses;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ArrayList<InetAddress> removeIPv6Addresses(Collection<InetAddress> addresses)
/*     */   {
/* 111 */     ArrayList<InetAddress> filtered = new ArrayList();
/* 112 */     for (InetAddress addr : addresses) {
/* 113 */       if ((addr instanceof Inet4Address)) {
/* 114 */         filtered.add(addr);
/*     */       }
/*     */     }
/*     */     
/* 118 */     return filtered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ArrayList<InetAddress> removeIPv4Addresses(Collection<InetAddress> addresses)
/*     */   {
/* 128 */     ArrayList<InetAddress> filtered = new ArrayList();
/* 129 */     for (InetAddress addr : addresses) {
/* 130 */       if ((addr instanceof Inet6Address)) {
/* 131 */         filtered.add(addr);
/*     */       }
/*     */     }
/*     */     
/* 135 */     return filtered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ArrayList<InetAddress> removeLoopbackAddresses(Collection<InetAddress> addresses)
/*     */   {
/* 145 */     ArrayList<InetAddress> filtered = new ArrayList();
/* 146 */     for (InetAddress addr : addresses) {
/* 147 */       if (!addr.isLoopbackAddress()) {
/* 148 */         filtered.add(addr);
/*     */       }
/*     */     }
/*     */     
/* 152 */     return filtered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ArrayList<String> getHostAddresses(Collection<InetAddress> addresses)
/*     */   {
/* 162 */     ArrayList<String> hostnames = new ArrayList();
/*     */     
/* 164 */     for (InetAddress addr : addresses) {
/* 165 */       String host = addr.getHostAddress();
/* 166 */       if (host.contains("%")) host = host.substring(0, host.indexOf('%'));
/* 167 */       hostnames.add(host);
/*     */     }
/*     */     
/* 170 */     return hostnames;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\Network.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */