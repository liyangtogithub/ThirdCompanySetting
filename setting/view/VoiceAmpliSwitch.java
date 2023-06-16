package com.landsem.setting.view;


import com.landsem.common.tools.LogManager;
import com.landsem.setting.SettingApp;
import com.landsem.setting.entity.ConfigValue;
import com.ls.lseasycontrol.LSEasyControlManager;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

public class VoiceAmpliSwitch extends Switch implements OnClickListener {

	private LSEasyControlManager mLSEasyControlManager;
	
	public VoiceAmpliSwitch(Context context) {
		this(context, null);
	}

	public VoiceAmpliSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VoiceAmpliSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mLSEasyControlManager = new LSEasyControlManager(context);
		initView();
	}

	private void initView() {
		setOnClickListener(this);
		refreshViewsState();
	}
	
	
	
	public void refreshViewsState() {
		boolean voiceState = isAmpliSwitch();
		setChecked(voiceState);
	}
	
	private boolean isAmpliSwitch(){
		int ampliValue = mLSEasyControlManager.Get_Power_Amplifier_State();
		LogManager.d(" ampliValue: "+ampliValue);
		return ampliValue==1;
	}
	
	private void changeAmpliSwitch(boolean status){
		int futureStatus = status?1:0;
		int changeResult = mLSEasyControlManager.Set_Power_Amplifier_State(futureStatus);
		LogManager.d( "changeFullParkSwitch      &&&&&&      changeResult : "+changeResult);
	}
	
	public void initSwitchState() {
		try {
			boolean reverseSwitch = isAmpliSwitch();
			setChecked(reverseSwitch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateSwitchState(){
		try {
			changeAmpliSwitch(!isAmpliSwitch());
			initSwitchState();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View arg0) {
		updateSwitchState();
	}

}
