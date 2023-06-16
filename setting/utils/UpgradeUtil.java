package com.landsem.setting.utils;

import com.landsem.common.tools.PreferencesUtils;
import com.landsem.setting.SettingApp;
import com.landsem.setting.upgrade.EasyTool;

import android.content.Intent;
import android.provider.Settings;


public class UpgradeUtil {
	private static final String KEY_MCU_UPGRADING = "com.tcl.settings.mcu_upgrading";
	private static final String tag = "UpgradeUtil";
	public static final String ACTION_TCL_UPGRADE_SYSTEM = "ACTION_TCL_UPGRADE_SYSTEM";
	public static final String EXTRA_SCAN_PATH = "path";
	private static int myPath;
	public static final int PATH_EXTERNAL = 1;
	public static final int PATH_INTERNAL = 2;
	public static final int PATH_ALL = PATH_EXTERNAL | PATH_INTERNAL;
	private static final String KEY_LAST_PATH = "UpgradeUtil.KEY_LAST_PATH";

	public static void notifyUpgrade(int path) {
		if (EasyTool.in(path, PATH_EXTERNAL, PATH_INTERNAL, PATH_ALL) == false) {
			path = PATH_ALL;
			myPath = PATH_ALL;
		}
		PreferencesUtils.putInt(SettingApp.getContext(), KEY_LAST_PATH, path);
		Intent intent = new Intent(ACTION_TCL_UPGRADE_SYSTEM);
		intent.putExtra(EXTRA_SCAN_PATH, path);
		SettingApp.getContext().sendBroadcast(intent);
	}

//	public static int getLastPath() {
//		return SharedPreferenceUtil.get(SettingApp.getContext(), KEY_LAST_PATH, PATH_ALL);
//	}

	public static void setMcuUpgrading(boolean value) {
		Settings.System.putString(SettingApp.getContext().getContentResolver(), KEY_MCU_UPGRADING, value ? "true" : "false");
	}
	
}
