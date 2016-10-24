/*     */ package org.firstinspires.ftc.robotcore.internal;
/*     */ 
/*     */ import com.vuforia.Matrix34F;
/*     */ import com.vuforia.Matrix44F;
/*     */ import com.vuforia.Tool;
/*     */ import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
/*     */ import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
/*     */ import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
/*     */ import org.firstinspires.ftc.robotcore.external.matrices.RowMajorMatrixF;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VuforiaPoseMatrix
/*     */   extends RowMajorMatrixF
/*     */ {
/*     */   float[] data;
/*     */   
/*     */   public VuforiaPoseMatrix()
/*     */   {
/*  60 */     super(3, 4);
/*  61 */     this.data = new float[12];
/*     */   }
/*     */   
/*     */   public VuforiaPoseMatrix(Matrix34F matrix)
/*     */   {
/*  66 */     super(3, 4);
/*  67 */     this.data = matrix.getData();
/*     */   }
/*     */   
/*     */   public MatrixF emptyMatrix(int numRows, int numCols)
/*     */   {
/*  72 */     if ((numRows == 3) && (numCols == 4))
/*     */     {
/*  74 */       return new VuforiaPoseMatrix();
/*     */     }
/*     */     
/*     */ 
/*  78 */     return new GeneralMatrixF(numRows, numCols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float[] getData()
/*     */   {
/*  88 */     return this.data;
/*     */   }
/*     */   
/*     */   public Matrix34F getMatrix34F()
/*     */   {
/*  93 */     Matrix34F result = new Matrix34F();
/*  94 */     result.setData(this.data);
/*  95 */     return result;
/*     */   }
/*     */   
/*     */   public OpenGLMatrix toOpenGL()
/*     */   {
/* 100 */     Matrix34F matrix34F = getMatrix34F();
/* 101 */     Matrix44F matrix44F = Tool.convertPose2GLMatrix(matrix34F);
/* 102 */     return new OpenGLMatrix(matrix44F);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\VuforiaPoseMatrix.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */