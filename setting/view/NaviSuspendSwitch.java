package com.landsem.setting.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

import com.landsem.common.tools.LogManager;

public class NaviSuspendSwitch extends Switch implements OnClickListener {
	private String NAVI_WINDOW_DISPLAY_CHANGE = "com.ls.action.NAVI_WINDOW_DISPLAY_CHANGE";
	private String NAVI_WINDOW_DISPLAY_VALUE = "navi_window_display";

	public NaviSuspendSwitch(Context context) {
		this(context, null);
	}

	public NaviSuspendSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NaviSuspendSwitch(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		setOnClickListener(this);
		refreshViewsState();
	}

	public void refreshViewsState() {
		setChecked(!"false".equals(Settings.System.getString(getContext().getContentResolver(), NAVI_WINDOW_DISPLAY_VALUE)));
		LogManager.d("BB", "refreshing state of SuspensionFrame,and  state=" + isChecked());
	}

	private void takeNegative() {
		ContentResolver contentResolver = getContext().getContentResolver();
		String config = Settings.System.getString(contentResolver, NAVI_WINDOW_DISPLAY_VALUE);
		Settings.System.putString(contentResolver, NAVI_WINDOW_DISPLAY_VALUE, (config = "false".equals(config) ? "true" : "false"));
		getContext().sendBroadcastAsUser(new Intent(NAVI_WINDOW_DISPLAY_CHANGE), UserHandle.ALL);
		LogManager.d("BB", "taking NegativeValue of NaviSuspensionfFrame,Now Config= " + config);
	}


	@Override
	public void onClick(View view) {
		takeNegative();
		refreshViewsState();
	}

}
