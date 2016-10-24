/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.ArmableUsbDevice.OpenRobotUsbDevice;
/*     */ import com.qualcomm.hardware.HardwareFactory;
/*     */ import com.qualcomm.hardware.R.string;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
/*     */ import com.qualcomm.robotcore.hardware.ServoController;
/*     */ import com.qualcomm.robotcore.hardware.ServoController.PwmStatus;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.FirmwareVersion;
/*     */ import com.qualcomm.robotcore.util.LastKnown;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
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
/*     */ public final class ModernRoboticsUsbServoController
/*     */   extends ModernRoboticsUsbController
/*     */   implements ServoController
/*     */ {
/*     */   public static final boolean DEBUG_LOGGING = false;
/*     */   public static final int MONITOR_LENGTH = 9;
/*     */   protected static final int SERVO_FIRST = 1;
/*     */   protected static final int SERVO_LAST = 6;
/*     */   public static final byte PWM_ENABLE = 0;
/*     */   public static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
/*     */   public static final byte PWM_DISABLE = -1;
/*     */   public static final byte START_ADDRESS = 64;
/*     */   public static final int ADDRESS_CHANNEL1 = 66;
/*     */   public static final int ADDRESS_CHANNEL2 = 67;
/*     */   public static final int ADDRESS_CHANNEL3 = 68;
/*     */   public static final int ADDRESS_CHANNEL4 = 69;
/*     */   public static final int ADDRESS_CHANNEL5 = 70;
/*     */   public static final int ADDRESS_CHANNEL6 = 71;
/*     */   public static final int ADDRESS_PWM = 72;
/*     */   public static final int ADDRESS_UNUSED = -1;
/* 129 */   public static final byte[] ADDRESS_CHANNEL_MAP = { -1, 66, 67, 68, 69, 70, 71 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final double apiPositionMin = 0.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final double apiPositionMax = 1.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final double servoPositionMin = 0.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final double servoPositionMax = 255.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LastKnown<Double>[] commandedServoPositions;
/*     */   
/*     */ 
/*     */ 
/*     */   protected LastKnown<Boolean> lastKnownPwmEnabled;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModernRoboticsUsbServoController(Context context, final SerialNumber serialNumber, ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice, EventLoopManager manager)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 166 */     super(context, serialNumber, manager, openRobotUsbDevice, new ModernRoboticsUsbDevice.CreateReadWriteRunnable()
/*     */     {
/*     */       public ReadWriteRunnable create(RobotUsbDevice device)
/*     */       {
/* 170 */         return new ReadWriteRunnableStandard(ModernRoboticsUsbServoController.this, serialNumber, device, 9, 64, false);
/*     */       }
/*     */       
/* 173 */     });
/* 174 */     this.commandedServoPositions = LastKnown.createArray(ADDRESS_CHANNEL_MAP.length);
/* 175 */     this.lastKnownPwmEnabled = new LastKnown();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initializeHardware()
/*     */   {
/* 182 */     floatHardware();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doArm()
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 191 */     doArmOrPretend(true);
/*     */   }
/*     */   
/*     */   protected void doPretend() throws RobotCoreException, InterruptedException {
/* 195 */     doArmOrPretend(false);
/*     */   }
/*     */   
/*     */   private void doArmOrPretend(boolean isArm) throws RobotCoreException, InterruptedException
/*     */   {
/* 200 */     RobotLog.d("arming modern servo controller %s%s...", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber), isArm ? "" : " (pretend)" });
/*     */     
/* 202 */     if (isArm) {
/* 203 */       armDevice();
/*     */     } else {
/* 205 */       pretendDevice();
/*     */     }
/* 207 */     RobotLog.d("...arming modern servo controller %s complete", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
/*     */   }
/*     */   
/*     */   protected void doDisarm() throws RobotCoreException, InterruptedException
/*     */   {
/* 212 */     RobotLog.d("disarming modern servo controller \"%s\"...", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
/*     */     
/* 214 */     disarmDevice();
/*     */     
/* 216 */     RobotLog.d("...disarming modern servo controller %s complete", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
/*     */   }
/*     */   
/*     */   protected void doCloseFromArmed()
/*     */   {
/* 221 */     floatHardware();
/* 222 */     doCloseFromOther();
/*     */   }
/*     */   
/*     */   protected void doCloseFromOther()
/*     */   {
/*     */     try
/*     */     {
/* 229 */       doDisarm();
/*     */     }
/*     */     catch (InterruptedException e)
/*     */     {
/* 233 */       Thread.currentThread().interrupt();
/*     */     }
/*     */     catch (RobotCoreException localRobotCoreException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/* 247 */     return HardwareDevice.Manufacturer.ModernRobotics;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/* 253 */     RobotUsbDevice.FirmwareVersion version = this.robotUsbDevice.getFirmwareVersion();
/* 254 */     if (version != null)
/*     */     {
/* 256 */       return String.format("%s v%d.%d", new Object[] { this.context.getString(R.string.moduleDisplayNameServoController), Integer.valueOf(version.majorVersion), Integer.valueOf(version.minorVersion) });
/*     */     }
/*     */     
/*     */ 
/* 260 */     return this.context.getString(R.string.moduleDisplayNameServoController);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/* 267 */     return "USB " + getSerialNumber();
/*     */   }
/*     */   
/*     */ 
/*     */   public void resetDeviceConfigurationForOpMode()
/*     */   {
/* 273 */     floatHardware();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void pwmEnable()
/*     */   {
/* 283 */     if (this.lastKnownPwmEnabled.updateValue(Boolean.valueOf(true)))
/*     */     {
/* 285 */       write8(72, (byte)0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void pwmDisable()
/*     */   {
/* 292 */     if (this.lastKnownPwmEnabled.updateValue(Boolean.valueOf(false)))
/*     */     {
/* 294 */       write8(72, (byte)-1);
/*     */       
/*     */ 
/* 297 */       for (LastKnown<Double> commandedPosition : this.commandedServoPositions)
/*     */       {
/* 299 */         commandedPosition.invalidate();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized ServoController.PwmStatus getPwmStatus()
/*     */   {
/* 307 */     byte[] resp = read(72, 1);
/* 308 */     if (resp[0] == -1)
/*     */     {
/* 310 */       return ServoController.PwmStatus.DISABLED;
/*     */     }
/*     */     
/*     */ 
/* 314 */     return ServoController.PwmStatus.ENABLED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized void setServoPosition(int servo, double position)
/*     */   {
/* 321 */     validateServo(servo);
/* 322 */     position = Range.clip(position, 0.0D, 1.0D);
/* 323 */     validateApiPosition(position);
/*     */     
/*     */ 
/* 326 */     if (this.commandedServoPositions[servo].updateValue(Double.valueOf(position)))
/*     */     {
/* 328 */       byte bPosition = (byte)(int)Range.scale(position, 0.0D, 1.0D, 0.0D, 255.0D);
/* 329 */       write8(ADDRESS_CHANNEL_MAP[servo], bPosition);
/* 330 */       pwmEnable();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized double getServoPosition(int servo)
/*     */   {
/* 338 */     validateServo(servo);
/* 339 */     Double commanded = (Double)this.commandedServoPositions[servo].getRawValue();
/* 340 */     return commanded == null ? NaN.0D : commanded.doubleValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void floatHardware()
/*     */   {
/* 349 */     pwmDisable();
/*     */   }
/*     */   
/*     */   private void validateServo(int servo)
/*     */   {
/* 354 */     if ((servo < 1) || (servo > 6))
/*     */     {
/* 356 */       throw new IllegalArgumentException(String.format("Servo %d is invalid; valid servos are %d..%d", new Object[] { Integer.valueOf(servo), Integer.valueOf(1), Integer.valueOf(6) }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateApiPosition(double position)
/*     */   {
/* 362 */     if ((0.0D > position) || (position > 1.0D))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 367 */       throw new IllegalArgumentException(String.format("illegal servo position %f; must be in interval [%f,%f]", new Object[] { Double.valueOf(position), Double.valueOf(0.0D), Double.valueOf(1.0D) }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbServoController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */