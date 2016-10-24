/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.ARMINGSTATE;
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.Callback;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class I2cControllerPortDeviceImpl
/*    */   implements RobotArmingStateNotifier.Callback, I2cControllerPortDevice
/*    */ {
/*    */   protected final I2cController controller;
/*    */   protected final int physicalPort;
/*    */   
/*    */   protected I2cControllerPortDeviceImpl(I2cController controller, int physicalPort)
/*    */   {
/* 52 */     this.controller = controller;
/* 53 */     this.physicalPort = physicalPort;
/*    */   }
/*    */   
/*    */   protected void finishConstruction()
/*    */   {
/* 58 */     controllerNowArmedOrPretending();
/*    */     
/* 60 */     if ((this.controller instanceof RobotArmingStateNotifier)) {
/* 61 */       ((RobotArmingStateNotifier)this.controller).registerCallback(this);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   protected void controllerNowArmedOrPretending() {}
/*    */   
/*    */ 
/*    */   protected void controllerNowDisarmed() {}
/*    */   
/*    */   public synchronized void onModuleStateChange(RobotArmingStateNotifier module, RobotArmingStateNotifier.ARMINGSTATE state)
/*    */   {
/* 73 */     switch (state)
/*    */     {
/*    */     case ARMED: 
/*    */     case PRETENDING: 
/* 77 */       controllerNowArmedOrPretending();
/* 78 */       break;
/*    */     case DISARMED: 
/* 80 */       controllerNowDisarmed();
/*    */     }
/*    */     
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public I2cController getI2cController()
/*    */   {
/* 91 */     return this.controller;
/*    */   }
/*    */   
/*    */   public int getPort()
/*    */   {
/* 96 */     return this.physicalPort;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cControllerPortDeviceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */