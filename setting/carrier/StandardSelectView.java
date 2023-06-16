package com.landsem.setting.carrier;

import java.util.ArrayList;
import java.util.List;

import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.adapter.SingalAdapter;
import com.landsem.setting.entity.SpinnerOption;
import com.landsem.setting.utils.APNUpdate;
import com.landsem.setting.utils.SignalParser;
import com.ls.config.ConfigManager.LS_CONFIG_ID;
import com.ls.lseasycontrol.LSEasyControlManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StandardSelectView extends RelativeLayout implements OnItemClickListener, Constant{

	private Context context;
	
	private String selectSingal;
	private APNUpdate mAPNUpdate;
	private TextView singalNameView;
	private ListView singalListView;
	private SingalAdapter mSingalAdapter;
	private List<SpinnerOption> spinnerOptions = new ArrayList<SpinnerOption>();
	private static final int AUTOMODE = 0;
	private static final int MANUALMODE = 1;
	//(倒车摄像头)
	int LstvinArray[] = {0,1,2,3};
	//参数system为对应的制式，从如下变量中选择:
	public static final int SYSTEM_N = 0;//(N制)
	public static final int SYSTEM_P = 1;//(P制)
	private static final String AUTO_LIB_STRING = "auto";
	private static final String N_LIB_STRING = "N";
	private static final String PAL_LIB_STRING = "PAL";
	private LSEasyControlManager mLSEasyControlManager;
	
	public StandardSelectView(Context context) {
		super(context);
		infiateView(context);
	}

	public StandardSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		infiateView(context);
	}

	public StandardSelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		infiateView(context);
	}
	
	private void infiateView(Context context){
		this.context = context;
		mLSEasyControlManager = new LSEasyControlManager(context);
		inflate(context, R.layout.standard_carrier_layout, this);
		mSingalAdapter = new SingalAdapter(context, spinnerOptions);
		initViews();
		singalListView.setAdapter(mSingalAdapter);
		singalListView.setOnItemClickListener(this);
		initViewsData();
	}
	
	private void initViewsData() {
		NaviLoadTask mNaviLoadTask = new NaviLoadTask();
		mNaviLoadTask.execute();
	}

	private void initViews(){
		singalNameView = (TextView) findViewById(R.id.standard_auto_name);
		singalListView = (ListView) findViewById(R.id.standard_listview);
	}
	
	private final class NaviLoadTask extends AsyncTask<Object, Integer, List<SpinnerOption>>{

		@Override
		protected List<SpinnerOption> doInBackground(Object... arg0) {
			
			return initNaviData();
		}
		
		@Override
		protected void onPostExecute(List<SpinnerOption> result) {
			if(!StringUtils.isBlank(selectSingal)) singalNameView.setText(selectSingal);
			spinnerOptions = result;
			mSingalAdapter.setList(spinnerOptions);
		}
		
		private List<SpinnerOption> initNaviData() {
			List<SpinnerOption> infoList = parserSetting();
			String defSingal = getCvbsModeLib();
			if (!StringUtils.isEmpty(defSingal)) {
				for (int location = 0; location < infoList.size(); location++) {
					SpinnerOption mSpinnerOption = infoList.get(location);
					boolean currSelect = null!=mSpinnerOption && StringUtils.isEquals(mSpinnerOption.libName, defSingal);
					mSpinnerOption.select = currSelect;
					if(currSelect) selectSingal = mSpinnerOption.lable;
				}
			}
			return infoList;
		}

		private String getCvbsModeLib() {
			String libString = AUTO_LIB_STRING;
			int mode = mLSEasyControlManager.Get_Cvbs_System_Mode();
			int system = mLSEasyControlManager.Get_Cvbs_System(LstvinArray[0]);
			if (AUTOMODE == mode) {
				libString = AUTO_LIB_STRING;
			}else if (SYSTEM_N == system) {
				libString = N_LIB_STRING;
			}else if (SYSTEM_P == system) {
				libString = PAL_LIB_STRING;
			}
			LogManager.d("888888 libString "+libString);
			return libString;
		}

		private List<SpinnerOption> parserSetting() {
			List<SpinnerOption> signalList = new ArrayList<SpinnerOption>();
			String[] nameArray = getResources().getStringArray(R.array.standardArray);
			String[] libArray = getResources().getStringArray(R.array.standardLibArray);
			for (int i = 0; i < nameArray.length; i++) {
				SpinnerOption item = new SpinnerOption(nameArray[i], libArray[i]);
				signalList.add(item);
			}
			return signalList;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if(mSingalAdapter.isLocationValid(position)){
			SpinnerOption clickItem = mSingalAdapter.getItem(position);
			if(null!=clickItem){
				for(int location=0;location<mSingalAdapter.getCount();location++){
					SpinnerOption newItem = mSingalAdapter.getItem(location);
					boolean mark = clickItem==newItem;
					newItem.select = mark;
					if(mark){
						setCvbs(clickItem.libName);
						selectSingal = clickItem.lable;
						singalNameView.setText(selectSingal);
					}
				}
			}
			mSingalAdapter.notifyDataSetChanged();
		}
		
	}

	private void setCvbs(String libName) {
		LogManager.d("8888888888  libName "+libName);
		if (AUTO_LIB_STRING.equals(libName)) {
			mLSEasyControlManager.Set_Cvbs_System_Mode(AUTOMODE);
			sendSingalToOther(AUTO_LIB_STRING);
		}else if(N_LIB_STRING.equals(libName)){
			mLSEasyControlManager.Set_Cvbs_System_Mode(MANUALMODE);
			for (int i = 0; i < LstvinArray.length; i++) {
				mLSEasyControlManager.Set_Cvbs_System(LstvinArray[i], SYSTEM_N);
			}
			sendSingalToOther(N_LIB_STRING);
		}else if(PAL_LIB_STRING.equals(libName)){
			mLSEasyControlManager.Set_Cvbs_System_Mode(MANUALMODE);
			for (int i = 0; i < LstvinArray.length; i++) {
				mLSEasyControlManager.Set_Cvbs_System(LstvinArray[i], SYSTEM_P);
			}
			sendSingalToOther(PAL_LIB_STRING);
		}
		
	}

	private void sendSingalToOther(String value) {
		Intent mIntent = new Intent("com.cvbs.mode");
		mIntent.putExtra("mode", value);
		context.sendBroadcast(mIntent);
	}
	


}
