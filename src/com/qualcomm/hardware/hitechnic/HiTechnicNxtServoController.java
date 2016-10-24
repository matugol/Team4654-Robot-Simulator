/*     */ package com.qualcomm.hardware.hitechnic;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.R.string;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.I2cAddr;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.HeartbeatAction;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadMode;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadWindow;
/*     */ import com.qualcomm.robotcore.hardware.ServoController;
/*     */ import com.qualcomm.robotcore.hardware.ServoController.PwmStatus;
/*     */ import com.qualcomm.robotcore.util.LastKnown;
/*     */ import com.qualcomm.robotcore.util.Range;
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
/*     */ public final class HiTechnicNxtServoController
/*     */   extends HiTechnicNxtController
/*     */   implements ServoController
/*     */ {
/*  84 */   protected static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
/*     */   
/*     */   protected static final int SERVO_FIRST = 1;
/*     */   
/*     */   protected static final int SERVO_LAST = 6;
/*     */   protected static final byte PWM_ENABLE = 0;
/*     */   protected static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
/*     */   protected static final byte PWM_DISABLE = -1;
/*  92 */   protected static final byte[] ADDRESS_CHANNEL_MAP = { -1, 66, 67, 68, 69, 70, 71 };
/*     */   
/*     */ 
/*     */   protected static final int ADDRESS_PWM = 72;
/*     */   
/*     */   protected static final int iRegWindowFirst = 64;
/*     */   
/*     */   protected static final int iRegWindowMax = 73;
/*     */   
/*     */   protected static final double apiPositionMin = 0.0D;
/*     */   
/*     */   protected static final double apiPositionMax = 1.0D;
/*     */   
/*     */   protected static final double servoPositionMin = 0.0D;
/*     */   
/*     */   protected static final double servoPositionMax = 255.0D;
/*     */   
/*     */   protected LastKnown<Double>[] commandedServoPositions;
/*     */   
/*     */   protected LastKnown<Boolean> lastKnownPwmEnabled;
/*     */   
/*     */ 
/*     */   public HiTechnicNxtServoController(Context context, I2cController module, int physicalPort)
/*     */   {
/* 116 */     super(context, module, physicalPort, I2C_ADDRESS);
/* 117 */     this.commandedServoPositions = LastKnown.createArray(ADDRESS_CHANNEL_MAP.length);
/* 118 */     this.lastKnownPwmEnabled = new LastKnown();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 123 */     I2cDeviceSynch.HeartbeatAction heartbeatAction = new I2cDeviceSynch.HeartbeatAction(true, true, new I2cDeviceSynch.ReadWindow(ADDRESS_CHANNEL_MAP[1], 1, I2cDeviceSynch.ReadMode.ONLY_ONCE));
/*     */     
/*     */ 
/*     */ 
/* 127 */     this.i2cDeviceSynch.setHeartbeatAction(heartbeatAction);
/* 128 */     this.i2cDeviceSynch.setHeartbeatInterval(9000);
/* 129 */     this.i2cDeviceSynch.enableWriteCoalescing(true);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */     this.i2cDeviceSynch.setReadWindow(new I2cDeviceSynch.ReadWindow(64, 9, I2cDeviceSynch.ReadMode.BALANCED));
/*     */     
/* 138 */     finishConstruction();
/*     */   }
/*     */   
/*     */   protected void controllerNowArmedOrPretending()
/*     */   {
/* 143 */     adjustHookingToMatchEngagement();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doHook()
/*     */   {
/* 149 */     this.i2cDeviceSynch.engage();
/*     */   }
/*     */   
/*     */ 
/*     */   public void initializeHardware()
/*     */   {
/* 155 */     pwmDisable();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doUnhook()
/*     */   {
/* 161 */     this.i2cDeviceSynch.disengage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 170 */     return HardwareDevice.Manufacturer.HiTechnic;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 176 */     return this.context.getString(R.string.nxtServoControllerName);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 182 */     return String.format(this.context.getString(R.string.controllerPortConnectionInfoFormat), new Object[] { this.controller.getConnectionInfo(), Integer.valueOf(this.physicalPort) });
/*     */   }
/*     */   
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode()
/*     */   {
/* 188 */     floatHardware();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getVersion()
/*     */   {
/* 194 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void pwmEnable()
/*     */   {
/* 205 */     if (this.lastKnownPwmEnabled.updateValue(Boolean.valueOf(true)))
/*     */     {
/* 207 */       write8(72, (byte)0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void pwmDisable()
/*     */   {
/* 214 */     if (this.lastKnownPwmEnabled.updateValue(Boolean.valueOf(false)))
/*     */     {
/* 216 */       write8(72, (byte)-1);
/*     */       
/*     */ 
/* 219 */       for (LastKnown<Double> commandedPosition : this.commandedServoPositions)
/*     */       {
/* 221 */         commandedPosition.invalidate();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized ServoController.PwmStatus getPwmStatus()
/*     */   {
/* 229 */     return read8(72) == -1 ? ServoController.PwmStatus.DISABLED : ServoController.PwmStatus.ENABLED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setServoPosition(int servo, double position)
/*     */   {
/* 237 */     validateServo(servo);
/* 238 */     position = Range.clip(position, 0.0D, 1.0D);
/* 239 */     validateApiPosition(position);
/*     */     
/*     */ 
/* 242 */     if (this.commandedServoPositions[servo].updateValue(Double.valueOf(position)))
/*     */     {
/* 244 */       byte bPosition = (byte)(int)Range.scale(position, 0.0D, 1.0D, 0.0D, 255.0D);
/* 245 */       write8(ADDRESS_CHANNEL_MAP[servo], bPosition);
/* 246 */       pwmEnable();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized double getServoPosition(int servo)
/*     */   {
/* 254 */     validateServo(servo);
/* 255 */     Double commanded = (Double)this.commandedServoPositions[servo].getRawValue();
/* 256 */     return commanded == null ? NaN.0D : commanded.doubleValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void floatHardware()
/*     */   {
/* 265 */     pwmDisable();
/*     */   }
/*     */   
/*     */   private void validateServo(int servo)
/*     */   {
/* 270 */     if ((servo < 1) || (servo > 6))
/*     */     {
/* 272 */       throw new IllegalArgumentException(String.format("Servo %d is invalid; valid servos are %d..%d", new Object[] { Integer.valueOf(servo), Integer.valueOf(1), Integer.valueOf(6) }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateApiPosition(double position)
/*     */   {
/* 278 */     if ((0.0D > position) || (position > 1.0D))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 283 */       throw new IllegalArgumentException(String.format("illegal servo position %f; must be in interval [%f,%f]", new Object[] { Double.valueOf(position), Double.valueOf(0.0D), Double.valueOf(1.0D) }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtServoController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */