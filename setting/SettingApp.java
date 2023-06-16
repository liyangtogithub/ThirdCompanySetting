package com.landsem.setting;

import java.util.List;
import java.util.Locale;

import com.landsem.common.app.App;
import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.PreferencesUtils;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.Constant.Action;
import com.landsem.setting.brightness.InitBackLight;
import com.landsem.setting.brightness.StatusBrightnessHelper;
import com.landsem.setting.carrier.BackLightCarrier;
import com.landsem.setting.entity.AppInfo;
import com.landsem.setting.entity.ConfigValue;
import com.landsem.setting.receiver.TimePickerReceiver;
import com.landsem.setting.service.MonitorCanService;
import com.landsem.setting.service.MonitorLogService;
import com.landsem.setting.utils.LightnessControl;
import com.ls.bluetooth.service.IBluetooth;
import com.ls.config.ConfigManager;
import com.ls.config.ConfigManager.LS_CONFIG_ID;
import com.ls.mcu.McuManager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;

public final class SettingApp extends App implements Constant {

	private static final long serialVersionUID = -1995612920401990694L;
	private static final String TAG = SettingApp.class.getSimpleName();
	private static final String PREFER_NAME = "setting";
	private static SettingApp instance = null;
	public static final boolean IS_DEVELOPING = false;
	public static final int SHOW_TOAST = 0x01;
	public static int CLIENT_ID = ClientId.COMMON;
	public static ConfigManager mConfigManager;
	public static boolean isChinese = false;
	private NightLevelSetting mNightLevelSetting;
	private StatusBrightnessHelper statusBrightnessHelper;
	public IBluetooth iBluetooth = null;
	public BackLightCarrier mBackLightCarrier;
	private TimePickerReceiver mTimePickerReceiver;
	public static final int WHAT_SCREEN_OFF = 0x01;
	public static final int WHAT_SCREEN_ON = 0x02;
	public static final int WHAT_RECOVE_MARK = 0x03;
	public static final int SYSTEM_TIME_CHANGED = 0x04;
	private static boolean wifiPreState;
	private static boolean accMark;
	private static boolean wifiCurrState;
	private GlobalHandler mGlobalHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		LogManager.LOG_PRDFIX = "Setting_App ==)    ";
		getConfigManager();
		initClientId();
		ConfigValue.initDefConfigValue();
		Configuration configuration = getResources().getConfiguration();
		initLangage(configuration);
		mNightLevelSetting = new NightLevelSetting(this);
		getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, mBrightnessObserver);
		Intent intent = new Intent(Action.LIGHT_SERVICE);
		startService(intent);
		bindBluetooth();
		statusBrightnessHelper =  StatusBrightnessHelper.newInstance(this);
		mBackLightCarrier = BackLightCarrier.newInstance(LayoutInflater.from(getContext()), R.layout.backlight_carrier_layout);
		Type.initTypes(this);
		notifyMcuStandBy();
//		mTimePickerReceiver = new TimePickerReceiver();
//		mTimePickerReceiver.register(this);
		mGlobalHandler = new GlobalHandler();
		InitBackLight.updateBrightnessStatus(instance);
		startService(new Intent(this,MonitorCanService.class));
		startService(new Intent(this,MonitorLogService.class));
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		LogManager.d(TAG, "onTerminate ");
	}
	
	private void initLangage(Configuration configuration){
		if(null!=configuration){
			Locale locale = configuration.locale;
			if(null!=locale){
				String language = locale.getLanguage();
				if(!StringUtils.isEmpty(language)) isChinese = language.equalsIgnoreCase("zh");
			}
		}
	}

	public static synchronized ConfigManager getConfigManager() {
		if (null==mConfigManager) mConfigManager = (ConfigManager) getContext().getSystemService(Context.CONFIG_SERVICE);
		return mConfigManager;
	}
	
	private void initClientId(){
		try {
			String client = mConfigManager.getConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED22);
			if(!StringUtils.isBlank(client)) CLIENT_ID = Integer.parseInt(client);
		} catch (Exception e) {
			e.printStackTrace();
			CLIENT_ID = ClientId.COMMON;
		}
	}
	
	public void changeLightMode(){
		if(null==statusBrightnessHelper) statusBrightnessHelper = StatusBrightnessHelper.newInstance(this);
		statusBrightnessHelper.changeLightMode();
	}
	
	public void saveLightValue(int lightValue){
		int doorsillValue = MAX_LIGHT_VALUE*ZOON_DIGIT / 2;
		LS_CONFIG_ID configId = LS_CONFIG_ID.CAR_CONFIG_RESERVED5;
		switch (LightnessControl.LIGHT_MODE) {
		case DAYLIGHT_MODE:
			configId = LS_CONFIG_ID.CAR_CONFIG_RESERVED5;
			lightValue = lightValue<doorsillValue ? doorsillValue : lightValue;
			break;
		case NIGHTLIGHT_MODE:
			configId = LS_CONFIG_ID.CAR_CONFIG_RESERVED6;
			lightValue = lightValue>doorsillValue ? doorsillValue : lightValue;
			break;
		default:
			lightValue = lightValue>doorsillValue ? doorsillValue : lightValue;
			break;
		}
		LogManager.d("saveLightValue  = "+ lightValue+"  configId  = "+configId);
		mConfigManager.setConfigValue(configId, lightValue + "");
		mConfigManager.configFlush();
	}
	
	public int checkOutLightMode(){
		if(null==statusBrightnessHelper) statusBrightnessHelper = StatusBrightnessHelper.newInstance(this);
		return statusBrightnessHelper.checkOutLightMode();
	}
	

	@SuppressWarnings("finally")
	public int updateHeadLampState() {
		int headlampState = HEADLAMP_OFF;
		try {
			headlampState = Settings.System.getInt(getContentResolver(), Key.HEADLAMP_STATE);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return headlampState;
		}
	}
	
	

	public static boolean putSystemString(String key, String value) {
		boolean result = false;
		try {
			Context context = SettingApp.getInstance();
			ContentResolver contentResolver = context.getContentResolver();
			result =  Settings.System.putString(contentResolver, key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean putSystemInt(String key, int value) {
		boolean result = false;
		try {
			if (!StringUtils.isEmpty(key)) {
				Context context = SettingApp.getInstance();
				ContentResolver contentResolver = context.getContentResolver();
				result = Settings.System.putInt(contentResolver, key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean putSystemBoolean(String key, boolean value) {
		boolean result = false;
		try {
			if (!StringUtils.isEmpty(key)) {
				Context context = SettingApp.getInstance();
				ContentResolver contentResolver = context.getContentResolver();
				String booleanValue = String.valueOf(value);
				result = Settings.System.putString(contentResolver, key, booleanValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getSystemString(String key, String defValue) {
		String result = defValue;
		try {
			if (!StringUtils.isEmpty(key)) {
				Context context = SettingApp.getInstance();
				ContentResolver contentResolver = context.getContentResolver();
				result = Settings.System.getString(contentResolver, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static int getSystemInt(String key, int defValue) {
		int result = defValue;
		try {
			if (!StringUtils.isEmpty(key)) {
				Context context = SettingApp.getInstance();
				ContentResolver contentResolver = context.getContentResolver();
				result = Settings.System.getInt(contentResolver, key, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean getSystemBoolean(String key, boolean defResult) {
		boolean result = defResult;
		try {
			if (!StringUtils.isEmpty(key)) {
				Context context = SettingApp.getInstance();
				ContentResolver contentResolver = context.getContentResolver();
				String booleanValue = Settings.System.getString(contentResolver, key);
				if (!StringUtils.isEmpty(booleanValue)) result = Boolean.valueOf(booleanValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void putBoolean(String keyName, boolean value) {
		PreferencesUtils.putBoolean(getContext(), PREFER_NAME, keyName, value);
	}

	public static void putString(String keyName, String value) {
		PreferencesUtils.putString(getContext(), PREFER_NAME, keyName, value);
	}

	public static void putInt(String keyName, int value) {
		PreferencesUtils.putInt(getContext(), PREFER_NAME, keyName, value);
	}

	public static int getInt(String keyName, int value) {
		return PreferencesUtils.getInt(getContext(), PREFER_NAME, keyName, value);
	}

	public static boolean getBoolean(String keyName, boolean value) {
		return PreferencesUtils.getBoolean(getContext(), PREFER_NAME, keyName, value);
	}

	public static String getString(String keyName, String value) {
		return PreferencesUtils.getString(getContext(), PREFER_NAME, keyName, value);
	}
	

	public static SettingApp getInstance() {
		return instance;
	}

	public NightLevelSetting getNightLevelSetting() {
		return mNightLevelSetting;
	}

	public StatusBrightnessHelper getStatusBrightnessHelper() {
		return statusBrightnessHelper;
	}


	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null==info) info = new PackageInfo();
		return info;
	}

	private ContentObserver mBrightnessObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			if (isApplicationBroughtToProscenium(getApplicationContext())) {
				onBrightnessChanged();
			}
		}
	};

	private void onBrightnessChanged() {
		int lightness = LightnessControl.getScreenLightness(this);
//		ConfigManager cfgManager = getConfigManager();
//		cfgManager.setConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED5, lightness + "");
//		cfgManager.configFlush();
		SettingApp.getInstance().onTimeChanged();
	}

	public static boolean isApplicationBroughtToProscenium(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (topActivity.getPackageName().equals("com.android.settings")) {
				return true;
			}
		}
		return false;
	}
	
	public static void notifyMcuStandBy(){
		boolean standByState = SettingApp.getBoolean(Key.STANDBY_SWITCH, Value.STANDBY_SWITCH_STATE);
		int duration = standByState ? 144:0;
		McuManager.sendMcuCommand(McuManager.SET_CAR_STANDBY_TIME, duration);
		LogManager.d(TAG, "notifyMcuStandBy      &&&&&&      duration : "+duration+", standByState : "+standByState);
	}
	
	@Override
	public void onConfigurationChanged(Configuration configuration) {
		LogManager.d(TAG, "onConfigurationChanged  ");
//		unbindService(conn);
//		iBluetooth = null;
		initLangage(configuration);
		super.onConfigurationChanged(configuration);
//		LogManager.d(TAG, "onConfigurationChanged      bindBluetooth");
//		bindBluetooth();
		if(null!=mBackLightCarrier) mBackLightCarrier.onConfigurationChanged();
	}
	
	
	public void bindBluetooth(){
		Intent intentSev = new Intent();
		intentSev.setAction("com.ls.bluetooth.action.BT_SERVICE");
		bindService(intentSev, conn, Service.BIND_AUTO_CREATE);
	}
	/**
	 * 本为根据时间判断白天、晚上，现改为大灯亮灭来判断白天晚上，再初始化亮度
	 */
	public void onTimeChanged() {
		if(null!=mGlobalHandler){
			mGlobalHandler.removeMessages(SYSTEM_TIME_CHANGED);
			mGlobalHandler.sendEmptyMessageDelayed(SYSTEM_TIME_CHANGED, SECOND/2);
		}
	}
	
	public void saveCurrWifiStatus() {
		LogManager.d(TAG, "saveCurrWifiStatus start  accMark: " + accMark+",  wifiPreState: "+wifiPreState);
		if(!accMark){
			WifiManager mWifiManager = (WifiManager) SettingApp.getInstance().getSystemService(Context.WIFI_SERVICE);
			wifiPreState = WifiManager.WIFI_STATE_ENABLED==mWifiManager.getWifiApState();
		}
		LogManager.d(TAG, "saveCurrWifiStatus end  accMark: " + accMark+",  wifiPreState: "+wifiPreState);

	}
	
	public void sendHandScreenOff(){
		if(null!=mGlobalHandler){
			mGlobalHandler.removeMessages(WHAT_SCREEN_ON);
			mGlobalHandler.removeMessages(WHAT_SCREEN_OFF);
			mGlobalHandler.removeMessages(WHAT_RECOVE_MARK);
			mGlobalHandler.sendEmptyMessageDelayed(WHAT_SCREEN_OFF, SECOND);
		}
	}
	
	public void sendHandScreenON(){
		if(null!=mGlobalHandler){
			mGlobalHandler.removeMessages(WHAT_SCREEN_ON);
			mGlobalHandler.removeMessages(WHAT_SCREEN_OFF);
			mGlobalHandler.removeMessages(WHAT_RECOVE_MARK);
			mGlobalHandler.sendEmptyMessageDelayed(WHAT_SCREEN_ON, 3*SECOND);
		}
	}
	
	private void stopWifiEnabled() {
		LogManager.d(TAG, "stopWifiEnabled start  accMark :   " + accMark);
		WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		int wifistate = mWifiManager.getWifiState();
		wifiCurrState =  wifistate==WifiManager.WIFI_STATE_ENABLED;
		accMark = true;
		if(wifiCurrState) mWifiManager.setWifiEnabled(false);
		LogManager.d(TAG, "stopWifiEnabled end  accMark :   " + accMark);
		
	}

	private void recoverWifiState() {
		LogManager.d(TAG, "recoverWifiState start  wifiPreState :   " + wifiPreState);
		if(wifiPreState) {
			WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			mWifiManager.setWifiEnabled(true);
		}
		mGlobalHandler.sendEmptyMessageDelayed(WHAT_RECOVE_MARK, SECOND);
		LogManager.d(TAG, "recoverWifiState end  wifiPreState :   " + wifiPreState);

	}
	
	private final class GlobalHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SYSTEM_TIME_CHANGED:
				if(null!=mBackLightCarrier) mBackLightCarrier.onModeChanged();
				break;
			case WHAT_SCREEN_OFF:
				stopWifiEnabled();
				break;
			case WHAT_SCREEN_ON:
				recoverWifiState();
				break;
			case WHAT_RECOVE_MARK:
				accMark = false;
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	}
	
	public boolean isPackageNameExist(String packageName) {
		PackageManager packageManager  = getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_CONFIGURATIONS);//
		for (PackageInfo packageInfo : packageInfos) {
			if (null!=packageInfo && StringUtils.isEquals(packageInfo.packageName, packageName)) {
				LogManager.d("isPackageNameExist  true ");
				  return  true;
			}
		}
        return  false;
	}
	

	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iBluetooth = IBluetooth.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			iBluetooth = null;
		}
	};
	
	

}
