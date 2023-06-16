package com.landsem.setting.carrier;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.R;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.System;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

@SuppressWarnings("serial")
public class HotspotCarrier extends BaseCarrier implements OnCheckedChangeListener {

	private static final int HOTPOINT_PASSWORD_LENTH = 6;
	private static final String DEF_SSID = "carAp";
	private static final String DEF_PWD = "00000000";
	private static final String SS_ID = "wifihot_ss_id";
	private static final String PRE_SHARD_KEY = "wifihot_PreShardKey";
	private static final String WIFI_STATE = "hotpoint_wifiState";
	private static final String HOTSPOT_SECURITY_INDEX = "hotspot_security_index";
	private static final String AP_STATE_CHANGED = "com.landsem.actions.AP_STATE_CHANGED";
	private static final String AP_STATE = "ap_state";
	private static final int AP_STATE_ENABLED = 0x01;
	private static final int AP_STATE_DISABLED = 0x02;
	private static final int RESTART_WIFI = 0x20;
	private static final int REGAIN_ENABLE = 0x21;
	public static final int CLOSE_HOTSPOT = 0x11;
	public static final int OPEN_INDEX = 0;
	public static final int WPA_INDEX = 1;
	public static final int WPA2_INDEX = 2;
	private static final String TAG = HotspotCarrier.class.getSimpleName();
	public int encryptType = WPA2_INDEX;
	private WifiManager wifiManager;
	private WifiConfiguration wifiConfig;
	private ContentResolver resolver;
	private TextView nameView;
	private TextView nameEdit;
	private TextView nameAction;
	private TextView passwordView;
	private TextView passwordEdit;
	private TextView passwordaction;
	private NameWatcher mNameWatcher;
	private PasswordWatcher mPasswordWatcher;
	private Resources mResources;
	private String ssId;
	private String password;
	private String apOpeningStr;
	private String apStopingStr;
	private String apStopedStr;
	private Switch hotspotSwitch;
	private ApStateReceiver mApStateReceiver;
	private String originalWifiState;
	private HotspotHandler mHotspotHandler;
	private TextView stateTextView;
	private View hotspotContent;
	private Context context;
	private int complete_resource;
	private int modify_resource;
	private boolean resetHotspot;
	private boolean resetPassword;

	public HotspotCarrier(Context context, LayoutInflater inflater, int resource) {
		super(context, inflater, resource);
		this.context = context;
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiConfig = wifiManager.getWifiApConfiguration();
		resolver = context.getContentResolver();
		mNameWatcher = new NameWatcher();
		mPasswordWatcher = new PasswordWatcher();
		mApStateReceiver = new ApStateReceiver();
		mHotspotHandler = new HotspotHandler();
		initResources();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.name_action:
			doRenameHotspot();
			break;
		case R.id.password_action:
			doResetPassword();
			break;
		case R.id.hotspot_connect:
//			mHotspotHandler.removeMessages(RESTART_WIFI);
			hotspotConnect();
			break;
		}

	}

	private void doResetPassword() {
		resetPassword = !resetPassword;
		LogManager.d(TAG, "doAlterHotspotPwd      &&&&&&      start");
		if (resetPassword) {
			passwordView.setVisibility(View.GONE);
			passwordEdit.setVisibility(View.VISIBLE);
			passwordEdit.setText(password);
			passwordEdit.setEnabled(true);
			passwordEdit.setClickable(true);
			passwordEdit.requestFocus();
			passwordEdit.setSelected(true);
			CharSequence text = passwordEdit.getText();
			if (text instanceof Spannable) {
				Spannable spanText = (Spannable) text;
				Selection.setSelection(spanText, text.length());
			}
			passwordaction.setText(complete_resource);
		} else {
			passwordView.setVisibility(View.VISIBLE);
			passwordEdit.setVisibility(View.GONE);
			passwordEdit.setEnabled(false);
			passwordEdit.setClickable(false);
			passwordaction.setText(modify_resource);
			password = passwordEdit.getText().toString();
			wifiConfig.preSharedKey = password;
			System.putString(resolver, PRE_SHARD_KEY, password);
			password = System.getString(resolver, PRE_SHARD_KEY);
			passwordView.setText(password);
			stopHotspot();
			startHotspot();
			passwordEdit.clearFocus();
		}
	
		LogManager.d(TAG, "doAlterHotspotPwd      &&&&&&      end");
	}
	
	private void updatePwdActionEnable(boolean enable){
		if(null!=passwordaction){
			passwordaction.setEnabled(enable);
			passwordaction.setClickable(enable);
		}
	}

	private void doRenameHotspot() {
		resetHotspot = !resetHotspot;
		LogManager.d(TAG, "doRenameHotspot      &&&&&&      start");
		if(resetHotspot){
			nameView.setVisibility(View.GONE);
			LogManager.d(TAG, "nameView.setVisibility(View.GONE)");
			nameEdit.setVisibility(View.VISIBLE);
			nameEdit.setText(ssId);
			nameEdit.setEnabled(true);
			nameEdit.setClickable(true);
			nameEdit.requestFocus();
			nameEdit.setSelected(true);
			CharSequence text = nameEdit.getText();
			if (text instanceof Spannable) {
				Spannable spanText = (Spannable) text;
				Selection.setSelection(spanText, text.length());
			}
			nameAction.setText(complete_resource);
		}else{
			nameView.setVisibility(View.VISIBLE);
			LogManager.d(TAG, "nameView.setVisibility(View.VISIBLE)");
			nameEdit.setVisibility(View.GONE);
			nameEdit.setEnabled(false);
			nameEdit.setClickable(false);
			nameAction.setText(modify_resource);
			ssId = conductName(nameEdit.getText().toString());
			nameView.setText(ssId);
			LogManager.d(TAG, "conductName nameView.setText(ssId):"+ssId);
			nameEdit.setText(ssId);
			System.putString(resolver, SS_ID, ssId);
			ssId = System.getString(resolver, SS_ID);
			stopHotspot();
			startHotspot();
		}
		LogManager.d(TAG, "doRenameHotspot      &&&&&&      end");
	}
	
	private String conductName(String hotspotName){
		String resultName = hotspotName;
		if(StringUtils.isBlank(resultName)){
			resultName = ssId;
		}
		return resultName;
	}
	
	

	public void hotspotConnect() {
		LogManager.d(TAG, "hotspotConnect  originalWifiState:   "+originalWifiState);
		boolean wifiStatus = wifiManager.isWifiEnabled();
		LogManager.d(TAG, "hotspotConnect  wifiStatus:   "+wifiStatus);
//		setHotspotEnabled(false);
		boolean wifiApStatus = wifiManager.isWifiApEnabled();
		hotspotSwitch.setChecked(wifiApStatus);
		LogManager.d(TAG, "hotspotConnect  wifiApStatus:   "+wifiApStatus);
		if(wifiApStatus) {
			stopHotspot();
		}else {
			if(wifiStatus){
				openHotspotDialog(context);
			}else{
				startHotspot();
			}
		}
	}
	
	private void openHotspotDialog(Context context) {
		try{
			final Builder dialogBeforeFormat = new Builder(context, 0);
			dialogBeforeFormat.setTitle(R.string.str_prompt);
			dialogBeforeFormat.setMessage(R.string.str_tethering_attention);
			dialogBeforeFormat.setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					setHotspotEnabled(false);
					wifiManager.setWifiEnabled(false);
					System.putString(resolver, WIFI_STATE, String.valueOf(true));
					startHotspot();
					dialog.dismiss();
				}
			});
			dialogBeforeFormat.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
				}
			});
//			dialogBeforeFormat.setOnDismissListener(new OnDismissListener() {
//				@Override
//				public void onDismiss(DialogInterface dialog) {
//					setHotspotEnabled(true);
//					LogManager.d("luohong", "setOnDismissListener      ******************************");
//				}
//			});
			dialogBeforeFormat.create().show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void initViews(View convertView) {
		hotspotContent = convertView.findViewById(R.id.hotspot_content);
		stateTextView = (TextView) convertView.findViewById(R.id.hotspot_state_hint);
		nameView = (TextView) convertView.findViewById(R.id.hotspot_name);
		nameEdit = (TextView) convertView.findViewById(R.id.hotspot_name_edit);
		nameAction = (TextView) convertView.findViewById(R.id.name_action);
		passwordView = (TextView) convertView.findViewById(R.id.hotspot_password);
		passwordEdit = (TextView) convertView.findViewById(R.id.hotspot_password_edit);
		passwordaction = (TextView) convertView.findViewById(R.id.password_action);
		hotspotSwitch = (Switch) convertView.findViewById(R.id.hotspot_connect);

	}

	@Override
	protected void initListener() {
		hotspotSwitch.setOnClickListener(this);
		nameAction.setOnClickListener(this);
		passwordaction.setOnClickListener(this);
		nameEdit.addTextChangedListener(mNameWatcher);
		passwordEdit.addTextChangedListener(mPasswordWatcher);
	}

	@Override
	protected void initViewsState() {
		initSsidPassword();
		wifiConfig.SSID = ssId;
		wifiConfig.preSharedKey = password;
		try {
			encryptType = System.getInt(resolver, HOTSPOT_SECURITY_INDEX);
		} catch (Exception e) {
			e.printStackTrace();
			encryptType = WPA2_INDEX;
			System.putInt(resolver, HOTSPOT_SECURITY_INDEX, encryptType);
		}
		switch (encryptType) {
		case OPEN_INDEX:
			wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			break;
		}
		initHotspotContent(wifiManager.isWifiApEnabled());
	}

	private void initHotspotContent(boolean state) {
		hotspotSwitch.setChecked(state);
		int textViewState = stateTextView.getVisibility();
		int contentState = hotspotContent.getVisibility();
		if (state) {
			if (textViewState==View.VISIBLE) stateTextView.setVisibility(View.GONE);
			if (contentState!=View.VISIBLE) hotspotContent.setVisibility(View.VISIBLE);
		} else {
			if (contentState==View.VISIBLE) hotspotContent.setVisibility(View.GONE);
			if (textViewState!=View.VISIBLE) stateTextView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		LogManager.d(TAG, "onCheckedChanged :   " + isChecked);
	}

	private void initResources() {
		mResources = getContext().getResources();
		complete_resource = R.string.str_tethering_finish;
		modify_resource = R.string.str_tethering_modify;
		apStopedStr = mResources.getString(R.string.str_hotspot_close_msg);
		apStopingStr = mResources.getString(R.string.wifi_ap_stopping);
		apOpeningStr = mResources.getString(R.string.wifi_ap_starting);
//		noneEncrypt = mResources.getString(R.string.str_hotspot_open);
//		wpaEncrypt = mResources.getString(R.string.str_hotspot_wpa);
//		wpa2Encrypt = mResources.getString(R.string.str_hotspot_wpa2_list);
	}

	// 开启热点
	public void startHotspot() {
		try {
			LogManager.d(TAG, "startHotspot      &&&&&&      start ");
			setHotspotEnabled(false);
			if (wifiManager.isWifiEnabled()) {
				System.putString(resolver, WIFI_STATE, String.valueOf(true));
				wifiManager.setWifiEnabled(false);
			}
			LogManager.d(TAG, "startHotspot      mWifiConfig : " + wifiConfig);
//			if (null != wifiConfig) wifiManager.setWifiApEnabled(wifiConfig, false);
//			wifiConfig = new WifiConfiguration();
			
			initSsidPassword();
			wifiConfig.SSID = ssId;
			wifiConfig.preSharedKey = password;
			switch (encryptType) {
			case OPEN_INDEX:
				wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				break;
			case WPA_INDEX:
				wifiConfig.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
				wifiConfig.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
				break;
			default:
				wifiConfig.allowedKeyManagement.set(KeyMgmt.WPA2_PSK);
				wifiConfig.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
				break;
			}
			System.putInt(resolver, HOTSPOT_SECURITY_INDEX, encryptType);
			LogManager.d(TAG, "startHotspot      mWifiConfig22 : " + wifiConfig);
			if (wifiConfig != null) wifiManager.setWifiApEnabled(wifiConfig, true);
			LogManager.d(TAG, "startHotspot      &&&&&&      end ");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initSsidPassword() {
		ssId = System.getString(resolver, SS_ID);
		password = System.getString(resolver, PRE_SHARD_KEY);
		LogManager.d(TAG, "initSsidPassword() --ssId:"+ssId+"---password:"+password);
		if (StringUtils.isEmpty(ssId) || ssId.length() > 32) {
			ssId = DEF_SSID;
			System.putString(resolver, SS_ID, ssId);
		}
		if (StringUtils.isEmpty(password) || password.length() < HOTPOINT_PASSWORD_LENTH) {
			password = DEF_PWD;
			System.putString(resolver, PRE_SHARD_KEY, password);
		}
		nameView.setText(ssId);
		nameEdit.setText(ssId);
		passwordView.setText(password);
	}

	public void stopHotspot() {
		// mHotspotHandler.obtainMessage(CLOSE_HOTSPOT).sendToTarget();
		try {
			setHotspotEnabled(false);
			wifiManager.setWifiApEnabled(wifiConfig, false);
			originalWifiState = System.getString(resolver, WIFI_STATE);
			// Intent intent = new Intent(AP_STATE_CHANGED);
			// intent.putExtra(AP_STATE, AP_STATE_DISABLED);
			// context.sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void doApStateChanged(int state) {
		LogManager.d(TAG, "doApStateChanged      &&&&&&      state : " + state);
		Intent intent = null;
		switch (state) {
		case WifiManager.WIFI_AP_STATE_ENABLING:
			apStateEnabling();
			break;
		case WifiManager.WIFI_AP_STATE_ENABLED:
			intent = new Intent(AP_STATE_CHANGED);
			intent.putExtra(AP_STATE, AP_STATE_ENABLED);
			apStateEnabled();
			break;
		case WifiManager.WIFI_AP_STATE_DISABLING:
			apStateDisabling();
			break;
		case WifiManager.WIFI_AP_STATE_DISABLED:
			apStateDisabled();
			intent = new Intent(AP_STATE_CHANGED);
			intent.putExtra(AP_STATE, AP_STATE_DISABLED);
//			mHotspotHandler.sendEmptyMessage(RESTART_WIFI);
			break;
		}
		if (null != intent) getContext().sendBroadcast(intent);
	}

	private void setHotspotEnabled(boolean enabled) {
		if (null!=hotspotSwitch) {
			hotspotSwitch.setEnabled(enabled);
			hotspotSwitch.setClickable(enabled);
			mHotspotHandler.removeMessages(REGAIN_ENABLE);
			if(!enabled) mHotspotHandler.sendEmptyMessageDelayed(REGAIN_ENABLE, 5*SECOND);
		}
	}

	private void apStateEnabling() {
		setHotspotEnabled(false);
		stateTextView.setText(apOpeningStr);
		hotspotContent.setVisibility(View.GONE);
		stateTextView.setVisibility(View.VISIBLE);
	}

	private void apStateEnabled() {
		stateTextView.setVisibility(View.GONE);
		hotspotContent.setVisibility(View.VISIBLE);
		hotspotSwitch.setChecked(true);
		setHotspotEnabled(true);
	}

	private void apStateDisabling() {
		setHotspotEnabled(false);
		stateTextView.setText(apStopingStr);
		hotspotContent.setVisibility(View.GONE);
		stateTextView.setVisibility(View.VISIBLE);
	}

	private void apStateDisabled() {
		stateTextView.setText(apStopedStr);
		hotspotContent.setVisibility(View.GONE);
		stateTextView.setVisibility(View.VISIBLE);
		hotspotSwitch.setChecked(false);
		setHotspotEnabled(true);
	}
	
	public void init() {
		initViews(contentView);
		initListener();
		initViewsState();
	}
	
	
	public void doOnCreate() {
		mApStateReceiver.registReceiver(context);
	}

	public void doOnDestroy() {
		mApStateReceiver.unregistReceiver(context);
	}
	
	private void updateNameActionEnable(boolean enable){
		if(null!=nameAction){
			nameAction.setEnabled(enable);
			nameAction.setClickable(enable);
		}
	}

	private final class NameWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			boolean enable = StringUtils.isBlank(nameEdit.getText().toString());
			updateNameActionEnable(!enable);
	  }

		@Override
		public void afterTextChanged(Editable s) {

		}
	}

	private final class PasswordWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			int hotspotDisplay = passwordEdit.getVisibility();
			int length = passwordEdit.getText().toString().length();
			if(hotspotDisplay==View.VISIBLE && length<HOTPOINT_PASSWORD_LENTH){
				updatePwdActionEnable(false);
			}else{
				updatePwdActionEnable(true);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	}

	private final class ApStateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(WifiManager.WIFI_AP_STATE_CHANGED_ACTION)) {
				int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_AP_STATE, WifiManager.WIFI_AP_STATE_FAILED);
				doApStateChanged(state);
			}

		}

		public void registReceiver(Context context) {
			IntentFilter filter = new IntentFilter(WifiManager.WIFI_AP_STATE_CHANGED_ACTION);
			context.registerReceiver(this, filter);
		}

		public void unregistReceiver(Context context) {
			context.unregisterReceiver(this);
		}
	}

	private final class HotspotHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CLOSE_HOTSPOT:
				// turnoffHotspot();
				break;
			case CHANGE_STATE_AP_ENABLED:
				// apEnabled();
				break;
			case CHANGE_STATE_AP_DISENABLED:
				// apDisenabled();
				break;
			case RESTART_WIFI:
				if (String.valueOf(true).equals(originalWifiState)) {
					setHotspotEnabled(false);
					wifiManager.setWifiEnabled(true);
					System.putString(resolver, WIFI_STATE, String.valueOf(false));
					sendEmptyMessageDelayed(REGAIN_ENABLE, 3*SECOND);
				}
				break;
			case REGAIN_ENABLE:
				setHotspotEnabled(true);
				break;
			default:
				break;
			}

		}

		private void turnoffHotspot() {
			try {
				wifiManager.setWifiApEnabled(wifiConfig, false);
				originalWifiState = System.getString(resolver, WIFI_STATE);
				Intent intent = new Intent(AP_STATE_CHANGED);
				intent.putExtra(AP_STATE, AP_STATE_DISABLED);
				getContext().sendBroadcast(intent);
				LogManager.d(TAG, "wifiStateBeforeOpenHotspot :   "+ originalWifiState);
				if (String.valueOf(true).equals(originalWifiState)) wifiManager.setWifiEnabled(true);
				System.putString(resolver, WIFI_STATE, String.valueOf(false));
				setHotspotEnabled(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
