package com.landsem.setting.wifi;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiConfiguration;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.R;

public final class WifiRow extends LinearLayout implements AccessPoint.OnStateChangedListener{
	
	private AccessPoint accessPoint;

	public WifiRow(Context context) {
		super(context);
		setGravity(Gravity.CENTER_VERTICAL);
		inflate(context, R.layout.wifi_item, this);
	}

	public void refresh(AccessPoint accessPoint, String fromMathed) {
		this.accessPoint = accessPoint;
		if (null!=accessPoint) {
			TextView ssidTextView = (TextView) findViewById(R.id.ssid);
			ssidTextView.setText(accessPoint.ssid);
			ImageView statuView = (ImageView) findViewById(R.id.statu);
			AnimationDrawable ad;
			WifiConfiguration config = accessPoint.getConfig();
			if(null!=config && config.status==WifiConfiguration.Status.DISABLED && config.disableReason==WifiConfiguration.DISABLED_AUTH_FAILURE) {
				if (isAnimationDrawable(statuView)) {
					ad = (AnimationDrawable) statuView.getBackground();
					ad.stop();
				}
				statuView.setBackgroundResource(R.drawable.cha);
				statuView.setVisibility(VISIBLE);
			}else {
				DetailedState state = accessPoint.getState();
//				LogManager.d("luohong", "refresh      &&&&&&      state: "+state+", position: "+accessPoint.position+", fromMathed: "+fromMathed);
				if (null!=state) {
					switch (state) {
					case CONNECTED:
						if (isAnimationDrawable(statuView)) {
							ad = (AnimationDrawable) statuView.getBackground();
							ad.stop();
						}
						statuView.setBackgroundResource(R.drawable.gou);
						statuView.setVisibility(VISIBLE);
						break;
					default:
						if (isAnimationDrawable(statuView)) break;
						statuView.setBackgroundResource(R.drawable.ani_loading);
						ad = (AnimationDrawable) statuView.getBackground();
						ad.start();
						statuView.setVisibility(VISIBLE);
						break;
					}
				} else {
					if (isAnimationDrawable(statuView)) {
						ad = (AnimationDrawable) statuView.getBackground();
						ad.stop();
					}
					statuView.setBackground(null);
					statuView.setVisibility(GONE);
				}
			}
			ImageView lockView = (ImageView) findViewById(R.id.lock);
			lockView.setVisibility(accessPoint.security==AccessPoint.SECURITY_NONE ? GONE : GONE);
			ImageView signalView = (ImageView) findViewById(R.id.level);
			if (accessPoint.security!=AccessPoint.SECURITY_NONE) {
				signalView.setImageResource(R.drawable.wifi_signal_open_lock);
			}
			int sinalLevel = accessPoint.getLevel();
			if (sinalLevel == -1) {
				signalView.setVisibility(INVISIBLE);
				ssidTextView.setText(accessPoint.ssid + "("+ getContext().getString(R.string.wifi_not_in_range)+ ")");
				lockView.setVisibility(INVISIBLE);
			} else {
				signalView.setVisibility(VISIBLE);
				signalView.setImageLevel(sinalLevel);
			}
		}
	}
	
	private synchronized boolean isAnimationDrawable(ImageView statuView){
		
		return null!=statuView?(statuView.getBackground() instanceof AnimationDrawable):false;
	}
	
	public AccessPoint getAccessPoint() {
		return accessPoint;
	}

	public void setAccessPoint(AccessPoint accessPoint) {
		this.accessPoint = accessPoint;
	}
	
	public void setStateChangedListener(){
		if(null!=accessPoint) accessPoint.setOnStateChangedListener(this);
	}

	@Override
	public void onStateChanged(AccessPoint accessPoint, String fromMathed) {
//		LogManager.d("luohong", "onStateChanged      &&&&&&      fromMathed: "+fromMathed);
		refresh(accessPoint, fromMathed);
	}

	
	
	
}
