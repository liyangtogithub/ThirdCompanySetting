package com.landsem.setting.carrier;

import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.view.VoiceSwitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public final class VoiceWakeUpCarrier extends BaseCarrier{
	
	private static final long serialVersionUID = 317359879528648800L;
	private VoiceSwitch mVoiceSwitch;

	public VoiceWakeUpCarrier(LayoutInflater inflater, int resource) {
		this(SettingApp.getContext(), inflater, resource);
	}
	
	public VoiceWakeUpCarrier(Context context, LayoutInflater inflater, int resource) {
		super(context, inflater, resource);
		initViews(contentView);
		initViewsState();
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected void initViews(View convertView) {
		mVoiceSwitch = (VoiceSwitch) convertView.findViewById(R.id.voicewakeup_switch);
	}

	@Override
	protected void initListener() {
		
	}

	@Override
	protected void initViewsState() {
		if(null!=mVoiceSwitch) mVoiceSwitch.refreshViewsState();
	}

}
