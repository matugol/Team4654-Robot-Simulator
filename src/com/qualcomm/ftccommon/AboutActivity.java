/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.pm.ApplicationInfo;
/*     */ import android.content.pm.PackageInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.content.pm.PackageManager.NameNotFoundException;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.ListView;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.util.Network;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.Version;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
/*     */ import com.qualcomm.robotcore.wifi.NetworkType;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
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
/*     */ public class AboutActivity
/*     */   extends Activity
/*     */ {
/*     */   public static final String launchIntent = "com.qualcomm.ftccommon.configuration.AboutActivity.intent.action.Launch";
/*     */   NetworkConnection networkConnection;
/*     */   NetworkType networkType;
/*     */   
/*     */   public AboutActivity()
/*     */   {
/*  96 */     this.networkConnection = null;
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 101 */     super.onStart();
/* 102 */     setContentView(R.layout.activity_about);
/*     */     
/* 104 */     Intent intent = getIntent();
/* 105 */     Serializable extra = intent.getSerializableExtra("org.firstinspires.ftc.ftccommon.connectionType");
/* 106 */     if (extra != null) {
/* 107 */       this.networkType = ((NetworkType)extra);
/*     */     }
/*     */     
/*     */ 
/* 111 */     ListView aboutList = (ListView)findViewById(R.id.aboutList);
/*     */     try
/*     */     {
/* 114 */       this.networkConnection = NetworkConnectionFactory.getNetworkConnection(this.networkType, null);
/* 115 */       this.networkConnection.enable();
/*     */     } catch (NullPointerException e) {
/* 117 */       RobotLog.e("Cannot start Network Connection of type: " + this.networkType);
/* 118 */       this.networkConnection = null;
/*     */     }
/*     */     
/* 121 */     ArrayAdapter<Item> adapter = new ArrayAdapter(this, 17367044, 16908308)
/*     */     {
/*     */       public View getView(int position, View convertView, ViewGroup parent) {
/* 124 */         View view = super.getView(position, convertView, parent);
/* 125 */         TextView topLine = (TextView)view.findViewById(16908308);
/* 126 */         TextView bottomLine = (TextView)view.findViewById(16908309);
/*     */         
/* 128 */         AboutActivity.Item item = getItem(position);
/* 129 */         topLine.setText(item.title);
/* 130 */         bottomLine.setText(item.info);
/*     */         
/* 132 */         return view;
/*     */       }
/*     */       
/*     */ 
/*     */       public int getCount()
/*     */       {
/* 138 */         return 5;
/*     */       }
/*     */       
/*     */       public AboutActivity.Item getItem(int pos)
/*     */       {
/* 143 */         switch (pos) {
/* 144 */         case 0:  return getAppVersion();
/* 145 */         case 1:  return getLibVersion();
/* 146 */         case 2:  return getNetworkProtocolVersion();
/* 147 */         case 3:  return getConnectionInfo();
/* 148 */         case 4:  return getBuildTimeInfo();
/*     */         }
/* 150 */         return new AboutActivity.Item();
/*     */       }
/*     */       
/*     */       private AboutActivity.Item getAppVersion() {
/* 154 */         AboutActivity.Item i = new AboutActivity.Item();
/* 155 */         i.title = AboutActivity.this.getString(R.string.about_app_version);
/*     */         try {
/* 157 */           i.info = AboutActivity.this.getPackageManager().getPackageInfo(AboutActivity.this.getPackageName(), 0).versionName;
/*     */         } catch (PackageManager.NameNotFoundException e) {
/* 159 */           i.info = e.getMessage();
/*     */         }
/* 161 */         return i;
/*     */       }
/*     */       
/*     */       private AboutActivity.Item getLibVersion() {
/* 165 */         AboutActivity.Item i = new AboutActivity.Item();
/* 166 */         i.title = AboutActivity.this.getString(R.string.about_library_version);
/* 167 */         i.info = Version.getLibraryVersion();
/* 168 */         return i;
/*     */       }
/*     */       
/*     */       private AboutActivity.Item getNetworkProtocolVersion() {
/* 172 */         AboutActivity.Item i = new AboutActivity.Item();
/* 173 */         i.title = AboutActivity.this.getString(R.string.about_network_protocol_version);
/* 174 */         i.info = String.format("v%d", new Object[] { Byte.valueOf(11) });
/* 175 */         return i;
/*     */       }
/*     */       
/*     */       private AboutActivity.Item getConnectionInfo() {
/* 179 */         AboutActivity.Item i = new AboutActivity.Item();
/* 180 */         i.title = AboutActivity.this.getString(R.string.about_network_connection_info);
/* 181 */         i.info = AboutActivity.this.networkConnection.getInfo();
/*     */         
/* 183 */         return i;
/*     */       }
/*     */       
/*     */       private AboutActivity.Item getBuildTimeInfo() {
/* 187 */         AboutActivity.Item i = new AboutActivity.Item();
/* 188 */         i.title = AboutActivity.this.getString(R.string.about_build_time);
/* 189 */         i.info = AboutActivity.this.getBuildTime();
/* 190 */         return i;
/*     */       }
/*     */       
/* 193 */     };
/* 194 */     aboutList.setAdapter(adapter);
/*     */   }
/*     */   
/*     */   protected void onStop() {
/* 198 */     super.onStop();
/*     */     
/* 200 */     if (this.networkConnection != null) {
/* 201 */       this.networkConnection.disable();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getLocalIpAddressesAsString()
/*     */   {
/* 208 */     ArrayList<InetAddress> addrs = Network.getLocalIpAddresses();
/* 209 */     addrs = Network.removeLoopbackAddresses(addrs);
/* 210 */     addrs = Network.removeIPv6Addresses(addrs);
/*     */     
/* 212 */     if (addrs.size() < 1) { return "unavailable";
/*     */     }
/* 214 */     StringBuilder sb = new StringBuilder();
/* 215 */     sb.append(((InetAddress)addrs.get(0)).getHostAddress());
/* 216 */     for (int i = 1; i < addrs.size(); i++) {
/* 217 */       sb.append(", ").append(((InetAddress)addrs.get(i)).getHostAddress());
/*     */     }
/* 219 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected String getBuildTime() {
/* 223 */     String buildTime = "unavailable";
/*     */     try {
/* 225 */       ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
/*     */       
/* 227 */       ZipFile zf = new ZipFile(ai.sourceDir);
/* 228 */       ZipEntry ze = zf.getEntry("classes.dex");
/* 229 */       zf.close();
/*     */       
/* 231 */       long time = ze.getTime();
/* 232 */       buildTime = SimpleDateFormat.getInstance().format(new Date(time));
/*     */     } catch (PackageManager.NameNotFoundException e) {
/* 234 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/* 236 */       e.printStackTrace();
/*     */     }
/* 238 */     return buildTime;
/*     */   }
/*     */   
/*     */   public static class Item {
/* 242 */     public String title = "";
/* 243 */     public String info = "";
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\AboutActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */