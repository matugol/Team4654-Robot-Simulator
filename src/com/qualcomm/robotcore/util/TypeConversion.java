/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class TypeConversion
/*     */ {
/*  43 */   private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
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
/*     */   public static byte[] shortToByteArray(short shortInt)
/*     */   {
/*  57 */     return shortToByteArray(shortInt, ByteOrder.BIG_ENDIAN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] shortToByteArray(short shortInt, ByteOrder byteOrder)
/*     */   {
/*  67 */     return ByteBuffer.allocate(2).order(byteOrder).putShort(shortInt).array();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] intToByteArray(int integer)
/*     */   {
/*  76 */     return intToByteArray(integer, ByteOrder.BIG_ENDIAN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] intToByteArray(int integer, ByteOrder byteOrder)
/*     */   {
/*  86 */     return ByteBuffer.allocate(4).order(byteOrder).putInt(integer).array();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] longToByteArray(long longInt)
/*     */   {
/*  95 */     return longToByteArray(longInt, ByteOrder.BIG_ENDIAN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] longToByteArray(long longInt, ByteOrder byteOrder)
/*     */   {
/* 105 */     return ByteBuffer.allocate(8).order(byteOrder).putLong(longInt).array();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static short byteArrayToShort(byte[] byteArray)
/*     */   {
/* 114 */     return byteArrayToShort(byteArray, ByteOrder.BIG_ENDIAN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static short byteArrayToShort(byte[] byteArray, ByteOrder byteOrder)
/*     */   {
/* 124 */     return ByteBuffer.wrap(byteArray).order(byteOrder).getShort();
/*     */   }
/*     */   
/*     */   public static short byteArrayToShort(byte[] byteArray, int ibFirst, ByteOrder byteOrder) {
/* 128 */     int cb = byteArray.length - ibFirst;
/* 129 */     return ByteBuffer.wrap(byteArray, ibFirst, cb).order(byteOrder).getShort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int byteArrayToInt(byte[] byteArray)
/*     */   {
/* 138 */     return byteArrayToInt(byteArray, ByteOrder.BIG_ENDIAN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int byteArrayToInt(byte[] byteArray, ByteOrder byteOrder)
/*     */   {
/* 148 */     return ByteBuffer.wrap(byteArray).order(byteOrder).getInt();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long byteArrayToLong(byte[] byteArray)
/*     */   {
/* 157 */     return byteArrayToLong(byteArray, ByteOrder.BIG_ENDIAN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long byteArrayToLong(byte[] byteArray, ByteOrder byteOrder)
/*     */   {
/* 167 */     return ByteBuffer.wrap(byteArray).order(byteOrder).getLong();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int unsignedByteToInt(byte b)
/*     */   {
/* 178 */     return b & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int unsignedShortToInt(short s)
/*     */   {
/* 187 */     return s & 0xFFFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double unsignedByteToDouble(byte b)
/*     */   {
/* 196 */     return b & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long unsignedIntToLong(int i)
/*     */   {
/* 205 */     return i & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] stringToUtf8(String javaString)
/*     */   {
/* 215 */     byte[] utf8String = javaString.getBytes(UTF8_CHARSET);
/*     */     
/* 217 */     if (!javaString.equals(new String(utf8String, UTF8_CHARSET))) {
/* 218 */       String msg = String.format("string cannot be cleanly encoded into %s - '%s' -> '%s'", new Object[] { UTF8_CHARSET.name(), javaString, new String(utf8String, UTF8_CHARSET) });
/*     */       
/* 220 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */     
/* 223 */     return utf8String;
/*     */   }
/*     */   
/*     */   public static String utf8ToString(byte[] utf8String) {
/* 227 */     return new String(utf8String, UTF8_CHARSET);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\TypeConversion.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */