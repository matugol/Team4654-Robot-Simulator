/*     */ package com.google.blocks.ftcrobotcontroller.runtime;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.webkit.JavascriptInterface;
/*     */ import android.webkit.WebSettings;
/*     */ import android.webkit.WebView;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareItem;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareItemMap;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareType;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareUtil;
/*     */ import com.google.blocks.ftcrobotcontroller.util.ProjectsUtil;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class BlocksOpMode
/*     */   extends LinearOpMode
/*     */ {
/*     */   public static final String BLOCKS_OP_MODE_IDENTIFIER = "blocksOpMode";
/*     */   public static final String COLOR_IDENTIFIER = "colorAccess";
/*     */   public static final String ELAPSED_TIME_IDENTIFIER = "elapsedTimeAccess";
/*     */   public static final String GAMEPAD_1_IDENTIFIER = "gamepad1";
/*     */   public static final String GAMEPAD_2_IDENTIFIER = "gamepad2";
/*     */   public static final String LINEAR_OP_MODE_IDENTIFIER = "linearOpMode";
/*     */   public static final String TELEMETRY_IDENTIFIER = "telemetry";
/*     */   private static Activity activity;
/*     */   private static WebView webView;
/*     */   private final String project;
/*     */   
/*     */   private BlocksOpMode(String project)
/*     */   {
/*  48 */     this.project = project;
/*     */   }
/*     */   
/*     */   public void runOpMode() throws InterruptedException
/*     */   {
/*  53 */     RobotLog.i("BlocksOpMode - start of runOpMode for " + this.project);
/*     */     
/*  55 */     final Object scriptFinishedLock = new Object();
/*  56 */     final List<Access> javascriptInterfaces = new ArrayList();
/*     */     
/*  58 */     synchronized (scriptFinishedLock) {
/*  59 */       activity.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/*     */           try {
/*  63 */             BlocksOpMode.this.addJavascriptInterfaces(scriptFinishedLock, javascriptInterfaces);
/*  64 */             BlocksOpMode.this.loadScript();
/*     */           } catch (Exception e) {
/*  66 */             RobotLog.e("BlocksOpMode - run 1 - caught " + e);
/*     */             
/*     */ 
/*  69 */             if (e.getStackTrace() != null) {
/*  70 */               RobotLog.logStacktrace(e);
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */ 
/*  78 */       });
/*  79 */       boolean interrupted = false;
/*     */       try {
/*  81 */         scriptFinishedLock.wait();
/*     */       } catch (InterruptedException e) {
/*  83 */         interrupted = true;
/*     */       }
/*     */       
/*  86 */       activity.runOnUiThread(new Runnable()
/*     */       {
/*     */ 
/*     */         public void run()
/*     */         {
/*     */           try
/*     */           {
/*  93 */             RobotLog.e("BlocksOpMode - run 2 - before removeJavascriptInterfaces");
/*  94 */             BlocksOpMode.this.removeJavascriptInterfaces(javascriptInterfaces);
/*  95 */             RobotLog.e("BlocksOpMode - run 2 - after removeJavascriptInterfaces");
/*     */             
/*  97 */             RobotLog.e("BlocksOpMode - run 2 - before clearScript");
/*  98 */             BlocksOpMode.this.clearScript();
/*  99 */             RobotLog.e("BlocksOpMode - run 2 - after clearScript");
/*     */           } catch (Exception e) {
/* 101 */             RobotLog.e("BlocksOpMode - run 2 - caught " + e);
/*     */             
/*     */ 
/* 104 */             if (e.getStackTrace() != null) {
/* 105 */               RobotLog.logStacktrace(e);
/*     */             }
/*     */           }
/*     */         }
/*     */       });
/*     */       
/* 111 */       if (interrupted) {
/* 112 */         Thread.currentThread().interrupt();
/*     */       }
/* 114 */       RobotLog.i("BlocksOpMode - end of runOpMode for " + this.project);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addJavascriptInterfaces(Object scriptFinishedLock, List<Access> javascriptInterfaces) {
/* 119 */     javascriptInterfaces.add(new JavascriptAccess("blocksOpMode", scriptFinishedLock, null));
/* 120 */     javascriptInterfaces.add(new LinearOpModeAccess("linearOpMode", this));
/* 121 */     javascriptInterfaces.add(new TelemetryAccess("telemetry", this.telemetry));
/* 122 */     javascriptInterfaces.add(new GamepadAccess("gamepad1", this.gamepad1));
/* 123 */     javascriptInterfaces.add(new GamepadAccess("gamepad2", this.gamepad2));
/* 124 */     javascriptInterfaces.add(new ColorAccess("colorAccess"));
/* 125 */     javascriptInterfaces.add(new ElapsedTimeAccess("elapsedTimeAccess"));
/*     */     
/* 127 */     HardwareItemMap hardwareItemMap = HardwareItemMap.newHardwareItemMap(this.hardwareMap);
/*     */     
/* 129 */     for (Iterator i$ = HardwareUtil.getSupportedHardwareTypes().iterator(); i$.hasNext();) { hardwareType = (HardwareType)i$.next();
/* 130 */       if (hardwareItemMap.contains(hardwareType)) {
/* 131 */         for (HardwareItem hardwareItem : hardwareItemMap.getHardwareItems(hardwareType)) {
/* 132 */           javascriptInterfaces.add(HardwareAccess.newHardwareAccess(hardwareType, this.hardwareMap, hardwareItem));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     HardwareType hardwareType;
/* 138 */     for (Access access : javascriptInterfaces) {
/* 139 */       webView.addJavascriptInterface(access, access.identifier);
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeJavascriptInterfaces(List<Access> javascriptInterfaces) {
/* 144 */     for (Access access : javascriptInterfaces) {
/* 145 */       webView.removeJavascriptInterface(access.identifier);
/*     */     }
/*     */   }
/*     */   
/*     */   private class JavascriptAccess extends Access {
/*     */     private final Object scriptFinishedLock;
/*     */     
/*     */     private JavascriptAccess(String identifier, Object scriptFinishedLock) {
/* 153 */       super();
/* 154 */       this.scriptFinishedLock = scriptFinishedLock;
/*     */     }
/*     */     
/*     */     @JavascriptInterface
/*     */     public void scriptFinished()
/*     */     {
/* 160 */       synchronized (this.scriptFinishedLock) {
/* 161 */         this.scriptFinishedLock.notifyAll();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadScript() throws IOException {
/* 167 */     String javaScript = ProjectsUtil.fetchJavaScript(this.project);
/* 168 */     String html = "<html><body onload='callRunOpMode()'><script type='text/javascript'>\nfunction callRunOpMode() {\n  runOpMode();\n  blocksOpMode.scriptFinished();\n}\n\n" + javaScript + "\n" + "</script></body></html>\n";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 177 */     webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
/*     */   }
/*     */   
/*     */   private void clearScript()
/*     */   {
/* 182 */     webView.loadDataWithBaseURL(null, "", "text/html", "UTF-8", null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setActivityAndWebView(Activity a, WebView wv)
/*     */   {
/* 190 */     activity = a;
/* 191 */     webView = wv;
/* 192 */     webView.getSettings().setJavaScriptEnabled(true);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void registerAll(OpModeManager manager)
/*     */   {
/*     */     try
/*     */     {
/* 200 */       String[] projects = ProjectsUtil.fetchProjectsWithJavaScript();
/* 201 */       for (String project : projects) {
/* 202 */         manager.register(project, new BlocksOpMode(project));
/*     */       }
/*     */     } catch (Exception e) {
/* 205 */       RobotLog.logStacktrace(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\runtime\BlocksOpMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */