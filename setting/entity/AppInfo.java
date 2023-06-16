package com.landsem.setting.entity;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class AppInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Drawable icon;// 应用图标byte类型
	private String versionName;// 版本名
	private String appName;// 应用名
	private String packageName;// 应用包名
	private boolean isUserApp;// 应用是否是用户安装的应用，默认false是系统，true用户应用
	private String sourceDir;// apk安装包所在路径
	private long length;// 安装包大小
	private String versionCode;// 版本号
	private long firstInstallTime;// 安装日期

	// public String[] requestedPermissions;//应用权限


	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isUserApp() {
		return isUserApp;
	}

	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public long getFirstInstallTime() {
		return firstInstallTime;
	}

	public void setFirstInstallTime(long firstInstallTime) {
		this.firstInstallTime = firstInstallTime;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		return result;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AppInfo other = (AppInfo) obj;
		if (appName==null) {
			if (other.appName != null) return false;
		}else if (!appName.equals(other.appName)) return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "appName= " + appName + ",   isUserApp=  " + isUserApp + "";
	}
	
}