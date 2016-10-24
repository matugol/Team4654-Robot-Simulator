/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.shaders;
/*    */ 
/*    */ import android.content.Context;
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
/*    */ public class SimpleColorProgram
/*    */   extends ShaderProgram
/*    */ {
/*    */   public final SimpleVertexShader vertex;
/*    */   public final ColorFragmentShader fragment;
/*    */   
/*    */   public SimpleColorProgram(Context context)
/*    */   {
/* 44 */     super(context, SimpleVertexShader.resourceId, ColorFragmentShader.resourceId);
/* 45 */     this.vertex = new SimpleVertexShader(this.program);
/* 46 */     this.fragment = new ColorFragmentShader(this.program);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\SimpleColorProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */