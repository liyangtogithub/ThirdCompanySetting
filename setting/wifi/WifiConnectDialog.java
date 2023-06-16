package com.landsem.setting.wifi;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class WifiConnectDialog extends Dialog implements OnClickListener {
	
	public static final int BUTTON_SUBMIT = DialogInterface.BUTTON_POSITIVE;
	public static final int BUTTON_FORGET = DialogInterface.BUTTON_NEUTRAL;
	private final DialogInterface.OnClickListener mListener;
	private final AccessPoint mAccessPoint;
	private boolean isConfigured;
	private boolean isInRange;

	public WifiConnectDialog(Context context, DialogInterface.OnClickListener listener, AccessPoint accessPoint) {
		super(context);
		mListener = listener;
		mAccessPoint = accessPoint;
		isConfigured = mAccessPoint.networkId != WifiConfiguration.INVALID_NETWORK_ID;
		isInRange = accessPoint.getLevel() != -1;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup_dialog_wifi_connect);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_conn).setOnClickListener(this);
		findViewById(R.id.btn_forget).setOnClickListener(this);
		resetUI();
	}

	@Override
	public void onClick(View v) {
		dismiss();// dismiss first
		if (mListener != null) {
			int id = DialogInterface.BUTTON_NEGATIVE;
			switch (v.getId()) {
			case R.id.btn_conn:
				id = BUTTON_SUBMIT;
				break;
			case R.id.btn_forget:
				id = BUTTON_FORGET;
			default:
				break;
			}
			mListener.onClick(this, id);
		}
	}

	private void resetUI() {
		if (mAccessPoint.getState() == DetailedState.CONNECTED) {
			findViewById(R.id.btn_conn).setVisibility(View.GONE);
		} else {
			findViewById(R.id.btn_conn).setVisibility(isInRange ? View.VISIBLE : View.GONE);
		}
		findViewById(R.id.btn_forget).setVisibility(isConfigured ? View.VISIBLE : View.GONE);
		String ssid = mAccessPoint.ssid;
		String ssidLabelStr = getContext().getString(R.string.str_wifi_dialog_label_name);
		((TextView) findViewById(R.id.ssid)).setText(String.format(ssidLabelStr, ssid));
		int level = mAccessPoint.getLevel();
		if (level < 0 || level > 4) level = 0;
		String[] array = getContext().getResources().getStringArray(R.array.wifi_signal);
		String levelStr = getContext().getString(R.string.str_wifi_dialog_label_level);
		levelStr = String.format(levelStr, array[level]);
		((TextView) findViewById(R.id.level)).setText(levelStr);
		LogManager.d(" mAccessPoint.getInfo() == "+ mAccessPoint.getInfo());
		if ( null==mAccessPoint.getInfo() ) {
			((TextView) findViewById(R.id.ip)).setVisibility(View.GONE);
			return;
		}
		String ipStr = getContext().getString(R.string.str_wifi_dialog_label_ip);
		ipStr = String.format(ipStr,intToIp( mAccessPoint.getInfo().getIpAddress()));
		((TextView) findViewById(R.id.ip)).setText(ipStr);
	}
	
	 private String intToIp(int ipAddress) {       
         return (ipAddress & 0xFF ) + "." +       
       ((ipAddress >> 8 ) & 0xFF) + "." +       
       ((ipAddress >> 16 ) & 0xFF) + "." +       
       ( ipAddress >> 24 & 0xFF) ;  
    }   
}
