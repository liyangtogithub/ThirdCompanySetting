package com.landsem.setting.fragment;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.carrier.BluetoothCarrier;
import com.landsem.setting.carrier.HotspotCarrier;
import com.landsem.setting.carrier.NaviSelectView;
import com.landsem.setting.carrier.RemotePairCarriar;
import com.landsem.setting.carrier.SingalSelectView;
import com.landsem.setting.carrier.WifiConnectCarrier;
import com.landsem.setting.view.MenuTextView;
import com.landsem.setting.view.RemotePairView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("serial")
public class ConnectSetFragment extends BaseFragment {

	private static final String TAG = ConnectSetFragment.class.getSimpleName();
	private TextView wifiConnectOption;
	private TextView bluetoothOption;
	private TextView hotspotOption;
	private TextView naviOption;
	private TextView singalOption;
	private TextView remotePairOption;
	private WifiConnectCarrier mWifiConnectCarrier;
	private BluetoothCarrier mBluetoothCarrier;
	private HotspotCarrier mHotspotCarrier;
	private NaviSelectView mNaviSelectView;
	private SingalSelectView mSingalSelectView;
	private RemotePairView mRemotePairView;

	public ConnectSetFragment() {
		super();
		injectResource(R.layout.connect_set_layout);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWifiConnectCarrier = new WifiConnectCarrier(getActivity(), inflater,
				R.layout.wificonnect_carrier_layout);
		mBluetoothCarrier = new BluetoothCarrier(inflater,
				R.layout.bluetooth_carrier_layout);
	}

	@Override
	public void initViews(View converView) {
		contentLayout = (LinearLayout) converView
				.findViewById(R.id.connect_content);
		wifiConnectOption = (TextView) converView
				.findViewById(R.id.wifi_connect_option);
		bluetoothOption = (TextView) converView
				.findViewById(R.id.bluetooth_connect_option);
		hotspotOption = (TextView) converView.findViewById(R.id.hotspot_option);
		naviOption = (TextView) converView
				.findViewById(R.id.navi_select_option);
		singalOption = (TextView) converView
				.findViewById(R.id.singal_select_option);
		if (!SettingApp.getConfigManager().getMobileSupportState()) {
			singalOption.setVisibility(View.GONE);
		}
		remotePairOption = (TextView) converView.findViewById(R.id.remote_pair);
		if (null != remotePairOption
				&& SettingApp.CLIENT_ID == ClientId.ID_HCSJ) {
			remotePairOption.setVisibility(View.VISIBLE);
		}
		if (SettingApp.CLIENT_ID == ClientId.BAICMOTOR)
			hotspotOption.setVisibility(View.GONE);
		initGravity();
		
		
	}

	private void initGravity() {
		int gravity = Gravity.CENTER;
		wifiConnectOption.setGravity(gravity);
		bluetoothOption.setGravity(gravity);
		hotspotOption.setGravity(gravity);
		naviOption.setGravity(gravity);
		singalOption.setGravity(gravity);
	}

	@Override
	public void initListener() {
		LogManager.d(TAG, "initListener      start");
		wifiConnectOption.setOnClickListener(this);
		bluetoothOption.setOnClickListener(this);
		hotspotOption.setOnClickListener(this);
		naviOption.setOnClickListener(this);
		singalOption.setOnClickListener(this);
		if (null != remotePairOption)
			remotePairOption.setOnClickListener(this);
		onClick(wifiConnectOption);
	}

	@Override
	public void onClick(View view) {
		TextView textView = null;
		if (null != view && view instanceof MenuTextView)
			textView = (TextView) view;
		boolean result = doChangeEnabled(textView);
		switch (view.getId()) {
		case R.id.wifi_connect_option:
			if (result)
				contentLayout.addView(mWifiConnectCarrier.contentView);
			break;
		case R.id.bluetooth_connect_option:
			if (result)
				contentLayout.addView(mBluetoothCarrier.contentView);
			break;
		case R.id.hotspot_option:
			if (result)
				contentLayout.addView(mHotspotCarrier.contentView);
			break;
		case R.id.singal_select_option:
			if (null == mSingalSelectView)
				mSingalSelectView = new SingalSelectView(getActivity());
			if (result)
				contentLayout.addView(mSingalSelectView);
			break;
		case R.id.navi_select_option:
			if (null == mNaviSelectView)
				mNaviSelectView = new NaviSelectView(getActivity());
			if (result)
				contentLayout.addView(mNaviSelectView);
			break;
		case R.id.remote_pair:
			if (result) {
				initRemotePairView();
				if (null != mRemotePairView) {
					mRemotePairView.updateViewsStatus(true, true, true, true);
					contentLayout.addView(mRemotePairView);
				}
			}
			break;
		}
	}

	private void initRemotePairView() {
		if (null == mRemotePairView) {
			mRemotePairView = new RemotePairView(getActivity());
		}
	}

	private void initHotspot() {
		if (null == mHotspotCarrier) {
			mHotspotCarrier = new HotspotCarrier(getActivity(), inflater,
					R.layout.hotspot_layout);
			mHotspotCarrier.init();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}

	@Override
	public void doOnResume() {
		mWifiConnectCarrier.init();
		mWifiConnectCarrier.doOnCreate();
		initHotspot();
		mHotspotCarrier.doOnCreate();
	}

	@Override
	public void doOnPause() {
		mWifiConnectCarrier.doOnDestroy();
		mHotspotCarrier.doOnDestroy();
	}

}
