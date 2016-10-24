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
/*     */ public class DigitalChannel
/*     */   implements HardwareDevice
/*     */ {
/*  38 */   private DigitalChannelController controller = null;
/*  39 */   private int channel = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DigitalChannel(DigitalChannelController controller, int channel)
/*     */   {
/*  48 */     this.controller = controller;
/*  49 */     this.channel = channel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DigitalChannelController.Mode getMode()
/*     */   {
/*  58 */     return this.controller.getDigitalChannelMode(this.channel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMode(DigitalChannelController.Mode mode)
/*     */   {
/*  67 */     this.controller.setDigitalChannelMode(this.channel, mode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getState()
/*     */   {
/*  76 */     return this.controller.getDigitalChannelState(this.channel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setState(boolean state)
/*     */   {
/*  87 */     this.controller.setDigitalChannelState(this.channel, state);
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer() {
/*  91 */     return this.controller.getManufacturer();
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/*  96 */     return "Digital Channel";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 101 */     return this.controller.getConnectionInfo() + "; digital port " + this.channel;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/* 106 */     return 1;
/*     */   }
/*     */   
/*     */   public void resetDeviceConfigurationForOpMode() {}
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DigitalChannel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */