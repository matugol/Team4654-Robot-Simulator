/*     */ package com.qualcomm.robotcore.hardware.configuration;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.support.annotation.NonNull;
/*     */ import com.google.gson.annotations.Expose;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.HardwareDevice;
/*     */ import com.qualcomm.robotcore.hardware.I2cController;
/*     */ import com.qualcomm.robotcore.hardware.I2cDevice;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
/*     */ import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
/*     */ import com.qualcomm.robotcore.util.ClassUtil;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UserSensorType
/*     */   implements ConfigurationType, Serializable
/*     */ {
/*     */   @Expose
/*     */   protected String xmlTag;
/*     */   @Expose
/*     */   protected Flavor flavor;
/*     */   @Expose
/*     */   protected String name;
/*     */   @Expose
/*     */   protected String description;
/*     */   protected Class<? extends HardwareDevice> clazz;
/*     */   protected List<Constructor> constructors;
/*     */   
/*     */   public static enum Flavor
/*     */   {
/*  68 */     I2C;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Flavor() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */   protected static final Class<?>[] ctorI2cDeviceSynchSimple = { I2cDeviceSynchSimple.class };
/*  82 */   protected static final Class<?>[] ctorI2cDeviceSynch = { I2cDeviceSynch.class };
/*  83 */   protected static final Class<?>[] ctorI2cDevice = { I2cDevice.class };
/*  84 */   protected static final Class<?>[] ctorI2cControllerPort = { I2cController.class, Integer.TYPE };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UserSensorType(Flavor flavor, Class<? extends HardwareDevice> clazz, String name, String description, String xmlTag)
/*     */   {
/*  92 */     this.flavor = flavor;
/*  93 */     this.clazz = clazz;
/*  94 */     this.name = name;
/*  95 */     this.xmlTag = xmlTag;
/*  96 */     this.description = description;
/*  97 */     this.constructors = findConstructors();
/*     */   }
/*     */   
/*     */ 
/*     */   public UserSensorType()
/*     */   {
/* 103 */     this.xmlTag = (this.name = this.description = null);
/* 104 */     this.flavor = Flavor.I2C;
/* 105 */     this.clazz = HardwareDevice.class;
/* 106 */     this.constructors = new LinkedList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class SerializationProxy
/*     */     implements Serializable
/*     */   {
/*     */     protected String xmlTag;
/*     */     
/*     */ 
/*     */     public SerializationProxy(UserSensorType userSensorType)
/*     */     {
/* 119 */       this.xmlTag = userSensorType.xmlTag;
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 123 */       return UserSensorTypeManager.getInstance().typeFromTag(this.xmlTag);
/*     */     }
/*     */   }
/*     */   
/*     */   private Object writeReplace()
/*     */   {
/* 129 */     return new SerializationProxy(this);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws InvalidObjectException
/*     */   {
/* 134 */     throw new InvalidObjectException("proxy required");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonNull
/*     */   public HardwareDevice createInstance(I2cController controller, int port)
/*     */     throws InvocationTargetException
/*     */   {
/*     */     try
/*     */     {
/* 146 */       Constructor ctor = findMatch(ctorI2cDeviceSynch);
/* 147 */       if (null == ctor) ctor = findMatch(ctorI2cDeviceSynchSimple);
/* 148 */       if (null != ctor)
/*     */       {
/* 150 */         I2cDevice i2cDevice = new I2cDeviceImpl(controller, port);
/* 151 */         I2cDeviceSynch i2cDeviceSynch = new I2cDeviceSynchImpl(i2cDevice, true);
/* 152 */         return (HardwareDevice)ctor.newInstance(new Object[] { i2cDeviceSynch });
/*     */       }
/*     */       
/* 155 */       ctor = findMatch(ctorI2cDevice);
/* 156 */       if (null != ctor)
/*     */       {
/* 158 */         I2cDevice i2cDevice = new I2cDeviceImpl(controller, port);
/* 159 */         return (HardwareDevice)ctor.newInstance(new Object[] { i2cDevice });
/*     */       }
/*     */       
/* 162 */       ctor = findMatch(ctorI2cControllerPort);
/* 163 */       if (null != ctor)
/*     */       {
/* 165 */         return (HardwareDevice)ctor.newInstance(new Object[] { controller, Integer.valueOf(port) });
/*     */       }
/*     */     }
/*     */     catch (IllegalAccessException|InstantiationException e)
/*     */     {
/* 170 */       throw new RuntimeException("internal error: exception");
/*     */     }
/* 172 */     throw new RuntimeException("internal error");
/*     */   }
/*     */   
/*     */   protected Constructor findMatch(Class<?>[] prototype)
/*     */   {
/* 177 */     for (Constructor ctor : this.constructors)
/*     */     {
/* 179 */       Class<?>[] parameters = ctor.getParameterTypes();
/* 180 */       if (match(parameters, prototype))
/*     */       {
/* 182 */         return ctor;
/*     */       }
/*     */     }
/* 185 */     return null;
/*     */   }
/*     */   
/*     */   protected List<Constructor> findConstructors()
/*     */   {
/* 190 */     List<Constructor> result = new LinkedList();
/* 191 */     List<Constructor> constructors = ClassUtil.getDeclaredConstructors(this.clazz);
/* 192 */     for (Constructor<?> ctor : constructors)
/*     */     {
/* 194 */       int requiredModifiers = 1;
/* 195 */       int prohibitedModifiers = 1032;
/* 196 */       if (((ctor.getModifiers() & requiredModifiers) == requiredModifiers) && ((ctor.getModifiers() & prohibitedModifiers) == 0))
/*     */       {
/*     */ 
/* 199 */         Class<?>[] parameters = ctor.getParameterTypes();
/* 200 */         switch (this.flavor)
/*     */         {
/*     */         case I2C: 
/* 203 */           if ((match(parameters, ctorI2cControllerPort)) || (match(parameters, ctorI2cDevice)) || (match(parameters, ctorI2cDeviceSynch)) || (match(parameters, ctorI2cDeviceSynchSimple)))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 208 */             result.add(ctor); }
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 213 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean match(Class<?>[] declared, Class<?>[] desired)
/*     */   {
/* 218 */     if (declared.length == desired.length)
/*     */     {
/* 220 */       for (int i = 0; i < declared.length; i++)
/*     */       {
/* 222 */         if (!declared[i].equals(desired[i]))
/*     */         {
/* 224 */           return false;
/*     */         }
/*     */       }
/* 227 */       return true;
/*     */     }
/*     */     
/* 230 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Flavor getFlavor()
/*     */   {
/* 239 */     return this.flavor;
/*     */   }
/*     */   
/*     */   public Class<? extends HardwareDevice> getClazz()
/*     */   {
/* 244 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 249 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getDescription()
/*     */   {
/* 254 */     return this.description;
/*     */   }
/*     */   
/*     */   public boolean hasConstructors()
/*     */   {
/* 259 */     return this.constructors.size() > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @NonNull
/*     */   public String getDisplayName(Context context)
/*     */   {
/* 268 */     return this.name;
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public String getXmlTag() {
/* 273 */     return this.xmlTag;
/*     */   }
/*     */   
/*     */   @NonNull
/*     */   public DeviceManager.DeviceType toUSBDeviceType() {
/* 278 */     return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
/*     */   }
/*     */   
/*     */   public boolean isI2cDevice()
/*     */   {
/* 283 */     return this.flavor == Flavor.I2C;
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\UserSensorType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */