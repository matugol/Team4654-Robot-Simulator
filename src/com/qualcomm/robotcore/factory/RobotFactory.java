/*    */ package com.qualcomm.robotcore.factory;
/*    */ 
/*    */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*    */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*    */ import com.qualcomm.robotcore.robot.Robot;
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
/*    */ public class RobotFactory
/*    */ {
/*    */   public static Robot createRobot(EventLoopManager eventLoopManager)
/*    */     throws RobotCoreException
/*    */   {
/* 44 */     return new Robot(eventLoopManager);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\factory\RobotFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */