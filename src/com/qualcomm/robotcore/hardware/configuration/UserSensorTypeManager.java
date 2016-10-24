/*     */ package com.qualcomm.robotcore.hardware.configuration;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.util.ClassFilter;
/*     */ import com.qualcomm.robotcore.util.ClassUtil;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserSensorTypeManager
/*     */   implements ClassFilter
/*     */ {
/*     */   public static final String TAG = "UserSensorTypeManager";
/*  73 */   protected static UserSensorTypeManager theInstance = new UserSensorTypeManager();
/*     */   protected Context context;
/*     */   protected Map<String, UserSensorType> mapTagToType;
/*     */   
/*  77 */   public static UserSensorTypeManager getInstance() { return theInstance; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Set<String> existingConfigurationTypeNames;
/*     */   
/*     */ 
/*     */ 
/*     */   protected Set<String> existingXmlTags;
/*     */   
/*     */ 
/*     */   public UserSensorTypeManager()
/*     */   {
/*  91 */     this.context = AppUtil.getInstance().getApplication();
/*  92 */     this.mapTagToType = new HashMap();
/*  93 */     this.existingConfigurationTypeNames = new HashSet();
/*  94 */     this.existingXmlTags = new HashSet();
/*     */     
/*  96 */     for (BuiltInConfigurationType type : BuiltInConfigurationType.values())
/*     */     {
/*  98 */       this.existingConfigurationTypeNames.add(type.getDisplayName(this.context));
/*  99 */       this.existingXmlTags.add(type.getXmlTag());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConfigurationType typeFromTag(String xmlTag)
/*     */   {
/* 109 */     ConfigurationType result = BuiltInConfigurationType.fromXmlTag(xmlTag);
/* 110 */     if (result == BuiltInConfigurationType.UNKNOWN)
/*     */     {
/* 112 */       result = userTypeFromTag(xmlTag);
/* 113 */       if (result == null)
/*     */       {
/* 115 */         result = BuiltInConfigurationType.UNKNOWN;
/*     */       }
/*     */     }
/* 118 */     return result;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public UserSensorType userTypeFromTag(String xmlTag) {
/* 123 */     return (UserSensorType)this.mapTagToType.get(xmlTag);
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public Collection<UserSensorType> allUserTypes(UserSensorType.Flavor flavor) {
/* 128 */     List<UserSensorType> result = new LinkedList();
/* 129 */     for (UserSensorType userSensorType : this.mapTagToType.values())
/*     */     {
/* 131 */       if (userSensorType.flavor == flavor)
/*     */       {
/* 133 */         result.add(userSensorType);
/*     */       }
/*     */     }
/* 136 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Gson newGson()
/*     */   {
/* 145 */     return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
/*     */   }
/*     */   
/*     */   public String serializeUserSensorTypes()
/*     */   {
/* 150 */     return newGson().toJson(this.mapTagToType.values());
/*     */   }
/*     */   
/*     */ 
/*     */   public void deserializeUserSensorTypes(String serialization)
/*     */   {
/* 156 */     for (UserSensorType sensorType : this.mapTagToType.values())
/*     */     {
/* 158 */       this.existingConfigurationTypeNames.remove(sensorType.getName());
/* 159 */       this.existingXmlTags.remove(sensorType.getXmlTag());
/*     */     }
/* 161 */     this.mapTagToType.clear();
/*     */     
/* 163 */     UserSensorType[] newTypes = (UserSensorType[])newGson().fromJson(serialization, UserSensorType[].class);
/* 164 */     for (UserSensorType sensorType : newTypes)
/*     */     {
/* 166 */       this.mapTagToType.put(sensorType.getXmlTag(), sensorType);
/* 167 */       this.existingConfigurationTypeNames.add(sensorType.getName());
/* 168 */       this.existingXmlTags.add(sensorType.getXmlTag());
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendUserSensorTypes()
/*     */   {
/* 174 */     String userSensorTypes = serializeUserSensorTypes();
/* 175 */     NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_REQUEST_USER_SENSOR_LIST_RESP", userSensorTypes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void filter(Class clazz)
/*     */   {
/* 185 */     UserSensorType sensorType = isUserSensor(clazz);
/* 186 */     if (sensorType != null)
/*     */     {
/*     */ 
/* 189 */       if (!checkSensorClassConstraints(sensorType)) {
/* 190 */         return;
/*     */       }
/* 192 */       this.mapTagToType.put(sensorType.getXmlTag(), sensorType);
/* 193 */       this.existingConfigurationTypeNames.add(sensorType.getName());
/* 194 */       this.existingXmlTags.add(sensorType.getXmlTag());
/*     */     }
/*     */   }
/*     */   
/*     */   UserSensorType isUserSensor(Class clazz)
/*     */   {
/* 200 */     if (clazz.isAnnotationPresent(I2cSensor.class))
/*     */     {
/* 202 */       I2cSensor i2cSensor = (I2cSensor)clazz.getAnnotation(I2cSensor.class);
/* 203 */       String name = getSensorName(clazz, i2cSensor.name());
/* 204 */       String description = i2cSensor.description();
/* 205 */       String tag = i2cSensor.xmlTag();
/*     */       
/* 207 */       return new UserSensorType(UserSensorType.Flavor.I2C, clazz, name, description, tag);
/*     */     }
/* 209 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean checkSensorClassConstraints(UserSensorType sensorType)
/*     */   {
/* 215 */     if (!isHardwareDevice(sensorType.getClazz()))
/*     */     {
/* 217 */       reportConfigurationError("'%s' class doesn't inherit from the class 'HardwareDevice'", new Object[] { sensorType.getClazz().getSimpleName() });
/* 218 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 223 */     if (!Modifier.isPublic(sensorType.getClazz().getModifiers()))
/*     */     {
/* 225 */       reportConfigurationError("'%s' class is not declared 'public'", new Object[] { sensorType.getClazz().getSimpleName() });
/* 226 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 230 */     if (!sensorType.hasConstructors())
/*     */     {
/* 232 */       reportConfigurationError("'%s' class lacks necessary constructor", new Object[] { sensorType.getClazz().getSimpleName() });
/* 233 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 237 */     if (!isLegalSensorName(sensorType.getName()))
/*     */     {
/* 239 */       reportConfigurationError("\"%s\" is not a legal sensor name", new Object[] { sensorType.getName() });
/* 240 */       return false;
/*     */     }
/* 242 */     if (this.existingConfigurationTypeNames.contains(sensorType.getName()))
/*     */     {
/* 244 */       reportConfigurationError("the sensor \"%s\" is already defined", new Object[] { sensorType.getName() });
/* 245 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 249 */     if (!isLegalXmlTag(sensorType.getXmlTag()))
/*     */     {
/* 251 */       reportConfigurationError("\"%s\" is not a legal XML tag for the sensor \"%s\"", new Object[] { sensorType.getXmlTag(), sensorType.getName() });
/* 252 */       return false;
/*     */     }
/* 254 */     if (this.existingXmlTags.contains(sensorType.getXmlTag()))
/*     */     {
/* 256 */       reportConfigurationError("the XML tag \"%s\" is already defined", new Object[] { sensorType.getXmlTag() });
/* 257 */       return false;
/*     */     }
/*     */     
/* 260 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getSensorName(Class<HardwareDevice> clazz, String annotatedName)
/*     */   {
/*     */     String name;
/*     */     String name;
/* 268 */     if (annotatedName != null) {
/* 269 */       name = annotatedName;
/*     */     } else {
/* 271 */       name = clazz.getSimpleName();
/*     */     }
/* 273 */     if (name.trim().equals("")) {
/* 274 */       name = clazz.getSimpleName();
/*     */     }
/* 276 */     return name;
/*     */   }
/*     */   
/*     */   protected boolean isLegalXmlTag(String xmlTag)
/*     */   {
/* 281 */     if (!isGoodString(xmlTag)) {
/* 282 */       return false;
/*     */     }
/*     */     
/* 285 */     if (!xmlTag.matches("^\\p{Alnum}+$")) {
/* 286 */       return false;
/*     */     }
/* 288 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean isLegalSensorName(String name)
/*     */   {
/* 293 */     if (!isGoodString(name)) {
/* 294 */       return false;
/*     */     }
/* 296 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean isGoodString(String string)
/*     */   {
/* 301 */     if (string == null)
/* 302 */       return false;
/* 303 */     if (!string.trim().equals(string))
/* 304 */       return false;
/* 305 */     if (string.length() == 0) {
/* 306 */       return false;
/*     */     }
/* 308 */     return true;
/*     */   }
/*     */   
/*     */   void reportConfigurationError(String format, Object... args)
/*     */   {
/* 313 */     String message = String.format(format, args);
/* 314 */     RobotLog.ww("UserSensorTypeManager", String.format("configuration error: %s", new Object[] { message }));
/* 315 */     RobotLog.setGlobalErrorMsg(message);
/*     */   }
/*     */   
/*     */   private boolean isHardwareDevice(Class clazz)
/*     */   {
/* 320 */     return ClassUtil.inheritsFrom(clazz, HardwareDevice.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\UserSensorTypeManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */