/*     */ package com.qualcomm.analytics;
/*     */ 
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.content.SharedPreferences;
/*     */ import android.content.SharedPreferences.Editor;
/*     */ import android.content.pm.ApplicationInfo;
/*     */ import android.net.ConnectivityManager;
/*     */ import android.net.NetworkInfo;
/*     */ import android.net.NetworkInfo.State;
/*     */ import android.os.AsyncTask;
/*     */ import android.os.Build;
/*     */ import android.os.Bundle;
/*     */ import android.os.Environment;
/*     */ import android.preference.PreferenceManager;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorController;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*     */ import com.qualcomm.robotcore.hardware.ServoController;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.Version;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.EOFException;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
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
/*     */ public class Analytics
/*     */   extends BroadcastReceiver
/*     */ {
/*     */   public static final String UUID_PATH = ".analytics_id";
/*     */   public static final String DATA_COLLECTION_PATH = ".ftcdc";
/*     */   
/*     */   public void onReceive(Context context, Intent intent)
/*     */   {
/*  84 */     Bundle localBundle = intent.getExtras();
/*  85 */     if ((localBundle != null) && 
/*  86 */       (localBundle.containsKey("networkInfo"))) {
/*  87 */       NetworkInfo localNetworkInfo = (NetworkInfo)localBundle.get("networkInfo");
/*  88 */       NetworkInfo.State localState = localNetworkInfo.getState();
/*  89 */       if (localState.equals(NetworkInfo.State.CONNECTED)) {
/*  90 */         RobotLog.i("Analytics detected NetworkInfo.State.CONNECTED");
/*  91 */         communicateWithServer();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DataInfo
/*     */     implements Serializable
/*     */   {
/*     */     private final String a;
/*     */     protected int numUsages;
/*     */     
/*     */     public DataInfo(String adate, int numUsages)
/*     */     {
/* 104 */       this.a = adate;
/* 105 */       this.numUsages = numUsages;
/*     */     }
/*     */     
/* 108 */     public String date() { return this.a; }
/* 109 */     public int numUsages() { return this.numUsages; }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 114 */   static String a = "https://ftcdc.qualcomm.com/DataApi";
/*     */   public static final String RC_COMMAND_STRING = "update_rc";
/*     */   public static final String DS_COMMAND_STRING = "update_ds";
/* 117 */   public static final String EXTERNAL_STORAGE_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/";
/*     */   public static final String LAST_UPLOAD_DATE = "last_upload_date";
/*     */   public static final String MAX_DEVICES = "max_usb_devices";
/* 120 */   public static int MAX_ENTRIES_SIZE = 100;
/* 121 */   public static int TRIMMED_SIZE = 90;
/*     */   
/* 123 */   private static final Charset m = Charset.forName("UTF-8");
/*     */   
/*     */   static long b;
/* 126 */   static UUID c = null;
/*     */   static String d;
/*     */   String e;
/* 129 */   static String f = "";
/*     */   
/*     */   Context g;
/*     */   SharedPreferences h;
/* 133 */   boolean i = false;
/* 134 */   long j = 0L;
/* 135 */   int k = 0;
/*     */   
/*     */   private class a extends AsyncTask {
/*     */     private a() {}
/*     */     
/*     */     protected Object doInBackground(Object[] params) {
/* 141 */       if (Analytics.this.isConnected()) {
/*     */         try {
/* 143 */           URL localURL = null;
/* 144 */           localURL = new URL(Analytics.a);
/*     */           
/* 146 */           long l = Analytics.this.h.getLong("last_upload_date", Analytics.this.j);
/*     */           
/* 148 */           if (!Analytics.getDateFromTime(Analytics.b).equals(Analytics.getDateFromTime(l)))
/*     */           {
/* 150 */             String str1 = Analytics.a(Analytics.this, "cmd", "=", "ping");
/* 151 */             String str2 = Analytics.ping(localURL, str1);
/* 152 */             String str3 = "\"rc\": \"OK\"";
/*     */             
/* 154 */             if ((str2 == null) || (!str2.contains(str3))) {
/* 155 */               RobotLog.e("Analytics: Ping failed.");
/* 156 */               return null;
/*     */             }
/* 158 */             RobotLog.i("Analytics ping succeeded.");
/*     */             
/* 160 */             String str4 = Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc";
/* 161 */             ArrayList localArrayList = Analytics.this.readObjectsFromFile(str4);
/*     */             
/* 163 */             if (localArrayList.size() >= Analytics.MAX_ENTRIES_SIZE) {
/* 164 */               Analytics.this.trimEntries(localArrayList);
/*     */             }
/*     */             
/* 167 */             String str5 = Analytics.this.updateStats(Analytics.c.toString(), localArrayList, Analytics.this.e);
/* 168 */             String str6 = Analytics.call(localURL, str5);
/*     */             
/* 170 */             if ((str6 == null) || (!str6.contains(str3))) {
/* 171 */               RobotLog.e("Analytics: Upload failed.");
/* 172 */               return null;
/*     */             }
/* 174 */             RobotLog.i("Analytics: Upload succeeded.");
/*     */             
/*     */ 
/* 177 */             SharedPreferences.Editor localEditor = Analytics.this.h.edit();
/* 178 */             localEditor.putLong("last_upload_date", Analytics.b);
/* 179 */             localEditor.apply();
/*     */             
/* 181 */             localEditor.putInt("max_usb_devices", 0);
/* 182 */             localEditor.apply();
/*     */             
/* 184 */             File localFile = new File(str4);
/* 185 */             boolean bool = localFile.delete();
/*     */           }
/*     */         }
/*     */         catch (MalformedURLException localMalformedURLException1) {
/* 189 */           RobotLog.e("Analytics encountered a malformed URL exception");
/*     */         }
/*     */         catch (Exception localException1) {
/* 192 */           Analytics.this.i = true;
/*     */         }
/* 194 */         if (Analytics.this.i) {
/* 195 */           RobotLog.i("Analytics encountered a problem during communication");
/* 196 */           Analytics.a(Analytics.this);
/* 197 */           Analytics.this.i = false;
/*     */         }
/*     */       }
/* 200 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void unregister() {
/* 205 */     this.g.unregisterReceiver(this);
/*     */   }
/*     */   
/*     */   public void register() {
/* 209 */     this.g.registerReceiver(this, new IntentFilter("android.net.wifi.STATE_CHANGE"));
/*     */   }
/*     */   
/*     */   public Analytics(Context context, String commandString, HardwareMap map) {
/* 213 */     this.g = context;
/* 214 */     this.e = commandString;
/*     */     try {
/*     */       try {
/* 217 */         this.h = PreferenceManager.getDefaultSharedPreferences(context);
/* 218 */         b = System.currentTimeMillis();
/* 219 */         f = Version.getLibraryVersion();
/* 220 */         handleUUID(".analytics_id");
/*     */         
/* 222 */         int n = calculateUsbDevices(map);
/*     */         
/* 224 */         int i1 = this.h.getInt("max_usb_devices", this.k);
/* 225 */         if (i1 < n) {
/* 226 */           SharedPreferences.Editor localEditor = this.h.edit();
/* 227 */           localEditor.putInt("max_usb_devices", n);
/* 228 */           localEditor.apply();
/*     */         }
/*     */         
/* 231 */         setApplicationName(context.getApplicationInfo().loadLabel(context.getPackageManager()).toString());
/*     */         
/* 233 */         handleData();
/*     */         
/* 235 */         register();
/* 236 */         RobotLog.i("Analytics has completed initialization.");
/*     */       }
/*     */       catch (Exception localException2) {
/* 239 */         this.i = true;
/*     */       }
/* 241 */       if (this.i) {
/* 242 */         a();
/* 243 */         this.i = false;
/*     */       }
/*     */     } catch (Exception localException3) {
/* 246 */       RobotLog.i("Analytics encountered a problem during initialization");
/* 247 */       RobotLog.logStacktrace(localException3);
/*     */     }
/*     */   }
/*     */   
/*     */   protected int calculateUsbDevices(HardwareMap map) {
/* 252 */     int n = 0;
/* 253 */     n += map.legacyModule.size();
/* 254 */     n += map.deviceInterfaceModule.size();
/*     */     
/* 256 */     for (Iterator localIterator = map.servoController.iterator(); localIterator.hasNext();) { localObject = (ServoController)localIterator.next();
/* 257 */       str = ((ServoController)localObject).getDeviceName();
/* 258 */       localPattern = Pattern.compile("(?i)usb");
/* 259 */       if (localPattern.matcher(str).find())
/* 260 */         n++; }
/*     */     Object localObject;
/*     */     String str;
/*     */     Pattern localPattern;
/* 264 */     for (localIterator = map.dcMotorController.iterator(); localIterator.hasNext();) { localObject = (DcMotorController)localIterator.next();
/* 265 */       str = ((DcMotorController)localObject).getDeviceName();
/* 266 */       localPattern = Pattern.compile("(?i)usb");
/* 267 */       if (localPattern.matcher(str).find()) {
/* 268 */         n++;
/*     */       }
/*     */     }
/* 271 */     return n;
/*     */   }
/*     */   
/*     */   protected void handleData() throws IOException, ClassNotFoundException {
/* 275 */     String str = EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc";
/* 276 */     File localFile = new File(str);
/* 277 */     if (!localFile.exists()) {
/* 278 */       createInitialFile(str);
/*     */     } else {
/* 280 */       ArrayList localArrayList = updateExistingFile(str, getDateFromTime(b));
/* 281 */       if (localArrayList.size() >= MAX_ENTRIES_SIZE) {
/* 282 */         trimEntries(localArrayList);
/*     */       }
/* 284 */       writeObjectsToFile(str, localArrayList);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void trimEntries(ArrayList<DataInfo> dataInfoArrayList) {
/* 289 */     dataInfoArrayList.subList(TRIMMED_SIZE, dataInfoArrayList.size()).clear();
/*     */   }
/*     */   
/*     */   protected ArrayList<DataInfo> updateExistingFile(String filepath, String date) throws ClassNotFoundException, IOException {
/* 293 */     ArrayList localArrayList = readObjectsFromFile(filepath);
/* 294 */     DataInfo localDataInfo1 = (DataInfo)localArrayList.get(localArrayList.size() - 1);
/* 295 */     if (DataInfo.a(localDataInfo1).equalsIgnoreCase(date)) {
/* 296 */       localDataInfo1.numUsages += 1;
/*     */     } else {
/* 298 */       DataInfo localDataInfo2 = new DataInfo(date, 1);
/* 299 */       localArrayList.add(localDataInfo2);
/*     */     }
/* 301 */     return localArrayList;
/*     */   }
/*     */   
/*     */   protected ArrayList<DataInfo> readObjectsFromFile(String filepath) throws IOException, ClassNotFoundException {
/* 305 */     FileInputStream localFileInputStream = new FileInputStream(new File(filepath));
/* 306 */     ObjectInputStream localObjectInputStream = new ObjectInputStream(localFileInputStream);
/* 307 */     ArrayList localArrayList = new ArrayList();
/* 308 */     int n = 1;
/* 309 */     while (n != 0) {
/*     */       try {
/* 311 */         DataInfo localDataInfo = (DataInfo)localObjectInputStream.readObject();
/* 312 */         localArrayList.add(localDataInfo);
/*     */       } catch (EOFException localEOFException1) {
/* 314 */         n = 0;
/*     */       }
/*     */     }
/* 317 */     localObjectInputStream.close();
/*     */     
/* 319 */     return localArrayList;
/*     */   }
/*     */   
/*     */   protected void createInitialFile(String filepath) throws IOException {
/* 323 */     DataInfo localDataInfo = new DataInfo(getDateFromTime(b), 1);
/* 324 */     ArrayList localArrayList = new ArrayList();
/* 325 */     localArrayList.add(localDataInfo);
/* 326 */     writeObjectsToFile(filepath, localArrayList);
/*     */   }
/*     */   
/*     */   private void a() {
/* 330 */     RobotLog.i("Analytics is starting with a clean slate.");
/*     */     
/*     */ 
/* 333 */     SharedPreferences.Editor localEditor = this.h.edit();
/* 334 */     localEditor.putLong("last_upload_date", this.j);
/* 335 */     localEditor.apply();
/*     */     
/* 337 */     localEditor.putInt("max_usb_devices", 0);
/* 338 */     localEditor.apply();
/*     */     
/*     */ 
/* 341 */     File localFile1 = new File(EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc");
/* 342 */     localFile1.delete();
/*     */     
/* 344 */     File localFile2 = new File(EXTERNAL_STORAGE_DIRECTORY_PATH + ".analytics_id");
/* 345 */     localFile2.delete();
/*     */     
/* 347 */     this.i = false;
/*     */   }
/*     */   
/*     */   public void communicateWithServer() {
/* 351 */     String[] arrayOfString = new String[1];
/* 352 */     arrayOfString[0] = a;
/* 353 */     new a(null).execute((Object[])arrayOfString);
/*     */   }
/*     */   
/* 356 */   public static void setApplicationName(String name) { d = name; }
/*     */   
/*     */   public void handleUUID(String filename)
/*     */   {
/* 360 */     File localFile = new File(EXTERNAL_STORAGE_DIRECTORY_PATH + filename);
/* 361 */     if (!localFile.exists()) {
/* 362 */       c = UUID.randomUUID();
/* 363 */       handleCreateNewFile(EXTERNAL_STORAGE_DIRECTORY_PATH + filename, c.toString());
/*     */     }
/* 365 */     String str = readFromFile(localFile);
/*     */     try {
/* 367 */       c = UUID.fromString(str);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 369 */       RobotLog.i("Analytics encountered an IllegalArgumentException");
/* 370 */       c = UUID.randomUUID();
/* 371 */       handleCreateNewFile(EXTERNAL_STORAGE_DIRECTORY_PATH + filename, c.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   protected String readFromFile(File file) {
/*     */     try {
/* 377 */       char[] arrayOfChar = new char['က'];
/*     */       
/* 379 */       FileReader localFileReader = new FileReader(file);
/* 380 */       int n = localFileReader.read(arrayOfChar);
/* 381 */       localFileReader.close();
/*     */       
/* 383 */       String str = new String(arrayOfChar, 0, n);
/* 384 */       return str.trim();
/*     */     }
/*     */     catch (FileNotFoundException localFileNotFoundException1) {
/* 387 */       RobotLog.i("Analytics encountered a FileNotFoundException while trying to read a file.");
/*     */     } catch (IOException localIOException1) {
/* 389 */       RobotLog.i("Analytics encountered an IOException while trying to read.");
/*     */     }
/*     */     
/* 392 */     return "";
/*     */   }
/*     */   
/*     */   protected void writeObjectsToFile(String filepath, ArrayList<DataInfo> info) throws IOException {
/* 396 */     ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(new FileOutputStream(filepath));
/*     */     
/*     */ 
/* 399 */     for (DataInfo localDataInfo : info) {
/* 400 */       localObjectOutputStream.writeObject(localDataInfo);
/*     */     }
/* 402 */     localObjectOutputStream.close();
/*     */   }
/*     */   
/*     */   protected void handleCreateNewFile(String filepath, String data) {
/* 406 */     BufferedWriter localBufferedWriter = null;
/*     */     try {
/* 408 */       File localFile = new File(filepath);
/* 409 */       FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
/* 410 */       localBufferedWriter = new BufferedWriter(new OutputStreamWriter(localFileOutputStream, "utf-8"));
/* 411 */       localBufferedWriter.write(data); return;
/*     */     } catch (IOException localIOException5) {
/* 413 */       RobotLog.i("Analytics encountered an IOException: " + localIOException5.toString());
/*     */     } finally {
/* 415 */       if (localBufferedWriter != null) {
/*     */         try {
/* 417 */           localBufferedWriter.close();
/*     */         }
/*     */         catch (IOException localIOException3) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getDateFromTime(long time)
/*     */   {
/* 426 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
/* 427 */     String str = localSimpleDateFormat.format(new Date(time));
/* 428 */     return str;
/*     */   }
/*     */   
/*     */   protected static UUID getUuid() {
/* 432 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String updateStats(String user, ArrayList<DataInfo> dateInfo, String commandString)
/*     */   {
/* 444 */     String str1 = a("cmd", "=", commandString) + "&" + a("uuid", "=", user) + "&" + a("device_hw", "=", Build.MANUFACTURER) + "&" + a("device_ver", "=", Build.MODEL) + "&" + a("chip_type", "=", b()) + "&" + a("sw_ver", "=", f) + "&";
/*     */     
/* 446 */     if (commandString.equalsIgnoreCase("update_rc")) {
/* 447 */       int n = this.h.getInt("max_usb_devices", this.k);
/* 448 */       str1 = str1 + a("max_dev", "=", String.valueOf(n)) + "&";
/*     */     }
/*     */     
/* 451 */     String str2 = "";
/* 452 */     for (int i1 = 0; i1 < dateInfo.size(); i1++)
/*     */     {
/* 454 */       if (i1 > 0) str2 = str2 + ",";
/* 455 */       str2 = str2 + a(((DataInfo)dateInfo.get(i1)).date(), ",", String.valueOf(((DataInfo)dateInfo.get(i1)).numUsages()));
/*     */     }
/* 457 */     str1 = str1 + a("dc", "=", "");
/* 458 */     str1 = str1 + str2;
/*     */     
/* 460 */     return str1;
/*     */   }
/*     */   
/*     */   private String a(String paramString1, String paramString2, String paramString3) {
/* 464 */     String str = "";
/*     */     try
/*     */     {
/* 467 */       str = URLEncoder.encode(paramString1, m.name()) + paramString2 + URLEncoder.encode(paramString3, m.name());
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 469 */       RobotLog.i("Analytics caught an UnsupportedEncodingException");
/*     */     }
/* 471 */     return str;
/*     */   }
/*     */   
/*     */   private String b() {
/* 475 */     String str1 = "UNKNOWN";
/* 476 */     String str2 = "/proc/cpuinfo";
/* 477 */     String[] arrayOfString1 = { "CPU implementer", "Hardware" };
/*     */     
/* 479 */     HashMap localHashMap = new HashMap();
/*     */     try
/*     */     {
/* 482 */       BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
/* 483 */       String str3 = localBufferedReader.readLine();
/* 484 */       while (str3 != null) {
/* 485 */         str3 = str3.toLowerCase();
/* 486 */         localObject = str3.split(":");
/* 487 */         if (localObject.length >= 2) {
/* 488 */           localHashMap.put(localObject[0].trim(), localObject[1].trim());
/*     */         }
/* 490 */         str3 = localBufferedReader.readLine();
/*     */       }
/* 492 */       localBufferedReader.close();
/*     */       
/* 494 */       Object localObject = "";
/* 495 */       for (String str4 : arrayOfString1) {
/* 496 */         localObject = (String)localObject + (String)localHashMap.get(str4.toLowerCase()) + " ";
/*     */       }
/*     */       
/* 499 */       localObject = ((String)localObject).trim();
/* 500 */       if (((String)localObject).isEmpty()) { return str1;
/*     */       }
/* 502 */       return (String)localObject;
/*     */     }
/*     */     catch (FileNotFoundException localFileNotFoundException) {
/* 505 */       RobotLog.i("Analytics encountered a FileNotFoundException while looking for CPU info");
/*     */     } catch (IOException localIOException) {
/* 507 */       RobotLog.i("Analytics encountered an IOException while looking for CPU info");
/*     */     }
/*     */     
/* 510 */     return str1;
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 514 */     ConnectivityManager localConnectivityManager = (ConnectivityManager)this.g.getSystemService("connectivity");
/* 515 */     NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
/* 516 */     return (localNetworkInfo != null) && (localNetworkInfo.isConnected());
/*     */   }
/*     */   
/*     */   public static String ping(URL baseUrl, String data)
/*     */   {
/* 521 */     String str = call(baseUrl, data);
/* 522 */     return str;
/*     */   }
/*     */   
/*     */   public static String call(URL url, String data)
/*     */   {
/* 527 */     String str1 = null;
/*     */     
/* 529 */     if ((url != null) && (data != null)) {
/*     */       try {
/* 531 */         long l1 = System.currentTimeMillis();
/*     */         
/* 533 */         Object localObject1 = null;
/*     */         
/* 535 */         if (url.getProtocol().toLowerCase().equals("https"))
/*     */         {
/* 537 */           c();
/* 538 */           localObject2 = (HttpsURLConnection)url.openConnection();
/* 539 */           ((HttpsURLConnection)localObject2).setHostnameVerifier(l);
/* 540 */           localObject1 = localObject2;
/*     */         }
/*     */         else
/*     */         {
/* 544 */           localObject1 = (HttpURLConnection)url.openConnection();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 550 */         ((HttpURLConnection)localObject1).setDoOutput(true);
/* 551 */         OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(((HttpURLConnection)localObject1).getOutputStream());
/* 552 */         localOutputStreamWriter.write(data);
/* 553 */         localOutputStreamWriter.flush();
/* 554 */         localOutputStreamWriter.close();
/*     */         
/* 556 */         Object localObject2 = new BufferedReader(new InputStreamReader(((HttpURLConnection)localObject1).getInputStream()));
/*     */         
/*     */ 
/* 559 */         str1 = new String();
/* 560 */         String str2; while ((str2 = ((BufferedReader)localObject2).readLine()) != null) {
/* 561 */           str1 = str1 + str2;
/*     */         }
/* 563 */         ((BufferedReader)localObject2).close();
/*     */         
/* 565 */         RobotLog.i("Analytics took: " + (
/* 566 */           System.currentTimeMillis() - l1) + "ms");
/*     */       } catch (IOException localIOException1) {
/* 568 */         RobotLog.i("Analytics Failed to process command.");
/*     */       }
/*     */     }
/* 571 */     return str1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 579 */   static final HostnameVerifier l = new HostnameVerifier() {
/*     */     public boolean verify(String hostname, SSLSession session) {
/* 581 */       return true;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void c()
/*     */   {
/* 590 */     TrustManager[] arrayOfTrustManager = { new X509TrustManager() {
/*     */       public X509Certificate[] getAcceptedIssuers() {
/* 592 */         return new X509Certificate[0];
/*     */       }
/*     */       
/*     */ 
/*     */       public void checkClientTrusted(X509Certificate[] chain, String authType)
/*     */         throws CertificateException
/*     */       {}
/*     */       
/*     */       public void checkServerTrusted(X509Certificate[] chain, String authType)
/*     */         throws CertificateException
/*     */       {}
/*     */     } };
/*     */     try
/*     */     {
/* 606 */       SSLContext localSSLContext = SSLContext.getInstance("TLS");
/* 607 */       localSSLContext.init(null, arrayOfTrustManager, new SecureRandom());
/*     */       
/* 609 */       HttpsURLConnection.setDefaultSSLSocketFactory(localSSLContext.getSocketFactory());
/*     */     } catch (Exception localException) {
/* 611 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Analytics-release.jar!\classes.jar!\com\qualcomm\analytics\Analytics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */