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
/*    */ public class PWMOutputImpl
/*    */   implements PWMOutput
/*    */ {
/* 38 */   protected PWMOutputController controller = null;
/* 39 */   protected int port = -1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PWMOutputImpl(PWMOutputController controller, int port)
/*    */   {
/* 48 */     this.controller = controller;
/* 49 */     this.port = port;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPulseWidthOutputTime(int time)
/*    */   {
/* 58 */     this.controller.setPulseWidthOutputTime(this.port, time);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getPulseWidthOutputTime()
/*    */   {
/* 65 */     return this.controller.getPulseWidthOutputTime(this.port);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPulseWidthPeriod(int period)
/*    */   {
/* 73 */     this.controller.setPulseWidthPeriod(this.port, period);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getPulseWidthPeriod()
/*    */   {
/* 80 */     return this.controller.getPulseWidthPeriod(this.port);
/*    */   }
/*    */   
/*    */   public HardwareDevice.Manufacturer getManufacturer() {
/* 84 */     return this.controller.getManufacturer();
/*    */   }
/*    */   
/*    */   public String getDeviceName()
/*    */   {
/* 89 */     return "PWM Output";
/*    */   }
/*    */   
/*    */   public String getConnectionInfo()
/*    */   {
/* 94 */     return this.controller.getConnectionInfo() + "; port " + this.port;
/*    */   }
/*    */   
/*    */   public int getVersion()
/*    */   {
/* 99 */     return 1;
/*    */   }
/*    */   
/*    */   public void resetDeviceConfigurationForOpMode() {}
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\PWMOutputImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */