package com.lqpdc.commonlib.adapter;

import android.view.View;

public interface PoolObjectFactory<T extends View> {
	
	public T createObject();
	
}
