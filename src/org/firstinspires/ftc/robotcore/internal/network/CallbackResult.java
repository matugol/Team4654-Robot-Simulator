/*    */ package org.firstinspires.ftc.robotcore.internal.network;
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
/*    */ public enum CallbackResult
/*    */ {
/* 42 */   NOT_HANDLED,  HANDLED,  HANDLED_CONTINUE;
/*    */   
/*    */   private CallbackResult() {}
/*    */   
/* 46 */   public boolean isHandled() { return this != NOT_HANDLED; }
/*    */   
/*    */   public boolean stopDispatch()
/*    */   {
/* 50 */     return this == HANDLED;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\network\CallbackResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */