/*     */ package com.qualcomm.hardware.matrix;
/*     */ 
/*     */ import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.TypeConversion;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Iterator;
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
/*     */ public class MatrixMasterController
/*     */   implements I2cController.I2cPortReadyCallback
/*     */ {
/*     */   private static final byte WASTED_BYTE = 0;
/*     */   private static final byte MATRIX_CONTROLLER_I2C_ADDR = 16;
/*     */   private static final byte TIMEOUT_OFFSET = 66;
/*     */   private static final byte BATTERY_OFFSET = 67;
/*     */   private static final byte START_FLAG_OFFSET = 68;
/*     */   private static final byte SERVO_ENABLE_OFFSET = 69;
/*  58 */   private static final byte[] servoSpeedOffset = { 0, 70, 72, 74, 76 };
/*  59 */   private static final byte[] motorPositionOffset = { 0, 78, 88, 98, 108 };
/*  60 */   private static final byte[] motorTargetOffset = { 0, 82, 92, 102, 112 };
/*  61 */   private static final byte[] motorSpeedOffset = { 0, 86, 96, 106, 116 };
/*  62 */   private static final byte[] motorModeOffset = { 0, 87, 97, 107, 117 };
/*     */   
/*     */   protected ConcurrentLinkedQueue<MatrixI2cTransaction> transactionQueue;
/*     */   protected ModernRoboticsUsbLegacyModule legacyModule;
/*     */   protected MatrixDcMotorController motorController;
/*     */   protected MatrixServoController servoController;
/*     */   protected int physicalPort;
/*  69 */   private volatile boolean waitingForGodot = false;
/*     */   
/*     */   private static final boolean debug = false;
/*  72 */   private final ElapsedTime lastTransaction = new ElapsedTime(0L);
/*     */   private static final double MIN_TRANSACTION_RATE = 2.0D;
/*     */   private static final int DEFAULT_TIMEOUT = 3;
/*     */   
/*     */   public MatrixMasterController(ModernRoboticsUsbLegacyModule legacyModule, int physicalPort)
/*     */   {
/*  78 */     this.legacyModule = legacyModule;
/*  79 */     this.physicalPort = physicalPort;
/*     */     
/*  81 */     this.transactionQueue = new ConcurrentLinkedQueue();
/*     */     
/*  83 */     legacyModule.registerForI2cPortReadyCallback(this, physicalPort);
/*     */   }
/*     */   
/*     */   public void registerMotorController(MatrixDcMotorController mc)
/*     */   {
/*  88 */     this.motorController = mc;
/*     */   }
/*     */   
/*     */   public void registerServoController(MatrixServoController sc)
/*     */   {
/*  93 */     this.servoController = sc;
/*     */   }
/*     */   
/*     */   public int getPort()
/*     */   {
/*  98 */     return this.physicalPort;
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 103 */     return this.legacyModule.getConnectionInfo() + "; port " + this.physicalPort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean queueTransaction(MatrixI2cTransaction transaction, boolean force)
/*     */   {
/* 114 */     if (!force) {
/* 115 */       Iterator<MatrixI2cTransaction> it = this.transactionQueue.iterator();
/* 116 */       while (it.hasNext()) {
/* 117 */         MatrixI2cTransaction t = (MatrixI2cTransaction)it.next();
/* 118 */         if (t.isEqual(transaction)) {
/* 119 */           buginf("NO Queue transaction " + transaction.toString());
/* 120 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */     buginf("YES Queue transaction " + transaction.toString());
/* 134 */     this.transactionQueue.add(transaction);
/* 135 */     return true;
/*     */   }
/*     */   
/*     */   public boolean queueTransaction(MatrixI2cTransaction transaction)
/*     */   {
/* 140 */     return queueTransaction(transaction, false);
/*     */   }
/*     */   
/*     */   public void waitOnRead()
/*     */   {
/* 145 */     synchronized (this) {
/* 146 */       this.waitingForGodot = true;
/*     */       try {
/* 148 */         while (this.waitingForGodot) {
/* 149 */           wait(0L);
/*     */         }
/*     */       } catch (InterruptedException e) {
/* 152 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void handleReadDone(MatrixI2cTransaction transaction)
/*     */   {
/* 160 */     byte[] readBuffer = this.legacyModule.getI2cReadCache(this.physicalPort);
/*     */     
/* 162 */     switch (transaction.property) {
/*     */     case PROPERTY_BATTERY: 
/* 164 */       this.motorController.handleReadBattery(readBuffer);
/* 165 */       break;
/*     */     case PROPERTY_POSITION: 
/* 167 */       this.motorController.handleReadPosition(transaction, readBuffer);
/* 168 */       break;
/*     */     case PROPERTY_TARGET: 
/* 170 */       this.motorController.handleReadPosition(transaction, readBuffer);
/* 171 */       break;
/*     */     case PROPERTY_MODE: 
/* 173 */       this.motorController.handleReadMode(transaction, readBuffer);
/* 174 */       break;
/*     */     case PROPERTY_SERVO: 
/* 176 */       this.servoController.handleReadServo(transaction, readBuffer);
/* 177 */       break;
/*     */     default: 
/* 179 */       RobotLog.e("Transaction not a read " + transaction.property);
/*     */     }
/*     */     
/* 182 */     synchronized (this) {
/* 183 */       if (this.waitingForGodot) {
/* 184 */         this.waitingForGodot = false;
/* 185 */         notify();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void sendHeartbeat()
/*     */   {
/* 196 */     MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_TIMEOUT, 3);
/*     */     
/* 198 */     queueTransaction(transaction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void portIsReady(int port)
/*     */   {
/* 207 */     if (this.transactionQueue.isEmpty()) {
/* 208 */       if (this.lastTransaction.time() > 2.0D) {
/* 209 */         sendHeartbeat();
/* 210 */         this.lastTransaction.reset();
/*     */       }
/* 212 */       return;
/*     */     }
/*     */     
/* 215 */     MatrixI2cTransaction transaction = (MatrixI2cTransaction)this.transactionQueue.peek();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 225 */     if (transaction.state == MatrixI2cTransaction.I2cTransactionState.PENDING_I2C_READ)
/*     */     {
/*     */ 
/*     */ 
/* 229 */       this.legacyModule.readI2cCacheFromModule(this.physicalPort);
/* 230 */       transaction.state = MatrixI2cTransaction.I2cTransactionState.PENDING_READ_DONE;
/* 231 */       return; }
/* 232 */     if (transaction.state == MatrixI2cTransaction.I2cTransactionState.PENDING_I2C_WRITE)
/*     */     {
/*     */ 
/*     */ 
/* 236 */       transaction = (MatrixI2cTransaction)this.transactionQueue.poll();
/*     */       
/*     */ 
/*     */ 
/* 240 */       if (this.transactionQueue.isEmpty()) {
/* 241 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 246 */       transaction = (MatrixI2cTransaction)this.transactionQueue.peek();
/* 247 */     } else if (transaction.state == MatrixI2cTransaction.I2cTransactionState.PENDING_READ_DONE)
/*     */     {
/*     */ 
/*     */ 
/* 251 */       handleReadDone(transaction);
/*     */       
/* 253 */       transaction = (MatrixI2cTransaction)this.transactionQueue.poll();
/* 254 */       if (this.transactionQueue.isEmpty()) {
/* 255 */         return;
/*     */       }
/* 257 */       transaction = (MatrixI2cTransaction)this.transactionQueue.peek(); }
/*     */     byte len;
/*     */     byte len;
/* 260 */     byte len; byte len; byte len; byte len; byte len; byte len; byte len; byte offset; byte[] buffer; byte len; switch (transaction.property) {
/*     */     case PROPERTY_POSITION: 
/* 262 */       byte offset = motorPositionOffset[transaction.motor];
/* 263 */       byte len = 4;
/*     */       
/*     */ 
/*     */ 
/* 267 */       byte[] buffer = new byte[1];
/* 268 */       buffer[0] = 0;
/* 269 */       break;
/*     */     case PROPERTY_BATTERY: 
/* 271 */       byte offset = 67;
/* 272 */       byte[] buffer = new byte[1];
/* 273 */       buffer[0] = 0;
/* 274 */       len = 1;
/* 275 */       break;
/*     */     case PROPERTY_TIMEOUT: 
/* 277 */       byte offset = 66;
/* 278 */       byte[] buffer = new byte[1];
/* 279 */       buffer[0] = ((byte)transaction.value);
/* 280 */       len = 1;
/* 281 */       break;
/*     */     case PROPERTY_START: 
/* 283 */       byte offset = 68;
/* 284 */       byte[] buffer = new byte[1];
/* 285 */       buffer[0] = ((byte)transaction.value);
/* 286 */       len = 1;
/* 287 */       break;
/*     */     case PROPERTY_SPEED: 
/* 289 */       byte offset = motorSpeedOffset[transaction.motor];
/* 290 */       byte[] buffer = new byte[1];
/* 291 */       buffer[0] = ((byte)transaction.value);
/* 292 */       len = 1;
/* 293 */       break;
/*     */     case PROPERTY_TARGET: 
/* 295 */       byte offset = motorTargetOffset[transaction.motor];
/* 296 */       byte[] buffer = TypeConversion.intToByteArray(transaction.value);
/* 297 */       len = 4;
/* 298 */       break;
/*     */     case PROPERTY_MODE: 
/* 300 */       byte offset = motorModeOffset[transaction.motor];
/* 301 */       byte[] buffer = new byte[1];
/* 302 */       buffer[0] = ((byte)transaction.value);
/* 303 */       len = 1;
/* 304 */       break;
/*     */     case PROPERTY_MOTOR_BATCH: 
/* 306 */       byte offset = motorPositionOffset[transaction.motor];
/* 307 */       ByteBuffer bb = ByteBuffer.allocate(10);
/*     */       
/*     */ 
/*     */ 
/* 311 */       bb.put(TypeConversion.intToByteArray(0));
/* 312 */       bb.put(TypeConversion.intToByteArray(transaction.target));
/* 313 */       bb.put(transaction.speed);
/* 314 */       bb.put(transaction.mode);
/* 315 */       byte[] buffer = bb.array();
/* 316 */       len = 10;
/* 317 */       break;
/*     */     case PROPERTY_SERVO: 
/* 319 */       byte offset = servoSpeedOffset[transaction.servo];
/* 320 */       byte[] buffer = new byte[2];
/* 321 */       buffer[0] = transaction.speed;
/* 322 */       buffer[1] = ((byte)transaction.target);
/* 323 */       len = 2;
/* 324 */       break;
/*     */     case PROPERTY_SERVO_ENABLE: 
/* 326 */       byte offset = 69;
/* 327 */       byte[] buffer = new byte[1];
/* 328 */       buffer[0] = ((byte)transaction.value);
/* 329 */       len = 1;
/* 330 */       break;
/*     */     default: 
/* 332 */       offset = 0;
/* 333 */       buffer = new byte[1];
/* 334 */       buffer[0] = ((byte)transaction.value);
/* 335 */       len = 1;
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 342 */       if (transaction.write) {
/* 343 */         this.legacyModule.setWriteMode(this.physicalPort, 16, offset);
/* 344 */         this.legacyModule.setData(this.physicalPort, buffer, len);
/* 345 */         transaction.state = MatrixI2cTransaction.I2cTransactionState.PENDING_I2C_WRITE;
/*     */       } else {
/* 347 */         this.legacyModule.setReadMode(this.physicalPort, 16, offset, len);
/* 348 */         transaction.state = MatrixI2cTransaction.I2cTransactionState.PENDING_I2C_READ;
/*     */       }
/* 350 */       this.legacyModule.setI2cPortActionFlag(this.physicalPort);
/* 351 */       this.legacyModule.writeI2cCacheToModule(this.physicalPort);
/*     */     } catch (IllegalArgumentException e) {
/* 353 */       RobotLog.e(e.getMessage());
/*     */     }
/* 355 */     buginf(transaction.toString());
/*     */   }
/*     */   
/*     */   protected void buginf(String s) {}
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Hardware-release.jar!\classes.jar!\com\qualcomm\hardware\matrix\MatrixMasterController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */