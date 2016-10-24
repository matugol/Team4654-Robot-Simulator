/*    */ package org.firstinspires.inspection;
/*    */ 
/*    */ import android.content.BroadcastReceiver;
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
/*    */ import android.net.wifi.p2p.WifiP2pDevice;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeviceNameReceiver
/*    */   extends BroadcastReceiver
/*    */ {
/*    */   private OnDeviceNameReceivedListener listener;
/*    */   
/*    */   public void onReceive(Context context, Intent intent)
/*    */   {
/* 17 */     WifiP2pDevice device = (WifiP2pDevice)intent.getParcelableExtra("wifiP2pDevice");
/* 18 */     String thisDeviceName = device.deviceName;
/*    */     
/* 20 */     if (this.listener != null) {
/* 21 */       this.listener.onDeviceNameReceived(thisDeviceName);
/*    */     }
/*    */   }
/*    */   
/*    */   public void setOnDeviceNameReceivedListener(Context context) {
/* 26 */     this.listener = ((OnDeviceNameReceivedListener)context);
/*    */   }
/*    */   
/*    */   public static abstract interface OnDeviceNameReceivedListener
/*    */   {
/*    */     public abstract void onDeviceNameReceived(String paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\Inspection-release.jar!\classes.jar!\org\firstinspires\inspection\DeviceNameReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */