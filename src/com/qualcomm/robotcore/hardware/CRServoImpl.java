/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CRServoImpl
/*     */   implements CRServo
/*     */ {
/*  15 */   protected ServoController controller = null;
/*  16 */   protected int portNumber = -1;
/*  17 */   protected DcMotorSimple.Direction direction = DcMotorSimple.Direction.FORWARD;
/*     */   
/*     */ 
/*     */ 
/*     */   protected static final double apiPowerMin = -1.0D;
/*     */   
/*     */ 
/*     */   protected static final double apiPowerMax = 1.0D;
/*     */   
/*     */ 
/*     */   protected static final double apiServoPositionMin = 0.0D;
/*     */   
/*     */ 
/*     */   protected static final double apiServoPositionMax = 1.0D;
/*     */   
/*     */ 
/*     */ 
/*     */   public CRServoImpl(ServoController controller, int portNumber)
/*     */   {
/*  36 */     this(controller, portNumber, DcMotorSimple.Direction.FORWARD);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CRServoImpl(ServoController controller, int portNumber, DcMotorSimple.Direction direction)
/*     */   {
/*  48 */     this.direction = direction;
/*  49 */     this.controller = controller;
/*  50 */     this.portNumber = portNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/*  60 */     return this.controller.getManufacturer();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDeviceName()
/*     */   {
/*  66 */     return "Continuous Rotation Servo";
/*     */   }
/*     */   
/*     */ 
/*     */   public String getConnectionInfo()
/*     */   {
/*  72 */     return this.controller.getConnectionInfo() + "; port " + this.portNumber;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getVersion()
/*     */   {
/*  78 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void resetDeviceConfigurationForOpMode()
/*     */   {
/*  84 */     this.direction = DcMotorSimple.Direction.FORWARD;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServoController getController()
/*     */   {
/* 100 */     return this.controller;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getPortNumber()
/*     */   {
/* 106 */     return this.portNumber;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void setDirection(DcMotorSimple.Direction direction)
/*     */   {
/* 112 */     this.direction = direction;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized DcMotorSimple.Direction getDirection()
/*     */   {
/* 118 */     return this.direction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPower(double power)
/*     */   {
/* 130 */     if (this.direction == DcMotorSimple.Direction.REVERSE) power = -power;
/* 131 */     power = Range.clip(power, -1.0D, 1.0D);
/* 132 */     power = Range.scale(power, -1.0D, 1.0D, 0.0D, 1.0D);
/* 133 */     this.controller.setServoPosition(this.portNumber, power);
/*     */   }
/*     */   
/*     */ 
/*     */   public double getPower()
/*     */   {
/* 139 */     double power = this.controller.getServoPosition(this.portNumber);
/* 140 */     power = Range.scale(power, 0.0D, 1.0D, -1.0D, 1.0D);
/* 141 */     if (this.direction == DcMotorSimple.Direction.REVERSE) power = -power;
/* 142 */     return power;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\CRServoImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */