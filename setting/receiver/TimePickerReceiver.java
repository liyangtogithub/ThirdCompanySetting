package com.landsem.setting.receiver;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.ObjectUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

@SuppressWarnings("serial")
public final class TimePickerReceiver extends BroadcastReceiver implements Constant{

	private static final String TAG = TimePickerReceiver.class.getSimpleName();
	private Context context;


	@Override
	public void onReceive(Context context, Intent intent) {
//		LogManager.d(TAG, "time changed    &&&&&&    doCheckStorage");
//		SettingApp.getInstance().changeLightMode();
		
		//SettingApp.getInstance().onTimeChanged();现改用大灯亮灭，判断白天晚上，故暂时屏蔽时间判断逻辑
	}

	public void register(Context context) {
		if(!ObjectUtils.isEquals(context, null)){
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_TIME_TICK);
			filter.addAction("android.intent.action.TIME_SET");
			filter.addAction(Intent.ACTION_DATE_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			context.registerReceiver(this, filter);
		}
	}

	
	public void unregister(Context context){
		if(!ObjectUtils.isEquals(context, null)){
			context.unregisterReceiver(this);
		}
	}
	
	
	
	
}
