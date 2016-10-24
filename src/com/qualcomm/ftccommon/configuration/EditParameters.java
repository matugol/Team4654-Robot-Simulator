/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.support.annotation.NonNull;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditParameters
/*     */   implements Serializable
/*     */ {
/*  65 */   private boolean isConfigDirty = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  70 */   private DeviceConfiguration configuration = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  75 */   private List<DeviceConfiguration> currentItems = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  80 */   private int initialPortNumber = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   private int maxItemCount = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private boolean growable = false;
/*     */   
/*     */ 
/*     */ 
/*     */   @NonNull
/*  98 */   private ScannedDevices scannedDevices = new ScannedDevices();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   private RobotConfigMap robotConfigMap = new RobotConfigMap();
/* 104 */   private boolean haveRobotConfigMapParameter = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 109 */   private List<RobotConfigFile> extantRobotConfigurations = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 114 */   private ConfigurationType[] configurationTypes = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 119 */   private RobotConfigFile currentCfgFile = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EditParameters(EditActivity editActivity, DeviceConfiguration configuration)
/*     */   {
/* 127 */     this(editActivity);
/* 128 */     this.configuration = configuration;
/*     */   }
/*     */   
/*     */   public EditParameters(EditActivity editActivity, DeviceConfiguration configuration, RobotConfigMap robotConfigMap)
/*     */   {
/* 133 */     this(editActivity);
/* 134 */     this.configuration = configuration;
/* 135 */     this.robotConfigMap = robotConfigMap;
/* 136 */     this.haveRobotConfigMapParameter = true;
/*     */   }
/*     */   
/*     */   public EditParameters(EditActivity editActivity, List<DeviceConfiguration> list)
/*     */   {
/* 141 */     this(editActivity);
/* 142 */     this.currentItems = list;
/*     */   }
/*     */   
/*     */   public EditParameters(EditActivity editActivity, DeviceConfiguration configuration, List<DeviceConfiguration> list)
/*     */   {
/* 147 */     this(editActivity);
/* 148 */     this.configuration = configuration;
/* 149 */     this.currentItems = list;
/*     */   }
/*     */   
/*     */   public EditParameters(EditActivity editActivity, List<DeviceConfiguration> list, int maxItemCount)
/*     */   {
/* 154 */     this(editActivity);
/* 155 */     this.currentItems = list;
/* 156 */     this.maxItemCount = maxItemCount;
/*     */   }
/*     */   
/*     */   public EditParameters(EditActivity editActivity)
/*     */   {
/* 161 */     this.isConfigDirty = editActivity.currentCfgFile.isDirty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EditParameters() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeviceConfiguration getConfiguration()
/*     */   {
/* 174 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public List<DeviceConfiguration> getCurrentItems()
/*     */   {
/* 179 */     return this.currentItems == null ? new LinkedList() : this.currentItems;
/*     */   }
/*     */   
/*     */   public int getMaxItemCount()
/*     */   {
/* 184 */     if (this.currentItems == null) {
/* 185 */       return this.maxItemCount;
/*     */     }
/* 187 */     return Math.max(this.maxItemCount, this.currentItems.size());
/*     */   }
/*     */   
/*     */   public boolean isGrowable()
/*     */   {
/* 192 */     return this.growable;
/*     */   }
/*     */   
/*     */   public void setGrowable(boolean growable)
/*     */   {
/* 197 */     this.growable = growable;
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public ScannedDevices getScannedDevices() {
/* 202 */     return this.scannedDevices;
/*     */   }
/*     */   
/*     */   public void setScannedDevices(@NonNull ScannedDevices devices)
/*     */   {
/* 207 */     this.scannedDevices = devices;
/*     */   }
/*     */   
/*     */   public void setInitialPortNumber(int initialPortNumber)
/*     */   {
/* 212 */     this.initialPortNumber = initialPortNumber;
/*     */   }
/*     */   
/*     */   public int getInitialPortNumber()
/*     */   {
/* 217 */     return this.initialPortNumber;
/*     */   }
/*     */   
/*     */   public RobotConfigMap getRobotConfigMap()
/*     */   {
/* 222 */     return this.robotConfigMap;
/*     */   }
/*     */   
/*     */   public void setRobotConfigMap(RobotConfigMap robotConfigMap)
/*     */   {
/* 227 */     this.robotConfigMap = robotConfigMap;
/* 228 */     this.haveRobotConfigMapParameter = true;
/*     */   }
/*     */   
/*     */   public boolean haveRobotConfigMapParameter()
/*     */   {
/* 233 */     return this.haveRobotConfigMapParameter;
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public List<RobotConfigFile> getExtantRobotConfigurations() {
/* 238 */     return this.extantRobotConfigurations;
/*     */   }
/*     */   
/*     */   public void setExtantRobotConfigurations(List<RobotConfigFile> configurations)
/*     */   {
/* 243 */     this.extantRobotConfigurations = configurations;
/*     */   }
/*     */   
/*     */   public ConfigurationType[] getConfigurationTypes()
/*     */   {
/* 248 */     return this.configurationTypes;
/*     */   }
/*     */   
/*     */   public void setConfigurationTypes(ConfigurationType[] configurationTypes)
/*     */   {
/* 253 */     this.configurationTypes = configurationTypes;
/*     */   }
/*     */   
/*     */   public RobotConfigFile getCurrentCfgFile()
/*     */   {
/* 258 */     return this.currentCfgFile;
/*     */   }
/*     */   
/*     */   public void setCurrentCfgFile(RobotConfigFile currentCfgFile)
/*     */   {
/* 263 */     this.currentCfgFile = currentCfgFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putIntent(Intent intent)
/*     */   {
/* 272 */     intent.putExtras(toBundle());
/*     */   }
/*     */   
/*     */   public Bundle toBundle()
/*     */   {
/* 277 */     Bundle result = new Bundle();
/*     */     
/* 279 */     if (this.configuration != null)
/*     */     {
/* 281 */       result.putSerializable("configuration", this.configuration);
/*     */     }
/* 283 */     if ((this.scannedDevices != null) && (this.scannedDevices.size() > 0))
/*     */     {
/* 285 */       result.putSerializable("scannedDevices", this.scannedDevices);
/*     */     }
/* 287 */     if ((this.robotConfigMap != null) && (this.robotConfigMap.size() > 0))
/*     */     {
/* 289 */       result.putSerializable("robotConfigMap", this.robotConfigMap);
/*     */     }
/* 291 */     if ((this.extantRobotConfigurations != null) && (this.extantRobotConfigurations.size() > 0))
/*     */     {
/* 293 */       result.putString("extantRobotConfigurations", RobotConfigFileManager.serializeXMLConfigList(this.extantRobotConfigurations));
/*     */     }
/* 295 */     if (this.configurationTypes != null)
/*     */     {
/* 297 */       result.putSerializable("configurationTypes", this.configurationTypes);
/*     */     }
/* 299 */     if (this.currentCfgFile != null)
/*     */     {
/* 301 */       result.putString("currentCfgFile", RobotConfigFileManager.serializeConfig(this.currentCfgFile));
/*     */     }
/* 303 */     result.putBoolean("haveRobotConfigMap", this.haveRobotConfigMapParameter);
/* 304 */     result.putInt("initialPortNumber", this.initialPortNumber);
/* 305 */     result.putInt("maxItemCount", this.maxItemCount);
/* 306 */     result.putBoolean("growable", this.growable);
/* 307 */     result.putBoolean("isConfigDirty", this.isConfigDirty);
/* 308 */     if (this.currentItems != null)
/*     */     {
/* 310 */       for (int i = 0; i < this.currentItems.size(); i++)
/*     */       {
/* 312 */         result.putSerializable(String.valueOf(i), (Serializable)this.currentItems.get(i));
/*     */       }
/*     */     }
/* 315 */     return result;
/*     */   }
/*     */   
/*     */   public static EditParameters fromIntent(EditActivity editActivity, Intent intent)
/*     */   {
/* 320 */     return fromBundle(editActivity, intent.getExtras());
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public static EditParameters fromBundle(EditActivity editActivity, Bundle bundle) {
/* 325 */     EditParameters result = new EditParameters();
/* 326 */     if (bundle == null) { return result;
/*     */     }
/* 328 */     for (String key : bundle.keySet())
/*     */     {
/* 330 */       if (key.equals("configuration"))
/*     */       {
/* 332 */         result.configuration = ((DeviceConfiguration)bundle.getSerializable(key));
/*     */       }
/* 334 */       else if (key.equals("scannedDevices"))
/*     */       {
/* 336 */         result.scannedDevices = new ScannedDevices((HashMap)bundle.getSerializable(key));
/*     */       }
/* 338 */       else if (key.equals("robotConfigMap"))
/*     */       {
/* 340 */         result.robotConfigMap = ((RobotConfigMap)bundle.getSerializable(key));
/*     */       }
/* 342 */       else if (key.equals("haveRobotConfigMap"))
/*     */       {
/* 344 */         result.haveRobotConfigMapParameter = bundle.getBoolean(key);
/*     */       }
/* 346 */       else if (key.equals("extantRobotConfigurations"))
/*     */       {
/* 348 */         result.extantRobotConfigurations = RobotConfigFileManager.deserializeXMLConfigList(bundle.getString(key));
/*     */       }
/* 350 */       else if (key.equals("configurationTypes"))
/*     */       {
/* 352 */         Object[] objects = (Object[])bundle.getSerializable(key);
/* 353 */         result.configurationTypes = new ConfigurationType[objects.length];
/* 354 */         for (int i = 0; i < objects.length; i++)
/*     */         {
/* 356 */           result.configurationTypes[i] = ((ConfigurationType)objects[i]);
/*     */         }
/*     */       }
/* 359 */       else if (key.equals("currentCfgFile"))
/*     */       {
/* 361 */         result.currentCfgFile = RobotConfigFileManager.deserializeConfig(bundle.getString(key));
/*     */       }
/* 363 */       else if (key.equals("initialPortNumber"))
/*     */       {
/* 365 */         result.initialPortNumber = bundle.getInt(key);
/*     */       }
/* 367 */       else if (key.equals("maxItemCount"))
/*     */       {
/* 369 */         result.maxItemCount = bundle.getInt(key);
/*     */       }
/* 371 */       else if (key.equals("growable"))
/*     */       {
/* 373 */         result.growable = bundle.getBoolean(key);
/*     */       }
/* 375 */       else if (key.equals("isConfigDirty"))
/*     */       {
/* 377 */         result.isConfigDirty = bundle.getBoolean(key);
/*     */       }
/*     */       else
/*     */       {
/*     */         try
/*     */         {
/* 383 */           int i = Integer.parseInt(key);
/* 384 */           DeviceConfiguration dev = (DeviceConfiguration)bundle.getSerializable(key);
/* 385 */           if (result.currentItems == null)
/*     */           {
/* 387 */             result.currentItems = new ArrayList();
/*     */           }
/* 389 */           result.currentItems.add(i, dev);
/*     */         }
/*     */         catch (NumberFormatException e) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 399 */     if (result.isConfigDirty)
/*     */     {
/* 401 */       editActivity.currentCfgFile.markDirty();
/*     */     }
/*     */     
/* 404 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */