/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.shaders;
/*    */ 
/*    */ import android.opengl.GLES20;
/*    */ import android.support.annotation.RawRes;
/*    */ import com.qualcomm.robotcore.R.raw;
/*    */ import org.firstinspires.ftc.robotcore.internal.opengl.models.MeshObject;
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
/*    */ public class CubeMeshVertexShader
/*    */   extends SimpleVertexShader
/*    */ {
/*    */   protected final int a_vertexNormal;
/*    */   protected final int a_vertexTexCoord;
/*    */   @RawRes
/* 47 */   public static final int resourceId = R.raw.cube_mesh_vertex_shader;
/*    */   
/*    */   public CubeMeshVertexShader(int programId)
/*    */   {
/* 51 */     super(programId);
/* 52 */     this.a_vertexNormal = GLES20.glGetAttribLocation(programId, "vertexNormal");
/* 53 */     this.a_vertexTexCoord = GLES20.glGetAttribLocation(programId, "vertexTexCoord");
/*    */   }
/*    */   
/*    */   public void setCoordinates(MeshObject meshObject)
/*    */   {
/* 58 */     GLES20.glVertexAttribPointer(this.a_vertexPosition, 3, 5126, false, 0, meshObject.getVertices());
/* 59 */     GLES20.glVertexAttribPointer(this.a_vertexNormal, 3, 5126, false, 0, meshObject.getNormals());
/* 60 */     GLES20.glVertexAttribPointer(this.a_vertexTexCoord, 2, 5126, false, 0, meshObject.getTexCoords());
/*    */     
/* 62 */     GLES20.glEnableVertexAttribArray(this.a_vertexPosition);
/* 63 */     GLES20.glEnableVertexAttribArray(this.a_vertexNormal);
/* 64 */     GLES20.glEnableVertexAttribArray(this.a_vertexTexCoord);
/*    */   }
/*    */   
/*    */   public void disableAttributes()
/*    */   {
/* 69 */     super.disableAttributes();
/* 70 */     GLES20.glDisableVertexAttribArray(this.a_vertexNormal);
/* 71 */     GLES20.glDisableVertexAttribArray(this.a_vertexTexCoord);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\CubeMeshVertexShader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */