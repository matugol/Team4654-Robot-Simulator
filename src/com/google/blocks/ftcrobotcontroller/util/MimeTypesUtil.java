/*    */ package com.google.blocks.ftcrobotcontroller.util;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MimeTypesUtil
/*    */ {
/* 14 */   private static final Map<String, String> MIME_TYPES_BY_EXT = new HashMap();
/*    */   
/* 16 */   static { MIME_TYPES_BY_EXT.put("asc", "text/plain");
/* 17 */     MIME_TYPES_BY_EXT.put("class", "application/octet-stream");
/* 18 */     MIME_TYPES_BY_EXT.put("css", "text/css");
/* 19 */     MIME_TYPES_BY_EXT.put("cur", "image/x-win-bitmap");
/* 20 */     MIME_TYPES_BY_EXT.put("doc", "application/msword");
/* 21 */     MIME_TYPES_BY_EXT.put("exe", "application/octet-stream");
/* 22 */     MIME_TYPES_BY_EXT.put("flv", "video/x-flv");
/* 23 */     MIME_TYPES_BY_EXT.put("gif", "image/gif");
/* 24 */     MIME_TYPES_BY_EXT.put("html", "text/html");
/* 25 */     MIME_TYPES_BY_EXT.put("htm", "text/html");
/* 26 */     MIME_TYPES_BY_EXT.put("ico", "image/x-icon");
/* 27 */     MIME_TYPES_BY_EXT.put("java", "text/x-java-source, text/java");
/* 28 */     MIME_TYPES_BY_EXT.put("jpeg", "image/jpeg");
/* 29 */     MIME_TYPES_BY_EXT.put("jpg", "image/jpeg");
/* 30 */     MIME_TYPES_BY_EXT.put("js", "application/javascript");
/* 31 */     MIME_TYPES_BY_EXT.put("m3u8", "application/vnd.apple.mpegurl");
/* 32 */     MIME_TYPES_BY_EXT.put("m3u", "audio/mpeg-url");
/* 33 */     MIME_TYPES_BY_EXT.put("md", "text/plain");
/* 34 */     MIME_TYPES_BY_EXT.put("mov", "video/quicktime");
/* 35 */     MIME_TYPES_BY_EXT.put("mp3", "audio/mpeg");
/* 36 */     MIME_TYPES_BY_EXT.put("mp4", "video/mp4");
/* 37 */     MIME_TYPES_BY_EXT.put("ogg", "application/x-ogg");
/* 38 */     MIME_TYPES_BY_EXT.put("ogv", "video/ogg");
/* 39 */     MIME_TYPES_BY_EXT.put("pdf", "application/pdf");
/* 40 */     MIME_TYPES_BY_EXT.put("png", "image/png");
/* 41 */     MIME_TYPES_BY_EXT.put("svg", "image/svg+xml");
/* 42 */     MIME_TYPES_BY_EXT.put("swf", "application/x-shockwave-flash");
/* 43 */     MIME_TYPES_BY_EXT.put("ts", "video/mp2t");
/* 44 */     MIME_TYPES_BY_EXT.put("txt", "text/plain");
/* 45 */     MIME_TYPES_BY_EXT.put("wav", "audio/wav");
/* 46 */     MIME_TYPES_BY_EXT.put("xml", "text/xml");
/* 47 */     MIME_TYPES_BY_EXT.put("zip", "application/octet-stream");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String getMimeType(String extension)
/*    */   {
/* 59 */     if (extension.startsWith(".")) {
/* 60 */       extension = extension.substring(1);
/*    */     }
/* 62 */     return (String)MIME_TYPES_BY_EXT.get(extension);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\util\MimeTypesUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */