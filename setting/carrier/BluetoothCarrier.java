package com.landsem.setting.carrier;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.ls.bluetooth.service.IBluetooth;

@SuppressLint("HandlerLeak")
public class BluetoothCarrier extends BaseCarrier implements OnCheckedChangeListener {

	private static final long serialVersionUID = 3562817739855352123L;
	private static final String TAG = BluetoothCarrier.class.getSimpleName();
	private static final int INIT_BLUETOOTH_INFO = 0x01;
	private static final int GET_BLUETOOTH_INFO = 0x02;
	private static final int CHECK_BRANCH_HANDLER = 0x03;
	private Switch bluetoothAutoConnect;
	private Switch phoneAutoAnswer;
	private TextView bluetoothPattenCode;
	private TextView bluetoothName;
	private TextView bluetoothAddress;
	private IBluetooth iBluetooth;
	private UpdateHandler mUpdateHandler;
	private BluetoothHandler bluetoothHandler;
	private BluetoothRunnable bluetoothRunnable;
//	private String localName;
//	private String pairCode;
//	private String address;
	private boolean autoConnState;
	private boolean autoAsState;

	public BluetoothCarrier(LayoutInflater inflater, int resource) {
		super(inflater, resource);
		mUpdateHandler = new UpdateHandler();
		initViews(contentView);
		initListener();
		if (null == bluetoothRunnable) {
			bluetoothRunnable = new BluetoothRunnable();
			new Thread(bluetoothRunnable).start();
		}
		doSendGetInfoMsg();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void initViews(View convertView) {
		contentView = convertView.findViewById(R.id.bluetooth_content);
		bluetoothAutoConnect = (Switch) convertView.findViewById(R.id.bluetooth_auto_connect);
		phoneAutoAnswer = (Switch) convertView.findViewById(R.id.phone_auto_answer);
		bluetoothPattenCode = (TextView) convertView.findViewById(R.id.bluetooth_patten_code);
		bluetoothName = (TextView) convertView.findViewById(R.id.bluetooth_name);
		bluetoothAddress = (TextView) convertView.findViewById(R.id.bluetooth_address);
	}

	@Override
	protected void initListener() {
		bluetoothAutoConnect.setOnCheckedChangeListener(this);
		phoneAutoAnswer.setOnCheckedChangeListener(this);
		bluetoothAutoConnect.setOnClickListener(this);
		phoneAutoAnswer.setOnClickListener(this);

	}

	@Override
	protected void initViewsState() {
		bluetoothAutoConnect.setChecked(autoConnState);
		phoneAutoAnswer.setChecked(autoAsState);
//		bluetoothName.setText(localName);
//		bluetoothPattenCode.setText(pairCode);
//		bluetoothAddress.setText(address);

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		try {
			if (null != iBluetooth) {
				switch (buttonView.getId()) {
				case R.id.bluetooth_auto_connect:
					iBluetooth.setAutoConnState(isChecked);
					autoConnState = isChecked;
					bluetoothAutoConnect.setChecked(autoConnState);
					break;
				case R.id.phone_auto_answer:
					iBluetooth.setAutoAnswerState(isChecked);
					autoAsState = isChecked;
					phoneAutoAnswer.setChecked(autoAsState);
					break;
				}
				LogManager.d(TAG, "isChecked  :   " + isChecked);
//				doSendGetInfoMsg();
			} else {
				buttonView.setChecked(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final class UpdateHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INIT_BLUETOOTH_INFO:
				initViewsState();
				break;
			case CHECK_BRANCH_HANDLER:
				doSendGetInfoMsg();
				break;
			}
		}
	}

	@SuppressWarnings("unused")
	private final class BluetoothRunnable implements Runnable {

		@Override
		public void run() {
			Looper.prepare();
			bluetoothHandler = new BluetoothHandler();
			Looper.loop();
		}

	}

	private final class BluetoothHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_BLUETOOTH_INFO:
				getBluetoothInfo();
				break;
			}
		}
	}

	public void getBluetoothInfo() {
		iBluetooth = SettingApp.getInstance().iBluetooth;
//		LogManager.d(TAG, "iBluetooth   :    " + iBluetooth);
		if (null==iBluetooth) {
			bluetoothHandler.sendEmptyMessageDelayed(GET_BLUETOOTH_INFO, 2*SECOND);
		} else {
			try {
				bluetoothHandler.removeMessages(GET_BLUETOOTH_INFO);
//				localName = null == iBluetooth.getLocalName() ? "" : iBluetooth.getLocalName();
//				pairCode = iBluetooth.getPairCode();
//				address = iBluetooth.getBluetoothAddress();
				autoConnState = iBluetooth.getAutoConnState();
				autoAsState = iBluetooth.getAutoAnswerState();
//				LogManager.d(TAG, "localName : " + localName);
//				LogManager.d(TAG, "pairCode : " + pairCode);
//				LogManager.d(TAG, "address : " + address);
				LogManager.d(TAG, "autoConnState : " + autoConnState);
				LogManager.d(TAG, "autoAsState : " + autoAsState);

			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mUpdateHandler.sendEmptyMessage(INIT_BLUETOOTH_INFO);
		}
	}

	private void doSendGetInfoMsg() {
		if (null == bluetoothHandler) {
			mUpdateHandler.sendEmptyMessageDelayed(CHECK_BRANCH_HANDLER, SECOND / 2);
		} else {
			mUpdateHandler.removeMessages(CHECK_BRANCH_HANDLER);
			bluetoothHandler.sendEmptyMessage(GET_BLUETOOTH_INFO);
		}
	}

}
