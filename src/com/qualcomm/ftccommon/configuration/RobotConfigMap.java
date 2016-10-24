/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RobotConfigMap
/*     */   implements Serializable
/*     */ {
/*  64 */   Map<SerialNumber, ControllerConfiguration> map = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RobotConfigMap(Collection<ControllerConfiguration> controllerConfigurations)
/*     */   {
/*  72 */     for (ControllerConfiguration controllerConfiguration : controllerConfigurations)
/*     */     {
/*  74 */       put(controllerConfiguration.getSerialNumber(), controllerConfiguration);
/*     */     }
/*     */   }
/*     */   
/*     */   public RobotConfigMap(Map<SerialNumber, ControllerConfiguration> map)
/*     */   {
/*  80 */     this.map = new HashMap(map);
/*     */   }
/*     */   
/*     */   public RobotConfigMap(RobotConfigMap him)
/*     */   {
/*  85 */     this(him.map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RobotConfigMap() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(SerialNumber serialNumber)
/*     */   {
/*  99 */     return this.map.containsKey(serialNumber);
/*     */   }
/*     */   
/*     */   public ControllerConfiguration get(SerialNumber serialNumber)
/*     */   {
/* 104 */     return (ControllerConfiguration)this.map.get(serialNumber);
/*     */   }
/*     */   
/*     */   public void put(SerialNumber serialNumber, ControllerConfiguration controllerConfiguration)
/*     */   {
/* 109 */     this.map.put(serialNumber, controllerConfiguration);
/*     */   }
/*     */   
/*     */   public boolean remove(SerialNumber serialNumber)
/*     */   {
/* 114 */     return this.map.remove(serialNumber) != null;
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 119 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public Collection<SerialNumber> serialNumbers()
/*     */   {
/* 124 */     return this.map.keySet();
/*     */   }
/*     */   
/*     */   public Collection<ControllerConfiguration> controllerConfigurations()
/*     */   {
/* 129 */     return this.map.values();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeToLog(String tag, String message)
/*     */   {
/* 139 */     RobotLog.vv(tag, "robotConfigMap: %s", new Object[] { message });
/* 140 */     for (ControllerConfiguration controllerConfiguration : controllerConfigurations())
/*     */     {
/* 142 */       RobotLog.vv(tag, "   serial=%s id=0x%08x name='%s' ", new Object[] { controllerConfiguration.getSerialNumber().toString(), Integer.valueOf(controllerConfiguration.hashCode()), controllerConfiguration.getName() });
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToLog(String tag, String message, ControllerConfiguration controllerConfiguration)
/*     */   {
/* 148 */     writeToLog(tag, message);
/* 149 */     RobotLog.vv(tag, "  :serial=%s id=0x%08x name='%s' ", new Object[] { controllerConfiguration.getSerialNumber().toString(), Integer.valueOf(controllerConfiguration.hashCode()), controllerConfiguration.getName() });
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
/*     */   boolean allControllersAreBound()
/*     */   {
/* 162 */     for (ControllerConfiguration controllerConfiguration : controllerConfigurations())
/*     */     {
/* 164 */       if (controllerConfiguration.getSerialNumber().isFake())
/*     */       {
/* 166 */         return false;
/*     */       }
/*     */     }
/* 169 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void bindUnboundControllers(ScannedDevices scannedDevices)
/*     */   {
/* 179 */     ScannedDevices extraDevices = new ScannedDevices(scannedDevices);
/* 180 */     for (ControllerConfiguration controllerConfiguration : controllerConfigurations())
/*     */     {
/* 182 */       extraDevices.remove(controllerConfiguration.getSerialNumber());
/*     */     }
/*     */     
/*     */ 
/* 186 */     Map<ConfigurationType, List<SerialNumber>> extraByType = new HashMap();
/* 187 */     for (Map.Entry<SerialNumber, DeviceManager.DeviceType> pair : extraDevices.entrySet())
/*     */     {
/* 189 */       ConfigurationType configurationType = BuiltInConfigurationType.fromUSBDeviceType((DeviceManager.DeviceType)pair.getValue());
/* 190 */       if (configurationType != BuiltInConfigurationType.UNKNOWN)
/*     */       {
/* 192 */         List<SerialNumber> list = (List)extraByType.get(configurationType);
/* 193 */         if (list == null)
/*     */         {
/* 195 */           list = new LinkedList();
/* 196 */           extraByType.put(configurationType, list);
/*     */         }
/* 198 */         list.add(pair.getKey());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 203 */     for (ControllerConfiguration controllerConfiguration : controllerConfigurations())
/*     */     {
/* 205 */       if (controllerConfiguration.getSerialNumber().isFake())
/*     */       {
/* 207 */         List<SerialNumber> list = (List)extraByType.get(controllerConfiguration.getType());
/* 208 */         if ((list != null) && (!list.isEmpty()))
/*     */         {
/*     */ 
/* 211 */           SerialNumber newSerialNumber = (SerialNumber)list.remove(0);
/* 212 */           controllerConfiguration.setSerialNumber(newSerialNumber);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 218 */     Collection<ControllerConfiguration> controllers = new ArrayList(controllerConfigurations());
/* 219 */     this.map.clear();
/* 220 */     for (ControllerConfiguration controllerConfiguration : controllers)
/*     */     {
/* 222 */       put(controllerConfiguration.getSerialNumber(), controllerConfiguration);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSerialNumber(ControllerConfiguration controllerConfiguration, SerialNumber serialNumber)
/*     */   {
/* 233 */     remove(controllerConfiguration.getSerialNumber());
/* 234 */     controllerConfiguration.setSerialNumber(serialNumber);
/* 235 */     put(serialNumber, controllerConfiguration);
/*     */   }
/*     */   
/*     */ 
/*     */   public void swapSerialNumbers(ControllerConfiguration a, ControllerConfiguration b)
/*     */   {
/* 241 */     SerialNumber aSerialNumber = a.getSerialNumber();
/* 242 */     a.setSerialNumber(b.getSerialNumber());
/* 243 */     b.setSerialNumber(aSerialNumber);
/*     */     
/* 245 */     put(a.getSerialNumber(), a);
/* 246 */     put(b.getSerialNumber(), b);
/*     */     
/* 248 */     boolean knownToBeAttached = a.isKnownToBeAttached();
/* 249 */     a.setKnownToBeAttached(b.isKnownToBeAttached());
/* 250 */     b.setKnownToBeAttached(knownToBeAttached);
/*     */   }
/*     */   
/*     */   public boolean isSwappable(ControllerConfiguration target, ScannedDevices scannedDevices, Context context)
/*     */   {
/* 255 */     return !getEligibleSwapTargets(target, scannedDevices, context).isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ControllerConfiguration> getEligibleSwapTargets(ControllerConfiguration target, ScannedDevices scannedDevices, Context context)
/*     */   {
/* 267 */     List<ControllerConfiguration> result = new LinkedList();
/*     */     
/*     */ 
/* 270 */     ConfigurationType type = target.getType();
/* 271 */     if ((type != BuiltInConfigurationType.MOTOR_CONTROLLER) && (type != BuiltInConfigurationType.SERVO_CONTROLLER) && (type != BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) && (type != BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER))
/*     */     {
/*     */ 
/*     */ 
/* 275 */       return result;
/*     */     }
/* 277 */     if (target.getSerialNumber().isFake())
/*     */     {
/* 279 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 283 */     for (ControllerConfiguration other : controllerConfigurations())
/*     */     {
/* 285 */       SerialNumber serialNumber = other.getSerialNumber();
/*     */       
/* 287 */       if ((!serialNumber.isFake()) && 
/* 288 */         (!serialNumber.equals(target.getSerialNumber())) && 
/* 289 */         (!containsSerialNumber(result, serialNumber))) {
/* 290 */         if (other.getType() == target.getType())
/*     */         {
/* 292 */           result.add(other);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 297 */     for (Map.Entry<SerialNumber, DeviceManager.DeviceType> entry : scannedDevices.entrySet())
/*     */     {
/* 299 */       SerialNumber serialNumber = (SerialNumber)entry.getKey();
/*     */       
/* 301 */       if ((!serialNumber.isFake()) && 
/* 302 */         (!serialNumber.equals(target.getSerialNumber())) && 
/* 303 */         (!containsSerialNumber(result, serialNumber))) {
/* 304 */         if (entry.getValue() == target.toUSBDeviceType())
/*     */         {
/* 306 */           String name = generateName(context, target.getType(), result);
/* 307 */           ControllerConfiguration controllerConfiguration = ControllerConfiguration.forType(name, (SerialNumber)entry.getKey(), target.getType());
/* 308 */           controllerConfiguration.setKnownToBeAttached(scannedDevices.containsKey(controllerConfiguration.getSerialNumber()));
/* 309 */           result.add(controllerConfiguration);
/*     */         }
/*     */       }
/*     */     }
/* 313 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String generateName(Context context, ConfigurationType type, List<ControllerConfiguration> resultSoFar)
/*     */   {
/* 322 */     for (int i = 0;; i++)
/*     */     {
/* 324 */       String name = String.format("%s %d", new Object[] { type.getDisplayName(context), Integer.valueOf(i) });
/* 325 */       if (!nameExists(name, resultSoFar))
/*     */       {
/* 327 */         return name;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean nameExists(String name, List<ControllerConfiguration> resultSoFar) {
/* 333 */     for (ControllerConfiguration controllerConfiguration : resultSoFar)
/*     */     {
/* 335 */       if (controllerConfiguration.getName().equalsIgnoreCase(name)) return true;
/*     */     }
/* 337 */     for (ControllerConfiguration controllerConfiguration : controllerConfigurations())
/*     */     {
/* 339 */       if (controllerConfiguration.getName().equalsIgnoreCase(name)) return true;
/*     */     }
/* 341 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean containsSerialNumber(List<ControllerConfiguration> list, SerialNumber serialNumber)
/*     */   {
/* 346 */     for (ControllerConfiguration controllerConfiguration : list)
/*     */     {
/* 348 */       if (controllerConfiguration.getSerialNumber().equals(serialNumber))
/*     */       {
/* 350 */         return true;
/*     */       }
/*     */     }
/* 353 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\RobotConfigMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */