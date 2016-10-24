/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.Resources;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.support.annotation.XmlRes;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.util.Collection;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlPullParserFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RobotConfigFile
/*     */ {
/*     */   private static final String LOGGER_TAG = "RobotConfigFile";
/*     */   private String name;
/*     */   @XmlRes
/*     */   private int resourceId;
/*     */   private FileLocation location;
/*     */   private boolean isDirty;
/*     */   
/*     */   public static enum FileLocation
/*     */   {
/*  65 */     NONE, 
/*  66 */     LOCAL_STORAGE, 
/*  67 */     RESOURCE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private FileLocation() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Gson newGson()
/*     */   {
/*  79 */     return new Gson();
/*     */   }
/*     */   
/*     */   public static RobotConfigFile noConfig(RobotConfigFileManager configFileManager)
/*     */   {
/*  84 */     return new RobotConfigFile(configFileManager, configFileManager.noConfig);
/*     */   }
/*     */   
/*     */   public RobotConfigFile(RobotConfigFileManager configFileManager, String name)
/*     */   {
/*  89 */     this.name = RobotConfigFileManager.stripFileNameExtension(name);
/*  90 */     this.resourceId = 0;
/*  91 */     this.location = (this.name.equalsIgnoreCase(configFileManager.noConfig) ? FileLocation.NONE : FileLocation.LOCAL_STORAGE);
/*  92 */     this.isDirty = false;
/*     */   }
/*     */   
/*     */   public RobotConfigFile(String name, @XmlRes int resourceId)
/*     */   {
/*  97 */     this.name = name;
/*  98 */     this.resourceId = resourceId;
/*  99 */     this.location = FileLocation.RESOURCE;
/* 100 */     this.isDirty = false;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly()
/*     */   {
/* 105 */     return (this.location == FileLocation.RESOURCE) || (this.location == FileLocation.NONE);
/*     */   }
/*     */   
/*     */   public boolean containedIn(Collection<RobotConfigFile> configFiles)
/*     */   {
/* 110 */     for (RobotConfigFile him : configFiles) {
/* 111 */       if (him.name.equalsIgnoreCase(this.name)) {
/* 112 */         return true;
/*     */       }
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public void markDirty()
/*     */   {
/* 120 */     this.isDirty = true;
/*     */   }
/*     */   
/*     */   public void markClean()
/*     */   {
/* 125 */     this.isDirty = false;
/*     */   }
/*     */   
/*     */   public boolean isDirty()
/*     */   {
/* 130 */     return this.isDirty;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 135 */     return this.name;
/*     */   }
/*     */   
/*     */   public File getFullPath()
/*     */   {
/* 140 */     return RobotConfigFileManager.getFullPath(getName());
/*     */   }
/*     */   
/*     */   public int getResourceId()
/*     */   {
/* 145 */     return this.resourceId;
/*     */   }
/*     */   
/*     */   public FileLocation getLocation()
/*     */   {
/* 150 */     return this.location;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public XmlPullParser getXml() {
/* 155 */     switch (this.location) {
/*     */     case LOCAL_STORAGE: 
/* 157 */       return getXmlLocalStorage();
/*     */     case RESOURCE: 
/* 159 */       return getXmlResource();
/*     */     case NONE: 
/* 161 */       return null;
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected XmlPullParser getXmlLocalStorage()
/*     */   {
/* 172 */     XmlPullParser parser = null;
/*     */     try {
/* 174 */       File fullPath = RobotConfigFileManager.getFullPath(getName());
/* 175 */       FileInputStream inputStream = new FileInputStream(fullPath);
/* 176 */       XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
/* 177 */       factory.setNamespaceAware(true);
/* 178 */       parser = factory.newPullParser();
/* 179 */       parser.setInput(inputStream, null);
/*     */     } catch (XmlPullParserException|FileNotFoundException e) {
/* 181 */       e.printStackTrace();
/*     */     }
/*     */     
/* 184 */     return parser;
/*     */   }
/*     */   
/*     */   protected XmlPullParser getXmlResource()
/*     */   {
/* 189 */     Context context = AppUtil.getInstance().getApplication();
/* 190 */     return context.getResources().getXml(this.resourceId);
/*     */   }
/*     */   
/*     */   public boolean isNoConfig()
/*     */   {
/* 195 */     return this.location == FileLocation.NONE;
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public String toString() {
/* 200 */     Gson gson = newGson();
/* 201 */     return gson.toJson(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonNull
/*     */   public static RobotConfigFile fromString(RobotConfigFileManager configFileManager, String serializedForm)
/*     */   {
/*     */     try
/*     */     {
/* 212 */       Gson gson = newGson();
/* 213 */       RobotConfigFile file = (RobotConfigFile)gson.fromJson(serializedForm, RobotConfigFile.class);
/* 214 */       if (file == null) {
/* 215 */         return noConfig(configFileManager);
/*     */       }
/* 217 */       return file;
/*     */     }
/*     */     catch (JsonSyntaxException e) {
/* 220 */       RobotLog.ee("RobotConfigFile", "Could not parse the stored config file data from shared preferences"); }
/* 221 */     return noConfig(configFileManager);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\RobotConfigFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */