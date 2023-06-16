package com.landsem.setting.view;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.utils.LightnessControl;
import com.ls.sensor.CarSensorManager;
import com.ls.sensor.CarSensorManager.EnableStatus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ParkMonitorSeekbar extends SeekBar implements
		OnSeekBarChangeListener, Constant {

	private static final long serialVersionUID = -3309306219680215461L;
	private static final String TAG = ParkMonitorSeekbar.class.getSimpleName();
	private static final int CHANGE_SUCCESS = 0x00;
	private Rect moreThan50Rect = new Rect();
	private Point point = new Point();
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint moreThan50Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int thumbCircleRadius = 20;
	private int thumbCircleBgColor;
	private int thumbCircleProgressTextColor = Color.parseColor("#88000000");
	private Drawable progressDrawableLess50;
	private Drawable progressDrawable50;
	private int nightModeCircleBg;
	private int dayModeCircleBg;
	private Context context;
	private int middle;
	private TextView modeTextView;
	private Resources resources;
	private CarSensorManager mCarSensorManager;
	private boolean sensorAvailable;
	private int sensitiveValue;
	private static final int maxValue = 30;

	public ParkMonitorSeekbar(Context context) {
		this(context, null);
	}

	public ParkMonitorSeekbar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ParkMonitorSeekbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		mCarSensorManager = new CarSensorManager();
		setMax(maxValue);
		paint.setTextSize(22);
		resources = getResources();
		thumbCircleRadius = (int) resources.getDimension(R.dimen.circle_radius);
		progressDrawable50 = resources.getDrawable(R.drawable.seekbar_50_style);
		progressDrawableLess50 = resources
				.getDrawable(R.drawable.seekbar_20_style);
		moreThan50Paint.setColor(resources.getColor(R.color.passed_color));
		thumbCircleBgColor = resources.getColor(R.color.passed_color);
		thumbCircleProgressTextColor = Color.parseColor("#88000000");
		// setProgressDrawable(progressDrawable50);
		initProgress();
		setOnSeekBarChangeListener(this);
	}

	public void setModeTextView(TextView modeTextView) {
		this.modeTextView = modeTextView;
	}

	private Point getThumbCenterPoint() {
		int maxProgress = getMax();
		int height = getHeight();
		int progress = getProgress();
		if (maxProgress != 0) {
			point.x = thumbCircleRadius
					+ (progress * (getWidth() - 2 * thumbCircleRadius) / maxProgress);
			point.y = height / 2;
		}
		return point;
	}

	private void initMeasure() {
		int w = getWidth();
		int h = getHeight();
		int mPaddingRight = getPaddingLeft();
		int mPaddingLeft = getPaddingLeft();
		int mPaddingTop = getPaddingTop();
		int mPaddingBottom = getPaddingBottom();
		w -= mPaddingRight + mPaddingLeft;
		h -= mPaddingTop + mPaddingBottom;
		progressDrawable50.setBounds(0, 0, w, h);
		progressDrawableLess50.setBounds(0, 0, w, h);
		int paddingTop = getPaddingTop();
		moreThan50Rect.set(0, paddingTop, w, h + paddingTop);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int progress = getProgress();
		initMeasure();
		super.onDraw(canvas);
		Point thumbCircleCenter = getThumbCenterPoint();
		if (null != thumbCircleCenter) {
			// when progress more than 50%,we need draw another layer progress
			// color for more than 50% duration.
			canvas.save();
			moreThan50Rect.set(moreThan50Rect.left, moreThan50Rect.top,
					thumbCircleCenter.x, moreThan50Rect.bottom);
			canvas.drawRect(moreThan50Rect, moreThan50Paint);
			canvas.restore();
			// draw thumb
			paint.setColor(thumbCircleBgColor);
			canvas.drawCircle(thumbCircleCenter.x, thumbCircleCenter.y,
					thumbCircleRadius, paint);
			canvas.save();
			// draw text
			paint.setColor(thumbCircleProgressTextColor);
			String progressText = Integer.toString(progress);
			FontMetrics fm = paint.getFontMetrics();
			canvas.drawText(progressText,
					thumbCircleCenter.x - paint.measureText(progressText) / 2,
					(float) (thumbCircleCenter.y + (Math.abs(fm.top) - (Math
							.abs(fm.bottom))) / 2), paint);
			canvas.restore();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

		int progress = seekBar.getProgress();
		boolean isValidValue = progress != 0;
		if (isValidValue) {
			int sensorValue = maxValue - progress;
			sensorValue = sensorValue == 0 ? 1 : sensorValue;
			updateSensorValue(sensorValue);
		}
		boolean currAvailable = isSensorAvailable();
		if (isValidValue ^ currAvailable) {
			LogManager.d("luohong", "isValidValue: " + isValidValue
					+ ", currAvailable: " + currAvailable);
			changeSensorAvailable(isValidValue);
		}
	}

	public void trimmingLight(boolean increase) {
		int progress = getProgress();
		int step = increase ? 1 : -1;
		progress += step;
		setProgress(progress);
		progress = getProgress();
		LightnessControl.setLightness(context, progress*ZOON_DIGIT, false);
		SettingApp.getInstance().saveLightValue(progress*ZOON_DIGIT);
	}

	private void initProgress() {
		sensitiveValue = 30;
		if (isSensorAvailable()) {
			sensitiveValue = getSensorValue();
		}
		setProgress(maxValue - sensitiveValue);
	}

	private boolean isSensorAvailable() {
		try {
			EnableStatus status = mCarSensorManager.gSensorGetTapStatus();
			sensorAvailable = status == EnableStatus.STATUS_ON;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensorAvailable;
	}

	private int getSensorValue() {
		try {
			sensitiveValue = mCarSensorManager.gSensorGetTapThreshold();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveValue;
	}

	private boolean changeSensorAvailable(boolean futureStatus) {
		EnableStatus status = futureStatus ? EnableStatus.STATUS_ON
				: EnableStatus.STATUS_OFF;
		int changeResult = mCarSensorManager.gSensorSetTapStatus(status);// disable
																			// gsensor
																			// tap
																			// interrupt.
		LogManager.d(TAG, "updateSensorValue status: " + status
				+ ", changeResult: " + changeResult);
		return CHANGE_SUCCESS == changeResult;
	}

	private boolean updateSensorValue(int value) {
		int updateResult = mCarSensorManager.gSensorSetTapThreshold(value);
		LogManager.d(TAG, "updateSensorValue value: " + value
				+ ", updateResult: " + updateResult);
		return CHANGE_SUCCESS == updateResult;
	}

	// private int getSensorValue(){
	// try {
	// sensitiveValue = mCarSensorManager.gSensorGetTapThreshold();
	// if(!isSensorAvailable()){
	// if(sensitiveValue!=maxValue){
	// mCarSensorManager.gSensorSetTapThreshold(maxValue);
	// sensitiveValue = mCarSensorManager.gSensorGetTapThreshold();
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return sensitiveValue;
	// }

}
