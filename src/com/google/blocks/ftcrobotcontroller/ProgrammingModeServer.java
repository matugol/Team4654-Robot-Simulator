/*     */ package com.google.blocks.ftcrobotcontroller;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.res.AssetManager;
/*     */ import com.google.blocks.ProgrammingModeConnectionInfo;
/*     */ import com.google.blocks.ftcrobotcontroller.util.HardwareUtil;
/*     */ import com.google.blocks.ftcrobotcontroller.util.MimeTypesUtil;
/*     */ import com.google.blocks.ftcrobotcontroller.util.ProjectsUtil;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnection;
/*     */ import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
/*     */ import com.qualcomm.robotcore.wifi.NetworkType;
/*     */ import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
/*     */ import fi.iki.elonen.NanoHTTPD;
/*     */ import fi.iki.elonen.NanoHTTPD.Method;
/*     */ import fi.iki.elonen.NanoHTTPD.Response;
/*     */ import fi.iki.elonen.NanoHTTPD.Response.Status;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Map;
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
/*     */ public class ProgrammingModeServer
/*     */ {
/*     */   private static final int DEFAULT_PORT = 8080;
/*     */   private static final String URI_LIST = "/list";
/*     */   private static final String URI_HARDWARE = "/hardware";
/*     */   private static final String URI_TOOLBOX = "/toolbox";
/*     */   private static final String URI_LOAD = "/load";
/*     */   private static final String URI_SAVE = "/save";
/*     */   private static final String URI_RENAME = "/rename";
/*     */   private static final String URI_COPY = "/copy";
/*     */   private static final String URI_DELETE = "/delete";
/*     */   private static final String PARAM_PROJECT = "project";
/*     */   private static final String PARAM_NEWPROJECT = "newproject";
/*     */   private static final String PARAM_BLK = "blk";
/*     */   private static final String PARAM_JS = "js";
/*     */   private final Activity activity;
/*     */   private final NetworkType networkType;
/*     */   private final ProgrammingModeLog programmingModeLog;
/*     */   private final AssetManager assetManager;
/*     */   private final NanoHTTPD nanoHttpd;
/*     */   private long timeServerStartedMillis;
/*     */   private NetworkConnection networkConnection;
/*     */   private String networkName;
/*     */   private String passphrase;
/*     */   private InetAddress connectionOwnerAddress;
/*     */   private boolean serverIsAlive;
/*     */   
/*     */   public ProgrammingModeServer(Activity activity, NetworkType networkType, ProgrammingModeLog programmingModeLog)
/*     */   {
/*  73 */     this(8080, activity, networkType, programmingModeLog);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProgrammingModeServer(int port, Activity activity, NetworkType networkType, ProgrammingModeLog programmingModeLog)
/*     */   {
/*  81 */     this.activity = activity;
/*  82 */     this.networkType = networkType;
/*  83 */     this.programmingModeLog = programmingModeLog;
/*  84 */     this.assetManager = activity.getAssets();
/*  85 */     this.nanoHttpd = createNanoHTTPD(port);
/*     */   }
/*     */   
/*     */   public void start() throws IOException {
/*  89 */     this.nanoHttpd.start();
/*  90 */     this.timeServerStartedMillis = System.currentTimeMillis();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  95 */       this.networkConnection = NetworkConnectionFactory.getNetworkConnection(this.networkType, null);
/*  96 */       this.networkConnection.enable();
/*     */     } catch (NullPointerException e) {
/*  98 */       RobotLog.e("Cannot start Network Connection of type: " + this.networkType);
/*  99 */       this.networkConnection = null;
/*     */     }
/*     */     
/* 102 */     if (this.networkConnection != null) {
/* 103 */       if ((this.networkConnection instanceof WifiDirectAssistant)) {
/* 104 */         this.networkName = ((WifiDirectAssistant)this.networkConnection).getGroupNetworkName();
/*     */       } else {
/* 106 */         this.networkName = this.networkConnection.getConnectionOwnerName();
/*     */       }
/* 108 */       this.passphrase = this.networkConnection.getPassphrase();
/* 109 */       this.connectionOwnerAddress = this.networkConnection.getConnectionOwnerAddress();
/*     */     } else {
/* 111 */       this.networkName = null;
/* 112 */       this.passphrase = null;
/* 113 */       this.connectionOwnerAddress = null;
/*     */     }
/*     */     
/* 116 */     this.serverIsAlive = this.nanoHttpd.isAlive();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 120 */     this.nanoHttpd.stop();
/*     */     
/* 122 */     if (this.networkConnection != null) {
/* 123 */       this.networkConnection.disable();
/* 124 */       this.networkConnection = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public ProgrammingModeConnectionInfo getConnectionInformation() {
/* 129 */     String serverUrl = null;
/* 130 */     if (this.connectionOwnerAddress != null) {
/* 131 */       int port = this.nanoHttpd.getListeningPort();
/* 132 */       if (port != -1) {
/* 133 */         serverUrl = "http://" + this.connectionOwnerAddress.getHostAddress() + ":" + port;
/*     */       }
/*     */     }
/*     */     
/* 137 */     return new ProgrammingModeConnectionInfo(this.networkName, this.passphrase, serverUrl, this.serverIsAlive, this.timeServerStartedMillis);
/*     */   }
/*     */   
/*     */   private NanoHTTPD createNanoHTTPD(int port)
/*     */   {
/* 142 */     new NanoHTTPD(port)
/*     */     {
/*     */ 
/*     */ 
/*     */       public NanoHTTPD.Response serve(String uri, NanoHTTPD.Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files)
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/* 152 */           if (ProgrammingModeServer.this.programmingModeLog != null) {
/* 153 */             ProgrammingModeServer.this.programmingModeLog.addToLog(uri);
/*     */           }
/*     */           
/* 156 */           if (uri.equals("/")) {
/* 157 */             NanoHTTPD.Response response = newFixedLengthResponse(NanoHTTPD.Response.Status.REDIRECT, "text/plain", "");
/*     */             
/* 159 */             response.addHeader("Location", "/FtcProjects.html");
/* 160 */             return response;
/*     */           }
/*     */           
/* 163 */           if (uri.equals("/list")) {
/* 164 */             return ProgrammingModeServer.this.fetchProjects();
/*     */           }
/*     */           
/* 167 */           if (uri.equals("/hardware")) {
/* 168 */             return ProgrammingModeServer.this.fetchJavaScriptForHardware();
/*     */           }
/*     */           
/* 171 */           if (uri.equals("/toolbox")) {
/* 172 */             return ProgrammingModeServer.this.fetchToolbox();
/*     */           }
/*     */           
/* 175 */           if (uri.equals("/load")) {
/* 176 */             String project = (String)parms.get("project");
/* 177 */             if (project != null) {
/* 178 */               return ProgrammingModeServer.this.fetchBlocks(project);
/*     */             }
/* 180 */             return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: project parameter is required");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 186 */           if (uri.equals("/save")) {
/* 187 */             String project = (String)parms.get("project");
/* 188 */             String blk = (String)parms.get("blk");
/* 189 */             String js = (String)parms.get("js");
/* 190 */             if ((project != null) && (blk != null) && (js != null)) {
/* 191 */               return ProgrammingModeServer.this.saveProject(project, blk, js);
/*     */             }
/* 193 */             return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: project, blk, and js parameters are required");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 199 */           if (uri.equals("/rename")) {
/* 200 */             String oldProject = (String)parms.get("project");
/* 201 */             String newProject = (String)parms.get("newproject");
/* 202 */             if ((oldProject != null) && (newProject != null)) {
/* 203 */               return ProgrammingModeServer.this.renameProject(oldProject, newProject);
/*     */             }
/* 205 */             return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: oldProject and newProject parameters are required");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 211 */           if (uri.equals("/copy")) {
/* 212 */             String oldProject = (String)parms.get("project");
/* 213 */             String newProject = (String)parms.get("newproject");
/* 214 */             if ((oldProject != null) && (newProject != null)) {
/* 215 */               return ProgrammingModeServer.this.copyProject(oldProject, newProject);
/*     */             }
/* 217 */             return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: oldProject and newProject parameters are required");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 223 */           if (uri.equals("/delete")) {
/* 224 */             String project = (String)parms.get("project");
/* 225 */             if (project != null) {
/* 226 */               return ProgrammingModeServer.this.deleteProjects(project);
/*     */             }
/* 228 */             return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: project, blk, and js parameters are required");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 235 */           String path = uri.startsWith("/") ? uri.substring(1) : uri;
/* 236 */           return ProgrammingModeServer.this.serveAsset(path);
/*     */         } catch (IOException e) {
/* 238 */           RobotLog.logStacktrace(e); }
/* 239 */         return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Internal Error");
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private NanoHTTPD.Response fetchProjects()
/*     */     throws IOException
/*     */   {
/* 250 */     String jsonProjects = ProjectsUtil.fetchProjectsWithBlocks();
/* 251 */     return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", jsonProjects);
/*     */   }
/*     */   
/*     */ 
/*     */   private NanoHTTPD.Response fetchJavaScriptForHardware()
/*     */     throws IOException
/*     */   {
/* 258 */     String jsHardware = HardwareUtil.fetchJavaScriptForHardware(this.activity);
/* 259 */     return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/javascript", jsHardware);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private NanoHTTPD.Response fetchToolbox()
/*     */     throws IOException
/*     */   {
/* 267 */     String xmlToolbox = HardwareUtil.fetchToolbox(this.activity, this.assetManager);
/* 268 */     return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", xmlToolbox);
/*     */   }
/*     */   
/*     */ 
/*     */   private NanoHTTPD.Response fetchBlocks(String projectName)
/*     */     throws IOException
/*     */   {
/* 275 */     String blkContent = ProjectsUtil.fetchBlocks(projectName, this.activity);
/* 276 */     return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", blkContent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private NanoHTTPD.Response saveProject(String projectName, String blkContent, String jsContent)
/*     */     throws IOException
/*     */   {
/* 284 */     ProjectsUtil.saveProject(projectName, blkContent, jsContent);
/* 285 */     return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private NanoHTTPD.Response renameProject(String oldProjectName, String newProjectName)
/*     */     throws IOException
/*     */   {
/* 293 */     ProjectsUtil.renameProject(oldProjectName, newProjectName);
/* 294 */     return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private NanoHTTPD.Response copyProject(String oldProjectName, String newProjectName)
/*     */     throws IOException
/*     */   {
/* 302 */     ProjectsUtil.copyProject(oldProjectName, newProjectName);
/* 303 */     return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "");
/*     */   }
/*     */   
/*     */ 
/*     */   private NanoHTTPD.Response deleteProjects(String csvProjectNames)
/*     */     throws IOException
/*     */   {
/* 310 */     String[] projectNames = csvProjectNames.split(",");
/* 311 */     ProjectsUtil.deleteProjects(projectNames);
/* 312 */     return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "");
/*     */   }
/*     */   
/*     */ 
/*     */   private NanoHTTPD.Response serveAsset(String path)
/*     */     throws IOException
/*     */   {
/* 319 */     String mimeType = "text/plain";
/* 320 */     int lastDot = path.lastIndexOf(".");
/* 321 */     if (lastDot != -1) {
/* 322 */       String ext = path.substring(lastDot + 1);
/* 323 */       mimeType = MimeTypesUtil.getMimeType(ext);
/* 324 */       if (mimeType == null) {
/* 325 */         throw new IllegalStateException("Mime type unknown for file extension " + ext);
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 331 */       inputStream = this.assetManager.open(path);
/*     */     } catch (IOException e) { InputStream inputStream;
/* 333 */       return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "");
/*     */     }
/*     */     InputStream inputStream;
/* 336 */     return NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, mimeType, inputStream);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ftcrobotcontroller\ProgrammingModeServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */