package com.landsem.setting.view;

import com.landsem.common.tools.StringUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;
import com.ls.config.ConfigManager.LS_CONFIG_ID;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;


public final class ParkLineSwicth extends Switch implements OnClickListener, Constant {
	
	private static final String TAG = ParkLineSwicth.class.getSimpleName();
//	public static final String REVERXE_TRACK = "reverxeTrack";
	
	public ParkLineSwicth(Context context) {
		this(context, null);
	}

	public ParkLineSwicth(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ParkLineSwicth(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
		initialState();
	}

	private void initialState(){
		String parkingLineState = SettingApp.getConfigManager().getConfigValue(LS_CONFIG_ID.IMIRROR_CONFIG_RESERVED1);
		boolean parkingState = (!StringUtils.isEmpty(parkingLineState) && parkingLineState.equals(STATE_ON+""));
		setChecked(parkingState);
	}
	
	private void updateParkingLine(){
		String parkingLineState = SettingApp.getConfigManager().getConfigValue(LS_CONFIG_ID.IMIRROR_CONFIG_RESERVED1);
		parkingLineState = (!StringUtils.isEmpty(parkingLineState) && parkingLineState.equals(STATE_ON+""))?STATE_OFF+"":STATE_ON+"";
		SettingApp.getConfigManager().setConfigValue(LS_CONFIG_ID.IMIRROR_CONFIG_RESERVED1,parkingLineState);
		SettingApp.getConfigManager().configFlush();	
		initialState();
	}



	@Override
	public void onClick(View view) {
		updateParkingLine();
	}

}
