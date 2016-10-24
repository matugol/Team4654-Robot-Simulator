/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface DcMotorSimple
/*    */   extends HardwareDevice
/*    */ {
/*    */   public abstract void setDirection(Direction paramDirection);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract Direction getDirection();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void setPower(double paramDouble);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract double getPower();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static enum Direction
/*    */   {
/* 51 */     FORWARD,  REVERSE;
/*    */     
/*    */     private Direction() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DcMotorSimple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */