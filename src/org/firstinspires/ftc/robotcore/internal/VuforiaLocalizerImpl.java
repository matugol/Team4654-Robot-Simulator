/*      */ package org.firstinspires.ftc.robotcore.internal;
/*      */ 
/*      */ import android.app.Activity;
/*      */ import android.app.Application;
/*      */ import android.app.Application.ActivityLifecycleCallbacks;
/*      */ import android.content.res.Configuration;
/*      */ import android.content.res.Resources;
/*      */ import android.graphics.Point;
/*      */ import android.graphics.PointF;
/*      */ import android.opengl.GLES20;
/*      */ import android.opengl.GLSurfaceView.Renderer;
/*      */ import android.opengl.Matrix;
/*      */ import android.os.Bundle;
/*      */ import android.support.annotation.IdRes;
/*      */ import android.support.annotation.Nullable;
/*      */ import android.view.View;
/*      */ import android.view.ViewGroup;
/*      */ import android.view.ViewGroup.LayoutParams;
/*      */ import android.view.ViewGroupOverlay;
/*      */ import android.widget.LinearLayout.LayoutParams;
/*      */ import android.widget.RelativeLayout;
/*      */ import com.qualcomm.robotcore.R.id;
/*      */ import com.qualcomm.robotcore.R.layout;
/*      */ import com.qualcomm.robotcore.R.string;
/*      */ import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/*      */ import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
/*      */ import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier.Notifications;
/*      */ import com.vuforia.CameraCalibration;
/*      */ import com.vuforia.CameraDevice;
/*      */ import com.vuforia.Matrix44F;
/*      */ import com.vuforia.ObjectTracker;
/*      */ import com.vuforia.Renderer;
/*      */ import com.vuforia.RotationalDeviceTracker;
/*      */ import com.vuforia.State;
/*      */ import com.vuforia.Tool;
/*      */ import com.vuforia.TrackableResult;
/*      */ import com.vuforia.TrackerManager;
/*      */ import com.vuforia.Vec2I;
/*      */ import com.vuforia.VideoBackgroundConfig;
/*      */ import com.vuforia.VideoMode;
/*      */ import com.vuforia.Vuforia;
/*      */ import com.vuforia.Vuforia.UpdateCallbackInterface;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javax.microedition.khronos.egl.EGLConfig;
/*      */ import javax.microedition.khronos.opengles.GL10;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters.CameraMonitorFeedback;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
/*      */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.AutoConfigGLSurfaceView;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.Texture;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.models.SavedMeshObject;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.models.SolidCylinder;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.models.Teapot;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.ColorFragmentShader;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.CubeMeshFragmentShader;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.CubeMeshProgram;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.CubeMeshVertexShader;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.SimpleColorProgram;
/*      */ import org.firstinspires.ftc.robotcore.internal.opengl.shaders.SimpleVertexShader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class VuforiaLocalizerImpl
/*      */   implements VuforiaLocalizer
/*      */ {
/*      */   public static final String TAG = "Vuforia";
/*  111 */   protected LifeCycleCallbacks lifeCycleCallbacks = new LifeCycleCallbacks();
/*  112 */   protected OpModeManagerImpl opModeManager = null;
/*  113 */   protected OpModeNotifications opModeNotifications = new OpModeNotifications();
/*  114 */   protected VuforiaCallback vuforiaCallback = new VuforiaCallback();
/*  115 */   protected GLSurfaceViewRenderer glSurfaceViewRenderer = new GLSurfaceViewRenderer();
/*      */   
/*  117 */   protected AppUtil appUtil = AppUtil.getInstance();
/*  118 */   protected VuforiaLocalizer.Parameters parameters = null;
/*      */   protected final Activity activity;
/*  120 */   protected int vuforiaFlags = 0;
/*  121 */   protected boolean wantCamera = false;
/*  122 */   protected boolean isCameraInited = false;
/*  123 */   protected boolean isCameraStarted = false;
/*  124 */   protected boolean isCameraRunning = false;
/*  125 */   protected int cameraIndex = -1;
/*  126 */   protected CameraCalibration camCal = null;
/*      */   
/*  128 */   protected ViewGroup glSurfaceParent = null;
/*  129 */   protected AutoConfigGLSurfaceView glSurface = null;
/*  130 */   protected boolean fillSurfaceParent = false;
/*  131 */   protected boolean isPortrait = true;
/*  132 */   protected VuforiaLocalizer.Parameters.CameraMonitorFeedback cameraCameraMonitorFeedback = null;
/*      */   
/*  134 */   protected RelativeLayout loadingIndicatorOverlay = null;
/*  135 */   protected View loadingIndicator = null;
/*  136 */   protected Renderer renderer = null;
/*  137 */   protected boolean rendererIsActive = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  143 */   protected final Object startStopLock = new Object();
/*      */   
/*      */   public static class ViewPort {
/*  146 */     Point lowerLeft = new Point();
/*  147 */     Point extent = new Point();
/*  148 */     public String toString() { return String.format("[(%d,%d)-(%d,%d)]", new Object[] { Integer.valueOf(this.lowerLeft.x), Integer.valueOf(this.lowerLeft.y), Integer.valueOf(this.extent.x), Integer.valueOf(this.extent.y) }); } }
/*      */   
/*  150 */   protected ViewPort viewport = null;
/*  151 */   protected Matrix44F projectionMatrix = null;
/*  152 */   protected CubeMeshProgram cubeMeshProgram = null;
/*  153 */   protected SimpleColorProgram simpleColorProgram = null;
/*  154 */   protected List<Texture> textures = null;
/*  155 */   protected Texture teapotTexture = null;
/*  156 */   protected Teapot teapot = null;
/*  157 */   protected float teapotScale = 3.0F;
/*  158 */   protected CoordinateAxes coordinateAxes = new CoordinateAxes();
/*  159 */   protected Texture buildingsTexture = null;
/*  160 */   protected SavedMeshObject buildingsModel = null;
/*  161 */   protected float buildingsScale = 12.0F;
/*      */   
/*  163 */   protected final List<VuforiaTrackablesImpl> loadedTrackableSets = new LinkedList();
/*  164 */   protected boolean isExtendedTrackingActive = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public VuforiaLocalizerImpl(VuforiaLocalizer.Parameters parameters)
/*      */   {
/*  172 */     this.parameters = parameters;
/*  173 */     this.activity = (parameters.activity == null ? this.appUtil.getActivity() : parameters.activity);
/*  174 */     this.isExtendedTrackingActive = parameters.useExtendedTracking;
/*  175 */     this.cameraCameraMonitorFeedback = parameters.cameraMonitorFeedback;
/*  176 */     if (this.cameraCameraMonitorFeedback == null)
/*      */     {
/*  178 */       this.cameraCameraMonitorFeedback = (this.isExtendedTrackingActive ? VuforiaLocalizer.Parameters.CameraMonitorFeedback.BUILDINGS : VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES);
/*      */     }
/*      */     
/*  181 */     this.fillSurfaceParent = parameters.fillCameraMonitorViewParent;
/*  182 */     registerLifeCycleCallbacks();
/*  183 */     if (parameters.cameraMonitorViewParent != null) {
/*  184 */       setMonitorViewParent(parameters.cameraMonitorViewParent);
/*      */     } else
/*  186 */       setMonitorViewParent(parameters.cameraMonitorViewIdParent);
/*  187 */     loadTextures();
/*  188 */     startAR();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void close()
/*      */   {
/*  194 */     stopAR();
/*  195 */     unregisterLifeCycleCallbacks();
/*      */     
/*  197 */     if (this.glSurfaceParent != null)
/*      */     {
/*  199 */       this.appUtil.runOnUiThread(new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/*  203 */           if (VuforiaLocalizerImpl.this.glSurface != null)
/*      */           {
/*  205 */             VuforiaLocalizerImpl.this.glSurface.setVisibility(4);
/*      */           }
/*  207 */           if (VuforiaLocalizerImpl.this.loadingIndicatorOverlay != null)
/*      */           {
/*  209 */             ((ViewGroup)VuforiaLocalizerImpl.this.loadingIndicatorOverlay.getParent()).removeView(VuforiaLocalizerImpl.this.loadingIndicatorOverlay);
/*  210 */             VuforiaLocalizerImpl.this.loadingIndicatorOverlay = null;
/*      */           }
/*  212 */           VuforiaLocalizerImpl.this.loadingIndicator = null;
/*  213 */           VuforiaLocalizerImpl.this.glSurfaceParent.removeAllViews();
/*  214 */           VuforiaLocalizerImpl.this.glSurfaceParent.getOverlay().clear();
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setMonitorViewParent(@IdRes int resourceId)
/*      */   {
/*  226 */     View view = this.activity.findViewById(resourceId);
/*  227 */     setMonitorViewParent((ViewGroup)view);
/*      */   }
/*      */   
/*      */   protected void setMonitorViewParent(@Nullable ViewGroup viewParent)
/*      */   {
/*  232 */     this.glSurfaceParent = viewParent;
/*  233 */     makeLoadingIndicator();
/*      */   }
/*      */   
/*      */   public VuforiaTrackables loadTrackablesFromAsset(String assetName)
/*      */   {
/*  238 */     return loadDataSet(assetName, 1);
/*      */   }
/*      */   
/*      */   public VuforiaTrackables loadTrackablesFromFile(String absoluteFileName)
/*      */   {
/*  243 */     return loadDataSet(absoluteFileName, 2);
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected VuforiaTrackables loadDataSet(String name, int type)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: iconst_0
/*      */     //   2: invokevirtual 80	org/firstinspires/ftc/robotcore/internal/VuforiaLocalizerImpl:showLoadingIndicator	(I)V
/*      */     //   5: invokestatic 81	org/firstinspires/ftc/robotcore/internal/VuforiaLocalizerImpl:getObjectTracker	()Lcom/vuforia/ObjectTracker;
/*      */     //   8: astore_3
/*      */     //   9: aload_3
/*      */     //   10: invokevirtual 82	com/vuforia/ObjectTracker:createDataSet	()Lcom/vuforia/DataSet;
/*      */     //   13: astore 4
/*      */     //   15: ldc 83
/*      */     //   17: ldc 84
/*      */     //   19: iconst_1
/*      */     //   20: anewarray 34	java/lang/Object
/*      */     //   23: dup
/*      */     //   24: iconst_0
/*      */     //   25: aload_1
/*      */     //   26: aastore
/*      */     //   27: invokestatic 85	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   30: aload 4
/*      */     //   32: new 86	java/lang/StringBuilder
/*      */     //   35: dup
/*      */     //   36: invokespecial 87	java/lang/StringBuilder:<init>	()V
/*      */     //   39: aload_1
/*      */     //   40: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   43: ldc 89
/*      */     //   45: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   48: invokevirtual 90	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   51: iload_2
/*      */     //   52: invokevirtual 91	com/vuforia/DataSet:load	(Ljava/lang/String;I)Z
/*      */     //   55: invokestatic 92	org/firstinspires/ftc/robotcore/internal/VuforiaLocalizerImpl:throwIfFail	(Z)V
/*      */     //   58: ldc 83
/*      */     //   60: ldc 93
/*      */     //   62: iconst_1
/*      */     //   63: anewarray 34	java/lang/Object
/*      */     //   66: dup
/*      */     //   67: iconst_0
/*      */     //   68: aload_1
/*      */     //   69: aastore
/*      */     //   70: invokestatic 85	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   73: goto +23 -> 96
/*      */     //   76: astore 5
/*      */     //   78: ldc 83
/*      */     //   80: ldc 93
/*      */     //   82: iconst_1
/*      */     //   83: anewarray 34	java/lang/Object
/*      */     //   86: dup
/*      */     //   87: iconst_0
/*      */     //   88: aload_1
/*      */     //   89: aastore
/*      */     //   90: invokestatic 85	com/qualcomm/robotcore/util/RobotLog:vv	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   93: aload 5
/*      */     //   95: athrow
/*      */     //   96: new 94	org/firstinspires/ftc/robotcore/internal/VuforiaTrackablesImpl
/*      */     //   99: dup
/*      */     //   100: aload_0
/*      */     //   101: aload 4
/*      */     //   103: invokespecial 95	org/firstinspires/ftc/robotcore/internal/VuforiaTrackablesImpl:<init>	(Lorg/firstinspires/ftc/robotcore/internal/VuforiaLocalizerImpl;Lcom/vuforia/DataSet;)V
/*      */     //   106: astore 5
/*      */     //   108: aload 5
/*      */     //   110: aload_1
/*      */     //   111: invokevirtual 96	org/firstinspires/ftc/robotcore/internal/VuforiaTrackablesImpl:setName	(Ljava/lang/String;)V
/*      */     //   114: aload_0
/*      */     //   115: getfield 54	org/firstinspires/ftc/robotcore/internal/VuforiaLocalizerImpl:loadedTrackableSets	Ljava/util/List;
/*      */     //   118: dup
/*      */     //   119: astore 6
/*      */     //   121: monitorenter
/*      */     //   122: aload_0
/*      */     //   123: getfield 54	org/firstinspires/ftc/robotcore/internal/VuforiaLocalizerImpl:loadedTrackableSets	Ljava/util/List;
/*      */     //   126: aload 5
/*      */     //   128: invokeinterface 97 2 0
/*      */     //   133: pop
/*      */     //   134: aload 6
/*      */     //   136: monitorexit
/*      */     //   137: goto +11 -> 148
/*      */     //   140: astore 7
/*      */     //   142: aload 6
/*      */     //   144: monitorexit
/*      */     //   145: aload 7
/*      */     //   147: athrow
/*      */     //   148: aload 5
/*      */     //   150: astore 6
/*      */     //   152: aload_0
/*      */     //   153: iconst_4
/*      */     //   154: invokevirtual 80	org/firstinspires/ftc/robotcore/internal/VuforiaLocalizerImpl:showLoadingIndicator	(I)V
/*      */     //   157: aload 6
/*      */     //   159: areturn
/*      */     //   160: astore 8
/*      */     //   162: aload_0
/*      */     //   163: iconst_4
/*      */     //   164: invokevirtual 80	org/firstinspires/ftc/robotcore/internal/VuforiaLocalizerImpl:showLoadingIndicator	(I)V
/*      */     //   167: aload 8
/*      */     //   169: athrow
/*      */     // Line number table:
/*      */     //   Java source line #248	-> byte code offset #0
/*      */     //   Java source line #250	-> byte code offset #5
/*      */     //   Java source line #251	-> byte code offset #9
/*      */     //   Java source line #252	-> byte code offset #15
/*      */     //   Java source line #254	-> byte code offset #30
/*      */     //   Java source line #258	-> byte code offset #58
/*      */     //   Java source line #259	-> byte code offset #73
/*      */     //   Java source line #258	-> byte code offset #76
/*      */     //   Java source line #260	-> byte code offset #96
/*      */     //   Java source line #261	-> byte code offset #108
/*      */     //   Java source line #263	-> byte code offset #114
/*      */     //   Java source line #265	-> byte code offset #122
/*      */     //   Java source line #266	-> byte code offset #134
/*      */     //   Java source line #267	-> byte code offset #148
/*      */     //   Java source line #271	-> byte code offset #152
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	170	0	this	VuforiaLocalizerImpl
/*      */     //   0	170	1	name	String
/*      */     //   0	170	2	type	int
/*      */     //   8	2	3	tracker	ObjectTracker
/*      */     //   13	89	4	dataSet	com.vuforia.DataSet
/*      */     //   76	18	5	localObject1	Object
/*      */     //   106	43	5	result	VuforiaTrackablesImpl
/*      */     //   140	6	7	localObject2	Object
/*      */     //   160	8	8	localObject3	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   30	58	76	finally
/*      */     //   76	78	76	finally
/*      */     //   122	137	140	finally
/*      */     //   140	145	140	finally
/*      */     //   5	152	160	finally
/*      */     //   160	162	160	finally
/*      */   }
/*      */   
/*      */   protected void showLoadingIndicator(final int visibility)
/*      */   {
/*  277 */     this.appUtil.runOnUiThread(new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/*  281 */         if (VuforiaLocalizerImpl.this.loadingIndicator != null)
/*      */         {
/*  283 */           VuforiaLocalizerImpl.this.loadingIndicator.setVisibility(visibility);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected void makeLoadingIndicator()
/*      */   {
/*  291 */     this.appUtil.synchronousRunOnUiThread(new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/*  295 */         VuforiaLocalizerImpl.this.loadingIndicatorOverlay = ((RelativeLayout)View.inflate(VuforiaLocalizerImpl.this.activity, R.layout.loading_indicator_overlay, null));
/*  296 */         VuforiaLocalizerImpl.this.loadingIndicator = VuforiaLocalizerImpl.this.loadingIndicatorOverlay.findViewById(R.id.loadingIndicator);
/*  297 */         VuforiaLocalizerImpl.this.loadingIndicator.setVisibility(4);
/*      */         
/*      */ 
/*  300 */         VuforiaLocalizerImpl.this.activity.addContentView(VuforiaLocalizerImpl.this.loadingIndicatorOverlay, new ViewGroup.LayoutParams(-1, -1));
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected void adjustExtendedTracking()
/*      */   {
/*  307 */     synchronized (this.loadedTrackableSets)
/*      */     {
/*  309 */       for (VuforiaTrackablesImpl set : this.loadedTrackableSets)
/*      */       {
/*  311 */         set.adjustExtendedTracking(this.isExtendedTrackingActive);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void destroyTrackables()
/*      */   {
/*  318 */     synchronized (this.loadedTrackableSets)
/*      */     {
/*  320 */       for (VuforiaTrackablesImpl trackable : this.loadedTrackableSets)
/*      */       {
/*  322 */         trackable.destroy();
/*      */       }
/*  324 */       this.loadedTrackableSets.clear();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void registerLifeCycleCallbacks()
/*      */   {
/*  334 */     this.appUtil.getApplication().registerActivityLifecycleCallbacks(this.lifeCycleCallbacks);
/*  335 */     this.opModeManager = OpModeManagerImpl.getOpModeManagerOfActivity(this.activity);
/*  336 */     if (this.opModeManager != null)
/*      */     {
/*  338 */       this.opModeManager.registerListener(this.opModeNotifications);
/*      */     }
/*      */   }
/*      */   
/*      */   private void unregisterLifeCycleCallbacks()
/*      */   {
/*  344 */     if (this.opModeManager != null)
/*      */     {
/*  346 */       this.opModeManager.unregisterListener(this.opModeNotifications);
/*      */     }
/*  348 */     this.appUtil.getApplication().unregisterActivityLifecycleCallbacks(this.lifeCycleCallbacks);
/*      */   }
/*      */   
/*      */ 
/*      */   protected class OpModeNotifications
/*      */     implements OpModeManagerNotifier.Notifications
/*      */   {
/*      */     protected OpModeNotifications() {}
/*      */     
/*      */ 
/*      */     public void onOpModePreInit(OpMode opMode) {}
/*      */     
/*      */     public void onOpModePreStart(OpMode opMode) {}
/*      */     
/*      */     public void onOpModePostStop(OpMode opMode)
/*      */     {
/*  364 */       VuforiaLocalizerImpl.this.close();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected class LifeCycleCallbacks
/*      */     implements Application.ActivityLifecycleCallbacks
/*      */   {
/*      */     protected LifeCycleCallbacks() {}
/*      */     
/*      */ 
/*      */     public void onActivityCreated(Activity activity, Bundle bundle) {}
/*      */     
/*      */ 
/*      */     public void onActivityStarted(Activity activity) {}
/*      */     
/*      */ 
/*      */     public void onActivityResumed(Activity activity)
/*      */     {
/*  383 */       if (activity == VuforiaLocalizerImpl.this.activity)
/*      */       {
/*  385 */         VuforiaLocalizerImpl.this.resumeAR();
/*  386 */         if (VuforiaLocalizerImpl.this.glSurface != null)
/*      */         {
/*  388 */           VuforiaLocalizerImpl.this.glSurface.setVisibility(0);
/*  389 */           VuforiaLocalizerImpl.this.glSurface.onResume();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void onActivityPaused(Activity activity)
/*      */     {
/*  397 */       if (activity == VuforiaLocalizerImpl.this.activity)
/*      */       {
/*  399 */         if (VuforiaLocalizerImpl.this.glSurface != null)
/*      */         {
/*  401 */           VuforiaLocalizerImpl.this.glSurface.setVisibility(4);
/*  402 */           VuforiaLocalizerImpl.this.glSurface.onPause();
/*      */         }
/*  404 */         VuforiaLocalizerImpl.this.pauseAR();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void onActivityStopped(Activity activity) {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void onActivityDestroyed(Activity activity)
/*      */     {
/*  416 */       if (activity == VuforiaLocalizerImpl.this.activity)
/*      */       {
/*  418 */         VuforiaLocalizerImpl.this.close();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void startAR()
/*      */   {
/*  434 */     synchronized (this.startStopLock)
/*      */     {
/*  436 */       showLoadingIndicator(0);
/*      */       
/*  438 */       updateActivityOrientation();
/*      */       
/*  440 */       this.vuforiaFlags = 2;
/*      */       
/*  442 */       Vuforia.setInitParameters(this.activity, this.vuforiaFlags, this.parameters.vuforiaLicenseKey);
/*  443 */       int initProgress = -1;
/*      */       do {
/*  445 */         initProgress = Vuforia.init();
/*      */       }
/*  447 */       while ((initProgress >= 0) && (initProgress < 100));
/*      */       
/*  449 */       if (initProgress < 0) {
/*  450 */         throwFailure("%s", new Object[] { getInitializationErrorString(initProgress) });
/*      */       }
/*  452 */       initTracker();
/*  453 */       Vuforia.registerCallback(this.vuforiaCallback);
/*      */       
/*      */ 
/*  456 */       int depthSize = 16;
/*  457 */       int stencilSize = 0;
/*  458 */       final boolean translucent = Vuforia.requiresAlpha();
/*      */       
/*  460 */       if (this.glSurfaceParent != null)
/*      */       {
/*  462 */         this.appUtil.synchronousRunOnUiThread(new Runnable() {
/*      */           public void run() {
/*  464 */             VuforiaLocalizerImpl.this.glSurface = new AutoConfigGLSurfaceView(VuforiaLocalizerImpl.this.activity);
/*  465 */             VuforiaLocalizerImpl.this.glSurface.init(translucent, 16, 0);
/*      */             
/*  467 */             VuforiaLocalizerImpl.this.glSurface.setRenderer(VuforiaLocalizerImpl.this.glSurfaceViewRenderer);
/*      */             
/*      */ 
/*      */ 
/*  471 */             VuforiaLocalizerImpl.this.glSurface.setVisibility(4);
/*  472 */             VuforiaLocalizerImpl.this.glSurfaceParent.addView(VuforiaLocalizerImpl.this.glSurface);
/*      */           }
/*      */         });
/*      */       }
/*  476 */       this.wantCamera = true;
/*  477 */       startCamera(this.parameters.cameraDirection.direction);
/*      */       
/*      */ 
/*  480 */       CameraDevice.getInstance().setFocusMode(2);
/*      */       
/*  482 */       this.rendererIsActive = true;
/*      */       
/*  484 */       showLoadingIndicator(4);
/*      */     }
/*      */   }
/*      */   
/*      */   private String getInitializationErrorString(int code)
/*      */   {
/*  490 */     switch (code) {
/*      */     case -2: 
/*  492 */       return this.activity.getString(R.string.VUFORIA_INIT_ERROR_DEVICE_NOT_SUPPORTED);
/*  493 */     case -3:  return this.activity.getString(R.string.VUFORIA_INIT_ERROR_NO_CAMERA_ACCESS);
/*  494 */     case -4:  return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_MISSING_KEY);
/*  495 */     case -5:  return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_INVALID_KEY);
/*  496 */     case -7:  return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_NO_NETWORK_TRANSIENT);
/*  497 */     case -6:  return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_NO_NETWORK_PERMANENT);
/*  498 */     case -8:  return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_CANCELED_KEY);
/*  499 */     case -9:  return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_PRODUCT_TYPE_MISMATCH); }
/*  500 */     return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_UNKNOWN_ERROR);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void stopAR()
/*      */   {
/*  506 */     synchronized (this.startStopLock)
/*      */     {
/*  508 */       this.wantCamera = false;
/*  509 */       stopCamera();
/*  510 */       destroyTrackables();
/*  511 */       deinitTracker();
/*  512 */       Vuforia.deinit();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void resumeAR()
/*      */   {
/*      */     
/*  519 */     if (this.wantCamera)
/*      */     {
/*  521 */       startCamera(this.cameraIndex);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void pauseAR()
/*      */   {
/*  527 */     stopCamera();
/*  528 */     Vuforia.onPause();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static ObjectTracker getObjectTracker()
/*      */   {
/*  538 */     return (ObjectTracker)TrackerManager.getInstance().getTracker(ObjectTracker.getClassType());
/*      */   }
/*      */   
/*      */   protected void initTracker()
/*      */   {
/*  543 */     TrackerManager.getInstance().initTracker(ObjectTracker.getClassType());
/*      */   }
/*      */   
/*      */   protected void startTracker()
/*      */   {
/*  548 */     getObjectTracker().start();
/*      */   }
/*      */   
/*      */   protected boolean isObjectTrackerResult(TrackableResult trackableResult)
/*      */   {
/*  553 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   protected void stopTracker()
/*      */   {
/*  559 */     getObjectTracker().stop();
/*      */   }
/*      */   
/*      */   protected void deinitTracker()
/*      */   {
/*  564 */     TrackerManager.getInstance().deinitTracker(ObjectTracker.getClassType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static RotationalDeviceTracker getRotationalDeviceTracker()
/*      */   {
/*  571 */     return (RotationalDeviceTracker)TrackerManager.getInstance().getTracker(RotationalDeviceTracker.getClassType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized CameraCalibration getCameraCalibration()
/*      */   {
/*  581 */     return this.camCal;
/*      */   }
/*      */   
/*      */   protected synchronized void startCamera(int cameraIndex)
/*      */   {
/*  586 */     throwIfFail(!this.isCameraRunning, "camera already running", new Object[0]);
/*      */     
/*  588 */     this.cameraIndex = cameraIndex;
/*      */     
/*  590 */     throwIfFail(CameraDevice.getInstance().init(cameraIndex), "unable to initialize camera #%d", new Object[] { Integer.valueOf(cameraIndex) });
/*  591 */     this.isCameraInited = true;
/*      */     
/*  593 */     throwIfFail(CameraDevice.getInstance().selectVideoMode(-1), "unable to select video mode on camera #%d", new Object[] { Integer.valueOf(cameraIndex) });
/*      */     
/*  595 */     configureVideoBackground();
/*      */     
/*  597 */     throwIfFail(CameraDevice.getInstance().start(), "unable to select start camera #%d", new Object[] { Integer.valueOf(cameraIndex) });
/*  598 */     this.isCameraStarted = true;
/*      */     
/*  600 */     this.camCal = CameraDevice.getInstance().getCameraCalibration();
/*  601 */     this.projectionMatrix = Tool.getProjectionGL(this.camCal, 10.0F, 5000.0F);
/*      */     
/*  603 */     startTracker();
/*      */     
/*  605 */     if (!CameraDevice.getInstance().setFocusMode(2))
/*      */     {
/*  607 */       if (!CameraDevice.getInstance().setFocusMode(1)) {
/*  608 */         CameraDevice.getInstance().setFocusMode(0);
/*      */       }
/*      */     }
/*  611 */     this.isCameraRunning = true;
/*      */   }
/*      */   
/*      */   synchronized void stopCamera()
/*      */   {
/*  616 */     if (this.isCameraRunning)
/*      */     {
/*  618 */       stopTracker();
/*  619 */       if (this.isCameraStarted) CameraDevice.getInstance().stop();
/*  620 */       if (this.isCameraInited) CameraDevice.getInstance().deinit();
/*  621 */       this.isCameraInited = false;
/*  622 */       this.isCameraStarted = false;
/*  623 */       this.isCameraRunning = false;
/*      */     }
/*      */   }
/*      */   
/*      */   void configureVideoBackground()
/*      */   {
/*  629 */     if (this.glSurface == null) {
/*  630 */       return;
/*      */     }
/*      */     
/*  633 */     PointF view = new PointF(this.glSurface.getWidth(), this.glSurface.getHeight());
/*      */     
/*      */ 
/*  636 */     CameraDevice cameraDevice = CameraDevice.getInstance();
/*  637 */     VideoMode videoMode = cameraDevice.getVideoMode(-1);
/*  638 */     PointF video = new PointF(videoMode.getWidth(), videoMode.getHeight());
/*      */     
/*  640 */     this.viewport = new ViewPort();
/*  641 */     if (this.isPortrait)
/*      */     {
/*  643 */       this.viewport.extent.x = ((int)(video.y * view.y / video.x));
/*  644 */       this.viewport.extent.y = ((int)view.y);
/*      */       
/*  646 */       if (this.viewport.extent.x < view.x)
/*      */       {
/*  648 */         if (this.fillSurfaceParent)
/*      */         {
/*  650 */           this.viewport.extent.x = ((int)view.x);
/*  651 */           this.viewport.extent.y = ((int)(view.x * video.x / video.y));
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  656 */           this.appUtil.synchronousRunOnUiThread(new Runnable() {
/*      */             public void run() {
/*  658 */               LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(VuforiaLocalizerImpl.this.viewport.extent.x, VuforiaLocalizerImpl.this.glSurface.getLayoutParams().height);
/*  659 */               params.gravity = 1;
/*  660 */               VuforiaLocalizerImpl.this.glSurface.setLayoutParams(params);
/*      */             }
/*      */           });
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  667 */       this.viewport.extent.x = ((int)view.x);
/*  668 */       this.viewport.extent.y = ((int)(video.y * view.x / video.x));
/*      */       
/*  670 */       if (this.viewport.extent.y < view.y)
/*      */       {
/*  672 */         if (this.fillSurfaceParent)
/*      */         {
/*  674 */           this.viewport.extent.x = ((int)(view.y * video.x / video.y));
/*  675 */           this.viewport.extent.y = ((int)view.y);
/*      */         }
/*      */         else
/*      */         {
/*  679 */           this.appUtil.synchronousRunOnUiThread(new Runnable() {
/*      */             public void run() {
/*  681 */               LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(VuforiaLocalizerImpl.this.glSurface.getLayoutParams().width, VuforiaLocalizerImpl.this.viewport.extent.y);
/*  682 */               params.gravity = 16;
/*  683 */               VuforiaLocalizerImpl.this.glSurface.setLayoutParams(params);
/*      */             }
/*      */           });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  690 */     view = new PointF(this.glSurface.getWidth(), this.glSurface.getHeight());
/*  691 */     this.appUtil.runOnUiThread(new Runnable() {
/*      */       public void run() {
/*  693 */         VuforiaLocalizerImpl.this.glSurface.setVisibility(0);
/*      */       }
/*  695 */     });
/*  696 */     VideoBackgroundConfig videoBackgroundConfig = new VideoBackgroundConfig();
/*  697 */     videoBackgroundConfig.setEnabled(true);
/*      */     
/*      */ 
/*  700 */     videoBackgroundConfig.setPosition(new Vec2I(0, 0));
/*  701 */     videoBackgroundConfig.setSize(new Vec2I(this.viewport.extent.x, this.viewport.extent.y));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  706 */     this.viewport.lowerLeft.x = ((int)((view.x - this.viewport.extent.x) / 2.0F) + videoBackgroundConfig.getPosition().getData()[0]);
/*  707 */     this.viewport.lowerLeft.y = ((int)((view.y - this.viewport.extent.y) / 2.0F) + videoBackgroundConfig.getPosition().getData()[1]);
/*      */     
/*      */ 
/*  710 */     this.viewport.lowerLeft.x = Math.min(0, this.viewport.lowerLeft.x);
/*  711 */     this.viewport.lowerLeft.y = Math.min(0, this.viewport.lowerLeft.y);
/*      */     
/*  713 */     Renderer.getInstance().setVideoBackgroundConfig(videoBackgroundConfig);
/*      */   }
/*      */   
/*      */ 
/*      */   private void updateActivityOrientation()
/*      */   {
/*  719 */     Configuration config = this.activity.getResources().getConfiguration();
/*  720 */     switch (config.orientation)
/*      */     {
/*      */     case 2: 
/*  723 */       this.isPortrait = false;
/*  724 */       break;
/*      */     case 0: 
/*      */     case 1: 
/*      */     default: 
/*  728 */       this.isPortrait = true;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */   void initRendering()
/*      */   {
/*  735 */     this.teapot = new Teapot();
/*  736 */     this.renderer = Renderer.getInstance();
/*      */     
/*      */ 
/*  739 */     GLES20.glClearColor(0.0F, 0.0F, 0.0F, Vuforia.requiresAlpha() ? 0.0F : 1.0F);
/*      */     
/*  741 */     for (Texture t : this.textures)
/*      */     {
/*  743 */       GLES20.glGenTextures(1, t.mTextureID, 0);
/*  744 */       GLES20.glBindTexture(3553, t.mTextureID[0]);
/*  745 */       GLES20.glTexParameterf(3553, 10241, 9729.0F);
/*  746 */       GLES20.glTexParameterf(3553, 10240, 9729.0F);
/*  747 */       GLES20.glTexImage2D(3553, 0, 6408, t.mWidth, t.mHeight, 0, 6408, 5121, t.mData);
/*      */     }
/*      */     
/*  750 */     this.cubeMeshProgram = new CubeMeshProgram(this.activity);
/*  751 */     this.simpleColorProgram = new SimpleColorProgram(this.activity);
/*      */     
/*  753 */     this.buildingsModel = new SavedMeshObject();
/*      */     try {
/*  755 */       this.buildingsModel.loadModel(this.activity.getResources().getAssets(), "Buildings.txt");
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  759 */       throwFailure(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void loadTextures()
/*      */   {
/*  769 */     this.buildingsTexture = Texture.loadTextureFromApk("Buildings.jpeg", this.activity.getAssets());
/*  770 */     this.teapotTexture = Texture.loadTextureFromApk("TextureTeapotBrass.png", this.activity.getAssets());
/*  771 */     this.textures = new LinkedList();
/*  772 */     this.textures.add(this.buildingsTexture);
/*  773 */     this.textures.add(this.teapotTexture);
/*      */   }
/*      */   
/*      */   protected class GLSurfaceViewRenderer implements GLSurfaceView.Renderer {
/*      */     protected GLSurfaceViewRenderer() {}
/*      */     
/*      */     public void onSurfaceCreated(GL10 gl, EGLConfig config) {
/*  780 */       VuforiaLocalizerImpl.this.initRendering();
/*      */       
/*      */ 
/*      */ 
/*  784 */       Vuforia.onSurfaceCreated();
/*      */     }
/*      */     
/*      */     public void onSurfaceChanged(GL10 gl, int width, int height)
/*      */     {
/*  789 */       Vuforia.onSurfaceChanged(width, height);
/*      */     }
/*      */     
/*      */     public void onDrawFrame(GL10 gl)
/*      */     {
/*  794 */       if (VuforiaLocalizerImpl.this.rendererIsActive)
/*      */       {
/*  796 */         VuforiaLocalizerImpl.this.renderFrame();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void renderFrame()
/*      */   {
/*  803 */     if (this.glSurface == null) {
/*  804 */       return;
/*      */     }
/*  806 */     GLES20.glClear(16640);
/*      */     
/*  808 */     State state = this.renderer.begin();
/*  809 */     this.renderer.drawVideoBackground();
/*      */     
/*  811 */     GLES20.glEnable(2929);
/*      */     
/*      */ 
/*  814 */     GLES20.glViewport(this.viewport.lowerLeft.x, this.viewport.lowerLeft.y, this.viewport.extent.x, this.viewport.extent.y);
/*      */     
/*      */ 
/*  817 */     GLES20.glEnable(2884);
/*  818 */     GLES20.glCullFace(1029);
/*  819 */     if (Renderer.getInstance().getVideoBackgroundConfig().getReflection() == 1) {
/*  820 */       GLES20.glFrontFace(2304);
/*      */     } else {
/*  822 */       GLES20.glFrontFace(2305);
/*      */     }
/*      */     
/*  825 */     for (int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++)
/*      */     {
/*  827 */       TrackableResult trackableResult = state.getTrackableResult(tIdx);
/*  828 */       if (isObjectTrackerResult(trackableResult))
/*      */       {
/*  830 */         Matrix44F poseMatrixOpenGl = Tool.convertPose2GLMatrix(trackableResult.getPose());
/*  831 */         float[] poseMatrix = poseMatrixOpenGl.getData();
/*      */         
/*  833 */         switch (this.cameraCameraMonitorFeedback) {
/*      */         case NONE: 
/*      */           break;
/*  836 */         case BUILDINGS:  drawBuildings(poseMatrix); break;
/*  837 */         case TEAPOT:  drawTeapot(poseMatrix); break;
/*  838 */         case AXES:  drawAxes(poseMatrix);
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*  843 */     GLES20.glDisable(2929);
/*  844 */     this.renderer.end();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void drawAxes(float[] poseMatrix)
/*      */   {
/*  850 */     this.coordinateAxes.draw(poseMatrix);
/*      */   }
/*      */   
/*      */   class CoordinateAxes
/*      */   {
/*      */     private SolidCylinder axis;
/*      */     
/*      */     public CoordinateAxes()
/*      */     {
/*  859 */       float radius = 0.05F;
/*  860 */       float height = 1.0F;
/*  861 */       this.axis = new SolidCylinder(radius, height, 32);
/*      */     }
/*      */     
/*      */ 
/*      */     public void draw(float[] poseMatrixIn)
/*      */     {
/*  867 */       drawAxis(poseMatrixIn, this.axis, -65536, 0.0F, 0.0F, -1.0F, 0.0F, this.axis.height / 2.0F, 0.0F);
/*  868 */       drawAxis(poseMatrixIn, this.axis, -16776961, -1.0F, 0.0F, 0.0F, 0.0F, -this.axis.height / 2.0F, 0.0F);
/*  869 */       drawAxis(poseMatrixIn, this.axis, -16711936, 0.0F, 0.0F, 0.0F, 0.0F, this.axis.height / 2.0F, 0.0F);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void drawAxis(float[] poseMatrixIn, SolidCylinder axis, int color, float rx, float ry, float rz, float dx, float dy, float dz)
/*      */     {
/*  876 */       float axesScale = 100.0F;
/*      */       
/*  878 */       float[] poseMatrix = Arrays.copyOf(poseMatrixIn, poseMatrixIn.length);
/*      */       
/*      */ 
/*      */ 
/*  882 */       if ((rx != 0.0F) || (ry != 0.0F) || (rz != 0.0F)) Matrix.rotateM(poseMatrix, 0, 90.0F, rx, ry, rz);
/*  883 */       Matrix.translateM(poseMatrix, 0, dx * axesScale, dy * axesScale, dz * axesScale);
/*      */       
/*  885 */       Matrix.scaleM(poseMatrix, 0, axesScale, axesScale, axesScale);
/*      */       
/*  887 */       float[] modelViewProjectionMatrix = new float[16];
/*  888 */       Matrix.multiplyMM(modelViewProjectionMatrix, 0, VuforiaLocalizerImpl.this.projectionMatrix.getData(), 0, poseMatrix, 0);
/*      */       
/*  890 */       VuforiaLocalizerImpl.this.simpleColorProgram.useProgram();
/*  891 */       VuforiaLocalizerImpl.this.simpleColorProgram.fragment.setColor(color);
/*      */       
/*  893 */       axis.bindData(VuforiaLocalizerImpl.this.simpleColorProgram.vertex);
/*      */       
/*  895 */       VuforiaLocalizerImpl.this.simpleColorProgram.vertex.setModelViewProjectionMatrix(modelViewProjectionMatrix);
/*  896 */       axis.draw();
/*  897 */       VuforiaLocalizerImpl.this.simpleColorProgram.vertex.disableAttributes();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void drawTeapot(float[] poseMatrix)
/*      */   {
/*  904 */     Matrix.scaleM(poseMatrix, 0, this.teapotScale, this.teapotScale, this.teapotScale);
/*      */     
/*  906 */     float[] modelViewProjectionMatrix = new float[16];
/*  907 */     Matrix.multiplyMM(modelViewProjectionMatrix, 0, this.projectionMatrix.getData(), 0, poseMatrix, 0);
/*      */     
/*  909 */     this.cubeMeshProgram.useProgram();
/*  910 */     this.cubeMeshProgram.vertex.setCoordinates(this.teapot);
/*  911 */     this.cubeMeshProgram.fragment.setTexture(this.teapotTexture);
/*  912 */     this.cubeMeshProgram.vertex.setModelViewProjectionMatrix(modelViewProjectionMatrix);
/*      */     
/*  914 */     GLES20.glDrawElements(4, this.teapot.getNumObjectIndex(), 5123, this.teapot.getIndices());
/*      */     
/*  916 */     this.cubeMeshProgram.vertex.disableAttributes();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void drawBuildings(float[] poseMatrix)
/*      */   {
/*  922 */     Matrix.rotateM(poseMatrix, 0, 90.0F, 1.0F, 0.0F, 0.0F);
/*  923 */     Matrix.scaleM(poseMatrix, 0, this.buildingsScale, this.buildingsScale, this.buildingsScale);
/*      */     
/*  925 */     float[] modelViewProjectionMatrix = new float[16];
/*  926 */     Matrix.multiplyMM(modelViewProjectionMatrix, 0, this.projectionMatrix.getData(), 0, poseMatrix, 0);
/*      */     
/*      */ 
/*  929 */     this.cubeMeshProgram.useProgram();
/*      */     
/*  931 */     GLES20.glDisable(2884);
/*  932 */     this.cubeMeshProgram.vertex.setCoordinates(this.buildingsModel);
/*  933 */     this.cubeMeshProgram.fragment.setTexture(this.buildingsTexture);
/*  934 */     this.cubeMeshProgram.vertex.setModelViewProjectionMatrix(modelViewProjectionMatrix);
/*      */     
/*  936 */     GLES20.glDrawArrays(4, 0, this.buildingsModel.getNumObjectVertex());
/*      */     
/*  938 */     this.cubeMeshProgram.vertex.disableAttributes();
/*      */   }
/*      */   
/*      */ 
/*      */   protected class VuforiaCallback
/*      */     implements Vuforia.UpdateCallbackInterface
/*      */   {
/*      */     protected VuforiaCallback() {}
/*      */     
/*      */     public synchronized void Vuforia_onUpdate(State state)
/*      */     {
/*  949 */       Set<VuforiaTrackableImpl> notVisible = new HashSet();
/*      */       
/*  951 */       synchronized (VuforiaLocalizerImpl.this.loadedTrackableSets)
/*      */       {
/*  953 */         for (VuforiaTrackablesImpl trackables : VuforiaLocalizerImpl.this.loadedTrackableSets)
/*      */         {
/*  955 */           for (VuforiaTrackable vuforiaTrackable : trackables)
/*      */           {
/*  957 */             notVisible.add((VuforiaTrackableImpl)vuforiaTrackable);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  962 */       for (int i = 0; i < state.getNumTrackableResults(); i++)
/*      */       {
/*  964 */         TrackableResult trackableResult = state.getTrackableResult(i);
/*  965 */         if (VuforiaLocalizerImpl.this.isObjectTrackerResult(trackableResult))
/*      */         {
/*  967 */           VuforiaTrackableImpl trackable = VuforiaTrackableImpl.getTrackable(trackableResult.getTrackable());
/*  968 */           notVisible.remove(trackable);
/*  969 */           trackable.noteTracked(trackableResult);
/*      */         }
/*      */       }
/*      */       
/*  973 */       for (VuforiaTrackableImpl trackable : notVisible)
/*      */       {
/*  975 */         trackable.noteNotTracked();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class FailureException
/*      */     extends RuntimeException
/*      */   {
/*      */     protected Exception nestedException;
/*      */     
/*      */ 
/*      */     public FailureException(String format, Object... args)
/*      */     {
/*  990 */       super();
/*      */     }
/*      */     
/*      */     public FailureException(Exception nestedException, String format, Object... args)
/*      */     {
/*  995 */       super();
/*  996 */       this.nestedException = nestedException;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static void throwIfFail(boolean success)
/*      */   {
/* 1002 */     if (!success)
/*      */     {
/* 1004 */       throwFailure();
/*      */     }
/*      */   }
/*      */   
/*      */   protected static void throwIfFail(boolean success, String format, Object... args)
/*      */   {
/* 1010 */     if (!success)
/*      */     {
/* 1012 */       throwFailure(format, args);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static void throwFailure()
/*      */   {
/* 1018 */     throwFailure("Vuforia operation failed", new Object[0]);
/*      */   }
/*      */   
/*      */   protected static void throwFailure(String format, Object... args)
/*      */   {
/* 1023 */     throw new FailureException(format, args);
/*      */   }
/*      */   
/*      */   protected static void throwFailure(Exception nested)
/*      */   {
/* 1028 */     throw new FailureException(nested, "Vuforia operation failed", new Object[0]);
/*      */   }
/*      */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\VuforiaLocalizerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */