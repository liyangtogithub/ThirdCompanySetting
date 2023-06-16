package com.landsem.setting.carrier;

import java.util.ArrayList;
import java.util.List;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.Constant.Action;
import com.landsem.setting.adapter.NaviAdapter;
import com.landsem.setting.entity.NaviInfo;
import com.landsem.setting.receiver.BootCompletedReceiver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NaviSelectView extends RelativeLayout implements OnItemClickListener, Constant{
	private static final String TAG = NaviSelectView.class.getSimpleName();
	private Context context;
	private String selectApp;
	private TextView naviappName;
	private ListView naviListView;
	private NaviAdapter mNaviAdapter;
	private PackageManager packageManager;
	private List<NaviInfo> naviInfos = new ArrayList<NaviInfo>();
	private boolean isNaviListViewVisible = true;
	
	
	public NaviSelectView(Context context) {
		super(context);
		infiateView(context);
	}

	public NaviSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		infiateView(context);
	}

	public NaviSelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		infiateView(context);
	}
	
	private void infiateView(Context context){
		this.context = context;
		inflate(context, R.layout.navi_select_layout, this);
		mNaviAdapter = new NaviAdapter(context, naviInfos);
		packageManager = context.getPackageManager();
		initViews();
		naviListView.setAdapter(mNaviAdapter);
		naviListView.setOnItemClickListener(this);
		initViewsData();
	}
	
	private void initViewsData() {
		NaviLoadTask mNaviLoadTask = new NaviLoadTask();
		mNaviLoadTask.execute();
	}

	private void initViews(){
		naviappName = (TextView) findViewById(R.id.naviapp_name);
		naviListView = (ListView) findViewById(R.id.navi_listview);
	}
	
	private final class NaviLoadTask extends AsyncTask<Object, Integer, List<NaviInfo>>{

		@Override
		protected List<NaviInfo> doInBackground(Object... arg0) {
			
			return initNaviData();
		}
		
		@Override
		protected void onPostExecute(List<NaviInfo> result) {
			if(!StringUtils.isBlank(selectApp)) naviappName.setText(selectApp);
			naviInfos = result;
			mNaviAdapter.setList(naviInfos);
			showNaviListViewVisibility();
			
		}
		
		private void showNaviListViewVisibility() {
			if (!isNaviListViewVisible) {
				naviListView.setVisibility(View.INVISIBLE);
			}
		}

		private List<NaviInfo> initNaviData() {
			List<NaviInfo> infoList = new ArrayList<NaviInfo>();
			List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_CONFIGURATIONS);//
			for (PackageInfo packageInfo : packageInfos) {
				if (null!=packageInfo && !StringUtils.isEmpty(packageInfo.packageName) && isMapPackage(packageInfo)) {
					ApplicationInfo applicationInfo = packageInfo.applicationInfo;
					String appName = applicationInfo.loadLabel(packageManager).toString();
					Intent intent = packageManager.getLaunchIntentForPackage(packageInfo.packageName);
					ComponentName componentName = null;
					if(null!=intent) componentName = intent.getComponent();
				    if(null!=componentName){
				        String className = componentName.getClassName();
				        boolean isUserApp = !((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
				        if(isUserApp) infoList.add(new NaviInfo(appName, packageInfo.packageName, className));
				    }
				}
			}
			String mapPackageName = SettingApp.getSystemString(Key.MAP_PACKAGE, null);
			if (!StringUtils.isEmpty(mapPackageName)) {
				for (int location = 0; location < infoList.size(); location++) {
					NaviInfo naviInfo = infoList.get(location);
					boolean currSelect = null!=naviInfo && !StringUtils.isEmpty(naviInfo.packageName) && naviInfo.packageName.equals(mapPackageName);
					naviInfo.select = currSelect;
					if(currSelect) selectApp = naviInfo.appLable;
//					initListViewVisible(naviInfo.packageName);
				}
			}
			return infoList;
		}
		
		private boolean isMapPackage(PackageInfo packageInfo) {
			if (!packageInfo.packageName.startsWith("com.ls.") && 
					!packageInfo.packageName.startsWith("com.car.") && 
					!packageInfo.packageName.startsWith("com.android.")) {
				if (     ( -1 != packageInfo.packageName.indexOf("navi") )  ||
						 ( -1 != packageInfo.packageName.indexOf("map")  )  ||
						 ( -1 != packageInfo.packageName.indexOf("Map")  )  ||
						 packageInfo.packageName.startsWith("com.waze")     ||
						 packageInfo.packageName.startsWith("com.nng.igo") /* ||
						 packageInfo.packageName.startsWith("air.com.cellogroup")||
						 packageInfo.packageName.startsWith("tunein.player")*/
						 
						) {
					
					return true;
				}
			}
			return false;
		}
//TODO 如果是AR，就指定默认百度
//		private void initListViewVisible(String packageName) {
//			if (Action.AR_PACKAGE_NAME.equals(packageName)) {
//				isNaviListViewVisible = false;
//			}
//		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if(mNaviAdapter.isLocationValid(position)){
			NaviInfo naviInfo = mNaviAdapter.getItem(position);
			if(null!=naviInfo){
				for(int location=0;location<mNaviAdapter.getCount();location++){
					NaviInfo newInfo = mNaviAdapter.getItem(location);
					boolean mark = naviInfo==newInfo;
					newInfo.select = mark;
					if(mark){
						SettingApp.putSystemString(Key.MAP_PACKAGE, naviInfo.packageName);
						SettingApp.putSystemString(Key.MAP_CLASS, naviInfo.className);
						LogManager.d(TAG, "insert into def Navi  naviInfo.className: "+naviInfo.className);
						selectApp = naviInfo.appLable;
						naviappName.setText(selectApp);
						Intent naviChange = new Intent(Action.NAVI_CHANGE);
						context.sendBroadcastAsUser(naviChange, UserHandle.ALL);
					}
				}
			}
			mNaviAdapter.notifyDataSetChanged();
		}
		
	}
	


}
