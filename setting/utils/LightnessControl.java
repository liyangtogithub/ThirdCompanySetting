package com.landsem.setting.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.view.CustomToast;
import com.ls.config.ConfigManager.LS_CONFIG_ID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.System;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressWarnings("serial")
public class LightnessControl implements Constant {

	private static final String TAG = LightnessControl.class.getSimpleName();
	private static final Integer NO_VALUE = -1;
	public static int LIGHT_MODE = INVALID_VALUE;
	/**
	 * 大灯的开关，暂默认白天，关闭状态
	 */
	public static int HEAD_LIGHT_MODE = DAYLIGHT_MODE;
	public static final int LIGHT_MIN = 15;
	public static int NIGHT_DEFAULT_LIGHTNESS;
	public static int DAY_DEFAULT_LIGHTNESS;
	private LightModeObserver lightModeObserver;
	
	
	public void setLightModeObserver(LightModeObserver lightModeObserver) {
		this.lightModeObserver = lightModeObserver;
	}

	public static void outerChangeLight(Context context, int progress, boolean mark){
		int temValue = MAX_LIGHT_VALUE / 2;
		if(!mark){
			LS_CONFIG_ID lsConfigID = LS_CONFIG_ID.CAR_CONFIG_RESERVED5;
			switch (LIGHT_MODE) {
			case DAYLIGHT_MODE:
				progress = progress < temValue ? temValue : progress;
				lsConfigID = LS_CONFIG_ID.CAR_CONFIG_RESERVED5;
				break;
			case NIGHTLIGHT_MODE:
				progress = progress > temValue? temValue : progress;
				lsConfigID = LS_CONFIG_ID.CAR_CONFIG_RESERVED6;
				break;
			default:
				break;
			}
			setLightness(context, progress*ZOON_DIGIT, mark);
//			SettingApp.getInstance().saveLightValue(progress);
		}
	}

	public static synchronized void initLightness() {
		String value = SettingApp.getConfigManager().getConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED5);
		if (!StringUtils.isBlank(value)) DAY_DEFAULT_LIGHTNESS = Integer.valueOf(value);
		value = SettingApp.getConfigManager().getConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED6);
		if (!StringUtils.isBlank(value)) NIGHT_DEFAULT_LIGHTNESS = Integer.valueOf(value);
	}

	// 判断是否开启了自动亮度调节
	public static boolean isAutoBrightness(Activity act) {
		boolean automicBrightness = false;
		ContentResolver aContentResolver = act.getContentResolver();
		try {
			automicBrightness = Settings.System.getInt(aContentResolver,
					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (Exception e) {
			CustomToast.makeText(act, "无法获取亮度").show();
		}
		return automicBrightness;
	}

	private static void showToast(Context context) {
		try {
			View layout = View.inflate(context, R.layout.toast_layout, null);
			Toast toast = new Toast(context);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(layout);
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 改变亮度
	public static void setLight(Activity act, int value) {
		try {
			System.putInt(act.getContentResolver(), System.SCREEN_BRIGHTNESS, value + LIGHT_MIN);
			WindowManager.LayoutParams lp = act.getWindow().getAttributes();
			lp.screenBrightness = (value <= 0 ? 1 : value) / 255f;
			act.getWindow().setAttributes(lp);
			saveBrightness(act.getContentResolver(), value);
		} catch (Exception e) {
			CustomToast.makeText(act, "无法改变亮度").show();
		}
	}

	public static synchronized void setLightness(Context context, int value, boolean sensitiveType) {
		try {
			if(!sensitiveType){
				
				changeLightness(context, value);
			}else{
				initLightness();
				switch (value) {
				case DAYLIGHT_MODE:
					LogManager.d(TAG, "day lightness :  " + DAY_DEFAULT_LIGHTNESS);
					changeLightness(context, DAY_DEFAULT_LIGHTNESS);
					break;
				case NIGHTLIGHT_MODE:
					LogManager.d(TAG, "night lightness :  " + NIGHT_DEFAULT_LIGHTNESS);
					changeLightness(context, NIGHT_DEFAULT_LIGHTNESS);
					break;
				default:break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			CustomToast.makeText(context, "无法改变亮度").show();
		}
	}

	@SuppressLint("ShowToast")
	private static void changeLightness(Context context, int value) {
		try {
//			System.putInt(context.getContentResolver(), System.SCREEN_BRIGHTNESS, value + LIGHT_MIN);
			saveBrightness(context.getContentResolver(), value+ LIGHT_MIN);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			showToast(context);
		}
	}

	public static void saveBrightness(ContentResolver resolver, int brightness) {
		LogManager.d(TAG, "saveBrightness :  " + brightness);
		Uri uri = android.provider.Settings.System .getUriFor(System.SCREEN_BRIGHTNESS);
		android.provider.Settings.System.putInt(resolver, System.SCREEN_BRIGHTNESS, brightness);
		resolver.notifyChange(uri, null);
	}

	public static int getScreenLightness(Context context) {
		return System.getInt(context.getContentResolver(), System.SCREEN_BRIGHTNESS, NO_VALUE);
	}

	public static void saveScreenBrightness(Activity act, int paramInt) {
		try {
			Settings.System.putInt(act.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	// 停止自动亮度调节
	public static void stopAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	// 开启亮度自动调节
	public static void startAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}
	
	private static SimpleDateFormat format = new SimpleDateFormat("HH");
	
//	public static int checkOutMode(){
//		String hour = format.format(new Date());
//		int hours = 8;
//		try {
//			hours = Integer.parseInt(hour);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		int futureMode = INVALID_VALUE;
//		if(hours>=18 || hours<=6){
//			futureMode = NIGHTLIGHT_MODE;
//		}else{
//			futureMode = DAYLIGHT_MODE;
//		}
//		BACKLIGHT_MODE = futureMode;
//		return BACKLIGHT_MODE;
//	}
	/**
	 * 根据时间判断白天、晚上，用来初始化LIGHT_MODE值
	 */
	public static boolean isLightModeChangedByTime(){
		String hour = format.format(new Date());
		int hours = 8;
		try {
			hours = Integer.parseInt(hour);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int futureMode = INVALID_VALUE;
		if(hours>=18 || hours<6){
			futureMode = NIGHTLIGHT_MODE;
		}else{
			futureMode = DAYLIGHT_MODE;
		}
		if(LIGHT_MODE!=futureMode){
			LIGHT_MODE = futureMode;
			return true;
		}
		return false;
	}
	/**
	 * 根据大灯亮灭，判断白天、晚上，用来初始化LIGHT_MODE值
	 */
	public static boolean isLightModeChangedByLight(){
		if(LIGHT_MODE!=HEAD_LIGHT_MODE){
			LIGHT_MODE = HEAD_LIGHT_MODE;
			return true;
		}
		return false;
	}
	
	public static void toNightMode() {
		HEAD_LIGHT_MODE = NIGHTLIGHT_MODE;
	}

	public static void toDayLightMode() {
		HEAD_LIGHT_MODE = DAYLIGHT_MODE;
    }

	public interface LightModeObserver{
		void onLightModeChange(int mode);
	}
	
}