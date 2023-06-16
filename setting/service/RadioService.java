package com.landsem.setting.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;
import com.ls.fmradio.IRadioCallback;
import com.ls.fmradio.IRadioService;
import com.ls.mcu.FmManager;
import com.ls.mcu.McuManager;


public class RadioService extends Service implements Constant {

	private static final String TAG = "RadioService";
	private static FmManager mFmManager = null;
	private static int FM_STATUS_PLAYING = 1;
	private static int FM_STATUS_STOPING = 2;
	private int playStatus = FM_STATUS_STOPING;

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;

	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogManager.d(TAG, "RadioService      &&&&&& onCreate()");
		mFmManager = (FmManager) getSystemService(Context.FM_SERVICE);
	}

	
	private IRadioService.Stub mBinder = new IRadioService.Stub() {
		
		@Override
		public void stopPlay() throws RemoteException {
			playStatus = FM_STATUS_STOPING;
			McuManager.sendMcuCommand(McuManager.FM_AUDIO, McuManager.CLOSE);
			SettingApp.putInt("sleeptype", 0);
			if(null!=mFmManager){
				mFmManager.fmStop();
				LogManager.d(TAG, "stopPlay      &&&&&&");
			}
		}

		@Override
		public void scanStart(int freq, int direction) throws RemoteException {
		}

		@Override
		public void scanStop(int type) throws RemoteException {
		}

		@Override
		public void registerCallback(IRadioCallback callback) throws RemoteException {
		}

		@Override
		public void unregisterCallback(IRadioCallback callback) throws RemoteException {
		}

		@Override
		public void playFreq(int bandType, int freq) throws RemoteException {
			playStatus = FM_STATUS_PLAYING;
			McuManager.sendMcuCommand(McuManager.FM_AUDIO, McuManager.START);
			SettingApp.putInt("sleeptype", 1);
			if(null!=mFmManager){
				mFmManager.fmPlay(freq, bandType);
				LogManager.d(TAG, "playFreq      &&&&&&      freq : "+freq);
			}
		}

		@Override
		public void searchWhole() throws RemoteException {
		}

		@Override
		public void activityResumed() throws RemoteException {
		}

	};

}
