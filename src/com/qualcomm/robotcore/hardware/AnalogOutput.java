/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnalogOutput
/*     */   implements HardwareDevice
/*     */ {
/*  38 */   private AnalogOutputController controller = null;
/*  39 */   private int channel = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnalogOutput(AnalogOutputController controller, int channel)
/*     */   {
/*  48 */     this.controller = controller;
/*  49 */     this.channel = channel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAnalogOutputVoltage(int voltage)
/*     */   {
/*  59 */     this.controller.setAnalogOutputVoltage(this.channel, voltage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAnalogOutputFrequency(int freq)
/*     */   {
/*  68 */     this.controller.setAnalogOutputFrequency(this.channel, freq);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAnalogOutputMode(byte mode)
/*     */   {
/*  80 */     this.controller.setAnalogOutputMode(this.channel, mode);
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/*  84 */     return this.controller.getManufacturer();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/*  90 */     return "Analog Output";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/*  95 */     return this.controller.getConnectionInfo() + "; analog port " + this.channel;
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


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\AnalogOutput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */