/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.TouchSensor;
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
/*     */ public class HiTechnicNxtTouchSensor
/*     */   extends LegacyModulePortDeviceImpl
/*     */   implements TouchSensor
/*     */ {
/*     */   public HiTechnicNxtTouchSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/*  50 */     super(legacyModule, physicalPort);
/*  51 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void moduleNowArmedOrPretending()
/*     */   {
/*  56 */     this.module.enableAnalogReadMode(this.physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  65 */     return String.format("Touch Sensor: %1.2f", new Object[] { Double.valueOf(getValue()) });
/*     */   }
/*     */   
/*     */   public String status() {
/*  69 */     return String.format("NXT Touch Sensor, connected via device %s, port %d", new Object[] { this.module.getSerialNumber().toString(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */   public double getValue()
/*     */   {
/*  74 */     double val = TypeConversion.byteArrayToShort(this.module.readAnalogRaw(this.physicalPort), ByteOrder.LITTLE_ENDIAN);
/*  75 */     val = val > 675.0D ? 0.0D : 1.0D;
/*  76 */     return val;
/*     */   }
/*     */   
/*     */   public boolean isPressed()
/*     */   {
/*  81 */     return getValue() > 0.0D;
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/*  85 */     return HardwareDevice.Manufacturer.Lego;
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/*  90 */     return "NXT Touch Sensor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/*  95 */     return this.module.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 100 */     return 1;
/*     */   }
/*     */   
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtTouchSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */