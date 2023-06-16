package com.landsem.setting.carrier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landsem.setting.R;
import com.landsem.setting.view.SettingDialog;
import com.landsem.setting.view.SettingItemCheckboxView;

/**
 * TODO
 *
 * @author NMOS facebook:LuckMerlin QQ:676046361
 * @version 0.0.0 Create Time:Jan 6, 2017 11:35:18 AM @ShenZhen
 *
 */
public class RadarCarrier extends BaseCarrier {
	private SettingDialog dialog;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7119346753746835308L;

	public RadarCarrier(Context context, LayoutInflater inflater, int resource) {
		super(context, inflater, resource);
		dialog = new SettingDialog(context);
		initViews(contentView);
		initListener();
		initViewsState();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.Radar_RadarVolumeSIV:
			dialog.showContentView(R.layout.layout_radar_dialog_radarvolume,
					R.string.RadarVolume);
			break;
		case R.id.Radar_RadarDistanceSIV:
			dialog.showContentView(R.layout.layout_radar_dialog_radardistance,
					R.string.RadarDistance);
			break;
		case R.id.LayoutDialogCancelSIV:
			dialog.dismiss();
			break;
		default:
			if (arg0 instanceof SettingItemCheckboxView) { // test
				((SettingItemCheckboxView) arg0)
						.setSelected(!arg0.isSelected());
			}
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
