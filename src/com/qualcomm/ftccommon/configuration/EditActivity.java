/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.ComponentName;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.preference.PreferenceManager;
/*     */ import android.support.annotation.IdRes;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.text.Editable;
/*     */ import android.text.TextWatcher;
/*     */ import android.view.View;
/*     */ import android.view.ViewParent;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemSelectedListener;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.EditText;
/*     */ import android.widget.Spinner;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.ftccommon.R.xml;
/*     */ import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.robocol.PeerApp;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class EditActivity
/*     */   extends Activity
/*     */ {
/*     */   public static final String TAG = "EditActivity";
/*     */   protected Context context;
/*  79 */   protected AppUtil appUtil = AppUtil.getInstance();
/*  80 */   protected boolean remoteConfigure = AppUtil.getInstance().getThisApp().isDriverStation();
/*     */   protected Utility utility;
/*     */   protected RobotConfigFileManager robotConfigFileManager;
/*     */   protected RobotConfigFile currentCfgFile; @IdRes
/*  84 */   protected int idAddButton = R.id.addButton; @IdRes
/*  85 */   protected int idFixButton = R.id.fixButton; @IdRes
/*  86 */   protected int idSwapButton = R.id.swapButton;
/*     */   protected ControllerConfiguration controllerConfiguration;
/*  88 */   protected RobotConfigMap robotConfigMap = new RobotConfigMap();
/*  89 */   protected boolean haveRobotConfigMapParameter = false; @NonNull
/*  90 */   protected ScannedDevices scannedDevices = new ScannedDevices();
/*  91 */   protected List<RobotConfigFile> extantRobotConfigurations = new LinkedList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/* 103 */     super.onCreate(savedInstanceState);
/* 104 */     this.context = this;
/* 105 */     RobotLog.writeLogcatToDisk(this, 1024);
/* 106 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/* 107 */     this.utility = new Utility(this);
/* 108 */     this.robotConfigFileManager = new RobotConfigFileManager(this);
/* 109 */     this.currentCfgFile = this.robotConfigFileManager.getActiveConfig();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/* 115 */     super.onStart();
/* 116 */     this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
/*     */   }
/*     */   
/*     */   protected void deserialize(EditParameters parameters)
/*     */   {
/* 121 */     this.scannedDevices = parameters.getScannedDevices();
/* 122 */     this.extantRobotConfigurations = parameters.getExtantRobotConfigurations();
/* 123 */     this.controllerConfiguration = ((parameters.getConfiguration() instanceof ControllerConfiguration) ? (ControllerConfiguration)parameters.getConfiguration() : null);
/*     */     
/* 125 */     if (parameters.getCurrentCfgFile() != null) this.currentCfgFile = parameters.getCurrentCfgFile();
/* 126 */     deserializeConfigMap(parameters);
/*     */   }
/*     */   
/*     */   protected void deserializeConfigMap(EditParameters parameters)
/*     */   {
/* 131 */     this.robotConfigMap = new RobotConfigMap(parameters.getRobotConfigMap());
/* 132 */     this.haveRobotConfigMapParameter = parameters.haveRobotConfigMapParameter();
/*     */     
/*     */ 
/* 135 */     if ((this.robotConfigMap != null) && (this.controllerConfiguration != null))
/*     */     {
/* 137 */       if (this.robotConfigMap.contains(this.controllerConfiguration.getSerialNumber()))
/*     */       {
/* 139 */         this.controllerConfiguration = this.robotConfigMap.get(this.controllerConfiguration.getSerialNumber());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RobotConfigMap getRobotConfigMap()
/*     */   {
/* 150 */     return this.robotConfigMap == null ? new RobotConfigMap() : this.robotConfigMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleLaunchEdit(RequestCode requestCode, Class clazz, List<DeviceConfiguration> currentItems)
/*     */   {
/* 159 */     handleLaunchEdit(requestCode, clazz, new EditParameters(this, currentItems));
/*     */   }
/*     */   
/*     */   protected void handleLaunchEdit(RequestCode requestCode, Class clazz, DeviceConfiguration config) {
/* 163 */     handleLaunchEdit(requestCode, clazz, new EditParameters(this, config));
/*     */   }
/*     */   
/*     */   protected void handleLaunchEdit(RequestCode requestCode, Class clazz, EditParameters parameters)
/*     */   {
/* 168 */     handleLaunchEdit(requestCode, clazz, parameters.toBundle());
/*     */   }
/*     */   
/*     */   private void handleLaunchEdit(RequestCode requestCode, Class clazz, Bundle bundle) {
/* 172 */     Intent editIntent = new Intent(this.context, clazz);
/* 173 */     editIntent.putExtras(bundle);
/* 174 */     setResult(-1, editIntent);
/* 175 */     RobotLog.v("%s: starting activity %s code=%d", new Object[] { getClass().getSimpleName(), editIntent.getComponent().getShortClassName(), Integer.valueOf(requestCode.value) });
/* 176 */     startActivityForResult(editIntent, requestCode.value);
/*     */   }
/*     */   
/*     */   public static String formatSerialNumber(Context context, ControllerConfiguration controllerConfiguration)
/*     */   {
/* 181 */     String result = controllerConfiguration.getSerialNumber().toString(context);
/* 182 */     if (controllerConfiguration.getSerialNumber().isFake())
/*     */     {
/* 184 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 188 */     if (!controllerConfiguration.isKnownToBeAttached())
/*     */     {
/* 190 */       result = result + context.getString(R.string.serialNumberNotAttached);
/*     */     }
/* 192 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void finishCancel()
/*     */   {
/* 198 */     RobotLog.v("%s: cancelling", new Object[] { getClass().getSimpleName() });
/* 199 */     setResult(0, new Intent());
/* 200 */     finish();
/*     */   }
/*     */   
/*     */   protected void finishOk(EditParameters parameters) {
/* 204 */     RobotLog.v("%s: OK", new Object[] { getClass().getSimpleName() });
/* 205 */     Intent returnIntent = new Intent();
/* 206 */     parameters.putIntent(returnIntent);
/* 207 */     finishOk(returnIntent);
/*     */   }
/*     */   
/*     */   protected void finishOk() {
/* 211 */     finishOk(new Intent());
/*     */   }
/*     */   
/*     */   protected void finishOk(Intent intent) {
/* 215 */     setResult(-1, intent);
/* 216 */     finish();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onBackPressed()
/*     */   {
/* 222 */     logBackPressed();
/* 223 */     finishOk();
/*     */   }
/*     */   
/*     */   protected void logActivityResult(int requestCodeValue, int resultCode, Intent data)
/*     */   {
/* 228 */     RobotLog.v("%s: activity result: code=%d result=%d", new Object[] { getClass().getSimpleName(), Integer.valueOf(requestCodeValue), Integer.valueOf(resultCode) });
/*     */   }
/*     */   
/*     */   protected void logBackPressed() {
/* 232 */     RobotLog.v("%s: backPressed received", new Object[] { getClass().getSimpleName() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected class SetNameTextWatcher
/*     */     implements TextWatcher
/*     */   {
/*     */     private final DeviceConfiguration deviceConfiguration;
/*     */     
/*     */ 
/*     */     protected SetNameTextWatcher(DeviceConfiguration deviceConfiguration)
/*     */     {
/* 245 */       this.deviceConfiguration = deviceConfiguration;
/*     */     }
/*     */     
/*     */ 
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void afterTextChanged(Editable editable)
/*     */     {
/* 255 */       String text = editable.toString();
/* 256 */       this.deviceConfiguration.setName(text);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class DisplayNameAndRequestCode
/*     */     implements Comparable<DisplayNameAndRequestCode>
/*     */   {
/*     */     public final String displayName;
/*     */     
/*     */     public final RequestCode requestCode;
/*     */     
/*     */ 
/*     */     public DisplayNameAndRequestCode(String combined)
/*     */     {
/* 272 */       String[] parts = combined.split("\\|");
/* 273 */       this.displayName = parts[0];
/* 274 */       this.requestCode = RequestCode.fromString(parts[1]);
/*     */     }
/*     */     
/*     */     public DisplayNameAndRequestCode(String displayName, RequestCode requestCode)
/*     */     {
/* 279 */       this.displayName = displayName;
/* 280 */       this.requestCode = requestCode;
/*     */     }
/*     */     
/*     */     public static DisplayNameAndRequestCode[] fromArray(String[] strings)
/*     */     {
/* 285 */       DisplayNameAndRequestCode[] result = new DisplayNameAndRequestCode[strings.length];
/* 286 */       for (int i = 0; i < result.length; i++)
/*     */       {
/* 288 */         result[i] = new DisplayNameAndRequestCode(strings[i]);
/*     */       }
/* 290 */       return result;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 295 */       return this.displayName;
/*     */     }
/*     */     
/*     */     public int compareTo(DisplayNameAndRequestCode another)
/*     */     {
/* 300 */       return this.displayName.compareTo(another.displayName);
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
/*     */   protected void clearNameIfNecessary(EditText nameText, DeviceConfiguration device)
/*     */   {
/* 317 */     if (!device.isEnabled())
/*     */     {
/* 319 */       nameText.setText("");
/* 320 */       device.setName("");
/*     */     }
/*     */     else
/*     */     {
/* 324 */       nameText.setText(device.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public String disabledDeviceName()
/*     */   {
/* 330 */     return getString(R.string.noDeviceAttached);
/*     */   }
/*     */   
/*     */   public String nameOf(DeviceConfiguration config)
/*     */   {
/* 335 */     return nameOf(config.getName());
/*     */   }
/*     */   
/*     */   public String nameOf(String name)
/*     */   {
/* 340 */     if (name.equals("NO$DEVICE$ATTACHED"))
/*     */     {
/* 342 */       name = getString(R.string.noDeviceAttached);
/*     */     }
/* 344 */     return name;
/*     */   }
/*     */   
/*     */   public String displayNameOfConfigurationType(ConfigurationType type)
/*     */   {
/* 349 */     return type.getDisplayName(this);
/*     */   }
/*     */   
/*     */   protected class ConfigurationTypeAndDisplayName
/*     */     implements Comparable<ConfigurationTypeAndDisplayName>
/*     */   {
/*     */     public final ConfigurationType configurationType;
/*     */     public final String displayName;
/*     */     
/*     */     public ConfigurationTypeAndDisplayName(ConfigurationType configurationType)
/*     */     {
/* 360 */       this.configurationType = configurationType;
/* 361 */       this.displayName = EditActivity.this.displayNameOfConfigurationType(configurationType);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 366 */       return this.displayName;
/*     */     }
/*     */     
/*     */     public int compareTo(ConfigurationTypeAndDisplayName another)
/*     */     {
/* 371 */       return this.displayName.compareTo(another.displayName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void localizeConfigTypeSpinner(Spinner spinner)
/*     */   {
/* 382 */     ArrayAdapter<String> existingAdapter = (ArrayAdapter)spinner.getAdapter();
/* 383 */     List<String> strings = new ArrayList();
/* 384 */     for (int i = 0; i < existingAdapter.getCount(); i++)
/*     */     {
/* 386 */       strings.add(existingAdapter.getItem(i));
/*     */     }
/* 388 */     localizeConfigTypeSpinnerStrings(spinner, strings);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void localizeConfigTypeSpinnerStrings(Spinner spinner, List<String> strings)
/*     */   {
/* 394 */     List<ConfigurationType> types = new LinkedList();
/* 395 */     for (String string : strings)
/*     */     {
/* 397 */       types.add(BuiltInConfigurationType.fromString(string));
/*     */     }
/* 399 */     localizeConfigTypeSpinnerTypes(spinner, types);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void localizeConfigTypeSpinnerTypes(Spinner spinner, List<ConfigurationType> types)
/*     */   {
/* 405 */     ConfigurationTypeAndDisplayName[] pairs = new ConfigurationTypeAndDisplayName[types.size()];
/* 406 */     for (int i = 0; i < types.size(); i++)
/*     */     {
/* 408 */       ConfigurationType type = (ConfigurationType)types.get(i);
/* 409 */       pairs[i] = new ConfigurationTypeAndDisplayName(type);
/*     */     }
/*     */     
/*     */ 
/* 413 */     Arrays.sort(pairs);
/*     */     
/* 415 */     ArrayAdapter<ConfigurationTypeAndDisplayName> newAdapter = new ArrayAdapter(this, 17367049, pairs);
/*     */     
/* 417 */     spinner.setAdapter(newAdapter);
/*     */   }
/*     */   
/*     */ 
/*     */   protected int findPosition(Spinner spinner, ConfigurationType type)
/*     */   {
/* 423 */     ArrayAdapter<ConfigurationTypeAndDisplayName> adapter = (ArrayAdapter)spinner.getAdapter();
/* 424 */     for (int i = 0; i < adapter.getCount(); i++)
/*     */     {
/* 426 */       if (((ConfigurationTypeAndDisplayName)adapter.getItem(i)).configurationType == type)
/*     */       {
/* 428 */         return i;
/*     */       }
/*     */     }
/* 431 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   protected int findPosition(Spinner spinner, ConfigurationType typeA, ConfigurationType typeB)
/*     */   {
/* 437 */     int result = findPosition(spinner, typeA);
/* 438 */     if (result < 0) result = findPosition(spinner, typeB);
/* 439 */     return result;
/*     */   }
/*     */   
/*     */   protected void handleSpinner(View view, int spinnerId, DeviceConfiguration deviceConfiguration)
/*     */   {
/* 444 */     handleSpinner(view, spinnerId, deviceConfiguration, false);
/*     */   }
/*     */   
/*     */   protected void handleSpinner(View view, int spinnerId, DeviceConfiguration deviceConfiguration, boolean forceFind) {
/* 448 */     Spinner choiceSpinner = (Spinner)view.findViewById(spinnerId);
/* 449 */     if ((forceFind) || (deviceConfiguration.isEnabled()))
/*     */     {
/* 451 */       int spinnerPosition = findPosition(choiceSpinner, deviceConfiguration.getType(), BuiltInConfigurationType.NOTHING);
/* 452 */       choiceSpinner.setSelection(spinnerPosition);
/*     */     }
/*     */     else
/*     */     {
/* 456 */       choiceSpinner.setSelection(findPosition(choiceSpinner, BuiltInConfigurationType.NOTHING));
/*     */     }
/* 458 */     choiceSpinner.setOnItemSelectedListener(this.spinnerListener);
/*     */   }
/*     */   
/*     */ 
/* 462 */   protected AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener()
/*     */   {
/*     */ 
/*     */     public void onItemSelected(AdapterView<?> parent, View spinnerItem, int pos, long l)
/*     */     {
/* 467 */       EditActivity.ConfigurationTypeAndDisplayName selected = (EditActivity.ConfigurationTypeAndDisplayName)parent.getItemAtPosition(pos);
/* 468 */       View itemView = itemViewFromSpinnerItem(spinnerItem);
/* 469 */       if (selected.configurationType == BuiltInConfigurationType.NOTHING)
/*     */       {
/* 471 */         EditActivity.this.clearDevice(itemView);
/*     */       }
/*     */       else
/*     */       {
/* 475 */         EditActivity.this.changeDevice(itemView, selected.configurationType);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected View itemViewFromSpinnerItem(View spinnerItem)
/*     */     {
/* 485 */       ViewParent spinner = spinnerItem.getParent();
/* 486 */       ViewParent spinnerParent = spinner.getParent();
/* 487 */       ViewParent spinnerParentParent = spinnerParent.getParent();
/*     */       
/* 489 */       View itemView = (View)spinnerParentParent;
/* 490 */       return itemView;
/*     */     }
/*     */     
/*     */     public void onNothingSelected(AdapterView<?> adapterView) {}
/*     */   };
/*     */   
/*     */   protected void clearDevice(View itemView) {}
/*     */   
/*     */   protected void changeDevice(View itemView, ConfigurationType type) {}
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */