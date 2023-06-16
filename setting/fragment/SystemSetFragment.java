package com.landsem.setting.fragment;

import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.Constant.PasswordSelect;
import com.landsem.setting.carrier.AssistCarrier;
import com.landsem.setting.carrier.FactoryCarrier;
import com.landsem.setting.carrier.LanguageCarrier;
import com.landsem.setting.carrier.LockScreenCarrier;
import com.landsem.setting.carrier.LogUploadView;
import com.landsem.setting.carrier.LogoLoginCarrier;
import com.landsem.setting.carrier.ParkMonitorCarrier;
import com.landsem.setting.carrier.StandbyCarrier;
import com.landsem.setting.carrier.TimeFormatCarrier;
import com.landsem.setting.carrier.UpGradeCarrier;
import com.landsem.setting.view.MenuTextView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("serial")
public final class SystemSetFragment extends BaseFragment {

	private static final String TAG = SystemSetFragment.class.getSimpleName();
	private TextView alarmScaleOption;
	private TextView recoveryOption;
	private TextView delayAccOption;
	private TextView standbyOption;
	private TextView upgradeOption;
	private TextView languageOption;
	private TextView lockScreenOption;
	private TextView timesetOption;
	private TextView assistTouchOption;
	private TextView parkMonitorOption;
	private TextView uploadLogOption;
	private TextView calibrationTextView;
	private TextView lightStudyTextView;
	private TextView toneSetTextView;
	private TextView cvbsSetTextView;
	private LanguageCarrier mLanguageCarrier;
	private StandbyCarrier mStandbyCarrier;
	private UpGradeCarrier mUpGradeCarrier;
	private FactoryCarrier mFactoryCarrier;
	private LockScreenCarrier mLockScreenCarrier;
	private TimeFormatCarrier mTimeFormatCarrier;
	private AssistCarrier mAssistCarrier;
	private ParkMonitorCarrier mParkMonitorCarrier;
	private LogUploadView mLogUploadView;
	private LogoLoginCarrier mLogoLoginCarrier;

