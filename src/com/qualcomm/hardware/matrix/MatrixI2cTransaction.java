/*     */ package com.qualcomm.hardware.matrix;
/*     */ 
/*     */ import com.qualcomm.robotcore.util.RobotLog;
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
/*     */ public class MatrixI2cTransaction
/*     */ {
/*     */   public byte motor;
/*     */   public byte servo;
/*     */   public I2cTransactionProperty property;
/*     */   public int value;
/*     */   public boolean write;
/*     */   public byte speed;
/*     */   public int target;
/*     */   public byte mode;
/*     */   public I2cTransactionState state;
/*     */   
/*     */   static enum I2cTransactionState
/*     */   {
/*  38 */     QUEUED, 
/*  39 */     PENDING_I2C_READ, 
/*  40 */     PENDING_I2C_WRITE, 
/*  41 */     PENDING_READ_DONE, 
/*  42 */     DONE;
/*     */     
/*     */     private I2cTransactionState() {}
/*     */   }
/*     */   
/*     */   static enum I2cTransactionProperty
/*     */   {
/*  49 */     PROPERTY_MODE, 
/*  50 */     PROPERTY_TARGET, 
/*  51 */     PROPERTY_SPEED, 
/*  52 */     PROPERTY_BATTERY, 
/*  53 */     PROPERTY_POSITION, 
/*  54 */     PROPERTY_MOTOR_BATCH, 
/*  55 */     PROPERTY_SERVO, 
/*  56 */     PROPERTY_SERVO_ENABLE, 
/*  57 */     PROPERTY_START, 
/*  58 */     PROPERTY_TIMEOUT;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private I2cTransactionProperty() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   MatrixI2cTransaction(byte motor, I2cTransactionProperty property)
/*     */   {
/*  81 */     this.motor = motor;
/*  82 */     this.property = property;
/*  83 */     this.state = I2cTransactionState.QUEUED;
/*  84 */     this.write = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   MatrixI2cTransaction(byte motor, I2cTransactionProperty property, int value)
/*     */   {
/*  92 */     this.motor = motor;
/*  93 */     this.value = value;
/*  94 */     this.property = property;
/*  95 */     this.state = I2cTransactionState.QUEUED;
/*  96 */     this.write = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   MatrixI2cTransaction(byte motor, byte speed, int target, byte mode)
/*     */   {
/* 104 */     this.motor = motor;
/* 105 */     this.speed = speed;
/* 106 */     this.target = target;
/* 107 */     this.mode = mode;
/* 108 */     this.property = I2cTransactionProperty.PROPERTY_MOTOR_BATCH;
/* 109 */     this.state = I2cTransactionState.QUEUED;
/* 110 */     this.write = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   MatrixI2cTransaction(byte servo, byte target, byte speed)
/*     */   {
/* 118 */     this.servo = servo;
/* 119 */     this.speed = speed;
/* 120 */     this.target = target;
/* 121 */     this.property = I2cTransactionProperty.PROPERTY_SERVO;
/* 122 */     this.state = I2cTransactionState.QUEUED;
/* 123 */     this.write = true;
/*     */   }
/*     */   
/*     */   public boolean isEqual(MatrixI2cTransaction transaction)
/*     */   {
/* 128 */     if (this.property != transaction.property) {
/* 129 */       return false;
/*     */     }
/* 131 */     switch (this.property) {
/*     */     case PROPERTY_MODE: 
/*     */     case PROPERTY_START: 
/*     */     case PROPERTY_TIMEOUT: 
/*     */     case PROPERTY_TARGET: 
/*     */     case PROPERTY_SPEED: 
/*     */     case PROPERTY_BATTERY: 
/*     */     case PROPERTY_POSITION: 
/* 139 */       return (this.write == transaction.write) && (this.motor == transaction.motor) && (this.value == transaction.value);
/*     */     case PROPERTY_MOTOR_BATCH: 
/* 141 */       return (this.write == transaction.write) && (this.motor == transaction.motor) && (this.speed == transaction.speed) && (this.target == transaction.target) && (this.mode == transaction.mode);
/*     */     case PROPERTY_SERVO: 
/* 143 */       return (this.write == transaction.write) && (this.servo == transaction.servo) && (this.speed == transaction.speed) && (this.target == transaction.target);
/*     */     case PROPERTY_SERVO_ENABLE: 
/* 145 */       return (this.write == transaction.write) && (this.value == transaction.value);
/*     */     }
/* 147 */     RobotLog.e("Can not compare against unknown transaction property " + transaction.toString());
/* 148 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 155 */     if (this.property == I2cTransactionProperty.PROPERTY_MOTOR_BATCH)
/* 156 */       return "Matrix motor transaction: " + this.property + " motor " + this.motor + " write " + this.write + " speed " + this.speed + " target " + this.target + " mode " + this.mode;
/* 157 */     if (this.property == I2cTransactionProperty.PROPERTY_SERVO)
/* 158 */       return "Matrix servo transaction: " + this.property + " servo " + this.servo + " write " + this.write + " change rate " + this.speed + " target " + this.target;
/* 159 */     if (this.property == I2cTransactionProperty.PROPERTY_SERVO_ENABLE) {
/* 160 */       return "Matrix servo transaction: " + this.property + " servo " + this.servo + " write " + this.write + " value " + this.value;
/*     */     }
/* 162 */     return "Matrix motor transaction: " + this.property + " motor " + this.motor + " write " + this.write + " value " + this.value;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\matrix\MatrixI2cTransaction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */