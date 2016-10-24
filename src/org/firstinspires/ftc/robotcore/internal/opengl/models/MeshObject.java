/*    */ package org.firstinspires.ftc.robotcore.internal.opengl.models;
/*    */ 
/*    */ import java.nio.Buffer;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
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
/*    */ public abstract class MeshObject
/*    */ {
/*    */   public static enum BUFFER_TYPE
/*    */   {
/* 20 */     BUFFER_TYPE_VERTEX,  BUFFER_TYPE_TEXTURE_COORD,  BUFFER_TYPE_NORMALS,  BUFFER_TYPE_INDICES;
/*    */     
/*    */     private BUFFER_TYPE() {}
/*    */   }
/*    */   
/* 25 */   public Buffer getVertices() { return getBuffer(BUFFER_TYPE.BUFFER_TYPE_VERTEX); }
/*    */   
/*    */ 
/*    */   public Buffer getTexCoords()
/*    */   {
/* 30 */     return getBuffer(BUFFER_TYPE.BUFFER_TYPE_TEXTURE_COORD);
/*    */   }
/*    */   
/*    */   public Buffer getNormals()
/*    */   {
/* 35 */     return getBuffer(BUFFER_TYPE.BUFFER_TYPE_NORMALS);
/*    */   }
/*    */   
/*    */   public Buffer getIndices()
/*    */   {
/* 40 */     return getBuffer(BUFFER_TYPE.BUFFER_TYPE_INDICES);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Buffer fillBuffer(double[] array)
/*    */   {
/* 48 */     ByteBuffer bb = ByteBuffer.allocateDirect(4 * array.length);
/* 49 */     bb.order(ByteOrder.LITTLE_ENDIAN);
/* 50 */     for (double d : array)
/* 51 */       bb.putFloat((float)d);
/* 52 */     bb.rewind();
/*    */     
/* 54 */     return bb;
/*    */   }
/*    */   
/*    */ 
/*    */   protected Buffer fillBuffer(float[] array)
/*    */   {
/* 60 */     ByteBuffer bb = ByteBuffer.allocateDirect(4 * array.length);
/* 61 */     bb.order(ByteOrder.LITTLE_ENDIAN);
/* 62 */     for (float d : array)
/* 63 */       bb.putFloat(d);
/* 64 */     bb.rewind();
/*    */     
/* 66 */     return bb;
/*    */   }
/*    */   
/*    */ 
/*    */   protected Buffer fillBuffer(short[] array)
/*    */   {
/* 72 */     ByteBuffer bb = ByteBuffer.allocateDirect(2 * array.length);
/* 73 */     bb.order(ByteOrder.LITTLE_ENDIAN);
/* 74 */     for (short s : array)
/* 75 */       bb.putShort(s);
/* 76 */     bb.rewind();
/*    */     
/* 78 */     return bb;
/*    */   }
/*    */   
/*    */   public abstract Buffer getBuffer(BUFFER_TYPE paramBUFFER_TYPE);
/*    */   
/*    */   public abstract int getNumObjectVertex();
/*    */   
/*    */   public abstract int getNumObjectIndex();
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\MeshObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */