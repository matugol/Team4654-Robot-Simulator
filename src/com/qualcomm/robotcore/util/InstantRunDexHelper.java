/*    */ package com.qualcomm.robotcore.util;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.content.pm.ApplicationInfo;
/*    */ import android.content.pm.PackageManager;
/*    */ import android.content.pm.PackageManager.NameNotFoundException;
/*    */ import dalvik.system.DexFile;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public class InstantRunDexHelper
/*    */ {
/* 47 */   private static final String INSTANT_RUN_LOCATION = "files" + File.separator + "instant-run" + File.separator + "dex";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static List<String> getAllClasses(Context context)
/*    */   {
/* 57 */     ApplicationInfo applicationInfo = null;
/* 58 */     List<String> classNames = new ArrayList();
/*    */     try
/*    */     {
/* 61 */       applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
/*    */     } catch (PackageManager.NameNotFoundException e) {
/* 63 */       RobotLog.e("Could not obtain application info for class scanning");
/* 64 */       e.printStackTrace();
/* 65 */       return classNames;
/*    */     }
/*    */     
/* 68 */     File[] instantRunDexes = new File(applicationInfo.dataDir, INSTANT_RUN_LOCATION).listFiles();
/* 69 */     if (instantRunDexes != null) {
/* 70 */       for (File f : instantRunDexes)
/*    */       {
/*    */ 
/*    */         try
/*    */         {
/*    */ 
/* 76 */           DexFile dexFile = new DexFile(f.getAbsolutePath());
/* 77 */           classNames.addAll(Collections.list(dexFile.entries()));
/*    */ 
/*    */         }
/*    */         catch (IOException e)
/*    */         {
/* 82 */           RobotLog.e("Error loading dex file: " + f.toString() + ", IOException: " + e.toString());
/*    */         }
/*    */       }
/*    */     }
/* 86 */     return classNames;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\InstantRunDexHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */