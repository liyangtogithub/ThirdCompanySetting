package com.landsem.setting.activities;


import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.R;
import com.ls.lseasycontrol.LSEasyControlManager;

public class CalibLayout extends RelativeLayout {



	Context mContext;

	/**           
	 * LSEasyControlManager.java public int Set_Tp_Adjust_Pos(int index, int x, int y)
	 * 第一步设置第一个校准点 index = 0， x y为显示的坐标 
	 * 第二步设置第二个校准点 index = 1， x y为显示的坐标
	 * 第三步设置第三个校准点 index = 2， x y为显示的坐标，
	 * 如果返回值为-1，则回到第一步，并且弹窗提示。否则显示校准成功退出。触摸校准那个
	 */  
	private LSEasyControlManager mLSEasyControlManager;
	ImageView ImageCalib;
	byte calibTimes = 0;
	int windowWidth;
	int windowHeight;
	RelativeLayout calibLayout;
	/** 图标中心点*/
	int xValue;
	int yValue;
	private WindowManager mWindowManager;
	private boolean isDiaplay;
	private WindowManager.LayoutParams omniParams;
	
	public CalibLayout(Context context) {
		this(context, null);
	}
	public CalibLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CalibLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.activity_calib, this);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mContext = context;
		mLSEasyControlManager = new LSEasyControlManager(mContext);
		initDisPlay();
		initViews();
	}
	
	private void initViews() {
		this.setClickable(true);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		ImageCalib = (ImageView) findViewById(R.id.ImageCalib);
		calibLayout = (RelativeLayout) findViewById(R.id.calibLayout);
		calibLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case  MotionEvent.ACTION_UP:
					LogManager.d( "event.getAction() ACTION_UP =1: "+action);
					int x = (int) event.getX();
			        int y = (int) event.getY();
			        LogManager.d( "receive- x: "+x+"  y"+y+"  calibTimes"+calibTimes);
					calibTimes++;
					if (calibTimes == 1) {
						setLayout(ImageCalib, windowWidth - windowWidth/8, windowHeight-windowHeight/8);
					} else if (calibTimes == 2) {
						setLayout(ImageCalib, windowWidth - windowWidth/8, windowHeight/8);
					} else if (calibTimes == 3 && isCabitOk(x, y)) {
						Toast.makeText(mContext, mContext.getString(R.string.toast_calib_success),Toast.LENGTH_SHORT).show();
						hideDisplay();
					} else {
						calibTimes = 0;
						setLayout(ImageCalib, windowWidth/8, windowHeight/8);
						Toast.makeText(mContext, mContext.getString(R.string.toast_calib_error),Toast.LENGTH_SHORT).show();
					}
					break;
				case MotionEvent.ACTION_DOWN:
					LogManager.d( "event.getAction() ACTION_DOWN =0: "+action);
				    break;
				}
				 return true;
			}
		});
	}
	
	 private void initDisPlay() {
		 windowWidth = getResources().getDisplayMetrics().widthPixels;       
		 windowHeight =getResources().getDisplayMetrics().heightPixels;   
		 LogManager.d( "windowWidth: "+windowWidth+"  windowHeight"+windowHeight);
	}
	 
	 private boolean isCabitOk(int x, int y) {
			int xCabit = xValue-x;
			int yCabit = yValue-y;
			 if ( xCabit<10 && xCabit>-10 &&  yCabit<10 && yCabit>-10) {
				 return true;
			}
			return false;
		}

	public void showDisplay(){
		if (!isDiaplay) {
			mLSEasyControlManager.Tp_Adjust_Start(); 
			if (null==omniParams) {
				omniParams = createOmniParams();
			}
			mWindowManager.addView(this, omniParams);
			isDiaplay = true;
			setLayout(ImageCalib, windowWidth/8, windowHeight/8);
		}
	}
	
	public void hideDisplay(){
		if (isDiaplay) {
			mLSEasyControlManager.Tp_Adjust_Finish();
			mWindowManager.removeView(this);
			isDiaplay = false;
		}
	}
	
	public WindowManager.LayoutParams createOmniParams() {
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL;
		params.format = PixelFormat.RGBA_8888;
		params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		params.gravity = Gravity.CENTER;
		params.x = 0;
		params.y = 0;
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		return params;
	}

	/*
	 * 设置控件所在的位置，并且不改变宽高， XY为绝对位置
	 */  
    public  void setLayout(View view,int x,int y)  
    {  
        MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());  
        margin.setMargins(x,y, x+margin.width, y+margin.height);  
        xValue = x+15;
        yValue = y+15;
        mLSEasyControlManager.Set_Tp_Adjust_Pos(calibTimes, xValue , yValue);
        LogManager.d( "send- x: "+x+"  y"+y);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);  
        view.setLayoutParams(layoutParams);  
    }  
	
}
