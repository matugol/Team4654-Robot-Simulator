/*    */ package com.qualcomm.robotcore.hardware;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface IrSeekerSensor
/*    */   extends HardwareDevice
/*    */ {
/*    */   public abstract void setSignalDetectedThreshold(double paramDouble);
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract double getSignalDetectedThreshold();
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract void setMode(Mode paramMode);
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract Mode getMode();
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract boolean signalDetected();
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract double getAngle();
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract double getStrength();
/*    */   
/*    */ 
/*    */   public abstract IrSeekerIndividualSensor[] getIndividualSensors();
/*    */   
/*    */ 
/*    */   public abstract void setI2cAddress(I2cAddr paramI2cAddr);
/*    */   
/*    */ 
/*    */   public abstract I2cAddr getI2cAddress();
/*    */   
/*    */ 
/*    */   public static enum Mode
/*    */   {
/* 46 */     MODE_600HZ,  MODE_1200HZ;
/*    */     
/*    */ 
/*    */     private Mode() {}
/*    */   }
/*    */   
/*    */ 
/*    */   public static class IrSeekerIndividualSensor
/*    */   {
/* 55 */     private double angle = 0.0D;
/* 56 */     private double strength = 0.0D;
/*    */     
/*    */ 
/*    */ 
/*    */     public IrSeekerIndividualSensor()
/*    */     {
/* 62 */       this(0.0D, 0.0D);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */     public IrSeekerIndividualSensor(double angle, double strength)
/*    */     {
/* 69 */       this.angle = angle;
/* 70 */       this.strength = strength;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public double getSensorAngle()
/*    */     {
/* 78 */       return this.angle;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public double getSensorStrength()
/*    */     {
/* 86 */       return this.strength;
/*    */     }
/*    */     
/*    */     public String toString()
/*    */     {
/* 91 */       return String.format("IR Sensor: %3.1f degrees at %3.1f%% power", new Object[] { Double.valueOf(this.angle), Double.valueOf(this.strength * 100.0D) });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\IrSeekerSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */