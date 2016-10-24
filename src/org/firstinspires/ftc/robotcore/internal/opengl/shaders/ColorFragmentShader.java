/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.shaders;
/*    */ 
/*    */ import android.graphics.Color;
/*    */ import android.opengl.GLES20;
/*    */ import android.support.annotation.ColorInt;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ColorFragmentShader
/*    */ {
/*    */   protected final int u_Color;
/*    */   @RawRes
/* 49 */   public static final int resourceId = R.raw.color_fragment_shader;
/*    */   
/*    */   public ColorFragmentShader(int programId)
/*    */   {
/* 53 */     this.u_Color = GLES20.glGetUniformLocation(programId, "u_Color");
/*    */   }
/*    */   
/*    */   public void setColor(float r, float g, float b, float a)
/*    */   {
/* 58 */     GLES20.glUniform4f(this.u_Color, r, g, b, a);
/*    */   }
/*    */   
/*    */   public void setColor(float r, float g, float b) {
/* 62 */     setColor(r, g, b, 1.0F);
/*    */   }
/*    */   
/*    */   public void setColor(@ColorInt int color) {
/* 66 */     setColor(rescale(Color.red(color)), rescale(Color.green(color)), rescale(Color.blue(color)), rescale(Color.alpha(color)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static float rescale(int colorComponent)
/*    */   {
/* 74 */     return colorComponent / 255.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\ColorFragmentShader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */