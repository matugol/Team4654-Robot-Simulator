/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import com.qualcomm.ftccommon.configuration.RobotConfigFile;
/*     */ import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
/*     */ import com.qualcomm.hardware.HardwareFactory;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoop;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
/*     */ import com.qualcomm.robotcore.hardware.configuration.WriteXMLFileHandler;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
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
/*     */ public abstract class FtcEventLoopBase
/*     */   implements EventLoop
/*     */ {
/*     */   public static final String TAG = "FtcEventLoop";
/* 128 */   protected NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
/*     */   protected Activity activityContext;
/*     */   protected RobotConfigFileManager robotCfgFileMgr;
/*     */   protected FtcEventLoopHandler ftcEventLoopHandler;
/* 132 */   protected boolean runningOnDriverStation = false;
/*     */   
/*     */ 
/*     */   protected final ProgrammingModeController programmingModeController;
/*     */   
/*     */ 
/*     */ 
/*     */   protected FtcEventLoopBase(HardwareFactory hardwareFactory, UpdateUI.Callback callback, Activity activityContext, ProgrammingModeController programmingModeController)
/*     */   {
/* 141 */     this.activityContext = activityContext;
/* 142 */     this.robotCfgFileMgr = new RobotConfigFileManager(activityContext);
/* 143 */     this.ftcEventLoopHandler = new FtcEventLoopHandler(hardwareFactory, callback, activityContext);
/* 144 */     this.programmingModeController = programmingModeController;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallbackResult processCommand(Command command)
/*     */     throws InterruptedException, RobotCoreException
/*     */   {
/* 154 */     CallbackResult result = CallbackResult.HANDLED;
/*     */     
/* 156 */     String name = command.getName();
/* 157 */     String extra = command.getExtra();
/*     */     
/* 159 */     if (name.equals("CMD_RESTART_ROBOT"))
/*     */     {
/* 161 */       handleCommandRestartRobot();
/*     */     }
/* 163 */     else if (name.equals("CMD_REQUEST_CONFIGURATIONS"))
/*     */     {
/* 165 */       handleCommandRequestConfigurations();
/*     */     }
/* 167 */     else if (name.equals("CMD_REQUEST_CONFIGURATION_TEMPLATES"))
/*     */     {
/* 169 */       handleCommandRequestConfigurationTemplates();
/*     */     }
/* 171 */     else if (name.equals("CMD_REQUEST_PARTICULAR_CONFIGURATION"))
/*     */     {
/* 173 */       handleCommandRequestParticularConfiguration(extra);
/*     */     }
/* 175 */     else if (name.equals("CMD_ACTIVATE_CONFIGURATION"))
/*     */     {
/* 177 */       handleCommandActivateConfiguration(extra);
/*     */     }
/* 179 */     else if (name.equals("CMD_REQUEST_ACTIVE_CONFIGURATION"))
/*     */     {
/* 181 */       handleCommandRequestActiveConfiguration(extra);
/*     */     }
/* 183 */     else if (name.equals("CMD_SAVE_CONFIGURATION"))
/*     */     {
/* 185 */       handleCommandSaveConfiguration(extra);
/*     */     }
/* 187 */     else if (name.equals("CMD_DELETE_CONFIGURATION"))
/*     */     {
/* 189 */       handleCommandDeleteConfiguration(extra);
/*     */     }
/* 191 */     else if (name.equals("CMD_START_PROGRAMMING_MODE"))
/*     */     {
/* 193 */       handleCommandStartProgrammingMode();
/*     */     }
/* 195 */     else if (name.equals("CMD_STOP_PROGRAMMING_MODE"))
/*     */     {
/* 197 */       handleCommandStopProgrammingMode();
/*     */     }
/*     */     else
/*     */     {
/* 201 */       result = CallbackResult.NOT_HANDLED;
/*     */     }
/* 203 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void handleCommandActivateConfiguration(String data)
/*     */   {
/* 209 */     RobotConfigFile cfgFile = this.robotCfgFileMgr.getConfigFromString(data);
/* 210 */     this.robotCfgFileMgr.setActiveConfigAndUpdateUI(this.runningOnDriverStation, cfgFile);
/*     */   }
/*     */   
/*     */   protected void handleCommandRequestActiveConfiguration(String extra)
/*     */   {
/* 215 */     RobotConfigFile configFile = this.robotCfgFileMgr.getActiveConfig();
/* 216 */     String serialized = configFile.toString();
/* 217 */     this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_ACTIVE_CONFIGURATION_RESP", serialized));
/*     */   }
/*     */   
/*     */   protected void handleCommandRestartRobot()
/*     */   {
/* 222 */     this.ftcEventLoopHandler.restartRobot();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleCommandRequestParticularConfiguration(String data)
/*     */   {
/* 230 */     RobotConfigFile file = this.robotCfgFileMgr.getConfigFromString(data);
/* 231 */     ReadXMLFileHandler parser = new ReadXMLFileHandler();
/*     */     
/* 233 */     if (file.isNoConfig())
/*     */     {
/*     */ 
/* 236 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 241 */       WriteXMLFileHandler writeXMLFileHandler = new WriteXMLFileHandler(this.activityContext);
/* 242 */       ArrayList<ControllerConfiguration> deviceList = (ArrayList)parser.parse(file.getXml());
/* 243 */       String xmlData = writeXMLFileHandler.toXml(deviceList);
/* 244 */       RobotLog.vv("FtcConfigTag", "FtcEventLoop: handleCommandRequestParticularConfigFile, data: " + xmlData);
/* 245 */       this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_PARTICULAR_CONFIGURATION_RESP", xmlData));
/*     */     }
/*     */     catch (RobotCoreException e)
/*     */     {
/* 249 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void handleCommandDeleteConfiguration(String fileInfo)
/*     */   {
/* 255 */     RobotConfigFile cfgFile = this.robotCfgFileMgr.getConfigFromString(fileInfo);
/* 256 */     File file = RobotConfigFileManager.getFullPath(cfgFile.getName());
/* 257 */     if (!file.delete())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 263 */       DbgLog.error("Tried to delete a file that does not exist: " + cfgFile.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void handleCommandSaveConfiguration(String fileInfo)
/*     */   {
/* 269 */     String[] fileInfoArray = fileInfo.split(";");
/*     */     try
/*     */     {
/* 272 */       RobotConfigFile cfgFile = this.robotCfgFileMgr.getConfigFromString(fileInfoArray[0]);
/* 273 */       this.robotCfgFileMgr.writeToFile(cfgFile, false, fileInfoArray[1]);
/* 274 */       this.robotCfgFileMgr.setActiveConfigAndUpdateUI(false, cfgFile);
/*     */     }
/*     */     catch (RobotCoreException|IOException e)
/*     */     {
/* 278 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleCommandRequestConfigurations()
/*     */   {
/* 287 */     ArrayList<RobotConfigFile> fileList = this.robotCfgFileMgr.getXMLFiles();
/* 288 */     String objsSerialized = RobotConfigFileManager.serializeXMLConfigList(fileList);
/* 289 */     this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATIONS_RESP", objsSerialized));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleCommandRequestConfigurationTemplates()
/*     */   {
/* 297 */     ArrayList<RobotConfigFile> fileList = this.robotCfgFileMgr.getXMLTemplates();
/* 298 */     String objsSerialized = RobotConfigFileManager.serializeXMLConfigList(fileList);
/* 299 */     this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATION_TEMPLATES_RESP", objsSerialized));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleCommandStartProgrammingMode()
/*     */   {
/* 307 */     this.programmingModeController.startProgrammingMode(this.ftcEventLoopHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleCommandStopProgrammingMode()
/*     */   {
/* 315 */     this.programmingModeController.stopProgrammingMode();
/* 316 */     this.ftcEventLoopHandler.restartRobot();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\FtcEventLoopBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */