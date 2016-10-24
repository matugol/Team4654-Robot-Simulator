/*     */ package com.qualcomm.robotcore.hardware.configuration;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ControllerConfiguration
/*     */   extends DeviceConfiguration
/*     */   implements Serializable
/*     */ {
/*     */   private List<DeviceConfiguration> devices;
/*     */   @NonNull
/*     */   private SerialNumber serialNumber;
/*  64 */   private boolean knownToBeAttached = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ControllerConfiguration(String name, @NonNull SerialNumber serialNumber, ConfigurationType type)
/*     */   {
/*  71 */     this(name, new ArrayList(), serialNumber, type);
/*     */   }
/*     */   
/*     */   public ControllerConfiguration(String name, List<DeviceConfiguration> devices, @NonNull SerialNumber serialNumber, ConfigurationType type) {
/*  75 */     super(type);
/*  76 */     super.setName(name);
/*  77 */     this.devices = devices;
/*  78 */     this.serialNumber = serialNumber;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static ControllerConfiguration forType(String name, @NonNull SerialNumber serialNumber, ConfigurationType type) {
/*  83 */     if (type == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) return new DeviceInterfaceModuleConfiguration(name, serialNumber);
/*  84 */     if (type == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) return new LegacyModuleControllerConfiguration(name, new LinkedList(), serialNumber);
/*  85 */     if (type == BuiltInConfigurationType.MATRIX_CONTROLLER) return new MatrixControllerConfiguration(name, new LinkedList(), new LinkedList(), serialNumber);
/*  86 */     if (type == BuiltInConfigurationType.MOTOR_CONTROLLER) return new MotorControllerConfiguration(name, new LinkedList(), serialNumber);
/*  87 */     if (type == BuiltInConfigurationType.SERVO_CONTROLLER) { return new ServoControllerConfiguration(name, new LinkedList(), serialNumber);
/*     */     }
/*  89 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<DeviceConfiguration> getDevices()
/*     */   {
/*  97 */     return this.devices;
/*     */   }
/*     */   
/*     */   public ConfigurationType getType() {
/* 101 */     return super.getType();
/*     */   }
/*     */   
/*     */   public void setSerialNumber(@NonNull SerialNumber serialNumber) {
/* 105 */     this.serialNumber = serialNumber;
/*     */   }
/*     */   
/*     */   @NonNull
/* 109 */   public SerialNumber getSerialNumber() { return this.serialNumber; }
/*     */   
/*     */   public boolean isKnownToBeAttached()
/*     */   {
/* 113 */     return this.knownToBeAttached;
/*     */   }
/*     */   
/*     */   public void setKnownToBeAttached(boolean knownToBeAttached) {
/* 117 */     this.knownToBeAttached = knownToBeAttached;
/*     */   }
/*     */   
/*     */   public void setDevices(List<DeviceConfiguration> devices) {
/* 121 */     this.devices = devices;
/*     */   }
/*     */   
/*     */   public DeviceManager.DeviceType toUSBDeviceType() {
/* 125 */     return getType().toUSBDeviceType();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\ControllerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */