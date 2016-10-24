/*     */ package org.firstinspires.ftc.robotcore.external.matrices;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MatrixF
/*     */ {
/*     */   protected int numRows;
/*     */   protected int numCols;
/*     */   
/*     */   public MatrixF(int numRows, int numCols)
/*     */   {
/*  76 */     this.numRows = numRows;
/*  77 */     this.numCols = numCols;
/*  78 */     if ((numRows <= 0) || (numCols <= 0)) { throw dimensionsError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public SliceMatrixF slice(int row, int col, int numRows, int numCols)
/*     */   {
/*  92 */     return new SliceMatrixF(this, row, col, numRows, numCols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public SliceMatrixF slice(int numRows, int numCols)
/*     */   {
/* 104 */     return slice(0, 0, numRows, numCols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MatrixF identityMatrix(int dim)
/*     */   {
/* 115 */     return diagonalMatrix(dim, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MatrixF diagonalMatrix(int dim, float scale)
/*     */   {
/* 127 */     GeneralMatrixF result = new GeneralMatrixF(dim, dim);
/* 128 */     for (int i = 0; i < dim; i++)
/*     */     {
/* 130 */       result.put(i, i, scale);
/*     */     }
/* 132 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MatrixF diagonalMatrix(VectorF vector)
/*     */   {
/* 143 */     int dim = vector.length();
/* 144 */     GeneralMatrixF result = new GeneralMatrixF(dim, dim);
/* 145 */     for (int i = 0; i < dim; i++)
/*     */     {
/* 147 */       result.put(i, i, vector.get(i));
/*     */     }
/* 149 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public abstract MatrixF emptyMatrix(int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public int numRows()
/*     */   {
/* 169 */     return this.numRows;
/*     */   }
/*     */   
/*     */   @Const
/*     */   public int numCols()
/*     */   {
/* 175 */     return this.numCols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public abstract float get(int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonConst
/*     */   public abstract void put(int paramInt1, int paramInt2, float paramFloat);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public VectorF getRow(int row)
/*     */   {
/* 201 */     VectorF result = VectorF.length(this.numCols);
/* 202 */     for (int j = 0; j < this.numCols; j++)
/*     */     {
/* 204 */       result.put(j, get(row, j));
/*     */     }
/* 206 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public VectorF getColumn(int col)
/*     */   {
/* 216 */     VectorF result = VectorF.length(this.numRows);
/* 217 */     for (int i = 0; i < this.numRows; i++)
/*     */     {
/* 219 */       result.put(i, get(i, col));
/*     */     }
/* 221 */     return result;
/*     */   }
/*     */   
/*     */   @Const
/*     */   public String toString() {
/* 226 */     StringBuilder result = new StringBuilder();
/* 227 */     result.append("{");
/* 228 */     for (int i = 0; i < this.numRows; i++)
/*     */     {
/* 230 */       if (i > 0) result.append(",");
/* 231 */       result.append("{");
/* 232 */       for (int j = 0; j < this.numCols; j++)
/*     */       {
/* 234 */         if (j > 0) result.append(",");
/* 235 */         result.append(String.format("%.3f", new Object[] { Float.valueOf(get(i, j)) }));
/*     */       }
/* 237 */       result.append("}");
/*     */     }
/* 239 */     result.append("}");
/* 240 */     return result.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public VectorF transform(VectorF him)
/*     */   {
/* 260 */     him = adaptHomogeneous(him);
/* 261 */     return multiplied(him).normalized3D();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   protected VectorF adaptHomogeneous(VectorF him)
/*     */   {
/* 272 */     if (this.numCols == 4)
/*     */     {
/* 274 */       if (him.length() == 3)
/*     */       {
/* 276 */         float[] newData = Arrays.copyOf(him.getData(), 4);
/* 277 */         newData[3] = 1.0F;
/* 278 */         return new VectorF(newData);
/*     */       }
/*     */     }
/* 281 */     else if (this.numCols == 3)
/*     */     {
/* 283 */       if (him.length() == 4)
/*     */       {
/* 285 */         return new VectorF(Arrays.copyOf(him.normalized3D().getData(), 3));
/*     */       }
/*     */     }
/* 288 */     return him;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String formatAsTransform()
/*     */   {
/* 308 */     return formatAsTransform(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String formatAsTransform(AxesReference axesReference, AxesOrder axesOrder, AngleUnit unit)
/*     */   {
/* 330 */     VectorF translation = getTranslation();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 337 */     Orientation orientation = Orientation.getOrientation(this, axesReference, axesOrder, unit);
/*     */     
/* 339 */     return String.format("%s %s", new Object[] { orientation.toString(), translation.toString() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public MatrixF transposed()
/*     */   {
/* 352 */     MatrixF result = emptyMatrix(this.numCols, this.numRows);
/* 353 */     for (int i = 0; i < result.numRows; i++)
/*     */     {
/* 355 */       for (int j = 0; j < result.numCols; j++)
/*     */       {
/* 357 */         result.put(i, j, get(j, i));
/*     */       }
/*     */     }
/* 360 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonConst
/*     */   public void multiply(MatrixF him)
/*     */   {
/* 373 */     if (this.numCols == him.numRows)
/*     */     {
/* 375 */       if (him.numRows == him.numCols)
/*     */       {
/* 377 */         MatrixF temp = multiplied(him);
/*     */         
/*     */ 
/* 380 */         for (int i = 0; i < this.numRows; i++)
/*     */         {
/* 382 */           for (int j = 0; j < this.numCols; j++)
/*     */           {
/* 384 */             put(i, j, temp.get(i, j));
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 389 */         throw dimensionsError();
/*     */       }
/*     */     } else {
/* 392 */       throw dimensionsError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public MatrixF multiplied(MatrixF him)
/*     */   {
/* 402 */     if (this.numCols == him.numRows)
/*     */     {
/* 404 */       MatrixF result = emptyMatrix(this.numRows, him.numCols);
/* 405 */       for (int i = 0; i < result.numRows; i++)
/*     */       {
/* 407 */         for (int j = 0; j < result.numCols; j++)
/*     */         {
/* 409 */           float sum = 0.0F;
/* 410 */           for (int k = 0; k < this.numCols; k++)
/*     */           {
/* 412 */             sum += get(i, k) * him.get(k, j);
/*     */           }
/* 414 */           result.put(i, j, sum);
/*     */         }
/*     */       }
/* 417 */       return result;
/*     */     }
/*     */     
/* 420 */     throw dimensionsError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public MatrixF multiplied(float scale)
/*     */   {
/* 431 */     MatrixF result = emptyMatrix(this.numCols, this.numRows);
/* 432 */     for (int i = 0; i < result.numRows; i++)
/*     */     {
/* 434 */       for (int j = 0; j < result.numCols; j++)
/*     */       {
/* 436 */         result.put(i, j, get(i, j) * scale);
/*     */       }
/*     */     }
/* 439 */     return result;
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void multiply(float scale) {
/* 444 */     for (int i = 0; i < this.numRows; i++)
/*     */     {
/* 446 */       for (int j = 0; j < this.numCols; j++)
/*     */       {
/* 448 */         put(i, j, get(i, j) * scale);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public VectorF multiplied(VectorF him)
/*     */   {
/* 460 */     return multiplied(new ColumnMatrixF(him)).toVector();
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void multiply(VectorF him) {
/* 465 */     VectorF result = multiplied(new ColumnMatrixF(him)).toVector();
/* 466 */     for (int i = 0; i < result.length(); i++)
/*     */     {
/* 468 */       put(i, 0, result.get(i));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public VectorF multiplied(float[] him)
/*     */   {
/* 479 */     return multiplied(new VectorF(him));
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void multiply(float[] him) {
/* 484 */     VectorF result = multiplied(new VectorF(him));
/* 485 */     for (int i = 0; i < result.length(); i++)
/*     */     {
/* 487 */       put(i, 0, result.get(i));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public VectorF toVector()
/*     */   {
/* 498 */     if (this.numCols == 1)
/*     */     {
/* 500 */       VectorF result = VectorF.length(this.numRows);
/* 501 */       for (int i = 0; i < this.numRows; i++)
/*     */       {
/* 503 */         result.put(i, get(i, 0));
/*     */       }
/* 505 */       return result;
/*     */     }
/* 507 */     if (this.numRows == 1)
/*     */     {
/* 509 */       VectorF result = VectorF.length(this.numCols);
/* 510 */       for (int j = 0; j < this.numCols; j++)
/*     */       {
/* 512 */         result.put(j, get(0, j));
/*     */       }
/* 514 */       return result;
/*     */     }
/*     */     
/* 517 */     throw dimensionsError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public MatrixF added(MatrixF addend)
/*     */   {
/* 528 */     if ((this.numRows == addend.numRows) && (this.numCols == addend.numCols))
/*     */     {
/* 530 */       MatrixF result = emptyMatrix(this.numRows, this.numCols);
/* 531 */       for (int i = 0; i < result.numRows; i++)
/*     */       {
/* 533 */         for (int j = 0; j < result.numCols; j++)
/*     */         {
/* 535 */           result.put(i, j, get(i, j) + addend.get(i, j));
/*     */         }
/*     */       }
/* 538 */       return result;
/*     */     }
/*     */     
/* 541 */     throw dimensionsError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonConst
/*     */   public void add(MatrixF addend)
/*     */   {
/* 550 */     if ((this.numRows == addend.numRows) && (this.numCols == addend.numCols))
/*     */     {
/* 552 */       for (int i = 0; i < this.numRows; i++)
/*     */       {
/* 554 */         for (int j = 0; j < this.numCols; j++)
/*     */         {
/* 556 */           put(i, j, get(i, j) + addend.get(i, j));
/*     */         }
/*     */         
/*     */       }
/*     */     } else {
/* 561 */       throw dimensionsError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public MatrixF subtracted(MatrixF subtrahend)
/*     */   {
/* 572 */     if ((this.numRows == subtrahend.numRows) && (this.numCols == subtrahend.numCols))
/*     */     {
/* 574 */       MatrixF result = emptyMatrix(this.numRows, this.numCols);
/* 575 */       for (int i = 0; i < result.numRows; i++)
/*     */       {
/* 577 */         for (int j = 0; j < result.numCols; j++)
/*     */         {
/* 579 */           result.put(i, j, get(i, j) - subtrahend.get(i, j));
/*     */         }
/*     */       }
/* 582 */       return result;
/*     */     }
/*     */     
/* 585 */     throw dimensionsError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonConst
/*     */   public void subtract(MatrixF subtrahend)
/*     */   {
/* 594 */     if ((this.numRows == subtrahend.numRows) && (this.numCols == subtrahend.numCols))
/*     */     {
/* 596 */       for (int i = 0; i < this.numRows; i++)
/*     */       {
/* 598 */         for (int j = 0; j < this.numCols; j++)
/*     */         {
/* 600 */           put(i, j, get(i, j) - subtrahend.get(i, j));
/*     */         }
/*     */         
/*     */       }
/*     */     } else {
/* 605 */       throw dimensionsError();
/*     */     }
/*     */   }
/*     */   
/*     */   @Const
/*     */   public MatrixF added(VectorF him) {
/* 611 */     return added(new ColumnMatrixF(him));
/*     */   }
/*     */   
/*     */   @Const
/*     */   public MatrixF added(float[] him) {
/* 616 */     return added(new VectorF(him));
/*     */   }
/*     */   
/*     */   @Const
/*     */   public MatrixF subtracted(VectorF him) {
/* 621 */     return subtracted(new ColumnMatrixF(him));
/*     */   }
/*     */   
/*     */   @Const
/*     */   public MatrixF subtracted(float[] him) {
/* 626 */     return subtracted(new VectorF(him));
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void add(VectorF him)
/*     */   {
/* 632 */     add(new ColumnMatrixF(him));
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void add(float[] him) {
/* 637 */     add(new VectorF(him));
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void subtract(VectorF him) {
/* 642 */     subtract(new ColumnMatrixF(him));
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void subtract(float[] him) {
/* 647 */     subtract(new VectorF(him));
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
/*     */   @Const
/*     */   public VectorF getTranslation()
/*     */   {
/* 661 */     return getColumn(3).normalized3D();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RuntimeException dimensionsError()
/*     */   {
/* 670 */     return dimensionsError(this.numRows, this.numCols);
/*     */   }
/*     */   
/*     */   @SuppressLint({"DefaultLocale"})
/*     */   protected static RuntimeException dimensionsError(int numRows, int numCols)
/*     */   {
/* 676 */     return new IllegalArgumentException(String.format("matrix dimensions are incorrect: rows=%d cols=%d", new Object[] { Integer.valueOf(numRows), Integer.valueOf(numCols) }));
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
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public MatrixF inverted()
/*     */   {
/* 693 */     if (this.numRows != this.numCols) { throw dimensionsError();
/*     */     }
/* 695 */     if (this.numRows == 4)
/*     */     {
/* 697 */       MatrixF result = emptyMatrix(4, 4);
/*     */       
/* 699 */       float m00 = get(0, 0);float m01 = get(0, 1);float m02 = get(0, 2);float m03 = get(0, 3);
/* 700 */       float m10 = get(1, 0);float m11 = get(1, 1);float m12 = get(1, 2);float m13 = get(1, 3);
/* 701 */       float m20 = get(2, 0);float m21 = get(2, 1);float m22 = get(2, 2);float m23 = get(2, 3);
/* 702 */       float m30 = get(3, 0);float m31 = get(3, 1);float m32 = get(3, 2);float m33 = get(3, 3);
/*     */       
/* 704 */       float denom = m00 * m11 * m22 * m33 + m00 * m12 * m23 * m31 + m00 * m13 * m21 * m32 + m01 * m10 * m23 * m32 + m01 * m12 * m20 * m33 + m01 * m13 * m22 * m30 + m02 * m10 * m21 * m33 + m02 * m11 * m23 * m30 + m02 * m13 * m20 * m31 + m03 * m10 * m22 * m31 + m03 * m11 * m20 * m32 + m03 * m12 * m21 * m30 - m01 * m10 * m22 * m33 - m00 * m12 * m21 * m33 - m02 * m11 * m20 * m33 - m00 * m11 * m23 * m32 - m03 * m10 * m21 * m32 - m01 * m13 * m20 * m32 - m02 * m10 * m23 * m31 - m00 * m13 * m22 * m31 - m03 * m12 * m20 * m31 - m01 * m12 * m23 * m30 - m03 * m11 * m22 * m30 - m02 * m13 * m21 * m30;
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
/* 729 */       result.put(0, 0, (m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32 - m12 * m21 * m33 - m11 * m23 * m32 - m13 * m22 * m31) / denom);
/* 730 */       result.put(0, 1, (m01 * m23 * m32 + m02 * m21 * m33 + m03 * m22 * m31 - m01 * m22 * m33 - m03 * m21 * m32 - m02 * m23 * m31) / denom);
/* 731 */       result.put(0, 2, (m01 * m12 * m33 + m02 * m13 * m31 + m03 * m11 * m32 - m02 * m11 * m33 - m01 * m13 * m32 - m03 * m12 * m31) / denom);
/* 732 */       result.put(0, 3, (m01 * m13 * m22 + m02 * m11 * m23 + m03 * m12 * m21 - m01 * m12 * m23 - m03 * m11 * m22 - m02 * m13 * m21) / denom);
/* 733 */       result.put(1, 0, (m10 * m23 * m32 + m12 * m20 * m33 + m13 * m22 * m30 - m10 * m22 * m33 - m13 * m20 * m32 - m12 * m23 * m30) / denom);
/* 734 */       result.put(1, 1, (m00 * m22 * m33 + m02 * m23 * m30 + m03 * m20 * m32 - m02 * m20 * m33 - m00 * m23 * m32 - m03 * m22 * m30) / denom);
/* 735 */       result.put(1, 2, (m00 * m13 * m32 + m02 * m10 * m33 + m03 * m12 * m30 - m00 * m12 * m33 - m03 * m10 * m32 - m02 * m13 * m30) / denom);
/* 736 */       result.put(1, 3, (m00 * m12 * m23 + m02 * m13 * m20 + m03 * m10 * m22 - m02 * m10 * m23 - m00 * m13 * m22 - m03 * m12 * m20) / denom);
/* 737 */       result.put(2, 0, (m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31 - m11 * m20 * m33 - m10 * m23 * m31 - m13 * m21 * m30) / denom);
/* 738 */       result.put(2, 1, (m00 * m23 * m31 + m01 * m20 * m33 + m03 * m21 * m30 - m00 * m21 * m33 - m03 * m20 * m31 - m01 * m23 * m30) / denom);
/* 739 */       result.put(2, 2, (m00 * m11 * m33 + m01 * m13 * m30 + m03 * m10 * m31 - m01 * m10 * m33 - m00 * m13 * m31 - m03 * m11 * m30) / denom);
/* 740 */       result.put(2, 3, (m00 * m13 * m21 + m01 * m10 * m23 + m03 * m11 * m20 - m00 * m11 * m23 - m03 * m10 * m21 - m01 * m13 * m20) / denom);
/* 741 */       result.put(3, 0, (m10 * m22 * m31 + m11 * m20 * m32 + m12 * m21 * m30 - m10 * m21 * m32 - m12 * m20 * m31 - m11 * m22 * m30) / denom);
/* 742 */       result.put(3, 1, (m00 * m21 * m32 + m01 * m22 * m30 + m02 * m20 * m31 - m01 * m20 * m32 - m00 * m22 * m31 - m02 * m21 * m30) / denom);
/* 743 */       result.put(3, 2, (m00 * m12 * m31 + m01 * m10 * m32 + m02 * m11 * m30 - m00 * m11 * m32 - m02 * m10 * m31 - m01 * m12 * m30) / denom);
/* 744 */       result.put(3, 3, (m00 * m11 * m22 + m01 * m12 * m20 + m02 * m10 * m21 - m01 * m10 * m22 - m00 * m12 * m21 - m02 * m11 * m20) / denom);
/*     */       
/* 746 */       return result;
/*     */     }
/*     */     
/* 749 */     if (this.numRows == 3)
/*     */     {
/* 751 */       MatrixF result = emptyMatrix(3, 3);
/*     */       
/* 753 */       float m00 = get(0, 0);float m01 = get(0, 1);float m02 = get(0, 2);
/* 754 */       float m10 = get(1, 0);float m11 = get(1, 1);float m12 = get(1, 2);
/* 755 */       float m20 = get(2, 0);float m21 = get(2, 1);float m22 = get(2, 2);
/*     */       
/* 757 */       float denom = m00 * m11 * m22 + m01 * m12 * m20 + m02 * m10 * m21 - m01 * m10 * m22 - m00 * m12 * m21 - m02 * m11 * m20;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 764 */       result.put(0, 0, (m11 * m22 - m12 * m21) / denom);
/* 765 */       result.put(0, 1, (m02 * m21 - m01 * m22) / denom);
/* 766 */       result.put(0, 2, (m01 * m12 - m02 * m11) / denom);
/* 767 */       result.put(1, 0, (m12 * m20 - m10 * m22) / denom);
/* 768 */       result.put(1, 1, (m00 * m22 - m02 * m20) / denom);
/* 769 */       result.put(1, 2, (m02 * m10 - m00 * m12) / denom);
/* 770 */       result.put(2, 0, (m10 * m21 - m11 * m20) / denom);
/* 771 */       result.put(2, 1, (m01 * m20 - m00 * m21) / denom);
/* 772 */       result.put(2, 2, (m00 * m11 - m01 * m10) / denom);
/*     */       
/* 774 */       return result;
/*     */     }
/*     */     
/* 777 */     if (this.numRows == 2)
/*     */     {
/* 779 */       MatrixF result = emptyMatrix(4, 4);
/*     */       
/* 781 */       float m00 = get(0, 0);float m01 = get(0, 1);
/* 782 */       float m10 = get(1, 0);float m11 = get(1, 1);
/*     */       
/* 784 */       float denom = m00 * m11 - m01 * m10;
/*     */       
/* 786 */       result.put(0, 0, m11 / denom);
/* 787 */       result.put(0, 1, -m01 / denom);
/* 788 */       result.put(1, 0, -m10 / denom);
/* 789 */       result.put(1, 1, m00 / denom);
/*     */       
/* 791 */       return result;
/*     */     }
/*     */     
/* 794 */     if (this.numRows == 1)
/*     */     {
/* 796 */       MatrixF result = emptyMatrix(4, 4);
/* 797 */       result.put(0, 0, 1.0F / get(0, 0));
/* 798 */       return result;
/*     */     }
/*     */     
/* 801 */     throw dimensionsError();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\matrices\MatrixF.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */