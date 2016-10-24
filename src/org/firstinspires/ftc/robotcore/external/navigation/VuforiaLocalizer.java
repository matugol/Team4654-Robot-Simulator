/*     */ package org.firstinspires.ftc.robotcore.external.navigation;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.support.annotation.IdRes;
/*     */ import android.view.ViewGroup;
/*     */ import com.vuforia.CameraCalibration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface VuforiaLocalizer
/*     */ {
/*     */   public abstract VuforiaTrackables loadTrackablesFromAsset(String paramString);
/*     */   
/*     */   public abstract VuforiaTrackables loadTrackablesFromFile(String paramString);
/*     */   
/*     */   public abstract CameraCalibration getCameraCalibration();
/*     */   
/*     */   public static enum CameraDirection
/*     */   {
/*  95 */     BACK(1), 
/*  96 */     FRONT(2);
/*     */     
/*     */     public final int direction;
/*     */     
/*     */     private CameraDirection(int direction)
/*     */     {
/* 102 */       this.direction = direction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Parameters
/*     */   {
/* 123 */     public String vuforiaLicenseKey = "<visit https://developer.vuforia.com/license-manager to obtain a license key>";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 129 */     public VuforiaLocalizer.CameraDirection cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */     public boolean useExtendedTracking = true;
/*     */     
/*     */ 
/*     */     public Parameters() {}
/*     */     
/*     */ 
/*     */     public static enum CameraMonitorFeedback
/*     */     {
/* 145 */       NONE,  AXES,  TEAPOT,  BUILDINGS;
/*     */       
/*     */ 
/*     */ 
/*     */       private CameraMonitorFeedback() {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 154 */     public CameraMonitorFeedback cameraMonitorFeedback = CameraMonitorFeedback.AXES;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @IdRes
/* 166 */     public int cameraMonitorViewIdParent = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 177 */     public ViewGroup cameraMonitorViewParent = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 187 */     public boolean fillCameraMonitorViewParent = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */     public Activity activity = null;
/*     */     
/*     */     public Parameters(@IdRes int cameraMonitorViewIdParent) {
/* 196 */       this.cameraMonitorViewIdParent = cameraMonitorViewIdParent;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaLocalizer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */