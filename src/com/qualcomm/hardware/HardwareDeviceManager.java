/*     */ package com.qualcomm.hardware;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtAccelerationSensor;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtColorSensor;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtCompassSensor;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtDcMotorController;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtGyroSensor;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtIrSeekerSensor;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtServoController;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtTouchSensor;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtTouchSensorMultiplexer;
/*     */ import com.qualcomm.hardware.hitechnic.HiTechnicNxtUltrasonicSensor;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsDigitalTouchSensor;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cIrSeekerSensorV3;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDcMotorController;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbServoController;
/*     */ import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
/*     */ import com.qualcomm.modernrobotics.RobotUsbManagerEmulator;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.AccelerationSensor;
/*     */ import com.qualcomm.robotcore.hardware.AnalogInput;
/*     */ import com.qualcomm.robotcore.hardware.AnalogInputController;
/*     */ import com.qualcomm.robotcore.hardware.AnalogOutput;
/*     */ import com.qualcomm.robotcore.hardware.AnalogOutputController;
/*     */ import com.qualcomm.robotcore.hardware.CRServo;
/*     */ import com.qualcomm.robotcore.hardware.CRServoImpl;
/*     */ import com.qualcomm.robotcore.hardware.ColorSensor;
/*     */ import com.qualcomm.robotcore.hardware.CompassSensor;
/*     */ import com.qualcomm.robotcore.hardware.DcMotor;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorController;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorImpl;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorImplEx;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
/*     */ import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.DigitalChannel;
/*     */ import com.qualcomm.robotcore.hardware.DigitalChannelController;
/*     */ import com.qualcomm.robotcore.hardware.GyroSensor;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cDevice;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.IrSeekerSensor;
/*     */ import com.qualcomm.robotcore.hardware.LED;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.LightSensor;
/*     */ import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
/*     */ import com.qualcomm.robotcore.hardware.PWMOutput;
/*     */ import com.qualcomm.robotcore.hardware.PWMOutputController;
/*     */ import com.qualcomm.robotcore.hardware.PWMOutputImpl;
/*     */ import com.qualcomm.robotcore.hardware.PWMOutputImplEx;
/*     */ import com.qualcomm.robotcore.hardware.Servo;
/*     */ import com.qualcomm.robotcore.hardware.Servo.Direction;
/*     */ import com.qualcomm.robotcore.hardware.ServoController;
/*     */ import com.qualcomm.robotcore.hardware.ServoImpl;
/*     */ import com.qualcomm.robotcore.hardware.ServoImplEx;
/*     */ import com.qualcomm.robotcore.hardware.TouchSensor;
/*     */ import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
/*     */ import com.qualcomm.robotcore.hardware.UltrasonicSensor;
/*     */ import com.qualcomm.robotcore.hardware.configuration.UserSensorType;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.FirmwareVersion;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.USBIdentifiers;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
/*     */ import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbManagerFtdi;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HardwareDeviceManager
/*     */   implements DeviceManager
/*     */ {
/*     */   public static final String TAG = "HardwareDeviceManager";
/*     */   public static final String TAG_USB_SCAN = "USBScan";
/*     */   
/*     */   private static enum Mode
/*     */   {
/* 131 */     DEFAULT, 
/* 132 */     ENABLE_DEVICE_EMULATION;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Mode() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 142 */   private static Mode mode = Mode.DEFAULT;
/*     */   
/* 144 */   public static final Object scanDevicesLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */   private RobotUsbManager usbManager;
/*     */   
/*     */ 
/*     */   private final EventLoopManager manager;
/*     */   
/*     */ 
/*     */   private final Context context;
/*     */   
/*     */ 
/*     */ 
/*     */   public HardwareDeviceManager(Context context, EventLoopManager manager)
/*     */     throws RobotCoreException
/*     */   {
/* 161 */     this.context = context;
/* 162 */     this.manager = manager;
/*     */     
/* 164 */     switch (mode) {
/*     */     case ENABLE_DEVICE_EMULATION: 
/* 166 */       this.usbManager = new RobotUsbManagerEmulator();
/* 167 */       break;
/*     */     default: 
/* 169 */       this.usbManager = new RobotUsbManagerFtdi(context);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Map<SerialNumber, DeviceManager.DeviceType> scanForUsbDevices()
/*     */     throws RobotCoreException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 14	com/qualcomm/hardware/HardwareDeviceManager:scanDevicesLock	Ljava/lang/Object;
/*     */     //   3: dup
/*     */     //   4: astore_1
/*     */     //   5: monitorenter
/*     */     //   6: invokestatic 15	java/lang/System:nanoTime	()J
/*     */     //   9: lstore_2
/*     */     //   10: new 16	java/util/concurrent/ConcurrentHashMap
/*     */     //   13: dup
/*     */     //   14: invokespecial 17	java/util/concurrent/ConcurrentHashMap:<init>	()V
/*     */     //   17: astore 4
/*     */     //   19: aload_0
/*     */     //   20: getfield 3	com/qualcomm/hardware/HardwareDeviceManager:usbManager	Lcom/qualcomm/robotcore/hardware/usb/RobotUsbManager;
/*     */     //   23: invokeinterface 18 1 0
/*     */     //   28: istore 5
/*     */     //   30: aload_0
/*     */     //   31: getfield 3	com/qualcomm/hardware/HardwareDeviceManager:usbManager	Lcom/qualcomm/robotcore/hardware/usb/RobotUsbManager;
/*     */     //   34: invokeinterface 19 1 0
/*     */     //   39: iload 5
/*     */     //   41: ifle +416 -> 457
/*     */     //   44: iload 5
/*     */     //   46: invokestatic 20	com/qualcomm/robotcore/util/ThreadPool:newFixedThreadPool	(I)Ljava/util/concurrent/ExecutorService;
/*     */     //   49: astore 6
/*     */     //   51: new 16	java/util/concurrent/ConcurrentHashMap
/*     */     //   54: dup
/*     */     //   55: invokespecial 17	java/util/concurrent/ConcurrentHashMap:<init>	()V
/*     */     //   58: astore 7
/*     */     //   60: iconst_0
/*     */     //   61: istore 8
/*     */     //   63: iload 8
/*     */     //   65: iload 5
/*     */     //   67: if_icmpge +41 -> 108
/*     */     //   70: aload_0
/*     */     //   71: getfield 3	com/qualcomm/hardware/HardwareDeviceManager:usbManager	Lcom/qualcomm/robotcore/hardware/usb/RobotUsbManager;
/*     */     //   74: iload 8
/*     */     //   76: invokeinterface 21 2 0
/*     */     //   81: astore 9
/*     */     //   83: aload 6
/*     */     //   85: new 22	com/qualcomm/hardware/HardwareDeviceManager$1
/*     */     //   88: dup
/*     */     //   89: aload_0
/*     */     //   90: aload 9
/*     */     //   92: aload 7
/*     */     //   94: invokespecial 23	com/qualcomm/hardware/HardwareDeviceManager$1:<init>	(Lcom/qualcomm/hardware/HardwareDeviceManager;Lcom/qualcomm/robotcore/util/SerialNumber;Ljava/util/concurrent/ConcurrentHashMap;)V
/*     */     //   97: invokeinterface 24 2 0
/*     */     //   102: iinc 8 1
/*     */     //   105: goto -42 -> 63
/*     */     //   108: aload 6
/*     */     //   110: invokeinterface 25 1 0
/*     */     //   115: aload 6
/*     */     //   117: ldc2_w 26
/*     */     //   120: getstatic 28	java/util/concurrent/TimeUnit:SECONDS	Ljava/util/concurrent/TimeUnit;
/*     */     //   123: ldc 29
/*     */     //   125: ldc 30
/*     */     //   127: invokestatic 31	com/qualcomm/robotcore/util/ThreadPool:awaitTerminationOrExitApplication	(Ljava/util/concurrent/ExecutorService;JLjava/util/concurrent/TimeUnit;Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   130: aload 7
/*     */     //   132: invokevirtual 32	java/util/concurrent/ConcurrentHashMap:entrySet	()Ljava/util/Set;
/*     */     //   135: invokeinterface 33 1 0
/*     */     //   140: astore 8
/*     */     //   142: aload 8
/*     */     //   144: invokeinterface 34 1 0
/*     */     //   149: ifeq +44 -> 193
/*     */     //   152: aload 8
/*     */     //   154: invokeinterface 35 1 0
/*     */     //   159: checkcast 36	java/util/Map$Entry
/*     */     //   162: astore 9
/*     */     //   164: aload_0
/*     */     //   165: aload 9
/*     */     //   167: invokeinterface 37 1 0
/*     */     //   172: checkcast 38	com/qualcomm/robotcore/hardware/usb/RobotUsbDevice
/*     */     //   175: aload 9
/*     */     //   177: invokeinterface 39 1 0
/*     */     //   182: checkcast 40	com/qualcomm/robotcore/util/SerialNumber
/*     */     //   185: aload 4
/*     */     //   187: invokevirtual 41	com/qualcomm/hardware/HardwareDeviceManager:determineModernRoboticsDeviceType	(Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice;Lcom/qualcomm/robotcore/util/SerialNumber;Ljava/util/Map;)V
/*     */     //   190: goto -48 -> 142
/*     */     //   193: invokestatic 42	com/qualcomm/robotcore/hardware/usb/ftdi/RobotUsbDeviceFtdi:getExtantDevices	()Ljava/util/Collection;
/*     */     //   196: invokeinterface 43 1 0
/*     */     //   201: astore 8
/*     */     //   203: aload 8
/*     */     //   205: invokeinterface 34 1 0
/*     */     //   210: ifeq +93 -> 303
/*     */     //   213: aload 8
/*     */     //   215: invokeinterface 35 1 0
/*     */     //   220: checkcast 38	com/qualcomm/robotcore/hardware/usb/RobotUsbDevice
/*     */     //   223: astore 9
/*     */     //   225: aload 9
/*     */     //   227: invokeinterface 44 1 0
/*     */     //   232: astore 10
/*     */     //   234: aload 7
/*     */     //   236: aload 10
/*     */     //   238: invokevirtual 45	java/util/concurrent/ConcurrentHashMap:containsKey	(Ljava/lang/Object;)Z
/*     */     //   241: ifne +59 -> 300
/*     */     //   244: aload 9
/*     */     //   246: invokeinterface 46 1 0
/*     */     //   251: astore 11
/*     */     //   253: aload 11
/*     */     //   255: getstatic 47	com/qualcomm/robotcore/hardware/DeviceManager$DeviceType:FTDI_USB_UNKNOWN_DEVICE	Lcom/qualcomm/robotcore/hardware/DeviceManager$DeviceType;
/*     */     //   258: if_acmpeq +42 -> 300
/*     */     //   261: ldc 48
/*     */     //   263: ldc 49
/*     */     //   265: iconst_2
/*     */     //   266: anewarray 50	java/lang/Object
/*     */     //   269: dup
/*     */     //   270: iconst_0
/*     */     //   271: aload 10
/*     */     //   273: invokevirtual 51	com/qualcomm/robotcore/util/SerialNumber:toString	()Ljava/lang/String;
/*     */     //   276: aastore
/*     */     //   277: dup
/*     */     //   278: iconst_1
/*     */     //   279: aload 11
/*     */     //   281: invokevirtual 52	com/qualcomm/robotcore/hardware/DeviceManager$DeviceType:toString	()Ljava/lang/String;
/*     */     //   284: aastore
/*     */     //   285: invokestatic 53	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   288: aload 4
/*     */     //   290: aload 10
/*     */     //   292: aload 11
/*     */     //   294: invokeinterface 54 3 0
/*     */     //   299: pop
/*     */     //   300: goto -97 -> 203
/*     */     //   303: aload 7
/*     */     //   305: invokevirtual 32	java/util/concurrent/ConcurrentHashMap:entrySet	()Ljava/util/Set;
/*     */     //   308: invokeinterface 33 1 0
/*     */     //   313: astore 8
/*     */     //   315: aload 8
/*     */     //   317: invokeinterface 34 1 0
/*     */     //   322: ifeq +54 -> 376
/*     */     //   325: aload 8
/*     */     //   327: invokeinterface 35 1 0
/*     */     //   332: checkcast 36	java/util/Map$Entry
/*     */     //   335: astore 9
/*     */     //   337: ldc 48
/*     */     //   339: ldc 55
/*     */     //   341: iconst_1
/*     */     //   342: anewarray 50	java/lang/Object
/*     */     //   345: dup
/*     */     //   346: iconst_0
/*     */     //   347: aload 9
/*     */     //   349: invokeinterface 39 1 0
/*     */     //   354: aastore
/*     */     //   355: invokestatic 53	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   358: aload 9
/*     */     //   360: invokeinterface 37 1 0
/*     */     //   365: checkcast 38	com/qualcomm/robotcore/hardware/usb/RobotUsbDevice
/*     */     //   368: invokeinterface 56 1 0
/*     */     //   373: goto -58 -> 315
/*     */     //   376: goto +81 -> 457
/*     */     //   379: astore 12
/*     */     //   381: aload 7
/*     */     //   383: invokevirtual 32	java/util/concurrent/ConcurrentHashMap:entrySet	()Ljava/util/Set;
/*     */     //   386: invokeinterface 33 1 0
/*     */     //   391: astore 13
/*     */     //   393: aload 13
/*     */     //   395: invokeinterface 34 1 0
/*     */     //   400: ifeq +54 -> 454
/*     */     //   403: aload 13
/*     */     //   405: invokeinterface 35 1 0
/*     */     //   410: checkcast 36	java/util/Map$Entry
/*     */     //   413: astore 14
/*     */     //   415: ldc 48
/*     */     //   417: ldc 55
/*     */     //   419: iconst_1
/*     */     //   420: anewarray 50	java/lang/Object
/*     */     //   423: dup
/*     */     //   424: iconst_0
/*     */     //   425: aload 14
/*     */     //   427: invokeinterface 39 1 0
/*     */     //   432: aastore
/*     */     //   433: invokestatic 53	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   436: aload 14
/*     */     //   438: invokeinterface 37 1 0
/*     */     //   443: checkcast 38	com/qualcomm/robotcore/hardware/usb/RobotUsbDevice
/*     */     //   446: invokeinterface 56 1 0
/*     */     //   451: goto -58 -> 393
/*     */     //   454: aload 12
/*     */     //   456: athrow
/*     */     //   457: aload_0
/*     */     //   458: getfield 3	com/qualcomm/hardware/HardwareDeviceManager:usbManager	Lcom/qualcomm/robotcore/hardware/usb/RobotUsbManager;
/*     */     //   461: invokeinterface 57 1 0
/*     */     //   466: goto +17 -> 483
/*     */     //   469: astore 15
/*     */     //   471: aload_0
/*     */     //   472: getfield 3	com/qualcomm/hardware/HardwareDeviceManager:usbManager	Lcom/qualcomm/robotcore/hardware/usb/RobotUsbManager;
/*     */     //   475: invokeinterface 57 1 0
/*     */     //   480: aload 15
/*     */     //   482: athrow
/*     */     //   483: invokestatic 15	java/lang/System:nanoTime	()J
/*     */     //   486: lstore 6
/*     */     //   488: ldc 48
/*     */     //   490: ldc 58
/*     */     //   492: iconst_2
/*     */     //   493: anewarray 50	java/lang/Object
/*     */     //   496: dup
/*     */     //   497: iconst_0
/*     */     //   498: lload 6
/*     */     //   500: lload_2
/*     */     //   501: lsub
/*     */     //   502: ldc2_w 59
/*     */     //   505: ldiv
/*     */     //   506: l2i
/*     */     //   507: invokestatic 61	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   510: aastore
/*     */     //   511: dup
/*     */     //   512: iconst_1
/*     */     //   513: aload 4
/*     */     //   515: invokeinterface 62 1 0
/*     */     //   520: invokestatic 61	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   523: aastore
/*     */     //   524: invokestatic 53	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   527: aload 4
/*     */     //   529: aload_1
/*     */     //   530: monitorexit
/*     */     //   531: areturn
/*     */     //   532: astore 16
/*     */     //   534: aload_1
/*     */     //   535: monitorexit
/*     */     //   536: aload 16
/*     */     //   538: athrow
/*     */     // Line number table:
/*     */     //   Java source line #184	-> byte code offset #0
/*     */     //   Java source line #185	-> byte code offset #6
/*     */     //   Java source line #186	-> byte code offset #10
/*     */     //   Java source line #187	-> byte code offset #19
/*     */     //   Java source line #188	-> byte code offset #30
/*     */     //   Java source line #190	-> byte code offset #39
/*     */     //   Java source line #192	-> byte code offset #44
/*     */     //   Java source line #193	-> byte code offset #51
/*     */     //   Java source line #195	-> byte code offset #60
/*     */     //   Java source line #196	-> byte code offset #70
/*     */     //   Java source line #197	-> byte code offset #83
/*     */     //   Java source line #195	-> byte code offset #102
/*     */     //   Java source line #212	-> byte code offset #108
/*     */     //   Java source line #213	-> byte code offset #115
/*     */     //   Java source line #216	-> byte code offset #130
/*     */     //   Java source line #217	-> byte code offset #164
/*     */     //   Java source line #218	-> byte code offset #190
/*     */     //   Java source line #221	-> byte code offset #193
/*     */     //   Java source line #222	-> byte code offset #225
/*     */     //   Java source line #223	-> byte code offset #234
/*     */     //   Java source line #224	-> byte code offset #244
/*     */     //   Java source line #225	-> byte code offset #253
/*     */     //   Java source line #226	-> byte code offset #261
/*     */     //   Java source line #227	-> byte code offset #288
/*     */     //   Java source line #230	-> byte code offset #300
/*     */     //   Java source line #234	-> byte code offset #303
/*     */     //   Java source line #235	-> byte code offset #337
/*     */     //   Java source line #236	-> byte code offset #358
/*     */     //   Java source line #237	-> byte code offset #373
/*     */     //   Java source line #238	-> byte code offset #376
/*     */     //   Java source line #234	-> byte code offset #379
/*     */     //   Java source line #235	-> byte code offset #415
/*     */     //   Java source line #236	-> byte code offset #436
/*     */     //   Java source line #237	-> byte code offset #451
/*     */     //   Java source line #241	-> byte code offset #457
/*     */     //   Java source line #242	-> byte code offset #466
/*     */     //   Java source line #241	-> byte code offset #469
/*     */     //   Java source line #243	-> byte code offset #483
/*     */     //   Java source line #244	-> byte code offset #488
/*     */     //   Java source line #245	-> byte code offset #527
/*     */     //   Java source line #246	-> byte code offset #532
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	539	0	this	HardwareDeviceManager
/*     */     //   4	531	1	Ljava/lang/Object;	Object
/*     */     //   9	492	2	start	long
/*     */     //   17	511	4	deviceMap	Map<SerialNumber, DeviceManager.DeviceType>
/*     */     //   28	38	5	devCount	int
/*     */     //   49	67	6	executorService	java.util.concurrent.ExecutorService
/*     */     //   486	13	6	end	long
/*     */     //   58	324	7	newlyFoundDevices	ConcurrentHashMap<SerialNumber, RobotUsbDevice>
/*     */     //   61	42	8	id	int
/*     */     //   140	13	8	i$	java.util.Iterator
/*     */     //   201	13	8	i$	java.util.Iterator
/*     */     //   313	13	8	i$	java.util.Iterator
/*     */     //   81	10	9	serialNumber	SerialNumber
/*     */     //   162	14	9	pair	java.util.Map.Entry<SerialNumber, RobotUsbDevice>
/*     */     //   223	22	9	existingDevice	RobotUsbDevice
/*     */     //   335	24	9	pair	java.util.Map.Entry<SerialNumber, RobotUsbDevice>
/*     */     //   232	59	10	serialNumber	SerialNumber
/*     */     //   251	42	11	deviceType	DeviceManager.DeviceType
/*     */     //   379	76	12	localObject1	Object
/*     */     //   391	13	13	i$	java.util.Iterator
/*     */     //   413	24	14	pair	java.util.Map.Entry<SerialNumber, RobotUsbDevice>
/*     */     //   469	12	15	localObject2	Object
/*     */     //   532	5	16	localObject3	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   60	303	379	finally
/*     */     //   379	381	379	finally
/*     */     //   39	457	469	finally
/*     */     //   469	471	469	finally
/*     */     //   6	531	532	finally
/*     */     //   532	536	532	finally
/*     */   }
/*     */   
/*     */   void determineModernRoboticsDeviceType(RobotUsbDevice dev, SerialNumber serialNumber, Map<SerialNumber, DeviceManager.DeviceType> deviceMap)
/*     */   {
/* 251 */     DeviceManager.DeviceType deviceType = DeviceManager.DeviceType.UNKNOWN_DEVICE;
/* 252 */     RobotUsbDevice.USBIdentifiers ids = dev.getUsbIdentifiers();
/* 253 */     if (ids.isModernRoboticsDevice()) {
/*     */       try {
/* 255 */         RobotLog.vv("USBScan", "getting MR device device header %s ...", new Object[] { serialNumber });
/* 256 */         deviceType = getModernRoboticsDeviceType(dev);
/* 257 */         RobotLog.vv("USBScan", "... done getting MR device device header %s type=%s", new Object[] { serialNumber, deviceType });
/*     */       } catch (RobotCoreException ignored) {
/* 259 */         RobotLog.vv("USBScan", "exception retrieving MR device device header %s", new Object[] { serialNumber });
/* 260 */         return;
/*     */       }
/*     */       
/*     */     } else {
/* 264 */       return;
/*     */     }
/*     */     
/* 267 */     deviceMap.put(serialNumber, deviceType);
/*     */   }
/*     */   
/*     */   DeviceManager.DeviceType getModernRoboticsDeviceType(RobotUsbDevice dev) throws RobotCoreException {
/* 271 */     byte[] modernRoboticsDeviceHeader = getModernRoboticsDeviceHeader(dev);
/* 272 */     return getModernRoboticsDeviceType(dev, modernRoboticsDeviceHeader);
/*     */   }
/*     */   
/*     */   DeviceManager.DeviceType getModernRoboticsDeviceType(RobotUsbDevice dev, byte[] modernRoboticsDeviceHeader) throws RobotCoreException {
/* 276 */     DeviceManager.DeviceType deviceType = ModernRoboticsUsbUtil.getDeviceType(modernRoboticsDeviceHeader);
/*     */     
/* 278 */     dev.setDeviceType(deviceType);
/* 279 */     return deviceType;
/*     */   }
/*     */   
/*     */   byte[] getModernRoboticsDeviceHeader(RobotUsbDevice dev) throws RobotCoreException {
/* 283 */     return ModernRoboticsUsbUtil.getUsbDeviceHeader(dev);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DcMotorController createUsbDcMotorController(final SerialNumber serialNumber)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 296 */     HardwareFactory.noteSerialNumberType(this.context, serialNumber, this.context.getString(R.string.moduleDisplayNameMotorController));
/* 297 */     RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
/*     */     
/* 299 */     ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
/*     */       public RobotUsbDevice open() throws RobotCoreException, InterruptedException {
/* 301 */         RobotUsbDevice dev = null;
/*     */         try {
/* 303 */           dev = ModernRoboticsUsbUtil.openUsbDevice(HardwareDeviceManager.this.usbManager, serialNumber);
/* 304 */           byte[] deviceHeader = HardwareDeviceManager.this.getModernRoboticsDeviceHeader(dev);
/* 305 */           DeviceManager.DeviceType type = HardwareDeviceManager.this.getModernRoboticsDeviceType(dev, deviceHeader);
/*     */           
/* 307 */           if (type != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
/* 308 */             HardwareDeviceManager.this.closeAndThrowOnFailedDeviceTypeCheck(dev, serialNumber);
/*     */           }
/* 310 */           dev.setFirmwareVersion(HardwareDeviceManager.this.getModernRoboticsFirmwareVersion(deviceHeader));
/*     */         } catch (RobotCoreException e) {
/* 312 */           if (dev != null) dev.close();
/* 313 */           throw e;
/*     */         } catch (RuntimeException e) {
/* 315 */           if (dev != null) dev.close();
/* 316 */           throw e;
/*     */         }
/* 318 */         return dev;
/*     */       }
/*     */       
/* 321 */     };
/* 322 */     ModernRoboticsUsbDcMotorController controller = new ModernRoboticsUsbDcMotorController(this.context, serialNumber, openRobotUsbDevice, this.manager);
/* 323 */     controller.armOrPretend();
/* 324 */     controller.initializeHardware();
/* 325 */     return controller;
/*     */   }
/*     */   
/*     */   public DcMotor createDcMotor(DcMotorController controller, int portNumber)
/*     */   {
/* 330 */     return new DcMotorImpl(controller, portNumber, DcMotorSimple.Direction.FORWARD);
/*     */   }
/*     */   
/*     */   public DcMotor createDcMotorEx(DcMotorController controller, int portNumber) {
/* 334 */     return new DcMotorImplEx(controller, portNumber, DcMotorSimple.Direction.FORWARD);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServoController createUsbServoController(final SerialNumber serialNumber)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 344 */     HardwareFactory.noteSerialNumberType(this.context, serialNumber, this.context.getString(R.string.moduleDisplayNameServoController));
/* 345 */     RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
/*     */     
/* 347 */     ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
/*     */       public RobotUsbDevice open() throws RobotCoreException, InterruptedException {
/* 349 */         RobotUsbDevice dev = null;
/*     */         try {
/* 351 */           dev = ModernRoboticsUsbUtil.openUsbDevice(HardwareDeviceManager.this.usbManager, serialNumber);
/* 352 */           byte[] deviceHeader = HardwareDeviceManager.this.getModernRoboticsDeviceHeader(dev);
/* 353 */           DeviceManager.DeviceType type = HardwareDeviceManager.this.getModernRoboticsDeviceType(dev, deviceHeader);
/*     */           
/* 355 */           if (type != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
/* 356 */             HardwareDeviceManager.this.closeAndThrowOnFailedDeviceTypeCheck(dev, serialNumber);
/*     */           }
/* 358 */           dev.setFirmwareVersion(HardwareDeviceManager.this.getModernRoboticsFirmwareVersion(deviceHeader));
/*     */         } catch (RobotCoreException e) {
/* 360 */           if (dev != null) dev.close();
/* 361 */           throw e;
/*     */         } catch (RuntimeException e) {
/* 363 */           if (dev != null) dev.close();
/* 364 */           throw e;
/*     */         }
/* 366 */         return dev;
/*     */       }
/*     */       
/* 369 */     };
/* 370 */     ModernRoboticsUsbServoController controller = new ModernRoboticsUsbServoController(this.context, serialNumber, openRobotUsbDevice, this.manager);
/* 371 */     controller.armOrPretend();
/* 372 */     controller.initializeHardware();
/* 373 */     return controller;
/*     */   }
/*     */   
/*     */   public Servo createServo(ServoController controller, int portNumber)
/*     */   {
/* 378 */     return new ServoImpl(controller, portNumber, Servo.Direction.FORWARD);
/*     */   }
/*     */   
/*     */   public CRServo createCRServo(ServoController controller, int portNumber)
/*     */   {
/* 383 */     return new CRServoImpl(controller, portNumber, DcMotorSimple.Direction.FORWARD);
/*     */   }
/*     */   
/*     */   public Servo createServoEx(ServoController controller, int portNumber)
/*     */   {
/* 388 */     return new ServoImplEx(controller, portNumber, Servo.Direction.FORWARD);
/*     */   }
/*     */   
/*     */   public DeviceInterfaceModule createDeviceInterfaceModule(final SerialNumber serialNumber)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 394 */     HardwareFactory.noteSerialNumberType(this.context, serialNumber, this.context.getString(R.string.moduleDisplayNameCDIM));
/* 395 */     RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
/*     */     
/* 397 */     ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
/*     */       public RobotUsbDevice open() throws RobotCoreException, InterruptedException {
/* 399 */         RobotUsbDevice dev = null;
/*     */         try {
/* 401 */           dev = ModernRoboticsUsbUtil.openUsbDevice(HardwareDeviceManager.this.usbManager, serialNumber);
/* 402 */           byte[] deviceHeader = HardwareDeviceManager.this.getModernRoboticsDeviceHeader(dev);
/* 403 */           DeviceManager.DeviceType type = HardwareDeviceManager.this.getModernRoboticsDeviceType(dev, deviceHeader);
/*     */           
/* 405 */           if (type != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE) {
/* 406 */             HardwareDeviceManager.this.closeAndThrowOnFailedDeviceTypeCheck(dev, serialNumber);
/*     */           }
/* 408 */           dev.setFirmwareVersion(HardwareDeviceManager.this.getModernRoboticsFirmwareVersion(deviceHeader));
/*     */         } catch (RobotCoreException e) {
/* 410 */           if (dev != null) dev.close();
/* 411 */           throw e;
/*     */         } catch (RuntimeException e) {
/* 413 */           if (dev != null) dev.close();
/* 414 */           throw e;
/*     */         }
/* 416 */         return dev;
/*     */       }
/* 418 */     };
/* 419 */     ModernRoboticsUsbDeviceInterfaceModule deviceInterfaceModule = new ModernRoboticsUsbDeviceInterfaceModule(this.context, serialNumber, openRobotUsbDevice, this.manager);
/* 420 */     deviceInterfaceModule.armOrPretend();
/* 421 */     deviceInterfaceModule.initializeHardware();
/* 422 */     return deviceInterfaceModule;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LegacyModule createUsbLegacyModule(final SerialNumber serialNumber)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 431 */     HardwareFactory.noteSerialNumberType(this.context, serialNumber, this.context.getString(R.string.moduleDisplayNameLegacyModule));
/* 432 */     RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
/*     */     
/* 434 */     ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
/*     */       public RobotUsbDevice open() throws RobotCoreException, InterruptedException {
/* 436 */         RobotUsbDevice dev = null;
/*     */         try {
/* 438 */           dev = ModernRoboticsUsbUtil.openUsbDevice(HardwareDeviceManager.this.usbManager, serialNumber);
/* 439 */           byte[] deviceHeader = HardwareDeviceManager.this.getModernRoboticsDeviceHeader(dev);
/* 440 */           DeviceManager.DeviceType type = HardwareDeviceManager.this.getModernRoboticsDeviceType(dev, deviceHeader);
/*     */           
/* 442 */           if (type != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
/* 443 */             HardwareDeviceManager.this.closeAndThrowOnFailedDeviceTypeCheck(dev, serialNumber);
/*     */           }
/* 445 */           dev.setFirmwareVersion(HardwareDeviceManager.this.getModernRoboticsFirmwareVersion(deviceHeader));
/*     */         } catch (RobotCoreException e) {
/* 447 */           if (dev != null) dev.close();
/* 448 */           throw e;
/*     */         } catch (RuntimeException e) {
/* 450 */           if (dev != null) dev.close();
/* 451 */           throw e;
/*     */         }
/* 453 */         return dev;
/*     */       }
/* 455 */     };
/* 456 */     ModernRoboticsUsbLegacyModule legacyModule = new ModernRoboticsUsbLegacyModule(this.context, serialNumber, openRobotUsbDevice, this.manager);
/* 457 */     legacyModule.armOrPretend();
/* 458 */     legacyModule.initializeHardware();
/* 459 */     return legacyModule;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DcMotorController createNxtDcMotorController(LegacyModule legacyModule, int physicalPort)
/*     */   {
/* 467 */     RobotLog.v("Creating HiTechnic NXT DC Motor Controller - Port: " + physicalPort);
/* 468 */     return new HiTechnicNxtDcMotorController(this.context, legacyModule, physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServoController createNxtServoController(LegacyModule legacyModule, int physicalPort)
/*     */   {
/* 476 */     RobotLog.v("Creating HiTechnic NXT Servo Controller - Port: " + physicalPort);
/* 477 */     return new HiTechnicNxtServoController(this.context, legacyModule, physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompassSensor createNxtCompassSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/* 485 */     RobotLog.v("Creating HiTechnic NXT Compass Sensor - Port: " + physicalPort);
/* 486 */     return new HiTechnicNxtCompassSensor(promote(legacyModule), physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TouchSensor createDigitalTouchSensor(DeviceInterfaceModule deviceInterfaceModule, int physicalPort)
/*     */   {
/* 494 */     RobotLog.v("Creating Modern Robotics Digital Touch Sensor - Port: " + physicalPort);
/* 495 */     return new ModernRoboticsDigitalTouchSensor(promote(deviceInterfaceModule), physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AccelerationSensor createNxtAccelerationSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/* 503 */     RobotLog.v("Creating HiTechnic NXT Acceleration Sensor - Port: " + physicalPort);
/* 504 */     return new HiTechnicNxtAccelerationSensor(promote(legacyModule), physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LightSensor createNxtLightSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/* 512 */     RobotLog.v("Creating HiTechnic NXT Light Sensor - Port: " + physicalPort);
/* 513 */     return new HiTechnicNxtLightSensor(promote(legacyModule), physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GyroSensor createNxtGyroSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/* 521 */     RobotLog.v("Creating HiTechnic NXT Gyro Sensor - Port: " + physicalPort);
/* 522 */     return new HiTechnicNxtGyroSensor(promote(legacyModule), physicalPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IrSeekerSensor createNxtIrSeekerSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/* 530 */     RobotLog.v("Creating HiTechnic NXT IR Seeker Sensor - Port: " + physicalPort);
/* 531 */     return new HiTechnicNxtIrSeekerSensor(promote(legacyModule), physicalPort);
/*     */   }
/*     */   
/*     */   public IrSeekerSensor createI2cIrSeekerSensorV3(I2cController i2cController, int physicalPort)
/*     */   {
/* 536 */     RobotLog.v("Creating Modern Robotics I2C IR Seeker Sensor V3 - Port: " + physicalPort);
/* 537 */     return new ModernRoboticsI2cIrSeekerSensorV3(i2cController, physicalPort);
/*     */   }
/*     */   
/*     */   public UltrasonicSensor createNxtUltrasonicSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/* 542 */     RobotLog.v("Creating HiTechnic NXT Ultrasonic Sensor - Port: " + physicalPort);
/* 543 */     return new HiTechnicNxtUltrasonicSensor(promote(legacyModule), physicalPort);
/*     */   }
/*     */   
/*     */   public OpticalDistanceSensor createAnalogOpticalDistanceSensor(AnalogInputController analogInputController, int physicalPort)
/*     */   {
/* 548 */     RobotLog.v("Creating Modern Robotics Analog Optical Distance Sensor - Port: " + physicalPort);
/* 549 */     return new ModernRoboticsAnalogOpticalDistanceSensor(analogInputController, physicalPort);
/*     */   }
/*     */   
/*     */   public TouchSensor createNxtTouchSensor(LegacyModule legacyModule, int physicalPort)
/*     */   {
/* 554 */     RobotLog.v("Creating HiTechnic NXT Touch Sensor - Port: " + physicalPort);
/* 555 */     return new HiTechnicNxtTouchSensor(promote(legacyModule), physicalPort);
/*     */   }
/*     */   
/*     */   public TouchSensorMultiplexer createNxtTouchSensorMultiplexer(LegacyModule legacyModule, int port)
/*     */   {
/* 560 */     RobotLog.v("Creating HiTechnic NXT Touch Sensor Multiplexer - Port: " + port);
/* 561 */     return new HiTechnicNxtTouchSensorMultiplexer(promote(legacyModule), port);
/*     */   }
/*     */   
/*     */   public AnalogInput createAnalogInputDevice(AnalogInputController controller, int channel)
/*     */   {
/* 566 */     RobotLog.v("Creating Analog Input Device - Port: " + channel);
/* 567 */     return new AnalogInput(controller, channel);
/*     */   }
/*     */   
/*     */   public AnalogOutput createAnalogOutputDevice(AnalogOutputController controller, int channel)
/*     */   {
/* 572 */     RobotLog.v("Creating Analog Output Device - Port: " + channel);
/* 573 */     return new AnalogOutput(controller, channel);
/*     */   }
/*     */   
/*     */   public DigitalChannel createDigitalChannelDevice(DigitalChannelController controller, int channel)
/*     */   {
/* 578 */     RobotLog.v("Creating Digital Channel Device - Port: " + channel);
/* 579 */     return new DigitalChannel(controller, channel);
/*     */   }
/*     */   
/*     */   public PWMOutput createPwmOutputDevice(PWMOutputController controller, int channel)
/*     */   {
/* 584 */     RobotLog.v("Creating PWM Output Device - Port: " + channel);
/* 585 */     return new PWMOutputImpl(controller, channel);
/*     */   }
/*     */   
/*     */   public PWMOutput createPwmOutputDeviceEx(PWMOutputController controller, int channel)
/*     */   {
/* 590 */     RobotLog.v("Creating PWM Output Device - Port: " + channel);
/* 591 */     return new PWMOutputImplEx(controller, channel);
/*     */   }
/*     */   
/*     */   public I2cDevice createI2cDevice(I2cController controller, int channel)
/*     */   {
/* 596 */     RobotLog.v("Creating I2C Device - Port: " + channel);
/* 597 */     return new I2cDeviceImpl(controller, channel);
/*     */   }
/*     */   
/*     */   public HardwareDevice createUserI2cDevice(I2cController controller, int channel, UserSensorType type)
/*     */   {
/* 602 */     RobotLog.v("Creating user sensor %s - Port: %d", new Object[] { type.getName(), Integer.valueOf(channel) });
/*     */     try {
/* 604 */       return type.createInstance(controller, channel);
/*     */     } catch (InvocationTargetException e) {
/* 606 */       RobotLog.v("Creating user sensor %s failed: ", new Object[] { type.getName() });
/* 607 */       Exception eToLog = (e.getTargetException() != null) && ((e.getTargetException() instanceof Exception)) ? (Exception)e.getTargetException() : e;
/* 608 */       RobotLog.logStacktrace(eToLog); }
/* 609 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public ColorSensor createAdafruitI2cColorSensor(I2cController controller, int channel)
/*     */   {
/* 615 */     RobotLog.v("Creating Adafruit I2C Color Sensor - Port: " + channel);
/* 616 */     return new AdafruitI2cColorSensor(controller, channel);
/*     */   }
/*     */   
/*     */   public ColorSensor createNxtColorSensor(LegacyModule controller, int channel)
/*     */   {
/* 621 */     RobotLog.v("Creating HiTechnic NXT Color Sensor - Port: " + channel);
/* 622 */     return new HiTechnicNxtColorSensor(controller, channel);
/*     */   }
/*     */   
/*     */   public ColorSensor createModernRoboticsI2cColorSensor(I2cController controller, int channel)
/*     */   {
/* 627 */     RobotLog.v("Creating Modern Robotics I2C Color Sensor - Port: " + channel);
/* 628 */     return new ModernRoboticsI2cColorSensor(controller, channel);
/*     */   }
/*     */   
/*     */   public GyroSensor createModernRoboticsI2cGyroSensor(I2cController controller, int channel)
/*     */   {
/* 633 */     RobotLog.v("Creating Modern Robotics I2C Gyro Sensor - Port: " + channel);
/* 634 */     return new ModernRoboticsI2cGyro(controller, channel);
/*     */   }
/*     */   
/*     */   public LED createLED(DigitalChannelController controller, int channel)
/*     */   {
/* 639 */     RobotLog.v("Creating LED - Port: " + channel);
/* 640 */     return new LED(controller, channel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private RobotUsbDevice.FirmwareVersion getModernRoboticsFirmwareVersion(byte[] modernRoboticsDeviceHeader)
/*     */   {
/* 648 */     RobotUsbDevice.FirmwareVersion result = new RobotUsbDevice.FirmwareVersion();
/* 649 */     result.majorVersion = (modernRoboticsDeviceHeader[0] >> 4 & 0xF);
/* 650 */     result.minorVersion = (modernRoboticsDeviceHeader[0] >> 0 & 0xF);
/* 651 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void enableDeviceEmulation()
/*     */   {
/* 658 */     mode = Mode.ENABLE_DEVICE_EMULATION;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void disableDeviceEmulation()
/*     */   {
/* 665 */     mode = Mode.DEFAULT;
/*     */   }
/*     */   
/*     */   private ModernRoboticsUsbLegacyModule promote(LegacyModule module) {
/* 669 */     if (!(module instanceof ModernRoboticsUsbLegacyModule)) {
/* 670 */       throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics LegacyModule");
/*     */     }
/*     */     
/* 673 */     return (ModernRoboticsUsbLegacyModule)module;
/*     */   }
/*     */   
/*     */   private ModernRoboticsUsbDeviceInterfaceModule promote(DeviceInterfaceModule module) {
/* 677 */     if (!(module instanceof ModernRoboticsUsbDeviceInterfaceModule)) {
/* 678 */       throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics Device Interface Module");
/*     */     }
/*     */     
/* 681 */     return (ModernRoboticsUsbDeviceInterfaceModule)module;
/*     */   }
/*     */   
/*     */   private void closeAndThrowOnFailedDeviceTypeCheck(RobotUsbDevice dev, SerialNumber serialNumber) throws RobotCoreException {
/* 685 */     String msg = String.format("%s is returning garbage data on the USB bus", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
/* 686 */     dev.close();
/* 687 */     logAndThrow(msg);
/*     */   }
/*     */   
/*     */   private void logAndThrow(String errMsg) throws RobotCoreException {
/* 691 */     System.err.println(errMsg);
/* 692 */     throw new RobotCoreException(errMsg);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\HardwareDeviceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */