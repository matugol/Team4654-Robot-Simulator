/*     */ package com.qualcomm.robotcore.eventloop.opmode;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Context;
/*     */ import android.os.Debug;
/*     */ import com.qualcomm.robotcore.R.string;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoop;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorSimple;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.hardware.LightSensor;
/*     */ import com.qualcomm.robotcore.hardware.ServoController;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.TelemetryMessage;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import com.qualcomm.robotcore.util.WeakReferenceSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import junit.framework.Assert;
/*     */ import org.firstinspires.ftc.robotcore.external.Telemetry;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpModeManagerImpl
/*     */   implements OpModeServices, OpModeManager, OpModeManagerNotifier
/*     */ {
/*     */   public static final String DEFAULT_OP_MODE_NAME = "$Stop$Robot$";
/*     */   
/*     */   class OpModeStateTransition
/*     */   {
/*     */     OpModeStateTransition() {}
/*     */     
/*  85 */     String queuedOpModeName = null;
/*  86 */     Boolean opModeSwapNeeded = null;
/*  87 */     Boolean callToInitNeeded = null;
/*  88 */     Boolean gamepadResetNeeded = null;
/*  89 */     Boolean telemetryClearNeeded = null;
/*  90 */     Boolean callToStartNeeded = null;
/*     */     
/*     */     void apply() {
/*  93 */       if (this.queuedOpModeName != null) { OpModeManagerImpl.this.queuedOpModeName = this.queuedOpModeName;
/*     */       }
/*     */       
/*  96 */       if (this.opModeSwapNeeded != null) OpModeManagerImpl.this.opModeSwapNeeded = this.opModeSwapNeeded.booleanValue();
/*  97 */       if (this.callToInitNeeded != null) OpModeManagerImpl.this.callToInitNeeded = this.callToInitNeeded.booleanValue();
/*  98 */       if (this.gamepadResetNeeded != null) OpModeManagerImpl.this.gamepadResetNeeded = this.gamepadResetNeeded.booleanValue();
/*  99 */       if (this.telemetryClearNeeded != null) OpModeManagerImpl.this.telemetryClearNeeded = this.telemetryClearNeeded.booleanValue();
/* 100 */       if (this.callToStartNeeded != null) OpModeManagerImpl.this.callToStartNeeded = this.callToStartNeeded.booleanValue();
/*     */     }
/*     */     
/*     */     OpModeStateTransition copy() {
/* 104 */       OpModeStateTransition result = new OpModeStateTransition(OpModeManagerImpl.this);
/* 105 */       result.queuedOpModeName = this.queuedOpModeName;
/* 106 */       result.opModeSwapNeeded = this.opModeSwapNeeded;
/* 107 */       result.callToInitNeeded = this.callToInitNeeded;
/* 108 */       result.gamepadResetNeeded = this.gamepadResetNeeded;
/* 109 */       result.telemetryClearNeeded = this.telemetryClearNeeded;
/* 110 */       result.callToStartNeeded = this.callToStartNeeded;
/* 111 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */   public static final OpMode DEFAULT_OP_MODE = new DefaultOpMode();
/*     */   
/* 122 */   protected static enum OpModeState { INIT,  LOOPING;
/*     */     
/*     */     private OpModeState() {} }
/*     */   
/*     */   protected static class OpModeObjectAndMeta { public final OpModeMeta meta;
/* 127 */     public OpModeObjectAndMeta(OpModeMeta meta, OpMode opMode) { this.meta = meta;this.opMode = opMode; }
/*     */     
/*     */     public final OpMode opMode; }
/*     */   
/* 131 */   protected Context context; protected Map<String, OpModeAndMeta> opModeClasses = new LinkedHashMap();
/* 132 */   protected Map<String, OpModeObjectAndMeta> opModeObjects = new LinkedHashMap();
/* 133 */   protected volatile boolean opmodesAreRegistered = false;
/* 134 */   protected String activeOpModeName = "$Stop$Robot$";
/* 135 */   protected OpMode activeOpMode = DEFAULT_OP_MODE;
/* 136 */   protected String queuedOpModeName = "$Stop$Robot$";
/* 137 */   protected HardwareMap hardwareMap = null;
/* 138 */   protected EventLoopManager eventLoopManager = null;
/* 139 */   protected final WeakReferenceSet<OpModeManagerNotifier.Notifications> listeners = new WeakReferenceSet();
/* 140 */   protected OpModeStuckCodeMonitor stuckMonitor = null;
/*     */   
/* 142 */   protected OpModeState opModeState = OpModeState.INIT;
/* 143 */   protected boolean opModeSwapNeeded = false;
/* 144 */   protected boolean callToInitNeeded = false;
/* 145 */   protected boolean callToStartNeeded = false;
/* 146 */   protected boolean gamepadResetNeeded = false;
/* 147 */   protected boolean telemetryClearNeeded = false;
/* 148 */   protected AtomicReference<OpModeStateTransition> nextOpModeState = new AtomicReference(null);
/*     */   
/* 150 */   protected static final WeakHashMap<Activity, OpModeManagerImpl> mapActivityToOpModeManager = new WeakHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpModeManagerImpl(Activity activity, HardwareMap hardwareMap)
/*     */   {
/* 158 */     this.hardwareMap = hardwareMap;
/*     */     
/*     */ 
/* 161 */     register("$Stop$Robot$", DefaultOpMode.class);
/*     */     
/*     */ 
/* 164 */     initActiveOpMode("$Stop$Robot$");
/*     */     
/* 166 */     this.context = activity;
/* 167 */     synchronized (mapActivityToOpModeManager) {
/* 168 */       mapActivityToOpModeManager.put(activity, this);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static OpModeManagerImpl getOpModeManagerOfActivity(Activity activity)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 32	com/qualcomm/robotcore/eventloop/opmode/OpModeManagerImpl:mapActivityToOpModeManager	Ljava/util/WeakHashMap;
/*     */     //   3: dup
/*     */     //   4: astore_1
/*     */     //   5: monitorenter
/*     */     //   6: getstatic 32	com/qualcomm/robotcore/eventloop/opmode/OpModeManagerImpl:mapActivityToOpModeManager	Ljava/util/WeakHashMap;
/*     */     //   9: aload_0
/*     */     //   10: invokevirtual 34	java/util/WeakHashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   13: checkcast 35	com/qualcomm/robotcore/eventloop/opmode/OpModeManagerImpl
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: areturn
/*     */     //   19: astore_2
/*     */     //   20: aload_1
/*     */     //   21: monitorexit
/*     */     //   22: aload_2
/*     */     //   23: athrow
/*     */     // Line number table:
/*     */     //   Java source line #174	-> byte code offset #0
/*     */     //   Java source line #175	-> byte code offset #6
/*     */     //   Java source line #176	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	24	0	activity	Activity
/*     */     //   4	17	1	Ljava/lang/Object;	Object
/*     */     //   19	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   6	18	19	finally
/*     */     //   19	22	19	finally
/*     */   }
/*     */   
/*     */   public void init(EventLoopManager eventLoopManager)
/*     */   {
/* 181 */     this.stuckMonitor = new OpModeStuckCodeMonitor();
/* 182 */     this.eventLoopManager = eventLoopManager;
/*     */   }
/*     */   
/*     */   public void teardown() {
/* 186 */     this.stuckMonitor.shutdown();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpMode registerListener(OpModeManagerNotifier.Notifications listener)
/*     */   {
/* 195 */     synchronized (this.listeners) {
/* 196 */       this.listeners.add(listener);
/* 197 */       return this.activeOpMode;
/*     */     }
/*     */   }
/*     */   
/*     */   public void unregisterListener(OpModeManagerNotifier.Notifications listener)
/*     */   {
/* 203 */     synchronized (this.listeners) {
/* 204 */       this.listeners.remove(listener);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setActiveOpMode(OpMode opMode, String activeOpModeName) {
/* 209 */     synchronized (this.listeners) {
/* 210 */       this.activeOpMode = opMode;
/* 211 */       this.activeOpModeName = activeOpModeName;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerOpModes(OpModeRegister register)
/*     */   {
/* 223 */     register.register(this);
/* 224 */     this.opmodesAreRegistered = true;
/*     */   }
/*     */   
/*     */   public boolean areOpModesRegistered() {
/* 228 */     return this.opmodesAreRegistered;
/*     */   }
/*     */   
/*     */   public void setHardwareMap(HardwareMap hardwareMap)
/*     */   {
/* 233 */     this.hardwareMap = hardwareMap;
/*     */   }
/*     */   
/*     */   public HardwareMap getHardwareMap() {
/* 237 */     return this.hardwareMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<OpModeMeta> getOpModes()
/*     */   {
/* 246 */     Assert.assertTrue(this.opmodesAreRegistered);
/* 247 */     List<OpModeMeta> result = new LinkedList();
/* 248 */     for (OpModeAndMeta opModeAndMetaData : this.opModeClasses.values()) {
/* 249 */       if (!opModeAndMetaData.meta.name.equals("$Stop$Robot$")) {
/* 250 */         result.add(opModeAndMetaData.meta);
/*     */       }
/*     */     }
/* 253 */     for (OpModeObjectAndMeta opModeObjectAndMeta : this.opModeObjects.values()) {
/* 254 */       result.add(opModeObjectAndMeta.meta);
/*     */     }
/* 256 */     return result;
/*     */   }
/*     */   
/*     */   private boolean isOpModeRegistered(String name) {
/* 260 */     return (this.opModeClasses.containsKey(name)) || (this.opModeObjects.containsKey(name));
/*     */   }
/*     */   
/*     */   public String getActiveOpModeName() {
/* 264 */     return this.activeOpModeName;
/*     */   }
/*     */   
/*     */   public OpMode getActiveOpMode() {
/* 268 */     return this.activeOpMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initActiveOpMode(String name)
/*     */   {
/* 275 */     OpModeStateTransition newState = new OpModeStateTransition();
/* 276 */     newState.queuedOpModeName = name;
/* 277 */     newState.opModeSwapNeeded = Boolean.valueOf(true);
/* 278 */     newState.callToInitNeeded = Boolean.valueOf(true);
/* 279 */     newState.gamepadResetNeeded = Boolean.valueOf(true);
/* 280 */     newState.telemetryClearNeeded = Boolean.valueOf(!name.equals("$Stop$Robot$"));
/* 281 */     newState.callToStartNeeded = Boolean.valueOf(false);
/*     */     
/*     */ 
/* 284 */     this.nextOpModeState.set(newState);
/*     */   }
/*     */   
/*     */ 
/*     */   public void startActiveOpMode()
/*     */   {
/* 290 */     OpModeStateTransition existingState = null;
/*     */     for (;;) { OpModeStateTransition newState;
/*     */       OpModeStateTransition newState;
/* 293 */       if (existingState != null) {
/* 294 */         newState = existingState.copy();
/*     */       } else {
/* 296 */         newState = new OpModeStateTransition();
/*     */       }
/* 298 */       newState.callToStartNeeded = Boolean.valueOf(true);
/* 299 */       if (this.nextOpModeState.compareAndSet(existingState, newState))
/*     */         break;
/* 301 */       Thread.yield();
/* 302 */       existingState = (OpModeStateTransition)this.nextOpModeState.get();
/*     */     }
/*     */   }
/*     */   
/*     */   public void stopActiveOpMode()
/*     */   {
/* 308 */     callActiveOpModeStop();
/* 309 */     initActiveOpMode("$Stop$Robot$");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void runActiveOpMode(Gamepad[] gamepads)
/*     */   {
/* 316 */     OpModeStateTransition transition = (OpModeStateTransition)this.nextOpModeState.getAndSet(null);
/* 317 */     if (transition != null) {
/* 318 */       transition.apply();
/*     */     }
/* 320 */     this.activeOpMode.time = this.activeOpMode.getRuntime();
/* 321 */     this.activeOpMode.gamepad1 = gamepads[0];
/* 322 */     this.activeOpMode.gamepad2 = gamepads[1];
/*     */     
/*     */ 
/*     */ 
/* 326 */     if (this.gamepadResetNeeded) {
/* 327 */       this.activeOpMode.gamepad1.reset();
/* 328 */       this.activeOpMode.gamepad2.reset();
/* 329 */       this.gamepadResetNeeded = false;
/*     */     }
/*     */     
/* 332 */     if ((this.telemetryClearNeeded) && (this.eventLoopManager != null))
/*     */     {
/*     */ 
/*     */ 
/* 336 */       TelemetryMessage telemetry = new TelemetryMessage();
/* 337 */       telemetry.addData("\000", "");
/* 338 */       this.eventLoopManager.sendTelemetryData(telemetry);
/* 339 */       this.telemetryClearNeeded = false;
/*     */     }
/*     */     
/* 342 */     if (this.opModeSwapNeeded) {
/* 343 */       callActiveOpModeStop();
/* 344 */       performOpModeSwap();
/* 345 */       this.opModeSwapNeeded = false;
/*     */     }
/*     */     
/* 348 */     if (this.callToInitNeeded) {
/* 349 */       this.activeOpMode.gamepad1 = gamepads[0];
/* 350 */       this.activeOpMode.gamepad2 = gamepads[1];
/* 351 */       this.activeOpMode.hardwareMap = this.hardwareMap;
/* 352 */       this.activeOpMode.opModeServices = this;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 357 */       if (!this.activeOpModeName.equals("$Stop$Robot$")) {
/* 358 */         resetHardwareForOpMode();
/*     */       }
/*     */       
/* 361 */       this.activeOpMode.resetStartTime();
/* 362 */       callActiveOpModeInit();
/* 363 */       this.opModeState = OpModeState.INIT;
/* 364 */       this.callToInitNeeded = false;
/* 365 */       NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_INIT_OP_MODE_RESP", this.activeOpModeName));
/*     */     }
/*     */     
/* 368 */     if (this.callToStartNeeded) {
/* 369 */       callActiveOpModeStart();
/* 370 */       this.opModeState = OpModeState.LOOPING;
/* 371 */       this.callToStartNeeded = false;
/* 372 */       NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_RUN_OP_MODE_RESP", this.activeOpModeName));
/*     */     }
/*     */     
/* 375 */     if (this.opModeState == OpModeState.INIT) {
/* 376 */       callActiveOpModeInitLoop();
/* 377 */     } else if (this.opModeState == OpModeState.LOOPING) {
/* 378 */       callActiveOpModeLoop();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void resetHardwareForOpMode() {
/* 383 */     for (HardwareDevice device : this.hardwareMap) {
/* 384 */       device.resetDeviceConfigurationForOpMode();
/*     */     }
/*     */   }
/*     */   
/*     */   public void logOpModes()
/*     */   {
/* 390 */     int opModeCount = this.opModeClasses.size() + this.opModeObjects.size();
/* 391 */     String format = "   Op Mode: name=\"%s\" flavor=%s group=\"%s\"";
/* 392 */     RobotLog.i("There are " + opModeCount + " Op Modes");
/* 393 */     for (Map.Entry<String, OpModeAndMeta> entry : this.opModeClasses.entrySet()) {
/* 394 */       RobotLog.i(format, new Object[] { entry.getKey(), ((OpModeAndMeta)entry.getValue()).meta.flavor, ((OpModeAndMeta)entry.getValue()).meta.group });
/*     */     }
/* 396 */     for (Map.Entry<String, OpModeObjectAndMeta> entry : this.opModeObjects.entrySet()) {
/* 397 */       RobotLog.i(format, new Object[] { entry.getKey(), ((OpModeObjectAndMeta)entry.getValue()).meta.flavor, ((OpModeObjectAndMeta)entry.getValue()).meta.group });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void register(String name, Class opMode)
/*     */   {
/* 408 */     register(new OpModeMeta(name), opMode);
/*     */   }
/*     */   
/*     */   public void register(OpModeMeta meta, Class opMode) {
/* 412 */     if (isOpModeRegistered(meta.name)) throw new IllegalArgumentException(String.format("Can't register OpMode name twice: '%s'", new Object[] { meta.name }));
/* 413 */     this.opModeClasses.put(meta.name, new OpModeAndMeta(meta, opMode));
/* 414 */     RobotLog.dd("OpmodeRegistration", String.format("registered {%s} as {%s}", new Object[] { opMode.getSimpleName(), meta.name }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void register(String name, OpMode opMode)
/*     */   {
/* 426 */     register(new OpModeMeta(name), opMode);
/*     */   }
/*     */   
/*     */   public void register(OpModeMeta meta, OpMode opMode) {
/* 430 */     if (isOpModeRegistered(meta.name)) throw new IllegalArgumentException(String.format("Can't register OpMode name twice: '%s'", new Object[] { meta.name }));
/* 431 */     this.opModeObjects.put(meta.name, new OpModeObjectAndMeta(meta, opMode));
/* 432 */     RobotLog.dd("OpmodeRegistration", String.format("registered instance as {%s}", new Object[] { meta }));
/*     */   }
/*     */   
/*     */   private void performOpModeSwap() {
/* 436 */     RobotLog.i("Attempting to switch to op mode " + this.queuedOpModeName);
/*     */     try
/*     */     {
/* 439 */       if (this.opModeObjects.containsKey(this.queuedOpModeName)) {
/* 440 */         setActiveOpMode(((OpModeObjectAndMeta)this.opModeObjects.get(this.queuedOpModeName)).opMode, this.queuedOpModeName);
/*     */       } else {
/* 442 */         setActiveOpMode((OpMode)((OpModeAndMeta)this.opModeClasses.get(this.queuedOpModeName)).clazz.newInstance(), this.queuedOpModeName);
/*     */       }
/*     */     } catch (InstantiationException e) {
/* 445 */       failedToSwapOpMode(e);
/*     */     } catch (IllegalAccessException e) {
/* 447 */       failedToSwapOpMode(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void failedToSwapOpMode(Exception e) {
/* 452 */     RobotLog.e("Unable to start op mode " + this.activeOpModeName);
/* 453 */     RobotLog.logStacktrace(e);
/* 454 */     setActiveOpMode(DEFAULT_OP_MODE, "$Stop$Robot$");
/*     */   }
/*     */   
/*     */   protected void callActiveOpModeStop() {
/* 458 */     detectStuck("stop()", new Runnable()
/*     */     {
/* 460 */       public void run() { OpModeManagerImpl.this.activeOpMode.stop(); }
/*     */     });
/* 462 */     synchronized (this.listeners) {
/* 463 */       for (OpModeManagerNotifier.Notifications listener : this.listeners) {
/* 464 */         listener.onOpModePostStop(this.activeOpMode);
/*     */       }
/*     */     }
/* 467 */     for (HardwareDevice device : this.hardwareMap) {
/* 468 */       if ((device instanceof OpModeManagerNotifier.Notifications)) {
/* 469 */         ((OpModeManagerNotifier.Notifications)device).onOpModePostStop(this.activeOpMode);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void detectStuck(String method, Runnable runnable) {
/* 475 */     detectStuck(method, runnable, false);
/*     */   }
/*     */   
/*     */   protected void detectStuck(String method, Runnable runnable, boolean resetDebuggerCheck) {
/* 479 */     this.stuckMonitor.startMonitoring(method, resetDebuggerCheck);
/*     */     try {
/* 481 */       runnable.run();
/*     */     } finally {
/* 483 */       this.stuckMonitor.stopMonitoring();
/*     */     } }
/*     */   
/*     */   protected class OpModeStuckCodeMonitor { ExecutorService executorService;
/*     */     Semaphore stopped;
/*     */     
/* 489 */     protected OpModeStuckCodeMonitor() { this.executorService = ThreadPool.newSingleThreadExecutor();
/* 490 */       this.stopped = new Semaphore(0);
/* 491 */       this.acquired = null;
/* 492 */       this.debuggerDetected = false;
/*     */     }
/*     */     
/*     */     public void startMonitoring(String method, boolean resetDebuggerCheck)
/*     */     {
/* 497 */       if (this.acquired != null)
/* 498 */         try { this.acquired.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt();
/*     */         }
/* 500 */       this.method = method;
/* 501 */       this.stopped.drainPermits();
/* 502 */       this.acquired = new CountDownLatch(1);
/* 503 */       this.executorService.execute(new Runner());
/* 504 */       if (resetDebuggerCheck) {
/* 505 */         this.debuggerDetected = false;
/*     */       }
/*     */     }
/*     */     
/*     */     public void stopMonitoring() {
/* 510 */       this.stopped.release();
/*     */     }
/*     */     
/*     */ 
/* 514 */     public void shutdown() { this.executorService.shutdownNow(); }
/*     */     
/*     */     CountDownLatch acquired;
/*     */     boolean debuggerDetected;
/*     */     String method;
/*     */     protected boolean checkForDebugger() {
/* 520 */       this.debuggerDetected = ((this.debuggerDetected) || (Debug.isDebuggerConnected()));
/* 521 */       return this.debuggerDetected;
/*     */     }
/*     */     
/*     */     protected class Runner implements Runnable { protected Runner() {}
/*     */       
/* 526 */       public void run() { boolean errorWasSet = false;
/*     */         
/*     */         try
/*     */         {
/* 530 */           if (OpModeManagerImpl.OpModeStuckCodeMonitor.this.checkForDebugger())
/*     */             return;
/* 532 */           if (!OpModeManagerImpl.OpModeStuckCodeMonitor.this.stopped.tryAcquire(5L, TimeUnit.SECONDS))
/*     */           {
/* 534 */             if (OpModeManagerImpl.OpModeStuckCodeMonitor.this.checkForDebugger())
/*     */               return;
/* 536 */             String message = String.format(OpModeManagerImpl.this.context.getString(R.string.errorOpModeStuck), new Object[] { OpModeManagerImpl.this.activeOpModeName, OpModeManagerImpl.OpModeStuckCodeMonitor.this.method });
/* 537 */             errorWasSet = RobotLog.setGlobalErrorMsg(message);
/* 538 */             RobotLog.e(message);
/* 539 */             AppUtil.getInstance().showToast(OpModeManagerImpl.this.context, String.format(OpModeManagerImpl.this.context.getString(R.string.toastOpModeStuck), new Object[] { OpModeManagerImpl.OpModeStuckCodeMonitor.this.method }));
/*     */             
/* 541 */             Thread.sleep(1000L);
/*     */             
/* 543 */             AppUtil.getInstance().restartApp(-1);
/*     */           }
/*     */         }
/*     */         catch (InterruptedException e) {
/* 547 */           if (errorWasSet) RobotLog.clearGlobalErrorMsg();
/*     */         } finally {
/* 549 */           OpModeManagerImpl.OpModeStuckCodeMonitor.this.acquired.countDown();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callActiveOpModeInit() {
/* 556 */     synchronized (this.listeners) {
/* 557 */       for (OpModeManagerNotifier.Notifications listener : this.listeners) {
/* 558 */         listener.onOpModePreInit(this.activeOpMode);
/*     */       }
/*     */     }
/* 561 */     for (HardwareDevice device : this.hardwareMap) {
/* 562 */       if ((device instanceof OpModeManagerNotifier.Notifications)) {
/* 563 */         ((OpModeManagerNotifier.Notifications)device).onOpModePreInit(this.activeOpMode);
/*     */       }
/*     */     }
/*     */     
/* 567 */     this.activeOpMode.preInit();
/* 568 */     detectStuck("init()", new Runnable()
/*     */     {
/* 570 */       public void run() { OpModeManagerImpl.this.activeOpMode.init(); } }, true);
/*     */   }
/*     */   
/*     */   protected void callActiveOpModeStart()
/*     */   {
/* 575 */     synchronized (this.listeners) {
/* 576 */       for (OpModeManagerNotifier.Notifications listener : this.listeners) {
/* 577 */         listener.onOpModePreStart(this.activeOpMode);
/*     */       }
/*     */     }
/* 580 */     for (HardwareDevice device : this.hardwareMap) {
/* 581 */       if ((device instanceof OpModeManagerNotifier.Notifications)) {
/* 582 */         ((OpModeManagerNotifier.Notifications)device).onOpModePreStart(this.activeOpMode);
/*     */       }
/*     */     }
/* 585 */     detectStuck("start()", new Runnable()
/*     */     {
/* 587 */       public void run() { OpModeManagerImpl.this.activeOpMode.start(); }
/*     */     });
/*     */   }
/*     */   
/*     */   protected void callActiveOpModeInitLoop() {
/* 592 */     detectStuck("init_loop()", new Runnable()
/*     */     {
/* 594 */       public void run() { OpModeManagerImpl.this.activeOpMode.init_loop(); }
/* 595 */     });
/* 596 */     this.activeOpMode.postInitLoop();
/*     */   }
/*     */   
/*     */   protected void callActiveOpModeLoop() {
/* 600 */     detectStuck("loop()", new Runnable()
/*     */     {
/* 602 */       public void run() { OpModeManagerImpl.this.activeOpMode.loop(); }
/* 603 */     });
/* 604 */     this.activeOpMode.postLoop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void updateTelemetryNow(OpMode opMode, TelemetryMessage telemetry)
/*     */   {
/* 613 */     opMode.updateTelemetryNow(telemetry);
/*     */   }
/*     */   
/*     */   public void refreshUserTelemetry(TelemetryMessage telemetry, double sInterval) {
/* 617 */     this.eventLoopManager.getEventLoop().refreshUserTelemetry(telemetry, sInterval);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void requestOpModeStop(OpMode opModeToStopIfActive)
/*     */   {
/* 628 */     this.eventLoopManager.getEventLoop().requestOpModeStop(opModeToStopIfActive);
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
/*     */   public static class DefaultOpMode
/*     */     extends OpMode
/*     */   {
/*     */     private long nanoFirstSafe;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public DefaultOpMode()
/*     */     {
/* 653 */       this.nanoFirstSafe = 0L;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void init()
/*     */     {
/* 662 */       moveToSafeState();
/*     */     }
/*     */     
/*     */     public void init_loop()
/*     */     {
/* 667 */       moveToSafeState();
/* 668 */       this.telemetry.addData("Status", "Robot is stopped");
/*     */     }
/*     */     
/*     */     public void loop()
/*     */     {
/* 673 */       moveToSafeState();
/* 674 */       this.telemetry.addData("Status", "Robot is stopped");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void stop() {}
/*     */     
/*     */ 
/*     */     private void moveToSafeState()
/*     */     {
/* 684 */       if (this.nanoFirstSafe == 0L) {
/* 685 */         this.nanoFirstSafe = System.nanoTime();
/*     */       }
/* 687 */       long now = System.nanoTime();
/*     */       
/*     */ 
/* 690 */       for (DcMotorSimple motor : this.hardwareMap.getAll(DcMotorSimple.class)) {
/* 691 */         motor.setPower(0.0D);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 698 */       for (DcMotor dcMotor : this.hardwareMap.getAll(DcMotor.class))
/*     */       {
/* 700 */         dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 705 */       for (ServoController servoController : this.hardwareMap.getAll(ServoController.class))
/*     */       {
/* 707 */         servoController.pwmDisable();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 712 */       for (LightSensor light : this.hardwareMap.getAll(LightSensor.class))
/*     */       {
/* 714 */         light.enableLed(false);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\opmode\OpModeManagerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */