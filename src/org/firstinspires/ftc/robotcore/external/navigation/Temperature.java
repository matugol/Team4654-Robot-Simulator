/*    */ package org.firstinspires.ftc.robotcore.external.navigation;
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
/*    */ public class Temperature
/*    */ {
/*    */   public TempUnit unit;
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
/*    */   public double temperature;
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
/*    */   public long acquisitionTime;
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
/*    */   public Temperature()
/*    */   {
/* 60 */     this(TempUnit.CELSIUS, 0.0D, 0L);
/*    */   }
/*    */   
/*    */   public Temperature(TempUnit unit, double temperature, long acquisitionTime)
/*    */   {
/* 65 */     this.unit = unit;
/* 66 */     this.temperature = temperature;
/* 67 */     this.acquisitionTime = acquisitionTime;
/*    */   }
/*    */   
/*    */   public Temperature toUnit(TempUnit tempUnit)
/*    */   {
/* 72 */     if (tempUnit != this.unit)
/*    */     {
/* 74 */       return new Temperature(tempUnit, tempUnit.fromUnit(this.unit, this.temperature), this.acquisitionTime);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 79 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\Temperature.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */