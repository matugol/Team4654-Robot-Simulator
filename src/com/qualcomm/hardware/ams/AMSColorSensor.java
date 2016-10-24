/*     */ package com.qualcomm.hardware.ams;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.ColorSensor;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadMode;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadWindow;
/*     */ 
/*     */ public abstract interface AMSColorSensor extends ColorSensor
/*     */ {
/*     */   public static final int AMS_TCS34725_ADDRESS = 41;
/*     */   public static final int AMS_TMD37821_ADDRESS = 57;
/*     */   public static final byte AMS_TCS34725_ID = 68;
/*     */   public static final byte AMS_TMD37821_ID = 96;
/*     */   public static final byte AMS_TMD37823_ID = 105;
/*     */   public static final int AMS_COLOR_COMMAND_BIT = 128;
/*     */   public static final int AMS_COLOR_ENABLE = 0;
/*     */   public static final int AMS_COLOR_ENABLE_PIEN = 32;
/*     */   public static final int AMS_COLOR_ENABLE_AIEN = 16;
/*     */   public static final int AMS_COLOR_ENABLE_WEN = 8;
/*     */   public static final int AMS_COLOR_ENABLE_PEN = 4;
/*     */   public static final int AMS_COLOR_ENABLE_AEN = 2;
/*     */   public static final int AMS_COLOR_ENABLE_PON = 1;
/*     */   public static final int AMS_COLOR_ATIME = 1;
/*     */   public static final int AMS_COLOR_WTIME = 3;
/*     */   public static final int AMS_COLOR_WTIME_2_4MS = 255;
/*     */   public static final int AMS_COLOR_WTIME_204MS = 171;
/*     */   public static final int AMS_COLOR_WTIME_614MS = 0;
/*     */   public static final int AMS_COLOR_AILTL = 4;
/*     */   public static final int AMS_COLOR_AILTH = 5;
/*     */   public static final int AMS_COLOR_AIHTL = 6;
/*     */   public static final int AMS_COLOR_AIHTH = 7;
/*     */   public static final int AMS_COLOR_PERS = 12;
/*     */   public static final int AMS_COLOR_PERS_NONE = 0;
/*     */   public static final int AMS_COLOR_PERS_1_CYCLE = 1;
/*     */   public static final int AMS_COLOR_PERS_2_CYCLE = 2;
/*     */   public static final int AMS_COLOR_PERS_3_CYCLE = 3;
/*     */   public static final int AMS_COLOR_PERS_5_CYCLE = 4;
/*     */   public static final int AMS_COLOR_PERS_10_CYCLE = 5;
/*     */   public static final int AMS_COLOR_PERS_15_CYCLE = 6;
/*     */   public static final int AMS_COLOR_PERS_20_CYCLE = 7;
/*     */   public static final int AMS_COLOR_PERS_25_CYCLE = 8;
/*     */   public static final int AMS_COLOR_PERS_30_CYCLE = 9;
/*     */   public static final int AMS_COLOR_PERS_35_CYCLE = 10;
/*     */   public static final int AMS_COLOR_PERS_40_CYCLE = 11;
/*     */   public static final int AMS_COLOR_PERS_45_CYCLE = 12;
/*     */   public static final int AMS_COLOR_PERS_50_CYCLE = 13;
/*     */   public static final int AMS_COLOR_PERS_55_CYCLE = 14;
/*     */   public static final int AMS_COLOR_PERS_60_CYCLE = 15;
/*     */   public static final int AMS_COLOR_CONFIG = 13;
/*     */   public static final int AMS_COLOR_CONFIG_NORMAL = 0;
/*     */   public static final int AMS_COLOR_CONFIG_WLONG = 2;
/*     */   public static final int AMS_COLOR_CONTROL = 15;
/*     */   public static final int AMS_COLOR_GAIN_1 = 0;
/*     */   public static final int AMS_COLOR_GAIN_4 = 1;
/*     */   public static final int AMS_COLOR_GAIN_16 = 2;
/*     */   public static final int AMS_COLOR_GAIN_60 = 3;
/*     */   public static final int AMS_COLOR_ID = 18;
/*     */   public static final int AMS_COLOR_STATUS = 19;
/*     */   public static final int AMS_COLOR_STATUS_AINT = 16;
/*     */   public static final int AMS_COLOR_STATUS_AVALID = 1;
/*     */   public static final int AMS_COLOR_CDATAL = 20;
/*     */   public static final int AMS_COLOR_CDATAH = 21;
/*     */   public static final int AMS_COLOR_RDATAL = 22;
/*     */   public static final int AMS_COLOR_RDATAH = 23;
/*     */   public static final int AMS_COLOR_GDATAL = 24;
/*     */   public static final int AMS_COLOR_GDATAH = 25;
/*     */   public static final int AMS_COLOR_BDATAL = 26;
/*     */   public static final int AMS_COLOR_BDATAH = 27;
/*     */   public static final int IREG_READ_FIRST = 20;
/*     */   public static final int IREG_READ_LAST = 27;
/*     */   
/*     */   public abstract boolean initialize(Parameters paramParameters);
/*     */   
/*     */   public abstract boolean initialize();
/*     */   
/*     */   public abstract Parameters getParameters();
/*     */   
/*     */   public abstract byte getDeviceID();
/*     */   
/*     */   public abstract byte read8(Register paramRegister);
/*     */   
/*     */   public static class Parameters
/*     */   {
/*     */     public final int deviceId;
/*     */     public I2cAddr i2cAddr;
/*  86 */     public AMSColorSensor.IntegrationTime integrationTime = AMSColorSensor.IntegrationTime.MS_24;
/*     */     
/*     */ 
/*  89 */     public AMSColorSensor.Gain gain = AMSColorSensor.Gain.GAIN_4;
/*     */     
/*     */ 
/*  92 */     public I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(20, 8, I2cDeviceSynch.ReadMode.REPEAT);
/*     */     
/*     */ 
/*  95 */     public boolean loggingEnabled = false;
/*     */     
/*     */ 
/*  98 */     public String loggingTag = "AMSColorSensor";
/*     */     
/*     */     public Parameters(I2cAddr i2cAddr, int deviceId)
/*     */     {
/* 102 */       this.i2cAddr = i2cAddr;
/* 103 */       this.deviceId = deviceId;
/*     */     }
/*     */     
/*     */     public static Parameters createForAdaFruit() {
/* 107 */       return new Parameters(I2cAddr.create7bit(41), 68);
/*     */     }
/*     */     
/*     */     public static Parameters createForLynx() {
/* 111 */       return new Parameters(I2cAddr.create7bit(57), 96);
/*     */     }
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
/*     */   public abstract byte[] read(Register paramRegister, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int readUnsignedShort(Register paramRegister);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void write8(Register paramRegister, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void write(Register paramRegister, byte[] paramArrayOfByte);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void writeShort(Register paramRegister, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum Register
/*     */   {
/* 188 */     ENABLE(0), 
/* 189 */     ATIME(1), 
/* 190 */     CONFIGURATION(13), 
/* 191 */     CONTROL(15), 
/* 192 */     DEVICE_ID(18), 
/* 193 */     STATUS(19), 
/* 194 */     CLEAR(20), 
/* 195 */     RED(22), 
/* 196 */     GREEN(24), 
/* 197 */     BLUE(26);
/*     */     
/*     */ 
/* 200 */     private Register(int i) { this.byteVal = ((byte)i); }
/*     */     
/*     */     public final byte byteVal;
/*     */   }
/*     */   
/* 205 */   public static enum Gain { GAIN_1(0), 
/* 206 */     GAIN_4(1), 
/* 207 */     GAIN_16(2), 
/* 208 */     GAIN_64(3);
/*     */     
/*     */ 
/* 211 */     private Gain(int i) { this.byteVal = ((byte)i); }
/*     */     
/*     */     public final byte byteVal;
/*     */   }
/*     */   
/* 216 */   public static enum IntegrationTime { MS_2_4(255), 
/* 217 */     MS_24(246), 
/* 218 */     MS_50(235), 
/* 219 */     MS_101(213), 
/* 220 */     MS_154(192), 
/* 221 */     MS_700(0);
/*     */     
/*     */     private IntegrationTime(int i) {
/* 224 */       this.byteVal = ((byte)i);
/*     */     }
/*     */     
/*     */     public final byte byteVal;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\ams\AMSColorSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */