/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import android.os.Build;
/*     */ import android.view.InputDevice;
/*     */ import java.util.HashSet;
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
/*     */ public class Hardware
/*     */ {
/*  43 */   private static boolean mIsIFC = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<Integer> getGameControllerIds()
/*     */   {
/*  51 */     Set<Integer> gameControllerDeviceIds = new HashSet();
/*  52 */     int[] deviceIds = InputDevice.getDeviceIds();
/*  53 */     for (int deviceId : deviceIds) {
/*  54 */       int sources = InputDevice.getDevice(deviceId).getSources();
/*     */       
/*  56 */       if (((sources & 0x401) == 1025) || ((sources & 0x1000010) == 16777232))
/*     */       {
/*  58 */         gameControllerDeviceIds.add(Integer.valueOf(deviceId));
/*     */       }
/*     */     }
/*  61 */     return gameControllerDeviceIds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean IsIFC()
/*     */   {
/*  73 */     return mIsIFC;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean CheckIfIFC()
/*     */   {
/*  83 */     boolean ifcBoard = false;
/*     */     
/*  85 */     String board = Build.BOARD;
/*  86 */     String brand = Build.BRAND;
/*  87 */     String device = Build.DEVICE;
/*  88 */     String hardware = Build.HARDWARE;
/*  89 */     String manufacturer = Build.MANUFACTURER;
/*  90 */     String model = Build.MODEL;
/*  91 */     String product = Build.PRODUCT;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  96 */     RobotLog.d("Platform information: board = " + board + " brand = " + brand + " device = " + device + " hardware = " + hardware + " manufacturer = " + manufacturer + " model = " + model + " product = " + product);
/*     */     
/*     */ 
/*     */ 
/* 100 */     if ((board.equals("MSM8960") == true) && (brand.equals("qcom") == true) && (device.equals("msm8960") == true) && (hardware.equals("qcom") == true) && (manufacturer.equals("unknown") == true) && (model.equals("msm8960") == true) && (product.equals("msm8960") == true))
/*     */     {
/*     */ 
/*     */ 
/* 104 */       RobotLog.d("Detected IFC6410 Device!");
/* 105 */       ifcBoard = true;
/*     */     } else {
/* 107 */       RobotLog.d("Detected regular SmartPhone Device!");
/*     */     }
/*     */     
/* 110 */     return ifcBoard;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\Hardware.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */