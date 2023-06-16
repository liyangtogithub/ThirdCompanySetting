package com.landsem.setting.view;

import com.landsem.setting.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhoneSelectView extends LinearLayout {

	private TextView phoneSelect;
	
	public PhoneSelectView(Context context) {
		this(context, null);
	}
	
	public PhoneSelectView(Context context, AttributeSet attrs) {
		this(context, attrs, 1);
	}
	
	public PhoneSelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.phone_select_layout, this);
		initViews();
	}
	
	private void initViews(){
		phoneSelect = (TextView) findViewById(R.id.select_textview);
	}

	public void updateViewContent(String content){
		if(null!=phoneSelect) phoneSelect.setText(content);
	}
	

}
