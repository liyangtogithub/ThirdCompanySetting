package com.landsem.setting.carrier;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.brightness.BacklightCarrierView;
import com.landsem.setting.brightness.StatusBrightnessHelper;
import com.landsem.setting.brightness.StatusBrightnessHelper.OnUpdateBrightnessSeekbarProgress;
import com.landsem.setting.utils.LightnessControl;
import com.landsem.setting.view.CustomSeekbar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class BackLightCarrier extends BaseCarrier implements OnDismissListener, OnUpdateBrightnessSeekbarProgress, Constant {
	
	private static final long serialVersionUID = 1L;
	private static final int MAXIMUM_BACKLIGHT = 255;
	private Context context;
	private CustomSeekbar daylightSeekBar;
	private int curProgress;
	private BacklightCarrierView backlightCarrierView;
	private Handler handler = new LightHandler();
	private static BackLightCarrier instance;
	private TextView modeTextView;
	private ImageView lightMinish;
	private ImageView lightIncrease;

	public static BackLightCarrier newInstance(LayoutInflater inflater, int resource){
		if(null==instance){
			synchronized (BackLightCarrier.class) {
				if(null==instance) instance = new BackLightCarrier(inflater, resource);
			}
		}
		return instance;
	}
	
	public static BackLightCarrier reNewInstance(LayoutInflater inflater, int resource){
		if(null!=instance) instance = null;
		return newInstance(inflater, resource);
	}
	
	
	private BackLightCarrier(LayoutInflater inflater, int resource) {
		super(inflater, resource);
		context = SettingApp.getContext();
		initViews(contentView);
		initListener();
		LogManager.d("BackLightCarrier", "BackLightCarrier   created");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.light_minish:
			daylightSeekBar.trimmingLight(false);
			break;
		case R.id.light_increase:
			daylightSeekBar.trimmingLight(true);
			break;
		default:
			break;
		}
	}
	
	public View getContentView(){
		if(null!=contentView){
			ViewParent viewParent = contentView.getParent();
			if(null!=viewParent){
				ViewGroup parent = (ViewGroup) viewParent;
				parent.removeView(contentView);
			}
		}
		return contentView;
	}

	@Override
	protected void initViews(View convertView) {// nightlight_seekbar
		modeTextView = (TextView) convertView.findViewById(R.id.daylight_adjust);
		daylightSeekBar = (CustomSeekbar) convertView.findViewById(R.id.daylight_seekbar);
		lightMinish = (ImageView) convertView.findViewById(R.id.light_minish);
		lightIncrease = (ImageView) convertView.findViewById(R.id.light_increase);
		backlightCarrierView = ((BacklightCarrierView) convertView.findViewById(R.id.backlightCarrierView));
		initViewsState();
	}

	@Override
	protected void initListener() {
		StatusBrightnessHelper brightnessHelper = SettingApp.getInstance().getStatusBrightnessHelper();
		brightnessHelper.setOnUpdateBrightnessSeekbarProgress(this);
		if (null != backlightCarrierView) {
			backlightCarrierView.setOnBackLightCarrierViewVisablityChangeListener(brightnessHelper);
		}
		lightMinish.setOnClickListener(this);
		lightIncrease.setOnClickListener(this);
	}

	@Override
	protected void initViewsState() {
		daylightSeekBar.setModeTextView(modeTextView);
		daylightSeekBar.setMax(MAX_LIGHT_VALUE);
		daylightSeekBar.initLightMode(true);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		
		LightnessControl.setLightness(context, curProgress, false);
	}

	@Override
	public void updateBrightnessSeekbarProgress(int type, int progress) {
		handler.removeMessages(type);
		Message msg = handler.obtainMessage(type);
		msg.arg1 = progress;
		handler.sendMessage(msg);
	}
	
	private class LightHandler extends Handler{
		@Override
		public synchronized void handleMessage(Message msg) {
			
			daylightSeekBar.setProgress(msg.arg1);
		}
	}

	public void onModeChanged() {
		
		if(null!=daylightSeekBar) daylightSeekBar.onModeChanged();
	}
	
	public void onConfigurationChanged(){
//		if(null!=modeTextView) modeTextView.setText(R.string.);
		if(null!=daylightSeekBar) daylightSeekBar.initLightMode(false);
	}

}
