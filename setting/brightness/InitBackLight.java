package com.landsem.setting.brightness;

import android.content.Context;
import android.provider.Settings;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.SettingApp;
import com.landsem.setting.Constant.Key;
import com.landsem.setting.utils.LightnessControl;
import com.ls.lseasycontrol.LSEasyControlManager;

public class InitBackLight {
	
	public static void updateBrightnessStatus(Context context) {
		
		if(getCurrentScreenModel( context) == SCREEN_MODEL.screenModelNight) {
			LogManager.d("InitBackLight  toNightMode");
        	LightnessControl.toNightMode();
			//借用时间改变时，改变背光和按键灯的逻辑
			SettingApp.getInstance().onTimeChanged();
			setPanelLight(context,1);
        }else {
        	LogManager.d("InitBackLight  toDayLightMode");
        	LightnessControl.toDayLightMode();
			//借用时间改变时，改变背光和按键灯的逻辑
			SettingApp.getInstance().onTimeChanged();
			setPanelLight(context,0);
        }
	}
	
	/***Set_Panel_Light_State（int value）方法，value为1是设置灯亮，为0设置灯灭
	 * @param context */
	 private static void setPanelLight(Context context, int value) {
		 LSEasyControlManager mLSEasyControlManager = new LSEasyControlManager(context);
		 mLSEasyControlManager.Set_Panel_Light_State(value);
	}

	public static SCREEN_MODEL getCurrentScreenModel(Context context) {
	        int val = getScreenModel(context);
	        SCREEN_MODEL[] models = SCREEN_MODEL.values();
	        for (SCREEN_MODEL model : models) {
	            if (model.ordinal() == val) {
	                return model;
	            }
	        }
	        // other status we think it's day model.
	        return SCREEN_MODEL.screenModelDay;
	    }    
		
		  protected static int getScreenModel(Context context) {
		        int model = -1;
		        try {
		            model = Settings.System.getInt(context.getContentResolver(), Key.HEADLAMP_STATE);
		        } catch (Exception e) {
		        	e.printStackTrace();
		            LogManager.e("Not found setting item for " + Key.HEADLAMP_STATE);
		        } finally {
		            return model;
		        }
		    }

		
		protected enum SCREEN_MODEL {
	        screenModelDay, /* backlight work in day model. */
	        screenModelNight, /* backlight work in night model. */
	    }

}
