package com.landsem.setting.carrier;

import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.view.VoiceAmpliSwitch;
import com.landsem.setting.view.VoiceSwitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public final class VoiceAmpliCarrier extends BaseCarrier{
	
	private static final long serialVersionUID = 317359879528648800L;
	private VoiceAmpliSwitch mVoiceAmpliSwitch;

	public VoiceAmpliCarrier(LayoutInflater inflater, int resource) {
		this(SettingApp.getContext(), inflater, resource);
	}
	
	public VoiceAmpliCarrier(Context context, LayoutInflater inflater, int resource) {
		super(context, inflater, resource);
		initViews(contentView);
		initViewsState();
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected void initViews(View convertView) {
		mVoiceAmpliSwitch = (VoiceAmpliSwitch) convertView.findViewById(R.id.voice_ampli_switch);
	}

	@Override
	protected void initListener() {
		
	}

	@Override
	protected void initViewsState() {
		if(null!=mVoiceAmpliSwitch) mVoiceAmpliSwitch.refreshViewsState();
	}

}
