package com.landsem.setting.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 泛型的Adapter
 * @author luohong
 * @param <T>
 */
public abstract class CustomBaseAdapter<T> extends BaseAdapter{
	
	protected List<T> mList;
	protected Context mContext;
	protected LayoutInflater mInflater;

	public CustomBaseAdapter(Context context,List<T> entitys) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.mList = entitys;
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public T getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setList(List<T> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public List<T> getList() {
		return mList;
	}
	
	public void addItem(T item){
		if(null!=item){
			mList.add(item);
			notifyDataSetChanged();
		}
	}
	
	public void removePosition(int position){
		if(assertPosition(position)){
			mList.remove(position);
			notifyDataSetChanged();
		}
	}
	
	
	public boolean assertPosition(int position){
		return position < getCount();
	}


	public void setList(T[] list) {
		ArrayList<T> arrayList = new ArrayList<T>(list.length);
		for (T t : list) {
			arrayList.add(t);
		}
		setList(arrayList);
	}

	public void clear() {
		if (mList == null) return;
		mList.clear();
		notifyDataSetChanged();
	}
	
	public boolean isLocationValid(int location){
		return location>=0 && location<getCount();
	}
}
