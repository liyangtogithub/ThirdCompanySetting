package com.landsem.setting.receiver;

import java.io.File;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.activities.GradeMapActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SDCardReceiver extends BroadcastReceiver {  
    //当sd卡被卸载或者被挂载上来的时候会执行  
	public static final String UP_SOURCE_FILE = "/mnt/extsd/landsemAutoCopy/";
	public static final String UP_SOURCE_FILE1 = "/mnt/extsd2/landsemAutoCopy/";
	public static final String UP_SOURCE_FILE2 = "/mnt/usbhost/Storage01/landsemAutoCopy/";
	public static final String UP_SOURCE_FILE3 = "/mnt/usbhost/Storage02/landsemAutoCopy/";
	public static final String UP_DEST_FILE_PATH = "/mnt/sdcard/";
	String mapPathString = UP_SOURCE_FILE;
    @Override  
    public void onReceive(Context context, Intent intent) {  
        //[1]获取到当前广播的事件类型   
        String action = intent.getAction();  
        LogManager.d("SDCardReceiver  action :"+action);
        //[2]对action做一个判断   
        if("android.intent.action.MEDIA_UNMOUNTED".equals(action)){  
            LogManager.d("MEDIA_UNMOUNTED");  
  
        }else if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {  
        	 LogManager.d("MEDIA_MOUNTED"); 
        	 if (isNeedUpgrade()) {
        		 GradeMapActivity.goToGradeMapActivity(context,mapPathString);
			}
        }  
    }  
    
    private boolean isNeedUpgrade() {
    	
		boolean sourceExist = getRightPath(UP_SOURCE_FILE);;
		if (!sourceExist) {
			sourceExist = getRightPath(UP_SOURCE_FILE1);
			if (!sourceExist) {
				sourceExist = getRightPath(UP_SOURCE_FILE2);
				if (!sourceExist) {
					sourceExist = getRightPath(UP_SOURCE_FILE3);
				}
			}
		}
		LogManager.d("SDCardReceiver  mapPathString :"+mapPathString); 
		File fileDest = new File(UP_DEST_FILE_PATH);
		boolean destPathExist = fileDest.isDirectory();
		if (sourceExist && destPathExist) {
			return true;
		}
		return false;
	}

	private boolean getRightPath(String upSourceFile) {
		mapPathString = upSourceFile;
		File fileSource = new File(upSourceFile);
		return fileSource.isDirectory();
	}
  
}  
