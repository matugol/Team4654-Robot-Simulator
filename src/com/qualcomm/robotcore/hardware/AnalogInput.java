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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnalogInput
/*    */   implements HardwareDevice
/*    */ {
/* 38 */   private AnalogInputController controller = null;
/* 39 */   private int channel = -1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AnalogInput(AnalogInputController controller, int channel)
/*    */   {
/* 48 */     this.controller = controller;
/* 49 */     this.channel = channel;
/*    */   }
/*    */   
/*    */   public HardwareDevice.Manufacturer getManufacturer() {
/* 53 */     return this.controller.getManufacturer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public double getVoltage()
/*    */   {
/* 61 */     return this.controller.getAnalogInputVoltage(this.channel);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public double getMaxVoltage()
/*    */   {
/* 70 */     return this.controller.getMaxAnalogInputVoltage();
/*    */   }
/*    */   
/*    */   public String getDeviceName()
/*    */   {
/* 75 */     return "Analog Input";
/*    */   }
/*    */   
/*    */   public String getConnectionInfo()
/*    */   {
/* 80 */     return this.controller.getConnectionInfo() + "; analog port " + this.channel;
/*    */   }
/*    */   
/*    */   public int getVersion()
/*    */   {
/* 85 */     return 1;
/*    */   }
/*    */   
/*    */   public void resetDeviceConfigurationForOpMode() {}
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\AnalogInput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */