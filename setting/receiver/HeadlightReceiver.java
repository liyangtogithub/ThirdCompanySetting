package com.landsem.setting.receiver;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;
import com.landsem.setting.brightness.InitBackLight;
import com.landsem.setting.utils.LightnessControl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

@SuppressWarnings("serial")
public class HeadlightReceiver extends BroadcastReceiver implements Constant {

	private static final String TAG = HeadlightReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		LogManager.d("HeadlightReceiver  action:  "+action);
		InitBackLight.updateBrightnessStatus(context);
		/*switch (action) {
		case Action.HEADLIGHT_ON:
			//LightnessControl.setLightness(context, LightnessControl.DAYLIGHT_MODE, true);
			LightnessControl.toNightMode();
			//借用时间改变时，改变背光的逻辑
			SettingApp.getInstance().onTimeChanged();
			break;
		case Action.HEADLIGHT_OFF:
			//LightnessControl.setLightness(context, LightnessControl.NIGHTLIGHT_MODE, true);
			//SettingApp.getInstance().changeLightMode();
			LightnessControl.toDayLightMode();
			//借用时间改变时，改变背光的逻辑
			SettingApp.getInstance().onTimeChanged();
			break;
		default:
			break;
		}*/
	}

}
