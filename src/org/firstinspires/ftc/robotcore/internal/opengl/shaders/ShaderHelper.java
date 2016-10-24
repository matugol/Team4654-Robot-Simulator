/*     */ package org.firstinspires.ftc.robotcore.internal.opengl.shaders;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import android.util.Log;
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
/*     */ public class ShaderHelper
/*     */ {
/*     */   public static final String TAG = "ShaderHelper";
/*     */   
/*     */   public static int compileVertexShader(String shaderCode)
/*     */   {
/*  41 */     return compileShader(35633, shaderCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int compileFragmentShader(String shaderCode)
/*     */   {
/*  49 */     return compileShader(35632, shaderCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int compileShader(int type, String shaderCode)
/*     */   {
/*  58 */     int shaderObjectId = GLES20.glCreateShader(type);
/*     */     
/*  60 */     if (shaderObjectId == 0)
/*     */     {
/*  62 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*  66 */     GLES20.glShaderSource(shaderObjectId, shaderCode);
/*     */     
/*     */ 
/*  69 */     GLES20.glCompileShader(shaderObjectId);
/*     */     
/*     */ 
/*  72 */     int[] compileStatus = new int[1];
/*  73 */     GLES20.glGetShaderiv(shaderObjectId, 35713, compileStatus, 0);
/*     */     
/*     */ 
/*  76 */     if (compileStatus[0] == 0)
/*     */     {
/*     */ 
/*  79 */       GLES20.glDeleteShader(shaderObjectId);
/*  80 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*  84 */     return shaderObjectId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int linkProgram(int vertexShaderId, int fragmentShaderId)
/*     */   {
/*  94 */     int programObjectId = GLES20.glCreateProgram();
/*     */     
/*  96 */     if (programObjectId == 0)
/*     */     {
/*  98 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 102 */     GLES20.glAttachShader(programObjectId, vertexShaderId);
/*     */     
/*     */ 
/* 105 */     GLES20.glAttachShader(programObjectId, fragmentShaderId);
/*     */     
/*     */ 
/* 108 */     GLES20.glLinkProgram(programObjectId);
/*     */     
/*     */ 
/* 111 */     int[] linkStatus = new int[1];
/* 112 */     GLES20.glGetProgramiv(programObjectId, 35714, linkStatus, 0);
/*     */     
/*     */ 
/* 115 */     if (linkStatus[0] == 0)
/*     */     {
/*     */ 
/* 118 */       GLES20.glDeleteProgram(programObjectId);
/*     */       
/* 120 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 124 */     return programObjectId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean validateProgram(int programObjectId)
/*     */   {
/* 133 */     GLES20.glValidateProgram(programObjectId);
/* 134 */     int[] validateStatus = new int[1];
/* 135 */     GLES20.glGetProgramiv(programObjectId, 35715, validateStatus, 0);
/* 136 */     Log.v("ShaderHelper", "Results of validating program: " + validateStatus[0] + "\nLog:" + GLES20.glGetProgramInfoLog(programObjectId));
/* 137 */     return validateStatus[0] != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int buildProgram(String vertexShaderSource, String fragmentShaderSource)
/*     */   {
/* 149 */     int vertexShader = compileVertexShader(vertexShaderSource);
/* 150 */     int fragmentShader = compileFragmentShader(fragmentShaderSource);
/*     */     
/*     */ 
/* 153 */     int program = linkProgram(vertexShader, fragmentShader);
/*     */     
/* 155 */     return program;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\ShaderHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */