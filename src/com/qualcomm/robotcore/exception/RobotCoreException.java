/*    */ package com.qualcomm.robotcore.exception;
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
/*    */ public class RobotCoreException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -6837027308140695266L;
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
/* 42 */   private Exception chainedException = null;
/*    */   
/*    */   public RobotCoreException(String message) {
/* 45 */     super(message);
/*    */   }
/*    */   
/*    */   public RobotCoreException(String format, Object... args) {
/* 49 */     super(String.format(format, args));
/*    */   }
/*    */   
/*    */   public static RobotCoreException createChained(Exception e, String format, Object... args) {
/* 53 */     RobotCoreException result = new RobotCoreException(format, args);
/* 54 */     result.chainedException = e;
/* 55 */     return result;
/*    */   }
/*    */   
/* 58 */   public boolean isChainedException() { return this.chainedException != null; }
/*    */   
/* 60 */   public Exception getChainedException() { return this.chainedException; }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\exception\RobotCoreException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */