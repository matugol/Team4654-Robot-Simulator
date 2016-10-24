/*    */ package com.qualcomm.ftccommon.configuration;
/*    */ 
/*    */ import android.view.LayoutInflater;
/*    */ import android.view.View;
/*    */ import android.view.ViewGroup;
/*    */ import android.widget.BaseAdapter;
/*    */ import android.widget.ListAdapter;
/*    */ import android.widget.TextView;
/*    */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*    */ import java.util.LinkedList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeviceInfoAdapter
/*    */   extends BaseAdapter
/*    */   implements ListAdapter
/*    */ {
/* 52 */   private List<ControllerConfiguration> deviceControllers = new LinkedList();
/*    */   private EditActivity editActivity;
/*    */   private int list_id;
/*    */   
/*    */   public DeviceInfoAdapter(EditActivity editActivity, int list_id, List<ControllerConfiguration> deviceControllers)
/*    */   {
/* 58 */     this.editActivity = editActivity;
/* 59 */     this.deviceControllers = deviceControllers;
/* 60 */     this.list_id = list_id;
/*    */   }
/*    */   
/*    */   public int getCount()
/*    */   {
/* 65 */     return this.deviceControllers.size();
/*    */   }
/*    */   
/*    */   public Object getItem(int arg0)
/*    */   {
/* 70 */     return this.deviceControllers.get(arg0);
/*    */   }
/*    */   
/*    */ 
/*    */   public long getItemId(int arg0)
/*    */   {
/* 76 */     return 0L;
/*    */   }
/*    */   
/*    */   public View getView(int pos, View convertView, ViewGroup parent)
/*    */   {
/* 81 */     View row = convertView;
/* 82 */     if (row == null) {
/* 83 */       LayoutInflater inflater = this.editActivity.getLayoutInflater();
/* 84 */       row = inflater.inflate(this.list_id, parent, false);
/*    */     }
/*    */     
/* 87 */     ControllerConfiguration controllerConfiguration = (ControllerConfiguration)this.deviceControllers.get(pos);
/* 88 */     String serialNum = EditActivity.formatSerialNumber(this.editActivity, controllerConfiguration);
/* 89 */     TextView displayNum = (TextView)row.findViewById(16908309);
/* 90 */     displayNum.setText(serialNum);
/*    */     
/* 92 */     String name = ((ControllerConfiguration)this.deviceControllers.get(pos)).getName();
/* 93 */     TextView text = (TextView)row.findViewById(16908308);
/* 94 */     text.setText(name);
/* 95 */     return row;
/*    */   }
/*    */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\DeviceInfoAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */