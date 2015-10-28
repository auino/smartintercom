package com.auino.smartintercom;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.auino.smartintercom.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.RemoteViews;

public class ActivityWidget extends AppWidgetProvider {
	public static String WIDGET_BUTTON = "com.auino.smartintercom.clickbutton";
	
	 @Override
	 public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		 context.getApplicationContext().registerReceiver(this,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		 updateUI(context);
	 }
	
	 public static boolean openWebPage(String url) {
		 Log.d("SmartIntercom", "Opening URL: "+url);
		 try {
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			 //InputStream inputStream = httpResponse.getEntity().getContent();
			 httpResponse.getEntity().getContent();
		 } catch (Exception e) {
			 e.printStackTrace();
			 return false;
		 }
		 return true;
	 }
	 
	 @Override
	 public void onReceive(final Context context, Intent intent) {
		 //Log.d("SmartIntercom", intent.getAction().toString());
		 
		 if (WIDGET_BUTTON.equals(intent.getAction())) {
			 new Thread(new Runnable() {
				   public void run() {
					   //Log.d("SmartIntercom", "Button clicked!!");
					   String url = context.getResources().getString(R.string.triggeredurl);
					   openWebPage(url);
				   }                        
			 }).start();
		 }
		 
		 updateUI(context);
		 super.onReceive(context, intent);
	 }

	 private void updateUI(Context context) {
		 RemoteViews thisViews = new RemoteViews(context.getApplicationContext() .getPackageName(), R.layout.widget_layout);
		 
		 Intent intent = new Intent(WIDGET_BUTTON);
		 PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 thisViews.setOnClickPendingIntent(R.id.clickbutton, pendingIntent);
		 
		 ComponentName thisWidget = new ComponentName(context, ActivityWidget.class);
		 AppWidgetManager.getInstance(context).updateAppWidget(thisWidget,thisViews);
	 }
}