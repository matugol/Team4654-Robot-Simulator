/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
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
/*     */ public class HiTechnicNxtTouchSensorMultiplexer
/*     */   extends LegacyModulePortDeviceImpl
/*     */   implements TouchSensorMultiplexer
/*     */ {
/*  49 */   int NUM_TOUCH_SENSORS = 4;
/*     */   
/*     */   public static final int MASK_TOUCH_SENSOR_1 = 1;
/*     */   
/*     */   public static final int MASK_TOUCH_SENSOR_2 = 2;
/*     */   
/*     */   public static final int MASK_TOUCH_SENSOR_3 = 4;
/*     */   public static final int MASK_TOUCH_SENSOR_4 = 8;
/*     */   public static final int INVALID = -1;
/*  58 */   public static final int[] MASK_MAP = { -1, 1, 2, 4, 8 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HiTechnicNxtTouchSensorMultiplexer(LegacyModule legacyModule, int physicalPort)
/*     */   {
/*  71 */     super(legacyModule, physicalPort);
/*  72 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void moduleNowArmedOrPretending()
/*     */   {
/*  77 */     this.module.enableAnalogReadMode(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String status()
/*     */   {
/*  85 */     return String.format("NXT Touch Sensor Multiplexer, connected via device %s, port %d", new Object[] { this.module.getSerialNumber().toString(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/*  89 */     return HardwareDevice.Manufacturer.HiTechnic;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/*  94 */     return "NXT Touch Sensor Multiplexer";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/*  99 */     return this.module.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 104 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */   public boolean isTouchSensorPressed(int channel)
/*     */   {
/* 118 */     throwIfChannelInvalid(channel);
/* 119 */     int switches = getAllSwitches();
/*     */     
/* 121 */     int touchSensor = switches & MASK_MAP[channel];
/* 122 */     return touchSensor > 0;
/*     */   }
/*     */   
/*     */   public int getSwitches()
/*     */   {
/* 127 */     return getAllSwitches();
/*     */   }
/*     */   
/*     */   private int getAllSwitches() {
/* 131 */     byte[] analogBuffer = this.module.readAnalogRaw(3);
/* 132 */     int analogValue = TypeConversion.byteArrayToShort(analogBuffer, ByteOrder.LITTLE_ENDIAN);
/*     */     
/* 134 */     int svalue = 1023 - analogValue;
/* 135 */     int switches = 339 * svalue;
/* 136 */     switches /= (1023 - svalue);
/* 137 */     switches += 5;
/* 138 */     switches /= 10;
/*     */     
/* 140 */     return switches;
/*     */   }
/*     */   
/*     */   private void throwIfChannelInvalid(int channel) {
/* 144 */     if ((channel <= 0) || (channel > this.NUM_TOUCH_SENSORS)) {
/* 145 */       throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", new Object[] { Integer.valueOf(channel), Integer.valueOf(this.NUM_TOUCH_SENSORS) }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtTouchSensorMultiplexer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */