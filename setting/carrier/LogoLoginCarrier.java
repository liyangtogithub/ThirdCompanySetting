package com.landsem.setting.carrier;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.Constant.Action;
import com.landsem.setting.activities.CalibLayout;
import com.landsem.setting.activities.CarLogoSelect;
import com.landsem.setting.activities.CvbcLayout;
import com.landsem.setting.activities.LightStudyActivity;
import com.landsem.setting.activities.ToneSetActivity;

public class LogoLoginCarrier  extends BaseCarrier{
	
	private static final String TAG = LogoLoginCarrier.class.getSimpleName();
	private static final String EXPECTED_PWD = "3368";
	private static final int EMPTY = 0x00;
	private static final int ONE = 0x01;
	private static final int TWO = 0x02;
	private static final int THREE = 0x03;
	private static final int FOUR = 0x04;
	private StringBuffer pwdBuffer = new StringBuffer();
	private TextView inputHint;
	private ImageView password1;
	private ImageView password2;
	private ImageView password3;
	private ImageView password4;
	private Button buttonCancel;
	private Button buttonDelete;
	private Button button0;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private Button button7;
	private Button button8;
	private Button button9;
	private String reload;
	Context mContext;
	private byte activityType = PasswordSelect.CAR_ICON;
	
	
	public LogoLoginCarrier(Context context,LayoutInflater inflater, int resource) {
		super(inflater, resource);
		mContext = context;
		initViews(contentView);
		initListener();
		reload = context.getResources().getString(R.string.logo_pwd_reload);
	}

	public void setActivityType(byte activityType) {
		this.activityType = activityType;
	}


	@Override
	protected void initViews(View convertView) {
		button0 = (Button) convertView.findViewById(R.id.key0);
		button1 = (Button) convertView.findViewById(R.id.key1);
		button2 = (Button) convertView.findViewById(R.id.key2);
		button3 = (Button) convertView.findViewById(R.id.key3);
		button4 = (Button) convertView.findViewById(R.id.key4);
		button5 = (Button) convertView.findViewById(R.id.key5);
		button6 = (Button) convertView.findViewById(R.id.key6);
		button7 = (Button) convertView.findViewById(R.id.key7);
		button8 = (Button) convertView.findViewById(R.id.key8);
		button9 = (Button) convertView.findViewById(R.id.key9);
		buttonCancel = (Button) convertView.findViewById(R.id.keycancel);
		buttonDelete = (Button) convertView.findViewById(R.id.keydelete);
		password1 = (ImageView) convertView.findViewById(R.id.password1);
		password2 = (ImageView) convertView.findViewById(R.id.password2);
		password3 = (ImageView) convertView.findViewById(R.id.password3);
		password4 = (ImageView) convertView.findViewById(R.id.password4);
		inputHint = (TextView) convertView.findViewById(R.id.input_hint);
	}
	
	@Override
	protected void initListener() {
		button0.setOnClickListener(this);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		button7.setOnClickListener(this);
		button8.setOnClickListener(this);
		button9.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);
		buttonDelete.setOnClickListener(this);
	}
	
	@Override
	protected void initViewsState() {
	}
	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.keycancel:
			cancelPwd();
			break;
		case R.id.keydelete:
			deletePwd();
			break;

		default:
			inputPassword(view);
			break;
		}
	}
	
	private void inputPassword(View passwordKey){
		Object obj = passwordKey.getTag();
		if(null!=obj){
			String password = (String) obj;
			LogManager.d(TAG, "password : "+password);
			if(!StringUtils.isEmpty(password)){
				pwdBuffer.append(password);
				updatePassword();
			}
		}
	}
	
	private void cancelPwd(){
		if(pwdBuffer.length()!=EMPTY){
			cleanBuffer();
			updatePassword();
		}
	}
	
	private void deletePwd(){
		if(pwdBuffer.length()!=EMPTY){
			pwdBuffer.deleteCharAt(pwdBuffer.length()-1);
			updatePassword();
		}
	}
	
	private void updatePassword(){
		LogManager.d(TAG, pwdBuffer.toString());
		switch (pwdBuffer.length()) {
		case EMPTY:
			password1.setImageResource(R.drawable.indicator_normal);
			password2.setImageResource(R.drawable.indicator_normal);
			password3.setImageResource(R.drawable.indicator_normal);
			password4.setImageResource(R.drawable.indicator_normal);
			break;
		case ONE:
			password1.setImageResource(R.drawable.indicator_select);
			password2.setImageResource(R.drawable.indicator_normal);
			password3.setImageResource(R.drawable.indicator_normal);
			password4.setImageResource(R.drawable.indicator_normal);
			break;
		case TWO:
			password1.setImageResource(R.drawable.indicator_select);
			password2.setImageResource(R.drawable.indicator_select);
			password3.setImageResource(R.drawable.indicator_normal);
			password4.setImageResource(R.drawable.indicator_normal);
			break;
		case THREE:
			password1.setImageResource(R.drawable.indicator_select);
			password2.setImageResource(R.drawable.indicator_select);
			password3.setImageResource(R.drawable.indicator_select);
			password4.setImageResource(R.drawable.indicator_normal);
			break;
		case FOUR:
			password1.setImageResource(R.drawable.indicator_select);
			password2.setImageResource(R.drawable.indicator_select);
			password3.setImageResource(R.drawable.indicator_select);
			password4.setImageResource(R.drawable.indicator_select);
			break;

		default:
			break;
		}
		LogManager.d(TAG, "pwdBuffer.length() : "+pwdBuffer.length());
		if(pwdBuffer.length()==FOUR){
			String actualPwd = pwdBuffer.toString();
			if(StringUtils.isEquals(actualPwd, EXPECTED_PWD)){
				switch (activityType) {
				case PasswordSelect.CAR_ICON:
					Intent intent = new Intent(mContext, CarLogoSelect.class);
					mContext.startActivityAsUser(intent, UserHandle.CURRENT_OR_SELF);
					break;
                case PasswordSelect.LIGHT_STUDY:
                	Intent intentLight = new Intent(mContext, LightStudyActivity.class);
					mContext.startActivityAsUser(intentLight, UserHandle.CURRENT_OR_SELF);
					break;
                case PasswordSelect.SCREEN_CALIBRATION:
                	new CalibLayout(SettingApp.getInstance()).showDisplay();
					break;
                case PasswordSelect.TONE_SET:
                	Intent intentToneSet = new Intent(mContext, ToneSetActivity.class);
					mContext.startActivityAsUser(intentToneSet, UserHandle.CURRENT_OR_SELF);
					break;
                case PasswordSelect.CVBS_SET:
                	new CvbcLayout(SettingApp.getInstance()).showDisplay();
					break;
                case PasswordSelect.CAR_SET:
                	//mContext.sendBroadcast(new Intent(Action.CAR_SET_CORRECT));把can0界面做到设置里的
                	 Intent intentCanSet = new Intent();
                	 intentCanSet.setAction("com.landsem.canboxui.SetActivity"); 
                     if (mContext.getPackageManager().resolveActivity(intentCanSet,   
                             PackageManager.MATCH_DEFAULT_ONLY) != null) {   
                    	 mContext.startActivityAsUser(intentCanSet, UserHandle.CURRENT_OR_SELF);
            		 }
					break;
				}
				
				pwdBuffer.delete(0, pwdBuffer.length()-1);
//				this.finish();
//				ActivityAnimator.unzoomBackAnimation(this);
				cleanBuffer();
				updatePassword();
				inputHint.setText(mContext.getString(R.string.logo_pwd_load));
				return;
			}
			cleanBuffer();
			updatePassword();
			if(!inputHint.getText().equals(reload)) inputHint.setText(reload);
		}
	}
	
	
	private void cleanBuffer(){
		if(null!=pwdBuffer && pwdBuffer.length()!=EMPTY){
			pwdBuffer.delete(0, pwdBuffer.length()-1);
			pwdBuffer.deleteCharAt(0);
		}
	}

}
