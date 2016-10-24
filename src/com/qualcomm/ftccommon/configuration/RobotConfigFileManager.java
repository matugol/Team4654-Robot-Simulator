/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Context;
/*     */ import android.content.SharedPreferences;
/*     */ import android.content.SharedPreferences.Editor;
/*     */ import android.content.res.AssetManager;
/*     */ import android.content.res.Resources;
/*     */ import android.content.res.XmlResourceParser;
/*     */ import android.graphics.Color;
/*     */ import android.preference.PreferenceManager;
/*     */ import android.support.annotation.IdRes;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.view.View;
/*     */ import android.widget.TextView;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.exception.DuplicateNameException;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
/*     */ import com.qualcomm.robotcore.hardware.configuration.UserSensorType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.UserSensorType.Flavor;
/*     */ import com.qualcomm.robotcore.hardware.configuration.UserSensorTypeManager;
/*     */ import com.qualcomm.robotcore.hardware.configuration.WriteXMLFileHandler;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.Dom2XmlPullBuilder;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RobotConfigFileManager
/*     */ {
/*     */   public static final String TAG = "RobotConfigFileManager";
/*     */   public static final boolean DEBUG = false;
/*     */   @Deprecated
/*  80 */   public static final File CONFIG_FILES_DIR = AppUtil.FIRST_FOLDER;
/*     */   public static final String ROBOT_CONFIG_DESCRIPTION_GENERATE_XSLT = "RobotConfigDescriptionGenerate.xslt";
/*     */   public static final String ROBOT_CONFIG_TAXONOMY_XML = "RobotConfigTaxonomy.xml";
/*     */   public static final String FILE_LIST_COMMAND_DELIMITER = ";";
/*     */   public static final String FILE_EXT = ".xml";
/*     */   public final String noConfig;
/*     */   private Context context;
/*     */   private Activity activity;
/*     */   private Resources resources;
/*     */   private WriteXMLFileHandler writer;
/*     */   private SharedPreferences preferences;
/*     */   @IdRes
/*  92 */   private int idActiveConfigName = R.id.idActiveConfigName; @IdRes
/*  93 */   private int idActiveConfigHeader = R.id.idActiveConfigHeader;
/*  94 */   private NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
/*  95 */   private AppUtil appUtil = AppUtil.getInstance();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */   private static List<Integer> xmlResourceIds = new LinkedList();
/* 102 */   private static List<Integer> xmlResourceTemplateIds = new LinkedList();
/*     */   
/*     */   public RobotConfigFileManager(Activity activity)
/*     */   {
/* 106 */     this.activity = activity;
/* 107 */     this.context = this.appUtil.getApplication();
/* 108 */     this.resources = this.context.getResources();
/* 109 */     this.writer = new WriteXMLFileHandler(this.context);
/* 110 */     this.preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
/* 111 */     this.noConfig = this.context.getString(R.string.noCurrentConfigFile);
/*     */   }
/*     */   
/*     */   public RobotConfigFileManager()
/*     */   {
/* 116 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createConfigFolder()
/*     */   {
/* 125 */     File robotDir = CONFIG_FILES_DIR;
/* 126 */     boolean createdDir = true;
/*     */     
/* 128 */     if (!robotDir.exists()) {
/* 129 */       createdDir = robotDir.mkdir();
/*     */     }
/*     */     
/* 132 */     if (!createdDir) {
/* 133 */       RobotLog.ee("RobotConfigFileManager", "Can't create the Robot Config Files directory!");
/* 134 */       this.appUtil.showToast(this.context, this.context.getString(R.string.toastCantCreateRobotConfigFilesDir));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getRobotConfigTypeAttribute()
/*     */   {
/* 141 */     return "FirstInspires-FTC";
/*     */   }
/*     */   
/*     */   public static String getRobotConfigTemplateAttribute()
/*     */   {
/* 146 */     return "FirstInspires-FTC-template";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Collection<Integer> getXmlResourceIds()
/*     */   {
/* 154 */     return xmlResourceIds;
/*     */   }
/*     */   
/*     */   public static Collection<Integer> getXmlResourceTemplateIds()
/*     */   {
/* 159 */     return xmlResourceTemplateIds;
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public RobotConfigFile getConfigFromString(String objSerialized) {
/* 164 */     return RobotConfigFile.fromString(this, objSerialized);
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public RobotConfigFile getActiveConfigAndUpdateUI() {
/* 169 */     RobotConfigFile file = getActiveConfig();
/* 170 */     updateActiveConfigHeader(file);
/* 171 */     return file;
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public RobotConfigFile getActiveConfig() {
/* 176 */     String key = this.context.getString(R.string.pref_hardware_config_filename);
/*     */     
/*     */ 
/*     */ 
/* 180 */     String objSerialized = this.preferences.getString(key, null);
/* 181 */     RobotConfigFile file; RobotConfigFile file; if (objSerialized == null) {
/* 182 */       file = RobotConfigFile.noConfig(this);
/*     */     } else {
/* 184 */       file = getConfigFromString(objSerialized);
/*     */     }
/*     */     
/*     */ 
/* 188 */     return file;
/*     */   }
/*     */   
/*     */ 
/*     */   public void requestActiveConfigFromRobotController()
/*     */   {
/* 194 */     this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_ACTIVE_CONFIGURATION"));
/*     */   }
/*     */   
/*     */   public void sendActiveConfigToDriverStation()
/*     */   {
/* 199 */     RobotConfigFile configFile = getActiveConfig();
/* 200 */     String serialized = configFile.toString();
/*     */     
/* 202 */     this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_ACTIVE_CONFIGURATION_RESP", serialized));
/*     */   }
/*     */   
/*     */   public void setActiveConfigAndUpdateUI(boolean runningOnDriverStation, @NonNull RobotConfigFile configFile)
/*     */   {
/* 207 */     setActiveConfig(runningOnDriverStation, configFile);
/* 208 */     updateActiveConfigHeader(configFile);
/*     */   }
/*     */   
/*     */   public void setActiveConfigAndUpdateUI(@NonNull RobotConfigFile config)
/*     */   {
/* 213 */     setActiveConfig(config);
/* 214 */     updateActiveConfigHeader(config);
/*     */   }
/*     */   
/*     */   public void setActiveConfig(boolean runningOnDriverStation, @NonNull RobotConfigFile config)
/*     */   {
/* 219 */     if (runningOnDriverStation) {
/* 220 */       sendRobotControllerActiveConfigAndUpdateUI(config);
/*     */     } else {
/* 222 */       setActiveConfig(config);
/* 223 */       sendActiveConfigToDriverStation();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setActiveConfig(@NonNull RobotConfigFile cfgFile)
/*     */   {
/* 230 */     Gson gson = RobotConfigFile.newGson();
/* 231 */     String objSerialized = gson.toJson(cfgFile);
/* 232 */     SharedPreferences.Editor edit = this.preferences.edit();
/* 233 */     String key = this.context.getString(R.string.pref_hardware_config_filename);
/*     */     
/* 235 */     edit.putString(key, objSerialized);
/* 236 */     edit.apply();
/*     */   }
/*     */   
/*     */   public void sendRobotControllerActiveConfigAndUpdateUI(@NonNull RobotConfigFile config)
/*     */   {
/* 241 */     this.networkConnectionHandler.sendCommand(new Command("CMD_ACTIVATE_CONFIGURATION", config.toString()));
/*     */   }
/*     */   
/*     */   public void updateActiveConfigHeader(RobotConfigFile robotConfigFile)
/*     */   {
/* 246 */     updateActiveConfigHeader(robotConfigFile.getName(), robotConfigFile.isDirty());
/*     */   }
/*     */   
/*     */   public void updateActiveConfigHeader(final String fileNameIn, final boolean dirty)
/*     */   {
/* 251 */     this.appUtil.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 255 */         String fileName = RobotConfigFileManager.stripFileNameExtension(fileNameIn).trim();
/* 256 */         if (fileName.isEmpty()) {
/* 257 */           fileName = RobotConfigFileManager.this.noConfig;
/*     */         }
/* 259 */         if (dirty) {
/* 260 */           fileName = String.format(RobotConfigFileManager.this.context.getString(R.string.configDirtyLabel), new Object[] { fileName });
/*     */         }
/*     */         
/* 263 */         TextView activeFile = (TextView)RobotConfigFileManager.this.activity.findViewById(RobotConfigFileManager.this.idActiveConfigName);
/* 264 */         if (activeFile != null) {
/* 265 */           activeFile.setText(fileName);
/*     */         } else {
/* 267 */           RobotLog.ee("RobotConfigFileManager", "unable to find header text 0x%08x", new Object[] { Integer.valueOf(RobotConfigFileManager.this.idActiveConfigName) });
/*     */         }
/*     */         
/* 270 */         if ((!dirty) && (fileName.equalsIgnoreCase(RobotConfigFileManager.this.noConfig)))
/*     */         {
/* 272 */           int color = Color.parseColor("#bf0510");
/* 273 */           RobotConfigFileManager.this.changeBackground(color, RobotConfigFileManager.this.idActiveConfigHeader);
/* 274 */         } else if (dirty) {
/* 275 */           RobotConfigFileManager.this.changeBackground(-12303292, RobotConfigFileManager.this.idActiveConfigHeader);
/*     */         }
/*     */         else {
/* 278 */           int color = Color.parseColor("#790E15");
/* 279 */           RobotConfigFileManager.this.changeBackground(color, RobotConfigFileManager.this.idActiveConfigHeader);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void changeBackground(int color, @IdRes int idView)
/*     */   {
/* 287 */     View view = this.activity.findViewById(idView);
/* 288 */     if (view != null) {
/* 289 */       view.setBackgroundColor(color);
/*     */     } else {
/* 291 */       RobotLog.ee("RobotConfigFileManager", "unable to find header 0x%08x", new Object[] { Integer.valueOf(idView) });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ConfigNameCheckResult
/*     */   {
/* 297 */     public boolean success = true;
/* 298 */     public String errorFormat = null;
/*     */     
/*     */     public ConfigNameCheckResult(boolean success) {
/* 301 */       this.success = success;
/*     */     }
/*     */     
/*     */     public ConfigNameCheckResult(@NonNull String errorFormat) {
/* 305 */       this.success = false;
/* 306 */       this.errorFormat = errorFormat;
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
/*     */   public ConfigNameCheckResult isPlausibleConfigName(RobotConfigFile existingConfig, String candidate, List<RobotConfigFile> extantConfigurations)
/*     */   {
/* 321 */     if (!candidate.equals(candidate.trim())) { return new ConfigNameCheckResult(this.context.getString(R.string.configNameWhitespace));
/*     */     }
/*     */     
/* 324 */     if (candidate.length() == 0) { return new ConfigNameCheckResult(this.context.getString(R.string.configNameEmpty));
/*     */     }
/*     */     
/* 327 */     if (candidate.equalsIgnoreCase(this.noConfig)) { return new ConfigNameCheckResult(this.context.getString(R.string.configNameReserved));
/*     */     }
/*     */     
/* 330 */     if (candidate.equalsIgnoreCase(existingConfig.getName())) {
/* 331 */       return existingConfig.isReadOnly() ? new ConfigNameCheckResult(this.context.getString(R.string.configNameReadOnly)) : new ConfigNameCheckResult(true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 337 */     for (RobotConfigFile configFile : extantConfigurations) {
/* 338 */       if (candidate.equalsIgnoreCase(configFile.getName())) {
/* 339 */         return new ConfigNameCheckResult(this.context.getString(R.string.configNameExists));
/*     */       }
/*     */     }
/*     */     
/* 343 */     return new ConfigNameCheckResult(true);
/*     */   }
/*     */   
/*     */   public static String stripFileNameExtension(String fileName)
/*     */   {
/* 348 */     fileName = fileName.replaceFirst("[.][^.]+$", "");
/* 349 */     return fileName;
/*     */   }
/*     */   
/*     */   public static File stripFileNameExtension(File path)
/*     */   {
/* 354 */     File folder = path.getParentFile();
/* 355 */     String file = path.getName();
/* 356 */     file = stripFileNameExtension(file);
/* 357 */     return new File(folder, file);
/*     */   }
/*     */   
/*     */   public static String withExtension(String fileName)
/*     */   {
/* 362 */     return stripFileNameExtension(fileName) + ".xml";
/*     */   }
/*     */   
/*     */   public static File getFullPath(String fileNameWithoutExtension)
/*     */   {
/* 367 */     fileNameWithoutExtension = withExtension(fileNameWithoutExtension);
/* 368 */     return new File(CONFIG_FILES_DIR, fileNameWithoutExtension);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<RobotConfigFile> getXMLFiles()
/*     */   {
/* 378 */     File robotDir = CONFIG_FILES_DIR;
/* 379 */     File[] configFiles = robotDir.listFiles();
/*     */     
/* 381 */     ArrayList<RobotConfigFile> fileList = new ArrayList();
/*     */     
/* 383 */     for (File f : configFiles) {
/* 384 */       if (f.isFile()) {
/* 385 */         String name = f.getName();
/* 386 */         Pattern pattern = Pattern.compile("(?i).xml");
/* 387 */         if (pattern.matcher(name).find()) {
/* 388 */           String nameNoExt = stripFileNameExtension(name);
/* 389 */           fileList.add(new RobotConfigFile(this, nameNoExt));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 397 */     for (Iterator i$ = xmlResourceIds.iterator(); i$.hasNext();) { int id = ((Integer)i$.next()).intValue();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 407 */       XmlResourceParser xpp = this.resources.getXml(id);
/* 408 */       String name = RobotConfigResFilter.getRootAttribute(xpp, "Robot", "name", this.resources.getResourceEntryName(id));
/*     */       
/*     */ 
/* 411 */       RobotConfigFile configFile = new RobotConfigFile(name, id);
/* 412 */       if (!configFile.containedIn(fileList)) {
/* 413 */         fileList.add(configFile);
/*     */       }
/*     */     }
/*     */     
/* 417 */     return fileList;
/*     */   }
/*     */   
/*     */   public ArrayList<RobotConfigFile> getXMLTemplates()
/*     */   {
/* 422 */     ArrayList<RobotConfigFile> templateList = new ArrayList();
/* 423 */     for (Iterator i$ = xmlResourceTemplateIds.iterator(); i$.hasNext();) { int id = ((Integer)i$.next()).intValue();
/*     */       
/* 425 */       XmlResourceParser xpp = this.resources.getXml(id);
/* 426 */       String name = RobotConfigResFilter.getRootAttribute(xpp, "Robot", "name", this.resources.getResourceEntryName(id));
/*     */       
/*     */ 
/* 429 */       RobotConfigFile configFile = new RobotConfigFile(name, id);
/* 430 */       if (!configFile.containedIn(templateList)) {
/* 431 */         templateList.add(configFile);
/*     */       }
/*     */     }
/* 434 */     return templateList;
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
/*     */   @NonNull
/*     */   public String getRobotConfigDescription(@NonNull XmlPullParser xpp)
/*     */   {
/*     */     try
/*     */     {
/* 456 */       Source xmlTemplate = getSourceFromPullParser(xpp);
/*     */       
/* 458 */       Source xslt = getRobotConfigDescriptionTransform();
/* 459 */       StringWriter output = new StringWriter();
/* 460 */       Result result = new StreamResult(output);
/*     */       
/* 462 */       TransformerFactory factory = TransformerFactory.newInstance();
/* 463 */       Transformer transformer = factory.newTransformer(xslt);
/* 464 */       transformer.transform(xmlTemplate, result);
/*     */       
/* 466 */       return output.toString().trim();
/*     */     }
/*     */     catch (IOException|TransformerException|XmlPullParserException e) {
/* 469 */       RobotLog.logStacktrace(e);
/*     */     }
/* 471 */     return this.activity.getString(R.string.templateConfigureNoDescriptionAvailable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonNull
/*     */   protected Source getRobotConfigDescriptionTransform()
/*     */     throws XmlPullParserException, IOException, TransformerConfigurationException, TransformerException
/*     */   {
/* 481 */     Reader xmlConfigTaxonomyReader = new InputStreamReader(this.context.getAssets().open("RobotConfigTaxonomy.xml"));
/* 482 */     XmlPullParser xmlConfigTaxonomyParser = ReadXMLFileHandler.xmlPullParserFromReader(xmlConfigTaxonomyReader);
/* 483 */     Dom2XmlPullBuilder builder = new Dom2XmlPullBuilder();
/* 484 */     Element rootElement = builder.parseSubTree(xmlConfigTaxonomyParser);
/* 485 */     Document document = rootElement.getOwnerDocument();
/*     */     
/*     */ 
/* 488 */     for (UserSensorType userSensorType : UserSensorTypeManager.getInstance().allUserTypes(UserSensorType.Flavor.I2C))
/*     */     {
/* 490 */       Element sensor = document.createElement("Sensor");
/* 491 */       addChild(document, sensor, "XmlTag", userSensorType.getXmlTag());
/* 492 */       addChild(document, sensor, "Description", userSensorType.getDescription());
/* 493 */       addChild(document, sensor, "Bus", "i2cbus");
/* 494 */       addChild(document, sensor, "BusDefault", this.activity.getString(R.string.userSensorTypeBusDefault));
/*     */       
/* 496 */       rootElement.appendChild(sensor);
/*     */     }
/*     */     
/*     */ 
/* 500 */     Source sourceConfigTaxonomy = new DOMSource(rootElement);
/*     */     
/*     */ 
/* 503 */     Source xsltGenerate = new StreamSource(this.context.getAssets().open("RobotConfigDescriptionGenerate.xslt"));
/*     */     
/*     */ 
/* 506 */     StringWriter xsltDescriptionWriter = new StringWriter();
/* 507 */     Result transformerResult = new StreamResult(xsltDescriptionWriter);
/* 508 */     TransformerFactory factory = TransformerFactory.newInstance();
/* 509 */     Transformer transformer = factory.newTransformer(xsltGenerate);
/* 510 */     transformer.transform(sourceConfigTaxonomy, transformerResult);
/* 511 */     String xsltDescriptionTransform = xsltDescriptionWriter.toString().trim();
/*     */     
/*     */ 
/* 514 */     StringReader xsltDescriptionTransformReader = new StringReader(xsltDescriptionTransform);
/* 515 */     Source result = new StreamSource(xsltDescriptionTransformReader);
/* 516 */     return result;
/*     */   }
/*     */   
/*     */   protected void addChild(Document document, Element parent, String tag, String contents)
/*     */   {
/* 521 */     Element child = document.createElement(tag);
/* 522 */     child.setTextContent(contents);
/* 523 */     parent.appendChild(child);
/*     */   }
/*     */   
/*     */   protected Source getSourceFromPullParser(@NonNull XmlPullParser xpp) throws XmlPullParserException, IOException
/*     */   {
/* 528 */     Dom2XmlPullBuilder builder = new Dom2XmlPullBuilder();
/* 529 */     Element rootElement = builder.parseSubTree(xpp);
/* 530 */     return new DOMSource(rootElement);
/*     */   }
/*     */   
/*     */   public static String serializeXMLConfigList(List<RobotConfigFile> configList)
/*     */   {
/* 535 */     Gson gson = RobotConfigFile.newGson();
/* 536 */     String objsSerialized = gson.toJson(configList);
/* 537 */     return objsSerialized;
/*     */   }
/*     */   
/*     */   public static String serializeConfig(RobotConfigFile configFile)
/*     */   {
/* 542 */     Gson gson = RobotConfigFile.newGson();
/* 543 */     String serialized = gson.toJson(configFile);
/* 544 */     return serialized;
/*     */   }
/*     */   
/*     */   public static List<RobotConfigFile> deserializeXMLConfigList(String objsSerialized)
/*     */   {
/* 549 */     Gson gson = RobotConfigFile.newGson();
/* 550 */     Type collectionType = new TypeToken() {}.getType();
/* 551 */     List<RobotConfigFile> configList = (List)gson.fromJson(objsSerialized, collectionType);
/* 552 */     return configList;
/*     */   }
/*     */   
/*     */   public static RobotConfigFile deserializeConfig(String serialized)
/*     */   {
/* 557 */     Gson gson = RobotConfigFile.newGson();
/* 558 */     Type type = RobotConfigFile.class;
/* 559 */     RobotConfigFile config = (RobotConfigFile)gson.fromJson(serialized, type);
/* 560 */     return config;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toXml(Map<SerialNumber, ControllerConfiguration> deviceControllers)
/*     */   {
/* 571 */     ArrayList<ControllerConfiguration> deviceList = new ArrayList();
/* 572 */     deviceList.addAll(deviceControllers.values());
/* 573 */     String output = null;
/*     */     try {
/* 575 */       output = this.writer.toXml(deviceList, "type", getRobotConfigTypeAttribute());
/*     */     } catch (DuplicateNameException e) {
/* 577 */       this.appUtil.showToast(this.context, String.format(this.context.getString(R.string.toastDuplicateName), new Object[] { e.getMessage() }));
/* 578 */       RobotLog.ee("RobotConfigFileManager", "Found " + e.getMessage());
/* 579 */       output = null;
/*     */     } catch (RuntimeException e) {
/* 581 */       RobotLog.ee("RobotConfigFileManager", "exception while writing XML");
/* 582 */       RobotLog.logStacktrace(e);
/* 583 */       output = null;
/*     */     }
/* 585 */     return output;
/*     */   }
/*     */   
/*     */   public String toXml(RobotConfigMap robotConfigMap) {
/* 589 */     return toXml(robotConfigMap.map);
/*     */   }
/*     */   
/*     */   void writeXMLToFile(String filenameWithExt, String data)
/*     */     throws RobotCoreException, IOException
/*     */   {
/* 595 */     this.writer.writeToFile(data, CONFIG_FILES_DIR, filenameWithExt);
/*     */   }
/*     */   
/*     */   void writeToRobotController(RobotConfigFile cfgFile, String data)
/*     */   {
/* 600 */     this.networkConnectionHandler.sendCommand(new Command("CMD_SAVE_CONFIGURATION", cfgFile.toString() + ";" + data));
/*     */   }
/*     */   
/*     */   public void writeToFile(RobotConfigFile cfgFile, boolean runningOnDriverStation, @NonNull String data)
/*     */     throws RobotCoreException, IOException
/*     */   {
/* 606 */     boolean wasDirty = cfgFile.isDirty();
/* 607 */     cfgFile.markClean();
/*     */     try {
/* 609 */       if (runningOnDriverStation) {
/* 610 */         writeToRobotController(cfgFile, data);
/*     */       }
/*     */       else {
/* 613 */         writeXMLToFile(withExtension(cfgFile.getName()), data);
/*     */       }
/*     */     } catch (RobotCoreException|IOException|RuntimeException e) {
/* 616 */       if (wasDirty) cfgFile.markDirty();
/* 617 */       throw e;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\RobotConfigFileManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */