/*    */ package org.firstinspires.ftc.robotcore.internal.opengl;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.content.res.Resources;
/*    */ import android.content.res.Resources.NotFoundException;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
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
/*    */ public class TextResourceReader
/*    */ {
/*    */   public static String readTextFileFromResource(Context context, int resourceId)
/*    */   {
/* 27 */     StringBuilder body = new StringBuilder();
/*    */     
/*    */     try
/*    */     {
/* 31 */       InputStream inputStream = context.getResources().openRawResource(resourceId);
/* 32 */       InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
/* 33 */       BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
/*    */       
/*    */       String nextLine;
/*    */       
/* 37 */       while ((nextLine = bufferedReader.readLine()) != null)
/*    */       {
/* 39 */         body.append(nextLine);
/* 40 */         body.append('\n');
/*    */       }
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 45 */       throw new RuntimeException("Could not open resource: " + resourceId, e);
/*    */     }
/*    */     catch (Resources.NotFoundException nfe)
/*    */     {
/* 49 */       throw new RuntimeException("Resource not found: " + resourceId, nfe);
/*    */     }
/*    */     
/* 52 */     return body.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\TextResourceReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */