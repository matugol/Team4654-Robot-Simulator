/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.models;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.PositionAttributeShader;
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
/*    */ public class SolidCylinder
/*    */ {
/*    */   private static final int coordinatesPerVertex = 3;
/*    */   public final float radius;
/*    */   public final float height;
/*    */   private final VertexArray vertexArray;
/*    */   private final List<VertexBuilder.DrawCommand> drawList;
/*    */   
/*    */   public SolidCylinder(float radius, float height, int numPointsAroundPuck)
/*    */   {
/* 31 */     VertexBuilder.GeneratedData generatedData = VertexBuilder.createSolidCylinder(new Geometry.Cylinder(new Geometry.Point3(0.0F, 0.0F, 0.0F), radius, height), numPointsAroundPuck);
/* 32 */     this.radius = radius;
/* 33 */     this.height = height;
/*    */     
/* 35 */     this.vertexArray = new VertexArray(generatedData.vertexData);
/* 36 */     this.drawList = generatedData.drawList;
/*    */   }
/*    */   
/*    */   public void bindData(PositionAttributeShader shader)
/*    */   {
/* 41 */     this.vertexArray.setVertexAttribPointer(0, shader.getPositionAttributeLocation(), 3, 0);
/*    */   }
/*    */   
/*    */   public void draw()
/*    */   {
/* 46 */     for (VertexBuilder.DrawCommand drawCommand : this.drawList)
/*    */     {
/* 48 */       drawCommand.draw();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\SolidCylinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */