/*     */ package com.qualcomm.robotcore.robocol;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Comparator;
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
/*     */ public class Command
/*     */   extends RobocolParsableBase
/*     */   implements Comparable<Command>, Comparator<Command>
/*     */ {
/*     */   private static final short BASE_PAYLOAD_SIZE = 13;
/*  54 */   private static final Charset CHARSET = Charset.forName("UTF-8");
/*     */   
/*     */   String mName;
/*     */   String mExtra;
/*     */   byte[] mNameBytes;
/*     */   byte[] mExtraBytes;
/*     */   long mTimestamp;
/*  61 */   boolean mAcknowledged = false;
/*  62 */   byte mAttempts = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Command(String name)
/*     */   {
/*  69 */     this(name, "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Command(String name, String extra)
/*     */   {
/*  78 */     this.mName = name;
/*  79 */     this.mExtra = extra;
/*  80 */     this.mNameBytes = TypeConversion.stringToUtf8(this.mName);
/*  81 */     this.mExtraBytes = TypeConversion.stringToUtf8(this.mExtra);
/*  82 */     this.mTimestamp = generateTimestamp();
/*     */     
/*     */ 
/*     */ 
/*  86 */     int cbPayload = getPayloadSize();
/*  87 */     if (cbPayload > 32767)
/*  88 */       throw new IllegalArgumentException(String.format("command payload is too large: %d", new Object[] { Integer.valueOf(cbPayload) }));
/*     */   }
/*     */   
/*     */   public Command(byte[] byteArray) throws RobotCoreException {
/*  92 */     fromByteArray(byteArray);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void acknowledge()
/*     */   {
/*  99 */     this.mAcknowledged = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAcknowledged()
/*     */   {
/* 107 */     return this.mAcknowledged;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 115 */     return this.mName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getExtra()
/*     */   {
/* 122 */     return this.mExtra;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getAttempts()
/*     */   {
/* 132 */     return this.mAttempts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getTimestamp()
/*     */   {
/* 140 */     return this.mTimestamp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RobocolParsable.MsgType getRobocolMsgType()
/*     */   {
/* 149 */     return RobocolParsable.MsgType.COMMAND;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] toByteArray()
/*     */     throws RobotCoreException
/*     */   {
/* 159 */     if (this.mAttempts != Byte.MAX_VALUE) { this.mAttempts = ((byte)(this.mAttempts + 1));
/*     */     }
/* 161 */     short payloadSize = (short)getPayloadSize();
/*     */     
/* 163 */     ByteBuffer buffer = getWriteBuffer(payloadSize);
/*     */     try {
/* 165 */       buffer.putLong(this.mTimestamp);
/*     */       
/* 167 */       buffer.put((byte)(this.mAcknowledged ? 1 : 0));
/*     */       
/* 169 */       buffer.putShort((short)this.mNameBytes.length);
/* 170 */       buffer.put(this.mNameBytes);
/* 171 */       buffer.putShort((short)this.mExtraBytes.length);
/* 172 */       buffer.put(this.mExtraBytes);
/*     */     } catch (BufferOverflowException e) {
/* 174 */       RobotLog.logStacktrace(e);
/*     */     }
/* 176 */     return buffer.array();
/*     */   }
/*     */   
/*     */   int getPayloadSize() {
/* 180 */     return 13 + this.mNameBytes.length + this.mExtraBytes.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fromByteArray(byte[] byteArray)
/*     */     throws RobotCoreException
/*     */   {
/* 189 */     ByteBuffer buffer = getReadBuffer(byteArray);
/*     */     
/* 191 */     this.mTimestamp = buffer.getLong();
/*     */     
/* 193 */     this.mAcknowledged = (buffer.get() != 0);
/*     */     
/* 195 */     int length = TypeConversion.unsignedShortToInt(buffer.getShort());
/* 196 */     this.mNameBytes = new byte[length];
/* 197 */     buffer.get(this.mNameBytes);
/* 198 */     this.mName = TypeConversion.utf8ToString(this.mNameBytes);
/*     */     
/* 200 */     length = TypeConversion.unsignedShortToInt(buffer.getShort());
/* 201 */     this.mExtraBytes = new byte[length];
/* 202 */     buffer.get(this.mExtraBytes);
/* 203 */     this.mExtra = TypeConversion.utf8ToString(this.mExtraBytes);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 208 */     return String.format("command: %20d %5s %s", new Object[] { Long.valueOf(this.mTimestamp), Boolean.valueOf(this.mAcknowledged), this.mName });
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 213 */     if ((o instanceof Command)) {
/* 214 */       Command c = (Command)o;
/* 215 */       if ((this.mName.equals(c.mName)) && (this.mTimestamp == c.mTimestamp)) { return true;
/*     */       }
/*     */     }
/* 218 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 223 */     return this.mName.hashCode() ^ (int)this.mTimestamp;
/*     */   }
/*     */   
/*     */   public int compareTo(Command another)
/*     */   {
/* 228 */     int diff = this.mName.compareTo(another.mName);
/*     */     
/* 230 */     if (diff != 0) { return diff;
/*     */     }
/* 232 */     if (this.mTimestamp < another.mTimestamp) return -1;
/* 233 */     if (this.mTimestamp > another.mTimestamp) { return 1;
/*     */     }
/* 235 */     return 0;
/*     */   }
/*     */   
/*     */   public int compare(Command c1, Command c2)
/*     */   {
/* 240 */     return c1.compareTo(c2);
/*     */   }
/*     */   
/*     */   public static long generateTimestamp() {
/* 244 */     return System.nanoTime();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\Command.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */