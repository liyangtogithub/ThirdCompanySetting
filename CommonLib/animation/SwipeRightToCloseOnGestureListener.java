package com.lqpdc.commonlib.animation;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.lqpdc.commonlib.R;


/**
 * 
 * 向右滑动finish掉activity
 */
public class SwipeRightToCloseOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    private Activity activity;
    protected MotionEvent mLastOnDownEvent = null;
    private int scaledMinimumFlingVelocity;

    public SwipeRightToCloseOnGestureListener(Activity activity) {
        this.activity = activity;
        this.scaledMinimumFlingVelocity = ViewConfiguration.get(activity).getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mLastOnDownEvent = e;
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null)
            e1 = mLastOnDownEvent;
        if (e1 == null || e2 == null)
            return false;
        if (e2.getRawX() - e1.getRawX() > 200 && Math.abs(velocityX) > scaledMinimumFlingVelocity) {
            this.activity.finish();
            this.activity.overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
            return true;
        }
        return false;
    }
}
