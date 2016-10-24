/*     */ package com.google.blocks;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.graphics.Typeface;
/*     */ import android.os.Bundle;
/*     */ import android.text.format.DateUtils;
/*     */ import android.view.Menu;
/*     */ import android.view.MenuInflater;
/*     */ import android.view.MenuItem;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.Button;
/*     */ import android.widget.ImageButton;
/*     */ import android.widget.TextView;
/*     */ import android.widget.Toast;
/*     */ import org.firstinspires.ftc.robotcore.internal.AppUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AbstractProgrammingModeActivity
/*     */   extends Activity
/*     */ {
/*     */   private static final int COLOR_GREEN = -16738048;
/*     */   private static final int COLOR_RED = -6750208;
/*     */   protected TextView textViewLog;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  34 */     super.onCreate(savedInstanceState);
/*     */     
/*  36 */     setContentView(R.layout.activity_programming_mode);
/*  37 */     setTypeface();
/*     */     
/*  39 */     this.textViewLog = ((TextView)findViewById(R.id.log));
/*     */     
/*  41 */     ImageButton buttonMenu = (ImageButton)findViewById(R.id.menu_buttons);
/*  42 */     buttonMenu.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View v) {
/*  45 */         AppUtil.getInstance().openOptionsMenuFor(AbstractProgrammingModeActivity.this);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public boolean onCreateOptionsMenu(Menu menu)
/*     */   {
/*  52 */     getMenuInflater().inflate(R.menu.menu_server, menu);
/*  53 */     return true;
/*     */   }
/*     */   
/*     */   public boolean onOptionsItemSelected(MenuItem item)
/*     */   {
/*  58 */     if (item.getItemId() == R.id.action_help) {
/*  59 */       Toast.makeText(this, getString(R.string.help_content), 0).show();
/*  60 */       return true;
/*     */     }
/*  62 */     return super.onOptionsItemSelected(item);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onClickDisplayServerLogButton(View view)
/*     */   {
/*  69 */     View container = findViewById(R.id.log_container);
/*  70 */     container.setVisibility(container.getVisibility() == 0 ? 4 : 0);
/*     */     
/*     */ 
/*  73 */     Button button = (Button)findViewById(R.id.display_server_log_button);
/*  74 */     button.setText(getString(container.getVisibility() == 0 ? R.string.hide_server_log_button : R.string.display_server_log_button));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void updateDisplay(final ProgrammingModeConnectionInfo programmingModeConnectionInfo)
/*     */   {
/*  81 */     runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/*  84 */         String notAvailable = AbstractProgrammingModeActivity.this.getString(R.string.not_available);
/*  85 */         boolean networkOK = true;
/*     */         
/*  87 */         TextView textView = (TextView)AbstractProgrammingModeActivity.this.findViewById(R.id.network_name);
/*  88 */         if ((programmingModeConnectionInfo.networkName != null) && (!programmingModeConnectionInfo.networkName.isEmpty()))
/*     */         {
/*  90 */           textView.setText(programmingModeConnectionInfo.networkName);
/*     */         } else {
/*  92 */           networkOK = false;
/*  93 */           textView.setText(notAvailable);
/*     */         }
/*  95 */         textView.requestLayout();
/*     */         
/*  97 */         textView = (TextView)AbstractProgrammingModeActivity.this.findViewById(R.id.passphrase);
/*  98 */         if ((programmingModeConnectionInfo.passphrase != null) && (!programmingModeConnectionInfo.passphrase.isEmpty()))
/*     */         {
/* 100 */           textView.setText(programmingModeConnectionInfo.passphrase);
/*     */         } else {
/* 102 */           networkOK = false;
/* 103 */           textView.setText(notAvailable);
/*     */         }
/* 105 */         textView.requestLayout();
/*     */         
/* 107 */         textView = (TextView)AbstractProgrammingModeActivity.this.findViewById(R.id.server_url);
/* 108 */         if ((programmingModeConnectionInfo.serverUrl != null) && (!programmingModeConnectionInfo.serverUrl.isEmpty()))
/*     */         {
/* 110 */           textView.setText(programmingModeConnectionInfo.serverUrl);
/*     */         } else {
/* 112 */           networkOK = false;
/* 113 */           textView.setText(notAvailable);
/*     */         }
/* 115 */         textView.requestLayout();
/*     */         
/* 117 */         textView = (TextView)AbstractProgrammingModeActivity.this.findViewById(R.id.server_status);
/* 118 */         if (!networkOK) {
/* 119 */           textView.setText(AbstractProgrammingModeActivity.this.getString(R.string.network_not_ok));
/* 120 */           textView.setTextColor(-6750208);
/* 121 */         } else if (!programmingModeConnectionInfo.serverIsAlive)
/*     */         {
/* 123 */           textView.setText(AbstractProgrammingModeActivity.this.getString(R.string.server_not_ok));
/* 124 */           textView.setTextColor(-6750208);
/*     */         } else {
/* 126 */           int flags = 131089;
/*     */           
/*     */ 
/* 129 */           String formattedDate = DateUtils.formatDateTime(AbstractProgrammingModeActivity.this, programmingModeConnectionInfo.timeServerStartedMillis, flags);
/*     */           
/*     */ 
/* 132 */           textView.setText(String.format(AbstractProgrammingModeActivity.this.getString(R.string.server_ok), new Object[] { formattedDate }));
/* 133 */           textView.setTextColor(-16738048);
/*     */         }
/* 135 */         textView.requestLayout();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setTypeface()
/*     */   {
/* 145 */     Typeface typeface = Typeface.create(Typeface.SERIF, 0);
/*     */     
/*     */ 
/*     */ 
/* 149 */     TextView textView = (TextView)findViewById(R.id.network_name);
/* 150 */     textView.setTypeface(typeface);
/* 151 */     textView.requestLayout();
/*     */     
/* 153 */     textView = (TextView)findViewById(R.id.passphrase);
/* 154 */     textView.setTypeface(typeface);
/* 155 */     textView.requestLayout();
/*     */     
/* 157 */     textView = (TextView)findViewById(R.id.server_url);
/* 158 */     textView.setTypeface(typeface);
/* 159 */     textView.requestLayout();
/*     */   }
/*     */   
/*     */   protected void addMessageToTextViewLog(final String msg) {
/* 163 */     Runnable updateTextViewLog = new Runnable()
/*     */     {
/*     */       public void run() {
/* 166 */         AbstractProgrammingModeActivity.this.textViewLog.setText(AbstractProgrammingModeActivity.this.textViewLog.getText() + msg + "\n");
/* 167 */         AbstractProgrammingModeActivity.this.textViewLog.requestLayout();
/*     */       }
/* 169 */     };
/* 170 */     runOnUiThread(updateTextViewLog);
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\Blocks-release.jar!\classes.jar!\com\google\blocks\AbstractProgrammingModeActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */