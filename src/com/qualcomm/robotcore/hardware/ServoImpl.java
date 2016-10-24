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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServoImpl
/*     */   implements Servo
/*     */ {
/*  45 */   protected ServoController controller = null;
/*  46 */   protected int portNumber = -1;
/*     */   
/*  48 */   protected Servo.Direction direction = Servo.Direction.FORWARD;
/*  49 */   protected double limitPositionMin = 0.0D;
/*  50 */   protected double limitPositionMax = 1.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServoImpl(ServoController controller, int portNumber)
/*     */   {
/*  62 */     this(controller, portNumber, Servo.Direction.FORWARD);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServoImpl(ServoController controller, int portNumber, Servo.Direction direction)
/*     */   {
/*  72 */     this.direction = direction;
/*  73 */     this.controller = controller;
/*  74 */     this.portNumber = portNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareDevice.Manufacturer getManufacturer()
/*     */   {
/*  82 */     return this.controller.getManufacturer();
/*     */   }
/*     */   
/*     */   public String getDeviceName()
/*     */   {
/*  87 */     return "Servo";
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/*  92 */     return this.controller.getConnectionInfo() + "; port " + this.portNumber;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/*  97 */     return 1;
/*     */   }
/*     */   
/*     */   public synchronized void resetDeviceConfigurationForOpMode()
/*     */   {
/* 102 */     this.limitPositionMin = 0.0D;
/* 103 */     this.limitPositionMax = 1.0D;
/* 104 */     this.direction = Servo.Direction.FORWARD;
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
/*     */ 
/*     */   public ServoController getController()
/*     */   {
/* 121 */     return this.controller;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setDirection(Servo.Direction direction)
/*     */   {
/* 129 */     this.direction = direction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Servo.Direction getDirection()
/*     */   {
/* 137 */     return this.direction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPortNumber()
/*     */   {
/* 145 */     return this.portNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setPosition(double position)
/*     */   {
/* 156 */     position = Range.clip(position, 0.0D, 1.0D);
/* 157 */     if (this.direction == Servo.Direction.REVERSE) position = reverse(position);
/* 158 */     double scaled = Range.scale(position, 0.0D, 1.0D, this.limitPositionMin, this.limitPositionMax);
/* 159 */     internalSetPosition(scaled);
/*     */   }
/*     */   
/*     */   protected void internalSetPosition(double position) {
/* 163 */     this.controller.setServoPosition(this.portNumber, position);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized double getPosition()
/*     */   {
/* 174 */     double position = this.controller.getServoPosition(this.portNumber);
/* 175 */     if (this.direction == Servo.Direction.REVERSE) position = reverse(position);
/* 176 */     double scaled = Range.scale(position, this.limitPositionMin, this.limitPositionMax, 0.0D, 1.0D);
/* 177 */     return Range.clip(scaled, 0.0D, 1.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized void scaleRange(double min, double max)
/*     */   {
/* 184 */     min = Range.clip(min, 0.0D, 1.0D);
/* 185 */     max = Range.clip(max, 0.0D, 1.0D);
/*     */     
/* 187 */     if (min >= max) {
/* 188 */       throw new IllegalArgumentException("min must be less than max");
/*     */     }
/*     */     
/* 191 */     this.limitPositionMin = min;
/* 192 */     this.limitPositionMax = max;
/*     */   }
/*     */   
/*     */   private double reverse(double position) {
/* 196 */     return 1.0D - position + 0.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\ServoImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */