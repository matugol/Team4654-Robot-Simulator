/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.HardwareFactory;
/*     */ import com.qualcomm.hardware.R.string;
/*     */ import com.qualcomm.modernrobotics.ReadWriteRunnableUsbHandler;
/*     */ import com.qualcomm.robotcore.exception.FTDeviceClosedException;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.Channel;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import com.qualcomm.robotcore.util.GlobalWarningSource;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.locks.Lock;
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
/*     */ public class ReadWriteRunnableStandard
/*     */   implements ReadWriteRunnable
/*     */ {
/*  67 */   protected final byte[] localDeviceReadCache = new byte['Ā'];
/*  68 */   protected final byte[] localDeviceWriteCache = new byte['Ā'];
/*     */   
/*  70 */   protected Map<Integer, ReadWriteRunnableSegment> segments = new HashMap();
/*  71 */   protected ConcurrentLinkedQueue<Integer> segmentReadQueue = new ConcurrentLinkedQueue();
/*  72 */   protected ConcurrentLinkedQueue<Integer> segmentWriteQueue = new ConcurrentLinkedQueue();
/*     */   
/*     */   protected final Context context;
/*     */   
/*     */   protected final SerialNumber serialNumber;
/*     */   
/*     */   protected RobotUsbDevice robotUsbDevice;
/*     */   protected ReadWriteRunnableUsbHandler usbHandler;
/*     */   protected int startAddress;
/*     */   protected int monitorLength;
/*  82 */   protected CountDownLatch runningInterlock = new CountDownLatch(1);
/*  83 */   protected volatile boolean running = false;
/*  84 */   protected volatile boolean shutdownAbnormally = false;
/*  85 */   protected volatile boolean shutdownComplete = false;
/*  86 */   private volatile boolean writeNeeded = false;
/*  87 */   protected final Object acceptingWritesLock = new Object();
/*  88 */   protected volatile boolean acceptingWrites = false;
/*     */   
/*     */ 
/*     */ 
/*     */   protected ReadWriteRunnable.Callback callback;
/*     */   
/*     */ 
/*     */ 
/*     */   protected RobotUsbModule owner;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final boolean DEBUG_LOGGING;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReadWriteRunnableStandard(Context context, SerialNumber serialNumber, RobotUsbDevice device, int monitorLength, int startAddress, boolean debug)
/*     */   {
/* 107 */     this.context = context;
/* 108 */     this.serialNumber = serialNumber;
/* 109 */     this.startAddress = startAddress;
/* 110 */     this.monitorLength = monitorLength;
/* 111 */     this.DEBUG_LOGGING = debug;
/* 112 */     this.callback = new ReadWriteRunnable.EmptyCallback();
/* 113 */     this.owner = null;
/* 114 */     this.robotUsbDevice = device;
/* 115 */     this.usbHandler = new ReadWriteRunnableUsbHandler(device);
/*     */   }
/*     */   
/*     */   public void setCallback(ReadWriteRunnable.Callback callback) {
/* 119 */     this.callback = callback;
/*     */   }
/*     */   
/*     */   public void setOwner(RobotUsbModule owner)
/*     */   {
/* 124 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public RobotUsbModule getOwner()
/*     */   {
/* 129 */     return this.owner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void blockUntilReady()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startBlockingWork() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean writeNeeded()
/*     */   {
/* 154 */     return this.writeNeeded;
/*     */   }
/*     */   
/*     */   public void resetWriteNeeded()
/*     */   {
/* 159 */     this.writeNeeded = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(int address, byte[] data)
/*     */   {
/* 170 */     synchronized (this.acceptingWritesLock) {
/* 171 */       if (this.acceptingWrites) {
/* 172 */         synchronized (this.localDeviceWriteCache) {
/* 173 */           System.arraycopy(data, 0, this.localDeviceWriteCache, address, data.length);
/* 174 */           this.writeNeeded = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public byte[] readFromWriteCache(int address, int size)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 3	com/qualcomm/hardware/modernrobotics/ReadWriteRunnableStandard:localDeviceWriteCache	[B
/*     */     //   4: dup
/*     */     //   5: astore_3
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 3	com/qualcomm/hardware/modernrobotics/ReadWriteRunnableStandard:localDeviceWriteCache	[B
/*     */     //   11: iload_1
/*     */     //   12: iload_1
/*     */     //   13: iload_2
/*     */     //   14: iadd
/*     */     //   15: invokestatic 35	java/util/Arrays:copyOfRange	([BII)[B
/*     */     //   18: aload_3
/*     */     //   19: monitorexit
/*     */     //   20: areturn
/*     */     //   21: astore 4
/*     */     //   23: aload_3
/*     */     //   24: monitorexit
/*     */     //   25: aload 4
/*     */     //   27: athrow
/*     */     // Line number table:
/*     */     //   Java source line #189	-> byte code offset #0
/*     */     //   Java source line #190	-> byte code offset #7
/*     */     //   Java source line #191	-> byte code offset #21
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	28	0	this	ReadWriteRunnableStandard
/*     */     //   0	28	1	address	int
/*     */     //   0	28	2	size	int
/*     */     //   5	19	3	Ljava/lang/Object;	Object
/*     */     //   21	5	4	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	20	21	finally
/*     */     //   21	25	21	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public byte[] read(int address, int size)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 2	com/qualcomm/hardware/modernrobotics/ReadWriteRunnableStandard:localDeviceReadCache	[B
/*     */     //   4: dup
/*     */     //   5: astore_3
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 2	com/qualcomm/hardware/modernrobotics/ReadWriteRunnableStandard:localDeviceReadCache	[B
/*     */     //   11: iload_1
/*     */     //   12: iload_1
/*     */     //   13: iload_2
/*     */     //   14: iadd
/*     */     //   15: invokestatic 35	java/util/Arrays:copyOfRange	([BII)[B
/*     */     //   18: aload_3
/*     */     //   19: monitorexit
/*     */     //   20: areturn
/*     */     //   21: astore 4
/*     */     //   23: aload_3
/*     */     //   24: monitorexit
/*     */     //   25: aload 4
/*     */     //   27: athrow
/*     */     // Line number table:
/*     */     //   Java source line #202	-> byte code offset #0
/*     */     //   Java source line #203	-> byte code offset #7
/*     */     //   Java source line #204	-> byte code offset #21
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	28	0	this	ReadWriteRunnableStandard
/*     */     //   0	28	1	address	int
/*     */     //   0	28	2	size	int
/*     */     //   5	19	3	Ljava/lang/Object;	Object
/*     */     //   21	5	4	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	20	21	finally
/*     */     //   21	25	21	finally
/*     */   }
/*     */   
/*     */   public void executeUsing(ExecutorService service)
/*     */   {
/* 210 */     synchronized (this) {
/* 211 */       service.execute(this);
/* 212 */       awaitRunning();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 222 */     synchronized (this)
/*     */     {
/* 224 */       if (this.running)
/*     */       {
/*     */ 
/* 227 */         this.robotUsbDevice.requestInterrupt();
/*     */         
/*     */ 
/* 230 */         this.running = false;
/*     */         
/*     */         try
/*     */         {
/* 234 */           blockUntilReady();
/*     */           
/*     */ 
/* 237 */           startBlockingWork();
/*     */         }
/*     */         catch (InterruptedException e) {
/* 240 */           RobotLog.w("Exception while closing USB device: " + e.getMessage());
/*     */         }
/*     */         catch (RobotCoreException e) {
/* 243 */           RobotLog.w("Exception while closing USB device: " + e.getMessage());
/*     */         }
/*     */         
/* 246 */         while (!this.shutdownComplete) Thread.yield();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ReadWriteRunnableSegment createSegment(int key, int address, int size) {
/* 252 */     ReadWriteRunnableSegment segment = new ReadWriteRunnableSegment(address, size);
/* 253 */     this.segments.put(Integer.valueOf(key), segment);
/* 254 */     return segment;
/*     */   }
/*     */   
/*     */   public void destroySegment(int key) {
/* 258 */     this.segments.remove(Integer.valueOf(key));
/*     */   }
/*     */   
/*     */   public ReadWriteRunnableSegment getSegment(int key) {
/* 262 */     return (ReadWriteRunnableSegment)this.segments.get(Integer.valueOf(key));
/*     */   }
/*     */   
/*     */   public void queueSegmentRead(int key) {
/* 266 */     queueIfNotAlreadyQueued(key, this.segmentReadQueue);
/*     */   }
/*     */   
/*     */   public void queueSegmentWrite(int key) {
/* 270 */     synchronized (this.acceptingWritesLock) {
/* 271 */       if (this.acceptingWrites) {
/* 272 */         queueIfNotAlreadyQueued(key, this.segmentWriteQueue);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void awaitRunning()
/*     */   {
/*     */     try
/*     */     {
/* 282 */       this.runningInterlock.await();
/*     */     }
/*     */     catch (InterruptedException ignored) {
/* 285 */       while (this.runningInterlock.getCount() != 0L) {
/* 286 */         Thread.yield();
/*     */       }
/* 288 */       Thread.currentThread().interrupt();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 299 */     if (this.shutdownComplete) {
/* 300 */       return;
/*     */     }
/* 302 */     ThreadPool.logThreadLifeCycle(String.format("r/w loop: %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) }), new Runnable()
/*     */     {
/*     */       public void run() {
/* 305 */         boolean isFirstRun = true;
/* 306 */         int address = 0;
/* 307 */         byte[] buffer = new byte[ReadWriteRunnableStandard.this.monitorLength + ReadWriteRunnableStandard.this.startAddress];
/*     */         
/* 309 */         ElapsedTime timer = new ElapsedTime();
/* 310 */         String timerString = "Device " + ReadWriteRunnableStandard.this.serialNumber.toString();
/*     */         
/*     */ 
/* 313 */         ReadWriteRunnableStandard.this.running = true;
/*     */         try
/*     */         {
/* 316 */           ReadWriteRunnableStandard.this.callback.startupComplete();
/*     */         }
/*     */         catch (InterruptedException e) {
/* 319 */           ReadWriteRunnableStandard.this.running = false;
/*     */         }
/* 321 */         ReadWriteRunnableStandard.this.runningInterlock.countDown();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/* 329 */           ReadWriteRunnableStandard.this.usbHandler.purge(RobotUsbDevice.Channel.RX);
/*     */           
/* 331 */           while (ReadWriteRunnableStandard.this.running)
/*     */           {
/* 333 */             if (ReadWriteRunnableStandard.this.DEBUG_LOGGING) {
/* 334 */               timer.log(timerString);
/* 335 */               timer.reset();
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */             try
/*     */             {
/* 343 */               ReadWriteRunnableStandard.this.usbHandler.read(address, buffer);
/*     */               
/*     */ 
/* 346 */               while (!ReadWriteRunnableStandard.this.segmentReadQueue.isEmpty()) {
/* 347 */                 ReadWriteRunnableSegment segment = (ReadWriteRunnableSegment)ReadWriteRunnableStandard.this.segments.get(ReadWriteRunnableStandard.this.segmentReadQueue.remove());
/* 348 */                 byte[] readBuffer = new byte[segment.getReadBuffer().length];
/* 349 */                 ReadWriteRunnableStandard.this.usbHandler.read(segment.getAddress(), readBuffer);
/*     */                 try {
/* 351 */                   segment.getReadLock().lock();
/* 352 */                   System.arraycopy(readBuffer, 0, segment.getReadBuffer(), 0, segment.getReadBuffer().length);
/*     */                 } finally {
/* 354 */                   segment.getReadLock().unlock();
/*     */                 }
/*     */               }
/*     */             } catch (FTDeviceClosedException e) {
/* 358 */               throw e;
/*     */             } catch (RobotCoreException e) {
/* 360 */               RobotLog.w(String.format("could not read %s: %s", new Object[] { HardwareFactory.getDeviceDisplayName(ReadWriteRunnableStandard.this.context, ReadWriteRunnableStandard.this.serialNumber), e.getMessage() }));
/*     */             }
/* 362 */             synchronized (ReadWriteRunnableStandard.this.localDeviceReadCache) {
/* 363 */               System.arraycopy(buffer, 0, ReadWriteRunnableStandard.this.localDeviceReadCache, address, buffer.length);
/*     */             }
/*     */             
/* 366 */             if (ReadWriteRunnableStandard.this.DEBUG_LOGGING) ReadWriteRunnableStandard.this.dumpBuffers("read", ReadWriteRunnableStandard.this.localDeviceReadCache);
/* 367 */             ReadWriteRunnableStandard.this.callback.readComplete();
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 372 */             ReadWriteRunnableStandard.this.waitForSyncdEvents();
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 377 */             if (isFirstRun)
/*     */             {
/* 379 */               address = ReadWriteRunnableStandard.this.startAddress;
/* 380 */               buffer = new byte[ReadWriteRunnableStandard.this.monitorLength];
/* 381 */               isFirstRun = false;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 387 */             synchronized (ReadWriteRunnableStandard.this.localDeviceWriteCache) {
/* 388 */               System.arraycopy(ReadWriteRunnableStandard.this.localDeviceWriteCache, address, buffer, 0, buffer.length);
/*     */             }
/*     */             
/*     */             try
/*     */             {
/* 393 */               if (ReadWriteRunnableStandard.this.writeNeeded()) {
/* 394 */                 ReadWriteRunnableStandard.this.usbHandler.write(address, buffer);
/* 395 */                 ReadWriteRunnableStandard.this.resetWriteNeeded();
/*     */               }
/*     */               
/*     */ 
/* 399 */               while (!ReadWriteRunnableStandard.this.segmentWriteQueue.isEmpty()) {
/* 400 */                 ReadWriteRunnableSegment segment = (ReadWriteRunnableSegment)ReadWriteRunnableStandard.this.segments.get(ReadWriteRunnableStandard.this.segmentWriteQueue.remove());
/*     */                 try
/*     */                 {
/* 403 */                   segment.getWriteLock().lock();
/* 404 */                   writeBuffer = Arrays.copyOf(segment.getWriteBuffer(), segment.getWriteBuffer().length);
/*     */                 } finally { byte[] writeBuffer;
/* 406 */                   segment.getWriteLock().unlock(); }
/*     */                 byte[] writeBuffer;
/* 408 */                 ReadWriteRunnableStandard.this.usbHandler.write(segment.getAddress(), writeBuffer);
/*     */               }
/*     */             } catch (FTDeviceClosedException e) {
/* 411 */               throw e;
/*     */             } catch (RobotCoreException e) {
/* 413 */               RobotLog.w(String.format("could not write to %s: %s", new Object[] { HardwareFactory.getDeviceDisplayName(ReadWriteRunnableStandard.this.context, ReadWriteRunnableStandard.this.serialNumber), e.getMessage() }));
/*     */             }
/*     */             
/* 416 */             if (ReadWriteRunnableStandard.this.DEBUG_LOGGING) ReadWriteRunnableStandard.this.dumpBuffers("write", ReadWriteRunnableStandard.this.localDeviceWriteCache);
/* 417 */             ReadWriteRunnableStandard.this.callback.writeComplete();
/*     */             
/* 419 */             ReadWriteRunnableStandard.this.usbHandler.throwIfUsbErrorCountIsTooHigh();
/*     */           }
/*     */         } catch (NullPointerException e) {
/* 422 */           RobotLog.w(String.format("could not write to %s: FTDI Null Pointer Exception", new Object[] { HardwareFactory.getDeviceDisplayName(ReadWriteRunnableStandard.this.context, ReadWriteRunnableStandard.this.serialNumber) }));
/* 423 */           RobotLog.logStacktrace(e);
/* 424 */           ReadWriteRunnableStandard.this.setOwnerWarningMessage(ReadWriteRunnableStandard.this.context.getString(R.string.warningProblemCommunicatingWithUSBDevice), new Object[] { HardwareFactory.getDeviceDisplayName(ReadWriteRunnableStandard.this.context, ReadWriteRunnableStandard.this.serialNumber) });
/* 425 */           ReadWriteRunnableStandard.this.shutdownAbnormally = true;
/*     */         }
/*     */         catch (InterruptedException e) {
/* 428 */           RobotLog.w(String.format("thread interrupt: could not write to %s", new Object[] { HardwareFactory.getDeviceDisplayName(ReadWriteRunnableStandard.this.context, ReadWriteRunnableStandard.this.serialNumber) }));
/* 429 */           ReadWriteRunnableStandard.this.shutdownAbnormally = true;
/*     */         }
/*     */         catch (RobotCoreException e) {
/* 432 */           RobotLog.w(e.getMessage());
/* 433 */           ReadWriteRunnableStandard.this.setOwnerWarningMessage(ReadWriteRunnableStandard.this.context.getString(R.string.warningProblemCommunicatingWithUSBDevice), new Object[] { HardwareFactory.getDeviceDisplayName(ReadWriteRunnableStandard.this.context, ReadWriteRunnableStandard.this.serialNumber) });
/* 434 */           ReadWriteRunnableStandard.this.shutdownAbnormally = true;
/*     */         }
/*     */         finally {
/* 437 */           ReadWriteRunnableStandard.this.usbHandler.close();
/* 438 */           ReadWriteRunnableStandard.this.running = false;
/*     */           try {
/* 440 */             ReadWriteRunnableStandard.this.callback.shutdownComplete();
/*     */           }
/*     */           catch (InterruptedException localInterruptedException5) {}
/*     */           
/* 444 */           ReadWriteRunnableStandard.this.shutdownComplete = true;
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/* 450 */   void setOwnerWarningMessage(String format, Object... args) { String message = String.format(format, args);
/*     */     
/* 452 */     if ((this.owner != null) && ((this.owner instanceof GlobalWarningSource))) {
/* 453 */       ((GlobalWarningSource)this.owner).setGlobalWarning(message);
/*     */     } else {
/* 455 */       RobotLog.setGlobalWarningMessage(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasShutdownAbnormally()
/*     */   {
/* 461 */     return this.shutdownAbnormally;
/*     */   }
/*     */   
/*     */   boolean hasPendingWrites() {
/* 465 */     return (this.writeNeeded) || (!this.segmentWriteQueue.isEmpty());
/*     */   }
/*     */   
/*     */   public void drainPendingWrites()
/*     */   {
/* 470 */     while ((this.running) && (hasPendingWrites()))
/*     */     {
/* 472 */       startBlockingWork();
/* 473 */       Thread.yield();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAcceptingWrites(boolean acceptingWrites) {
/* 478 */     synchronized (this.acceptingWritesLock) {
/* 479 */       this.acceptingWrites = acceptingWrites;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getAcceptingWrites() {
/* 484 */     return this.acceptingWrites;
/*     */   }
/*     */   
/*     */   protected void waitForSyncdEvents() throws RobotCoreException, InterruptedException
/*     */   {}
/*     */   
/*     */   protected void dumpBuffers(String name, byte[] byteArray)
/*     */   {
/* 492 */     RobotLog.v("Dumping " + name + " buffers for " + this.serialNumber);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 499 */     StringBuilder s = new StringBuilder(1024);
/* 500 */     for (int i = 0; i < this.startAddress + this.monitorLength; i++) {
/* 501 */       s.append(String.format(" %02x", new Object[] { Integer.valueOf(TypeConversion.unsignedByteToInt(byteArray[i])) }));
/* 502 */       if ((i + 1) % 16 == 0) { s.append("\n");
/*     */       }
/*     */     }
/* 505 */     RobotLog.v(s.toString());
/*     */   }
/*     */   
/*     */   protected void queueIfNotAlreadyQueued(int key, ConcurrentLinkedQueue<Integer> queue) {
/* 509 */     if (!queue.contains(Integer.valueOf(key))) {
/* 510 */       queue.add(Integer.valueOf(key));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ReadWriteRunnableStandard.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */