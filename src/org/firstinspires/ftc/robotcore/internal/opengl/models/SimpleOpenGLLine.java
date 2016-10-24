/*     */ package org.firstinspires.ftc.robotcore.internal.opengl.models;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.FloatBuffer;
/*     */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.ColorFragmentShader;
/*     */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.SimpleColorProgram;
/*     */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.SimpleVertexShader;
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
/*     */ public class SimpleOpenGLLine
/*     */ {
/*     */   protected FloatBuffer vertexBuffer;
/*     */   protected static final int coordinatesPerVertex = 3;
/*  58 */   protected static final float[] defaultCoordinates = { 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */   protected final int vertexCount = defaultCoordinates.length / 3;
/*  65 */   protected final int vertexStride = 12;
/*     */   
/*     */ 
/*  68 */   protected float[] color = { 0.0F, 0.0F, 0.0F, 1.0F };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleOpenGLLine()
/*     */   {
/*  77 */     ByteBuffer bb = ByteBuffer.allocateDirect(this.vertexCount * 12);
/*     */     
/*     */ 
/*  80 */     bb.order(ByteOrder.nativeOrder());
/*  81 */     this.vertexBuffer = bb.asFloatBuffer();
/*     */     
/*  83 */     this.vertexBuffer.put(defaultCoordinates);
/*  84 */     this.vertexBuffer.position(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVerts(float v0, float v1, float v2, float v3, float v4, float v5)
/*     */   {
/*  93 */     float[] coords = new float[this.vertexCount * 3];
/*     */     
/*  95 */     coords[0] = v0;
/*  96 */     coords[1] = v1;
/*  97 */     coords[2] = v2;
/*  98 */     coords[3] = v3;
/*  99 */     coords[4] = v4;
/* 100 */     coords[5] = v5;
/*     */     
/* 102 */     this.vertexBuffer.put(coords);
/* 103 */     this.vertexBuffer.position(0);
/*     */   }
/*     */   
/*     */   public void setColor(float red, float green, float blue, float alpha)
/*     */   {
/* 108 */     this.color[0] = red;
/* 109 */     this.color[1] = green;
/* 110 */     this.color[2] = blue;
/* 111 */     this.color[3] = alpha;
/*     */   }
/*     */   
/*     */   public void draw(float[] modelViewProjectionMatrix, SimpleColorProgram program)
/*     */   {
/* 116 */     program.useProgram();
/* 117 */     program.fragment.setColor(this.color[0], this.color[1], this.color[2], this.color[3]);
/*     */     
/*     */ 
/* 120 */     int attributeLocation = program.vertex.getPositionAttributeLocation();
/* 121 */     GLES20.glVertexAttribPointer(attributeLocation, 3, 5126, false, 12, this.vertexBuffer);
/* 122 */     GLES20.glEnableVertexAttribArray(attributeLocation);
/*     */     
/* 124 */     program.vertex.setModelViewProjectionMatrix(modelViewProjectionMatrix);
/* 125 */     GLES20.glDrawArrays(1, 0, this.vertexCount);
/* 126 */     program.vertex.disableAttributes();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\SimpleOpenGLLine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */