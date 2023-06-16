package com.landsem.setting.upgrade;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.os.SystemProperties;

import com.landsem.common.tools.LogManager;

public class SystemInfo {
	private static final String TAG = "TclSettings";
	private static final String SYSTEM_VERSION_PROPERTY_KEY = "ro.build.display.id";
//	private static final String SYSTEM_VERSION_PROPERTY_KEY = "android.os.Build.ID";
	
	// private static final String SYSTEM_PUB_DATE = "ro.build.release.date";
	private static final String SYSTEM_PUB_DATE = "ro.build.version.incremental";
	private static final String MCU_FILE_PATH = "/data/mcu_version.txt";
	private static final String DVD_FILE_PATH = "/data/dvd_version.txt";
	private static final String BUZZER_FILE_PATH = "/data/data/tcl_data/buzzer.txt";
	// private static final String VERSION_DESC = "com.tcl.navigator.setting.VERSION_DESC";

	private static final String STR_UNKNOWN = "ICAR.001.0001";

	public static String getSystemPublishDate() {
		String result = STR_UNKNOWN;
		try {
			String incrementalStr = SystemProperties.get(SYSTEM_PUB_DATE, STR_UNKNOWN);
			String dateStr = incrementalStr.split("\\.")[2];
			if (dateStr.length() == 8) {
				result = dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6);
			}
		}
		catch (Exception e) {}
		return result;
	}

	public static String getSystemVersion() {
		
		return SystemProperties.get(SYSTEM_VERSION_PROPERTY_KEY, STR_UNKNOWN);
		
	}
	
	public static String getVersion(){
		return SystemProperties.get("ro.build.id", STR_UNKNOWN);

	}

	public static String getMcuVersion() {
		return readText(MCU_FILE_PATH, 13);
	}

	public static String getDVDLocalVerion() {
		return readText(DVD_FILE_PATH, 40);
	}

	public static String getBuzzerPara() {
		return readBuzzer(BUZZER_FILE_PATH, 1);
	}

	private static String readText(String path, int length) {
		String result = STR_UNKNOWN;
		File file = new File(path);
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			is = new FileInputStream(file);
			IOUtils.write(baos, is, length);
			result = new String(baos.toByteArray());
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			IOUtils.close(is, baos);
		}
		return result;
	}

	private static String readBuzzer(String path, int length) {
		String result = "0";
		File file = new File(path);
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			is = new FileInputStream(file);
			IOUtils.write(baos, is, length);
			result = new String(baos.toByteArray());
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			IOUtils.close(is, baos);
		}
		LogManager.d(TAG, "readBuzzer====" + result);
		return result;
	}

}
