/*     */ package com.qualcomm.robotcore.eventloop.opmode;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.util.Log;
/*     */ import com.qualcomm.robotcore.util.ClassFilter;
/*     */ import com.qualcomm.robotcore.util.ClassUtil;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotatedOpModeRegistrar
/*     */   implements ClassFilter
/*     */ {
/*     */   public static final String TAG = "OpmodeRegistration";
/*     */   final OpModeManager opModeManager;
/*  66 */   final String defaultOpModeGroupName = "$$$$$$$";
/*  67 */   private static final HashMap<String, LinkedList<OpModeAndMeta>> opModeGroups = new HashMap();
/*  68 */   private static final HashSet<Class> classesSeen = new HashSet();
/*  69 */   private static final HashMap<Class, OpModeMeta> classNameOverrides = new HashMap();
/*     */   
/*  71 */   private static final List<Class> annotatedOpModeList = new LinkedList();
/*  72 */   private static final Set<Method> registrarMethods = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Context context;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void register(OpModeManager manager)
/*     */   {
/*  87 */     AnnotatedOpModeRegistrar registrar = null;
/*     */     try {
/*  89 */       registrar = new AnnotatedOpModeRegistrar(manager);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  93 */       Log.wtf("OpmodeRegistration", e);
/*  94 */       registrar = null;
/*     */     }
/*     */     
/*  97 */     if (registrar != null) {
/*  98 */       registrar.doRegistration();
/*     */     }
/*     */   }
/*     */   
/*     */   public AnnotatedOpModeRegistrar() {
/* 103 */     this.opModeManager = null;
/*     */   }
/*     */   
/*     */   private AnnotatedOpModeRegistrar(OpModeManager opModeManager)
/*     */   {
/* 108 */     this.opModeManager = opModeManager;
/* 109 */     this.context = AppUtil.getInstance().getApplication();
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
/*     */   void doRegistration()
/*     */   {
/* 122 */     processAnnotatedStaticMethods();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 127 */     for (Class clazz : annotatedOpModeList)
/*     */     {
/* 129 */       addAnnotatedOpMode(clazz);
/*     */     }
/*     */     
/*     */ 
/* 133 */     List<OpModeAndMeta> opModesToRegister = new LinkedList();
/* 134 */     for (LinkedList<OpModeAndMeta> opModeList : opModeGroups.values())
/*     */     {
/* 136 */       for (OpModeAndMeta opMode : opModeList)
/*     */       {
/* 138 */         opModesToRegister.add(opMode);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 143 */     for (OpModeAndMeta opModeAndMeta : opModesToRegister)
/*     */     {
/* 145 */       String name = getOpModeName(opModeAndMeta);
/* 146 */       this.opModeManager.register(OpModeMeta.forName(name, opModeAndMeta.meta), opModeAndMeta.clazz);
/*     */     }
/*     */   }
/*     */   
/*     */   void reportOpModeConfigurationError(String format, Object... args)
/*     */   {
/* 152 */     String message = String.format(format, args);
/*     */     
/* 154 */     Log.w("OpmodeRegistration", String.format("configuration error: %s", new Object[] { message }));
/*     */     
/* 156 */     RobotLog.setGlobalErrorMsg(message);
/*     */   }
/*     */   
/*     */ 
/*     */   boolean checkOpModeClassConstraints(Class clazz, String opModeName)
/*     */   {
/* 162 */     if (!isOpMode(clazz))
/*     */     {
/* 164 */       reportOpModeConfigurationError("'%s' class doesn't inherit from the class 'OpMode'", new Object[] { clazz.getSimpleName() });
/* 165 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 170 */     if (!Modifier.isPublic(clazz.getModifiers()))
/*     */     {
/* 172 */       reportOpModeConfigurationError("'%s' class is not declared 'public'", new Object[] { clazz.getSimpleName() });
/* 173 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 177 */     if (opModeName == null)
/*     */     {
/* 179 */       opModeName = getOpModeName(clazz);
/*     */     }
/* 181 */     if (!isLegalOpModeName(opModeName))
/*     */     {
/* 183 */       reportOpModeConfigurationError("\"%s\" is not a legal OpMode name", new Object[] { opModeName });
/* 184 */       return false;
/*     */     }
/*     */     
/* 187 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void processAnnotatedStaticMethods()
/*     */   {
/* 193 */     AnnotationOpModeManagerImpl manager = new AnnotationOpModeManagerImpl();
/* 194 */     for (Method method : registrarMethods)
/*     */     {
/*     */       try
/*     */       {
/* 198 */         if (getParameterCount(method) == 1) {
/* 199 */           method.invoke(null, new Object[] { manager });
/* 200 */         } else if (getParameterCount(method) == 2) {
/* 201 */           method.invoke(null, new Object[] { this.context, manager });
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int getParameterCount(Method method)
/*     */   {
/* 212 */     Class<?>[] parameters = method.getParameterTypes();
/* 213 */     return parameters.length;
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
/*     */   public void filter(Class clazz)
/*     */   {
/* 237 */     checkForOpModeRegistrar(clazz);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 242 */     boolean isTeleOp = clazz.isAnnotationPresent(TeleOp.class);
/* 243 */     boolean isAutonomous = clazz.isAnnotationPresent(Autonomous.class);
/*     */     
/*     */ 
/* 246 */     if ((!isTeleOp) && (!isAutonomous)) {
/* 247 */       return;
/*     */     }
/*     */     
/* 250 */     if ((isTeleOp) && (isAutonomous))
/*     */     {
/* 252 */       reportOpModeConfigurationError("'%s' class is annotated both as 'TeleOp' and 'Autonomous'; please choose at most one", new Object[] { clazz.getSimpleName() });
/* 253 */       return;
/*     */     }
/*     */     
/*     */ 
/* 257 */     if (!checkOpModeClassConstraints(clazz, null)) {
/* 258 */       return;
/*     */     }
/*     */     
/* 261 */     if (clazz.isAnnotationPresent(Disabled.class)) {
/* 262 */       return;
/*     */     }
/* 264 */     annotatedOpModeList.add(clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void checkForOpModeRegistrar(Class clazz)
/*     */   {
/* 272 */     List<Method> methods = ClassUtil.getDeclaredMethodsIncludingSuper(clazz);
/* 273 */     for (Method method : methods)
/*     */     {
/* 275 */       int requiredModifiers = 9;
/* 276 */       int prohibitedModifiers = 1024;
/* 277 */       if (((method.getModifiers() & requiredModifiers) == requiredModifiers) && ((method.getModifiers() & prohibitedModifiers) == 0))
/*     */       {
/*     */ 
/* 280 */         Class<?>[] parameters = method.getParameterTypes();
/*     */         
/* 282 */         if (method.isAnnotationPresent(OpModeRegistrar.class))
/*     */         {
/*     */ 
/* 285 */           if ((getParameterCount(method) == 1) || (getParameterCount(method) == 2))
/* 286 */             registrarMethods.add(method); }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   class AnnotationOpModeManagerImpl implements AnnotatedOpModeManager {
/*     */     AnnotationOpModeManagerImpl() {}
/*     */     
/*     */     public void register(Class clazz) {
/* 295 */       if (AnnotatedOpModeRegistrar.this.checkOpModeClassConstraints(clazz, null))
/*     */       {
/* 297 */         AnnotatedOpModeRegistrar.this.addAnnotatedOpMode(clazz);
/*     */       }
/*     */     }
/*     */     
/*     */     public void register(String name, Class clazz)
/*     */     {
/* 303 */       if (AnnotatedOpModeRegistrar.this.checkOpModeClassConstraints(clazz, name))
/*     */       {
/* 305 */         AnnotatedOpModeRegistrar.this.addUserNamedOpMode(clazz, new OpModeMeta(name));
/*     */       }
/*     */     }
/*     */     
/*     */     public void register(OpModeMeta meta, Class clazz)
/*     */     {
/* 311 */       if (AnnotatedOpModeRegistrar.this.checkOpModeClassConstraints(clazz, meta.name))
/*     */       {
/* 313 */         AnnotatedOpModeRegistrar.this.addUserNamedOpMode(clazz, meta);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void register(String name, OpMode opModeInstance)
/*     */     {
/* 320 */       AnnotatedOpModeRegistrar.this.opModeManager.register(name, opModeInstance);
/* 321 */       Log.d("OpmodeRegistration", String.format("registered instance {%s} as {%s}", new Object[] { opModeInstance.toString(), name }));
/*     */     }
/*     */     
/*     */ 
/*     */     public void register(OpModeMeta meta, OpMode opModeInstance)
/*     */     {
/* 327 */       AnnotatedOpModeRegistrar.this.opModeManager.register(meta, opModeInstance);
/* 328 */       Log.d("OpmodeRegistration", String.format("registered instance {%s} as {%s}", new Object[] { opModeInstance.toString(), meta.name }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void addAnnotatedOpMode(Class<OpMode> clazz)
/*     */   {
/* 335 */     if (clazz.isAnnotationPresent(TeleOp.class))
/*     */     {
/* 337 */       Annotation annotation = clazz.getAnnotation(TeleOp.class);
/* 338 */       String groupName = ((TeleOp)annotation).group();
/* 339 */       addOpModeWithGroupName(clazz, OpModeMeta.Flavor.TELEOP, groupName);
/*     */     }
/*     */     
/* 342 */     if (clazz.isAnnotationPresent(Autonomous.class))
/*     */     {
/* 344 */       Annotation annotation = clazz.getAnnotation(Autonomous.class);
/* 345 */       String groupName = ((Autonomous)annotation).group();
/* 346 */       addOpModeWithGroupName(clazz, OpModeMeta.Flavor.AUTONOMOUS, groupName);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addOpModeWithGroupName(Class<OpMode> clazz, OpModeMeta.Flavor flavor, String groupName)
/*     */   {
/* 352 */     OpModeAndMeta meta = new OpModeAndMeta(new OpModeMeta(flavor, groupName), clazz);
/* 353 */     if (groupName.equals("")) {
/* 354 */       addToOpModeGroup("$$$$$$$", meta);
/*     */     } else {
/* 356 */       addToOpModeGroup(groupName, meta);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void addUserNamedOpMode(Class<OpMode> clazz, OpModeMeta meta)
/*     */   {
/* 363 */     addToOpModeGroup("$$$$$$$", new OpModeAndMeta(meta, clazz));
/* 364 */     classNameOverrides.put(clazz, meta);
/*     */   }
/*     */   
/*     */ 
/*     */   private void addToOpModeGroup(String groupName, OpModeAndMeta opModeAndMetaData)
/*     */   {
/* 370 */     Class clazz = opModeAndMetaData.clazz;
/* 371 */     opModeAndMetaData = new OpModeAndMeta(OpModeMeta.forGroup(groupName, opModeAndMetaData.meta), clazz);
/*     */     
/*     */ 
/* 374 */     if (!classesSeen.contains(clazz))
/*     */     {
/* 376 */       classesSeen.add(clazz);
/*     */       
/* 378 */       if (opModeGroups.containsKey(groupName))
/*     */       {
/* 380 */         ((LinkedList)opModeGroups.get(groupName)).add(opModeAndMetaData);
/*     */       }
/*     */       else
/*     */       {
/* 384 */         LinkedList<OpModeAndMeta> temp = new LinkedList();
/* 385 */         temp.add(opModeAndMetaData);
/* 386 */         opModeGroups.put(groupName, temp);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getOpModeName(OpModeAndMeta opModeAndMetaData)
/*     */   {
/* 397 */     return getOpModeName(opModeAndMetaData.clazz);
/*     */   }
/*     */   
/*     */ 
/*     */   private String getOpModeName(Class<OpMode> opMode)
/*     */   {
/*     */     String name;
/*     */     String name;
/* 405 */     if (classNameOverrides.containsKey(opMode)) {
/* 406 */       name = ((OpModeMeta)classNameOverrides.get(opMode)).name; } else { String name;
/* 407 */       if (opMode.isAnnotationPresent(TeleOp.class)) {
/* 408 */         name = ((TeleOp)opMode.getAnnotation(TeleOp.class)).name(); } else { String name;
/* 409 */         if (opMode.isAnnotationPresent(Autonomous.class)) {
/* 410 */           name = ((Autonomous)opMode.getAnnotation(Autonomous.class)).name();
/*     */         } else
/* 412 */           name = opMode.getSimpleName();
/*     */       } }
/* 414 */     if (name.trim().equals("")) {
/* 415 */       name = opMode.getSimpleName();
/*     */     }
/* 417 */     return name;
/*     */   }
/*     */   
/*     */   private boolean isLegalOpModeName(String name)
/*     */   {
/* 422 */     if (name == null)
/* 423 */       return false;
/* 424 */     if ((name.equals("$Stop$Robot$")) || (name.trim().equals("")))
/*     */     {
/* 426 */       return false;
/*     */     }
/* 428 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isOpMode(Class clazz)
/*     */   {
/* 433 */     return ClassUtil.inheritsFrom(clazz, OpMode.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\opmode\AnnotatedOpModeRegistrar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */