/*    */ package com.qualcomm.robotcore.hardware.configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MatrixConstants
/*    */ {
/*    */   public static final int NUMBER_OF_MOTORS = 4;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int NUMBER_OF_SERVOS = 4;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final double ENCODER_TICKS_PER_REVOLUTION = 757.12D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final double MAX_NO_LOAD_RPM = 265.0D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void validateMotorZ(int motorZ)
/*    */   {
/* 49 */     if ((motorZ < 0) || (motorZ >= 4))
/* 50 */       throw new IllegalArgumentException(String.format("invalid motor: %d", new Object[] { Integer.valueOf(motorZ) }));
/*    */   }
/*    */   
/*    */   public static void validateServoChannelZ(int channelZ) {
/* 54 */     if ((channelZ < 0) || (channelZ >= 4)) {
/* 55 */       throw new IllegalArgumentException(String.format("invalid servo channel: %d", new Object[] { Integer.valueOf(channelZ) }));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\MatrixConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */