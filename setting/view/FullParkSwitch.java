package com.landsem.setting.view;

import com.landsem.common.tools.LogManager;
import com.ls.lseasycontrol.LSEasyControlManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

public class FullParkSwitch extends Switch implements OnClickListener {
	
	private static final String TAG = FullParkSwitch.class.getSimpleName();
	private static final int change_success = 0x00;
	private static final int fullpark_off = 0x00;
	private static final int fullpark_on = 0x01;
	private LSEasyControlManager mLSEasyControlManager;

	public FullParkSwitch(Context context) {
		this(context, null);
	}

	public FullParkSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FullParkSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mLSEasyControlManager = new LSEasyControlManager(context);
		initView();
	}

	private void initView() {
		setOnClickListener(this);
		initSwitchState();
	}
	
	
	private boolean isFullPark(){
		int fullParkSwitch = mLSEasyControlManager.Get_Fullscr_Switch();
		LogManager.d(TAG, "isFullPark      &&&&&&      fullParkSwitch: "+fullParkSwitch);
		return fullParkSwitch==fullpark_on;
	}
	
	private boolean changeFullParkSwitch(boolean status){
		int futureStatus = status?fullpark_on:fullpark_off;
		int changeResult = mLSEasyControlManager.Set_Fullscr_Switch(futureStatus);
		LogManager.d(TAG, "changeFullParkSwitch      &&&&&&      changeResult : "+changeResult);
		return changeResult==change_success;
	}
	
	public void initSwitchState() {
		try {
			boolean reverseSwitch = isFullPark();
			setChecked(reverseSwitch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateSwitchState(){
		try {
			changeFullParkSwitch(!isFullPark());
			initSwitchState();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onClick(View view) {
		updateSwitchState();
	}

}
