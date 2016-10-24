/*    */ package com.qualcomm.robotcore.robot;
/*    */ 
/*    */ import android.support.annotation.NonNull;
/*    */ import com.qualcomm.robotcore.eventloop.EventLoop;
/*    */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*    */ import com.qualcomm.robotcore.exception.RobotCoreException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Robot
/*    */ {
/*    */   public final EventLoopManager eventLoopManager;
/*    */   
/*    */   public Robot(EventLoopManager eventLoopManager)
/*    */   {
/* 55 */     this.eventLoopManager = eventLoopManager;
/*    */   }
/*    */   
/*    */   public void start(@NonNull EventLoop eventLoop) throws RobotCoreException {
/* 59 */     this.eventLoopManager.start(eventLoop);
/*    */   }
/*    */   
/*    */   public void shutdown() {
/* 63 */     if (this.eventLoopManager != null) {
/* 64 */       this.eventLoopManager.shutdown();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robot\Robot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */