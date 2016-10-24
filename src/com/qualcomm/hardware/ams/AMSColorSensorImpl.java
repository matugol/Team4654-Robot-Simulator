/*     */ package com.qualcomm.hardware.ams;
/*     */ 
/*     */ import android.graphics.Color;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cDevice;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AMSColorSensorImpl
/*     */   extends I2cDeviceSynchDevice<I2cDeviceSynchSimple>
/*     */   implements AMSColorSensor
/*     */ {
/*     */   protected AMSColorSensor.Parameters parameters;
/*     */   
/*     */   protected AMSColorSensorImpl(AMSColorSensor.Parameters params, I2cDeviceSynchSimple deviceClient, boolean isOwned)
/*     */   {
/*  71 */     super(deviceClient, isOwned);
/*  72 */     this.parameters = params;
/*  73 */     this.deviceClient.setLogging(this.parameters.loggingEnabled);
/*  74 */     this.deviceClient.setLoggingTag(this.parameters.loggingTag);
/*  75 */     super.registerArmingStateCallback();
/*     */   }
/*     */   
/*     */   public static AMSColorSensor create(AMSColorSensor.Parameters parameters, I2cDevice i2cDevice)
/*     */   {
/*  80 */     I2cDeviceSynchSimple i2cDeviceSynchSimple = new I2cDeviceSynchImpl(i2cDevice, parameters.i2cAddr, false);
/*  81 */     return create(parameters, i2cDeviceSynchSimple, true);
/*     */   }
/*     */   
/*     */   public static AMSColorSensor create(AMSColorSensor.Parameters parameters, I2cDeviceSynchSimple i2cDevice, boolean isOwned) {
/*  85 */     AMSColorSensor result = new AMSColorSensorImpl(parameters, i2cDevice, isOwned);
/*  86 */     result.initialize(parameters);
/*  87 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AMSColorSensor.Parameters getParameters()
/*     */   {
/*  96 */     return this.parameters;
/*     */   }
/*     */   
/*     */ 
/*     */   protected synchronized boolean doInitialize()
/*     */   {
/* 102 */     return initialize(this.parameters);
/*     */   }
/*     */   
/*     */   public synchronized boolean initialize(AMSColorSensor.Parameters parameters)
/*     */   {
/* 107 */     if (initializeOnce(parameters))
/*     */     {
/* 109 */       this.isInitialized = true;
/* 110 */       return true;
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   private synchronized boolean initializeOnce(AMSColorSensor.Parameters parameters)
/*     */   {
/* 117 */     if ((this.parameters != null) && (this.parameters.deviceId != parameters.deviceId))
/*     */     {
/* 119 */       throw new IllegalArgumentException(String.format("can't change device types (modify existing params instead): old=%d new=%d", new Object[] { Integer.valueOf(this.parameters.deviceId), Integer.valueOf(parameters.deviceId) }));
/*     */     }
/*     */     
/*     */ 
/* 123 */     this.parameters = parameters;
/*     */     
/*     */ 
/* 126 */     if (!this.deviceClient.isArmed())
/*     */     {
/* 128 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 132 */     engage();
/*     */     
/*     */ 
/* 135 */     setI2cAddress(parameters.i2cAddr);
/*     */     
/*     */ 
/* 138 */     byte id = getDeviceID();
/* 139 */     if (id != parameters.deviceId)
/*     */     {
/* 141 */       RobotLog.e("unexpected AMS color sensor chipid: found=%d expected=%d", new Object[] { Byte.valueOf(id), Integer.valueOf(parameters.deviceId) });
/* 142 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 146 */     setIntegrationTime(parameters.integrationTime);
/* 147 */     setGain(parameters.gain);
/*     */     
/*     */ 
/* 150 */     if (((this.deviceClient instanceof I2cDeviceSynch)) && (parameters.readWindow != null))
/*     */     {
/* 152 */       I2cDeviceSynch synch = (I2cDeviceSynch)this.deviceClient;
/* 153 */       synch.setReadWindow(parameters.readWindow);
/*     */     }
/*     */     
/*     */ 
/* 157 */     enable();
/* 158 */     return true;
/*     */   }
/*     */   
/*     */   private synchronized void enable()
/*     */   {
/* 163 */     write8(AMSColorSensor.Register.ENABLE, 1);
/* 164 */     delayLore(6);
/* 165 */     write8(AMSColorSensor.Register.ENABLE, 3);
/*     */   }
/*     */   
/*     */ 
/*     */   private synchronized void disable()
/*     */   {
/* 171 */     byte reg = read8(AMSColorSensor.Register.ENABLE);
/* 172 */     write8(AMSColorSensor.Register.ENABLE, reg & 0xFFFFFFFC);
/*     */   }
/*     */   
/*     */   private void setIntegrationTime(AMSColorSensor.IntegrationTime time)
/*     */   {
/* 177 */     write8(AMSColorSensor.Register.ATIME, time.byteVal);
/*     */   }
/*     */   
/*     */   private boolean is3782()
/*     */   {
/* 182 */     return (this.parameters.deviceId == 96) || (this.parameters.deviceId == 105);
/*     */   }
/*     */   
/*     */ 
/*     */   private void setGain(AMSColorSensor.Gain gain)
/*     */   {
/* 188 */     write8(AMSColorSensor.Register.CONTROL, gain.byteVal | (is3782() ? 32 : 0));
/*     */   }
/*     */   
/*     */   private void reportEnable()
/*     */   {
/* 193 */     int b = TypeConversion.unsignedByteToInt(read8(AMSColorSensor.Register.ENABLE));
/* 194 */     RobotLog.v("enable=0x%08x", new Object[] { Integer.valueOf(b) });
/*     */   }
/*     */   
/*     */   private void reportIntegrationTime()
/*     */   {
/* 199 */     int b = TypeConversion.unsignedByteToInt(read8(AMSColorSensor.Register.ATIME));
/* 200 */     RobotLog.v("integration=0x%08x", new Object[] { Integer.valueOf(b) });
/*     */   }
/*     */   
/*     */   private void reportGain()
/*     */   {
/* 205 */     int b = TypeConversion.unsignedByteToInt(read8(AMSColorSensor.Register.CONTROL));
/* 206 */     RobotLog.v("gain=0x%08x", new Object[] { Integer.valueOf(b) });
/*     */   }
/*     */   
/*     */   public byte getDeviceID()
/*     */   {
/* 211 */     return read8(AMSColorSensor.Register.DEVICE_ID);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized int red()
/*     */   {
/* 217 */     return readColorRegister(AMSColorSensor.Register.RED);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized int green()
/*     */   {
/* 223 */     return readColorRegister(AMSColorSensor.Register.GREEN);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized int blue()
/*     */   {
/* 229 */     return readColorRegister(AMSColorSensor.Register.BLUE);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized int alpha()
/*     */   {
/* 235 */     return readColorRegister(AMSColorSensor.Register.CLEAR);
/*     */   }
/*     */   
/*     */   private int readColorRegister(AMSColorSensor.Register reg)
/*     */   {
/* 240 */     return readUnsignedShort(reg);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized int argb()
/*     */   {
/* 246 */     byte[] bytes = read(AMSColorSensor.Register.CLEAR, 8);
/* 247 */     ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
/*     */     
/* 249 */     int clear = TypeConversion.unsignedShortToInt(buffer.getShort());
/* 250 */     int red = TypeConversion.unsignedShortToInt(buffer.getShort());
/* 251 */     int green = TypeConversion.unsignedShortToInt(buffer.getShort());
/* 252 */     int blue = TypeConversion.unsignedShortToInt(buffer.getShort());
/*     */     
/* 254 */     return Color.argb(clear, red, green, blue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized void enableLed(boolean enable)
/*     */   {
/* 261 */     throw new UnsupportedOperationException("controlling LED is not supported on this color sensor; use a digital channel for that.");
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized I2cAddr getI2cAddress()
/*     */   {
/* 267 */     return this.deviceClient.getI2cAddr();
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void setI2cAddress(I2cAddr i2cAddr)
/*     */   {
/* 273 */     this.deviceClient.setI2cAddr(i2cAddr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 283 */     return "AMS I2C Color Sensor";
/*     */   }
/*     */   
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 289 */     return HardwareDevice.Manufacturer.AMS;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized byte read8(AMSColorSensor.Register reg)
/*     */   {
/* 299 */     return this.deviceClient.read8(reg.byteVal | 0x80);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized byte[] read(AMSColorSensor.Register reg, int cb)
/*     */   {
/* 305 */     return this.deviceClient.read(reg.byteVal | 0x80, cb);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void write8(AMSColorSensor.Register reg, int data)
/*     */   {
/* 311 */     this.deviceClient.write8(reg.byteVal | 0x80, data);
/* 312 */     this.deviceClient.waitForWriteCompletions();
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void write(AMSColorSensor.Register reg, byte[] data)
/*     */   {
/* 318 */     this.deviceClient.write(reg.byteVal | 0x80, data);
/* 319 */     this.deviceClient.waitForWriteCompletions();
/*     */   }
/*     */   
/*     */ 
/*     */   public int readUnsignedShort(AMSColorSensor.Register reg)
/*     */   {
/* 325 */     byte[] bytes = read(reg, 2);
/* 326 */     int result = 0;
/* 327 */     if (bytes.length == 2)
/*     */     {
/* 329 */       ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
/* 330 */       result = TypeConversion.unsignedShortToInt(buffer.getShort());
/*     */     }
/* 332 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeShort(AMSColorSensor.Register ireg, int value)
/*     */   {
/* 338 */     byte[] bytes = TypeConversion.shortToByteArray((short)value, ByteOrder.LITTLE_ENDIAN);
/* 339 */     write(ireg, bytes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void delay(int ms)
/*     */   {
/*     */     try
/*     */     {
/* 350 */       Thread.sleep(ms);
/*     */     }
/*     */     catch (InterruptedException e)
/*     */     {
/* 354 */       Thread.currentThread().interrupt();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void delayLore(int ms)
/*     */   {
/*     */     try
/*     */     {
/* 366 */       Thread.sleep(ms);
/*     */     }
/*     */     catch (InterruptedException e)
/*     */     {
/* 370 */       Thread.currentThread().interrupt();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\ams\AMSColorSensorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */