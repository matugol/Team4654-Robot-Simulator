/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Intent;
/*     */ import android.support.annotation.IdRes;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.view.View;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ import org.firstinspires.ftc.robotcore.internal.ToastLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditUSBDeviceActivity
/*     */   extends EditActivity
/*     */ {
/*  27 */   protected ScannedDevices extraUSBDevices = new ScannedDevices();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void deserialize(EditParameters parameters)
/*     */   {
/*  39 */     super.deserialize(parameters);
/*  40 */     determineExtraUSBDevices();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void swapConfiguration()
/*     */   {
/*  49 */     if (getRobotConfigMap().isSwappable(this.controllerConfiguration, this.scannedDevices, this))
/*     */     {
/*  51 */       EditParameters parameters = new EditParameters(this, this.controllerConfiguration);
/*  52 */       parameters.setRobotConfigMap(getRobotConfigMap());
/*  53 */       parameters.setScannedDevices(this.scannedDevices);
/*  54 */       handleLaunchEdit(EditSwapUsbDevices.requestCode, EditSwapUsbDevices.class, parameters);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean completeSwapConfiguration(int requestCodeValue, int resultCode, Intent data)
/*     */   {
/*  60 */     if (resultCode == -1)
/*     */     {
/*  62 */       RequestCode requestCode = RequestCode.fromValue(requestCodeValue);
/*  63 */       if (requestCode == EditSwapUsbDevices.requestCode)
/*     */       {
/*     */ 
/*  66 */         EditParameters returnedParameters = EditParameters.fromIntent(this, data);
/*  67 */         SerialNumber swappeeSerialNumber = ((ControllerConfiguration)returnedParameters.getConfiguration()).getSerialNumber();
/*     */         
/*  69 */         ControllerConfiguration swappee = getRobotConfigMap().get(swappeeSerialNumber);
/*  70 */         if (swappee != null)
/*     */         {
/*     */ 
/*  73 */           this.robotConfigMap.swapSerialNumbers(this.controllerConfiguration, swappee);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*  78 */           this.robotConfigMap.setSerialNumber(this.controllerConfiguration, swappeeSerialNumber);
/*  79 */           this.controllerConfiguration.setKnownToBeAttached(true);
/*     */         }
/*     */         
/*     */ 
/*  83 */         determineExtraUSBDevices();
/*     */         
/*     */ 
/*  86 */         refreshAfterSwap();
/*     */         
/*  88 */         return true;
/*     */       }
/*     */     }
/*  91 */     return false;
/*     */   }
/*     */   
/*     */   protected void fixConfiguration()
/*     */   {
/*  96 */     SerialNumber candidate = getFixableCandidate();
/*  97 */     boolean isFixable = candidate != null;
/*  98 */     if (isFixable)
/*     */     {
/* 100 */       this.robotConfigMap.setSerialNumber(this.controllerConfiguration, candidate);
/* 101 */       this.controllerConfiguration.setKnownToBeAttached(true);
/* 102 */       determineExtraUSBDevices();
/*     */     }
/*     */     else
/*     */     {
/* 106 */       String format = getString(R.string.fixFailNoneAvailable);
/* 107 */       String name = this.controllerConfiguration.getName();
/* 108 */       String type = displayNameOfConfigurationType(this.controllerConfiguration.getType());
/* 109 */       this.appUtil.showToast(ToastLocation.ONLY_LOCAL, this.context, String.format(format, new Object[] { name, type }));
/*     */     }
/*     */     
/* 112 */     refreshAfterFix();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected SerialNumber getFixableCandidate() {
/* 117 */     SerialNumber candidate = null;
/*     */     
/*     */ 
/* 120 */     if (this.controllerConfiguration.isKnownToBeAttached())
/*     */     {
/* 122 */       return null;
/*     */     }
/*     */     
/* 125 */     boolean isFixable = false;
/* 126 */     DeviceManager.DeviceType deviceType = this.controllerConfiguration.toUSBDeviceType();
/*     */     
/*     */ 
/* 129 */     for (Map.Entry<SerialNumber, DeviceManager.DeviceType> pair : this.extraUSBDevices.entrySet())
/*     */     {
/* 131 */       if (pair.getValue() == deviceType)
/*     */       {
/* 133 */         if (candidate != null)
/*     */         {
/*     */ 
/* 136 */           isFixable = false;
/* 137 */           break;
/*     */         }
/*     */         
/* 140 */         candidate = (SerialNumber)pair.getKey();
/* 141 */         isFixable = true;
/*     */       }
/*     */     }
/* 144 */     return isFixable ? candidate : null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isFixable()
/*     */   {
/* 150 */     return getFixableCandidate() != null;
/*     */   }
/*     */   
/*     */   protected boolean isSwappable()
/*     */   {
/* 155 */     List<ControllerConfiguration> swapCandidates = getRobotConfigMap().getEligibleSwapTargets(this.controllerConfiguration, this.scannedDevices, this);
/* 156 */     SerialNumber fixCandidate = getFixableCandidate();
/*     */     
/*     */ 
/* 159 */     return (!swapCandidates.isEmpty()) && ((fixCandidate == null) || (swapCandidates.size() != 1) || (!((ControllerConfiguration)swapCandidates.get(0)).getSerialNumber().equals(fixCandidate)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void refreshSerialNumber() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void refreshAfterFix()
/*     */   {
/* 176 */     showFixSwapButtons();
/* 177 */     this.currentCfgFile.markDirty();
/* 178 */     this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
/*     */   }
/*     */   
/*     */   protected void refreshAfterSwap()
/*     */   {
/* 183 */     showFixSwapButtons();
/* 184 */     this.currentCfgFile.markDirty();
/* 185 */     this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
/*     */   }
/*     */   
/*     */   protected void showFixSwapButtons()
/*     */   {
/* 190 */     showFixButton(isFixable());
/* 191 */     showSwapButton(isSwappable());
/* 192 */     refreshSerialNumber();
/*     */   }
/*     */   
/*     */   protected void showFixButton(boolean show)
/*     */   {
/* 197 */     showButton(this.idFixButton, show);
/*     */   }
/*     */   
/*     */   protected void showSwapButton(boolean show)
/*     */   {
/* 202 */     showButton(this.idSwapButton, show);
/*     */   }
/*     */   
/*     */   protected void showButton(@IdRes int id, boolean show)
/*     */   {
/* 207 */     View button = findViewById(id);
/* 208 */     if (button != null)
/*     */     {
/* 210 */       button.setVisibility(show ? 0 : 8);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void determineExtraUSBDevices()
/*     */   {
/* 217 */     this.extraUSBDevices = new ScannedDevices(this.scannedDevices);
/* 218 */     for (SerialNumber serialNumber : getRobotConfigMap().serialNumbers())
/*     */     {
/* 220 */       this.extraUSBDevices.remove(serialNumber);
/*     */     }
/* 222 */     for (ControllerConfiguration controllerConfiguration : getRobotConfigMap().controllerConfigurations())
/*     */     {
/* 224 */       controllerConfiguration.setKnownToBeAttached(this.scannedDevices.containsKey(controllerConfiguration.getSerialNumber()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\FtcCommon-release.jar!\classes.jar!\com\qualcomm\ftccommon\configuration\EditUSBDeviceActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */