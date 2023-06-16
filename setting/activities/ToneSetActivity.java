package com.landsem.setting.activities;

import com.landsem.setting.R;
import com.ls.enhance.EnhanceManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class ToneSetActivity extends Activity implements
		OnSeekBarChangeListener ,OnClickListener {

	private SeekBar contrastSeekBar, colorSeekBar, saturationSeekBar;
	TextView contrastText, colorText, saturationText;
	int contrastValue, colorValue, saturationValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tone_set);
		initUI();
		initData();
		refreshUI();

	}

	private void refreshUI() {
		contrastText.setText(contrastValue+"");
		colorText.setText(colorValue+"");
		saturationText.setText(saturationValue+"");
		contrastSeekBar.setProgress(contrastValue);
		colorSeekBar.setProgress(colorValue);
		saturationSeekBar.setProgress(saturationValue);
	}

	private void initData() {
		contrastValue = EnhanceManager.getDisp0EnhanceContrast();
		colorValue = EnhanceManager.getDisp0EnhanceHue();
		saturationValue = EnhanceManager.getDisp0EnhanceSaturation();
	}

	private void initUI() {
		contrastText = (TextView) findViewById(R.id.contrast_value);
		colorText = (TextView) findViewById(R.id.color_value);
		saturationText = (TextView) findViewById(R.id.saturation_value);
		contrastSeekBar = (SeekBar) findViewById(R.id.contrast_seekbar);
		colorSeekBar = (SeekBar) findViewById(R.id.color_seekbar);
		saturationSeekBar = (SeekBar) findViewById(R.id.saturation_seekbar);
		contrastSeekBar.setOnSeekBarChangeListener(this);
		colorSeekBar.setOnSeekBarChangeListener(this);
		saturationSeekBar.setOnSeekBarChangeListener(this);
		((ImageView) findViewById(R.id.contrast_down)).setOnClickListener(this);
		((ImageView) findViewById(R.id.contrast_up)).setOnClickListener(this);
		((ImageView) findViewById(R.id.color_down)).setOnClickListener(this);
		((ImageView) findViewById(R.id.color_up)).setOnClickListener(this);
		((ImageView) findViewById(R.id.saturation_down)).setOnClickListener(this);
		((ImageView) findViewById(R.id.saturation_up)).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.contrast_down:
			contrastValue--;
			refreshUI();
			EnhanceManager.setDisp0EnhanceContrast(contrastValue);
			break;
       case R.id.contrast_up:
    	   contrastValue++;
    	   refreshUI();
		   EnhanceManager.setDisp0EnhanceContrast(contrastValue);
			break;
       case R.id.color_down:
			colorValue--;
			refreshUI();
			EnhanceManager.setDisp0EnhanceHue(colorValue);
			break;
       case R.id.color_up:
    	    colorValue++;
			refreshUI();
			EnhanceManager.setDisp0EnhanceHue(colorValue);
			break;
       case R.id.saturation_down:
    	    saturationValue--;
		    refreshUI();
		    EnhanceManager.setDisp0EnhanceSaturation(saturationValue);
			break;
       case R.id.saturation_up:
    	    saturationValue++;
		    refreshUI();
		    EnhanceManager.setDisp0EnhanceSaturation(saturationValue);
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		if (!fromUser) {
			return;
		}
		if (progress<=0) {
			progress = 1;
		}
		switch (seekBar.getId()) {
		case R.id.contrast_seekbar:
			contrastValue = progress;
			EnhanceManager.setDisp0EnhanceContrast(contrastValue);
			break;
		case R.id.color_seekbar:
			colorValue = progress;
			EnhanceManager.setDisp0EnhanceHue(colorValue);
			break;
		case R.id.saturation_seekbar:
			saturationValue = progress;
			EnhanceManager.setDisp0EnhanceSaturation(saturationValue);
			break;
		}
		uIHandler.sendEmptyMessage(0);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
		
	}

	Handler uIHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			refreshUI();
		};
	};

}
