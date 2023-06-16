package com.landsem.setting.activities;

import com.landsem.setting.R;
import com.ls.lseasycontrol.LSEasyControlManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class LightStudyActivity extends Activity implements OnClickListener ,OnSeekBarChangeListener{

	private SeekBar backSeekBar;
	TextView backText, saveLowText, saveHighText;
	private static final int MAXIMUM_BACKLIGHT = 255;
	private static final int DELAY_MILLIS = 500;
	private static final int BACK_LIGHT_ADD = 1;
	private static final int BACK_LIGHT_REDUCE = -1;
	private int lightChanged = BACK_LIGHT_REDUCE;
	private Handler backHandler = new Handler();
	private int lightValue = 100;
	private int lightValueOld = 125;
	private boolean saveLow = false;
	private boolean saveHigh = false;
	private int saveLowValue ;
	private int saveHighValue ;
	private LSEasyControlManager mLSEasyControlManager;
	boolean fromUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back_light_task);
		mLSEasyControlManager = new LSEasyControlManager(this);
		mLSEasyControlManager.Back_Light_Adjust_Start();
		initUI();
		initData();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		backHandler.post(runnable);
	}

	private void initData() {
		try {
			lightValueOld = System.getInt(getContentResolver(),System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			lightValueOld = 125;
			e.printStackTrace();
		}
	}

	private void initUI() {
		backText = (TextView) findViewById(R.id.back_text);
		backSeekBar = (SeekBar) findViewById(R.id.back_light_seekbar);
		backSeekBar.setMax(MAXIMUM_BACKLIGHT);
		backSeekBar.setOnSeekBarChangeListener(this);
		saveHighText = (TextView) findViewById(R.id.back_high_confirm);
		saveLowText = (TextView) findViewById(R.id.back_low_confirm);
		saveHighText.setOnClickListener(this);
		saveLowText.setOnClickListener(this);
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		this.fromUser = fromUser;
		if (fromUser) {
			lightValue = seekBar.getProgress();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_high_confirm:
			saveHigh = true;
			saveHighValue = lightValue;
			saveHighText.setBackgroundColor(R.color.black);
			break;

		case R.id.back_low_confirm:
			saveLow = true;
			saveLowValue = lightValue;
			saveLowText.setBackgroundColor(R.color.black);
			break;
		}
		if (saveLow&&saveHigh) {
			mLSEasyControlManager.Set_Back_Light_Zoom(saveHighValue, saveLowValue);
			finish();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		backHandler.removeCallbacks(runnable);
		// 进来是多少，还是多少
		setLight(lightValueOld);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLSEasyControlManager.Back_Light_Adjust_Stop();
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			lightValue = lightValue + lightChanged;
			setLight(lightValue);
			if (lightValue <= 0) {
				lightChanged = BACK_LIGHT_ADD;
			} else if (lightValue >= 255) {
				lightChanged = BACK_LIGHT_REDUCE;
			}
			backHandler.postDelayed(runnable, DELAY_MILLIS);
		}
	};

	private void setLight(int value) {
		lightValue = value;
		refreshTextView();
		try {
			System.putInt(getContentResolver(), System.SCREEN_BRIGHTNESS, value);
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.screenBrightness = (value <= 0 ? 1 : value) / 255f;
			getWindow().setAttributes(lp);
		} catch (Exception e) {
		}
	}
	
	private void refreshTextView() {
		backText.setText(getString(R.string.backlight) + lightValue);
		backSeekBar.setProgress(lightValue);
	}

}
