/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.os.Bundle;
/*     */ import android.view.View;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemClickListener;
/*     */ import android.widget.Button;
/*     */ import android.widget.ListView;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
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
/*     */ public class EditSwapUsbDevices
/*     */   extends EditActivity
/*     */ {
/*     */   public static final String TAG = "EditSwapUsbDevices";
/*  57 */   public static final RequestCode requestCode = RequestCode.EDIT_SWAP_USB_DEVICES;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ControllerConfiguration targetConfiguration;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  70 */     super.onCreate(savedInstanceState);
/*  71 */     RobotLog.vv("EditSwapUsbDevices", "onCreate()");
/*  72 */     setContentView(R.layout.activity_swap_usb_devices);
/*     */     
/*  74 */     EditParameters parameters = EditParameters.fromIntent(this, getIntent());
/*  75 */     deserialize(parameters);
/*  76 */     this.targetConfiguration = ((ControllerConfiguration)parameters.getConfiguration());
/*     */     
/*  78 */     String message = String.format(getString(R.string.swapPrompt), new Object[] { this.targetConfiguration.getName() });
/*  79 */     TextView caption = (TextView)findViewById(R.id.swapCaption);
/*  80 */     caption.setText(message);
/*     */     
/*     */ 
/*  83 */     Button doneButton = (Button)findViewById(R.id.doneButton);
/*  84 */     doneButton.setVisibility(8);
/*     */     
/*  86 */     populateList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void populateList()
/*     */   {
/*  95 */     ListView controllerListView = (ListView)findViewById(R.id.controllersList);
/*     */     
/*  97 */     DeviceInfoAdapter adapter = new DeviceInfoAdapter(this, 17367044, getRobotConfigMap().getEligibleSwapTargets(this.targetConfiguration, this.scannedDevices, this));
/*  98 */     controllerListView.setAdapter(adapter);
/*     */     
/* 100 */     controllerListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
/*     */     {
/*     */ 
/*     */       public void onItemClick(AdapterView<?> adapterView, View v, int pos, long arg3)
/*     */       {
/* 105 */         ControllerConfiguration controllerConfiguration = (ControllerConfiguration)adapterView.getItemAtPosition(pos);
/* 106 */         EditSwapUsbDevices.this.finishOk(new EditParameters(EditSwapUsbDevices.this, controllerConfiguration));
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public void onBackPressed()
/*     */   {
/* 114 */     RobotLog.vv("EditSwapUsbDevices", "onBackPressed()");
/* 115 */     doBackOrCancel();
/*     */   }
/*     */   
/*     */   public void onCancelButtonPressed(View view)
/*     */   {
/* 120 */     RobotLog.vv("EditSwapUsbDevices", "onCancelButtonPressed()");
/* 121 */     doBackOrCancel();
/*     */   }
/*     */   
/*     */   protected void doBackOrCancel()
/*     */   {
/* 126 */     finishCancel();
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditSwapUsbDevices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */