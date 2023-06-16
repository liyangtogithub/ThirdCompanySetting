package com.landsem.setting;

import android.content.Context;
import android.content.res.Resources;

public class Type {
	
	public static int kinship_phone;
	public static int navi_phone;
	public static int insurance_phone;
	public static int alarn_phone;
	
	public static void initTypes(Context context){
		if(null!=context){
			Resources resources= context.getResources();
			if(null!=resources){
				kinship_phone = resources.getInteger(R.integer.kinship_phone);
				navi_phone = resources.getInteger(R.integer.navi_phone);
				insurance_phone = resources.getInteger(R.integer.insurance_phone);
				alarn_phone = resources.getInteger(R.integer.alarn_phone);
			}
		}
	}
    
}
