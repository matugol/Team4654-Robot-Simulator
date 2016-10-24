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
/*    */ public enum AxesReference
/*    */ {
/* 46 */   EXTRINSIC,  INTRINSIC;
/*    */   
/*    */   private AxesReference() {}
/*    */   
/* 50 */   public AxesReference reverse() { switch (this) {
/*    */     case EXTRINSIC: 
/*    */     default: 
/* 53 */       return INTRINSIC; }
/* 54 */     return EXTRINSIC;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\external\navigation\AxesReference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */