/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.shaders;
/*    */ 
/*    */ import android.opengl.GLES20;
/*    */ import android.support.annotation.RawRes;
/*    */ import com.qualcomm.robotcore.R.raw;
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
/*    */ public class SimpleVertexShader
/*    */   implements PositionAttributeShader
/*    */ {
/*    */   protected final int a_vertexPosition;
/*    */   protected final int u_modelViewProjectionMatrix;
/*    */   @RawRes
/* 45 */   public static final int resourceId = R.raw.simple_vertex_shader;
/*    */   
/*    */   public SimpleVertexShader(int programId)
/*    */   {
/* 49 */     this.a_vertexPosition = GLES20.glGetAttribLocation(programId, "vertexPosition");
/* 50 */     this.u_modelViewProjectionMatrix = GLES20.glGetUniformLocation(programId, "modelViewProjectionMatrix");
/*    */   }
/*    */   
/*    */   public void setModelViewProjectionMatrix(float[] modelViewProjectionMatrix)
/*    */   {
/* 55 */     GLES20.glUniformMatrix4fv(this.u_modelViewProjectionMatrix, 1, false, modelViewProjectionMatrix, 0);
/*    */   }
/*    */   
/*    */   public int getPositionAttributeLocation()
/*    */   {
/* 60 */     return this.a_vertexPosition;
/*    */   }
/*    */   
/*    */   public void disableAttributes()
/*    */   {
/* 65 */     GLES20.glDisableVertexAttribArray(this.a_vertexPosition);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\SimpleVertexShader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */