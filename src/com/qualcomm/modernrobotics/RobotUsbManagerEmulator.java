/*    */ package com.qualcomm.modernrobotics;
/*    */ 
/*    */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
/*    */ import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
/*    */ import com.qualcomm.robotcore.util.SerialNumber;
/*    */ import java.util.ArrayList;
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
/*    */ public class RobotUsbManagerEmulator
/*    */   implements RobotUsbManager
/*    */ {
/* 30 */   private ArrayList<b> a = new ArrayList();
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
/*    */   public int scanForDevices()
/*    */     throws RobotCoreException
/*    */   {
/* 44 */     return this.a.size();
/*    */   }
/*    */   
/*    */   public SerialNumber getDeviceSerialNumberByIndex(int index) throws RobotCoreException
/*    */   {
/* 49 */     return ((b)this.a.get(index)).b;
/*    */   }
/*    */   
/*    */   public String getDeviceDescriptionByIndex(int index) throws RobotCoreException
/*    */   {
/* 54 */     return ((b)this.a.get(index)).c;
/*    */   }
/*    */   
/*    */   public RobotUsbDevice openBySerialNumber(SerialNumber serialNumber) throws RobotCoreException
/*    */   {
/* 59 */     RobotLog.d("attempting to open emulated device " + serialNumber);
/* 60 */     for (b localb : this.a) {
/* 61 */       if (localb.b.equals(serialNumber)) { return localb;
/*    */       }
/*    */     }
/*    */     
/* 65 */     throw new RobotCoreException("cannot open device - could not find device with serial number " + serialNumber);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\ModernRobotics-release.jar!\classes.jar!\com\qualcomm\modernrobotics\RobotUsbManagerEmulator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */