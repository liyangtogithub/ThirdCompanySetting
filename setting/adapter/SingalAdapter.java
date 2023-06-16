package com.landsem.setting.adapter;

import java.util.List;

import com.landsem.setting.R;
import com.landsem.setting.entity.SpinnerOption;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SingalAdapter extends CustomBaseAdapter<SpinnerOption> {

	public SingalAdapter(Context context, List<SpinnerOption> entitys) {
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
			SpinnerOption naviInfo = mList.get(location);
			if(null!=naviInfo){
				naviLable.setText(naviInfo.lable);
				naviLable.setEnabled(!naviInfo.select);
			}
		}
	}

}
