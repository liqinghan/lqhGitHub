package com.example.tmmeter;

//import android.content.Context;  
//import android.hardware.Sensor;  
//import android.hardware.SensorEvent;  
//import android.hardware.SensorEventListener;  
//import android.hardware.SensorManager; 


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.http.util.EncodingUtils;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context; 
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;  
import android.hardware.SensorEvent;  
import android.hardware.SensorEventListener;   
import android.hardware.SensorManager;


public class MainActivity extends Activity {
	
	private TextView  TempText,TempText1;
	private ImageView ig,ig1;
	private SensorManager mSensorManager;
	private int season;
	private int ShowFflag;
    private SensorEventListener mSensorListener;
    private Sensor mSensor,mSensor1;
	private LinearLayout myLayout;
	private Resources resources;
	private Drawable dw;
    private float value = -999f; 
    private String fileString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //load layout
        setContentView(R.layout.activity_main);
    
        //initial layout item
		init();
        //apply a system service
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //if(mSensorManager == null) System.out.println("get sensor Manager fail");
        //else{System.out.println("get sensor Manager Success");}
    	mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY  );
    	mSensor1 = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    	//
    	mSensorListener = new SensorEventListener(){
    		 @Override 
             public void onAccuracyChanged(Sensor sensor, int accuracy) {  
             }  
          
			@Override 
             public void onSensorChanged(SensorEvent event) { 					   
                    if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    	value= event.values[0];              
                    	SetTempText(0);
                    	}
                    else if(event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
                    	value =event.values[0];
                    	SetTempText(1);
                    }
                   }  
    			}; 
    		
   }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_settings:
    		Intent intent=new Intent();
    	    intent.setClass(MainActivity.this, TempMenuActivity.class);
    	    intent.putExtra(ConstUtil.SEASON_KEY, season);
    	    intent.putExtra(ConstUtil.F_FLAG, ShowFflag);
            startActivityForResult(intent,ConstUtil.RESULT_CODE);
    		return true;
    	case R.id.action_quit:
    		finish();
    		//android.os.Process.killProcess(android.os.Process.myPid()); 
    		return true;
    	}
    	return false;
    }
    private void init() {
    	TempText = (TextView) findViewById(R.id.tempString);
    	TempText1 = (TextView)findViewById(R.id.tempString1);
    	
    	String tempString = ""; 
    	TempText.setText(tempString);
    	TempText1.setText(tempString);
    	ig = (ImageView) findViewById(R.id.tempImage);
    	ig.setImageResource(R.drawable.tempbg);
    	ig1 = (ImageView) findViewById(R.id.tempImage1);
    	ig1.setImageResource(R.drawable.shuidi);
    	if(OpenSdcardFile()){ 
    		//Log.i("TestFile", "read SDcard file is success");
    		phraseString();
    	}
    	//else  Log.i("TestFile", "read SDcard file is fail");
    	
    	
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
    			//System.out.println(getStr);
    			//System.out.println(IntStr);
    			
    			if(getStr.equals(new String("showfflag"))){
    				if(IntStr!=null)
    					ShowFflag=Integer.parseInt(IntStr);
    				//System.out.println("season is"+ ShowFflag);
    			}
    			else if(getStr.equals(new String("selbackflag"))){
    				if(IntStr!=null)
    					season=Integer.parseInt(IntStr);
    				//System.out.println("season is"+ season);
    			}
    		}
    	}
    	return 0;
    }
    @Override
    protected void onResume() {
       super.onResume();
       mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
       mSensorManager.registerListener(mSensorListener, mSensor1, SensorManager.SENSOR_DELAY_NORMAL);
       SetLayoutBG();
      }
    @Override
    public void onDestroy(){
    	super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
    	if(requestCode == ConstUtil.RESULT_CODE){
    		int var = data.getExtras().getInt(ConstUtil.SEASON_KEY);
    		ShowFflag = data.getExtras().getInt(ConstUtil.F_FLAG);
    		if(var != season){
    			season =var;
    			SetLayoutBG();
    		}
    		SetTempText(0);
    	} 
    }
    
    public void SetTempText(int flag){
    	switch(flag){
    	case 0:
    	
    	if(ShowFflag==1){
    		value = value*9/5+32;
    		BigDecimal bd = new BigDecimal(value); 
        	bd = bd.setScale(1,4);
        	value = bd.floatValue();
    		TempText.setText("ÎÂ¶È\n"+value+"F");  
    	}
		else{
			BigDecimal bd = new BigDecimal(value); 
        	bd = bd.setScale(1,4);
        	value = bd.floatValue();
        	TempText.setText("ÎÂ¶È\n"+value+"¡æ");  
        }break;
    	case 1:
    		BigDecimal bd = new BigDecimal(value); 
        	bd = bd.setScale(1,4);
        	value = bd.floatValue();
        	TempText1.setText("Êª¶È\n"+value+"%");  
    		break;
    	default:
    		break;
    	}
    }
    
   public void SetLayoutBG(){
	   resources= getBaseContext().getResources(); 
	   myLayout = (LinearLayout)findViewById(R.id.AppLayout);
	    switch(season){
		case ConstUtil.SEASON_SPRINT: 
			dw= resources.getDrawable(R.drawable.spring);  
			myLayout.setBackground(dw);   
			break;
		case ConstUtil.SEASON_SUMMER: 
			dw= resources.getDrawable(R.drawable.summer);  
			myLayout.setBackground(dw);   
			break;
		case ConstUtil.SEASON_AUTUMN:  
			dw= resources.getDrawable(R.drawable.autumn);  
			myLayout.setBackground(dw);   
			break;
		case ConstUtil.SEASON_WINTER:  
			dw= resources.getDrawable(R.drawable.winter);  
			myLayout.setBackground(dw);   
			break;
		default: break;
	    }
    }
}
