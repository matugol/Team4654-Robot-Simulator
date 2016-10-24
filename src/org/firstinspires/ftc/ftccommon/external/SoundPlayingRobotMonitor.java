/*     */ package org.firstinspires.ftc.ftccommon.external;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.support.annotation.RawRes;
/*     */ import com.qualcomm.ftccommon.R.raw;
/*     */ import com.qualcomm.ftccommon.SoundPlayer;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ import com.qualcomm.robotcore.robot.RobotStatus;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.NetworkStatus;
/*     */ import org.firstinspires.ftc.robotcore.internal.network.PeerStatus;
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
/*     */ public class SoundPlayingRobotMonitor
/*     */   implements RobotStateMonitor
/*     */ {
/*     */   protected static final boolean DEBUG = false;
/*     */   protected Context context;
/*  62 */   protected RobotState robotState = RobotState.UNKNOWN;
/*  63 */   protected RobotStatus robotStatus = RobotStatus.UNKNOWN;
/*  64 */   protected NetworkStatus networkStatus = NetworkStatus.UNKNOWN;
/*  65 */   protected PeerStatus peerStatus = PeerStatus.UNKNOWN;
/*  66 */   protected String errorMessage = null;
/*  67 */   protected String warningMessage = null;
/*     */   
/*     */ 
/*     */   @RawRes
/*  71 */   public int soundConnect = R.raw.chimeconnect; @RawRes
/*  72 */   public int soundDisconnect = R.raw.chimedisconnect; @RawRes
/*  73 */   public int soundRunning = R.raw.nxtstartupsound; @RawRes
/*  74 */   public int soundWarning = R.raw.warningmessage; @RawRes
/*  75 */   public int soundError = R.raw.errormessage;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SoundPlayingRobotMonitor()
/*     */   {
/*  83 */     this(AppUtil.getInstance().getApplication());
/*     */   }
/*     */   
/*     */   public SoundPlayingRobotMonitor(Context context) {
/*  87 */     this.context = context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void updateRobotState(@NonNull RobotState robotState)
/*     */   {
/*  96 */     if (robotState != this.robotState)
/*     */     {
/*     */ 
/*  99 */       switch (robotState) {
/*     */       case NOT_STARTED: 
/*     */         break;
/*     */       case INIT: 
/*     */         break;
/*     */       case STOPPED: 
/*     */         break;
/*     */       case EMERGENCY_STOP: 
/*     */         break;
/*     */       default:  break; case RUNNING:  this.errorMessage = null;
/* 109 */         this.warningMessage = null;
/* 110 */         playSound(this.soundRunning);
/*     */       }
/*     */       
/*     */     }
/* 114 */     this.robotState = robotState;
/*     */   }
/*     */   
/*     */   public synchronized void updateRobotStatus(@NonNull RobotStatus robotStatus)
/*     */   {
/* 119 */     if (robotStatus != this.robotStatus)
/*     */     {
/*     */ 
/* 122 */       switch (robotStatus) {
/*     */       case NONE: 
/*     */         break;
/*     */       case SCANNING_USB: 
/*     */         break;
/*     */       case WAITING_ON_NETWORK: 
/*     */         break;
/*     */       case NETWORK_TIMED_OUT: 
/*     */         break;
/*     */       case STARTING_ROBOT: 
/*     */         break;
/*     */       case FAILED_TO_START_ROBOT:  break; case UNABLE_TO_CREATE_ROBOT:  break; } }
/* 134 */     this.robotStatus = robotStatus;
/*     */   }
/*     */   
/*     */   public void updatePeerStatus(@NonNull PeerStatus peerStatus)
/*     */   {
/* 139 */     if (peerStatus != this.peerStatus)
/*     */     {
/*     */ 
/* 142 */       switch (peerStatus) {
/*     */       case UNKNOWN: 
/*     */         break;
/* 145 */       case CONNECTED:  if (this.peerStatus != PeerStatus.CONNECTED) playSound(this.soundConnect);
/*     */         break; case DISCONNECTED:  if (this.peerStatus != PeerStatus.DISCONNECTED) playSound(this.soundDisconnect);
/*     */         break;
/*     */       }
/*     */     }
/* 150 */     this.peerStatus = peerStatus;
/*     */   }
/*     */   
/*     */   public synchronized void updateNetworkStatus(@NonNull NetworkStatus networkStatus, @Nullable String extra)
/*     */   {
/* 155 */     if (networkStatus != this.networkStatus)
/*     */     {
/*     */ 
/* 158 */       switch (networkStatus) {
/*     */       case UNKNOWN: 
/*     */         break;
/*     */       case ACTIVE: 
/*     */         break;
/*     */       case INACTIVE: 
/*     */         break;
/*     */       case ENABLED: 
/*     */         break;
/*     */       case ERROR: 
/*     */         break; case CREATED_AP_CONNECTION:  break; } }
/* 169 */     this.networkStatus = networkStatus;
/*     */   }
/*     */   
/*     */   public synchronized void updateErrorMessage(@Nullable String errorMessage)
/*     */   {
/* 174 */     if ((errorMessage != null) && (!errorMessage.equals(this.errorMessage)))
/*     */     {
/*     */ 
/* 177 */       playSound(this.soundError);
/*     */     }
/* 179 */     this.errorMessage = errorMessage;
/*     */   }
/*     */   
/*     */   public synchronized void updateWarningMessage(@Nullable String warningMessage)
/*     */   {
/* 184 */     if ((warningMessage != null) && (!warningMessage.equals(this.warningMessage)))
/*     */     {
/*     */ 
/* 187 */       playSound(this.soundWarning);
/*     */     }
/* 189 */     this.warningMessage = warningMessage;
/*     */   }
/*     */   
/*     */   protected void playSound(@RawRes int resourceId)
/*     */   {
/* 194 */     SoundPlayer.getInstance().play(this.context, resourceId);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\org\firstinspires\ftc\ftccommon\external\SoundPlayingRobotMonitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */