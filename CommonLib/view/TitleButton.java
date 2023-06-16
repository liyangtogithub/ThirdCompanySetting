package com.lqpdc.commonlib.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lqpdc.commonlib.R;

public class TitleButton extends LinearLayout {
	
	private static final int ID_IMAGE = 1;
	private static final int ID_TEXT = 2;
	private onBackListener backListener;
    private ColorStateList textColor ;

	public void setBackListener(onBackListener backListener) {
		this.backListener = backListener;
	}

	public TitleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	@SuppressLint("ClickableViewAccessibility")
	private void init(Context context, AttributeSet attrs) {
		final ImageView iconView = new ImageView(context);
		iconView.setId(ID_IMAGE);
		LayoutParams imageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iconView.setLayoutParams(imageParams);
		iconView.setBackgroundResource(R.drawable.title_back_normal);
		addView(iconView);

		final TextView textView = new TextView(context, attrs);
		textView.setId(ID_TEXT);
		LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textParams.setMargins(12, 0, 0, 0);
		textView.setLayoutParams(textParams);
		textView.setTextSize(30);
		parseTextColor(context, attrs, com.android.internal.R.attr.textViewStyle);
//		String clickAttr = attr.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick");
		textView.setTextColor(textColor != null ? textColor : ColorStateList.valueOf(0xFF000000));
//		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		addView(textView);

		OnTouchListener impassiveListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		};
		iconView.setOnTouchListener(impassiveListener);
		textView.setOnTouchListener(impassiveListener);

		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					iconView.setBackgroundResource(R.drawable.title_back_pressed);
					textView.setTextColor(Color.rgb(0x38, 0xCC, 0xFC));
//					Resources resource = (Resources) getBaseContext().getResources();  
//					ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.button_bg_select); 
//					textView.setTextColor(csl);
//					textView.setTextColor(Color.parseColor("#FFFFFF"));  
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					iconView.setBackgroundResource(R.drawable.title_back_normal);
					
					textView.setTextColor(textColor != null ? textColor : ColorStateList.valueOf(0xFF000000));
			
					break;
				default:
					break;
				}
				return false;
			}
		});

		String clickAttr = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick");
		if (clickAttr == null || clickAttr.length() == 0) {
			setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Activity activity = (Activity) getContext();
					activity.finish();
					if(null!=backListener){
//						backListener.back();
					}
				}
			});
		}

		setBackgroundColor(Color.TRANSPARENT);
		setPadding(10, 5, 10, 5);
		setGravity(Gravity.CENTER_VERTICAL);
	}

	public TextView getTextView() {
		return (TextView) findViewById(ID_TEXT);
	}

	public ImageView getIconView() {
		return (ImageView) findViewById(ID_IMAGE);
	}
	
	public interface onBackListener{
		void back();
	}
	
	private void parseTextColor(Context context, AttributeSet attrs,int defStyle){
		
       final Resources.Theme theme = context.getTheme();

	   TypedArray a = theme.obtainStyledAttributes(
                   attrs, com.android.internal.R.styleable.TextViewAppearance,  defStyle, 0);
       TypedArray appearance = null;
       int ap = a.getResourceId(
               com.android.internal.R.styleable.TextViewAppearance_textAppearance, -1);
       a.recycle();
       if (ap != -1) {
           appearance = theme.obtainStyledAttributes(
                   ap, com.android.internal.R.styleable.TextAppearance);
       }
       
       if(null!=appearance){
           int n = appearance.getIndexCount();
           for (int i = 0; i < n; i++) {
               int attr = appearance.getIndex(i);

               switch (attr) {
               case com.android.internal.R.styleable.TextAppearance_textColor:
                   textColor = appearance.getColorStateList(attr);
                   break;
               }
           }
           appearance.recycle();
       }
       
       a = theme.obtainStyledAttributes(
               attrs, com.android.internal.R.styleable.TextView, defStyle, 0);

       int n = a.getIndexCount();
       for(int i=0;i<n;i++){
           int attr = a.getIndex(i);

           switch (attr) {

           case com.android.internal.R.styleable.TextView_textColor:
               textColor = a.getColorStateList(attr);
               break;

           }
       }
       a.recycle();
	}
	
	
	
}
