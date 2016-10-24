/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.os.Environment;
/*     */ import android.text.Spannable;
/*     */ import android.text.SpannableString;
/*     */ import android.text.style.ForegroundColorSpan;
/*     */ import android.widget.ScrollView;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public class ViewLogsActivity
/*     */   extends Activity
/*     */ {
/*     */   public static final String launchIntent = "com.qualcomm.ftccommon.ViewLogsActivity.intent.action.Launch";
/*     */   TextView textAdbLogs;
/*  65 */   int DEFAULT_NUMBER_OF_LINES = 300;
/*     */   
/*     */   public static final String FILENAME = "org.firstinspires.ftc.ftccommon.logFilename";
/*  68 */   String filepath = " ";
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  72 */     super.onCreate(savedInstanceState);
/*  73 */     setContentView(R.layout.activity_view_logs);
/*     */     
/*  75 */     this.textAdbLogs = ((TextView)findViewById(R.id.textAdbLogs));
/*     */     
/*  77 */     final ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView);
/*  78 */     scrollView.post(new Runnable()
/*     */     {
/*     */       public void run() {
/*  81 */         scrollView.fullScroll(130);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/*  88 */     super.onStart();
/*     */     
/*  90 */     Intent intent = getIntent();
/*  91 */     Serializable extra = intent.getSerializableExtra("org.firstinspires.ftc.ftccommon.logFilename");
/*  92 */     if (extra != null) {
/*  93 */       this.filepath = ((String)extra);
/*     */     }
/*  95 */     runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/*  99 */           String output = ViewLogsActivity.this.readNLines(ViewLogsActivity.this.DEFAULT_NUMBER_OF_LINES);
/* 100 */           Spannable colorized = ViewLogsActivity.this.colorize(output);
/* 101 */           ViewLogsActivity.this.textAdbLogs.setText(colorized);
/*     */         } catch (IOException e) {
/* 103 */           RobotLog.e(e.toString());
/* 104 */           ViewLogsActivity.this.textAdbLogs.setText("File not found: " + ViewLogsActivity.this.filepath);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public String readNLines(int n) throws IOException {
/* 111 */     File sdcard = Environment.getExternalStorageDirectory();
/* 112 */     File file = new File(this.filepath);
/* 113 */     BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
/* 114 */     String[] ringBuffer = new String[n];
/* 115 */     int totalLines = 0;
/* 116 */     String line = null;
/*     */     
/* 118 */     while ((line = bufferedReader.readLine()) != null) {
/* 119 */       ringBuffer[(totalLines % ringBuffer.length)] = line;
/* 120 */       totalLines++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 126 */     int start = totalLines - n;
/* 127 */     if (start < 0) {
/* 128 */       start = 0;
/*     */     }
/*     */     
/* 131 */     String output = "";
/* 132 */     for (int i = start; i < totalLines; i++)
/*     */     {
/* 134 */       int index = i % ringBuffer.length;
/* 135 */       String currentLine = ringBuffer[index];
/* 136 */       output = output + currentLine + "\n";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 141 */     int mostRecentIndex = output.lastIndexOf("--------- beginning");
/* 142 */     if (mostRecentIndex < 0)
/*     */     {
/* 144 */       return output;
/*     */     }
/* 146 */     return output.substring(mostRecentIndex);
/*     */   }
/*     */   
/*     */   private Spannable colorize(String output)
/*     */   {
/* 151 */     Spannable span = new SpannableString(output);
/* 152 */     String[] lines = output.split("\\n");
/* 153 */     int currentStringIndex = 0;
/* 154 */     for (String line : lines) {
/* 155 */       if ((line.contains("E/RobotCore")) || (line.contains("### ERROR: "))) {
/* 156 */         span.setSpan(new ForegroundColorSpan(-65536), currentStringIndex, currentStringIndex + line.length(), 33);
/*     */       }
/*     */       
/*     */ 
/* 160 */       currentStringIndex += line.length();
/* 161 */       currentStringIndex++;
/*     */     }
/*     */     
/* 164 */     return span;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\ViewLogsActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */