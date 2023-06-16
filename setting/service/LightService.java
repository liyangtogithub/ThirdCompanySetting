package com.landsem.setting.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.landsem.setting.view.BrightnessDialog;

public class LightService extends Service {

	private BrightnessDialog mBrightnessDialog;
	private BrightnessReceiver mBrightnessReceiver;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mBrightnessDialog = new BrightnessDialog(this);
		mBrightnessReceiver = new BrightnessReceiver();
		mBrightnessReceiver.register(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mBrightnessReceiver) {
			mBrightnessReceiver.unRegister(this);
			mBrightnessReceiver = null;
		}
	}

	private final class BrightnessReceiver extends BroadcastReceiver {

		private static final String SHOW_BRIGHTNESS = "com.landsem.actions.SHOW_BRIGHTNESS";

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SHOW_BRIGHTNESS)) {
				if (null != mBrightnessDialog && !mBrightnessDialog.isShowing())
					mBrightnessDialog.show();
			}
		}

		public void register(Context context) {
			IntentFilter filter = new IntentFilter(SHOW_BRIGHTNESS);
			context.registerReceiver(this, filter);
		}

		public void unRegister(Context context) {
			context.unregisterReceiver(this);
		}

	}

}
