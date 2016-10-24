/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.ARMINGSTATE;
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.Callback;
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
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
/*    */ public abstract class LegacyModulePortDeviceImpl
/*    */   implements RobotArmingStateNotifier.Callback, LegacyModulePortDevice
/*    */ {
/*    */   protected final LegacyModule module;
/*    */   protected final int physicalPort;
/*    */   
/*    */   protected LegacyModulePortDeviceImpl(LegacyModule module, int physicalPort)
/*    */   {
/* 53 */     this.module = module;
/* 54 */     this.physicalPort = physicalPort;
/*    */   }
/*    */   
/*    */   protected void finishConstruction()
/*    */   {
/* 59 */     moduleNowArmedOrPretending();
/*    */     
/* 61 */     if ((this.module instanceof RobotUsbModule)) {
/* 62 */       ((RobotUsbModule)this.module).registerCallback(this);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   protected void moduleNowArmedOrPretending() {}
/*    */   
/*    */ 
/*    */   protected void moduleNowDisarmed() {}
/*    */   
/*    */ 
/*    */   public synchronized void onModuleStateChange(RobotArmingStateNotifier module, RobotArmingStateNotifier.ARMINGSTATE state)
/*    */   {
/* 75 */     switch (state)
/*    */     {
/*    */     case ARMED: 
/*    */     case PRETENDING: 
/* 79 */       moduleNowArmedOrPretending();
/* 80 */       break;
/*    */     case DISARMED: 
/* 82 */       moduleNowDisarmed();
/*    */     }
/*    */     
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LegacyModule getLegacyModule()
/*    */   {
/* 93 */     return this.module;
/*    */   }
/*    */   
/*    */   public int getPort()
/*    */   {
/* 98 */     return this.physicalPort;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\LegacyModulePortDeviceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */