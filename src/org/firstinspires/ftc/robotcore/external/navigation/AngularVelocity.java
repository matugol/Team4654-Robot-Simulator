/*     */ package org.firstinspires.ftc.robotcore.external.navigation;
/*     */ 
/*     */ import junit.framework.Assert;
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
/*     */ public class AngularVelocity
/*     */ {
/*     */   public AxesReference axesReference;
/*     */   public AxesOrder axesOrder;
/*     */   public AngleUnit unit;
/*     */   public float firstAngleRate;
/*     */   public float secondAngleRate;
/*     */   public float thirdAngleRate;
/*     */   public long acquisitionTime;
/*     */   
/*     */   public AngularVelocity()
/*     */   {
/*  92 */     this(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS, 0.0F, 0.0F, 0.0F, 0L);
/*     */   }
/*     */   
/*     */   public AngularVelocity(AxesReference axesReference, AxesOrder axesOrder, AngleUnit unit, float firstAngle, float secondAngle, float thirdAngle, long acquisitionTime)
/*     */   {
/*  97 */     this.axesReference = axesReference;
/*  98 */     this.axesOrder = axesOrder;
/*  99 */     this.unit = unit;
/* 100 */     this.firstAngleRate = firstAngle;
/* 101 */     this.secondAngleRate = secondAngle;
/* 102 */     this.thirdAngleRate = thirdAngle;
/* 103 */     this.acquisitionTime = acquisitionTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AngularVelocity toAngleUnit(AngleUnit unit)
/*     */   {
/* 114 */     if (unit != this.unit)
/*     */     {
/* 116 */       return new AngularVelocity(this.axesReference, this.axesOrder, unit, unit.fromUnit(this.unit, this.firstAngleRate), unit.fromUnit(this.unit, this.secondAngleRate), unit.fromUnit(this.unit, this.thirdAngleRate), this.acquisitionTime);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AngularVelocity toAxesReference(AxesReference axesReference)
/*     */   {
/* 134 */     if (this.axesReference != axesReference)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 141 */       Assert.assertTrue(axesReference == this.axesReference.reverse());
/* 142 */       return new AngularVelocity(this.axesReference.reverse(), this.axesOrder.reverse(), this.unit, this.thirdAngleRate, this.secondAngleRate, this.firstAngleRate, this.acquisitionTime);
/*     */     }
/*     */     
/*     */ 
/* 146 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\AngularVelocity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */