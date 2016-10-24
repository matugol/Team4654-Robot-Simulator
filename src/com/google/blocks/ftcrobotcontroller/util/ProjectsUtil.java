/*     */ package com.google.blocks.ftcrobotcontroller.util;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
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
/*     */ public class ProjectsUtil
/*     */ {
/*  28 */   private static final File BLOCKS_DIR = new File(AppUtil.FIRST_FOLDER, "blocks");
/*     */   
/*     */ 
/*     */   private static final String BLK_EXT = ".blk";
/*     */   
/*     */   private static final String JS_EXT = ".js";
/*     */   
/*     */   private static final String VALID_PROJECT_REGEX = "^[a-zA-Z0-9]+$";
/*     */   
/*     */ 
/*     */   public static String fetchProjectsWithBlocks()
/*     */     throws IOException
/*     */   {
/*  41 */     File[] files = BLOCKS_DIR.listFiles(new FilenameFilter()
/*     */     {
/*     */       public boolean accept(File dir, String filename) {
/*  44 */         return filename.endsWith(".blk");
/*     */       }
/*     */     });
/*  47 */     if (files != null) {
/*  48 */       StringBuilder jsonProjects = new StringBuilder();
/*  49 */       jsonProjects.append("[");
/*  50 */       String delimiter = "";
/*  51 */       for (int i = 0; i < files.length; i++) {
/*  52 */         String name = files[i].getName();
/*     */         
/*  54 */         name = name.substring(0, name.length() - ".blk".length());
/*  55 */         jsonProjects.append(delimiter).append("{\"name\":\"").append(name).append("\", \"dateModifiedMillis\":").append(files[i].lastModified()).append("}");
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */         delimiter = ",";
/*     */       }
/*  63 */       jsonProjects.append("]");
/*  64 */       return jsonProjects.toString();
/*     */     }
/*  66 */     return "[]";
/*     */   }
/*     */   
/*     */ 
/*     */   public static String[] fetchProjectsWithJavaScript()
/*     */     throws IOException
/*     */   {
/*  73 */     String[] files = BLOCKS_DIR.list(new FilenameFilter()
/*     */     {
/*     */       public boolean accept(File dir, String filename) {
/*  76 */         return filename.endsWith(".js");
/*     */       }
/*     */     });
/*  79 */     if (files != null) {
/*  80 */       String[] projects = new String[files.length];
/*  81 */       for (int i = 0; i < files.length; i++) {
/*  82 */         projects[i] = files[i].substring(0, files[i].length() - ".js".length());
/*     */       }
/*  84 */       return projects;
/*     */     }
/*  86 */     return new String[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isValidProjectName(String projectName)
/*     */   {
/*  94 */     if (projectName != null) {
/*  95 */       return projectName.matches("^[a-zA-Z0-9]+$");
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String fetchBlocks(String projectName, Activity activity)
/*     */     throws IOException
/*     */   {
/* 106 */     if (!isValidProjectName(projectName)) {
/* 107 */       throw new IllegalArgumentException();
/*     */     }
/* 109 */     String blkContent = readFile(new File(BLOCKS_DIR, projectName + ".blk"));
/* 110 */     blkContent = HardwareUtil.upgradeBlocks(blkContent, activity);
/* 111 */     return blkContent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String fetchJavaScript(String projectName)
/*     */     throws IOException
/*     */   {
/* 120 */     if (!isValidProjectName(projectName)) {
/* 121 */       throw new IllegalArgumentException();
/*     */     }
/* 123 */     return readFile(new File(BLOCKS_DIR, projectName + ".js"));
/*     */   }
/*     */   
/*     */ 
/*     */   private static String readFile(File file)
/*     */     throws IOException
/*     */   {
/* 130 */     StringBuilder sb = new StringBuilder();
/* 131 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/*     */     try {
/* 133 */       String line = null;
/* 134 */       while ((line = reader.readLine()) != null) {
/* 135 */         sb.append(line).append("\n");
/*     */       }
/*     */     } finally {
/* 138 */       reader.close();
/*     */     }
/* 140 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void saveProject(String projectName, String blkContent, String jsContent)
/*     */     throws IOException
/*     */   {
/* 152 */     if (!isValidProjectName(projectName)) {
/* 153 */       throw new IllegalArgumentException();
/*     */     }
/* 155 */     if (!BLOCKS_DIR.exists()) {
/* 156 */       BLOCKS_DIR.mkdirs();
/*     */     }
/* 158 */     writeFile(new File(BLOCKS_DIR, projectName + ".blk"), blkContent);
/* 159 */     writeFile(new File(BLOCKS_DIR, projectName + ".js"), jsContent);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void writeFile(File file, String content)
/*     */     throws IOException
/*     */   {
/* 166 */     BufferedWriter writer = new BufferedWriter(new FileWriter(file));
/*     */     try {
/* 168 */       writer.write(content);
/*     */     } finally {
/* 170 */       writer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void renameProject(String oldProjectName, String newProjectName)
/*     */     throws IOException
/*     */   {
/* 182 */     if ((!isValidProjectName(oldProjectName)) || (!isValidProjectName(newProjectName))) {
/* 183 */       throw new IllegalArgumentException();
/*     */     }
/* 185 */     if (!BLOCKS_DIR.exists()) {
/* 186 */       BLOCKS_DIR.mkdirs();
/*     */     }
/* 188 */     File oldBlk = new File(BLOCKS_DIR, oldProjectName + ".blk");
/* 189 */     File newBlk = new File(BLOCKS_DIR, newProjectName + ".blk");
/* 190 */     if (oldBlk.renameTo(newBlk)) {
/* 191 */       File oldJs = new File(BLOCKS_DIR, oldProjectName + ".js");
/* 192 */       File newJs = new File(BLOCKS_DIR, newProjectName + ".js");
/* 193 */       oldJs.renameTo(newJs);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void copyProject(String oldProjectName, String newProjectName)
/*     */     throws IOException
/*     */   {
/* 205 */     if ((!isValidProjectName(oldProjectName)) || (!isValidProjectName(newProjectName))) {
/* 206 */       throw new IllegalArgumentException();
/*     */     }
/* 208 */     if (!BLOCKS_DIR.exists()) {
/* 209 */       BLOCKS_DIR.mkdirs();
/*     */     }
/*     */     
/* 212 */     File oldBlk = new File(BLOCKS_DIR, oldProjectName + ".blk");
/* 213 */     File newBlk = new File(BLOCKS_DIR, newProjectName + ".blk");
/* 214 */     copyFile(oldBlk, newBlk);
/* 215 */     File oldJs = new File(BLOCKS_DIR, oldProjectName + ".js");
/* 216 */     File newJs = new File(BLOCKS_DIR, newProjectName + ".js");
/* 217 */     copyFile(oldJs, newJs);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void copyFile(File source, File dest)
/*     */     throws IOException
/*     */   {
/* 224 */     FileChannel sourceChannel = null;
/* 225 */     FileChannel destChannel = null;
/*     */     try {
/* 227 */       sourceChannel = new FileInputStream(source).getChannel();
/* 228 */       destChannel = new FileOutputStream(dest).getChannel();
/* 229 */       destChannel.transferFrom(sourceChannel, 0L, sourceChannel.size());
/*     */     } finally {
/* 231 */       sourceChannel.close();
/* 232 */       destChannel.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean deleteProjects(String[] projectNames)
/*     */   {
/* 242 */     for (String projectName : projectNames) {
/* 243 */       if (!isValidProjectName(projectName)) {
/* 244 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/* 247 */     boolean success = true;
/* 248 */     for (String projectName : projectNames) {
/* 249 */       File jsFile = new File(BLOCKS_DIR, projectName + ".js");
/* 250 */       if ((jsFile.exists()) && 
/* 251 */         (!jsFile.delete())) {
/* 252 */         success = false;
/*     */       }
/*     */       
/* 255 */       if (success) {
/* 256 */         File blkFile = new File(BLOCKS_DIR, projectName + ".blk");
/* 257 */         if ((blkFile.exists()) && 
/* 258 */           (!blkFile.delete())) {
/* 259 */           success = false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 264 */     return success;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\util\ProjectsUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */