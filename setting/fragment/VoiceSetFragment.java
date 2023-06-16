package com.landsem.setting.fragment;

import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.activities.MainActivity;
import com.landsem.setting.carrier.AudioCarrier;
import com.landsem.setting.carrier.FmEmitCarrier;
import com.landsem.setting.carrier.VoiceAmpliCarrier;
import com.landsem.setting.carrier.VoiceCarrier;
import com.landsem.setting.carrier.VoiceWakeUpCarrier;
import com.landsem.setting.view.MenuTextView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("serial")
public class VoiceSetFragment extends BaseFragment {

	private static final String TAG = VoiceSetFragment.class.getSimpleName();
	private TextView launchOption;
	private TextView tohesOption;
	private TextView voiceWakeUp;
	private TextView voiceAmpli;
	private AudioCarrier mAudioCarrier;
	private FmEmitCarrier mFmEmitCarrier;
	private VoiceCarrier mVoiceCarrier;
	private VoiceWakeUpCarrier mVoiceWakeUpCarrier;
	private VoiceAmpliCarrier mVoiceAmpliCarrier;

	public VoiceSetFragment() {
		super();
		injectResource(R.layout.voice_set_layout);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initListener() {
		launchOption.setOnClickListener(this);
		tohesOption.setOnClickListener(this);
		voiceWakeUp.setOnClickListener(this);
		voiceAmpli.setOnClickListener(this);
		onClick(tohesOption);
	}

	@Override
	public void initViews(View converView) {
		contentLayout = (LinearLayout) converView
				.findViewById(R.id.display_content);
		launchOption = (TextView) converView.findViewById(R.id.fmlaunch_option);
		tohesOption = (TextView) converView.findViewById(R.id.tohes_option);
		voiceWakeUp = (TextView) converView.findViewById(R.id.voice_wakeup);
		voiceAmpli = (TextView) converView.findViewById(R.id.voice_ampli);
		initGravity();
	}

	public void displayOption(int optionLocation) {
		// switch (optionLocation) {
		// // case 0:
		// // onClick(launchOption);
		// // break;
		// case 1:
		// onClick(tohesOption);
		// break;
		// case 2:
		// onClick(voiceWakeUp);
		// break;
		// default:
		// break;
		// }
	}

	private void initGravity() {
		int gravity = Gravity.CENTER;
		launchOption.setGravity(gravity);
		tohesOption.setGravity(gravity);
		voiceWakeUp.setGravity(gravity);
		voiceAmpli.setGravity(gravity);
	}

	@Override
	public void onClick(View view) {
		TextView textView = null;
		if (view instanceof MenuTextView)
			textView = (MenuTextView) view;
		boolean result = doChangeEnabled(textView);
		int optionLocation = 0;
		switch (view.getId()) {
		// case R.id.fmlaunch_option:
		// // createEmitCarrier();
		// // contentLayout.addView(mFmEmitCarrier.getContentView());
		// optionLocation = 0;
		// break;
		case R.id.tohes_option:
			createVoiceCarrier();
			contentLayout.addView(mVoiceCarrier.contentView);
			optionLocation = 1;
			break;
		case R.id.voice_wakeup:
			initVoiceWakeUpCarrier();
			contentLayout.addView(mVoiceWakeUpCarrier.contentView);
			optionLocation = 2;
			break;
		case R.id.voice_ampli:
			initVoiceAmpliCarrier();
			contentLayout.addView(mVoiceAmpliCarrier.contentView);
			optionLocation = 3;
			break;
		}
		((MainActivity) getActivity()).setOptionLocation(optionLocation);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mFmEmitCarrier)
			mFmEmitCarrier.releaseConnect();
	}

	private void createVoiceCarrier() {
		if (null == mVoiceCarrier)
			mVoiceCarrier = new VoiceCarrier(getActivity(), inflater,
					R.layout.voice_carrier_layout);
	}

	public void initVoiceWakeUpCarrier() {
		if (null == mVoiceWakeUpCarrier)
			mVoiceWakeUpCarrier = new VoiceWakeUpCarrier(getActivity(),
					inflater, R.layout.voicewakeup_carrier_layout);
	}
	
	public void initVoiceAmpliCarrier() {
		if (null == mVoiceAmpliCarrier)
			mVoiceAmpliCarrier = new VoiceAmpliCarrier(getActivity(),
					inflater, R.layout.voice_ampli_carrier_layout);
	}

	private void createEmitCarrier() {
		if (null == mFmEmitCarrier)
			mFmEmitCarrier = FmEmitCarrier.newInstance(getActivity(), inflater,
					R.layout.emit_carrier_layout);
	}

	@Override
	public void doOnResume() {
	}

	@Override
	public void doOnPause() {

	}

	@Override
	public void onResume() {
		super.onResume();
		doOnResume();
	}

}
