package com.landsem.setting.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.landsem.setting.R;
import com.landsem.setting.entity.AppInfo;

public class AppListAdapter extends CustomBaseAdapter<AppInfo> implements OnClickListener{

	
	public AppListAdapter(Context context, List<AppInfo> entitys) {
		super(context, entitys);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null==convertView){
			convertView = mInflater.inflate(R.layout.applist_item_layout, null);
		}
		TextView appNameView = ViewHolder.get(convertView, R.id.app_name);
		ImageView appUninstall = ViewHolder.get(convertView, R.id.uninstall);
		if(assertPosition(position)){
			AppInfo aooInfo = mList.get(position);
			if(null!=aooInfo){
				appNameView.setText(aooInfo.getAppName());
				appUninstall.setEnabled(aooInfo.isUserApp());
				appUninstall.setTag(position);
				appUninstall.setOnClickListener(this);
			}

		}
		return convertView;
	}
	
	
	
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



	@Override
	public void onClick(View v) {
		Object o = v.getTag();
		if(o instanceof Integer){
			int position = (Integer) o;
			if(assertPosition(position)){
				AppInfo info = mList.get(position);
				if(null!=info){
					String uristr = "package:" + info.getPackageName();
					Uri uri = Uri.parse(uristr);
					Intent deleteIntent = new Intent();
					deleteIntent.setAction(Intent.ACTION_DELETE);
					deleteIntent.setData(uri);
					mContext.startActivity(deleteIntent);
				}
			}
		}
	}
	

}
