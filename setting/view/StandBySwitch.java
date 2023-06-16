package com.landsem.setting.view;


import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public final class StandBySwitch extends Switch implements OnCheckedChangeListener, Constant {

	private static final long serialVersionUID = -6167637244118344462L;


	public StandBySwitch(Context context) {
		this(context, null);
	}

	public StandBySwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StandBySwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnCheckedChangeListener(this);
		initStandByState();
	}

	private void initStandByState(){
		boolean standByState = SettingApp.getBoolean(Key.STANDBY_SWITCH, Value.STANDBY_SWITCH_STATE);
		this.setChecked(standByState);
	}
		

	@Override
	public void onCheckedChanged(CompoundButton mSwitch, boolean checked) {
		SettingApp.putBoolean(Key.STANDBY_SWITCH, checked);
		SettingApp.notifyMcuStandBy();
	}


}
