package com.landsem.setting.activities;

import java.util.ArrayList;
import java.util.List;

import com.landsem.common.tools.ListUtils;
import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.ActivityAnimator;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.fragment.CarSetFragment;
import com.landsem.setting.fragment.ConnectSetFragment;
import com.landsem.setting.fragment.DisplaySetFragment;
import com.landsem.setting.fragment.SystemSetFragment;
import com.landsem.setting.fragment.VoiceSetFragment;
import com.ls.config.ConfigManager;
import com.ls.config.ConfigManager.LS_CONFIG_ID;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.drm.DrmStore.RightsStatus;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class MainActivity extends Activity implements OnClickListener, Constant {

	private static final long serialVersionUID = 1L;
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String CONNECTSET_FRAGMENT = "ConnectSet_fragment";
	private static final String DISPLAYSET_FRAGMENT = "DisplaySet_Fragment";
	private static final String VOICESET_FRAGMENT = "VoiceSet_Fragment";
	private static final String SYSTEMSET_FRAGMENT = "SystemSet_Fragment";
	private static final String CAR_FRAGMENT = "CarSet_Fragment";
	private static final int CONNECTSET_TAB = 0x00;
	private static final int DISPLAYSET_TAB = 0x01;
	private static final int VOICESET_TAB = 0x02;
	private static final int SYSTEMSET_TAB = 0x03;
	private static final int CAR_TAB = 0x04;
	private static final String CONFIG_ON = "1";
	private static final String CURRSEL_LOCATION = "currsel_location";
	private static final String LASTSEL_LOCATION = "lastsel_location";
	private static final String OPTION_LOCATION = "option_location";
	private static final String DELAYED = "delayed";
	private List<View> topOptionList;
	private int currselLocation = CONNECTSET_TAB; // 0～3分别对应4个Tab标签的索引值
	private int lastselLocation = -1;
	private ConfigManager mConfigManager;
	public static boolean DEF_VOICE_SWITCH_STATE;
	// public static boolean SUPPORT_3G_NET;
	public static boolean REVEAL_SERIAL;
	public static boolean changeLanguage;
	private UiHandler mUiHandler;
	private View connectOption;
	private View displayOption;
	private View voiceOption;
	private View systemOption;
	private View carOption;
	private View generalFragment;
	private int mainLayoutId;
	public int optionLocation;
	private static final int SHOW_DELAYED = 1;
	private static final int SHOW_TIMELY = 2;
	private static final int SHOW_FRAGMENT = 3;
	private static final int RESELECT_LOCALE = 4;
	public static boolean localeChange;
	ConnectSetFragment mConnectSetFragment;
	DisplaySetFragment mDisplaySetFragment;
	VoiceSetFragment mVoiceSetFragment;
	SystemSetFragment mSystemSetFragment;
	CarSetFragment mCarSetFragment;
	Bundle mSavedInstanceState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		changeLanguage = false;
		initLayoutPointer();
		setContentView(mainLayoutId);
		initHandler();
		doInitConfig();
		initView();
		initOutsideIntent(getIntent());
	
		mSavedInstanceState = savedInstanceState;
		LogManager.d(TAG, "onCreate      &&&&&&      end");
	}

	@Override
	public void onBackPressed() {
		this.finish();
		ActivityAnimator.unzoomBackAnimation(this);
	}

	private void initOutsideIntent(Intent intent) {
		if (null != intent) {
			currselLocation = intent.getIntExtra(CURRSEL_LOCATION,
					currselLocation);
			lastselLocation = intent.getIntExtra(LASTSEL_LOCATION,
					lastselLocation);
			optionLocation = intent
					.getIntExtra(OPTION_LOCATION, optionLocation);
		}
	}

	public void setOptionLocation(int optionLocation) {
		this.optionLocation = optionLocation;
	}

	private void initLayoutPointer() {
		switch (SettingApp.CLIENT_ID) {
		case ClientId.ROAD_ROVER:
			mainLayoutId = R.layout.overall_layout;
			break;
		case ClientId.BAICMOTOR:
			mainLayoutId = R.layout.overall_layout_baicmotor;
			break;
		default:
			mainLayoutId = R.layout.overall_layout;
			break;
		}
	}

	private void initHandler() {
		if (null == mUiHandler)
			mUiHandler = new UiHandler();
	}

	private void initView() {
		topOptionList = new ArrayList<View>();
		generalFragment = findViewById(R.id.general_fragment);
		connectOption = findViewById(R.id.connect_option);
		displayOption = findViewById(R.id.display_option);
		voiceOption = findViewById(R.id.voice_option);
		systemOption = findViewById(R.id.system_option);
		carOption = findViewById(R.id.car_option);
		topOptionList.add(connectOption);
		topOptionList.add(displayOption);
		topOptionList.add(voiceOption);
		topOptionList.add(systemOption);
		if (null != carOption) {
			topOptionList.add(carOption);
		}
		initListener();
	}

	private void initListener() {
		if (!ListUtils.isEmpty(topOptionList)) {
			for (int location = 0; location < topOptionList.size(); location++) {
				topOptionList.get(location).setOnClickListener(this);
			}
		}
	}

	private void doInitConfig() {
		if (null == mConfigManager)
			mConfigManager = SettingApp.getConfigManager();
		String configValue = mConfigManager
				.getConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED19);
		String twodimensionValue = mConfigManager
				.getConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED20);
		DEF_VOICE_SWITCH_STATE = !StringUtils.isEmpty(configValue)
				&& configValue.equals(CONFIG_ON);
		REVEAL_SERIAL = !StringUtils.isEmpty(twodimensionValue)
				&& twodimensionValue.equals(CONFIG_ON);
	}

	private void showDefaultFragment(Bundle savedInstanceState) {
		if (null != savedInstanceState) {
			currselLocation = savedInstanceState.getInt(CURRSEL_LOCATION);
			lastselLocation = savedInstanceState.getInt(LASTSEL_LOCATION);
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			for (int position = 0; position < topOptionList.size(); position++) {
				Fragment fragment = findFragment(position);
				if (null != fragment)
					transaction.hide(fragment);
			}
			transaction.commit();
		}
		setTopButtonState(currselLocation);
		showFragment();
		if (localeChange && null != mUiHandler) {
			mUiHandler.sendEmptyMessageDelayed(RESELECT_LOCALE, 300);
		}
	}
	
	private Fragment findFragment(int location) {
		Fragment fragment = null;
		switch (location) {
		case CONNECTSET_TAB:
			fragment = (Fragment) (getFragmentManager()
					.findFragmentByTag(CONNECTSET_FRAGMENT));
			break;
		case DISPLAYSET_TAB:
			fragment = (Fragment) (getFragmentManager()
					.findFragmentByTag(DISPLAYSET_FRAGMENT));
			break;
		case VOICESET_TAB:
			fragment = (Fragment) (getFragmentManager()
					.findFragmentByTag(VOICESET_FRAGMENT));
			break;
		case SYSTEMSET_TAB:
			fragment = (Fragment) (getFragmentManager()
					.findFragmentByTag(SYSTEMSET_FRAGMENT));
			break;
		case CAR_TAB:
			fragment = (Fragment) (getFragmentManager()
					.findFragmentByTag(CAR_FRAGMENT));
			break;
		}
		return fragment;
	}

	private String getFragmentTag() {
		String tag = null;
		switch (currselLocation) {
		case CONNECTSET_TAB:
			tag = CONNECTSET_FRAGMENT;
			break;
		case DISPLAYSET_TAB:
			tag = DISPLAYSET_FRAGMENT;
			break;
		case VOICESET_TAB:
			tag = VOICESET_FRAGMENT;
			break;
		case SYSTEMSET_TAB:
			tag = SYSTEMSET_FRAGMENT;
			break;
		case CAR_TAB:
			tag = CAR_FRAGMENT;
			break;
		}
		LogManager.d(TAG, "currselLocation : " + currselLocation + "    tag : "
				+ tag);
		return tag;
	}

	private Fragment getFragmentInstance() {
		Fragment fragment = null;
		switch (currselLocation) {
		case CONNECTSET_TAB:
			mConnectSetFragment = new ConnectSetFragment();
			fragment = mConnectSetFragment;
			break;
		case DISPLAYSET_TAB:
			mDisplaySetFragment = new DisplaySetFragment();
			fragment = mDisplaySetFragment;
			break;
		case VOICESET_TAB:
			mVoiceSetFragment = new VoiceSetFragment();
			fragment = mVoiceSetFragment;
			break;
		case SYSTEMSET_TAB:
			mSystemSetFragment = new SystemSetFragment();
			fragment = mSystemSetFragment;
			break;
		case CAR_TAB:
			mCarSetFragment = new CarSetFragment();
			fragment = mCarSetFragment;
			break;
		}
		return fragment;
	}

	public synchronized void showFragment() {
		boolean isFirstAdd = false;
		String tag = null;
		LogManager.d(TAG, "currselLocation : " + currselLocation
				+ "      lastselLocation : " + lastselLocation);
		Fragment curselFragment = findFragment(currselLocation); // 当前要显示的Fragment
		Fragment lastselFragment = findFragment(lastselLocation); // 上一个显示的Fragment
		if (null == curselFragment) {
			isFirstAdd = true;
			tag = getFragmentTag();
			curselFragment = getFragmentInstance();
		}
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		if (null != lastselFragment)
			transaction.hide(lastselFragment);
		if (isFirstAdd) {
			transaction.add(R.id.general_fragment, curselFragment, tag);
		} else {
			transaction.show(curselFragment);
		}
		transaction.commit();
	}

	public void switchFragment(int location) {
		if (location != currselLocation) {
			lastselLocation = currselLocation;
			currselLocation = location;
			showFragment();
		}
	}

	private void setTopButtonState(int optionLocation) {
		for (int location = 0; location < topOptionList.size(); location++) {
			boolean isCurrOption = location == optionLocation;
			topOptionList.get(location).setEnabled(!isCurrOption);
		}
	}

	private boolean switchtoNewFragment(int location) {
		if (location < 0 || location > 4)
			return false;
		switchFragment(location);
		setTopButtonState(location);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CURRSEL_LOCATION, currselLocation);
		outState.putInt(LASTSEL_LOCATION, lastselLocation);
		removeFragment();
		if (changeLanguage) {
			generalFragment.setVisibility(View.INVISIBLE);
		}
		LogManager.d(TAG, "onSaveInstanceState      &&&&&&      end");
	}
	
	private void removeFragment() {
	FragmentTransaction transaction = getFragmentManager().beginTransaction();
	if (transaction != null) {
		if (mConnectSetFragment != null) {
			transaction.remove(mConnectSetFragment);
		}
		if (mDisplaySetFragment != null) {
			transaction.remove(mDisplaySetFragment);
		}
		if (mVoiceSetFragment != null) {
			transaction.remove(mVoiceSetFragment);
		}
		if (mSystemSetFragment != null) {
			transaction.remove(mSystemSetFragment);
		}
		if (mCarSetFragment != null) {
			transaction.remove(mCarSetFragment);
		}
		transaction.commitAllowingStateLoss();
		LogManager.d(TAG, "removeFragment      &&&&&&      end");
	}		
}

//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		lastselLocation = currselLocation;
//		initOutsideIntent(intent);
//		showDefaultFragment(null);
//		gotoOptionLocation();
//	}

//	private void gotoOptionLocation() {
//		Fragment curselFragment = findFragment(currselLocation); // 当前要显示的Fragment
//		if (curselFragment instanceof VoiceSetFragment) {
//			VoiceSetFragment mVoiceSetFragment = (VoiceSetFragment) curselFragment;
//			mVoiceSetFragment.displayOption(optionLocation);
//		}
//	}

	@Override
	protected void onResume() {
		super.onResume();
		int what = null == mSavedInstanceState ? SHOW_TIMELY : SHOW_DELAYED;
		Message msg = mUiHandler.obtainMessage(what, mSavedInstanceState);
		mUiHandler.sendMessageDelayed(msg, 300);
		LogManager.d(TAG, "onResume      &&&&&&      end");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogManager.d(TAG, "onPause      &&&&&&      end");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearView();
		LogManager.d(TAG, "onDestroy      &&&&&&      end");
	}

	private void clearView() {
		if (null != topOptionList && !topOptionList.isEmpty()) {
			topOptionList.clear();
			topOptionList = null;
		}
	}

	private final class UiHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_TIMELY:
				showDefaultFragment((Bundle) msg.obj);
				generalFragment.setVisibility(View.VISIBLE);
				break;
			case SHOW_DELAYED:
				showDefaultFragment((Bundle) msg.obj);
				this.sendEmptyMessageDelayed(SHOW_FRAGMENT, 300);
				break;
			case SHOW_FRAGMENT:
				generalFragment.setVisibility(View.VISIBLE);
				break;
			case RESELECT_LOCALE:
				if (localeChange) {
					onClick(systemOption);
					localeChange = false;
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public synchronized void onClick(View view) {
		switch (view.getId()) {
		case R.id.connect_option:
			LogManager.d(TAG,
					"connect_option   view.getHeight() : " + view.getHeight());
			switchtoNewFragment(CONNECTSET_TAB);
			break;
		case R.id.display_option:
			LogManager.d(TAG, "display_option");
			switchtoNewFragment(DISPLAYSET_TAB);
			break;
		case R.id.voice_option:
			LogManager.d(TAG, "voice_option");
			switchtoNewFragment(VOICESET_TAB);
			break;
		case R.id.system_option:
			LogManager.d(TAG, "system_option");
			switchtoNewFragment(SYSTEMSET_TAB);
			break;
		case R.id.car_option:
			LogManager.d(TAG, "car_option");
			switchtoNewFragment(CAR_TAB);
			break;
		default:
			break;
		}
	}

}
