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
/*    */ public class GeneralMatrixF
/*    */   extends RowMajorMatrixF
/*    */ {
/*    */   float[] data;
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
/*    */   public GeneralMatrixF(int numRows, int numCols)
/*    */   {
/* 45 */     super(numRows, numCols);
/* 46 */     this.data = new float[numRows * numCols];
/*    */   }
/*    */   
/*    */   private GeneralMatrixF(int numRows, int numCols, int flag)
/*    */   {
/* 51 */     super(numRows, numCols);
/*    */   }
/*    */   
/*    */   public GeneralMatrixF(int numRows, int numCols, float[] data)
/*    */   {
/* 56 */     super(numRows, numCols);
/* 57 */     if (data.length != numRows * numCols) throw dimensionsError(numRows, numCols);
/* 58 */     this.data = data;
/*    */   }
/*    */   
/*    */   public float[] getData()
/*    */   {
/* 63 */     return this.data;
/*    */   }
/*    */   
/*    */   public MatrixF emptyMatrix(int numRows, int numCols)
/*    */   {
/* 68 */     return new GeneralMatrixF(numRows, numCols);
/*    */   }
/*    */   
/*    */   public GeneralMatrixF transposed()
/*    */   {
/* 73 */     return (GeneralMatrixF)super.transposed();
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\matrices\GeneralMatrixF.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */