package com.example.tmmeter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TempMenuActivity extends Activity {
	private List<Map<String , String>> listdata; 
	private ListView menuLv;
	private ListAdapter menuLvAdapter;
	int scroll_num = 10;
	ViewHolder holder;
	ViewHolder1 holder1;
	ArrayList<Map<String, String>> data;
	HashMap<Integer,Boolean> isSelected;
	MyAdapter adapter;
	private Bundle b;
	private int season;
  	private RadioGroup raGroup1; 
  	private int ShowFflag;
  	private int flashScreen;
    private Drawable dw;
	private Resources resources;
	private LinearLayout myLayout;
	
	private String[] ItemStr = new String[] {
			"温度计","设置",
			"显示华氏度","显示闪屏","显示帮助界面","解锁更新","屏幕待机",
			"日期格式","自动更新","更新间隔","选择背景","关于"
	};
	private String[] ItemStr1 = new String[]{
			"","",
			"显示值显示华氏度","","每次打开软件显示","解锁更新桌面数据","设置屏幕超时时间",
			"设置日期的显示格式","自动更新桌面数据","自动更新数据间隔时间","选择背景样式",""	
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
              WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.menusetting_layout);
        menuLv= (ListView)findViewById(R.id.menuListView);
        InitData();
        InitListView();
        menuLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(arg2){
				case ItemElement.ITEM_SHOWF:break; 
				case ItemElement.ITEM_FLASHSCREEN:break; 
				case ItemElement.ITEM_SHOWHELP:break;
				case ItemElement.ITEM_LOCKUPDATE:break;
				case ItemElement.ITEM_STANDBYTIME:break;
				case ItemElement.ITEM_TIMEFORMAT:break; 
				case ItemElement.ITEM_AUTOUPDATE:break;
				case ItemElement.ITEM_SELBG: 
					showSetBg();
					break;
				case ItemElement.ITEM_ABOUTAPP:
					ShowAboutApp();
					break;
				default:break;
				}
				
			}
		});      
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			b.putInt(ConstUtil.SEASON_KEY, season);	
			b.putInt(ConstUtil.F_FLAG, ShowFflag);
			Intent MainIntent = new Intent();//(TempMenuActivity.this,MainActivity.class);
			MainIntent.putExtras(b);
			setResult(ConstUtil.RESULT_CODE,MainIntent);
			if(OpenSdcardFile()) System.out.println("Write file success ");
			else System.out.println("Write file fail ");
			finish();
					
		}
		return super.onKeyLongPress(keyCode, event);
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		
	}

	private void showSetBg(){
		
		final CharSequence[] items = {"春","夏","秋","冬"};
		final int beforeSeason;
		AlertDialog.Builder dlg_builder = new AlertDialog.Builder(this);
		dlg_builder.setTitle("设置背景图片");
		beforeSeason = season;
		dlg_builder.setSingleChoiceItems(items, season, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				season = which;
			}
		});
		dlg_builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		dlg_builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				season = beforeSeason;
			}
		});
		AlertDialog dlg = dlg_builder.create();
		dlg.getWindow().setGravity(Gravity.CENTER);
		dlg.setCanceledOnTouchOutside(true);
		dlg.show();
		
	}
	private void ShowAboutApp(){
		final CharSequence str="此温度由sss设计！\n"+"软件版本V1.0\n"+"有问题请反馈到576791284@qq.com\n" +
				"\n";
		AlertDialog.Builder dlg_builder = new AlertDialog.Builder(this);
		dlg_builder.setTitle("温度计");
		AlertDialog dlg = dlg_builder.create();
		dlg.setCanceledOnTouchOutside(true);
		dlg.setMessage(str);
		dlg.show();
	}
	private void SetItemFunc(int position,int checkFlag){
		switch(position){
		case ItemElement.ITEM_SHOWF: ShowFflag=checkFlag;break;
		default: break;
		}
	}
	public void InitListView1(){     
        SimpleAdapter adapter = new SimpleAdapter(this,  
        								listdata,  
                                        android.R.layout.simple_list_item_2,        // List 显示两行item1、item2  
                                        new String[]{ "TITLE", "CONTENT" },  
                                        new int[]{ android.R.id.text1, android.R.id.text2 }  
                                        );  
          
        menuLv.setAdapter(adapter);  
	}
	public void InitData(){
		listdata = new ArrayList<Map<String, String>>();  
		data = new ArrayList<Map<String, String>>();
		for(int i=0; i<ItemStr.length; i++){  
			Map<String, String> Tempmap = new HashMap<String, String>();   
			Tempmap.put("TITLE", ItemStr[i]);  
			Tempmap.put("CONTENT",ItemStr1[i]);  
			listdata.add(Tempmap);  
			data.add(Tempmap);
	      } 
		b=getIntent().getExtras();
	    season= b.getInt(ConstUtil.SEASON_KEY);	
	    ShowFflag = b.getInt(ConstUtil.F_FLAG);
		myLayout = (LinearLayout)findViewById(R.id.setMenu);
		resources= getBaseContext().getResources(); 
	}
	
	public void InitListView(){
	    adapter = new MyAdapter(data,TempMenuActivity.this);
		menuLv.setAdapter(adapter);
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
    		}
    		FileOutputStream stream = new FileOutputStream(file);
    		res =CreateFile();
    		byte[] midbytes=res.getBytes("UTF8");
        	stream.write(midbytes);           
        	stream.close();
        	return true;
    	} catch(Exception e) {
    		Log.e("TestFile", "Error on writeFilToSD.");
    		e.printStackTrace();
    	}
		return false;
   }
	public String CreateFile(){
		String retStr="";
		String showf;
		String selbackflag;
		showf=String.valueOf(ShowFflag);
		selbackflag=String.valueOf(season);
		retStr ="$1showfflag:"+showf+"\n"+
				"$2flashscreenflag:1"+"\n"+
				"$32showhelpflag:1"+"\n"+
				"$4lockupdateflag:1"+"\n"+
				"$5standbytimeoutflag:1"+"\n"+
				"$6dateformatflag:1"+"\n"+
				"$7autoupdateflag:1"+"\n"+
				"$8updatetimeflag:1"+"\n"+
				"$9selbackflag:"+selbackflag+"\n";
		
		return retStr;
	}
class MyAdapter extends BaseAdapter{
	int count = scroll_num;
	Context mContext;		
	List<Map<String, String>> mData;
	LayoutInflater mInflater;
	
	public MyAdapter(ArrayList<Map<String, String>>data,Context context) {
		this.mContext = context;
		this.mData = data;
		mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
		if(count>mData.size()){
			count = mData.size();
		}
		isSelected = new HashMap<Integer,Boolean>();
		for(int i = 0; i<data.size(); i++){
			isSelected.put(i, false);
		}
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}
	
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if(position ==0){
			return mInflater.inflate(R.layout.listview_title, null);
		}
		if(position == 1){
			return mInflater.inflate(R.layout.listview_title1, null);
		}
		if( (position ==ItemElement.ITEM_STANDBYTIME ||position ==ItemElement.ITEM_TIMEFORMAT ) ||
			(position >=ItemElement.ITEM_UPDATEINTERVAL && position<= ItemElement.ITEM_ABOUTAPP) ){
			if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_listview1, null);
			holder1 = new ViewHolder1();
			holder1.title = (TextView) convertView.findViewById(R.id.item_listview_title1);
			holder1.content = (TextView) convertView.findViewById(R.id.item_listview_content1);		
			convertView.setTag(holder1);
			}else{
				holder1 = (ViewHolder1) convertView.getTag();
			}
			holder1.title.setText(data.get(position).get("TITLE").toString());
			holder1.content.setText(data.get(position).get("CONTENT").toString());
			return convertView;
		}
		else{
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.item_listview, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.item_listview_title);
				holder.content = (TextView) convertView.findViewById(R.id.item_listview_content);		
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_listview_checkbox);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
		
			holder.title.setText(data.get(position).get("TITLE").toString());
			holder.content.setText(data.get(position).get("CONTENT").toString());
			if(position == ItemElement.ITEM_SHOWF){
				if(ShowFflag ==1){
					holder.checkBox.setChecked(true);
					isSelected.put(position, true);
				}
				else {
					holder.checkBox.setChecked(false);
					isSelected.put(position, false);
				}
			}
			else
			holder.checkBox.setChecked(isSelected.get(position));
			holder.checkBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isSelected.get(position)){
					isSelected.put(position, false);
					Log.i("CheckBox","unChecked ");
					SetItemFunc(position,0);
				}
				else {
					isSelected.put(position, true);
					Log.i("CheckBox","Checked ");
					SetItemFunc(position,1);
				}
				
			}
			});
			return convertView;
		}
	}
}

class ViewHolder{
	TextView title;
	TextView content;
	CheckBox checkBox;
}

class ViewHolder1{
	TextView title;
	TextView content;
	}
}
class ItemElement{
	public final static int ITEM_SHOWF=2;
	public final static int ITEM_FLASHSCREEN=3;
	public final static int ITEM_SHOWHELP =4;
	public final static int ITEM_LOCKUPDATE=5;
	public final static int ITEM_STANDBYTIME =6;
	public final static int ITEM_TIMEFORMAT = 7; 
	public final static int ITEM_AUTOUPDATE = 8; 
	public final static int ITEM_UPDATEINTERVAL= 9; 
	public final static int ITEM_SELBG = 10; 
	public final static int ITEM_ABOUTAPP = 11; 
}

