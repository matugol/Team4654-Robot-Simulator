/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.util.Log;
/*     */ import dalvik.system.DexFile;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassManager
/*     */ {
/*     */   private static final String LOGGING_TAG = "ClassManager";
/*     */   private List<Class> allClasses;
/*     */   private List<String> packagesAndClassesToIgnore;
/*     */   private List<ClassFilter> filters;
/*     */   private Context context;
/*     */   private DexFile dexFile;
/*     */   
/*     */   public ClassManager()
/*     */     throws IOException
/*     */   {
/*  71 */     this.packagesAndClassesToIgnore = new LinkedList();
/*  72 */     this.packagesAndClassesToIgnore.add("com.google");
/*  73 */     this.packagesAndClassesToIgnore.add("io.netty");
/*     */     
/*  75 */     this.context = AppUtil.getInstance().getApplication();
/*  76 */     this.dexFile = new DexFile(this.context.getPackageCodePath());
/*     */     
/*  78 */     this.filters = new LinkedList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerFilter(ClassFilter filter)
/*     */   {
/*  88 */     this.filters.add(filter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<Class> findAllClasses()
/*     */   {
/*  97 */     List<Class> result = new LinkedList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     LinkedList<String> classNames = new LinkedList(Collections.list(this.dexFile.entries()));
/* 104 */     List<String> instantRunClassNames = InstantRunDexHelper.getAllClasses(this.context);
/* 105 */     classNames.addAll(instantRunClassNames);
/*     */     
/* 107 */     for (String className : classNames)
/*     */     {
/*     */ 
/* 110 */       boolean shouldIgnore = false;
/* 111 */       for (String packageName : this.packagesAndClassesToIgnore)
/*     */       {
/* 113 */         if (Util.isPrefixOf(packageName, className))
/*     */         {
/* 115 */           shouldIgnore = true;
/* 116 */           break;
/*     */         }
/*     */       }
/* 119 */       if (!shouldIgnore)
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/* 125 */           clazz = Class.forName(className, false, this.context.getClassLoader());
/*     */         }
/*     */         catch (NoClassDefFoundError|ClassNotFoundException ex)
/*     */         {
/*     */           Class clazz;
/* 130 */           if (logClassNotFound(className)) Log.w("ClassManager", className + " " + ex.toString(), ex);
/* 131 */           if (className.contains("$"))
/*     */           {
/*     */ 
/* 134 */             className = className.substring(0, className.indexOf("$"));
/*     */           }
/*     */           
/* 137 */           this.packagesAndClassesToIgnore.add(className); }
/* 138 */         continue;
/*     */         
/*     */         Class clazz;
/*     */         
/* 142 */         result.add(clazz);
/*     */       }
/*     */     }
/* 145 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean logClassNotFound(String className)
/*     */   {
/* 150 */     return !className.startsWith("com.vuforia.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processAllClasses()
/*     */   {
/* 158 */     this.allClasses = findAllClasses();
/*     */     
/* 160 */     for (Iterator i$ = this.allClasses.iterator(); i$.hasNext();) { clazz = (Class)i$.next();
/* 161 */       for (ClassFilter f : this.filters) {
/* 162 */         f.filter(clazz);
/*     */       }
/*     */     }
/*     */     Class clazz;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\ClassManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */