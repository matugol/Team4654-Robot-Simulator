/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MovingStatistics
/*     */ {
/*     */   final Statistics statistics;
/*     */   final int capacity;
/*     */   final Queue<Double> samples;
/*     */   
/*     */   public MovingStatistics(int capacity)
/*     */   {
/*  59 */     if (capacity <= 0) throw new IllegalArgumentException("MovingStatistics capacity must be positive");
/*  60 */     this.statistics = new Statistics();
/*  61 */     this.capacity = capacity;
/*  62 */     this.samples = new LinkedList();
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
/*  75 */     return this.statistics.getCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getMean()
/*     */   {
/*  84 */     return this.statistics.getMean();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getVariance()
/*     */   {
/*  93 */     return this.statistics.getVariance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getStandardDeviation()
/*     */   {
/* 102 */     return this.statistics.getStandardDeviation();
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
/* 114 */     this.statistics.clear();
/* 115 */     this.samples.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(double x)
/*     */   {
/* 124 */     this.statistics.add(x);
/* 125 */     this.samples.add(Double.valueOf(x));
/* 126 */     if (this.samples.size() > this.capacity)
/*     */     {
/* 128 */       this.statistics.remove(((Double)this.samples.remove()).doubleValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\MovingStatistics.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */