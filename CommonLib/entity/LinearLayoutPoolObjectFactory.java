package com.lqpdc.commonlib.entity;

import android.content.Context;

import com.lqpdc.commonlib.adapter.PoolObjectFactory;
import com.lqpdc.commonlib.view.IcsLinearLayout;

public class LinearLayoutPoolObjectFactory implements
		PoolObjectFactory<IcsLinearLayout> {

	private final Context context;

	public LinearLayoutPoolObjectFactory(final Context context) {
		this.context = context;
	}

	@Override
	public IcsLinearLayout createObject() {
		return new IcsLinearLayout(context, null);
	}
}
