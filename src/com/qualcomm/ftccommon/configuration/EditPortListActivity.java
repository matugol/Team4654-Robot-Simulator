/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.widget.EditText;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public abstract class EditPortListActivity
/*     */   extends EditUSBDeviceActivity
/*     */ {
/*     */   public static final String TAG = "EditPortListActivity";
/*     */   protected int layoutMain;
/*  63 */   protected int layoutControllerNameBanner = 0;
/*     */   
/*     */   protected int idListParentLayout;
/*     */   protected int layoutItem;
/*     */   protected int idItemRowPort;
/*     */   protected int idItemPortNumber;
/*     */   protected int idItemEditTextResult;
/*  70 */   protected ArrayList<View> itemViews = new ArrayList();
/*  71 */   protected List<DeviceConfiguration> configList = new ArrayList();
/*     */   
/*     */   protected int initialPortNumber;
/*     */   
/*  75 */   protected int idBannerParent = R.id.bannerParent;
/*  76 */   protected int idControllerName = R.id.controller_name;
/*  77 */   protected int idControllerSerialNumber = R.id.serialNumber;
/*     */   
/*     */ 
/*     */ 
/*     */   protected EditText editTextBannerControllerName;
/*     */   
/*     */ 
/*     */ 
/*     */   protected TextView textViewSerialNumber;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  92 */     super.onCreate(savedInstanceState);
/*  93 */     setContentView(this.layoutMain);
/*     */     
/*  95 */     Intent intent = getIntent();
/*  96 */     EditParameters parameters = EditParameters.fromIntent(this, intent);
/*     */     
/*     */ 
/*  99 */     deserialize(parameters);
/* 100 */     this.initialPortNumber = parameters.getInitialPortNumber();
/*     */     
/*     */ 
/* 103 */     showButton(this.idAddButton, parameters.isGrowable());
/*     */     
/*     */ 
/* 106 */     if (this.layoutControllerNameBanner != 0)
/*     */     {
/* 108 */       LinearLayout parent = (LinearLayout)findViewById(this.idBannerParent);
/* 109 */       View banner = getLayoutInflater().inflate(this.layoutControllerNameBanner, parent, false);
/* 110 */       parent.addView(banner);
/*     */       
/* 112 */       this.editTextBannerControllerName = ((EditText)banner.findViewById(this.idControllerName));
/* 113 */       this.textViewSerialNumber = ((TextView)banner.findViewById(this.idControllerSerialNumber));
/* 114 */       this.editTextBannerControllerName.setText(this.controllerConfiguration.getName());
/* 115 */       showFixSwapButtons();
/*     */     }
/*     */     
/* 118 */     createListViews(parameters);
/* 119 */     addViewListeners();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void refreshSerialNumber()
/*     */   {
/* 125 */     String serialNum = formatSerialNumber(this, this.controllerConfiguration);
/* 126 */     this.textViewSerialNumber.setText(serialNum);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/* 132 */     super.onStart();
/*     */   }
/*     */   
/*     */   protected void createListViews(EditParameters parameters)
/*     */   {
/* 137 */     if (parameters != null)
/*     */     {
/* 139 */       this.configList = parameters.getCurrentItems();
/* 140 */       Collections.sort(this.configList);
/*     */       
/* 142 */       for (int index = 0; index < this.configList.size(); index++)
/*     */       {
/* 144 */         View itemView = createItemViewForPort(findConfigByIndex(index).getPort());
/* 145 */         this.itemViews.add(itemView);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addViewListeners()
/*     */   {
/* 152 */     for (int index = 0; index < this.configList.size(); index++)
/*     */     {
/* 154 */       addViewListenersOnIndex(index);
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void addViewListenersOnIndex(int paramInt);
/*     */   
/*     */   protected View createItemViewForPort(int portNumber)
/*     */   {
/* 162 */     LinearLayout parent = (LinearLayout)findViewById(this.idListParentLayout);
/* 163 */     View child = getLayoutInflater().inflate(this.layoutItem, parent, false);
/* 164 */     parent.addView(child);
/*     */     
/* 166 */     View result = child.findViewById(this.idItemRowPort);
/*     */     
/* 168 */     TextView port = (TextView)result.findViewById(this.idItemPortNumber);
/* 169 */     if (port != null)
/*     */     {
/* 171 */       port.setText(String.format("%d", new Object[] { Integer.valueOf(portNumber) }));
/*     */     }
/*     */     
/* 174 */     return result;
/*     */   }
/*     */   
/*     */   protected void addNameTextChangeWatcherOnIndex(int index)
/*     */   {
/* 179 */     View itemView = findViewByIndex(index);
/* 180 */     EditText name = (EditText)itemView.findViewById(this.idItemEditTextResult);
/*     */     
/* 182 */     name.addTextChangedListener(new EditActivity.SetNameTextWatcher(this, findConfigByIndex(index)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onAddButtonPressed(View v)
/*     */   {
/* 191 */     addNewItem();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addNewItem()
/*     */   {
/* 200 */     int portNumber = this.configList.isEmpty() ? this.initialPortNumber : ((DeviceConfiguration)this.configList.get(this.configList.size() - 1)).getPort() + 1;
/* 201 */     int index = this.configList.size();
/*     */     
/*     */ 
/* 204 */     DeviceConfiguration deviceConfiguration = new DeviceConfiguration(portNumber, BuiltInConfigurationType.NOTHING);
/*     */     
/*     */ 
/* 207 */     this.configList.add(deviceConfiguration);
/* 208 */     this.itemViews.add(createItemViewForPort(portNumber));
/* 209 */     addViewListenersOnIndex(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onFixButtonPressed(View v)
/*     */   {
/* 218 */     fixConfiguration();
/*     */   }
/*     */   
/*     */   public void onSwapButtonPressed(View view)
/*     */   {
/* 223 */     swapConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onActivityResult(int requestCodeValue, int resultCode, Intent data)
/*     */   {
/* 229 */     if (resultCode == -1)
/*     */     {
/* 231 */       completeSwapConfiguration(requestCodeValue, resultCode, data);
/* 232 */       this.currentCfgFile.markDirty();
/* 233 */       this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected View findViewByIndex(int index)
/*     */   {
/* 243 */     return (View)this.itemViews.get(index);
/*     */   }
/*     */   
/*     */   protected DeviceConfiguration findConfigByIndex(int index)
/*     */   {
/* 248 */     return (DeviceConfiguration)this.configList.get(index);
/*     */   }
/*     */   
/*     */   protected DeviceConfiguration findConfigByPort(int port)
/*     */   {
/* 253 */     for (DeviceConfiguration config : this.configList)
/*     */     {
/* 255 */       if (config.getPort() == port) return config;
/*     */     }
/* 257 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDoneButtonPressed(View v)
/*     */   {
/* 266 */     finishOk();
/*     */   }
/*     */   
/*     */   public void onCancelButtonPressed(View v)
/*     */   {
/* 271 */     finishCancel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void finishOk()
/*     */   {
/* 280 */     if (this.controllerConfiguration != null)
/*     */     {
/* 282 */       this.controllerConfiguration.setName(this.editTextBannerControllerName.getText().toString());
/* 283 */       finishOk(new EditParameters(this, this.controllerConfiguration, getRobotConfigMap()));
/*     */     }
/*     */     else
/*     */     {
/* 287 */       finishOk(new EditParameters(this, this.configList));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditPortListActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */