/*     */ package org.firstinspires.ftc.robotcore.internal.opengl;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.opengl.GLSurfaceView;
/*     */ import android.opengl.GLSurfaceView.EGLConfigChooser;
/*     */ import android.opengl.GLSurfaceView.EGLContextFactory;
/*     */ import android.view.SurfaceHolder;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import javax.microedition.khronos.egl.EGL10;
/*     */ import javax.microedition.khronos.egl.EGLConfig;
/*     */ import javax.microedition.khronos.egl.EGLContext;
/*     */ import javax.microedition.khronos.egl.EGLDisplay;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoConfigGLSurfaceView
/*     */   extends GLSurfaceView
/*     */ {
/*     */   private static final String LOGTAG = "LocalizationGLView";
/*     */   
/*     */   public AutoConfigGLSurfaceView(Context context)
/*     */   {
/*  41 */     super(context);
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
/*     */   public void init(boolean translucent, int depth, int stencil)
/*     */   {
/*  59 */     if (translucent)
/*     */     {
/*  61 */       getHolder().setFormat(-3);
/*     */     }
/*     */     
/*     */ 
/*  65 */     setEGLContextFactory(new ContextFactory(null));
/*     */     
/*     */ 
/*     */ 
/*  69 */     setEGLConfigChooser(translucent ? new ConfigChooser(8, 8, 8, 8, depth, stencil) : new ConfigChooser(5, 6, 5, 0, depth, stencil));
/*     */   }
/*     */   
/*     */   private static class ContextFactory
/*     */     implements GLSurfaceView.EGLContextFactory
/*     */   {
/*  75 */     private static int EGL_CONTEXT_CLIENT_VERSION = 12440;
/*     */     
/*     */ 
/*     */ 
/*     */     public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig)
/*     */     {
/*  81 */       AutoConfigGLSurfaceView.checkEglError("Before eglCreateContext", egl);
/*  82 */       int[] attrib_list_gl20 = { EGL_CONTEXT_CLIENT_VERSION, 2, 12344 };
/*  83 */       EGLContext context = egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list_gl20);
/*     */       
/*  85 */       AutoConfigGLSurfaceView.checkEglError("After eglCreateContext", egl);
/*  86 */       return context;
/*     */     }
/*     */     
/*     */ 
/*     */     public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context)
/*     */     {
/*  92 */       egl.eglDestroyContext(display, context);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void checkEglError(String prompt, EGL10 egl)
/*     */   {
/*     */     int error;
/* 100 */     while ((error = egl.eglGetError()) != 12288)
/*     */     {
/* 102 */       RobotLog.ee("LocalizationGLView", "%s: EGL error: 0x%x", new Object[] { prompt, Integer.valueOf(error) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class ConfigChooser
/*     */     implements GLSurfaceView.EGLConfigChooser
/*     */   {
/*     */     protected int mRedSize;
/*     */     
/*     */     protected int mGreenSize;
/*     */     
/*     */     protected int mBlueSize;
/*     */     
/*     */     protected int mAlphaSize;
/*     */     
/*     */     protected int mDepthSize;
/*     */     protected int mStencilSize;
/* 120 */     private int[] mValue = new int[1];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConfigChooser(int r, int g, int b, int a, int depth, int stencil)
/*     */     {
/* 128 */       this.mRedSize = r;
/* 129 */       this.mGreenSize = g;
/* 130 */       this.mBlueSize = b;
/* 131 */       this.mAlphaSize = a;
/* 132 */       this.mDepthSize = depth;
/* 133 */       this.mStencilSize = stencil;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private EGLConfig getMatchingConfig(EGL10 egl, EGLDisplay display, int[] configAttribs)
/*     */     {
/* 143 */       int[] num_config = new int[1];
/* 144 */       egl.eglChooseConfig(display, configAttribs, null, 0, num_config);
/*     */       
/* 146 */       int numConfigs = num_config[0];
/* 147 */       if (numConfigs <= 0) {
/* 148 */         throw new IllegalArgumentException("No matching EGL configs");
/*     */       }
/*     */       
/* 151 */       EGLConfig[] configs = new EGLConfig[numConfigs];
/* 152 */       egl.eglChooseConfig(display, configAttribs, configs, numConfigs, num_config);
/*     */       
/*     */ 
/* 155 */       return chooseConfig(egl, display, configs);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display)
/*     */     {
/* 164 */       int EGL_OPENGL_ES2_BIT = 4;
/* 165 */       int[] s_configAttribs_gl20 = { 12324, 4, 12323, 4, 12322, 4, 12352, 4, 12344 };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 170 */       return getMatchingConfig(egl, display, s_configAttribs_gl20);
/*     */     }
/*     */     
/*     */     public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs)
/*     */     {
/* 175 */       for (EGLConfig config : configs)
/*     */       {
/* 177 */         int d = findConfigAttrib(egl, display, config, 12325, 0);
/* 178 */         int s = findConfigAttrib(egl, display, config, 12326, 0);
/*     */         
/*     */ 
/* 181 */         if ((d >= this.mDepthSize) && (s >= this.mStencilSize))
/*     */         {
/*     */ 
/*     */ 
/* 185 */           int r = findConfigAttrib(egl, display, config, 12324, 0);
/* 186 */           int g = findConfigAttrib(egl, display, config, 12323, 0);
/* 187 */           int b = findConfigAttrib(egl, display, config, 12322, 0);
/* 188 */           int a = findConfigAttrib(egl, display, config, 12321, 0);
/*     */           
/* 190 */           if ((r == this.mRedSize) && (g == this.mGreenSize) && (b == this.mBlueSize) && (a == this.mAlphaSize))
/* 191 */             return config;
/*     */         }
/*     */       }
/* 194 */       return null;
/*     */     }
/*     */     
/*     */     private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue)
/*     */     {
/* 199 */       if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
/* 200 */         return this.mValue[0];
/*     */       }
/* 202 */       return defaultValue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\opengl\AutoConfigGLSurfaceView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */