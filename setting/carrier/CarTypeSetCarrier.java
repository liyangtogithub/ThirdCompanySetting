package com.landsem.setting.carrier;

import java.util.ArrayList;
import java.util.List;
import com.landsem.setting.R;
import com.landsem.setting.adapter.CarTypeAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class CarTypeSetCarrier  extends BaseCarrier implements OnItemClickListener{
	
	Context mContext;
	private ListView carTitleListView;
	private ListView carChoiceListView;
	private ListView carCanListView;
	private CarTypeAdapter mCarTitleAdapter;
	private CarTypeAdapter mCarChoiceAdapter;
	private CarTypeAdapter mCarCanAdapter;
	TextView cartitleTextView , carChoiceTextView , carCanTextView;  
	int carTitlePosition = 0;
	int carChoicePosition = 0;
	
	/*** 车系（第一）列表 */
	private List<String> carTitleList = new ArrayList<String>();
	/*** 车型选择（第二）列表 */
	private List<String> carChoiceList = new ArrayList<String>();
	/*** 协议盒（第三）列表 */
	private List<String> carCanList = new ArrayList<String>();
	/*** 车型选择（第二）列表是用的哪个数组内容 */
	int carTitleChildArray[] = null;
	
	
	public CarTypeSetCarrier(Context context,LayoutInflater inflater, int resource) {
		super(inflater, resource);
		mContext = context;
		initViews(contentView);
		initListener();
		initViewsState();
	}



	@Override
	protected void initViews(View convertView) {
		carTitleListView = (ListView) convertView.findViewById(R.id.car_title_list);
		carChoiceListView = (ListView) convertView.findViewById(R.id.car_choice_list);
		carCanListView = (ListView) convertView.findViewById(R.id.car_can_list);
		cartitleTextView = (TextView) convertView.findViewById(R.id.tv_car_title);
		carChoiceTextView = (TextView) convertView.findViewById(R.id.tv_car_choice);
		carCanTextView = (TextView) convertView.findViewById(R.id.tv_car_can);
	}
	
	@Override
	protected void initListener() {
		carTitleListView.setOnItemClickListener(this);
		carChoiceListView.setOnItemClickListener(this);
		carCanListView.setOnItemClickListener(this);
	}
	
	@Override
	protected void initViewsState() {
		mCarTitleAdapter = new CarTypeAdapter(mContext, carTitleList);
		mCarChoiceAdapter = new CarTypeAdapter(mContext, carChoiceList);
		mCarCanAdapter = new CarTypeAdapter(mContext,carCanList);
		carTitleListView.setAdapter(mCarTitleAdapter);
		carChoiceListView.setAdapter(mCarChoiceAdapter);
		carCanListView.setAdapter(mCarCanAdapter);
		initCarTitleData();
		carTitlePosition = 0;
		mCarTitleAdapter.setCurrentPosition(0);
		initCarTitleChildArray();
		initCarChoiceData(0);
		carChoicePosition = 6;
		mCarChoiceAdapter.setCurrentPosition(6);
		initCarCanData();
		//mCarCanAdapter.setCurrentPosition(0);
		initTextView(0,6,CarTypeAdapter.INVALID_CURRENT_POSITION);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		if (parent.getId() == carTitleListView.getId()) {
			if(mCarTitleAdapter.isLocationValid(position)){
				carTitlePosition = position;
				carChoicePosition = CarTypeAdapter.INVALID_CURRENT_POSITION;
				updateCarTitleList(position);
				initCarChoiceData(position);
				mCarChoiceAdapter.setCurrentPosition(CarTypeAdapter.INVALID_CURRENT_POSITION);
				initCarCanData();
				initTextView(position, CarTypeAdapter.INVALID_CURRENT_POSITION, CarTypeAdapter.INVALID_CURRENT_POSITION);
			}
		}
		if (parent.getId() == carChoiceListView.getId()) {
			if(mCarChoiceAdapter.isLocationValid(position)){
				carChoicePosition = position;
				updateCarChoiceList(position);
				initTextView(CarTypeAdapter.INVALID_CURRENT_POSITION, position, CarTypeAdapter.INVALID_CURRENT_POSITION);
				initCarCanData();
				mCarCanAdapter.setCurrentPosition(CarTypeAdapter.INVALID_CURRENT_POSITION);
			}
		}
		if (parent.getId() == carCanListView.getId()) {
			updateCarCanList(position);
			carCanTextView.setText(carCanList.get(position));
			//TODO qie huan xie yi 
			changeProtocol();
		}
	}
	
	private void updateCarCanList(int position) {
		mCarCanAdapter.setCurrentPosition( position);
		mCarCanAdapter.notifyDataSetChanged();
	}

	private void updateCarChoiceList(int position) {
		mCarChoiceAdapter.setCurrentPosition( position);
		mCarChoiceAdapter.notifyDataSetChanged();
	}

	private void updateCarTitleList(int position) {
		mCarTitleAdapter.setCurrentPosition(position);
		mCarTitleAdapter.notifyDataSetChanged();
	}



	private void initTextView(int titleIndex, int choiceIndex, int canIndex) {
		if (titleIndex!=CarTypeAdapter.INVALID_CURRENT_POSITION) {
			cartitleTextView.setText(carTitleList.get(titleIndex)); 
		}
		if (choiceIndex!=CarTypeAdapter.INVALID_CURRENT_POSITION) {
			carChoiceTextView.setText(carChoiceList.get(choiceIndex)); 
		}else {
			carChoiceTextView.setText(""); 
		}
		if (canIndex!=CarTypeAdapter.INVALID_CURRENT_POSITION) {
			carCanTextView.setText(carCanList.get(canIndex));
		}else {
			carCanTextView.setText(""); 
		}
	}
	
	/***点击第一个列表后，第二个列表要加载的内容 */
	private void initCarTitleChildArray() {
		carTitleChildArray = new int[]{R.array.gmArray,R.array.dasautoArray,R.array.toyotaArray,
				R.array.psaArray,R.array.fordArray,R.array.hondaArray,R.array.hyundaiArray
				,R.array.nissanArray};
	}
	
	/***初始化第一个列表内容 */
	private void initCarTitleData() {
		String carTitleValueArray[] = mContext.getResources().getStringArray(R.array.carTitleValueArray);
		for (int i = 0; i < carTitleValueArray.length; i++) {
			carTitleList.add(carTitleValueArray[i]);
		}
		mCarTitleAdapter.setList(carTitleList);
	}
	
	/***初始化第二个列表内容 */
	private void initCarChoiceData(int carChoiceIndex) {
		String carTitleValueArray[] = mContext.getResources().getStringArray(carTitleChildArray[carChoiceIndex]);		
		carChoiceList.clear();
		for (int i = 0; i < carTitleValueArray.length; i++) {
	    	carChoiceList.add(carTitleValueArray[i]);
	    }
	    mCarChoiceAdapter.setList(carChoiceList);
	}
	
	/***初始化第三个列表内容 */
	private void initCarCanData() {
		String carCanValueArray[] =   switchCarCanListArray(); 
		carCanList.clear();
		for (int i = 0; i < carCanValueArray.length; i++) {
			carCanList.add(carCanValueArray[i]);
		}
		mCarCanAdapter.setList(carCanList);
	}
	
	/***根据前两个列表的Position决定第三个列表显示哪个数组内容 */
	private String[] switchCarCanListArray() {
		String carCanValueArray[] = {};
		switch (carTitlePosition) {
		case 0:
			if (carChoicePosition!=1) {
				carCanValueArray = mContext.getResources().getStringArray(R.array.carCanValueArray);
			}
			break;
		case 1:
			carCanValueArray = mContext.getResources().getStringArray(R.array.carCanValueArray);
			break;

		}
		return carCanValueArray;
	}
	/***切换协议 */
	private void changeProtocol() {
		switch (carTitlePosition) {
		case 0:
			if (carChoicePosition!=1) {
//				SerialManager.getInstance().onCutDataProtocol(ProtocolID.ID_GM);
//				ProtocoChoicelUtils.putProtocoName(ProtocolID.ID_GM);
			}
			break;
		case 1:
//			if (carChoicePosition==2||carChoicePosition==5) {
//				SerialManager.getInstance().onCutDataProtocol(ProtocolID.ID_KODIAK);
//				ProtocoChoicelUtils.putProtocoName(ProtocolID.ID_KODIAK);
//			}else {
//				SerialManager.getInstance().onCutDataProtocol(ProtocolID.ID_DASAUTO);
//				ProtocoChoicelUtils.putProtocoName(ProtocolID.ID_DASAUTO);
//			}
			break;

		}
	}




	@Override
	public void onClick(View arg0) {
	}
	
}
