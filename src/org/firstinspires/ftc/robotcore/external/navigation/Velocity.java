/*    */ package org.firstinspires.ftc.robotcore.external.navigation;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Velocity
/*    */ {
/*    */   public DistanceUnit unit;
/*    */   public double xVeloc;
/*    */   public double yVeloc;
/*    */   public double zVeloc;
/*    */   public long acquisitionTime;
/*    */   
/*    */   public Velocity()
/*    */   {
/* 67 */     this(DistanceUnit.MM, 0.0D, 0.0D, 0.0D, 0L);
/*    */   }
/*    */   
/*    */   public Velocity(DistanceUnit unit, double xVeloc, double yVeloc, double zVeloc, long acquisitionTime)
/*    */   {
/* 72 */     this.unit = unit;
/* 73 */     this.xVeloc = xVeloc;
/* 74 */     this.yVeloc = yVeloc;
/* 75 */     this.zVeloc = zVeloc;
/* 76 */     this.acquisitionTime = acquisitionTime;
/*    */   }
/*    */   
/*    */   public Velocity toUnit(DistanceUnit distanceUnit)
/*    */   {
/* 81 */     if (distanceUnit != this.unit)
/*    */     {
/* 83 */       return new Velocity(distanceUnit, distanceUnit.fromUnit(this.unit, this.xVeloc), distanceUnit.fromUnit(this.unit, this.yVeloc), distanceUnit.fromUnit(this.unit, this.zVeloc), this.acquisitionTime);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 90 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 99 */     return String.format(Locale.getDefault(), "(%.3f %.3f %.3f)%s/s", new Object[] { Double.valueOf(this.xVeloc), Double.valueOf(this.yVeloc), Double.valueOf(this.zVeloc), this.unit.toString() });
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\Velocity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */