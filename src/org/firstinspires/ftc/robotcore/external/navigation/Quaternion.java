/*     */ package org.firstinspires.ftc.robotcore.external.navigation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Quaternion
/*     */ {
/*     */   public float w;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float x;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float y;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float z;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long acquisitionTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Quaternion()
/*     */   {
/*  67 */     this(0.0F, 0.0F, 0.0F, 0.0F, 0L);
/*     */   }
/*     */   
/*     */   public Quaternion(float w, float x, float y, float z, long acquisitionTime)
/*     */   {
/*  72 */     this.w = w;
/*  73 */     this.x = x;
/*  74 */     this.y = y;
/*  75 */     this.z = z;
/*  76 */     this.acquisitionTime = acquisitionTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float magnitude()
/*     */   {
/*  85 */     return (float)Math.sqrt(this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
/*     */   }
/*     */   
/*     */   public Quaternion normalized()
/*     */   {
/*  90 */     float mag = magnitude();
/*  91 */     return new Quaternion(this.w / mag, this.x / mag, this.y / mag, this.z / mag, this.acquisitionTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Quaternion congugate()
/*     */   {
/* 101 */     return new Quaternion(this.w, -this.x, -this.y, -this.z, this.acquisitionTime);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\Quaternion.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */