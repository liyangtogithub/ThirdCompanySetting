package com.lqpdc.commonlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 *
 */

public class CustomLinearLayout extends LinearLayout {

	 private GestureDetector gestureDetector;
	 
	public CustomLinearLayout(Context context) {
		super(context);
	}
	
   public CustomLinearLayout(Context context, AttributeSet attrs) {
       super(context, attrs);
   }
   
   public void setGestureDetector(GestureDetector gestureDetector) {
       this.gestureDetector = gestureDetector;
   }

   @Override
   public boolean dispatchTouchEvent(MotionEvent ev) {
       if (this.gestureDetector != null)
           this.gestureDetector.onTouchEvent(ev);
       return super.dispatchTouchEvent(ev);

   }

}