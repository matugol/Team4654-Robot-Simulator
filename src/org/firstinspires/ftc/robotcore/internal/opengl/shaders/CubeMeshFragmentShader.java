/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.shaders;
/*    */ 
/*    */ import android.opengl.GLES20;
/*    */ import android.support.annotation.RawRes;
/*    */ import com.qualcomm.robotcore.R.raw;
/*    */ import org.firstinspires.ftc.robotcore.internal.opengl.Texture;
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
/*    */ public class CubeMeshFragmentShader
/*    */ {
/*    */   protected final int u_texSampler2DHandle;
/*    */   @RawRes
/* 46 */   public static final int resourceId = R.raw.cube_mesh_fragment_shader;
/*    */   
/*    */   public CubeMeshFragmentShader(int programId)
/*    */   {
/* 50 */     this.u_texSampler2DHandle = GLES20.glGetUniformLocation(programId, "texSampler2D");
/*    */   }
/*    */   
/*    */   public void setTexture(Texture texture)
/*    */   {
/* 55 */     GLES20.glActiveTexture(33984);
/* 56 */     GLES20.glBindTexture(3553, texture.mTextureID[0]);
/* 57 */     GLES20.glUniform1i(this.u_texSampler2DHandle, 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\CubeMeshFragmentShader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */