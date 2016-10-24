/*     */ package org.firstinspires.inspection;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.bluetooth.BluetoothAdapter;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.content.pm.ApplicationInfo;
/*     */ import android.content.pm.PackageInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.content.pm.PackageManager.NameNotFoundException;
/*     */ import android.graphics.Color;
/*     */ import android.net.NetworkInfo.DetailedState;
/*     */ import android.net.wifi.SupplicantState;
/*     */ import android.net.wifi.WifiConfiguration;
/*     */ import android.net.wifi.WifiInfo;
/*     */ import android.net.wifi.WifiManager;
/*     */ import android.net.wifi.p2p.WifiP2pDevice;
/*     */ import android.net.wifi.p2p.WifiP2pGroup;
/*     */ import android.net.wifi.p2p.WifiP2pManager;
/*     */ import android.net.wifi.p2p.WifiP2pManager.ActionListener;
/*     */ import android.net.wifi.p2p.WifiP2pManager.Channel;
/*     */ import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
/*     */ import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
/*     */ import android.os.Build;
/*     */ import android.os.Build.VERSION;
/*     */ import android.os.Bundle;
/*     */ import android.os.Handler;
/*     */ import android.provider.Settings.Global;
/*     */ import android.util.Log;
/*     */ import android.view.Menu;
/*     */ import android.view.MenuInflater;
/*     */ import android.view.MenuItem;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.ImageButton;
/*     */ import android.widget.TextView;
/*     */ import android.widget.Toast;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public abstract class InspectionActivity
/*     */   extends Activity
/*     */   implements View.OnClickListener, DeviceNameReceiver.OnDeviceNameReceivedListener
/*     */ {
/*  65 */   String rcApp = "com.qualcomm.ftcrobotcontroller"; String dsApp = "com.qualcomm.ftcdriverstation"; String ccApp = "com.zte.wifichanneleditor"; String widiNameString = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   Integer darkGreen = Integer.valueOf(Color.rgb(47, 151, 47));
/*  73 */   Integer yellow = Integer.valueOf(Color.rgb(178, 178, 0));
/*  74 */   Integer orange = Integer.valueOf(Color.rgb(255, 128, 0));
/*     */   TextView widiName;
/*     */   TextView widiConnected;
/*     */   TextView wifiEnabled;
/*     */   TextView batteryLevel;
/*     */   TextView osVersion;
/*     */   TextView airplaneMode;
/*     */   
/*     */   protected abstract Boolean validateAppsInstalled();
/*     */   
/*     */   protected abstract boolean channelChangerRequired();
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState) {
/*  87 */     super.onCreate(savedInstanceState);
/*  88 */     setContentView(R.layout.activity_inspection);
/*     */     
/*  90 */     this.refreshRunnable = new Runnable()
/*     */     {
/*     */       public void run() {
/*  93 */         InspectionActivity.this.refresh();
/*  94 */         InspectionActivity.this.handler.postDelayed(InspectionActivity.this.getRefreshRunnable(), 1000L);
/*  95 */         Log.d("Handler", "Boop.");
/*     */       }
/*     */       
/*  98 */     };
/*  99 */     this.txtIsRCInstalled = ((TextView)findViewById(R.id.txtIsRCInstalled));
/* 100 */     this.txtIsDSInstalled = ((TextView)findViewById(R.id.txtIsDSInstalled));
/* 101 */     this.txtIsCCInstalled = ((TextView)findViewById(R.id.txtIsCCInstalled));
/*     */     
/*     */ 
/* 104 */     this.widiName = ((TextView)findViewById(R.id.widiName));
/* 105 */     this.widiConnected = ((TextView)findViewById(R.id.widiConnected));
/* 106 */     this.wifiEnabled = ((TextView)findViewById(R.id.wifiEnabled));
/* 107 */     this.batteryLevel = ((TextView)findViewById(R.id.batteryLevel));
/* 108 */     this.osVersion = ((TextView)findViewById(R.id.osVersion));
/* 109 */     this.airplaneMode = ((TextView)findViewById(R.id.airplaneMode));
/* 110 */     this.bluetooth = ((TextView)findViewById(R.id.bluetoothEnabled));
/* 111 */     this.wifiConnected = ((TextView)findViewById(R.id.wifiConnected));
/* 112 */     this.appsStatus = ((TextView)findViewById(R.id.appsStatus));
/* 113 */     this.txtAppVersion = ((TextView)findViewById(R.id.textDeviceName));
/*     */     
/* 115 */     this.txtAppVersion.setText("FTC Inspect: 1.0");
/*     */     
/* 117 */     this.txtManufacturer = ((TextView)findViewById(R.id.txtManufacturer));
/* 118 */     this.txtModel = ((TextView)findViewById(R.id.txtModel));
/*     */     
/* 120 */     this.teamNoRegex = Pattern.compile("^\\d{1,5}(-\\w)?-(RC|DS)\\z", 2);
/*     */     
/* 122 */     ImageButton buttonMenu = (ImageButton)findViewById(R.id.menu_buttons);
/* 123 */     buttonMenu.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View v) {
/* 126 */         AppUtil.getInstance().openOptionsMenuFor(InspectionActivity.this);
/*     */       }
/* 128 */     });
/* 129 */     initReceiver();
/* 130 */     startReceivingWidiInfo();
/*     */     
/*     */ 
/* 133 */     refresh();
/*     */   }
/*     */   
/*     */   public boolean onCreateOptionsMenu(Menu menu)
/*     */   {
/* 138 */     MenuInflater inflater = getMenuInflater();
/* 139 */     inflater.inflate(R.menu.main_menu, menu);
/* 140 */     return true;
/*     */   }
/*     */   
/*     */   public boolean onOptionsItemSelected(MenuItem item)
/*     */   {
/* 145 */     int id = item.getItemId();
/*     */     
/* 147 */     if (id == R.id.clear_wifi) {
/* 148 */       return deleteAllWifi();
/*     */     }
/*     */     
/* 151 */     if (id == R.id.clear_widi) {
/* 152 */       deleteRememberedWiDi();
/* 153 */       Toast.makeText(getApplicationContext(), "Deleted remembered WifiDirect Connections!", 0).show();
/*     */       
/*     */ 
/* 156 */       return true;
/*     */     }
/*     */     
/* 159 */     if (id == R.id.disc_widi) {
/* 160 */       disconnectWiDi();
/* 161 */       return true;
/*     */     }
/*     */     
/* 164 */     return super.onOptionsItemSelected(item);
/*     */   }
/*     */   
/*     */   protected void onPause()
/*     */   {
/* 169 */     unregisterReceiver(this.mDeviceNameReceiver);
/* 170 */     super.onPause();
/*     */     
/* 172 */     if (this.handler != null) {
/* 173 */       Log.d("InspectionActivity", "Boop - removing callbacks...");
/*     */       
/* 175 */       this.handler.removeCallbacks(this.refreshRunnable);
/*     */       
/* 177 */       Log.d("InspectionActivity", "Boop - removed callbacks...");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onResume()
/*     */   {
/* 183 */     startReceivingWidiInfo();
/* 184 */     super.onResume();
/*     */     
/*     */ 
/* 187 */     this.handler = new Handler();
/* 188 */     this.handler.postDelayed(getRefreshRunnable(), 1000L);
/*     */   }
/*     */   
/*     */   private void refresh() {
/* 192 */     this.widiConnected.setText(getWiDiConnected().booleanValue() ? "✓" : "X");
/* 193 */     this.wifiEnabled.setText(getWiFiEnabled().booleanValue() ? "✓" : "X");
/* 194 */     this.osVersion.setText(Build.VERSION.RELEASE);
/* 195 */     this.airplaneMode.setText(getAirplaneMode().booleanValue() ? "✓" : "X");
/* 196 */     this.bluetooth.setText(getBluetooth().booleanValue() ? "On" : "Off");
/* 197 */     this.wifiConnected.setText(getWifiConnected().booleanValue() ? "Yes" : "No");
/* 198 */     this.widiName.setText(this.widiNameString);
/*     */     
/* 200 */     this.txtManufacturer.setText(Build.MANUFACTURER);
/* 201 */     this.txtModel.setText(Build.MODEL);
/*     */     
/* 203 */     this.widiConnected.setTextColor(getWiDiConnected().booleanValue() ? this.darkGreen.intValue() : -65536);
/* 204 */     this.wifiEnabled.setTextColor(getWiFiEnabled().booleanValue() ? this.darkGreen.intValue() : -65536);
/* 205 */     this.airplaneMode.setTextColor(getAirplaneMode().booleanValue() ? this.darkGreen.intValue() : -65536);
/* 206 */     this.bluetooth.setTextColor(!getBluetooth().booleanValue() ? this.darkGreen.intValue() : -65536);
/* 207 */     this.osVersion.setTextColor(validateVersion().booleanValue() ? this.darkGreen.intValue() : -65536);
/*     */     
/* 209 */     this.widiName.setTextColor(validateDeviceName().booleanValue() ? this.darkGreen.intValue() : -65536);
/*     */     
/* 211 */     this.wifiConnected.setTextColor(!getWifiConnected().booleanValue() ? this.darkGreen.intValue() : -65536);
/*     */     
/*     */ 
/* 214 */     Boolean appsOkay = Boolean.valueOf(true);
/*     */     
/*     */ 
/*     */ 
/* 218 */     if (("zte".equalsIgnoreCase(Build.MANUFACTURER)) && ("N9130".equalsIgnoreCase(Build.MODEL)) && (channelChangerRequired()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 224 */       if (packageExists(this.ccApp).booleanValue()) {
/* 225 */         this.txtIsCCInstalled.setText("✓");
/* 226 */         this.txtIsCCInstalled.setTextColor(this.darkGreen.intValue());
/*     */       } else {
/* 228 */         this.txtIsCCInstalled.setText("X");
/* 229 */         this.txtIsCCInstalled.setTextColor(-65536);
/* 230 */         appsOkay = Boolean.valueOf(false);
/*     */       }
/*     */     } else {
/* 233 */       this.txtIsCCInstalled.setText("N/A");
/* 234 */       this.txtIsCCInstalled.setTextColor(this.darkGreen.intValue());
/*     */     }
/*     */     
/*     */ 
/* 238 */     if (packageExists(this.rcApp).booleanValue())
/*     */     {
/* 240 */       this.txtIsRCInstalled.setText(getPackageInfo(this.rcApp).versionName);
/* 241 */       if (getPackageInfo(this.rcApp).versionCode < 9) {
/* 242 */         this.txtIsRCInstalled.setTextColor(this.orange.intValue());
/* 243 */         appsOkay = Boolean.valueOf(false);
/*     */       } else {
/* 245 */         this.txtIsRCInstalled.setTextColor(this.darkGreen.intValue());
/*     */       }
/*     */     } else {
/* 248 */       this.txtIsRCInstalled.setText("X");
/* 249 */       this.txtIsRCInstalled.setTextColor(this.darkGreen.intValue());
/*     */     }
/*     */     
/*     */ 
/* 253 */     if (packageExists(this.dsApp).booleanValue())
/*     */     {
/* 255 */       this.txtIsDSInstalled.setText(getPackageInfo(this.dsApp).versionName);
/* 256 */       if (getPackageInfo(this.dsApp).versionCode < 9) {
/* 257 */         this.txtIsDSInstalled.setTextColor(this.orange.intValue());
/* 258 */         appsOkay = Boolean.valueOf(false);
/*     */       } else {
/* 260 */         this.txtIsDSInstalled.setTextColor(this.darkGreen.intValue());
/*     */       }
/*     */     } else {
/* 263 */       this.txtIsDSInstalled.setText("X");
/* 264 */       this.txtIsDSInstalled.setTextColor(this.darkGreen.intValue());
/*     */     }
/*     */     
/* 267 */     if ((!packageExists(this.rcApp).booleanValue()) && (!packageExists(this.dsApp).booleanValue()))
/*     */     {
/* 269 */       appsOkay = Boolean.valueOf(false);
/* 270 */       this.txtIsDSInstalled.setTextColor(-65536);
/* 271 */       this.txtIsRCInstalled.setTextColor(-65536);
/*     */     }
/*     */     
/* 274 */     if ((packageExists(this.rcApp).booleanValue()) && (packageExists(this.dsApp).booleanValue()))
/*     */     {
/* 276 */       appsOkay = Boolean.valueOf(false);
/* 277 */       this.txtIsDSInstalled.setTextColor(-65536);
/* 278 */       this.txtIsRCInstalled.setTextColor(-65536);
/*     */     }
/*     */     
/*     */ 
/* 282 */     this.appsStatus.setTextColor(appsOkay.booleanValue() ? this.darkGreen.intValue() : -65536);
/* 283 */     this.appsStatus.setText(appsOkay.booleanValue() ? "✓" : "X");
/*     */     
/* 285 */     getBatteryInfo();
/*     */   }
/*     */   
/*     */   TextView bluetooth;
/*     */   TextView wifiConnected;
/*     */   TextView appsStatus;
/*     */   
/* 292 */   public void onDeviceNameReceived(String deviceName) { this.widiNameString = deviceName;
/* 293 */     refresh();
/*     */   }
/*     */   
/*     */   public Boolean getAirplaneMode() {
/* 297 */     return Boolean.valueOf(Settings.Global.getInt(getContentResolver(), "airplane_mode_on", 0) != 0);
/*     */   }
/*     */   
/*     */   public Boolean getWifiDeviceLockdown()
/*     */   {
/* 302 */     return Boolean.valueOf(Settings.Global.getInt(getContentResolver(), "wifi_device_owner_configs_lockdown", 0) != 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public Boolean getWifiConnected()
/*     */   {
/* 308 */     WifiManager m = (WifiManager)getSystemService("wifi");
/* 309 */     SupplicantState s = m.getConnectionInfo().getSupplicantState();
/* 310 */     NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(s);
/* 311 */     Log.v("getWifiConnected", state.toString());
/*     */     
/* 313 */     return Boolean.valueOf((state == NetworkInfo.DetailedState.CONNECTED) || (state == NetworkInfo.DetailedState.OBTAINING_IPADDR));
/*     */   }
/*     */   
/*     */   public Boolean getBluetooth()
/*     */   {
/* 318 */     BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
/* 319 */     if (mBluetoothAdapter == null) {
/* 320 */       return Boolean.valueOf(false);
/*     */     }
/* 322 */     if (!mBluetoothAdapter.isEnabled()) {
/* 323 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/* 327 */     return Boolean.valueOf(true);
/*     */   }
/*     */   
/*     */ 
/*     */   public Boolean validateVersion()
/*     */   {
/* 333 */     if (("zte".equalsIgnoreCase(Build.MANUFACTURER)) && ("N9130".equalsIgnoreCase(Build.MODEL)))
/*     */     {
/* 335 */       if (Build.VERSION.SDK_INT == 19) {
/* 336 */         return Boolean.valueOf(true);
/*     */       }
/* 338 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 341 */     if (Build.VERSION.SDK_INT >= 23)
/*     */     {
/* 343 */       return Boolean.valueOf(true);
/*     */     }
/* 345 */     return Boolean.valueOf(false);
/*     */   }
/*     */   
/*     */ 
/*     */   public Boolean validateDeviceName()
/*     */   {
/* 351 */     if ((this.widiNameString.contains("\n")) || (this.widiNameString.contains("\r"))) return Boolean.valueOf(false);
/* 352 */     return Boolean.valueOf(this.teamNoRegex.matcher(this.widiNameString).find());
/*     */   }
/*     */   
/*     */   public Boolean getWiFiEnabled() {
/* 356 */     WifiManager wifi = (WifiManager)getSystemService("wifi");
/* 357 */     return Boolean.valueOf(wifi.isWifiEnabled());
/*     */   }
/*     */   
/*     */   public Boolean getWiDiConnected() {
/* 361 */     return Boolean.valueOf(new WifiP2pDevice().status == 0);
/*     */   }
/*     */   
/*     */   private void initReceiver() {
/* 365 */     this.mDeviceNameReceiver = new DeviceNameReceiver();
/* 366 */     this.mDeviceNameReceiver.setOnDeviceNameReceivedListener(this);
/* 367 */     this.filter = new IntentFilter("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
/*     */   }
/*     */   
/*     */   private void startReceivingWidiInfo() {
/* 371 */     registerReceiver(this.mDeviceNameReceiver, this.filter);
/*     */   }
/*     */   
/*     */   private void getBatteryInfo()
/*     */   {
/* 376 */     IntentFilter ifilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
/* 377 */     Intent batteryStatus = registerReceiver(null, ifilter);
/*     */     
/* 379 */     int level = batteryStatus.getIntExtra("level", -1);
/* 380 */     int scale = batteryStatus.getIntExtra("scale", -1);
/*     */     
/* 382 */     float batteryPct = level / scale;
/*     */     
/* 384 */     this.batteryLevel.setText(Math.round(batteryPct * 100.0F) + "%");
/* 385 */     this.batteryLevel.setTextColor((batteryPct > 0.6D ? this.darkGreen : this.orange).intValue());
/*     */   }
/*     */   
/*     */   private Runnable getRefreshRunnable() {
/* 389 */     return this.refreshRunnable;
/*     */   }
/*     */   
/*     */   public Boolean packageExists(String targetPackage) {
/* 393 */     return Boolean.valueOf(!packageCode(targetPackage).equals("na"));
/*     */   }
/*     */   
/*     */   public String packageCode(String targetPackage) {
/* 397 */     PackageManager pm = getPackageManager();
/*     */     try {
/* 399 */       return pm.getPackageInfo(targetPackage, 128).versionName;
/*     */     } catch (PackageManager.NameNotFoundException e) {}
/* 401 */     return "na"; }
/*     */   
/*     */   TextView txtManufacturer;
/*     */   TextView txtModel;
/*     */   TextView txtAppVersion;
/* 406 */   protected Boolean appInventorExists() { return Boolean.valueOf((!findAppStudioApps().equals("na")) && (!findAppStudioApps().equals("duplicate"))); }
/*     */   
/*     */   TextView txtIsRCInstalled;
/*     */   TextView txtIsDSInstalled;
/*     */   TextView txtIsCCInstalled;
/*     */   
/*     */   private String findAppStudioApps()
/*     */   {
/* 414 */     String strval = "na";
/*     */     
/* 416 */     PackageManager pm = getPackageManager();
/* 417 */     List<ApplicationInfo> installedApps = pm.getInstalledApplications(128);
/*     */     
/* 419 */     if (installedApps != null) {
/* 420 */       for (ApplicationInfo app : installedApps) {
/* 421 */         if (app.packageName.startsWith("appinventor.ai_"))
/*     */         {
/* 423 */           if (strval.equals("na")) {
/* 424 */             strval = getPackageInfo(app.packageName).versionName;
/*     */           } else {
/* 426 */             strval = "Duplicate";
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 432 */     return strval; }
/*     */   
/*     */   DeviceNameReceiver mDeviceNameReceiver;
/*     */   
/* 436 */   public PackageInfo getPackageInfo(String targetPackage) { PackageManager pm = getPackageManager();
/*     */     try {
/* 438 */       return pm.getPackageInfo(targetPackage, 128);
/*     */     } catch (PackageManager.NameNotFoundException e) {}
/* 440 */     return null;
/*     */   }
/*     */   
/*     */   Pattern teamNoRegex;
/*     */   Handler handler;
/*     */   
/* 446 */   public void onClick(View view) { int id = view.getId(); }
/*     */   
/*     */   Runnable refreshRunnable;
/*     */   IntentFilter filter;
/*     */   static final String STR_ZTE = "zte";
/*     */   
/* 452 */   private boolean deleteAllWifi() { if (Build.VERSION.SDK_INT >= 23) {
/* 453 */       Toast.makeText(getApplicationContext(), "Cannot delete networks! This feature is not available with Android M or higher.", 1).show();
/*     */       
/*     */ 
/* 456 */       return false;
/*     */     }
/*     */     
/* 459 */     boolean bError = false;
/* 460 */     WifiManager mainWifiObj = (WifiManager)getSystemService("wifi");
/* 461 */     List<WifiConfiguration> list = mainWifiObj.getConfiguredNetworks();
/* 462 */     for (WifiConfiguration i : list) {
/* 463 */       if (mainWifiObj.removeNetwork(i.networkId)) {
/* 464 */         Log.d("TIENG", String.format("removeNetwork successful for %s", new Object[] { i.SSID }));
/*     */       } else {
/* 466 */         Log.d("TIENG", String.format("removeNetwork failed for %s", new Object[] { i.SSID }));
/*     */       }
/* 468 */       if (mainWifiObj.saveConfiguration()) {
/* 469 */         Log.d("TIENG", String.format("saveConfiguration successful for %s", new Object[] { i.SSID }));
/*     */       } else {
/* 471 */         Log.d("TIENG", String.format("saveConfiguration FAILED for %s", new Object[] { i.SSID }));
/* 472 */         bError = true;
/*     */       }
/*     */     }
/* 475 */     if (bError)
/*     */     {
/* 477 */       Toast.makeText(getApplicationContext(), "An error occurred while deleting one or more Wifi Networks", 1).show();
/*     */       
/* 479 */       return false;
/*     */     }
/* 481 */     Toast.makeText(getApplicationContext(), "Deleted remembered Wifi Networks!", 1).show();
/*     */     
/* 483 */     return true; }
/*     */   
/*     */   static final String STR_ZTESPEED = "N9130";
/*     */   static final int RC_MIN_VERSIONCODE = 9;
/*     */   static final int DS_MIN_VERSIONCODE = 9;
/* 488 */   private void deleteRememberedWiDi() { WifiP2pManager wifiP2pManagerObj = (WifiP2pManager)getSystemService("wifip2p");
/* 489 */     Context context = getApplicationContext();
/* 490 */     WifiP2pManager.Channel channel = wifiP2pManagerObj.initialize(context, context.getMainLooper(), new WifiP2pManager.ChannelListener()
/*     */     {
/*     */       public void onChannelDisconnected() {
/* 493 */         Log.d("WIFIDIRECT", "Channel disconnected!");
/*     */       }
/*     */     });
/*     */     try
/*     */     {
/* 498 */       Method[] methods = WifiP2pManager.class.getMethods();
/* 499 */       for (int i = 0; i < methods.length; i++) {
/* 500 */         if (methods[i].getName().equals("deletePersistentGroup"))
/*     */         {
/* 502 */           for (int netid = 0; netid < 32; netid++) {
/* 503 */             methods[i].invoke(wifiP2pManagerObj, new Object[] { channel, Integer.valueOf(netid), null });
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 508 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void disconnectWiDi() {
/* 513 */     final WifiP2pManager wifiP2pManagerObj = (WifiP2pManager)getSystemService("wifip2p");
/* 514 */     Context context = getApplicationContext();
/* 515 */     final WifiP2pManager.Channel mChannel = wifiP2pManagerObj.initialize(context, context.getMainLooper(), new WifiP2pManager.ChannelListener()
/*     */     {
/*     */       public void onChannelDisconnected() {
/* 518 */         Log.d("WIFIDIRECT", "Channel disconnected!");
/*     */       }
/*     */     });
/*     */     
/* 522 */     if (mChannel != null) {
/* 523 */       wifiP2pManagerObj.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener()
/*     */       {
/*     */         public void onGroupInfoAvailable(WifiP2pGroup group) {
/* 526 */           if ((group != null) && (group.isGroupOwner())) {
/* 527 */             wifiP2pManagerObj.removeGroup(mChannel, new WifiP2pManager.ActionListener()
/*     */             {
/*     */               public void onSuccess() {
/* 530 */                 Log.d("WIFIDIRECT", "Current WifiDirect Connection Removed");
/* 531 */                 Toast.makeText(InspectionActivity.this, "Successfully disconnected from WiFi Direct", 0).show();
/*     */               }
/*     */               
/*     */ 
/*     */               public void onFailure(int reason)
/*     */               {
/* 537 */                 Log.d("WIFIDIRECT", "Current WifiDirect Connection Removal Failed - " + reason);
/* 538 */                 Toast.makeText(InspectionActivity.this, "There was an error disconnecting from WiFi Direct!", 0).show();
/*     */               }
/*     */             });
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Inspection-release.jar!\classes.jar!\org\firstinspires\inspection\InspectionActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */