/*     */ package org.firstinspires.ftc.robotcore.external.navigation;
/*     */ 
/*     */ import junit.framework.Assert;
/*     */ import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
/*     */ import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
/*     */ import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Orientation
/*     */ {
/*     */   public AxesReference axesReference;
/*     */   public AxesOrder axesOrder;
/*     */   public AngleUnit angleUnit;
/*     */   public float firstAngle;
/*     */   public float secondAngle;
/*     */   public float thirdAngle;
/*     */   public long acquisitionTime;
/*     */   
/*     */   public Orientation()
/*     */   {
/* 157 */     this(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS, 0.0F, 0.0F, 0.0F, 0L);
/*     */   }
/*     */   
/*     */   public Orientation(AxesReference axesReference, AxesOrder axesOrder, AngleUnit angleUnit, float firstAngle, float secondAngle, float thirdAngle, long acquisitionTime)
/*     */   {
/* 162 */     this.axesReference = axesReference;
/* 163 */     this.axesOrder = axesOrder;
/* 164 */     this.angleUnit = angleUnit;
/* 165 */     this.firstAngle = firstAngle;
/* 166 */     this.secondAngle = secondAngle;
/* 167 */     this.thirdAngle = thirdAngle;
/* 168 */     this.acquisitionTime = acquisitionTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Orientation toAngleUnit(AngleUnit angleUnit)
/*     */   {
/* 179 */     if (angleUnit != this.angleUnit)
/*     */     {
/* 181 */       return new Orientation(this.axesReference, this.axesOrder, angleUnit, angleUnit.fromUnit(this.angleUnit, this.firstAngle), angleUnit.fromUnit(this.angleUnit, this.secondAngle), angleUnit.fromUnit(this.angleUnit, this.thirdAngle), this.acquisitionTime);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 188 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Orientation toAxesReference(AxesReference axesReference)
/*     */   {
/* 199 */     if (this.axesReference != axesReference)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */       Assert.assertTrue(axesReference == this.axesReference.reverse());
/* 207 */       return new Orientation(this.axesReference.reverse(), this.axesOrder.reverse(), this.angleUnit, this.thirdAngle, this.secondAngle, this.firstAngle, this.acquisitionTime);
/*     */     }
/*     */     
/*     */ 
/* 211 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Orientation toAxesOrder(AxesOrder axesOrder)
/*     */   {
/* 221 */     if (this.axesOrder != axesOrder)
/*     */     {
/* 223 */       return getOrientation(getRotationMatrix(), this.axesReference, axesOrder, this.angleUnit);
/*     */     }
/*     */     
/* 226 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 235 */     if (this.angleUnit == AngleUnit.DEGREES) {
/* 236 */       return String.format("{%s %s %.0f %.0f %.0f}", new Object[] { this.axesReference.toString(), this.axesOrder.toString(), Float.valueOf(this.firstAngle), Float.valueOf(this.secondAngle), Float.valueOf(this.thirdAngle) });
/*     */     }
/* 238 */     return String.format("{%s %s %.3f %.3f %.3f}", new Object[] { this.axesReference.toString(), this.axesOrder.toString(), Float.valueOf(this.firstAngle), Float.valueOf(this.secondAngle), Float.valueOf(this.thirdAngle) });
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
/*     */   public OpenGLMatrix getRotationMatrix()
/*     */   {
/* 254 */     return getRotationMatrix(this.axesReference, this.axesOrder, this.angleUnit, this.firstAngle, this.secondAngle, this.thirdAngle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OpenGLMatrix getRotationMatrix(AxesReference axesReference, AxesOrder axesOrder, AngleUnit unit, float firstAngle, float secondAngle, float thirdAngle)
/*     */   {
/* 266 */     if (axesReference == AxesReference.INTRINSIC)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 273 */       return getRotationMatrix(axesReference.reverse(), axesOrder.reverse(), unit, thirdAngle, secondAngle, firstAngle);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 290 */     firstAngle = unit.toRadians(firstAngle);
/* 291 */     secondAngle = unit.toRadians(secondAngle);
/* 292 */     thirdAngle = unit.toRadians(thirdAngle);
/*     */     float m22;
/*     */     float m22;
/*     */     float m22;
/*     */     float m22;
/*     */     float m22;
/* 298 */     float m22; float m22; float m22; float m22; float m22; float m22; float m00; float m01; float m02; float m10; float m11; float m12; float m20; float m21; float m22; switch (axesOrder)
/*     */     {
/*     */     case XZX: 
/*     */     default: 
/* 302 */       float m00 = (float)Math.cos(secondAngle);
/* 303 */       float m01 = (float)-(Math.cos(firstAngle) * Math.sin(secondAngle));
/* 304 */       float m02 = (float)(Math.sin(firstAngle) * Math.sin(secondAngle));
/* 305 */       float m10 = (float)(Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 306 */       float m11 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle) * Math.cos(thirdAngle) - Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 307 */       float m12 = (float)(-(Math.cos(firstAngle) * Math.sin(thirdAngle)) - Math.cos(secondAngle) * Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 308 */       float m20 = (float)(Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 309 */       float m21 = (float)(Math.cos(thirdAngle) * Math.sin(firstAngle) + Math.cos(firstAngle) * Math.cos(secondAngle) * Math.sin(thirdAngle));
/* 310 */       m22 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) - Math.cos(secondAngle) * Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 311 */       break;
/*     */     case XYX: 
/* 313 */       float m00 = (float)Math.cos(secondAngle);
/* 314 */       float m01 = (float)(Math.sin(firstAngle) * Math.sin(secondAngle));
/* 315 */       float m02 = (float)(Math.cos(firstAngle) * Math.sin(secondAngle));
/* 316 */       float m10 = (float)(Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 317 */       float m11 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) - Math.cos(secondAngle) * Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 318 */       float m12 = (float)(-(Math.cos(firstAngle) * Math.cos(secondAngle) * Math.sin(thirdAngle)) - Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 319 */       float m20 = (float)-(Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 320 */       float m21 = (float)(Math.cos(firstAngle) * Math.sin(thirdAngle) + Math.cos(secondAngle) * Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 321 */       m22 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle) * Math.cos(thirdAngle) - Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 322 */       break;
/*     */     case YXY: 
/* 324 */       float m00 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) - Math.cos(secondAngle) * Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 325 */       float m01 = (float)(Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 326 */       float m02 = (float)(Math.cos(thirdAngle) * Math.sin(firstAngle) + Math.cos(firstAngle) * Math.cos(secondAngle) * Math.sin(thirdAngle));
/* 327 */       float m10 = (float)(Math.sin(firstAngle) * Math.sin(secondAngle));
/* 328 */       float m11 = (float)Math.cos(secondAngle);
/* 329 */       float m12 = (float)-(Math.cos(firstAngle) * Math.sin(secondAngle));
/* 330 */       float m20 = (float)(-(Math.cos(firstAngle) * Math.sin(thirdAngle)) - Math.cos(secondAngle) * Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 331 */       float m21 = (float)(Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 332 */       m22 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle) * Math.cos(thirdAngle) - Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 333 */       break;
/*     */     case YZY: 
/* 335 */       float m00 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle) * Math.cos(thirdAngle) - Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 336 */       float m01 = (float)-(Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 337 */       float m02 = (float)(Math.cos(firstAngle) * Math.sin(thirdAngle) + Math.cos(secondAngle) * Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 338 */       float m10 = (float)(Math.cos(firstAngle) * Math.sin(secondAngle));
/* 339 */       float m11 = (float)Math.cos(secondAngle);
/* 340 */       float m12 = (float)(Math.sin(firstAngle) * Math.sin(secondAngle));
/* 341 */       float m20 = (float)(-(Math.cos(firstAngle) * Math.cos(secondAngle) * Math.sin(thirdAngle)) - Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 342 */       float m21 = (float)(Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 343 */       m22 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) - Math.cos(secondAngle) * Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 344 */       break;
/*     */     case ZYZ: 
/* 346 */       float m00 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle) * Math.cos(thirdAngle) - Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 347 */       float m01 = (float)(-(Math.cos(firstAngle) * Math.sin(thirdAngle)) - Math.cos(secondAngle) * Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 348 */       float m02 = (float)(Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 349 */       float m10 = (float)(Math.cos(thirdAngle) * Math.sin(firstAngle) + Math.cos(firstAngle) * Math.cos(secondAngle) * Math.sin(thirdAngle));
/* 350 */       float m11 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) - Math.cos(secondAngle) * Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 351 */       float m12 = (float)(Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 352 */       float m20 = (float)-(Math.cos(firstAngle) * Math.sin(secondAngle));
/* 353 */       float m21 = (float)(Math.sin(firstAngle) * Math.sin(secondAngle));
/* 354 */       m22 = (float)Math.cos(secondAngle);
/* 355 */       break;
/*     */     case ZXZ: 
/* 357 */       float m00 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) - Math.cos(secondAngle) * Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 358 */       float m01 = (float)(-(Math.cos(firstAngle) * Math.cos(secondAngle) * Math.sin(thirdAngle)) - Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 359 */       float m02 = (float)(Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 360 */       float m10 = (float)(Math.cos(firstAngle) * Math.sin(thirdAngle) + Math.cos(secondAngle) * Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 361 */       float m11 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle) * Math.cos(thirdAngle) - Math.sin(firstAngle) * Math.sin(thirdAngle));
/* 362 */       float m12 = (float)-(Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 363 */       float m20 = (float)(Math.sin(firstAngle) * Math.sin(secondAngle));
/* 364 */       float m21 = (float)(Math.cos(firstAngle) * Math.sin(secondAngle));
/* 365 */       m22 = (float)Math.cos(secondAngle);
/* 366 */       break;
/*     */     case XZY: 
/* 368 */       float m00 = (float)(Math.cos(secondAngle) * Math.cos(thirdAngle));
/* 369 */       float m01 = (float)(Math.sin(firstAngle) * Math.sin(thirdAngle) - Math.cos(firstAngle) * Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 370 */       float m02 = (float)(Math.cos(firstAngle) * Math.sin(thirdAngle) + Math.cos(thirdAngle) * Math.sin(firstAngle) * Math.sin(secondAngle));
/* 371 */       float m10 = (float)Math.sin(secondAngle);
/* 372 */       float m11 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle));
/* 373 */       float m12 = (float)-(Math.cos(secondAngle) * Math.sin(firstAngle));
/* 374 */       float m20 = (float)-(Math.cos(secondAngle) * Math.sin(thirdAngle));
/* 375 */       float m21 = (float)(Math.cos(thirdAngle) * Math.sin(firstAngle) + Math.cos(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 376 */       m22 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) - Math.sin(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 377 */       break;
/*     */     case XYZ: 
/* 379 */       float m00 = (float)(Math.cos(secondAngle) * Math.cos(thirdAngle));
/* 380 */       float m01 = (float)(Math.cos(thirdAngle) * Math.sin(firstAngle) * Math.sin(secondAngle) - Math.cos(firstAngle) * Math.sin(thirdAngle));
/* 381 */       float m02 = (float)(Math.sin(firstAngle) * Math.sin(thirdAngle) + Math.cos(firstAngle) * Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 382 */       float m10 = (float)(Math.cos(secondAngle) * Math.sin(thirdAngle));
/* 383 */       float m11 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) + Math.sin(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 384 */       float m12 = (float)(Math.cos(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle) - Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 385 */       float m20 = (float)-Math.sin(secondAngle);
/* 386 */       float m21 = (float)(Math.cos(secondAngle) * Math.sin(firstAngle));
/* 387 */       m22 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle));
/* 388 */       break;
/*     */     case YXZ: 
/* 390 */       float m00 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) - Math.sin(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 391 */       float m01 = (float)-(Math.cos(secondAngle) * Math.sin(thirdAngle));
/* 392 */       float m02 = (float)(Math.cos(thirdAngle) * Math.sin(firstAngle) + Math.cos(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 393 */       float m10 = (float)(Math.cos(firstAngle) * Math.sin(thirdAngle) + Math.cos(thirdAngle) * Math.sin(firstAngle) * Math.sin(secondAngle));
/* 394 */       float m11 = (float)(Math.cos(secondAngle) * Math.cos(thirdAngle));
/* 395 */       float m12 = (float)(Math.sin(firstAngle) * Math.sin(thirdAngle) - Math.cos(firstAngle) * Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 396 */       float m20 = (float)-(Math.cos(secondAngle) * Math.sin(firstAngle));
/* 397 */       float m21 = (float)Math.sin(secondAngle);
/* 398 */       m22 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle));
/* 399 */       break;
/*     */     case YZX: 
/* 401 */       float m00 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle));
/* 402 */       float m01 = (float)-Math.sin(secondAngle);
/* 403 */       float m02 = (float)(Math.cos(secondAngle) * Math.sin(firstAngle));
/* 404 */       float m10 = (float)(Math.sin(firstAngle) * Math.sin(thirdAngle) + Math.cos(firstAngle) * Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 405 */       float m11 = (float)(Math.cos(secondAngle) * Math.cos(thirdAngle));
/* 406 */       float m12 = (float)(Math.cos(thirdAngle) * Math.sin(firstAngle) * Math.sin(secondAngle) - Math.cos(firstAngle) * Math.sin(thirdAngle));
/* 407 */       float m20 = (float)(Math.cos(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle) - Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 408 */       float m21 = (float)(Math.cos(secondAngle) * Math.sin(thirdAngle));
/* 409 */       m22 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) + Math.sin(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 410 */       break;
/*     */     case ZYX: 
/* 412 */       float m00 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle));
/* 413 */       float m01 = (float)-(Math.cos(secondAngle) * Math.sin(firstAngle));
/* 414 */       float m02 = (float)Math.sin(secondAngle);
/* 415 */       float m10 = (float)(Math.cos(thirdAngle) * Math.sin(firstAngle) + Math.cos(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 416 */       float m11 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) - Math.sin(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 417 */       float m12 = (float)-(Math.cos(secondAngle) * Math.sin(thirdAngle));
/* 418 */       float m20 = (float)(Math.sin(firstAngle) * Math.sin(thirdAngle) - Math.cos(firstAngle) * Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 419 */       float m21 = (float)(Math.cos(firstAngle) * Math.sin(thirdAngle) + Math.cos(thirdAngle) * Math.sin(firstAngle) * Math.sin(secondAngle));
/* 420 */       m22 = (float)(Math.cos(secondAngle) * Math.cos(thirdAngle));
/* 421 */       break;
/*     */     case ZXY: 
/* 423 */       m00 = (float)(Math.cos(firstAngle) * Math.cos(thirdAngle) + Math.sin(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle));
/* 424 */       m01 = (float)(Math.cos(firstAngle) * Math.sin(secondAngle) * Math.sin(thirdAngle) - Math.cos(thirdAngle) * Math.sin(firstAngle));
/* 425 */       m02 = (float)(Math.cos(secondAngle) * Math.sin(thirdAngle));
/* 426 */       m10 = (float)(Math.cos(secondAngle) * Math.sin(firstAngle));
/* 427 */       m11 = (float)(Math.cos(firstAngle) * Math.cos(secondAngle));
/* 428 */       m12 = (float)-Math.sin(secondAngle);
/* 429 */       m20 = (float)(Math.cos(thirdAngle) * Math.sin(firstAngle) * Math.sin(secondAngle) - Math.cos(firstAngle) * Math.sin(thirdAngle));
/* 430 */       m21 = (float)(Math.sin(firstAngle) * Math.sin(thirdAngle) + Math.cos(firstAngle) * Math.cos(thirdAngle) * Math.sin(secondAngle));
/* 431 */       m22 = (float)(Math.cos(secondAngle) * Math.cos(thirdAngle));
/*     */     }
/*     */     
/*     */     
/* 435 */     OpenGLMatrix result = new OpenGLMatrix();
/* 436 */     result.put(0, 0, m00);
/* 437 */     result.put(0, 1, m01);
/* 438 */     result.put(0, 2, m02);
/* 439 */     result.put(1, 0, m10);
/* 440 */     result.put(1, 1, m11);
/* 441 */     result.put(1, 2, m12);
/* 442 */     result.put(2, 0, m20);
/* 443 */     result.put(2, 1, m21);
/* 444 */     result.put(2, 2, m22);
/* 445 */     return result;
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
/*     */   public static Orientation getOrientation(MatrixF rot, AxesReference axesReference, AxesOrder axesOrder, AngleUnit unit)
/*     */   {
/* 467 */     Orientation one = getOrientation(rot, axesReference, axesOrder, unit, AngleSet.THEONE);
/* 468 */     Orientation theOther = getOrientation(rot, axesReference, axesOrder, unit, AngleSet.THEOTHER);
/*     */     
/* 470 */     VectorF vOne = new VectorF(one.firstAngle, one.secondAngle, one.thirdAngle);
/* 471 */     VectorF vOther = new VectorF(theOther.firstAngle, theOther.secondAngle, theOther.thirdAngle);
/*     */     
/* 473 */     return vOne.magnitude() <= vOther.magnitude() ? one : theOther;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum AngleSet
/*     */   {
/* 480 */     THEONE,  THEOTHER;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private AngleSet() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Orientation getOrientation(MatrixF rot, AxesReference axesReference, AxesOrder axesOrder, AngleUnit unit, AngleSet angleSet)
/*     */   {
/* 499 */     if (axesReference == AxesReference.INTRINSIC)
/*     */     {
/* 501 */       return getOrientation(rot, axesReference.reverse(), axesOrder.reverse(), unit, angleSet).toAxesReference(axesReference);
/*     */     }
/*     */     
/*     */     float thirdAngle;
/*     */     
/*     */     float thirdAngle;
/*     */     
/*     */     float thirdAngle;
/*     */     float thirdAngle;
/*     */     float thirdAngle;
/*     */     float thirdAngle;
/*     */     float thirdAngle;
/*     */     float thirdAngle;
/*     */     float thirdAngle;
/*     */     float thirdAngle;
/*     */     float thirdAngle;
/*     */     float secondAngle;
/*     */     float firstAngle;
/*     */     float thirdAngle;
/* 520 */     switch (axesOrder)
/*     */     {
/*     */     case XZX: 
/*     */     default: 
/* 524 */       float test = rot.get(0, 0);
/* 525 */       float thirdAngle; if (test == 1.0F)
/*     */       {
/* 527 */         float secondAngle = 0.0F;
/* 528 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 531 */         thirdAngle = (float)(Math.atan2(rot.get(2, 1), rot.get(1, 1)) - firstAngle);
/*     */       } else { float thirdAngle;
/* 533 */         if (test == -1.0F)
/*     */         {
/* 535 */           float secondAngle = 3.1415927F;
/* 536 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 539 */           thirdAngle = (float)(firstAngle - Math.atan2(rot.get(1, 2), rot.get(2, 2)));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 544 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? Math.acos(rot.get(0, 0)) : -Math.acos(rot.get(0, 0)));
/*     */           
/*     */ 
/* 547 */           float firstAngle = (float)Math.atan2(rot.get(0, 2) / Math.sin(secondAngle), -rot.get(0, 1) / Math.sin(secondAngle));
/*     */           
/*     */ 
/* 550 */           thirdAngle = (float)Math.atan2(rot.get(2, 0) / Math.sin(secondAngle), rot.get(1, 0) / Math.sin(secondAngle));
/*     */         } }
/* 552 */       break;
/*     */     case XYX: 
/* 554 */       float test = rot.get(0, 0);
/* 555 */       float thirdAngle; if (test == 1.0F)
/*     */       {
/* 557 */         float secondAngle = 0.0F;
/* 558 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 561 */         thirdAngle = (float)(Math.atan2(rot.get(2, 1), rot.get(1, 1)) - firstAngle);
/*     */       } else { float thirdAngle;
/* 563 */         if (test == -1.0F)
/*     */         {
/* 565 */           float secondAngle = 3.1415927F;
/* 566 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 569 */           thirdAngle = (float)(firstAngle - Math.atan2(-rot.get(1, 2), rot.get(1, 1)));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 574 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? Math.acos(rot.get(0, 0)) : -Math.acos(rot.get(0, 0)));
/*     */           
/*     */ 
/* 577 */           float firstAngle = (float)Math.atan2(rot.get(0, 1) / Math.sin(secondAngle), rot.get(0, 2) / Math.sin(secondAngle));
/*     */           
/*     */ 
/* 580 */           thirdAngle = (float)Math.atan2(rot.get(1, 0) / Math.sin(secondAngle), -rot.get(2, 0) / Math.sin(secondAngle));
/*     */         } }
/* 582 */       break;
/*     */     case YXY: 
/* 584 */       float test = rot.get(1, 1);
/* 585 */       float thirdAngle; if (test == 1.0F)
/*     */       {
/* 587 */         float secondAngle = 0.0F;
/* 588 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 591 */         thirdAngle = (float)(Math.atan2(rot.get(0, 2), rot.get(0, 0)) - firstAngle);
/*     */       } else { float thirdAngle;
/* 593 */         if (test == -1.0F)
/*     */         {
/* 595 */           float secondAngle = 3.1415927F;
/* 596 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 599 */           thirdAngle = (float)(firstAngle - Math.atan2(rot.get(0, 2), rot.get(0, 0)));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 604 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? Math.acos(rot.get(1, 1)) : -Math.acos(rot.get(1, 1)));
/*     */           
/*     */ 
/* 607 */           float firstAngle = (float)Math.atan2(rot.get(1, 0) / Math.sin(secondAngle), -rot.get(1, 2) / Math.sin(secondAngle));
/*     */           
/*     */ 
/* 610 */           thirdAngle = (float)Math.atan2(rot.get(0, 1) / Math.sin(secondAngle), rot.get(2, 1) / Math.sin(secondAngle));
/*     */         } }
/* 612 */       break;
/*     */     case YZY: 
/* 614 */       float test = rot.get(1, 1);
/* 615 */       float thirdAngle; if (test == 1.0F)
/*     */       {
/* 617 */         float secondAngle = 0.0F;
/* 618 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 621 */         thirdAngle = (float)(Math.atan2(rot.get(0, 2), rot.get(0, 0)) - firstAngle);
/*     */       } else { float thirdAngle;
/* 623 */         if (test == -1.0F)
/*     */         {
/* 625 */           float secondAngle = 3.1415927F;
/* 626 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 629 */           thirdAngle = (float)(firstAngle - Math.atan2(-rot.get(0, 2), rot.get(2, 2)));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 634 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? Math.acos(rot.get(1, 1)) : -Math.acos(rot.get(1, 1)));
/*     */           
/*     */ 
/* 637 */           float firstAngle = (float)Math.atan2(rot.get(1, 2) / Math.sin(secondAngle), rot.get(1, 0) / Math.sin(secondAngle));
/*     */           
/*     */ 
/* 640 */           thirdAngle = (float)Math.atan2(rot.get(2, 1) / Math.sin(secondAngle), -rot.get(0, 1) / Math.sin(secondAngle));
/*     */         } }
/* 642 */       break;
/*     */     case ZYZ: 
/* 644 */       float test = rot.get(2, 2);
/* 645 */       float thirdAngle; if (test == 1.0F)
/*     */       {
/* 647 */         float secondAngle = 0.0F;
/* 648 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 651 */         thirdAngle = (float)(Math.atan2(rot.get(1, 0), rot.get(0, 0)) - firstAngle);
/*     */       } else { float thirdAngle;
/* 653 */         if (test == -1.0F)
/*     */         {
/* 655 */           float secondAngle = 3.1415927F;
/* 656 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 659 */           thirdAngle = (float)(firstAngle - Math.atan2(rot.get(0, 1), rot.get(1, 1)));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 664 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? Math.acos(rot.get(2, 2)) : -Math.acos(rot.get(2, 2)));
/*     */           
/*     */ 
/* 667 */           float firstAngle = (float)Math.atan2(rot.get(2, 1) / Math.sin(secondAngle), -rot.get(2, 0) / Math.sin(secondAngle));
/*     */           
/*     */ 
/* 670 */           thirdAngle = (float)Math.atan2(rot.get(1, 2) / Math.sin(secondAngle), rot.get(0, 2) / Math.sin(secondAngle));
/*     */         } }
/* 672 */       break;
/*     */     case ZXZ: 
/* 674 */       float test = rot.get(2, 2);
/* 675 */       float thirdAngle; if (test == 1.0F)
/*     */       {
/* 677 */         float secondAngle = 0.0F;
/* 678 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 681 */         thirdAngle = (float)(Math.atan2(rot.get(1, 0), rot.get(0, 0)) - firstAngle);
/*     */       } else { float thirdAngle;
/* 683 */         if (test == -1.0F)
/*     */         {
/* 685 */           float secondAngle = 3.1415927F;
/* 686 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 689 */           thirdAngle = (float)(firstAngle - Math.atan2(-rot.get(0, 1), rot.get(0, 0)));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 694 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? Math.acos(rot.get(2, 2)) : -Math.acos(rot.get(2, 2)));
/*     */           
/*     */ 
/* 697 */           float firstAngle = (float)Math.atan2(rot.get(2, 0) / Math.sin(secondAngle), rot.get(2, 1) / Math.sin(secondAngle));
/*     */           
/*     */ 
/* 700 */           thirdAngle = (float)Math.atan2(rot.get(0, 2) / Math.sin(secondAngle), -rot.get(1, 2) / Math.sin(secondAngle));
/*     */         } }
/* 702 */       break;
/*     */     case XZY: 
/* 704 */       float test = rot.get(1, 0);
/* 705 */       float thirdAngle; if (test == 1.0F)
/*     */       {
/* 707 */         float secondAngle = 1.5707964F;
/* 708 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 711 */         thirdAngle = (float)(Math.atan2(rot.get(0, 2), rot.get(2, 2)) - firstAngle);
/*     */       } else { float thirdAngle;
/* 713 */         if (test == -1.0F)
/*     */         {
/* 715 */           float secondAngle = -1.5707964F;
/* 716 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 719 */           thirdAngle = (float)(firstAngle - Math.atan2(rot.get(2, 1), rot.get(0, 1)));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 724 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? Math.asin(rot.get(1, 0)) : 3.141592653589793D - Math.asin(rot.get(1, 0)));
/*     */           
/*     */ 
/* 727 */           float firstAngle = (float)Math.atan2(-rot.get(1, 2) / Math.cos(secondAngle), rot.get(1, 1) / Math.cos(secondAngle));
/*     */           
/*     */ 
/* 730 */           thirdAngle = (float)Math.atan2(-rot.get(2, 0) / Math.cos(secondAngle), rot.get(0, 0) / Math.cos(secondAngle));
/*     */         } }
/* 732 */       break;
/*     */     case XYZ: 
/* 734 */       float test = rot.get(2, 0);
/* 735 */       float thirdAngle; if (test == -1.0F)
/*     */       {
/* 737 */         float secondAngle = 1.5707964F;
/* 738 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 741 */         thirdAngle = (float)(firstAngle - Math.atan2(rot.get(0, 1), rot.get(0, 2)));
/*     */       } else { float thirdAngle;
/* 743 */         if (test == 1.0F)
/*     */         {
/* 745 */           float secondAngle = -1.5707964F;
/* 746 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 749 */           thirdAngle = (float)(Math.atan2(-rot.get(0, 1), rot.get(1, 1)) - firstAngle);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 754 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? -Math.asin(rot.get(2, 0)) : 3.141592653589793D + Math.asin(rot.get(2, 0)));
/*     */           
/*     */ 
/* 757 */           float firstAngle = (float)Math.atan2(rot.get(2, 1) / Math.cos(secondAngle), rot.get(2, 2) / Math.cos(secondAngle));
/*     */           
/*     */ 
/* 760 */           thirdAngle = (float)Math.atan2(rot.get(1, 0) / Math.cos(secondAngle), rot.get(0, 0) / Math.cos(secondAngle));
/*     */         } }
/* 762 */       break;
/*     */     case YXZ: 
/* 764 */       float test = rot.get(2, 1);
/* 765 */       float thirdAngle; if (test == 1.0F)
/*     */       {
/* 767 */         float secondAngle = 1.5707964F;
/* 768 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 771 */         thirdAngle = (float)(Math.atan2(rot.get(0, 2), rot.get(0, 0)) - firstAngle);
/*     */       } else { float thirdAngle;
/* 773 */         if (test == -1.0F)
/*     */         {
/* 775 */           float secondAngle = -1.5707964F;
/* 776 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 779 */           thirdAngle = (float)(firstAngle - Math.atan2(rot.get(0, 2), rot.get(0, 0)));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 784 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? Math.asin(rot.get(2, 1)) : 3.141592653589793D - Math.asin(rot.get(2, 1)));
/*     */           
/*     */ 
/* 787 */           float firstAngle = (float)Math.atan2(-rot.get(2, 0) / Math.cos(secondAngle), rot.get(2, 2) / Math.cos(secondAngle));
/*     */           
/*     */ 
/* 790 */           thirdAngle = (float)Math.atan2(-rot.get(0, 1) / Math.cos(secondAngle), rot.get(1, 1) / Math.cos(secondAngle));
/*     */         } }
/* 792 */       break;
/*     */     case YZX: 
/* 794 */       float test = rot.get(0, 1);
/* 795 */       float thirdAngle; if (test == -1.0F)
/*     */       {
/* 797 */         float secondAngle = 1.5707964F;
/* 798 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 801 */         thirdAngle = (float)(firstAngle - Math.atan2(rot.get(1, 2), rot.get(1, 0)));
/*     */       } else { float thirdAngle;
/* 803 */         if (test == 1.0F)
/*     */         {
/* 805 */           float secondAngle = -1.5707964F;
/* 806 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 809 */           thirdAngle = (float)(Math.atan2(-rot.get(1, 2), rot.get(2, 2)) - firstAngle);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 814 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? -Math.asin(rot.get(0, 1)) : 3.141592653589793D + Math.asin(rot.get(0, 1)));
/*     */           
/*     */ 
/* 817 */           float firstAngle = (float)Math.atan2(rot.get(0, 2) / Math.cos(secondAngle), rot.get(0, 0) / Math.cos(secondAngle));
/*     */           
/*     */ 
/* 820 */           thirdAngle = (float)Math.atan2(rot.get(2, 1) / Math.cos(secondAngle), rot.get(1, 1) / Math.cos(secondAngle));
/*     */         } }
/* 822 */       break;
/*     */     case ZYX: 
/* 824 */       float test = rot.get(0, 2);
/* 825 */       float thirdAngle; if (test == 1.0F)
/*     */       {
/* 827 */         float secondAngle = 1.5707964F;
/* 828 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 831 */         thirdAngle = (float)(Math.atan2(rot.get(1, 0), rot.get(1, 1)) - firstAngle);
/*     */       } else { float thirdAngle;
/* 833 */         if (test == -1.0F)
/*     */         {
/* 835 */           float secondAngle = -1.5707964F;
/* 836 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 839 */           thirdAngle = (float)(firstAngle - Math.atan2(rot.get(1, 0), rot.get(1, 1)));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 844 */           float secondAngle = (float)(angleSet == AngleSet.THEONE ? Math.asin(rot.get(0, 2)) : 3.141592653589793D - Math.asin(rot.get(0, 2)));
/*     */           
/*     */ 
/* 847 */           float firstAngle = (float)Math.atan2(-rot.get(0, 1) / Math.cos(secondAngle), rot.get(0, 0) / Math.cos(secondAngle));
/*     */           
/*     */ 
/* 850 */           thirdAngle = (float)Math.atan2(-rot.get(1, 2) / Math.cos(secondAngle), rot.get(2, 2) / Math.cos(secondAngle));
/*     */         } }
/* 852 */       break;
/*     */     case ZXY: 
/* 854 */       float test = rot.get(1, 2);
/* 855 */       float thirdAngle; if (test == -1.0F)
/*     */       {
/* 857 */         float secondAngle = 1.5707964F;
/* 858 */         float firstAngle = 0.0F;
/*     */         
/*     */ 
/* 861 */         thirdAngle = (float)(firstAngle - Math.atan2(rot.get(2, 0), rot.get(0, 0)));
/*     */       } else { float thirdAngle;
/* 863 */         if (test == 1.0F)
/*     */         {
/* 865 */           float secondAngle = -1.5707964F;
/* 866 */           float firstAngle = 0.0F;
/*     */           
/*     */ 
/* 869 */           thirdAngle = (float)(Math.atan2(-rot.get(0, 1), rot.get(0, 0)) - firstAngle);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 874 */           secondAngle = (float)(angleSet == AngleSet.THEONE ? -Math.asin(rot.get(1, 2)) : 3.141592653589793D + Math.asin(rot.get(1, 2)));
/*     */           
/*     */ 
/* 877 */           firstAngle = (float)Math.atan2(rot.get(1, 0) / Math.cos(secondAngle), rot.get(1, 1) / Math.cos(secondAngle));
/*     */           
/*     */ 
/* 880 */           thirdAngle = (float)Math.atan2(rot.get(0, 2) / Math.cos(secondAngle), rot.get(2, 2) / Math.cos(secondAngle));
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/* 885 */     return new Orientation(axesReference, axesOrder, unit, unit.fromRadians(firstAngle), unit.fromRadians(secondAngle), unit.fromRadians(thirdAngle), 0L);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\Orientation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */