package com.landsem.setting.entity;

import com.landsem.common.tools.ArrayUtils;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.SettingApp;
import com.ls.config.ConfigManager;
import com.ls.config.ConfigManager.LS_CONFIG_ID;

public class ConfigValue implements Constant{
	
	public static boolean voiceState;
	public static boolean assistiveState;
	public static int powerOffDelay;
	public static int standbyDuration;

	
	public static void initDefConfigValue(){
		ConfigManager manager = SettingApp.getConfigManager();
		String switchStates = manager.getConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED19);
		String standby = manager.getConfigValue(LS_CONFIG_ID.CAR_CONFIG_RESERVED20);
		String powerOff = manager.getConfigValue(LS_CONFIG_ID.IMIRROR_CONFIG_RESERVED9);
		parserSwitchState(switchStates);
		standbyDuration = !StringUtils.isBlank(standby)?Integer.parseInt(standby):2;
		powerOffDelay = !StringUtils.isBlank(standby)?Integer.parseInt(powerOff):0;
	}
	
	private static void parserSwitchState(String switchStates){
		if(!StringUtils.isBlank(switchStates) && switchStates.contains(COMMA)){
			String[] states = switchStates.split(COMMA);
			if(!ArrayUtils.isEmpty(states) && states.length==2){
				voiceState = states[0].equals(STATE_ON+"");
				assistiveState = states[1].equals(STATE_ON+"");
			}
		}
	}
	
//	private static void parserSpinnerValue(String spinnerValues){
//		if(!StringUtils.isBlank(spinnerValues) && spinnerValues.contains(COMMA)){
//			String[] values = spinnerValues.split(COMMA);
//			if(!ArrayUtils.isEmpty(values) && values.length==2){
//				standbyDuration = Integer.parseInt(values[0]);
//				powerOffDelay = Integer.parseInt(values[1]);
//			}
//		}
//	}

}
