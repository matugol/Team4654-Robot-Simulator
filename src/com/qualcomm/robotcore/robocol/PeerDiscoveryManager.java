/*     */ package com.qualcomm.robotcore.robocol;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import java.net.InetAddress;
/*     */ import java.util.concurrent.CountDownLatch;
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
/*     */ public class PeerDiscoveryManager
/*     */ {
/*     */   public static final String TAG = "PeerDiscovery";
/*     */   private static final boolean DEBUG = false;
/*     */   private InetAddress peerDiscoveryDevice;
/*     */   private final RobocolDatagramSocket socket;
/*     */   private ScheduledExecutorService discoveryLoopService;
/*     */   private ScheduledFuture<?> discoveryLoopFuture;
/*     */   private final PeerDiscovery message;
/*     */   
/*     */   private class PeerDiscoveryRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private PeerDiscoveryRunnable() {}
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*  59 */         RobocolDatagram packet = new RobocolDatagram(PeerDiscoveryManager.this.message);
/*  60 */         if (PeerDiscoveryManager.this.socket.getInetAddress() == null) packet.setAddress(PeerDiscoveryManager.this.peerDiscoveryDevice);
/*  61 */         PeerDiscoveryManager.this.socket.send(packet);
/*     */       } catch (RobotCoreException e) {
/*  63 */         RobotLog.ee("PeerDiscovery", "Unable to send peer discovery packet: " + e.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private CountDownLatch interlock = new CountDownLatch(0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeerDiscoveryManager(RobocolDatagramSocket socket, InetAddress peerDiscoveryDevice)
/*     */   {
/*  80 */     this.socket = socket;
/*  81 */     this.message = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
/*  82 */     this.peerDiscoveryDevice = peerDiscoveryDevice;
/*  83 */     start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public InetAddress getPeerDiscoveryDevice()
/*     */   {
/*  91 */     return this.peerDiscoveryDevice;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void start()
/*     */   {
/*  98 */     RobotLog.vv("PeerDiscovery", "Starting peer discovery");
/*     */     
/* 100 */     if (this.peerDiscoveryDevice == this.socket.getLocalAddress())
/*     */     {
/* 102 */       RobotLog.vv("PeerDiscovery", "No need for peer discovery, we are the peer discovery device");
/*     */     }
/*     */     else {
/* 105 */       this.discoveryLoopService = ThreadPool.newSingleThreadScheduledExecutor();
/* 106 */       this.discoveryLoopFuture = this.discoveryLoopService.scheduleAtFixedRate(new PeerDiscoveryRunnable(null), 1L, 1L, TimeUnit.SECONDS);
/*     */     }
/*     */     
/* 109 */     this.interlock.countDown();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void stop()
/*     */   {
/* 116 */     RobotLog.vv("PeerDiscovery", "Stopping peer discovery");
/*     */     try
/*     */     {
/* 119 */       this.interlock.await();
/*     */     } catch (InterruptedException e) {
/* 121 */       Thread.currentThread().interrupt();
/*     */     }
/*     */     
/* 124 */     if (this.discoveryLoopFuture != null) {
/* 125 */       this.discoveryLoopFuture.cancel(true);
/* 126 */       this.discoveryLoopFuture = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\PeerDiscoveryManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */