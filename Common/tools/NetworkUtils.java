package com.landsem.common.tools;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

public class NetworkUtils {

	private static final String TAG = "NetworkUtils";

	public static boolean isNetworkAvailable(Context context) {
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = cm.getActiveNetworkInfo();
			if (mNetworkInfo != null)
				return mNetworkInfo.isAvailable() || mNetworkInfo.isConnectedOrConnecting();
//			return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
		}
		return false;
	}

	public static boolean isWIFIAvailable(Context context) {
		return isNetworkAvailable(context, ConnectivityManager.TYPE_WIFI);
	}

	public static boolean isMobileAvailable(Context context) {
		return isNetworkAvailable(context, ConnectivityManager.TYPE_MOBILE);
	}

	private static boolean isNetworkAvailable(Context context, int type) {
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = cm.getNetworkInfo(type);
			if (mNetworkInfo != null)
				return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static boolean isDisconnectEvent(Intent intent) {
		boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		if (noConnectivity) {
			Log.e(TAG, "EXTRA_NO_CONNECTIVITY is true");
			return true;
		}
		NetworkInfo otherNetwork = null;
		try {
			otherNetwork = intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
			if (otherNetwork != null) {
				Log.e(TAG, "EXTRA_OTHER_NETWORK_INFO is not null");
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		NetworkInfo networkInfo = null;
		try {
			networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (networkInfo == null) return false;
		boolean isDisconnect = networkInfo.getState() == State.DISCONNECTED;
		if (isDisconnect) {
			Log.e(TAG, "networkInfo.getState() == State.DISCONNECTED");
		}
		return isDisconnect;
	}

	public static boolean pingHost(String host, int count, int timeoutSec) {
		boolean result = false;
		Process p = null;
		try {
			String script = String.format("ping -c %d -w %d %s", count, timeoutSec, host);
			p = Runtime.getRuntime().exec(script);
			int status = p.waitFor();
			Log.d(TAG,"status: " + status);
			result = status == 0;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				p.destroy();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
