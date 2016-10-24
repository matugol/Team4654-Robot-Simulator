/*    */ package org.firstinspires.inspection;
/*    */ 
/*    */ 
/*    */ public class DsInspectionActivity
/*    */   extends InspectionActivity
/*    */ {
/*    */   public static final String dsLaunchIntent = "org.firstinspires.inspection.DsInspectionActivity.intent.action.Launch";
/*    */   
/*    */ 
/*    */   protected boolean channelChangerRequired()
/*    */   {
/* 12 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Boolean validateAppsInstalled()
/*    */   {
/* 21 */     if ((packageExists(this.rcApp).booleanValue()) || (appInventorExists().booleanValue())) {
/* 22 */       return Boolean.valueOf(false);
/*    */     }
/*    */     
/*    */ 
/* 26 */     return packageExists(this.dsApp);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Inspection-release.jar!\classes.jar!\org\firstinspires\inspection\DsInspectionActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */