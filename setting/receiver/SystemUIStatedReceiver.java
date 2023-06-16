package com.landsem.setting.receiver;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.SettingApp;
import com.landsem.setting.brightness.StatusBrightnessHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 监听SystemUI启动，去注册SystemUI的背光回调
 */
public class SystemUIStatedReceiver extends BroadcastReceiver  {

	private static final String TAG = SystemUIStatedReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		LogManager.d(TAG, "SystemUIStatedReceiver :"+intent.getAction());
		StatusBrightnessHelper.newInstance(context).registSyetemUICallback();
	}
}
