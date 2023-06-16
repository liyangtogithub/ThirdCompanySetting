package com.landsem.setting.wifi;

import static android.net.wifi.WifiConfiguration.INVALID_NETWORK_ID;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;

public class WifiHelper {

	public static WifiConfiguration getConfig(AccessPoint mAccessPoint, String ssid, String password) {
		if (mAccessPoint != null && mAccessPoint.networkId != INVALID_NETWORK_ID) {
			return null;
		}

		WifiConfiguration config = new WifiConfiguration();

		if (mAccessPoint == null) {
			config.SSID = AccessPoint.convertToQuotedString(ssid);
			// If the user adds a network manually, assume that it is hidden.
			config.hiddenSSID = true;
		} else if (mAccessPoint.networkId == INVALID_NETWORK_ID) {
			config.SSID = AccessPoint.convertToQuotedString(ssid);
		} else {
			config.networkId = mAccessPoint.networkId;
		}

		switch (mAccessPoint.security) {
		case AccessPoint.SECURITY_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;

		case AccessPoint.SECURITY_WEP:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			if (password != null && password.length() != 0) {
				int length = password.length();
				// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
				if ((length == 10 || length == 26 || length == 58) && password.matches("[0-9A-Fa-f]*")) {
					config.wepKeys[0] = password;
				} else {
					config.wepKeys[0] = '"' + password + '"';
				}
			}
			break;

		case AccessPoint.SECURITY_PSK:
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			if (password != null && password.length() != 0) {
				if (password.matches("[0-9A-Fa-f]{64}")) {
					config.preSharedKey = password;
				} else {
					config.preSharedKey = '"' + password + '"';
				}
			}
			break;
		default:
			return null;
		}
		return config;
	}

	private static final String KEYSTORE_SPACE = "keystore://";

	public static boolean requireKeyStore(WifiConfiguration config) {
		if (config == null) {
			return false;
		}
//		String values[] = { config.ca_cert.value(), config.client_cert.value(), config.private_key.value() };
//		for (String value : values) {
//			if (value != null && value.startsWith(KEYSTORE_SPACE)) {
//				return true;
//			}
//		}
		return false;
	}
}
