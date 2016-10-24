/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.support.annotation.NonNull;
/*     */ import com.qualcomm.hardware.HardwareDeviceManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager;
/*     */ import com.qualcomm.robotcore.util.NextLock;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import com.qualcomm.robotcore.util.ThreadPool.Singleton;
/*     */ import com.qualcomm.robotcore.util.ThreadPool.SingletonResult;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class USBScanManager
/*     */ {
/*     */   public static final String TAG = "FtcConfigTag";
/*     */   public static final int msScanWaitDefault = 4000;
/*     */   protected Context context;
/*     */   protected boolean isRemoteConfig;
/*  71 */   protected ExecutorService executorService = null;
/*  72 */   protected ThreadPool.Singleton<ScannedDevices> scanningSingleton = new ThreadPool.Singleton();
/*     */   protected DeviceManager deviceManager;
/*     */   protected NextLock scanResultsSequence;
/*  75 */   protected final Object remoteScannedDevicesLock = new Object();
/*     */   protected ScannedDevices remoteScannedDevices;
/*  77 */   protected NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public USBScanManager(Context context, boolean isRemoteConfig)
/*     */     throws RobotCoreException
/*     */   {
/*  85 */     this.context = context;
/*  86 */     this.isRemoteConfig = isRemoteConfig;
/*  87 */     this.scanResultsSequence = new NextLock();
/*     */     
/*  89 */     if (!isRemoteConfig)
/*     */     {
/*  91 */       this.deviceManager = new HardwareDeviceManager(context, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startExecutorService(int extraThreads)
/*     */   {
/*  97 */     this.executorService = ThreadPool.newFixedThreadPool(1 + extraThreads);
/*  98 */     this.scanningSingleton.reset();
/*  99 */     this.scanningSingleton.setService(this.executorService);
/*     */   }
/*     */   
/*     */   public void stopExecutorService()
/*     */   {
/* 104 */     this.executorService.shutdownNow();
/* 105 */     ThreadPool.awaitTerminationOrExitApplication(this.executorService, 5L, TimeUnit.SECONDS, "USBScanManager service", "internal error");
/* 106 */     this.executorService = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExecutorService getExecutorService()
/*     */   {
/* 115 */     return this.executorService;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ThreadPool.SingletonResult<ScannedDevices> startDeviceScanIfNecessary()
/*     */   {
/* 124 */     this.scanningSingleton.submit(new Callable()
/*     */     {
/*     */       /* Error */
/*     */       public ScannedDevices call()
/*     */         throws InterruptedException
/*     */       {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: getfield 1	com/qualcomm/ftccommon/configuration/USBScanManager$1:this$0	Lcom/qualcomm/ftccommon/configuration/USBScanManager;
/*     */         //   4: getfield 3	com/qualcomm/ftccommon/configuration/USBScanManager:isRemoteConfig	Z
/*     */         //   7: ifeq +76 -> 83
/*     */         //   10: aload_0
/*     */         //   11: getfield 1	com/qualcomm/ftccommon/configuration/USBScanManager$1:this$0	Lcom/qualcomm/ftccommon/configuration/USBScanManager;
/*     */         //   14: getfield 4	com/qualcomm/ftccommon/configuration/USBScanManager:scanResultsSequence	Lcom/qualcomm/robotcore/util/NextLock;
/*     */         //   17: invokevirtual 5	com/qualcomm/robotcore/util/NextLock:getNextWaiter	()Lcom/qualcomm/robotcore/util/NextLock$Waiter;
/*     */         //   20: astore_1
/*     */         //   21: ldc 6
/*     */         //   23: ldc 7
/*     */         //   25: invokestatic 8	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;)V
/*     */         //   28: aload_0
/*     */         //   29: getfield 1	com/qualcomm/ftccommon/configuration/USBScanManager$1:this$0	Lcom/qualcomm/ftccommon/configuration/USBScanManager;
/*     */         //   32: getfield 9	com/qualcomm/ftccommon/configuration/USBScanManager:networkConnectionHandler	Lorg/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler;
/*     */         //   35: new 10	com/qualcomm/robotcore/robocol/Command
/*     */         //   38: dup
/*     */         //   39: ldc 11
/*     */         //   41: invokespecial 12	com/qualcomm/robotcore/robocol/Command:<init>	(Ljava/lang/String;)V
/*     */         //   44: invokevirtual 13	org/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler:sendCommand	(Lcom/qualcomm/robotcore/robocol/Command;)V
/*     */         //   47: aload_1
/*     */         //   48: invokevirtual 14	com/qualcomm/robotcore/util/NextLock$Waiter:awaitNext	()V
/*     */         //   51: ldc 6
/*     */         //   53: ldc 15
/*     */         //   55: invokestatic 8	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;)V
/*     */         //   58: aload_0
/*     */         //   59: getfield 1	com/qualcomm/ftccommon/configuration/USBScanManager$1:this$0	Lcom/qualcomm/ftccommon/configuration/USBScanManager;
/*     */         //   62: getfield 16	com/qualcomm/ftccommon/configuration/USBScanManager:remoteScannedDevicesLock	Ljava/lang/Object;
/*     */         //   65: dup
/*     */         //   66: astore_2
/*     */         //   67: monitorenter
/*     */         //   68: aload_0
/*     */         //   69: getfield 1	com/qualcomm/ftccommon/configuration/USBScanManager$1:this$0	Lcom/qualcomm/ftccommon/configuration/USBScanManager;
/*     */         //   72: getfield 17	com/qualcomm/ftccommon/configuration/USBScanManager:remoteScannedDevices	Lcom/qualcomm/ftccommon/configuration/ScannedDevices;
/*     */         //   75: aload_2
/*     */         //   76: monitorexit
/*     */         //   77: areturn
/*     */         //   78: astore_3
/*     */         //   79: aload_2
/*     */         //   80: monitorexit
/*     */         //   81: aload_3
/*     */         //   82: athrow
/*     */         //   83: ldc 6
/*     */         //   85: ldc 18
/*     */         //   87: invokestatic 8	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;)V
/*     */         //   90: aconst_null
/*     */         //   91: astore_1
/*     */         //   92: new 19	com/qualcomm/ftccommon/configuration/ScannedDevices
/*     */         //   95: dup
/*     */         //   96: aload_0
/*     */         //   97: getfield 1	com/qualcomm/ftccommon/configuration/USBScanManager$1:this$0	Lcom/qualcomm/ftccommon/configuration/USBScanManager;
/*     */         //   100: getfield 20	com/qualcomm/ftccommon/configuration/USBScanManager:deviceManager	Lcom/qualcomm/robotcore/hardware/DeviceManager;
/*     */         //   103: invokeinterface 21 1 0
/*     */         //   108: invokespecial 22	com/qualcomm/ftccommon/configuration/ScannedDevices:<init>	(Ljava/util/Map;)V
/*     */         //   111: astore_1
/*     */         //   112: ldc 6
/*     */         //   114: ldc 23
/*     */         //   116: iconst_1
/*     */         //   117: anewarray 24	java/lang/Object
/*     */         //   120: dup
/*     */         //   121: iconst_0
/*     */         //   122: aload_1
/*     */         //   123: ifnonnull +8 -> 131
/*     */         //   126: ldc 25
/*     */         //   128: goto +10 -> 138
/*     */         //   131: aload_1
/*     */         //   132: invokevirtual 26	com/qualcomm/ftccommon/configuration/ScannedDevices:keySet	()Ljava/util/Set;
/*     */         //   135: invokevirtual 27	java/lang/Object:toString	()Ljava/lang/String;
/*     */         //   138: aastore
/*     */         //   139: invokestatic 28	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*     */         //   142: goto +101 -> 243
/*     */         //   145: astore_2
/*     */         //   146: ldc 6
/*     */         //   148: new 30	java/lang/StringBuilder
/*     */         //   151: dup
/*     */         //   152: invokespecial 31	java/lang/StringBuilder:<init>	()V
/*     */         //   155: ldc 32
/*     */         //   157: invokevirtual 33	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   160: aload_2
/*     */         //   161: invokevirtual 34	com/qualcomm/robotcore/exception/RobotCoreException:toString	()Ljava/lang/String;
/*     */         //   164: invokevirtual 33	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   167: invokevirtual 35	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   170: invokestatic 36	com/qualcomm/robotcore/util/RobotLog:ee	(Ljava/lang/String;Ljava/lang/String;)V
/*     */         //   173: aconst_null
/*     */         //   174: astore_1
/*     */         //   175: ldc 6
/*     */         //   177: ldc 23
/*     */         //   179: iconst_1
/*     */         //   180: anewarray 24	java/lang/Object
/*     */         //   183: dup
/*     */         //   184: iconst_0
/*     */         //   185: aload_1
/*     */         //   186: ifnonnull +8 -> 194
/*     */         //   189: ldc 25
/*     */         //   191: goto +10 -> 201
/*     */         //   194: aload_1
/*     */         //   195: invokevirtual 26	com/qualcomm/ftccommon/configuration/ScannedDevices:keySet	()Ljava/util/Set;
/*     */         //   198: invokevirtual 27	java/lang/Object:toString	()Ljava/lang/String;
/*     */         //   201: aastore
/*     */         //   202: invokestatic 28	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*     */         //   205: goto +38 -> 243
/*     */         //   208: astore 4
/*     */         //   210: ldc 6
/*     */         //   212: ldc 23
/*     */         //   214: iconst_1
/*     */         //   215: anewarray 24	java/lang/Object
/*     */         //   218: dup
/*     */         //   219: iconst_0
/*     */         //   220: aload_1
/*     */         //   221: ifnonnull +8 -> 229
/*     */         //   224: ldc 25
/*     */         //   226: goto +10 -> 236
/*     */         //   229: aload_1
/*     */         //   230: invokevirtual 26	com/qualcomm/ftccommon/configuration/ScannedDevices:keySet	()Ljava/util/Set;
/*     */         //   233: invokevirtual 27	java/lang/Object:toString	()Ljava/lang/String;
/*     */         //   236: aastore
/*     */         //   237: invokestatic 28	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*     */         //   240: aload 4
/*     */         //   242: athrow
/*     */         //   243: aload_1
/*     */         //   244: areturn
/*     */         // Line number table:
/*     */         //   Java source line #128	-> byte code offset #0
/*     */         //   Java source line #131	-> byte code offset #10
/*     */         //   Java source line #134	-> byte code offset #21
/*     */         //   Java source line #135	-> byte code offset #28
/*     */         //   Java source line #138	-> byte code offset #47
/*     */         //   Java source line #139	-> byte code offset #51
/*     */         //   Java source line #142	-> byte code offset #58
/*     */         //   Java source line #144	-> byte code offset #68
/*     */         //   Java source line #145	-> byte code offset #78
/*     */         //   Java source line #149	-> byte code offset #83
/*     */         //   Java source line #150	-> byte code offset #90
/*     */         //   Java source line #153	-> byte code offset #92
/*     */         //   Java source line #162	-> byte code offset #112
/*     */         //   Java source line #163	-> byte code offset #142
/*     */         //   Java source line #155	-> byte code offset #145
/*     */         //   Java source line #157	-> byte code offset #146
/*     */         //   Java source line #158	-> byte code offset #173
/*     */         //   Java source line #162	-> byte code offset #175
/*     */         //   Java source line #163	-> byte code offset #205
/*     */         //   Java source line #162	-> byte code offset #208
/*     */         //   Java source line #164	-> byte code offset #243
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	245	0	this	1
/*     */         //   20	28	1	waiter	com.qualcomm.robotcore.util.NextLock.Waiter
/*     */         //   91	153	1	localResult	ScannedDevices
/*     */         //   66	14	2	Ljava/lang/Object;	Object
/*     */         //   145	16	2	e	RobotCoreException
/*     */         //   78	4	3	localObject1	Object
/*     */         //   208	33	4	localObject2	Object
/*     */         // Exception table:
/*     */         //   from	to	target	type
/*     */         //   68	77	78	finally
/*     */         //   78	81	78	finally
/*     */         //   92	112	145	com/qualcomm/robotcore/exception/RobotCoreException
/*     */         //   92	112	208	finally
/*     */         //   145	175	208	finally
/*     */         //   208	210	208	finally
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public ScannedDevices await(long ms)
/*     */     throws InterruptedException
/*     */   {
/* 172 */     ScannedDevices result = (ScannedDevices)this.scanningSingleton.await(ms);
/* 173 */     if (result == null)
/*     */     {
/* 175 */       RobotLog.vv("FtcConfigTag", "USBScanManager.await() returning made-up scan result");
/* 176 */       result = new ScannedDevices();
/*     */     }
/* 178 */     return result;
/*     */   }
/*     */   
/*     */   public String packageCommandResponse(ScannedDevices scannedDevices)
/*     */   {
/* 183 */     return scannedDevices.toSerializationString();
/*     */   }
/*     */   
/*     */   public void handleCommandScanResponse(String extra) throws RobotCoreException
/*     */   {
/* 188 */     ScannedDevices scannedDevices = ScannedDevices.fromSerializationString(extra);
/* 189 */     synchronized (this.remoteScannedDevicesLock)
/*     */     {
/* 191 */       this.remoteScannedDevices = scannedDevices;
/* 192 */       this.scanResultsSequence.advanceNext();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\USBScanManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */