/*     */ package com.qualcomm.robotcore.hardware.configuration;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.util.Xml;
/*     */ import com.qualcomm.robotcore.exception.DuplicateNameException;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import org.xmlpull.v1.XmlSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WriteXMLFileHandler
/*     */ {
/*     */   private XmlSerializer serializer;
/*  56 */   private HashSet<String> names = new HashSet();
/*  57 */   private List<String> duplicates = new ArrayList();
/*  58 */   private String[] indentation = { "    ", "        ", "            " };
/*  59 */   private int indent = 0;
/*     */   
/*     */   public WriteXMLFileHandler(Context context) {
/*  62 */     this.serializer = Xml.newSerializer();
/*     */   }
/*     */   
/*     */   public String toXml(Collection<ControllerConfiguration> deviceControllerConfigurations) {
/*  66 */     return toXml(deviceControllerConfigurations, null, null);
/*     */   }
/*     */   
/*     */   public String toXml(Collection<ControllerConfiguration> deviceControllerConfigurations, @Nullable String attribute, @Nullable String attributeValue) {
/*  70 */     this.duplicates = new ArrayList();
/*  71 */     this.names = new HashSet();
/*     */     
/*  73 */     StringWriter writer = new StringWriter();
/*     */     try {
/*  75 */       this.serializer.setOutput(writer);
/*  76 */       this.serializer.startDocument("UTF-8", Boolean.valueOf(true));
/*  77 */       this.serializer.ignorableWhitespace("\n");
/*  78 */       this.serializer.startTag("", "Robot");
/*  79 */       if (attribute != null) this.serializer.attribute("", attribute, attributeValue);
/*  80 */       this.serializer.ignorableWhitespace("\n");
/*  81 */       for (ControllerConfiguration controllerConfiguration : deviceControllerConfigurations)
/*     */       {
/*  83 */         ConfigurationType type = controllerConfiguration.getType();
/*  84 */         if ((type == BuiltInConfigurationType.MOTOR_CONTROLLER) || (type == BuiltInConfigurationType.SERVO_CONTROLLER)) {
/*  85 */           handleController(controllerConfiguration, true);
/*  86 */         } else if (type == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
/*  87 */           handleLegacyModuleController(controllerConfiguration);
/*  88 */         } else if (type == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
/*  89 */           handleDeviceInterfaceModule(controllerConfiguration);
/*     */         }
/*     */       }
/*  92 */       this.serializer.endTag("", "Robot");
/*  93 */       this.serializer.ignorableWhitespace("\n");
/*  94 */       this.serializer.endDocument();
/*  95 */       return writer.toString();
/*     */     } catch (Exception e) {
/*  97 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkForDuplicates(DeviceConfiguration config) {
/* 102 */     if (config.isEnabled()) {
/* 103 */       String name = config.getName();
/* 104 */       if (this.names.contains(name)) {
/* 105 */         this.duplicates.add(name);
/*     */       } else {
/* 107 */         this.names.add(name);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleDeviceInterfaceModule(ControllerConfiguration controller) throws IOException {
/* 113 */     this.serializer.ignorableWhitespace(this.indentation[this.indent]);
/* 114 */     this.serializer.startTag("", conform(controller.getType()));
/* 115 */     checkForDuplicates(controller);
/* 116 */     this.serializer.attribute("", "name", controller.getName());
/* 117 */     this.serializer.attribute("", "serialNumber", controller.getSerialNumber().toString());
/* 118 */     this.serializer.ignorableWhitespace("\n");
/* 119 */     this.indent += 1;
/*     */     
/* 121 */     DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = (DeviceInterfaceModuleConfiguration)controller;
/*     */     
/* 123 */     for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getPwmOutputs()) {
/* 124 */       buildDeviceNameAndPort(device);
/*     */     }
/*     */     
/* 127 */     for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getI2cDevices()) {
/* 128 */       buildDeviceNameAndPort(device);
/*     */     }
/*     */     
/* 131 */     for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getAnalogInputDevices()) {
/* 132 */       buildDeviceNameAndPort(device);
/*     */     }
/*     */     
/* 135 */     for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getDigitalDevices()) {
/* 136 */       buildDeviceNameAndPort(device);
/*     */     }
/*     */     
/* 139 */     for (DeviceConfiguration device : deviceInterfaceModuleConfiguration.getAnalogOutputDevices()) {
/* 140 */       buildDeviceNameAndPort(device);
/*     */     }
/*     */     
/* 143 */     this.indent -= 1;
/* 144 */     this.serializer.ignorableWhitespace(this.indentation[this.indent]);
/* 145 */     this.serializer.endTag("", conform(controller.getType()));
/* 146 */     this.serializer.ignorableWhitespace("\n");
/*     */   }
/*     */   
/*     */   private void handleLegacyModuleController(ControllerConfiguration controller) throws IOException {
/* 150 */     this.serializer.ignorableWhitespace(this.indentation[this.indent]);
/* 151 */     this.serializer.startTag("", conform(controller.getType()));
/* 152 */     checkForDuplicates(controller);
/* 153 */     this.serializer.attribute("", "name", controller.getName());
/* 154 */     this.serializer.attribute("", "serialNumber", controller.getSerialNumber().toString());
/* 155 */     this.serializer.ignorableWhitespace("\n");
/* 156 */     this.indent += 1;
/*     */     
/*     */ 
/* 159 */     for (DeviceConfiguration device : controller.getDevices()) {
/* 160 */       ConfigurationType type = device.getType();
/* 161 */       if ((type == BuiltInConfigurationType.MOTOR_CONTROLLER) || (type == BuiltInConfigurationType.SERVO_CONTROLLER) || (type == BuiltInConfigurationType.MATRIX_CONTROLLER))
/*     */       {
/*     */ 
/* 164 */         handleController((ControllerConfiguration)device, false);
/*     */       } else {
/* 166 */         buildDeviceNameAndPort(device);
/*     */       }
/*     */     }
/*     */     
/* 170 */     this.indent -= 1;
/* 171 */     this.serializer.ignorableWhitespace(this.indentation[this.indent]);
/* 172 */     this.serializer.endTag("", conform(controller.getType()));
/* 173 */     this.serializer.ignorableWhitespace("\n");
/*     */   }
/*     */   
/*     */   private void handleController(ControllerConfiguration controller, boolean isUsbDevice) throws IOException {
/* 177 */     this.serializer.ignorableWhitespace(this.indentation[this.indent]);
/* 178 */     this.serializer.startTag("", conform(controller.getType()));
/* 179 */     checkForDuplicates(controller);
/* 180 */     this.serializer.attribute("", "name", controller.getName());
/*     */     
/* 182 */     if (isUsbDevice) {
/* 183 */       this.serializer.attribute("", "serialNumber", controller.getSerialNumber().toString());
/*     */     } else {
/* 185 */       this.serializer.attribute("", "port", String.valueOf(controller.getPort()));
/*     */     }
/*     */     
/* 188 */     this.serializer.ignorableWhitespace("\n");
/* 189 */     this.indent += 1;
/*     */     
/* 191 */     if (controller.getType() == BuiltInConfigurationType.MATRIX_CONTROLLER) {
/* 192 */       for (DeviceConfiguration device : ((MatrixControllerConfiguration)controller).getMotors()) {
/* 193 */         buildDeviceNameAndPort(device);
/*     */       }
/* 195 */       for (DeviceConfiguration device : ((MatrixControllerConfiguration)controller).getServos()) {
/* 196 */         buildDeviceNameAndPort(device);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 201 */       for (DeviceConfiguration device : controller.getDevices()) {
/* 202 */         buildDeviceNameAndPort(device);
/*     */       }
/*     */     }
/*     */     
/* 206 */     this.indent -= 1;
/* 207 */     this.serializer.ignorableWhitespace(this.indentation[this.indent]);
/* 208 */     this.serializer.endTag("", conform(controller.getType()));
/* 209 */     this.serializer.ignorableWhitespace("\n");
/*     */   }
/*     */   
/*     */   private void buildDeviceNameAndPort(DeviceConfiguration device)
/*     */   {
/* 214 */     if (!device.isEnabled()) {
/* 215 */       return;
/*     */     }
/*     */     try {
/* 218 */       this.serializer.ignorableWhitespace(this.indentation[this.indent]);
/* 219 */       this.serializer.startTag("", conform(device.getType()));
/* 220 */       checkForDuplicates(device);
/* 221 */       this.serializer.attribute("", "name", device.getName());
/* 222 */       this.serializer.attribute("", "port", String.valueOf(device.getPort()));
/* 223 */       this.serializer.endTag("", conform(device.getType()));
/* 224 */       this.serializer.ignorableWhitespace("\n");
/*     */     } catch (Exception e) {
/* 226 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToFile(String data, File folder, String filenameWithExt) throws RobotCoreException, IOException {
/* 231 */     if (this.duplicates.size() > 0) {
/* 232 */       throw new DuplicateNameException("Duplicate names: " + this.duplicates);
/*     */     }
/*     */     
/* 235 */     boolean success = true;
/*     */     
/* 237 */     if (!folder.exists()) {
/* 238 */       success = folder.mkdir();
/*     */     }
/* 240 */     if (success) {
/* 241 */       File file = new File(folder, filenameWithExt);
/* 242 */       FileOutputStream stream = null;
/*     */       try {
/* 244 */         stream = new FileOutputStream(file);
/* 245 */         stream.write(data.getBytes());
/*     */       } catch (Exception e) {
/* 247 */         e.printStackTrace();
/*     */       } finally {
/*     */         try {
/* 250 */           stream.close();
/*     */         }
/*     */         catch (IOException e) {
/* 253 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 257 */     throw new RobotCoreException("Unable to create directory");
/*     */   }
/*     */   
/*     */   private String conform(ConfigurationType type)
/*     */   {
/* 262 */     return type.getXmlTag();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\WriteXMLFileHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */