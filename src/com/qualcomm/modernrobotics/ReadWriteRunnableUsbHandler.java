/*     */ package com.qualcomm.modernrobotics;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.Channel;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.Util;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReadWriteRunnableUsbHandler
/*     */ {
/*  29 */   protected final int MAX_SEQUENTIAL_USB_ERROR_COUNT = 10;
/*  30 */   protected final int USB_MSG_TIMEOUT = 100;
/*     */   
/*  32 */   protected int usbSequentialReadErrorCount = 0;
/*  33 */   protected int usbSequentialWriteErrorCount = 0;
/*     */   
/*     */   protected RobotUsbDevice device;
/*  36 */   protected final byte[] respHeader = new byte[5];
/*     */   
/*  38 */   protected byte[] writeCmd = { 85, -86, 0, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  46 */   protected byte[] readCmd = { 85, -86, Byte.MIN_VALUE, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReadWriteRunnableUsbHandler(RobotUsbDevice device)
/*     */   {
/*  55 */     this.device = device;
/*     */   }
/*     */   
/*     */   public void throwIfUsbErrorCountIsTooHigh() throws RobotCoreException {
/*  59 */     if ((this.usbSequentialReadErrorCount > 10) || (this.usbSequentialWriteErrorCount > 10))
/*     */     {
/*  61 */       throw new RobotCoreException("Too many sequential USB errors on device");
/*     */     }
/*     */   }
/*     */   
/*     */   public void read(int address, byte[] buffer) throws RobotCoreException, InterruptedException {
/*  66 */     a(address, buffer);
/*     */   }
/*     */   
/*     */   private void a(int paramInt, byte[] paramArrayOfByte)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/*  72 */     this.readCmd[3] = ((byte)paramInt);
/*  73 */     this.readCmd[4] = ((byte)paramArrayOfByte.length);
/*  74 */     this.device.write(this.readCmd);
/*     */     
/*  76 */     Arrays.fill(this.respHeader, (byte)0);
/*  77 */     int i = this.device.read(this.respHeader, this.respHeader.length, 100);
/*  78 */     if (!a.a(this.respHeader, paramArrayOfByte.length)) {
/*  79 */       this.usbSequentialReadErrorCount += 1;
/*  80 */       if (i == this.respHeader.length)
/*     */       {
/*  82 */         Thread.sleep(100L);
/*  83 */         a(this.readCmd, "comm error");
/*     */       } else {
/*  85 */         a(this.readCmd, "comm timeout");
/*     */       }
/*     */     }
/*     */     
/*  89 */     i = this.device.read(paramArrayOfByte, paramArrayOfByte.length, 100);
/*  90 */     if (i != paramArrayOfByte.length) {
/*  91 */       a(this.readCmd, "comm timeout on payload");
/*     */     }
/*     */     
/*  94 */     this.usbSequentialReadErrorCount = 0;
/*     */   }
/*     */   
/*     */   public void write(int address, byte[] buffer) throws RobotCoreException, InterruptedException {
/*  98 */     b(address, buffer);
/*     */   }
/*     */   
/*     */   private void b(int paramInt, byte[] paramArrayOfByte)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 104 */     this.writeCmd[3] = ((byte)paramInt);
/* 105 */     this.writeCmd[4] = ((byte)paramArrayOfByte.length);
/* 106 */     this.device.write(Util.concatenateByteArrays(this.writeCmd, paramArrayOfByte));
/*     */     
/* 108 */     Arrays.fill(this.respHeader, (byte)0);
/* 109 */     int i = this.device.read(this.respHeader, this.respHeader.length, 100);
/* 110 */     if (!a.a(this.respHeader, 0)) {
/* 111 */       this.usbSequentialWriteErrorCount += 1;
/* 112 */       if (i == this.respHeader.length)
/*     */       {
/* 114 */         Thread.sleep(100L);
/* 115 */         a(this.writeCmd, "comm error");
/*     */       } else {
/* 117 */         a(this.writeCmd, "comm timeout");
/*     */       }
/*     */     }
/*     */     
/* 121 */     this.usbSequentialWriteErrorCount = 0;
/*     */   }
/*     */   
/*     */   public void purge(RobotUsbDevice.Channel channel) throws RobotCoreException {
/* 125 */     this.device.purge(channel);
/*     */   }
/*     */   
/*     */   public void close() {
/* 129 */     this.device.close();
/*     */   }
/*     */   
/*     */   private void a(byte[] paramArrayOfByte, String paramString) throws RobotCoreException {
/* 133 */     RobotLog.w(bufferToString(paramArrayOfByte) + " -> " + bufferToString(this.respHeader));
/* 134 */     this.device.purge(RobotUsbDevice.Channel.BOTH);
/* 135 */     throw new RobotCoreException(paramString);
/*     */   }
/*     */   
/*     */   protected static String bufferToString(byte[] buffer) {
/* 139 */     StringBuilder localStringBuilder = new StringBuilder();
/* 140 */     localStringBuilder.append("[");
/*     */     
/* 142 */     if (buffer.length > 0) {
/* 143 */       localStringBuilder.append(String.format("%02x", new Object[] { Byte.valueOf(buffer[0]) }));
/*     */     }
/*     */     
/* 146 */     for (int i = 1; i < buffer.length; i++) {
/* 147 */       localStringBuilder.append(String.format(" %02x", new Object[] { Byte.valueOf(buffer[i]) }));
/*     */     }
/*     */     
/* 150 */     localStringBuilder.append("]");
/* 151 */     return localStringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\ModernRobotics-release.jar!\classes.jar!\com\qualcomm\modernrobotics\ReadWriteRunnableUsbHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */