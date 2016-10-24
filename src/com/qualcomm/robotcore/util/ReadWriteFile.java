/*    */ package com.qualcomm.robotcore.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ReadWriteFile
/*    */ {
/*    */   public static String readFileOrThrow(File file) throws IOException
/*    */   {
/* 12 */     java.io.FileReader reader = new java.io.FileReader(file);
/* 13 */     StringBuilder content = new StringBuilder();
/* 14 */     BufferedReader bufferedReader = new BufferedReader(reader);
/*    */     String line;
/* 16 */     while ((line = bufferedReader.readLine()) != null) {
/* 17 */       content.append(line);
/*    */     }
/* 19 */     return content.toString();
/*    */   }
/*    */   
/*    */   public static String readFile(File file) {
/*    */     try {
/* 24 */       return readFileOrThrow(file);
/*    */     } catch (IOException e) {
/* 26 */       RobotLog.e("Error reading file: " + e.getMessage());
/*    */     }
/* 28 */     return "";
/*    */   }
/*    */   
/*    */   public static void writeFile(File file, String fileContents) {
/* 32 */     writeFile(file.getParentFile(), file.getName(), fileContents);
/*    */   }
/*    */   
/*    */   public static void writeFile(File directory, String fileName, String fileContents)
/*    */   {
/*    */     try {
/* 38 */       if (!directory.exists()) {
/* 39 */         directory.mkdirs();
/*    */       }
/* 41 */       File file = new File(directory, fileName);
/* 42 */       FileWriter writer = new FileWriter(file);
/* 43 */       writer.append(fileContents);
/* 44 */       writer.flush();
/* 45 */       writer.close();
/*    */     }
/*    */     catch (IOException e) {
/* 48 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\ReadWriteFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */