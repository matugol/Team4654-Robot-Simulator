/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ElapsedTime
/*     */ {
/*     */   @Deprecated
/*     */   public static final double dSECOND_IN_NANO = 1.0E9D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static final long lSECOND_IN_NANO = 1000000000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static final double dMILLIS_IN_NANO = 1000000.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static final long lMILLIS_IN_NANO = 1000000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final long SECOND_IN_NANO = 1000000000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final long MILLIS_IN_NANO = 1000000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long startTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final double resolution;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum Resolution
/*     */   {
/*  55 */     SECONDS, 
/*  56 */     MILLISECONDS;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Resolution() {}
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
/*     */   public ElapsedTime()
/*     */   {
/*  87 */     reset();
/*  88 */     this.resolution = 1.0E9D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElapsedTime(long startTime)
/*     */   {
/* 100 */     this.startTime = startTime;
/* 101 */     this.resolution = 1.0E9D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElapsedTime(Resolution resolution)
/*     */   {
/* 112 */     reset();
/* 113 */     switch (resolution) {
/*     */     case SECONDS: 
/*     */     default: 
/* 116 */       this.resolution = 1.0E9D;
/* 117 */       break;
/*     */     case MILLISECONDS: 
/* 119 */       this.resolution = 1000000.0D;
/*     */     }
/*     */     
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
/*     */   public void reset()
/*     */   {
/* 134 */     this.startTime = System.nanoTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double startTime()
/*     */   {
/* 142 */     return this.startTime / this.resolution;
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
/*     */   public double time()
/*     */   {
/* 156 */     return (System.nanoTime() - this.startTime) / this.resolution;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double seconds()
/*     */   {
/* 165 */     return (System.nanoTime() - this.startTime) / 1.0E9D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double milliseconds()
/*     */   {
/* 174 */     return seconds() * 1000.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Resolution getResolution()
/*     */   {
/* 182 */     if (this.resolution == 1000000.0D) {
/* 183 */       return Resolution.MILLISECONDS;
/*     */     }
/* 185 */     return Resolution.SECONDS;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String resolutionStr()
/*     */   {
/* 193 */     if (this.resolution == 1.0E9D)
/* 194 */       return "seconds";
/* 195 */     if (this.resolution == 1000000.0D) {
/* 196 */       return "milliseconds";
/*     */     }
/* 198 */     return "Unknown units";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void log(String label)
/*     */   {
/* 206 */     RobotLog.v(String.format("TIMER: %20s - %1.3f %s", new Object[] { label, Double.valueOf(time()), resolutionStr() }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 214 */     return String.format("%1.4f %s", new Object[] { Double.valueOf(time()), resolutionStr() });
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\ElapsedTime.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */