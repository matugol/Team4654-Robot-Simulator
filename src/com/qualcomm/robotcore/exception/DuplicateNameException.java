/*    */ package com.qualcomm.robotcore.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DuplicateNameException
/*    */   extends RuntimeException
/*    */ {
/*    */   public DuplicateNameException(String message)
/*    */   {
/* 10 */     super(message);
/*    */   }
/*    */   
/*    */   public DuplicateNameException(String format, Object... args)
/*    */   {
/* 15 */     super(String.format(format, args));
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\exception\DuplicateNameException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */