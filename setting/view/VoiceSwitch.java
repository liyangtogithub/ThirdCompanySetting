package com.landsem.setting.view;


import com.landsem.setting.SettingApp;
import com.landsem.setting.entity.ConfigValue;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

public class VoiceSwitch extends Switch implements OnClickListener {

	private static final String WAKEUP_BUTTON = "com.ls.voicecontrol.wakeupbutton";
	private static final String VOICE_SWITCH = "voice_wakeup";

	
	public VoiceSwitch(Context context) {
		this(context, null);
	}

	public VoiceSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VoiceSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		setOnClickListener(this);
		refreshViewsState();
	}
	
	
	public void refreshViewsState() {
		boolean voiceState = SettingApp.getSystemBoolean(VOICE_SWITCH, ConfigValue.voiceState);
//		LogManager.d("luohong", "refreshViewsState: voiceState: "+voiceState);
		setChecked(voiceState);
	}
	
	private void updateVoiceState(){
		boolean state = SettingApp.getSystemBoolean(VOICE_SWITCH, ConfigValue.voiceState);
		SettingApp.putSystemBoolean(VOICE_SWITCH, !state);
//		LogManager.d("luohong", "updateVoiceState: state: "+state);
		refreshViewsState();
		Intent intent = new Intent(WAKEUP_BUTTON);
		SettingApp.getInstance().sendBroadcast(intent);
	}

	@Override
	public void onClick(View arg0) {
		updateVoiceState();
	}

}
