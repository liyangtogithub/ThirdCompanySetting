package com.landsem.setting.upgrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.landsem.setting.SettingApp;
import com.landsem.setting.utils.UpgradeUtil;


public class ScanService {
	private static final String tag = "ScanService";
	public static final String ACTION_TCL_SCAN_PACKAGE = "ACTION_TCL_SCAN_PACKAGE";
	public static final String ACTION_TCL_SCAN_PACKAGE_RESULT = "ACTION_TCL_SCAN_PACKAGE_RESULT";
	public static final String EXTRA_SCAN_HAS_RESULT = "hasPackage";
//	private ScanServiceHandler handler;

//	private BroadcastReceiver receiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (intent == null || ACTION_TCL_SCAN_PACKAGE_RESULT.equals(intent.getAction()) == false)
//				return;
//			handle(context, intent);
//		}
//
//		private void handle(Context context, Intent intent) {
//			boolean hasPackage = intent.getBooleanExtra(EXTRA_SCAN_HAS_RESULT, false);
//			if (hasPackage) {
//				AvailableUpgradeActivity.openWith(context, null, UpgradeUtil.PATH_ALL);
//			}
//			ScanService.this.stopSelf();
//		}
//	};

//	@Override
//	public void onCreate() {
//		super.onCreate();
//		Log.d(tag, "onCreate");
//		handler = new ScanServiceHandler(this);
//		registerReceiver(receiver, new IntentFilter(ACTION_TCL_SCAN_PACKAGE_RESULT));
//	}

//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		String action = null;
//		if (intent == null || (action = intent.getAction()) == null) return 0;
//		if (ACTION_TCL_SCAN_PACKAGE.equals(action)) {
//			handler.obtainMessage().sendToTarget();
//		}
//		return super.onStartCommand(intent, flags, startId);
//	}

//	@Override
//	public void onDestroy() {
//		Log.d(tag, "onDestroy");
//		super.onDestroy();
//		handler = null;
//		unregisterReceiver(receiver);
//	}
//
//	private TupleOfTwo<Integer, Integer> compareToCurrentVersion(ClientInfo info) {
//		int andCmpRet = 0, mcuCmpRet = 0;
//		String andVer = info.android.versionId;
//		if (andVer != null && andVer.trim().length() > 0) {
//			andCmpRet = andVer.compareTo(SystemInfo.getSystemVersion());
//		}
//		String mcuVer = info.mcu.versionId;
//		if (mcuVer != null && mcuVer.trim().length() > 0) {
//			mcuCmpRet = mcuVer.compareTo(SystemInfo.getMcuVersion());
//		}
//		TupleOfTwo<Integer, Integer> result = new TupleOfTwo<Integer, Integer>(andCmpRet, mcuCmpRet);
//		return result;
//	}
//
//	private void notifyAvailableUpgrade(ClientInfo info) {
//		if (info.getOptionalState() == ClientInfo.ALL_OPTIONAL) {
//			AvailableUpgradeActivity.openWith(this, info, UpgradeUtil.PATH_INTERNAL);
//		}
//		else {
//			UpgradeSoonActivity.openWith(this, info, UpgradeUtil.PATH_INTERNAL);
//		}
//		stopSelf();
//	}
//
//
//	public IBinder onBind(Intent intent) {
//		return null;
//	}

	public static void sendScanBroadcast(final int path) {
		Intent intent = new Intent(ACTION_TCL_SCAN_PACKAGE);
		intent.putExtra(UpgradeUtil.EXTRA_SCAN_PATH, path);
		SettingApp.getContext().sendBroadcast(intent);
	}

//	private static class ScanServiceHandler extends WeakReferenceHandler<ScanService> {
//
//		public ScanServiceHandler(ScanService t) {
//			super(t);
//		}
//
//		@Override
//		protected void handleMessage(ScanService ref, Message msg) {
//			ClientInfo info = new CacheFileFinder().find();
//			if (ClientInfo.isEmpty(info) == false) {
//				TupleOfTwo<Integer, Integer> cmpRet = ref.compareToCurrentVersion(info);
//				if (cmpRet.first > 0 || cmpRet.second > 0) {
//					ref.notifyAvailableUpgrade(info);
//				}
//				else {
//					DownloadDirectory.deleteAll();
//				}
//			}
//			else {
//				sendScanBroadcast(UpgradeUtil.PATH_ALL);
//			}
//		}
//	}

}
