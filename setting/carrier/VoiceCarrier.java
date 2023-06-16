package com.landsem.setting.carrier;

import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.view.KeyToneSwitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public final class VoiceCarrier extends BaseCarrier{
	
	private static final long serialVersionUID = 317359879528648800L;
	private KeyToneSwitch mKeyToneSwitch;

	public VoiceCarrier(LayoutInflater inflater, int resource) {
		this(SettingApp.getContext(), inflater, resource);
	}
	
	public VoiceCarrier(Context context, LayoutInflater inflater, int resource) {
		super(context, inflater, resource);
		initViews(contentView);
		initViewsState();
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected void initViews(View convertView) {
		mKeyToneSwitch = (KeyToneSwitch) convertView.findViewById(R.id.keytone_switch);
	}

	@Override
	protected void initListener() {
		
	}

	@Override
	protected void initViewsState() {
		if(null!=mKeyToneSwitch) mKeyToneSwitch.refreshViewsState();
	}

}
