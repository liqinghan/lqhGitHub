package com.example.tmmeter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.EncodingUtils;



import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

public class SensorService extends Service  {
	private RemoteViews remoteViews;
	final static int UPDATE=0x168;
	final static int UPDATA1=0x169;
	final static int UPDATA2=0x170;
	private float value = -999f,value1=0.5f; 
	private SensorEventListener mSensorListener;
	private Sensor mSensor,mSensor1;
	private SensorManager mSensorManager;
	private String fileString;
	private int ShowFflag;
	@Override
		public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
			return null;
		}
	
	 @Override  
	    public void onCreate() {
		 
		 super.onCreate();
		 
		 System.out.println("SensorService create ");
		 
		 remoteViews = new RemoteViews(getApplication().getPackageName(), R.layout.sensorwidgetlayout); 
		 
		 Intent intent = new Intent(getApplicationContext(),MainActivity.class); 
	 	 PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0); 
	 	 remoteViews.setOnClickPendingIntent(R.id.yunduoImage, pendingIntent);
	 	 
	 	 mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	 	 mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY  );
	 	 mSensor1 = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    	
	 	
	 	 mSensorListener = new SensorEventListener(){
   		 @Override 
          public void onAccuracyChanged(Sensor sensor, int accuracy) {  
          }  
         
			@Override 
            public void onSensorChanged(SensorEvent event) { 					   
                   if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                   		value= event.values[0];  
                   		Message msg = handler.obtainMessage();  
                        msg.what = UPDATA1;  
                        handler.sendMessage(msg);  
                   	}
                    else if(event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
                    	value1 =event.values[0]; 
                    	Message msg = handler.obtainMessage();  
                        msg.what = UPDATA2;  
                        handler.sendMessage(msg);  
                   }
              }  
   		}; 
   		
	 	 new Timer().scheduleAtFixedRate(new TimerTask() {
	 	 @Override
	 	 public void run() {
	 		Message msg = handler.obtainMessage();  
            msg.what = UPDATE;  
            handler.sendMessage(msg);  
	 	 	}
	 	 }, 1, 1000);// 每小时更新一次天气  
	 }

	 	private Handler handler = new Handler() { 
	 		@Override  
	 		public void handleMessage(Message msg) {  
	 			switch (msg.what) { 
	 			case UPDATE:
	 				updateDate(1);
	 				break;
	 			case UPDATA1:
	 				updateDate(2);
	 				break;
	 			case UPDATA2:
	 				updateDate(3);
	 				break;
	 			default: break;  
	 			}  
	 		} 
	 	}; 
	 
	 	private BroadcastReceiver mTimePickerBroadcast = new BroadcastReceiver() {  
		  
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	           // updateTime();  
	        }  
	    };
	    
	    private void updateDate(int flag){
	    	if(flag == 1){
	    	SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 		Date curDate = new Date(System.currentTimeMillis());//获取当前时间  
	 		String str = formatter.format(curDate);  
	 		remoteViews.setTextViewText(R.id.yunduoImage, str); 
	 		}
	    	else if(flag == 2){
	    		String sign="";
	        	if(ShowFflag ==1){
	        		value = value*9/5+32;
	        		sign ="F";
	        	}else sign ="℃";
	        	BigDecimal bd = new BigDecimal(value); 
	        	bd = bd.setScale(1,4);
	        	value = bd.floatValue();
	    		String str = ""+value+sign;
		 		remoteViews.setTextViewText(R.id.WidgetString, str);
		 		
	    	}else if(flag ==3){
	    		BigDecimal bd = new BigDecimal(value1); 
	        	bd = bd.setScale(1,4);
	        	value1 = bd.floatValue();
	    		String str1 = ""+value1+"%";
	    		remoteViews.setTextViewText(R.id.WidgetString1, str1);
	    	}
	 		ComponentName componentName = new ComponentName(getApplication(),  
	 				SensorAppWidget.class);  
	        AppWidgetManager.getInstance(getApplication()).updateAppWidget(  
	                componentName, remoteViews);
	   }
	    
	    public boolean OpenSdcardFile(){
	    	String res="";
	    	String sdStatus = Environment.getExternalStorageState();
	    	if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
	    		Log.d("TestFile", "SD card is not avaiable/writeable right now.");
	    		return false;
	    	}
	    	try {
	    		String fileName="sensor.txt";
	    		String pathName="/sdcard/sensorFors4/";
	    		File path = new File(pathName);
	    		File file = new File(pathName + fileName);
	    		if( !path.exists()) {
	    			Log.i("TestFile", "Create the path:" + pathName);
	    			path.mkdir();
	    		}
	    		if(!file.exists()) {
	    			Log.i("TestFile", "Create the file:" + fileName);
	    			file.createNewFile();
	    			FileOutputStream stream = new FileOutputStream(file);
	    			res =ConstUtil.defaultFile;
	        		byte[] midbytes=res.getBytes("UTF8");
	            	stream.write(midbytes);           
	            	stream.close();
	    		}
	    		FileInputStream stream = new FileInputStream(file);
	    		int length = stream.available();     		
	        	byte [] buffer = new byte[length];  
	        	stream.read(buffer);   
	        	res = EncodingUtils.getString(buffer, "UTF-8");     
	        	stream.close();
	        	fileString=res;
	        	return true;
	    	} catch(Exception e) {
	    		Log.e("TestFile", "Error on ReadFilToSD."+e.toString());
	    		e.printStackTrace();
	    	}
	    	
			return false;
	   }
	    
	    public int phraseString(){
	    	int i,count=0,count1=0;
	    	char ch =':';
	    	for(i=0;i<fileString.length();i++){
	    		if(fileString.charAt(i) == '$'){
	    			count1=i;
	    		}
	    		if(fileString.charAt(i) == ch){
	    			count =i;
	    			String getStr = fileString.substring(count1+2, count);
	    			String IntStr = fileString.substring(i+1, i+2);
	    			count++;	    			
	    			if(getStr.equals(new String("showfflag"))){
	    				if(IntStr!=null)
	    					ShowFflag=Integer.parseInt(IntStr);	    				
	    			}
	    		}
	    	}
	    	return 0;
	    }
	    	    
	    @Override  
	    public void onStart(Intent intent, int startId) {  
	        // 注册系统每分钟提醒广播（注意：这个广播只能在代码中注册）  
	        IntentFilter updateIntent = new IntentFilter();  
	        updateIntent.addAction("android.intent.action.TIME_TICK");  
	        registerReceiver(mTimePickerBroadcast, updateIntent); 
	        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
	        mSensorManager.registerListener(mSensorListener, mSensor1, SensorManager.SENSOR_DELAY_NORMAL);
	        if(OpenSdcardFile()){ 
	    		phraseString();
	    	}	    
	        System.out.println("Strart service");
	        super.onStart(intent, startId);  
	    } 
	    
	    @Override  
	    public void onDestroy() {  
	        // 注销系统的这个广播  
	        unregisterReceiver(mTimePickerBroadcast);  
	        //被系统干掉后，服务重启,做一次流氓软件,哈哈  
	        //Intent intent = new Intent(getApplicationContext(), SensorService.class);  
	        //getApplication().startService(intent);  
	        System.out.println("remove service");
	        super.onDestroy();  
	    }  
	    		        
}
