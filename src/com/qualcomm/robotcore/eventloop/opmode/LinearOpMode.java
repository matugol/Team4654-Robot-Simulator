/*     */ package com.qualcomm.robotcore.eventloop.opmode;
/*     */ 
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public abstract class LinearOpMode
/*     */   extends OpMode
/*     */ {
/*  25 */   private LinearOpModeHelper helper = null;
/*  26 */   private ExecutorService executorService = null;
/*  27 */   private volatile boolean isStarted = false;
/*  28 */   private volatile boolean stopRequested = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void runOpMode()
/*     */     throws InterruptedException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void waitForStart()
/*     */     throws InterruptedException
/*     */   {
/*  63 */     while (!this.isStarted) {
/*  64 */       synchronized (this) {
/*  65 */         wait();
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void waitOneFullHardwareCycle()
/*     */     throws InterruptedException
/*     */   {
/*  93 */     waitForNextHardwareCycle();
/*     */     
/*     */ 
/*  96 */     Thread.sleep(1L);
/*     */     
/*     */ 
/*  99 */     waitForNextHardwareCycle();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void waitForNextHardwareCycle()
/*     */     throws InterruptedException
/*     */   {
/* 131 */     synchronized (this) {
/* 132 */       wait();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void idle()
/*     */     throws InterruptedException
/*     */   {
/* 153 */     if (isStopRequested()) {
/* 154 */       throw new InterruptedException();
/*     */     }
/*     */     
/*     */ 
/* 158 */     Thread.yield();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void sleep(long milliseconds)
/*     */     throws InterruptedException
/*     */   {
/* 170 */     Thread.sleep(milliseconds);
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
/*     */   public final boolean opModeIsActive()
/*     */   {
/* 184 */     return (!isStopRequested()) && (isStarted());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isStarted()
/*     */   {
/* 195 */     return this.isStarted;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isStopRequested()
/*     */   {
/* 206 */     return (this.stopRequested) || (Thread.currentThread().isInterrupted());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void init()
/*     */   {
/* 214 */     this.executorService = ThreadPool.newSingleThreadExecutor();
/* 215 */     this.helper = new LinearOpModeHelper();
/* 216 */     this.isStarted = false;
/* 217 */     this.stopRequested = false;
/*     */     
/* 219 */     this.executorService.execute(this.helper);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void init_loop()
/*     */   {
/* 227 */     handleLoop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void start()
/*     */   {
/* 235 */     this.stopRequested = false;
/* 236 */     this.isStarted = true;
/* 237 */     synchronized (this) {
/* 238 */       notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void loop()
/*     */   {
/* 247 */     handleLoop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void stop()
/*     */   {
/* 257 */     this.stopRequested = true;
/*     */     
/* 259 */     if (this.executorService != null)
/*     */     {
/*     */ 
/* 262 */       this.executorService.shutdownNow();
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 267 */         String serviceName = "user linear op mode";
/* 268 */         ThreadPool.awaitTermination(this.executorService, 100L, TimeUnit.DAYS, serviceName);
/*     */       } catch (InterruptedException e) {
/* 270 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void handleLoop()
/*     */   {
/* 278 */     if (this.helper.hasRuntimeException()) {
/* 279 */       throw this.helper.getRuntimeException();
/*     */     }
/*     */     
/* 282 */     synchronized (this) {
/* 283 */       notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */   protected class LinearOpModeHelper implements Runnable
/*     */   {
/* 289 */     protected RuntimeException exception = null;
/* 290 */     protected boolean isShutdown = false;
/*     */     
/*     */ 
/*     */     public LinearOpModeHelper() {}
/*     */     
/*     */     public void run()
/*     */     {
/* 297 */       ThreadPool.logThreadLifeCycle("LinearOpMode main", new Runnable() {
/* 298 */         public void run() { LinearOpMode.LinearOpModeHelper.this.exception = null;
/* 299 */           LinearOpMode.LinearOpModeHelper.this.isShutdown = false;
/*     */           try
/*     */           {
/* 302 */             LinearOpMode.this.runOpMode();
/* 303 */             LinearOpMode.this.requestOpModeStop();
/*     */           }
/*     */           catch (InterruptedException ie) {
/* 306 */             RobotLog.d("LinearOpMode received an InterruptedException; shutting down this linear op mode");
/*     */ 
/*     */ 
/*     */           }
/*     */           catch (CancellationException ie)
/*     */           {
/*     */ 
/* 313 */             RobotLog.d("LinearOpMode received a CancellationException; shutting down this linear op mode");
/*     */           } catch (RuntimeException e) {
/* 315 */             LinearOpMode.LinearOpModeHelper.this.exception = e;
/*     */           } finally {
/* 317 */             LinearOpMode.LinearOpModeHelper.this.isShutdown = true;
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 323 */     public boolean hasRuntimeException() { return this.exception != null; }
/*     */     
/*     */     public RuntimeException getRuntimeException()
/*     */     {
/* 327 */       return this.exception;
/*     */     }
/*     */     
/*     */     public boolean isShutdown() {
/* 331 */       return this.isShutdown;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void postInitLoop()
/*     */   {
/* 341 */     if ((this.telemetry instanceof TelemetryInternal)) {
/* 342 */       ((TelemetryInternal)this.telemetry).tryUpdateIfDirty();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void postLoop()
/*     */   {
/* 348 */     if ((this.telemetry instanceof TelemetryInternal)) {
/* 349 */       ((TelemetryInternal)this.telemetry).tryUpdateIfDirty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\opmode\LinearOpMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */