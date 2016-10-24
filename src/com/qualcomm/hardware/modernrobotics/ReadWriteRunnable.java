/*    */ package com.qualcomm.hardware.modernrobotics;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ 
/*    */ public abstract interface ReadWriteRunnable extends Runnable, com.qualcomm.robotcore.eventloop.SyncdDevice {
/*    */   public static final int MAX_BUFFER_SIZE = 256;
/*    */   
/*    */   public abstract void setCallback(Callback paramCallback);
/*    */   
/*    */   public abstract void blockUntilReady() throws com.qualcomm.robotcore.exception.RobotCoreException, InterruptedException;
/*    */   
/*    */   public abstract void startBlockingWork();
/*    */   
/*    */   public abstract boolean writeNeeded();
/*    */   
/*    */   public static class EmptyCallback implements ReadWriteRunnable.Callback {
/*    */     public void startupComplete() throws InterruptedException
/*    */     {}
/*    */     
/*    */     public void readComplete() throws InterruptedException
/*    */     {}
/*    */     
/*    */     public void writeComplete() throws InterruptedException
/*    */     {}
/*    */     
/*    */     public void shutdownComplete() throws InterruptedException
/*    */     {}
/*    */   }
/*    */   
/*    */   public static abstract interface Callback {
/*    */     public abstract void startupComplete() throws InterruptedException;
/*    */     
/*    */     public abstract void readComplete() throws InterruptedException;
/*    */     
/*    */     public abstract void writeComplete() throws InterruptedException;
/*    */     
/*    */     public abstract void shutdownComplete() throws InterruptedException;
/*    */   }
/*    */   
/*    */   public static enum BlockingState {
/* 41 */     BLOCKING,  WAITING;
/*    */     
/*    */     private BlockingState() {}
/*    */   }
/*    */   
/*    */   public abstract void resetWriteNeeded();
/*    */   
/*    */   public abstract void write(int paramInt, byte[] paramArrayOfByte);
/*    */   
/*    */   public abstract void setAcceptingWrites(boolean paramBoolean);
/*    */   
/*    */   public abstract boolean getAcceptingWrites();
/*    */   
/*    */   public abstract void drainPendingWrites();
/*    */   
/*    */   public abstract byte[] readFromWriteCache(int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract byte[] read(int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract void close();
/*    */   
/*    */   public abstract ReadWriteRunnableSegment createSegment(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   public abstract void destroySegment(int paramInt);
/*    */   
/*    */   public abstract ReadWriteRunnableSegment getSegment(int paramInt);
/*    */   
/*    */   public abstract void queueSegmentRead(int paramInt);
/*    */   
/*    */   public abstract void queueSegmentWrite(int paramInt);
/*    */   
/*    */   public abstract void executeUsing(ExecutorService paramExecutorService);
/*    */   
/*    */   public abstract void run();
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ReadWriteRunnable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */