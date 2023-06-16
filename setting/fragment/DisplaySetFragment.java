package com.landsem.setting.fragment;

import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.activities.MainActivity;
import com.landsem.setting.carrier.AppManageCarrier;
import com.landsem.setting.carrier.AudioCarrier;
import com.landsem.setting.carrier.BackLightCarrier;
import com.landsem.setting.carrier.LogoLoginCarrier;
import com.landsem.setting.carrier.SerialCarrier;
import com.landsem.setting.carrier.ReverseCarrier;
import com.landsem.setting.carrier.SpeedDialCarrier;
import com.landsem.setting.carrier.StandardSelectView;
import com.landsem.setting.view.MenuTextView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("serial")
public class DisplaySetFragment extends BaseFragment {

	// private static final String TAG =
	// DisplaySetFragment.class.getSimpleName();
	private TextView carLogoOption;
	private TextView lightAdjustOption;
	private TextView microcodeOption;
	private TextView ecartelOption;
	private TextView appListOption;
	private TextView assistOption,backStandard;
	private TextView volumeOption;
	private TextView speedDialOption;
	private BackLightCarrier mBackLightCarrier;
	private ReverseCarrier mAssistCarrier;
	private StandardSelectView mStandardSelectView;
	private AppManageCarrier mAppManageCarrier;
	private SerialCarrier mMicrocodeCarrier;
	private AudioCarrier mAudioCarrier;
	private SpeedDialCarrier mSpeedDialCarrier;
	private LogoLoginCarrier mLogoLoginCarrier;

	public DisplaySetFragment() {
		super();
		injectResource(R.layout.display_set_layout);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initListener() {
		lightAdjustOption.setOnClickListener(this);
		carLogoOption.setOnClickListener(this);
		microcodeOption.setOnClickListener(this);
		ecartelOption.setOnClickListener(this);
		appListOption.setOnClickListener(this);
		assistOption.setOnClickListener(this);
		backStandard.setOnClickListener(this);
		volumeOption.setOnClickListener(this);
		speedDialOption.setOnClickListener(this);
		MainActivity aty = (MainActivity) getActivity();
		// displayOption(aty.optionLocation);
		displayOption(0);
	}

	@Override
	public void initViews(View convertView) {
		contentLayout = (LinearLayout) convertView.findViewById(R.id.display_content);
		carLogoOption = (TextView) convertView.findViewById(R.id.carlogo_option);// 开机画面
		lightAdjustOption = (TextView) convertView.findViewById(R.id.light_adjust_option);// 屏幕亮度调节
		ecartelOption = (TextView) convertView.findViewById(R.id.ecartel_option);
		microcodeOption = (TextView) convertView.findViewById(R.id.microcode_option);
		appListOption = (TextView) convertView.findViewById(R.id.apps_manage);
		assistOption = (TextView) convertView.findViewById(R.id.assist_switchs);
		backStandard = (TextView) convertView.findViewById(R.id.back_standard);
		volumeOption = (TextView) convertView.findViewById(R.id.volume_option);
		speedDialOption = (TextView) convertView.findViewById(R.id.speed_dial_option);
		initGravity();
		if (SettingApp.CLIENT_ID == ClientId.BAICMOTOR)
			appListOption.setVisibility(View.GONE);
	}

	public void displayOption(int optionLocation) {
		switch (optionLocation) {
		case 0:
			onClick(lightAdjustOption);
			break;
		default:
			break;
		}
	}

	private void initGravity() {
		int gravity = Gravity.CENTER;
		lightAdjustOption.setGravity(gravity);
		appListOption.setGravity(gravity);
		assistOption.setGravity(gravity);
		backStandard.setGravity(gravity);
		microcodeOption.setGravity(gravity);
		carLogoOption.setGravity(gravity);
		volumeOption.setGravity(gravity);
		speedDialOption.setGravity(gravity);
	}

	@Override
	public void onClick(View view) {
		TextView textView = null;
		if (view instanceof MenuTextView)
			textView = (MenuTextView) view;
		boolean result = doChangeEnabled(textView);
		int optionLocation = 0;
		switch (view.getId()) {
		case R.id.light_adjust_option:
			createLightCarrier();
			if (result)
				contentLayout.addView(mBackLightCarrier.getContentView());
			optionLocation = 0;
			break;
		case R.id.apps_manage:
			createAppsCarrier();
			if (result)
				contentLayout.addView(mAppManageCarrier.contentView);
			optionLocation = 1;
			break;
		case R.id.assist_switchs:
			createAssistCarrier();
			if (result)
				contentLayout.addView(mAssistCarrier.contentView);
			optionLocation = 2;
			break;
		case R.id.back_standard:
			createStandardCarrier();
			if (result)
				contentLayout.addView(mStandardSelectView);
			optionLocation = 3;
			break;
		case R.id.volume_option:
			createVolumeCarrier();
			if (result)
				contentLayout.addView(mAudioCarrier.contentView);
			optionLocation = 4;
			break;
		case R.id.speed_dial_option:
			createPhoneCarrier();
			if (result)
				contentLayout.addView(mSpeedDialCarrier.contentView);
			optionLocation = 5;
			break;
		case R.id.microcode_option:
			createMicrocodeCarrier();
			if (result)
				contentLayout.addView(mMicrocodeCarrier.contentView);
			optionLocation = 6;
			break;
		case R.id.carlogo_option:
			
			createLogoPasswordCarrier();
			if (result)
				contentLayout.addView(mLogoLoginCarrier.contentView);
			optionLocation = 7;
			break;
			
		}
		((MainActivity) getActivity()).setOptionLocation(optionLocation);
	}

	private void createAppsCarrier() {
		if (null == mAppManageCarrier) {
			mAppManageCarrier = new AppManageCarrier(getActivity(), inflater,
					R.layout.applist_layout);
			mAppManageCarrier.loadApps();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mAppManageCarrier)
			mAppManageCarrier.unregist();
	}

	private void createAssistCarrier() {
		if (null == mAssistCarrier)
			mAssistCarrier = new ReverseCarrier(getActivity(), inflater,
					R.layout.reverse_carrier_layout);
	}
	
	private void createStandardCarrier() {
		if (null == mStandardSelectView)
			mStandardSelectView = new StandardSelectView(getActivity());
	}

	private void createVolumeCarrier() {
		if (null == mAudioCarrier)
			mAudioCarrier = new AudioCarrier(getActivity(), inflater,
					R.layout.audio_carrier_layout);
	}

	private void createLightCarrier() {
		if (null == mBackLightCarrier)
			mBackLightCarrier = BackLightCarrier.newInstance(inflater,
					R.layout.backlight_carrier_layout);
		// if(null==mBackLightCarrier) mBackLightCarrier = new
		// BackLightCarrier(inflater, R.layout.backlight_carrier_layout);
	}

	private void createMicrocodeCarrier() {
		if (null == mMicrocodeCarrier)
			mMicrocodeCarrier = new SerialCarrier(inflater,
					R.layout.microcode_layout);
	}

	private void createPhoneCarrier() {
		if (null == mSpeedDialCarrier)
			mSpeedDialCarrier = new SpeedDialCarrier(inflater,
					R.layout.phone_carrier_layout);
	}

	private void createLogoPasswordCarrier() {
		if (null == mLogoLoginCarrier)
			mLogoLoginCarrier = new LogoLoginCarrier(getActivity(),inflater,
					R.layout.logo_login_layout);
	}
	
	@Override
	public void doOnResume() {
		if (null != mSpeedDialCarrier)
			mSpeedDialCarrier.initViewsState();
	}

	@Override
	public void doOnPause() {

	}

	@Override
	public void onResume() {
		super.onResume();
		doOnResume();
	}

}
