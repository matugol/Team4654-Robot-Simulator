/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class I2cDeviceSynchImplOnSimple
/*     */   implements I2cDeviceSynch
/*     */ {
/*     */   protected I2cDeviceSynchSimple i2cDeviceSynchSimple;
/*     */   protected boolean isSimpleOwned;
/*     */   protected I2cDeviceSynch.ReadWindow readWindow;
/*     */   protected int iregReadLast;
/*     */   protected int cregReadLast;
/*     */   protected int iregWriteLast;
/*     */   protected byte[] rgbWriteLast;
/*     */   protected boolean isHooked;
/*     */   protected boolean isEngaged;
/*     */   protected boolean isClosing;
/*     */   protected int msHeartbeatInterval;
/*     */   protected I2cDeviceSynch.HeartbeatAction heartbeatAction;
/*     */   protected ScheduledExecutorService heartbeatExecutor;
/*  68 */   protected final Object engagementLock = new Object();
/*  69 */   protected final Object concurrentClientLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public I2cDeviceSynchImplOnSimple(I2cDeviceSynchSimple simple, boolean isSimpleOwned)
/*     */   {
/*  77 */     this.i2cDeviceSynchSimple = simple;
/*  78 */     this.isSimpleOwned = isSimpleOwned;
/*     */     
/*  80 */     this.msHeartbeatInterval = 0;
/*  81 */     this.heartbeatAction = null;
/*  82 */     this.heartbeatExecutor = null;
/*  83 */     this.readWindow = null;
/*  84 */     this.cregReadLast = 0;
/*  85 */     this.rgbWriteLast = null;
/*  86 */     this.isEngaged = false;
/*  87 */     this.isHooked = false;
/*  88 */     this.isClosing = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLogging(boolean enabled)
/*     */   {
/*  98 */     this.i2cDeviceSynchSimple.setLogging(enabled);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setLoggingTag(String loggingTag)
/*     */   {
/* 104 */     this.i2cDeviceSynchSimple.setLoggingTag(loggingTag);
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
/*     */   public void engage()
/*     */   {
/* 118 */     synchronized (this.engagementLock)
/*     */     {
/* 120 */       this.isEngaged = true;
/* 121 */       adjustHooking();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void hook()
/*     */   {
/* 132 */     synchronized (this.engagementLock)
/*     */     {
/* 134 */       if (!this.isHooked)
/*     */       {
/* 136 */         startHeartBeat();
/* 137 */         this.isHooked = true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void adjustHooking()
/*     */   {
/* 145 */     synchronized (this.engagementLock)
/*     */     {
/* 147 */       if ((!this.isHooked) && (this.isEngaged)) {
/* 148 */         hook();
/* 149 */       } else if ((this.isHooked) && (!this.isEngaged)) {
/* 150 */         unhook();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isEngaged() {
/* 156 */     return this.isEngaged;
/*     */   }
/*     */   
/*     */   public boolean isArmed()
/*     */   {
/* 161 */     synchronized (this.engagementLock)
/*     */     {
/* 163 */       if (this.isHooked)
/*     */       {
/* 165 */         return this.i2cDeviceSynchSimple.isArmed();
/*     */       }
/* 167 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public void disengage()
/*     */   {
/* 173 */     synchronized (this.engagementLock)
/*     */     {
/* 175 */       this.isEngaged = false;
/* 176 */       adjustHooking();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void unhook()
/*     */   {
/* 182 */     synchronized (this.engagementLock)
/*     */     {
/* 184 */       if (this.isHooked)
/*     */       {
/* 186 */         stopHeartBeat();
/* 187 */         synchronized (this.concurrentClientLock)
/*     */         {
/* 189 */           waitForWriteCompletions();
/* 190 */           this.isHooked = false;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeartbeatInterval(int ms)
/*     */   {
/* 203 */     synchronized (this.concurrentClientLock)
/*     */     {
/* 205 */       this.msHeartbeatInterval = Math.max(0, this.msHeartbeatInterval);
/* 206 */       stopHeartBeat();
/* 207 */       startHeartBeat();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getHeartbeatInterval()
/*     */   {
/* 214 */     return this.msHeartbeatInterval;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setHeartbeatAction(I2cDeviceSynch.HeartbeatAction action)
/*     */   {
/* 220 */     this.heartbeatAction = action;
/*     */   }
/*     */   
/*     */ 
/*     */   public I2cDeviceSynch.HeartbeatAction getHeartbeatAction()
/*     */   {
/* 226 */     return this.heartbeatAction;
/*     */   }
/*     */   
/*     */   void startHeartBeat()
/*     */   {
/* 231 */     if (this.msHeartbeatInterval > 0)
/*     */     {
/* 233 */       this.heartbeatExecutor = ThreadPool.newSingleThreadScheduledExecutor();
/* 234 */       this.heartbeatExecutor.scheduleAtFixedRate(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 238 */           I2cDeviceSynch.HeartbeatAction action = I2cDeviceSynchImplOnSimple.this.getHeartbeatAction();
/* 239 */           if (action != null)
/*     */           {
/* 241 */             synchronized (I2cDeviceSynchImplOnSimple.this.concurrentClientLock)
/*     */             {
/* 243 */               if ((action.rereadLastRead) && (I2cDeviceSynchImplOnSimple.this.cregReadLast != 0))
/*     */               {
/* 245 */                 I2cDeviceSynchImplOnSimple.this.read(I2cDeviceSynchImplOnSimple.this.iregReadLast, I2cDeviceSynchImplOnSimple.this.cregReadLast);
/* 246 */                 return;
/*     */               }
/* 248 */               if ((action.rewriteLastWritten) && (I2cDeviceSynchImplOnSimple.this.rgbWriteLast != null))
/*     */               {
/* 250 */                 I2cDeviceSynchImplOnSimple.this.write(I2cDeviceSynchImplOnSimple.this.iregWriteLast, I2cDeviceSynchImplOnSimple.this.rgbWriteLast);
/* 251 */                 return;
/*     */               }
/* 253 */               if (action.heartbeatReadWindow != null)
/*     */               {
/* 255 */                 I2cDeviceSynchImplOnSimple.this.read(action.heartbeatReadWindow.getRegisterFirst(), action.heartbeatReadWindow.getRegisterCount()); } } } } }, 0L, this.msHeartbeatInterval, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void stopHeartBeat()
/*     */   {
/* 266 */     if (this.heartbeatExecutor != null)
/*     */     {
/* 268 */       this.heartbeatExecutor.shutdownNow();
/* 269 */       ThreadPool.awaitTerminationOrExitApplication(this.heartbeatExecutor, 2L, TimeUnit.SECONDS, "heartbeat executor", "internal error");
/* 270 */       this.heartbeatExecutor = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReadWindow(I2cDeviceSynch.ReadWindow window)
/*     */   {
/* 281 */     synchronized (this.concurrentClientLock)
/*     */     {
/* 283 */       this.readWindow = window.readableCopy();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public I2cDeviceSynch.ReadWindow getReadWindow()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	com/qualcomm/robotcore/hardware/I2cDeviceSynchImplOnSimple:concurrentClientLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 10	com/qualcomm/robotcore/hardware/I2cDeviceSynchImplOnSimple:readWindow	Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: areturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #290	-> byte code offset #0
/*     */     //   Java source line #292	-> byte code offset #7
/*     */     //   Java source line #293	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	I2cDeviceSynchImplOnSimple
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   public void ensureReadWindow(I2cDeviceSynch.ReadWindow windowNeeded, I2cDeviceSynch.ReadWindow windowToSet)
/*     */   {
/* 299 */     synchronized (this.concurrentClientLock)
/*     */     {
/* 301 */       if ((this.readWindow == null) || (!this.readWindow.containsWithSameMode(windowNeeded)))
/*     */       {
/* 303 */         setReadWindow(windowToSet);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public I2cDeviceSynchSimple.TimestampedData readTimeStamped(int ireg, int creg, I2cDeviceSynch.ReadWindow readWindowNeeded, I2cDeviceSynch.ReadWindow readWindowSet)
/*     */   {
/* 311 */     ensureReadWindow(readWindowNeeded, readWindowSet);
/* 312 */     return readTimeStamped(ireg, creg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 322 */     return this.i2cDeviceSynchSimple.getManufacturer();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 328 */     return this.i2cDeviceSynchSimple.getDeviceName();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 334 */     return this.i2cDeviceSynchSimple.getConnectionInfo();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getVersion()
/*     */   {
/* 340 */     return this.i2cDeviceSynchSimple.getVersion();
/*     */   }
/*     */   
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode()
/*     */   {
/* 346 */     this.i2cDeviceSynchSimple.resetDeviceConfigurationForOpMode();
/* 347 */     this.readWindow = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 354 */     this.isClosing = true;
/* 355 */     disengage();
/* 356 */     if (this.isSimpleOwned)
/*     */     {
/* 358 */       this.i2cDeviceSynchSimple.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isOpenForReading()
/*     */   {
/* 368 */     return (this.isHooked) && (newReadsAndWritesAllowed());
/*     */   }
/*     */   
/*     */   protected boolean isOpenForWriting() {
/* 372 */     return (this.isHooked) && (newReadsAndWritesAllowed());
/*     */   }
/*     */   
/*     */   protected boolean newReadsAndWritesAllowed() {
/* 376 */     return !this.isClosing;
/*     */   }
/*     */   
/*     */   public void setI2cAddress(I2cAddr newAddress)
/*     */   {
/* 381 */     setI2cAddr(newAddress);
/*     */   }
/*     */   
/*     */   public I2cAddr getI2cAddress()
/*     */   {
/* 386 */     return getI2cAddr();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setI2cAddr(I2cAddr i2cAddr)
/*     */   {
/* 392 */     this.i2cDeviceSynchSimple.setI2cAddr(i2cAddr);
/*     */   }
/*     */   
/*     */ 
/*     */   public I2cAddr getI2cAddr()
/*     */   {
/* 398 */     return this.i2cDeviceSynchSimple.getI2cAddr();
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized byte read8(int ireg)
/*     */   {
/* 404 */     return read(ireg, 1)[0];
/*     */   }
/*     */   
/*     */ 
/*     */   public byte[] read(int ireg, int creg)
/*     */   {
/* 410 */     return readTimeStamped(ireg, creg).data;
/*     */   }
/*     */   
/*     */ 
/*     */   public I2cDeviceSynchSimple.TimestampedData readTimeStamped(int ireg, int creg)
/*     */   {
/* 416 */     synchronized (this.concurrentClientLock)
/*     */     {
/* 418 */       if (!isOpenForReading()) {
/* 419 */         return I2cDeviceSynchImpl.makeFakeData(ireg, creg);
/*     */       }
/* 421 */       this.iregReadLast = ireg;
/* 422 */       this.cregReadLast = creg;
/* 423 */       if ((this.readWindow != null) && (this.readWindow.contains(ireg, creg)))
/*     */       {
/* 425 */         I2cDeviceSynchSimple.TimestampedData windowedData = this.i2cDeviceSynchSimple.readTimeStamped(this.readWindow.getRegisterFirst(), this.readWindow.getRegisterCount());
/* 426 */         int ibFirst = ireg - this.readWindow.getRegisterFirst();
/* 427 */         I2cDeviceSynchSimple.TimestampedData result = new I2cDeviceSynchSimple.TimestampedData();
/* 428 */         result.data = Arrays.copyOfRange(windowedData.data, ibFirst, ibFirst + creg);
/* 429 */         result.nanoTime = windowedData.nanoTime;
/* 430 */         return result;
/*     */       }
/*     */       
/*     */ 
/* 434 */       return this.i2cDeviceSynchSimple.readTimeStamped(ireg, creg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write8(int ireg, int bVal)
/*     */   {
/* 442 */     write8(ireg, bVal, false);
/*     */   }
/*     */   
/*     */ 
/*     */   public void write8(int ireg, int bVal, boolean waitForCompletion)
/*     */   {
/* 448 */     write(ireg, new byte[] { (byte)bVal }, waitForCompletion);
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(int ireg, byte[] data)
/*     */   {
/* 454 */     write(ireg, data, false);
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(int ireg, byte[] data, boolean waitForCompletion)
/*     */   {
/* 460 */     synchronized (this.concurrentClientLock)
/*     */     {
/* 462 */       if (!isOpenForWriting()) {
/* 463 */         return;
/*     */       }
/* 465 */       this.iregWriteLast = ireg;
/* 466 */       this.rgbWriteLast = Arrays.copyOf(data, data.length);
/* 467 */       this.i2cDeviceSynchSimple.write(ireg, data, waitForCompletion);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void waitForWriteCompletions()
/*     */   {
/* 474 */     this.i2cDeviceSynchSimple.waitForWriteCompletions();
/*     */   }
/*     */   
/*     */ 
/*     */   public void enableWriteCoalescing(boolean enable)
/*     */   {
/* 480 */     this.i2cDeviceSynchSimple.enableWriteCoalescing(enable);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isWriteCoalescingEnabled()
/*     */   {
/* 486 */     return this.i2cDeviceSynchSimple.isWriteCoalescingEnabled();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchImplOnSimple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */