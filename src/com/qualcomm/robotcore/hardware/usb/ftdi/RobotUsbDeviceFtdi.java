/*     */ package com.qualcomm.robotcore.hardware.usb.ftdi;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import com.ftdi.j2xx.D2xxManager.FtDeviceInfoListNode;
/*     */ import com.ftdi.j2xx.FT_Device;
/*     */ import com.qualcomm.robotcore.exception.FTDeviceClosedException;
/*     */ import com.qualcomm.robotcore.exception.FTDeviceIncompleteInitializationException;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.Channel;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.FirmwareVersion;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.USBIdentifiers;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RobotUsbDeviceFtdi
/*     */   implements RobotUsbDevice
/*     */ {
/*  55 */   protected static final ConcurrentHashMap<SerialNumber, RobotUsbDeviceFtdi> extantDevices = new ConcurrentHashMap();
/*  56 */   protected static final ConcurrentHashMap<SerialNumber, DeviceManager.DeviceType> deviceTypes = new ConcurrentHashMap();
/*     */   
/*     */   private FT_Device device;
/*     */   private RobotUsbDevice.FirmwareVersion firmwareVersion;
/*     */   private boolean purgeNeeded;
/*     */   private byte purgeWhat;
/*     */   private boolean interruptRequested;
/*     */   private SerialNumber serialNumber;
/*     */   private DeviceManager.DeviceType deviceType;
/*  65 */   private static final Object writeLock = new Object();
/*     */   
/*     */   public RobotUsbDeviceFtdi(FT_Device device, SerialNumber serialNumber) {
/*  68 */     this.device = device;
/*  69 */     this.firmwareVersion = null;
/*  70 */     this.purgeNeeded = false;
/*  71 */     this.purgeWhat = 0;
/*  72 */     this.interruptRequested = false;
/*  73 */     this.serialNumber = serialNumber;
/*  74 */     this.deviceType = ((DeviceManager.DeviceType)deviceTypes.get(this.serialNumber));
/*  75 */     if (this.deviceType == null) { this.deviceType = DeviceManager.DeviceType.UNKNOWN_DEVICE;
/*     */     }
/*  77 */     Assert.assertFalse(extantDevices.contains(serialNumber));
/*  78 */     extantDevices.put(serialNumber, this);
/*     */   }
/*     */   
/*     */   public static Collection<RobotUsbDeviceFtdi> getExtantDevices() {
/*  82 */     return extantDevices.values();
/*     */   }
/*     */   
/*     */   @NonNull
/*  86 */   public SerialNumber getSerialNumber() { return this.serialNumber; }
/*     */   
/*     */   public void setDeviceType(@NonNull DeviceManager.DeviceType deviceType)
/*     */   {
/*  90 */     this.deviceType = deviceType;
/*  91 */     deviceTypes.put(this.serialNumber, deviceType);
/*     */   }
/*     */   
/*     */   @NonNull
/*  95 */   public DeviceManager.DeviceType getDeviceType() { return this.deviceType; }
/*     */   
/*     */   private void purgeIfNecessary()
/*     */   {
/*  99 */     if (this.purgeNeeded) {
/* 100 */       this.purgeNeeded = false;
/* 101 */       this.device.purge(this.purgeWhat);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBaudRate(int rate) throws RobotCoreException
/*     */   {
/* 107 */     if (!this.device.setBaudRate(rate)) {
/* 108 */       throw new RobotCoreException("FTDI driver failed to set baud rate to " + rate);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDataCharacteristics(byte dataBits, byte stopBits, byte parity) throws RobotCoreException
/*     */   {
/* 114 */     if (!this.device.setDataCharacteristics(dataBits, stopBits, parity)) {
/* 115 */       throw new RobotCoreException("FTDI driver failed to set data characteristics");
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLatencyTimer(int latencyTimer) throws RobotCoreException
/*     */   {
/* 121 */     if (!this.device.setLatencyTimer((byte)latencyTimer)) {
/* 122 */       throw new RobotCoreException("FTDI driver failed to set latency timer to " + latencyTimer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBreak(boolean enable) throws RobotCoreException {
/* 127 */     boolean success = enable ? this.device.setBreakOn() : this.device.setBreakOff();
/* 128 */     if (!success) {
/* 129 */       throw new RobotCoreException("FTDI driver failed to set/clear break mode");
/*     */     }
/*     */   }
/*     */   
/*     */   public void purge(RobotUsbDevice.Channel channel) throws RobotCoreException
/*     */   {
/* 135 */     purgeIfNecessary();
/* 136 */     byte purgeByte = 0;
/*     */     
/* 138 */     switch (channel) {
/* 139 */     case RX:  purgeByte = 1; break;
/* 140 */     case TX:  purgeByte = 2; break;
/* 141 */     case BOTH:  purgeByte = 3;
/*     */     }
/*     */     
/* 144 */     this.purgeWhat = purgeByte;
/* 145 */     this.purgeNeeded = true;
/*     */   }
/*     */   
/*     */   public void write(byte[] data) throws RobotCoreException, NullPointerException
/*     */   {
/* 150 */     purgeIfNecessary();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */     synchronized (writeLock) {
/* 165 */       this.device.write(data);
/*     */     }
/*     */   }
/*     */   
/*     */   public int read(byte[] data) throws RobotCoreException, InterruptedException, NullPointerException
/*     */   {
/* 171 */     return read(data, data.length, this.device.getReadTimeout());
/*     */   }
/*     */   
/*     */   public int read(byte[] data, int cbToRead, int msTimeoutRemaining) throws RobotCoreException, InterruptedException, NullPointerException
/*     */   {
/* 176 */     return read(data, cbToRead, msTimeoutRemaining);
/*     */   }
/*     */   
/*     */   public int read(byte[] data, int cbToRead, long msTimeoutRemaining) throws RobotCoreException, InterruptedException, NullPointerException
/*     */   {
/* 181 */     purgeIfNecessary();
/* 182 */     while (msTimeoutRemaining > 0L)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 195 */       long msTimeoutQuantum = 100L;
/* 196 */       long msTimeout = Math.min(100L, msTimeoutRemaining);
/* 197 */       msTimeoutRemaining -= msTimeout;
/*     */       
/* 199 */       int cbRead = this.device.read(data, cbToRead, msTimeout);
/*     */       
/* 201 */       if (cbRead == cbToRead)
/* 202 */         return cbRead;
/* 203 */       if (cbRead < 0) {
/* 204 */         switch (cbRead) {
/*     */         case -1: 
/* 206 */           throw new FTDeviceClosedException("error: closed: FT_Device.read()=%d", new Object[] { Integer.valueOf(cbRead) });
/*     */         case -2: 
/* 208 */           throw new IllegalArgumentException(String.format("error: non-positive read length %d passed to FT_Device.read()", new Object[] { Integer.valueOf(cbToRead) }));
/*     */         case -3: 
/* 210 */           throw new FTDeviceIncompleteInitializationException("error: inc init: FT_Device.read()=%d", new Object[] { Integer.valueOf(cbRead) });
/*     */         }
/* 212 */         throw new RobotCoreException("error: FT_Device.read()=%d", new Object[] { Integer.valueOf(cbRead) });
/*     */       }
/* 214 */       if (cbRead == 0)
/*     */       {
/* 216 */         if ((this.interruptRequested) || (Thread.currentThread().isInterrupted())) {
/* 217 */           throw new InterruptedException();
/*     */         }
/*     */       } else {
/* 220 */         throw new RobotCoreException("unexpected result %d from FT_Device_.read()", new Object[] { Integer.valueOf(cbRead) });
/*     */       }
/*     */     }
/*     */     
/* 224 */     return 0;
/*     */   }
/*     */   
/*     */   public void requestInterrupt()
/*     */   {
/* 229 */     this.interruptRequested = true;
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 234 */     this.device.close();
/* 235 */     extantDevices.remove(this.serialNumber);
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 240 */     return this.device.isOpen();
/*     */   }
/*     */   
/*     */   public RobotUsbDevice.FirmwareVersion getFirmwareVersion()
/*     */   {
/* 245 */     return this.firmwareVersion;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setFirmwareVersion(RobotUsbDevice.FirmwareVersion version)
/*     */   {
/* 251 */     this.firmwareVersion = version;
/*     */   }
/*     */   
/*     */   public RobotUsbDevice.USBIdentifiers getUsbIdentifiers()
/*     */   {
/* 256 */     RobotUsbDevice.USBIdentifiers result = new RobotUsbDevice.USBIdentifiers();
/*     */     
/* 258 */     int id = this.device.getDeviceInfo().id;
/* 259 */     result.vendorId = (id >> 16 & 0xFFFF);
/* 260 */     result.productId = (id & 0xFFFF);
/* 261 */     result.bcdDevice = this.device.getDeviceInfo().bcdDevice;
/* 262 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\usb\ftdi\RobotUsbDeviceFtdi.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */