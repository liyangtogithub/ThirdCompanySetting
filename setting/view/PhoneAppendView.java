package com.landsem.setting.view;

import com.landsem.setting.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhoneAppendView extends LinearLayout {

	private TextView appendView; 
	
	public PhoneAppendView(Context context) {
		this(context, null);
	}
	
	public PhoneAppendView(Context context, AttributeSet attrs) {
		this(context, attrs, 1);
	}
	
	public PhoneAppendView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.phone_append_layout, this);
		initViews();
	}

	private void initViews(){
		appendView = (TextView) findViewById(R.id.append_textview);
	}

	public void updateViewContent(String content){
		if(null!=appendView) appendView.setText(content);
	}
}
