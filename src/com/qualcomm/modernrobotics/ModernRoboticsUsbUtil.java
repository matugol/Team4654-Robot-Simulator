/*     */ package com.qualcomm.modernrobotics;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.analytics.Analytics;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.Channel;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModernRoboticsUsbUtil
/*     */ {
/*     */   public static final int MFG_CODE_MODERN_ROBOTICS = 77;
/*     */   public static final int DEVICE_ID_DC_MOTOR_CONTROLLER = 77;
/*     */   public static final int DEVICE_ID_SERVO_CONTROLLER = 83;
/*     */   public static final int DEVICE_ID_LEGACY_MODULE = 73;
/*     */   public static final int DEVICE_ID_DEVICE_INTERFACE_MODULE = 65;
/*     */   private static Analytics a;
/*     */   
/*     */   public static void init(Context context, HardwareMap map)
/*     */   {
/*  84 */     if (a == null) {
/*  85 */       a = new Analytics(context, "update_rc", map);
/*     */     }
/*     */   }
/*     */   
/*     */   public static RobotUsbDevice openUsbDevice(RobotUsbManager usbManager, SerialNumber serialNumber) throws RobotCoreException {
/*  90 */     return a(usbManager, serialNumber);
/*     */   }
/*     */   
/*     */   private static RobotUsbDevice a(RobotUsbManager paramRobotUsbManager, SerialNumber paramSerialNumber)
/*     */     throws RobotCoreException
/*     */   {
/*  96 */     String str = "";
/*     */     
/*     */ 
/*  99 */     int i = 0;
/* 100 */     int j = paramRobotUsbManager.scanForDevices();
/* 101 */     for (int k = 0; k < j; k++) {
/* 102 */       if (paramSerialNumber.equals(paramRobotUsbManager.getDeviceSerialNumberByIndex(k))) {
/* 103 */         i = 1;
/*     */         
/* 105 */         str = paramRobotUsbManager.getDeviceDescriptionByIndex(k) + " [" + paramSerialNumber.getSerialNumber() + "]";
/*     */         
/* 107 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 112 */     if (i == 0) { a("unable to find USB device with serial number " + paramSerialNumber.toString());
/*     */     }
/* 114 */     RobotUsbDevice localRobotUsbDevice = null;
/*     */     try {
/* 116 */       localRobotUsbDevice = paramRobotUsbManager.openBySerialNumber(paramSerialNumber);
/*     */     } catch (RobotCoreException localRobotCoreException1) {
/* 118 */       a("Unable to open USB device " + paramSerialNumber + " - " + str + ": " + localRobotCoreException1.getMessage());
/*     */     }
/*     */     try
/*     */     {
/* 122 */       localRobotUsbDevice.setBaudRate(250000);
/* 123 */       localRobotUsbDevice.setDataCharacteristics((byte)8, (byte)0, (byte)0);
/* 124 */       localRobotUsbDevice.setLatencyTimer(2);
/*     */     } catch (RobotCoreException localRobotCoreException2) {
/* 126 */       localRobotUsbDevice.close();
/* 127 */       a("Unable to open USB device " + paramSerialNumber + " - " + str + ": " + localRobotCoreException2.getMessage());
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 133 */       Thread.sleep(400L);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {
/* 136 */       Thread.currentThread().interrupt();
/*     */     }
/*     */     
/* 139 */     return localRobotUsbDevice;
/*     */   }
/*     */   
/*     */   public static byte[] getUsbDeviceHeader(RobotUsbDevice dev) throws RobotCoreException {
/* 143 */     return a(dev);
/*     */   }
/*     */   
/*     */   private static byte[] a(RobotUsbDevice paramRobotUsbDevice) throws RobotCoreException
/*     */   {
/* 148 */     int i = 3;
/*     */     
/*     */ 
/* 151 */     byte[] arrayOfByte1 = new byte[5];
/* 152 */     byte[] arrayOfByte2 = new byte[3];
/* 153 */     byte[] arrayOfByte3 = new byte[5];
/* 154 */     arrayOfByte3[0] = 85;
/* 155 */     arrayOfByte3[1] = -86;
/* 156 */     arrayOfByte3[2] = Byte.MIN_VALUE;
/* 157 */     arrayOfByte3[3] = 0;
/* 158 */     arrayOfByte3[4] = 3;
/*     */     try
/*     */     {
/* 161 */       paramRobotUsbDevice.purge(RobotUsbDevice.Channel.RX);
/* 162 */       paramRobotUsbDevice.write(arrayOfByte3);
/* 163 */       paramRobotUsbDevice.read(arrayOfByte1);
/*     */     } catch (RobotCoreException localRobotCoreException) {
/* 165 */       a("error reading Modern Robotics USB device headers");
/*     */     }
/*     */     
/* 168 */     if (!a.a(arrayOfByte1, 3)) {
/* 169 */       return arrayOfByte2;
/*     */     }
/*     */     
/* 172 */     paramRobotUsbDevice.read(arrayOfByte2);
/* 173 */     return arrayOfByte2;
/*     */   }
/*     */   
/*     */   public static DeviceManager.DeviceType getDeviceType(byte[] deviceHeader) {
/* 177 */     return a(deviceHeader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static DeviceManager.DeviceType a(byte[] paramArrayOfByte)
/*     */   {
/* 184 */     if (paramArrayOfByte[1] != 77) {
/* 185 */       return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
/*     */     }
/*     */     
/* 188 */     switch (paramArrayOfByte[2]) {
/*     */     case 77: 
/* 190 */       return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
/*     */     case 83: 
/* 192 */       return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
/*     */     case 73: 
/* 194 */       return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE;
/*     */     case 65: 
/* 196 */       return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE;
/*     */     }
/* 198 */     return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_UNKNOWN_DEVICE;
/*     */   }
/*     */   
/*     */   private static void a(String paramString) throws RobotCoreException
/*     */   {
/* 203 */     System.err.println(paramString);
/* 204 */     throw new RobotCoreException(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\ModernRobotics-release.jar!\classes.jar!\com\qualcomm\modernrobotics\ModernRoboticsUsbUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */