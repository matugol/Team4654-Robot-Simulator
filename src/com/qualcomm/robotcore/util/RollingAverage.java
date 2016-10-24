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
/*     */ public class RollingAverage
/*     */ {
/*     */   public static final int DEFAULT_SIZE = 100;
/*  45 */   private final Queue<Integer> queue = new LinkedList();
/*     */   
/*     */   private long total;
/*     */   
/*     */   private int size;
/*     */   
/*     */   public RollingAverage()
/*     */   {
/*  53 */     resize(100);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RollingAverage(int size)
/*     */   {
/*  61 */     resize(size);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/*  69 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resize(int size)
/*     */   {
/*  79 */     this.size = size;
/*  80 */     this.queue.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addNumber(int number)
/*     */   {
/*  88 */     if (this.queue.size() >= this.size) {
/*  89 */       int last = ((Integer)this.queue.remove()).intValue();
/*  90 */       this.total -= last;
/*     */     }
/*     */     
/*  93 */     this.queue.add(Integer.valueOf(number));
/*  94 */     this.total += number;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getAverage()
/*     */   {
/* 102 */     if (this.queue.isEmpty()) return 0;
/* 103 */     return (int)(this.total / this.queue.size());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 110 */     this.queue.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\RollingAverage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */