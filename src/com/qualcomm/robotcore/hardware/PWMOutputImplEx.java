/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ 
/*    */ public class PWMOutputImplEx
/*    */   extends PWMOutputImpl
/*    */   implements PWMOutputEx
/*    */ {
/*    */   PWMOutputControllerEx controllerEx;
/*    */   
/*    */   public PWMOutputImplEx(PWMOutputController controller, int port)
/*    */   {
/* 12 */     super(controller, port);
/* 13 */     this.controllerEx = ((PWMOutputControllerEx)controller);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setPwmEnable()
/*    */   {
/* 19 */     this.controllerEx.setPwmEnable(this.port);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setPwmDisable()
/*    */   {
/* 25 */     this.controllerEx.setPwmDisable(this.port);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isPwmEnabled()
/*    */   {
/* 31 */     return this.controllerEx.isPwmEnabled(this.port);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\PWMOutputImplEx.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */