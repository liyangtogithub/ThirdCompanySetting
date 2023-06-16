/*
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */

package com.landsem.setting.wifi;

import java.util.concurrent.atomic.AtomicBoolean;
import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.view.CustomToast;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
 

public class WifiEnabler implements OnCheckedChangeListener, Constant {
	
	
	private static final String TAG = WifiEnabler.class.getSimpleName();
	private static final int RECOVER_ENABLED = 0x01;
	private final Context mContext;
	private Switch mSwitch;
	private final WifiManager mWifiManager;
	private boolean mStateMachineEvent;
	private final IntentFilter mIntentFilter;
	private WifiHandler mWifiHandler = new WifiHandler();
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			
			if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
				handleWifiStateChanged(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_UNKNOWN));
			
			}else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
			}else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
			}
		}
	};

	public WifiEnabler(Context context, Switch switch_) {
		this.mContext = context;
		this.mSwitch = switch_;
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mIntentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
	}
	
	public void doOnCreate(){
		mContext.registerReceiver(mReceiver, mIntentFilter);
		mSwitch.setOnCheckedChangeListener(this);
	}
	
	public void doOnDestroy(){
		if (mReceiver!=null ) {
			mContext.unregisterReceiver(mReceiver);
		}
		mSwitch.setOnCheckedChangeListener(null);
		if (mWifiHandler!=null) {
			mWifiHandler.removeMessages(RECOVER_ENABLED);
			mWifiHandler=null;
		}
	}
	
	public void resume() {
		// Wi-Fi state is sticky, so just let the receiver update UI
		mContext.registerReceiver(mReceiver, mIntentFilter);
		mSwitch.setOnCheckedChangeListener(this);
//		if(((WifiSettingActvity) mContext).flag){
//			((WifiSettingActvity) mContext).flag = false;
//			((WifiSettingActvity) mContext).isNeedSendToMcu = true;
//			if(!mSwitch.isChecked())mSwitch.setChecked(true);;
//		}
	}

	public void pause() {
		mContext.unregisterReceiver(mReceiver);
		mSwitch.setOnCheckedChangeListener(null);
	}

//	public void setSwitch(Switch switch_) {
//		if (mSwitch == switch_) return;
//		mSwitch.setOnCheckedChangeListener(null);
//		mSwitch = switch_;
//		mSwitch.setOnCheckedChangeListener(this);
//		final int wifiState = mWifiManager.getWifiState();
//		boolean isEnabled = wifiState == WifiManager.WIFI_STATE_ENABLED;
//		boolean isDisabled = wifiState == WifiManager.WIFI_STATE_DISABLED;
//		mSwitch.setChecked(isEnabled);
////		mSwitch.setEnabled(isEnabled || isDisabled);
//		changeSwitchEnable(isEnabled || isDisabled);
//
//	}
	@Override
	public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
		// Do nothing if called as a result of a state machine event
		LogManager.d(TAG, "isChecked  :   mStateMachineEvent = "+mStateMachineEvent);
		if (mStateMachineEvent) return;
		// Disable tethering if enabling Wifi
		int wifiApState = mWifiManager.getWifiApState();
		boolean apState = wifiApState==WifiManager.WIFI_AP_STATE_ENABLING || wifiApState == WifiManager.WIFI_AP_STATE_ENABLED;
		LogManager.d(TAG, "isChecked  :   "+isChecked+"      apState  :   "+apState);
		if (isChecked && apState) {
			openWifiDialog(mContext);
		}else {
			setWifiEnable(isChecked);
		}

	}

	private void setWifiEnable(boolean isChecked) {
//		if (isChecked) mWifiManager.setWifiEnabled(false);
		if (mWifiManager.setWifiEnabled(isChecked)) {
			if (isChecked) {
				Intent intent = new Intent();
				intent.setAction("com.tcl.wifihotspot.action.ACTION_STATE_CHANGED");
				intent.putExtra("com.tcl.wifihotspot.extra.DISPLAY_STATE", 2);
				mContext.sendBroadcast(intent);
			}
//			mSwitch.setEnabled(false);
			changeSwitchEnable(false);
		}else {
			CustomToast.makeText(mContext, R.string.wifi_error).show();
		}
	}

	private void handleWifiStateChanged(int state) {
		switch (state) {
		case WifiManager.WIFI_STATE_ENABLING:
			LogManager.d("WIFI_STATE_ENABLING");
//			mSwitch.setEnabled(false);
			changeSwitchEnable(false);
			break;
		case WifiManager.WIFI_STATE_ENABLED:
			LogManager.d("WIFI_STATE_ENABLED");
			setSwitchChecked(true);
//			mSwitch.setEnabled(true);
			changeSwitchEnable(true);
			break;
		case WifiManager.WIFI_STATE_DISABLING:
			LogManager.d("WIFI_STATE_DISABLING");
//			mSwitch.setEnabled(false);
			changeSwitchEnable(false);
			break;
		case WifiManager.WIFI_STATE_DISABLED:
			LogManager.d("WIFI_STATE_DISABLED");
			setSwitchChecked(false);
//			mSwitch.setEnabled(true);
			changeSwitchEnable(true);
			break;
		default:
			LogManager.d("WIFI_STATE ："+state);
			setSwitchChecked(false);
//			mSwitch.setEnabled(true);
			changeSwitchEnable(true);
			break;
		}
	}
	
	private synchronized void changeSwitchEnable(boolean enabled){
		if(null!=mSwitch){
			mSwitch.setEnabled(enabled);
			mWifiHandler.removeMessages(RECOVER_ENABLED);
			if(!enabled) mWifiHandler.sendEmptyMessageDelayed(RECOVER_ENABLED, 8*SECOND);
			
		}
	}

	public void setSwitchChecked(boolean checked) {
		if (checked != mSwitch.isChecked()) {
			mStateMachineEvent = true;
			mSwitch.setChecked(checked);
			mStateMachineEvent = false;
		}
	}

	private void openWifiDialog(Context context) {
		try{
			Builder dialogBeforeFormat = new Builder(context, 0);
			dialogBeforeFormat.setTitle(R.string.str_prompt);
			dialogBeforeFormat.setMessage(R.string.str_wifi_attention);
			setSwitchChecked(false);//ADD by djj.2013.11.04
			ConfirmListener mConfirmListener = new ConfirmListener();
			CancelListener mCancelListener = new CancelListener();
			dialogBeforeFormat.setPositiveButton(R.string.str_ok, mConfirmListener);
			dialogBeforeFormat.setNegativeButton(R.string.str_cancel, mCancelListener);
			dialogBeforeFormat.create().show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private final class WifiHandler extends Handler{
		@Override
		public synchronized void handleMessage(Message msg) {
			switch (msg.what) {
			case RECOVER_ENABLED:
				LogManager.d("RECOVER_ENABLED mSwitch= "+mSwitch);
				if(null!=mSwitch) mSwitch.setEnabled(true);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	private final class ConfirmListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int arg1) {
			dialog.dismiss();
			mWifiManager.setWifiApEnabled(null, false);
			setWifiEnable(true);
		}
		
	}
	
	private final class CancelListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int arg1) {
			dialog.dismiss();
		}
		
	}


}
