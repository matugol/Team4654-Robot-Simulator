/*    */ package org.firstinspires.ftc.robotcore.external.matrices;
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
/*    */ public class RowMatrixF
/*    */   extends MatrixF
/*    */ {
/*    */   VectorF vector;
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
/*    */   public RowMatrixF(VectorF vector)
/*    */   {
/* 44 */     super(1, vector.length());
/* 45 */     this.vector = vector;
/*    */   }
/*    */   
/*    */   public float get(int row, int col)
/*    */   {
/* 50 */     return this.vector.get(col);
/*    */   }
/*    */   
/*    */   public void put(int row, int col, float value)
/*    */   {
/* 55 */     this.vector.put(col, value);
/*    */   }
/*    */   
/*    */   public MatrixF emptyMatrix(int numRows, int numCols)
/*    */   {
/* 60 */     return new GeneralMatrixF(numRows, numCols);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\matrices\RowMatrixF.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */