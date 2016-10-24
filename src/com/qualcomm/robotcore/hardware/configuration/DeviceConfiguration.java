/*     */ package com.qualcomm.robotcore.hardware.configuration;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeviceConfiguration
/*     */   implements Serializable, Comparable<DeviceConfiguration>
/*     */ {
/*     */   public static final String DISABLED_DEVICE_NAME = "NO$DEVICE$ATTACHED";
/*     */   protected String name;
/*  40 */   private ConfigurationType type = BuiltInConfigurationType.NOTHING;
/*     */   private int port;
/*  42 */   private boolean enabled = false;
/*     */   
/*     */   public DeviceConfiguration(int port, ConfigurationType type, String name, boolean enabled)
/*     */   {
/*  46 */     this.port = port;
/*  47 */     this.type = type;
/*  48 */     this.name = name;
/*  49 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public DeviceConfiguration(int port)
/*     */   {
/*  54 */     this(port, BuiltInConfigurationType.NOTHING, "NO$DEVICE$ATTACHED", false);
/*     */   }
/*     */   
/*     */   public DeviceConfiguration(ConfigurationType type)
/*     */   {
/*  59 */     this(0, type, "", false);
/*     */   }
/*     */   
/*     */ 
/*     */   public DeviceConfiguration(int port, ConfigurationType type)
/*     */   {
/*  65 */     this(port, type, "NO$DEVICE$ATTACHED", false);
/*     */   }
/*     */   
/*     */   public boolean isEnabled()
/*     */   {
/*  70 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled)
/*     */   {
/*  75 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  80 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String newName)
/*     */   {
/*  85 */     this.name = newName;
/*     */   }
/*     */   
/*     */   public void setType(ConfigurationType type)
/*     */   {
/*  90 */     this.type = type;
/*     */   }
/*     */   
/*     */   public ConfigurationType getType()
/*     */   {
/*  95 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getPort()
/*     */   {
/* 100 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setPort(int port)
/*     */   {
/* 105 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */   public int compareTo(DeviceConfiguration another)
/*     */   {
/* 111 */     return getPort() - another.getPort();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\DeviceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */