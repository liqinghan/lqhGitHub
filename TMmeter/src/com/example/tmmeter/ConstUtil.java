package com.example.tmmeter;

public class ConstUtil {
	public final static int SEASON_SPRINT=0;
	public final static int SEASON_SUMMER=1;
	public final static int SEASON_AUTUMN =2;
	public final static int SEASON_WINTER=3;
	
	public final static int RESULT_CODE =0;
	
	public final static String filePath= "/sdcard/sensorFors4/";
	public final static String fileName= "sensor.txt";
	public final static String SEASON_KEY = "SEASON"; 
	public final static String F_FLAG ="SHOWF";
	
	public final static String FILE_VAR1 = "season";
	public final static String FILE_VAR2 = "showfflag";
	
	public final static String defaultFile="$1showfflag:0"+"\n"+
				"$2flashscreenflag:1"+"\n"+
				"$32showhelpflag:1"+"\n"+
				"$4lockupdateflag:1"+"\n"+
				"$5standbytimeoutflag:1"+"\n"+
				"$6dateformatflag:1"+"\n"+
				"$7autoupdateflag:1"+"\n"+
				"$8updatetimeflag:1"+"\n"+
				"$9selbackflag:1"+"\n";
}
