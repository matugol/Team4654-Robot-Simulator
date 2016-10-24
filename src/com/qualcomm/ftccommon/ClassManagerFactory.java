/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
/*    */ import com.qualcomm.ftccommon.configuration.RobotConfigResFilter;
/*    */ import com.qualcomm.robotcore.eventloop.opmode.AnnotatedOpModeRegistrar;
/*    */ import com.qualcomm.robotcore.hardware.configuration.UserSensorTypeManager;
/*    */ import com.qualcomm.robotcore.util.ClassManager;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassManagerFactory
/*    */ {
/*    */   public static void processClasses()
/*    */   {
/* 53 */     ClassManager classManager = null;
/*    */     try {
/* 55 */       classManager = new ClassManager();
/*    */       
/*    */ 
/* 58 */       classManager.registerFilter(new RobotConfigResFilter(RobotConfigFileManager.getRobotConfigTypeAttribute(), RobotConfigFileManager.getXmlResourceIds()));
/* 59 */       classManager.registerFilter(new RobotConfigResFilter(RobotConfigFileManager.getRobotConfigTemplateAttribute(), RobotConfigFileManager.getXmlResourceTemplateIds()));
/* 60 */       classManager.registerFilter(new AnnotatedOpModeRegistrar());
/* 61 */       classManager.registerFilter(UserSensorTypeManager.getInstance());
/*    */       
/*    */ 
/* 64 */       classManager.processAllClasses();
/*    */     } catch (IOException e) {
/* 66 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\ClassManagerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */