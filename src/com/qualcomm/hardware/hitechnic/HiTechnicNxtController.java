/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.robotcore.hardware.Engagable;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.I2cDevice;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
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
/*     */ public abstract class HiTechnicNxtController
/*     */   extends I2cControllerPortDeviceImpl
/*     */   implements Engagable, HardwareDevice
/*     */ {
/*     */   protected Context context;
/*     */   protected boolean isEngaged;
/*     */   protected boolean isHooked;
/*     */   protected I2cDevice i2cDevice;
/*     */   protected I2cDeviceSynch i2cDeviceSynch;
/*     */   protected boolean isHardwareInitialized;
/*     */   
/*     */   public HiTechnicNxtController(Context context, I2cController module, int physicalPort, I2cAddr i2cAddr)
/*     */   {
/*  72 */     super(module, physicalPort);
/*  73 */     this.context = context;
/*  74 */     this.isEngaged = true;
/*  75 */     this.isHooked = false;
/*  76 */     this.isHardwareInitialized = false;
/*     */     
/*  78 */     this.i2cDevice = new I2cDeviceImpl(module, physicalPort);
/*  79 */     this.i2cDeviceSynch = new I2cDeviceSynchImpl(this.i2cDevice, i2cAddr, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/*  88 */     return HardwareDevice.Manufacturer.HiTechnic;
/*     */   }
/*     */   
/*     */   public synchronized void close()
/*     */   {
/*  93 */     if (isEngaged())
/*     */     {
/*  95 */       floatHardware();
/*  96 */       disengage();
/*     */     }
/*  98 */     this.i2cDeviceSynch.close();
/*     */   }
/*     */   
/*     */   void initializeHardwareIfNecessary()
/*     */   {
/* 103 */     if ((!this.isHardwareInitialized) && (isArmed()))
/*     */     {
/* 105 */       this.isHardwareInitialized = true;
/* 106 */       initializeHardware();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void initializeHardware();
/*     */   
/*     */ 
/*     */   protected abstract void floatHardware();
/*     */   
/*     */ 
/*     */   public synchronized void engage()
/*     */   {
/* 120 */     this.isEngaged = true;
/* 121 */     adjustHookingToMatchEngagement();
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void disengage()
/*     */   {
/* 127 */     this.isEngaged = true;
/* 128 */     adjustHookingToMatchEngagement();
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized boolean isEngaged()
/*     */   {
/* 134 */     return this.isEngaged;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void controllerNowDisarmed()
/*     */   {
/* 144 */     if (this.isHooked)
/*     */     {
/* 146 */       unhook();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void adjustHookingToMatchEngagement()
/*     */   {
/* 153 */     if ((!this.isHooked) && (this.isEngaged))
/*     */     {
/* 155 */       hook();
/*     */     }
/* 157 */     else if ((this.isHooked) && (!this.isEngaged))
/*     */     {
/* 159 */       unhook();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void hook()
/*     */   {
/* 165 */     doHook();
/* 166 */     this.isHooked = true;
/* 167 */     initializeHardwareIfNecessary();
/*     */   }
/*     */   
/*     */   protected void unhook()
/*     */   {
/* 172 */     doUnhook();
/* 173 */     this.isHooked = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doHook() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doUnhook() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isArmed()
/*     */   {
/* 192 */     return this.i2cDeviceSynch.isArmed();
/*     */   }
/*     */   
/*     */   protected void write8(int ireg, byte data)
/*     */   {
/* 197 */     if (isEngaged())
/*     */     {
/* 199 */       this.i2cDeviceSynch.write8(ireg, data, false);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void write(int ireg, byte[] data)
/*     */   {
/* 205 */     if (isEngaged())
/*     */     {
/* 207 */       this.i2cDeviceSynch.write(ireg, data, false);
/*     */     }
/*     */   }
/*     */   
/*     */   protected byte read8(int ireg)
/*     */   {
/* 213 */     return isEngaged() ? this.i2cDeviceSynch.read8(ireg) : 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */