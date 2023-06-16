package com.landsem.setting.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.landsem.setting.R;

/**
 * TODO
 *
 * @author NMOS facebook:LuckMerlin QQ:676046361
 * @version 0.0.0 Create Time:Jan 3, 2017 5:30:21 PM @ShenZhen
 *
 */
public class SettingDialog extends TransparentDialog {
	private TextView titleTV;
	private FrameLayout content;
	private final FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	public interface Callback {
		SettingDialog getDialog();
	}

	// private final SparseArray<View> viewCaches = new SparseArray<>();

	@SuppressLint("InflateParams")
	public SettingDialog(Context context) {
		super(context);
		setCancelable(false);
		setWindowBackground(context.getResources().getDrawable(
				R.drawable.dialog_background2));
		setContentView(R.layout.layout_dialog);
		content = (FrameLayout) findViewById(R.id.LayoutDialogContentFrameLayout);
		titleTV = (TextView) findViewById(R.id.LayoutDialogTitleSIV);
		super.setLayout(500, 350);
	}

	public boolean showContentView(int layoutResID, int titleResId) {
		setContentView(layoutResID);
		setTitle(titleResId);
		return show();
	}

	public void setContentView(int layoutResID, int titleResId) {
		setContentView(layoutResID);
		setTitle(titleResId);
	}

	// @Override
	// public View setContentView(int layoutResID) {
	// View view = viewCaches.get(layoutResID);
	// if (null == view && null != (view = super.setContentView(layoutResID))) {
	// viewCaches.put(layoutResID, view);
	// return view;
	// } else {
	// setContentView(view);
	// }
	// return view;
	// }

	@Override
	public boolean setContentView(View view) {
		if (null != view) {
			if (null != content) {
				content.removeAllViews();
				content.addView(view, parms);
			} else {
				super.setContentView(view);
			}
			return true;
		}
		return false;
	}

	public boolean setTitle(String title) {
		if (null != titleTV && null != title) {
			titleTV.setText(title);
			setTitleVisiblity(View.VISIBLE);
			return true;
		}
		return false;
	}

	public boolean setTitle(int resId) {
		try {
			setTitle((String) getContext().getResources().getText(resId));
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public void setTitleVisiblity(int visibility) {
		if (null != titleTV) {
			titleTV.setVisibility(visibility);
		}
	}

}
