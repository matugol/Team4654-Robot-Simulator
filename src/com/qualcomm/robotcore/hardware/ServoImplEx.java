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
/*    */ public class ServoImplEx
/*    */   extends ServoImpl
/*    */   implements ServoEx
/*    */ {
/*    */   protected ServoControllerEx controllerEx;
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
/*    */   public ServoImplEx(ServoController controller, int portNumber)
/*    */   {
/* 52 */     this(controller, portNumber, Servo.Direction.FORWARD);
/*    */   }
/*    */   
/*    */   public ServoImplEx(ServoController controller, int portNumber, Servo.Direction direction)
/*    */   {
/* 57 */     super(controller, portNumber, direction);
/* 58 */     this.controllerEx = ((ServoControllerEx)controller);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPwmRange(ServoEx.ServoPwmRange range)
/*    */   {
/* 68 */     this.controllerEx.setServoPwmRange(getPortNumber(), range);
/*    */   }
/*    */   
/*    */ 
/*    */   public ServoEx.ServoPwmRange getPwmRange()
/*    */   {
/* 74 */     return this.controllerEx.getServoPwmRange(getPortNumber());
/*    */   }
/*    */   
/*    */ 
/*    */   public void setPwmEnable()
/*    */   {
/* 80 */     this.controllerEx.setServoPwmEnable(getPortNumber());
/*    */   }
/*    */   
/*    */ 
/*    */   public void setPwmDisable()
/*    */   {
/* 86 */     this.controllerEx.setServoPwmDisable(getPortNumber());
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isPwmEnabled()
/*    */   {
/* 92 */     return this.controllerEx.isSwervoPwmEnabled(getPortNumber());
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\ServoImplEx.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */