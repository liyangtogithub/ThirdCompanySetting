package com.landsem.setting.view;

import java.util.ArrayList;
import java.util.List;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;

public class SimpleMode extends LinearLayout implements Constant {

	private static final long serialVersionUID = -6904391428928603379L;
	public static final String TAG = SimpleMode.class.getSimpleName();
	private Switch modeSwitch;
	private Switch screenSwitch;
	private RadioGroup radioGroup;
	private Context context;
	private int[] radioIds = { R.id.simple_mode1, R.id.simple_mode2, R.id.simple_mode3 };
	private List<ModeRadio> modeRadioList = new ArrayList<ModeRadio>(radioIds.length);
	private SwitchCheckListener mSwitchCheckListener = new SwitchCheckListener();
	private ModeCheckListener mModeCheckListener = new ModeCheckListener();

	public SimpleMode(Context context) {
		this(context, null);
	}

	public SimpleMode(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SimpleMode(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		inflate(context, R.layout.simplemode_layout, this);
		initViews();
		initListener();
		initViewsStatus();
	}

	private void initListener() {
		modeSwitch.setOnCheckedChangeListener(mSwitchCheckListener);
		screenSwitch.setOnCheckedChangeListener(mSwitchCheckListener);
		radioGroup.setOnCheckedChangeListener(mModeCheckListener);
	}

	private void initViews() {
		screenSwitch = (Switch) findViewById(R.id.screenoff_switch);
		modeSwitch = (Switch) findViewById(R.id.simplemode_switch);
		radioGroup = (RadioGroup) findViewById(R.id.mode_group);
		for (int position = 0; position < radioIds.length; position++) {
			int radioId = radioIds[position];
			ModeRadio mModeRadio = (ModeRadio) findViewById(radioId);
			modeRadioList.add(mModeRadio);
		}
	}

	private void initViewsStatus() {
		int duration = getScreenOffTime();
		boolean simpleSate = duration!=Duration.LOCK_NEVER;
		modeSwitch.setChecked(simpleSate);
		int visiable = simpleSate ? View.VISIBLE : View.INVISIBLE;
		radioGroup.setVisibility(visiable);
		recoveSimpleMode(duration);
		boolean screenStatus = SettingApp.getSystemBoolean(Key.SCREEN_SWITCH, Value.SCREEN_SWITCH);
		if(!modeSwitch.isChecked()){
			if(screenStatus){
				screenStatus = !screenStatus;
				SettingApp.putSystemBoolean(Key.SCREEN_SWITCH, screenStatus);
			}
		}
		screenSwitch.setChecked(screenStatus);
	}
	
	private void resetScreenOffTime(){
		int duration = SettingApp.getSystemInt(Key.MODE_DURATION, Value.MODE_DURATION);
		setScreenOffTime(duration);
		recoveSimpleMode(duration);
		
	}
	
	private void recoveSimpleMode(int duration){
		for(ModeRadio mModeRadio:modeRadioList) {
			int modeDuration = mModeRadio.getDuration();
			boolean checked = modeDuration==duration;
			LogManager.d(TAG, "recoveSimpleMode      &&&&&&      modeDuration: "+modeDuration+", duration: "+duration);
			mModeRadio.setChecked(checked);
		}
	}
	
	private synchronized void setScreenOffTime(int paramInt) {
		ContentResolver resolver = null;
		try {
			resolver = getContext().getContentResolver();
			Settings.System.putInt(resolver,Settings.System.SCREEN_OFF_TIMEOUT, paramInt);
			LogManager.d(TAG, "setScreenOffTime      &&&&&&      " + paramInt + " ms");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!=resolver) resolver = null;
		}
	}
	
	private synchronized int getScreenOffTime() {
		ContentResolver resolver = null;
		int duration = Value.MODE_DURATION;
		try {
			resolver = getContext().getContentResolver();
			duration = Settings.System.getInt(resolver, Settings.System.SCREEN_OFF_TIMEOUT, Value.MODE_DURATION);
			LogManager.d(TAG, "getScreenOffTime      &&&&&&      " + duration + " ms");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!=resolver) resolver = null;
		}
		return duration;
	}
	
	protected void simpleModeCheckChanged(boolean checked) {
		if(checked) resetScreenOffTime();
		else setScreenOffTime(Duration.LOCK_NEVER);
		int visiable = checked ? View.VISIBLE : View.INVISIBLE;
		radioGroup.setVisibility(visiable);
		if(!checked && screenSwitch.isChecked()) screenSwitch.setChecked(false);
	}
	
	protected void screenOffCheckedChanged(boolean checked) {
		LogManager.d(TAG, "screenOffCheckedChanged      &&&&&&      checked: "+checked);
		boolean notifyMark = true;
		if(checked){
			if(!modeSwitch.isChecked()){
				checked = !checked;
				screenSwitch.setChecked(checked);
				notifyMark = false;
				CustomToast.makeText(context, R.string.screen_switch_hint).show();
			}
		}
		if(notifyMark){
			SettingApp.putSystemBoolean(Key.SCREEN_SWITCH, checked);
			LogManager.d(TAG, "screenOffCheckedChanged      ------------------------------ checked : "+checked);
			Intent intent = new Intent(Action.SCREEN_OFF_STATUS_CHANGED);
			intent.putExtra(Key.SCREEN_SWITCH, checked);
			context.sendBroadcast(intent);
		}
	
	}
	
	private final class SwitchCheckListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton mSwitch, boolean checked) {
			switch (mSwitch.getId()) {
			case R.id.simplemode_switch:
				simpleModeCheckChanged(checked);
				break;
			case R.id.screenoff_switch:
				screenOffCheckedChanged(checked);
				break;
			default:
				break;
			}
		}

	}

	private final class ModeCheckListener implements android.widget.RadioGroup.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
			int duration = INVALID_VALUE;
			switch (checkId) {
			case R.id.simple_mode1:
				duration = modeRadioList.get(0).getDuration();
				break;
			case R.id.simple_mode2:
				duration = modeRadioList.get(1).getDuration();
				break;
			case R.id.simple_mode3:
				duration = modeRadioList.get(2).getDuration();
				break;
			default:
				break;
			}
			setScreenOffTime(duration);
			SettingApp.putSystemInt(Key.MODE_DURATION, duration);
			LogManager.d(TAG, "onCheckedChanged      &&&&&&  duration : "+duration);
		}

	}



}
