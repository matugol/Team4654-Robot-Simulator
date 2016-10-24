/*     */ package com.google.blocks.ftcrobotcontroller.runtime;
/*     */ 
/*     */ import android.graphics.Color;
/*     */ import android.webkit.JavascriptInterface;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ColorAccess
/*     */   extends Access
/*     */ {
/*     */   ColorAccess(String identifier)
/*     */   {
/*  16 */     super(identifier);
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public double getRed(int color)
/*     */   {
/*  22 */     return Color.red(color);
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public double getGreen(int color)
/*     */   {
/*  28 */     return Color.green(color);
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public double getBlue(int color)
/*     */   {
/*  34 */     return Color.blue(color);
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public double getAlpha(int color)
/*     */   {
/*  40 */     return Color.alpha(color);
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public float getHue(int color)
/*     */   {
/*  46 */     float[] array = new float[3];
/*  47 */     Color.colorToHSV(color, array);
/*  48 */     return array[0];
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public float getSaturation(int color)
/*     */   {
/*  54 */     float[] array = new float[3];
/*  55 */     Color.colorToHSV(color, array);
/*  56 */     return array[1];
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public float getValue(int color)
/*     */   {
/*  62 */     float[] array = new float[3];
/*  63 */     Color.colorToHSV(color, array);
/*  64 */     return array[2];
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int rgbToColor(int red, int green, int blue)
/*     */   {
/*  70 */     return Color.rgb(red, green, blue);
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int argbToColor(int alpha, int red, int green, int blue)
/*     */   {
/*  76 */     return Color.argb(alpha, red, green, blue);
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int hsvToColor(float hue, float saturation, float value)
/*     */   {
/*  82 */     float[] array = new float[3];
/*  83 */     array[0] = hue;
/*  84 */     array[1] = saturation;
/*  85 */     array[2] = value;
/*  86 */     return Color.HSVToColor(array);
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int ahsvToColor(int alpha, float hue, float saturation, float value)
/*     */   {
/*  92 */     float[] array = new float[3];
/*  93 */     array[0] = hue;
/*  94 */     array[1] = saturation;
/*  95 */     array[2] = value;
/*  96 */     return Color.HSVToColor(alpha, array);
/*     */   }
/*     */   
/*     */   @JavascriptInterface
/*     */   public int textToColor(String text)
/*     */   {
/* 102 */     return Color.parseColor(text);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\ColorAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */