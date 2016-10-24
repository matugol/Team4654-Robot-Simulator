/*     */ package org.firstinspires.ftc.robotcore.external.matrices;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import org.firstinspires.ftc.robotcore.external.Const;
/*     */ import org.firstinspires.ftc.robotcore.external.NonConst;
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
/*     */ public class VectorF
/*     */ {
/*     */   protected float[] data;
/*     */   
/*     */   public static VectorF length(int length)
/*     */   {
/*  69 */     return new VectorF(new float[length]);
/*     */   }
/*     */   
/*     */   public VectorF(float[] data)
/*     */   {
/*  74 */     this.data = data;
/*     */   }
/*     */   
/*     */   public VectorF(float x)
/*     */   {
/*  79 */     this.data = new float[1];
/*  80 */     this.data[0] = x;
/*     */   }
/*     */   
/*     */   public VectorF(float x, float y)
/*     */   {
/*  85 */     this.data = new float[2];
/*  86 */     this.data[0] = x;
/*  87 */     this.data[1] = y;
/*     */   }
/*     */   
/*     */   public VectorF(float x, float y, float z)
/*     */   {
/*  92 */     this.data = new float[3];
/*  93 */     this.data[0] = x;
/*  94 */     this.data[1] = y;
/*  95 */     this.data[2] = z;
/*     */   }
/*     */   
/*     */   public VectorF(float x, float y, float z, float w)
/*     */   {
/* 100 */     this.data = new float[4];
/* 101 */     this.data[0] = x;
/* 102 */     this.data[1] = y;
/* 103 */     this.data[2] = z;
/* 104 */     this.data[3] = w;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public float[] getData()
/*     */   {
/* 113 */     return this.data;
/*     */   }
/*     */   
/*     */   @Const
/*     */   public int length() {
/* 118 */     return this.data.length;
/*     */   }
/*     */   
/*     */   @Const
/*     */   public float get(int index) {
/* 123 */     return this.data[index];
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void put(int index, float value) {
/* 128 */     this.data[index] = value;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 133 */     StringBuilder result = new StringBuilder();
/* 134 */     result.append("{");
/* 135 */     for (int i = 0; i < length(); i++)
/*     */     {
/* 137 */       if (i > 0) result.append(" ");
/* 138 */       result.append(String.format("%.2f", new Object[] { Float.valueOf(this.data[i]) }));
/*     */     }
/* 140 */     result.append("}");
/* 141 */     return result.toString();
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
/*     */   public VectorF normalized3D()
/*     */   {
/* 158 */     if (length() == 3)
/*     */     {
/* 160 */       return this;
/*     */     }
/* 162 */     if (length() == 4)
/*     */     {
/* 164 */       return new VectorF(this.data[0] / this.data[3], this.data[1] / this.data[3], this.data[2] / this.data[3]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 170 */     throw dimensionsError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public float magnitude()
/*     */   {
/* 179 */     return (float)Math.sqrt(dotProduct(this));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public float dotProduct(VectorF him)
/*     */   {
/* 191 */     if (length() == him.length())
/*     */     {
/* 193 */       float sum = 0.0F;
/* 194 */       for (int i = 0; i < length(); i++)
/*     */       {
/* 196 */         sum += get(i) * him.get(i);
/*     */       }
/* 198 */       return sum;
/*     */     }
/*     */     
/* 201 */     throw dimensionsError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public MatrixF multiplied(MatrixF him)
/*     */   {
/* 209 */     return new RowMatrixF(this).multiplied(him);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Const
/*     */   public MatrixF added(MatrixF addend)
/*     */   {
/* 217 */     return new RowMatrixF(this).added(addend);
/*     */   }
/*     */   
/*     */   @Const
/*     */   public VectorF added(VectorF addend) {
/* 222 */     if (length() == addend.length())
/*     */     {
/* 224 */       VectorF result = length(length());
/* 225 */       for (int i = 0; i < length(); i++)
/*     */       {
/* 227 */         result.put(i, get(i) + addend.get(i));
/*     */       }
/* 229 */       return result;
/*     */     }
/*     */     
/* 232 */     throw dimensionsError();
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void add(VectorF addend) {
/* 237 */     if (length() == addend.length())
/*     */     {
/* 239 */       for (int i = 0; i < length(); i++)
/*     */       {
/* 241 */         put(i, get(i) + addend.get(i));
/*     */       }
/*     */       
/*     */     } else {
/* 245 */       throw dimensionsError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Const
/*     */   public MatrixF subtracted(MatrixF subtrahend)
/*     */   {
/* 253 */     return new RowMatrixF(this).subtracted(subtrahend);
/*     */   }
/*     */   
/*     */   @Const
/*     */   public VectorF subtracted(VectorF subtrahend) {
/* 258 */     if (length() == subtrahend.length())
/*     */     {
/* 260 */       VectorF result = length(length());
/* 261 */       for (int i = 0; i < length(); i++)
/*     */       {
/* 263 */         result.put(i, get(i) - subtrahend.get(i));
/*     */       }
/* 265 */       return result;
/*     */     }
/*     */     
/* 268 */     throw dimensionsError();
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void subtract(VectorF subtrahend) {
/* 273 */     if (length() == subtrahend.length())
/*     */     {
/* 275 */       for (int i = 0; i < length(); i++)
/*     */       {
/* 277 */         put(i, get(i) - subtrahend.get(i));
/*     */       }
/*     */       
/*     */     } else {
/* 281 */       throw dimensionsError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Const
/*     */   public VectorF multiplied(float scale)
/*     */   {
/* 289 */     VectorF result = length(length());
/* 290 */     for (int i = 0; i < length(); i++)
/*     */     {
/* 292 */       result.put(i, get(i) * scale);
/*     */     }
/* 294 */     return result;
/*     */   }
/*     */   
/*     */   @NonConst
/*     */   public void multiply(float scale) {
/* 299 */     for (int i = 0; i < length(); i++)
/*     */     {
/* 301 */       put(i, get(i) * scale);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RuntimeException dimensionsError()
/*     */   {
/* 311 */     return dimensionsError(length());
/*     */   }
/*     */   
/*     */   @SuppressLint({"DefaultLocale"})
/*     */   protected static RuntimeException dimensionsError(int length) {
/* 316 */     return new IllegalArgumentException(String.format("vector dimensions are incorrect: length=%d", new Object[] { Integer.valueOf(length) }));
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\matrices\VectorF.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */