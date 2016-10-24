/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class I2cDeviceImpl
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements I2cDevice, HardwareDevice, I2cController.I2cPortReadyCallback
/*     */ {
/*     */   protected I2cController.I2cPortReadyCallback callback;
/*     */   protected AtomicInteger callbackCount;
/*     */   
/*     */   public I2cDeviceImpl(I2cController controller, int port)
/*     */   {
/*  59 */     super(controller, port);
/*  60 */     this.callback = null;
/*  61 */     this.callbackCount = new AtomicInteger(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void controllerNowArmedOrPretending() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public I2cController getController()
/*     */   {
/*  78 */     return this.controller;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/*  86 */     return this.physicalPort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void enableI2cReadMode(I2cAddr i2cAddr, int memAddress, int length)
/*     */   {
/*  96 */     this.controller.enableI2cReadMode(this.physicalPort, i2cAddr, memAddress, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void enableI2cWriteMode(I2cAddr i2cAddr, int memAddress, int length)
/*     */   {
/* 106 */     this.controller.enableI2cWriteMode(this.physicalPort, i2cAddr, memAddress, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getCopyOfReadBuffer()
/*     */   {
/* 114 */     return this.controller.getCopyOfReadBuffer(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getCopyOfWriteBuffer()
/*     */   {
/* 122 */     return this.controller.getCopyOfWriteBuffer(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void copyBufferIntoWriteBuffer(byte[] buffer)
/*     */   {
/* 130 */     this.controller.copyBufferIntoWriteBuffer(this.physicalPort, buffer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setI2cPortActionFlag()
/*     */   {
/* 137 */     this.controller.setI2cPortActionFlag(this.physicalPort);
/*     */   }
/*     */   
/*     */   public void clearI2cPortActionFlag() {
/* 141 */     this.controller.clearI2cPortActionFlag(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isI2cPortActionFlagSet()
/*     */   {
/* 149 */     return this.controller.isI2cPortActionFlagSet(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readI2cCacheFromController()
/*     */   {
/* 156 */     this.controller.readI2cCacheFromController(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writeI2cCacheToController()
/*     */   {
/* 163 */     this.controller.writeI2cCacheToController(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writeI2cPortFlagOnlyToController()
/*     */   {
/* 170 */     this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isI2cPortInReadMode()
/*     */   {
/* 178 */     return this.controller.isI2cPortInReadMode(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isI2cPortInWriteMode()
/*     */   {
/* 186 */     return this.controller.isI2cPortInWriteMode(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isI2cPortReady()
/*     */   {
/* 194 */     return this.controller.isI2cPortReady(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lock getI2cReadCacheLock()
/*     */   {
/* 205 */     return this.controller.getI2cReadCacheLock(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lock getI2cWriteCacheLock()
/*     */   {
/* 217 */     return this.controller.getI2cWriteCacheLock(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getI2cReadCache()
/*     */   {
/* 227 */     return this.controller.getI2cReadCache(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getI2cWriteCache()
/*     */   {
/* 237 */     return this.controller.getI2cWriteCache(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void portIsReady(int port)
/*     */   {
/* 246 */     this.callbackCount.incrementAndGet();
/* 247 */     I2cController.I2cPortReadyCallback callback = this.callback;
/* 248 */     if (callback != null) {
/* 249 */       callback.portIsReady(port);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback callback)
/*     */   {
/* 258 */     this.callback = callback;
/* 259 */     this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public I2cController.I2cPortReadyCallback getI2cPortReadyCallback()
/*     */   {
/* 267 */     return this.callback;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized void deregisterForPortReadyCallback()
/*     */   {
/* 274 */     this.controller.deregisterForPortReadyCallback(this.physicalPort);
/* 275 */     this.callback = null;
/*     */   }
/*     */   
/*     */   public int getCallbackCount()
/*     */   {
/* 280 */     return this.callbackCount.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerForPortReadyBeginEndCallback(I2cController.I2cPortReadyBeginEndNotifications callback)
/*     */   {
/* 292 */     this.controller.registerForPortReadyBeginEndCallback(callback, this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public I2cController.I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback()
/*     */   {
/* 300 */     return this.controller.getPortReadyBeginEndCallback(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void deregisterForPortReadyBeginEndCallback()
/*     */   {
/* 307 */     this.controller.deregisterForPortReadyBeginEndCallback(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isArmed()
/*     */   {
/* 316 */     return this.controller.isArmed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 324 */     return this.controller.getManufacturer();
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/* 329 */     return "I2cDevice";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 334 */     return this.controller.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 339 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void readI2cCacheFromModule()
/*     */   {
/* 356 */     readI2cCacheFromController();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void writeI2cCacheToModule()
/*     */   {
/* 364 */     writeI2cCacheToController();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void writeI2cPortFlagOnlyToModule()
/*     */   {
/* 372 */     writeI2cPortFlagOnlyToController();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cDeviceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */