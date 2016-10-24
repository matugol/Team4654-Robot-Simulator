/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.AlertDialog;
/*     */ import android.app.AlertDialog.Builder;
/*     */ import android.content.DialogInterface;
/*     */ import android.content.DialogInterface.OnClickListener;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.Button;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.DbgLog;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.RobocolDatagram;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.Event;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection.NetworkConnectionCallback;
/*     */ import java.io.File;
/*     */ import java.text.Collator;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.ToastLocation;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable.RecvLoopCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcLoadFileActivity
/*     */   extends EditActivity
/*     */   implements RecvLoopRunnable.RecvLoopCallback, NetworkConnection.NetworkConnectionCallback
/*     */ {
/*     */   public static final String launchIntent = "com.qualcomm.ftccommon.configuration.FtcLoadFileActivity.intent.action.Launch";
/*     */   public static final String TAG = "FtcConfigTag";
/*     */   private static final boolean DEBUG = false;
/*  82 */   private List<RobotConfigFile> fileList = new CopyOnWriteArrayList();
/*  83 */   private NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  91 */     super.onCreate(savedInstanceState);
/*     */     
/*  93 */     RobotLog.vv("FtcConfigTag", "FtcLoadFileActivity started");
/*  94 */     setContentView(R.layout.activity_load);
/*     */     
/*  96 */     EditParameters parameters = EditParameters.fromIntent(this, getIntent());
/*  97 */     deserialize(parameters);
/*     */     
/*  99 */     buildInfoButtons();
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 104 */     super.onStart();
/*     */     
/* 106 */     if (!this.remoteConfigure) {
/* 107 */       this.robotConfigFileManager.createConfigFolder();
/*     */     }
/*     */     
/* 110 */     if (this.remoteConfigure)
/*     */     {
/* 112 */       this.networkConnectionHandler.pushNetworkConnectionCallback(this);
/* 113 */       this.networkConnectionHandler.pushReceiveLoopCallback(this);
/*     */     }
/*     */     
/* 116 */     if (!this.remoteConfigure) {
/* 117 */       this.fileList = this.robotConfigFileManager.getXMLFiles();
/* 118 */       warnIfNoFiles();
/*     */     }
/*     */     else {
/* 121 */       this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATIONS"));
/*     */     }
/* 123 */     populate();
/*     */   }
/*     */   
/*     */   protected CallbackResult handleCommandRequestConfigFilesResp(String extra) throws RobotCoreException
/*     */   {
/* 128 */     this.fileList = RobotConfigFileManager.deserializeXMLConfigList(extra);
/* 129 */     warnIfNoFiles();
/* 130 */     populate();
/* 131 */     return CallbackResult.HANDLED;
/*     */   }
/*     */   
/*     */   public void onResume()
/*     */   {
/* 136 */     super.onResume();
/*     */   }
/*     */   
/*     */   protected void onStop()
/*     */   {
/* 141 */     super.onStop();
/* 142 */     if (this.remoteConfigure) {
/* 143 */       this.networkConnectionHandler.removeNetworkConnectionCallback(this);
/* 144 */       this.networkConnectionHandler.removeReceiveLoopCallback(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void buildInfoButtons()
/*     */   {
/* 153 */     Button saveConfigButton = (Button)findViewById(R.id.files_holder).findViewById(R.id.info_btn);
/* 154 */     saveConfigButton.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 157 */         AlertDialog.Builder builder = FtcLoadFileActivity.this.utility.buildBuilder(FtcLoadFileActivity.this.getString(R.string.availableConfigListCaption), FtcLoadFileActivity.this.getString(R.string.availableConfigsInfoMessage));
/* 158 */         builder.setPositiveButton(FtcLoadFileActivity.this.getString(R.string.buttonNameOK), FtcLoadFileActivity.this.doNothingAndCloseListener);
/* 159 */         AlertDialog alert = builder.create();
/* 160 */         alert.show();
/* 161 */         TextView textView = (TextView)alert.findViewById(16908299);
/* 162 */         textView.setTextSize(14.0F);
/*     */       }
/*     */       
/* 165 */     });
/* 166 */     Button configFromTemplateButton = (Button)findViewById(R.id.configureFromTemplateArea).findViewById(R.id.info_btn);
/* 167 */     configFromTemplateButton.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 170 */         AlertDialog.Builder builder = FtcLoadFileActivity.this.utility.buildBuilder(FtcLoadFileActivity.this.getString(R.string.configFromTemplateInfoTitle), FtcLoadFileActivity.this.getString(R.string.configFromTemplateInfoMessage));
/* 171 */         builder.setPositiveButton(FtcLoadFileActivity.this.getString(R.string.buttonNameOK), FtcLoadFileActivity.this.doNothingAndCloseListener);
/* 172 */         AlertDialog alert = builder.create();
/* 173 */         alert.show();
/* 174 */         TextView textView = (TextView)alert.findViewById(16908299);
/* 175 */         textView.setTextSize(14.0F);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/* 180 */   DialogInterface.OnClickListener doNothingAndCloseListener = new DialogInterface.OnClickListener()
/*     */   {
/*     */     public void onClick(DialogInterface dialog, int button) {}
/*     */   };
/*     */   
/*     */   private void warnIfNoFiles()
/*     */   {
/* 187 */     if (this.fileList.size() == 0) {
/* 188 */       final String msg0 = getString(R.string.noFilesFoundTitle);
/* 189 */       final String msg1 = getString(R.string.noFilesFoundMessage);
/* 190 */       runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/* 193 */           FtcLoadFileActivity.this.utility.setOrangeText(msg0, msg1, R.id.empty_filelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */         }
/*     */       });
/*     */     } else {
/* 197 */       runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/* 200 */           ViewGroup empty_filelist = (ViewGroup)FtcLoadFileActivity.this.findViewById(R.id.empty_filelist);
/* 201 */           empty_filelist.removeAllViews();
/* 202 */           empty_filelist.setVisibility(8);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private void populate() {
/* 209 */     runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 212 */         View readOnlyExplanation = FtcLoadFileActivity.this.findViewById(R.id.readOnlyExplanation);
/* 213 */         readOnlyExplanation.setVisibility(8);
/*     */         
/* 215 */         ViewGroup inclusionViewGroup = (ViewGroup)FtcLoadFileActivity.this.findViewById(R.id.inclusionlayout);
/* 216 */         inclusionViewGroup.removeAllViews();
/*     */         
/* 218 */         final Collator coll = Collator.getInstance();
/* 219 */         coll.setStrength(0);
/* 220 */         Collections.sort(FtcLoadFileActivity.this.fileList, new Comparator() {
/*     */           public int compare(RobotConfigFile lhs, RobotConfigFile rhs) {
/* 222 */             return coll.compare(lhs.getName(), rhs.getName());
/*     */           }
/*     */         });
/*     */         
/* 226 */         for (RobotConfigFile file : FtcLoadFileActivity.this.fileList) {
/* 227 */           View child = LayoutInflater.from(FtcLoadFileActivity.this.context).inflate(R.layout.file_info, null);
/* 228 */           inclusionViewGroup.addView(child);
/*     */           
/*     */ 
/* 231 */           if (file.isReadOnly()) {
/* 232 */             Button deleteButton = (Button)child.findViewById(R.id.file_delete_button);
/* 233 */             deleteButton.setEnabled(false);
/* 234 */             deleteButton.setClickable(false);
/* 235 */             readOnlyExplanation.setVisibility(0);
/*     */           }
/*     */           
/* 238 */           TextView name = (TextView)child.findViewById(R.id.filename_editText);
/* 239 */           name.setText(file.getName());
/* 240 */           name.setTag(file);
/*     */           
/* 242 */           child.findViewById(R.id.configIsReadOnlyFeedback).setVisibility(file.isReadOnly() ? 0 : 8);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onActivityResult(int requestCodeValue, int resultCode, Intent data)
/*     */   {
/* 254 */     logActivityResult(requestCodeValue, resultCode, data);
/*     */     
/* 256 */     this.currentCfgFile = this.robotConfigFileManager.getActiveConfigAndUpdateUI();
/*     */   }
/*     */   
/*     */   public void onNewButtonPressed(View v) {
/* 260 */     RobotConfigFile file = RobotConfigFile.noConfig(this.robotConfigFileManager);
/* 261 */     this.robotConfigFileManager.setActiveConfigAndUpdateUI(this.remoteConfigure, file);
/* 262 */     Intent intent = makeEditConfigIntent(FtcNewFileActivity.class, null);
/* 263 */     startActivityForResult(intent, FtcNewFileActivity.requestCode.value);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onFileEditButtonPressed(View v)
/*     */   {
/* 269 */     RobotConfigFile file = getFile(v);
/* 270 */     this.robotConfigFileManager.setActiveConfig(this.remoteConfigure, file);
/* 271 */     Intent intent = makeEditConfigIntent(FtcConfigurationActivity.class, file);
/* 272 */     startActivityForResult(intent, FtcConfigurationActivity.requestCode.value);
/*     */   }
/*     */   
/*     */   public void onConfigureFromTemplatePressed(View v) {
/* 276 */     Intent intent = makeEditConfigIntent(ConfigureFromTemplateActivity.class, null);
/* 277 */     startActivityForResult(intent, ConfigureFromTemplateActivity.requestCode.value);
/*     */   }
/*     */   
/*     */   Intent makeEditConfigIntent(Class clazz, @Nullable RobotConfigFile configFile) {
/* 281 */     EditParameters parameters = new EditParameters(this);
/* 282 */     parameters.setExtantRobotConfigurations(this.fileList);
/*     */     
/* 284 */     if (configFile != null) parameters.setCurrentCfgFile(configFile);
/* 285 */     Intent intent = new Intent(this.context, clazz);
/* 286 */     parameters.putIntent(intent);
/* 287 */     return intent;
/*     */   }
/*     */   
/*     */   public void onFileActivateButtonPressed(View v) {
/* 291 */     RobotConfigFile file = getFile(v);
/* 292 */     this.robotConfigFileManager.setActiveConfigAndUpdateUI(this.remoteConfigure, file);
/*     */     
/* 294 */     if (this.remoteConfigure)
/*     */     {
/* 296 */       this.networkConnectionHandler.sendCommand(new Command("CMD_ACTIVATE_CONFIGURATION", file.toString()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void onFileDeleteButtonPressed(View v)
/*     */   {
/* 302 */     final RobotConfigFile robotConfigFile = getFile(v);
/* 303 */     if (robotConfigFile.getLocation() == RobotConfigFile.FileLocation.LOCAL_STORAGE) {
/* 304 */       AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.confirmConfigDeleteTitle), getString(R.string.confirmConfigDeleteMessage));
/* 305 */       DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
/*     */         public void onClick(DialogInterface dialog, int button) {
/* 307 */           FtcLoadFileActivity.this.doDeleteConfiguration(robotConfigFile);
/*     */         }
/* 309 */       };
/* 310 */       builder.setPositiveButton(R.string.buttonNameOK, okListener);
/* 311 */       builder.setNegativeButton(R.string.buttonNameCancel, this.doNothingAndCloseListener);
/* 312 */       builder.show();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void doDeleteConfiguration(RobotConfigFile robotConfigFile)
/*     */   {
/* 322 */     if (this.remoteConfigure) {
/* 323 */       if (robotConfigFile.getLocation() == RobotConfigFile.FileLocation.LOCAL_STORAGE) {
/* 324 */         this.networkConnectionHandler.sendCommand(new Command("CMD_DELETE_CONFIGURATION", robotConfigFile.toString()));
/* 325 */         this.fileList.remove(robotConfigFile);
/* 326 */         populate();
/*     */       }
/*     */       
/* 329 */       this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATIONS"));
/*     */     }
/*     */     else {
/* 332 */       if (robotConfigFile.getLocation() == RobotConfigFile.FileLocation.LOCAL_STORAGE) {
/* 333 */         File file = robotConfigFile.getFullPath();
/* 334 */         if (!file.delete())
/*     */         {
/*     */ 
/* 337 */           String filenameWExt = file.getName();
/* 338 */           this.appUtil.showToast(ToastLocation.ONLY_LOCAL, this.context, String.format(getString(R.string.configToDeleteDoesNotExist), new Object[] { filenameWExt }));
/* 339 */           DbgLog.error("Tried to delete a file that does not exist: " + filenameWExt);
/*     */         }
/*     */       }
/* 342 */       this.fileList = this.robotConfigFileManager.getXMLFiles();
/* 343 */       populate();
/*     */     }
/*     */     
/* 346 */     RobotConfigFile cfgFile = RobotConfigFile.noConfig(this.robotConfigFileManager);
/* 347 */     this.robotConfigFileManager.setActiveConfigAndUpdateUI(this.remoteConfigure, cfgFile);
/*     */   }
/*     */   
/*     */   private RobotConfigFile getFile(View v) {
/* 351 */     LinearLayout horizontalButtons = (LinearLayout)v.getParent();
/* 352 */     LinearLayout linearLayout = (LinearLayout)horizontalButtons.getParent();
/* 353 */     TextView name = (TextView)linearLayout.findViewById(R.id.filename_editText);
/* 354 */     return (RobotConfigFile)name.getTag();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onBackPressed()
/*     */   {
/* 361 */     logBackPressed();
/* 362 */     finishOk();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallbackResult commandEvent(Command command)
/*     */   {
/* 371 */     CallbackResult result = CallbackResult.NOT_HANDLED;
/*     */     try {
/* 373 */       String name = command.getName();
/* 374 */       String extra = command.getExtra();
/*     */       
/* 376 */       if (name.equals("CMD_REQUEST_CONFIGURATIONS_RESP")) {
/* 377 */         result = handleCommandRequestConfigFilesResp(extra);
/*     */       }
/*     */     } catch (RobotCoreException e) {
/* 380 */       RobotLog.logStacktrace(e);
/*     */     }
/* 382 */     return result;
/*     */   }
/*     */   
/*     */   public CallbackResult onNetworkConnectionEvent(NetworkConnection.Event event)
/*     */   {
/* 387 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult packetReceived(RobocolDatagram packet)
/*     */   {
/* 392 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult peerDiscoveryEvent(RobocolDatagram packet)
/*     */   {
/* 397 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult heartbeatEvent(RobocolDatagram packet, long tReceived)
/*     */   {
/* 402 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult telemetryEvent(RobocolDatagram packet)
/*     */   {
/* 407 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult gamepadEvent(RobocolDatagram packet)
/*     */   {
/* 412 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult emptyEvent(RobocolDatagram packet)
/*     */   {
/* 417 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */   
/*     */   public CallbackResult reportGlobalError(String error, boolean recoverable)
/*     */   {
/* 422 */     return CallbackResult.NOT_HANDLED;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\FtcLoadFileActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */