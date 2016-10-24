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
/*     */ public class LastKnown<T>
/*     */ {
/*     */   protected T value;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValid;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ElapsedTime timer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected double msFreshness;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LastKnown()
/*     */   {
/*  63 */     this(500.0D);
/*     */   }
/*     */   
/*     */   public LastKnown(double msFreshness)
/*     */   {
/*  68 */     this.value = null;
/*  69 */     this.isValid = false;
/*  70 */     this.timer = new ElapsedTime();
/*  71 */     this.msFreshness = msFreshness;
/*     */   }
/*     */   
/*     */   public static <X> LastKnown<X>[] createArray(int length)
/*     */   {
/*  76 */     LastKnown<X>[] result = new LastKnown[length];
/*  77 */     for (int i = 0; i < length; i++)
/*  78 */       result[i] = new LastKnown();
/*  79 */     return result;
/*     */   }
/*     */   
/*     */   public static <X> void invalidateArray(LastKnown<X>[] array)
/*     */   {
/*  84 */     for (int i = 0; i < array.length; i++)
/*     */     {
/*  86 */       array[i].invalidate();
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
/*     */ 
/*     */ 
/*     */   public void invalidate()
/*     */   {
/* 101 */     this.isValid = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isValid()
/*     */   {
/* 110 */     return (this.isValid) && (this.timer.milliseconds() <= this.msFreshness);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getValue()
/*     */   {
/* 119 */     return (T)(isValid() ? this.value : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getNonTimedValue()
/*     */   {
/* 128 */     return (T)(this.isValid ? this.value : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getRawValue()
/*     */   {
/* 137 */     return (T)this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(T value)
/*     */   {
/* 146 */     this.value = value;
/* 147 */     this.isValid = true;
/* 148 */     if (null == value) {
/* 149 */       invalidate();
/*     */     } else {
/* 151 */       this.timer.reset();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isValue(T valueQ)
/*     */   {
/* 162 */     if (isValid()) {
/* 163 */       return this.value.equals(valueQ);
/*     */     }
/* 165 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean updateValue(T valueQ)
/*     */   {
/* 176 */     if (!isValue(valueQ))
/*     */     {
/* 178 */       setValue(valueQ);
/* 179 */       return true;
/*     */     }
/*     */     
/* 182 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\LastKnown.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */