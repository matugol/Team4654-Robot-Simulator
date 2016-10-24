/*    */ package org.firstinspires.inspection;
/*    */ 
/*    */ 
/*    */ public class RcInspectionActivity
/*    */   extends InspectionActivity
/*    */ {
/*    */   public static final String rcLaunchIntent = "org.firstinspires.inspection.RcInspectionActivity.intent.action.Launch";
/*    */   
/*    */ 
/*    */   protected boolean channelChangerRequired()
/*    */   {
/* 12 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected Boolean validateAppsInstalled()
/*    */   {
/* 19 */     if (!packageExists(this.ccApp).booleanValue()) {
/* 20 */       return Boolean.valueOf(false);
/*    */     }
/*    */     
/* 23 */     if (!packageExists(this.dsApp).booleanValue()) {
/* 24 */       return Boolean.valueOf(false);
/*    */     }
/*    */     
/*    */ 
/* 28 */     return Boolean.valueOf((packageExists(this.rcApp).booleanValue()) || (appInventorExists().booleanValue()));
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Inspection-release.jar!\classes.jar!\org\firstinspires\inspection\RcInspectionActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */