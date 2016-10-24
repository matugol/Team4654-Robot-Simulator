/*     */ package org.firstinspires.ftc.robotcore.internal;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import com.android.internal.util.Predicate;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
/*     */ import com.qualcomm.robotcore.robocol.TelemetryMessage;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.firstinspires.ftc.robotcore.external.Func;
/*     */ import org.firstinspires.ftc.robotcore.external.Telemetry;
/*     */ import org.firstinspires.ftc.robotcore.external.Telemetry.Item;
/*     */ import org.firstinspires.ftc.robotcore.external.Telemetry.Line;
/*     */ import org.firstinspires.ftc.robotcore.external.Telemetry.Log;
/*     */ import org.firstinspires.ftc.robotcore.external.Telemetry.Log.DisplayOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TelemetryImpl
/*     */   implements Telemetry, TelemetryInternal
/*     */ {
/*     */   protected class Value<T>
/*     */   {
/*  68 */     protected String format = null;
/*  69 */     protected Object[] formatArgs = null;
/*  70 */     protected Object value = null;
/*  71 */     protected Func<T> valueProducer = null;
/*  72 */     protected String composed = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Value(String format, Object... formatArgs)
/*     */     {
/*  80 */       this.format = format;
/*  81 */       this.formatArgs = formatArgs;
/*     */     }
/*     */     
/*     */     Value(Func<T> format)
/*     */     {
/*  86 */       this.format = format;
/*  87 */       this.valueProducer = valueProducer;
/*     */     }
/*     */     
/*     */     Value(Object value)
/*     */     {
/*  92 */       this.value = value;
/*     */     }
/*     */     
/*     */     Value()
/*     */     {
/*  97 */       this.valueProducer = valueProducer;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     boolean isProducer()
/*     */     {
/* 106 */       return this.valueProducer != null;
/*     */     }
/*     */     
/*     */     @NonNull
/*     */     String getComposed(boolean recompose) {
/* 111 */       if ((recompose) || (this.composed == null))
/*     */       {
/* 113 */         this.composed = compose();
/*     */       }
/* 115 */       return this.composed;
/*     */     }
/*     */     
/*     */     protected String compose()
/*     */     {
/* 120 */       if (this.format != null)
/*     */       {
/* 122 */         if (this.formatArgs != null) return String.format(this.format, this.formatArgs);
/* 123 */         if (this.valueProducer != null) return String.format(this.format, new Object[] { this.valueProducer.value() });
/*     */       }
/*     */       else
/*     */       {
/* 127 */         if (this.value != null) return this.value.toString();
/* 128 */         if (this.valueProducer != null) { return this.valueProducer.value().toString();
/*     */         }
/*     */       }
/* 131 */       return "";
/*     */     }
/*     */   }
/*     */   
/*     */   protected static abstract interface Lineable
/*     */   {
/*     */     public abstract String getComposed(boolean paramBoolean);
/*     */   }
/*     */   
/*     */   protected class LineableContainer implements Iterable<TelemetryImpl.Lineable>
/*     */   {
/* 142 */     private ArrayList<TelemetryImpl.Lineable> list = new ArrayList();
/*     */     
/*     */     protected LineableContainer() {}
/*     */     
/*     */     /* Error */
/*     */     public java.util.Iterator<TelemetryImpl.Lineable> iterator()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	org/firstinspires/ftc/robotcore/internal/TelemetryImpl$LineableContainer:this$0	Lorg/firstinspires/ftc/robotcore/internal/TelemetryImpl;
/*     */       //   4: getfield 6	org/firstinspires/ftc/robotcore/internal/TelemetryImpl:theLock	Ljava/lang/Object;
/*     */       //   7: dup
/*     */       //   8: astore_1
/*     */       //   9: monitorenter
/*     */       //   10: aload_0
/*     */       //   11: getfield 5	org/firstinspires/ftc/robotcore/internal/TelemetryImpl$LineableContainer:list	Ljava/util/ArrayList;
/*     */       //   14: invokevirtual 7	java/util/ArrayList:iterator	()Ljava/util/Iterator;
/*     */       //   17: aload_1
/*     */       //   18: monitorexit
/*     */       //   19: areturn
/*     */       //   20: astore_2
/*     */       //   21: aload_1
/*     */       //   22: monitorexit
/*     */       //   23: aload_2
/*     */       //   24: athrow
/*     */       // Line number table:
/*     */       //   Java source line #146	-> byte code offset #0
/*     */       //   Java source line #148	-> byte code offset #10
/*     */       //   Java source line #149	-> byte code offset #20
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	25	0	this	LineableContainer
/*     */       //   8	14	1	Ljava/lang/Object;	Object
/*     */       //   20	4	2	localObject1	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   10	19	20	finally
/*     */       //   20	23	20	finally
/*     */     }
/*     */     
/*     */     Telemetry.Line addLineAfter(TelemetryImpl.Lineable prev, String lineCaption)
/*     */     {
/* 154 */       synchronized (TelemetryImpl.this.theLock)
/*     */       {
/* 156 */         TelemetryImpl.this.onAddData();
/*     */         
/* 158 */         TelemetryImpl.LineImpl result = new TelemetryImpl.LineImpl(TelemetryImpl.this, lineCaption, this);
/* 159 */         int index = prev == null ? this.list.size() : this.list.indexOf(prev) + 1;
/* 160 */         this.list.add(index, result);
/* 161 */         return result;
/*     */       }
/*     */     }
/*     */     
/*     */     Telemetry.Item addItemAfter(TelemetryImpl.Lineable prev, String caption, TelemetryImpl.Value value)
/*     */     {
/* 167 */       synchronized (TelemetryImpl.this.theLock)
/*     */       {
/* 169 */         TelemetryImpl.this.onAddData();
/*     */         
/* 171 */         TelemetryImpl.ItemImpl result = new TelemetryImpl.ItemImpl(TelemetryImpl.this, this, caption, value);
/* 172 */         int index = prev == null ? this.list.size() : this.list.indexOf(prev) + 1;
/* 173 */         this.list.add(index, result);
/* 174 */         return result;
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     boolean isEmpty()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	org/firstinspires/ftc/robotcore/internal/TelemetryImpl$LineableContainer:this$0	Lorg/firstinspires/ftc/robotcore/internal/TelemetryImpl;
/*     */       //   4: getfield 6	org/firstinspires/ftc/robotcore/internal/TelemetryImpl:theLock	Ljava/lang/Object;
/*     */       //   7: dup
/*     */       //   8: astore_1
/*     */       //   9: monitorenter
/*     */       //   10: aload_0
/*     */       //   11: getfield 5	org/firstinspires/ftc/robotcore/internal/TelemetryImpl$LineableContainer:list	Ljava/util/ArrayList;
/*     */       //   14: invokevirtual 16	java/util/ArrayList:isEmpty	()Z
/*     */       //   17: aload_1
/*     */       //   18: monitorexit
/*     */       //   19: ireturn
/*     */       //   20: astore_2
/*     */       //   21: aload_1
/*     */       //   22: monitorexit
/*     */       //   23: aload_2
/*     */       //   24: athrow
/*     */       // Line number table:
/*     */       //   Java source line #180	-> byte code offset #0
/*     */       //   Java source line #182	-> byte code offset #10
/*     */       //   Java source line #183	-> byte code offset #20
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	25	0	this	LineableContainer
/*     */       //   8	14	1	Ljava/lang/Object;	Object
/*     */       //   20	4	2	localObject1	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   10	19	20	finally
/*     */       //   20	23	20	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     int size()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	org/firstinspires/ftc/robotcore/internal/TelemetryImpl$LineableContainer:this$0	Lorg/firstinspires/ftc/robotcore/internal/TelemetryImpl;
/*     */       //   4: getfield 6	org/firstinspires/ftc/robotcore/internal/TelemetryImpl:theLock	Ljava/lang/Object;
/*     */       //   7: dup
/*     */       //   8: astore_1
/*     */       //   9: monitorenter
/*     */       //   10: aload_0
/*     */       //   11: getfield 5	org/firstinspires/ftc/robotcore/internal/TelemetryImpl$LineableContainer:list	Ljava/util/ArrayList;
/*     */       //   14: invokevirtual 11	java/util/ArrayList:size	()I
/*     */       //   17: aload_1
/*     */       //   18: monitorexit
/*     */       //   19: ireturn
/*     */       //   20: astore_2
/*     */       //   21: aload_1
/*     */       //   22: monitorexit
/*     */       //   23: aload_2
/*     */       //   24: athrow
/*     */       // Line number table:
/*     */       //   Java source line #188	-> byte code offset #0
/*     */       //   Java source line #190	-> byte code offset #10
/*     */       //   Java source line #191	-> byte code offset #20
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	25	0	this	LineableContainer
/*     */       //   8	14	1	Ljava/lang/Object;	Object
/*     */       //   20	4	2	localObject1	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   10	19	20	finally
/*     */       //   20	23	20	finally
/*     */     }
/*     */     
/*     */     boolean remove(TelemetryImpl.Lineable lineable)
/*     */     {
/* 196 */       synchronized (TelemetryImpl.this.theLock)
/*     */       {
/* 198 */         for (int i = 0; i < this.list.size(); i++)
/*     */         {
/* 200 */           if (this.list.get(i) == lineable)
/*     */           {
/* 202 */             this.list.remove(i);
/* 203 */             return true;
/*     */           }
/*     */         }
/* 206 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */     boolean removeAllRecurse(Predicate<TelemetryImpl.ItemImpl> predicate)
/*     */     {
/* 212 */       synchronized (TelemetryImpl.this.theLock)
/*     */       {
/* 214 */         boolean result = false;
/* 215 */         for (int i = 0; i < this.list.size();)
/*     */         {
/* 217 */           TelemetryImpl.Lineable cur = (TelemetryImpl.Lineable)this.list.get(i);
/* 218 */           if ((cur instanceof TelemetryImpl.LineImpl))
/*     */           {
/* 220 */             TelemetryImpl.LineImpl line = (TelemetryImpl.LineImpl)cur;
/* 221 */             line.lineables.removeAllRecurse(predicate);
/*     */             
/*     */ 
/* 224 */             if (line.lineables.isEmpty())
/*     */             {
/* 226 */               this.list.remove(i);
/* 227 */               result = true;
/*     */             }
/*     */             else {
/* 230 */               i++;
/*     */             }
/* 232 */           } else if ((cur instanceof TelemetryImpl.ItemImpl))
/*     */           {
/* 234 */             if (predicate.apply((TelemetryImpl.ItemImpl)cur))
/*     */             {
/* 236 */               this.list.remove(i);
/* 237 */               result = true;
/*     */             }
/*     */             else {
/* 240 */               i++;
/*     */             }
/*     */           } else {
/* 243 */             i++;
/*     */           } }
/* 245 */         return result;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected class ItemImpl
/*     */     implements Telemetry.Item, TelemetryImpl.Lineable
/*     */   {
/*     */     final TelemetryImpl.LineableContainer parent;
/*     */     
/* 257 */     String caption = null;
/* 258 */     TelemetryImpl.Value value = null;
/* 259 */     Boolean retained = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     ItemImpl(TelemetryImpl.LineableContainer parent, String caption, TelemetryImpl.Value value)
/*     */     {
/* 267 */       this.parent = parent;
/* 268 */       this.caption = caption;
/* 269 */       this.value = value;
/* 270 */       this.retained = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getComposed(boolean recompose)
/*     */     {
/* 279 */       synchronized (TelemetryImpl.this.theLock)
/*     */       {
/* 281 */         return String.format("%s%s%s", new Object[] { this.caption, TelemetryImpl.this.getCaptionValueSeparator(), this.value.getComposed(recompose) });
/*     */       }
/*     */     }
/*     */     
/*     */     public String getCaption()
/*     */     {
/* 287 */       return this.caption;
/*     */     }
/*     */     
/*     */     public Telemetry.Item setCaption(String caption)
/*     */     {
/* 292 */       this.caption = caption;
/* 293 */       return this;
/*     */     }
/*     */     
/*     */     public boolean isRetained()
/*     */     {
/* 298 */       synchronized (TelemetryImpl.this.theLock)
/*     */       {
/* 300 */         return this.retained != null ? this.retained.booleanValue() : isProducer();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Telemetry.Item setRetained(@Nullable Boolean retained)
/*     */     {
/* 308 */       synchronized (TelemetryImpl.this.theLock)
/*     */       {
/* 310 */         this.retained = retained;
/* 311 */         return this;
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     boolean isProducer()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	org/firstinspires/ftc/robotcore/internal/TelemetryImpl$ItemImpl:this$0	Lorg/firstinspires/ftc/robotcore/internal/TelemetryImpl;
/*     */       //   4: getfield 7	org/firstinspires/ftc/robotcore/internal/TelemetryImpl:theLock	Ljava/lang/Object;
/*     */       //   7: dup
/*     */       //   8: astore_1
/*     */       //   9: monitorenter
/*     */       //   10: aload_0
/*     */       //   11: getfield 4	org/firstinspires/ftc/robotcore/internal/TelemetryImpl$ItemImpl:value	Lorg/firstinspires/ftc/robotcore/internal/TelemetryImpl$Value;
/*     */       //   14: invokevirtual 15	org/firstinspires/ftc/robotcore/internal/TelemetryImpl$Value:isProducer	()Z
/*     */       //   17: aload_1
/*     */       //   18: monitorexit
/*     */       //   19: ireturn
/*     */       //   20: astore_2
/*     */       //   21: aload_1
/*     */       //   22: monitorexit
/*     */       //   23: aload_2
/*     */       //   24: athrow
/*     */       // Line number table:
/*     */       //   Java source line #317	-> byte code offset #0
/*     */       //   Java source line #319	-> byte code offset #10
/*     */       //   Java source line #320	-> byte code offset #20
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	25	0	this	ItemImpl
/*     */       //   8	14	1	Ljava/lang/Object;	Object
/*     */       //   20	4	2	localObject1	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   10	19	20	finally
/*     */       //   20	23	20	finally
/*     */     }
/*     */     
/*     */     void internalSetValue(TelemetryImpl.Value value)
/*     */     {
/* 325 */       synchronized (TelemetryImpl.this.theLock)
/*     */       {
/* 327 */         this.value = value;
/*     */       }
/*     */     }
/*     */     
/*     */     public Telemetry.Item setValue(String format, Object... args)
/*     */     {
/* 333 */       internalSetValue(new TelemetryImpl.Value(TelemetryImpl.this, format, args));
/* 334 */       return this;
/*     */     }
/*     */     
/*     */     public Telemetry.Item setValue(Object value)
/*     */     {
/* 339 */       internalSetValue(new TelemetryImpl.Value(TelemetryImpl.this, value));
/* 340 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Telemetry.Item setValue(Func<T> valueProducer)
/*     */     {
/* 345 */       internalSetValue(new TelemetryImpl.Value(TelemetryImpl.this, valueProducer));
/* 346 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Telemetry.Item setValue(String format, Func<T> valueProducer)
/*     */     {
/* 351 */       internalSetValue(new TelemetryImpl.Value(TelemetryImpl.this, format, valueProducer));
/* 352 */       return this;
/*     */     }
/*     */     
/*     */     public Telemetry.Item addData(String caption, String format, Object... args)
/*     */     {
/* 357 */       return this.parent.addItemAfter(this, caption, new TelemetryImpl.Value(TelemetryImpl.this, format, args));
/*     */     }
/*     */     
/*     */     public Telemetry.Item addData(String caption, Object value)
/*     */     {
/* 362 */       return this.parent.addItemAfter(this, caption, new TelemetryImpl.Value(TelemetryImpl.this, value));
/*     */     }
/*     */     
/*     */     public <T> Telemetry.Item addData(String caption, Func<T> valueProducer)
/*     */     {
/* 367 */       return this.parent.addItemAfter(this, caption, new TelemetryImpl.Value(TelemetryImpl.this, valueProducer));
/*     */     }
/*     */     
/*     */     public <T> Telemetry.Item addData(String caption, String format, Func<T> valueProducer)
/*     */     {
/* 372 */       return this.parent.addItemAfter(this, caption, new TelemetryImpl.Value(TelemetryImpl.this, format, valueProducer));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected class LineImpl
/*     */     implements Telemetry.Line, TelemetryImpl.Lineable
/*     */   {
/*     */     final TelemetryImpl.LineableContainer parent;
/*     */     
/*     */ 
/*     */     String lineCaption;
/*     */     
/*     */ 
/*     */     TelemetryImpl.LineableContainer lineables;
/*     */     
/*     */ 
/*     */     LineImpl(String lineCaption, TelemetryImpl.LineableContainer parent)
/*     */     {
/* 392 */       this.parent = parent;
/* 393 */       this.lineCaption = lineCaption;
/* 394 */       this.lineables = new TelemetryImpl.LineableContainer(TelemetryImpl.this);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getComposed(boolean recompose)
/*     */     {
/* 403 */       StringBuilder result = new StringBuilder();
/* 404 */       result.append(this.lineCaption);
/* 405 */       boolean firstTime = true;
/* 406 */       for (TelemetryImpl.Lineable lineable : this.lineables)
/*     */       {
/* 408 */         if (!firstTime)
/*     */         {
/* 410 */           result.append(TelemetryImpl.this.getItemSeparator());
/*     */         }
/* 412 */         result.append(lineable.getComposed(recompose));
/* 413 */         firstTime = false;
/*     */       }
/* 415 */       return result.toString();
/*     */     }
/*     */     
/*     */     public Telemetry.Item addData(String caption, String format, Object... args)
/*     */     {
/* 420 */       return this.lineables.addItemAfter(null, caption, new TelemetryImpl.Value(TelemetryImpl.this, format, args));
/*     */     }
/*     */     
/*     */     public Telemetry.Item addData(String caption, Object value)
/*     */     {
/* 425 */       return this.lineables.addItemAfter(null, caption, new TelemetryImpl.Value(TelemetryImpl.this, value));
/*     */     }
/*     */     
/*     */     public <T> Telemetry.Item addData(String caption, Func<T> valueProducer)
/*     */     {
/* 430 */       return this.lineables.addItemAfter(null, caption, new TelemetryImpl.Value(TelemetryImpl.this, valueProducer));
/*     */     }
/*     */     
/*     */     public <T> Telemetry.Item addData(String caption, String format, Func<T> valueProducer)
/*     */     {
/* 435 */       return this.lineables.addItemAfter(null, caption, new TelemetryImpl.Value(TelemetryImpl.this, format, valueProducer));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected class LogImpl
/*     */     implements Telemetry.Log
/*     */   {
/* 445 */     final List<String> entries = new ArrayList();
/* 446 */     int capacity = 9;
/* 447 */     Telemetry.Log.DisplayOrder displayOrder = Telemetry.Log.DisplayOrder.OLDEST_FIRST;
/* 448 */     boolean isDirty = false;
/*     */     
/*     */ 
/*     */     protected LogImpl() {}
/*     */     
/*     */ 
/*     */     void markDirty()
/*     */     {
/* 456 */       this.isDirty = true;
/*     */     }
/*     */     
/*     */     void markClean()
/*     */     {
/* 461 */       this.isDirty = false;
/*     */     }
/*     */     
/*     */     boolean isDirty()
/*     */     {
/* 466 */       return this.isDirty;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Object getLock()
/*     */     {
/* 476 */       return TelemetryImpl.this;
/*     */     }
/*     */     
/*     */     int size()
/*     */     {
/* 481 */       return this.entries.size();
/*     */     }
/*     */     
/*     */     String get(int index)
/*     */     {
/* 486 */       return (String)this.entries.get(index);
/*     */     }
/*     */     
/*     */     void prune()
/*     */     {
/* 491 */       synchronized (getLock())
/*     */       {
/* 493 */         while ((this.entries.size() > this.capacity) && (this.entries.size() > 0))
/*     */         {
/* 495 */           this.entries.remove(0);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getCapacity()
/*     */     {
/* 506 */       return this.capacity;
/*     */     }
/*     */     
/*     */     public void setCapacity(int capacity)
/*     */     {
/* 511 */       synchronized (getLock())
/*     */       {
/* 513 */         this.capacity = capacity;
/* 514 */         prune();
/*     */       }
/*     */     }
/*     */     
/*     */     public Telemetry.Log.DisplayOrder getDisplayOrder()
/*     */     {
/* 520 */       return this.displayOrder;
/*     */     }
/*     */     
/*     */     public void setDisplayOrder(Telemetry.Log.DisplayOrder displayOrder)
/*     */     {
/* 525 */       synchronized (getLock())
/*     */       {
/* 527 */         this.displayOrder = displayOrder;
/*     */       }
/*     */     }
/*     */     
/*     */     public void add(String format, Object... args)
/*     */     {
/* 533 */       synchronized (getLock())
/*     */       {
/* 535 */         String datum = String.format(format, args);
/* 536 */         this.entries.add(datum);
/* 537 */         markDirty();
/* 538 */         prune();
/*     */         
/*     */ 
/* 541 */         TelemetryImpl.this.tryUpdate(TelemetryImpl.UpdateReason.LOG);
/*     */       }
/*     */     }
/*     */     
/*     */     public void add(String entry)
/*     */     {
/* 547 */       add("%s", new Object[] { entry });
/*     */     }
/*     */     
/*     */     public void clear()
/*     */     {
/* 552 */       synchronized (getLock())
/*     */       {
/* 554 */         this.entries.clear();
/* 555 */         markDirty();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 564 */   protected final Object theLock = new Object();
/*     */   
/*     */   protected LineableContainer lines;
/*     */   
/*     */   protected List<String> composedLines;
/*     */   
/*     */   protected List<Runnable> actions;
/*     */   
/*     */   protected LogImpl log;
/*     */   protected ElapsedTime transmissionTimer;
/*     */   protected boolean isDirty;
/*     */   protected boolean clearOnAdd;
/*     */   protected OpMode opMode;
/*     */   protected boolean isAutoClear;
/*     */   protected int msTransmissionInterval;
/*     */   protected String captionValueSeparator;
/*     */   protected String itemSeparator;
/*     */   
/*     */   public TelemetryImpl(OpMode opMode)
/*     */   {
/* 584 */     this.opMode = opMode;
/* 585 */     resetTelemetryForOpMode();
/*     */   }
/*     */   
/*     */ 
/*     */   public void resetTelemetryForOpMode()
/*     */   {
/* 591 */     this.lines = new LineableContainer();
/* 592 */     this.composedLines = new ArrayList();
/* 593 */     this.actions = new LinkedList();
/* 594 */     this.log = new LogImpl();
/* 595 */     this.transmissionTimer = new ElapsedTime();
/* 596 */     this.isDirty = false;
/* 597 */     this.clearOnAdd = false;
/* 598 */     this.isAutoClear = true;
/* 599 */     this.msTransmissionInterval = 250;
/* 600 */     this.captionValueSeparator = " : ";
/* 601 */     this.itemSeparator = " | ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void markDirty()
/*     */   {
/* 610 */     this.isDirty = true;
/*     */   }
/*     */   
/*     */   void markClean()
/*     */   {
/* 615 */     this.isDirty = false;
/*     */   }
/*     */   
/*     */   boolean isDirty()
/*     */   {
/* 620 */     return this.isDirty;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static String getKey(int iLine)
/*     */   {
/* 632 */     return String.format("\000%c", new Object[] { Integer.valueOf(384 + iLine) });
/*     */   }
/*     */   
/*     */   public boolean update()
/*     */   {
/* 637 */     return tryUpdate(UpdateReason.USER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 642 */   public boolean tryUpdateIfDirty() { return tryUpdate(UpdateReason.IFDIRTY); }
/*     */   
/*     */   protected static enum UpdateReason {
/* 645 */     USER,  LOG,  IFDIRTY;
/*     */     
/*     */     private UpdateReason() {} }
/*     */   
/* 649 */   protected boolean tryUpdate(UpdateReason updateReason) { synchronized (this.theLock)
/*     */     {
/* 651 */       boolean result = false;
/*     */       
/* 653 */       boolean intervalElapsed = this.transmissionTimer.milliseconds() > this.msTransmissionInterval;
/*     */       
/* 655 */       boolean wantToTransmit = (updateReason == UpdateReason.USER) || (updateReason == UpdateReason.LOG) || ((updateReason == UpdateReason.IFDIRTY) && ((isDirty()) || (this.log.isDirty())));
/*     */       
/*     */ 
/*     */ 
/* 659 */       boolean recompose = (updateReason == UpdateReason.USER) || (isDirty());
/*     */       
/*     */ 
/* 662 */       if ((intervalElapsed) && (wantToTransmit))
/*     */       {
/*     */ 
/* 665 */         for (Runnable action : this.actions)
/*     */         {
/* 667 */           action.run();
/*     */         }
/*     */         
/*     */ 
/* 671 */         TelemetryMessage transmitter = new TelemetryMessage();
/* 672 */         saveToTransmitter(recompose, transmitter);
/*     */         
/*     */ 
/* 675 */         if (transmitter.hasData())
/*     */         {
/* 677 */           OpModeManagerImpl.updateTelemetryNow(this.opMode, transmitter);
/*     */         }
/*     */         
/*     */ 
/* 681 */         this.log.markClean();
/* 682 */         markClean();
/*     */         
/*     */ 
/* 685 */         this.transmissionTimer.reset();
/* 686 */         result = true;
/*     */       }
/* 688 */       else if (updateReason == UpdateReason.USER)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 693 */         markDirty();
/*     */       }
/*     */       
/*     */ 
/* 697 */       if (updateReason == UpdateReason.USER)
/*     */       {
/*     */ 
/*     */ 
/* 701 */         this.clearOnAdd = isAutoClear();
/*     */       }
/*     */       
/* 704 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void saveToTransmitter(boolean recompose, TelemetryMessage transmitter)
/*     */   {
/* 710 */     transmitter.setSorted(false);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 716 */     if (recompose)
/*     */     {
/* 718 */       this.composedLines = new ArrayList();
/* 719 */       for (Lineable lineable : this.lines)
/*     */       {
/* 721 */         this.composedLines.add(lineable.getComposed(recompose));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 726 */     int iLine = 0;
/* 727 */     for (iLine = 0; iLine < this.composedLines.size(); iLine++)
/*     */     {
/* 729 */       transmitter.addData(getKey(iLine), (String)this.composedLines.get(iLine));
/*     */     }
/*     */     
/*     */ 
/* 733 */     int size = this.log.size();
/* 734 */     for (int i = 0; i < size; i++)
/*     */     {
/* 736 */       String s = this.log.getDisplayOrder() == Telemetry.Log.DisplayOrder.OLDEST_FIRST ? this.log.get(i) : this.log.get(size - 1 - i);
/*     */       
/*     */ 
/* 739 */       transmitter.addData(getKey(iLine), s);
/* 740 */       iLine++;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Telemetry.Log log()
/*     */   {
/* 750 */     return this.log;
/*     */   }
/*     */   
/*     */   public boolean isAutoClear()
/*     */   {
/* 755 */     return this.isAutoClear;
/*     */   }
/*     */   
/*     */   public void setAutoClear(boolean autoClear)
/*     */   {
/* 760 */     synchronized (this.theLock)
/*     */     {
/* 762 */       this.isAutoClear = autoClear;
/*     */     }
/*     */   }
/*     */   
/*     */   public int getMsTransmissionInterval()
/*     */   {
/* 768 */     return this.msTransmissionInterval;
/*     */   }
/*     */   
/*     */   public void setMsTransmissionInterval(int msTransmissionInterval)
/*     */   {
/* 773 */     synchronized (this.theLock)
/*     */     {
/* 775 */       this.msTransmissionInterval = msTransmissionInterval;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getItemSeparator()
/*     */   {
/* 781 */     return this.itemSeparator;
/*     */   }
/*     */   
/*     */   public void setItemSeparator(String itemSeparator)
/*     */   {
/* 786 */     synchronized (this.theLock)
/*     */     {
/* 788 */       this.itemSeparator = itemSeparator;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getCaptionValueSeparator()
/*     */   {
/* 794 */     return this.captionValueSeparator;
/*     */   }
/*     */   
/*     */   public void setCaptionValueSeparator(String captionValueSeparator)
/*     */   {
/* 799 */     synchronized (this.theLock)
/*     */     {
/* 801 */       this.captionValueSeparator = captionValueSeparator;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object addAction(Runnable action)
/*     */   {
/* 811 */     synchronized (this.theLock)
/*     */     {
/* 813 */       this.actions.add(action);
/* 814 */       return action;
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean removeAction(Object token)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 3	org/firstinspires/ftc/robotcore/internal/TelemetryImpl:theLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 14	org/firstinspires/ftc/robotcore/internal/TelemetryImpl:actions	Ljava/util/List;
/*     */     //   11: aload_1
/*     */     //   12: checkcast 42	java/lang/Runnable
/*     */     //   15: invokeinterface 68 2 0
/*     */     //   20: aload_2
/*     */     //   21: monitorexit
/*     */     //   22: ireturn
/*     */     //   23: astore_3
/*     */     //   24: aload_2
/*     */     //   25: monitorexit
/*     */     //   26: aload_3
/*     */     //   27: athrow
/*     */     // Line number table:
/*     */     //   Java source line #820	-> byte code offset #0
/*     */     //   Java source line #822	-> byte code offset #7
/*     */     //   Java source line #823	-> byte code offset #23
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	28	0	this	TelemetryImpl
/*     */     //   0	28	1	token	Object
/*     */     //   5	20	2	Ljava/lang/Object;	Object
/*     */     //   23	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	22	23	finally
/*     */     //   23	26	23	finally
/*     */   }
/*     */   
/*     */   public Telemetry.Item addData(String caption, String format, Object... args)
/*     */   {
/* 828 */     return this.lines.addItemAfter(null, caption, new Value(format, args));
/*     */   }
/*     */   
/*     */   public Telemetry.Item addData(String caption, Object value) {
/* 832 */     return this.lines.addItemAfter(null, caption, new Value(value));
/*     */   }
/*     */   
/*     */   public <T> Telemetry.Item addData(String caption, Func<T> valueProducer) {
/* 836 */     return this.lines.addItemAfter(null, caption, new Value(valueProducer));
/*     */   }
/*     */   
/*     */   public <T> Telemetry.Item addData(String caption, String format, Func<T> valueProducer) {
/* 840 */     return this.lines.addItemAfter(null, caption, new Value(format, valueProducer));
/*     */   }
/*     */   
/*     */   public Telemetry.Line addLine()
/*     */   {
/* 845 */     return this.lines.addLineAfter(null, "");
/*     */   }
/*     */   
/*     */   public Telemetry.Line addLine(String lineCaption) {
/* 849 */     return this.lines.addLineAfter(null, lineCaption);
/*     */   }
/*     */   
/*     */   public boolean removeItem(Telemetry.Item item)
/*     */   {
/* 854 */     if ((item instanceof ItemImpl))
/*     */     {
/* 856 */       ItemImpl itemImpl = (ItemImpl)item;
/* 857 */       return itemImpl.parent.remove(itemImpl);
/*     */     }
/* 859 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removeLine(Telemetry.Line line)
/*     */   {
/* 864 */     if ((line instanceof LineImpl))
/*     */     {
/* 866 */       LineImpl lineImpl = (LineImpl)line;
/* 867 */       return lineImpl.parent.remove(lineImpl);
/*     */     }
/* 869 */     return false;
/*     */   }
/*     */   
/*     */   protected void onAddData()
/*     */   {
/* 874 */     if (this.clearOnAdd)
/*     */     {
/* 876 */       clear();
/* 877 */       this.clearOnAdd = false;
/*     */     }
/*     */     
/*     */ 
/* 881 */     markClean();
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 886 */     synchronized (this.theLock)
/*     */     {
/* 888 */       this.clearOnAdd = false;
/* 889 */       markClean();
/*     */       
/* 891 */       this.lines.removeAllRecurse(new Predicate()
/*     */       {
/*     */         public boolean apply(TelemetryImpl.ItemImpl item)
/*     */         {
/* 895 */           return !item.isRetained();
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearAll()
/*     */   {
/* 903 */     synchronized (this.theLock)
/*     */     {
/* 905 */       this.clearOnAdd = false;
/* 906 */       markClean();
/*     */       
/* 908 */       this.actions.clear();
/* 909 */       this.lines.removeAllRecurse(new Predicate()
/*     */       {
/*     */         public boolean apply(TelemetryImpl.ItemImpl item)
/*     */         {
/* 913 */           return true;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\TelemetryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */