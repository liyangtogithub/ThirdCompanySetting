package com.landsem.setting.entity;

import android.content.Context;

public class TransitBean {
	
	public CarrierType type;
	public Context context;
	public int resourceId;
	
	
	public TransitBean(Context context, CarrierType type, int resourceId) {
		super();
		this.context = context;
		this.type = type;
		this.resourceId = resourceId;
	}

}
