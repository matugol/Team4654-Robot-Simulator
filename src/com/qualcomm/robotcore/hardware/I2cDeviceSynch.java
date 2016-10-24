/*     */ package com.qualcomm.robotcore.hardware;
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
/*     */ public abstract interface I2cDeviceSynch
/*     */   extends I2cDeviceSynchSimple, Engagable
/*     */ {
/*     */   public abstract void setReadWindow(ReadWindow paramReadWindow);
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
/*     */   public abstract ReadWindow getReadWindow();
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
/*     */   public abstract void ensureReadWindow(ReadWindow paramReadWindow1, ReadWindow paramReadWindow2);
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
/*     */   public abstract I2cDeviceSynchSimple.TimestampedData readTimeStamped(int paramInt1, int paramInt2, ReadWindow paramReadWindow1, ReadWindow paramReadWindow2);
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
/*     */   public abstract void setHeartbeatInterval(int paramInt);
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
/*     */   public abstract int getHeartbeatInterval();
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
/*     */   public abstract void setHeartbeatAction(HeartbeatAction paramHeartbeatAction);
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
/*     */   public abstract HeartbeatAction getHeartbeatAction();
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
/*     */   public static class HeartbeatAction
/*     */   {
/*     */     public final boolean rereadLastRead;
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
/*     */     public final boolean rewriteLastWritten;
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
/*     */     public final I2cDeviceSynch.ReadWindow heartbeatReadWindow;
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
/*     */     public HeartbeatAction(boolean rereadLastRead, boolean rewriteLastWritten, I2cDeviceSynch.ReadWindow readWindow)
/*     */     {
/* 220 */       this.rereadLastRead = rereadLastRead;
/* 221 */       this.rewriteLastWritten = rewriteLastWritten;
/* 222 */       this.heartbeatReadWindow = readWindow;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum ReadMode
/*     */   {
/* 247 */     REPEAT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 256 */     BALANCED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 263 */     ONLY_ONCE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private ReadMode() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ReadWindow
/*     */   {
/*     */     public static final int READ_REGISTER_COUNT_MAX = 26;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public static final int WRITE_REGISTER_COUNT_MAX = 26;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private final int iregFirst;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private final int creg;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private final I2cDeviceSynch.ReadMode readMode;
/*     */     
/*     */ 
/*     */ 
/*     */     private boolean usedForRead;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getRegisterFirst()
/*     */     {
/* 309 */       return this.iregFirst;
/*     */     }
/*     */     
/*     */     public int getRegisterMax()
/*     */     {
/* 314 */       return this.iregFirst + this.creg;
/*     */     }
/*     */     
/*     */     public int getRegisterCount()
/*     */     {
/* 319 */       return this.creg;
/*     */     }
/*     */     
/*     */     public I2cDeviceSynch.ReadMode getReadMode()
/*     */     {
/* 324 */       return this.readMode;
/*     */     }
/*     */     
/*     */     public boolean hasWindowBeenUsedForRead()
/*     */     {
/* 329 */       return this.usedForRead;
/*     */     }
/*     */     
/*     */     public void noteWindowUsedForRead() {
/* 333 */       this.usedForRead = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean canBeUsedToRead()
/*     */     {
/* 342 */       return (!this.usedForRead) || (this.readMode != I2cDeviceSynch.ReadMode.ONLY_ONCE);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean mayInitiateSwitchToReadMode()
/*     */     {
/* 352 */       return (!this.usedForRead) || (this.readMode == I2cDeviceSynch.ReadMode.REPEAT);
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
/*     */ 
/*     */ 
/*     */ 
/*     */     public ReadWindow(int iregFirst, int creg, I2cDeviceSynch.ReadMode readMode)
/*     */     {
/* 368 */       this.readMode = readMode;
/* 369 */       this.usedForRead = false;
/* 370 */       this.iregFirst = iregFirst;
/* 371 */       this.creg = creg;
/* 372 */       if ((creg < 0) || (creg > 26)) {
/* 373 */         throw new IllegalArgumentException(String.format("buffer length %d invalid; max is %d", new Object[] { Integer.valueOf(creg), Integer.valueOf(26) }));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ReadWindow readableCopy()
/*     */     {
/* 382 */       return new ReadWindow(this.iregFirst, this.creg, this.readMode);
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
/*     */ 
/*     */ 
/*     */     public boolean sameAsIncludingMode(ReadWindow him)
/*     */     {
/* 397 */       if (him == null) {
/* 398 */         return false;
/*     */       }
/* 400 */       return (getRegisterFirst() == him.getRegisterFirst()) && (getRegisterCount() == him.getRegisterCount()) && (getReadMode() == him.getReadMode());
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
/*     */ 
/*     */     public boolean contains(ReadWindow him)
/*     */     {
/* 414 */       if (him == null) {
/* 415 */         return false;
/*     */       }
/* 417 */       return (getRegisterFirst() <= him.getRegisterFirst()) && (him.getRegisterMax() <= getRegisterMax());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean containsWithSameMode(ReadWindow him)
/*     */     {
/* 429 */       return (contains(him)) && (getReadMode() == him.getReadMode());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean contains(int ireg, int creg)
/*     */     {
/* 441 */       return containsWithSameMode(new ReadWindow(ireg, creg, getReadMode()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */