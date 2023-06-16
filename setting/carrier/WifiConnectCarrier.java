package com.landsem.setting.carrier;

import static android.net.wifi.WifiConfiguration.INVALID_NETWORK_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.landsem.common.tools.ListUtils;
import com.landsem.common.tools.LogManager;
import com.landsem.setting.R;
import com.landsem.setting.adapter.CustomBaseAdapter;
import com.landsem.setting.view.CustomToast;
import com.landsem.setting.wifi.AccessPoint;
import com.landsem.setting.wifi.InputDialog;
import com.landsem.setting.wifi.WifiConnectDialog;
import com.landsem.setting.wifi.WifiConnectErrowDialog;
import com.landsem.setting.wifi.WifiEnabler;
import com.landsem.setting.wifi.WifiHelper;
import com.landsem.setting.wifi.WifiRow;
import com.ls.mcu.McuManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.security.Credentials;
import android.security.KeyStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class WifiConnectCarrier extends BaseCarrier implements DialogInterface.OnClickListener {

	private static final long serialVersionUID = 6102733818871577063L;
	private static final String TAG = WifiConnectCarrier.class.getSimpleName();
	private static final int DIALOG_CONN = 0;
	private static final int DIALOG_PWD = 1;
	private int mKeyStoreNetworkId = INVALID_NETWORK_ID;
	private WifiManager.ActionListener mConnectionListener;
	private WifiManager.ActionListener mSaveListener;
	private WifiManager.ActionListener mForgetListener;
	private AccessPoint mSelectedAccessPoint;
	private WifiManager mWifiManager;
	private Activity context;
	private WifiEnabler mWifiEnabler;
	private WifiAdapter mAdapter;
	private TextView emptyView;
	private Switch toggle;
	private Dialog mDialog;
	private Scanner mScanner = null;
	private List<AccessPoint> accessPointList = new ArrayList<AccessPoint>();
	private DetailedState mLastState;
	private WifiInfo mLastInfo;
	private IntentFilter mFilter;
	private AtomicBoolean mConnected = new AtomicBoolean(false);
	private WifiConnectReceiver mWifiConnectReceiver;
	private ListView wifiListView;
	private boolean errowDialogShowed;

	public WifiConnectCarrier(Context context, LayoutInflater inflater, int resource) {
		super(inflater, resource);
		this.context = (Activity) context;
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiConnectReceiver = new WifiConnectReceiver();
	}

	public void init() {
		initViews(contentView);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void initViews(View convertView) {
		wifiListView = (ListView) convertView.findViewById(R.id.wifi_list);
		emptyView = (TextView) convertView.findViewById(R.id.empty);
		toggle = (Switch) convertView.findViewById(R.id.wifi_connect);
		mWifiEnabler = new WifiEnabler(context, toggle);
		mAdapter = new WifiAdapter(context, accessPointList);
		initViewsState();
	}

	@Override
	protected void initListener() {

	}

	public void doOnCreate() {
		mWifiConnectReceiver.registReceiver();
		mWifiEnabler.doOnCreate();
	}

	public void doOnDestroy() {
		mWifiConnectReceiver.unregistReceiver();
		mWifiEnabler.doOnDestroy();
	}

	@Override
	protected void initViewsState() {
		wifiListView.setAdapter(mAdapter);
		if (mKeyStoreNetworkId != INVALID_NETWORK_ID && KeyStore.getInstance().state()==KeyStore.State.UNLOCKED) {
			mWifiManager.connect(mKeyStoreNetworkId, mConnectionListener);
		}
		mKeyStoreNetworkId = INVALID_NETWORK_ID;
		updateAccessPoints();
	}

	private void showWifiDialog(int id) {
		if (mDialog == null || !mDialog.isShowing()) {
			switch (id) {
			case DIALOG_CONN:
				mDialog = new WifiConnectDialog(context, this, mSelectedAccessPoint);
				mDialog.show();
				break;
			case DIALOG_PWD:
				mDialog = new InputDialog(context, this);
				mDialog.show();
				break;
			}
		}
	}

	private final class WifiAdapter extends CustomBaseAdapter<AccessPoint>
			implements OnClickListener {

		public WifiAdapter(Context context, List<AccessPoint> entitys) {
			super(context, entitys);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AccessPoint accessPoint = getItem(position);
			final WifiRow mWifiRow;
			if (convertView != null && convertView instanceof WifiRow) {
				mWifiRow = (WifiRow) convertView;
			} else {
				mWifiRow = new WifiRow(mContext);
				mWifiRow.setOnClickListener(this);
//				mWifiRow.refresh(accessPoint, "getView");
			}
			if(null!=mWifiRow && null!=accessPoint){
				accessPoint.position = position;
				mWifiRow.setAccessPoint(accessPoint);
				mWifiRow.setStateChangedListener();
				mWifiRow.refresh(accessPoint, "getView");
			}
			return mWifiRow;
		}

		@Override
		public void onClick(View view) {
			WifiRow wifiRow = (WifiRow) view;
			AccessPoint accessPoint = wifiRow.getAccessPoint();
			if (accessPoint != null) {
				mSelectedAccessPoint = accessPoint;
				showWifiDialog(DIALOG_CONN);
			}
			return;
		}
	}

	private class Scanner extends Handler {
		private int mRetry = 0;

		protected void resume() {
			if (!hasMessages(0)) {
				sendEmptyMessage(0);
			}
		}

		protected void pause() {
			mRetry = 0;
			removeMessages(0);
		}

		@Override
		public void handleMessage(Message message) {
			long startTime = System.currentTimeMillis();
			if (mWifiManager.startScan()) {
				mRetry = 0;
			} else if (++mRetry >= 3) {
				mRetry = 0;
				CustomToast.makeText(context, R.string.wifi_fail_to_scan).show();
				return;
			}
			sendEmptyMessageDelayed(0, WIFI_RESCAN_INTERVAL_MS);
		}
	}

	/** A restricted multimap for use in constructAccessPoints */
	private class Multimap<K, V> {

		private HashMap<K, List<V>> store = new HashMap<K, List<V>>();

		/** retrieve a non-null list of values with key K */
		List<V> getAll(K key) {
			List<V> values = store.get(key);
			return values != null ? values : Collections.<V> emptyList();
		}

		void put(K key, V val) {
			List<V> curVals = store.get(key);
			if (curVals == null) {
				curVals = new ArrayList<V>(3);
				store.put(key, curVals);
			}
			curVals.add(val);
		}
	}

	private static final int WIFI_RESCAN_INTERVAL_MS = 10 * 1000;

	private boolean requireKeyStore(WifiConfiguration config) {
		if (WifiHelper.requireKeyStore(config)
				&& KeyStore.getInstance().state() != KeyStore.State.UNLOCKED) {
			mKeyStoreNetworkId = config.networkId;
			Credentials.getInstance().unlock(context);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(DialogInterface dialogInterface, int which) {
		if (dialogInterface instanceof WifiConnectDialog) {
			switch (which) {
			case WifiConnectDialog.BUTTON_SUBMIT:
				if (mSelectedAccessPoint.security == AccessPoint.SECURITY_NONE) {
					mSelectedAccessPoint.generateOpenNetworkConfig();
					submit(mSelectedAccessPoint.getConfig());
					LogManager.d("WifiConnectDialog  security =  AccessPoint.SECURITY_NONE");
				} else if (mSelectedAccessPoint != null
						&& mSelectedAccessPoint.networkId != INVALID_NETWORK_ID) {
					LogManager.d("WifiConnectDialog  go to connect by pwd ");
					submit(null);
				} else {
					showWifiDialog(DIALOG_PWD);
				}
				break;
			case WifiConnectDialog.BUTTON_FORGET:
				forget();
				break;
			default:
				break;
			}
		} else if (dialogInterface instanceof InputDialog) {
			submit(WifiHelper.getConfig(mSelectedAccessPoint,
					mSelectedAccessPoint.ssid,
					((InputDialog) dialogInterface).getInputString()));
		}
	}

	private void submit(WifiConfiguration config) {
		errowDialogShowed = false;
		LogManager.d("WifiConnectDialog  mSelectedAccessPoint ="+mSelectedAccessPoint);
		if (config == null) {
			if (mSelectedAccessPoint != null
					&& !requireKeyStore(mSelectedAccessPoint.getConfig())
					&& mSelectedAccessPoint.networkId != INVALID_NETWORK_ID) {
				// mWifiManager.connectNetwork(mSelectedAccessPoint.networkId);//note
				mWifiManager.connect(mSelectedAccessPoint.networkId,
						mConnectionListener);
				LogManager.d("WifiConnectDialog  connect have done: mSelectedAccessPoint.networkId ="
						        +mSelectedAccessPoint.networkId);
			}
		} else if (config.networkId != INVALID_NETWORK_ID) {
			if (mSelectedAccessPoint != null) {
				// mWifiManager.connectNetwork(config);//note for 4.3 2014.02.13
				mWifiManager.connect(config, mConnectionListener);
				LogManager.d("WifiConnectDialog  connect have done: networkId ="+config.networkId);
			}
		} else {
			if (requireKeyStore(config)) {
				// mWifiManager.saveNetwork(config);//note for 4.3 2014.02.13 by
				mWifiManager.save(config, mSaveListener);
			} else {
				// mWifiManager.connectNetwork(config);//note for 4.3 2014.02.13
				mWifiManager.connect(config, mConnectionListener);
				LogManager.d("WifiConnectDialog  connect have done: config.networkId ="+config.networkId);
			}
		}
		if (mWifiManager.isWifiEnabled()) {
			mScanner.resume();
		}
		updateAccessPoints();
	}

	private void forget() {
		mWifiManager.forget(mSelectedAccessPoint.networkId, mForgetListener);
		if (mWifiManager.isWifiEnabled()) {
			mScanner.resume();
		}
		updateAccessPoints();
	}

	public boolean isNeedSendToMcu = true;

	private void clearWifiList() {
		if (null!=mAdapter) mAdapter.clear();
	}

	private void addMessagePreference(int messageId) {
		emptyView.setText(messageId);
		emptyView.setVisibility(View.VISIBLE);
		clearWifiList();
	}

	/**
	 * Shows the latest access points available with supplimental information
	 * like the strength of network and the security for it.
	 */
	private synchronized void updateAccessPoints() {
		final int wifiState = mWifiManager.getWifiState();
		switch (wifiState) {
		case WifiManager.WIFI_STATE_ENABLED:
			emptyView.setVisibility(View.GONE);
			final List<AccessPoint> accessPoints = constructAccessPoints();
			Collections.sort(accessPoints);
			clearWifiList();
			for (AccessPoint accessPoint : accessPoints) {
				if (accessPoint.security != AccessPoint.SECURITY_EAP) mAdapter.addItem(accessPoint);
			}
			if (isNeedSendToMcu) {
				isNeedSendToMcu = !isNeedSendToMcu;
				McuManager.sendMcuCommandValues(61, 6, 1);
			}
			break;

		case WifiManager.WIFI_STATE_ENABLING:
			addMessagePreference(R.string.wifi_starting);
			break;

		case WifiManager.WIFI_STATE_DISABLING:
			addMessagePreference(R.string.wifi_stopping);
			break;

		case WifiManager.WIFI_STATE_DISABLED:
			addMessagePreference(R.string.str_wifi_empty_msg);
			break;
		}
	}

	/** Returns sorted list of access points */
	private List<AccessPoint> constructAccessPoints() {
		ArrayList<AccessPoint> accessPoints = new ArrayList<AccessPoint>();
		Multimap<String, AccessPoint> apMap = new Multimap<String, AccessPoint>();
		final List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
		if (!ListUtils.isEmpty(configs)) {
			for (WifiConfiguration config : configs) {
				AccessPoint accessPoint = new AccessPoint(config);
				accessPoint.update(mLastInfo, mLastState, "constructAccessPoints_11");
				accessPoints.add(accessPoint);
				apMap.put(accessPoint.ssid, accessPoint);
			}
		}/*else {
			LogManager.d("constructAccessPoints() WifiManager.getConfiguredNetworks == null ");
		}*/
		final List<ScanResult> results = mWifiManager.getScanResults();
		if (!ListUtils.isEmpty(results)) {
			for (ScanResult result : results) {
				if (result.SSID==null || result.SSID.length()==0 || result.capabilities.contains("[IBSS]")) continue;
				boolean found = false;
				for (AccessPoint accessPoint : apMap.getAll(result.SSID)) {
					if (accessPoint.update(result, "constructAccessPoints_22")) found = true;
				}
				if (!found) {
					AccessPoint accessPoint = new AccessPoint(context, result);
					accessPoints.add(accessPoint);
					apMap.put(accessPoint.ssid, accessPoint);
				}
			}
		}
		return accessPoints;
	}

	private void updateWifiState(int state) {
		
		switch (state) {
		case WifiManager.WIFI_STATE_ENABLED:
			mScanner.resume();
			return;
		case WifiManager.WIFI_STATE_ENABLING:
			addMessagePreference(R.string.wifi_starting);
			mSelectedAccessPoint = null;
			break;

		case WifiManager.WIFI_STATE_DISABLED:
			addMessagePreference(R.string.str_wifi_empty_msg);
			mSelectedAccessPoint = null;
			break;
		}
		mLastInfo = null;
		mLastState = null;
		mScanner.pause();
	}

	private void handleEvent(Context context, Intent intent) {
		String action = intent.getAction();
//		LogManager.d(TAG, "handleEvent      &&&&&&      action: "+action);
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
			updateWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
		} else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
			updateAccessPoints();
		} else if (WifiManager.CONFIGURED_NETWORKS_CHANGED_ACTION.equals(action)
				|| WifiManager.LINK_CONFIGURATION_CHANGED_ACTION.equals(action)) {
			updateAccessPoints();
		} else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
			SupplicantState state = (SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
			updateConnectionState(WifiInfo.getDetailedStateOf(state));
			passWordErrorDialog(intent);
		} else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
			NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			mConnected.set(info.isConnected());
			updateAccessPoints();
			updateConnectionState(info.getDetailedState());
		} else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
			updateConnectionState(null);
		}

	}
	
	/**
	 * WIFI密码错误，弹窗提示
	 * @param context 
	 * @param intent 
	 */
	private void passWordErrorDialog( Intent intent) {
			int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
			if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
				final WifiConnectErrowDialog	wifiErrorDialog = new WifiConnectErrowDialog(context);
				if (!errowDialogShowed) {
					wifiErrorDialog.show();
					errowDialogShowed = true;
				}
			}
	}

	private void updateConnectionState(DetailedState state) {
		if (!mWifiManager.isWifiEnabled()) {
			mScanner.pause();
			return;
		}
		if (state == DetailedState.OBTAINING_IPADDR) {
			mScanner.pause();
		} else {
			mScanner.resume();
		}
		mLastInfo = mWifiManager.getConnectionInfo();
		if (state != null) {
			mLastState = state;
		}

		for (int i = accessPointList.size() - 1; i >= 0; --i) {
			AccessPoint accessPoint = accessPointList.get(i);
			accessPoint.update(mLastInfo, mLastState, "updateConnectionState");
		}
	}

	private final class WifiConnectReceiver extends BroadcastReceiver {

		private boolean regist;
		
		@Override
		public void onReceive(Context context, Intent intent) {
			handleEvent(context, intent);
		}

		public synchronized void registReceiver() {
				mFilter = new IntentFilter();
				mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
				mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
				mFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
				mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
				mFilter.addAction(WifiManager.CONFIGURED_NETWORKS_CHANGED_ACTION);
				mFilter.addAction(WifiManager.LINK_CONFIGURATION_CHANGED_ACTION);
				mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
				mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
				context.registerReceiver(this, mFilter);
				mScanner = new Scanner();
				regist = true;
		}

		public synchronized void unregistReceiver() {
			if(regist){
				context.unregisterReceiver(this);
				regist = false;
			}
		}

	}

}
