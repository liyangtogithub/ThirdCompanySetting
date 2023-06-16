package com.landsem.setting.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.landsem.setting.R;
import com.landsem.setting.entity.AppInfo;

public class AppListBaseAdapter extends CustomBaseAdapter<AppInfo> {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final String COLON = ": ";
	private String packageName;
	private String versions;
	private String appSize;
	private String installTime;
	
	public AppListBaseAdapter(Context context, List<AppInfo> entitys) {
		super(context, entitys);
		Resources mResources = context.getResources();
		packageName = mResources.getString(R.string.package_name);
		versions = mResources.getString(R.string.versions);
		appSize = mResources.getString(R.string.app_size);
		installTime = mResources.getString(R.string.install_time);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null==convertView) convertView = mInflater.inflate(R.layout.activity_applist_listview_item, parent, false);
		TextView appNameView = ViewHolder.get(convertView, R.id.app_name);
		TextView versionNameView = ViewHolder.get(convertView, R.id.version_name);
		TextView apkLengthView = ViewHolder.get(convertView, R.id.apk_length);
		TextView installTimeView = ViewHolder.get(convertView, R.id.install_time);
		ImageView appIconView = ViewHolder.get(convertView, R.id.app_icon);
		AppInfo appInfo = mList.get(position);
		appIconView.setBackground(appInfo.getIcon());
		appNameView.setSelected(true);// 设置了此属性才能有跑马灯效果
		versionNameView.setSelected(true);// 设置了此属性才能有跑马灯效果
		apkLengthView.setSelected(true);// 设置了此属性才能有跑马灯效果
		installTimeView.setSelected(true);// 设置了此属性才能有跑马灯效果
		appNameView.setText(packageName + COLON + appInfo.getAppName());
		versionNameView.setText(versions + COLON + appInfo.getVersionName());
		apkLengthView.setText(appSize + COLON + appInfo.getLength());
		installTimeView.setText(installTime + COLON + dateFormat.format(appInfo.getFirstInstallTime()));
		return convertView;
	}

	/**
	 * ViewHolder模式超简洁写法，很cool!
	 */
	private static class ViewHolder {
		@SuppressWarnings("unchecked")
		public static <T extends View> T get(View v, int id) {
			SparseArray<View> viewHolder = (SparseArray<View>) v.getTag();
			if (null==viewHolder) viewHolder = new SparseArray<View>();
			View childView = viewHolder.get(id);
			if (null==childView) {
				childView = v.findViewById(id);
				viewHolder.put(id, childView);
			}
			return (T) childView;
		}
	}
}
