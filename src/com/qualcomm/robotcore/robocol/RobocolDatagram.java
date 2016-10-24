/*     */ package com.qualcomm.robotcore.robocol;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
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
/*     */ public class RobocolDatagram
/*     */ {
/*     */   public static final String TAG = "Robocol";
/*     */   private DatagramPacket packet;
/*  27 */   private byte[] receiveBuffer = null;
/*     */   
/*     */ 
/*  30 */   static Queue<byte[]> receiveBuffers = new ConcurrentLinkedQueue();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RobocolDatagram(RobocolParsable message)
/*     */     throws RobotCoreException
/*     */   {
/*  41 */     setData(message.toByteArrayForTransmission());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RobocolDatagram(byte[] message)
/*     */   {
/*  49 */     setData(message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RobocolDatagram forReceive()
/*     */   {
/*  60 */     byte[] buffer = (byte[])receiveBuffers.poll();
/*  61 */     if (buffer == null) {
/*  62 */       buffer = new byte['ဂ'];
/*     */     }
/*  64 */     DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
/*     */     
/*  66 */     RobocolDatagram result = new RobocolDatagram();
/*  67 */     result.packet = packet;
/*  68 */     result.receiveBuffer = buffer;
/*  69 */     return result;
/*     */   }
/*     */   
/*     */   protected RobocolDatagram() {
/*  73 */     this.packet = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/*  82 */     if (this.receiveBuffer != null) {
/*  83 */       receiveBuffers.add(this.receiveBuffer);
/*  84 */       this.receiveBuffer = null;
/*     */     }
/*  86 */     this.packet = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RobocolParsable.MsgType getMsgType()
/*     */   {
/*  98 */     return RobocolParsable.MsgType.fromByte(this.packet.getData()[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 106 */     return this.packet.getLength();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPayloadLength()
/*     */   {
/* 114 */     return this.packet.getLength() - 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getData()
/*     */   {
/* 122 */     return this.packet.getData();
/*     */   }
/*     */   
/*     */   public void setData(byte[] data) {
/* 126 */     this.packet = new DatagramPacket(data, data.length);
/*     */   }
/*     */   
/*     */   public InetAddress getAddress() {
/* 130 */     return this.packet.getAddress();
/*     */   }
/*     */   
/*     */   public void setAddress(InetAddress address) {
/* 134 */     this.packet.setAddress(address);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 138 */     int size = 0;
/* 139 */     String type = "NONE";
/* 140 */     String addr = null;
/*     */     
/* 142 */     if ((this.packet != null) && (this.packet.getAddress() != null) && (this.packet.getLength() > 0)) {
/* 143 */       type = RobocolParsable.MsgType.fromByte(this.packet.getData()[0]).name();
/* 144 */       size = this.packet.getLength();
/* 145 */       addr = this.packet.getAddress().getHostAddress();
/*     */     }
/*     */     
/* 148 */     return String.format("RobocolDatagram - type:%s, addr:%s, size:%d", new Object[] { type, addr, Integer.valueOf(size) });
/*     */   }
/*     */   
/*     */   protected DatagramPacket getPacket() {
/* 152 */     return this.packet;
/*     */   }
/*     */   
/*     */   protected void setPacket(DatagramPacket packet) {
/* 156 */     this.packet = packet;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\RobocolDatagram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */