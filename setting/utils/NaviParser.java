package com.landsem.setting.utils;

import java.io.File;
import java.io.FileInputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.landsem.setting.Constant;
import com.landsem.setting.entity.NaviInfo;

import android.content.res.XmlResourceParser;

public class NaviParser implements Constant{

	private static final long serialVersionUID = 8814018458534091292L;
	private static String naviConfig = "/system/etc/naviConfig.xml";
	
	public static NaviInfo parserSetting() {
		NaviInfo mNaviInfo = null;
		try {
			File naviCfgFile = new File(naviConfig);
			if(naviCfgFile.exists()){
				FileInputStream inputStream = new FileInputStream(naviCfgFile);
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(inputStream, "UTF-8");
				while (parser.getEventType()!=XmlResourceParser.END_DOCUMENT) {
					// 如果遇到了开始标签
					if (parser.getEventType() == XmlResourceParser.START_TAG) {
						String tagName = parser.getName();// 获取标签的名字
						if (tagName.equals("string")) {
							String packageName = parser.getAttributeValue(null, "packageName");// 通过属性名来获取属性值
							String className = parser.getAttributeValue(null, "className");
							mNaviInfo = new NaviInfo(packageName, className);
						}
					}
					parser.next();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mNaviInfo;
	}

}
