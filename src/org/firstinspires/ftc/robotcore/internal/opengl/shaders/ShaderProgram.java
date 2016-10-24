/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.shaders;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.opengl.GLES20;
/*    */ import org.firstinspires.ftc.robotcore.internal.opengl.TextResourceReader;
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
/*    */ public abstract class ShaderProgram
/*    */ {
/*    */   protected final int program;
/*    */   
/*    */   protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId)
/*    */   {
/* 26 */     this.program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId), TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void useProgram()
/*    */   {
/* 34 */     GLES20.glUseProgram(this.program);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\ShaderProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */