/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LED
/*    */   implements HardwareDevice
/*    */ {
/* 35 */   private DigitalChannelController controller = null;
/* 36 */   private int physicalPort = -1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LED(DigitalChannelController controller, int physicalPort)
/*    */   {
/* 44 */     this.controller = controller;
/* 45 */     this.physicalPort = physicalPort;
/*    */     
/* 47 */     controller.setDigitalChannelMode(physicalPort, DigitalChannelController.Mode.OUTPUT);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void enable(boolean set)
/*    */   {
/* 55 */     this.controller.setDigitalChannelState(this.physicalPort, set);
/*    */   }
/*    */   
/*    */   public HardwareDevice.Manufacturer getManufacturer() {
/* 59 */     return this.controller.getManufacturer();
/*    */   }
/*    */   
/*    */   public String getDeviceName()
/*    */   {
/* 64 */     return "LED";
/*    */   }
/*    */   
/*    */   public String getConnectionInfo()
/*    */   {
/* 69 */     return String.format("%s; port %d", new Object[] { this.controller.getConnectionInfo(), Integer.valueOf(this.physicalPort) });
/*    */   }
/*    */   
/*    */   public int getVersion()
/*    */   {
/* 74 */     return 0;
/*    */   }
/*    */   
/*    */   public void resetDeviceConfigurationForOpMode() {}
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\LED.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */