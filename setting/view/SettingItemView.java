package com.landsem.setting.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.landsem.setting.R;

/**
 * TODO
 *
 * @author NMOS facebook:LuckMerlin QQ:676046361
 * @version 0.0.0 Create Time:Jan 3, 2017 1:34:19 PM @ShenZhen
 *          com.landsem.setting.view.SettingItemView
 */
public class SettingItemView extends TextView {
	public final static int DIVIDER_BOTTOM = 0;
	public final static int DIVIDER_TOP = 1;
	public final static int DIVIDER_NONE = 2;
	private int divider;
	private int dividerColor = Color.WHITE;
	private boolean fixture;
	private Drawable normal, selected;

	public SettingItemView(Context context) {
		this(context, null);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SettingItemView, defStyle, 0);
		divider = a.getInt(R.styleable.SettingItemView_divider1, DIVIDER_BOTTOM);
		fixture = a.getBoolean(R.styleable.SettingItemView_fixture, false);
		dividerColor = a.getColor(R.styleable.SettingItemView_dividerColor,
				Color.WHITE);
		a.recycle();
		Resources resources = getResources();
		normal = resources.getDrawable(R.drawable.checkboc_enable);
		selected = resources.getDrawable(R.drawable.checkboc_pressed);
		initBounds(normal);
		initBounds(selected);
		setSelected(isSelected());// perfer default
	}

	private void initBounds(Drawable drawable) {
		if (null != drawable) {
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
		}
	}

	public void setFixture(boolean fixture) {
		if (this.fixture != fixture) {
			this.fixture = fixture;
			setSelected(isSelected());
		}
	}

	public boolean isFixture() {
		return fixture;
	}

	public int getDvider() {
		return divider;
	}

	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		if (selected) {
			setCompoundDrawables(null, null, this.selected, null);
		} else {
			setCompoundDrawables(null, null, fixture ? normal : null, null);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// draw line
		Paint paint = getPaint();
		int currColor = paint.getColor();
		paint.setColor(dividerColor);
		switch (divider) {
		case DIVIDER_TOP:
			canvas.drawLine(0, 0, getWidth(), 1, getPaint());
			break;
		case DIVIDER_BOTTOM:
			int height = getHeight();
			canvas.drawLine(0, height - 1, getWidth(), height, getPaint());
			break;
		case DIVIDER_NONE:// get through
		default:
			break;
		}
		paint.setColor(currColor);// reset color
	}
}
