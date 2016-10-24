/*    */ package com.qualcomm.robotcore.util;
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
/*    */ public class DifferentialControlLoopCoefficients
/*    */ {
/* 41 */   public double p = 0.0D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 46 */   public double i = 0.0D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 51 */   public double d = 0.0D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DifferentialControlLoopCoefficients() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DifferentialControlLoopCoefficients(double p, double i, double d)
/*    */   {
/* 65 */     this.p = p;
/* 66 */     this.i = i;
/* 67 */     this.d = d;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\DifferentialControlLoopCoefficients.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */