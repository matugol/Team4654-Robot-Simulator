/*     */ package org.firstinspires.ftc.robotcore.internal.network;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.os.SystemClock;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import com.qualcomm.robotcore.R.string;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.Heartbeat;
/*     */ import com.qualcomm.robotcore.robocol.PeerApp;
/*     */ import com.qualcomm.robotcore.robocol.RobocolDatagram;
/*     */ import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
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
/*     */ public class SendOnceRunnable
/*     */   implements Runnable
/*     */ {
/*     */   public static final String TAG = "Robocol";
/*     */   public static final boolean DEBUG = false;
/*     */   protected static final double ASSUME_DISCONNECT_TIMER = 2.0D;
/*     */   protected static final int MAX_COMMAND_ATTEMPTS = 10;
/*     */   protected static final long GAMEPAD_UPDATE_THRESHOLD = 1000L;
/*     */   protected static final int MS_HEARTBEAT_TRANSMISSION_INTERVAL = 100;
/*     */   protected ElapsedTime lastRecvPacket;
/*     */   
/*     */   public static class Parameters
/*     */   {
/*  48 */     public boolean disconnectOnTimeout = true;
/*  49 */     public boolean originateHeartbeats = AppUtil.getInstance().getThisApp().isDriverStation();
/*  50 */     public Map<Integer, Gamepad> gamepads = null;
/*  51 */     public Map<Integer, Integer> userToGamepadMap = null;
/*     */     
/*     */     public Parameters() {}
/*     */     
/*  55 */     public Parameters(Map<Integer, Gamepad> gamepads, Map<Integer, Integer> userToGamepadMap) { this.gamepads = gamepads;
/*  56 */       this.userToGamepadMap = userToGamepadMap;
/*     */     }
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
/*  73 */   protected List<Command> pendingCommands = new CopyOnWriteArrayList();
/*  74 */   protected Heartbeat heartbeatSend = new Heartbeat();
/*     */   
/*     */ 
/*     */   protected RobocolDatagramSocket socket;
/*     */   
/*     */ 
/*     */   protected ClientCallback clientCallback;
/*     */   
/*     */ 
/*     */   protected Context context;
/*     */   
/*     */   protected final Parameters parameters;
/*     */   
/*     */ 
/*     */   public SendOnceRunnable(@NonNull Context context, @Nullable ClientCallback clientCallback, @NonNull RobocolDatagramSocket socket, @Nullable ElapsedTime lastRecvPacket, @NonNull Parameters parameters)
/*     */   {
/*  90 */     this.context = context;
/*  91 */     this.clientCallback = clientCallback;
/*  92 */     this.socket = socket;
/*  93 */     this.lastRecvPacket = lastRecvPacket;
/*  94 */     this.parameters = parameters;
/*     */     
/*  96 */     RobotLog.vv("Robocol", "SendOnceRunnable created");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 108 */       if ((this.parameters.disconnectOnTimeout) && (this.lastRecvPacket != null) && (this.lastRecvPacket.time() > 2.0D)) {
/* 109 */         if (this.clientCallback != null) {
/* 110 */           this.clientCallback.peerDisconnected();
/*     */         }
/* 112 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 117 */       if ((this.parameters.originateHeartbeats) && (this.heartbeatSend.getElapsedSeconds() > 0.1D))
/*     */       {
/* 119 */         this.heartbeatSend = Heartbeat.createWithTimeStamp();
/* 120 */         this.heartbeatSend.t0 = Heartbeat.getMsTimeSyncTime();
/* 121 */         RobocolDatagram packetHeartbeat = new RobocolDatagram(this.heartbeatSend);
/* 122 */         send(packetHeartbeat);
/*     */       }
/*     */       
/*     */       long now;
/* 126 */       if ((this.parameters.userToGamepadMap != null) && (this.parameters.gamepads != null)) {
/* 127 */         now = SystemClock.uptimeMillis();
/* 128 */         for (Map.Entry<Integer, Integer> userEntry : this.parameters.userToGamepadMap.entrySet())
/*     */         {
/* 130 */           int user = ((Integer)userEntry.getKey()).intValue();
/* 131 */           int id = ((Integer)userEntry.getValue()).intValue();
/*     */           
/* 133 */           Gamepad gamepad = (Gamepad)this.parameters.gamepads.get(Integer.valueOf(id));
/* 134 */           gamepad.user = ((byte)user);
/*     */           
/*     */ 
/* 137 */           if ((now - gamepad.timestamp <= 1000L) || (!gamepad.atRest()))
/*     */           {
/*     */ 
/* 140 */             gamepad.setSequenceNumber();
/* 141 */             RobocolDatagram packetGamepad = new RobocolDatagram(gamepad);
/* 142 */             send(packetGamepad);
/*     */           }
/*     */         }
/*     */       }
/* 146 */       long nanotimeNow = System.nanoTime();
/*     */       
/*     */ 
/* 149 */       List<Command> commandsToRemove = new ArrayList();
/* 150 */       for (Command command : this.pendingCommands)
/*     */       {
/*     */ 
/* 153 */         if (command.getAttempts() > 10) {
/* 154 */           String msg = String.format(this.context.getString(R.string.configGivingUpOnCommand), new Object[] { command.getName(), Integer.valueOf(command.getSequenceNumber()), Byte.valueOf(command.getAttempts()) });
/* 155 */           RobotLog.vv("Robocol", msg);
/* 156 */           commandsToRemove.add(command);
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/* 161 */         else if ((command.isAcknowledged()) || (command.shouldTransmit(nanotimeNow)))
/*     */         {
/* 163 */           if (!command.isAcknowledged()) {
/* 164 */             RobotLog.vv("Robocol", "sending %s(%d), attempt: %d", new Object[] { command.getName(), Integer.valueOf(command.getSequenceNumber()), Byte.valueOf(command.getAttempts()) });
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 170 */           RobocolDatagram packetCommand = new RobocolDatagram(command);
/* 171 */           send(packetCommand);
/*     */           
/*     */ 
/* 174 */           if (command.isAcknowledged()) commandsToRemove.add(command);
/*     */         }
/*     */       }
/* 177 */       this.pendingCommands.removeAll(commandsToRemove);
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/* 184 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void send(RobocolDatagram datagram) {
/* 189 */     if (this.socket.getInetAddress() != null) {
/* 190 */       this.socket.send(datagram);
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendCommand(Command cmd) {
/* 195 */     this.pendingCommands.add(cmd);
/*     */   }
/*     */   
/*     */   public boolean removeCommand(Command cmd) {
/* 199 */     return this.pendingCommands.remove(cmd);
/*     */   }
/*     */   
/* 202 */   public void clearCommands() { this.pendingCommands.clear(); }
/*     */   
/*     */   public static abstract interface ClientCallback
/*     */   {
/*     */     public abstract void peerConnected(boolean paramBoolean);
/*     */     
/*     */     public abstract void peerDisconnected();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\network\SendOnceRunnable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */