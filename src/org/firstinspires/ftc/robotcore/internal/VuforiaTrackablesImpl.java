/*     */ package org.firstinspires.ftc.robotcore.internal;
/*     */ 
/*     */ import com.vuforia.DataSet;
/*     */ import com.vuforia.ObjectTracker;
/*     */ import com.vuforia.Trackable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
/*     */ import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VuforiaTrackablesImpl
/*     */   extends AbstractList<VuforiaTrackable>
/*     */   implements VuforiaTrackables
/*     */ {
/*     */   VuforiaLocalizerImpl vuforiaLocalizer;
/*     */   DataSet dataSet;
/*     */   String name;
/*     */   boolean isActive;
/*     */   List<VuforiaTrackableImpl> trackables;
/*     */   
/*     */   public VuforiaTrackablesImpl(VuforiaLocalizerImpl vuforiaLocalizer, DataSet dataSet)
/*     */   {
/*  67 */     this.vuforiaLocalizer = vuforiaLocalizer;
/*  68 */     this.dataSet = dataSet;
/*  69 */     this.isActive = false;
/*  70 */     this.trackables = new ArrayList(this.dataSet.getNumTrackables());
/*  71 */     for (int i = 0; i < this.dataSet.getNumTrackables(); i++)
/*     */     {
/*  73 */       this.trackables.add(new VuforiaTrackableImpl(this, i));
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void setName(String name)
/*     */   {
/*  79 */     this.name = name;
/*  80 */     for (VuforiaTrackableImpl trackable : this.trackables)
/*     */     {
/*  82 */       if (trackable.getName() == null)
/*     */       {
/*  84 */         trackable.setName(name);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized String getName()
/*     */   {
/*  91 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 100 */     return this.trackables.size();
/*     */   }
/*     */   
/*     */   public VuforiaTrackable get(int index)
/*     */   {
/* 105 */     return (VuforiaTrackable)this.trackables.get(index);
/*     */   }
/*     */   
/*     */   public VuforiaLocalizer getLocalizer()
/*     */   {
/* 110 */     return this.vuforiaLocalizer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void activate()
/*     */   {
/* 119 */     if (!this.isActive)
/*     */     {
/* 121 */       VuforiaLocalizerImpl.throwIfFail(VuforiaLocalizerImpl.getObjectTracker().activateDataSet(this.dataSet));
/* 122 */       this.isActive = true;
/*     */       
/* 124 */       adjustExtendedTracking(this.vuforiaLocalizer.isExtendedTrackingActive);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void deactivate()
/*     */   {
/* 130 */     if (this.isActive)
/*     */     {
/* 132 */       VuforiaLocalizerImpl.throwIfFail(VuforiaLocalizerImpl.getObjectTracker().deactivateDataSet(this.dataSet));
/* 133 */       this.isActive = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public void adjustExtendedTracking(boolean isExtendedTrackingActive)
/*     */   {
/* 139 */     if (this.isActive)
/*     */     {
/* 141 */       for (VuforiaTrackableImpl trackable : this.trackables)
/*     */       {
/* 143 */         if (isExtendedTrackingActive) {
/* 144 */           trackable.getVuforiaTrackable().startExtendedTracking();
/*     */         } else {
/* 146 */           trackable.getVuforiaTrackable().stopExtendedTracking();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void destroy() {
/* 153 */     deactivate();
/* 154 */     VuforiaLocalizerImpl.getObjectTracker().destroyDataSet(this.dataSet);
/* 155 */     this.dataSet = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\VuforiaTrackablesImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */