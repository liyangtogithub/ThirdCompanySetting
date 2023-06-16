package com.landsem.setting.view;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.utils.LightnessControl;
import com.ls.config.ConfigManager;
import com.ls.config.ConfigManager.LS_CONFIG_ID;

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

@SuppressWarnings("serial")
public class CustomSeekbar extends SeekBar implements OnSeekBarChangeListener,
		Constant {

	private static final String TAG = CustomSeekbar.class.getSimpleName();
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
	public ConfigManager mConfigManager;
	private int middle;
	private boolean nightMark;
	private boolean dayMark;
	private Drawable mDrawable;
	private TextView modeTextView;
	private Resources resources;

	public CustomSeekbar(Context context) {
		this(context, null);
	}

	public CustomSeekbar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomSeekbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		mConfigManager = SettingApp.getConfigManager();
		paint.setTextSize(22);
		resources = getResources();
		thumbCircleRadius = (int) resources.getDimension(R.dimen.circle_radius);
		progressDrawable50 = resources.getDrawable(R.drawable.seekbar_50_style);
		progressDrawableLess50 = resources
				.getDrawable(R.drawable.seekbar_20_style);
		moreThan50Paint.setColor(resources.getColor(R.color.passed_color));
		nightModeCircleBg = resources.getColor(R.color.white);
		dayModeCircleBg = resources.getColor(R.color.passed_color);
		thumbCircleProgressTextColor = resources.getColor(R.color.white);
		initLightMode(true);
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

	// @Override
	// protected synchronized void onMeasure(int widthMeasureSpec, int
	// heightMeasureSpec) {
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// int w = getWidth();
	// int h = getHeight();
	// w -= mPaddingRight + mPaddingLeft;
	// h -= mPaddingTop + mPaddingBottom;
	// progressDrawable50.setBounds(0, 0, w, h);
	// progressDrawableLess50.setBounds(0, 0, w, h);
	// int paddingTop = getPaddingTop();
	// moreThan50Rect.set(w / 2, paddingTop, w / 2, h + paddingTop);
	// }

	public void initLightMode(boolean isInit) {
		//还有根据时间判断是否切换背光模式，暂时屏蔽
		boolean isChanged = LightnessControl.isLightModeChangedByLight();
		if (isInit || isChanged) {
			middle = MAX_LIGHT_VALUE / 2;
			LS_CONFIG_ID configId = LS_CONFIG_ID.CAR_CONFIG_RESERVED5;
			int progress = 0;
			LogManager.d("initLightMode  LightnessControl.LIGHT_MODE  "+LightnessControl.LIGHT_MODE);
			switch (LightnessControl.LIGHT_MODE) {
			case DAYLIGHT_MODE:
				configId = LS_CONFIG_ID.CAR_CONFIG_RESERVED5;
				progress = Integer.parseInt(mConfigManager
						.getConfigValue(configId));
				progress = progress / ZOON_DIGIT;
				progress = progress < middle ? middle : progress;
				if (null != modeTextView)
					modeTextView.setText(R.string.day_light);
				thumbCircleProgressTextColor = Color.parseColor("#88000000");
				break;
			case NIGHTLIGHT_MODE:
				configId = LS_CONFIG_ID.CAR_CONFIG_RESERVED6;
				progress = Integer.parseInt(mConfigManager
						.getConfigValue(configId));
				progress = progress / ZOON_DIGIT;
				progress = progress > middle ? middle : progress;
				if (null != modeTextView)
					modeTextView.setText(R.string.night_light);
				thumbCircleProgressTextColor = Color.parseColor("#88000000");
				break;
			default:
				break;
			}
			LogManager.d("initLightMode  progress  "+progress+" configId"+configId);
			initProgressDrawable();
			setProgress(progress);
			if (!isInit) {
				LightnessControl.setLightness(context, progress*ZOON_DIGIT, false);
			}
		}
	}

	private void initProgressDrawable() {
		switch (LightnessControl.LIGHT_MODE) {
		case DAYLIGHT_MODE:
			if (!dayMark) {
				dayMark = true;
				nightMark = false;
				thumbCircleBgColor = dayModeCircleBg;
				mDrawable = progressDrawable50;
				setProgressDrawable(mDrawable);
			}
			break;
		case NIGHTLIGHT_MODE:
			if (!nightMark) {
				nightMark = true;
				dayMark = false;
				thumbCircleBgColor = nightModeCircleBg;
				mDrawable = progressDrawableLess50;
				setProgressDrawable(mDrawable);
			}
			break;
		default:
			break;
		}
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
		moreThan50Rect.set(w / 2, paddingTop, w / 2, h + paddingTop);
	}

	@Override
	public synchronized void setProgress(int progress) {
		switch (LightnessControl.LIGHT_MODE) {
		case DAYLIGHT_MODE:
			progress = progress < middle ? middle : progress;
			break;
		case NIGHTLIGHT_MODE:
			progress = progress > middle ? middle : progress;
			break;
		default:
			break;
		}
		super.setProgress(progress);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int progress = getProgress();
		initMeasure();
		initProgressDrawable();
		super.onDraw(canvas);
		Point thumbCircleCenter = getThumbCenterPoint();
		if (null != thumbCircleCenter) {
			// when progress more than 50%,we need draw another layer progress
			// color for more than 50% duration.
			if (progress > middle) {
				canvas.save();
				moreThan50Rect.set(moreThan50Rect.left, moreThan50Rect.top,
						thumbCircleCenter.x, moreThan50Rect.bottom);
				canvas.drawRect(moreThan50Rect, moreThan50Paint);
				canvas.restore();
			}
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

	boolean fromUser;

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		LogManager.d(TAG, "onProgressChanged      &&&&&&      fromUser : "
				+ fromUser + ", progress: " + progress + ", middle: " + middle
				+ ", BACKLIGHT_MODE: ");
		this.fromUser = fromUser;
		if (fromUser) {
			switch (LightnessControl.LIGHT_MODE) {
			case LightnessControl.DAYLIGHT_MODE:
				if (progress < middle) {
					progress = middle;
					setProgress(progress);
				}
				break;
			case LightnessControl.NIGHTLIGHT_MODE:
				if (progress > middle) {
					progress = middle;
					setProgress(progress);
				}
				break;
			default:
				break;
			}
			LightnessControl.setLightness(context, progress*ZOON_DIGIT, false);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (fromUser) {
			SettingApp.getInstance().saveLightValue(seekBar.getProgress()*ZOON_DIGIT);
			fromUser = false;
		}
	}

	public void onModeChanged() {

		initLightMode(false);
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
}
