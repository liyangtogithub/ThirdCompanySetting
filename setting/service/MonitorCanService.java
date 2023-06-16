package com.landsem.setting.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.landsem.common.tools.LogManager;
/**
 * 监控Can盒的服务
 */
public class MonitorCanService extends Service {


	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogManager.d("MonitorCanService  onCreate()");
		bindService(new Intent("com.landsem.canboxui.DialogService"), CanBoxconn, Service.BIND_AUTO_CREATE);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		LogManager.d("MonitorCanService  onDestroy()");
	}

	private ServiceConnection CanBoxconn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LogManager.d("MONITOR CAN onServiceConnected name: "+name);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			LogManager.d("MONITOR CAN onServiceDisconnected name: "+name);
			bindService(new Intent("com.landsem.canboxui.DialogService"), CanBoxconn, Service.BIND_AUTO_CREATE);
		}
	};

}
