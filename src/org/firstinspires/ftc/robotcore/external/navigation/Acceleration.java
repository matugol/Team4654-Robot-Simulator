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
/*     */ public class Acceleration
/*     */ {
/*     */   public static final double earthGravity = 9.80665D;
/*     */   public DistanceUnit unit;
/*     */   public double xAccel;
/*     */   public double yAccel;
/*     */   public double zAccel;
/*     */   public long acquisitionTime;
/*     */   
/*     */   public Acceleration()
/*     */   {
/*  70 */     this(DistanceUnit.MM, 0.0D, 0.0D, 0.0D, 0L);
/*     */   }
/*     */   
/*     */   public Acceleration(DistanceUnit unit, double xAccel, double yAccel, double zAccel, long acquisitionTime)
/*     */   {
/*  75 */     this.unit = unit;
/*  76 */     this.xAccel = xAccel;
/*  77 */     this.yAccel = yAccel;
/*  78 */     this.zAccel = zAccel;
/*  79 */     this.acquisitionTime = acquisitionTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Acceleration fromGravity(double gx, double gy, double gz, long acquisitionTime)
/*     */   {
/*  88 */     return new Acceleration(DistanceUnit.METER, gx * 9.80665D, gy * 9.80665D, gz * 9.80665D, acquisitionTime);
/*     */   }
/*     */   
/*     */   public Acceleration toUnit(DistanceUnit distanceUnit)
/*     */   {
/*  93 */     if (distanceUnit != this.unit)
/*     */     {
/*  95 */       return new Acceleration(distanceUnit, distanceUnit.fromUnit(this.unit, this.xAccel), distanceUnit.fromUnit(this.unit, this.yAccel), distanceUnit.fromUnit(this.unit, this.zAccel), this.acquisitionTime);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 111 */     return String.format(Locale.getDefault(), "(%.3f %.3f %.3f)%s/s^2", new Object[] { Double.valueOf(this.xAccel), Double.valueOf(this.yAccel), Double.valueOf(this.zAccel), this.unit.toString() });
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\Acceleration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */