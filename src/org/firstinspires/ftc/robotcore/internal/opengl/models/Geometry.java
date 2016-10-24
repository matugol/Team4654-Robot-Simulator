/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.models;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Geometry
/*    */ {
/*    */   public static class Point3
/*    */   {
/*    */     public final float x;
/*    */     
/*    */ 
/*    */     public final float y;
/*    */     
/*    */     public final float z;
/*    */     
/*    */ 
/*    */     public Point3(float x, float y, float z)
/*    */     {
/* 19 */       this.x = x;
/* 20 */       this.y = y;
/* 21 */       this.z = z;
/*    */     }
/*    */     
/*    */     public Point3 translateY(float distance)
/*    */     {
/* 26 */       return new Point3(this.x, this.y + distance, this.z);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Circle
/*    */   {
/*    */     public final Geometry.Point3 center;
/*    */     public final float radius;
/*    */     
/*    */     public Circle(Geometry.Point3 center, float radius)
/*    */     {
/* 37 */       this.center = center;
/* 38 */       this.radius = radius;
/*    */     }
/*    */     
/*    */     public Circle scale(float scale)
/*    */     {
/* 43 */       return new Circle(this.center, this.radius * scale);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Cylinder
/*    */   {
/*    */     public final Geometry.Point3 center;
/*    */     public final float radius;
/*    */     public final float height;
/*    */     
/*    */     public Cylinder(Geometry.Point3 center, float radius, float height)
/*    */     {
/* 55 */       this.center = center;
/* 56 */       this.radius = radius;
/* 57 */       this.height = height;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\Geometry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */