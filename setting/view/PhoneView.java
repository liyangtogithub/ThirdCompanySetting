package com.landsem.setting.view;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.Type;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhoneView extends LinearLayout implements OnClickListener, OnLongClickListener{

	private static final String TAG = PhoneView.class.getSimpleName();
	public static final String QINQING_NUM = "qinqing_num";
	public static final String QINQING_NAME = "qinqing_name";
	public static final String DAOHANG_NUM = "daohang_num";
	public static final String DAOHANG_NAME = "daohang_name";
	public static final String BAOXIAN_NUM = "baoxian_num";
	public static final String BAOXIAN_NAME = "baoxian_name";
	public static final String BAOJING_NUM = "baojing_num";
	public static final String BAOJING_NAME = "baojing_name";
	private int type;
	private String phoneKey;
	private String nameKey;
	private TextView contactsType;
	private TextView contactsName;
	private ImageView contactsAppend;
	
	public PhoneView(Context context) {
		this(context, null);
	}
	
	public PhoneView(Context context, AttributeSet attrs) {
		this(context, attrs, 1);
	}
	
	public PhoneView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray aypedArray = context.obtainStyledAttributes(attrs, R.styleable.landsem, defStyle, 0);
		type = aypedArray.getInteger(R.styleable.landsem_type, 0);
		inflate(context, R.layout.phone_view_layout, this);
		initViews();
		initListener();
		initViewState();
	}
	
	public void initViewState(){
		String phone = "";
		String name = "";
		String appendText = "";
		if(type==Type.kinship_phone){
			phoneKey = QINQING_NUM;
			nameKey = QINQING_NAME;
			appendText = getContext().getResources().getString(R.string.family_phone);
		}else if(type==Type.navi_phone){
			phoneKey = DAOHANG_NUM;
			nameKey = DAOHANG_NAME;
			appendText = getContext().getResources().getString(R.string.navi_phone);
		}else if(type==Type.insurance_phone){
			phoneKey = BAOXIAN_NUM;
			nameKey = BAOXIAN_NAME;
			appendText = getContext().getResources().getString(R.string.insurance_phone);
		}else if(type==Type.alarn_phone){
			phoneKey = BAOJING_NUM;
			nameKey = BAOJING_NAME;
			appendText = getContext().getResources().getString(R.string.police_phone);
		}
		phone = SettingApp.getSystemString(phoneKey, "");
		name = SettingApp.getSystemString(nameKey, "");
		contactsType.setText(appendText);
		if(StringUtils.isEmpty(phone)){
			contactsAppend.setVisibility(View.VISIBLE);
			contactsName.setVisibility(View.INVISIBLE);
		}else{
			contactsAppend.setVisibility(View.INVISIBLE);
			contactsName.setVisibility(View.VISIBLE);
			contactsName.setText(StringUtils.isEmpty(name)?phone:name);
		}
	}
	
	private void initViews(){
		contactsType = (TextView) findViewById(R.id.contacts_type);
		contactsName = (TextView) findViewById(R.id.contacts_name);
		contactsAppend = (ImageView) findViewById(R.id.contacts_append);
	}

	
	private void initListener() {
		contactsAppend.setOnClickListener(this);
		contactsName.setOnClickListener(this);
		contactsName.setOnLongClickListener(this);
	}
	
	
	@Override
	public void onClick(View view) {
		try {
			LogManager.d(TAG, "222222222  +++++++++++++++++++++++");
			Intent intent = new Intent("android.intent.action.BLUETOOTHPHONE");
	    	intent.setDataAndType(Uri.EMPTY, "bluetooth.phone.dir/phonebook");
	    	intent.putExtra("withtabs", true);
	    	intent.putExtra("phoneType", type);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        getContext().startActivity(intent); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onLongClick(View view) {
		SettingApp.putSystemString(phoneKey, "");
		SettingApp.putSystemString(nameKey, "");
		initViewState();
		return true;
	}

	

}
