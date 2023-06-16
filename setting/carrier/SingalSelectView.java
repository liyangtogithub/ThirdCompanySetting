package com.landsem.setting.carrier;

import java.util.ArrayList;
import java.util.List;

import com.landsem.common.tools.StringUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.adapter.SingalAdapter;
import com.landsem.setting.entity.SpinnerOption;
import com.landsem.setting.utils.APNUpdate;
import com.landsem.setting.utils.SignalParser;
import com.ls.config.ConfigManager.LS_CONFIG_ID;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SingalSelectView extends RelativeLayout implements OnItemClickListener, Constant{

	private Context context;
	
	private String selectSingal;
	private APNUpdate mAPNUpdate;
	private TextView singalNameView;
	private ListView singalListView;
	private SingalAdapter mSingalAdapter;
	private PackageManager packageManager;
	private List<SpinnerOption> spinnerOptions = new ArrayList<SpinnerOption>();
	
	public SingalSelectView(Context context) {
		super(context);
		infiateView(context);
	}

	public SingalSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		infiateView(context);
	}

	public SingalSelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		infiateView(context);
	}
	
	private void infiateView(Context context){
		this.context = context;
		inflate(context, R.layout.singal_select_layout, this);
		mSingalAdapter = new SingalAdapter(context, spinnerOptions);
		packageManager = context.getPackageManager();
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
		singalNameView = (TextView) findViewById(R.id.singal_name);
		singalListView = (ListView) findViewById(R.id.singal_listview);
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
			List<SpinnerOption> infoList = SignalParser.parserSetting();
			String defSingal = SettingApp.getConfigManager().getConfigValue(LS_CONFIG_ID.IMIRROR_CONFIG_RESERVED4);
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
						SettingApp.getConfigManager().setConfigValue(LS_CONFIG_ID.IMIRROR_CONFIG_RESERVED4, clickItem.libName);
						SettingApp.getConfigManager().configFlush();
						if(null==mAPNUpdate) mAPNUpdate = new APNUpdate(context);
						boolean changeAPNResult = mAPNUpdate.changeAPN(clickItem.libName);
						selectSingal = clickItem.lable;
						singalNameView.setText(selectSingal);
					}
				}
			}
			mSingalAdapter.notifyDataSetChanged();
		}
		
	}
	


}
