/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.AssetManager;
/*     */ import android.os.Environment;
/*     */ import android.util.Log;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
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
/*     */ public class ExtractAssets
/*     */ {
/*  47 */   private static final String TAG = ExtractAssets.class.getSimpleName();
/*     */   
/*     */ 
/*     */ 
/*     */   public static ArrayList<String> ExtractToStorage(Context context, ArrayList<String> files, boolean useInternalStorage)
/*     */     throws IOException
/*     */   {
/*  54 */     if (!useInternalStorage) {
/*  55 */       String state = Environment.getExternalStorageState();
/*     */       
/*  57 */       if (!"mounted".equals(state)) {
/*  58 */         throw new IOException("External Storage not accessible");
/*     */       }
/*     */     }
/*     */     
/*  62 */     ArrayList<String> fileList = new ArrayList();
/*  63 */     for (String ipFile : files) {
/*  64 */       ExtractAndCopy(context, ipFile, useInternalStorage, fileList);
/*     */       
/*  66 */       if (fileList != null) {
/*  67 */         Log.d(TAG, "got " + fileList.size() + " elements");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  74 */     return fileList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ArrayList<String> ExtractAndCopy(Context context, String file, boolean useInternalStorage, ArrayList<String> retList)
/*     */   {
/*  83 */     Log.d(TAG, "Extracting assests for " + file);
/*     */     
/*  85 */     String[] fileList = null;
/*     */     
/*  87 */     AssetManager am = context.getAssets();
/*     */     try
/*     */     {
/*  90 */       fileList = am.list(file);
/*     */     } catch (IOException e1) {
/*  92 */       e1.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  97 */     InputStream ipStream = null;
/*  98 */     FileOutputStream outStream = null;
/*     */     
/* 100 */     if (fileList.length == 0) {
/*     */       try
/*     */       {
/* 103 */         ipStream = am.open(file);
/* 104 */         Log.d(TAG, "File: " + file + " opened for streaming");
/*     */         
/*     */ 
/* 107 */         if (!file.startsWith(File.separator))
/*     */         {
/* 109 */           file = File.separator + file;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 114 */         File fileSystemPath = null;
/* 115 */         if (useInternalStorage)
/*     */         {
/* 117 */           fileSystemPath = context.getFilesDir();
/*     */         }
/*     */         else {
/* 120 */           fileSystemPath = context.getExternalFilesDir(null);
/*     */         }
/*     */         
/* 123 */         String path = fileSystemPath.getPath();
/* 124 */         String outFile = path.concat(file);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 129 */         if ((retList != null) && (retList.contains(outFile))) {
/* 130 */           Log.e(TAG, "Ignoring Duplicate entry for " + outFile);
/* 131 */           return retList;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 137 */         int dirPathEnd = outFile.lastIndexOf(File.separatorChar);
/* 138 */         String dirName = outFile.substring(0, dirPathEnd);
/* 139 */         String fileName = outFile.substring(dirPathEnd, outFile.length());
/*     */         
/* 141 */         File fp = new File(dirName);
/*     */         
/* 143 */         if (fp.mkdirs()) {
/* 144 */           Log.d(TAG, "Dir created " + dirName);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */         File outFileHandle = new File(fp, fileName);
/* 152 */         outStream = new FileOutputStream(outFileHandle);
/*     */         
/* 154 */         if (outStream != null)
/*     */         {
/* 156 */           byte[] readBuf = new byte['Ѐ'];
/*     */           
/* 158 */           int bytesRead = 0;
/*     */           
/* 160 */           while ((bytesRead = ipStream.read(readBuf)) != -1) {
/* 161 */             outStream.write(readBuf, 0, bytesRead);
/*     */           }
/*     */         }
/* 164 */         outStream.close();
/*     */         
/* 166 */         if (retList != null) {
/* 167 */           retList.add(outFile);
/*     */         }
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
/* 190 */         return retList;
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 171 */         Log.d(TAG, "File: " + file + " doesn't exist");
/*     */       } finally {
/* 173 */         if (ipStream != null) {
/*     */           try {
/* 175 */             ipStream.close();
/*     */           } catch (IOException e) {
/* 177 */             Log.d(TAG, "Unable to close in stream");
/* 178 */             e.printStackTrace();
/*     */           }
/* 180 */           if (outStream != null) {
/*     */             try {
/* 182 */               outStream.close();
/*     */             } catch (IOException e) {
/* 184 */               Log.d(TAG, "Unable to close out stream");
/* 185 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 197 */     String ipFilePath = file;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 202 */     if ((!file.equals("")) && (!file.endsWith(File.separator)))
/*     */     {
/* 204 */       ipFilePath = ipFilePath.concat(File.separator);
/*     */     }
/*     */     
/* 207 */     for (String ipFile : fileList) {
/* 208 */       String ipFileName = ipFilePath.concat(ipFile);
/* 209 */       ExtractAndCopy(context, ipFileName, useInternalStorage, retList);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 214 */     return retList;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\ExtractAssets.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */