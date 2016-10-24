/*    */ package com.qualcomm.robotcore.hardware;
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
/*    */ public final class I2cAddr
/*    */ {
/*    */   private final int i2cAddr7Bit;
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
/*    */   public I2cAddr(int i2cAddr7Bit)
/*    */   {
/* 44 */     this.i2cAddr7Bit = (i2cAddr7Bit & 0x7F);
/*    */   }
/*    */   
/*    */   public static I2cAddr zero()
/*    */   {
/* 49 */     return create7bit(0);
/*    */   }
/*    */   
/*    */   public static I2cAddr create7bit(int i2cAddr7Bit) {
/* 53 */     return new I2cAddr(i2cAddr7Bit);
/*    */   }
/*    */   
/*    */   public static I2cAddr create8bit(int i2cAddr8Bit) {
/* 57 */     return new I2cAddr(i2cAddr8Bit / 2);
/*    */   }
/*    */   
/*    */   public int get8Bit()
/*    */   {
/* 62 */     return this.i2cAddr7Bit * 2;
/*    */   }
/*    */   
/*    */   public int get7Bit() {
/* 66 */     return this.i2cAddr7Bit;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cAddr.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */