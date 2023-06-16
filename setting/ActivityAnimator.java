package com.landsem.setting;

import android.app.Activity;

public final class ActivityAnimator {

	/**
	 * 缩放动画进入
	 */
	public static void unzoomAnimation(Activity a) {
		if (null != a)
			a.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}

	/**
	 * 缩放动画退出
	 */
	public static void unzoomBackAnimation(Activity a) {
		if (null != a)
			a.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}

	/**
	 * 水平平移效果-右进左退
	 */
	public static void slideLeftRightAnimation(Activity a) {
		if (null != a)
			a.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}

	/**
	 * 水平平移效果-左进右出
	 */
	public static void slideLeftRightBackAnimation(Activity a) {
		if (null != a)
			a.overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

}
