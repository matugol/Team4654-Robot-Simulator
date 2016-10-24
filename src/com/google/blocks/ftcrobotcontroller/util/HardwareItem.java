/*     */ package com.google.blocks.ftcrobotcontroller.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HardwareItem
/*     */ {
/*     */   public final HardwareType hardwareType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String deviceName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String identifier;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String visibleName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareItem(HardwareType hardwareType, String deviceName)
/*     */   {
/*  38 */     if ((hardwareType == null) || (deviceName == null)) {
/*  39 */       throw new NullPointerException();
/*     */     }
/*  41 */     this.hardwareType = hardwareType;
/*  42 */     this.deviceName = deviceName;
/*  43 */     this.identifier = makeIdentifier(hardwareType, deviceName);
/*  44 */     this.visibleName = makeVisibleName(deviceName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String makeIdentifier(HardwareType hardwareType, String deviceName)
/*     */   {
/*  51 */     int length = deviceName.length();
/*  52 */     StringBuilder identifier = new StringBuilder();
/*     */     
/*  54 */     char ch = deviceName.charAt(0);
/*  55 */     if (Character.isJavaIdentifierStart(ch)) {
/*  56 */       identifier.append(ch);
/*  57 */     } else if (Character.isJavaIdentifierPart(ch)) {
/*  58 */       identifier.append('_').append(ch);
/*     */     }
/*  60 */     for (int i = 1; i < length; i++) {
/*  61 */       ch = deviceName.charAt(i);
/*  62 */       if (Character.isJavaIdentifierPart(ch)) {
/*  63 */         identifier.append(ch);
/*     */       }
/*     */     }
/*     */     
/*  67 */     identifier.append(hardwareType.identifierSuffix);
/*  68 */     return identifier.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String makeVisibleName(String deviceName)
/*     */   {
/*  75 */     int length = deviceName.length();
/*  76 */     StringBuilder visibleName = new StringBuilder();
/*     */     
/*  78 */     for (int i = 0; i < length; i++) {
/*  79 */       char ch = deviceName.charAt(i);
/*  80 */       if (ch == ' ') {
/*  81 */         visibleName.append(' ');
/*     */       } else {
/*  83 */         visibleName.append(ch);
/*     */       }
/*     */     }
/*  86 */     return visibleName.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  93 */     if ((o instanceof HardwareItem)) {
/*  94 */       HardwareItem that = (HardwareItem)o;
/*  95 */       return (this.hardwareType.equals(that.hardwareType)) && (this.deviceName.equals(that.deviceName)) && (this.identifier.equals(that.identifier)) && (this.visibleName.equals(that.visibleName));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 105 */     return this.hardwareType.hashCode() + this.deviceName.hashCode() + this.identifier.hashCode() + this.visibleName.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\util\HardwareItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */