package com.landsem.setting.view;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

/**
 * TODO
 *
 * @author NMOS facebook:LuckMerlin QQ:676046361
 * @version 0.0.0 Create Time:Jul 4, 2016 4:33:14 PM @ShenZhen
 *
 */
public class TransparentDialog {
	private Dialog dialog;

	public TransparentDialog(Context context) {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setWindowBackground(null);
		setCanceledOnTouchOutside(false);
	}

	public void setWindowBackground(Drawable drawable) {
		Window window = dialog.getWindow();
		window.setBackgroundDrawable(null != drawable ? drawable
				: new ColorDrawable(Color.TRANSPARENT));
	}

	public void setLayout(int width, int height) {
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		if (params.width != width || params.height != height) {
			params.width = width <= 0 ? params.width : width;
			params.height = height <= 0 ? params.height : height;
			dialog.getWindow().setAttributes(params);
		}
	}

	public boolean setContentView(View view) {
		if (null != view) {
			dialog.setContentView(view, new ViewGroup.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			return true;
		}
		return false;
	}

	public LayoutInflater getLayoutInflater() {
		return dialog.getLayoutInflater();
	}

	public View findViewById(int id) {
		return dialog.findViewById(id);
	}

	public Window getWindow() {
		return dialog.getWindow();
	}

	public View setContentView(int layoutResID) {
		View view = dialog.getLayoutInflater().inflate(layoutResID, null);
		setContentView(view);
		return view;
	}

	public void setCancelable(boolean cancelable) {
		dialog.setCancelable(cancelable);
	}

	public void setCanceledOnTouchOutside(boolean cancelable) {
		dialog.setCanceledOnTouchOutside(cancelable);
	}

	public boolean show() {
		if (!dialog.isShowing()) {
			dialog.show();
			return true;
		}
		return false;
	}

	public boolean dismiss() {
		if (dialog.isShowing()) {
			dialog.dismiss();
			return true;
		}
		return false;
	}

	public Context getContext() {
		return dialog.getContext();
	}
}
