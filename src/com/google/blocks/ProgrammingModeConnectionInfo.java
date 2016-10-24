/*    */ package com.google.blocks;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProgrammingModeConnectionInfo
/*    */ {
/*    */   final String networkName;
/*    */   final String passphrase;
/*    */   final String serverUrl;
/*    */   final boolean serverIsAlive;
/*    */   final long timeServerStartedMillis;
/*    */   
/*    */   public ProgrammingModeConnectionInfo(String networkName, String passphrase, String serverUrl, boolean serverIsAlive, long timeServerStartedMillis)
/*    */   {
/* 22 */     this.networkName = networkName;
/* 23 */     this.passphrase = passphrase;
/* 24 */     this.serverUrl = serverUrl;
/* 25 */     this.serverIsAlive = serverIsAlive;
/* 26 */     this.timeServerStartedMillis = timeServerStartedMillis;
/*    */   }
/*    */   
/*    */   public String toJson() {
/* 30 */     return new Gson().toJson(this);
/*    */   }
/*    */   
/*    */   public static ProgrammingModeConnectionInfo fromJson(String json) {
/* 34 */     return (ProgrammingModeConnectionInfo)new Gson().fromJson(json, ProgrammingModeConnectionInfo.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\ProgrammingModeConnectionInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */