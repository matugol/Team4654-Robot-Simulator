/*     */ package com.qualcomm.wirelessp2p;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.net.wifi.WifiManager;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.RunShellCommand;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
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
/*     */ public class WifiDirectChannelSelection
/*     */ {
/*     */   public static final int INVALID = -1;
/*     */   private final String a;
/*     */   private final String b;
/*     */   private final String c;
/*     */   private final WifiManager d;
/*  61 */   private final RunShellCommand e = new RunShellCommand();
/*     */   
/*     */   public WifiDirectChannelSelection(Context context, WifiManager wifiManager)
/*     */   {
/*  65 */     this.a = (context.getFilesDir().getAbsolutePath() + "/");
/*  66 */     this.d = wifiManager;
/*     */     
/*  68 */     this.b = (this.a + "get_current_wifi_direct_staus");
/*  69 */     this.c = (this.a + "config_wifi_direct");
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void config(int wifiClass, int wifiChannel)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 51	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:d	Landroid/net/wifi/WifiManager;
/*     */     //   4: iconst_0
/*     */     //   5: invokevirtual 54	android/net/wifi/WifiManager:setWifiEnabled	(Z)Z
/*     */     //   8: pop
/*     */     //   9: aload_0
/*     */     //   10: invokespecial 63	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:c	()V
/*     */     //   13: aload_0
/*     */     //   14: getfield 52	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:e	Lcom/qualcomm/robotcore/util/RunShellCommand;
/*     */     //   17: aload_0
/*     */     //   18: getfield 49	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:b	Ljava/lang/String;
/*     */     //   21: invokevirtual 59	com/qualcomm/robotcore/util/RunShellCommand:runAsRoot	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   24: pop
/*     */     //   25: aload_0
/*     */     //   26: iload_1
/*     */     //   27: iload_2
/*     */     //   28: invokespecial 61	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:a	(II)V
/*     */     //   31: aload_0
/*     */     //   32: invokespecial 62	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:b	()V
/*     */     //   35: aload_0
/*     */     //   36: getfield 52	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:e	Lcom/qualcomm/robotcore/util/RunShellCommand;
/*     */     //   39: aload_0
/*     */     //   40: getfield 50	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:c	Ljava/lang/String;
/*     */     //   43: invokevirtual 59	com/qualcomm/robotcore/util/RunShellCommand:runAsRoot	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   46: pop
/*     */     //   47: aload_0
/*     */     //   48: getfield 51	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:d	Landroid/net/wifi/WifiManager;
/*     */     //   51: iconst_1
/*     */     //   52: invokevirtual 54	android/net/wifi/WifiManager:setWifiEnabled	(Z)Z
/*     */     //   55: pop
/*     */     //   56: aload_0
/*     */     //   57: invokespecial 64	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:d	()V
/*     */     //   60: goto +10 -> 70
/*     */     //   63: astore_3
/*     */     //   64: aload_0
/*     */     //   65: invokespecial 64	com/qualcomm/wirelessp2p/WifiDirectChannelSelection:d	()V
/*     */     //   68: aload_3
/*     */     //   69: athrow
/*     */     //   70: return
/*     */     // Line number table:
/*     */     //   Java source line #84	-> byte code offset #0
/*     */     //   Java source line #86	-> byte code offset #9
/*     */     //   Java source line #88	-> byte code offset #13
/*     */     //   Java source line #90	-> byte code offset #25
/*     */     //   Java source line #91	-> byte code offset #31
/*     */     //   Java source line #93	-> byte code offset #35
/*     */     //   Java source line #95	-> byte code offset #47
/*     */     //   Java source line #97	-> byte code offset #56
/*     */     //   Java source line #98	-> byte code offset #60
/*     */     //   Java source line #97	-> byte code offset #63
/*     */     //   Java source line #99	-> byte code offset #70
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	71	0	this	WifiDirectChannelSelection
/*     */     //   0	71	1	wifiClass	int
/*     */     //   0	71	2	wifiChannel	int
/*     */     //   63	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	56	63	finally
/*     */   }
/*     */   
/*     */   private int a()
/*     */     throws RuntimeException
/*     */   {
/* 103 */     String str1 = this.e.run("/system/bin/ps");
/* 104 */     for (String str2 : str1.split("\n")) {
/* 105 */       if (str2.contains("wpa_supplicant")) {
/* 106 */         String[] arrayOfString2 = str2.split("\\s+");
/* 107 */         return Integer.parseInt(arrayOfString2[1]);
/*     */       }
/*     */     }
/*     */     
/* 111 */     throw new RuntimeException("could not find wpa_supplicant PID");
/*     */   }
/*     */   
/*     */   private void b() {
/*     */     try {
/* 116 */       char[] arrayOfChar = new char['က'];
/*     */       
/* 118 */       FileReader localFileReader = new FileReader(this.a + "wpa_supplicant.conf");
/* 119 */       int i = localFileReader.read(arrayOfChar);
/* 120 */       localFileReader.close();
/*     */       
/* 122 */       String str = new String(arrayOfChar, 0, i);
/* 123 */       RobotLog.v("WPA FILE: \n" + str);
/*     */       
/*     */ 
/* 126 */       str = str.replaceAll("(?s)network\\s*=\\{.*\\}", "");
/*     */       
/*     */ 
/* 129 */       str = str.replaceAll("(?m)^\\s+$", "");
/*     */       
/* 131 */       RobotLog.v("WPA REPLACE: \n" + str);
/*     */       
/* 133 */       FileWriter localFileWriter = new FileWriter(this.a + "wpa_supplicant.conf");
/* 134 */       localFileWriter.write(str);
/* 135 */       localFileWriter.close();
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 137 */       RobotLog.e("File not found: " + localFileNotFoundException.toString());
/* 138 */       localFileNotFoundException.printStackTrace();
/*     */     } catch (IOException localIOException) {
/* 140 */       RobotLog.e("FIO exception: " + localIOException.toString());
/* 141 */       localIOException.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(int paramInt1, int paramInt2) {
/*     */     try {
/* 147 */       char[] arrayOfChar = new char[' '];
/*     */       
/* 149 */       FileReader localFileReader = new FileReader(this.a + "p2p_supplicant.conf");
/* 150 */       int i = localFileReader.read(arrayOfChar);
/* 151 */       localFileReader.close();
/*     */       
/* 153 */       String str = new String(arrayOfChar, 0, i);
/* 154 */       RobotLog.v("P2P ORIG FILE: \n" + str);
/*     */       
/*     */ 
/* 157 */       str = str.replaceAll("p2p_listen_reg_class\\w*=.*", "");
/* 158 */       str = str.replaceAll("p2p_listen_channel\\w*=.*", "");
/* 159 */       str = str.replaceAll("p2p_oper_reg_class\\w*=.*", "");
/* 160 */       str = str.replaceAll("p2p_oper_channel\\w*=.*", "");
/* 161 */       str = str.replaceAll("p2p_pref_chan\\w*=.*", "");
/*     */       
/*     */ 
/* 164 */       str = str.replaceAll("(?s)network\\s*=\\{.*\\}", "");
/*     */       
/*     */ 
/* 167 */       str = str.replaceAll("(?m)^\\s+$", "");
/*     */       
/*     */ 
/* 170 */       if ((paramInt1 != -1) && (paramInt2 != -1)) {
/* 171 */         str = str + "p2p_oper_reg_class=" + paramInt1 + "\n";
/* 172 */         str = str + "p2p_oper_channel=" + paramInt2 + "\n";
/* 173 */         str = str + "p2p_pref_chan=" + paramInt1 + ":" + paramInt2 + "\n";
/*     */       }
/*     */       
/* 176 */       RobotLog.v("P2P NEW FILE: \n" + str);
/*     */       
/* 178 */       FileWriter localFileWriter = new FileWriter(this.a + "p2p_supplicant.conf");
/* 179 */       localFileWriter.write(str);
/* 180 */       localFileWriter.close();
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 182 */       RobotLog.e("File not found: " + localFileNotFoundException.toString());
/* 183 */       localFileNotFoundException.printStackTrace();
/*     */     } catch (IOException localIOException) {
/* 185 */       RobotLog.e("FIO exception: " + localIOException.toString());
/* 186 */       localIOException.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void c()
/*     */     throws IOException
/*     */   {
/* 199 */     String str1 = String.format("cp /data/misc/wifi/wpa_supplicant.conf %s/wpa_supplicant.conf \ncp /data/misc/wifi/p2p_supplicant.conf %s/p2p_supplicant.conf \nchmod 666 %s/*supplicant* \n", new Object[] { this.a, this.a, this.a });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 205 */     String str2 = String.format("cp %s/p2p_supplicant.conf /data/misc/wifi/p2p_supplicant.conf \ncp %s/wpa_supplicant.conf /data/misc/wifi/wpa_supplicant.conf \nrm %s/*supplicant* \nchown system.wifi /data/misc/wifi/wpa_supplicant.conf \nchown system.wifi /data/misc/wifi/p2p_supplicant.conf \nkill -HUP %d \n", new Object[] { this.a, this.a, this.a, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 212 */       Integer.valueOf(a()) });
/*     */     
/*     */ 
/*     */ 
/* 216 */     FileWriter localFileWriter = new FileWriter(this.b);
/* 217 */     localFileWriter.write(str1);
/* 218 */     localFileWriter.close();
/*     */     
/* 220 */     localFileWriter = new FileWriter(this.c);
/* 221 */     localFileWriter.write(str2);
/* 222 */     localFileWriter.close();
/*     */     
/* 224 */     this.e.run("chmod 700 " + this.b);
/* 225 */     this.e.run("chmod 700 " + this.c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void d()
/*     */   {
/* 236 */     File localFile = new File(this.b);
/* 237 */     localFile.delete();
/*     */     
/* 239 */     localFile = new File(this.c);
/* 240 */     localFile.delete();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\WirelessP2p-release.jar!\classes.jar!\com\qualcomm\wirelessp2p\WifiDirectChannelSelection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */