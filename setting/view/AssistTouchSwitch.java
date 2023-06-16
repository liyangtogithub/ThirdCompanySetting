package com.landsem.setting.view;


import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public final class AssistTouchSwitch extends Switch implements OnCheckedChangeListener, Constant {

	private static final long serialVersionUID = -6167637244118344462L;
	private static final String ASSISTIVE_TOUCH = "assistive_touch";
	private static final String PANEL_MODULE_STATE = "com.ls.action.ASSISTIVE_TOUCH";


	public AssistTouchSwitch(Context context) {
		this(context, null);
	}

	public AssistTouchSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AssistTouchSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnCheckedChangeListener(this);
		initAssistTouchState();
	}

	private void initAssistTouchState(){
		boolean assistiveState = SettingApp.getSystemBoolean(ASSISTIVE_TOUCH, true);
		this.setChecked(assistiveState);
	}
		

	@Override
	public void onCheckedChanged(CompoundButton mSwitch, boolean checked) {
		SettingApp.putSystemBoolean(ASSISTIVE_TOUCH, checked);
		sendStateBroadCast(PANEL_MODULE_STATE);
	}

	private void sendStateBroadCast(String action){
		Intent intent = new Intent(action);
		getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
		LogManager.d("luohong", "action : "+action);
	}

}
