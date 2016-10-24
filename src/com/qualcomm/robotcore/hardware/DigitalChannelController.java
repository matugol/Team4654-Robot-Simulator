/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
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
/*    */ public abstract interface DigitalChannelController
/*    */   extends HardwareDevice
/*    */ {
/*    */   public abstract SerialNumber getSerialNumber();
/*    */   
/*    */   public abstract Mode getDigitalChannelMode(int paramInt);
/*    */   
/*    */   public abstract void setDigitalChannelMode(int paramInt, Mode paramMode);
/*    */   
/*    */   public abstract boolean getDigitalChannelState(int paramInt);
/*    */   
/*    */   public abstract void setDigitalChannelState(int paramInt, boolean paramBoolean);
/*    */   
/*    */   public static enum Mode
/*    */   {
/* 45 */     INPUT,  OUTPUT;
/*    */     
/*    */     private Mode() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DigitalChannelController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */