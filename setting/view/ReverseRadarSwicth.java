package com.landsem.setting.view;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;
import com.ls.config.ConfigManager.LS_CONFIG_ID;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;


public final class ReverseRadarSwicth extends Switch implements OnClickListener, Constant {
	
	public static final String REVERXE_RADAR = "reverxeRadar";
	
	public ReverseRadarSwicth(Context context) {
		this(context, null);
	}

	public ReverseRadarSwicth(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ReverseRadarSwicth(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
	}

	public void refreshViewsState() {
		try {
			ContentResolver contentResolver = getContext().getContentResolver();
			int reverxeRadarState = Settings.System.getInt(contentResolver, REVERXE_RADAR, STATE_OFF);
			LogManager.d( "reverxeRadarState  :   "+reverxeRadarState);
			setChecked(reverxeRadarState==STATE_ON);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeReverseTrackState() {
		try {
			ContentResolver contentResolver = getContext().getContentResolver();
			int trackState = Settings.System.getInt(contentResolver, REVERXE_RADAR, STATE_OFF);
			Settings.System.putInt(contentResolver, REVERXE_RADAR, trackState==STATE_OFF?STATE_ON:STATE_OFF);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {
		changeReverseTrackState();
		refreshViewsState();
	}

}
