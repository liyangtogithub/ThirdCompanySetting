package com.landsem.setting.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.landsem.common.tools.LogManager;
/**
 * 监控Log的服务
 */
public class MonitorLogService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogManager.d("MonitorLogService  onCreate()");
		bindLogService();
	}
	

	private void bindLogService() {
		Intent mIntent = new Intent("cn.landsem.logger.service.CaptureService");
//		mIntent.putExtra("isBind", true);
		bindService(mIntent, Logconn, Service.BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogManager.d("MonitorLogService  onDestroy()");
	}

	private ServiceConnection Logconn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LogManager.d("MONITOR Log onServiceConnected name: "+name);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			LogManager.d("MONITOR Log onServiceDisconnected name: "+name);
			bindLogService();
		}
	};

}
