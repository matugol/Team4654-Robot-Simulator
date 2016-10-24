/*    */ package com.qualcomm.ftccommon.configuration;
/*    */ 
/*    */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
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
/*    */ 
/*    */ public class ScannedDevices
/*    */   extends HashMap<SerialNumber, DeviceManager.DeviceType>
/*    */ {
/*    */   private static final String pairSeparatorWrite = "|";
/*    */   private static final String pairSeparatorSplit = "\\|";
/*    */   private static final String keyValueSeparatorWrite = ",";
/*    */   private static final String keyValueSeparatorSplit = ",";
/*    */   
/*    */   public ScannedDevices(Map<SerialNumber, DeviceManager.DeviceType> map)
/*    */   {
/* 50 */     super(map);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ScannedDevices() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toSerializationString()
/*    */   {
/* 65 */     StringBuilder result = new StringBuilder();
/* 66 */     for (Map.Entry<SerialNumber, DeviceManager.DeviceType> entry : entrySet())
/*    */     {
/* 68 */       if (result.length() > 0) result.append("|");
/* 69 */       result.append(((SerialNumber)entry.getKey()).toString());
/* 70 */       result.append(",");
/* 71 */       result.append(((DeviceManager.DeviceType)entry.getValue()).toString());
/*    */     }
/* 73 */     return result.toString();
/*    */   }
/*    */   
/*    */   public static ScannedDevices fromSerializationString(String string)
/*    */   {
/* 78 */     ScannedDevices result = new ScannedDevices();
/*    */     
/* 80 */     string = string.trim();
/* 81 */     if (string.length() > 0)
/*    */     {
/* 83 */       String[] pairs = string.split("\\|");
/* 84 */       for (String pair : pairs)
/*    */       {
/* 86 */         String[] keyValue = pair.split(",");
/* 87 */         SerialNumber serialNumber = new SerialNumber(keyValue[0]);
/* 88 */         DeviceManager.DeviceType deviceType = DeviceManager.DeviceType.valueOf(keyValue[1]);
/* 89 */         result.put(serialNumber, deviceType);
/*    */       }
/*    */     }
/*    */     
/* 93 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\ScannedDevices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */