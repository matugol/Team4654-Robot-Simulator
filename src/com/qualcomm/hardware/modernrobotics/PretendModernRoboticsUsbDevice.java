/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.Channel;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.FirmwareVersion;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.USBIdentifiers;
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
/*     */ public class PretendModernRoboticsUsbDevice
/*     */   implements RobotUsbDevice
/*     */ {
/*  48 */   protected RobotUsbDevice.FirmwareVersion firmwareVersion = null;
/*  49 */   protected byte cbExpected = 0;
/*  50 */   protected boolean interruptRequested = false;
/*     */   protected SerialNumber serialNumber;
/*  52 */   protected DeviceManager.DeviceType deviceType = DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
/*     */   
/*     */   public PretendModernRoboticsUsbDevice(SerialNumber serialNumber)
/*     */   {
/*  56 */     this.serialNumber = serialNumber;
/*     */   }
/*     */   
/*     */   @NonNull
/*  60 */   public SerialNumber getSerialNumber() { return this.serialNumber; }
/*     */   
/*     */   public void setDeviceType(@NonNull DeviceManager.DeviceType deviceType)
/*     */   {
/*  64 */     this.deviceType = deviceType;
/*     */   }
/*     */   
/*     */   @NonNull
/*  68 */   public DeviceManager.DeviceType getDeviceType() { return this.deviceType; }
/*     */   
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public void setBaudRate(int i) throws RobotCoreException
/*     */   {}
/*     */   
/*     */   public void setDataCharacteristics(byte b, byte b1, byte b2) throws RobotCoreException
/*     */   {}
/*     */   
/*     */   public void setLatencyTimer(int i) throws RobotCoreException
/*     */   {}
/*     */   
/*     */   public void setBreak(boolean enable) throws RobotCoreException
/*     */   {}
/*     */   
/*     */   public void purge(RobotUsbDevice.Channel channel) throws RobotCoreException
/*     */   {}
/*     */   
/*     */   public int read(byte[] bytes) throws RobotCoreException, InterruptedException {
/*  94 */     return read(bytes, bytes.length, 0);
/*     */   }
/*     */   
/*     */   public void write(byte[] bytes) throws RobotCoreException
/*     */   {
/*  99 */     byte bCommand = bytes[2];
/* 100 */     this.cbExpected = (bCommand == 0 ? 0 : bytes[4]);
/*     */   }
/*     */   
/*     */   public int read(byte[] bytes, int cbReadExpected, int timeout) throws RobotCoreException, InterruptedException {
/* 104 */     return read(bytes, cbReadExpected, timeout);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public int read(byte[] bytes, int cbReadExpected, long timeout)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 122 */     Thread.sleep(3L, 500000);
/*     */     
/*     */ 
/*     */ 
/* 126 */     bytes[0] = 51;
/* 127 */     bytes[1] = -52;
/* 128 */     bytes[4] = this.cbExpected;
/* 129 */     return cbReadExpected;
/*     */   }
/*     */   
/*     */ 
/*     */   public RobotUsbDevice.FirmwareVersion getFirmwareVersion()
/*     */   {
/* 135 */     return this.firmwareVersion;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setFirmwareVersion(RobotUsbDevice.FirmwareVersion version)
/*     */   {
/* 141 */     this.firmwareVersion = version;
/*     */   }
/*     */   
/*     */ 
/*     */   public void requestInterrupt()
/*     */   {
/* 147 */     this.interruptRequested = true;
/*     */   }
/*     */   
/*     */ 
/*     */   public RobotUsbDevice.USBIdentifiers getUsbIdentifiers()
/*     */   {
/* 153 */     RobotUsbDevice.USBIdentifiers result = new RobotUsbDevice.USBIdentifiers();
/* 154 */     result.vendorId = 1027;
/* 155 */     result.productId = 0;
/* 156 */     result.bcdDevice = 0;
/* 157 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\PretendModernRoboticsUsbDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */