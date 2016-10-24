/*    */ package org.firstinspires.ftc.robotcore.external;
/*    */ 
/*    */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
/*    */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;
/*    */ import org.firstinspires.ftc.robotcore.internal.VuforiaLocalizerImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassFactory
/*    */ {
/*    */   public static VuforiaLocalizer createVuforiaLocalizer(VuforiaLocalizer.Parameters parameters)
/*    */   {
/* 57 */     return new VuforiaLocalizerImpl(parameters);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\ClassFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */