/*      */ package com.qualcomm.robotcore.hardware;
/*      */ 
/*      */ import android.util.Log;
/*      */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
/*      */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.ARMINGSTATE;
/*      */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.Callback;
/*      */ import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
/*      */ import com.qualcomm.robotcore.util.ElapsedTime;
/*      */ import com.qualcomm.robotcore.util.ThreadPool;
/*      */ import java.util.Arrays;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReadWriteLock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import org.firstinspires.ftc.robotcore.internal.Assert;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class I2cDeviceSynchImpl
/*      */   implements I2cDeviceSynch, Engagable
/*      */ {
/*      */   protected I2cAddr i2cAddr;
/*      */   protected I2cDevice i2cDevice;
/*      */   protected boolean isI2cDeviceOwned;
/*      */   protected I2cController controller;
/*      */   protected RobotUsbModule robotUsbModule;
/*      */   protected boolean isHooked;
/*      */   protected boolean isEngaged;
/*      */   protected AtomicInteger readerWriterPreventionCount;
/*      */   protected ReadWriteLock readerWriterGate;
/*      */   protected AtomicInteger readerWriterCount;
/*      */   protected boolean isClosing;
/*      */   protected Callback callback;
/*      */   protected boolean loggingEnabled;
/*      */   protected String loggingTag;
/*      */   protected ElapsedTime timeSinceLastHeartbeat;
/*      */   protected byte[] readCache;
/*      */   protected byte[] writeCache;
/*      */   protected static final int dibCacheOverhead = 4;
/*      */   protected Lock readCacheLock;
/*      */   protected Lock writeCacheLock;
/*      */   protected static final int msCallbackLockWaitQuantum = 60;
/*      */   protected static final int msCallbackLockAbandon = 500;
/*      */   protected boolean isWriteCoalescingEnabled;
/*   97 */   protected final Object engagementLock = new Object();
/*   98 */   protected final Object concurrentClientLock = new Object();
/*   99 */   protected final Object callbackLock = new Object();
/*      */   
/*      */   protected volatile I2cDeviceSynch.ReadWindow readWindow;
/*      */   
/*      */   protected volatile I2cDeviceSynch.ReadWindow readWindowActuallyRead;
/*      */   protected volatile I2cDeviceSynch.ReadWindow readWindowSentToController;
/*      */   protected volatile boolean isReadWindowSentToControllerInitialized;
/*      */   protected volatile boolean hasReadWindowChanged;
/*      */   protected volatile long nanoTimeReadCacheValid;
/*      */   protected volatile READ_CACHE_STATUS readCacheStatus;
/*      */   protected volatile WRITE_CACHE_STATUS writeCacheStatus;
/*      */   protected volatile CONTROLLER_PORT_MODE controllerPortMode;
/*      */   protected volatile int iregWriteFirst;
/*      */   protected volatile int cregWrite;
/*      */   protected volatile int msHeartbeatInterval;
/*      */   protected volatile I2cDeviceSynch.HeartbeatAction heartbeatAction;
/*      */   protected volatile ExecutorService heartbeatExecutor;
/*      */   
/*      */   protected static enum READ_CACHE_STATUS
/*      */   {
/*  119 */     IDLE, 
/*  120 */     SWITCHINGTOREADMODE, 
/*  121 */     QUEUED, 
/*  122 */     QUEUE_COMPLETED, 
/*  123 */     VALID_ONLYONCE, 
/*  124 */     VALID_QUEUED;
/*      */     
/*      */     private READ_CACHE_STATUS() {}
/*      */     
/*  128 */     boolean isValid() { return (this == VALID_QUEUED) || (this == VALID_ONLYONCE); }
/*      */     
/*      */     boolean isQueued()
/*      */     {
/*  132 */       return (this == QUEUED) || (this == VALID_QUEUED);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected static enum WRITE_CACHE_STATUS
/*      */   {
/*  139 */     IDLE, 
/*  140 */     DIRTY, 
/*  141 */     QUEUED;
/*      */     
/*      */     private WRITE_CACHE_STATUS() {}
/*      */   }
/*      */   
/*      */   protected static enum CONTROLLER_PORT_MODE {
/*  147 */     UNKNOWN, 
/*  148 */     WRITE, 
/*  149 */     SWITCHINGTOREADMODE, 
/*      */     
/*  151 */     READ;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private CONTROLLER_PORT_MODE() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public I2cDeviceSynchImpl(I2cDevice i2cDevice, I2cAddr i2cAddr, boolean isI2cDeviceOwned)
/*      */   {
/*  176 */     this.i2cAddr = i2cAddr;
/*      */     
/*  178 */     this.i2cDevice = i2cDevice;
/*  179 */     this.isI2cDeviceOwned = isI2cDeviceOwned;
/*  180 */     this.controller = i2cDevice.getI2cController();
/*  181 */     this.isEngaged = false;
/*  182 */     this.isClosing = false;
/*  183 */     this.isHooked = false;
/*  184 */     this.readerWriterPreventionCount = new AtomicInteger(0);
/*  185 */     this.readerWriterGate = new ReentrantReadWriteLock();
/*  186 */     this.readerWriterCount = new AtomicInteger(0);
/*  187 */     this.callback = new Callback();
/*  188 */     this.loggingEnabled = false;
/*  189 */     this.loggingTag = String.format("%s:i2cSynch(%s)", new Object[] { "RobotCore", i2cDevice.getConnectionInfo() });
/*  190 */     this.timeSinceLastHeartbeat = new ElapsedTime();
/*  191 */     this.timeSinceLastHeartbeat.reset();
/*  192 */     this.msHeartbeatInterval = 0;
/*  193 */     this.heartbeatAction = null;
/*  194 */     this.heartbeatExecutor = null;
/*  195 */     this.isWriteCoalescingEnabled = false;
/*      */     
/*  197 */     this.readWindow = null;
/*      */     
/*  199 */     if ((this.controller instanceof RobotUsbModule))
/*      */     {
/*  201 */       this.robotUsbModule = ((RobotUsbModule)this.controller);
/*  202 */       this.robotUsbModule.registerCallback(this.callback);
/*      */     }
/*      */     else {
/*  205 */       throw new IllegalArgumentException("I2cController must also be a RobotUsbModule");
/*      */     }
/*  207 */     this.i2cDevice.registerForPortReadyBeginEndCallback(this.callback);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public I2cDeviceSynchImpl(I2cDevice i2cDevice, boolean isI2cDeviceOwned)
/*      */   {
/*  218 */     this(i2cDevice, I2cAddr.zero(), isI2cDeviceOwned);
/*      */   }
/*      */   
/*      */ 
/*      */   void attachToController()
/*      */   {
/*  224 */     this.readCache = this.i2cDevice.getI2cReadCache();
/*  225 */     this.readCacheLock = this.i2cDevice.getI2cReadCacheLock();
/*  226 */     this.writeCache = this.i2cDevice.getI2cWriteCache();
/*  227 */     this.writeCacheLock = this.i2cDevice.getI2cWriteCacheLock();
/*      */     
/*  229 */     this.nanoTimeReadCacheValid = 0L;
/*  230 */     this.readCacheStatus = READ_CACHE_STATUS.IDLE;
/*  231 */     this.writeCacheStatus = WRITE_CACHE_STATUS.IDLE;
/*  232 */     this.controllerPortMode = CONTROLLER_PORT_MODE.UNKNOWN;
/*      */     
/*  234 */     this.readWindowActuallyRead = null;
/*  235 */     this.readWindowSentToController = null;
/*  236 */     this.isReadWindowSentToControllerInitialized = false;
/*      */     
/*      */ 
/*  239 */     this.hasReadWindowChanged = true;
/*      */   }
/*      */   
/*      */   public void setI2cAddress(I2cAddr newAddress)
/*      */   {
/*  244 */     setI2cAddr(newAddress);
/*      */   }
/*      */   
/*      */   public void setI2cAddr(I2cAddr i2cAddr)
/*      */   {
/*  249 */     synchronized (this.engagementLock)
/*      */     {
/*  251 */       if (this.i2cAddr.get7Bit() != i2cAddr.get7Bit())
/*      */       {
/*  253 */         boolean wasArmed = this.isHooked;
/*  254 */         disengage();
/*      */         
/*  256 */         this.i2cAddr = i2cAddr;
/*      */         
/*  258 */         if (wasArmed) engage();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public I2cAddr getI2cAddress()
/*      */   {
/*  265 */     return getI2cAddr();
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public I2cAddr getI2cAddr()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 3	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:engagementLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 6	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:i2cAddr	Lcom/qualcomm/robotcore/hardware/I2cAddr;
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: areturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #270	-> byte code offset #0
/*      */     //   Java source line #272	-> byte code offset #7
/*      */     //   Java source line #273	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	I2cDeviceSynchImpl
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   public void engage()
/*      */   {
/*  283 */     synchronized (this.engagementLock)
/*      */     {
/*  285 */       this.isEngaged = true;
/*  286 */       adjustHooking();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void hook()
/*      */   {
/*  297 */     synchronized (this.engagementLock)
/*      */     {
/*  299 */       if (!this.isHooked)
/*      */       {
/*  301 */         log(2, "hooking ...");
/*      */         
/*  303 */         synchronized (this.callbackLock)
/*      */         {
/*  305 */           this.heartbeatExecutor = ThreadPool.newSingleThreadExecutor();
/*  306 */           this.i2cDevice.registerForI2cPortReadyCallback(this.callback);
/*      */         }
/*  308 */         this.isHooked = true;
/*      */         
/*  310 */         log(2, "... hooking complete");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void adjustHooking()
/*      */   {
/*  318 */     synchronized (this.engagementLock)
/*      */     {
/*  320 */       if ((!this.isHooked) && (this.isEngaged)) {
/*  321 */         hook();
/*  322 */       } else if ((this.isHooked) && (!this.isEngaged)) {
/*  323 */         unhook();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isEngaged() {
/*  329 */     return this.isEngaged;
/*      */   }
/*      */   
/*      */   public boolean isArmed()
/*      */   {
/*  334 */     synchronized (this.engagementLock)
/*      */     {
/*  336 */       if (this.isHooked)
/*      */       {
/*  338 */         return this.i2cDevice.isArmed();
/*      */       }
/*  340 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   public void disengage()
/*      */   {
/*  346 */     synchronized (this.engagementLock)
/*      */     {
/*  348 */       this.isEngaged = false;
/*  349 */       adjustHooking();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void unhook()
/*      */   {
/*      */     try {
/*  356 */       synchronized (this.engagementLock)
/*      */       {
/*  358 */         if (this.isHooked)
/*      */         {
/*  360 */           log(2, "unhooking ...");
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  366 */           this.heartbeatExecutor.shutdown();
/*  367 */           ThreadPool.awaitTerminationOrExitApplication(this.heartbeatExecutor, 10L, TimeUnit.SECONDS, "I2c Heartbeat", "internal error");
/*      */           
/*      */ 
/*  370 */           disableReadsAndWrites();
/*  371 */           gracefullyDrainReadersAndWriters();
/*      */           
/*  373 */           synchronized (this.callbackLock)
/*      */           {
/*      */ 
/*      */ 
/*  377 */             waitForWriteCompletionInternal();
/*      */             
/*      */ 
/*      */ 
/*  381 */             this.heartbeatExecutor = null;
/*      */             
/*      */ 
/*  384 */             this.i2cDevice.deregisterForPortReadyCallback();
/*      */           }
/*      */           
/*  387 */           this.isHooked = false;
/*  388 */           enableReadsAndWrites();
/*      */           
/*  390 */           log(2, "...unhooking complete");
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*  396 */       Thread.currentThread().interrupt();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void disableReadsAndWrites()
/*      */   {
/*  402 */     this.readerWriterPreventionCount.incrementAndGet();
/*      */   }
/*      */   
/*      */   protected void enableReadsAndWrites() {
/*  406 */     this.readerWriterPreventionCount.decrementAndGet();
/*      */   }
/*      */   
/*      */   protected boolean newReadsAndWritesAllowed() {
/*  410 */     return this.readerWriterPreventionCount.get() == 0;
/*      */   }
/*      */   
/*      */   protected void gracefullyDrainReadersAndWriters()
/*      */   {
/*  415 */     boolean interrupted = false;
/*  416 */     disableReadsAndWrites();
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*      */       for (;;)
/*      */       {
/*  423 */         if (this.readerWriterGate.writeLock().tryLock(20L, TimeUnit.MILLISECONDS))
/*      */         {
/*      */ 
/*  426 */           this.readerWriterGate.writeLock().unlock();
/*  427 */           break;
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*  436 */       interrupted = true;
/*      */     }
/*      */     
/*      */ 
/*  440 */     if (interrupted) {
/*  441 */       Thread.currentThread().interrupt();
/*      */     }
/*  443 */     Assert.assertTrue(this.readerWriterCount.get() == 0);
/*  444 */     enableReadsAndWrites();
/*      */   }
/*      */   
/*      */   protected void forceDrainReadersAndWriters()
/*      */   {
/*  449 */     boolean interrupted = false;
/*  450 */     boolean exitLoop = false;
/*  451 */     disableReadsAndWrites();
/*      */     
/*      */     for (;;)
/*      */     {
/*  455 */       synchronized (this.callbackLock)
/*      */       {
/*      */ 
/*  458 */         this.writeCacheStatus = WRITE_CACHE_STATUS.IDLE;
/*      */         
/*      */ 
/*  461 */         this.readCacheStatus = READ_CACHE_STATUS.VALID_QUEUED;
/*  462 */         this.hasReadWindowChanged = false;
/*  463 */         Assert.assertTrue(readCacheIsValid());
/*      */         
/*      */ 
/*  466 */         this.callbackLock.notifyAll();
/*      */       }
/*      */       
/*  469 */       if (exitLoop) {
/*      */         break;
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  475 */         if (this.readerWriterGate.writeLock().tryLock(20L, TimeUnit.MILLISECONDS))
/*      */         {
/*      */ 
/*  478 */           this.readerWriterGate.writeLock().unlock();
/*  479 */           exitLoop = true;
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (InterruptedException e)
/*      */       {
/*      */ 
/*  489 */         interrupted = true;
/*      */       }
/*      */     }
/*      */     
/*  493 */     if (interrupted) {
/*  494 */       Thread.currentThread().interrupt();
/*      */     }
/*      */     
/*      */ 
/*  498 */     Assert.assertTrue(this.readerWriterCount.get() == 0);
/*  499 */     enableReadsAndWrites();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HardwareDevice.Manufacturer getManufacturer()
/*      */   {
/*  508 */     return this.controller.getManufacturer();
/*      */   }
/*      */   
/*      */   public String getDeviceName()
/*      */   {
/*  513 */     return this.i2cDevice.getDeviceName();
/*      */   }
/*      */   
/*      */   public String getConnectionInfo()
/*      */   {
/*  518 */     return this.i2cDevice.getConnectionInfo();
/*      */   }
/*      */   
/*      */   public int getVersion()
/*      */   {
/*  523 */     return this.i2cDevice.getVersion();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetDeviceConfigurationForOpMode() {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void close()
/*      */   {
/*  535 */     this.isClosing = true;
/*  536 */     this.i2cDevice.deregisterForPortReadyBeginEndCallback();
/*      */     
/*  538 */     disengage();
/*      */     
/*  540 */     if (this.isI2cDeviceOwned) {
/*  541 */       this.i2cDevice.close();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReadWindow(I2cDeviceSynch.ReadWindow newWindow)
/*      */   {
/*  553 */     synchronized (this.concurrentClientLock)
/*      */     {
/*  555 */       synchronized (this.callbackLock)
/*      */       {
/*  557 */         if ((this.readWindow == null) || (!this.readWindow.canBeUsedToRead()) || (!this.readWindow.mayInitiateSwitchToReadMode()) || (!this.readWindow.sameAsIncludingMode(newWindow)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  564 */           setReadWindowInternal(newWindow.readableCopy());
/*  565 */           Assert.assertTrue((this.readWindow.canBeUsedToRead()) && (this.readWindow.mayInitiateSwitchToReadMode()));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void setReadWindowInternal(I2cDeviceSynch.ReadWindow newWindow)
/*      */   {
/*  574 */     this.readWindow = newWindow;
/*      */     
/*      */ 
/*  577 */     this.hasReadWindowChanged = true;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public I2cDeviceSynch.ReadWindow getReadWindow()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 4	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:concurrentClientLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 5	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:callbackLock	Ljava/lang/Object;
/*      */     //   11: dup
/*      */     //   12: astore_2
/*      */     //   13: monitorenter
/*      */     //   14: aload_0
/*      */     //   15: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: areturn
/*      */     //   23: astore_3
/*      */     //   24: aload_2
/*      */     //   25: monitorexit
/*      */     //   26: aload_3
/*      */     //   27: athrow
/*      */     //   28: astore 4
/*      */     //   30: aload_1
/*      */     //   31: monitorexit
/*      */     //   32: aload 4
/*      */     //   34: athrow
/*      */     // Line number table:
/*      */     //   Java source line #585	-> byte code offset #0
/*      */     //   Java source line #587	-> byte code offset #7
/*      */     //   Java source line #589	-> byte code offset #14
/*      */     //   Java source line #590	-> byte code offset #23
/*      */     //   Java source line #591	-> byte code offset #28
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	35	0	this	I2cDeviceSynchImpl
/*      */     //   5	26	1	Ljava/lang/Object;	Object
/*      */     //   12	13	2	Ljava/lang/Object;	Object
/*      */     //   23	4	3	localObject1	Object
/*      */     //   28	5	4	localObject2	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   14	20	23	finally
/*      */     //   23	26	23	finally
/*      */     //   7	22	28	finally
/*      */     //   23	32	28	finally
/*      */   }
/*      */   
/*      */   public void ensureReadWindow(I2cDeviceSynch.ReadWindow windowNeeded, I2cDeviceSynch.ReadWindow windowToSet)
/*      */   {
/*  599 */     synchronized (this.concurrentClientLock)
/*      */     {
/*  601 */       synchronized (this.callbackLock)
/*      */       {
/*  603 */         if ((this.readWindow == null) || (!this.readWindow.containsWithSameMode(windowNeeded)))
/*      */         {
/*  605 */           setReadWindow(windowToSet);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte read8(int ireg)
/*      */   {
/*  616 */     return read(ireg, 1)[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte[] read(int ireg, int creg)
/*      */   {
/*  624 */     return readTimeStamped(ireg, creg).data;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public I2cDeviceSynchSimple.TimestampedData readTimeStamped(int ireg, int creg)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 126	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:acquireReaderLockShared	()V
/*      */     //   4: aload_0
/*      */     //   5: invokevirtual 127	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:isOpenForReading	()Z
/*      */     //   8: ifne +15 -> 23
/*      */     //   11: iload_1
/*      */     //   12: iload_2
/*      */     //   13: invokestatic 128	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:makeFakeData	(II)Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple$TimestampedData;
/*      */     //   16: astore_3
/*      */     //   17: aload_0
/*      */     //   18: invokevirtual 129	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:releaseReaderLockShared	()V
/*      */     //   21: aload_3
/*      */     //   22: areturn
/*      */     //   23: aload_0
/*      */     //   24: getfield 4	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:concurrentClientLock	Ljava/lang/Object;
/*      */     //   27: dup
/*      */     //   28: astore_3
/*      */     //   29: monitorenter
/*      */     //   30: aload_0
/*      */     //   31: getfield 5	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:callbackLock	Ljava/lang/Object;
/*      */     //   34: dup
/*      */     //   35: astore 4
/*      */     //   37: monitorenter
/*      */     //   38: aload_0
/*      */     //   39: getfield 60	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:writeCacheStatus	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
/*      */     //   42: getstatic 59	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS:IDLE	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
/*      */     //   45: if_acmpeq +16 -> 61
/*      */     //   48: aload_0
/*      */     //   49: getfield 5	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:callbackLock	Ljava/lang/Object;
/*      */     //   52: ldc2_w 130
/*      */     //   55: invokevirtual 132	java/lang/Object:wait	(J)V
/*      */     //   58: goto -20 -> 38
/*      */     //   61: aload_0
/*      */     //   62: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   65: astore 5
/*      */     //   67: aload_0
/*      */     //   68: invokevirtual 133	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readCacheValidityCurrentOrImminent	()Z
/*      */     //   71: ifeq +25 -> 96
/*      */     //   74: aload_0
/*      */     //   75: getfield 63	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindowActuallyRead	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   78: ifnull +18 -> 96
/*      */     //   81: aload_0
/*      */     //   82: getfield 63	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindowActuallyRead	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   85: iload_1
/*      */     //   86: iload_2
/*      */     //   87: invokevirtual 134	com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow:contains	(II)Z
/*      */     //   90: ifeq +6 -> 96
/*      */     //   93: goto +86 -> 179
/*      */     //   96: aload_0
/*      */     //   97: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   100: ifnull +19 -> 119
/*      */     //   103: aload_0
/*      */     //   104: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   107: iload_1
/*      */     //   108: iload_2
/*      */     //   109: invokevirtual 134	com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow:contains	(II)Z
/*      */     //   112: ifeq +7 -> 119
/*      */     //   115: iconst_1
/*      */     //   116: goto +4 -> 120
/*      */     //   119: iconst_0
/*      */     //   120: istore 6
/*      */     //   122: iload 6
/*      */     //   124: ifeq +23 -> 147
/*      */     //   127: aload_0
/*      */     //   128: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   131: invokevirtual 116	com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow:canBeUsedToRead	()Z
/*      */     //   134: ifeq +13 -> 147
/*      */     //   137: aload_0
/*      */     //   138: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   141: invokevirtual 117	com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow:mayInitiateSwitchToReadMode	()Z
/*      */     //   144: ifne +35 -> 179
/*      */     //   147: iload 6
/*      */     //   149: ifeq +14 -> 163
/*      */     //   152: aload_0
/*      */     //   153: aload_0
/*      */     //   154: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   157: invokevirtual 122	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:setReadWindow	(Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)V
/*      */     //   160: goto +19 -> 179
/*      */     //   163: aload_0
/*      */     //   164: new 135	com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow
/*      */     //   167: dup
/*      */     //   168: iload_1
/*      */     //   169: iload_2
/*      */     //   170: getstatic 136	com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadMode:ONLY_ONCE	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadMode;
/*      */     //   173: invokespecial 137	com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow:<init>	(IILcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadMode;)V
/*      */     //   176: invokevirtual 122	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:setReadWindow	(Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)V
/*      */     //   179: aload_0
/*      */     //   180: invokevirtual 138	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:waitForValidReadCache	()V
/*      */     //   183: aload_0
/*      */     //   184: getfield 51	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readCacheLock	Ljava/util/concurrent/locks/Lock;
/*      */     //   187: invokeinterface 139 1 0
/*      */     //   192: aload_0
/*      */     //   193: getfield 63	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindowActuallyRead	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   196: aload_0
/*      */     //   197: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   200: invokevirtual 140	com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow:contains	(Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)Z
/*      */     //   203: invokestatic 107	org/firstinspires/ftc/robotcore/internal/Assert:assertTrue	(Z)V
/*      */     //   206: iload_1
/*      */     //   207: aload_0
/*      */     //   208: getfield 63	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindowActuallyRead	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   211: invokevirtual 141	com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow:getRegisterFirst	()I
/*      */     //   214: isub
/*      */     //   215: iconst_4
/*      */     //   216: iadd
/*      */     //   217: istore 6
/*      */     //   219: new 142	com/qualcomm/robotcore/hardware/I2cDeviceSynchSimple$TimestampedData
/*      */     //   222: dup
/*      */     //   223: invokespecial 143	com/qualcomm/robotcore/hardware/I2cDeviceSynchSimple$TimestampedData:<init>	()V
/*      */     //   226: astore 7
/*      */     //   228: aload 7
/*      */     //   230: aload_0
/*      */     //   231: getfield 49	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readCache	[B
/*      */     //   234: iload 6
/*      */     //   236: iload 6
/*      */     //   238: iload_2
/*      */     //   239: iadd
/*      */     //   240: invokestatic 144	java/util/Arrays:copyOfRange	([BII)[B
/*      */     //   243: putfield 125	com/qualcomm/robotcore/hardware/I2cDeviceSynchSimple$TimestampedData:data	[B
/*      */     //   246: aload 7
/*      */     //   248: aload_0
/*      */     //   249: getfield 56	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:nanoTimeReadCacheValid	J
/*      */     //   252: putfield 145	com/qualcomm/robotcore/hardware/I2cDeviceSynchSimple$TimestampedData:nanoTime	J
/*      */     //   255: aload 7
/*      */     //   257: astore 8
/*      */     //   259: aload_0
/*      */     //   260: getfield 51	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readCacheLock	Ljava/util/concurrent/locks/Lock;
/*      */     //   263: invokeinterface 106 1 0
/*      */     //   268: aload_0
/*      */     //   269: getfield 58	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readCacheStatus	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
/*      */     //   272: getstatic 146	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS:VALID_ONLYONCE	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
/*      */     //   275: if_acmpne +10 -> 285
/*      */     //   278: aload_0
/*      */     //   279: getstatic 57	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS:IDLE	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
/*      */     //   282: putfield 58	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readCacheStatus	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
/*      */     //   285: aload_0
/*      */     //   286: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   289: aload 5
/*      */     //   291: if_acmpeq +9 -> 300
/*      */     //   294: aload_0
/*      */     //   295: aload 5
/*      */     //   297: invokevirtual 120	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:setReadWindowInternal	(Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)V
/*      */     //   300: aload 4
/*      */     //   302: monitorexit
/*      */     //   303: aload_3
/*      */     //   304: monitorexit
/*      */     //   305: aload_0
/*      */     //   306: invokevirtual 129	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:releaseReaderLockShared	()V
/*      */     //   309: aload 8
/*      */     //   311: areturn
/*      */     //   312: astore 9
/*      */     //   314: aload_0
/*      */     //   315: getfield 51	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readCacheLock	Ljava/util/concurrent/locks/Lock;
/*      */     //   318: invokeinterface 106 1 0
/*      */     //   323: aload_0
/*      */     //   324: getfield 58	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readCacheStatus	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
/*      */     //   327: getstatic 146	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS:VALID_ONLYONCE	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
/*      */     //   330: if_acmpne +10 -> 340
/*      */     //   333: aload_0
/*      */     //   334: getstatic 57	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS:IDLE	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
/*      */     //   337: putfield 58	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readCacheStatus	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
/*      */     //   340: aload_0
/*      */     //   341: getfield 38	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*      */     //   344: aload 5
/*      */     //   346: if_acmpeq +9 -> 355
/*      */     //   349: aload_0
/*      */     //   350: aload 5
/*      */     //   352: invokevirtual 120	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:setReadWindowInternal	(Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)V
/*      */     //   355: aload 9
/*      */     //   357: athrow
/*      */     //   358: astore 10
/*      */     //   360: aload 4
/*      */     //   362: monitorexit
/*      */     //   363: aload 10
/*      */     //   365: athrow
/*      */     //   366: astore 11
/*      */     //   368: aload_3
/*      */     //   369: monitorexit
/*      */     //   370: aload 11
/*      */     //   372: athrow
/*      */     //   373: astore 12
/*      */     //   375: aload_0
/*      */     //   376: invokevirtual 129	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:releaseReaderLockShared	()V
/*      */     //   379: aload 12
/*      */     //   381: athrow
/*      */     //   382: astore_3
/*      */     //   383: invokestatic 96	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*      */     //   386: invokevirtual 97	java/lang/Thread:interrupt	()V
/*      */     //   389: iload_1
/*      */     //   390: iload_2
/*      */     //   391: invokestatic 128	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:makeFakeData	(II)Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple$TimestampedData;
/*      */     //   394: areturn
/*      */     // Line number table:
/*      */     //   Java source line #639	-> byte code offset #0
/*      */     //   Java source line #641	-> byte code offset #4
/*      */     //   Java source line #642	-> byte code offset #11
/*      */     //   Java source line #731	-> byte code offset #17
/*      */     //   Java source line #644	-> byte code offset #23
/*      */     //   Java source line #646	-> byte code offset #30
/*      */     //   Java source line #651	-> byte code offset #38
/*      */     //   Java source line #653	-> byte code offset #48
/*      */     //   Java source line #657	-> byte code offset #61
/*      */     //   Java source line #660	-> byte code offset #67
/*      */     //   Java source line #673	-> byte code offset #96
/*      */     //   Java source line #675	-> byte code offset #122
/*      */     //   Java source line #680	-> byte code offset #147
/*      */     //   Java source line #683	-> byte code offset #152
/*      */     //   Java source line #689	-> byte code offset #163
/*      */     //   Java source line #695	-> byte code offset #179
/*      */     //   Java source line #698	-> byte code offset #183
/*      */     //   Java source line #701	-> byte code offset #192
/*      */     //   Java source line #704	-> byte code offset #206
/*      */     //   Java source line #705	-> byte code offset #219
/*      */     //   Java source line #706	-> byte code offset #228
/*      */     //   Java source line #707	-> byte code offset #246
/*      */     //   Java source line #708	-> byte code offset #255
/*      */     //   Java source line #712	-> byte code offset #259
/*      */     //   Java source line #717	-> byte code offset #268
/*      */     //   Java source line #718	-> byte code offset #278
/*      */     //   Java source line #721	-> byte code offset #285
/*      */     //   Java source line #723	-> byte code offset #294
/*      */     //   Java source line #731	-> byte code offset #305
/*      */     //   Java source line #712	-> byte code offset #312
/*      */     //   Java source line #717	-> byte code offset #323
/*      */     //   Java source line #718	-> byte code offset #333
/*      */     //   Java source line #721	-> byte code offset #340
/*      */     //   Java source line #723	-> byte code offset #349
/*      */     //   Java source line #726	-> byte code offset #358
/*      */     //   Java source line #727	-> byte code offset #366
/*      */     //   Java source line #731	-> byte code offset #373
/*      */     //   Java source line #734	-> byte code offset #382
/*      */     //   Java source line #736	-> byte code offset #383
/*      */     //   Java source line #737	-> byte code offset #389
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	395	0	this	I2cDeviceSynchImpl
/*      */     //   0	395	1	ireg	int
/*      */     //   0	395	2	creg	int
/*      */     //   382	2	3	e	InterruptedException
/*      */     //   65	286	5	prevReadWindow	I2cDeviceSynch.ReadWindow
/*      */     //   120	28	6	readWindowRangeOk	boolean
/*      */     //   217	20	6	ibFirst	int
/*      */     //   226	30	7	result	I2cDeviceSynchSimple.TimestampedData
/*      */     //   257	53	8	localTimestampedData2	I2cDeviceSynchSimple.TimestampedData
/*      */     //   312	44	9	localObject1	Object
/*      */     //   358	6	10	localObject2	Object
/*      */     //   366	5	11	localObject3	Object
/*      */     //   373	7	12	localObject4	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   192	259	312	finally
/*      */     //   312	314	312	finally
/*      */     //   38	303	358	finally
/*      */     //   312	363	358	finally
/*      */     //   30	305	366	finally
/*      */     //   312	370	366	finally
/*      */     //   4	17	373	finally
/*      */     //   23	305	373	finally
/*      */     //   312	375	373	finally
/*      */     //   0	21	382	java/lang/InterruptedException
/*      */     //   23	309	382	java/lang/InterruptedException
/*      */     //   312	382	382	java/lang/InterruptedException
/*      */   }
/*      */   
/*      */   protected boolean isOpenForReading()
/*      */   {
/*  743 */     return (this.isHooked) && (newReadsAndWritesAllowed());
/*      */   }
/*      */   
/*      */   protected boolean isOpenForWriting() {
/*  747 */     return (this.isHooked) && (newReadsAndWritesAllowed());
/*      */   }
/*      */   
/*      */   protected void acquireReaderLockShared() throws InterruptedException {
/*  751 */     this.readerWriterGate.readLock().lockInterruptibly();
/*  752 */     this.readerWriterCount.incrementAndGet();
/*      */   }
/*      */   
/*      */   protected void releaseReaderLockShared() {
/*  756 */     this.readerWriterCount.decrementAndGet();
/*  757 */     this.readerWriterGate.readLock().unlock();
/*      */   }
/*      */   
/*      */   public static I2cDeviceSynchSimple.TimestampedData makeFakeData(int ireg, int creg)
/*      */   {
/*  762 */     I2cDeviceSynchSimple.TimestampedData result = new I2cDeviceSynchSimple.TimestampedData();
/*  763 */     result.data = new byte[creg];
/*  764 */     result.nanoTime = System.nanoTime();
/*  765 */     return result;
/*      */   }
/*      */   
/*      */   public I2cDeviceSynchSimple.TimestampedData readTimeStamped(int ireg, int creg, I2cDeviceSynch.ReadWindow readWindowNeeded, I2cDeviceSynch.ReadWindow readWindowSet)
/*      */   {
/*  770 */     ensureReadWindow(readWindowNeeded, readWindowSet);
/*  771 */     return readTimeStamped(ireg, creg);
/*      */   }
/*      */   
/*      */   protected boolean readCacheValidityCurrentOrImminent()
/*      */   {
/*  776 */     return (this.readCacheStatus != READ_CACHE_STATUS.IDLE) && (!this.hasReadWindowChanged);
/*      */   }
/*      */   
/*      */   protected boolean readCacheIsValid() {
/*  780 */     return (this.readCacheStatus.isValid()) && (!this.hasReadWindowChanged);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void write8(int ireg, int data)
/*      */   {
/*  788 */     write(ireg, new byte[] { (byte)data });
/*      */   }
/*      */   
/*      */   public void write8(int ireg, int data, boolean waitforCompletion) {
/*  792 */     write(ireg, new byte[] { (byte)data }, waitforCompletion);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void write(int ireg, byte[] data)
/*      */   {
/*  803 */     write(ireg, data, true);
/*      */   }
/*      */   
/*      */   public void write(int ireg, byte[] data, boolean waitForCompletion)
/*      */   {
/*      */     try
/*      */     {
/*  810 */       acquireReaderLockShared();
/*      */       try {
/*  812 */         if (!isOpenForWriting()) {
/*      */           return;
/*      */         }
/*  815 */         synchronized (this.concurrentClientLock)
/*      */         {
/*  817 */           if (data.length > 26) {
/*  818 */             throw new IllegalArgumentException(String.format("write request of %d bytes is too large; max is %d", new Object[] { Integer.valueOf(data.length), Integer.valueOf(26) }));
/*      */           }
/*  820 */           synchronized (this.callbackLock)
/*      */           {
/*      */ 
/*  823 */             boolean doCoalesce = false;
/*  824 */             if ((this.isWriteCoalescingEnabled) && (this.writeCacheStatus == WRITE_CACHE_STATUS.DIRTY) && (this.cregWrite + data.length <= 26))
/*      */             {
/*      */ 
/*      */ 
/*  828 */               if (ireg + data.length == this.iregWriteFirst)
/*      */               {
/*      */ 
/*      */ 
/*  832 */                 data = concatenateByteArrays(data, readWriteCache());
/*  833 */                 doCoalesce = true;
/*      */               }
/*  835 */               else if (this.iregWriteFirst + this.cregWrite == ireg)
/*      */               {
/*      */ 
/*  838 */                 ireg = this.iregWriteFirst;
/*  839 */                 data = concatenateByteArrays(readWriteCache(), data);
/*  840 */                 doCoalesce = true;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  848 */             if (!doCoalesce)
/*      */             {
/*  850 */               waitForIdleWriteCache();
/*      */             }
/*      */             
/*      */ 
/*  854 */             this.iregWriteFirst = ireg;
/*  855 */             this.cregWrite = data.length;
/*      */             
/*      */ 
/*  858 */             setWriteCacheStatusIfHooked(WRITE_CACHE_STATUS.DIRTY);
/*      */             
/*      */ 
/*  861 */             this.writeCacheLock.lock();
/*      */             try
/*      */             {
/*  864 */               System.arraycopy(data, 0, this.writeCache, 4, data.length);
/*      */             }
/*      */             finally
/*      */             {
/*  868 */               this.writeCacheLock.unlock();
/*      */             }
/*      */             
/*  871 */             if (!waitForCompletion) {}
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*      */ 
/*      */ 
/*  885 */         releaseReaderLockShared();
/*      */       }
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*  890 */       Thread.currentThread().interrupt();
/*      */     }
/*      */   }
/*      */   
/*      */   protected static byte[] concatenateByteArrays(byte[] left, byte[] right)
/*      */   {
/*  896 */     byte[] result = new byte[left.length + right.length];
/*  897 */     System.arraycopy(left, 0, result, 0, left.length);
/*  898 */     System.arraycopy(right, 0, result, left.length, right.length);
/*  899 */     return result;
/*      */   }
/*      */   
/*      */   public void waitForWriteCompletions()
/*      */   {
/*      */     try {
/*  905 */       synchronized (this.concurrentClientLock)
/*      */       {
/*  907 */         synchronized (this.callbackLock)
/*      */         {
/*  909 */           waitForWriteCompletionInternal();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*  915 */       Thread.currentThread().interrupt();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected byte[] readWriteCache()
/*      */   {
/*  922 */     this.writeCacheLock.lock();
/*      */     try {
/*  924 */       return Arrays.copyOfRange(this.writeCache, 4, 4 + this.cregWrite);
/*      */     }
/*      */     finally
/*      */     {
/*  928 */       this.writeCacheLock.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void waitForWriteCompletionInternal() throws InterruptedException
/*      */   {
/*  934 */     waitForIdleWriteCache();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void waitForIdleWriteCache()
/*      */     throws InterruptedException
/*      */   {
/*  942 */     ElapsedTime timer = null;
/*  943 */     while (this.writeCacheStatus != WRITE_CACHE_STATUS.IDLE)
/*      */     {
/*  945 */       if (timer == null) timer = new ElapsedTime();
/*  946 */       if (timer.milliseconds() > 500.0D)
/*  947 */         throw new InterruptedException();
/*  948 */       this.callbackLock.wait(60L);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void waitForValidReadCache()
/*      */     throws InterruptedException
/*      */   {
/*  955 */     ElapsedTime timer = null;
/*  956 */     while (!readCacheIsValid())
/*      */     {
/*  958 */       if (timer == null) timer = new ElapsedTime();
/*  959 */       if (timer.milliseconds() > 500.0D)
/*  960 */         throw new InterruptedException();
/*  961 */       this.callbackLock.wait(60L);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   void setWriteCacheStatusIfHooked(WRITE_CACHE_STATUS status)
/*      */   {
/*  968 */     if ((this.isHooked) && (newReadsAndWritesAllowed())) {
/*  969 */       this.writeCacheStatus = status;
/*      */     }
/*      */   }
/*      */   
/*      */   public void enableWriteCoalescing(boolean enable)
/*      */   {
/*  975 */     synchronized (this.concurrentClientLock)
/*      */     {
/*  977 */       this.isWriteCoalescingEnabled = enable;
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean isWriteCoalescingEnabled()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 4	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:concurrentClientLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 37	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:isWriteCoalescingEnabled	Z
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: ireturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #984	-> byte code offset #0
/*      */     //   Java source line #986	-> byte code offset #7
/*      */     //   Java source line #987	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	I2cDeviceSynchImpl
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   public void setLogging(boolean enabled)
/*      */   {
/*  992 */     synchronized (this.concurrentClientLock)
/*      */     {
/*  994 */       synchronized (this.callbackLock)
/*      */       {
/*  996 */         this.loggingEnabled = enabled;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setLoggingTag(String loggingTag)
/*      */   {
/* 1003 */     synchronized (this.concurrentClientLock)
/*      */     {
/* 1005 */       synchronized (this.callbackLock)
/*      */       {
/* 1007 */         this.loggingTag = (loggingTag + "I2C");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public int getHeartbeatInterval()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 4	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:concurrentClientLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 5	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:callbackLock	Ljava/lang/Object;
/*      */     //   11: dup
/*      */     //   12: astore_2
/*      */     //   13: monitorenter
/*      */     //   14: aload_0
/*      */     //   15: getfield 34	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:msHeartbeatInterval	I
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: ireturn
/*      */     //   23: astore_3
/*      */     //   24: aload_2
/*      */     //   25: monitorexit
/*      */     //   26: aload_3
/*      */     //   27: athrow
/*      */     //   28: astore 4
/*      */     //   30: aload_1
/*      */     //   31: monitorexit
/*      */     //   32: aload 4
/*      */     //   34: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1014	-> byte code offset #0
/*      */     //   Java source line #1016	-> byte code offset #7
/*      */     //   Java source line #1018	-> byte code offset #14
/*      */     //   Java source line #1019	-> byte code offset #23
/*      */     //   Java source line #1020	-> byte code offset #28
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	35	0	this	I2cDeviceSynchImpl
/*      */     //   5	26	1	Ljava/lang/Object;	Object
/*      */     //   12	13	2	Ljava/lang/Object;	Object
/*      */     //   23	4	3	localObject1	Object
/*      */     //   28	5	4	localObject2	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   14	20	23	finally
/*      */     //   23	26	23	finally
/*      */     //   7	22	28	finally
/*      */     //   23	32	28	finally
/*      */   }
/*      */   
/*      */   public void setHeartbeatInterval(int msHeartbeatInterval)
/*      */   {
/* 1025 */     synchronized (this.concurrentClientLock)
/*      */     {
/* 1027 */       synchronized (this.callbackLock)
/*      */       {
/* 1029 */         this.msHeartbeatInterval = Math.max(0, msHeartbeatInterval);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setHeartbeatAction(I2cDeviceSynch.HeartbeatAction action)
/*      */   {
/* 1036 */     synchronized (this.concurrentClientLock)
/*      */     {
/* 1038 */       synchronized (this.callbackLock)
/*      */       {
/* 1040 */         this.heartbeatAction = action;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public I2cDeviceSynch.HeartbeatAction getHeartbeatAction()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 4	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:concurrentClientLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 5	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:callbackLock	Ljava/lang/Object;
/*      */     //   11: dup
/*      */     //   12: astore_2
/*      */     //   13: monitorenter
/*      */     //   14: aload_0
/*      */     //   15: getfield 35	com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl:heartbeatAction	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$HeartbeatAction;
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: areturn
/*      */     //   23: astore_3
/*      */     //   24: aload_2
/*      */     //   25: monitorexit
/*      */     //   26: aload_3
/*      */     //   27: athrow
/*      */     //   28: astore 4
/*      */     //   30: aload_1
/*      */     //   31: monitorexit
/*      */     //   32: aload 4
/*      */     //   34: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1047	-> byte code offset #0
/*      */     //   Java source line #1049	-> byte code offset #7
/*      */     //   Java source line #1051	-> byte code offset #14
/*      */     //   Java source line #1052	-> byte code offset #23
/*      */     //   Java source line #1053	-> byte code offset #28
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	35	0	this	I2cDeviceSynchImpl
/*      */     //   5	26	1	Ljava/lang/Object;	Object
/*      */     //   12	13	2	Ljava/lang/Object;	Object
/*      */     //   23	4	3	localObject1	Object
/*      */     //   28	5	4	localObject2	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   14	20	23	finally
/*      */     //   23	26	23	finally
/*      */     //   7	22	28	finally
/*      */     //   23	32	28	finally
/*      */   }
/*      */   
/*      */   protected void log(int verbosity, String message)
/*      */   {
/* 1058 */     switch (verbosity) {
/*      */     case 2: 
/* 1060 */       Log.v(this.loggingTag, message); break;
/* 1061 */     case 3:  Log.d(this.loggingTag, message); break;
/* 1062 */     case 4:  Log.i(this.loggingTag, message); break;
/* 1063 */     case 5:  Log.w(this.loggingTag, message); break;
/* 1064 */     case 6:  Log.e(this.loggingTag, message); break;
/* 1065 */     case 7:  Log.wtf(this.loggingTag, message);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void log(int verbosity, String format, Object... args) {
/* 1070 */     log(verbosity, String.format(format, args));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class Callback
/*      */     implements I2cController.I2cPortReadyCallback, I2cController.I2cPortReadyBeginEndNotifications, RobotArmingStateNotifier.Callback
/*      */   {
/* 1080 */     protected boolean setActionFlag = false;
/* 1081 */     protected boolean queueFullWrite = false;
/* 1082 */     protected boolean queueRead = false;
/* 1083 */     protected boolean heartbeatRequired = false;
/* 1084 */     protected boolean enabledReadMode = false;
/* 1085 */     protected boolean enabledWriteMode = false;
/*      */     
/* 1087 */     protected I2cDeviceSynchImpl.READ_CACHE_STATUS prevReadCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.IDLE;
/* 1088 */     protected I2cDeviceSynchImpl.WRITE_CACHE_STATUS prevWriteCacheStatus = I2cDeviceSynchImpl.WRITE_CACHE_STATUS.IDLE;
/*      */     
/* 1090 */     protected boolean doModuleIsArmedWorkEnabledWrites = false;
/* 1091 */     protected boolean haveSeenModuleIsArmedWork = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Callback() {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void portIsReady(int port)
/*      */     {
/* 1102 */       updateStateMachines();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void onModuleStateChange(RobotArmingStateNotifier robotUsbModule, RobotArmingStateNotifier.ARMINGSTATE armingstate)
/*      */     {
/* 1112 */       switch (I2cDeviceSynchImpl.1.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE[armingstate.ordinal()])
/*      */       {
/*      */       case 1: 
/* 1115 */         I2cDeviceSynchImpl.this.log(2, "onArmed ...");
/* 1116 */         doModuleIsArmedWork(true);
/* 1117 */         I2cDeviceSynchImpl.this.log(2, "... onArmed");
/* 1118 */         break;
/*      */       case 2: 
/* 1120 */         I2cDeviceSynchImpl.this.log(2, "onPretending ...");
/* 1121 */         doModuleIsArmedWork(false);
/* 1122 */         I2cDeviceSynchImpl.this.log(2, "... onPretending");
/* 1123 */         break;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void onPortIsReadyCallbacksBegin(int port)
/*      */     {
/* 1140 */       I2cDeviceSynchImpl.this.log(2, "doPortIsReadyCallbackBeginWork ...");
/*      */       try {
/* 1142 */         switch (I2cDeviceSynchImpl.1.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE[I2cDeviceSynchImpl.this.robotUsbModule.getArmingState().ordinal()])
/*      */         {
/*      */         case 1: 
/* 1145 */           doModuleIsArmedWork(true);
/* 1146 */           break;
/*      */         case 2: 
/* 1148 */           doModuleIsArmedWork(false);
/*      */         }
/*      */         
/*      */       }
/*      */       finally
/*      */       {
/* 1154 */         I2cDeviceSynchImpl.this.log(2, "... doPortIsReadyCallbackBeginWork complete");
/*      */       }
/*      */     }
/*      */     
/*      */     protected void doModuleIsArmedWork(boolean arming)
/*      */     {
/*      */       try {
/* 1161 */         I2cDeviceSynchImpl.this.log(2, "doModuleIsArmedWork ...");
/* 1162 */         synchronized (I2cDeviceSynchImpl.this.engagementLock)
/*      */         {
/* 1164 */           I2cDeviceSynchImpl.this.disableReadsAndWrites();
/* 1165 */           I2cDeviceSynchImpl.this.forceDrainReadersAndWriters();
/* 1166 */           I2cDeviceSynchImpl.this.unhook();
/*      */           
/*      */ 
/* 1169 */           I2cDeviceSynchImpl.this.attachToController();
/*      */           
/* 1171 */           I2cDeviceSynchImpl.this.adjustHooking();
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1181 */           if (arming)
/*      */           {
/* 1183 */             I2cDeviceSynchImpl.this.enableReadsAndWrites();
/* 1184 */             this.doModuleIsArmedWorkEnabledWrites = true;
/*      */           }
/*      */           else {
/* 1187 */             this.doModuleIsArmedWorkEnabledWrites = false;
/*      */           }
/* 1189 */           this.haveSeenModuleIsArmedWork = true;
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 1194 */         I2cDeviceSynchImpl.this.log(2, "... doModuleIsArmedWork complete");
/*      */       }
/*      */     }
/*      */     
/*      */     public void onPortIsReadyCallbacksEnd(int port)
/*      */     {
/*      */       try
/*      */       {
/* 1202 */         I2cDeviceSynchImpl.this.log(2, "onPortIsReadyCallbacksEnd ...");
/*      */         
/* 1204 */         if (I2cDeviceSynchImpl.this.isClosing) {
/*      */           return;
/*      */         }
/* 1207 */         if (!this.haveSeenModuleIsArmedWork) {
/*      */           return;
/*      */         }
/* 1210 */         synchronized (I2cDeviceSynchImpl.this.engagementLock)
/*      */         {
/* 1212 */           if (this.doModuleIsArmedWorkEnabledWrites)
/*      */           {
/* 1214 */             I2cDeviceSynchImpl.this.disableReadsAndWrites();
/*      */           }
/*      */           
/* 1217 */           I2cDeviceSynchImpl.this.forceDrainReadersAndWriters();
/* 1218 */           I2cDeviceSynchImpl.this.unhook();
/* 1219 */           Assert.assertTrue((!I2cDeviceSynchImpl.this.isOpenForReading()) && (!I2cDeviceSynchImpl.this.isOpenForWriting()));
/* 1220 */           I2cDeviceSynchImpl.this.enableReadsAndWrites();
/*      */           
/* 1222 */           this.haveSeenModuleIsArmedWork = false;
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 1227 */         I2cDeviceSynchImpl.this.log(2, "... onPortIsReadyCallbacksEnd complete");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void startSwitchingToReadMode(I2cDeviceSynch.ReadWindow window)
/*      */     {
/* 1237 */       I2cDeviceSynchImpl.this.readCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.SWITCHINGTOREADMODE;
/*      */       
/* 1239 */       I2cDeviceSynchImpl.this.i2cDevice.enableI2cReadMode(I2cDeviceSynchImpl.this.i2cAddr, window.getRegisterFirst(), window.getRegisterCount());
/* 1240 */       this.enabledReadMode = true;
/*      */       
/*      */ 
/* 1243 */       I2cDeviceSynchImpl.this.readWindowSentToController = window;
/* 1244 */       I2cDeviceSynchImpl.this.isReadWindowSentToControllerInitialized = true;
/*      */       
/* 1246 */       this.setActionFlag = true;
/* 1247 */       this.queueFullWrite = true;
/*      */     }
/*      */     
/*      */ 
/*      */     protected void issueWrite()
/*      */     {
/* 1253 */       I2cDeviceSynchImpl.this.setWriteCacheStatusIfHooked(I2cDeviceSynchImpl.WRITE_CACHE_STATUS.QUEUED);
/*      */       
/* 1255 */       I2cDeviceSynchImpl.this.i2cDevice.enableI2cWriteMode(I2cDeviceSynchImpl.this.i2cAddr, I2cDeviceSynchImpl.this.iregWriteFirst, I2cDeviceSynchImpl.this.cregWrite);
/* 1256 */       this.enabledWriteMode = true;
/*      */       
/*      */ 
/* 1259 */       I2cDeviceSynchImpl.this.readWindowSentToController = null;
/* 1260 */       I2cDeviceSynchImpl.this.isReadWindowSentToControllerInitialized = true;
/*      */       
/* 1262 */       this.setActionFlag = true;
/* 1263 */       this.queueFullWrite = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected boolean isControllerPortInReadMode()
/*      */     {
/* 1270 */       return I2cDeviceSynchImpl.this.controllerPortMode == I2cDeviceSynchImpl.CONTROLLER_PORT_MODE.READ;
/*      */     }
/*      */     
/*      */ 
/*      */     protected void updateStateMachines()
/*      */     {
/* 1276 */       synchronized (I2cDeviceSynchImpl.this.callbackLock)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1281 */         this.setActionFlag = false;
/* 1282 */         this.queueFullWrite = false;
/* 1283 */         this.queueRead = false;
/* 1284 */         this.heartbeatRequired = ((I2cDeviceSynchImpl.this.msHeartbeatInterval > 0) && (I2cDeviceSynchImpl.this.timeSinceLastHeartbeat.milliseconds() >= I2cDeviceSynchImpl.this.msHeartbeatInterval));
/* 1285 */         this.enabledReadMode = false;
/* 1286 */         this.enabledWriteMode = false;
/*      */         
/* 1288 */         this.prevReadCacheStatus = I2cDeviceSynchImpl.this.readCacheStatus;
/* 1289 */         this.prevWriteCacheStatus = I2cDeviceSynchImpl.this.writeCacheStatus;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1299 */         if (I2cDeviceSynchImpl.this.controllerPortMode == I2cDeviceSynchImpl.CONTROLLER_PORT_MODE.SWITCHINGTOREADMODE) {
/* 1300 */           I2cDeviceSynchImpl.this.controllerPortMode = I2cDeviceSynchImpl.CONTROLLER_PORT_MODE.READ;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1305 */         if ((I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.QUEUED) || (I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.VALID_QUEUED))
/*      */         {
/* 1307 */           I2cDeviceSynchImpl.this.readCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.QUEUE_COMPLETED;
/* 1308 */           I2cDeviceSynchImpl.this.nanoTimeReadCacheValid = System.nanoTime();
/*      */         }
/*      */         
/* 1311 */         if (I2cDeviceSynchImpl.this.writeCacheStatus == I2cDeviceSynchImpl.WRITE_CACHE_STATUS.QUEUED)
/*      */         {
/* 1313 */           I2cDeviceSynchImpl.this.writeCacheStatus = I2cDeviceSynchImpl.WRITE_CACHE_STATUS.IDLE;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1331 */         Assert.assertTrue((I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.IDLE) || (I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.SWITCHINGTOREADMODE) || (I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.VALID_ONLYONCE) || (I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.QUEUE_COMPLETED));
/*      */         
/*      */ 
/*      */ 
/* 1335 */         Assert.assertTrue((I2cDeviceSynchImpl.this.writeCacheStatus == I2cDeviceSynchImpl.WRITE_CACHE_STATUS.IDLE) || (I2cDeviceSynchImpl.this.writeCacheStatus == I2cDeviceSynchImpl.WRITE_CACHE_STATUS.DIRTY));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1340 */         if (I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.SWITCHINGTOREADMODE)
/*      */         {
/*      */ 
/* 1343 */           if (isControllerPortInReadMode())
/*      */           {
/*      */ 
/* 1346 */             I2cDeviceSynchImpl.this.readCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.QUEUED;
/* 1347 */             this.setActionFlag = true;
/* 1348 */             this.queueRead = true;
/*      */           }
/*      */           else
/*      */           {
/* 1352 */             this.queueRead = true;
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */         }
/* 1359 */         else if (I2cDeviceSynchImpl.this.writeCacheStatus == I2cDeviceSynchImpl.WRITE_CACHE_STATUS.DIRTY)
/*      */         {
/* 1361 */           issueWrite();
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1366 */           I2cDeviceSynchImpl.this.readCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.IDLE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/* 1372 */         else if ((I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.IDLE) || (I2cDeviceSynchImpl.this.hasReadWindowChanged))
/*      */         {
/* 1374 */           boolean issuedRead = false;
/* 1375 */           if (I2cDeviceSynchImpl.this.readWindow != null)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1380 */             boolean readSwitchUnnecessary = (I2cDeviceSynchImpl.this.readWindowSentToController != null) && (I2cDeviceSynchImpl.this.readWindowSentToController.contains(I2cDeviceSynchImpl.this.readWindow)) && (isControllerPortInReadMode());
/*      */             
/*      */ 
/*      */ 
/* 1384 */             if ((I2cDeviceSynchImpl.this.readWindow.canBeUsedToRead()) && ((readSwitchUnnecessary) || (I2cDeviceSynchImpl.this.readWindow.mayInitiateSwitchToReadMode())))
/*      */             {
/* 1386 */               if (readSwitchUnnecessary)
/*      */               {
/*      */ 
/*      */ 
/* 1390 */                 I2cDeviceSynchImpl.this.readWindowActuallyRead = I2cDeviceSynchImpl.this.readWindowSentToController;
/* 1391 */                 I2cDeviceSynchImpl.this.readCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.QUEUED;
/* 1392 */                 this.setActionFlag = true;
/* 1393 */                 this.queueRead = true;
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/* 1398 */                 I2cDeviceSynchImpl.this.readWindowActuallyRead = I2cDeviceSynchImpl.this.readWindow;
/* 1399 */                 startSwitchingToReadMode(I2cDeviceSynchImpl.this.readWindow);
/*      */               }
/*      */               
/* 1402 */               issuedRead = true;
/*      */             }
/*      */           }
/*      */           
/* 1406 */           if (issuedRead)
/*      */           {
/*      */ 
/*      */ 
/* 1410 */             I2cDeviceSynchImpl.this.readWindow.noteWindowUsedForRead();
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 1415 */             I2cDeviceSynchImpl.this.readCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.IDLE;
/*      */           }
/*      */           
/* 1418 */           I2cDeviceSynchImpl.this.hasReadWindowChanged = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/* 1426 */         else if (I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.QUEUE_COMPLETED)
/*      */         {
/* 1428 */           if ((I2cDeviceSynchImpl.this.readWindow != null) && (I2cDeviceSynchImpl.this.readWindow.canBeUsedToRead()))
/*      */           {
/* 1430 */             I2cDeviceSynchImpl.this.readCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.VALID_QUEUED;
/* 1431 */             this.setActionFlag = true;
/* 1432 */             this.queueRead = true;
/*      */           }
/*      */           else
/*      */           {
/* 1436 */             I2cDeviceSynchImpl.this.readCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.VALID_ONLYONCE;
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */         }
/* 1443 */         else if (I2cDeviceSynchImpl.this.readCacheStatus != I2cDeviceSynchImpl.READ_CACHE_STATUS.VALID_ONLYONCE) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1453 */         if ((!this.setActionFlag) && (this.heartbeatRequired))
/*      */         {
/* 1455 */           if (I2cDeviceSynchImpl.this.heartbeatAction != null)
/*      */           {
/* 1457 */             if ((I2cDeviceSynchImpl.this.readWindowSentToController != null) && (I2cDeviceSynchImpl.this.heartbeatAction.rereadLastRead))
/*      */             {
/*      */ 
/*      */ 
/* 1461 */               if (isControllerPortInReadMode())
/*      */               {
/* 1463 */                 this.setActionFlag = true;
/*      */               }
/*      */               else
/*      */               {
/* 1467 */                 Assert.assertTrue(I2cDeviceSynchImpl.this.readCacheStatus == I2cDeviceSynchImpl.READ_CACHE_STATUS.SWITCHINGTOREADMODE);
/*      */               }
/*      */               
/*      */             }
/* 1471 */             else if ((I2cDeviceSynchImpl.this.isReadWindowSentToControllerInitialized) && (I2cDeviceSynchImpl.this.readWindowSentToController == null) && (I2cDeviceSynchImpl.this.heartbeatAction.rewriteLastWritten))
/*      */             {
/*      */ 
/* 1474 */               this.queueFullWrite = true;
/* 1475 */               this.setActionFlag = true;
/*      */ 
/*      */             }
/* 1478 */             else if (I2cDeviceSynchImpl.this.heartbeatAction.heartbeatReadWindow != null)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1484 */               final I2cDeviceSynch.ReadWindow window = I2cDeviceSynchImpl.this.heartbeatAction.heartbeatReadWindow;
/*      */               try {
/* 1486 */                 if (I2cDeviceSynchImpl.this.heartbeatExecutor != null)
/*      */                 {
/* 1488 */                   I2cDeviceSynchImpl.this.heartbeatExecutor.submit(new Runnable()
/*      */                   {
/*      */                     public void run()
/*      */                     {
/*      */                       try {
/* 1493 */                         I2cDeviceSynchImpl.this.read(window.getRegisterFirst(), window.getRegisterCount());
/*      */                       }
/*      */                       catch (Exception localException) {}
/*      */                     }
/*      */                   });
/*      */                 }
/*      */               }
/*      */               catch (RejectedExecutionException localRejectedExecutionException) {}
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1511 */         if (this.setActionFlag)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1516 */           I2cDeviceSynchImpl.this.timeSinceLastHeartbeat.reset();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1522 */         if ((this.enabledReadMode) || (this.enabledWriteMode))
/*      */         {
/* 1524 */           Assert.assertTrue(this.queueFullWrite);
/* 1525 */           Assert.assertFalse((this.enabledReadMode) && (this.enabledWriteMode));
/*      */           
/* 1527 */           if (this.enabledWriteMode)
/*      */           {
/* 1529 */             I2cDeviceSynchImpl.this.controllerPortMode = I2cDeviceSynchImpl.CONTROLLER_PORT_MODE.WRITE;
/*      */           }
/*      */           else
/*      */           {
/* 1533 */             I2cDeviceSynchImpl.this.controllerPortMode = I2cDeviceSynchImpl.CONTROLLER_PORT_MODE.SWITCHINGTOREADMODE;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1540 */         if (this.setActionFlag) {
/* 1541 */           I2cDeviceSynchImpl.this.i2cDevice.setI2cPortActionFlag();
/*      */         } else {
/* 1543 */           I2cDeviceSynchImpl.this.i2cDevice.clearI2cPortActionFlag();
/*      */         }
/* 1545 */         if ((this.setActionFlag) && (!this.queueFullWrite))
/*      */         {
/* 1547 */           I2cDeviceSynchImpl.this.i2cDevice.writeI2cPortFlagOnlyToController();
/*      */         }
/* 1549 */         else if (this.queueFullWrite)
/*      */         {
/* 1551 */           I2cDeviceSynchImpl.this.i2cDevice.writeI2cCacheToController();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1557 */         if (this.queueRead)
/*      */         {
/* 1559 */           I2cDeviceSynchImpl.this.i2cDevice.readI2cCacheFromController();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1565 */         if (I2cDeviceSynchImpl.this.loggingEnabled)
/*      */         {
/* 1567 */           StringBuilder message = new StringBuilder();
/* 1568 */           message.append(String.format("cyc %d", new Object[] { Integer.valueOf(I2cDeviceSynchImpl.this.i2cDevice.getCallbackCount()) }));
/* 1569 */           if (this.setActionFlag) message.append("|flag");
/* 1570 */           if ((this.setActionFlag) && (!this.queueFullWrite)) { message.append("|f");
/* 1571 */           } else if (this.queueFullWrite) message.append("|w"); else
/* 1572 */             message.append("|.");
/* 1573 */           if (this.queueRead) message.append("|r");
/* 1574 */           if (I2cDeviceSynchImpl.this.readCacheStatus != this.prevReadCacheStatus) message.append("| R." + this.prevReadCacheStatus.toString() + "->" + I2cDeviceSynchImpl.this.readCacheStatus.toString());
/* 1575 */           if (I2cDeviceSynchImpl.this.writeCacheStatus != this.prevWriteCacheStatus) message.append("| W." + this.prevWriteCacheStatus.toString() + "->" + I2cDeviceSynchImpl.this.writeCacheStatus.toString());
/* 1576 */           if (this.enabledWriteMode) message.append(String.format("| setWrite(0x%02x,%d)", new Object[] { Integer.valueOf(I2cDeviceSynchImpl.this.iregWriteFirst), Integer.valueOf(I2cDeviceSynchImpl.this.cregWrite) }));
/* 1577 */           if (this.enabledReadMode) { message.append(String.format("| setRead(0x%02x,%d)", new Object[] { Integer.valueOf(I2cDeviceSynchImpl.this.readWindow.getRegisterFirst()), Integer.valueOf(I2cDeviceSynchImpl.this.readWindow.getRegisterCount()) }));
/*      */           }
/* 1579 */           I2cDeviceSynchImpl.this.log(3, message.toString());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1584 */         I2cDeviceSynchImpl.this.callbackLock.notifyAll();
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */