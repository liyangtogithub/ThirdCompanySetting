package com.landsem.setting.view;

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

import com.landsem.common.tools.LogManager;
import com.landsem.setting.R;

public class InputDialogPassword extends Dialog implements OnClickListener , TextWatcher{
	
	private OnClickListener mListener;
	private String inputString;
	private Button btnOK;
	private EditText editView;

	public InputDialogPassword(Context context, OnClickListener listener) {
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
		setContentView(R.layout.popup_dialog_factory_reset);
		btnOK = (Button) findViewById(R.id.btn_ok);
		btnOK.setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		editView = (EditText) findViewById(R.id.pwd);
		editView.addTextChangedListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		editView = null;
		btnOK = null;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btn_ok && mListener != null) {
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
		btnOK.setEnabled(s.length() >= 4);
	}

}
