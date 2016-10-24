/*     */ package com.qualcomm.robotcore.eventloop.opmode;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpModeMeta
/*     */ {
/*     */   public static final String DefaultGroup = "$$$$$$$";
/*     */   @NonNull
/*     */   public final Flavor flavor;
/*     */   @NonNull
/*     */   public final String group;
/*     */   @NonNull
/*     */   public final String name;
/*     */   
/*     */   public static enum Flavor
/*     */   {
/*  47 */     AUTONOMOUS,  TELEOP;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Flavor() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpModeMeta()
/*     */   {
/*  66 */     this("");
/*     */   }
/*     */   
/*     */   public OpModeMeta(@NonNull String name) {
/*  70 */     this(name, Flavor.TELEOP);
/*     */   }
/*     */   
/*     */   public OpModeMeta(@NonNull String name, @NonNull Flavor flavor) {
/*  74 */     this(name, flavor, "$$$$$$$");
/*     */   }
/*     */   
/*     */   public OpModeMeta(@NonNull Flavor flavor, @NonNull String group) {
/*  78 */     this("", flavor, group);
/*     */   }
/*     */   
/*     */   public OpModeMeta(@NonNull String name, @NonNull Flavor flavor, @NonNull String group) {
/*  82 */     this.name = name;
/*  83 */     this.flavor = flavor;
/*  84 */     this.group = group;
/*     */   }
/*     */   
/*     */   public static OpModeMeta forName(@NonNull String name, @NonNull OpModeMeta base)
/*     */   {
/*  89 */     return new OpModeMeta(name, base.flavor, base.group);
/*     */   }
/*     */   
/*     */   public static OpModeMeta forGroup(@NonNull String group, @NonNull OpModeMeta base) {
/*  93 */     return new OpModeMeta(base.name, base.flavor, group);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 113 */     if ((o instanceof OpModeMeta))
/*     */     {
/* 115 */       return this.name.equals(((OpModeMeta)o).name);
/*     */     }
/*     */     
/* 118 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 123 */     return this.name.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\opmode\OpModeMeta.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */