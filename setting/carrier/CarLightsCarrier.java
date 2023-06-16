package com.landsem.setting.carrier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landsem.setting.R;
import com.landsem.setting.view.SettingDialog;
import com.landsem.setting.view.SettingItemView;

/**
 * TODO
 *
 * @author NMOS facebook:LuckMerlin QQ:676046361
 * @version 0.0.0 Create Time:Jan 6, 2017 11:35:18 AM @ShenZhen
 *
 */
public class CarLightsCarrier extends BaseCarrier {
	private SettingDialog dialog;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7119346753746835308L;

	public CarLightsCarrier(Context context, LayoutInflater inflater,
			int resource) {
		super(context, inflater, resource);
		dialog = new SettingDialog(context);
		initViews(contentView);
		initListener();
		initViewsState();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.Lights_DayLightCarLightSICBV:
			if (arg0 instanceof SettingItemView) { // test
				((SettingItemView) arg0).setSelected(!arg0.isSelected());
			}
			break;
		case R.id.Lights_DayLightCarLightSensitivitySIV:
			dialog.showContentView(
					R.layout.layout_light_dialog_autoheadlight_sensitivity,
					R.string.DaynightCarLightSensitivity);
			break;
		case R.id.Lights_HeadLightAutoShutdownTimeSIV:
			dialog.showContentView(
					R.layout.layout_light_dialog_autoheadlight_shutdown_time,
					R.string.HeadLightAutoShutdownTime);
			break;
		case R.id.Lights_CarInnerLightAutoShutdownTimeSIV:
			dialog.showContentView(
					R.layout.layout_light_dialog_innercarautolight_shutdown_time,
					R.string.CarInnerLightAutoShutdownTime);
			break;
		case R.id.LayoutDialogCancelSIV:
			dialog.dismiss();
			break;
		default:
			break;
		}
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
	protected void initListener() {

	}

	@Override
	protected void initViewsState() {

	}

}
