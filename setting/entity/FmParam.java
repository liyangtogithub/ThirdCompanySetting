package com.landsem.setting.entity;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;

import android.content.Intent;
import android.os.UserHandle;

public final class FmParam implements Constant{
	
	private static final long serialVersionUID = -665868738221036235L;
	private static final String TAG = FmParam.class.getSimpleName();
	private boolean fmState;
	private int fmHZ;
	
	public boolean isFmState() {
		LogManager.d(TAG, "fmState : "+fmState);
		return fmState;
	}

	public void setFmState(boolean fmState) {
		this.fmState = fmState;
		SettingApp.putSystemBoolean(Key.FM_STATE, fmState);
		Intent intent = new Intent(Action.FM_LAUNCH_CHANGED);
		intent.putExtra(Key.LAUNCH_STATUS, fmState);
		SettingApp.getInstance().sendBroadcastAsUser(intent, UserHandle.ALL);
	}

	public int getFmHZ() {
		return fmHZ;
	}

	public void setFmHZ(int fmHZ) {
		this.fmHZ = fmHZ;
		SettingApp.putInt(Key.FM_HZ, fmHZ);
	}
	
	public FmParam(){
		fmState = SettingApp.getSystemBoolean(Key.FM_STATE, false);
		fmHZ = SettingApp.getInt(Key.FM_HZ, Value.LAUNCH_HZ);
	}


	public int seekStep(boolean added){
		if(added) fmHZ += FM_STEP;
		else fmHZ -= FM_STEP;
		if (fmHZ > FM_FREQ_MAX) fmHZ = FM_FREQ_MIN;
		if(fmHZ < FM_FREQ_MIN) fmHZ = FM_FREQ_MAX;
		return fmHZ;
	}

}
