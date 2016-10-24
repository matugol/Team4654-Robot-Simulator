/*     */ package com.qualcomm.robotcore.robocol;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
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
/*     */ 
/*     */ public class Heartbeat
/*     */   extends RobocolParsableBase
/*     */ {
/*     */   public static final short PAYLOAD_SIZE = 33;
/*     */   private long timestamp;
/*     */   private RobotState robotState;
/*     */   public long t0;
/*     */   public long t1;
/*     */   public long t2;
/*     */   
/*     */   public Heartbeat()
/*     */   {
/*  69 */     this.timestamp = 0L;
/*  70 */     this.robotState = RobotState.NOT_STARTED;
/*  71 */     this.t0 = (this.t1 = this.t2 = 0L);
/*     */   }
/*     */   
/*     */   public static Heartbeat createWithTimeStamp() {
/*  75 */     Heartbeat result = new Heartbeat();
/*  76 */     result.timestamp = System.nanoTime();
/*  77 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getMsTimeSyncTime()
/*     */   {
/*  86 */     return System.currentTimeMillis();
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
/*     */   public long getTimestamp()
/*     */   {
/* 100 */     return this.timestamp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getElapsedSeconds()
/*     */   {
/* 110 */     return (System.nanoTime() - this.timestamp) / 1.0E9D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RobocolParsable.MsgType getRobocolMsgType()
/*     */   {
/* 119 */     return RobocolParsable.MsgType.HEARTBEAT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getRobotState()
/*     */   {
/* 127 */     return this.robotState.asByte();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRobotState(RobotState state)
/*     */   {
/* 134 */     this.robotState = state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] toByteArray()
/*     */     throws RobotCoreException
/*     */   {
/* 146 */     ByteBuffer buffer = getWriteBuffer(33);
/*     */     try {
/* 148 */       buffer.putLong(this.timestamp);
/* 149 */       buffer.put(this.robotState.asByte());
/* 150 */       buffer.putLong(this.t0);
/* 151 */       buffer.putLong(this.t1);
/* 152 */       buffer.putLong(this.t2);
/*     */     } catch (BufferOverflowException e) {
/* 154 */       RobotLog.logStacktrace(e);
/*     */     }
/* 156 */     return buffer.array();
/*     */   }
/*     */   
/*     */ 
/*     */   public void fromByteArray(byte[] byteArray)
/*     */     throws RobotCoreException
/*     */   {
/*     */     try
/*     */     {
/* 165 */       ByteBuffer byteBuffer = getReadBuffer(byteArray);
/* 166 */       this.timestamp = byteBuffer.getLong();
/* 167 */       this.robotState = RobotState.fromByte(byteBuffer.get());
/* 168 */       this.t0 = byteBuffer.getLong();
/* 169 */       this.t1 = byteBuffer.getLong();
/* 170 */       this.t2 = byteBuffer.getLong();
/*     */     } catch (BufferUnderflowException e) {
/* 172 */       throw RobotCoreException.createChained(e, "incoming packet too small", new Object[0]);
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
/*     */   public String toString()
/*     */   {
/* 186 */     return String.format("Heartbeat - seq: %4d, time: %d", new Object[] { Integer.valueOf(getSequenceNumber()), Long.valueOf(this.timestamp) });
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\Heartbeat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */