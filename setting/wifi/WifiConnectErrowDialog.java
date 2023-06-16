package com.landsem.setting.wifi;

import com.landsem.setting.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class WifiConnectErrowDialog extends Dialog implements OnClickListener {

	public WifiConnectErrowDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup_dialog_wifi_errow);
		findViewById(R.id.confirm).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		dismiss();
	}

}
