package com.landsem.setting.carrier;

import android.view.LayoutInflater;
import android.view.View;

public class StandbyCarrier extends BaseCarrier {

	private static final long serialVersionUID = 7598712493639967635L;
	private static final String TAG = StandbyCarrier.class.getSimpleName();

	public StandbyCarrier(LayoutInflater inflater, int resource) {
		super(inflater, resource);
		initViews(contentView);
		initListener();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void initViews(View convertView) {
		
	}

	@Override
	protected void initListener() {
	}

	@Override
	protected void initViewsState() {

	}

}
