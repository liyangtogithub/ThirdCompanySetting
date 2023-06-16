package com.landsem.setting.view;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

import com.landsem.common.tools.LogManager;

public class KeyToneSwitch extends Switch implements OnClickListener {

	public KeyToneSwitch(Context context) {
		this(context, null);
	}

	public KeyToneSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public KeyToneSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		setOnClickListener(this);
		refreshViewsState();
	}

	public void refreshViewsState() {
		try {
			int keyPadToneValue = Settings.System.getInt(getContext()
					.getContentResolver(),
					Settings.System.SOUND_EFFECTS_ENABLED);
			setChecked(keyPadToneValue == 1);
		} catch (SettingNotFoundException e) {
			LogManager.d("BB", "Error！！ When refreshViewsState of keypad_tone "
					+ e.toString());
		}
	}

	private void takeNegative() {
		ContentResolver contentResolver = getContext().getContentResolver();
		try {
			Settings.System
					.putInt(contentResolver,
							Settings.System.SOUND_EFFECTS_ENABLED,
							Settings.System.getInt(contentResolver,
									Settings.System.SOUND_EFFECTS_ENABLED) == 1 ? 0
									: 1);
		} catch (SettingNotFoundException e) {
			LogManager
					.d("BB",
							"Error！！ When NegativeValue of keypad_tone "
									+ e.toString());
		}
	}


	@Override
	public void onClick(View arg0) {
		takeNegative();
		refreshViewsState();
	}

}
