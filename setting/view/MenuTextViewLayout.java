package com.landsem.setting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * TODO
 *
 * @author NMOS facebook:LuckMerlin QQ:676046361
 * @version 0.0.0 Create Time:Jan 6, 2017 10:38:46 AM @ShenZhen
 *          com.landsem.setting.view.MenuTextViewLayout
 */
public class MenuTextViewLayout extends LinearLayout {
	public MenuTextViewLayout(Context context) {
		this(context, null);
	}

	public MenuTextViewLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MenuTextViewLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint.setColor(Color.parseColor("#88ffffff"));
	}

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int lineMargin = 15, lineHeight = 1;

	@Override
	protected void dispatchDraw(Canvas canvas) {
		int count = getChildCount();
		View child;
		for (int i = 0; i < count; i++) {
			child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				drawChild(canvas, child, getDrawingTime());
					final int bottom = child.getBottom();
					// draw spliteline of childs.
					canvas.drawLine(lineMargin, bottom - lineHeight, getWidth()
							- lineMargin, bottom, paint);
				
			}
		}
	}
}
