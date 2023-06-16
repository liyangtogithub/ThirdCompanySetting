package com.landsem.setting.carrier;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.entity.FmParam;
import com.landsem.setting.service.RadioService;
import com.ls.fmradio.IRadioService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class FmEmitCarrier extends BaseCarrier implements SeekBar.OnSeekBarChangeListener, Constant{

	private static final long serialVersionUID = -4277696990582603358L;
	private static final String TAG = FmEmitCarrier.class.getSimpleName();
	private SeekBar mFreqProgress;
	private TextView mFreqText;
	private AudioManager audioManager;
	private View nextFmPoint;
	private View prevFmPoint;
	private View emitContent;
	private TextView markedWords;
	private TextView launchSwitchWord;
	private Switch fmSwitch;
	public static FmParam mFmParam = new FmParam();
	private static IRadioService mFmService;
	private static FmEmitCarrier instance;
	private FmConnection connection = new FmConnection();
	
	public static FmEmitCarrier newInstance(Context context, LayoutInflater inflater, int resource){
		if(null==instance){
			synchronized (FmEmitCarrier.class) {
				if(null==instance) instance = new FmEmitCarrier(context, inflater, resource);
			}
		}
		return instance;
	}
	
	public static FmEmitCarrier reNewInstance(Context context, LayoutInflater inflater, int resource){
		if(null!=instance) instance = null;
		return newInstance(context, inflater, resource);
	}
	
	private FmEmitCarrier(LayoutInflater inflater, int resource) {
		super(inflater, resource);
		
	}

	private FmEmitCarrier(Context context, LayoutInflater inflater, int resource) {
		super(context, inflater, resource);
		initViews(contentView);
		initListener();
		Intent intent = new Intent(context, RadioService.class);
		context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
		updateProgress();
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		initFmStates();
	}
	
	public void doFMSend(){
		mFmParam.setFmState(!mFmParam.isFmState());
		initFmStates();
	}
	
	private void initFmStates(){
		fmSwitch.setChecked(mFmParam.isFmState());
		if(mFmParam.isFmState()){
			startFmPlayer(mFmParam.getFmHZ());
			updateProgress();
			markedWords.setVisibility(View.GONE);
			emitContent.setVisibility(View.VISIBLE);
		}else{
			stopFmPlayer();
			emitContent.setVisibility(View.GONE);
			markedWords.setVisibility(View.VISIBLE);
		}
	}
	
	private void sendFmWorkState(String action, Context context){
		if(null!=context && null!=action && !action.trim().equals("")){
			Intent intent = new Intent(action);
			context.sendBroadcastAsUser(intent, UserHandle.ALL);
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

	private void startFmPlayer(int freq) {
		LogManager.d(TAG, "startFmPlayer      &&&&&&      mFmService : "+mFmService);
		if(null!=mFmService && mFmParam.isFmState()){
			try {
				LogManager.d(TAG, "startFmPlayer      &&&&&&      start");
				mFmService.playFreq(BAND_FM, freq);
				audioManager.playSoundEffect(0xff);
				sendFmWorkState(Action.FM_LAUNCH_ON, context);
				LogManager.d(TAG, "startFmPlayer      &&&&&&      end");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void doStartFmPlay(){
		updateProgress();
		startFmPlayer(mFmParam.getFmHZ());
	}
	
	public void doOuterStartFmPlay(int hz){
		LogManager.d(TAG, "doOuterStartFmPlay      &&&&&&      HZ : "+hz);
		if(hz>=FM_FREQ_MIN && hz<=FM_FREQ_MAX){
			mFmParam.setFmHZ(hz);
			updateProgress();
		}
		if(!mFmParam.isFmState()){
			doFMSend();
		}else{
			startFmPlayer(mFmParam.getFmHZ());
		}
	}
	
	public void doOuterStopFmPlay(){
		boolean fmState = mFmParam.isFmState();
		LogManager.d(TAG, "doOuterStopFmPlay      &&&&&&      fmState : "+fmState);
		if(fmState) doFMSend();
	}
	

	public void stopFmPlayer() {
		LogManager.d(TAG, "stopFmPlayer      &&&&&&      mFmService : "+mFmService);
		if (null!=mFmService) {
			try {
				LogManager.d(TAG, "stopFmPlayer      &&&&&&      start");
				mFmService.stopPlay();
				audioManager.playSoundEffect(0xff00);
				sendFmWorkState(Action.FM_LAUNCH_OFF, context);
				LogManager.d(TAG, "stopFmPlayer      &&&&&&      end");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void doFmAction(){
		if(null!=mFmService){
			if(null!=mFmParam){
				if(mFmParam.isFmState()){
					doStartFmPlay();
				}else{
					stopFmPlayer();
				}
			}
		}
	}
	
	private int adjustFmPoint(boolean added){
		int freq = mFmParam.seekStep(added);
		updateProgress();
		mFmParam.setFmHZ(freq);
		startFmPlayer(freq);
		return freq;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.prev_freq_point:
			adjustFmPoint(false);
			break;
		case R.id.next_freq_point:
			adjustFmPoint(true);
			break;
		case R.id.fm_switch:
			doFMSend();
			break;
		}
	}

	@Override
	protected void initViews(View convertView) {
		emitContent = convertView.findViewById(R.id.emit_content);
		markedWords = (TextView) convertView.findViewById(R.id.marked_words);
		launchSwitchWord = (TextView) convertView.findViewById(R.id.launch_switch_word);
		prevFmPoint = convertView.findViewById(R.id.prev_freq_point);
		nextFmPoint = convertView.findViewById(R.id.next_freq_point);
		mFreqText = (TextView) convertView.findViewById(R.id.curr_freq_point);
		mFreqProgress = (SeekBar) convertView.findViewById(R.id.freq_seekbar);
		fmSwitch = (Switch) convertView.findViewById(R.id.fm_switch);
	}

	@Override
	protected void initListener() {
		nextFmPoint.setOnClickListener(this);;
		prevFmPoint.setOnClickListener(this);;
		fmSwitch.setOnClickListener(this);
		mFreqProgress.setOnSeekBarChangeListener(this);
	}

	@Override
	protected void initViewsState() {
		

	}
	
	public void updateWords(){
		if(null!=launchSwitchWord){
			String launchWord = SettingApp.getInstance().getResources().getString(R.string.fm_launch_switch);
			launchSwitchWord.setText(launchWord);
		}
		if(null!=markedWords){
			String markedword = SettingApp.getInstance().getResources().getString(R.string.fm_launch_hint);
			markedWords.setText(markedword);
		}
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int progress = seekBar.getProgress();
		int num1 = progress / 10;
		int num2 = progress % 10;
		if (num2>=5) progress = (num1 + 1) * 10;
		else progress = num1 * 10;
		int freq = FM_FREQ_MIN + progress;
		mFmParam.setFmHZ(freq);
		updateProgress();
		startFmPlayer(freq);
	}

	private void updateProgress() {
		int freq = mFmParam.getFmHZ();
		mFreqProgress.setMax(BAND_SCOPE);
		mFreqProgress.setProgress(freq - FM_FREQ_MIN);
		setFreqText(freq);
	}

	private void setFreqText(int freq) {
		StringBuffer buffer = new StringBuffer(String.valueOf(freq));
		buffer.insert(buffer.length() - 2, ".");
		mFreqText.setText(buffer.toString());
	}
	
	public void releaseConnect(){
//		context.unbindService(connection);
	}
	
	
	private final class FmConnection implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			try {
				mFmService = IRadioService.Stub.asInterface(service);
				doFmAction();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mFmService = null;
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

}
