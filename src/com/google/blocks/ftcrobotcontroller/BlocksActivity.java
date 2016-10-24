/*     */ package com.google.blocks.ftcrobotcontroller;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.res.Configuration;
/*     */ import android.os.Bundle;
/*     */ import android.webkit.JavascriptInterface;
/*     */ import android.webkit.WebChromeClient;
/*     */ import android.webkit.WebSettings;
/*     */ import android.webkit.WebView;
/*     */ import com.google.blocks.R.id;
/*     */ import com.google.blocks.R.layout;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareUtil;
/*     */ import com.google.blocks.ftcrobotcontroller.util.ProjectsUtil;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.net.URLEncoder;
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
/*     */ public class BlocksActivity
/*     */   extends Activity
/*     */ {
/*     */   private WebView webView;
/*     */   public static final String launchIntent = "com.google.blocks.ftcrobotcontroller.BlocksActivity.intent.action.Launch";
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  37 */     super.onCreate(savedInstanceState);
/*     */     
/*  39 */     setContentView(R.layout.activity_blocks);
/*     */     
/*  41 */     this.webView = ((WebView)findViewById(R.id.webViewBlockly));
/*  42 */     this.webView.setWebChromeClient(new WebChromeClient());
/*     */     
/*  44 */     WebSettings webSettings = this.webView.getSettings();
/*  45 */     webSettings.setJavaScriptEnabled(true);
/*     */     
/*  47 */     this.webView.addJavascriptInterface(new BlocksIO(null), "blocksIO");
/*  48 */     this.webView.loadUrl("file:///android_asset/FtcProjects.html");
/*     */   }
/*     */   
/*     */   public void onConfigurationChanged(Configuration newConfig)
/*     */   {
/*  53 */     super.onConfigurationChanged(newConfig);
/*     */   }
/*     */   
/*     */   private class BlocksIO {
/*     */     private BlocksIO() {}
/*     */     
/*     */     @JavascriptInterface
/*     */     public String fetchProjects() {
/*     */       try {
/*  62 */         return ProjectsUtil.fetchProjectsWithBlocks();
/*     */       } catch (Exception e) {}
/*  64 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     @JavascriptInterface
/*     */     public void openProjectBlocks(final String projectName)
/*     */     {
/*  71 */       BlocksActivity.this.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/*     */           try {
/*  75 */             BlocksActivity.this.webView.loadUrl("file:///android_asset/FtcBlocks.html?project=" + URLEncoder.encode(projectName, "UTF-8"));
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/*  79 */             RobotLog.e("BlocksActivity.openProjectBlocks - caught " + e);
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     @JavascriptInterface
/*     */     public String fetchJavaScriptForHardware()
/*     */     {
/*     */       try {
/*  89 */         return HardwareUtil.fetchJavaScriptForHardware(BlocksActivity.this);
/*     */       } catch (Exception e) {}
/*  91 */       return null;
/*     */     }
/*     */     
/*     */     @JavascriptInterface
/*     */     public String fetchToolbox()
/*     */     {
/*     */       try
/*     */       {
/*  99 */         return HardwareUtil.fetchToolbox(BlocksActivity.this, BlocksActivity.this.getAssets());
/*     */       } catch (Exception e) {}
/* 101 */       return null;
/*     */     }
/*     */     
/*     */     @JavascriptInterface
/*     */     public String fetchBlocks(String projectName)
/*     */     {
/*     */       try
/*     */       {
/* 109 */         return ProjectsUtil.fetchBlocks(projectName, BlocksActivity.this);
/*     */       } catch (Exception e) {}
/* 111 */       return null;
/*     */     }
/*     */     
/*     */     @JavascriptInterface
/*     */     public boolean saveProject(String projectName, String blkContent, String jsContent)
/*     */     {
/*     */       try
/*     */       {
/* 119 */         ProjectsUtil.saveProject(projectName, blkContent, jsContent);
/* 120 */         return true;
/*     */       } catch (Exception e) {}
/* 122 */       return false;
/*     */     }
/*     */     
/*     */     @JavascriptInterface
/*     */     public boolean renameProject(String oldProjectName, String newProjectName)
/*     */     {
/*     */       try
/*     */       {
/* 130 */         ProjectsUtil.renameProject(oldProjectName, newProjectName);
/* 131 */         return true;
/*     */       } catch (Exception e) {}
/* 133 */       return false;
/*     */     }
/*     */     
/*     */     @JavascriptInterface
/*     */     public boolean copyProject(String oldProjectName, String newProjectName)
/*     */     {
/*     */       try
/*     */       {
/* 141 */         ProjectsUtil.copyProject(oldProjectName, newProjectName);
/* 142 */         return true;
/*     */       } catch (Exception e) {}
/* 144 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     @JavascriptInterface
/*     */     public boolean deleteProjects(String csvProjectNames)
/*     */     {
/* 151 */       String[] projectNames = csvProjectNames.split(",");
/*     */       try {
/* 153 */         ProjectsUtil.deleteProjects(projectNames);
/* 154 */         return true;
/*     */       } catch (Exception e) {}
/* 156 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\BlocksActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */