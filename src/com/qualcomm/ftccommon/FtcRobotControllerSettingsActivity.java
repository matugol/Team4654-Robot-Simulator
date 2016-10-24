/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.FragmentManager;
/*     */ import android.app.FragmentTransaction;
/*     */ import android.content.Intent;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.os.Build;
/*     */ import android.os.Bundle;
/*     */ import android.preference.Preference;
/*     */ import android.preference.Preference.OnPreferenceClickListener;
/*     */ import android.preference.PreferenceFragment;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
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
/*     */ public class FtcRobotControllerSettingsActivity
/*     */   extends Activity
/*     */ {
/*     */   public static final String launchIntent = "com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity.intent.action.Launch";
/*     */   
/*     */   public static class SettingsFragment
/*     */     extends PreferenceFragment
/*     */   {
/*     */     public void onCreate(Bundle savedInstanceState)
/*     */     {
/*  55 */       super.onCreate(savedInstanceState);
/*     */       
/*     */ 
/*  58 */       addPreferencesFromResource(R.xml.preferences);
/*     */       
/*  60 */       if ((Build.MANUFACTURER.equalsIgnoreCase("zte")) && (Build.MODEL.equalsIgnoreCase("N9130"))) {
/*  61 */         Preference launchSettings = findPreference(getString(R.string.pref_launch_settings));
/*  62 */         launchSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
/*     */         {
/*     */           public boolean onPreferenceClick(Preference preference) {
/*  65 */             Intent intent = FtcRobotControllerSettingsActivity.SettingsFragment.this.getActivity().getPackageManager().getLaunchIntentForPackage("com.zte.wifichanneleditor");
/*     */             try {
/*  67 */               FtcRobotControllerSettingsActivity.SettingsFragment.this.startActivity(intent);
/*     */             } catch (NullPointerException e) {
/*  69 */               AppUtil.getInstance().showToast(ToastLocation.ONLY_LOCAL, FtcRobotControllerSettingsActivity.SettingsFragment.this.getActivity().getString(R.string.toastUnableToLaunchZTEWifiChannelEditor));
/*     */             }
/*  71 */             return true;
/*     */           }
/*     */         });
/*     */       } else {
/*  75 */         Preference launchSettings = findPreference(getString(R.string.pref_launch_settings));
/*  76 */         launchSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
/*     */         {
/*     */           public boolean onPreferenceClick(Preference preference) {
/*  79 */             Intent intent = new Intent(preference.getIntent().getAction());
/*  80 */             FtcRobotControllerSettingsActivity.SettingsFragment.this.startActivity(intent);
/*  81 */             return true;
/*     */           }
/*     */         });
/*     */       }
/*     */       
/*  86 */       if (Build.MODEL.equals("FL7007")) {
/*  87 */         Preference launchSettings = findPreference(getString(R.string.pref_launch_settings));
/*  88 */         launchSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
/*     */         {
/*     */           public boolean onPreferenceClick(Preference preference) {
/*  91 */             Intent viewIntent = new Intent("android.settings.SETTINGS");
/*  92 */             FtcRobotControllerSettingsActivity.SettingsFragment.this.startActivity(viewIntent);
/*     */             
/*  94 */             return true;
/*     */           }
/*     */         });
/*     */       }
/*     */       
/*  99 */       Preference prefViewLogs = findPreference(getString(R.string.pref_launch_viewlogs));
/* 100 */       prefViewLogs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
/*     */         public boolean onPreferenceClick(Preference preference) {
/* 102 */           Intent viewLogsIntent = new Intent("com.qualcomm.ftccommon.ViewLogsActivity.intent.action.Launch");
/* 103 */           viewLogsIntent.putExtra("org.firstinspires.ftc.ftccommon.logFilename", RobotLog.getLogFilename(FtcRobotControllerSettingsActivity.SettingsFragment.this.getActivity()));
/* 104 */           FtcRobotControllerSettingsActivity.SettingsFragment.this.startActivity(viewLogsIntent);
/* 105 */           return true;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public void onActivityResult(int request, int result, Intent intent)
/*     */     {
/* 112 */       if ((request == 3) && 
/* 113 */         (result == -1)) {
/* 114 */         getActivity().setResult(-1, intent);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/* 122 */     super.onCreate(savedInstanceState);
/*     */     
/*     */ 
/* 125 */     getFragmentManager().beginTransaction().replace(16908290, new SettingsFragment()).commit();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\FtcRobotControllerSettingsActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */