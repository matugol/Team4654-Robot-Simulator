/*     */ package org.firstinspires.ftc.robotcore.external.navigation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum AngleUnit
/*     */ {
/*  43 */   DEGREES(0),  RADIANS(1);
/*     */   
/*     */   public final byte bVal;
/*     */   protected static final double TwoPi = 6.283185307179586D;
/*     */   public static final float Pif = 3.1415927F;
/*     */   public static final float halfPif = 1.5707964F;
/*     */   
/*     */   private AngleUnit(int i)
/*     */   {
/*  52 */     this.bVal = ((byte)i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double fromDegrees(double degrees)
/*     */   {
/*  61 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/*  64 */       return normalizeRadians(degrees / 180.0D * 3.141592653589793D); }
/*  65 */     return normalizeDegrees(degrees);
/*     */   }
/*     */   
/*     */ 
/*     */   public float fromDegrees(float degrees)
/*     */   {
/*  71 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/*  74 */       return normalizeRadians(degrees / 180.0F * 3.1415927F); }
/*  75 */     return normalizeDegrees(degrees);
/*     */   }
/*     */   
/*     */ 
/*     */   public double fromRadians(double radians)
/*     */   {
/*  81 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/*  84 */       return normalizeRadians(radians); }
/*  85 */     return normalizeDegrees(radians / 3.141592653589793D * 180.0D);
/*     */   }
/*     */   
/*     */ 
/*     */   public float fromRadians(float radians)
/*     */   {
/*  91 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/*  94 */       return normalizeRadians(radians); }
/*  95 */     return normalizeDegrees(radians / 3.1415927F * 180.0F);
/*     */   }
/*     */   
/*     */ 
/*     */   public double fromUnit(AngleUnit him, double his)
/*     */   {
/* 101 */     switch (him) {
/*     */     case RADIANS: 
/*     */     default: 
/* 104 */       return fromRadians(his); }
/* 105 */     return fromDegrees(his);
/*     */   }
/*     */   
/*     */   public float fromUnit(AngleUnit him, float his)
/*     */   {
/* 110 */     switch (him) {
/*     */     case RADIANS: 
/*     */     default: 
/* 113 */       return fromRadians(his); }
/* 114 */     return fromDegrees(his);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double toDegrees(double inOurUnits)
/*     */   {
/* 124 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/* 127 */       return DEGREES.fromRadians(inOurUnits); }
/* 128 */     return DEGREES.fromDegrees(inOurUnits);
/*     */   }
/*     */   
/*     */ 
/*     */   public float toDegrees(float inOurUnits)
/*     */   {
/* 134 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/* 137 */       return DEGREES.fromRadians(inOurUnits); }
/* 138 */     return DEGREES.fromDegrees(inOurUnits);
/*     */   }
/*     */   
/*     */ 
/*     */   public double toRadians(double inOurUnits)
/*     */   {
/* 144 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/* 147 */       return RADIANS.fromRadians(inOurUnits); }
/* 148 */     return RADIANS.fromDegrees(inOurUnits);
/*     */   }
/*     */   
/*     */   public float toRadians(float inOurUnits)
/*     */   {
/* 153 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/* 156 */       return RADIANS.fromRadians(inOurUnits); }
/* 157 */     return RADIANS.fromDegrees(inOurUnits);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double normalize(double mine)
/*     */   {
/* 167 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/* 170 */       return normalizeRadians(mine); }
/* 171 */     return normalizeDegrees(mine);
/*     */   }
/*     */   
/*     */ 
/*     */   public float normalize(float mine)
/*     */   {
/* 177 */     switch (this) {
/*     */     case RADIANS: 
/*     */     default: 
/* 180 */       return normalizeRadians(mine); }
/* 181 */     return normalizeDegrees(mine);
/*     */   }
/*     */   
/*     */ 
/*     */   public static double normalizeDegrees(double degrees)
/*     */   {
/* 187 */     while (degrees >= 180.0D) degrees -= 360.0D;
/* 188 */     while (degrees < -180.0D) degrees += 360.0D;
/* 189 */     return degrees;
/*     */   }
/*     */   
/*     */   public static float normalizeDegrees(float degrees)
/*     */   {
/* 194 */     return (float)normalizeDegrees(degrees);
/*     */   }
/*     */   
/*     */   public static double normalizeRadians(double radians)
/*     */   {
/* 199 */     while (radians >= 3.141592653589793D) radians -= 6.283185307179586D;
/* 200 */     while (radians < -3.141592653589793D) radians += 6.283185307179586D;
/* 201 */     return radians;
/*     */   }
/*     */   
/*     */   public static float normalizeRadians(float radians)
/*     */   {
/* 206 */     return (float)normalizeRadians(radians);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\AngleUnit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */