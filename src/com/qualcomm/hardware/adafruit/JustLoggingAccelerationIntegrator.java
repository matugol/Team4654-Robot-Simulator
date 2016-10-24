/*    */ package com.qualcomm.hardware.adafruit;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
/*    */ import org.firstinspires.ftc.robotcore.external.navigation.Position;
/*    */ import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
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
/*    */ public class JustLoggingAccelerationIntegrator
/*    */   implements BNO055IMU.AccelerationIntegrator
/*    */ {
/*    */   BNO055IMU.Parameters parameters;
/*    */   Acceleration acceleration;
/*    */   
/*    */   public void initialize(BNO055IMU.Parameters parameters, Position initialPosition, Velocity initialVelocity)
/*    */   {
/* 53 */     this.parameters = parameters;
/*    */   }
/*    */   
/* 56 */   public Position getPosition() { return new Position(); }
/* 57 */   public Velocity getVelocity() { return new Velocity(); }
/*    */   
/*    */   public Acceleration getAcceleration() {
/* 60 */     return this.acceleration == null ? new Acceleration() : this.acceleration;
/*    */   }
/*    */   
/*    */ 
/*    */   public void update(Acceleration linearAcceleration)
/*    */   {
/* 66 */     if (linearAcceleration.acquisitionTime != 0L)
/*    */     {
/* 68 */       if (this.acceleration != null)
/*    */       {
/* 70 */         Acceleration accelPrev = this.acceleration;
/* 71 */         this.acceleration = linearAcceleration;
/* 72 */         if (this.parameters.loggingEnabled)
/*    */         {
/* 74 */           RobotLog.vv(this.parameters.loggingTag, "dt=%.3fs accel=%s", new Object[] { Double.valueOf((this.acceleration.acquisitionTime - accelPrev.acquisitionTime) * 1.0E-9D), this.acceleration });
/*    */         }
/*    */       }
/*    */       else {
/* 78 */         this.acceleration = linearAcceleration;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\adafruit\JustLoggingAccelerationIntegrator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */