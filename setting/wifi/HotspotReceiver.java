package com.landsem.setting.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.landsem.common.tools.LogManager;
 

public class HotspotReceiver extends BroadcastReceiver {
	private static final String TAG = "TclSettingsHotspot";

	private static final String packageName = "com.car.carsetting.wifi";
	public static final String ACTION_WIFI_HOT_SPOT_TURN_OFF = packageName + ".ACTION_WIFI_HOT_SPOT_TURN_OFF";
	
	private String wifiStateBeforeOpenHotspot;
	private WifiConfiguration mWifiConfig;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ACTION_WIFI_HOT_SPOT_TURN_OFF.equals(action) || "android.intent.SCREEN_OFF".equals(action)) {
			//turnOffSpot(context);
		}
	}

	private void turnOffSpot(Context context) {
		final Context appContext = context.getApplicationContext();
		Handler handler = new Handler(context.getApplicationContext().getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				WifiManager wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
//				try {
					mWifiConfig = wifiManager.getWifiApConfiguration();
					wifiManager.setWifiApEnabled(mWifiConfig, false);
//					if (wifiManager.isWifiApEnabled()) {
//						Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
//						method.setAccessible(true);
//						WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager, null);
//						Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
//						method2.invoke(wifiManager, config, false);
					
						LogManager.d(TAG,"CLOSE hotSpot!");
						try {
							wifiStateBeforeOpenHotspot=android.provider.Settings.System.getString(appContext.getContentResolver(), "com.car.carsetting.wifi.hotpoint.wifiState");
						} catch (Exception e) {
							// TODO: handle exception
						}
						LogManager.d(TAG,"wifiStateBeforeOpenHotspot="+wifiStateBeforeOpenHotspot);
						//display the hotspot icon in status Bar
						Intent intent = new Intent();
						intent.setAction("com.tcl.wifihotspot.action.ACTION_STATE_CHANGED");
						intent.putExtra("com.tcl.wifihotspot.extra.DISPLAY_STATE", 2);
						appContext.sendBroadcast(intent);
//						LogManager.d(TAG,"state = on");
//					}

//				} catch (NoSuchMethodException e) {
//					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					e.printStackTrace();
//				}
				LogManager.d(TAG,"wifiStateBeforeOpenHotspot="+wifiStateBeforeOpenHotspot);
				if ("true".equals(wifiStateBeforeOpenHotspot)) {
					LogManager.d(TAG,"turnOnWifi");
					wifiManager.setWifiEnabled(true);
				}
				//Clear the wifiState after close hotspot
				android.provider.Settings.System.putString(appContext.getContentResolver(), "com.car.carsetting.wifi.hotpoint.wifiState","false");

			}
		};
		handler.sendEmptyMessage(0);
		
	}
}
