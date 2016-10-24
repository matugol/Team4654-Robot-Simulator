/*    */ package org.firstinspires.ftc.robotcore.internal.network;
/*    */ 
/*    */ import android.support.annotation.NonNull;
/*    */ import android.support.annotation.Nullable;
/*    */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*    */ import com.qualcomm.robotcore.robocol.Command;
/*    */ import com.qualcomm.robotcore.robocol.Heartbeat;
/*    */ import com.qualcomm.robotcore.robocol.RobocolDatagram;
/*    */ import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
/*    */ import com.qualcomm.robotcore.robocol.RobocolParsable.MsgType;
/*    */ import com.qualcomm.robotcore.util.ElapsedTime;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ import com.qualcomm.robotcore.util.ThreadPool;
/*    */ import java.util.concurrent.LinkedBlockingDeque;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecvLoopRunnable
/*    */   implements Runnable
/*    */ {
/*    */   public static final String TAG = "Robocol";
/*    */   protected ElapsedTime lastRecvPacket;
/*    */   protected RobocolDatagramSocket socket;
/*    */   protected RecvLoopCallback callback;
/* 36 */   protected LinkedBlockingDeque<Command> commandsToProcess = new LinkedBlockingDeque();
/*    */   
/*    */   public RecvLoopRunnable(RecvLoopCallback callback, @NonNull RobocolDatagramSocket socket, @Nullable ElapsedTime lastRecvPacket) {
/* 39 */     this.callback = callback;
/* 40 */     this.socket = socket;
/* 41 */     this.lastRecvPacket = lastRecvPacket;
/* 42 */     RobotLog.vv("Robocol", "RecvLoopRunnable created");
/*    */   }
/*    */   
/*    */ 
/* 46 */   public void setCallback(RecvLoopCallback callback) { this.callback = callback; }
/*    */   
/*    */   public class CommandProcessor implements Runnable {
/*    */     public CommandProcessor() {}
/*    */     
/* 51 */     public void run() { while (!Thread.currentThread().isInterrupted()) {
/*    */         try
/*    */         {
/* 54 */           Command command = (Command)RecvLoopRunnable.this.commandsToProcess.takeFirst();
/* 55 */           RecvLoopRunnable.this.callback.commandEvent(command);
/*    */         }
/*    */         catch (InterruptedException e) {
/* 58 */           return;
/*    */         }
/*    */         catch (RobotCoreException|RuntimeException e) {
/* 61 */           RobotLog.ee("Robocol", "exception in %s: %s", new Object[] { Thread.currentThread().getName(), e.getMessage() });
/* 62 */           RobotLog.logStacktrace(e);
/* 63 */           RecvLoopRunnable.this.callback.reportGlobalError(e.getMessage(), false);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 71 */     ThreadPool.logThreadLifeCycle("RecvLoopRunnable.run()", new Runnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 75 */         while (!Thread.interrupted())
/*    */         {
/*    */ 
/*    */ 
/* 79 */           RobocolDatagram packet = RecvLoopRunnable.this.socket.recv();
/* 80 */           long tReceived = Heartbeat.getMsTimeSyncTime();
/*    */           
/*    */ 
/* 83 */           if (Thread.interrupted()) {
/* 84 */             return;
/*    */           }
/*    */           
/* 87 */           if (packet == null) {
/* 88 */             if (RecvLoopRunnable.this.socket.isClosed()) {
/* 89 */               RobotLog.vv("Robocol", "socket closed; %s returning", new Object[] { Thread.currentThread().getName() });
/* 90 */               return;
/*    */             }
/* 92 */             Thread.yield();
/*    */           }
/*    */           else {
/* 95 */             if (RecvLoopRunnable.this.lastRecvPacket != null) RecvLoopRunnable.this.lastRecvPacket.reset();
/*    */             try
/*    */             {
/* 98 */               if (RecvLoopRunnable.this.callback.packetReceived(packet) != CallbackResult.HANDLED)
/*    */               {
/* :0 */                 switch (RecvLoopRunnable.2.$SwitchMap$com$qualcomm$robotcore$robocol$RobocolParsable$MsgType[packet.getMsgType().ordinal()])
/*    */                 {
/*    */                 case 1: 
/* :3 */                   RecvLoopRunnable.this.callback.peerDiscoveryEvent(packet);
/* :4 */                   break;
/*    */                 case 2: 
/* :6 */                   RecvLoopRunnable.this.callback.heartbeatEvent(packet, tReceived);
/* :7 */                   break;
/*    */                 
/*    */ 
/*    */ 
/*    */ 
/*    */                 case 3: 
/* ;3 */                   Command command = new Command(packet.getData());
/* ;4 */                   RobotLog.vv("Robocol", "received command: %s(%d) %s", new Object[] { command.getName(), Integer.valueOf(command.getSequenceNumber()), command.getExtra() });
/* ;5 */                   CallbackResult result = NetworkConnectionHandler.getInstance().processAcknowledgments(command);
/* ;6 */                   if (!result.isHandled()) {
/* ;7 */                     RecvLoopRunnable.this.commandsToProcess.addLast(command);
/*    */                   }
/*    */                   break;
/*    */                 case 4: 
/* <1 */                   RecvLoopRunnable.this.callback.telemetryEvent(packet);
/* <2 */                   break;
/*    */                 case 5: 
/* <4 */                   RecvLoopRunnable.this.callback.gamepadEvent(packet);
/* <5 */                   break;
/*    */                 case 6: 
/* <7 */                   RecvLoopRunnable.this.callback.emptyEvent(packet);
/* <8 */                   break;
/*    */                 default: 
/* =0 */                   RobotLog.ee("Robocol", "Unhandled message type: " + packet.getMsgType().name());
/*    */                 }
/*    */               }
/*    */             }
/*    */             catch (RobotCoreException|RuntimeException e) {
/* =5 */               RobotLog.ee("Robocol", "exception in %s: %s", new Object[] { Thread.currentThread().getName(), e.getMessage() });
/* =6 */               RobotLog.logStacktrace(e);
/* =7 */               RecvLoopRunnable.this.callback.reportGlobalError(e.getMessage(), false);
/*    */             }
/*    */             finally {
/* >0 */               packet.close();
/*    */             }
/*    */           } }
/* >3 */         RobotLog.vv("Robocol", "interrupted; %s returning", new Object[] { Thread.currentThread().getName() });
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */   public static abstract interface RecvLoopCallback
/*    */   {
/*    */     public abstract CallbackResult packetReceived(RobocolDatagram paramRobocolDatagram)
/*    */       throws RobotCoreException;
/*    */     
/*    */     public abstract CallbackResult peerDiscoveryEvent(RobocolDatagram paramRobocolDatagram)
/*    */       throws RobotCoreException;
/*    */     
/*    */     public abstract CallbackResult heartbeatEvent(RobocolDatagram paramRobocolDatagram, long paramLong)
/*    */       throws RobotCoreException;
/*    */     
/*    */     public abstract CallbackResult commandEvent(Command paramCommand)
/*    */       throws RobotCoreException;
/*    */     
/*    */     public abstract CallbackResult telemetryEvent(RobocolDatagram paramRobocolDatagram)
/*    */       throws RobotCoreException;
/*    */     
/*    */     public abstract CallbackResult gamepadEvent(RobocolDatagram paramRobocolDatagram)
/*    */       throws RobotCoreException;
/*    */     
/*    */     public abstract CallbackResult emptyEvent(RobocolDatagram paramRobocolDatagram)
/*    */       throws RobotCoreException;
/*    */     
/*    */     public abstract CallbackResult reportGlobalError(String paramString, boolean paramBoolean);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\network\RecvLoopRunnable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */