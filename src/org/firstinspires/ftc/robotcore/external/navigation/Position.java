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
/*    */ public class Position
/*    */ {
/*    */   public DistanceUnit unit;
/*    */   public double x;
/*    */   public double y;
/*    */   public double z;
/*    */   public long acquisitionTime;
/*    */   
/*    */   public Position()
/*    */   {
/* 64 */     this(DistanceUnit.MM, 0.0D, 0.0D, 0.0D, 0L);
/*    */   }
/*    */   
/*    */   public Position(DistanceUnit unit, double x, double y, double z, long acquisitionTime)
/*    */   {
/* 69 */     this.unit = unit;
/* 70 */     this.x = x;
/* 71 */     this.y = y;
/* 72 */     this.z = z;
/* 73 */     this.acquisitionTime = acquisitionTime;
/*    */   }
/*    */   
/*    */   public Position toUnit(DistanceUnit distanceUnit)
/*    */   {
/* 78 */     if (distanceUnit != this.unit)
/*    */     {
/* 80 */       return new Position(distanceUnit, distanceUnit.fromUnit(this.unit, this.x), distanceUnit.fromUnit(this.unit, this.y), distanceUnit.fromUnit(this.unit, this.z), this.acquisitionTime);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 87 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 96 */     return String.format(Locale.getDefault(), "(%.3f %.3f %.3f)%s", new Object[] { Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z), this.unit.toString() });
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\Position.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */