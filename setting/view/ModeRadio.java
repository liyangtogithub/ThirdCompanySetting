package com.landsem.setting.view;

import com.landsem.setting.Constant;
import com.landsem.setting.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class ModeRadio extends RadioButton {

	private int duration;
	
	public ModeRadio(Context context) {
		this(context, null);
	}

	public ModeRadio(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ModeRadio(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray aypedArray = context.obtainStyledAttributes(attrs, R.styleable.landsem, defStyle, 0);
		duration = aypedArray.getInteger(R.styleable.landsem_duration, Constant.INVALID_VALUE);
		setText(duration/Constant.Duration.MINUTE+" min");
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	
	
}
