/*     */ package org.firstinspires.ftc.robotcore.internal;
/*     */ 
/*     */ import com.vuforia.DataSet;
/*     */ import com.vuforia.Trackable;
/*     */ import com.vuforia.TrackableResult;
/*     */ import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable.Listener;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class VuforiaTrackableImpl
/*     */   implements VuforiaTrackable
/*     */ {
/*     */   protected VuforiaTrackablesImpl trackables;
/*     */   protected int index;
/*     */   protected String name;
/*     */   protected VuforiaTrackable.Listener listener;
/*     */   protected Object userData;
/*  53 */   protected final Object locationLock = new Object();
/*     */   protected OpenGLMatrix location;
/*     */   
/*     */   VuforiaTrackableImpl(VuforiaTrackablesImpl trackables, int index)
/*     */   {
/*  58 */     this.trackables = trackables;
/*  59 */     this.index = index;
/*  60 */     this.userData = null;
/*  61 */     this.location = null;
/*  62 */     this.name = null;
/*  63 */     this.listener = new VuforiaTrackableDefaultListener(this);
/*  64 */     getVuforiaTrackable().setUserData(this);
/*     */   }
/*     */   
/*     */   static VuforiaTrackableImpl getTrackable(Trackable trackable)
/*     */   {
/*  69 */     return (VuforiaTrackableImpl)trackable.getUserData();
/*     */   }
/*     */   
/*     */   public static VuforiaTrackableImpl getTrackable(TrackableResult trackableResult)
/*     */   {
/*  74 */     return getTrackable(trackableResult.getTrackable());
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void setListener(VuforiaTrackable.Listener listener)
/*     */   {
/*  80 */     this.listener = (listener == null ? new VuforiaTrackableDefaultListener(this) : listener);
/*     */   }
/*     */   
/*     */   public synchronized VuforiaTrackable.Listener getListener()
/*     */   {
/*  85 */     return this.listener;
/*     */   }
/*     */   
/*     */   public synchronized void setUserData(Object object)
/*     */   {
/*  90 */     this.userData = object;
/*     */   }
/*     */   
/*     */   public synchronized Object getUserData()
/*     */   {
/*  95 */     return this.userData;
/*     */   }
/*     */   
/*     */   public VuforiaTrackables getTrackables()
/*     */   {
/* 100 */     return this.trackables;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void setLocation(OpenGLMatrix location)
/*     */   {
/* 106 */     synchronized (this.locationLock)
/*     */     {
/* 108 */       this.location = location;
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public synchronized OpenGLMatrix getLocation()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 3	org/firstinspires/ftc/robotcore/internal/VuforiaTrackableImpl:locationLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 7	org/firstinspires/ftc/robotcore/internal/VuforiaTrackableImpl:location	Lorg/firstinspires/ftc/robotcore/external/matrices/OpenGLMatrix;
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: areturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #114	-> byte code offset #0
/*     */     //   Java source line #116	-> byte code offset #7
/*     */     //   Java source line #117	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	VuforiaTrackableImpl
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 122 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name)
/*     */   {
/* 127 */     this.name = name;
/*     */   }
/*     */   
/*     */   public Trackable getVuforiaTrackable()
/*     */   {
/* 132 */     return this.trackables.dataSet.getTrackable(this.index);
/*     */   }
/*     */   
/*     */   synchronized void noteNotTracked()
/*     */   {
/* 137 */     getListener().onNotTracked();
/*     */   }
/*     */   
/*     */   synchronized void noteTracked(TrackableResult trackableResult)
/*     */   {
/* 142 */     getListener().onTracked(trackableResult);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\VuforiaTrackableImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */