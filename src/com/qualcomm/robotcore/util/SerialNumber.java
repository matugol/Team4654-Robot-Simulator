/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.robotcore.R.string;
/*     */ import java.io.Serializable;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SerialNumber
/*     */   implements Serializable
/*     */ {
/*     */   private final String serialNumber;
/*     */   private static final String fakePrefix = "FakeUSB:";
/*     */   
/*     */   public SerialNumber()
/*     */   {
/*  66 */     this.serialNumber = generateFake();
/*     */   }
/*     */   
/*     */   private static String generateFake() {
/*  70 */     return "FakeUSB:" + UUID.randomUUID().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerialNumber(String initializer)
/*     */   {
/*  80 */     this.serialNumber = (isLegacyFake(initializer) ? generateFake() : initializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFake()
/*     */   {
/*  88 */     return (this.serialNumber.startsWith("FakeUSB:")) || (isLegacyFake(this.serialNumber));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReal()
/*     */   {
/*  96 */     return !isFake();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isLegacyFake(String initializer)
/*     */   {
/* 106 */     return (initializer == null) || (initializer.equals("-1")) || (initializer.equalsIgnoreCase("N/A")) || (initializer.trim().isEmpty());
/*     */   }
/*     */   
/*     */   public boolean equals(Object object)
/*     */   {
/* 111 */     if (object == null) return false;
/* 112 */     if (object == this) { return true;
/*     */     }
/* 114 */     if ((object instanceof SerialNumber)) {
/* 115 */       return this.serialNumber.equals(((SerialNumber)object).serialNumber);
/*     */     }
/*     */     
/* 118 */     if ((object instanceof String)) {
/* 119 */       return this.serialNumber.equals(object);
/*     */     }
/*     */     
/* 122 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 127 */     return this.serialNumber.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 132 */     return this.serialNumber;
/*     */   }
/*     */   
/*     */   public String toString(Context context) {
/* 136 */     return isFake() ? context.getString(R.string.noSerialNumber) : this.serialNumber;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public String getSerialNumber()
/*     */   {
/* 142 */     return this.serialNumber;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\SerialNumber.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */