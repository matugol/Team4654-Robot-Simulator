/*     */ package org.firstinspires.ftc.robotcore.internal.opengl.models;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class VertexBuilder
/*     */ {
/*     */   private static final int coordinatesPerVertex = 3;
/*     */   private final float[] vertexData;
/*  31 */   private final List<DrawCommand> drawList = new ArrayList();
/*  32 */   private int offset = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private VertexBuilder(int sizeInVertices)
/*     */   {
/*  40 */     this.vertexData = new float[sizeInVertices * 3];
/*     */   }
/*     */   
/*     */   public static GeneratedData createSolidCylinder(Geometry.Cylinder cylinder, int numPoints)
/*     */   {
/*  45 */     int size = 2 * sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints);
/*     */     
/*  47 */     VertexBuilder builder = new VertexBuilder(size);
/*     */     
/*  49 */     Geometry.Circle puckTop = new Geometry.Circle(cylinder.center.translateY(cylinder.height / 2.0F), cylinder.radius);
/*  50 */     Geometry.Circle puckBottom = new Geometry.Circle(cylinder.center.translateY(-cylinder.height / 2.0F), cylinder.radius);
/*     */     
/*  52 */     builder.appendCircle(puckTop, numPoints, false);
/*  53 */     builder.appendCircle(puckBottom, numPoints, true);
/*  54 */     builder.appendOpenCylinder(cylinder, numPoints);
/*     */     
/*  56 */     return builder.build();
/*     */   }
/*     */   
/*     */   public static GeneratedData createMallet(Geometry.Point3 center, float radius, float height, int numPoints)
/*     */   {
/*  61 */     int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2;
/*     */     
/*  63 */     VertexBuilder builder = new VertexBuilder(size);
/*     */     
/*     */ 
/*  66 */     float baseHeight = height * 0.25F;
/*     */     
/*  68 */     Geometry.Circle baseCircle = new Geometry.Circle(center.translateY(-baseHeight), radius);
/*  69 */     Geometry.Cylinder baseCylinder = new Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2.0F), radius, baseHeight);
/*     */     
/*  71 */     builder.appendCircle(baseCircle, numPoints, true);
/*  72 */     builder.appendOpenCylinder(baseCylinder, numPoints);
/*     */     
/*     */ 
/*  75 */     float handleHeight = height * 0.75F;
/*  76 */     float handleRadius = radius / 3.0F;
/*     */     
/*  78 */     Geometry.Circle handleCircle = new Geometry.Circle(center.translateY(height * 0.5F), handleRadius);
/*  79 */     Geometry.Cylinder handleCylinder = new Geometry.Cylinder(handleCircle.center.translateY(-handleHeight / 2.0F), handleRadius, handleHeight);
/*     */     
/*  81 */     builder.appendCircle(handleCircle, numPoints, true);
/*  82 */     builder.appendOpenCylinder(handleCylinder, numPoints);
/*     */     
/*  84 */     return builder.build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class GeneratedData
/*     */   {
/*     */     public final float[] vertexData;
/*     */     
/*     */ 
/*     */ 
/*     */     public final List<VertexBuilder.DrawCommand> drawList;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     GeneratedData(float[] vertexData, List<VertexBuilder.DrawCommand> drawList)
/*     */     {
/* 103 */       this.vertexData = vertexData;
/* 104 */       this.drawList = drawList;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int sizeOfCircleInVertices(int numPoints)
/*     */   {
/* 114 */     return 1 + (numPoints + 1);
/*     */   }
/*     */   
/*     */   private static int sizeOfOpenCylinderInVertices(int numPoints)
/*     */   {
/* 119 */     return (numPoints + 1) * 2;
/*     */   }
/*     */   
/*     */   private void appendCircle(Geometry.Circle circle, int numPoints, boolean forward)
/*     */   {
/* 124 */     final int startVertex = this.offset / 3;
/* 125 */     final int numVertices = sizeOfCircleInVertices(numPoints);
/*     */     
/*     */ 
/* 128 */     this.vertexData[(this.offset++)] = circle.center.x;
/* 129 */     this.vertexData[(this.offset++)] = circle.center.y;
/* 130 */     this.vertexData[(this.offset++)] = circle.center.z;
/*     */     
/*     */ 
/*     */ 
/* 134 */     for (int j = 0; j <= numPoints; j++)
/*     */     {
/* 136 */       int i = forward ? j : numPoints - j;
/* 137 */       float angleInRadians = i / numPoints * 6.2831855F;
/* 138 */       this.vertexData[(this.offset++)] = (circle.center.x + circle.radius * (float)Math.cos(angleInRadians));
/* 139 */       this.vertexData[(this.offset++)] = circle.center.y;
/* 140 */       this.vertexData[(this.offset++)] = (circle.center.z + circle.radius * (float)Math.sin(angleInRadians));
/*     */     }
/* 142 */     this.drawList.add(new DrawCommand()
/*     */     {
/*     */ 
/*     */       public void draw()
/*     */       {
/* 147 */         GLES20.glDrawArrays(6, startVertex, numVertices);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void appendOpenCylinder(Geometry.Cylinder cylinder, int numPoints)
/*     */   {
/* 154 */     final int startVertex = this.offset / 3;
/* 155 */     final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
/* 156 */     float yStart = cylinder.center.y - cylinder.height / 2.0F;
/* 157 */     float yEnd = cylinder.center.y + cylinder.height / 2.0F;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 162 */     for (int i = 0; i <= numPoints; i++)
/*     */     {
/* 164 */       float angleInRadians = i / numPoints * 6.2831855F;
/*     */       
/* 166 */       float xPosition = cylinder.center.x + cylinder.radius * (float)Math.cos(angleInRadians);
/* 167 */       float zPosition = cylinder.center.z + cylinder.radius * (float)Math.sin(angleInRadians);
/*     */       
/* 169 */       this.vertexData[(this.offset++)] = xPosition;
/* 170 */       this.vertexData[(this.offset++)] = yStart;
/* 171 */       this.vertexData[(this.offset++)] = zPosition;
/*     */       
/* 173 */       this.vertexData[(this.offset++)] = xPosition;
/* 174 */       this.vertexData[(this.offset++)] = yEnd;
/* 175 */       this.vertexData[(this.offset++)] = zPosition;
/*     */     }
/* 177 */     this.drawList.add(new DrawCommand()
/*     */     {
/*     */       public void draw() {
/* 180 */         GLES20.glDrawArrays(5, startVertex, numVertices);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private GeneratedData build() {
/* 186 */     return new GeneratedData(this.vertexData, this.drawList);
/*     */   }
/*     */   
/*     */   public static abstract interface DrawCommand
/*     */   {
/*     */     public abstract void draw();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\VertexBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */