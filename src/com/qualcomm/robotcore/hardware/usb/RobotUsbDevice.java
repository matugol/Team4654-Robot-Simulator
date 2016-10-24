/*    */ package com.qualcomm.robotcore.hardware.usb;
/*    */ 
/*    */ import android.support.annotation.NonNull;
/*    */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*    */ import java.util.Set;
/*    */ 
/*    */ public abstract interface RobotUsbDevice
/*    */ {
/*    */   public abstract void setBaudRate(int paramInt) throws RobotCoreException;
/*    */   
/*    */   public abstract void setDataCharacteristics(byte paramByte1, byte paramByte2, byte paramByte3) throws RobotCoreException;
/*    */   
/*    */   public abstract void setLatencyTimer(int paramInt) throws RobotCoreException;
/*    */   
/*    */   public abstract void setBreak(boolean paramBoolean) throws RobotCoreException;
/*    */   
/*    */   public abstract void purge(Channel paramChannel) throws RobotCoreException;
/*    */   
/*    */   public abstract void write(byte[] paramArrayOfByte) throws RobotCoreException, NullPointerException;
/*    */   
/*    */   public abstract int read(byte[] paramArrayOfByte) throws RobotCoreException, InterruptedException, NullPointerException;
/*    */   
/*    */   public abstract int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws RobotCoreException, InterruptedException, NullPointerException;
/*    */   
/*    */   public abstract int read(byte[] paramArrayOfByte, int paramInt, long paramLong) throws RobotCoreException, InterruptedException, NullPointerException;
/*    */   
/*    */   public abstract void requestInterrupt();
/*    */   
/*    */   public abstract void close();
/*    */   
/*    */   public abstract boolean isOpen();
/*    */   
/*    */   public abstract FirmwareVersion getFirmwareVersion();
/*    */   
/*    */   public abstract void setFirmwareVersion(FirmwareVersion paramFirmwareVersion);
/*    */   
/*    */   public abstract USBIdentifiers getUsbIdentifiers();
/*    */   
/*    */   @NonNull
/*    */   public abstract com.qualcomm.robotcore.util.SerialNumber getSerialNumber();
/*    */   
/*    */   public abstract void setDeviceType(@NonNull com.qualcomm.robotcore.hardware.DeviceManager.DeviceType paramDeviceType);
/*    */   
/*    */   @NonNull
/*    */   public abstract com.qualcomm.robotcore.hardware.DeviceManager.DeviceType getDeviceType();
/*    */   
/*    */   public static enum Channel
/*    */   {
/* 49 */     RX,  TX,  NONE,  BOTH;
/*    */     
/*    */     private Channel() {} }
/*    */   
/*    */   public static class FirmwareVersion { public int majorVersion;
/*    */     public int minorVersion;
/*    */     
/* 56 */     public FirmwareVersion() { this(0); }
/*    */     
/*    */     public FirmwareVersion(int bVersion) {
/* 59 */       this.majorVersion = (bVersion >> 4 & 0xF);
/* 60 */       this.minorVersion = (bVersion >> 0 & 0xF);
/*    */     }
/*    */     
/*    */     public String toString() {
/* 64 */       return String.format("v%d.%d", new Object[] { Integer.valueOf(this.majorVersion), Integer.valueOf(this.minorVersion) });
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public static class USBIdentifiers
/*    */   {
/*    */     public int vendorId;
/*    */     public int productId;
/*    */     public int bcdDevice;
/*    */     private static final int vendorIdFTDI = 1027;
/* 75 */     private static final Set<Integer> productIdsModernRobotics = new java.util.HashSet(java.util.Arrays.asList(new Integer[] { Integer.valueOf(24577) }));
/* 76 */     private static final Set<Integer> bcdDevicesModernRobotics = new java.util.HashSet(java.util.Arrays.asList(new Integer[] { Integer.valueOf(1536) }));
/*    */     
/*    */     public boolean isModernRoboticsDevice() {
/* 79 */       return (this.vendorId == 1027) && (productIdsModernRobotics.contains(Integer.valueOf(this.productId))) && (bcdDevicesModernRobotics.contains(Integer.valueOf(this.bcdDevice & 0xFF00)));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\usb\RobotUsbDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */