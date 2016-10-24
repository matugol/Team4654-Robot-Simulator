/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.models;
/*    */ 
/*    */ import android.opengl.GLES20;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
/*    */ import java.nio.FloatBuffer;
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
/*    */ public class VertexArray
/*    */ {
/*    */   public static final int bytesPerFloat = 4;
/*    */   private final FloatBuffer vertexBuffer;
/*    */   
/*    */   public VertexArray(float[] vertexData)
/*    */   {
/* 27 */     this.vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setVertexAttribPointer(int dataOffset, int attributeLocation, int coordinatesPerVertex, int vertexStride)
/*    */   {
/* 36 */     this.vertexBuffer.position(dataOffset);
/* 37 */     GLES20.glVertexAttribPointer(attributeLocation, coordinatesPerVertex, 5126, false, vertexStride, this.vertexBuffer);
/* 38 */     GLES20.glEnableVertexAttribArray(attributeLocation);
/* 39 */     this.vertexBuffer.position(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\VertexArray.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */