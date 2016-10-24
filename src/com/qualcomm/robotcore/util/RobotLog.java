/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.os.Environment;
/*     */ import android.util.Log;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.robocol.Heartbeat;
/*     */ import java.io.File;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RobotLog
/*     */ {
/*  63 */   private static String globalErrorMessage = "";
/*  64 */   private static final Object globalWarningLock = new Object();
/*  65 */   private static String globalWarningMessage = "";
/*  66 */   private static WeakHashMap<GlobalWarningSource, Integer> globalWarningSources = new WeakHashMap();
/*  67 */   private static double msTimeOffset = 0.0D;
/*     */   
/*     */   public static final String TAG = "RobotCore";
/*     */   
/*  71 */   private static boolean writeLogcatToDiskEnabled = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void processTimeSynch(long t0, long t1, long t2, long t3)
/*     */   {
/*  83 */     if ((t0 == 0L) || (t1 == 0L) || (t2 == 0L) || (t3 == 0L)) {
/*  84 */       return;
/*     */     }
/*     */     
/*     */ 
/*  88 */     double offset = (t1 - t0 + (t2 - t3)) / 2.0D;
/*  89 */     setMsTimeOffset(offset);
/*     */   }
/*     */   
/*     */   public static void setMsTimeOffset(double offset)
/*     */   {
/*  94 */     msTimeOffset = offset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */   public static void a(String format, Object... args) { v(String.format(format, args)); }
/*     */   
/* 103 */   public static void a(String message) { internalLog(7, "RobotCore", message); }
/*     */   
/* 105 */   public static void aa(String tag, String format, Object... args) { vv(tag, String.format(format, args)); }
/*     */   
/* 107 */   public static void aa(String tag, String message) { internalLog(7, tag, message); }
/*     */   
/*     */ 
/* 110 */   public static void v(String format, Object... args) { v(String.format(format, args)); }
/*     */   
/* 112 */   public static void v(String message) { internalLog(2, "RobotCore", message); }
/*     */   
/* 114 */   public static void vv(String tag, String format, Object... args) { vv(tag, String.format(format, args)); }
/*     */   
/* 116 */   public static void vv(String tag, String message) { internalLog(2, tag, message); }
/*     */   
/*     */ 
/* 119 */   public static void d(String format, Object... args) { d(String.format(format, args)); }
/*     */   
/* 121 */   public static void d(String message) { internalLog(3, "RobotCore", message); }
/*     */   
/* 123 */   public static void dd(String tag, String format, Object... args) { dd(tag, String.format(format, args)); }
/*     */   
/* 125 */   public static void dd(String tag, String message) { internalLog(3, tag, message); }
/*     */   
/*     */ 
/* 128 */   public static void i(String format, Object... args) { i(String.format(format, args)); }
/*     */   
/* 130 */   public static void i(String message) { internalLog(4, "RobotCore", message); }
/*     */   
/* 132 */   public static void ii(String tag, String format, Object... args) { ii(tag, String.format(format, args)); }
/*     */   
/* 134 */   public static void ii(String tag, String message) { internalLog(4, tag, message); }
/*     */   
/*     */ 
/* 137 */   public static void w(String format, Object... args) { w(String.format(format, args)); }
/*     */   
/* 139 */   public static void w(String message) { internalLog(5, "RobotCore", message); }
/*     */   
/* 141 */   public static void ww(String tag, String format, Object... args) { ww(tag, String.format(format, args)); }
/*     */   
/* 143 */   public static void ww(String tag, String message) { internalLog(5, tag, message); }
/*     */   
/*     */ 
/* 146 */   public static void e(String format, Object... args) { e(String.format(format, args)); }
/*     */   
/* 148 */   public static void e(String message) { internalLog(6, "RobotCore", message); }
/*     */   
/* 150 */   public static void ee(String tag, String format, Object... args) { ee(tag, String.format(format, args)); }
/*     */   
/* 152 */   public static void ee(String tag, String message) { internalLog(6, tag, message); }
/*     */   
/*     */   private static void internalLog(int priority, String tag, String message)
/*     */   {
/* 156 */     if (msTimeOffset == 0.0D) {
/* 157 */       Log.println(priority, tag, message);
/*     */     } else {
/* 159 */       double offset = msTimeOffset;
/* 160 */       long now = (Heartbeat.getMsTimeSyncTime() + offset + 0.5D);
/* 161 */       GregorianCalendar tNow = new GregorianCalendar();tNow.setTimeInMillis(now);
/* 162 */       Log.println(priority, tag, String.format("dms=%d s=%d.%03d %s", new Object[] { Integer.valueOf((int)(msTimeOffset + 0.5D)), Integer.valueOf(tNow.get(13)), Integer.valueOf(tNow.get(14)), message }));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void logStacktrace(Thread thread, String format, Object... args) {
/* 167 */     String message = String.format(format, args);
/* 168 */     e("thread id=%d name=\"%s\" %s", new Object[] { Long.valueOf(thread.getId()), thread.getName(), message });
/* 169 */     for (StackTraceElement el : thread.getStackTrace()) {
/* 170 */       e("    at %s", new Object[] { el.toString() });
/*     */     }
/*     */   }
/*     */   
/*     */   public static void logStacktrace(Exception e) {
/* 175 */     e(e.toString());
/* 176 */     for (StackTraceElement el : e.getStackTrace()) {
/* 177 */       e(el.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void logStacktrace(RobotCoreException e) {
/* 182 */     e(e.toString());
/* 183 */     for (StackTraceElement el : e.getStackTrace()) {
/* 184 */       e(el.toString());
/*     */     }
/*     */     
/* 187 */     if (e.isChainedException()) {
/* 188 */       e("Exception chained from:");
/* 189 */       if ((e.getChainedException() instanceof RobotCoreException)) {
/* 190 */         logStacktrace((RobotCoreException)e.getChainedException());
/*     */       } else {
/* 192 */         logStacktrace(e.getChainedException());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void logAndThrow(String errMsg) throws RobotCoreException {
/* 198 */     w(errMsg);
/* 199 */     throw new RobotCoreException(errMsg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean setGlobalErrorMsg(String message)
/*     */   {
/* 222 */     if (globalErrorMessage.isEmpty()) {
/* 223 */       globalErrorMessage += message;
/* 224 */       return true;
/*     */     }
/* 226 */     return false;
/*     */   }
/*     */   
/*     */   public static void setGlobalErrorMsg(String format, Object... args) {
/* 230 */     setGlobalErrorMsg(String.format(format, args));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setGlobalWarningMessage(String message)
/*     */   {
/* 242 */     synchronized (globalWarningLock) {
/* 243 */       if (globalWarningMessage.isEmpty()) {
/* 244 */         globalWarningMessage += message;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setGlobalWarningMessage(String format, Object... args) {
/* 250 */     setGlobalWarningMessage(String.format(format, args));
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
/*     */ 
/*     */   public static void registerGlobalWarningSource(GlobalWarningSource globalWarningSource)
/*     */   {
/* 265 */     synchronized (globalWarningLock) {
/* 266 */       globalWarningSources.put(globalWarningSource, Integer.valueOf(1));
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
/*     */ 
/*     */   public static void unregisterGlobalWarningSource(GlobalWarningSource globalWarningSource)
/*     */   {
/* 282 */     synchronized (globalWarningLock) {
/* 283 */       globalWarningSources.remove(globalWarningSource);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setGlobalWarningMsg(RobotCoreException e, String message) {
/* 288 */     setGlobalWarningMessage(message + ": " + e.getMessage());
/*     */   }
/*     */   
/*     */   public static void setGlobalErrorMsg(RobotCoreException e, String message) {
/* 292 */     setGlobalErrorMsg(message + ": " + e.getMessage());
/*     */   }
/*     */   
/*     */   public static void setGlobalErrorMsgAndThrow(RobotCoreException e, String message) throws RobotCoreException {
/* 296 */     setGlobalErrorMsg(e, message);
/* 297 */     throw e;
/*     */   }
/*     */   
/*     */   public static void setGlobalErrorMsg(RuntimeException e, String message) {
/* 301 */     setGlobalErrorMsg(String.format("%s: %s: %s", new Object[] { message, e.getClass().getSimpleName(), e.getMessage() }));
/*     */   }
/*     */   
/*     */   public static void setGlobalErrorMsgAndThrow(RuntimeException e, String message) throws RobotCoreException {
/* 305 */     setGlobalErrorMsg(e, message);
/* 306 */     throw e;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getGlobalErrorMsg()
/*     */   {
/* 314 */     return globalErrorMessage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getGlobalWarningMessage()
/*     */   {
/* 323 */     StringBuilder result = new StringBuilder();
/*     */     
/* 325 */     synchronized (globalWarningLock) {
/* 326 */       if (!globalWarningMessage.isEmpty()) {
/* 327 */         result.append(globalWarningMessage);
/*     */       }
/* 329 */       for (GlobalWarningSource source : globalWarningSources.keySet()) {
/* 330 */         String warning = source.getGlobalWarning();
/* 331 */         if ((warning != null) && (!warning.isEmpty())) {
/* 332 */           if (result.length() > 0)
/* 333 */             result.append("; ");
/* 334 */           result.append(warning);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 339 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasGlobalErrorMsg()
/*     */   {
/* 347 */     return !getGlobalErrorMsg().isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasGlobalWarningMsg()
/*     */   {
/* 355 */     return !getGlobalWarningMessage().isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void clearGlobalErrorMsg()
/*     */   {
/* 362 */     globalErrorMessage = "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void clearGlobalWarningMsg()
/*     */   {
/* 369 */     synchronized (globalWarningLock) {
/* 370 */       globalWarningMessage = "";
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
/*     */   public static void writeLogcatToDisk(Context context, final int fileSizeKb)
/*     */   {
/* 385 */     if (writeLogcatToDiskEnabled) return;
/* 386 */     writeLogcatToDiskEnabled = true;
/*     */     
/* 388 */     final String packageName = context.getPackageName();
/* 389 */     final String filename = new File(getLogFilename(context)).getAbsolutePath();
/*     */     
/* 391 */     Thread logThread = new Thread("Logging Thread")
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         try {
/* 396 */           String filter = "UsbRequestJNI:S UsbRequest:S *:V";
/* 397 */           int maxRotatedLogs = 1;
/*     */           
/* 399 */           RobotLog.v("saving logcat to " + filename);
/* 400 */           RunShellCommand shell = new RunShellCommand();
/* 401 */           RunShellCommand.killSpawnedProcess("logcat", packageName, shell);
/* 402 */           shell.run(String.format("logcat -f %s -r%d -n%d -v time %s", new Object[] { filename, Integer.valueOf(fileSizeKb), Integer.valueOf(1), "UsbRequestJNI:S UsbRequest:S *:V" }));
/*     */         }
/*     */         catch (RobotCoreException e) {
/* 405 */           RobotLog.v("Error while writing log file to disk: " + e.toString());
/*     */         } finally {
/* 407 */           RobotLog.access$002(false);
/*     */         }
/*     */       }
/* 410 */     };
/* 411 */     logThread.start();
/*     */   }
/*     */   
/*     */   public static String getLogFilename(Context context) {
/* 415 */     String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName();
/* 416 */     return filename + ".logcat";
/*     */   }
/*     */   
/*     */   public static void cancelWriteLogcatToDisk(Context context) {
/* 420 */     final String packageName = context.getPackageName();
/* 421 */     String filename = new File(Environment.getExternalStorageDirectory(), packageName).getAbsolutePath();
/*     */     
/* 423 */     writeLogcatToDiskEnabled = false;
/*     */     
/* 425 */     Thread logThread = new Thread()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         try {
/* 430 */           Thread.sleep(1000L);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */         
/*     */         try
/*     */         {
/* 436 */           RobotLog.v("closing logcat file " + this.val$filename);
/* 437 */           RunShellCommand shell = new RunShellCommand();
/* 438 */           RunShellCommand.killSpawnedProcess("logcat", packageName, shell);
/*     */         } catch (RobotCoreException e) {
/* 440 */           RobotLog.v("Unable to cancel writing log file to disk: " + e.toString());
/*     */         }
/*     */       }
/* 443 */     };
/* 444 */     logThread.start();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\RobotLog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */