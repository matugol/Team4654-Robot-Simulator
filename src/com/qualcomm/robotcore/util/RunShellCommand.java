/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RunShellCommand
/*     */ {
/*     */   private static final int BUFFER_SIZE = 524288;
/*  47 */   boolean logging = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enableLogging(boolean enable)
/*     */   {
/*  61 */     this.logging = enable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String run(String cmd)
/*     */   {
/*  70 */     if (this.logging) RobotLog.v("running command: " + cmd);
/*  71 */     String output = runCommand(cmd, false);
/*  72 */     if (this.logging) RobotLog.v("         output: " + output);
/*  73 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String runAsRoot(String cmd)
/*     */   {
/*  82 */     if (this.logging) RobotLog.v("running command: " + cmd);
/*  83 */     String output = runCommand(cmd, true);
/*  84 */     if (this.logging) RobotLog.v("         output: " + output);
/*  85 */     return output;
/*     */   }
/*     */   
/*     */   private String runCommand(String cmd, boolean asRoot)
/*     */   {
/*  90 */     byte[] buffer = new byte[524288];
/*  91 */     int length = 0;
/*  92 */     String output = "";
/*  93 */     ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
/*  94 */     Process process = null;
/*     */     try
/*     */     {
/*  97 */       if (asRoot) {
/*  98 */         processBuilder.command(new String[] { "su", "-c", cmd }).redirectErrorStream(true);
/*     */       } else {
/* 100 */         processBuilder.command(new String[] { "sh", "-c", cmd }).redirectErrorStream(true);
/*     */       }
/* 102 */       process = processBuilder.start();
/* 103 */       process.waitFor();
/*     */       
/* 105 */       InputStream in = process.getInputStream();
/*     */       
/*     */ 
/* 108 */       length = in.read(buffer);
/* 109 */       if (length > 0) output = new String(buffer, 0, length);
/*     */     }
/*     */     catch (IOException e) {
/* 112 */       RobotLog.logStacktrace(e);
/*     */     } catch (InterruptedException e) {
/* 114 */       e.printStackTrace();
/*     */     } finally {
/* 116 */       if (process != null) process.destroy();
/*     */     }
/* 118 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void killSpawnedProcess(String processName, String packageName, RunShellCommand shell)
/*     */     throws RobotCoreException
/*     */   {
/*     */     try
/*     */     {
/* 130 */       int pid = getSpawnedProcessPid(processName, packageName, shell);
/* 131 */       while (pid != -1) {
/* 132 */         RobotLog.v("Killing PID " + pid);
/* 133 */         shell.run(String.format("kill %d", new Object[] { Integer.valueOf(pid) }));
/* 134 */         pid = getSpawnedProcessPid(processName, packageName, shell);
/*     */       }
/*     */     } catch (Exception e) {
/* 137 */       throw new RobotCoreException(String.format("Failed to kill %s instances started by this app", new Object[] { processName }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getSpawnedProcessPid(String processName, String packageName, RunShellCommand shell)
/*     */   {
/* 152 */     String psOutput = shell.run("ps");
/* 153 */     String username = "invalid";
/*     */     
/*     */ 
/* 156 */     for (String line : psOutput.split("\n")) {
/* 157 */       if (line.contains(packageName)) {
/* 158 */         String[] tokens = line.split("\\s+");
/* 159 */         username = tokens[0];
/* 160 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 165 */     for (String line : psOutput.split("\n")) {
/* 166 */       if ((line.contains(processName)) && (line.contains(username))) {
/* 167 */         String[] tokens = line.split("\\s+");
/* 168 */         return Integer.parseInt(tokens[1]);
/*     */       }
/*     */     }
/*     */     
/* 172 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\RunShellCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */