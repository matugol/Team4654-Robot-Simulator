/*     */ package com.qualcomm.robotcore.robocol;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.robotcore.R.string;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
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
/*     */ public class PeerDiscovery
/*     */   extends RobocolParsableBase
/*     */ {
/*     */   public static final String TAG = "PeerDiscovery";
/*     */   public static final byte ROBOCOL_VERSION = 11;
/*     */   private PeerType peerType;
/*     */   static final int cbBufferHistorical = 13;
/*     */   static final int cbPayloadHistorical = 10;
/*     */   
/*     */   public static enum PeerType
/*     */   {
/*  61 */     NOT_SET(0), 
/*  62 */     PEER(1), 
/*  63 */     GROUP_OWNER(2);
/*     */     
/*  65 */     private static final PeerType[] VALUES_CACHE = values();
/*     */     
/*     */ 
/*     */     private int type;
/*     */     
/*     */ 
/*     */ 
/*     */     public static PeerType fromByte(byte b)
/*     */     {
/*  74 */       PeerType p = NOT_SET;
/*     */       try {
/*  76 */         p = VALUES_CACHE[b];
/*     */       } catch (ArrayIndexOutOfBoundsException e) {
/*  78 */         RobotLog.w(String.format("Cannot convert %d to Peer: %s", new Object[] { Byte.valueOf(b), e.toString() }));
/*     */       }
/*  80 */       return p;
/*     */     }
/*     */     
/*     */     private PeerType(int type) {
/*  84 */       this.type = type;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public byte asByte()
/*     */     {
/*  92 */       return (byte)this.type;
/*     */     }
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
/*     */   public static PeerDiscovery forReceive()
/*     */   {
/* 114 */     return new PeerDiscovery(PeerType.NOT_SET);
/*     */   }
/*     */   
/*     */   public PeerDiscovery(PeerType peerType) {
/* 118 */     this.peerType = peerType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeerType getPeerType()
/*     */   {
/* 129 */     return this.peerType;
/*     */   }
/*     */   
/*     */   public RobocolParsable.MsgType getRobocolMsgType()
/*     */   {
/* 134 */     return RobocolParsable.MsgType.PEER_DISCOVERY;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] toByteArray()
/*     */     throws RobotCoreException
/*     */   {
/* 163 */     ByteBuffer buffer = allocateWholeWriteBuffer(13);
/*     */     try
/*     */     {
/* 166 */       buffer.put(getRobocolMsgType().asByte());
/* 167 */       buffer.putShort((short)10);
/* 168 */       buffer.put((byte)11);
/* 169 */       buffer.put(this.peerType.asByte());
/* 170 */       buffer.putShort((short)this.sequenceNumber);
/*     */     }
/*     */     catch (BufferOverflowException e) {
/* 173 */       RobotLog.logStacktrace(e);
/*     */     }
/* 175 */     return buffer.array();
/*     */   }
/*     */   
/*     */   public void fromByteArray(byte[] byteArray) throws RobotCoreException
/*     */   {
/* 180 */     if (byteArray.length < 13) {
/* 181 */       throw new RobotCoreException("Expected buffer of at least %d bytes, received %d", new Object[] { Integer.valueOf(13), Integer.valueOf(byteArray.length) });
/*     */     }
/*     */     
/* 184 */     ByteBuffer byteBuffer = getWholeReadBuffer(byteArray);
/*     */     
/* 186 */     byte peerMessageType = byteBuffer.get();
/* 187 */     short peerCbPayload = byteBuffer.getShort();
/* 188 */     byte peerRobocolVersion = byteBuffer.get();
/* 189 */     byte peerType = byteBuffer.get();
/* 190 */     short peerSeqNum = byteBuffer.getShort();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 195 */     PeerApp thisApp = AppUtil.getInstance().getThisApp();
/* 196 */     if (peerRobocolVersion != 11) {
/* 197 */       throw new RobotCoreException(thisApp.getContext().getString(R.string.incompatibleAppsError), new Object[] { thisApp.getContext().getString(thisApp.getIdThisApp()), Byte.valueOf(11), thisApp.getContext().getString(thisApp.getIdRemoteApp()), Byte.valueOf(peerRobocolVersion) });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 203 */     this.peerType = PeerType.fromByte(peerType);
/*     */     
/*     */ 
/* 206 */     if (peerRobocolVersion > 1) {
/* 207 */       setSequenceNumber(peerSeqNum);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 213 */     return String.format("Peer Discovery - peer type: %s", new Object[] { this.peerType.name() });
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\PeerDiscovery.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */