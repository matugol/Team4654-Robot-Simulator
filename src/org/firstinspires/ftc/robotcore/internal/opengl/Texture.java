/*    */ package org.firstinspires.ftc.robotcore.internal.opengl;
/*    */ 
/*    */ import android.content.res.AssetManager;
/*    */ import android.graphics.Bitmap;
/*    */ import android.graphics.BitmapFactory;
/*    */ import android.util.Log;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Texture
/*    */ {
/*    */   private static final String LOGTAG = "Vuforia_Texture";
/*    */   public int mWidth;
/*    */   public int mHeight;
/*    */   public int mChannels;
/*    */   public ByteBuffer mData;
/* 32 */   public int[] mTextureID = new int[1];
/* 33 */   public boolean mSuccess = false;
/*    */   
/*    */ 
/*    */   public static Texture loadTextureFromApk(String fileName, AssetManager assets)
/*    */   {
/* 38 */     InputStream inputStream = null;
/*    */     try
/*    */     {
/* 41 */       inputStream = assets.open(fileName, 3);
/*    */       
/* 43 */       BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
/* 44 */       Bitmap bitMap = BitmapFactory.decodeStream(bufferedStream);
/*    */       
/* 46 */       int[] data = new int[bitMap.getWidth() * bitMap.getHeight()];
/* 47 */       bitMap.getPixels(data, 0, bitMap.getWidth(), 0, 0, bitMap.getWidth(), bitMap.getHeight());
/*    */       
/*    */ 
/* 50 */       return loadTextureFromIntBuffer(data, bitMap.getWidth(), bitMap.getHeight());
/*    */ 
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 55 */       Log.e("Vuforia_Texture", "Failed to log texture '" + fileName + "' from APK");
/* 56 */       Log.i("Vuforia_Texture", e.getMessage()); }
/* 57 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Texture loadTextureFromIntBuffer(int[] data, int width, int height)
/*    */   {
/* 65 */     int numPixels = width * height;
/* 66 */     byte[] dataBytes = new byte[numPixels * 4];
/*    */     
/* 68 */     for (int p = 0; p < numPixels; p++)
/*    */     {
/* 70 */       int colour = data[p];
/* 71 */       dataBytes[(p * 4)] = ((byte)(colour >>> 16));
/* 72 */       dataBytes[(p * 4 + 1)] = ((byte)(colour >>> 8));
/* 73 */       dataBytes[(p * 4 + 2)] = ((byte)colour);
/* 74 */       dataBytes[(p * 4 + 3)] = ((byte)(colour >>> 24));
/*    */     }
/*    */     
/* 77 */     Texture texture = new Texture();
/* 78 */     texture.mWidth = width;
/* 79 */     texture.mHeight = height;
/* 80 */     texture.mChannels = 4;
/*    */     
/* 82 */     texture.mData = ByteBuffer.allocateDirect(dataBytes.length).order(ByteOrder.nativeOrder());
/* 83 */     int rowSize = texture.mWidth * texture.mChannels;
/* 84 */     for (int r = 0; r < texture.mHeight; r++)
/*    */     {
/* 86 */       texture.mData.put(dataBytes, rowSize * (texture.mHeight - 1 - r), rowSize);
/*    */     }
/*    */     
/* 89 */     texture.mData.rewind();
/*    */     
/*    */ 
/* 92 */     dataBytes = null;
/* 93 */     data = null;
/*    */     
/* 95 */     texture.mSuccess = true;
/* 96 */     return texture;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\Texture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */