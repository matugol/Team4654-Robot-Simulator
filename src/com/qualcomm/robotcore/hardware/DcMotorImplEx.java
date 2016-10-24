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
/*    */ public class DcMotorImplEx
/*    */   extends DcMotorImpl
/*    */   implements DcMotorEx
/*    */ {
/*    */   DcMotorControllerEx controllerEx;
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
/*    */   public DcMotorImplEx(DcMotorController controller, int portNumber)
/*    */   {
/* 52 */     this(controller, portNumber, DcMotorSimple.Direction.FORWARD);
/*    */   }
/*    */   
/*    */   public DcMotorImplEx(DcMotorController controller, int portNumber, DcMotorSimple.Direction direction)
/*    */   {
/* 57 */     super(controller, portNumber, direction);
/* 58 */     this.controllerEx = ((DcMotorControllerEx)controller);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setMotorEnable()
/*    */   {
/* 68 */     this.controllerEx.setMotorEnable(getPortNumber());
/*    */   }
/*    */   
/*    */ 
/*    */   public void setMotorDisable()
/*    */   {
/* 74 */     this.controllerEx.setMotorDisable(getPortNumber());
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isMotorEnabled()
/*    */   {
/* 80 */     return this.controllerEx.isMotorEnabled(getPortNumber());
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DcMotorImplEx.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */