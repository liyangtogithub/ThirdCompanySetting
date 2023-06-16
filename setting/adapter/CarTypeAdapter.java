package com.landsem.setting.adapter;

import java.util.List;
import com.landsem.setting.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class CarTypeAdapter extends CustomBaseAdapter<String> {
	
	int currentPosition = 0;
	public static final int INVALID_CURRENT_POSITION  = -1;

	public CarTypeAdapter(Context context, List<String> entitys) {
		super(context, entitys);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.car_type_list_item, null);
		if ((position%2) == 0) {
			convertView.setBackgroundResource(R.drawable.car_type_list_uncheck0);
		} else {
			convertView.setBackgroundResource(R.drawable.car_type_list_uncheck1);
		}
		if (position == getCurrentPosition()) {
			convertView.setBackgroundResource(R.drawable.car_type_list_in_back);
		}
		TextView textViewContent = (TextView) convertView.findViewById(R.id.tv_car_content);
		ImageView  imageViewSelect = (ImageView) convertView.findViewById(R.id.iv_car_select);
		
		initViewData(imageViewSelect,textViewContent, position);
		return convertView;     
	}
	
	private void initViewData(ImageView  imageViewSelect, TextView textViewContent, int location){
		if(isLocationValid(location)){
			String content = mList.get(location);
			if(null!=content){
				textViewContent.setText(content);
			}
			if (location == getCurrentPosition()) {
				imageViewSelect.setBackgroundResource(R.drawable.car_type_list_in);
			}
		}
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	

}
