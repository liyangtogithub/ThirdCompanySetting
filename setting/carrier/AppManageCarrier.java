package com.landsem.setting.carrier;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.R;
import com.landsem.setting.adapter.AppListAdapter;
import com.landsem.setting.entity.AppInfo;

public class AppManageCarrier extends BaseCarrier {

	private static final long serialVersionUID = -788284249076992804L;
	private static final String TAG = LoadAppTask.class.getSimpleName();
	private AppInstall mAppInstall = new AppInstall();
	private ListView appListView;
	private List<AppInfo> entitys;
	private AppListAdapter adapter;
	private LoadAppTask mLoadAppTask;
	private PackageManager packageManager;

	public AppManageCarrier(Context context, LayoutInflater inflater, int resource) {
		super(context, inflater, resource);
		packageManager = context.getPackageManager();
		mAppInstall.registReceiver(context);
		initViews(contentView);
		initViewsState();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void initViews(View convertView) {
		appListView = (ListView) convertView.findViewById(R.id.app_listview);
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initViewsState() {
		if (null == entitys) entitys = new ArrayList<AppInfo>();
		if (null == adapter) adapter = new AppListAdapter(getContext(), entitys);
		appListView.setAdapter(adapter);

	}

	public void loadApps() {
		mLoadAppTask = new LoadAppTask();
		mLoadAppTask.execute();
	}

	/**
	 * 异步加载类
	 */
	private final class LoadAppTask extends AsyncTask<Object, AppInfo, Object> {

		@Override
		protected void onPreExecute() {
			entitys.clear();
			adapter.setList(entitys);
		}

		protected Object doInBackground(Object... params) {
			try {
				List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_CONFIGURATIONS);
				for (PackageInfo packageInfo : packageInfos) {
					if (null==packageInfo) continue;
					AppInfo appInfo = createAppInfo(packageInfo);
					if (null!=appInfo) entitys.add(appInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			adapter.setList(entitys);
		}

	}

	private AppInfo createAppInfo(PackageInfo packageInfo) {
		AppInfo appInfo = null;
		if (null!=packageInfo) {
			appInfo = new AppInfo();
			appInfo.setPackageName(packageInfo.packageName);
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			appInfo.setAppName(applicationInfo.loadLabel(packageManager).toString().trim());
			appInfo.setUserApp(!((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0));
			appInfo.setVersionName(packageInfo.versionName);
			String sourceDir = applicationInfo.sourceDir;
			appInfo.setSourceDir(sourceDir);
			appInfo.setIcon(packageInfo.applicationInfo.loadIcon(packageManager));
		}
		return appInfo;
	}

	private void removeAppInfo(String packageName) {
		if (!StringUtils.isBlank(packageName)) {
			Integer removePosition = null;
			for (int position = 0; position < entitys.size(); position++) {
				AppInfo appInfo = entitys.get(position);
				if (appInfo.getPackageName().equals(packageName)) {
					removePosition = position;
					break;
				}
			}
			adapter.removePosition(removePosition);
		}
	}
	
	
	public void unregist(){
		if(null!=mAppInstall) mAppInstall.unRegistReceiver(context);
	}

	private final class AppInstall extends BroadcastReceiver {

		public boolean isRegist;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String packageName = null;
			try {
				LogManager.d(TAG, "action  " + action);
				if (StringUtils.isEquals(action, Intent.ACTION_PACKAGE_ADDED)) {
					packageName = intent.getData().getSchemeSpecificPart();
					PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
					AppInfo appInfo = createAppInfo(packageInfo);
					if (null != appInfo) adapter.addItem(appInfo);
				} else if (StringUtils.isEquals(action, Intent.ACTION_PACKAGE_REMOVED)) {
					packageName = intent.getData().getSchemeSpecificPart();
					removeAppInfo(packageName);
				} else if (StringUtils.isEquals(action, Intent.ACTION_PACKAGE_REPLACED)) {
					packageName = intent.getData().getSchemeSpecificPart();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void registReceiver(Context context) {
			if (!isRegist) {
				IntentFilter filter = new IntentFilter();
				filter.addAction(Intent.ACTION_PACKAGE_ADDED);
				filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
				filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
				filter.addDataScheme("package");
				filter.setPriority(1000);
				context.registerReceiver(this, filter);
				isRegist = !isRegist;
			}
		}

		public void unRegistReceiver(Context context) {
			if (isRegist) {
				context.unregisterReceiver(this);
				isRegist = !isRegist;
			}
		}

	}

}
