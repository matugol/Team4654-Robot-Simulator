/*     */ package org.firstinspires.ftc.robotcore.internal;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.AlarmManager;
/*     */ import android.app.Application;
/*     */ import android.app.Application.ActivityLifecycleCallbacks;
/*     */ import android.app.PendingIntent;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.res.Configuration;
/*     */ import android.content.res.Resources;
/*     */ import android.os.Bundle;
/*     */ import android.os.Environment;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.view.View;
/*     */ import android.widget.TextView;
/*     */ import android.widget.Toast;
/*     */ import com.google.gson.Gson;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.PeerApp;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.concurrent.CountDownLatch;
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
/*     */ public class AppUtil
/*     */ {
/*  73 */   public static final File FIRST_FOLDER = new File(Environment.getExternalStorageDirectory() + "/FIRST/");
/*     */   
/*     */ 
/*     */ 
/*  77 */   public static final File ROBOT_SETTINGS = new File(FIRST_FOLDER, "/settings/");
/*     */   private static AppUtil theInstance;
/*     */   private LifeCycleMonitor lifeCycleMonitor;
/*     */   private Activity rootActivity;
/*     */   
/*  82 */   public static AppUtil getInstance() { return theInstance; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Activity currentActivity;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Method methodCurrentApplication;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private PeerApp thisApp;
/*     */   
/*     */ 
/*     */ 
/*     */   public AppUtil(Application application)
/*     */   {
/* 104 */     this.lifeCycleMonitor = new LifeCycleMonitor(null);
/* 105 */     this.rootActivity = null;
/* 106 */     this.currentActivity = null;
/* 107 */     this.methodCurrentApplication = null;
/*     */     
/* 109 */     application.registerActivityLifecycleCallbacks(this.lifeCycleMonitor);
/*     */     try
/*     */     {
/* 112 */       Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
/* 113 */       this.methodCurrentApplication = activityThreadClass.getMethod("currentApplication", new Class[0]);
/*     */     }
/*     */     catch (ClassNotFoundException|NoSuchMethodException localClassNotFoundException) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 120 */     theInstance = this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getSettingsFile(String filename)
/*     */   {
/* 129 */     File file = new File(filename);
/* 130 */     if (!file.isAbsolute())
/*     */     {
/* 132 */       file = new File(ROBOT_SETTINGS, filename);
/*     */     }
/* 134 */     return file;
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
/*     */   public void restartApp(int exitCode)
/*     */   {
/* 149 */     PendingIntent pendingIntent = PendingIntent.getActivity(getApplication().getBaseContext(), 0, new Intent(this.rootActivity.getIntent()), this.rootActivity.getIntent().getFlags());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */     int msRestartDelay = 1500;
/* 156 */     AlarmManager alarmManager = (AlarmManager)this.rootActivity.getSystemService("alarm");
/* 157 */     alarmManager.set(1, System.currentTimeMillis() + msRestartDelay, pendingIntent);
/* 158 */     System.exit(exitCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonNull
/*     */   public Application getApplication()
/*     */   {
/*     */     try
/*     */     {
/* 172 */       return (Application)this.methodCurrentApplication.invoke(null, (Object[])null);
/*     */ 
/*     */     }
/*     */     catch (InvocationTargetException|IllegalAccessException e)
/*     */     {
/* 177 */       throw new IllegalStateException("internal error: getApplication() threw an error");
/*     */     }
/*     */   }
/*     */   
/*     */   public void setThisApp(@NonNull PeerApp thisApp)
/*     */   {
/* 183 */     this.thisApp = thisApp;
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public PeerApp getThisApp()
/*     */   {
/* 189 */     return this.thisApp;
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
/*     */   public void openOptionsMenuFor(Activity activity)
/*     */   {
/* 205 */     Configuration config = activity.getResources().getConfiguration();
/* 206 */     if ((config.screenLayout & 0xF) > 3)
/*     */     {
/* 208 */       int originalScreenLayout = config.screenLayout;
/* 209 */       config.screenLayout = 3;
/*     */       try {
/* 211 */         activity.openOptionsMenu();
/*     */       }
/*     */       finally
/*     */       {
/* 215 */         config.screenLayout = originalScreenLayout;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 220 */       activity.openOptionsMenu();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void synchronousRunOnUiThread(Runnable action)
/*     */   {
/* 231 */     synchronousRunOnUiThread(getActivity(), action);
/*     */   }
/*     */   
/*     */   public void synchronousRunOnUiThread(Activity activity, final Runnable action)
/*     */   {
/*     */     try {
/* 237 */       final CountDownLatch uiDone = new CountDownLatch(1);
/* 238 */       activity.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 242 */           action.run();
/* 243 */           uiDone.countDown();
/*     */         }
/* 245 */       });
/* 246 */       uiDone.await();
/*     */     }
/*     */     catch (InterruptedException e)
/*     */     {
/* 250 */       Thread.currentThread().interrupt();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void runOnUiThread(Runnable action)
/*     */   {
/* 259 */     runOnUiThread(getActivity(), action);
/*     */   }
/*     */   
/*     */   public void runOnUiThread(Activity activity, Runnable action)
/*     */   {
/* 264 */     activity.runOnUiThread(action);
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
/*     */   public void showToast(String msg)
/*     */   {
/* 279 */     showToast(getActivity(), getApplication(), msg);
/*     */   }
/*     */   
/*     */   public void showToast(ToastLocation toastLocation, String msg) {
/* 283 */     showToast(toastLocation, getActivity(), getApplication(), msg);
/*     */   }
/*     */   
/*     */   public void showToast(String msg, int duration) {
/* 287 */     showToast(ToastLocation.DEFAULT, getActivity(), getApplication(), msg, duration);
/*     */   }
/*     */   
/*     */   public void showToast(ToastLocation toastLocation, String msg, int duration) {
/* 291 */     showToast(toastLocation, getActivity(), getApplication(), msg, duration);
/*     */   }
/*     */   
/*     */   public void showToast(Context context, String msg) {
/* 295 */     showToast(getActivity(), context, msg);
/*     */   }
/*     */   
/*     */   public void showToast(ToastLocation toastLocation, Context context, String msg) {
/* 299 */     showToast(toastLocation, getActivity(), context, msg);
/*     */   }
/*     */   
/*     */   public void showToast(String msg, Context context, int duration) {
/* 303 */     showToast(ToastLocation.DEFAULT, getActivity(), context, msg, duration);
/*     */   }
/*     */   
/*     */   public void showToast(Activity activity, Context context, String msg) {
/* 307 */     showToast(ToastLocation.DEFAULT, activity, context, msg, 0);
/*     */   }
/*     */   
/*     */   public void showToast(ToastLocation toastLocation, Activity activity, Context context, String msg) {
/* 311 */     showToast(toastLocation, activity, context, msg, 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public void showToast(ToastLocation toastLocation, Activity activity, final Context context, final String msg, final int duration)
/*     */   {
/* 317 */     activity.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 321 */         Toast toast = Toast.makeText(context, msg, duration);
/* 322 */         TextView message = (TextView)toast.getView().findViewById(16908299);
/* 323 */         message.setTextColor(-1);
/* 324 */         message.setTextSize(18.0F);
/* 325 */         toast.show();
/*     */       }
/*     */     });
/*     */     
/* 329 */     if ((this.thisApp != null) && (this.thisApp.isRobotController()) && (toastLocation == ToastLocation.DEFAULT))
/*     */     {
/*     */ 
/* 332 */       ShowToast showToast = new ShowToast();
/* 333 */       showToast.message = msg;
/* 334 */       showToast.duration = duration;
/* 335 */       NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_SHOW_TOAST", showToast.serialize()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ShowToast
/*     */   {
/*     */     public String message;
/*     */     public int duration;
/*     */     
/*     */     public String serialize()
/*     */     {
/* 346 */       Gson gson = new Gson();
/* 347 */       return gson.toJson(this);
/*     */     }
/*     */     
/*     */     public static ShowToast deserialize(String serialized)
/*     */     {
/* 352 */       Gson gson = new Gson();
/* 353 */       Type type = ShowToast.class;
/* 354 */       return (ShowToast)gson.fromJson(serialized, type);
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
/*     */   public Activity getActivity()
/*     */   {
/* 368 */     return this.currentActivity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Activity getRootActivity()
/*     */   {
/* 377 */     return this.rootActivity;
/*     */   }
/*     */   
/*     */   private class LifeCycleMonitor
/*     */     implements Application.ActivityLifecycleCallbacks
/*     */   {
/*     */     private LifeCycleMonitor() {}
/*     */     
/*     */     public void onActivityCreated(Activity activity, Bundle savedInstanceState)
/*     */     {
/* 387 */       AppUtil.this.currentActivity = activity;
/* 388 */       if (AppUtil.this.rootActivity == null)
/*     */       {
/* 390 */         AppUtil.this.rootActivity = activity;
/*     */       }
/*     */     }
/*     */     
/*     */     public void onActivityStarted(Activity activity)
/*     */     {
/* 396 */       AppUtil.this.currentActivity = activity;
/* 397 */       if (AppUtil.this.rootActivity == null)
/*     */       {
/* 399 */         AppUtil.this.rootActivity = activity;
/*     */       }
/*     */     }
/*     */     
/*     */     public void onActivityResumed(Activity activity)
/*     */     {
/* 405 */       AppUtil.this.currentActivity = activity;
/* 406 */       if (AppUtil.this.rootActivity == null)
/*     */       {
/* 408 */         AppUtil.this.rootActivity = activity;
/*     */       }
/*     */     }
/*     */     
/*     */     public void onActivityPaused(Activity activity) {}
/*     */     
/*     */     public void onActivityStopped(Activity activity) {}
/*     */     
/*     */     public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
/*     */     
/*     */     public void onActivityDestroyed(Activity activity) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\AppUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */