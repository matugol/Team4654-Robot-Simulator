/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.ArmableUsbDevice.OpenRobotUsbDevice;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public abstract class ModernRoboticsUsbController
/*     */   extends ModernRoboticsUsbDevice
/*     */ {
/*     */   protected WRITE_STATUS writeStatus;
/*     */   protected boolean readWriteRunnableIsRunning;
/*     */   
/*     */   protected static enum WRITE_STATUS
/*     */   {
/*  54 */     IDLE,  DIRTY,  READ;
/*     */     
/*     */     private WRITE_STATUS() {} }
/*     */   
/*  58 */   protected final AtomicInteger callbackWaiterCount = new AtomicInteger();
/*  59 */   protected final AtomicLong readCompletionCount = new AtomicLong();
/*     */   
/*  61 */   protected final Object concurrentClientLock = new Object();
/*  62 */   protected final Object callbackLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModernRoboticsUsbController(Context context, SerialNumber serialNumber, EventLoopManager manager, ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice, ModernRoboticsUsbDevice.CreateReadWriteRunnable createReadWriteRunnable)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/*  71 */     super(context, serialNumber, manager, openRobotUsbDevice, createReadWriteRunnable);
/*  72 */     this.writeStatus = WRITE_STATUS.IDLE;
/*  73 */     this.readWriteRunnableIsRunning = false;
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
/*     */   public void write(int address, byte[] data)
/*     */   {
/*  86 */     synchronized (this.concurrentClientLock)
/*     */     {
/*  88 */       synchronized (this.callbackLock)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */         boolean abandoned = false;
/*  95 */         while (this.writeStatus == WRITE_STATUS.DIRTY)
/*     */         {
/*  97 */           if ((!isArmed()) || (!waitForCallback()))
/*     */           {
/*  99 */             abandoned = true;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 104 */         if ((!abandoned) && (isOkToReadOrWrite()))
/*     */         {
/*     */ 
/* 107 */           this.writeStatus = WRITE_STATUS.DIRTY;
/* 108 */           super.write(address, data);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] read(int address, int size)
/*     */   {
/* 120 */     synchronized (this.concurrentClientLock)
/*     */     {
/* 122 */       synchronized (this.callbackLock)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 127 */         boolean abandoned = false;
/* 128 */         while (this.writeStatus != WRITE_STATUS.IDLE)
/*     */         {
/* 130 */           if ((!isArmed()) || (!waitForCallback()))
/*     */           {
/* 132 */             abandoned = true;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 137 */         if ((!abandoned) && (isOkToReadOrWrite())) {
/* 138 */           return super.read(address, size);
/*     */         }
/* 140 */         return new byte[size];
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeComplete()
/*     */     throws InterruptedException
/*     */   {
/* 148 */     synchronized (this.callbackLock)
/*     */     {
/* 150 */       super.writeComplete();
/*     */       
/* 152 */       if (this.writeStatus == WRITE_STATUS.DIRTY)
/* 153 */         this.writeStatus = WRITE_STATUS.READ;
/* 154 */       this.callbackLock.notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */   public void readComplete() throws InterruptedException
/*     */   {
/* 160 */     synchronized (this.callbackLock)
/*     */     {
/* 162 */       super.readComplete();
/* 163 */       if (this.writeStatus == WRITE_STATUS.READ)
/* 164 */         this.writeStatus = WRITE_STATUS.IDLE;
/* 165 */       this.readCompletionCount.incrementAndGet();
/* 166 */       this.callbackLock.notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   boolean waitForNextReadComplete()
/*     */   {
/* 173 */     synchronized (this.concurrentClientLock)
/*     */     {
/* 175 */       synchronized (this.callbackLock)
/*     */       {
/* 177 */         long cur = this.readCompletionCount.get();
/* 178 */         long target = cur + 1L;
/* 179 */         while (this.readCompletionCount.get() < target)
/*     */         {
/* 181 */           if (!isArmed())
/* 182 */             return false;
/* 183 */           if (!waitForCallback())
/* 184 */             return false;
/*     */         }
/*     */       }
/*     */     }
/* 188 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean isOkToReadOrWrite()
/*     */   {
/* 193 */     return (isArmed()) && (this.readWriteRunnableIsRunning);
/*     */   }
/*     */   
/*     */   public void startupComplete()
/*     */   {
/* 198 */     this.readWriteRunnableIsRunning = true;
/*     */   }
/*     */   
/*     */   public void shutdownComplete()
/*     */   {
/* 203 */     this.readWriteRunnableIsRunning = false;
/* 204 */     synchronized (this.callbackLock)
/*     */     {
/* 206 */       this.writeStatus = WRITE_STATUS.IDLE;
/* 207 */       this.callbackLock.notifyAll();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 212 */     while (this.callbackWaiterCount.get() > 0) {
/* 213 */       Thread.yield();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean waitForCallback()
/*     */   {
/* 222 */     this.callbackWaiterCount.incrementAndGet();
/* 223 */     boolean interrupted = false;
/* 224 */     if (this.readWriteRunnableIsRunning) {
/*     */       try
/*     */       {
/* 227 */         this.callbackLock.wait();
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 231 */         interrupted = true;
/* 232 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
/* 235 */     boolean result = (!interrupted) && (this.readWriteRunnableIsRunning);
/* 236 */     this.callbackWaiterCount.decrementAndGet();
/* 237 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */