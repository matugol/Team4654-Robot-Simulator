/*     */ package com.qualcomm.robotcore.robocol;
/*     */ 
/*     */ import com.qualcomm.robotcore.util.Network;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RobocolConfig
/*     */ {
/*     */   public static final int MAX_PACKET_SIZE = 4098;
/*     */   public static final int PORT_NUMBER = 20884;
/*     */   public static final int TTL = 3;
/*     */   public static final int TIMEOUT = 1000;
/*     */   public static final int WIFI_P2P_SUBNET_MASK = -256;
/*     */   
/*     */   public static InetAddress determineBindAddress(InetAddress destAddress)
/*     */   {
/*  69 */     ArrayList<InetAddress> localIpAddresses = Network.getLocalIpAddresses();
/*  70 */     localIpAddresses = Network.removeLoopbackAddresses(localIpAddresses);
/*  71 */     localIpAddresses = Network.removeIPv6Addresses(localIpAddresses);
/*     */     
/*     */ 
/*  74 */     for (InetAddress address : localIpAddresses) {
/*     */       try {
/*  76 */         NetworkInterface iface = NetworkInterface.getByInetAddress(address);
/*  77 */         Enumeration<InetAddress> ifaceAddresses = iface.getInetAddresses();
/*  78 */         while (ifaceAddresses.hasMoreElements()) {
/*  79 */           InetAddress ifaceAddress = (InetAddress)ifaceAddresses.nextElement();
/*  80 */           if (ifaceAddress.equals(destAddress)) {
/*  81 */             return ifaceAddress;
/*     */           }
/*     */         }
/*     */       } catch (SocketException e) {
/*  85 */         RobotLog.v(String.format("socket exception while trying to get network interface of %s", new Object[] { address.getHostAddress() }));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  90 */     return determineBindAddressBasedOnWifiP2pSubnet(localIpAddresses, destAddress);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static InetAddress determineBindAddressBasedOnWifiP2pSubnet(ArrayList<InetAddress> localIpAddresses, InetAddress destAddress)
/*     */   {
/* 101 */     int destAddrInt = TypeConversion.byteArrayToInt(destAddress.getAddress());
/*     */     
/*     */ 
/* 104 */     for (InetAddress address : localIpAddresses) {
/* 105 */       int addrInt = TypeConversion.byteArrayToInt(address.getAddress());
/* 106 */       if ((addrInt & 0xFF00) == (destAddrInt & 0xFF00))
/*     */       {
/* 108 */         return address;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 113 */     return Network.getLoopbackAddress();
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
/*     */   public static InetAddress determineBindAddressBasedOnIsReachable(ArrayList<InetAddress> localIpAddresses, InetAddress destAddress)
/*     */   {
/* 127 */     for (InetAddress address : localIpAddresses) {
/*     */       try {
/* 129 */         NetworkInterface iface = NetworkInterface.getByInetAddress(address);
/* 130 */         if (address.isReachable(iface, 3, 1000)) {
/* 131 */           return address;
/*     */         }
/*     */       } catch (SocketException e) {
/* 134 */         RobotLog.v(String.format("socket exception while trying to get network interface of %s", new Object[] { address.getHostAddress() }));
/*     */       }
/*     */       catch (IOException e) {
/* 137 */         RobotLog.v(String.format("IO exception while trying to determine if %s is reachable via %s", new Object[] { destAddress.getHostAddress(), address.getHostAddress() }));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 143 */     return Network.getLoopbackAddress();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\RobocolConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */