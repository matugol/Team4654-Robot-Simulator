/*     */ package com.qualcomm.modernrobotics;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.Channel;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class b
/*     */   implements RobotUsbDevice
/*     */ {
/*     */   public final boolean a;
/*     */   public SerialNumber b;
/*     */   public String c;
/*     */   private byte[] f;
/*     */   private byte[] g;
/*     */   private BlockingQueue<byte[]> h;
/*     */   protected final byte[] d;
/*     */   protected final byte[] e;
/*     */   
/*     */   public void setBaudRate(int rate)
/*     */     throws RobotCoreException
/*     */   {}
/*     */   
/*     */   public void setDataCharacteristics(byte dataBits, byte stopBits, byte parity)
/*     */     throws RobotCoreException
/*     */   {}
/*     */   
/*     */   public void setLatencyTimer(int latencyTimer)
/*     */     throws RobotCoreException
/*     */   {}
/*     */   
/*     */   public void purge(RobotUsbDevice.Channel channel)
/*     */     throws RobotCoreException
/*     */   {
/*  99 */     this.h.clear();
/*     */   }
/*     */   
/*     */   public void write(byte[] data) throws RobotCoreException
/*     */   {
/* 104 */     a(data);
/*     */   }
/*     */   
/*     */   public int read(byte[] data) throws RobotCoreException
/*     */   {
/* 109 */     return read(data, data.length, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public int read(byte[] data, int length, int timeout) throws RobotCoreException
/*     */   {
/* 114 */     return a(data, length, timeout);
/*     */   }
/*     */   
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */   private void a(final byte[] paramArrayOfByte)
/*     */   {
/* 123 */     if (this.a) { RobotLog.d(this.b + " USB recd: " + Arrays.toString(paramArrayOfByte));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */     new Thread()
/*     */     {
/*     */       public void run()
/*     */       {
/* 128 */         int i = TypeConversion.unsignedByteToInt(paramArrayOfByte[3]);
/* 129 */         int j = TypeConversion.unsignedByteToInt(paramArrayOfByte[4]);
/*     */         try
/*     */         {
/* 132 */           Thread.sleep(10L);
/*     */           
/*     */           byte[] arrayOfByte;
/* 135 */           switch (paramArrayOfByte[2]) {
/*     */           case -128: 
/* 137 */             arrayOfByte = new byte[b.this.e.length + j];
/* 138 */             System.arraycopy(b.this.e, 0, arrayOfByte, 0, b.this.e.length);
/* 139 */             arrayOfByte[3] = paramArrayOfByte[3];
/* 140 */             arrayOfByte[4] = paramArrayOfByte[4];
/* 141 */             System.arraycopy(b.a(b.this), i, arrayOfByte, b.this.e.length, j);
/* 142 */             break;
/*     */           case 0: 
/* 144 */             arrayOfByte = new byte[b.this.d.length];
/* 145 */             System.arraycopy(b.this.d, 0, arrayOfByte, 0, b.this.d.length);
/* 146 */             arrayOfByte[3] = paramArrayOfByte[3];
/* 147 */             arrayOfByte[4] = 0;
/* 148 */             System.arraycopy(paramArrayOfByte, 5, b.a(b.this), i, j);
/* 149 */             break;
/*     */           default: 
/* 151 */             arrayOfByte = Arrays.copyOf(paramArrayOfByte, paramArrayOfByte.length);
/* 152 */             arrayOfByte[2] = -1;
/* 153 */             arrayOfByte[3] = paramArrayOfByte[3];
/* 154 */             arrayOfByte[4] = 0;
/*     */           }
/* 156 */           b.b(b.this).put(arrayOfByte);
/*     */         } catch (InterruptedException localInterruptedException) {
/* 158 */           RobotLog.w("USB mock bus interrupted during write");
/*     */         }
/*     */       }
/*     */     }.start();
/*     */   }
/*     */   
/*     */   private int a(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 165 */     byte[] arrayOfByte = null;
/*     */     
/* 167 */     if (this.g != null) {
/* 168 */       arrayOfByte = Arrays.copyOf(this.g, this.g.length);
/* 169 */       this.g = null;
/*     */     } else {
/*     */       try {
/* 172 */         arrayOfByte = (byte[])this.h.poll(paramInt2, TimeUnit.MILLISECONDS);
/*     */       } catch (InterruptedException localInterruptedException) {
/* 174 */         RobotLog.w("USB mock bus interrupted during read");
/*     */       }
/*     */     }
/*     */     
/* 178 */     if (arrayOfByte == null) {
/* 179 */       RobotLog.w("USB mock bus read timeout");
/* 180 */       System.arraycopy(this.e, 0, paramArrayOfByte, 0, this.e.length);
/* 181 */       paramArrayOfByte[2] = -1;
/* 182 */       paramArrayOfByte[4] = 0;
/*     */     } else {
/* 184 */       System.arraycopy(arrayOfByte, 0, paramArrayOfByte, 0, paramInt1);
/*     */     }
/*     */     
/* 187 */     if ((arrayOfByte != null) && (paramInt1 < arrayOfByte.length)) {
/* 188 */       this.g = new byte[arrayOfByte.length - paramInt1];
/* 189 */       System.arraycopy(arrayOfByte, paramArrayOfByte.length, this.g, 0, this.g.length);
/*     */     }
/*     */     
/* 192 */     if (this.a) RobotLog.d(this.b + " USB send: " + Arrays.toString(paramArrayOfByte));
/* 193 */     return paramArrayOfByte.length;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\ModernRobotics-release.jar!\classes.jar!\com\qualcomm\modernrobotics\b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */