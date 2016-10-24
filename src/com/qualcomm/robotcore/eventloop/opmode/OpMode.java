/*     */ package com.qualcomm.robotcore.eventloop.opmode;
/*     */ 
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.robocol.TelemetryMessage;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.firstinspires.ftc.robotcore.external.Telemetry;
/*     */ import org.firstinspires.ftc.robotcore.internal.TelemetryImpl;
/*     */ import org.firstinspires.ftc.robotcore.internal.TelemetryInternal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OpMode
/*     */ {
/*  50 */   OpModeServices opModeServices = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   public Gamepad gamepad1 = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   public Gamepad gamepad2 = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   public Telemetry telemetry = new TelemetryImpl(this);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  72 */   public HardwareMap hardwareMap = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   public double time = 0.0D;
/*     */   
/*     */ 
/*  81 */   private long startTime = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpMode()
/*     */   {
/*  90 */     this.startTime = System.nanoTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void init();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init_loop() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void loop();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stop() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void requestOpModeStop()
/*     */   {
/* 144 */     this.opModeServices.requestOpModeStop(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getRuntime()
/*     */   {
/* 154 */     double NANOSECONDS_PER_SECOND = TimeUnit.SECONDS.toNanos(1L);
/* 155 */     return (System.nanoTime() - this.startTime) / NANOSECONDS_PER_SECOND;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetStartTime()
/*     */   {
/* 162 */     this.startTime = System.nanoTime();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTelemetry(Telemetry telemetry)
/*     */   {
/* 179 */     telemetry.update();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final void updateTelemetryNow(TelemetryMessage telemetry)
/*     */   {
/* 190 */     this.opModeServices.refreshUserTelemetry(telemetry, 0.0D);
/*     */   }
/*     */   
/*     */   protected void preInit()
/*     */   {
/* 195 */     if ((this.telemetry instanceof TelemetryInternal)) {
/* 196 */       ((TelemetryInternal)this.telemetry).resetTelemetryForOpMode();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void postInitLoop()
/*     */   {
/* 202 */     this.telemetry.update();
/*     */   }
/*     */   
/*     */   protected void postLoop()
/*     */   {
/* 207 */     this.telemetry.update();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\opmode\OpMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */