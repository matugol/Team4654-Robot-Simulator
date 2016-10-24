/*    */ package org.firstinspires.ftc.robotcore.internal.network;
/*    */ 
/*    */ import com.qualcomm.robotcore.robocol.PeerDiscoveryManager;
/*    */ import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
/*    */ import com.qualcomm.robotcore.util.ElapsedTime;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ import com.qualcomm.robotcore.util.ThreadPool;
/*    */ import com.qualcomm.robotcore.wifi.NetworkConnection;
/*    */ import java.net.SocketException;
/*    */ import java.util.concurrent.CountDownLatch;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetupRunnable
/*    */   implements Runnable
/*    */ {
/*    */   protected RobocolDatagramSocket socket;
/*    */   protected NetworkConnection networkConnection;
/*    */   protected PeerDiscoveryManager peerDiscoveryManager;
/*    */   protected ExecutorService recvLoopService;
/*    */   protected RecvLoopRunnable.RecvLoopCallback recvLoopCallback;
/*    */   protected ElapsedTime lastRecvPacket;
/*    */   protected CountDownLatch countDownLatch;
/*    */   protected SocketConnect socketConnect;
/*    */   
/*    */   public SetupRunnable(RecvLoopRunnable.RecvLoopCallback recvLoopCallback, NetworkConnection networkConnection, ElapsedTime lastRecvPacket, SocketConnect socketConnect)
/*    */   {
/* 33 */     this.recvLoopCallback = recvLoopCallback;
/* 34 */     this.networkConnection = networkConnection;
/* 35 */     this.lastRecvPacket = lastRecvPacket;
/* 36 */     this.countDownLatch = new CountDownLatch(1);
/* 37 */     this.socketConnect = socketConnect;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 42 */     ThreadPool.logThreadLifeCycle("SetupRunnable.run()", new Runnable()
/*    */     {
/*    */       public void run() {
/*    */         try {
/* 46 */           if (SetupRunnable.this.socket != null) SetupRunnable.this.socket.close();
/* 47 */           SetupRunnable.this.socket = new RobocolDatagramSocket();
/* 48 */           SetupRunnable.this.socket.listenUsingDestination(SetupRunnable.this.networkConnection.getConnectionOwnerAddress());
/* 49 */           if (SetupRunnable.this.socketConnect == SocketConnect.CONNECTION_OWNER) {
/* 50 */             SetupRunnable.this.socket.connect(SetupRunnable.this.networkConnection.getConnectionOwnerAddress());
/*    */           }
/*    */         } catch (SocketException e) {
/* 53 */           RobotLog.e("Failed to open socket: " + e.toString());
/*    */         }
/*    */         
/*    */ 
/* 57 */         SetupRunnable.this.recvLoopService = Executors.newFixedThreadPool(2);
/* 58 */         RecvLoopRunnable recvLoopRunnable = new RecvLoopRunnable(SetupRunnable.this.recvLoopCallback, SetupRunnable.this.socket, SetupRunnable.this.lastRecvPacket); RecvLoopRunnable 
/* 59 */           tmp161_160 = recvLoopRunnable;tmp161_160.getClass();RecvLoopRunnable.CommandProcessor commandProcessor = new RecvLoopRunnable.CommandProcessor(tmp161_160);
/* 60 */         NetworkConnectionHandler.getInstance().setRecvLoopRunnable(recvLoopRunnable);
/* 61 */         SetupRunnable.this.recvLoopService.execute(commandProcessor);
/* 62 */         SetupRunnable.this.recvLoopService.execute(recvLoopRunnable);
/*    */         
/*    */ 
/* 65 */         if (SetupRunnable.this.peerDiscoveryManager != null) {
/* 66 */           SetupRunnable.this.peerDiscoveryManager.stop();
/*    */         }
/* 68 */         SetupRunnable.this.peerDiscoveryManager = new PeerDiscoveryManager(SetupRunnable.this.socket, SetupRunnable.this.networkConnection.getConnectionOwnerAddress());
/*    */         
/*    */ 
/* 71 */         SetupRunnable.this.countDownLatch.countDown();
/*    */         
/*    */ 
/* 74 */         RobotLog.v("Setup complete");
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */   public RobocolDatagramSocket getSocket() {
/* 80 */     return this.socket;
/*    */   }
/*    */   
/*    */   public void shutdown()
/*    */   {
/*    */     try {
/* 86 */       this.countDownLatch.await();
/*    */     } catch (InterruptedException e) {
/* 88 */       Thread.currentThread().interrupt();
/*    */     }
/*    */     
/* 91 */     if (this.recvLoopService != null) {
/* 92 */       this.recvLoopService.shutdownNow();
/* 93 */       ThreadPool.awaitTerminationOrExitApplication(this.recvLoopService, 5L, TimeUnit.SECONDS, "ReceiveLoopService", "internal error");
/* 94 */       this.recvLoopService = null;
/*    */     }
/*    */     
/* 97 */     if (this.peerDiscoveryManager != null) {
/* 98 */       this.peerDiscoveryManager.stop();
/* 99 */       this.peerDiscoveryManager = null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\network\SetupRunnable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */