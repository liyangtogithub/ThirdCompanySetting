package com.landsem.setting.carrier;

import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.view.PhoneView;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;

public class SpeedDialCarrier extends BaseCarrier implements Constant{

	private static final long serialVersionUID = -4277696990582603358L;
	private static final String TAG = SpeedDialCarrier.class.getSimpleName();
	private static final String PHONE_SWITCH_STATE = "phoneSwitchState";
	private Switch phoneSwitch;
	private PhoneView kinshipPhoneView;
	private PhoneView naviPhoneView;
	private PhoneView insurancePhoneView;
	private PhoneView alarnPhoneView;
	private View phoneContent;
	private View markedWords;
	private boolean switchState;
	
	public SpeedDialCarrier(LayoutInflater inflater, int resource) {
		this(SettingApp.getContext(), inflater, resource);
		
	}

	public SpeedDialCarrier(Context context, LayoutInflater inflater, int resource) {
		super(context, inflater, resource);
		initViews(contentView);
		initListener();
		updateSwitchState();
		initViewsState();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.phone_switch:
			switchState = !switchState;
			SettingApp.putSystemBoolean(PHONE_SWITCH_STATE, switchState);
			updateSwitchState();
			break;
		}
	}

	@Override
	protected void initViews(View convertView) {
		phoneContent = convertView.findViewById(R.id.phone_content);
		phoneSwitch = (Switch) convertView.findViewById(R.id.phone_switch);
		kinshipPhoneView = (PhoneView) convertView.findViewById(R.id.kinship_phoneview);
		naviPhoneView = (PhoneView) convertView.findViewById(R.id.navi_phoneview);
		insurancePhoneView = (PhoneView) convertView.findViewById(R.id.insurance_phoneview);
		alarnPhoneView = (PhoneView) convertView.findViewById(R.id.alarn_phoneview);
		markedWords = convertView.findViewById(R.id.marked_words);
	}

	@Override
	protected void initListener() {
		phoneSwitch.setOnClickListener(this);
	}

	@Override
	public void initViewsState() {
		kinshipPhoneView.initViewState();
		naviPhoneView.initViewState();
		insurancePhoneView.initViewState();
		alarnPhoneView.initViewState();
	}
	
	private void updateSwitchState(){
		switchState = SettingApp.getSystemBoolean(PHONE_SWITCH_STATE, true);
		phoneSwitch.setChecked(switchState);
		if(switchState){
			markedWords.setVisibility(View.GONE);
			phoneContent.setVisibility(View.VISIBLE);
		}else{
			phoneContent.setVisibility(View.GONE);
			markedWords.setVisibility(View.VISIBLE);
		}
		Intent intent = new Intent("com.landsem.actions.PHONE_SWITCH_STATE_CHANGED");
		getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
	}

}
