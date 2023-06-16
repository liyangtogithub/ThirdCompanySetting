package com.landsem.setting.utils;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceUtil {

	public static String getCPUSerial() {
		String str = "", strCPU = "", cpuAddress = "00000000000000000000000000000000";
		try {
			// 读取CPU信息
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			// 查找CPU序列号
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					// 查找到序列号所在行
					if (str.indexOf("Serial") > -1) {
						// 提取序列号
						strCPU = str.substring(str.indexOf(":") + 1, str.length());
						cpuAddress = strCPU.trim();
						break;
					}
				} 
			}
		} catch (Exception ex) {
			ex.printStackTrace();// 赋予默认值
		}
		return cpuAddress;
	}
	
	public static String getImeiSerial(Context context){
		return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//		return "123132132132132154657486";
	}
}
