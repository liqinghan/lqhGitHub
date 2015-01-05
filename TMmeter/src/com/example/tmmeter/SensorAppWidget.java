package com.example.tmmeter;
import java.sql.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;   
import android.appwidget.AppWidgetProvider;   
import android.content.ComponentName;
import android.content.Context;   
import android.content.Intent;
import android.os.Message;
import android.widget.RemoteViews;

public class SensorAppWidget extends AppWidgetProvider {
	private Intent intent;
	private RemoteViews remoteViews;
	 @Override  
	 public void onUpdate(Context context, AppWidgetManager appWidgetManager,   
	            int[] appWidgetIds) { 
		 
		 intent = new Intent(context, SensorService.class);  
	     context.startService(intent);  		  
	     super.onUpdate(context, appWidgetManager, appWidgetIds);        
	    } 
	 	
           
	 @Override
	 	public void onDeleted(Context context, int[] appWidgetIds) {
			System.out.println("onDeleted");
		 	if(intent!=null)
			context.stopService(intent);  
			super.onDeleted(context, appWidgetIds);
		}
	 @Override
		public void onDisabled(Context context) {
			System.out.println("onDisabled");
			super.onDisabled(context);
		}
	 @Override
		public void onEnabled(Context context) {
			System.out.println("onEnabled");
			super.onEnabled(context);
		}
	 
	 @Override
	 public void onReceive(Context context, Intent intent){
		 super.onReceive(context, intent);
		}
    
}

