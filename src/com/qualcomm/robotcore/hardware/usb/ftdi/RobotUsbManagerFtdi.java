/*     */ package com.qualcomm.robotcore.hardware.usb.ftdi;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.ftdi.j2xx.D2xxManager;
/*     */ import com.ftdi.j2xx.D2xxManager.D2xxException;
/*     */ import com.ftdi.j2xx.D2xxManager.FtDeviceInfoListNode;
/*     */ import com.ftdi.j2xx.FT_Device;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
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
/*     */ public class RobotUsbManagerFtdi
/*     */   implements RobotUsbManager
/*     */ {
/*     */   private Context context;
/*     */   private D2xxManager d2xxManager;
/*     */   private int numberOfDevices;
/*  49 */   private boolean scanIsLocked = false;
/*     */   
/*     */ 
/*     */ 
/*     */   public RobotUsbManagerFtdi(Context context)
/*     */     throws RobotCoreException
/*     */   {
/*  56 */     this.context = context;
/*     */     try
/*     */     {
/*  59 */       this.d2xxManager = D2xxManager.getInstance(context);
/*     */     } catch (D2xxManager.D2xxException e) {
/*  61 */       RobotLog.e("Unable to create D2xxManager; cannot open USB devices");
/*  62 */       RobotLog.logStacktrace(e);
/*  63 */       throw RobotCoreException.createChained(e, "unable to create D2xxManager", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized int scanForDevices()
/*     */     throws RobotCoreException
/*     */   {
/*  74 */     if (!this.scanIsLocked) {
/*  75 */       this.numberOfDevices = this.d2xxManager.createDeviceInfoList(this.context);
/*     */     }
/*  77 */     return this.numberOfDevices;
/*     */   }
/*     */   
/*     */   public synchronized void freezeScanForDevices() {
/*  81 */     this.scanIsLocked = true;
/*     */   }
/*     */   
/*     */   public synchronized void thawScanForDevices() {
/*  85 */     this.scanIsLocked = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerialNumber getDeviceSerialNumberByIndex(int index)
/*     */     throws RobotCoreException
/*     */   {
/*  96 */     return new SerialNumber(this.d2xxManager.getDeviceInfoListDetail(index).serialNumber);
/*     */   }
/*     */   
/*     */   public String getDeviceDescriptionByIndex(int index)
/*     */     throws RobotCoreException
/*     */   {
/* 102 */     return this.d2xxManager.getDeviceInfoListDetail(index).description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RobotUsbDevice openBySerialNumber(SerialNumber serialNumber)
/*     */     throws RobotCoreException
/*     */   {
/* 113 */     FT_Device device = this.d2xxManager.openBySerialNumber(this.context, serialNumber.toString());
/* 114 */     if (device == null) {
/* 115 */       throw new RobotCoreException("FTDI driver failed to open USB device with serial number " + serialNumber + " (returned null device)");
/*     */     }
/*     */     
/* 118 */     return new RobotUsbDeviceFtdi(device, serialNumber);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\usb\ftdi\RobotUsbManagerFtdi.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */