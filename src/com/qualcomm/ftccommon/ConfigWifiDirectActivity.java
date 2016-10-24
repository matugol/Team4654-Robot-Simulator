/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.ProgressDialog;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.net.wifi.WifiManager;
/*     */ import android.os.Bundle;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.wifi.FixWifiDirectSetup;
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
/*     */ public class ConfigWifiDirectActivity
/*     */   extends Activity
/*     */ {
/*     */   public static enum Flag
/*     */   {
/*  52 */     NONE, 
/*  53 */     WIFI_DIRECT_FIX_CONFIG, 
/*  54 */     WIFI_DIRECT_DEVICE_NAME_INVALID;
/*     */     
/*     */     private Flag() {} }
/*  57 */   private static Flag flag = Flag.NONE;
/*     */   
/*     */   private WifiManager wifiManager;
/*     */   
/*     */   private ProgressDialog progressDialog;
/*     */   
/*     */   private Context context;
/*     */   private TextView textPleaseWait;
/*     */   private TextView textBadDeviceName;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  69 */     super.onCreate(savedInstanceState);
/*  70 */     setContentView(R.layout.activity_config_wifi_direct);
/*     */     
/*  72 */     this.textPleaseWait = ((TextView)findViewById(R.id.textPleaseWait));
/*  73 */     this.textBadDeviceName = ((TextView)findViewById(R.id.textBadDeviceName));
/*     */     
/*  75 */     this.context = this;
/*     */   }
/*     */   
/*     */   protected void onResume()
/*     */   {
/*  80 */     super.onResume();
/*     */     
/*  82 */     this.textPleaseWait.setVisibility(0);
/*     */     
/*  84 */     this.wifiManager = ((WifiManager)getSystemService("wifi"));
/*     */     
/*  86 */     DbgLog.msg("Processing flag " + flag.toString());
/*     */     
/*  88 */     switch (flag) {
/*     */     case WIFI_DIRECT_DEVICE_NAME_INVALID: 
/*  90 */       new Thread(new DisableWifiAndWarnBadDeviceName(null)).start();
/*  91 */       break;
/*     */     case WIFI_DIRECT_FIX_CONFIG: 
/*  93 */       new Thread(new ToggleWifiRunnable(null)).start();
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   protected void onPause()
/*     */   {
/* 100 */     super.onPause();
/* 101 */     flag = Flag.NONE;
/* 102 */     this.textBadDeviceName.setVisibility(4);
/*     */   }
/*     */   
/*     */   private class ToggleWifiRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private ToggleWifiRunnable() {}
/*     */     
/*     */     public void run()
/*     */     {
/* 112 */       DbgLog.msg("attempting to reconfigure Wifi Direct");
/*     */       
/* 114 */       ConfigWifiDirectActivity.this.showProgressDialog();
/*     */       try
/*     */       {
/* 117 */         FixWifiDirectSetup.fixWifiDirectSetup(ConfigWifiDirectActivity.this.wifiManager);
/*     */       } catch (InterruptedException e) {
/* 119 */         DbgLog.error("Cannot fix wifi setup - interrupted");
/*     */       }
/*     */       
/* 122 */       ConfigWifiDirectActivity.this.dismissProgressDialog();
/*     */       
/* 124 */       DbgLog.msg("reconfigure Wifi Direct complete");
/*     */       
/* 126 */       ConfigWifiDirectActivity.this.runOnUiThread(new Runnable()
/*     */       {
/*     */ 
/* 129 */         public void run() { ConfigWifiDirectActivity.this.finish(); }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private class DisableWifiAndWarnBadDeviceName implements Runnable {
/*     */     private DisableWifiAndWarnBadDeviceName() {}
/*     */     
/*     */     public void run() {
/* 138 */       DbgLog.msg("attempting to disable Wifi due to bad wifi direct device name");
/*     */       
/* 140 */       ConfigWifiDirectActivity.this.showProgressDialog();
/*     */       try
/*     */       {
/* 143 */         FixWifiDirectSetup.disableWifiDirect(ConfigWifiDirectActivity.this.wifiManager);
/*     */       } catch (InterruptedException e) {
/* 145 */         DbgLog.error("Cannot fix wifi setup - interrupted");
/*     */       }
/*     */       
/* 148 */       ConfigWifiDirectActivity.this.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/* 151 */           ConfigWifiDirectActivity.this.textPleaseWait.setVisibility(4);
/* 152 */           ConfigWifiDirectActivity.this.textBadDeviceName.setVisibility(0);
/*     */         }
/*     */         
/* 155 */       });
/* 156 */       ConfigWifiDirectActivity.this.dismissProgressDialog();
/*     */     }
/*     */   }
/*     */   
/*     */   private void showProgressDialog() {
/* 161 */     runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 164 */         ConfigWifiDirectActivity.this.progressDialog = new ProgressDialog(ConfigWifiDirectActivity.this.context, R.style.CustomAlertDialog);
/* 165 */         ConfigWifiDirectActivity.this.progressDialog.setMessage("Please wait");
/* 166 */         ConfigWifiDirectActivity.this.progressDialog.setTitle("Configuring Wifi Direct");
/* 167 */         ConfigWifiDirectActivity.this.progressDialog.setIndeterminate(true);
/* 168 */         ConfigWifiDirectActivity.this.progressDialog.show();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void dismissProgressDialog() {
/* 174 */     runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 177 */         ConfigWifiDirectActivity.this.progressDialog.dismiss();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static void launch(Context context) {
/* 183 */     launch(context, Flag.WIFI_DIRECT_FIX_CONFIG);
/*     */   }
/*     */   
/*     */   public static void launch(Context context, Flag flag) {
/* 187 */     Intent configWifiDirectIntent = new Intent(context, ConfigWifiDirectActivity.class);
/* 188 */     configWifiDirectIntent.addFlags(1342177280);
/* 189 */     context.startActivity(configWifiDirectIntent);
/*     */     
/* 191 */     flag = flag;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\ConfigWifiDirectActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */