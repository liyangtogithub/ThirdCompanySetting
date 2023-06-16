package com.landsem.common.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

public class App extends Application {

	private static Application mApplication;
	public static String packageName;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		packageName = getPackageName();

	}
	
	public void catchApplicationException(){
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	public static Application getContext() {
		return mApplication;
	}

	public static String getAppPackageName() {
		return packageName;
	}

	public static <T extends Activity> void startActivity(Class<T> clz) {
		mApplication.startActivity(new Intent(mApplication, clz));
	}

	public static void exit() {
		System.exit(0);
	}
}
