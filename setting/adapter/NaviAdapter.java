package com.landsem.setting.adapter;

import java.util.List;

import com.landsem.setting.R;
import com.landsem.setting.entity.NaviInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NaviAdapter extends CustomBaseAdapter<NaviInfo> {

	public NaviAdapter(Context context, List<NaviInfo> entitys) {
		super(context, entitys);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.naviselect_item_layout, null);
		}
		TextView naviLable = (TextView) convertView.findViewById(R.id.navi_lable);
		initViewData(naviLable, position);
		return convertView;
	}
	
	private void initViewData(TextView naviLable, int location){
		if(isLocationValid(location)){
			NaviInfo naviInfo = mList.get(location);
			if(null!=naviInfo){
				naviLable.setText(naviInfo.appLable);
				naviLable.setEnabled(!naviInfo.select);
			}
		}
	}

}
