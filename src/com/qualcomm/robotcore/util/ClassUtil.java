/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public class ClassUtil
/*     */ {
/*     */   public static List<Constructor> getDeclaredConstructors(Class<?> clazz)
/*     */   {
/*     */     Constructor<?>[] constructors;
/*     */     try
/*     */     {
/*  47 */       constructors = clazz.getDeclaredConstructors();
/*     */     } catch (Exception|LinkageError e) {
/*     */       Constructor<?>[] constructors;
/*  50 */       constructors = new Constructor[0];
/*     */     }
/*  52 */     List<Constructor> result = new LinkedList();
/*  53 */     result.addAll(Arrays.asList(constructors));
/*  54 */     return result;
/*     */   }
/*     */   
/*     */   public static List<Method> getDeclaredMethods(Class<?> clazz)
/*     */   {
/*     */     Method[] methods;
/*     */     try {
/*  61 */       methods = clazz.getDeclaredMethods();
/*     */     } catch (Exception|LinkageError e) {
/*     */       Method[] methods;
/*  64 */       methods = new Method[0];
/*     */     }
/*  66 */     List<Method> result = new LinkedList();
/*  67 */     result.addAll(Arrays.asList(methods));
/*  68 */     return result;
/*     */   }
/*     */   
/*     */   public static List<Method> getDeclaredMethodsIncludingSuper(Class<?> clazz)
/*     */   {
/*  73 */     if (clazz.getSuperclass() == null) {
/*  74 */       return getDeclaredMethods(clazz);
/*     */     }
/*  76 */     List<Method> result = getDeclaredMethodsIncludingSuper(clazz.getSuperclass());
/*  77 */     result.addAll(getDeclaredMethods(clazz));
/*  78 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean inheritsFrom(Class baseClass, Class targetClass)
/*     */   {
/*  85 */     while (baseClass != null)
/*     */     {
/*  87 */       if (baseClass == targetClass) {
/*  88 */         return true;
/*     */       }
/*     */       
/*     */ 
/*  92 */       if (targetClass.isInterface()) {
/*  93 */         for (Class intf : baseClass.getInterfaces()) {
/*  94 */           if (inheritsFrom(intf, targetClass)) {
/*  95 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 100 */       baseClass = baseClass.getSuperclass();
/*     */     }
/* 102 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\ClassUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */