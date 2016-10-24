/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.ArmableUsbDevice.OpenRobotUsbDevice;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyBeginEndNotifications;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
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
/*     */ public abstract class ModernRoboticsUsbI2cController
/*     */   extends ModernRoboticsUsbDevice
/*     */   implements I2cController
/*     */ {
/*     */   protected final int numberOfI2cPorts;
/*     */   protected final I2cController.I2cPortReadyBeginEndNotifications[] i2cPortReadyBeginEndCallbacks;
/*     */   protected boolean notificationsActive;
/*     */   
/*     */   public ModernRoboticsUsbI2cController(int numberOfI2cPorts, Context context, SerialNumber serialNumber, EventLoopManager manager, ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice, ModernRoboticsUsbDevice.CreateReadWriteRunnable createReadWriteRunnable)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/*  64 */     super(context, serialNumber, manager, openRobotUsbDevice, createReadWriteRunnable);
/*  65 */     this.numberOfI2cPorts = numberOfI2cPorts;
/*  66 */     this.i2cPortReadyBeginEndCallbacks = new I2cController.I2cPortReadyBeginEndNotifications[numberOfI2cPorts];
/*  67 */     this.notificationsActive = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void throwIfI2cPortIsInvalid(int port)
/*     */   {
/*  76 */     if ((port < 0) || (port >= this.numberOfI2cPorts))
/*     */     {
/*  78 */       throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", new Object[] { Integer.valueOf(port), Integer.valueOf(0), Integer.valueOf(this.numberOfI2cPorts - 1) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean isArmed()
/*     */   {
/*  89 */     return super.isArmed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void registerForPortReadyBeginEndCallback(I2cController.I2cPortReadyBeginEndNotifications callback, int port)
/*     */   {
/* 100 */     throwIfI2cPortIsInvalid(port);
/* 101 */     if (callback == null) {
/* 102 */       throw new IllegalArgumentException(String.format("illegal null: registerForI2cNotificationsCallback(null,%d)", new Object[] { Integer.valueOf(port) }));
/*     */     }
/*     */     
/* 105 */     deregisterForPortReadyBeginEndCallback(port);
/*     */     
/*     */ 
/* 108 */     this.i2cPortReadyBeginEndCallbacks[port] = callback;
/*     */     
/*     */ 
/* 111 */     if (this.notificationsActive) {
/*     */       try
/*     */       {
/* 114 */         callback.onPortIsReadyCallbacksBegin(port);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 118 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized I2cController.I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback(int port)
/*     */   {
/* 126 */     throwIfI2cPortIsInvalid(port);
/* 127 */     return this.i2cPortReadyBeginEndCallbacks[port];
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void deregisterForPortReadyBeginEndCallback(int port)
/*     */   {
/* 133 */     throwIfI2cPortIsInvalid(port);
/*     */     
/*     */ 
/*     */ 
/* 137 */     if (this.i2cPortReadyBeginEndCallbacks[port] != null) {
/*     */       try
/*     */       {
/* 140 */         this.i2cPortReadyBeginEndCallbacks[port].onPortIsReadyCallbacksEnd(port);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 144 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 149 */     this.i2cPortReadyBeginEndCallbacks[port] = null;
/*     */   }
/*     */   
/*     */   public void startupComplete()
/*     */     throws InterruptedException
/*     */   {
/* 155 */     this.notificationsActive = true;
/*     */     
/* 157 */     if (this.i2cPortReadyBeginEndCallbacks != null)
/*     */     {
/* 159 */       for (int port = 0; port < this.numberOfI2cPorts; port++)
/*     */       {
/* 161 */         I2cController.I2cPortReadyBeginEndNotifications callback = this.i2cPortReadyBeginEndCallbacks[port];
/* 162 */         if (callback != null)
/*     */         {
/* 164 */           callback.onPortIsReadyCallbacksBegin(port);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdownComplete()
/*     */     throws InterruptedException
/*     */   {
/* 173 */     if (this.i2cPortReadyBeginEndCallbacks != null)
/*     */     {
/* 175 */       for (int port = 0; port < this.numberOfI2cPorts; port++)
/*     */       {
/* 177 */         I2cController.I2cPortReadyBeginEndNotifications callback = this.i2cPortReadyBeginEndCallbacks[port];
/* 178 */         if (callback != null)
/*     */         {
/* 180 */           callback.onPortIsReadyCallbacksEnd(port);
/*     */         }
/*     */       }
/*     */     }
/* 184 */     this.notificationsActive = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbI2cController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */