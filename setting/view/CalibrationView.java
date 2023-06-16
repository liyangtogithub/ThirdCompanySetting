package com.landsem.setting.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.landsem.setting.R;

public class CalibrationView extends View {
	private OnPoistionChangedListener mOnPoistionChangedListener;
	private Bitmap mMoveThumb;
	private int currentX=0,currentY=0;
	int pointIndex=0;
	private ArrayList<Point> points;

	public CalibrationView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public CalibrationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public CalibrationView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs,defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init()
	{
		points=new ArrayList<Point>();
		points.add(new Point(80, 60));
		points.add(new Point(720, 60));
		points.add(new Point(80, 400));
		points.add(new Point(720, 400));
		points.add(new Point(400, 240));
		Resources res = getResources();
		currentX=points.get(pointIndex).x;
		currentY=points.get(pointIndex).y;
		mMoveThumb = BitmapFactory.decodeResource(res, R.drawable.seekbar_thumb);
	}
	
	public void setOnPoistionChangedListener(OnPoistionChangedListener onPoistionChangedListener)
	{
		mOnPoistionChangedListener =onPoistionChangedListener;
	}

	@Override
	public void setLayoutParams(LayoutParams params) 
	{
		params.width =800;
		params.height =480;
		super.setLayoutParams(params);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint();
		if(pointIndex==0){
			CustomToast.makeText(getContext(), "校准开始").show();
		}
		canvas.drawBitmap(mMoveThumb, currentX-12, currentY-15, paint);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if(pointIndex>=0&&pointIndex<=4){
			int pressX=(int) event.getX();
			int pressY=(int) event.getY();
			
			if(mOnPoistionChangedListener!=null){
				mOnPoistionChangedListener.onPositionChanged(this, pressX, pressY,pointIndex);
				}
			if(pointIndex!=4){
			pointIndex++;
			currentX=points.get(pointIndex).x;
			currentY=points.get(pointIndex).y;
			invalidate();
			}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:

			
			break;

		default:
			break;
		}
		return true;
	}
	
	public void setIndex(int index){
		pointIndex=index;
	}
	
	public static interface OnPoistionChangedListener{
		public abstract void onPositionChanged(CalibrationView obj, int x,int y,int index);

	}


}
