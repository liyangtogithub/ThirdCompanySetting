package com.landsem.setting.receiver;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;
import com.landsem.setting.Constant.Action;
import com.landsem.setting.activities.CalibLayout;
import com.landsem.setting.entity.ConfigValue;
import com.landsem.setting.entity.NaviInfo;
import com.landsem.setting.upgrade.SystemInfo;
import com.landsem.setting.utils.NaviParser;
import com.ls.lseasycontrol.LSEasyControlManager;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver implements Constant {

	private static final long serialVersionUID = 1L;
	private static final String TAG = BootCompletedReceiver.class.getSimpleName();


	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		LogManager.d(TAG, "onReceiver: "+action);
		switch (action) {
		case Action.BOOT_COMPLETED:
			doBootCompleted(context);
			calibTest(context);
			break;
//		case Action.SCREEN_ON:
//			SettingApp.getInstance().sendHandScreenON();
//			break;
//		case Action.SCREEN_OFF:
//			SettingApp.getInstance().sendHandScreenOff();
//			break;
//		case WifiManager.WIFI_STATE_CHANGED_ACTION:
//			SettingApp.getInstance().saveCurrWifiStatus();
//			break;
		}
	}
	
	private void calibTest(Context context) {
		LSEasyControlManager mLSEasyControlManager = new LSEasyControlManager(context);
		int adjustState = mLSEasyControlManager.Get_Tp_Adjust_State();
		LogManager.d(TAG, "Get_Tp_Adjust_State : "+adjustState);
		if (adjustState==1) {
			new CalibLayout(SettingApp.getInstance()).showDisplay();
		}
		
	}

	public static void backToHome(Context context){
		Intent homeIntent = new Intent(Intent.ACTION_MAIN);   
	    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    homeIntent.addCategory(Intent.CATEGORY_HOME); 
	    context.startActivityAsUser(homeIntent, UserHandle.OWNER);
	}

	private void doBootCompleted(Context context) {
		int delayAcc = SettingApp.getSystemInt(Key.DELAY_ACC, INVALID_VALUE);
		if (delayAcc == INVALID_VALUE) SettingApp.getSystemInt(Key.DELAY_ACC, ConfigValue.powerOffDelay);
		String version = SystemInfo.getVersion();
		boolean versionResult = SettingApp.putSystemString("version", version);
		LogManager.d(TAG, "versionResult: " + versionResult);
		initDefNavi();
	}

	int curr_visition = 1;
	private void initDefNavi() {
		String naviPag = SettingApp.getSystemString(Key.MAP_PACKAGE, null);
		String naviClass = SettingApp.getSystemString(Key.MAP_CLASS, null);
		LogManager.d(TAG, "initDefNavi naviPag: "+naviPag+", naviClass: "+naviClass);
		if(StringUtils.isBlank(naviPag) || StringUtils.isBlank(naviClass)){
			NaviInfo mNaviInfo = NaviParser.parserSetting();
			String packageName = "com.autonavi.amapauto";
			String className = "com.autonavi.auto.remote.fill.UsbFillActivity";
			if(null!=mNaviInfo){
				packageName = mNaviInfo.packageName;
				className = mNaviInfo.className;
			}
			SettingApp.putSystemString(Key.MAP_PACKAGE, packageName);
			SettingApp.putSystemString(Key.MAP_CLASS, className);
			LogManager.d(TAG, "insert into def Navi  className: "+className);
		}
		int naviMapVertion= SettingApp.getSystemInt(Key.MAP_VERTION, 0);
		LogManager.d(" naviMapVertion "+naviMapVertion);
		if(naviMapVertion < curr_visition && SettingApp.getInstance().isPackageNameExist(Action.AR_PACKAGE_NAME)){
			gotoAr();
		}
	}

	private void gotoAr() {
		LogManager.d("gotoAr ");
		SettingApp.putSystemString(Key.MAP_PACKAGE, Action.AR_PACKAGE_NAME);
		SettingApp.putSystemString(Key.MAP_CLASS, "com.landsem.arnavi.Layar3DActivity");
		SettingApp.putSystemInt(Key.MAP_VERTION, curr_visition);
	}


}
