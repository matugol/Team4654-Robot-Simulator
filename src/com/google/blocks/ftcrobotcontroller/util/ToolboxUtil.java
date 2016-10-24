/*     */ package com.google.blocks.ftcrobotcontroller.util;
/*     */ 
/*     */ import android.content.res.AssetManager;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ class ToolboxUtil
/*     */ {
/*     */   static void addAssetToToolbox(StringBuilder xmlToolbox, AssetManager assetManager, String assetName)
/*     */     throws IOException
/*     */   {
/*  27 */     BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(assetName)));
/*     */     try {
/*  29 */       String line = null;
/*  30 */       while ((line = reader.readLine()) != null) {
/*  31 */         xmlToolbox.append(line).append("\n");
/*     */       }
/*     */     } finally {
/*  34 */       reader.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static String makeNumberShadow(int n)
/*     */   {
/*  42 */     return "<shadow type=\"math_number\"><field name=\"NUM\">" + n + "</field></shadow>\n";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static String makeNumberShadow(double n)
/*     */   {
/*  49 */     return "<shadow type=\"math_number\"><field name=\"NUM\">" + n + "</field></shadow>\n";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static String makeBooleanShadow(boolean value)
/*     */   {
/*  56 */     String b = value ? "TRUE" : "FALSE";
/*  57 */     return "<shadow type=\"logic_boolean\"><field name=\"BOOL\">" + b + "</field></shadow>\n";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static String makeEnumShadow(HardwareType hardwareType, String enumType)
/*     */   {
/*  64 */     return "<shadow type=\"" + hardwareType.blockTypePrefix + "_enum_" + enumType + "\">\n" + "</shadow>\n";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addPropertySetter(StringBuilder xmlToolbox, HardwareType hardwareType, HardwareItem hardwareItem, String propertyName, String setterValue)
/*     */   {
/*  74 */     xmlToolbox.append("<block type=\"").append(hardwareType.blockTypePrefix).append("_setProperty\">\n").append("<field name=\"IDENTIFIER\">").append(hardwareItem.identifier).append("</field>\n").append("<field name=\"PROP\">").append(propertyName).append("</field>\n").append("<value name=\"VALUE\">\n").append(setterValue).append("\n").append("</value>\n").append("</block>\n");
/*     */   }
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
/*     */   static void addDualPropertySetters(StringBuilder xmlToolbox, HardwareType hardwareType, String propertyName, HardwareItem hardwareItem1, String setterValue1, HardwareItem hardwareItem2, String setterValue2)
/*     */   {
/*  90 */     xmlToolbox.append("<block type=\"").append(hardwareType.blockTypePrefix).append("_setDualProperty\">\n").append("<field name=\"PROP\">").append(propertyName).append("</field>\n").append("<field name=\"IDENTIFIER1\">").append(hardwareItem1.identifier).append("</field>\n").append("<field name=\"IDENTIFIER2\">").append(hardwareItem2.identifier).append("</field>\n").append("<value name=\"VALUE1\">\n").append(setterValue1).append("</value>\n").append("<value name=\"VALUE2\">\n").append(setterValue2).append("</value>\n").append("</block>\n");
/*     */   }
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
/*     */   private static void addPropertyGetter(StringBuilder xmlToolbox, HardwareType hardwareType, HardwareItem hardwareItem, String propertyName)
/*     */   {
/* 106 */     xmlToolbox.append("<block type=\"").append(hardwareType.blockTypePrefix).append("_getProperty\">\n").append("<field name=\"IDENTIFIER\">").append(hardwareItem.identifier).append("</field>\n").append("<field name=\"PROP\">").append(propertyName).append("</field>\n").append("</block>\n");
/*     */   }
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
/*     */   static void addProperties(StringBuilder xmlToolbox, HardwareType hardwareType, HardwareItem hardwareItem, Set<String> properties, Map<String, String[]> setterValues)
/*     */   {
/* 120 */     for (String propertyName : properties) {
/* 121 */       if ((setterValues != null) && (setterValues.containsKey(propertyName))) {
/* 122 */         for (String setterValue : (String[])setterValues.get(propertyName)) {
/* 123 */           addPropertySetter(xmlToolbox, hardwareType, hardwareItem, propertyName, setterValue);
/*     */         }
/*     */       }
/*     */       
/* 127 */       addPropertyGetter(xmlToolbox, hardwareType, hardwareItem, propertyName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void addFunctions(StringBuilder xmlToolbox, HardwareType hardwareType, HardwareItem hardwareItem, Map<String, Map<String, String>> functions)
/*     */   {
/* 137 */     for (Map.Entry<String, Map<String, String>> functionEntry : functions.entrySet()) {
/* 138 */       String functionName = (String)functionEntry.getKey();
/* 139 */       Map<String, String> args = (Map)functionEntry.getValue();
/*     */       
/* 141 */       xmlToolbox.append("<block type=\"").append(hardwareType.blockTypePrefix).append("_").append(functionName).append("\">\n").append("<field name=\"IDENTIFIER\">").append(hardwareItem.identifier).append("</field>\n");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 146 */       for (Map.Entry<String, String> argEntry : args.entrySet()) {
/* 147 */         String argName = (String)argEntry.getKey();
/* 148 */         String value = (String)argEntry.getValue();
/* 149 */         xmlToolbox.append("<value name=\"" + argName + "\">\n").append(value).append("</value>\n");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 154 */       xmlToolbox.append("</block>\n");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\util\ToolboxUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */