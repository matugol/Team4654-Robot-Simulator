/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.Resources;
/*     */ import android.content.res.XmlResourceParser;
/*     */ import com.qualcomm.robotcore.util.ClassFilter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Collection;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.xmlpull.v1.XmlPullParserException;
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
/*     */ public class RobotConfigResFilter
/*     */   implements ClassFilter
/*     */ {
/*     */   public static final String robotConfigRootTag = "Robot";
/*     */   public static final String robotConfigRootTypeAttribute = "type";
/*     */   protected Resources resources;
/*     */   protected String typeAttributeValue;
/*     */   protected Collection<Integer> xmlIdCollection;
/*     */   
/*     */   public RobotConfigResFilter(String typeAttributeValue, Collection<Integer> xmlIdCollection)
/*     */   {
/*  38 */     this(AppUtil.getInstance().getApplication(), typeAttributeValue, xmlIdCollection);
/*     */   }
/*     */   
/*     */   public RobotConfigResFilter(Context context, String typeAttributeValue, Collection<Integer> xmlIdCollection)
/*     */   {
/*  43 */     this.typeAttributeValue = typeAttributeValue;
/*  44 */     this.resources = context.getResources();
/*  45 */     this.xmlIdCollection = xmlIdCollection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isRobotConfiguration(XmlResourceParser xpp)
/*     */   {
/*  55 */     return this.typeAttributeValue.equals(getRootAttribute(xpp, "Robot", "type", null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getRootAttribute(XmlResourceParser xpp, String rootElement, String attributeName, String defaultValue)
/*     */   {
/*     */     try
/*     */     {
/*  66 */       while (xpp.getEventType() != 1) {
/*  67 */         if (xpp.getEventType() == 2) {
/*  68 */           if (!xpp.getName().equals(rootElement)) {
/*  69 */             return null;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*  74 */           String result = xpp.getAttributeValue(null, attributeName);
/*  75 */           return result != null ? result : defaultValue;
/*     */         }
/*  77 */         xpp.next();
/*     */       }
/*     */     } catch (XmlPullParserException|IOException e) {
/*  80 */       e.printStackTrace();
/*     */     }
/*     */     
/*  83 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void filter(Class clazz)
/*     */   {
/*  91 */     if (clazz.getName().endsWith("R$xml"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  96 */       Field[] fields = clazz.getFields();
/*  97 */       for (Field f : fields) {
/*     */         try {
/*  99 */           Class<?> c = f.getType();
/* 100 */           if (c.equals(Integer.TYPE)) {
/* 101 */             int id = f.getInt(clazz);
/* 102 */             if (isRobotConfiguration(this.resources.getXml(id))) {
/* 103 */               this.xmlIdCollection.add(Integer.valueOf(id));
/*     */             }
/*     */           }
/*     */         } catch (IllegalAccessException e) {
/* 107 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\RobotConfigResFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */