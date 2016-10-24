/*    */ package com.qualcomm.robotcore.hardware.configuration;
/*    */ 
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LegacyModuleControllerConfiguration
/*    */   extends ControllerConfiguration
/*    */ {
/*    */   public LegacyModuleControllerConfiguration(String name, List<DeviceConfiguration> modules, SerialNumber serialNumber)
/*    */   {
/* 41 */     super(name, modules, serialNumber, BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\LegacyModuleControllerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */