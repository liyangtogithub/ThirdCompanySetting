package com.landsem.setting;

import java.io.Serializable;

public interface Constant extends Serializable {

	int HEADLAMP_ON 				= 0x01;
	int HEADLAMP_OFF 				= 0x00;
	int NONE						= 0x00;
	int SECOND 						= 1000;
	int STATE_ON 					= 1;
	int STATE_OFF 					= 0;
	int INVALID_VALUE 				= -1;
	int CHANGE_STATE_AP_ENABLED 	= 135;
	int CHANGE_STATE_AP_DISENABLED 	= 136;
	int FM_FREQ_MIN 				= 10000;//k //FM 放大100倍
	int FM_FREQ_MAX 				= 10800;//k
	int BAND_FM 					= 1;
	int FM_STEP 					= 5;//10k
	int BAND_SCOPE 					= FM_FREQ_MAX - FM_FREQ_MIN;
	int MAX_LIGHT_VALUE 			= 30;
	int ZOON_DIGIT					= 8;
	String COMMA					= ",";
	String URI_EMPTY 				= "bluetooth.phone.dir/setting";
	int DAYLIGHT_MODE = 0x01;
	int NIGHTLIGHT_MODE = 0x02;


	interface Key {
		String STANDBY_DURATION 		= "standby_duration";
		String DELAY_ACC 				= "delay_acc";
		String ALARM_SCALE 				= "alarm_scale";
		String sortType 				= "sortType";
//		String DAY_LIGHT_VALUE 			= "day_light_value";
//		String NIGHT_LIGHT_VALUE 		= "night_light_value";
		String MAX_LIGHT 				= "max_light";
		String LOGO_NAME 				= "logo_name";
		String ECAR_TEL 				= "ecar_tel";
		String DEVICE_SN 				= "device_sn";
		String HTTP_PORT 				= "tsp_http_port";
		String HTTP_HOST 				= "tsp_http_host";
		String WITHTABS 				= "withtabs";
		String BACKGROUND_TYPE 			= "com.ls.backgroundtype";
		String MAP_PACKAGE 				= "com.ls.map_package";
		String MAP_VERTION 				= "com.map_vertion";//没有此值，默认ar导航
		String NAVIGATION 				= "navigation";
		String SPLIT_FLAG 				= "=";
		String MAP_CLASS 				= "com.ls.map_class";
		String ALLOW_ALTER_CONFIG 		= "allow_alter_config";
		String LOCK_SCREEN_DT 			= "lock_screen_dt";
		String DATE_FORMAT 				= "dateFormat";
		String HEADLAMP_STATE 			= "com.ls.headlamp_state";
		String FM_FUTURE_STATE			= "fmFutureState";
		String CUSTOM_HZ				= "customHZ";
		String STANDBY_SWITCH			= "StandBySwitch";
		String SIMPLEMODE_SWITCH 		= "simpleModeSwitch";
		String MODE_DURATION 			= "modeDuration";
		String SCREEN_SWITCH			= "screenSwitch";
		String FM_STATE 				= "fmState";
		String FM_HZ 					= "fmHZ";
		String LAUNCH_STATUS			= "launchStatus";
		String WFSTATUS_BEF_ACCOFF		= "wfStatusBefAccoff";
		String APSTATUS_BEF_ACCOFF		= "apStatusBefAccoff";
	}
	
	
	interface ClientId{
		int COMMON		= 0x00;
		int SUO_HANG 	= 0x03;
		int ROAD_ROVER 	= 0x05;
		int BAICMOTOR 	= 0x06;
		int ID_HCSJ		= 0x0D;
	}
	
	interface PasswordSelect{
		/*** 车标 */
		byte CAR_ICON	= 0x00;
		/*** 背光学习 */
		byte LIGHT_STUDY 	= 0x01;
		/***屏幕校准 */
		byte SCREEN_CALIBRATION 	= 0x02;
		/***色调调整 */
		byte TONE_SET 	= 0x03;
		/***cvbs调整 */
		byte CVBS_SET 	= 0x04;
		/***车辆设置 */
		byte CAR_SET 	= 0x05;
	}
	
	interface Value {
//		int STANDBY_DURATION 			= Duration.STANDBY_15_MINUTE;
//		boolean ASSISTIVE_STATE 		= true;
//		int DEF_DELAY_ACC 				= 1;
		int ALARM_SCALE 				= 0;
		int LAUNCH_HZ 					= 9450;
		int LOCK_SCREEN_DT 				= Duration.LOCK_NEVER;
		int MODE_DURATION 				= 3 * Duration.MINUTE;
		boolean ALLOW_ALTER_CONFIG 		= true;
		boolean STANDBY_SWITCH_STATE	= true;
		boolean SIMPLEMODE_SWITCH 		= false;
		boolean SCREEN_SWITCH 			= true;
	}

	interface Duration {
		int MINUTE				= 60000;
		int LOCK_NEVER 			= -1;
		int STANDBY_NONE 		= 0; 
		int STANDBY_15_MINUTE 	= 215;
		int STANDBY_12_HOUR 	= 12;
		int STANDBY_48_HOUR 	= 48;
	}

	interface Action {
		String BOOT_COMPLETED 			= "android.intent.action.LS_BOOT_COMPLETED";
		String START_CAR_LOGO 			= "com.landsem.actions.START_CAR_LOGO";
		String MASTER_CLEAR 			= "android.intent.action.MASTER_CLEAR"; // 系统Action
		String TIME_SETTING 			= "com.landsem.actions.TIME_SETTRING";
		String HEADLIGHT_ON 			= "android.intent.HEADLAMP_ON"; // 大灯打开
		String HEADLIGHT_OFF 			= "android.intent.HEADLAMP_OFF"; // 大灯关闭
		String ACTION_CLOSE_SCREEN 		= "android.intent.SCREEN_OFF";
		String ACTION_UPGRADE_MCU 		= "android.intent.MCU_UPDATE_START";
		String BLUETOOTH_PHONE 			= "android.intent.action.BLUETOOTHPHONE";
		String SHOW_BRIGHTNESS 			= "com.landsem.actions.SHOW_BRIGHTNESS";
		String ECAR_TEL_CHANGE 			= "com.landsem.actions.ECAR_TEL_CHANGE";
		String NAVI_CHANGE 				= "com.ls.carsetting.action.NAVI_CHANGE";
		String DATE_FORMAT_CHANGEED 	= "com.landsem.actions.DATE_FORMAT_CHANGEED";
		String FM_LAUNCH_ON 			= "com.ls.fmlaunch.STATE_ON";
		String FM_LAUNCH_OFF 			= "com.ls.fmlaunch.STATE_OFF";
		String SCREEN_OFF				= "android.intent.SCREEN_OFF";
		String SCREEN_ON				= "android.intent.SCREEN_ON";
		String LIGHT_SERVICE			= "com.landsem.actions.START_LIGHT_SERVICE";
		String FM_CVONTROL				= "com.landsem.actions.FM_CONTROL";
		String SIMPLEMODE_SWITCH_CHANGE = "com.landsem.actions.SIMPLEMODE_SWITCH_CHANGE";
		String SCREEN_OFF_STATUS_CHANGED= "com.landsem.actions.SCREEN_OFF_STATUS_CHANGED";
		String FM_LAUNCH_CHANGED		= "com.landsem.actions.FM_LAUNCH_CHANGED";
		/***车辆设置，输入密码成功*/
		String CAR_SET_CORRECT		    = "com.landsem.actions.CAR_SET_CORRECT";
		/***AR导航包名,装了此app就隐藏导航选择的列表*/
		String AR_PACKAGE_NAME		    = "com.landsem.arnavi";
	}

}
