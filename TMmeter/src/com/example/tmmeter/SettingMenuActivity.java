package com.example.tmmeter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

//import android.os.Bundle;

public class SettingMenuActivity extends Activity {
	private Bundle b;
	private int season;
	private Drawable dw;
	private Resources resources;
	private RadioGroup raGroup1; 
	private LinearLayout myLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load layout
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        b=getIntent().getExtras();
        //load layout
        setContentView(R.layout.setmenu_layout);
        season= b.getInt("SEASON");
        init();
        //通过findViewById获得RadioGroup对象  
        raGroup1=(RadioGroup)findViewById(R.id.radioGroup1);       
        //添加事件监听器  
        raGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
              
            @Override 
            public void onCheckedChanged(RadioGroup group, int checkedId) { 
            	//myLayout = (LinearLayout)findViewById(R.id.setMenu);
            	resources= getBaseContext().getResources(); 
                // TODO Auto-generated method stub  
                if(checkedId==R.id.SprintBtn){                    
                	season= ConstUtil.SEASON_SPRINT;
                	dw= resources.getDrawable(R.drawable.spring);  
        			myLayout.setBackground(dw);
                }  
                else if(checkedId==R.id.SummerBtn){  
                	season= ConstUtil.SEASON_SUMMER;
                	dw= resources.getDrawable(R.drawable.summer);  
        			myLayout.setBackground(dw);
                }  
                else if(checkedId==R.id.AutumnBtn){  
                	season= ConstUtil.SEASON_AUTUMN;
                	dw= resources.getDrawable(R.drawable.autumn);  
        			myLayout.setBackground(dw);
                }  
                else if(checkedId==R.id.WinterBtn){  
                	season= ConstUtil.SEASON_WINTER;
                	dw= resources.getDrawable(R.drawable.winter);  
        			myLayout.setBackground(dw);
                }  
                
            }  
        });  
    }
	
	private void init(){
		myLayout = (LinearLayout)findViewById(R.id.setMenu);
		resources= getBaseContext().getResources(); 
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
;