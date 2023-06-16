package com.landsem.setting.brightness;

import com.android.internal.statusbar.IBrightnessCallbacks;
import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;
import com.landsem.setting.brightness.BacklightCarrierView.OnBackLightCarrierViewVisablityChangeListener;
import com.landsem.setting.utils.LightnessControl;
import com.ls.mcu.McuEvent;
import com.ls.mcu.McuEventListener;
import com.ls.mcu.McuManager;

import android.annotation.SuppressLint;
import android.app.StatusBarManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

public class StatusBrightnessHelper implements OnBackLightCarrierViewVisablityChangeListener, McuEventListener, Constant {

	private static final String TAG = StatusBrightnessHelper.class.getSimpleName();
	private static final int SCREEN_LOCKED_LIGHT = 0x03;
	private static final int SCREEN_UNLOCK_LIGHT = 0x04;
	private static final int START_REVERSE_LIGHT = 0x05;
	private static final int START_REVERSE = 0x01;
	private static final int STOP_REVERSE = 0x02;
	private boolean ownOBD;
	private boolean reverseState;
	private boolean screenLocked;
	private StatusBarManager statusbarManager;
	private McuManager mcuManager;
	private OnUpdateBrightnessSeekbarProgress updateBrightnessSeekbarProgress;
	private Context context;
	private int lastProgress = -1;
	private Handler lightHandler = new LightHandler();
	private static StatusBrightnessHelper instanceBrightnessHelper;

	public interface OnUpdateBrightnessSeekbarProgress {
		void updateBrightnessSeekbarProgress(int type, int progress);
	}

	public void setOnUpdateBrightnessSeekbarProgress(OnUpdateBrightnessSeekbarProgress updateBrightnessSeekbarProgress) {
		this.updateBrightnessSeekbarProgress = updateBrightnessSeekbarProgress;
	}

	public StatusBrightnessHelper(Context context) {
		this.context = context;
		registSyetemUICallback();
		mcuManager = new McuManager(context.getMainLooper());
		mcuManager.registerListener(this);
	}
	
	public static StatusBrightnessHelper newInstance(Context context){
		LogManager.d(TAG, "StatusBrightnessHelper  newInstance " + instanceBrightnessHelper);
		if(null==instanceBrightnessHelper){
			synchronized (StatusBrightnessHelper.class) {
				if(null==instanceBrightnessHelper) instanceBrightnessHelper = new StatusBrightnessHelper(context);
			}
		}
		return instanceBrightnessHelper;
	}

	public  void registSyetemUICallback() {
		statusbarManager = (StatusBarManager) context.getSystemService(Context.STATUS_BAR_SERVICE);
		statusbarManager.setBrightnessCallbacks(mBrightnessListener);
	}
	

	private IBrightnessCallbacks mBrightnessListener = new IBrightnessCallbacks.Stub() {

		@Override
		public void brightnessProgressChange(int type, int progress) throws RemoteException {
			lastProgress = progress;
			Message msg = lightHandler.obtainMessage(type);
			msg.arg1 = progress;
			lightHandler.sendMessage(msg);
		}

		@Override
		public void brightnessStopTracking(int type) throws RemoteException {
			if (-1 != lastProgress) {
				SettingApp.getInstance().saveLightValue(lastProgress*ZOON_DIGIT);
			}
		}

		@Override
		public void onExpandSettingViewVisibleChange(boolean visable) throws RemoteException {
			LogManager.d(TAG, "Received  ExpandSettingViewVisibleChange. isVisable=" + visable);
			if(!visable) checkOutLightMode();
		}

//		@Override
//		public void notifyStandbyScreeenVisiable(boolean isScreenLocked) throws RemoteException {
//			screenLocked = isScreenLocked;
//			adjustLight(reverseState, screenLocked);
//			LogManager.d(TAG, "notifyStandbyScreeenVisiable      &&&&&&      isScreenLocked : "+isScreenLocked);
//		}
	};

	public int checkOutLightMode(){
//		LightnessControl.initLightMode();
//		if(ownOBD) BACKLIGHT_MODE = McuManager.getheadlightstate();
//		else BACKLIGHT_MODE = SettingApp.getInstance().updateHeadLampState();
//		LogManager.d(TAG, "checkOutHeadLamp      &&&&&&      ownOBD : "+ownOBD+", headLampState : "+BACKLIGHT_MODE);
		return LightnessControl.LIGHT_MODE;
	}
	
	public void changeLightMode(){
		checkOutLightMode();
		adjustLight(reverseState, screenLocked);
	}

	@Override
	public void OnBackLightCarrierViewVisablityChange(boolean visable) {
		if(!visable) adjustLight(reverseState, screenLocked);
	}
	
	private void recoverScreenUnlock(){
		int lightMode = checkOutLightMode();
		LightnessControl.setLightness(SettingApp.getContext(), lightMode, true);
		LogManager.d(TAG, "recoverScreenUnlock      &&&&&&      lightMode : " + lightMode);
	}
	
	public void doReverse(int state){
		switch (state) {
		case START_REVERSE:reverseState = true;break;
		case STOP_REVERSE:reverseState = false;break;
		default:break;
		}
		adjustLight(reverseState, screenLocked);
	}
	
	private void adjustLight(boolean reverseState, boolean screenLocked){
		if(reverseState){
			lightHandler.sendEmptyMessage(START_REVERSE_LIGHT);
		}else{
			recoverLight(screenLocked);
		}
	}
	
	private void recoverLight(boolean screenState){
		
		if(screenState){
			lightHandler.sendEmptyMessage(SCREEN_LOCKED_LIGHT);
		}else{
			lightHandler.sendEmptyMessage(SCREEN_UNLOCK_LIGHT);
		}
	}
	
	private void changeProgressLight(int type, int progress){
		if (null!=updateBrightnessSeekbarProgress) {
			updateBrightnessSeekbarProgress.updateBrightnessSeekbarProgress(type, progress);
		}
//		LightnessControl.setLightness(context, progress, false);
		LightnessControl.outerChangeLight(context, progress, false);
	}
	
	
	@SuppressLint("HandlerLeak")
	private final class LightHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case StatusBarManager.BRIGHTNESS_TYPE_DAYTIME:
				LogManager.d(TAG, "received  brightness . Type : DAYTIME , progress : " + msg.arg1);
				changeProgressLight(msg.what, msg.arg1);
				break;
			case StatusBarManager.BRIGHTNESS_TYPE_NIGHT:
				LogManager.d(TAG, "received  brightness . Type : NIGHT , progress : " + msg.arg1);
				changeProgressLight(msg.what, msg.arg1);
				break;
			case SCREEN_UNLOCK_LIGHT:
				recoverScreenUnlock();
				break;
			case SCREEN_LOCKED_LIGHT:
				LightnessControl.setLightness(context, 3, false);
				break;
			case START_REVERSE_LIGHT:
				LightnessControl.setLightness(context, 200, false);
				break;
			}
		}
	}

	@Override
	public void onEventChanged(McuEvent event) {
		if(null!=event){
			short[] values = event.values;
			if(null!=values && values.length==2){
				LogManager.d(TAG, "values[0] :  " + values[0] + "    values[1] :  " + values[1]);
				switch (values[0]) {
				case McuManager.MCU_MESSAGE_TYPE_REVERSE_CAMERA:
					doReverse(values[1]);
					break;
				case McuManager.MCU_MESSAGE_TYPE_HEADLIGHT_STATE:
					if(!ownOBD) ownOBD = true;
					changeLightMode();
					break;
				}
			}
		}
	}
	
	
}
