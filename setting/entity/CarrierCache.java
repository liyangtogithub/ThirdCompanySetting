package com.landsem.setting.entity;

import com.landsem.setting.R;
import com.landsem.setting.carrier.BackLightCarrier;
import com.landsem.setting.carrier.BluetoothCarrier;
import com.landsem.setting.carrier.FactoryCarrier;
import com.landsem.setting.carrier.LanguageCarrier;
import com.landsem.setting.carrier.LockScreenCarrier;
import com.landsem.setting.carrier.StandbyCarrier;
import com.landsem.setting.carrier.TimeFormatCarrier;
import com.landsem.setting.carrier.UpGradeCarrier;
import com.landsem.setting.carrier.WifiConnectCarrier;

import android.app.Activity;
import android.view.LayoutInflater;


public class CarrierCache {
	
	private static CarrierCache instance;
	public WifiConnectCarrier connectCarrier;
	public BluetoothCarrier bluetoothCarrier;
	public TimeFormatCarrier timeCarrier;
	public BackLightCarrier lightCarrier;
	public LanguageCarrier languageCarrier;
	public StandbyCarrier standbyCarrier;
	public UpGradeCarrier upGradeCarrier;
	public FactoryCarrier factoryCarrier;
	public LockScreenCarrier lockScreenCarrier;
	
	private CarrierCache(){
		
	}
	
	public static CarrierCache getInstance() {
		if(null==instance) {
			synchronized(CarrierCache.class) {
				if(null==instance) {
					instance = new CarrierCache();
				}
			}
		}
		return instance;
	}
	
	
	public WifiConnectCarrier getConnectCarrier(Activity a, LayoutInflater inflater, int resource){
		if(null==connectCarrier) connectCarrier = new WifiConnectCarrier(a, inflater, R.layout.wificonnect_carrier_layout);
		return connectCarrier;
	}
	
	
	public BluetoothCarrier getBluetoothCarrier(LayoutInflater inflater, int resource){
		if(null==bluetoothCarrier) bluetoothCarrier = new BluetoothCarrier(inflater, resource);
		return bluetoothCarrier;
	}

}
