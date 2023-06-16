package com.lqpdc.commonlib.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListDataAdapter<T> extends BaseAdapter {
	private LayoutInflater mInflater;
	private int resId;

	public ListDataAdapter(Context context, int itemLayoutResId) {
		mInflater = LayoutInflater.from(context);
		resId = itemLayoutResId;
	}

	protected abstract List<T> getList();

	protected abstract void applyData(int position, View itemView);

	protected void onCreateView(View createdView) {
		// do nothing
	}

	@Override
	public int getCount() {
		List<T> list = getList();
		return list == null ? 0 : list.size();
	}

	@Override
	public T getItem(int position) {
		return getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(resId, parent, false);
			onCreateView(convertView);
		}
		applyData(position, convertView);
		return convertView;
	}

}
