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


public final class ReverseTrackSwicth extends Switch implements OnClickListener, Constant {
	
	private static final String TAG = ReverseTrackSwicth.class.getSimpleName();
	public static final String REVERXE_TRACK = "reverxeTrack";
//	private static final String REVERSE_TRACK_STATE = "com.landsem.REVERSE_TRACK_SWITCH_STATE_CHANGED";
	private String initialTrackState;
	
	public ReverseTrackSwicth(Context context) {
		this(context, null);
	}

	public ReverseTrackSwicth(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ReverseTrackSwicth(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
		initialState();
	}

	private void initialState(){
		try {
			initialTrackState = SettingApp.getConfigManager().getConfigValue(LS_CONFIG_ID.IMIRROR_CONFIG_RESERVED3);
			initialTrackState = initialTrackState.trim();
		} catch (Exception e) {
			e.printStackTrace();
			initialTrackState = STATE_ON+"";
		}
	}
	

	public void refreshViewsState() {
		try {
			ContentResolver contentResolver = getContext().getContentResolver();
			int reverxeTrackState = Settings.System.getInt(contentResolver, REVERXE_TRACK, Integer.parseInt(initialTrackState));
			LogManager.d( "reverxeTrackState  :   "+reverxeTrackState);
			setChecked(reverxeTrackState==STATE_ON);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeReverseTrackState() {
		try {
			ContentResolver contentResolver = getContext().getContentResolver();
			int trackState = Settings.System.getInt(contentResolver, REVERXE_TRACK, Integer.parseInt(initialTrackState));
			Settings.System.putInt(contentResolver, REVERXE_TRACK, trackState==STATE_OFF?STATE_ON:STATE_OFF);
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