	public SystemSetFragment() {
		super();
		injectResource(R.layout.system_set_layout);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void createLanguageCarrier() {
		if (null == mLanguageCarrier)
			mLanguageCarrier = new LanguageCarrier(inflater,
					R.layout.language_carrier_layout);
		mLanguageCarrier.setActivity(getActivity());
	}

	private void createStandbyCarrier() {
		if (null == mStandbyCarrier)
			mStandbyCarrier = new StandbyCarrier(inflater,
					R.layout.standby_carrier_layout);
	}

	private void createUpGradeCarrier() {
		if (null == mUpGradeCarrier)
			mUpGradeCarrier = new UpGradeCarrier(inflater,
					R.layout.upgrade_carrier_layout);
	}

	private void createFactoryCarrier() {
		if (null == mFactoryCarrier)
			mFactoryCarrier = new FactoryCarrier(getActivity(),inflater,
					R.layout.factory_carrier_layout);
	}

	private void createLockScreenCarrier() {
		if (null == mLockScreenCarrier)
			mLockScreenCarrier = new LockScreenCarrier(inflater,
					R.layout.lockscreen_carrier_layout);
	}

	private void createTimeCarrier() {
		if (null == mTimeFormatCarrier)
			mTimeFormatCarrier = new TimeFormatCarrier(inflater,
					R.layout.time_carrier_layout);
	}

	private void createAssistCarrier() {
		if (null == mAssistCarrier)
			mAssistCarrier = new AssistCarrier(inflater,
					R.layout.assist_carrier_layout);
	}

	private void createParkMonitorCarrier() {
		if (null == mParkMonitorCarrier)
			mParkMonitorCarrier = new ParkMonitorCarrier(inflater,
					R.layout.park_monitor_layout);
	}
	
	private void createLogoPasswordCarrier() {
		if (null == mLogoLoginCarrier)
			mLogoLoginCarrier = new LogoLoginCarrier(getActivity(),inflater,
					R.layout.logo_login_layout);
	}

	@Override
	public void initViews(View convertView) {
		contentLayout = (LinearLayout) convertView
				.findViewById(R.id.system_content);
		languageOption = (TextView) convertView
				.findViewById(R.id.language_option);
		timesetOption = (TextView) convertView
				.findViewById(R.id.timeset_option);
		recoveryOption = (TextView) convertView
				.findViewById(R.id.recovery_option);
		upgradeOption = (TextView) convertView
				.findViewById(R.id.system_upgrade_option);
		lockScreenOption = (TextView) convertView
				.findViewById(R.id.lockscreen_option);
		standbyOption = (TextView) convertView
				.findViewById(R.id.stand_duration_option);
		delayAccOption = (TextView) convertView
				.findViewById(R.id.delay_acc_option);
		alarmScaleOption = (TextView) convertView
				.findViewById(R.id.alarm_scale_option);
		assistTouchOption = (TextView) convertView
				.findViewById(R.id.assistive_touch);
		parkMonitorOption = (TextView) convertView
				.findViewById(R.id.park_monitor_option);
		uploadLogOption = (TextView) convertView
				.findViewById(R.id.upload_log_option);
		calibrationTextView = (TextView) convertView
				.findViewById(R.id.screen_calibration);
		lightStudyTextView = (TextView) convertView
				.findViewById(R.id.light_study);
		toneSetTextView = (TextView) convertView
				.findViewById(R.id.tone_set);
		cvbsSetTextView = (TextView) convertView
				.findViewById(R.id.cvbs_set);
		initGravity();
		if (SettingApp.CLIENT_ID == ClientId.SUO_HANG)
			delayAccOption.setVisibility(View.VISIBLE);
		// standbyOption.setVisibility(View.GONE);
	}

	private void initGravity() {
		int gravity = Gravity.CENTER;
		languageOption.setGravity(gravity);
		timesetOption.setGravity(gravity);
		upgradeOption.setGravity(gravity);
		recoveryOption.setGravity(gravity);
		standbyOption.setGravity(gravity);
		lockScreenOption.setGravity(gravity);
		assistTouchOption.setGravity(gravity);
		parkMonitorOption.setGravity(gravity);
		uploadLogOption.setGravity(gravity);
		calibrationTextView.setGravity(gravity);
		lightStudyTextView.setGravity(gravity);
		toneSetTextView.setGravity(gravity);
		cvbsSetTextView.setGravity(gravity);
	}

	@Override
	public void initListener() {
		languageOption.setOnClickListener(this);
		timesetOption.setOnClickListener(this);
		upgradeOption.setOnClickListener(this);
		recoveryOption.setOnClickListener(this);
		standbyOption.setOnClickListener(this);
		alarmScaleOption.setOnClickListener(this);
		delayAccOption.setOnClickListener(this);
		lockScreenOption.setOnClickListener(this);
		assistTouchOption.setOnClickListener(this);
		parkMonitorOption.setOnClickListener(this);
		uploadLogOption.setOnClickListener(this);
		calibrationTextView.setOnClickListener(this);
		lightStudyTextView.setOnClickListener(this);
		toneSetTextView.setOnClickListener(this);
		cvbsSetTextView.setOnClickListener(this);
		onClick(languageOption);
	}

	@Override
	public void onClick(View view) {
		TextView textView = null;
		if (view instanceof TextView)
			textView = (MenuTextView) view;
		boolean result = doChangeEnabled(textView);
		switch (view.getId()) {
		case R.id.language_option:
			createLanguageCarrier();
			if (result)
				contentLayout.addView(mLanguageCarrier.contentView);
			break;
		case R.id.timeset_option:
			createTimeCarrier();
			if (result)
				contentLayout.addView(mTimeFormatCarrier.contentView);
			break;
		case R.id.assistive_touch:
			createAssistCarrier();
			if (result)
				contentLayout.addView(mAssistCarrier.contentView);
			break;
		case R.id.system_upgrade_option:
			createUpGradeCarrier();
			if (result)
				contentLayout.addView(mUpGradeCarrier.contentView);
			break;
		case R.id.stand_duration_option:
			createStandbyCarrier();
			if (result)
				contentLayout.addView(mStandbyCarrier.contentView);
			break;
		case R.id.lockscreen_option:
			createLockScreenCarrier();
			if (result)
				contentLayout.addView(mLockScreenCarrier.contentView);
			break;
		case R.id.recovery_option:
			createFactoryCarrier();
			if (result)
				contentLayout.addView(mFactoryCarrier.contentView);
			break;
		case R.id.park_monitor_option:
			createParkMonitorCarrier();
			if (result)
				contentLayout.addView(mParkMonitorCarrier.contentView);
			break;
		case R.id.upload_log_option:
			if (null == mLogUploadView)
				mLogUploadView = new LogUploadView(getActivity());
			if (result)
				contentLayout.addView(mLogUploadView);
			break;
		case R.id.screen_calibration:
			createLogoPasswordCarrier();
			if (result){
				contentLayout.addView(mLogoLoginCarrier.contentView);
				mLogoLoginCarrier.setActivityType(PasswordSelect.SCREEN_CALIBRATION);
			}
			break;
		case R.id.light_study:
			createLogoPasswordCarrier();
			if (result){
				contentLayout.addView(mLogoLoginCarrier.contentView);
				mLogoLoginCarrier.setActivityType(PasswordSelect.LIGHT_STUDY);
			}
			break;
		case R.id.tone_set:
			createLogoPasswordCarrier();
			if (result){
				contentLayout.addView(mLogoLoginCarrier.contentView);
				mLogoLoginCarrier.setActivityType(PasswordSelect.TONE_SET);
			}
			break;
		case R.id.cvbs_set:
			createLogoPasswordCarrier();
			if (result){
				contentLayout.addView(mLogoLoginCarrier.contentView);
				mLogoLoginCarrier.setActivityType(PasswordSelect.CVBS_SET);
			}
			break;
			
		}
	}

	@Override
	public void doOnResume() {

	}

	@Override
	public void doOnPause() {

	}

}