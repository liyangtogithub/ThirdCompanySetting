package com.landsem.setting.fragment;

import com.landsem.setting.Constant;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment implements OnClickListener, Constant{


	private static final long serialVersionUID = 1L;
	protected LinearLayout contentLayout;
	protected LayoutInflater inflater;
	protected TextView lastTextView;
	private View convertView;
	private int resource;
	
	protected void injectResource(int resource) {
		this.resource = resource;
	}
	
	public abstract void initViews(View converView);
	
	public abstract void initListener();
	
	public abstract void doOnResume();
	
	public abstract void doOnPause();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		inflater = LayoutInflater.from(getActivity());
		inflater = getLayoutInflater(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(null==convertView){
			convertView = inflater.inflate(resource, null);
			initViews(convertView);
			initListener();
		}
		ViewParent viewParent = convertView.getParent();
		if(null!=viewParent){
			ViewGroup parent = (ViewGroup) viewParent;
			parent.removeView(convertView);
		}
		return convertView;
	}
	

	@Override
	public void onResume() {
		super.onResume();
		doOnResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		doOnPause();
	}
	
	protected synchronized boolean doChangeEnabled(TextView textView) {
		boolean result = true;
		if(null!=lastTextView){
			if(lastTextView==textView) {
				result = false;
				return result;
			}
			lastTextView.setEnabled(true);
		}
		lastTextView = textView;
		lastTextView.setEnabled(false);
		if(null!=contentLayout) contentLayout.removeAllViews();
		return result;
	}
	
}
