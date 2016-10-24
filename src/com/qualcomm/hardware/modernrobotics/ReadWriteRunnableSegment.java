/*    */ package com.qualcomm.hardware.modernrobotics;
/*    */ 
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import java.util.concurrent.locks.ReentrantLock;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReadWriteRunnableSegment
/*    */ {
/*    */   private int address;
/*    */   final Lock lockRead;
/*    */   private final byte[] bufferRead;
/*    */   final Lock lockWrite;
/*    */   private final byte[] bufferWrite;
/*    */   
/*    */   public ReadWriteRunnableSegment(int address, int size)
/*    */   {
/* 47 */     this.address = address;
/* 48 */     this.lockRead = new ReentrantLock();
/* 49 */     this.bufferRead = new byte[size];
/* 50 */     this.lockWrite = new ReentrantLock();
/* 51 */     this.bufferWrite = new byte[size];
/*    */   }
/*    */   
/*    */   public int getAddress() {
/* 55 */     return this.address;
/*    */   }
/*    */   
/*    */   public void setAddress(int address) {
/* 59 */     this.address = address;
/*    */   }
/*    */   
/*    */   public Lock getReadLock() {
/* 63 */     return this.lockRead;
/*    */   }
/*    */   
/*    */   public byte[] getReadBuffer() {
/* 67 */     return this.bufferRead;
/*    */   }
/*    */   
/*    */   public Lock getWriteLock() {
/* 71 */     return this.lockWrite;
/*    */   }
/*    */   
/*    */   public byte[] getWriteBuffer() {
/* 75 */     return this.bufferWrite;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 79 */     return String.format("Segment - address:%d read:%d write:%d", new Object[] { Integer.valueOf(this.address), Integer.valueOf(this.bufferRead.length), Integer.valueOf(this.bufferWrite.length) });
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ReadWriteRunnableSegment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */