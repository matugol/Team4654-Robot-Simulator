/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface Servo
/*    */   extends HardwareDevice
/*    */ {
/*    */   public static final double MIN_POSITION = 0.0D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final double MAX_POSITION = 1.0D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract ServoController getController();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract int getPortNumber();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void setDirection(Direction paramDirection);
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
/*    */   public abstract void setPosition(double paramDouble);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract double getPosition();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void scaleRange(double paramDouble1, double paramDouble2);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static enum Direction
/*    */   {
/* 62 */     FORWARD,  REVERSE;
/*    */     
/*    */     private Direction() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\Servo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */