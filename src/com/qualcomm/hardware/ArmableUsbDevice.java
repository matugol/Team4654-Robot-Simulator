/*     */ package com.qualcomm.hardware;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.ARMINGSTATE;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.Callback;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
/*     */ import com.qualcomm.robotcore.util.GlobalWarningSource;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.WeakReferenceSet;
/*     */ import junit.framework.Assert;
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
/*     */ public abstract class ArmableUsbDevice
/*     */   implements RobotUsbModule, GlobalWarningSource
/*     */ {
/*     */   protected final Context context;
/*     */   protected final SerialNumber serialNumber;
/*     */   protected final EventLoopManager eventLoopManager;
/*     */   protected final OpenRobotUsbDevice openRobotUsbDevice;
/*     */   protected RobotUsbDevice robotUsbDevice;
/*     */   protected RobotArmingStateNotifier.ARMINGSTATE armingState;
/*  36 */   protected final Object armingLock = new Object();
/*  37 */   protected final WeakReferenceSet<RobotArmingStateNotifier.Callback> registeredCallbacks = new WeakReferenceSet();
/*  38 */   protected final Object warningMessageLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */   protected int warningMessageSuppressionCount;
/*     */   
/*     */ 
/*     */ 
/*     */   protected String warningMessage;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArmableUsbDevice(Context context, SerialNumber serialNumber, EventLoopManager manager, OpenRobotUsbDevice openRobotUsbDevice)
/*     */   {
/*  53 */     this.context = context;
/*  54 */     this.serialNumber = serialNumber;
/*  55 */     this.eventLoopManager = manager;
/*  56 */     this.openRobotUsbDevice = openRobotUsbDevice;
/*  57 */     this.robotUsbDevice = null;
/*  58 */     this.armingState = RobotArmingStateNotifier.ARMINGSTATE.DISARMED;
/*  59 */     this.warningMessageSuppressionCount = 0;
/*  60 */     this.warningMessage = "";
/*     */   }
/*     */   
/*     */   protected void finishConstruction()
/*     */   {
/*  65 */     RobotLog.registerGlobalWarningSource(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Context getContext()
/*     */   {
/*  74 */     return this.context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerialNumber getSerialNumber()
/*     */   {
/*  84 */     return this.serialNumber;
/*     */   }
/*     */   
/*     */ 
/*     */   public void registerCallback(RobotArmingStateNotifier.Callback callback)
/*     */   {
/*  90 */     this.registeredCallbacks.add(callback);
/*     */   }
/*     */   
/*     */ 
/*     */   public void unregisterCallback(RobotArmingStateNotifier.Callback callback)
/*     */   {
/*  96 */     this.registeredCallbacks.remove(callback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGlobalWarning()
/*     */   {
/* 106 */     synchronized (this.warningMessageLock)
/*     */     {
/* 108 */       return this.warningMessageSuppressionCount > 0 ? "" : this.warningMessage;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void clearGlobalWarning()
/*     */   {
/* 115 */     synchronized (this.warningMessageLock)
/*     */     {
/* 117 */       this.warningMessage = "";
/* 118 */       this.warningMessageSuppressionCount = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void suppressGlobalWarning(boolean suppress)
/*     */   {
/* 125 */     synchronized (this.warningMessageLock)
/*     */     {
/* 127 */       if (suppress) {
/* 128 */         this.warningMessageSuppressionCount += 1;
/*     */       } else {
/* 130 */         this.warningMessageSuppressionCount -= 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean setGlobalWarning(String warning)
/*     */   {
/* 140 */     synchronized (this.warningMessageLock)
/*     */     {
/* 142 */       if ((warning != null) && (this.warningMessage.isEmpty()))
/*     */       {
/* 144 */         this.warningMessage = warning;
/* 145 */         return true;
/*     */       }
/* 147 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void armDevice()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 157 */     synchronized (this.armingLock)
/*     */     {
/*     */ 
/* 160 */       this.warningMessage = "";
/*     */       
/* 162 */       Assert.assertTrue(this.robotUsbDevice == null);
/* 163 */       RobotUsbDevice device = null;
/*     */       
/*     */       try
/*     */       {
/* 167 */         device = this.openRobotUsbDevice.open();
/*     */         
/* 169 */         armDevice(device);
/*     */       }
/*     */       catch (RobotCoreException e)
/*     */       {
/* 173 */         if (device != null) device.close();
/* 174 */         setGlobalWarning(String.format(this.context.getString(R.string.warningUnableToOpen), new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) }));
/* 175 */         throw e;
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (NullPointerException e)
/*     */       {
/*     */ 
/* 182 */         if (device != null) device.close();
/* 183 */         setGlobalWarning(String.format(this.context.getString(R.string.warningUnableToOpen), new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) }));
/* 184 */         throw e;
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 188 */         if (device != null) device.close();
/* 189 */         throw e;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void pretendDevice() throws RobotCoreException, InterruptedException
/*     */   {
/* 196 */     synchronized (this.armingLock)
/*     */     {
/*     */ 
/* 199 */       RobotUsbDevice device = getPretendDevice(this.serialNumber);
/*     */       
/* 201 */       armDevice(device);
/*     */     }
/*     */   }
/*     */   
/*     */   protected RobotUsbDevice getPretendDevice(SerialNumber serialNumber)
/*     */   {
/* 207 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract void armDevice(RobotUsbDevice paramRobotUsbDevice)
/*     */     throws RobotCoreException, InterruptedException;
/*     */   
/*     */ 
/*     */   protected abstract void disarmDevice()
/*     */     throws InterruptedException;
/*     */   
/*     */ 
/*     */   public RobotArmingStateNotifier.ARMINGSTATE getArmingState()
/*     */   {
/* 221 */     return this.armingState;
/*     */   }
/*     */   
/*     */   protected RobotArmingStateNotifier.ARMINGSTATE setArmingState(RobotArmingStateNotifier.ARMINGSTATE state)
/*     */   {
/* 226 */     RobotArmingStateNotifier.ARMINGSTATE prev = this.armingState;
/* 227 */     this.armingState = state;
/* 228 */     for (RobotArmingStateNotifier.Callback callback : this.registeredCallbacks)
/*     */     {
/* 230 */       callback.onModuleStateChange(this, state);
/*     */     }
/* 232 */     return prev;
/*     */   }
/*     */   
/*     */   protected boolean isArmed()
/*     */   {
/* 237 */     return this.armingState == RobotArmingStateNotifier.ARMINGSTATE.ARMED;
/*     */   }
/*     */   
/*     */   protected boolean isArmedOrArming() {
/* 241 */     return (this.armingState == RobotArmingStateNotifier.ARMINGSTATE.ARMED) || (this.armingState == RobotArmingStateNotifier.ARMINGSTATE.TO_ARMED);
/*     */   }
/*     */   
/*     */   protected boolean isPretending()
/*     */   {
/* 246 */     return this.armingState == RobotArmingStateNotifier.ARMINGSTATE.PRETENDING;
/*     */   }
/*     */   
/*     */ 
/*     */   public void arm()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 253 */     synchronized (this.armingLock)
/*     */     {
/*     */       try
/*     */       {
/* 257 */         switch (this.armingState)
/*     */         {
/*     */         case ARMED: 
/* 260 */           return;
/*     */         case DISARMED: 
/* 262 */           RobotArmingStateNotifier.ARMINGSTATE prev = setArmingState(RobotArmingStateNotifier.ARMINGSTATE.TO_ARMED);
/*     */           try {
/* 264 */             doArm();
/* 265 */             setArmingState(RobotArmingStateNotifier.ARMINGSTATE.ARMED);
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 269 */             setArmingState(prev);
/* 270 */             throw e;
/*     */           }
/*     */         
/*     */         default: 
/* 274 */           throw new RobotCoreException("illegal state: can't arm() from state %s", new Object[] { this.armingState.toString() });
/*     */         }
/*     */       }
/*     */       catch (RobotCoreException e)
/*     */       {
/* 279 */         disarm();
/* 280 */         throw e;
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 284 */         disarm();
/* 285 */         throw e;
/*     */       }
/*     */       catch (NullPointerException e)
/*     */       {
/* 289 */         disarm();
/* 290 */         throw e;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doArm()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 298 */     armDevice();
/*     */   }
/*     */   
/*     */   public void pretend()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 304 */     synchronized (this.armingLock)
/*     */     {
/*     */       try
/*     */       {
/* 308 */         switch (this.armingState)
/*     */         {
/*     */         case PRETENDING: 
/* 311 */           return;
/*     */         case DISARMED: 
/* 313 */           RobotArmingStateNotifier.ARMINGSTATE prev = setArmingState(RobotArmingStateNotifier.ARMINGSTATE.TO_PRETENDING);
/*     */           try {
/* 315 */             doPretend();
/* 316 */             setArmingState(RobotArmingStateNotifier.ARMINGSTATE.PRETENDING);
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 320 */             setArmingState(prev);
/* 321 */             throw e;
/*     */           }
/*     */         
/*     */         default: 
/* 325 */           throw new RobotCoreException("illegal state: can't pretend() from state %s", new Object[] { this.armingState.toString() });
/*     */         }
/*     */       }
/*     */       catch (RobotCoreException|RuntimeException|InterruptedException e)
/*     */       {
/* 330 */         disarm();
/* 331 */         throw e;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void armOrPretend()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 339 */     synchronized (this.armingLock)
/*     */     {
/*     */       try
/*     */       {
/* 343 */         arm();
/*     */       }
/*     */       catch (RobotCoreException|RuntimeException e)
/*     */       {
/* 347 */         pretend();
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 351 */         pretend();
/* 352 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doPretend()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 360 */     pretendDevice();
/*     */   }
/*     */   
/*     */   public void disarm()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 366 */     synchronized (this.armingLock)
/*     */     {
/* 368 */       switch (this.armingState)
/*     */       {
/*     */       case DISARMED: 
/*     */         
/*     */       case ARMED: 
/*     */       case PRETENDING: 
/*     */       case TO_ARMED: 
/*     */       case TO_PRETENDING: 
/* 376 */         RobotArmingStateNotifier.ARMINGSTATE prev = setArmingState(RobotArmingStateNotifier.ARMINGSTATE.TO_DISARMED);
/*     */         try {
/* 378 */           doDisarm();
/* 379 */           setArmingState(RobotArmingStateNotifier.ARMINGSTATE.DISARMED);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 383 */           setArmingState(prev);
/* 384 */           throw e;
/*     */         }
/*     */       
/*     */       default: 
/* 388 */         throw new RobotCoreException("illegal state: can't disarm() from state %s", new Object[] { this.armingState.toString() });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisarm()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 396 */     disarmDevice();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 403 */     synchronized (this.armingLock)
/*     */     {
/*     */       try
/*     */       {
/* 407 */         switch (this.armingState)
/*     */         {
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
/*     */         case CLOSED: 
/* 431 */           setArmingState(RobotArmingStateNotifier.ARMINGSTATE.CLOSED);
/* 432 */           this.warningMessage = "";
/*     */         case ARMED: 
/* 412 */           doCloseFromArmed();
/* 413 */           break;
/*     */         default: 
/* 415 */           doCloseFromOther();
/*     */         }
/*     */         
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 421 */         Thread.currentThread().interrupt();
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (RobotCoreException|RuntimeException localRobotCoreException) {}finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 431 */         setArmingState(RobotArmingStateNotifier.ARMINGSTATE.CLOSED);
/* 432 */         this.warningMessage = "";
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doCloseFromArmed()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 440 */     disarm();
/*     */   }
/*     */   
/*     */   protected void doCloseFromOther()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 446 */     disarm();
/*     */   }
/*     */   
/*     */   public static abstract interface OpenRobotUsbDevice
/*     */   {
/*     */     public abstract RobotUsbDevice open()
/*     */       throws RobotCoreException, InterruptedException;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\ArmableUsbDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */