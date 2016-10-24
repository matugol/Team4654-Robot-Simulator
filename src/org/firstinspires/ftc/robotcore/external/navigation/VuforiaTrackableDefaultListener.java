/*     */ package org.firstinspires.ftc.robotcore.external.navigation;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import com.vuforia.Matrix34F;
/*     */ import com.vuforia.TrackableResult;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
/*     */ import org.firstinspires.ftc.robotcore.internal.VuforiaPoseMatrix;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VuforiaTrackableDefaultListener
/*     */   implements VuforiaTrackable.Listener
/*     */ {
/*     */   public static final String TAG = "Vuforia";
/*     */   protected VuforiaTrackable trackable;
/*     */   protected boolean newPoseAvailable;
/*     */   protected boolean newLocationAvailable;
/*     */   protected Matrix34F currentPose;
/*     */   protected Matrix34F lastTrackedPose;
/*     */   protected TrackableResult lastTrackableResult;
/*     */   protected OpenGLMatrix phoneLocationOnRobotInverted;
/*     */   protected VuforiaLocalizer.CameraDirection cameraDirection;
/*     */   protected final Map<VuforiaLocalizer.CameraDirection, OpenGLMatrix> poseCorrectionMatrices;
/*     */   
/*     */   public VuforiaTrackableDefaultListener(VuforiaTrackable trackable)
/*     */   {
/*  77 */     this.trackable = trackable;
/*  78 */     this.newPoseAvailable = false;
/*  79 */     this.newLocationAvailable = false;
/*  80 */     this.currentPose = (this.lastTrackedPose = null);
/*  81 */     this.lastTrackableResult = null;
/*  82 */     this.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
/*     */     
/*  84 */     this.poseCorrectionMatrices = new HashMap();
/*  85 */     this.poseCorrectionMatrices.put(VuforiaLocalizer.CameraDirection.BACK, new OpenGLMatrix(new float[] { 0.0F, -1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F }));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */     this.poseCorrectionMatrices.put(VuforiaLocalizer.CameraDirection.FRONT, new OpenGLMatrix(new float[] { 0.0F, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F }));
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
/*     */ 
/*     */ 
/*     */   public synchronized void setPhoneInformation(@NonNull OpenGLMatrix phoneLocationOnRobot, @NonNull VuforiaLocalizer.CameraDirection cameraDirection)
/*     */   {
/* 117 */     this.phoneLocationOnRobotInverted = phoneLocationOnRobot.inverted();
/* 118 */     this.cameraDirection = cameraDirection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized OpenGLMatrix getPhoneLocationOnRobot()
/*     */   {
/* 129 */     return this.phoneLocationOnRobotInverted == null ? null : this.phoneLocationOnRobotInverted.inverted();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized VuforiaLocalizer.CameraDirection getCameraDirection()
/*     */   {
/* 139 */     return this.cameraDirection;
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
/*     */   @Nullable
/*     */   public synchronized OpenGLMatrix getRobotLocation()
/*     */   {
/* 160 */     OpenGLMatrix trackableLocationOnField = this.trackable.getLocation();
/* 161 */     OpenGLMatrix pose = getPose();
/* 162 */     if ((pose != null) && (trackableLocationOnField != null) && (this.phoneLocationOnRobotInverted != null))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 169 */       OpenGLMatrix result = trackableLocationOnField.multiplied(pose.inverted()).multiplied(this.phoneLocationOnRobotInverted);
/*     */       
/*     */ 
/*     */ 
/* 173 */       return result;
/*     */     }
/*     */     
/* 176 */     return null;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   @NonNull
/*     */   public OpenGLMatrix getPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection direction)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 12	org/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackableDefaultListener:poseCorrectionMatrices	Ljava/util/Map;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 12	org/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackableDefaultListener:poseCorrectionMatrices	Ljava/util/Map;
/*     */     //   11: aload_1
/*     */     //   12: invokeinterface 23 2 0
/*     */     //   17: checkcast 13	org/firstinspires/ftc/robotcore/external/matrices/OpenGLMatrix
/*     */     //   20: aload_2
/*     */     //   21: monitorexit
/*     */     //   22: areturn
/*     */     //   23: astore_3
/*     */     //   24: aload_2
/*     */     //   25: monitorexit
/*     */     //   26: aload_3
/*     */     //   27: athrow
/*     */     // Line number table:
/*     */     //   Java source line #188	-> byte code offset #0
/*     */     //   Java source line #190	-> byte code offset #7
/*     */     //   Java source line #191	-> byte code offset #23
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	28	0	this	VuforiaTrackableDefaultListener
/*     */     //   0	28	1	direction	VuforiaLocalizer.CameraDirection
/*     */     //   5	20	2	Ljava/lang/Object;	Object
/*     */     //   23	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	22	23	finally
/*     */     //   23	26	23	finally
/*     */   }
/*     */   
/*     */   public void setPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection direction, @NonNull OpenGLMatrix matrix)
/*     */   {
/* 197 */     synchronized (this.poseCorrectionMatrices)
/*     */     {
/* 199 */       this.poseCorrectionMatrices.put(direction, matrix);
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
/*     */   public synchronized OpenGLMatrix getUpdatedRobotLocation()
/*     */   {
/* 212 */     if (this.newLocationAvailable)
/*     */     {
/* 214 */       this.newLocationAvailable = false;
/* 215 */       return getRobotLocation();
/*     */     }
/*     */     
/* 218 */     return null;
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
/*     */   @Nullable
/*     */   public synchronized OpenGLMatrix getPose()
/*     */   {
/* 238 */     OpenGLMatrix pose = getRawPose();
/* 239 */     return pose == null ? null : getPoseCorrectionMatrix(this.cameraDirection).multiplied(pose);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   public synchronized OpenGLMatrix getRawPose()
/*     */   {
/* 251 */     if (this.currentPose != null)
/*     */     {
/* 253 */       OpenGLMatrix result = new VuforiaPoseMatrix(this.currentPose).toOpenGL();
/*     */       
/* 255 */       return result;
/*     */     }
/*     */     
/* 258 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   public synchronized OpenGLMatrix getRawUpdatedPose()
/*     */   {
/* 270 */     if (this.newPoseAvailable)
/*     */     {
/* 272 */       this.newPoseAvailable = false;
/* 273 */       return getRawPose();
/*     */     }
/*     */     
/* 276 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVisible()
/*     */   {
/* 287 */     return getPose() != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized OpenGLMatrix getLastTrackedRawPose()
/*     */   {
/* 299 */     return this.lastTrackedPose == null ? null : new VuforiaPoseMatrix(this.lastTrackedPose).toOpenGL();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void onTracked(TrackableResult trackableResult)
/*     */   {
/* 310 */     this.lastTrackableResult = trackableResult;
/* 311 */     this.currentPose = trackableResult.getPose();
/* 312 */     this.newPoseAvailable = true;
/* 313 */     this.newLocationAvailable = true;
/* 314 */     this.lastTrackedPose = this.currentPose;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void onNotTracked()
/*     */   {
/* 322 */     this.currentPose = null;
/* 323 */     this.newPoseAvailable = true;
/* 324 */     this.newLocationAvailable = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaTrackableDefaultListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */