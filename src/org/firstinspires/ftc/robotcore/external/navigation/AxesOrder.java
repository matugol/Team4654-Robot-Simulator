/*    */ package org.firstinspires.ftc.robotcore.external.navigation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum AxesOrder
/*    */ {
/* 46 */   XZX(new int[] { 0, 2, 0 }),  XYX(new int[] { 0, 1, 0 }),  YXY(new int[] { 1, 0, 1 }), 
/* 47 */   YZY(new int[] { 1, 2, 1 }),  ZYZ(new int[] { 2, 1, 2 }),  ZXZ(new int[] { 2, 0, 2 }), 
/* 48 */   XZY(new int[] { 0, 2, 1 }),  XYZ(new int[] { 0, 1, 2 }),  YXZ(new int[] { 1, 0, 2 }), 
/* 49 */   YZX(new int[] { 1, 2, 0 }),  ZYX(new int[] { 2, 1, 0 }),  ZXY(new int[] { 2, 0, 1 });
/*    */   
/*    */   private final int[] indices;
/*    */   
/*    */   private AxesOrder(int[] indices)
/*    */   {
/* 55 */     this.indices = indices;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] indices()
/*    */   {
/* 64 */     return this.indices;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AxesOrder reverse()
/*    */   {
/* 73 */     switch (this) {
/*    */     case XZX: 
/*    */     default: 
/* 76 */       return XZX;
/* 77 */     case XYX:  return XYX;
/* 78 */     case YXY:  return YXY;
/* 79 */     case YZY:  return YZY;
/* 80 */     case ZYZ:  return ZYZ;
/* 81 */     case ZXZ:  return ZXZ;
/* 82 */     case XZY:  return YZX;
/* 83 */     case XYZ:  return ZYX;
/* 84 */     case YXZ:  return ZXY;
/* 85 */     case YZX:  return XZY;
/* 86 */     case ZYX:  return XYZ; }
/* 87 */     return YXZ;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\AxesOrder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */