package com.landsem.setting;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.landsem.common.tools.LogManager;

public class NightLevelSetting {
	
	private static final int NIGIT_DEFVALUE = 15;
	private static final int DEFVALUE = 15;
	private int level;
	private int nightLevel;
	private static final String TAG ="CarSetting";

	SharedPreferences sharedPreferences;

	public NightLevelSetting(Context context) {
		//context.getSharedPreferences("brightnessLevel", Context.MODE_PRIVATE)
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		loadFromLocal();

	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNightLevel() {
		return nightLevel;
	}

	public void setNightLevel(int nightLevel) {
		this.nightLevel = nightLevel;
	}

	public void commit() {
		Editor editor = sharedPreferences.edit();
		editor.putInt("mCurrentLevel", level);
		editor.putInt("mCurrentLevelNight", nightLevel);
		LogManager.d(TAG, "commit mCurrentLevel="+level+"mCurrentLevelNight="+nightLevel);
		boolean res = editor.commit();
		System.out.println(res);
	}

	public void loadFromLocal() {
		level = sharedPreferences.getInt("mCurrentLevel", DEFVALUE);
		nightLevel = sharedPreferences.getInt("mCurrentLevelNight", NIGIT_DEFVALUE);
	}
}
