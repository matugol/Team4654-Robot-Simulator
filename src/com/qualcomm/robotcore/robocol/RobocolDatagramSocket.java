/*     */ package com.qualcomm.robotcore.robocol;
/*     */ 
/*     */ import android.support.annotation.Nullable;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RobocolDatagramSocket
/*     */ {
/*     */   public static final String TAG = "Robocol";
/*     */   private static final boolean DEBUG = false;
/*     */   private DatagramSocket socket;
/*     */   private volatile State state;
/*     */   
/*     */   public static enum State
/*     */   {
/*  27 */     LISTENING, 
/*  28 */     CLOSED, 
/*  29 */     ERROR;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */   
/*  34 */   private final Object recvLock = new Object();
/*  35 */   private final Object sendLock = new Object();
/*  36 */   private final Object bindCloseLock = new Object();
/*  37 */   private boolean sendErrorReported = false;
/*  38 */   private boolean recvErrorReported = false;
/*     */   
/*     */   public RobocolDatagramSocket() {
/*  41 */     this.state = State.CLOSED;
/*     */   }
/*     */   
/*     */   public void listenUsingDestination(InetAddress destAddress) throws SocketException {
/*  45 */     bind(new InetSocketAddress(RobocolConfig.determineBindAddress(destAddress), 20884));
/*     */   }
/*     */   
/*     */   public void bind(InetSocketAddress bindAddress) throws SocketException {
/*  49 */     synchronized (this.bindCloseLock) {
/*  50 */       if (this.state != State.CLOSED) {
/*  51 */         close();
/*     */       }
/*  53 */       this.state = State.LISTENING;
/*     */       
/*     */ 
/*  56 */       RobotLog.dd("Robocol", "RobocolDatagramSocket listening at " + bindAddress.toString());
/*  57 */       this.socket = new DatagramSocket(bindAddress);
/*  58 */       this.sendErrorReported = false;
/*  59 */       this.recvErrorReported = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public void connect(InetAddress connectAddress) throws SocketException {
/*  64 */     InetSocketAddress addr = new InetSocketAddress(connectAddress, 20884);
/*  65 */     RobotLog.dd("Robocol", "RobocolDatagramSocket connected to " + addr.toString());
/*  66 */     this.socket.connect(addr);
/*     */   }
/*     */   
/*     */   public void close() {
/*  70 */     synchronized (this.bindCloseLock) {
/*  71 */       this.state = State.CLOSED;
/*     */       
/*  73 */       if (this.socket != null) { this.socket.close();
/*     */       }
/*  75 */       RobotLog.dd("Robocol", "RobocolDatagramSocket is closed");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void send(RobocolDatagram message)
/*     */   {
/*  82 */     synchronized (this.sendLock) {
/*     */       try {
/*  84 */         this.socket.send(message.getPacket());
/*     */       }
/*     */       catch (IOException|RuntimeException e) {
/*  87 */         if (!this.sendErrorReported) {
/*  88 */           this.sendErrorReported = true;
/*  89 */           RobotLog.ww("Robocol", "Unable to send RobocolDatagram: " + e.toString());
/*  90 */           RobotLog.ww("Robocol", "               " + message.toString());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   public RobocolDatagram recv()
/*     */   {
/* 102 */     synchronized (this.recvLock) {
/* 103 */       RobocolDatagram result = RobocolDatagram.forReceive();
/* 104 */       DatagramPacket packetRecv = result.getPacket();
/*     */       
/*     */       try
/*     */       {
/* 108 */         this.socket.receive(packetRecv);
/*     */       }
/*     */       catch (IOException|RuntimeException e) {
/* 111 */         if (!this.recvErrorReported) {
/* 112 */           this.recvErrorReported = true;
/* 113 */           RobotLog.dd("Robocol", "no network packet received");
/*     */         }
/* 115 */         return null;
/*     */       }
/*     */       
/* 118 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   public State getState() {
/* 123 */     return this.state;
/*     */   }
/*     */   
/*     */   public InetAddress getInetAddress() {
/* 127 */     if (this.socket == null) { return null;
/*     */     }
/* 129 */     return this.socket.getInetAddress();
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 133 */     if (this.socket == null) { return null;
/*     */     }
/* 135 */     return this.socket.getLocalAddress();
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/* 139 */     return this.state == State.LISTENING;
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 143 */     return this.state == State.CLOSED;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\RobocolDatagramSocket.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */