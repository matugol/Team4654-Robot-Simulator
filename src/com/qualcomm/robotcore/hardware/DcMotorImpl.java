/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DcMotorImpl
/*     */   implements DcMotor
/*     */ {
/*  46 */   protected DcMotorController controller = null;
/*  47 */   protected int portNumber = -1;
/*  48 */   protected DcMotorSimple.Direction direction = DcMotorSimple.Direction.FORWARD;
/*  49 */   protected DcMotor.RunMode mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DcMotorImpl(DcMotorController controller, int portNumber)
/*     */   {
/*  62 */     this(controller, portNumber, DcMotorSimple.Direction.FORWARD);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DcMotorImpl(DcMotorController controller, int portNumber, DcMotorSimple.Direction direction)
/*     */   {
/*  73 */     this.controller = controller;
/*  74 */     this.portNumber = portNumber;
/*  75 */     this.direction = direction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/*  83 */     return this.controller.getManufacturer();
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/*  88 */     return "DC Motor";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/*  93 */     return this.controller.getConnectionInfo() + "; port " + this.portNumber;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/*  98 */     return 1;
/*     */   }
/*     */   
/*     */   public void resetDeviceConfigurationForOpMode()
/*     */   {
/* 103 */     setDirection(DcMotorSimple.Direction.FORWARD);
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 108 */     setPowerFloat();
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
/*     */   public DcMotorController getController()
/*     */   {
/* 121 */     return this.controller;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setDirection(DcMotorSimple.Direction direction)
/*     */   {
/* 130 */     this.direction = direction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DcMotorSimple.Direction getDirection()
/*     */   {
/* 138 */     return this.direction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPortNumber()
/*     */   {
/* 147 */     return this.portNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setPower(double power)
/*     */   {
/* 156 */     if (this.direction == DcMotorSimple.Direction.REVERSE) { power = -power;
/*     */     }
/* 158 */     if (this.mode == DcMotor.RunMode.RUN_TO_POSITION) power = Math.abs(power);
/* 159 */     internalSetPower(power);
/*     */   }
/*     */   
/*     */   protected void internalSetPower(double power) {
/* 163 */     this.controller.setMotorPower(this.portNumber, power);
/*     */   }
/*     */   
/*     */   public void setMaxSpeed(int encoderTicksPerSecond)
/*     */   {
/* 168 */     this.controller.setMotorMaxSpeed(this.portNumber, encoderTicksPerSecond);
/*     */   }
/*     */   
/*     */   public int getMaxSpeed()
/*     */   {
/* 173 */     return this.controller.getMotorMaxSpeed(this.portNumber);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized double getPower()
/*     */   {
/* 182 */     double power = this.controller.getMotorPower(this.portNumber);
/* 183 */     if (this.direction == DcMotorSimple.Direction.REVERSE) power = -power;
/* 184 */     return power;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBusy()
/*     */   {
/* 193 */     return this.controller.isBusy(this.portNumber);
/*     */   }
/*     */   
/*     */   public synchronized void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior)
/*     */   {
/* 198 */     this.controller.setMotorZeroPowerBehavior(this.portNumber, zeroPowerBehavior);
/*     */   }
/*     */   
/*     */   public synchronized DcMotor.ZeroPowerBehavior getZeroPowerBehavior()
/*     */   {
/* 203 */     return this.controller.getMotorZeroPowerBehavior(this.portNumber);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void setPowerFloat()
/*     */   {
/* 211 */     setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
/* 212 */     setPower(0.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean getPowerFloat()
/*     */   {
/* 221 */     return (getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.FLOAT) && (getPower() == 0.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setTargetPosition(int position)
/*     */   {
/* 232 */     if (this.direction == DcMotorSimple.Direction.REVERSE) position *= -1;
/* 233 */     internalSetTargetPosition(position);
/*     */   }
/*     */   
/*     */   protected void internalSetTargetPosition(int position) {
/* 237 */     this.controller.setMotorTargetPosition(this.portNumber, position);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized int getTargetPosition()
/*     */   {
/* 247 */     int position = this.controller.getMotorTargetPosition(this.portNumber);
/* 248 */     if (this.direction == DcMotorSimple.Direction.REVERSE) position *= -1;
/* 249 */     return position;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized int getCurrentPosition()
/*     */   {
/* 259 */     int position = this.controller.getMotorCurrentPosition(this.portNumber);
/* 260 */     if (this.direction == DcMotorSimple.Direction.REVERSE) position *= -1;
/* 261 */     return position;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setMode(DcMotor.RunMode mode)
/*     */   {
/* 270 */     mode = mode.migrate();
/* 271 */     this.mode = mode;
/* 272 */     internalSetMode(mode);
/*     */   }
/*     */   
/*     */   protected void internalSetMode(DcMotor.RunMode mode) {
/* 276 */     this.controller.setMotorMode(this.portNumber, mode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DcMotor.RunMode getMode()
/*     */   {
/* 285 */     return this.controller.getMotorMode(this.portNumber);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\DcMotorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */