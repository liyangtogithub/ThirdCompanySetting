package com.landsem.setting.view;

import com.landsem.setting.R;
import com.landsem.setting.effect.BaseEffects;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class NiftyDialog extends Dialog implements DialogInterface {

	private volatile static NiftyDialog instance;
	private final String defTextColor = "#FFFFFFFF";
	private final String defDividerColor = "#11000000";
	private final String defMsgColor = "#FFFFFFFF";
	private final String defDialogColor = "#48494B";
	private Effectstype type = null;
	private LinearLayout mLinearLayoutView;
	private RelativeLayout mRelativeLayoutView;
	private LinearLayout mLinearLayoutMsgView;
	private LinearLayout mLinearLayoutTopView;
	private LinearLayout controlPanelLayout;
	private FrameLayout mFrameLayoutCustomView;
	private View mDialogView;
	private View titleDivider;
	private View sternDivider;
	private TextView mTitle;
	private TextView mMessage;
	private ImageView mIcon;
	private TextView mButton1;
	private TextView mButton2;
	private View buttonSplit;
	private int mDuration = -1;
	private static int mOrientation = 1;
	private boolean isCancelable = true;


	public NiftyDialog(Context context) {
		super(context);
		init(context);

	}

	public NiftyDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

	}

	public static NiftyDialog getInstance(Context context) {

		int ort = context.getResources().getConfiguration().orientation;
		if (mOrientation != ort) {
			mOrientation = ort;
			instance = null;
		}

		if (instance == null) {
			synchronized (NiftyDialog.class) {
				if (instance == null) {
					instance = new NiftyDialog(context, R.style.dialog_untran);
				}
			}
		}
		return instance;

	}
	
	private void init(Context context) {
		mDialogView = View.inflate(context, R.layout.dialog_layout, null);
		mLinearLayoutView = (LinearLayout) mDialogView.findViewById(R.id.parentPanel);
		mRelativeLayoutView = (RelativeLayout) mDialogView.findViewById(R.id.main);
		mLinearLayoutTopView = (LinearLayout) mDialogView.findViewById(R.id.topPanel);
		mLinearLayoutMsgView = (LinearLayout) mDialogView.findViewById(R.id.contentPanel);
		mFrameLayoutCustomView = (FrameLayout) mDialogView.findViewById(R.id.customPanel);
		controlPanelLayout = (LinearLayout) mDialogView.findViewById(R.id.contorlPanel);
		mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
		mMessage = (TextView) mDialogView.findViewById(R.id.message);
		mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
		titleDivider = mDialogView.findViewById(R.id.titleDivider);
		sternDivider = mDialogView.findViewById(R.id.sternDivider);
		mButton1 = (TextView) mDialogView.findViewById(R.id.button1);
		mButton2 = (TextView) mDialogView.findViewById(R.id.button2);
		buttonSplit = mDialogView.findViewById(R.id.buttonSplit);
		
		setContentView(mDialogView);

		this.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {

				mLinearLayoutView.setVisibility(View.VISIBLE);
				if (type == null) {
					type = Effectstype.Slidetop;
				}
				start(type);
			}
		});
		mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isCancelable)
					dismiss();
			}
		});
	}

	public void toDefault() {
		mTitle.setTextColor(Color.parseColor(defTextColor));
		titleDivider.setBackgroundColor(Color.parseColor(defDividerColor));
		sternDivider.setBackgroundColor(Color.parseColor(defDividerColor));
		mMessage.setTextColor(Color.parseColor(defMsgColor));
		mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
	}

	public NiftyDialog withDividerColor(String colorString) {
		titleDivider.setBackgroundColor(Color.parseColor(colorString));
		return this;
	}
	
	public NiftyDialog withSternDividerColor(String colorString){
		sternDivider.setBackgroundColor(Color.parseColor(colorString));
		return this;
	}

	public NiftyDialog withTitle(CharSequence title) {
		toggleView(mLinearLayoutTopView, title);
		mTitle.setText(title);
		return this;
	}

	public NiftyDialog withTitleColor(String colorString) {
		mTitle.setTextColor(Color.parseColor(colorString));
		return this;
	}
	
	public NiftyDialog withTitleColor(int resId) {
		mTitle.setTextColor(resId);
		return this;
	}

	public NiftyDialog withMessage(int textResId) {
		toggleView(mLinearLayoutMsgView, textResId);
		mMessage.setText(textResId);
		return this;
	}
	
	public NiftyDialog withControlPanel(int visibility){
		controlPanelLayout.setVisibility(visibility);
		return this;
	}

	public NiftyDialog withTopPanel(int visibility){
		mLinearLayoutTopView.setVisibility(visibility);
		return this;
	}
	
	public NiftyDialog withContentPanel(int visibility){
		mLinearLayoutMsgView.setVisibility(visibility);
		return this;
	}

	public NiftyDialog withMessage(CharSequence msg) {
		toggleView(mLinearLayoutMsgView, msg);
		mMessage.setText(msg);
		return this;
	}

	public NiftyDialog withMessageColor(String colorString) {
		mMessage.setTextColor(Color.parseColor(colorString));
		return this;
	}
	
	public NiftyDialog withMessageColor(int colorId) {
		mMessage.setTextColor(colorId);
		return this;
	}

	public NiftyDialog withIcon(int drawableResId) {
		mIcon.setImageResource(drawableResId);
		return this;
	}

	public NiftyDialog withIcon(Drawable icon) {
		mIcon.setImageDrawable(icon);
		return this;
	}

	public NiftyDialog withDuration(int duration) {
		this.mDuration = duration;
		return this;
	}

	public NiftyDialog withEffect(Effectstype type) {
		this.type = type;
		return this;
	}

	public NiftyDialog withButtonDrawable(int resid) {
		mButton1.setBackgroundResource(resid);
		mButton2.setBackgroundResource(resid);
		return this;
	}

	public NiftyDialog withButton1Text(CharSequence text) {
		mButton1.setVisibility(View.VISIBLE);
		mButton1.setText(text);

		return this;
	}

	public NiftyDialog withButton2Text(CharSequence text) {
		mButton2.setVisibility(View.VISIBLE);
		mButton2.setText(text);
		return this;
	}
	
	public NiftyDialog withButton1Tag(Object obj){
		mButton1.setTag(obj);
		return this;
	}
	
	public NiftyDialog withButton2Tag(Object obj){
		mButton2.setTag(obj);
		return this;
	}
	
	
	public NiftyDialog hideButton2(){
		mButton2.setVisibility(View.GONE);
		buttonSplit.setVisibility(View.GONE);
		return this;
	}

	public NiftyDialog setButton1Click(View.OnClickListener click) {
		mButton1.setOnClickListener(click);
		return this;
	}

	public NiftyDialog setButton2Click(View.OnClickListener click) {
		mButton2.setOnClickListener(click);
		return this;
	}

	public NiftyDialog setCustomView(int resId, Context context) {
		View customView = View.inflate(context, resId, null);
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(customView);
		return this;
	}

	public NiftyDialog setCustomView(View view, Context context) {
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(view);

		return this;
	}

	public NiftyDialog isCancelableOnTouchOutside(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCanceledOnTouchOutside(cancelable);
		return this;
	}

	public NiftyDialog isCancelable(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCancelable(cancelable);
		return this;
	}

	private void toggleView(View view, Object obj) {
		if (obj == null) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void show() {
		if (mTitle.getText().equals("")) mDialogView.findViewById(R.id.topPanel).setVisibility(View.GONE);
		super.show();
	}

	private void start(Effectstype type) {
		BaseEffects animator = type.getAnimator();
		if (mDuration != -1) {
			animator.setDuration(Math.abs(mDuration));
		}
		animator.start(mRelativeLayoutView);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		mButton1.setVisibility(View.GONE);
		mButton2.setVisibility(View.GONE);
	}
}
