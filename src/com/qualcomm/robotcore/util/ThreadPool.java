/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import android.support.annotation.Nullable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadPool
/*     */ {
/*     */   public static class Singleton<T>
/*     */   {
/*  86 */     private ExecutorService service = null;
/*  87 */     private final Object lock = new Object();
/*  88 */     private ThreadPool.SingletonResult<T> result = null;
/*  89 */     private boolean inFlight = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setService(ExecutorService service)
/*     */     {
/*  97 */       this.service = service;
/*     */     }
/*     */     
/*     */     public void reset()
/*     */     {
/* 102 */       synchronized (this.lock)
/*     */       {
/* 104 */         this.result = null;
/* 105 */         this.inFlight = false;
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
/*     */ 
/*     */ 
/*     */     public ThreadPool.SingletonResult<T> submit(final Runnable runnable)
/*     */     {
/* 121 */       submit(new Callable()
/*     */       {
/*     */         public T call() throws Exception
/*     */         {
/* 125 */           runnable.run();
/* 126 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public ThreadPool.SingletonResult<T> submit(final Callable<T> callable)
/*     */     {
/* 133 */       synchronized (this.lock)
/*     */       {
/*     */ 
/* 136 */         if (!this.inFlight)
/*     */         {
/* 138 */           if (this.service == null)
/*     */           {
/* 140 */             throw new IllegalArgumentException("Singleton service must be set before work is submitted");
/*     */           }
/*     */           
/*     */ 
/* 144 */           this.inFlight = true;
/*     */           
/*     */ 
/* 147 */           this.result = new ThreadPool.SingletonResult(this.service.submit(new Callable()
/*     */           {
/*     */             public T call() throws Exception
/*     */             {
/*     */               try {
/* 152 */                 return (T)callable.call();
/*     */               }
/*     */               catch (InterruptedException ignored)
/*     */               {
/* 156 */                 Thread.currentThread().interrupt();
/* 157 */                 return null;
/*     */               }
/*     */               catch (Exception e)
/*     */               {
/* 161 */                 RobotLog.e("exception thrown during Singleton.submit()");
/* 162 */                 RobotLog.logStacktrace(e);
/* 163 */                 return null;
/*     */ 
/*     */               }
/*     */               finally
/*     */               {
/* 168 */                 synchronized (ThreadPool.Singleton.this.lock)
/*     */                 {
/* 170 */                   ThreadPool.Singleton.this.inFlight = false;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }));
/*     */         }
/* 176 */         return this.result;
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public ThreadPool.SingletonResult<T> getResult()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	com/qualcomm/robotcore/util/ThreadPool$Singleton:lock	Ljava/lang/Object;
/*     */       //   4: dup
/*     */       //   5: astore_1
/*     */       //   6: monitorenter
/*     */       //   7: aload_0
/*     */       //   8: getfield 6	com/qualcomm/robotcore/util/ThreadPool$Singleton:result	Lcom/qualcomm/robotcore/util/ThreadPool$SingletonResult;
/*     */       //   11: aload_1
/*     */       //   12: monitorexit
/*     */       //   13: areturn
/*     */       //   14: astore_2
/*     */       //   15: aload_1
/*     */       //   16: monitorexit
/*     */       //   17: aload_2
/*     */       //   18: athrow
/*     */       // Line number table:
/*     */       //   Java source line #186	-> byte code offset #0
/*     */       //   Java source line #188	-> byte code offset #7
/*     */       //   Java source line #189	-> byte code offset #14
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	19	0	this	Singleton<T>
/*     */       //   5	11	1	Ljava/lang/Object;	Object
/*     */       //   14	4	2	localObject1	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   7	13	14	finally
/*     */       //   14	17	14	finally
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public T await(long ms)
/*     */       throws InterruptedException
/*     */     {
/* 198 */       ThreadPool.SingletonResult<T> result = getResult();
/* 199 */       if (result != null)
/*     */       {
/* 201 */         return (T)result.await(ms);
/*     */       }
/*     */       
/* 204 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class SingletonResult<T>
/*     */   {
/* 215 */     private Future<T> future = null;
/*     */     
/*     */     public SingletonResult(Future<T> future)
/*     */     {
/* 219 */       this.future = future;
/*     */     }
/*     */     
/*     */     public SingletonResult()
/*     */     {
/* 224 */       this(null);
/*     */     }
/*     */     
/*     */     public void setFuture(Future<T> future)
/*     */     {
/* 229 */       this.future = future;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public T await(long ms)
/*     */       throws InterruptedException
/*     */     {
/*     */       try
/*     */       {
/* 242 */         if (this.future != null)
/*     */         {
/* 244 */           return (T)this.future.get(ms, TimeUnit.MILLISECONDS);
/*     */         }
/*     */       }
/*     */       catch (ExecutionException e)
/*     */       {
/* 249 */         RobotLog.v("singleton threw ExecutionExceptoin %s", new Object[] { e.toString() });
/*     */       }
/*     */       catch (TimeoutException e)
/*     */       {
/* 253 */         RobotLog.v("singleton timed out");
/*     */       }
/* 255 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public T await()
/*     */       throws InterruptedException
/*     */     {
/*     */       try
/*     */       {
/* 268 */         if (this.future != null)
/*     */         {
/* 270 */           return (T)this.future.get();
/*     */         }
/*     */       }
/*     */       catch (ExecutionException localExecutionException) {}
/*     */       
/*     */ 
/*     */ 
/* 277 */       return null;
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
/*     */   public static ExecutorService newSingleThreadExecutor()
/*     */   {
/* 292 */     ExecutorService result = new RecordingThreadPool(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
/*     */     
/* 294 */     noteNewExecutor(result);
/* 295 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ExecutorService newFixedThreadPool(int numberOfThreads)
/*     */   {
/* 305 */     ExecutorService result = new RecordingThreadPool(numberOfThreads, numberOfThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
/*     */     
/* 307 */     noteNewExecutor(result);
/* 308 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ExecutorService newCachedThreadPool()
/*     */   {
/* 318 */     ExecutorService result = new RecordingThreadPool(0, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new SynchronousQueue());
/*     */     
/* 320 */     noteNewExecutor(result);
/* 321 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ScheduledExecutorService newSingleThreadScheduledExecutor()
/*     */   {
/* 331 */     ScheduledExecutorService result = new RecordingSingleThreadedScheduledExecutor(1);
/* 332 */     noteNewExecutor(result);
/* 333 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 341 */   private static Map<ExecutorService, Integer> extantExecutors = new WeakHashMap();
/* 342 */   private static final Object extantExecutorsLock = new Object();
/*     */   
/*     */   private static void noteNewExecutor(ExecutorService executor)
/*     */   {
/* 346 */     synchronized (extantExecutorsLock)
/*     */     {
/* 348 */       extantExecutors.put(executor, Integer.valueOf(1));
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
/*     */   public static boolean awaitTermination(ExecutorService executorService, long timeout, TimeUnit unit, String serviceName)
/*     */     throws InterruptedException
/*     */   {
/* 369 */     double sDuration = unit.toSeconds(timeout);
/* 370 */     int msInterval = 2500;
/* 371 */     double sInterval = msInterval * 0.001D;
/* 372 */     int cAttempt = (int)(sDuration / sInterval + 0.5D);
/*     */     
/* 374 */     for (int iAttempt = 0; (!executorService.isTerminated()) && (iAttempt < cAttempt); iAttempt++)
/*     */     {
/* 376 */       RobotLog.v("waiting for service %s", new Object[] { serviceName });
/* 377 */       if (executorService.awaitTermination(msInterval, TimeUnit.MILLISECONDS))
/*     */       {
/* 379 */         RobotLog.v("awaitTermination returned, isTerminated=%s", new Object[] { Boolean.valueOf(executorService.isTerminated()) });
/* 380 */         break;
/*     */       }
/*     */       
/*     */ 
/* 384 */       RobotLog.e("awaiting shutdown: thread pool=\"%s\" attempt=%d", new Object[] { serviceName, Integer.valueOf(iAttempt + 1) });
/*     */       
/*     */ 
/* 387 */       if (iAttempt + 1 < cAttempt)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 392 */         logThreadStacks(executorService);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 398 */         interruptThreads(executorService);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 404 */         synchronized (extantExecutorsLock)
/*     */         {
/*     */ 
/* 407 */           System.gc();
/*     */           
/*     */ 
/* 410 */           for (ExecutorService e : extantExecutors.keySet())
/*     */           {
/* 412 */             logThreadStacks(e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 418 */     if (executorService.isTerminated()) {
/* 419 */       RobotLog.v("executive service %s is terminated", new Object[] { serviceName });
/*     */     } else {
/* 421 */       RobotLog.v("executive service %s is NOT terminated", new Object[] { serviceName });
/*     */     }
/* 423 */     return executorService.isTerminated();
/*     */   }
/*     */   
/*     */   private static void logThreadStacks(ExecutorService executorService)
/*     */   {
/* 428 */     if ((executorService instanceof ContainerOfThreads))
/*     */     {
/* 430 */       ContainerOfThreads container = (ContainerOfThreads)executorService;
/* 431 */       for (Thread thread : container)
/*     */       {
/* 433 */         if (thread.isAlive())
/*     */         {
/* 435 */           RobotLog.logStacktrace(thread, "", new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void interruptThreads(ExecutorService executorService)
/*     */   {
/* 443 */     if ((executorService instanceof ContainerOfThreads))
/*     */     {
/* 445 */       ContainerOfThreads container = (ContainerOfThreads)executorService;
/* 446 */       for (Thread thread : container)
/*     */       {
/* 448 */         if (thread.isAlive())
/*     */         {
/* 450 */           if (thread.getId() == Thread.currentThread().getId())
/* 451 */             RobotLog.v("interrupting current thread");
/* 452 */           thread.interrupt();
/*     */         }
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
/*     */   public static void awaitTerminationOrExitApplication(ExecutorService executorService, long timeout, TimeUnit unit, String serviceName, String message)
/*     */   {
/*     */     try
/*     */     {
/* 472 */       if (!awaitTermination(executorService, timeout, unit, serviceName))
/*     */       {
/* 474 */         exitApplication(serviceName, message);
/*     */       }
/*     */     }
/*     */     catch (InterruptedException e)
/*     */     {
/* 479 */       RobotLog.v("awaitTerminationOrExitApplication %s; interrupt thrown", new Object[] { serviceName });
/*     */       try {
/* 481 */         Thread.sleep(100L);
/*     */       }
/*     */       catch (InterruptedException ignoredInterrupt)
/*     */       {
/* 485 */         Thread.currentThread().interrupt();
/*     */       }
/* 487 */       if (!executorService.isTerminated())
/*     */       {
/* 489 */         RobotLog.v("awaitTerminationOrExitApplication %s; exiting application after interrupt", new Object[] { serviceName });
/* 490 */         exitApplication(serviceName, message);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void exitApplication(String serviceName, String message)
/*     */   {
/* 497 */     RobotLog.e("*****************************************************************");
/* 498 */     RobotLog.e("%s took too long to exit; emergency killing app.", new Object[] { serviceName });
/* 499 */     RobotLog.e("%s", new Object[] { message });
/* 500 */     RobotLog.e("*****************************************************************");
/* 501 */     System.exit(-1);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void logThreadLifeCycle(String name, Runnable runnable)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 51	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   3: aload_0
/*     */     //   4: invokevirtual 68	java/lang/Thread:setName	(Ljava/lang/String;)V
/*     */     //   7: ldc 69
/*     */     //   9: iconst_1
/*     */     //   10: anewarray 27	java/lang/Object
/*     */     //   13: dup
/*     */     //   14: iconst_0
/*     */     //   15: aload_0
/*     */     //   16: aastore
/*     */     //   17: invokestatic 70	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   20: invokestatic 53	com/qualcomm/robotcore/util/RobotLog:v	(Ljava/lang/String;)V
/*     */     //   23: aload_1
/*     */     //   24: invokeinterface 71 1 0
/*     */     //   29: ldc 72
/*     */     //   31: iconst_1
/*     */     //   32: anewarray 27	java/lang/Object
/*     */     //   35: dup
/*     */     //   36: iconst_0
/*     */     //   37: aload_0
/*     */     //   38: aastore
/*     */     //   39: invokestatic 70	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   42: invokestatic 53	com/qualcomm/robotcore/util/RobotLog:v	(Ljava/lang/String;)V
/*     */     //   45: goto +22 -> 67
/*     */     //   48: astore_2
/*     */     //   49: ldc 72
/*     */     //   51: iconst_1
/*     */     //   52: anewarray 27	java/lang/Object
/*     */     //   55: dup
/*     */     //   56: iconst_0
/*     */     //   57: aload_0
/*     */     //   58: aastore
/*     */     //   59: invokestatic 70	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   62: invokestatic 53	com/qualcomm/robotcore/util/RobotLog:v	(Ljava/lang/String;)V
/*     */     //   65: aload_2
/*     */     //   66: athrow
/*     */     //   67: return
/*     */     // Line number table:
/*     */     //   Java source line #511	-> byte code offset #0
/*     */     //   Java source line #512	-> byte code offset #7
/*     */     //   Java source line #513	-> byte code offset #23
/*     */     //   Java source line #517	-> byte code offset #29
/*     */     //   Java source line #518	-> byte code offset #45
/*     */     //   Java source line #517	-> byte code offset #48
/*     */     //   Java source line #519	-> byte code offset #67
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	68	0	name	String
/*     */     //   0	68	1	runnable	Runnable
/*     */     //   48	18	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	29	48	finally
/*     */     //   48	49	48	finally
/*     */   }
/*     */   
/*     */   static abstract interface ContainerOfThreads
/*     */     extends Iterable<Thread>
/*     */   {
/*     */     public abstract void noteNewThread(Thread paramThread);
/*     */   }
/*     */   
/*     */   static class ThreadFactoryImpl
/*     */     implements ThreadFactory
/*     */   {
/*     */     final ThreadFactory threadFactory;
/*     */     final ThreadPool.ContainerOfThreads container;
/*     */     
/*     */     ThreadFactoryImpl(ThreadPool.ContainerOfThreads container)
/*     */     {
/* 549 */       this.threadFactory = Executors.defaultThreadFactory();
/* 550 */       this.container = container;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Thread newThread(Runnable r)
/*     */     {
/* 560 */       Thread thread = this.threadFactory.newThread(r);
/* 561 */       this.container.noteNewThread(thread);
/* 562 */       return thread;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class ContainerOfThreadsRecorder
/*     */     implements ThreadPool.ContainerOfThreads
/*     */   {
/*     */     Queue<Thread> threads;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     ContainerOfThreadsRecorder()
/*     */     {
/* 584 */       this.threads = new ConcurrentLinkedQueue();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void noteNewThread(Thread thread)
/*     */     {
/* 594 */       this.threads.add(thread);
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator<Thread> iterator()
/*     */     {
/* 600 */       return this.threads.iterator();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class RecordingThreadPool
/*     */     extends ThreadPool.ContainerOfThreadsRecorder
/*     */     implements ExecutorService
/*     */   {
/*     */     ExecutorService executor;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     RecordingThreadPool(int nThreadsCore, int nThreadsMax, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)
/*     */     {
/* 618 */       this.executor = new ThreadPoolExecutor(nThreadsCore, nThreadsMax, keepAliveTime, unit, workQueue, new ThreadPool.ThreadFactoryImpl(this));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void execute(Runnable command)
/*     */     {
/* 630 */       this.executor.execute(command);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void shutdown()
/*     */     {
/* 639 */       this.executor.shutdown();
/*     */     }
/*     */     
/*     */     public List<Runnable> shutdownNow()
/*     */     {
/* 644 */       return this.executor.shutdownNow();
/*     */     }
/*     */     
/*     */     public boolean isShutdown()
/*     */     {
/* 649 */       return this.executor.isShutdown();
/*     */     }
/*     */     
/*     */     public boolean isTerminated()
/*     */     {
/* 654 */       return this.executor.isTerminated();
/*     */     }
/*     */     
/*     */     public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 659 */       return this.executor.awaitTermination(timeout, unit);
/*     */     }
/*     */     
/*     */     public <T> Future<T> submit(Callable<T> task)
/*     */     {
/* 664 */       return this.executor.submit(task);
/*     */     }
/*     */     
/*     */     public <T> Future<T> submit(Runnable task, T result)
/*     */     {
/* 669 */       return this.executor.submit(task, result);
/*     */     }
/*     */     
/*     */     public Future<?> submit(Runnable task)
/*     */     {
/* 674 */       return this.executor.submit(task);
/*     */     }
/*     */     
/*     */     public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
/*     */     {
/* 679 */       return this.executor.invokeAll(tasks);
/*     */     }
/*     */     
/*     */     public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 684 */       return this.executor.invokeAll(tasks, timeout, unit);
/*     */     }
/*     */     
/*     */     public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
/*     */     {
/* 689 */       return (T)this.executor.invokeAny(tasks);
/*     */     }
/*     */     
/*     */     public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
/*     */     {
/* 694 */       return (T)this.executor.invokeAny(tasks, timeout, unit);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class RecordingSingleThreadedScheduledExecutor
/*     */     extends ThreadPool.ContainerOfThreadsRecorder
/*     */     implements ScheduledExecutorService
/*     */   {
/*     */     ScheduledExecutorService executor;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     RecordingSingleThreadedScheduledExecutor(int numberOfThreads)
/*     */     {
/* 712 */       this.executor = Executors.newSingleThreadScheduledExecutor(new ThreadPool.ThreadFactoryImpl(this));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void execute(Runnable command)
/*     */     {
/* 721 */       this.executor.execute(command);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void shutdown()
/*     */     {
/* 730 */       this.executor.shutdown();
/*     */     }
/*     */     
/*     */     public List<Runnable> shutdownNow()
/*     */     {
/* 735 */       return this.executor.shutdownNow();
/*     */     }
/*     */     
/*     */     public boolean isShutdown()
/*     */     {
/* 740 */       return this.executor.isShutdown();
/*     */     }
/*     */     
/*     */     public boolean isTerminated()
/*     */     {
/* 745 */       return this.executor.isTerminated();
/*     */     }
/*     */     
/*     */     public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 750 */       return this.executor.awaitTermination(timeout, unit);
/*     */     }
/*     */     
/*     */     public <T> Future<T> submit(Callable<T> task)
/*     */     {
/* 755 */       return this.executor.submit(task);
/*     */     }
/*     */     
/*     */     public <T> Future<T> submit(Runnable task, T result)
/*     */     {
/* 760 */       return this.executor.submit(task, result);
/*     */     }
/*     */     
/*     */     public Future<?> submit(Runnable task)
/*     */     {
/* 765 */       return this.executor.submit(task);
/*     */     }
/*     */     
/*     */     public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
/*     */     {
/* 770 */       return this.executor.invokeAll(tasks);
/*     */     }
/*     */     
/*     */     public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 775 */       return this.executor.invokeAll(tasks, timeout, unit);
/*     */     }
/*     */     
/*     */     public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
/*     */     {
/* 780 */       return (T)this.executor.invokeAny(tasks);
/*     */     }
/*     */     
/*     */     public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
/*     */     {
/* 785 */       return (T)this.executor.invokeAny(tasks, timeout, unit);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
/*     */     {
/* 794 */       return this.executor.schedule(command, delay, unit);
/*     */     }
/*     */     
/*     */     public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
/*     */     {
/* 799 */       return this.executor.schedule(callable, delay, unit);
/*     */     }
/*     */     
/*     */     public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
/*     */     {
/* 804 */       return this.executor.scheduleAtFixedRate(command, initialDelay, period, unit);
/*     */     }
/*     */     
/*     */     public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
/*     */     {
/* 809 */       return this.executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\ThreadPool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */