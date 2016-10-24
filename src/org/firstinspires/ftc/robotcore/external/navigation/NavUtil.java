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
/*     */ 
/*     */ public class NavUtil
/*     */ {
/*     */   public static Position plus(Position a, Position b)
/*     */   {
/*  47 */     return new Position(a.unit, a.x + a.unit.fromUnit(b.unit, b.x), a.y + a.unit.fromUnit(b.unit, b.y), a.z + a.unit.fromUnit(b.unit, b.z), Math.max(a.acquisitionTime, b.acquisitionTime));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Velocity plus(Velocity a, Velocity b)
/*     */   {
/*  55 */     return new Velocity(a.unit, a.xVeloc + a.unit.fromUnit(b.unit, b.xVeloc), a.yVeloc + a.unit.fromUnit(b.unit, b.yVeloc), a.zVeloc + a.unit.fromUnit(b.unit, b.zVeloc), Math.max(a.acquisitionTime, b.acquisitionTime));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Acceleration plus(Acceleration a, Acceleration b)
/*     */   {
/*  63 */     return new Acceleration(a.unit, a.xAccel + a.unit.fromUnit(b.unit, b.xAccel), a.yAccel + a.unit.fromUnit(b.unit, b.yAccel), a.zAccel + a.unit.fromUnit(b.unit, b.zAccel), Math.max(a.acquisitionTime, b.acquisitionTime));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Position minus(Position a, Position b)
/*     */   {
/*  72 */     return new Position(a.unit, a.x - a.unit.fromUnit(b.unit, b.x), a.y - a.unit.fromUnit(b.unit, b.y), a.z - a.unit.fromUnit(b.unit, b.z), Math.max(a.acquisitionTime, b.acquisitionTime));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Velocity minus(Velocity a, Velocity b)
/*     */   {
/*  80 */     return new Velocity(a.unit, a.xVeloc - a.unit.fromUnit(b.unit, b.xVeloc), a.yVeloc - a.unit.fromUnit(b.unit, b.yVeloc), a.zVeloc - a.unit.fromUnit(b.unit, b.zVeloc), Math.max(a.acquisitionTime, b.acquisitionTime));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Acceleration minus(Acceleration a, Acceleration b)
/*     */   {
/*  88 */     return new Acceleration(a.unit, a.xAccel - a.unit.fromUnit(b.unit, b.xAccel), a.yAccel - a.unit.fromUnit(b.unit, b.yAccel), a.zAccel - a.unit.fromUnit(b.unit, b.zAccel), Math.max(a.acquisitionTime, b.acquisitionTime));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Position scale(Position p, double scale)
/*     */   {
/*  97 */     return new Position(p.unit, p.x * scale, p.y * scale, p.z * scale, p.acquisitionTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Velocity scale(Velocity v, double scale)
/*     */   {
/* 105 */     return new Velocity(v.unit, v.xVeloc * scale, v.yVeloc * scale, v.zVeloc * scale, v.acquisitionTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Acceleration scale(Acceleration a, double scale)
/*     */   {
/* 113 */     return new Acceleration(a.unit, a.xAccel * scale, a.yAccel * scale, a.zAccel * scale, a.acquisitionTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Position integrate(Velocity v, double dt)
/*     */   {
/* 122 */     return new Position(v.unit, v.xVeloc * dt, v.yVeloc * dt, v.zVeloc * dt, v.acquisitionTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Velocity integrate(Acceleration a, double dt)
/*     */   {
/* 130 */     return new Velocity(a.unit, a.xAccel * dt, a.yAccel * dt, a.zAccel * dt, a.acquisitionTime);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Position meanIntegrate(Velocity cur, Velocity prev)
/*     */   {
/* 153 */     double duration = (cur.acquisitionTime - prev.acquisitionTime) * 1.0E-9D;
/* 154 */     Velocity meanVelocity = scale(plus(cur, prev), 0.5D);
/* 155 */     return integrate(meanVelocity, duration);
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
/*     */   public static Velocity meanIntegrate(Acceleration cur, Acceleration prev)
/*     */   {
/* 170 */     double duration = (cur.acquisitionTime - prev.acquisitionTime) * 1.0E-9D;
/* 171 */     Acceleration meanAcceleration = scale(plus(cur, prev), 0.5D);
/* 172 */     return integrate(meanAcceleration, duration);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\NavUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */