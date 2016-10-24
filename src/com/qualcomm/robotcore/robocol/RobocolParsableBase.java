/*     */ package com.qualcomm.robotcore.robocol;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public abstract class RobocolParsableBase
/*     */   implements RobocolParsable
/*     */ {
/*     */   protected int sequenceNumber;
/*     */   protected long nanotimeTransmit;
/*     */   protected static final long nanotimeTransmitInterval = 200000000L;
/*  59 */   protected static AtomicInteger nextSequenceNumber = new AtomicInteger();
/*     */   
/*     */ 
/*     */   public static void initializeSequenceNumber(int sequenceNumber)
/*     */   {
/*  64 */     nextSequenceNumber = new AtomicInteger(sequenceNumber);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RobocolParsableBase()
/*     */   {
/*  73 */     setSequenceNumber();
/*  74 */     this.nanotimeTransmit = 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSequenceNumber()
/*     */   {
/*  83 */     return this.sequenceNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSequenceNumber(short sequenceNumber)
/*     */   {
/*  90 */     this.sequenceNumber = TypeConversion.unsignedShortToInt(sequenceNumber);
/*     */   }
/*     */   
/*     */   public void setSequenceNumber()
/*     */   {
/*  95 */     setSequenceNumber((short)nextSequenceNumber.getAndIncrement());
/*     */   }
/*     */   
/*     */   public byte[] toByteArrayForTransmission()
/*     */     throws RobotCoreException
/*     */   {
/* 101 */     byte[] result = toByteArray();
/* 102 */     this.nanotimeTransmit = System.nanoTime();
/* 103 */     return result;
/*     */   }
/*     */   
/*     */   public boolean shouldTransmit(long nanotimeNow)
/*     */   {
/* 108 */     return (this.nanotimeTransmit == 0L) || (nanotimeNow - this.nanotimeTransmit > 200000000L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteBuffer allocateWholeWriteBuffer(int overallSize)
/*     */   {
/* 117 */     return ByteBuffer.allocate(overallSize);
/*     */   }
/*     */   
/*     */   protected ByteBuffer getWholeReadBuffer(byte[] byteArray)
/*     */   {
/* 122 */     return ByteBuffer.wrap(byteArray);
/*     */   }
/*     */   
/*     */   protected ByteBuffer getWriteBuffer(int payloadSize)
/*     */   {
/* 127 */     ByteBuffer result = allocateWholeWriteBuffer(5 + payloadSize);
/*     */     
/* 129 */     result.put(getRobocolMsgType().asByte());
/* 130 */     result.putShort((short)payloadSize);
/* 131 */     result.putShort((short)this.sequenceNumber);
/*     */     
/* 133 */     return result;
/*     */   }
/*     */   
/*     */   protected ByteBuffer getReadBuffer(byte[] byteArray)
/*     */   {
/* 138 */     int cbHeaderWithoutSeqNum = 3;
/* 139 */     ByteBuffer result = ByteBuffer.wrap(byteArray, cbHeaderWithoutSeqNum, byteArray.length - cbHeaderWithoutSeqNum);
/*     */     
/* 141 */     setSequenceNumber(result.getShort());
/*     */     
/* 143 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\RobocolParsableBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */