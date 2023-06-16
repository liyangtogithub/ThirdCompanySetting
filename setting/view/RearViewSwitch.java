package com.landsem.setting.view;

import com.landsem.common.tools.LogManager;
import com.ls.config.ConfigManager;
import com.ls.lseasycontrol.LSEasyControlManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

public class RearViewSwitch extends Switch implements OnClickListener {
	
	private static final String TAG = RearViewSwitch.class.getSimpleName();
	private final static String ENABLE = "1-------------";
	private final static String DISENABLE = "0-------------";
	private static final int change_success = 0x01;
	private static final int reverse_switch_off = 0x00;
	private static final int reverse_switch_on = 0x01;
	private LSEasyControlManager mLSEasyControlManager;

	public RearViewSwitch(Context context) {
		this(context, null);
	}

	public RearViewSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RearViewSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mLSEasyControlManager = new LSEasyControlManager(context);
		initView();
	}

	private void initView() {
		setOnClickListener(this);
//		refreshViewsState();
		initSwitchState();
	}
	
	
	private boolean isReverseSwitch(){
		int reverseSwitch = mLSEasyControlManager.Get_Reverse_Switch();
		LogManager.d(TAG, "isReverseSwitch      &&&&&&      reverseSwitch : "+reverseSwitch);
		return reverseSwitch==reverse_switch_on;
	}
	
	private boolean changeReverseSwitch(boolean status){
		int futureStatus = status?reverse_switch_on:reverse_switch_off;
		int changeResult = mLSEasyControlManager.Set_Reverse_Switch(futureStatus);
		LogManager.d(TAG, "changeReverseSwitch      &&&&&&      changeResult : "+changeResult);
		return changeResult==change_success;
	}
	
	public void initSwitchState() {
		try {
			boolean reverseSwitch = isReverseSwitch();
			setChecked(reverseSwitch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateSwitchState(){
		try {
			changeReverseSwitch(!isReverseSwitch());
			initSwitchState();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void refreshViewsState() {
//		ConfigManager configManager = new ConfigManager();
//		String rearState = configManager.getConfigValue(ConfigManager.KERNAL_CONFIG_RESERVED12);
//		setChecked((null!=rearState && rearState.equals(ENABLE)));
//	}
//
//	private void takeNegative() {
//		ConfigManager configManager = new ConfigManager();
//		String rearState = configManager.getConfigValue(ConfigManager.KERNAL_CONFIG_RESERVED12);
//		if (null!=rearState && DISENABLE.equals(rearState)) {
//			configManager.setConfigValue(ConfigManager.KERNAL_CONFIG_RESERVED12, ENABLE);
//		} else {
//			configManager.setConfigValue(ConfigManager.KERNAL_CONFIG_RESERVED12, DISENABLE);
//		}
//		configManager.configFlush();
//	}


	@Override
	public synchronized void onClick(View view) {
//		takeNegative();
//		refreshViewsState();
		updateSwitchState();
	}

}
