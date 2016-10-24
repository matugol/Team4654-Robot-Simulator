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
/*    */ public abstract class RowMajorMatrixF
/*    */   extends DenseMatrixF
/*    */ {
/*    */   public RowMajorMatrixF(int nRows, int nCols)
/*    */   {
/* 44 */     super(nRows, nCols);
/*    */   }
/*    */   
/*    */ 
/*    */   protected int indexFromRowCol(int row, int col)
/*    */   {
/* 50 */     return row * this.numCols + col;
/*    */   }
/*    */   
/*    */   public VectorF toVector()
/*    */   {
/* 55 */     return new VectorF(getData());
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\matrices\RowMajorMatrixF.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */