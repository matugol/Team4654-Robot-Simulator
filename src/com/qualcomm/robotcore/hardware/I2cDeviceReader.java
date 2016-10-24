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
/*    */ 
/*    */ 
/*    */ public class I2cDeviceReader
/*    */ {
/*    */   private final I2cDevice i2cDevice;
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
/*    */   public I2cDeviceReader(I2cDevice i2cDevice, I2cAddr i2cAddress, int memAddress, int length)
/*    */   {
/* 48 */     this.i2cDevice = i2cDevice;
/*    */     
/* 50 */     i2cDevice.enableI2cReadMode(i2cAddress, memAddress, length);
/* 51 */     i2cDevice.setI2cPortActionFlag();
/* 52 */     i2cDevice.writeI2cCacheToController();
/*    */     
/* 54 */     i2cDevice.registerForI2cPortReadyCallback(new I2cController.I2cPortReadyCallback()
/*    */     {
/*    */       public void portIsReady(int port) {
/* 57 */         I2cDeviceReader.this.handleCallback();
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte[] getReadBuffer()
/*    */   {
/* 67 */     return this.i2cDevice.getCopyOfReadBuffer();
/*    */   }
/*    */   
/*    */   private void handleCallback() {
/* 71 */     this.i2cDevice.setI2cPortActionFlag();
/* 72 */     this.i2cDevice.readI2cCacheFromController();
/* 73 */     this.i2cDevice.writeI2cPortFlagOnlyToController();
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cDeviceReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */