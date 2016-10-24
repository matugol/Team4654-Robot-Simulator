/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface ServoEx
/*     */ {
/*     */   public abstract void setPwmRange(ServoPwmRange paramServoPwmRange);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ServoPwmRange getPwmRange();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void setPwmEnable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void setPwmDisable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isPwmEnabled();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ServoPwmRange
/*     */   {
/*     */     public static final double usFrameDefault = 20000.0D;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */     public static final ServoPwmRange defaultRange = new ServoPwmRange(600.0D, 2400.0D);
/*     */     
/*     */ 
/*     */ 
/*     */     public final double usPulseLower;
/*     */     
/*     */ 
/*     */ 
/*     */     public final double usPulseUpper;
/*     */     
/*     */ 
/*     */     public final double usFrame;
/*     */     
/*     */ 
/*     */ 
/*     */     public ServoPwmRange(double usPulseLower, double usPulseUpper)
/*     */     {
/* 108 */       this(usPulseLower, usPulseUpper, 20000.0D);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ServoPwmRange(double usPulseLower, double usPulseUpper, double usFrame)
/*     */     {
/* 120 */       this.usPulseLower = usPulseLower;
/* 121 */       this.usPulseUpper = usPulseUpper;
/* 122 */       this.usFrame = usFrame;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object o)
/*     */     {
/* 128 */       if ((o instanceof ServoPwmRange))
/*     */       {
/* 130 */         ServoPwmRange him = (ServoPwmRange)o;
/* 131 */         return (this.usPulseLower == him.usPulseLower) && (this.usPulseUpper == him.usPulseUpper) && (this.usFrame == him.usFrame);
/*     */       }
/*     */       
/*     */ 
/* 135 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 141 */       return Double.valueOf(this.usPulseLower).hashCode() ^ Double.valueOf(this.usPulseUpper).hashCode() ^ Double.valueOf(this.usFrame).hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\ServoEx.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */