/*     */ package com.qualcomm.robotcore.util;
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
/*     */ public class Range
/*     */ {
/*     */   public static double scale(double n, double x1, double x2, double y1, double y2)
/*     */   {
/*  57 */     double a = (y1 - y2) / (x1 - x2);
/*  58 */     double b = y1 - x1 * (y1 - y2) / (x1 - x2);
/*  59 */     return a * n + b;
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
/*     */   public static double clip(double number, double min, double max)
/*     */   {
/*  73 */     if (number < min) return min;
/*  74 */     if (number > max) return max;
/*  75 */     return number;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float clip(float number, float min, float max)
/*     */   {
/*  85 */     if (number < min) return min;
/*  86 */     if (number > max) return max;
/*  87 */     return number;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int clip(int number, int min, int max)
/*     */   {
/*  97 */     if (number < min) return min;
/*  98 */     if (number > max) return max;
/*  99 */     return number;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static short clip(short number, short min, short max)
/*     */   {
/* 109 */     if (number < min) return min;
/* 110 */     if (number > max) return max;
/* 111 */     return number;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte clip(byte number, byte min, byte max)
/*     */   {
/* 121 */     if (number < min) return min;
/* 122 */     if (number > max) return max;
/* 123 */     return number;
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
/*     */   public static void throwIfRangeIsInvalid(double number, double min, double max)
/*     */     throws IllegalArgumentException
/*     */   {
/* 138 */     if ((number < min) || (number > max)) {
/* 139 */       throw new IllegalArgumentException(String.format("number %f is invalid; valid ranges are %f..%f", new Object[] { Double.valueOf(number), Double.valueOf(min), Double.valueOf(max) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void throwIfRangeIsInvalid(int number, int min, int max)
/*     */     throws IllegalArgumentException
/*     */   {
/* 152 */     if ((number < min) || (number > max)) {
/* 153 */       throw new IllegalArgumentException(String.format("number %d is invalid; valid ranges are %d..%d", new Object[] { Integer.valueOf(number), Integer.valueOf(min), Integer.valueOf(max) }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\Range.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */