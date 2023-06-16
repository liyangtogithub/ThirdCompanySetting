package com.landsem.setting.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * TODO
 *
 * @author NMOS facebook:LuckMerlin QQ:676046361
 * @version 0.0.0 Create Time:Jan 3, 2017 1:34:19 PM @ShenZhen
 *          com.landsem.settings.automobile.SettingItemCheckboxView
 */
public class SettingItemCheckboxView extends SettingItemView {

	public SettingItemCheckboxView(Context context) {
		this(context, null);
	}

	public SettingItemCheckboxView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingItemCheckboxView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setFixture(true);
	}

}
