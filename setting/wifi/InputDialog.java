package com.landsem.setting.wifi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.landsem.setting.R;

public class InputDialog extends Dialog implements OnClickListener, TextWatcher {
	
	private OnClickListener mListener;
	private String inputString;
	private Button btnConn;
	private EditText editView;

	public InputDialog(Context context, OnClickListener listener) {
		super(context);
		this.mListener = listener;
	}

	public String getInputString() {
		return inputString;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup_dialog_wifi_inputpassword);
		btnConn = (Button) findViewById(R.id.btn_conn);
		btnConn.setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		editView = (EditText) findViewById(R.id.pwd);
		editView.addTextChangedListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		editView = null;
		btnConn = null;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btn_conn && mListener != null) {
			inputString = editView.getText().toString();
			mListener.onClick(this, 0);
		}
		dismiss();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		btnConn.setEnabled(s.length() >= 8);
	}
}
