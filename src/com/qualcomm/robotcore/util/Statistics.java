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
/*     */ public class Statistics
/*     */ {
/*     */   int n;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   double mean;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   double m2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Statistics()
/*     */   {
/*  57 */     clear();
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
/*     */   public int getCount()
/*     */   {
/*  70 */     return this.n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getMean()
/*     */   {
/*  79 */     return this.mean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getVariance()
/*     */   {
/*  88 */     return this.m2 / (this.n - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getStandardDeviation()
/*     */   {
/*  97 */     return Math.sqrt(getVariance());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 109 */     this.n = 0;
/* 110 */     this.mean = 0.0D;
/* 111 */     this.m2 = 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(double x)
/*     */   {
/* 120 */     this.n += 1;
/* 121 */     double delta = x - this.mean;
/* 122 */     this.mean += delta / this.n;
/* 123 */     this.m2 += delta * (x - this.mean);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(double x)
/*     */   {
/* 132 */     int nPrev = this.n - 1;
/* 133 */     double delta = x - this.mean;
/* 134 */     double deltaPrev = this.n * delta / nPrev;
/* 135 */     this.m2 -= deltaPrev * delta;
/* 136 */     this.mean = ((this.mean * this.n - x) / nPrev);
/* 137 */     this.n = nPrev;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\Statistics.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */