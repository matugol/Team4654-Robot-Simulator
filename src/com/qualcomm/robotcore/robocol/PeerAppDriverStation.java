/*    */ package com.qualcomm.robotcore.robocol;
/*    */ 
/*    */ import android.content.Context;
/*    */ import com.qualcomm.robotcore.R.string;
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
/*    */ public class PeerAppDriverStation
/*    */   implements PeerApp
/*    */ {
/*    */   Context context;
/*    */   
/*    */   public PeerAppDriverStation(Context context)
/*    */   {
/* 48 */     this.context = context;
/*    */   }
/*    */   
/*    */   public Context getContext()
/*    */   {
/* 53 */     return this.context;
/*    */   }
/*    */   
/*    */   public int getIdThisApp()
/*    */   {
/* 58 */     return R.string.idAppDriverStation;
/*    */   }
/*    */   
/*    */   public int getIdRemoteApp()
/*    */   {
/* 63 */     return R.string.idAppRobotController;
/*    */   }
/*    */   
/*    */   public boolean isRobotController()
/*    */   {
/* 68 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isDriverStation()
/*    */   {
/* 73 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\PeerAppDriverStation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */