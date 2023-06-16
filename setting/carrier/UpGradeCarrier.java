package com.landsem.setting.carrier;

import com.landsem.common.tools.StringUtils;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.upgrade.ScanService;
import com.landsem.setting.upgrade.SystemInfo;
import com.landsem.setting.utils.UpgradeUtil;
import com.landsem.setting.view.CustomToast;
import com.lqpdc.commonlib.view.ActionProcessButton;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UpGradeCarrier extends BaseCarrier {

	private static final long serialVersionUID = -8879639858451363965L;
//	private static final String TAG = UpGradeCarrier.class.getSimpleName();
	private static final String COPY_FILE_FAILED = "android.intent.update.copyfilefailed";
	private static final int DELAY_MILLIS = 2000;
	private ActionProcessButtonStateUpdateRunnable actionProcessButtonStateUpdateRunnable;
	private ActionProcessButtonStateUpdateHandler actionProcessButtonStateUpdateHandler;
	private ActionProcessButton localUpgradeButton;
	private TextView versionDescribe;
	private Context context;

	public UpGradeCarrier(LayoutInflater inflater, int resource) {
		super(inflater, resource);
		context = SettingApp.getContext();
		initViews(contentView);
		initListener();
		actionProcessButtonStateUpdateRunnable = new ActionProcessButtonStateUpdateRunnable();
		actionProcessButtonStateUpdateHandler = new ActionProcessButtonStateUpdateHandler();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ScanService.ACTION_TCL_SCAN_PACKAGE_RESULT);
		filter.addAction(COPY_FILE_FAILED);
		context.registerReceiver(mReceiver, filter);
	}

	@Override
	public void onClick(View view) {
		localUpgradeButton.setClickable(false);
		int state = localUpgradeButton.getState();
		switch (state) {
		case ActionProcessButton.NORMAL_STATE:
			localUpgradeButton.setState(ActionProcessButton.LOADING_STATE);
//			context.stopService(new Intent(context, ScanService.class));
			ScanService.sendScanBroadcast(UpgradeUtil.PATH_EXTERNAL);
			break;
		case ActionProcessButton.SUCCESS_STATE:
//			context.stopService(new Intent(context, ScanService.class));
			UpgradeUtil.notifyUpgrade(UpgradeUtil.PATH_EXTERNAL);
			localUpgradeButton.setState(ActionProcessButton.LOADING_STATE);
			localUpgradeButton.setText(context.getResources().getString(R.string.copy_files));
			break;
		case ActionProcessButton.ERROR_STATE:

			break;
		default:
			break;
		}
	}

	@Override
	protected void initViews(View convertView) {
		localUpgradeButton = (ActionProcessButton) convertView.findViewById(R.id.local_upgrade_button);
		versionDescribe = (TextView) convertView.findViewById(R.id.version_describe);
		initViewsState();
	}

	@Override
	protected void initListener() {
		localUpgradeButton.setOnClickListener(this);
	}

	@Override
	protected void initViewsState() {
		localUpgradeButton.setState(ActionProcessButton.NORMAL_STATE);
		boolean support3gNet = SettingApp.getConfigManager().getMobileSupportState();
		StringBuffer buffer = new StringBuffer();
		buffer.append(SystemInfo.getVersion()).append("-");
		buffer.append(SystemInfo.getMcuVersion()).append("-");
		if(support3gNet){
			String _3gVersion = get3GVersion();
			if(!StringUtils.isBlank(_3gVersion)) buffer.append(get3GVersion()).append("-");
		}
		buffer.append("Android").append(Build.VERSION.RELEASE);
		versionDescribe.setText(buffer.toString());
	}
	
	private String get3GVersion(){
		String _3gVersion = "";
		try {
			_3gVersion = SystemProperties.get("gsm.version.baseband", "L303v01.01b01");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _3gVersion;
	}

	public void onScanResultReceived(boolean hasPackage) {
		try {
			if (hasPackage) {
				localUpgradeButton.setState(ActionProcessButton.SUCCESS_STATE);
				localUpgradeButton.setClickable(true);
				return;
			}
			localUpgradeButton.setClickable(false);
			localUpgradeButton.setState(ActionProcessButton.ERROR_STATE);
			actionProcessButtonStateUpdateHandler.postDelayed(actionProcessButtonStateUpdateRunnable, DELAY_MILLIS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final class ActionProcessButtonStateUpdateRunnable implements Runnable {
		
		private int state;

		@SuppressWarnings("unused")
		public void setState(int state) {
			this.state = state;
		}

		@Override
		public void run() {
			Message msg = actionProcessButtonStateUpdateHandler.obtainMessage();
			msg.what = state;
			msg.sendToTarget();
		}
	}

	@SuppressLint("HandlerLeak")
	private final class ActionProcessButtonStateUpdateHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (null != localUpgradeButton) {
				localUpgradeButton.setState(msg.what);
				localUpgradeButton.setClickable(true);
			}
		}
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			if (null != intent) {
				String action = intent.getAction();
				if (!StringUtils.isEmpty(action)) {
					if (ScanService.ACTION_TCL_SCAN_PACKAGE_RESULT.equals(action)) {
						boolean hasPackage = intent.getBooleanExtra(ScanService.EXTRA_SCAN_HAS_RESULT, false);
						onScanResultReceived(hasPackage);
					} else if (COPY_FILE_FAILED.equals(action)) {
						localUpgradeButton.setState(ActionProcessButton.NORMAL_STATE);
						localUpgradeButton.setClickable(true);
						String copyFailed = context.getResources().getString(R.string.copy_file_failed);
						CustomToast.makeText(context, copyFailed + "").show();
					}
				}
			}
		}

	};

}
