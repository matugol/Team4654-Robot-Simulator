/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NextLock
/*     */ {
/*  42 */   protected final Object lock = this;
/*  43 */   protected long count = 0L;
/*     */   /* Error */
/*     */   public Waiter getNextWaiter()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 2	com/qualcomm/robotcore/util/NextLock:lock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: new 4	com/qualcomm/robotcore/util/NextLock$Waiter
/*     */     //   10: dup
/*     */     //   11: aload_0
/*     */     //   12: aload_0
/*     */     //   13: getfield 3	com/qualcomm/robotcore/util/NextLock:count	J
/*     */     //   16: lconst_1
/*     */     //   17: ladd
/*     */     //   18: invokespecial 5	com/qualcomm/robotcore/util/NextLock$Waiter:<init>	(Lcom/qualcomm/robotcore/util/NextLock;J)V
/*     */     //   21: aload_1
/*     */     //   22: monitorexit
/*     */     //   23: areturn
/*     */     //   24: astore_2
/*     */     //   25: aload_1
/*     */     //   26: monitorexit
/*     */     //   27: aload_2
/*     */     //   28: athrow
/*     */     // Line number table:
/*     */     //   Java source line #106	-> byte code offset #0
/*     */     //   Java source line #108	-> byte code offset #7
/*     */     //   Java source line #109	-> byte code offset #24
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	29	0	this	NextLock
/*     */     //   5	21	1	Ljava/lang/Object;	Object
/*     */     //   24	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	23	24	finally
/*     */     //   24	27	24	finally
/*     */   }
/*     */   
/*     */   public class Waiter
/*     */   {
/*     */     long nextCount;
/*     */     
/*     */     Waiter(long nextCount)
/*     */     {
/*  60 */       this.nextCount = nextCount;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void awaitNext()
/*     */       throws InterruptedException
/*     */     {
/*  70 */       synchronized (NextLock.this.lock)
/*     */       {
/*     */         do {
/*  73 */           NextLock.this.lock.wait();
/*     */         }
/*  75 */         while (NextLock.this.count < this.nextCount);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void advanceNext()
/*     */   {
/* 118 */     synchronized (this.lock)
/*     */     {
/* 120 */       this.count += 1L;
/* 121 */       this.lock.notifyAll();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\NextLock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */