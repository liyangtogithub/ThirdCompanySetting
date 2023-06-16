package com.landsem.setting.carrier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.landsem.setting.Constant;

public abstract class BaseCarrier implements OnClickListener, Constant {

	private static final long serialVersionUID = 85453297980898804L;
	protected LayoutInflater inflater;
	public View contentView;
	protected int resource;
	protected Context context;

	public BaseCarrier(LayoutInflater inflater, int resource) {
		super();
		this.inflater = inflater;
		this.resource = resource;
		contentView = inflater.inflate(resource, null);
	}

	public BaseCarrier(Context context, LayoutInflater inflater, int resource) {
		this.context = context;
		this.inflater = inflater;
		this.resource = resource;
		contentView = inflater.inflate(resource, null);
	}

	protected abstract void initViews(View convertView);

	protected abstract void initListener();

	protected abstract void initViewsState();

	protected Context getContext() {
		return null==context ? (null==inflater ? null : inflater.getContext()) : context;
	}
}
