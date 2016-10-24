/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import android.widget.TextView;
/*     */ import java.io.File;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Random;
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
/*     */ public class Util
/*     */ {
/*  46 */   public static String ASCII_RECORD_SEPARATOR = "\036";
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String LOWERCASE_ALPHA_NUM_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getRandomString(int stringLength, String charSet)
/*     */   {
/*  57 */     Random random = new Random();
/*  58 */     StringBuilder sb = new StringBuilder();
/*  59 */     for (int i = 0; i < stringLength; i++)
/*     */     {
/*  61 */       sb.append(charSet.charAt(random.nextInt(charSet.length())));
/*     */     }
/*  63 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void sortFilesByName(File[] files)
/*     */   {
/*  71 */     Arrays.sort(files, new Comparator()
/*     */     {
/*     */       public int compare(File f1, File f2) {
/*  74 */         return f1.getName().compareTo(f2.getName());
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static void updateTextView(TextView textView, final String msg) {
/*  80 */     if (textView != null) {
/*  81 */       textView.post(new Runnable() {
/*     */         public void run() {
/*  83 */           this.val$textView.setText(msg);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] concatenateByteArrays(byte[] first, byte[] second)
/*     */   {
/*  97 */     byte[] concatenated = new byte[first.length + second.length];
/*  98 */     System.arraycopy(first, 0, concatenated, 0, first.length);
/*  99 */     System.arraycopy(second, 0, concatenated, first.length, second.length);
/* 100 */     return concatenated;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isPrefixOf(String prefix, String target)
/*     */   {
/* 111 */     if (prefix == null)
/* 112 */       return true;
/* 113 */     if (target == null) {
/* 114 */       return false;
/*     */     }
/* 116 */     if (prefix.length() <= target.length()) {
/* 117 */       for (int ich = 0; ich < prefix.length(); ich++) {
/* 118 */         if (prefix.charAt(ich) != target.charAt(ich)) {
/* 119 */           return false;
/*     */         }
/*     */       }
/* 122 */       return true;
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\Util.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */