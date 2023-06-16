package com.landsem.setting.view;

import com.landsem.setting.R;
import com.landsem.setting.SettingApp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class NaviVolumeBar extends LinearLayout implements OnClickListener, OnSeekBarChangeListener{
	
	private static final String NAVI_VOLUME = "naviVolume";
	private static final int STEP = 5;
	private View volumeMinus;
	private View volumeAdd;
	private SeekBar volumeSeekBar;
	private TextView describeView;
	private int currVolume;
	

	public NaviVolumeBar(Context context) {
		this(context, null);
	}
	
	public NaviVolumeBar(Context context, AttributeSet attrs) {
		this(context, attrs, 1);
	}
	
	public NaviVolumeBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.volume_adjust_layout, this);
		initViews();
		initViewListener();
		initValue();
	}

	private void initViews(){
		volumeAdd = findViewById(R.id.volume_add);
		volumeMinus = findViewById(R.id.volume_minus);
		describeView = (TextView) findViewById(R.id.option_describe);
		volumeSeekBar = (SeekBar) findViewById(R.id.volume_seek);
	}
	
	private void initViewListener(){
		volumeAdd.setOnClickListener(this);
		volumeMinus.setOnClickListener(this);
		volumeSeekBar.setOnSeekBarChangeListener(this);
	}
	
	private void initValue(){
		describeView.setText(R.string.navi_volume);
		try {
			currVolume = SettingApp.getSystemInt(NAVI_VOLUME, 50);
		} catch (Exception e) {
			e.printStackTrace();
			currVolume = 50;
		}
		volumeSeekBar.setProgress(currVolume);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.volume_add:
			currVolume += STEP;
			break;
		case R.id.volume_minus:
			currVolume -= STEP;
			break;
		}
		if(currVolume>volumeSeekBar.getMax()) currVolume = volumeSeekBar.getMax();
		if(currVolume<0) currVolume = 0;
		volumeSeekBar.setProgress(currVolume);
		SettingApp.putSystemInt(NAVI_VOLUME, currVolume);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) {
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
		SettingApp.putSystemInt(NAVI_VOLUME, seekBar.getProgress());

	}


}
