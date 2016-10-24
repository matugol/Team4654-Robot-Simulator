/*     */ package com.qualcomm.hardware.matrix;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.ServoController;
/*     */ import com.qualcomm.robotcore.hardware.ServoController.PwmStatus;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.util.Arrays;
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
/*     */ public class MatrixServoController
/*     */   implements ServoController
/*     */ {
/*     */   public static final int SERVO_POSITION_MAX = 240;
/*     */   private static final byte MAX_SERVOS = 4;
/*     */   private static final byte SERVO_ENABLE_ALL = 15;
/*     */   private static final byte SERVO_DISABLE_ALL = 0;
/*     */   private static final byte I2C_DATA_OFFSET = 4;
/*     */   private MatrixMasterController master;
/*     */   protected ServoController.PwmStatus pwmStatus;
/*  54 */   protected double[] servoCache = new double[4];
/*     */   
/*     */   public MatrixServoController(MatrixMasterController master)
/*     */   {
/*  58 */     this.master = master;
/*  59 */     this.pwmStatus = ServoController.PwmStatus.DISABLED;
/*  60 */     Arrays.fill(this.servoCache, 0.0D);
/*     */     
/*  62 */     master.registerServoController(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void pwmEnable()
/*     */   {
/*  68 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_SERVO_ENABLE, 15);
/*  69 */     this.master.queueTransaction(transaction);
/*  70 */     this.pwmStatus = ServoController.PwmStatus.ENABLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public void pwmDisable()
/*     */   {
/*  76 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_SERVO_ENABLE, 0);
/*  77 */     this.master.queueTransaction(transaction);
/*  78 */     this.pwmStatus = ServoController.PwmStatus.DISABLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public ServoController.PwmStatus getPwmStatus()
/*     */   {
/*  84 */     return this.pwmStatus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServoPosition(int channel, double position)
/*     */   {
/*  93 */     throwIfChannelIsInvalid(channel);
/*  94 */     Range.throwIfRangeIsInvalid(position, 0.0D, 1.0D);
/*     */     
/*  96 */     byte newPosition = (byte)(int)(position * 240.0D);
/*     */     
/*  98 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)channel, newPosition, (byte)0);
/*  99 */     this.master.queueTransaction(transaction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServoPosition(int channel, double position, byte speed)
/*     */   {
/* 111 */     throwIfChannelIsInvalid(channel);
/* 112 */     Range.throwIfRangeIsInvalid(position, 0.0D, 1.0D);
/*     */     
/* 114 */     byte newPosition = (byte)(int)(position * 240.0D);
/*     */     
/* 116 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)channel, newPosition, speed);
/* 117 */     this.master.queueTransaction(transaction);
/*     */   }
/*     */   
/*     */ 
/*     */   public double getServoPosition(int channel)
/*     */   {
/* 123 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)channel, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_SERVO);
/*     */     
/* 125 */     if (this.master.queueTransaction(transaction)) {
/* 126 */       this.master.waitOnRead();
/*     */     }
/*     */     
/* 129 */     return this.servoCache[channel] / 240.0D;
/*     */   }
/*     */   
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 134 */     return HardwareDevice.Manufacturer.Matrix;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 140 */     return "Matrix Servo Controller";
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 146 */     return this.master.getConnectionInfo();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getVersion()
/*     */   {
/* 152 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode()
/*     */   {
/* 158 */     pwmDisable();
/*     */   }
/*     */   
/*     */ 
/*     */   public void close()
/*     */   {
/* 164 */     pwmDisable();
/*     */   }
/*     */   
/*     */   private void throwIfChannelIsInvalid(int channel) {
/* 168 */     if ((channel < 1) || (channel > 4)) {
/* 169 */       throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", new Object[] { Integer.valueOf(channel), Byte.valueOf(4) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleReadServo(MatrixI2cTransaction transaction, byte[] buffer)
/*     */   {
/* 182 */     this.servoCache[transaction.servo] = TypeConversion.unsignedByteToInt(buffer[4]);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\matrix\MatrixServoController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */