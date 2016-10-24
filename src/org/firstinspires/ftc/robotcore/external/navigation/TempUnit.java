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
/*     */ 
/*     */ public enum TempUnit
/*     */ {
/*  44 */   CELSIUS(0),  FARENHEIT(1),  KELVIN(2);
/*     */   
/*     */ 
/*     */   public final byte bVal;
/*     */   
/*     */   public static final double zeroCelsiusK = 273.15D;
/*     */   
/*     */   public static final double zeroCelsiusF = 32.0D;
/*     */   
/*     */   public static final double CperF = 0.5555555555555556D;
/*     */   
/*     */   private TempUnit(int i)
/*     */   {
/*  57 */     this.bVal = ((byte)i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double fromCelsius(double celsius)
/*     */   {
/*  66 */     switch (this) {
/*     */     case CELSIUS: 
/*     */     default: 
/*  69 */       return celsius;
/*  70 */     case KELVIN:  return celsius + 273.15D; }
/*  71 */     return celsius / 0.5555555555555556D + 32.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public double fromKelvin(double kelvin)
/*     */   {
/*  77 */     switch (this) {
/*     */     case CELSIUS: 
/*     */     default: 
/*  80 */       return kelvin - 273.15D;
/*  81 */     case KELVIN:  return kelvin; }
/*  82 */     return fromCelsius(CELSIUS.fromKelvin(kelvin));
/*     */   }
/*     */   
/*     */ 
/*     */   public double fromFarenheit(double farenheit)
/*     */   {
/*  88 */     switch (this) {
/*     */     case CELSIUS: 
/*     */     default: 
/*  91 */       return (farenheit - 32.0D) * 0.5555555555555556D;
/*  92 */     case KELVIN:  return fromCelsius(CELSIUS.fromFarenheit(farenheit)); }
/*  93 */     return farenheit;
/*     */   }
/*     */   
/*     */ 
/*     */   public double fromUnit(TempUnit him, double his)
/*     */   {
/*  99 */     switch (him) {
/*     */     case CELSIUS: 
/*     */     default: 
/* 102 */       return fromCelsius(his);
/* 103 */     case KELVIN:  return fromKelvin(his); }
/* 104 */     return fromFarenheit(his);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   double toCelsius(double inOurUnits)
/*     */   {
/* 114 */     switch (this) {
/*     */     case CELSIUS: 
/*     */     default: 
/* 117 */       return CELSIUS.fromCelsius(inOurUnits);
/* 118 */     case KELVIN:  return CELSIUS.fromKelvin(inOurUnits); }
/* 119 */     return CELSIUS.fromFarenheit(inOurUnits);
/*     */   }
/*     */   
/*     */ 
/*     */   double toKelvin(double inOurUnits)
/*     */   {
/* 125 */     switch (this) {
/*     */     case CELSIUS: 
/*     */     default: 
/* 128 */       return KELVIN.fromCelsius(inOurUnits);
/* 129 */     case KELVIN:  return KELVIN.fromKelvin(inOurUnits); }
/* 130 */     return KELVIN.fromFarenheit(inOurUnits);
/*     */   }
/*     */   
/*     */ 
/*     */   double toFarenheit(double inOurUnits)
/*     */   {
/* 136 */     switch (this) {
/*     */     case CELSIUS: 
/*     */     default: 
/* 139 */       return FARENHEIT.fromCelsius(inOurUnits);
/* 140 */     case KELVIN:  return FARENHEIT.fromKelvin(inOurUnits); }
/* 141 */     return FARENHEIT.fromFarenheit(inOurUnits);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\TempUnit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */