package com.landsem.setting.view;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.ls.mcu.McuEvent;
import com.ls.mcu.McuEventListener;
import com.ls.mcu.McuManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RemotePairView extends LinearLayout implements OnClickListener, McuEventListener{
	
	private static final String TAG = RemotePairView.class.getSimpleName();
	private static final String remotePair = "remotePair";
	private TextView enterRemotePair;
	private TextView exitRemotePair;
	private TextView removeRemotePair;
	private TextView remotePairHint;
	private McuManager mMcuManager;
	private boolean isPaired;
	
	
	public RemotePairView(Context context) {
		this(context, null);
	}
	
	public RemotePairView(Context context, AttributeSet attrs) {
		this(context, attrs, 1);
	}
	
	public RemotePairView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.remote_pair_layout, this);
		mMcuManager = new McuManager(context.getMainLooper());
		initViews();
		initListener();
		updateViewsStatus(true, true, true, true);
	}
	
	public void updateViewsStatus(boolean enterEnable, boolean exitEnable, boolean removeEnable, boolean updateHint){
		if(null!=enterRemotePair) enterRemotePair.setEnabled(enterEnable);
		if(null!=exitRemotePair) exitRemotePair.setEnabled(exitEnable);
		if(null!=removeRemotePair) removeRemotePair.setEnabled(removeEnable);
		if(updateHint && null!=remotePairHint){
			isPaired = SettingApp.getSystemBoolean(remotePair, false);
			if(isPaired){
				remotePairHint.setText("遥控已经配对");
			}else{
				remotePairHint.setText("");
			}
		}
	}

	private void initListener() {
		if(null!=mMcuManager) mMcuManager.registerListener(this);
		if(null!=enterRemotePair) enterRemotePair.setOnClickListener(this);
		if(null!=exitRemotePair) exitRemotePair.setOnClickListener(this);
		if(null!=removeRemotePair) removeRemotePair.setOnClickListener(this);
		
	}

	private void initViews() {
		enterRemotePair = (TextView) findViewById(R.id.enter_remote_pair);
		exitRemotePair = (TextView) findViewById(R.id.exit_remote_pair);
		removeRemotePair = (TextView) findViewById(R.id.remove_remote_pair);
		remotePairHint = (TextView) findViewById(R.id.remote_pair_hint);
	}

	@Override
	public void onClick(View view) {
		view.setEnabled(false);
		switch (view.getId()) {
		case R.id.enter_remote_pair:
			McuManager.sendMcuCommand(McuManager.SET_433_CONTROLLER_PAIR, McuManager.TYPE_CONTROLLER_PAIR_ENTER);
			break;
		case R.id.exit_remote_pair:
			McuManager.sendMcuCommand(McuManager.SET_433_CONTROLLER_PAIR, McuManager.TYPE_CONTROLLER_PAIR_EXIT);
			break;
		case R.id.remove_remote_pair:
			McuManager.sendMcuCommand(McuManager.SET_433_CONTROLLER_PAIR, McuManager.TYPE_CONTROLLER_DELETE);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onEventChanged(McuEvent mcuEvent) {
		short[] values = mcuEvent.values;
		LogManager.d(TAG, "onEventChanged start");
		if(null!=values && values.length==2){
			switch (values[0]) {
			case McuManager.MCU_MESSAGE_TYPE_CONTROLLER_PAIR:
				doRemotePair(values);
				break;
			}
		}
	}

	private void doRemotePair(short[] values) {
		switch (values[1]) {
		case McuManager.TYPE_CONTROLLER_PAIR_ENTER:
			updateViewsStatus(false, true, true, false);
			remotePairHint.setText("已进入配对模式");
			SettingApp.putSystemBoolean(remotePair, false);
			break;
		case McuManager.TYPE_CONTROLLER_PAIR_EXIT:
			updateViewsStatus(true, true, true, false);
			remotePairHint.setText("已退出配对模式");
			break;
		case McuManager.TYPE_CONTROLLER_PAIR_SUCCESS:
			updateViewsStatus(true, true, true, false);
			remotePairHint.setText("配对成功");
			SettingApp.putSystemBoolean(remotePair, true);
			break;
		case McuManager.TYPE_CONTROLLER_PAIR_FAILED:
			updateViewsStatus(true, true, true, false);
			remotePairHint.setText("配对失败，请重试");
			SettingApp.putSystemBoolean(remotePair, false);
			break;
		case McuManager.TYPE_CONTROLLER_DELETE:
			updateViewsStatus(true, true, true, false);
			remotePairHint.setText("配对删除成功");
			SettingApp.putSystemBoolean(remotePair, false);
			break;
		default:
			break;
		}
		
	}
}
