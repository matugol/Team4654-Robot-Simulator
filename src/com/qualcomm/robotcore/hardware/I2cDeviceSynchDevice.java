/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.ARMINGSTATE;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.Callback;
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
/*     */ public abstract class I2cDeviceSynchDevice<T extends I2cDeviceSynchSimple>
/*     */   implements RobotArmingStateNotifier.Callback, HardwareDevice
/*     */ {
/*     */   protected T deviceClient;
/*     */   protected boolean deviceClientIsOwned;
/*     */   protected boolean isInitialized;
/*     */   
/*     */   protected I2cDeviceSynchDevice(T deviceClient, boolean isOwned)
/*     */   {
/*  58 */     this.deviceClient = deviceClient;
/*  59 */     this.deviceClientIsOwned = isOwned;
/*  60 */     this.isInitialized = false;
/*  61 */     this.deviceClient.enableWriteCoalescing(false);
/*     */   }
/*     */   
/*     */   protected void registerArmingStateCallback()
/*     */   {
/*  66 */     if ((this.deviceClient instanceof RobotArmingStateNotifier))
/*     */     {
/*  68 */       ((RobotArmingStateNotifier)this.deviceClient).registerCallback(this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void engage()
/*     */   {
/*  74 */     if ((this.deviceClient instanceof Engagable))
/*     */     {
/*  76 */       ((Engagable)this.deviceClient).engage();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void disengage()
/*     */   {
/*  82 */     if ((this.deviceClient instanceof Engagable))
/*     */     {
/*  84 */       ((Engagable)this.deviceClient).disengage();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onModuleStateChange(RobotArmingStateNotifier module, RobotArmingStateNotifier.ARMINGSTATE state)
/*     */   {
/*  96 */     if (state == RobotArmingStateNotifier.ARMINGSTATE.ARMED)
/*     */     {
/*  98 */       initializeIfNecessary();
/*     */     }
/*     */   }
/*     */   
/*     */   protected synchronized void initializeIfNecessary()
/*     */   {
/* 104 */     if (!this.isInitialized)
/*     */     {
/* 106 */       initialize();
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized boolean initialize()
/*     */   {
/* 112 */     if (doInitialize())
/*     */     {
/* 114 */       this.isInitialized = true;
/* 115 */       return true;
/*     */     }
/* 117 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean doInitialize();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode()
/*     */   {
/* 133 */     this.isInitialized = false;
/* 134 */     initialize();
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 139 */     if (this.deviceClientIsOwned)
/*     */     {
/* 141 */       this.deviceClient.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getVersion()
/*     */   {
/* 148 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 154 */     return this.deviceClient.getConnectionInfo();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */