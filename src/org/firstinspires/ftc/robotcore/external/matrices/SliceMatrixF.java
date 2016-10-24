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
/*    */ public class SliceMatrixF
/*    */   extends MatrixF
/*    */ {
/*    */   protected MatrixF matrix;
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
/*    */   protected int row;
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
/*    */   protected int col;
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
/*    */   public SliceMatrixF(MatrixF matrix, int row, int col, int numRows, int numCols)
/*    */   {
/* 63 */     super(numRows, numCols);
/* 64 */     this.matrix = matrix;
/* 65 */     this.row = row;
/* 66 */     this.col = col;
/*    */     
/* 68 */     if (row + numRows >= matrix.numRows) throw dimensionsError();
/* 69 */     if (col + numCols >= matrix.numCols) { throw dimensionsError();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public float get(int row, int col)
/*    */   {
/* 78 */     return this.matrix.get(this.row + row, this.col + col);
/*    */   }
/*    */   
/*    */   public void put(int row, int col, float value)
/*    */   {
/* 83 */     this.matrix.put(this.row + row, this.col + col, value);
/*    */   }
/*    */   
/*    */   public MatrixF emptyMatrix(int numRows, int numCols)
/*    */   {
/* 88 */     return this.matrix.emptyMatrix(numRows, numCols);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\matrices\SliceMatrixF.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */