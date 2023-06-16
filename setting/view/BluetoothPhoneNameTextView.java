package com.landsem.setting.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.ls.bluetooth.service.IBluetooth;

public class BluetoothPhoneNameTextView extends TextView implements OnClickListener {
	
	private Dialog dialog;
	private EditText editText;

	public BluetoothPhoneNameTextView(Context context) {
		this(context, null);
	}

	public BluetoothPhoneNameTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BluetoothPhoneNameTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);

	}

	private void init(Context context) {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialog.setContentView(R.layout.layout_set_bluetooth_device_name);
		editText = (EditText) dialog.findViewById(R.id.setbluetoothdevicename_editext);
		dialog.findViewById(R.id.setbluetoothdevicename_cancel_tv).setOnClickListener(this);
		dialog.findViewById(R.id.setbluetoothdevicename_sure_tv).setOnClickListener(this);
//		setOnClickListener(this);
	}

	private String setDeviceName(String newName) throws Exception {
		Context context = getContext();
		Resources resources = context.getResources();
		if (newName == null || "".equals(newName)) {
			return resources.getString(R.string.cannot_empty);
		}
		if (newName.getBytes("utf-8").length > 20) {
			return resources.getString(R.string.length_less_20);
		}
		IBluetooth iBluetooth = SettingApp.getInstance().iBluetooth;
		if (null == iBluetooth) {
			return resources.getString(R.string.bt_device_not_connect);
		}
		super.setText(newName);
		iBluetooth.setLocalName(newName);
		LogManager.d("BB", "setting bluetooth device Name. Name=" + newName);
		return null;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.bluetooth_name:
			showDialog();
			break;
		case R.id.setbluetoothdevicename_sure_tv:
			doSure();
			break;
		case R.id.setbluetoothdevicename_cancel_tv:
			dismissDialog();
			break;
		}
	}

	private void showDialog() {
		if (null != dialog && !dialog.isShowing()) {
			dialog.show();
		}
	}

	private void dismissDialog() {
		if (null != dialog && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private void doSure() {
		try {
			String result = setDeviceName(editText.getEditableText().toString());
			if (null != result) {
				editText.setError(result);
			} else {
				dismissDialog();
			}
		} catch (Exception e) {
			LogManager.e("BB",
					"ERROR!!When set bluetooth device name. " + e.toString());
		}
	}
}
