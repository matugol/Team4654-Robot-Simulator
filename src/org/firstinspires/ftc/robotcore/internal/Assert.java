/*    */ package org.firstinspires.ftc.robotcore.internal;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.RobotLog;
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
/*    */ public class Assert
/*    */ {
/*    */   public static void assertTrue(boolean value)
/*    */   {
/* 48 */     if (!value)
/*    */     {
/* 50 */       assertFailed();
/*    */     }
/*    */   }
/*    */   
/*    */   public static void assertFalse(boolean value)
/*    */   {
/* 56 */     if (value)
/*    */     {
/* 58 */       assertFailed();
/*    */     }
/*    */   }
/*    */   
/*    */   public static void assertNull(Object value)
/*    */   {
/* 64 */     if (value != null)
/*    */     {
/* 66 */       assertFailed();
/*    */     }
/*    */   }
/*    */   
/*    */   public static void assertNotNull(Object value)
/*    */   {
/* 72 */     if (value == null)
/*    */     {
/* 74 */       assertFailed();
/*    */     }
/*    */   }
/*    */   
/*    */   public static void assertFailed()
/*    */   {
/*    */     try {
/* 81 */       throw new RuntimeException("assertion failed");
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 85 */       RobotLog.a("assertion failed");
/* 86 */       RobotLog.logStacktrace(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\Assert.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */