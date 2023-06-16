package com.landsem.setting.brightness;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class BacklightCarrierView extends LinearLayout {
	
	public interface OnBackLightCarrierViewVisablityChangeListener {
		void OnBackLightCarrierViewVisablityChange(boolean visable);
	}

	private OnBackLightCarrierViewVisablityChangeListener visablityChangeListener;

	public void setOnBackLightCarrierViewVisablityChangeListener(
			OnBackLightCarrierViewVisablityChangeListener visablityChangeListener) {
		this.visablityChangeListener = visablityChangeListener;
	}

	public BacklightCarrierView(Context context) {
		this(context, null);
	}

	public BacklightCarrierView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BacklightCarrierView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (null != visablityChangeListener) {
			visablityChangeListener.OnBackLightCarrierViewVisablityChange(visibility == VISIBLE);
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (null != visablityChangeListener) {
			visablityChangeListener.OnBackLightCarrierViewVisablityChange(visibility == VISIBLE);
		}
	}
}
