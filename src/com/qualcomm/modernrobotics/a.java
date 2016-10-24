/*    */ package com.qualcomm.modernrobotics;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ import com.qualcomm.robotcore.util.TypeConversion;
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
/*    */ class a
/*    */ {
/*    */   static boolean a(byte[] paramArrayOfByte, int paramInt)
/*    */   {
/* 50 */     if (paramArrayOfByte.length < 5) {
/* 51 */       RobotLog.w("Modern Robotics USB header length is too short");
/* 52 */       return false;
/*    */     }
/*    */     
/* 55 */     if ((paramArrayOfByte[0] != 51) || (paramArrayOfByte[1] != -52)) {
/* 56 */       RobotLog.w("Modern Robotics USB header sync bytes are incorrect");
/* 57 */       return false;
/*    */     }
/*    */     
/* 60 */     if (TypeConversion.unsignedByteToInt(paramArrayOfByte[4]) != paramInt) {
/* 61 */       RobotLog.w("Modern Robotics USB header reported unexpected packet size");
/* 62 */       return false;
/*    */     }
/*    */     
/* 65 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\ModernRobotics-release.jar!\classes.jar!\com\qualcomm\modernrobotics\a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */