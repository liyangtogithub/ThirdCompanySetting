package com.landsem.setting.carrier;

import com.landsem.setting.R;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogUploadView extends LinearLayout implements OnClickListener{

	private Context context;
	private TextView uploadLogView;
	
	public LogUploadView(Context context) {
		this(context, null);
	}

	public LogUploadView(Context context, AttributeSet attrs) {
		this(context, attrs, 1);
		
	}

	public LogUploadView(Context context, AttributeSet arg1, int arg2) {
		super(context, arg1, arg2);
		this.context = context;
		inflate(context, R.layout.logupload_layout, this);
		uploadLogView = (TextView) findViewById(R.id.upload_log_view);
		uploadLogView.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent("landsem.intent.action.LOGUPLOAD");
		context.sendBroadcastAsUser(intent, UserHandle.ALL);
	}

}
