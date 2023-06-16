package com.landsem.setting.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.landsem.common.tools.ListUtils;
import com.landsem.setting.Constant;
import com.landsem.setting.entity.SpinnerOption;

import android.content.res.XmlResourceParser;

public class SignalParser implements Constant{

	private static final long serialVersionUID = 8814018458534091292L;
	private static String settingPath = "/system/etc/signalConfig.xml";
	public static List<SpinnerOption> signalList = null;
	
	public static List<SpinnerOption> parserSetting() {
		if(ListUtils.isEmpty(signalList)){
			try {
				File setting = new File(settingPath);
				if (!setting.exists()) return signalList;
				signalList = new ArrayList<SpinnerOption>();
				FileInputStream inputStream = new FileInputStream(setting);
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(inputStream, "UTF-8");
				while (parser.getEventType()!=XmlResourceParser.END_DOCUMENT) {
					// 如果遇到了开始标签
					if (parser.getEventType() == XmlResourceParser.START_TAG) {
						String tagName = parser.getName();// 获取标签的名字
						if (tagName.equals("string")) {
							Map<String, String> map = new HashMap<String, String>();
							String name = parser.getAttributeValue(null, "name");// 通过属性名来获取属性值
							String value = parser.getAttributeValue(null, "value");
							SpinnerOption item = new SpinnerOption(name, value);
							signalList.add(item);
						}
					}
					parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return signalList;
	}

}
