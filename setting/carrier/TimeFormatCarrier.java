package com.landsem.setting.carrier;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class TimeFormatCarrier extends BaseCarrier {
	
	private static final long serialVersionUID = 5955570362258857148L;
	private static final String HOURS_12 = "12";
	private static final String HOURS_24 = "24";
	private static final String DATE_FORMAT1 = "yyyy/MM/dd";
	private static final String DATE_FORMAT2 = "MM/dd";
	private static final String DATE_FORMAT3 = "yyyy-MM-dd";
	private RadioGroup dateFormat;
	private RadioButton radioButton1; 
	private RadioButton radioButton2;
	private RadioButton radioButton3;
	private RadioGroupListener RadioGroupListener;
	private ContentResolver resolver;
	private Switch timeFormatSwitch;
	
	

	public TimeFormatCarrier(LayoutInflater inflater, int resource) {
		super(inflater, resource);
		resolver = SettingApp.getContext().getContentResolver();
		RadioGroupListener = new RadioGroupListener();
		initViews(contentView);
		initListener();
		LogManager.d("timeFormat", "TimeFormatCarrier             onCreate");
	}

	@Override
	protected void initViews(View convertView) {
		timeFormatSwitch = (Switch) convertView.findViewById(R.id.time_format_switch);
		dateFormat = (RadioGroup) convertView.findViewById(R.id.date_format);
		radioButton1 = (RadioButton) convertView.findViewById(R.id.format1);
		radioButton2 = (RadioButton) convertView.findViewById(R.id.format2);
		radioButton3 = (RadioButton) convertView.findViewById(R.id.format3);
		initViewsState();

	}

	@Override
	protected void initListener() {
		timeFormatSwitch.setOnClickListener(this);
		dateFormat.setOnCheckedChangeListener(RadioGroupListener);
		LogManager.d("timeFormat", SettingApp.getContext().getString(R.string.time_format));
	}
	
	@Override
	protected void initViewsState() {
		timeFormatSwitch.setChecked(is24Hour());
		String formatStyle = getDateFormat();
		initDataFormatShow();
		initRadioButtonCheched(formatStyle);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.time_format_switch:
			changeTimeFormat();
			break;
		default:
			break;
		}
	}
	
	private void changeTimeFormat(){
		boolean is24Hour = is24Hour();
		set24Hour(!is24Hour);
		is24Hour = is24Hour();
		timeFormatSwitch.setChecked(is24Hour);
	}
	
	@SuppressLint("SimpleDateFormat")
	private void initDataFormatShow(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat((String) radioButton1.getTag());
		radioButton1.setText(dateFormat.format(date));
		dateFormat.applyPattern((String) radioButton2.getTag());
		radioButton2.setText(dateFormat.format(date));
		dateFormat.applyPattern((String) radioButton3.getTag());
		radioButton3.setText(dateFormat.format(date));
	}
	
	private void initRadioButtonCheched(String formatStyle){
		if (!StringUtils.isEmpty(formatStyle)) {
			radioButton1.setChecked(DATE_FORMAT1.equals(formatStyle));
			radioButton2.setChecked(DATE_FORMAT2.equals(formatStyle));
			radioButton3.setChecked(DATE_FORMAT3.equals(formatStyle));
		}
	}
	
	private void timeUpdated() {
		Intent timeChanged = new Intent(Intent.ACTION_TIME_CHANGED);
		SettingApp.getContext().sendBroadcastAsUser(timeChanged, UserHandle.ALL);
	}

	private String getDateFormat() {
		String dateFormat = Settings.System.getString(resolver, Settings.System.DATE_FORMAT);
		if (StringUtils.isBlank(dateFormat)) dateFormat = DATE_FORMAT1;
		return dateFormat;
	}
	
	private void set24Hour(boolean is24Hour) {
		Settings.System.putString(SettingApp.getContext().getContentResolver(), Settings.System.TIME_12_24, is24Hour ? HOURS_24 : HOURS_12);
		timeUpdated();
	}
	
	private boolean is24Hour() {
		return DateFormat.is24HourFormat(SettingApp.getContext());
	}
	
	private final class RadioGroupListener implements RadioGroup.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
			RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedId);
			String format = (String) radioButton.getTag();
			doDateFormatChange(format);
		}
	}

	private void doDateFormatChange(String format){
		try {
			Settings.System.putString(resolver, Settings.System.DATE_FORMAT, format);
			String saveFormat = Settings.System.getString(resolver, Settings.System.DATE_FORMAT);
			Intent formatIntent = new Intent(Intent.ACTION_DATE_CHANGED);
//			Intent formatIntent = new Intent(Action.DATE_FORMAT_CHANGEED);
			formatIntent.putExtra(Key.DATE_FORMAT, saveFormat);
			SettingApp.getContext().sendBroadcastAsUser(formatIntent, UserHandle.ALL);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
