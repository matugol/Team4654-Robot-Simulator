/*     */ package com.qualcomm.robotcore.eventloop;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.hardware.usb.UsbDevice;
/*     */ import android.support.annotation.NonNull;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.hardware.configuration.UserSensorTypeManager;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.Heartbeat;
/*     */ import com.qualcomm.robotcore.robocol.PeerDiscovery;
/*     */ import com.qualcomm.robotcore.robocol.PeerDiscovery.PeerType;
/*     */ import com.qualcomm.robotcore.robocol.RobocolDatagram;
/*     */ import com.qualcomm.robotcore.robocol.TelemetryMessage;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import com.qualcomm.robotcore.util.Range;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.Event;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.NetworkConnectionCallback;
/*     */ import com.qualcomm.robotcore.wifi.NetworkType;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable.RecvLoopCallback;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.SendOnceRunnable.ClientCallback;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.SocketConnect;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventLoopManager
/*     */   implements RecvLoopRunnable.RecvLoopCallback, NetworkConnection.NetworkConnectionCallback, SendOnceRunnable.ClientCallback
/*     */ {
/*     */   public static final String TAG = "EventLoopManager";
/*     */   private static final boolean DEBUG = false;
/*     */   private static final int HEARTBEAT_WAIT_DELAY = 500;
/*     */   private static final int MAX_COMMAND_CACHE = 8;
/*     */   public static final String SYSTEM_NONE_KEY = "$System$None$";
/*     */   public static final String SYSTEM_ERROR_KEY = "$System$Error$";
/*     */   public static final String SYSTEM_WARNING_KEY = "$System$Warning$";
/*     */   public static final String ROBOT_BATTERY_LEVEL_KEY = "$Robot$Battery$Level$";
/*     */   public static final String RC_BATTERY_LEVEL_KEY = "$RobotController$Battery$Level$";
/*     */   private static final double SECONDS_UNTIL_FORCED_SHUTDOWN = 2.0D;
/* 114 */   private EventLoop idleEventLoop = new EmptyEventLoop(null);
/* 115 */   public RobotState state = RobotState.NOT_STARTED;
/* 116 */   protected boolean isPeerConnected = false;
/* 117 */   private ExecutorService executorEventLoop = ThreadPool.newSingleThreadExecutor();
/* 118 */   private ElapsedTime lastHeartbeatReceived = new ElapsedTime();
/* 119 */   private EventLoop eventLoop = this.idleEventLoop;
/* 120 */   private final Gamepad[] gamepad = { new Gamepad(), new Gamepad() };
/* 121 */   private Heartbeat heartbeat = new Heartbeat();
/* 122 */   private EventLoopMonitor callback = null;
/* 123 */   private final Set<SyncdDevice> syncdDevices = new CopyOnWriteArraySet();
/* 124 */   private final Command[] commandRecvCache = new Command[8];
/* 125 */   private int commandRecvCachePosition = 0;
/*     */   private InetAddress remoteAddr;
/* 127 */   private final Object refreshSystemTelemetryLock = new Object();
/* 128 */   private String lastSystemTelemetryMessage = null;
/* 129 */   private String lastSystemTelemetryKey = null;
/* 130 */   private long lastSystemTelemetryNanoTime = 0L;
/*     */   @NonNull
/* 132 */   private final Context context; private AppUtil appUtil = AppUtil.getInstance();
/* 133 */   private NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EventLoopManager(@NonNull Context context)
/*     */   {
/* 143 */     this.context = context;
/* 144 */     changeState(RobotState.NOT_STARTED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIdleEventLoop(EventLoop idleEventLoop)
/*     */   {
/* 152 */     this.idleEventLoop = idleEventLoop;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMonitor(EventLoopMonitor monitor)
/*     */   {
/* 161 */     this.callback = monitor;
/*     */   }
/*     */   
/*     */   public EventLoopMonitor getMonitor()
/*     */   {
/* 166 */     return this.callback;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */   public EventLoop getEventLoop() { return this.eventLoop; }
/*     */   
/*     */   public static abstract interface EventLoopMonitor {
/*     */     public abstract void onStateChange(@NonNull RobotState paramRobotState);
/*     */     
/*     */     public abstract void onTelemetryTransmitted();
/*     */     
/*     */     public abstract void onPeerConnected(boolean paramBoolean);
/*     */     
/*     */     public abstract void onPeerDisconnected();
/*     */   }
/*     */   
/* 187 */   public Gamepad getGamepad() { return getGamepad(0); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Gamepad getGamepad(int port)
/*     */   {
/* 196 */     Range.throwIfRangeIsInvalid(port, 0, 1);
/* 197 */     return this.gamepad[port];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Gamepad[] getGamepads()
/*     */   {
/* 207 */     return this.gamepad;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Heartbeat getHeartbeat()
/*     */   {
/* 216 */     return this.heartbeat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallbackResult telemetryEvent(RobocolDatagram packet)
/*     */   {
/* 225 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult reportGlobalError(String error, boolean recoverable)
/*     */   {
/* 230 */     RobotLog.setGlobalErrorMsg(error);
/* 231 */     return CallbackResult.HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult packetReceived(RobocolDatagram packet)
/*     */   {
/* 236 */     refreshSystemTelemetry();
/* 237 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   private class EventLoopRunnable implements Runnable
/*     */   {
/*     */     private EventLoopRunnable() {}
/*     */     
/*     */     public void run()
/*     */     {
/* 246 */       ThreadPool.logThreadLifeCycle("opmode loop()", new Runnable()
/*     */       {
/*     */         /* Error */
/*     */         public void run()
/*     */         {
/*     */           // Byte code:
/*     */           //   0: new 3	com/qualcomm/robotcore/util/ElapsedTime
/*     */           //   3: dup
/*     */           //   4: invokespecial 4	com/qualcomm/robotcore/util/ElapsedTime:<init>	()V
/*     */           //   7: astore_1
/*     */           //   8: ldc2_w 5
/*     */           //   11: dstore_2
/*     */           //   12: ldc2_w 7
/*     */           //   15: lstore 4
/*     */           //   17: invokestatic 9	java/lang/Thread:interrupted	()Z
/*     */           //   20: ifne +459 -> 479
/*     */           //   23: aload_1
/*     */           //   24: invokevirtual 10	com/qualcomm/robotcore/util/ElapsedTime:time	()D
/*     */           //   27: ldc2_w 5
/*     */           //   30: dcmpg
/*     */           //   31: ifge +12 -> 43
/*     */           //   34: ldc2_w 7
/*     */           //   37: invokestatic 11	java/lang/Thread:sleep	(J)V
/*     */           //   40: goto -17 -> 23
/*     */           //   43: aload_1
/*     */           //   44: invokevirtual 12	com/qualcomm/robotcore/util/ElapsedTime:reset	()V
/*     */           //   47: aload_0
/*     */           //   48: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   51: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   54: invokevirtual 14	com/qualcomm/robotcore/eventloop/EventLoopManager:refreshSystemTelemetry	()V
/*     */           //   57: aload_0
/*     */           //   58: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   61: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   64: invokestatic 15	com/qualcomm/robotcore/eventloop/EventLoopManager:access$100	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;)Lcom/qualcomm/robotcore/util/ElapsedTime;
/*     */           //   67: invokevirtual 16	com/qualcomm/robotcore/util/ElapsedTime:startTime	()D
/*     */           //   70: dconst_0
/*     */           //   71: dcmpl
/*     */           //   72: ifne +9 -> 81
/*     */           //   75: ldc2_w 17
/*     */           //   78: invokestatic 11	java/lang/Thread:sleep	(J)V
/*     */           //   81: aload_0
/*     */           //   82: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   85: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   88: invokestatic 19	com/qualcomm/robotcore/eventloop/EventLoopManager:access$200	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;)Ljava/util/Set;
/*     */           //   91: invokeinterface 20 1 0
/*     */           //   96: astore 6
/*     */           //   98: aload 6
/*     */           //   100: invokeinterface 21 1 0
/*     */           //   105: ifeq +25 -> 130
/*     */           //   108: aload 6
/*     */           //   110: invokeinterface 22 1 0
/*     */           //   115: checkcast 23	com/qualcomm/robotcore/eventloop/SyncdDevice
/*     */           //   118: astore 7
/*     */           //   120: aload 7
/*     */           //   122: invokeinterface 24 1 0
/*     */           //   127: goto -29 -> 98
/*     */           //   130: aload_0
/*     */           //   131: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   134: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   137: invokestatic 19	com/qualcomm/robotcore/eventloop/EventLoopManager:access$200	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;)Ljava/util/Set;
/*     */           //   140: invokeinterface 20 1 0
/*     */           //   145: astore 6
/*     */           //   147: aload 6
/*     */           //   149: invokeinterface 21 1 0
/*     */           //   154: ifeq +69 -> 223
/*     */           //   157: aload 6
/*     */           //   159: invokeinterface 22 1 0
/*     */           //   164: checkcast 23	com/qualcomm/robotcore/eventloop/SyncdDevice
/*     */           //   167: astore 7
/*     */           //   169: aload 7
/*     */           //   171: invokeinterface 25 1 0
/*     */           //   176: ifeq +44 -> 220
/*     */           //   179: ldc 26
/*     */           //   181: invokestatic 27	com/qualcomm/robotcore/util/RobotLog:v	(Ljava/lang/String;)V
/*     */           //   184: aload 7
/*     */           //   186: invokeinterface 28 1 0
/*     */           //   191: astore 8
/*     */           //   193: aload 8
/*     */           //   195: ifnull +25 -> 220
/*     */           //   198: ldc 29
/*     */           //   200: invokestatic 27	com/qualcomm/robotcore/util/RobotLog:v	(Ljava/lang/String;)V
/*     */           //   203: aload_0
/*     */           //   204: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   207: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   210: invokestatic 30	com/qualcomm/robotcore/eventloop/EventLoopManager:access$300	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;)Lcom/qualcomm/robotcore/eventloop/EventLoop;
/*     */           //   213: aload 8
/*     */           //   215: invokeinterface 31 2 0
/*     */           //   220: goto -73 -> 147
/*     */           //   223: aload_0
/*     */           //   224: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   227: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   230: invokestatic 30	com/qualcomm/robotcore/eventloop/EventLoopManager:access$300	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;)Lcom/qualcomm/robotcore/eventloop/EventLoop;
/*     */           //   233: invokeinterface 32 1 0
/*     */           //   238: aload_0
/*     */           //   239: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   242: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   245: invokestatic 30	com/qualcomm/robotcore/eventloop/EventLoopManager:access$300	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;)Lcom/qualcomm/robotcore/eventloop/EventLoop;
/*     */           //   248: invokeinterface 33 1 0
/*     */           //   253: aload_0
/*     */           //   254: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   257: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   260: invokestatic 19	com/qualcomm/robotcore/eventloop/EventLoopManager:access$200	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;)Ljava/util/Set;
/*     */           //   263: invokeinterface 20 1 0
/*     */           //   268: astore 6
/*     */           //   270: aload 6
/*     */           //   272: invokeinterface 21 1 0
/*     */           //   277: ifeq +25 -> 302
/*     */           //   280: aload 6
/*     */           //   282: invokeinterface 22 1 0
/*     */           //   287: checkcast 23	com/qualcomm/robotcore/eventloop/SyncdDevice
/*     */           //   290: astore 7
/*     */           //   292: aload 7
/*     */           //   294: invokeinterface 34 1 0
/*     */           //   299: goto -29 -> 270
/*     */           //   302: goto +174 -> 476
/*     */           //   305: astore 6
/*     */           //   307: ldc 36
/*     */           //   309: invokestatic 37	com/qualcomm/robotcore/util/RobotLog:e	(Ljava/lang/String;)V
/*     */           //   312: aload 6
/*     */           //   314: invokestatic 38	com/qualcomm/robotcore/util/RobotLog:logStacktrace	(Ljava/lang/Exception;)V
/*     */           //   317: new 39	java/lang/StringBuilder
/*     */           //   320: dup
/*     */           //   321: invokespecial 40	java/lang/StringBuilder:<init>	()V
/*     */           //   324: aload 6
/*     */           //   326: invokevirtual 41	java/lang/Object:getClass	()Ljava/lang/Class;
/*     */           //   329: invokevirtual 42	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */           //   332: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   335: aload 6
/*     */           //   337: invokevirtual 44	java/lang/Exception:getMessage	()Ljava/lang/String;
/*     */           //   340: ifnull +29 -> 369
/*     */           //   343: new 39	java/lang/StringBuilder
/*     */           //   346: dup
/*     */           //   347: invokespecial 40	java/lang/StringBuilder:<init>	()V
/*     */           //   350: ldc 45
/*     */           //   352: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   355: aload 6
/*     */           //   357: invokevirtual 44	java/lang/Exception:getMessage	()Ljava/lang/String;
/*     */           //   360: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   363: invokevirtual 46	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */           //   366: goto +5 -> 371
/*     */           //   369: ldc 47
/*     */           //   371: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   374: invokevirtual 46	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */           //   377: astore 7
/*     */           //   379: new 39	java/lang/StringBuilder
/*     */           //   382: dup
/*     */           //   383: invokespecial 40	java/lang/StringBuilder:<init>	()V
/*     */           //   386: ldc 48
/*     */           //   388: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   391: aload 7
/*     */           //   393: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   396: invokevirtual 46	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */           //   399: invokestatic 49	com/qualcomm/robotcore/util/RobotLog:setGlobalErrorMsg	(Ljava/lang/String;)Z
/*     */           //   402: pop
/*     */           //   403: new 50	com/qualcomm/robotcore/exception/RobotCoreException
/*     */           //   406: dup
/*     */           //   407: ldc 51
/*     */           //   409: iconst_1
/*     */           //   410: anewarray 52	java/lang/Object
/*     */           //   413: dup
/*     */           //   414: iconst_0
/*     */           //   415: aload 7
/*     */           //   417: aastore
/*     */           //   418: invokespecial 53	com/qualcomm/robotcore/exception/RobotCoreException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */           //   421: athrow
/*     */           //   422: astore 9
/*     */           //   424: aload_0
/*     */           //   425: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   428: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   431: invokestatic 19	com/qualcomm/robotcore/eventloop/EventLoopManager:access$200	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;)Ljava/util/Set;
/*     */           //   434: invokeinterface 20 1 0
/*     */           //   439: astore 10
/*     */           //   441: aload 10
/*     */           //   443: invokeinterface 21 1 0
/*     */           //   448: ifeq +25 -> 473
/*     */           //   451: aload 10
/*     */           //   453: invokeinterface 22 1 0
/*     */           //   458: checkcast 23	com/qualcomm/robotcore/eventloop/SyncdDevice
/*     */           //   461: astore 11
/*     */           //   463: aload 11
/*     */           //   465: invokeinterface 34 1 0
/*     */           //   470: goto -29 -> 441
/*     */           //   473: aload 9
/*     */           //   475: athrow
/*     */           //   476: goto -459 -> 17
/*     */           //   479: goto +96 -> 575
/*     */           //   482: astore_1
/*     */           //   483: ldc 55
/*     */           //   485: invokestatic 27	com/qualcomm/robotcore/util/RobotLog:v	(Ljava/lang/String;)V
/*     */           //   488: aload_0
/*     */           //   489: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   492: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   495: getstatic 56	com/qualcomm/robotcore/robot/RobotState:STOPPED	Lcom/qualcomm/robotcore/robot/RobotState;
/*     */           //   498: invokestatic 57	com/qualcomm/robotcore/eventloop/EventLoopManager:access$400	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;Lcom/qualcomm/robotcore/robot/RobotState;)V
/*     */           //   501: goto +74 -> 575
/*     */           //   504: astore_1
/*     */           //   505: ldc 59
/*     */           //   507: invokestatic 27	com/qualcomm/robotcore/util/RobotLog:v	(Ljava/lang/String;)V
/*     */           //   510: aload_0
/*     */           //   511: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   514: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   517: getstatic 56	com/qualcomm/robotcore/robot/RobotState:STOPPED	Lcom/qualcomm/robotcore/robot/RobotState;
/*     */           //   520: invokestatic 57	com/qualcomm/robotcore/eventloop/EventLoopManager:access$400	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;Lcom/qualcomm/robotcore/robot/RobotState;)V
/*     */           //   523: goto +52 -> 575
/*     */           //   526: astore_1
/*     */           //   527: new 39	java/lang/StringBuilder
/*     */           //   530: dup
/*     */           //   531: invokespecial 40	java/lang/StringBuilder:<init>	()V
/*     */           //   534: ldc 60
/*     */           //   536: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   539: aload_1
/*     */           //   540: invokevirtual 61	com/qualcomm/robotcore/exception/RobotCoreException:getMessage	()Ljava/lang/String;
/*     */           //   543: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   546: invokevirtual 46	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */           //   549: invokestatic 27	com/qualcomm/robotcore/util/RobotLog:v	(Ljava/lang/String;)V
/*     */           //   552: aload_0
/*     */           //   553: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   556: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   559: getstatic 62	com/qualcomm/robotcore/robot/RobotState:EMERGENCY_STOP	Lcom/qualcomm/robotcore/robot/RobotState;
/*     */           //   562: invokestatic 57	com/qualcomm/robotcore/eventloop/EventLoopManager:access$400	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;Lcom/qualcomm/robotcore/robot/RobotState;)V
/*     */           //   565: aload_0
/*     */           //   566: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   569: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   572: invokevirtual 14	com/qualcomm/robotcore/eventloop/EventLoopManager:refreshSystemTelemetry	()V
/*     */           //   575: aload_0
/*     */           //   576: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   579: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   582: invokestatic 30	com/qualcomm/robotcore/eventloop/EventLoopManager:access$300	(Lcom/qualcomm/robotcore/eventloop/EventLoopManager;)Lcom/qualcomm/robotcore/eventloop/EventLoop;
/*     */           //   585: invokeinterface 63 1 0
/*     */           //   590: goto +43 -> 633
/*     */           //   593: astore_1
/*     */           //   594: new 39	java/lang/StringBuilder
/*     */           //   597: dup
/*     */           //   598: invokespecial 40	java/lang/StringBuilder:<init>	()V
/*     */           //   601: ldc 64
/*     */           //   603: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   606: aload_1
/*     */           //   607: invokevirtual 65	java/lang/Exception:toString	()Ljava/lang/String;
/*     */           //   610: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   613: invokevirtual 46	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */           //   616: invokestatic 66	com/qualcomm/robotcore/util/RobotLog:w	(Ljava/lang/String;)V
/*     */           //   619: aload_1
/*     */           //   620: invokestatic 38	com/qualcomm/robotcore/util/RobotLog:logStacktrace	(Ljava/lang/Exception;)V
/*     */           //   623: aload_0
/*     */           //   624: getfield 1	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable$1:this$1	Lcom/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable;
/*     */           //   627: getfield 13	com/qualcomm/robotcore/eventloop/EventLoopManager$EventLoopRunnable:this$0	Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
/*     */           //   630: invokevirtual 14	com/qualcomm/robotcore/eventloop/EventLoopManager:refreshSystemTelemetry	()V
/*     */           //   633: return
/*     */           // Line number table:
/*     */           //   Java source line #249	-> byte code offset #0
/*     */           //   Java source line #250	-> byte code offset #8
/*     */           //   Java source line #251	-> byte code offset #12
/*     */           //   Java source line #253	-> byte code offset #17
/*     */           //   Java source line #255	-> byte code offset #23
/*     */           //   Java source line #257	-> byte code offset #34
/*     */           //   Java source line #259	-> byte code offset #43
/*     */           //   Java source line #262	-> byte code offset #47
/*     */           //   Java source line #264	-> byte code offset #57
/*     */           //   Java source line #267	-> byte code offset #75
/*     */           //   Java source line #271	-> byte code offset #81
/*     */           //   Java source line #272	-> byte code offset #120
/*     */           //   Java source line #273	-> byte code offset #127
/*     */           //   Java source line #276	-> byte code offset #130
/*     */           //   Java source line #277	-> byte code offset #169
/*     */           //   Java source line #278	-> byte code offset #179
/*     */           //   Java source line #279	-> byte code offset #184
/*     */           //   Java source line #280	-> byte code offset #193
/*     */           //   Java source line #281	-> byte code offset #198
/*     */           //   Java source line #282	-> byte code offset #203
/*     */           //   Java source line #285	-> byte code offset #220
/*     */           //   Java source line #288	-> byte code offset #223
/*     */           //   Java source line #292	-> byte code offset #238
/*     */           //   Java source line #304	-> byte code offset #253
/*     */           //   Java source line #305	-> byte code offset #292
/*     */           //   Java source line #306	-> byte code offset #299
/*     */           //   Java source line #307	-> byte code offset #302
/*     */           //   Java source line #293	-> byte code offset #305
/*     */           //   Java source line #295	-> byte code offset #307
/*     */           //   Java source line #296	-> byte code offset #312
/*     */           //   Java source line #299	-> byte code offset #317
/*     */           //   Java source line #300	-> byte code offset #379
/*     */           //   Java source line #301	-> byte code offset #403
/*     */           //   Java source line #304	-> byte code offset #422
/*     */           //   Java source line #305	-> byte code offset #463
/*     */           //   Java source line #306	-> byte code offset #470
/*     */           //   Java source line #322	-> byte code offset #479
/*     */           //   Java source line #309	-> byte code offset #482
/*     */           //   Java source line #311	-> byte code offset #483
/*     */           //   Java source line #312	-> byte code offset #488
/*     */           //   Java source line #322	-> byte code offset #501
/*     */           //   Java source line #313	-> byte code offset #504
/*     */           //   Java source line #315	-> byte code offset #505
/*     */           //   Java source line #316	-> byte code offset #510
/*     */           //   Java source line #322	-> byte code offset #523
/*     */           //   Java source line #317	-> byte code offset #526
/*     */           //   Java source line #318	-> byte code offset #527
/*     */           //   Java source line #319	-> byte code offset #552
/*     */           //   Java source line #321	-> byte code offset #565
/*     */           //   Java source line #326	-> byte code offset #575
/*     */           //   Java source line #332	-> byte code offset #590
/*     */           //   Java source line #327	-> byte code offset #593
/*     */           //   Java source line #328	-> byte code offset #594
/*     */           //   Java source line #329	-> byte code offset #619
/*     */           //   Java source line #331	-> byte code offset #623
/*     */           //   Java source line #333	-> byte code offset #633
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	signature
/*     */           //   0	634	0	this	1
/*     */           //   7	37	1	loopTime	ElapsedTime
/*     */           //   482	2	1	e	InterruptedException
/*     */           //   504	2	1	e	java.util.concurrent.CancellationException
/*     */           //   526	14	1	e	RobotCoreException
/*     */           //   593	27	1	e	Exception
/*     */           //   11	2	2	MIN_THROTTLE	double
/*     */           //   15	3	4	THROTTLE_RESOLUTION	long
/*     */           //   96	13	6	i$	java.util.Iterator
/*     */           //   145	13	6	i$	java.util.Iterator
/*     */           //   268	13	6	i$	java.util.Iterator
/*     */           //   305	51	6	e	Exception
/*     */           //   118	3	7	device	SyncdDevice
/*     */           //   167	18	7	device	SyncdDevice
/*     */           //   290	3	7	device	SyncdDevice
/*     */           //   377	39	7	errorMsg	String
/*     */           //   191	23	8	owner	RobotUsbModule
/*     */           //   422	52	9	localObject	Object
/*     */           //   439	13	10	i$	java.util.Iterator
/*     */           //   461	3	11	device	SyncdDevice
/*     */           // Exception table:
/*     */           //   from	to	target	type
/*     */           //   238	253	305	java/lang/Exception
/*     */           //   238	253	422	finally
/*     */           //   305	424	422	finally
/*     */           //   0	479	482	java/lang/InterruptedException
/*     */           //   0	479	504	java/util/concurrent/CancellationException
/*     */           //   0	479	526	com/qualcomm/robotcore/exception/RobotCoreException
/*     */           //   575	590	593	java/lang/Exception
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   public void refreshSystemTelemetryNow()
/*     */   {
/* 345 */     this.lastSystemTelemetryNanoTime = 0L;
/* 346 */     refreshSystemTelemetry();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refreshSystemTelemetry()
/*     */   {
/* 357 */     synchronized (this.refreshSystemTelemetryLock)
/*     */     {
/*     */ 
/*     */ 
/* 361 */       long now = System.nanoTime();
/*     */       
/* 363 */       String errorMessage = RobotLog.getGlobalErrorMsg();
/* 364 */       String warningMessage = RobotLog.getGlobalWarningMessage();
/*     */       String key;
/*     */       String message;
/* 367 */       String key; if (!errorMessage.isEmpty()) {
/* 368 */         String message = errorMessage;
/* 369 */         key = "$System$Error$";
/*     */       } else { String key;
/* 371 */         if (!warningMessage.isEmpty()) {
/* 372 */           String message = warningMessage;
/* 373 */           key = "$System$Warning$";
/*     */         }
/*     */         else {
/* 376 */           message = "";
/* 377 */           key = "$System$None$";
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 383 */       long nanoTimeRetransmitInterval = 5000000000L;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 388 */       boolean shouldLog = (!message.equals(this.lastSystemTelemetryMessage)) || (!key.equals(this.lastSystemTelemetryKey));
/*     */       
/* 390 */       boolean shouldTransmit = (shouldLog) || (now - this.lastSystemTelemetryNanoTime > nanoTimeRetransmitInterval);
/*     */       
/*     */ 
/* 393 */       if (shouldLog) {
/* 394 */         RobotLog.d("system telemetry: key=%s msg=\"%s\"", new Object[] { key, message });
/*     */       }
/*     */       
/* 397 */       if (shouldTransmit) {
/* 398 */         this.lastSystemTelemetryMessage = message;
/* 399 */         this.lastSystemTelemetryKey = key;
/* 400 */         this.lastSystemTelemetryNanoTime = now;
/*     */         
/* 402 */         buildAndSendTelemetry(key, message);
/* 403 */         if (this.callback != null) { this.callback.onTelemetryTransmitted();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class EmptyEventLoop
/*     */     implements EventLoop
/*     */   {
/*     */     public void init(EventLoopManager eventProcessor) {}
/*     */     
/*     */ 
/*     */     public void loop() {}
/*     */     
/*     */ 
/*     */     public void refreshUserTelemetry(TelemetryMessage telemetry, double sInterval) {}
/*     */     
/*     */     public void teardown() {}
/*     */     
/*     */     public void onUsbDeviceAttached(UsbDevice usbDevice) {}
/*     */     
/*     */     public void processedRecentlyAttachedUsbDevices()
/*     */       throws RobotCoreException, InterruptedException
/*     */     {}
/*     */     
/*     */     public void handleUsbModuleDetach(RobotUsbModule module)
/*     */       throws RobotCoreException, InterruptedException
/*     */     {}
/*     */     
/*     */     public CallbackResult processCommand(Command command)
/*     */     {
/* 435 */       RobotLog.ww("Robocol", "Dropping command " + command.getName() + ", no active event loop");
/* 436 */       return CallbackResult.NOT_HANDLED;
/*     */     }
/*     */     
/*     */     public OpModeManagerImpl getOpModeManager() {
/* 440 */       return null;
/*     */     }
/*     */     
/*     */     public void requestOpModeStop(OpMode opModeToStopIfActive) {}
/*     */   }
/*     */   
/*     */   public CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event) {
/* 447 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/* 448 */     switch (event) {
/*     */     case PEERS_AVAILABLE: 
/* 450 */       result = this.networkConnectionHandler.handlePeersAvailable();
/* 451 */       break;
/*     */     case CONNECTION_INFO_AVAILABLE: 
/* 453 */       result = this.networkConnectionHandler.handleConnectionInfoAvailable(SocketConnect.DEFER);
/*     */     }
/*     */     
/* 456 */     return result;
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
/*     */   public void start(@NonNull EventLoop eventLoop)
/*     */     throws RobotCoreException
/*     */   {
/* 471 */     RobotLog.vv("Robocol", "EventLoopManager.start()");
/*     */     
/* 473 */     this.networkConnectionHandler.pushNetworkConnectionCallback(this);
/* 474 */     this.networkConnectionHandler.pushReceiveLoopCallback(this);
/*     */     
/* 476 */     NetworkType networkType = this.networkConnectionHandler.getDefaultNetworkType(this.context);
/* 477 */     this.networkConnectionHandler.init(networkType, this.context);
/*     */     
/*     */ 
/* 480 */     if (this.networkConnectionHandler.isNetworkConnected())
/*     */     {
/*     */ 
/* 483 */       RobotLog.vv("Robocol", "Spoofing a Network Connection event...");
/* 484 */       onNetworkConnectionEvent(NetworkConnection.Event.CONNECTION_INFO_AVAILABLE);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 489 */     setEventLoop(eventLoop);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 497 */     RobotLog.vv("Robocol", "EventLoopManager.shutdown()");
/* 498 */     stopEventLoop();
/*     */   }
/*     */   
/*     */   public void close() {
/* 502 */     RobotLog.vv("Robocol", "EventLoopManager.close()");
/* 503 */     this.networkConnectionHandler.shutdown();
/* 504 */     this.networkConnectionHandler.removeNetworkConnectionCallback(this);
/* 505 */     this.networkConnectionHandler.removeReceiveLoopCallback(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerSyncdDevice(SyncdDevice device)
/*     */   {
/* 515 */     this.syncdDevices.add(device);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unregisterSyncdDevice(SyncdDevice device)
/*     */   {
/* 526 */     this.syncdDevices.remove(device);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEventLoop(@NonNull EventLoop eventLoop)
/*     */     throws RobotCoreException
/*     */   {
/* 538 */     stopEventLoop();
/*     */     
/*     */ 
/* 541 */     this.eventLoop = eventLoop;
/* 542 */     RobotLog.vv("Robocol", "eventLoop=%s", new Object[] { this.eventLoop.getClass().getSimpleName() });
/*     */     
/*     */ 
/* 545 */     startEventLoop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendTelemetryData(TelemetryMessage telemetry)
/*     */   {
/*     */     try
/*     */     {
/* 556 */       telemetry.setRobotState(this.state);
/* 557 */       this.networkConnectionHandler.sendDatagram(new RobocolDatagram(telemetry.toByteArrayForTransmission()));
/*     */     } catch (RobotCoreException e) {
/* 559 */       RobotLog.w("Failed to send telemetry data");
/* 560 */       RobotLog.logStacktrace(e);
/*     */     }
/*     */     
/*     */ 
/* 564 */     telemetry.clearData();
/*     */   }
/*     */   
/*     */   private void startEventLoop() throws RobotCoreException
/*     */   {
/*     */     try {
/* 570 */       changeState(RobotState.INIT);
/* 571 */       this.eventLoop.init(this);
/*     */       
/*     */ 
/* 574 */       for (SyncdDevice device : this.syncdDevices) {
/* 575 */         device.startBlockingWork();
/*     */       }
/*     */     } catch (Exception e) {
/* 578 */       RobotLog.w("Caught exception during looper init: " + e.toString());
/* 579 */       RobotLog.logStacktrace(e);
/* 580 */       changeState(RobotState.EMERGENCY_STOP);
/*     */       
/* 582 */       refreshSystemTelemetry();
/*     */       
/* 584 */       throw new RobotCoreException("Robot failed to start: " + e.getMessage());
/*     */     }
/*     */     
/*     */ 
/* 588 */     this.lastHeartbeatReceived = new ElapsedTime(0L);
/*     */     
/*     */ 
/* 591 */     changeState(RobotState.RUNNING);
/*     */     
/* 593 */     this.executorEventLoop = ThreadPool.newSingleThreadExecutor();
/* 594 */     this.executorEventLoop.execute(new EventLoopRunnable(null));
/*     */   }
/*     */   
/*     */   private void stopEventLoop()
/*     */   {
/* 599 */     this.executorEventLoop.shutdownNow();
/* 600 */     ThreadPool.awaitTerminationOrExitApplication(this.executorEventLoop, 10L, TimeUnit.SECONDS, "EventLoop", "possible infinite loop in user code?");
/*     */     
/*     */ 
/* 603 */     changeState(RobotState.STOPPED);
/*     */     
/* 605 */     this.eventLoop = this.idleEventLoop;
/*     */     
/*     */ 
/* 608 */     this.syncdDevices.clear();
/*     */   }
/*     */   
/*     */   private void changeState(@NonNull RobotState state) {
/* 612 */     this.state = state;
/* 613 */     RobotLog.v("EventLoopManager state is " + state.toString());
/* 614 */     if (this.callback != null) { this.callback.onStateChange(state);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallbackResult gamepadEvent(RobocolDatagram packet)
/*     */     throws RobotCoreException
/*     */   {
/* 624 */     Gamepad incomingGamepad = new Gamepad();
/* 625 */     incomingGamepad.fromByteArray(packet.getData());
/*     */     
/* 627 */     if ((incomingGamepad.user < 1) || (incomingGamepad.user > 2))
/*     */     {
/* 629 */       RobotLog.d("Gamepad with user %d received. Only users 1 and 2 are valid");
/* 630 */       return CallbackResult.HANDLED;
/*     */     }
/*     */     
/* 633 */     int position = incomingGamepad.user - 1;
/*     */     
/*     */ 
/* 636 */     this.gamepad[position].copy(incomingGamepad);
/*     */     
/* 638 */     if (this.gamepad[0].id == this.gamepad[1].id)
/*     */     {
/* 640 */       RobotLog.v("Gamepad moved position, removing stale gamepad");
/* 641 */       if (position == 0) this.gamepad[1].copy(new Gamepad());
/* 642 */       if (position == 1) this.gamepad[0].copy(new Gamepad());
/*     */     }
/* 644 */     return CallbackResult.HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public CallbackResult heartbeatEvent(RobocolDatagram packet, long tReceived)
/*     */     throws RobotCoreException
/*     */   {
/* 651 */     Heartbeat currentHeartbeat = new Heartbeat();
/* 652 */     currentHeartbeat.fromByteArray(packet.getData());
/* 653 */     currentHeartbeat.setRobotState(this.state);
/*     */     
/* 655 */     currentHeartbeat.t1 = tReceived;
/* 656 */     currentHeartbeat.t2 = Heartbeat.getMsTimeSyncTime();
/*     */     
/* 658 */     packet.setData(currentHeartbeat.toByteArrayForTransmission());
/* 659 */     this.networkConnectionHandler.sendDatagram(packet);
/*     */     
/* 661 */     this.lastHeartbeatReceived.reset();
/* 662 */     this.heartbeat = currentHeartbeat;
/* 663 */     return CallbackResult.HANDLED;
/*     */   }
/*     */   
/*     */   public void peerConnected(boolean peerLikelyChanged) {
/* 667 */     this.isPeerConnected = true;
/* 668 */     if (this.callback != null) {
/* 669 */       this.callback.onPeerConnected(peerLikelyChanged);
/*     */     }
/*     */     
/* 672 */     if (peerLikelyChanged) {
/* 673 */       UserSensorTypeManager.getInstance().sendUserSensorTypes();
/*     */     }
/*     */   }
/*     */   
/*     */   public void peerDisconnected() {
/* 678 */     if (this.callback != null) {
/* 679 */       this.callback.onPeerDisconnected();
/*     */     }
/*     */     
/* 682 */     if (this.isPeerConnected)
/*     */     {
/* 684 */       OpModeManagerImpl opModeManager = this.eventLoop.getOpModeManager();
/*     */       
/*     */ 
/* 687 */       if (opModeManager != null) {
/* 688 */         String msg = "Lost connection while running op mode: " + opModeManager.getActiveOpModeName();
/* 689 */         opModeManager.initActiveOpMode("$Stop$Robot$");
/* 690 */         RobotLog.i(msg);
/*     */       }
/*     */       else {
/* 693 */         RobotLog.i("Lost connection while main event loop not active");
/*     */       }
/*     */       
/* 696 */       this.isPeerConnected = false;
/*     */     }
/* 698 */     this.remoteAddr = null;
/* 699 */     this.lastHeartbeatReceived = new ElapsedTime(0L);
/*     */   }
/*     */   
/*     */   public CallbackResult peerDiscoveryEvent(RobocolDatagram packet) throws RobotCoreException
/*     */   {
/* 704 */     this.networkConnectionHandler.updateConnection(packet, null, this);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 714 */     PeerDiscovery outgoing = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
/* 715 */     RobocolDatagram outgoingDatagram = new RobocolDatagram(outgoing);
/* 716 */     this.networkConnectionHandler.sendDatagram(outgoingDatagram);
/*     */     
/* 718 */     return CallbackResult.HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CallbackResult commandEvent(Command command)
/*     */     throws RobotCoreException
/*     */   {
/* 726 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/*     */     
/*     */ 
/* 729 */     for (Command c : this.commandRecvCache) {
/* 730 */       if ((c != null) && (c.equals(command)))
/*     */       {
/*     */ 
/* 733 */         return CallbackResult.HANDLED;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 738 */     this.commandRecvCache[(this.commandRecvCachePosition++ % this.commandRecvCache.length)] = command;
/*     */     
/*     */     try
/*     */     {
/* 742 */       result = this.eventLoop.processCommand(command);
/*     */     }
/*     */     catch (Exception e) {
/* 745 */       RobotLog.e("Event loop threw an exception while processing a command");
/* 746 */       RobotLog.logStacktrace(e);
/*     */     }
/*     */     
/* 749 */     return result;
/*     */   }
/*     */   
/*     */   public CallbackResult emptyEvent(RobocolDatagram packet)
/*     */   {
/* 754 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public void buildAndSendTelemetry(String tag, String msg) {
/* 758 */     TelemetryMessage telemetry = new TelemetryMessage();
/* 759 */     telemetry.setTag(tag);
/* 760 */     telemetry.addData(tag, msg);
/* 761 */     sendTelemetryData(telemetry);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\EventLoopManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */