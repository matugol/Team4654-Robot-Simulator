/*     */ package org.firstinspires.ftc.robotcore.external.navigation;
/*     */ 
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum DistanceUnit
/*     */ {
/*  42 */   METER(0),  CM(1),  MM(2),  INCH(3);
/*     */   
/*     */   public final byte bVal;
/*     */   public static final double mmPerInch = 25.4D;
/*     */   public static final double mPerInch = 0.0254D;
/*     */   
/*     */   private DistanceUnit(int i)
/*     */   {
/*  50 */     this.bVal = ((byte)i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double fromMeters(double meters)
/*     */   {
/*  59 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/*  62 */       return meters;
/*  63 */     case CM:  return meters * 100.0D;
/*  64 */     case MM:  return meters * 1000.0D; }
/*  65 */     return meters / 0.0254D;
/*     */   }
/*     */   
/*     */ 
/*     */   public double fromInches(double inches)
/*     */   {
/*  71 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/*  74 */       return inches * 0.0254D;
/*  75 */     case CM:  return inches * 0.0254D * 100.0D;
/*  76 */     case MM:  return inches * 0.0254D * 1000.0D; }
/*  77 */     return inches;
/*     */   }
/*     */   
/*     */ 
/*     */   public double fromCm(double cm)
/*     */   {
/*  83 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/*  86 */       return cm / 100.0D;
/*  87 */     case CM:  return cm;
/*  88 */     case MM:  return cm * 10.0D; }
/*  89 */     return fromMeters(METER.fromCm(cm));
/*     */   }
/*     */   
/*     */ 
/*     */   public double fromMm(double mm)
/*     */   {
/*  95 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/*  98 */       return mm / 1000.0D;
/*  99 */     case CM:  return mm / 10.0D;
/* 100 */     case MM:  return mm; }
/* 101 */     return fromMeters(METER.fromMm(mm));
/*     */   }
/*     */   
/*     */ 
/*     */   public double fromUnit(DistanceUnit him, double his)
/*     */   {
/* 107 */     switch (him) {
/*     */     case METER: 
/*     */     default: 
/* 110 */       return fromMeters(his);
/* 111 */     case CM:  return fromCm(his);
/* 112 */     case MM:  return fromMm(his); }
/* 113 */     return fromInches(his);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double toMeters(double inOurUnits)
/*     */   {
/* 123 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/* 126 */       return METER.fromMeters(inOurUnits);
/* 127 */     case CM:  return METER.fromCm(inOurUnits);
/* 128 */     case MM:  return METER.fromMm(inOurUnits); }
/* 129 */     return METER.fromInches(inOurUnits);
/*     */   }
/*     */   
/*     */ 
/*     */   public double toInches(double inOurUnits)
/*     */   {
/* 135 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/* 138 */       return INCH.fromMeters(inOurUnits);
/* 139 */     case CM:  return INCH.fromCm(inOurUnits);
/* 140 */     case MM:  return INCH.fromMm(inOurUnits); }
/* 141 */     return INCH.fromInches(inOurUnits);
/*     */   }
/*     */   
/*     */ 
/*     */   public double toCm(double inOurUnits)
/*     */   {
/* 147 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/* 150 */       return CM.fromMeters(inOurUnits);
/* 151 */     case CM:  return CM.fromCm(inOurUnits);
/* 152 */     case MM:  return CM.fromMm(inOurUnits); }
/* 153 */     return CM.fromInches(inOurUnits);
/*     */   }
/*     */   
/*     */ 
/*     */   public double toMm(double inOurUnits)
/*     */   {
/* 159 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/* 162 */       return MM.fromMeters(inOurUnits);
/* 163 */     case CM:  return MM.fromCm(inOurUnits);
/* 164 */     case MM:  return MM.fromMm(inOurUnits); }
/* 165 */     return MM.fromInches(inOurUnits);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString(double inOurUnits)
/*     */   {
/* 175 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/* 178 */       return String.format(Locale.getDefault(), "%.3fm", new Object[] { Double.valueOf(inOurUnits) });
/* 179 */     case CM:  return String.format(Locale.getDefault(), "%.1fcm", new Object[] { Double.valueOf(inOurUnits) });
/* 180 */     case MM:  return String.format(Locale.getDefault(), "%.0fmm", new Object[] { Double.valueOf(inOurUnits) }); }
/* 181 */     return String.format(Locale.getDefault(), "%.2fin", new Object[] { Double.valueOf(inOurUnits) });
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 187 */     switch (this) {
/*     */     case METER: 
/*     */     default: 
/* 190 */       return "m";
/* 191 */     case CM:  return "cm";
/* 192 */     case MM:  return "mm"; }
/* 193 */     return "in";
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\DistanceUnit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */