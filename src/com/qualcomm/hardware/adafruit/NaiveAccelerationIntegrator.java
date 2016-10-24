/*     */ package com.qualcomm.hardware.adafruit;
/*     */ 
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.NavUtil;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Position;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
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
/*     */ public class NaiveAccelerationIntegrator
/*     */   implements BNO055IMU.AccelerationIntegrator
/*     */ {
/*     */   BNO055IMU.Parameters parameters;
/*     */   Position position;
/*     */   Velocity velocity;
/*     */   Acceleration acceleration;
/*     */   
/*  60 */   public Position getPosition() { return this.position; }
/*  61 */   public Velocity getVelocity() { return this.velocity; }
/*  62 */   public Acceleration getAcceleration() { return this.acceleration; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   NaiveAccelerationIntegrator()
/*     */   {
/*  70 */     this.parameters = null;
/*  71 */     this.position = null;
/*  72 */     this.velocity = null;
/*  73 */     this.acceleration = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialize(BNO055IMU.Parameters parameters, Position initialPosition, Velocity initialVelocity)
/*     */   {
/*  82 */     this.parameters = parameters;
/*  83 */     this.position = initialPosition;
/*  84 */     this.velocity = initialVelocity;
/*  85 */     this.acceleration = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void update(Acceleration linearAcceleration)
/*     */   {
/*  91 */     if (linearAcceleration.acquisitionTime != 0L)
/*     */     {
/*     */ 
/*  94 */       if (this.acceleration != null)
/*     */       {
/*  96 */         Acceleration accelPrev = this.acceleration;
/*  97 */         Velocity velocityPrev = this.velocity;
/*     */         
/*  99 */         this.acceleration = linearAcceleration;
/*     */         
/* 101 */         if (accelPrev.acquisitionTime != 0L)
/*     */         {
/* 103 */           Velocity deltaVelocity = NavUtil.meanIntegrate(this.acceleration, accelPrev);
/* 104 */           this.velocity = NavUtil.plus(this.velocity, deltaVelocity);
/*     */         }
/*     */         
/* 107 */         if (velocityPrev.acquisitionTime != 0L)
/*     */         {
/* 109 */           Position deltaPosition = NavUtil.meanIntegrate(this.velocity, velocityPrev);
/* 110 */           this.position = NavUtil.plus(this.position, deltaPosition);
/*     */         }
/*     */         
/* 113 */         if (this.parameters.loggingEnabled)
/*     */         {
/* 115 */           RobotLog.vv(this.parameters.loggingTag, "dt=%.3fs accel=%s vel=%s pos=%s", new Object[] { Double.valueOf((this.acceleration.acquisitionTime - accelPrev.acquisitionTime) * 1.0E-9D), this.acceleration, this.velocity, this.position });
/*     */         }
/*     */       }
/*     */       else {
/* 119 */         this.acceleration = linearAcceleration;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\adafruit\NaiveAccelerationIntegrator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */