package com.landsem.setting.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.landsem.common.tools.ListUtils;
import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.StringUtils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

@SuppressLint("NewApi")
public class APNUpdate {
	
	private static final String TAG = APNUpdate.class.getSimpleName();
	
	private static final boolean isDbgEnable = true;
	private static final String APN_ID = "apn_id";
	private static final String idColumnName = "_id";
	private static final String cfgAPNPath = "/system/etc/cfgAutoAPN.xml";
    public static final String PREFERRED_APN_URI = "content://telephony/carriers/preferapn";	
    private static final Uri PREFERAPN_URI = Uri.parse(PREFERRED_APN_URI);	
	protected List<String[]> mCardList = null;
	private Context mContext;
	
	public APNUpdate(Context context) {
		mContext = context;
		mCardList = parserAutoAPNSetting();
	}
		
	/**
	 * get auto APN setting from configuration file.
	 * @return
	 */
	private static List<String[]> parserAutoAPNSetting() {
		List<String[]> settingList = null;
		try {
			File setting = new File(cfgAPNPath);
			if (!setting.exists()) return settingList;			
			settingList = new ArrayList<String[]>();
			FileInputStream inputStream = new FileInputStream(setting);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(inputStream, "UTF-8");
			while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlResourceParser.START_TAG) {
					String tagName = parser.getName();
					if (tagName.equals("apn")) {
						String name = parser.getAttributeValue(null, "name");
						String value = parser.getAttributeValue(null, "value");
						settingList.add(new String[]{name,value});
					}
				}
				parser.next();// next one
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return settingList;
	}
	
	public boolean changeAPN(String key){
		String methodName = "changeAPN" + "      &&&&&&      ";
		boolean result = !StringUtils.isBlank(key);
		LogManager.d(TAG, methodName+"start result: "+result+", key: "+key);
		if(result){
			result = !ListUtils.isEmpty(mCardList);
			LogManager.d(TAG, methodName+"isListValid result: "+result);
			if(result){
				for(int position=0;position<mCardList.size();position++){
					String[] itemArray = mCardList.get(position);
					LogManager.d(TAG, methodName+"itemArray: "+itemArray[0]+"、 "+itemArray[1]);
					result = key.equalsIgnoreCase(itemArray[0]);
					if(result){
						String apnName = itemArray[1];
						String apnKey = getApnKeyByName(apnName);
						setSelectedApnKey(apnKey);
						result = true;
						LogManager.d(TAG, methodName + "key: "+apnName+", apnName: "+apnName+", apnKey: "+apnKey);
						break;
					}
				}
			}
			
		}
		LogManager.d(TAG, methodName+"end result: "+result+", key: "+key);
		return result;
	}
	
	/**
	 * get matched APN key.
	 * @param name
	 * @return
	 */
    private String getApnKeyByName(final String name) {
        String key = null;
        final String selection = "name='" + name +"'";
        Cursor cursor = mContext.getContentResolver().query(Telephony.Carriers.CONTENT_URI, new String[] {idColumnName},selection, null, Telephony.Carriers.DEFAULT_SORT_ORDER);
        if ((null != cursor) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            key = cursor.getString(cursor.getColumnIndex(idColumnName));
            cursor.close();
        }        
        return key;
    }
    
    /**
     * update APN.
     * @param key
     */
    private void setSelectedApnKey(String key) {
        final String mSelectedKey = key;
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(APN_ID, mSelectedKey);
        resolver.update(PREFERAPN_URI, values, null, null);
    }


}
