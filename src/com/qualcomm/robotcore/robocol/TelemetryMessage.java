/*     */ package com.qualcomm.robotcore.robocol;
/*     */ 
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ public class TelemetryMessage
/*     */   extends RobocolParsableBase
/*     */ {
/*     */   public static final String DEFAULT_TAG = "TELEMETRY_DATA";
/*  50 */   private static final Charset CHARSET = Charset.forName("UTF-8");
/*     */   
/*  52 */   private final Map<String, String> dataStrings = new LinkedHashMap();
/*  53 */   private final Map<String, Float> dataNumbers = new LinkedHashMap();
/*  54 */   private String tag = "";
/*  55 */   private long timestamp = 0L;
/*  56 */   private boolean isSorted = true;
/*  57 */   private RobotState robotState = RobotState.UNKNOWN;
/*     */   static final int cbTimestamp = 8;
/*     */   static final int cbSorted = 1;
/*     */   static final int cbRobotState = 1;
/*     */   
/*     */   public TelemetryMessage() {}
/*     */   
/*  64 */   public TelemetryMessage(byte[] byteArray) throws RobotCoreException { fromByteArray(byteArray); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized long getTimestamp()
/*     */   {
/*  72 */     return this.timestamp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSorted()
/*     */   {
/*  82 */     return this.isSorted;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSorted(boolean isSorted)
/*     */   {
/*  91 */     this.isSorted = isSorted;
/*     */   }
/*     */   
/*     */   public RobotState getRobotState() {
/*  95 */     return this.robotState;
/*     */   }
/*     */   
/*     */   public void setRobotState(RobotState robotState) {
/*  99 */     this.robotState = robotState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setTag(String tag)
/*     */   {
/* 111 */     this.tag = tag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized String getTag()
/*     */   {
/* 119 */     if (this.tag.length() == 0) { return "TELEMETRY_DATA";
/*     */     }
/* 121 */     return this.tag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void addData(String key, String msg)
/*     */   {
/* 132 */     this.dataStrings.put(key, msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void addData(String key, Object msg)
/*     */   {
/* 143 */     this.dataStrings.put(key, msg.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void addData(String key, float msg)
/*     */   {
/* 154 */     this.dataNumbers.put(key, Float.valueOf(msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void addData(String key, double msg)
/*     */   {
/* 165 */     this.dataNumbers.put(key, Float.valueOf((float)msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Map<String, String> getDataStrings()
/*     */   {
/* 173 */     return this.dataStrings;
/*     */   }
/*     */   
/*     */   public synchronized Map<String, Float> getDataNumbers() {
/* 177 */     return this.dataNumbers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean hasData()
/*     */   {
/* 185 */     return (!this.dataStrings.isEmpty()) || (!this.dataNumbers.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void clearData()
/*     */   {
/* 194 */     this.timestamp = 0L;
/* 195 */     this.dataStrings.clear();
/* 196 */     this.dataNumbers.clear();
/*     */   }
/*     */   
/*     */   public RobocolParsable.MsgType getRobocolMsgType()
/*     */   {
/* 201 */     return RobocolParsable.MsgType.TELEMETRY;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized byte[] toByteArray()
/*     */     throws RobotCoreException
/*     */   {
/* 208 */     this.timestamp = System.currentTimeMillis();
/*     */     
/* 210 */     if (this.dataStrings.size() > 255) {
/* 211 */       throw new RobotCoreException("Cannot have more than %d string data points", new Object[] { Integer.valueOf(255) });
/*     */     }
/*     */     
/* 214 */     if (this.dataNumbers.size() > 255) {
/* 215 */       throw new RobotCoreException("Cannot have more than %d number data points", new Object[] { Integer.valueOf(255) });
/*     */     }
/*     */     
/* 218 */     int payloadSize = countMessageBytes();
/* 219 */     int totalSize = 5 + payloadSize;
/*     */     
/* 221 */     if (totalSize > 4098) {
/* 222 */       throw new RobotCoreException(String.format("Cannot send telemetry data of %d bytes; max is %d", new Object[] { Integer.valueOf(totalSize), Integer.valueOf(4098) }));
/*     */     }
/*     */     
/* 225 */     ByteBuffer buffer = getWriteBuffer(payloadSize);
/*     */     
/*     */ 
/* 228 */     buffer.putLong(this.timestamp);
/*     */     
/*     */ 
/* 231 */     buffer.put((byte)(this.isSorted ? 1 : 0));
/*     */     
/*     */ 
/* 234 */     buffer.put(this.robotState.asByte());
/*     */     
/*     */ 
/* 237 */     if (this.tag.length() == 0) {
/* 238 */       putTagLen(buffer, 0);
/*     */     } else {
/* 240 */       byte[] tagBytes = this.tag.getBytes(CHARSET);
/*     */       
/* 242 */       if (tagBytes.length > 255) {
/* 243 */         throw new RobotCoreException(String.format("Telemetry tag cannot exceed %d bytes [%s]", new Object[] { Integer.valueOf(255), this.tag }));
/*     */       }
/*     */       
/* 246 */       putTagLen(buffer, tagBytes.length);
/* 247 */       buffer.put(tagBytes);
/*     */     }
/*     */     
/*     */ 
/* 251 */     putCount(buffer, this.dataStrings.size());
/* 252 */     for (Map.Entry<String, String> entry : this.dataStrings.entrySet()) {
/* 253 */       byte[] key = ((String)entry.getKey()).getBytes(CHARSET);
/* 254 */       byte[] value = ((String)entry.getValue()).getBytes(CHARSET);
/*     */       
/* 256 */       if (key.length > 65535)
/* 257 */         throw new RobotCoreException("telemetry key '%s' too long: %d bytes; max %d bytes", new Object[] { entry.getKey(), Integer.valueOf(key.length), Integer.valueOf(65535) });
/* 258 */       if (value.length > 65535) {
/* 259 */         throw new RobotCoreException("telemetry value '%s' too long: %d bytes; max %d bytes", new Object[] { entry.getValue(), Integer.valueOf(value.length), Integer.valueOf(65535) });
/*     */       }
/* 261 */       putKeyLen(buffer, key.length);
/* 262 */       buffer.put(key);
/* 263 */       putValueLen(buffer, value.length);
/* 264 */       buffer.put(value);
/*     */     }
/*     */     
/*     */ 
/* 268 */     putCount(buffer, this.dataNumbers.size());
/* 269 */     for (Map.Entry<String, Float> entry : this.dataNumbers.entrySet()) {
/* 270 */       byte[] key = ((String)entry.getKey()).getBytes(CHARSET);
/* 271 */       float val = ((Float)entry.getValue()).floatValue();
/*     */       
/* 273 */       if (key.length > 65535) {
/* 274 */         throw new RobotCoreException("telemetry key '%s' too long: %d bytes; max %d bytes", new Object[] { entry.getKey(), Integer.valueOf(key.length), Integer.valueOf(65535) });
/*     */       }
/* 276 */       putKeyLen(buffer, key.length);
/* 277 */       buffer.put(key);
/* 278 */       buffer.putFloat(val);
/*     */     }
/*     */     
/*     */ 
/* 282 */     return buffer.array();
/*     */   }
/*     */   
/*     */   public synchronized void fromByteArray(byte[] byteArray)
/*     */     throws RobotCoreException
/*     */   {
/* 288 */     clearData();
/*     */     
/* 290 */     ByteBuffer buffer = getReadBuffer(byteArray);
/*     */     
/*     */ 
/* 293 */     this.timestamp = buffer.getLong();
/*     */     
/*     */ 
/* 296 */     this.isSorted = (buffer.get() != 0);
/*     */     
/*     */ 
/* 299 */     this.robotState = RobotState.fromByte(buffer.get());
/*     */     
/*     */ 
/* 302 */     int tagLength = getTagLen(buffer);
/* 303 */     if (tagLength == 0) {
/* 304 */       this.tag = "";
/*     */     } else {
/* 306 */       byte[] tagBytes = new byte[tagLength];
/* 307 */       buffer.get(tagBytes);
/* 308 */       this.tag = new String(tagBytes, CHARSET);
/*     */     }
/*     */     
/*     */ 
/* 312 */     int stringDataPoints = getCount(buffer);
/* 313 */     for (int i = 0; i < stringDataPoints; i++) {
/* 314 */       int keyLength = getKeyLen(buffer);
/* 315 */       byte[] keyBytes = new byte[keyLength];
/* 316 */       buffer.get(keyBytes);
/*     */       
/* 318 */       int valLength = getValueLen(buffer);
/* 319 */       byte[] valBytes = new byte[valLength];
/* 320 */       buffer.get(valBytes);
/*     */       
/* 322 */       String key = new String(keyBytes, CHARSET);
/* 323 */       String val = new String(valBytes, CHARSET);
/*     */       
/* 325 */       this.dataStrings.put(key, val);
/*     */     }
/*     */     
/*     */ 
/* 329 */     int numberDataPoints = getCount(buffer);
/* 330 */     for (int i = 0; i < numberDataPoints; i++) {
/* 331 */       int keyLength = getKeyLen(buffer);
/* 332 */       byte[] keyBytes = new byte[keyLength];
/* 333 */       buffer.get(keyBytes);
/* 334 */       String key = new String(keyBytes, CHARSET);
/* 335 */       float val = buffer.getFloat();
/*     */       
/* 337 */       this.dataNumbers.put(key, Float.valueOf(val));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int cbTagLen = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int cbCountLen = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int cbKeyLen = 2;
/*     */   
/*     */ 
/*     */ 
/*     */   static void putCount(ByteBuffer buffer, int count)
/*     */   {
/* 360 */     buffer.put((byte)count);
/*     */   }
/*     */   
/* 363 */   static int getCount(ByteBuffer buffer) { return TypeConversion.unsignedByteToInt(buffer.get()); }
/*     */   
/*     */   static void putTagLen(ByteBuffer buffer, int cbTag)
/*     */   {
/* 367 */     buffer.put((byte)cbTag);
/*     */   }
/*     */   
/* 370 */   static int getTagLen(ByteBuffer buffer) { return TypeConversion.unsignedByteToInt(buffer.get()); }
/*     */   
/*     */   static void putKeyLen(ByteBuffer buffer, int cbKey)
/*     */   {
/* 374 */     buffer.putShort((short)cbKey);
/*     */   }
/*     */   
/* 377 */   static int getKeyLen(ByteBuffer buffer) { return TypeConversion.unsignedShortToInt(buffer.getShort()); }
/*     */   
/*     */ 
/* 380 */   static void putValueLen(ByteBuffer buffer, int cbValue) { putKeyLen(buffer, cbValue); }
/* 381 */   static int getValueLen(ByteBuffer buffer) { return getKeyLen(buffer); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int cbValueLen = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int cbFloat = 4;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int cbTagMax = 255;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int cCountMax = 255;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int cbKeyMax = 65535;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int cbValueMax = 65535;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int countMessageBytes()
/*     */   {
/* 419 */     int count = 10;
/*     */     
/*     */ 
/* 422 */     count += 1 + this.tag.getBytes(CHARSET).length;
/*     */     
/*     */ 
/* 425 */     count++;
/* 426 */     for (Map.Entry<String, String> entry : this.dataStrings.entrySet()) {
/* 427 */       count += 2 + ((String)entry.getKey()).getBytes(CHARSET).length;
/* 428 */       count += 2 + ((String)entry.getValue()).getBytes(CHARSET).length;
/*     */     }
/*     */     
/*     */ 
/* 432 */     count++;
/* 433 */     for (Map.Entry<String, Float> entry : this.dataNumbers.entrySet()) {
/* 434 */       count += 2 + ((String)entry.getKey()).getBytes(CHARSET).length;
/* 435 */       count += 4;
/*     */     }
/*     */     
/* 438 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\robocol\TelemetryMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */