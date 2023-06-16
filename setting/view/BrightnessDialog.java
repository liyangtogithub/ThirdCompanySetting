package com.landsem.setting.view;

import com.landsem.common.tools.PreferencesUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.utils.LightnessControl;
import com.ls.config.ConfigManager;
import com.ls.config.ConfigManager.LS_CONFIG_ID;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

@SuppressWarnings("serial")
public class BrightnessDialog extends Dialog implements OnSeekBarChangeListener, OnDismissListener, Constant {

	private static final String TAG = BrightnessDialog.class.getSimpleName();
	private static final int MAXIMUM_BACKLIGHT = 255;
	private Context context;
	private SeekBar daySeekBar;
	private SeekBar nightSeekBar;
	private int seekBarMax = 255;
	private int curProgress;
	private int mScreenBrightnessDim;
	public ConfigManager mConfigManager;

	public BrightnessDialog(Context context) {
		super(context);
		this.context = context;
		mConfigManager = SettingApp.getConfigManager();
	}

	/**
	 * Create the brightness dialog and any resources that are used for the
	 * entire lifetime of the dialog.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.setType(WindowManager.LayoutParams.TYPE_VOLUME_OVERLAY);
		window.getAttributes().privateFlags |= WindowManager.LayoutParams.PRIVATE_FLAG_SHOW_FOR_ALL_USERS;
		window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.requestFeature(Window.FEATURE_NO_TITLE);
		View convertentView = initContentView();
		setContentView(convertentView);
		setCanceledOnTouchOutside(true);
	}

	private View initContentView() {
		View convertentView = View.inflate(context, R.layout.light_contorl_popup_window, null);
		daySeekBar = (SeekBar) convertentView.findViewById(R.id.day_contorl_seekbar);
		nightSeekBar = (SeekBar) convertentView.findViewById(R.id.night_contorl_seekbar);
		seekBarMax = MAXIMUM_BACKLIGHT - mScreenBrightnessDim;
		daySeekBar.setMax(seekBarMax);
		nightSeekBar.setMax(seekBarMax);
		PreferencesUtils.putInt(context, Key.MAX_LIGHT, MAXIMUM_BACKLIGHT - mScreenBrightnessDim);
		int mOldBrightness = LightnessControl.getScreenLightness(context);
		daySeekBar.setProgress(mOldBrightness - mScreenBrightnessDim);
		daySeekBar.setOnSeekBarChangeListener(this);
		nightSeekBar.setOnSeekBarChangeListener(this);
		setOnDismissListener(this);
		curProgress = LightnessControl.getScreenLightness(context);
		return convertentView;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) LightnessControl.setLightness(context, progress, false);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
//		switch (seekBar.getId()) {
//		case R.id.day_contorl_seekbar:
//			mConfigManager.setConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED5, seekBar.getProgress() + "");
//			break;
//		case R.id.night_contorl_seekbar:
//			mConfigManager.setConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED6, seekBar.getProgress() + "");
//			break;
//		default:break;
//		}
//		mConfigManager.configFlush();
//		updateCurLightValue(seekBar.getId(), seekBar.getProgress());
	}

	private void updateCurLightValue(int viewId, int progress) {
//		int lightMode = SettingApp.getInstance().checkOutLightMode();
//		if (lightMode == HEADLAMP_OFF && viewId == R.id.daylight_seekbar) {
//			curProgress = progress;
//		} else if (lightMode == HEADLAMP_ON && viewId == R.id.nightlight_seekbar) {
//			curProgress = progress;
//		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {

		LightnessControl.setLightness(context, curProgress, false);
	}

}
