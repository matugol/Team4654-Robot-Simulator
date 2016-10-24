/*     */ package org.firstinspires.ftc.robotcore.external.matrices;
/*     */ 
/*     */ import android.opengl.Matrix;
/*     */ import com.vuforia.Matrix44F;
/*     */ import org.firstinspires.ftc.robotcore.external.Const;
/*     */ import org.firstinspires.ftc.robotcore.external.NonConst;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpenGLMatrix
/*     */   extends ColumnMajorMatrixF
/*     */ {
/*     */   float[] data;
/*     */   
/*     */   public OpenGLMatrix()
/*     */   {
/*  70 */     super(4, 4);
/*  71 */     this.data = new float[16];
/*  72 */     Matrix.setIdentityM(this.data, 0);
/*     */   }
/*     */   
/*     */   public OpenGLMatrix(float[] data)
/*     */   {
/*  77 */     super(4, 4);
/*  78 */     this.data = data;
/*  79 */     if (this.data.length != 16) throw dimensionsError();
/*     */   }
/*     */   
/*     */   public OpenGLMatrix(Matrix44F matrix)
/*     */   {
/*  84 */     this(matrix.getData());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpenGLMatrix(MatrixF him)
/*     */   {
/*  94 */     this();
/*  95 */     if ((him.numRows > 4) || (him.numCols > 4)) throw him.dimensionsError();
/*  96 */     for (int i = 0; i < Math.min(4, him.numRows); i++)
/*     */     {
/*  98 */       for (int j = 0; j < Math.min(4, him.numCols); j++)
/*     */       {
/* 100 */         put(i, j, him.get(i, j));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public MatrixF emptyMatrix(int numRows, int numCols)
/*     */   {
/* 107 */     if ((numRows == 4) && (numCols == 4)) {
/* 108 */       return new OpenGLMatrix();
/*     */     }
/* 110 */     return new GeneralMatrixF(numRows, numCols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OpenGLMatrix rotation(AngleUnit angleUnit, float angle, float dx, float dy, float dz)
/*     */   {
/* 118 */     float[] data = new float[16];
/* 119 */     Matrix.setRotateM(data, 0, angleUnit.toDegrees(angle), dx, dy, dz);
/* 120 */     return new OpenGLMatrix(data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OpenGLMatrix rotation(AxesReference axesReference, AxesOrder axesOrder, AngleUnit angleUnit, float first, float second, float third)
/*     */   {
/* 129 */     OpenGLMatrix rotation = Orientation.getRotationMatrix(axesReference, axesOrder, angleUnit, first, second, third);
/* 130 */     return identityMatrix().multiplied(rotation);
/*     */   }
/*     */   
/*     */   public static OpenGLMatrix translation(float dx, float dy, float dz) {
/* 134 */     OpenGLMatrix result = new OpenGLMatrix();
/* 135 */     result.translate(dx, dy, dz);
/* 136 */     return result;
/*     */   }
/*     */   
/*     */   public static OpenGLMatrix identityMatrix() {
/* 140 */     return new OpenGLMatrix();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float[] getData()
/*     */   {
/* 149 */     return this.data;
/*     */   }
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
/*     */   @NonConst
/*     */   public void scale(float scaleX, float scaleY, float scaleZ)
/*     */   {
/* 164 */     Matrix.scaleM(this.data, 0, scaleX, scaleY, scaleZ);
/*     */   }
/*     */   
/*     */   @NonConst
/* 168 */   public void scale(float scale) { scale(scale, scale, scale); }
/*     */   
/*     */   @NonConst
/*     */   public void translate(float dx, float dy, float dz) {
/* 172 */     Matrix.translateM(this.data, 0, dx, dy, dz);
/*     */   }
/*     */   
/*     */   @NonConst
/* 176 */   public void rotate(AngleUnit angleUnit, float angle, float dx, float dy, float dz) { Matrix.rotateM(this.data, 0, angleUnit.toDegrees(angle), dx, dy, dz); }
/*     */   
/*     */   @NonConst
/*     */   public void rotate(AxesReference axesReference, AxesOrder axesOrder, AngleUnit angleUnit, float first, float second, float third) {
/* 180 */     OpenGLMatrix rotation = Orientation.getRotationMatrix(axesReference, axesOrder, angleUnit, first, second, third);
/* 181 */     this.data = multiplied(rotation).getData();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public OpenGLMatrix scaled(float scaleX, float scaleY, float scaleZ)
/*     */   {
/* 190 */     OpenGLMatrix result = new OpenGLMatrix();
/* 191 */     Matrix.scaleM(result.data, 0, this.data, 0, scaleX, scaleY, scaleZ);
/* 192 */     return result;
/*     */   }
/*     */   
/*     */   @Const
/* 196 */   public OpenGLMatrix scaled(float scale) { return scaled(scale, scale, scale); }
/*     */   
/*     */   @Const
/*     */   public OpenGLMatrix translated(float dx, float dy, float dz) {
/* 200 */     OpenGLMatrix result = new OpenGLMatrix();
/* 201 */     Matrix.translateM(result.data, 0, this.data, 0, dx, dy, dz);
/* 202 */     return result;
/*     */   }
/*     */   
/*     */   @Const
/* 206 */   public OpenGLMatrix rotated(AngleUnit angleUnit, float angle, float dx, float dy, float dz) { OpenGLMatrix result = new OpenGLMatrix();
/* 207 */     Matrix.rotateM(result.data, 0, this.data, 0, angleUnit.toDegrees(angle), dx, dy, dz);
/* 208 */     return result;
/*     */   }
/*     */   
/*     */   @Const
/* 212 */   public OpenGLMatrix rotated(AxesReference axesReference, AxesOrder axesOrder, AngleUnit angleUnit, float first, float second, float third) { OpenGLMatrix rotation = Orientation.getRotationMatrix(axesReference, axesOrder, angleUnit, first, second, third);
/* 213 */     return multiplied(rotation);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public OpenGLMatrix inverted()
/*     */   {
/* 222 */     OpenGLMatrix result = new OpenGLMatrix();
/* 223 */     Matrix.invertM(result.data, 0, this.data, 0);
/* 224 */     return result;
/*     */   }
/*     */   
/*     */   @Const
/*     */   public OpenGLMatrix transposed() {
/* 229 */     return (OpenGLMatrix)super.transposed();
/*     */   }
/*     */   
/*     */   @Const
/*     */   public OpenGLMatrix multiplied(OpenGLMatrix him) {
/* 234 */     OpenGLMatrix result = new OpenGLMatrix();
/* 235 */     Matrix.multiplyMM(result.data, 0, this.data, 0, him.getData(), 0);
/* 236 */     return result;
/*     */   }
/*     */   
/*     */   @Const
/*     */   public MatrixF multiplied(MatrixF him) {
/* 241 */     if ((him instanceof OpenGLMatrix))
/*     */     {
/* 243 */       return multiplied((OpenGLMatrix)him);
/*     */     }
/*     */     
/* 246 */     return super.multiplied(him);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonConst
/*     */   public void multiply(OpenGLMatrix him)
/*     */   {
/* 255 */     this.data = multiplied(him).getData();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonConst
/*     */   public void multiply(MatrixF him)
/*     */   {
/* 264 */     if ((him instanceof OpenGLMatrix))
/*     */     {
/* 266 */       multiply((OpenGLMatrix)him);
/*     */     }
/*     */     else {
/* 269 */       super.multiply(him);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\matrices\OpenGLMatrix.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */