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
/*    */ public class CubeMeshProgram
/*    */   extends ShaderProgram
/*    */ {
/*    */   public final CubeMeshVertexShader vertex;
/*    */   public final CubeMeshFragmentShader fragment;
/*    */   
/*    */   public CubeMeshProgram(Context context)
/*    */   {
/* 44 */     super(context, CubeMeshVertexShader.resourceId, CubeMeshFragmentShader.resourceId);
/* 45 */     this.vertex = new CubeMeshVertexShader(this.program);
/* 46 */     this.fragment = new CubeMeshFragmentShader(this.program);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\CubeMeshProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */