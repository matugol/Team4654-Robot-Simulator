/*     */ package com.qualcomm.robotcore.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WeakReferenceSet<E>
/*     */   implements Set<E>
/*     */ {
/*  21 */   WeakHashMap<E, Integer> members = new WeakHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean add(E o)
/*     */   {
/*  30 */     synchronized (this.members)
/*     */     {
/*  32 */       return this.members.put(o, Integer.valueOf(1)) == null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean remove(Object o)
/*     */   {
/*  39 */     synchronized (this.members)
/*     */     {
/*  41 */       return this.members.remove(o) != null;
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean contains(Object o)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	com/qualcomm/robotcore/util/WeakReferenceSet:members	Ljava/util/WeakHashMap;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	com/qualcomm/robotcore/util/WeakReferenceSet:members	Ljava/util/WeakHashMap;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 8	java/util/WeakHashMap:containsKey	(Ljava/lang/Object;)Z
/*     */     //   15: aload_2
/*     */     //   16: monitorexit
/*     */     //   17: ireturn
/*     */     //   18: astore_3
/*     */     //   19: aload_2
/*     */     //   20: monitorexit
/*     */     //   21: aload_3
/*     */     //   22: athrow
/*     */     // Line number table:
/*     */     //   Java source line #48	-> byte code offset #0
/*     */     //   Java source line #50	-> byte code offset #7
/*     */     //   Java source line #51	-> byte code offset #18
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	23	0	this	WeakReferenceSet<E>
/*     */     //   0	23	1	o	Object
/*     */     //   5	15	2	Ljava/lang/Object;	Object
/*     */     //   18	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	17	18	finally
/*     */     //   18	21	18	finally
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends E> collection)
/*     */   {
/*  61 */     synchronized (this.members)
/*     */     {
/*  63 */       boolean modified = false;
/*  64 */       for (E o : collection)
/*     */       {
/*  66 */         if (add(o)) modified = true;
/*     */       }
/*  68 */       return modified;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void clear()
/*     */   {
/*  75 */     synchronized (this.members)
/*     */     {
/*  77 */       this.members.clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsAll(Collection<?> collection)
/*     */   {
/*  84 */     synchronized (this.members)
/*     */     {
/*  86 */       for (Object o : collection)
/*     */       {
/*  88 */         if (!contains(o)) return false;
/*     */       }
/*  90 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  97 */     return size() == 0;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public int size()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	com/qualcomm/robotcore/util/WeakReferenceSet:members	Ljava/util/WeakHashMap;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	com/qualcomm/robotcore/util/WeakReferenceSet:members	Ljava/util/WeakHashMap;
/*     */     //   11: invokevirtual 16	java/util/WeakHashMap:size	()I
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: ireturn
/*     */     //   17: astore_2
/*     */     //   18: aload_1
/*     */     //   19: monitorexit
/*     */     //   20: aload_2
/*     */     //   21: athrow
/*     */     // Line number table:
/*     */     //   Java source line #103	-> byte code offset #0
/*     */     //   Java source line #105	-> byte code offset #7
/*     */     //   Java source line #106	-> byte code offset #17
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	22	0	this	WeakReferenceSet<E>
/*     */     //   5	14	1	Ljava/lang/Object;	Object
/*     */     //   17	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	16	17	finally
/*     */     //   17	20	17	finally
/*     */   }
/*     */   
/*     */   public Object[] toArray()
/*     */   {
/* 112 */     synchronized (this.members)
/*     */     {
/* 114 */       List<Object> list = new LinkedList();
/* 115 */       for (Object o : this.members.keySet())
/*     */       {
/* 117 */         list.add(o);
/*     */       }
/* 119 */       return list.toArray();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 127 */     synchronized (this.members)
/*     */     {
/* 129 */       List<E> list = new LinkedList();
/* 130 */       for (E o : this.members.keySet())
/*     */       {
/* 132 */         list.add(o);
/*     */       }
/* 134 */       return list.iterator();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean removeAll(Collection<?> collection)
/*     */   {
/* 141 */     synchronized (this.members)
/*     */     {
/* 143 */       boolean modified = false;
/* 144 */       for (Object o : collection)
/*     */       {
/* 146 */         if (remove(o)) modified = true;
/*     */       }
/* 148 */       return modified;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean retainAll(Collection<?> collection)
/*     */   {
/* 155 */     synchronized (this.members)
/*     */     {
/* 157 */       boolean modified = false;
/* 158 */       for (Object o : this)
/*     */       {
/* 160 */         if (!collection.contains(o))
/*     */         {
/* 162 */           if (remove(o)) modified = true;
/*     */         }
/*     */       }
/* 165 */       return modified;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Object[] toArray(Object[] array)
/*     */   {
/* 172 */     synchronized (this.members)
/*     */     {
/* 174 */       Object[] cur = toArray();
/* 175 */       Object[] result = cur.length > array.length ? new Object[cur.length] : array;
/* 176 */       for (int i = 0; 
/* 177 */           i < cur.length; i++)
/*     */       {
/* 179 */         result[i] = cur[i];
/*     */       }
/* 181 */       for (; i < result.length; i++)
/*     */       {
/* 183 */         result[i] = null;
/*     */       }
/* 185 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\util\WeakReferenceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */