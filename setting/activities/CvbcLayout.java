package com.landsem.setting.activities;

import java.io.File;
import com.landsem.setting.R;
import com.ls.lseasycontrol.LSCvbsEeffectInfo;
import com.ls.lseasycontrol.LSEasyControlManager;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class CvbcLayout extends RelativeLayout implements OnSeekBarChangeListener {

	Context mContext;
	int windowWidth;
	int windowHeight;
	RelativeLayout calibLayout;
	private WindowManager mWindowManager;
	private boolean isDiaplay;
	private WindowManager.LayoutParams omniParams;
	private SeekBar contrastSeekBar, lumaSeekBar, saturationSeekBar;
	TextView contrastText, lumaText, saturationText;
	int contrastValue=110, lumaValue=33, saturationValue=110;
	int contrastValueOld=110, lumaValueOld=33, saturationValueOld=110;
	int contrastValueFactory=110, lumaValueFactory=33, saturationValueFactory=110;
	File pointFile ;
//	FileOutputStream out;
	ImageView tuichuView ;
	private LSEasyControlManager mLSEasyControlManager;
	
	public CvbcLayout(Context context) {
		this(context, null);
	}
	public CvbcLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CvbcLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.activity_cvbs_set, this);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mContext = context;
		initData();
		initViews();
	}
	
	private void initData() {
		mLSEasyControlManager = new LSEasyControlManager(mContext);
		LSCvbsEeffectInfo mLSCvbsEeffectInfo = mLSEasyControlManager.Get_Cvbs_Effect(0);
		if (mLSCvbsEeffectInfo != null) {
			contrastValueOld =contrastValue = mLSCvbsEeffectInfo.contrast;
			lumaValueOld = lumaValue = mLSCvbsEeffectInfo.luma;
			saturationValueOld = saturationValue = mLSCvbsEeffectInfo.saturation;
		}
	}
	private void initViews() {
		this.setClickable(true);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}
	

	public void showDisplay(){
		if (!isDiaplay) {
			if (null==omniParams) {
				omniParams = createOmniParams();
			}
			initUI();
			refreshUI();
	       // initFile();
			mWindowManager.addView(this, omniParams);
			isDiaplay = true;
		}
	}
	
	public void hideDisplay(){
		if (isDiaplay) {
//			if (out!=null) {
//				try {
//					out.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
			mWindowManager.removeView(this);
			isDiaplay = false;
		}
	}
	
	public WindowManager.LayoutParams createOmniParams() {
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		 params.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;     
	     params.format=1;  
	     params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; 
	     params.flags = params.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;  
	     params.flags = params.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;  
	        
	     params.alpha = 1.0f;  
	          
	     params.gravity=Gravity.LEFT|Gravity.TOP;   
	      
		params.gravity = Gravity.TOP;
		params.x = 0;
		params.y = 0;
		params.width = 600;
		params.height = 350;
		return params;
	}

//	private void initFile() {
//		pointFile = new File("/sys/devices/soc.0/1c31000.tvd0/tvd0_attr/tvd0_system");
//		try {
//			out = new FileOutputStream(pointFile);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

	private void refreshUI() {
		contrastText.setText(contrastValue+"");
		lumaText.setText(lumaValue+"");
		saturationText.setText(saturationValue+"");
		contrastSeekBar.setProgress(contrastValue);
		lumaSeekBar.setProgress(lumaValue);
		saturationSeekBar.setProgress(saturationValue);
	}
	
	private void initUI() {
		tuichuView  = (ImageView) findViewById(R.id.tuichu_view);
		tuichuView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changePoint(lumaValueOld, saturationValueOld, contrastValueOld);
				hideDisplay();
			}
		});
		contrastText = (TextView) findViewById(R.id.contrast_value);
		lumaText = (TextView) findViewById(R.id.luma_value);
		saturationText = (TextView) findViewById(R.id.saturation_value);
		contrastSeekBar = (SeekBar) findViewById(R.id.contrast_seekbar);
		lumaSeekBar = (SeekBar) findViewById(R.id.luma_seekbar);
		saturationSeekBar = (SeekBar) findViewById(R.id.saturation_seekbar);
		initListener();
		contrastSeekBar.setOnSeekBarChangeListener(this);
		lumaSeekBar.setOnSeekBarChangeListener(this);
		saturationSeekBar.setOnSeekBarChangeListener(this);
	}

	private void initListener() {
		
		((TextView) findViewById(R.id.cvbs_cancel)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cancelOperate();
			}

			private void cancelOperate() {
				changePoint(lumaValueOld, saturationValueOld, contrastValueOld);
				lumaValue = lumaValueOld;
				saturationValue = saturationValueOld;
				contrastValue = contrastValueOld;
				refreshUI();
			}
		});
		((TextView) findViewById(R.id.cvbs_recover)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				recoverOperate();
			}

			private void recoverOperate() {
				changePoint(lumaValueFactory, saturationValueFactory, contrastValueFactory);
				lumaValueOld =lumaValue = lumaValueFactory;
				saturationValueOld = saturationValue = saturationValueFactory;
				contrastValueOld = contrastValue = contrastValueFactory;
				refreshUI();
			}
		});
		((TextView) findViewById(R.id.cvbs_sure)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				changePoint(lumaValue, saturationValue, contrastValue);
				hideDisplay();
			}
		});
		((ImageView) findViewById(R.id.luma_down)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lumaValue--;
				changePoint(lumaValue, saturationValue, contrastValue);
				refreshUI();
			}
		});
		((ImageView) findViewById(R.id.luma_up)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lumaValue++;
				changePoint(lumaValue, saturationValue, contrastValue);
				refreshUI();
			}
		});
		((ImageView) findViewById(R.id.saturation_down)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				saturationValue--;
				changePoint(lumaValue, saturationValue, contrastValue);
				refreshUI();
			}
		});
		((ImageView) findViewById(R.id.saturation_up)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				saturationValue++;
				changePoint(lumaValue, saturationValue, contrastValue);
				refreshUI();
			}
		});
		((ImageView) findViewById(R.id.contrast_down)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				contrastValue--;
				changePoint(lumaValue, saturationValue, contrastValue);
				refreshUI();
			}
		});
		((ImageView) findViewById(R.id.contrast_up)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				contrastValue++;
				changePoint(lumaValue, saturationValue, contrastValue);
				refreshUI();
			}
		});
		
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		if (!fromUser) {
			return;
		}
		if (progress<=0) {
			progress = 1;
		}
		switch (seekBar.getId()) {
		case R.id.contrast_seekbar:
			contrastValue = progress;
			break;
		case R.id.luma_seekbar:
			lumaValue = progress;
			break;
		case R.id.saturation_seekbar:
			saturationValue = progress;
			break;
		}
		changePoint(lumaValue, saturationValue, contrastValue);
		uIHandler.sendEmptyMessage(0);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
	
	private void changePoint(int lumaValue, int saturationValue, int contrastValue) {
		 try {
//			 if (out!=null) {
//				 out.write((contrastValue+","+colorValue+";"+saturationValue).getBytes());
//			}
			 mLSEasyControlManager.Set_Cvbs_Effect(0, lumaValue, saturationValue, contrastValue);
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	
	Handler uIHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			refreshText();
		};
	};
	
	private void refreshText() {
		contrastText.setText(contrastValue+"");
		lumaText.setText(lumaValue+"");
		saturationText.setText(saturationValue+"");
	}
}
