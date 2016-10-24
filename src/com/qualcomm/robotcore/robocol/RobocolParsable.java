/*    */ package com.qualcomm.robotcore.robocol;
/*    */ 
/*    */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
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
/*    */ public abstract interface RobocolParsable
/*    */ {
/*    */   public static final int HEADER_LENGTH = 5;
/*    */   
/*    */   public abstract MsgType getRobocolMsgType();
/*    */   
/*    */   public abstract int getSequenceNumber();
/*    */   
/*    */   public abstract void setSequenceNumber();
/*    */   
/*    */   public abstract boolean shouldTransmit(long paramLong);
/*    */   
/*    */   public abstract byte[] toByteArrayForTransmission()
/*    */     throws RobotCoreException;
/*    */   
/*    */   public abstract byte[] toByteArray()
/*    */     throws RobotCoreException;
/*    */   
/*    */   public abstract void fromByteArray(byte[] paramArrayOfByte)
/*    */     throws RobotCoreException;
/*    */   
/*    */   public static enum MsgType
/*    */   {
/* 65 */     EMPTY(0), 
/* 66 */     HEARTBEAT(1), 
/* 67 */     GAMEPAD(2), 
/* 68 */     PEER_DISCOVERY(3), 
/* 69 */     COMMAND(4), 
/* 70 */     TELEMETRY(5);
/*    */     
/* 72 */     private static final MsgType[] VALUES_CACHE = values();
/*    */     
/*    */ 
/*    */     private final int type;
/*    */     
/*    */ 
/*    */ 
/*    */     public static MsgType fromByte(byte b)
/*    */     {
/* 81 */       MsgType t = EMPTY;
/*    */       try {
/* 83 */         t = VALUES_CACHE[b];
/*    */       } catch (ArrayIndexOutOfBoundsException e) {
/* 85 */         RobotLog.w(String.format("Cannot convert %d to MsgType: %s", new Object[] { Byte.valueOf(b), e.toString() }));
/*    */       }
/* 87 */       return t;
/*    */     }
/*    */     
/*    */     private MsgType(int type) {
/* 91 */       this.type = type;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public byte asByte()
/*    */     {
/* 99 */       return (byte)this.type;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\RobocolParsable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */