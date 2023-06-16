package com.landsem.setting.carrier;

import com.landsem.setting.R;
import com.landsem.setting.view.SettingDialog;
import com.landsem.setting.view.SettingItemView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class CarOtherSettingCarrier extends BaseCarrier {
	private SettingDialog dialog;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1499440818562923120L;

	public CarOtherSettingCarrier(Context context, LayoutInflater inflater,
			int resource) {
		super(context, inflater, resource);
		dialog = new SettingDialog(context);
		initViews(contentView);
		initListener();
		initViewsState();
	}

	@Override
	protected void initListener() {
	}

	@Override
	protected void initViews(View convertView) {
		ViewGroup viewGroup;
		if (null != convertView
				&& (convertView instanceof ViewGroup)
				&& ((viewGroup = (ViewGroup) convertView).getChildCount() > 0)
				&& ((convertView = viewGroup.getChildAt(0)) instanceof ViewGroup)) {
			viewGroup = (ViewGroup) convertView;
			int count = viewGroup.getChildCount();
			for (int i = 0; i < count; i++) {
				((ViewGroup) convertView).getChildAt(i)
						.setOnClickListener(this);
			}
		}
		dialog.findViewById(R.id.LayoutDialogCancelSIV)
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.airConditionarBindAuto:// test,get rhrough
		case R.id.indoorOutdoorGasBindAuto:
			if (arg0 instanceof SettingItemView) {
				((SettingItemView) arg0).setSelected(!arg0.isSelected());// test
			}
			break;
		case R.id.backCarCameraOrbit:
			dialog.showContentView(R.layout.layout_dialog_orbitcamera,
					R.string.BackCarCameraOrbit);
			break;
		case R.id.carTypeConfig:
			dialog.showContentView(R.layout.layout_dialog_carconfig,
					R.string.CarTypeConfig);
			break;
		case R.id.LayoutDialogCancelSIV:
			dialog.dismiss();
			break;
		}

	}

	@Override
	protected void initViewsState() {

	}

}
