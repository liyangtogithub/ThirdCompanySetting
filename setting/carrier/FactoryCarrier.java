package com.landsem.setting.carrier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.view.CustomToast;
import com.landsem.setting.view.InputDialogPassword;

public class FactoryCarrier extends BaseCarrier implements DialogInterface.OnClickListener{
	
	private static final long serialVersionUID = 1L;
	private static final String FACTORY_PASS_WORD = "6868";
//	private static final String TAG = FactoryCarrier.class.getSimpleName();
	private TextView resetFactary;
	
	public FactoryCarrier(Context mContext, LayoutInflater inflater, int resource) {
		super(mContext,inflater, resource);
		initViews(contentView);
		initListener();
	}

	@Override
	public void onClick(View v) {
		InputDialogPassword mDialog = new InputDialogPassword(context, this);
		mDialog.show();
		
	}

	@Override
	protected void initViews(View convertView) {
		resetFactary = (TextView) convertView.findViewById(R.id.reset_factary);

	}

	@Override
	protected void initListener() {
		resetFactary.setOnClickListener(this);

	}

	@Override
	protected void initViewsState() {
	

	}

	@Override
	public void onClick(DialogInterface dialogInterface, int which) {
		if (dialogInterface instanceof InputDialogPassword) {
			if (FACTORY_PASS_WORD.equals( ((InputDialogPassword) dialogInterface).getInputString() ) ) {
				Intent intent = new Intent(Action.MASTER_CLEAR);
				SettingApp.getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
			} else {
				CustomToast.makeText(context, R.string.logo_pwd_reload).show();
			}
		}
	}

}
