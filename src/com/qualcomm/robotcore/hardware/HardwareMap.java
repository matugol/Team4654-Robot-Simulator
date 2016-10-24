/*     */ package com.qualcomm.robotcore.hardware;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HardwareMap
/*     */   implements Iterable<HardwareDevice>
/*     */ {
/*  63 */   public DeviceMapping<DcMotorController> dcMotorController = new DeviceMapping();
/*  64 */   public DeviceMapping<DcMotor> dcMotor = new DeviceMapping();
/*     */   
/*  66 */   public DeviceMapping<ServoController> servoController = new DeviceMapping();
/*  67 */   public DeviceMapping<Servo> servo = new DeviceMapping();
/*  68 */   public DeviceMapping<CRServo> crservo = new DeviceMapping();
/*     */   
/*  70 */   public DeviceMapping<LegacyModule> legacyModule = new DeviceMapping();
/*  71 */   public DeviceMapping<TouchSensorMultiplexer> touchSensorMultiplexer = new DeviceMapping();
/*     */   
/*  73 */   public DeviceMapping<DeviceInterfaceModule> deviceInterfaceModule = new DeviceMapping();
/*  74 */   public DeviceMapping<AnalogInput> analogInput = new DeviceMapping();
/*  75 */   public DeviceMapping<DigitalChannel> digitalChannel = new DeviceMapping();
/*  76 */   public DeviceMapping<OpticalDistanceSensor> opticalDistanceSensor = new DeviceMapping();
/*  77 */   public DeviceMapping<TouchSensor> touchSensor = new DeviceMapping();
/*  78 */   public DeviceMapping<PWMOutput> pwmOutput = new DeviceMapping();
/*  79 */   public DeviceMapping<I2cDevice> i2cDevice = new DeviceMapping();
/*  80 */   public DeviceMapping<I2cDeviceSynch> i2cDeviceSynch = new DeviceMapping();
/*  81 */   public DeviceMapping<AnalogOutput> analogOutput = new DeviceMapping();
/*  82 */   public DeviceMapping<ColorSensor> colorSensor = new DeviceMapping();
/*  83 */   public DeviceMapping<LED> led = new DeviceMapping();
/*     */   
/*  85 */   public DeviceMapping<AccelerationSensor> accelerationSensor = new DeviceMapping();
/*  86 */   public DeviceMapping<CompassSensor> compassSensor = new DeviceMapping();
/*  87 */   public DeviceMapping<GyroSensor> gyroSensor = new DeviceMapping();
/*  88 */   public DeviceMapping<IrSeekerSensor> irSeekerSensor = new DeviceMapping();
/*  89 */   public DeviceMapping<LightSensor> lightSensor = new DeviceMapping();
/*  90 */   public DeviceMapping<UltrasonicSensor> ultrasonicSensor = new DeviceMapping();
/*  91 */   public DeviceMapping<VoltageSensor> voltageSensor = new DeviceMapping();
/*     */   
/*  93 */   protected Map<String, List<HardwareDevice>> allDevicesMap = new ConcurrentHashMap();
/*  94 */   protected List<HardwareDevice> allDevicesList = null;
/*     */   
/*     */   public final Context appContext;
/*     */   
/*     */   private static final String LOG_FORMAT = "%-50s %-30s %s";
/*     */   
/*     */ 
/*     */   public HardwareMap(Context appContext)
/*     */   {
/* 103 */     this.appContext = appContext;
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
/*     */   public <T> T get(Class<? extends T> classOrInterface, String deviceName)
/*     */   {
/* 127 */     List<HardwareDevice> list = (List)this.allDevicesMap.get(deviceName);
/* 128 */     if (list != null) {
/* 129 */       for (HardwareDevice device : list) {
/* 130 */         if (classOrInterface.isInstance(device)) {
/* 131 */           return (T)classOrInterface.cast(device);
/*     */         }
/*     */       }
/*     */     }
/* 135 */     throw new IllegalArgumentException(String.format("Unable to find a hardware device with name \"%s\" and type %s", new Object[] { deviceName, classOrInterface.getSimpleName() }));
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
/*     */   public HardwareDevice get(String deviceName)
/*     */   {
/* 155 */     List<HardwareDevice> list = (List)this.allDevicesMap.get(deviceName);
/* 156 */     if (list != null) {
/* 157 */       Iterator i$ = list.iterator(); if (i$.hasNext()) { HardwareDevice device = (HardwareDevice)i$.next();
/* 158 */         return device;
/*     */       }
/*     */     }
/* 161 */     throw new IllegalArgumentException(String.format("Unable to find a hardware device with name \"%s\"", new Object[] { deviceName }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> List<T> getAll(Class<? extends T> classOrInterface)
/*     */   {
/* 171 */     List<T> result = new LinkedList();
/* 172 */     for (HardwareDevice device : this) {
/* 173 */       if (classOrInterface.isInstance(device)) {
/* 174 */         result.add(classOrInterface.cast(device));
/*     */       }
/*     */     }
/* 177 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void put(String deviceName, HardwareDevice device)
/*     */   {
/* 186 */     List<HardwareDevice> list = (List)this.allDevicesMap.get(deviceName);
/* 187 */     if (list == null) {
/* 188 */       list = new ArrayList(1);
/* 189 */       this.allDevicesMap.put(deviceName, list);
/*     */     }
/* 191 */     if (!list.contains(device)) {
/* 192 */       this.allDevicesList = null;
/* 193 */       list.add(device);
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
/*     */   public boolean remove(String deviceName, HardwareDevice device)
/*     */   {
/* 210 */     List<HardwareDevice> list = (List)this.allDevicesMap.get(deviceName);
/* 211 */     if (list != null) {
/* 212 */       list.remove(device);
/* 213 */       if (list.isEmpty()) {
/* 214 */         this.allDevicesMap.remove(deviceName);
/*     */       }
/* 216 */       this.allDevicesList = null;
/* 217 */       return true;
/*     */     }
/* 219 */     return false;
/*     */   }
/*     */   
/*     */   private void buildAllDevicesList() {
/* 223 */     if (this.allDevicesList == null) {
/* 224 */       Set<HardwareDevice> set = new HashSet();
/* 225 */       for (String key : this.allDevicesMap.keySet()) {
/* 226 */         set.addAll((Collection)this.allDevicesMap.get(key));
/*     */       }
/* 228 */       this.allDevicesList = new ArrayList(set);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 238 */     buildAllDevicesList();
/* 239 */     return this.allDevicesList.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<HardwareDevice> iterator()
/*     */   {
/* 249 */     buildAllDevicesList();
/* 250 */     return this.allDevicesList.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class DeviceMapping<DEVICE_TYPE extends HardwareDevice>
/*     */     implements Iterable<DEVICE_TYPE>
/*     */   {
/*     */     public DeviceMapping() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 266 */     private Map<String, DEVICE_TYPE> map = new HashMap();
/*     */     
/*     */     public DEVICE_TYPE get(String deviceName) {
/* 269 */       DEVICE_TYPE device = (HardwareDevice)this.map.get(deviceName);
/* 270 */       if (device == null) {
/* 271 */         String msg = String.format("Unable to find a hardware device with the name \"%s\"", new Object[] { deviceName });
/* 272 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 274 */       return device;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void put(String deviceName, DEVICE_TYPE device)
/*     */     {
/* 290 */       remove(deviceName);
/*     */       
/*     */ 
/* 293 */       HardwareMap.this.put(deviceName, device);
/*     */       
/*     */ 
/* 296 */       this.map.put(deviceName, device);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean remove(String deviceName)
/*     */     {
/* 309 */       HardwareDevice device = (HardwareDevice)this.map.remove(deviceName);
/* 310 */       if (device != null) {
/* 311 */         HardwareMap.this.remove(deviceName, device);
/* 312 */         return true;
/*     */       }
/* 314 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Iterator<DEVICE_TYPE> iterator()
/*     */     {
/* 322 */       return this.map.values().iterator();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Set<Map.Entry<String, DEVICE_TYPE>> entrySet()
/*     */     {
/* 330 */       return this.map.entrySet();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int size()
/*     */     {
/* 338 */       return this.map.size();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logDevices()
/*     */   {
/* 349 */     RobotLog.i("========= Device Information ===================================================");
/* 350 */     RobotLog.i(String.format("%-50s %-30s %s", new Object[] { "Type", "Name", "Connection" }));
/*     */     
/* 352 */     for (Iterator i$ = this.allDevicesMap.entrySet().iterator(); i$.hasNext();) { entry = (Map.Entry)i$.next();
/* 353 */       List<HardwareDevice> list = (List)entry.getValue();
/* 354 */       for (HardwareDevice d : list) {
/* 355 */         String conn = d.getConnectionInfo();
/* 356 */         String name = (String)entry.getKey();
/* 357 */         String type = d.getDeviceName();
/* 358 */         RobotLog.i(String.format("%-50s %-30s %s", new Object[] { type, name, conn }));
/*     */       }
/*     */     }
/*     */     Map.Entry<String, List<HardwareDevice>> entry;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\HardwareMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */