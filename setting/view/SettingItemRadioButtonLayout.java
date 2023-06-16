package com.landsem.setting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * TODO
 *
 * @author NMOS facebook:LuckMerlin QQ:676046361
 * @version 0.0.0 Create Time:Jan 3, 2017 1:34:19 PM @ShenZhen
 *          com.landsem.setting.view.SettingItemRadioButtonLayout
 */
public class SettingItemRadioButtonLayout extends LinearLayout {

	public SettingItemRadioButtonLayout(Context context) {
		this(context, null);
	}

	public SettingItemRadioButtonLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingItemRadioButtonLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).setOnClickListener(clickListener);
		}
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			perferSelected(arg0);
		}
	};

	public boolean perferSelected(View v) {
		int count = null != v && indexOfChild(v) >= 0 ? getChildCount() : 0;
		if (count > 0) {
			View child;
			for (int i = 0; i < count; i++) {
				child = getChildAt(i);
				child.setSelected(child == v);
			}
			return true;
		}
		return false;
	}
}
