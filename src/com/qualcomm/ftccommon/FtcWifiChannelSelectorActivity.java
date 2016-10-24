/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.ProgressDialog;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.net.wifi.WifiManager;
/*     */ import android.net.wifi.p2p.WifiP2pManager;
/*     */ import android.net.wifi.p2p.WifiP2pManager.ActionListener;
/*     */ import android.net.wifi.p2p.WifiP2pManager.Channel;
/*     */ import android.os.Build;
/*     */ import android.os.Bundle;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemSelectedListener;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.Button;
/*     */ import android.widget.Spinner;
/*     */ import com.qualcomm.wirelessp2p.WifiDirectChannelSelection;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.ToastLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcWifiChannelSelectorActivity
/*     */   extends Activity
/*     */   implements AdapterView.OnItemSelectedListener, View.OnClickListener
/*     */ {
/*  62 */   private AppUtil appUtil = AppUtil.getInstance();
/*     */   private static final int INVALID = -1;
/*  64 */   private static int spinnerSelection = 0;
/*     */   
/*     */   private Button buttonConfigure;
/*     */   
/*     */   private Button buttonWifiSettings;
/*     */   
/*     */   private Spinner spinner;
/*     */   
/*     */   private ProgressDialog progressDialog;
/*     */   private WifiDirectChannelSelection wifiConfig;
/*  74 */   private int wifi_direct_class = -1;
/*  75 */   private int wifi_direct_channel = -1;
/*     */   
/*  77 */   private boolean isMotorola = false;
/*     */   
/*     */   private WifiP2pManager mManager;
/*     */   private WifiP2pManager.Channel mChannel;
/*     */   private Context context;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  85 */     super.onCreate(savedInstanceState);
/*  86 */     setContentView(R.layout.activity_ftc_wifi_channel_selector);
/*     */     
/*  88 */     this.context = this;
/*     */     
/*  90 */     this.spinner = ((Spinner)findViewById(R.id.spinnerChannelSelect));
/*  91 */     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.wifi_direct_channels, 17367048);
/*     */     
/*     */ 
/*  94 */     adapter.setDropDownViewResource(17367049);
/*  95 */     this.spinner.setAdapter(adapter);
/*  96 */     this.spinner.setOnItemSelectedListener(this);
/*     */     
/*  98 */     this.buttonConfigure = ((Button)findViewById(R.id.buttonConfigure));
/*  99 */     this.buttonConfigure.setOnClickListener(this);
/*     */     
/* 101 */     this.buttonWifiSettings = ((Button)findViewById(R.id.buttonWifiSettings));
/* 102 */     this.buttonWifiSettings.setOnClickListener(this);
/*     */     
/* 104 */     WifiManager wifiManager = (WifiManager)getSystemService("wifi");
/* 105 */     this.wifiConfig = new WifiDirectChannelSelection(this, wifiManager);
/*     */     
/*     */ 
/*     */ 
/* 109 */     if (new String("motorola").equalsIgnoreCase(Build.MANUFACTURER)) {
/* 110 */       this.isMotorola = true;
/*     */       
/*     */ 
/* 113 */       this.mManager = ((WifiP2pManager)getSystemService("wifip2p"));
/*     */       
/*     */ 
/* 116 */       this.mChannel = this.mManager.initialize(this, getMainLooper(), null);
/*     */     } else {
/* 118 */       this.isMotorola = false;
/* 119 */       this.mManager = null;
/* 120 */       this.mChannel = null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 126 */     super.onStart();
/* 127 */     this.spinner.setSelection(spinnerSelection);
/*     */   }
/*     */   
/*     */   public void onItemSelected(AdapterView<?> av, View v, int item, long l)
/*     */   {
/* 132 */     switch (item) {
/* 133 */     case 0:  this.wifi_direct_class = -1;this.wifi_direct_channel = (this.isMotorola ? 0 : -1); break;
/* 134 */     case 1:  this.wifi_direct_class = 81;this.wifi_direct_channel = 1; break;
/* 135 */     case 2:  this.wifi_direct_class = 81;this.wifi_direct_channel = 2; break;
/* 136 */     case 3:  this.wifi_direct_class = 81;this.wifi_direct_channel = 3; break;
/* 137 */     case 4:  this.wifi_direct_class = 81;this.wifi_direct_channel = 4; break;
/* 138 */     case 5:  this.wifi_direct_class = 81;this.wifi_direct_channel = 5; break;
/* 139 */     case 6:  this.wifi_direct_class = 81;this.wifi_direct_channel = 6; break;
/* 140 */     case 7:  this.wifi_direct_class = 81;this.wifi_direct_channel = 7; break;
/* 141 */     case 8:  this.wifi_direct_class = 81;this.wifi_direct_channel = 8; break;
/* 142 */     case 9:  this.wifi_direct_class = 81;this.wifi_direct_channel = 9; break;
/* 143 */     case 10:  this.wifi_direct_class = 81;this.wifi_direct_channel = 10; break;
/* 144 */     case 11:  this.wifi_direct_class = 81;this.wifi_direct_channel = 11;
/*     */     }
/*     */     
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
/*     */   public void onNothingSelected(AdapterView<?> av) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onClick(View v)
/*     */   {
/* 169 */     if (v.getId() == R.id.buttonConfigure) {
/* 170 */       spinnerSelection = this.spinner.getSelectedItemPosition();
/* 171 */       configure();
/*     */     }
/* 173 */     else if (v.getId() == R.id.buttonWifiSettings) {
/* 174 */       DbgLog.msg("launch wifi settings");
/*     */       
/* 176 */       startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
/*     */     }
/*     */   }
/*     */   
/*     */   private void configure() {
/* 181 */     DbgLog.msg(String.format("configure p2p channel - class %d channel %d", new Object[] { Integer.valueOf(this.wifi_direct_class), Integer.valueOf(this.wifi_direct_channel) }));
/*     */     
/*     */ 
/* 184 */     if (this.isMotorola)
/*     */     {
/* 186 */       DbgLog.msg(String.format("configure p2p channel - MANUFACTURER = MOTOROLA", new Object[0]));
/* 187 */       DbgLog.msg(String.format("configure p2p channel - using Wireless P2p reflection API", new Object[0]));
/*     */       try
/*     */       {
/* 190 */         Method setWifiP2pChannelsMethod = WifiP2pManager.class.getMethod("setWifiP2pChannels", new Class[] { WifiP2pManager.Channel.class, Integer.TYPE, Integer.TYPE, WifiP2pManager.ActionListener.class });
/*     */         try
/*     */         {
/* 193 */           setWifiP2pChannelsMethod.invoke(this.mManager, new Object[] { this.mChannel, Integer.valueOf(this.wifi_direct_channel), Integer.valueOf(this.wifi_direct_channel), new WifiP2pManager.ActionListener()
/*     */           {
/*     */ 
/*     */ 
/*     */             public void onSuccess()
/*     */             {
/*     */ 
/*     */ 
/* 201 */               FtcWifiChannelSelectorActivity.this.appUtil.showToast(ToastLocation.ONLY_LOCAL, String.format("Successfully changed WiFi P2P channel to %d", new Object[] { Integer.valueOf(FtcWifiChannelSelectorActivity.this.wifi_direct_channel) }), 1);
/*     */             }
/*     */             
/*     */ 
/*     */             public void onFailure(int reason)
/*     */             {
/* 207 */               FtcWifiChannelSelectorActivity.this.appUtil.showToast(ToastLocation.ONLY_LOCAL, "FAILED - unable to change WiFi P2P channel.", 1);
/*     */             }
/*     */           } });
/*     */         }
/*     */         catch (IllegalAccessException e)
/*     */         {
/* 213 */           this.appUtil.showToast(ToastLocation.ONLY_LOCAL, "FAILED - unable to change WiFi P2P channel... IllegalAccessException.", 1);
/*     */           
/* 215 */           DbgLog.error("configure p2p channel - IllegalAccessException caught");
/* 216 */           e.printStackTrace();
/*     */         } catch (InvocationTargetException e) {
/* 218 */           this.appUtil.showToast(ToastLocation.ONLY_LOCAL, "FAILED - unable to change WiFi P2P channel... InvocationTargetException.", 1);
/*     */           
/* 220 */           DbgLog.error("configure p2p channel - InvocationTargetException caught");
/* 221 */           e.printStackTrace();
/*     */         }
/*     */       } catch (NoSuchMethodException e) {
/* 224 */         this.appUtil.showToast(ToastLocation.ONLY_LOCAL, "FAILED - unable to change WiFi P2P channel... NoSuchMethodException.", 1);
/*     */         
/* 226 */         DbgLog.error("configure p2p channel - NoSuchMethodException caught");
/* 227 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 232 */       DbgLog.msg(String.format("configure p2p channel - using qcom WirelessP2p library.", new Object[0]));
/*     */       try {
/* 234 */         this.progressDialog = ProgressDialog.show(this, "Configuring Channel", "Please Wait", true);
/*     */         
/* 236 */         this.wifiConfig.config(this.wifi_direct_class, this.wifi_direct_channel);
/* 237 */         new Thread(new Runnable()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 241 */               Thread.sleep(5000L);
/*     */             }
/*     */             catch (InterruptedException localInterruptedException) {}
/* 244 */             FtcWifiChannelSelectorActivity.this.runOnUiThread(new Runnable()
/*     */             {
/*     */               public void run() {
/* 247 */                 FtcWifiChannelSelectorActivity.this.setResult(-1);
/* 248 */                 FtcWifiChannelSelectorActivity.this.progressDialog.dismiss();
/* 249 */                 FtcWifiChannelSelectorActivity.this.finish();
/*     */               }
/*     */             });
/*     */           }
/*     */         }).start();
/*     */       } catch (IOException e) {
/* 255 */         this.appUtil.showToast(ToastLocation.ONLY_LOCAL, "Failed - root is required");
/* 256 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\FtcWifiChannelSelectorActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */