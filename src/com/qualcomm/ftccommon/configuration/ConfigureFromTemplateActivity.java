/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.RobocolDatagram;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.Event;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.NetworkConnectionCallback;
/*     */ import java.io.StringReader;
/*     */ import java.text.Collator;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Deque;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import junit.framework.Assert;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.ToastLocation;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable.RecvLoopCallback;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigureFromTemplateActivity
/*     */   extends EditActivity
/*     */   implements NetworkConnection.NetworkConnectionCallback, RecvLoopRunnable.RecvLoopCallback
/*     */ {
/*  81 */   public static final RequestCode requestCode = RequestCode.CONFIG_FROM_TEMPLATE;
/*     */   public static final String TAG = "ConfigFromTemplate";
/*     */   
/*  84 */   public ConfigureFromTemplateActivity() { this.networkConnectionHandler = NetworkConnectionHandler.getInstance();
/*  85 */     this.configurationList = new CopyOnWriteArrayList();
/*  86 */     this.templateList = new CopyOnWriteArrayList();
/*     */     
/*     */ 
/*  89 */     this.remoteTemplates = new ConcurrentHashMap();
/*  90 */     this.receivedConfigProcessors = new LinkedList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCreate(Bundle savedInstanceState)
/*     */   {
/*  98 */     super.onCreate(savedInstanceState);
/*  99 */     setContentView(R.layout.activity_configure_from_template);
/*     */     
/* 101 */     EditParameters parameters = EditParameters.fromIntent(this, getIntent());
/* 102 */     deserialize(parameters);
/*     */     
/* 104 */     if (this.remoteConfigure)
/*     */     {
/* 106 */       this.networkConnectionHandler.pushNetworkConnectionCallback(this);
/* 107 */       this.networkConnectionHandler.pushReceiveLoopCallback(this);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 112 */       this.usbScanManager = new USBScanManager(this.context, this.remoteConfigure);
/* 113 */       this.usbScanManager.startExecutorService(0);
/* 114 */       this.usbScanManager.startDeviceScanIfNecessary();
/*     */     }
/*     */     catch (RobotCoreException e)
/*     */     {
/* 118 */       this.appUtil.showToast(ToastLocation.ONLY_LOCAL, this.context, getString(R.string.templateConfigureFailedToOpenUSBScanManager));
/* 119 */       RobotLog.ee("ConfigFromTemplate", "Failed to open usb scan manager: " + e.toString());
/* 120 */       RobotLog.logStacktrace(e);
/*     */     }
/*     */     
/* 123 */     this.orangeTextAnchor = ((ViewGroup)findViewById(R.id.orangeTextAnchor));
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/* 129 */     super.onStart();
/* 130 */     this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
/*     */     
/* 132 */     if (!this.remoteConfigure)
/*     */     {
/* 134 */       this.configurationList = this.robotConfigFileManager.getXMLFiles();
/* 135 */       this.templateList = this.robotConfigFileManager.getXMLTemplates();
/* 136 */       warnIfNoTemplates();
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 142 */       this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATIONS"));
/* 143 */       this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATION_TEMPLATES"));
/*     */     }
/* 145 */     populate();
/*     */   }
/*     */   
/*     */   protected CallbackResult handleCommandRequestConfigurationsResp(String extra)
/*     */     throws RobotCoreException
/*     */   {
/* 151 */     this.configurationList = RobotConfigFileManager.deserializeXMLConfigList(extra);
/* 152 */     return CallbackResult.HANDLED;
/*     */   }
/*     */   
/*     */   protected CallbackResult handleCommandRequestTemplatesResp(String extra)
/*     */     throws RobotCoreException
/*     */   {
/* 158 */     this.templateList = RobotConfigFileManager.deserializeXMLConfigList(extra);
/* 159 */     warnIfNoTemplates();
/* 160 */     populate();
/* 161 */     return CallbackResult.HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onDestroy()
/*     */   {
/* 167 */     super.onDestroy();
/* 168 */     this.usbScanManager.stopExecutorService();
/* 169 */     this.usbScanManager = null;
/* 170 */     if (this.remoteConfigure)
/*     */     {
/* 172 */       this.networkConnectionHandler.removeNetworkConnectionCallback(this);
/* 173 */       this.networkConnectionHandler.removeReceiveLoopCallback(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected NetworkConnectionHandler networkConnectionHandler;
/*     */   
/*     */   protected void warnIfNoTemplates()
/*     */   {
/* 183 */     if (this.templateList.size() == 0)
/*     */     {
/* 185 */       this.orangeTextAnchor.setVisibility(4);
/* 186 */       final String msg0 = getString(R.string.noTemplatesFoundTitle);
/* 187 */       final String msg1 = getString(R.string.noTemplatesFoundMessage);
/* 188 */       runOnUiThread(new Runnable()
/*     */       {
/*     */ 
/*     */         public void run()
/*     */         {
/* 193 */           ConfigureFromTemplateActivity.this.utility.setOrangeText(msg0, msg1, R.id.orangeTextAnchor, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */         }
/*     */       });
/*     */     }
/*     */     else
/*     */     {
/* 199 */       runOnUiThread(new Runnable()
/*     */       {
/*     */ 
/*     */         public void run()
/*     */         {
/* 204 */           ConfigureFromTemplateActivity.this.orangeTextAnchor.removeAllViews();
/* 205 */           ConfigureFromTemplateActivity.this.orangeTextAnchor.setVisibility(8);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   protected void populate()
/*     */   {
/* 213 */     runOnUiThread(new Runnable()
/*     */     {
/*     */ 
/*     */       public void run()
/*     */       {
/* 218 */         ViewGroup parent = (ViewGroup)ConfigureFromTemplateActivity.this.findViewById(R.id.templateList);
/* 219 */         parent.removeAllViews();
/*     */         
/* 221 */         final Collator coll = Collator.getInstance();
/* 222 */         coll.setStrength(0);
/* 223 */         Collections.sort(ConfigureFromTemplateActivity.this.templateList, new Comparator()
/*     */         {
/*     */           public int compare(RobotConfigFile lhs, RobotConfigFile rhs)
/*     */           {
/* 227 */             return coll.compare(lhs.getName(), rhs.getName());
/*     */           }
/*     */         });
/*     */         
/* 231 */         for (RobotConfigFile template : ConfigureFromTemplateActivity.this.templateList)
/*     */         {
/* 233 */           View child = LayoutInflater.from(ConfigureFromTemplateActivity.this.context).inflate(R.layout.template_info, null);
/* 234 */           parent.addView(child);
/*     */           
/* 236 */           TextView name = (TextView)child.findViewById(R.id.templateNameText);
/* 237 */           name.setText(template.getName());
/* 238 */           name.setTag(template);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void onConfigureButtonPressed(View v)
/*     */   {
/* 246 */     RobotConfigFile templateMeta = getTemplateMeta(v);
/* 247 */     getTemplateAndThen(templateMeta, new TemplateProcessor()
/*     */     {
/*     */       public void processTemplate(RobotConfigFile templateMeta, XmlPullParser xmlPullParser)
/*     */       {
/* 251 */         ConfigureFromTemplateActivity.this.configureFromTemplate(templateMeta, xmlPullParser);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   void configureFromTemplate(RobotConfigFile templateMeta, XmlPullParser xmlPullParser)
/*     */   {
/*     */     try {
/* 259 */       RobotConfigMap robotConfigMap = instantiateTemplate(templateMeta, xmlPullParser);
/* 260 */       awaitScannedDevices();
/*     */       
/* 262 */       Class clazz = FtcConfigurationActivity.class;
/* 263 */       EditParameters parameters = new EditParameters(this);
/* 264 */       parameters.setRobotConfigMap(robotConfigMap);
/* 265 */       parameters.setExtantRobotConfigurations(this.configurationList);
/* 266 */       parameters.setScannedDevices(this.scannedDevices);
/* 267 */       Intent intent = new Intent(this.context, clazz);
/* 268 */       parameters.putIntent(intent);
/*     */       
/*     */ 
/*     */ 
/* 272 */       this.robotConfigFileManager.setActiveConfig(RobotConfigFile.noConfig(this.robotConfigFileManager));
/* 273 */       startActivityForResult(intent, FtcConfigurationActivity.requestCode.value);
/*     */     }
/*     */     catch (RobotCoreException localRobotCoreException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void onActivityResult(int requestCode, int resultCode, Intent data)
/*     */   {
/* 282 */     if (requestCode == FtcConfigurationActivity.requestCode.value)
/*     */     {
/*     */ 
/* 285 */       this.currentCfgFile = this.robotConfigFileManager.getActiveConfigAndUpdateUI();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onInfoButtonPressed(View v)
/*     */   {
/* 291 */     RobotConfigFile templateMeta = getTemplateMeta(v);
/* 292 */     getTemplateAndThen(templateMeta, new TemplateProcessor()
/*     */     {
/*     */       public void processTemplate(RobotConfigFile templateMeta, XmlPullParser xmlPullParser)
/*     */       {
/* 296 */         ConfigureFromTemplateActivity.this.showInfo(templateMeta, xmlPullParser);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   protected void showInfo(RobotConfigFile template, XmlPullParser xmlPullParser)
/*     */   {
/* 303 */     String description = indent(3, this.robotConfigFileManager.getRobotConfigDescription(xmlPullParser));
/* 304 */     final String title = getString(R.string.templateConfigureConfigurationInstructionsTitle);
/* 305 */     final String message = String.format(getString(R.string.templateConfigurationInstructions), new Object[] { template.getName(), description });
/*     */     
/* 307 */     runOnUiThread(new Runnable()
/*     */     {
/*     */ 
/*     */       public void run()
/*     */       {
/* 312 */         ConfigureFromTemplateActivity.this.utility.setOrangeText(title, message.trim(), R.id.orangeTextAnchor, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1, R.id.orangeTextOKButton);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected List<RobotConfigFile> configurationList;
/*     */   
/*     */   protected List<RobotConfigFile> templateList;
/*     */   
/*     */   protected USBScanManager usbScanManager;
/*     */   
/*     */   protected ViewGroup orangeTextAnchor;
/*     */   
/*     */   protected Map<String, String> remoteTemplates;
/*     */   
/*     */   protected final Deque<StringProcessor> receivedConfigProcessors;
/*     */   
/*     */   protected void getTemplateAndThen(final RobotConfigFile templateMeta, final TemplateProcessor processor)
/*     */   {
/* 333 */     if (this.remoteConfigure)
/*     */     {
/*     */ 
/* 336 */       String template = (String)this.remoteTemplates.get(templateMeta.getName());
/* 337 */       if (template != null)
/*     */       {
/* 339 */         XmlPullParser xmlPullParser = xmlPullParserFromString(template);
/* 340 */         processor.processTemplate(templateMeta, xmlPullParser);
/*     */       }
/*     */       else
/*     */       {
/* 344 */         synchronized (this.receivedConfigProcessors)
/*     */         {
/* 346 */           this.receivedConfigProcessors.addLast(new StringProcessor()
/*     */           {
/*     */ 
/*     */             public void processString(String template)
/*     */             {
/* 351 */               ConfigureFromTemplateActivity.this.remoteTemplates.put(templateMeta.getName(), template);
/*     */               
/* 353 */               XmlPullParser xmlPullParser = ConfigureFromTemplateActivity.this.xmlPullParserFromString(template);
/* 354 */               processor.processTemplate(templateMeta, xmlPullParser);
/*     */             }
/* 356 */           });
/* 357 */           this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_PARTICULAR_CONFIGURATION", templateMeta.toString()));
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 363 */       processor.processTemplate(templateMeta, templateMeta.getXml());
/*     */     }
/*     */   }
/*     */   
/*     */   protected CallbackResult handleCommandRequestParticularConfigurationResp(String config) throws RobotCoreException
/*     */   {
/* 369 */     StringProcessor processor = null;
/* 370 */     synchronized (this.receivedConfigProcessors)
/*     */     {
/* 372 */       processor = (StringProcessor)this.receivedConfigProcessors.pollFirst();
/*     */     }
/* 374 */     if (processor != null)
/*     */     {
/* 376 */       processor.processString(config);
/*     */     }
/* 378 */     return CallbackResult.HANDLED;
/*     */   }
/*     */   
/*     */   protected XmlPullParser xmlPullParserFromString(String string)
/*     */   {
/* 383 */     return ReadXMLFileHandler.xmlPullParserFromReader(new StringReader(string));
/*     */   }
/*     */   
/*     */   protected RobotConfigFile getTemplateMeta(View v)
/*     */   {
/* 388 */     ViewGroup viewGroup = (ViewGroup)v.getParent();
/* 389 */     TextView name = (TextView)viewGroup.findViewById(R.id.templateNameText);
/* 390 */     return (RobotConfigFile)name.getTag();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ScannedDevices awaitScannedDevices()
/*     */   {
/*     */     try
/*     */     {
/* 400 */       this.scannedDevices = this.usbScanManager.await(4000L);
/*     */     }
/*     */     catch (InterruptedException e)
/*     */     {
/* 404 */       Thread.currentThread().interrupt();
/*     */     }
/* 406 */     return this.scannedDevices;
/*     */   }
/*     */   
/*     */   RobotConfigMap instantiateTemplate(RobotConfigFile templateMeta, XmlPullParser xmlPullParser) throws RobotCoreException
/*     */   {
/* 411 */     awaitScannedDevices();
/*     */     
/* 413 */     ReadXMLFileHandler readXMLFileHandler = new ReadXMLFileHandler();
/* 414 */     List<ControllerConfiguration> controllerList = readXMLFileHandler.parse(xmlPullParser);
/* 415 */     RobotConfigMap robotConfigMap = new RobotConfigMap(controllerList);
/* 416 */     robotConfigMap.bindUnboundControllers(this.scannedDevices);
/*     */     
/* 418 */     return robotConfigMap;
/*     */   }
/*     */   
/*     */   private String indent(int count, String target)
/*     */   {
/* 423 */     String indent = "";
/* 424 */     for (int i = 0; i < count; i++) indent = indent + " ";
/* 425 */     return indent + target.replace("\n", new StringBuilder().append("\n").append(indent).toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallbackResult commandEvent(Command command)
/*     */   {
/* 435 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/*     */     try
/*     */     {
/* 438 */       String name = command.getName();
/* 439 */       String extra = command.getExtra();
/*     */       
/* 441 */       if (name.equals("CMD_SCAN_RESP"))
/*     */       {
/* 443 */         result = handleCommandScanResp(extra);
/*     */       }
/* 445 */       else if (name.equals("CMD_REQUEST_CONFIGURATIONS_RESP"))
/*     */       {
/* 447 */         result = handleCommandRequestConfigurationsResp(extra);
/*     */       }
/* 449 */       else if (name.equals("CMD_REQUEST_CONFIGURATION_TEMPLATES_RESP"))
/*     */       {
/* 451 */         result = handleCommandRequestTemplatesResp(extra);
/*     */       }
/* 453 */       else if (name.equals("CMD_REQUEST_PARTICULAR_CONFIGURATION_RESP"))
/*     */       {
/* 455 */         result = handleCommandRequestParticularConfigurationResp(extra);
/*     */       }
/*     */     }
/*     */     catch (RobotCoreException e)
/*     */     {
/* 460 */       RobotLog.logStacktrace(e);
/*     */     }
/* 462 */     return result;
/*     */   }
/*     */   
/*     */   private CallbackResult handleCommandScanResp(String extra) throws RobotCoreException
/*     */   {
/* 467 */     Assert.assertTrue(this.remoteConfigure);
/* 468 */     this.usbScanManager.handleCommandScanResponse(extra);
/* 469 */     return CallbackResult.HANDLED_CONTINUE;
/*     */   }
/*     */   
/*     */ 
/*     */   public CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event)
/*     */   {
/* 475 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public CallbackResult packetReceived(RobocolDatagram packet)
/*     */   {
/* 481 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public CallbackResult peerDiscoveryEvent(RobocolDatagram packet)
/*     */   {
/* 487 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public CallbackResult heartbeatEvent(RobocolDatagram packet, long tReceived)
/*     */   {
/* 493 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public CallbackResult telemetryEvent(RobocolDatagram packet)
/*     */   {
/* 499 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public CallbackResult gamepadEvent(RobocolDatagram packet)
/*     */   {
/* 505 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public CallbackResult emptyEvent(RobocolDatagram packet)
/*     */   {
/* 511 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */ 
/*     */   public CallbackResult reportGlobalError(String error, boolean recoverable)
/*     */   {
/* 517 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   protected static abstract interface StringProcessor
/*     */   {
/*     */     public abstract void processString(String paramString);
/*     */   }
/*     */   
/*     */   protected static abstract interface TemplateProcessor
/*     */   {
/*     */     public abstract void processTemplate(RobotConfigFile paramRobotConfigFile, XmlPullParser paramXmlPullParser);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\ConfigureFromTemplateActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */