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
/*    */ 
/*    */ public class MagneticFlux
/*    */ {
/*    */   public double x;
/*    */   public double y;
/*    */   public double z;
/*    */   public long acquisitionTime;
/*    */   
/*    */   public MagneticFlux()
/*    */   {
/* 67 */     this(0.0D, 0.0D, 0.0D, 0L);
/*    */   }
/*    */   
/*    */   public MagneticFlux(double x, double y, double z, long acquisitionTime)
/*    */   {
/* 72 */     this.x = x;
/* 73 */     this.y = y;
/* 74 */     this.z = z;
/* 75 */     this.acquisitionTime = acquisitionTime;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 84 */     double scale = 1000.0D;
/* 85 */     return String.format(Locale.getDefault(), "(%.3f %.3f %.3f)mT", new Object[] { Double.valueOf(this.x * scale), Double.valueOf(this.y * scale), Double.valueOf(this.z * scale) });
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\MagneticFlux.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */