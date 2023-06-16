package com.landsem.setting.carrier;

import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.view.NaviSuspendSwitch;
import com.landsem.setting.view.RearViewSwitch;
import com.landsem.setting.view.ReverseRadarSwicth;
import com.landsem.setting.view.ReverseTrackSwicth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public final class ReverseCarrier extends BaseCarrier{
	
	private static final long serialVersionUID = 317359879528648800L;
	private View naviSuspend;
//	private KeyToneSwitch mKeyToneSwitch;
	private RearViewSwitch mRearViewSwitch;
	private ReverseTrackSwicth mReverseTrackSwicth;
	private ReverseRadarSwicth mReverseRadarSwicth;
	private NaviSuspendSwitch mSuspensionframeSwitch;


	public ReverseCarrier(LayoutInflater inflater, int resource) {
		this(SettingApp.getContext(), inflater, resource);
	}
	
	public ReverseCarrier(Context context, LayoutInflater inflater, int resource) {
		super(context, inflater, resource);
		initViews(contentView);
		initViewsState();
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected void initViews(View convertView) {
		naviSuspend = convertView.findViewById(R.id.navi_suspend);
//		mKeyToneSwitch = (KeyToneSwitch) convertView.findViewById(R.id.keytone_switch);
		mRearViewSwitch = (RearViewSwitch) convertView.findViewById(R.id.rearview_switch);
		mReverseTrackSwicth = (ReverseTrackSwicth) convertView.findViewById(R.id.reverse_track_switch);
		mReverseRadarSwicth = (ReverseRadarSwicth) convertView.findViewById(R.id.reverse_radar_switch);
		mSuspensionframeSwitch = (NaviSuspendSwitch) convertView.findViewById(R.id.navi_suspend_switch);
	}

	@Override
	protected void initListener() {
		
	}

	@Override
	protected void initViewsState() {
//		if(SettingApp.clientId==ClientId.ROAD_ROVER)
//		if(null!=naviSuspend) naviSuspend.setVisibility(View.VISIBLE);
//		if(null!=mKeyToneSwitch) mKeyToneSwitch.refreshViewsState();
//		if(null!=mRearViewSwitch) mRearViewSwitch.refreshViewsState();
		if(null!=mRearViewSwitch) mRearViewSwitch.initSwitchState();
		if(null!=mReverseTrackSwicth) mReverseTrackSwicth.refreshViewsState();
		if(null!=mReverseRadarSwicth) mReverseRadarSwicth.refreshViewsState();
		if(null!=mSuspensionframeSwitch) mSuspensionframeSwitch.refreshViewsState();
	}

}
