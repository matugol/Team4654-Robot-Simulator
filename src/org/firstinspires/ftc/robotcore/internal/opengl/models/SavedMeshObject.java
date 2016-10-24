/*     */ package org.firstinspires.ftc.robotcore.internal.opengl.models;
/*     */ 
/*     */ import android.content.res.AssetManager;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ public class SavedMeshObject
/*     */   extends MeshObject
/*     */ {
/*     */   private ByteBuffer verts;
/*     */   private ByteBuffer textCoords;
/*     */   private ByteBuffer norms;
/*  61 */   int numVerts = 0;
/*     */   
/*     */   public void loadModel(AssetManager assetManager, String filename) throws IOException
/*     */   {
/*  65 */     InputStream is = null;
/*     */     try
/*     */     {
/*  68 */       is = assetManager.open(filename);
/*  69 */       BufferedReader reader = new BufferedReader(new InputStreamReader(is));
/*     */       
/*  71 */       String line = reader.readLine();
/*     */       
/*  73 */       int floatsToRead = Integer.parseInt(line);
/*  74 */       this.numVerts = (floatsToRead / 3);
/*     */       
/*  76 */       this.verts = ByteBuffer.allocateDirect(floatsToRead * 4);
/*  77 */       this.verts.order(ByteOrder.nativeOrder());
/*  78 */       for (int i = 0; i < floatsToRead; i++)
/*     */       {
/*  80 */         this.verts.putFloat(Float.parseFloat(reader.readLine()));
/*     */       }
/*  82 */       this.verts.rewind();
/*     */       
/*  84 */       line = reader.readLine();
/*  85 */       floatsToRead = Integer.parseInt(line);
/*     */       
/*  87 */       this.norms = ByteBuffer.allocateDirect(floatsToRead * 4);
/*  88 */       this.norms.order(ByteOrder.nativeOrder());
/*  89 */       for (int i = 0; i < floatsToRead; i++)
/*     */       {
/*  91 */         this.norms.putFloat(Float.parseFloat(reader.readLine()));
/*     */       }
/*  93 */       this.norms.rewind();
/*     */       
/*  95 */       line = reader.readLine();
/*  96 */       floatsToRead = Integer.parseInt(line);
/*     */       
/*  98 */       this.textCoords = ByteBuffer.allocateDirect(floatsToRead * 4);
/*  99 */       this.textCoords.order(ByteOrder.nativeOrder());
/* 100 */       for (int i = 0; i < floatsToRead; i++)
/*     */       {
/* 102 */         this.textCoords.putFloat(Float.parseFloat(reader.readLine()));
/*     */       }
/* 104 */       this.textCoords.rewind();
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 109 */       if (is != null) {
/* 110 */         is.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Buffer getBuffer(MeshObject.BUFFER_TYPE bufferType)
/*     */   {
/* 117 */     Buffer result = null;
/* 118 */     switch (bufferType)
/*     */     {
/*     */     case BUFFER_TYPE_VERTEX: 
/* 121 */       result = this.verts;
/* 122 */       break;
/*     */     case BUFFER_TYPE_TEXTURE_COORD: 
/* 124 */       result = this.textCoords;
/* 125 */       break;
/*     */     case BUFFER_TYPE_NORMALS: 
/* 127 */       result = this.norms;
/*     */     }
/*     */     
/*     */     
/* 131 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getNumObjectVertex()
/*     */   {
/* 137 */     return this.numVerts;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getNumObjectIndex()
/*     */   {
/* 143 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\SavedMeshObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */