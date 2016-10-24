/*     */ package com.qualcomm.hardware.modernrobotics;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.ArmableUsbDevice;
/*     */ import com.qualcomm.hardware.ArmableUsbDevice.OpenRobotUsbDevice;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier.ARMINGSTATE;
/*     */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import com.qualcomm.robotcore.util.ThreadPool;
/*     */ import java.io.PrintStream;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ModernRoboticsUsbDevice
/*     */   extends ArmableUsbDevice
/*     */   implements ReadWriteRunnable.Callback
/*     */ {
/*     */   public static final int MFG_CODE_MODERN_ROBOTICS = 77;
/*     */   public static final int DEVICE_ID_DC_MOTOR_CONTROLLER = 77;
/*     */   public static final int DEVICE_ID_SERVO_CONTROLLER = 83;
/*     */   public static final int DEVICE_ID_LEGACY_MODULE = 73;
/*     */   public static final int DEVICE_ID_DEVICE_INTERFACE_MODULE = 65;
/*     */   static final int ADDRESS_VERSION_NUMBER = 0;
/*     */   static final int ADDRESS_MANUFACTURER_CODE = 1;
/*     */   static final int ADDRESS_DEVICE_ID = 2;
/*     */   protected ExecutorService readWriteService;
/*     */   protected volatile ReadWriteRunnable readWriteRunnable;
/*     */   protected final CreateReadWriteRunnable createReadWriteRunnable;
/*     */   
/*     */   public ModernRoboticsUsbDevice(Context context, SerialNumber serialNumber, EventLoopManager manager, ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice, CreateReadWriteRunnable createReadWriteRunnable)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/* 108 */     super(context, serialNumber, manager, openRobotUsbDevice);
/*     */     
/* 110 */     this.createReadWriteRunnable = createReadWriteRunnable;
/* 111 */     this.readWriteService = null;
/*     */     
/* 113 */     finishConstruction();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initializeHardware() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RobotUsbDevice getPretendDevice(SerialNumber serialNumber)
/*     */   {
/* 132 */     return new PretendModernRoboticsUsbDevice(serialNumber);
/*     */   }
/*     */   
/*     */   protected void armDevice(RobotUsbDevice device) throws RobotCoreException, InterruptedException
/*     */   {
/* 137 */     synchronized (this.armingLock)
/*     */     {
/*     */ 
/* 140 */       this.robotUsbDevice = device;
/*     */       
/*     */ 
/* 143 */       this.readWriteRunnable = this.createReadWriteRunnable.create(device);
/*     */       
/*     */ 
/* 146 */       if (this.readWriteRunnable != null) {
/* 147 */         RobotLog.v("Starting up %sdevice %s", new Object[] { this.armingState == RobotArmingStateNotifier.ARMINGSTATE.TO_PRETENDING ? "pretend " : "", this.serialNumber.toString() });
/*     */         
/* 149 */         this.readWriteRunnable.setOwner(this);
/*     */         
/* 151 */         this.readWriteRunnable.setCallback(this);
/* 152 */         this.readWriteService = ThreadPool.newSingleThreadExecutor();
/* 153 */         this.readWriteRunnable.executeUsing(this.readWriteService);
/*     */         
/*     */ 
/* 156 */         this.readWriteRunnable.blockUntilReady();
/*     */         
/* 158 */         this.eventLoopManager.registerSyncdDevice(this.readWriteRunnable);
/*     */         
/* 160 */         this.readWriteRunnable.setAcceptingWrites(true);
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
/*     */   protected void disarmDevice()
/*     */     throws InterruptedException
/*     */   {
/* 174 */     synchronized (this.armingLock)
/*     */     {
/*     */ 
/* 177 */       if (this.readWriteService != null) {
/* 178 */         this.readWriteService.shutdown();
/*     */       }
/*     */       
/* 181 */       if (this.readWriteRunnable != null)
/*     */       {
/* 183 */         this.readWriteRunnable.setAcceptingWrites(false);
/*     */         
/*     */ 
/* 186 */         this.readWriteRunnable.drainPendingWrites();
/*     */         
/*     */ 
/* 189 */         this.eventLoopManager.unregisterSyncdDevice(this.readWriteRunnable);
/* 190 */         this.readWriteRunnable.close();
/* 191 */         this.readWriteRunnable = null;
/*     */       }
/*     */       
/*     */ 
/* 195 */       if (this.readWriteService != null) {
/* 196 */         String serviceName = "ReadWriteRunnable for Modern Robotics USB Device";
/* 197 */         ThreadPool.awaitTerminationOrExitApplication(this.readWriteService, 30L, TimeUnit.SECONDS, serviceName, "internal error");
/* 198 */         this.readWriteService = null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 204 */       if (this.robotUsbDevice != null) {
/* 205 */         this.robotUsbDevice.close();
/* 206 */         this.robotUsbDevice = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReadWriteRunnable getReadWriteRunnable()
/*     */   {
/* 216 */     return this.readWriteRunnable;
/*     */   }
/*     */   
/*     */   public ArmableUsbDevice.OpenRobotUsbDevice getOpenRobotUsbDevice() {
/* 220 */     return this.openRobotUsbDevice;
/*     */   }
/*     */   
/*     */   public CreateReadWriteRunnable getCreateReadWriteRunnable() {
/* 224 */     return this.createReadWriteRunnable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getDeviceName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getVersion()
/*     */   {
/* 245 */     return read8(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write8(int address, byte data)
/*     */   {
/* 254 */     write(address, new byte[] { data });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write8(int address, int data)
/*     */   {
/* 264 */     write(address, new byte[] { (byte)data });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write8(int address, double data)
/*     */   {
/* 274 */     write(address, new byte[] { (byte)(int)data });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(int address, byte[] data)
/*     */   {
/* 285 */     ReadWriteRunnable r = this.readWriteRunnable;
/* 286 */     if (r != null) { r.write(address, data);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte readFromWriteCache(int address)
/*     */   {
/* 296 */     return readFromWriteCache(address, 1)[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] readFromWriteCache(int address, int size)
/*     */   {
/* 307 */     ReadWriteRunnable r = this.readWriteRunnable;
/* 308 */     if (r != null) {
/* 309 */       return r.readFromWriteCache(address, size);
/*     */     }
/* 311 */     return new byte[size];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte read8(int address)
/*     */   {
/* 321 */     return read(address, 1)[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] read(int address, int size)
/*     */   {
/* 332 */     ReadWriteRunnable r = this.readWriteRunnable;
/* 333 */     if (r != null) {
/* 334 */       return r.read(address, size);
/*     */     }
/* 336 */     return new byte[size];
/*     */   }
/*     */   
/*     */   public void startupComplete()
/*     */     throws InterruptedException
/*     */   {}
/*     */   
/*     */   public void readComplete()
/*     */     throws InterruptedException
/*     */   {}
/*     */   
/*     */   public void writeComplete()
/*     */     throws InterruptedException
/*     */   {}
/*     */   
/*     */   public void shutdownComplete() throws InterruptedException
/*     */   {}
/*     */   
/*     */   private static void logAndThrow(String errMsg) throws RobotCoreException
/*     */   {
/* 356 */     System.err.println(errMsg);
/* 357 */     throw new RobotCoreException(errMsg);
/*     */   }
/*     */   
/*     */   public static abstract interface CreateReadWriteRunnable
/*     */   {
/*     */     public abstract ReadWriteRunnable create(RobotUsbDevice paramRobotUsbDevice)
/*     */       throws RobotCoreException, InterruptedException;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */