package com.landsem.common.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.Context;

//日期和时间模式  结果  
//"yyyy.MM.dd G 'at' HH:mm:ss z"  	2001.07.04 AD at 12:08:56 PDT  
//"EEE, MMM d, ''yy"  				Wed, Jul 4, '01  
//"h:mm a"  							12:08 PM  
//"hh 'o''clock' a, zzzz"  			12 o'clock PM, Pacific Daylight Time  
//"K:mm a, z"  						0:08 PM, PDT  
//"yyyyy.MMMMM.dd GGG hh:mm aaa"  	02001.July.04 AD 12:08 PM  
//"EEE, d MMM yyyy HH:mm:ss Z"  		Wed, 4 Jul 2001 12:08:56 -0700  
//"yyMMddHHmmssZ"  					010704120856-0700  
//"yyyy-MM-dd'T'HH:mm:ss.SSSZ"  		2001-07-04T12:08:56.235-0700  
public class TimeUtils {

	public static final String XING_QI = "星期";
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DATE_FORMAT_MM_DD   = new SimpleDateFormat("MM-dd");
	private static final String[] WEEK = { "天", "一", "二", "三", "四", "五", "六" };
	private static final int MILLISECOND_OF_DAY= 86400000;
    
	public static String getTimeFormat(Context context){
		String strTimeFormat = android.provider.Settings.System.getString(context.getContentResolver(),android.provider.Settings.System.TIME_12_24);
		return strTimeFormat;
	}
	
	public static String getData(int num) {
		long curTime = System.currentTimeMillis() + num * MILLISECOND_OF_DAY;
		return DATE_FORMAT_MM_DD.format(new Date(curTime));
	}

    /**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
    
    
    
	public static String getDay(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天";
			break;
		case 1:
			result = "昨天";
			break;
		case 2:
			result = "前天";
			break;

		default:
			result = temp + "天前";
			break;
		}
		return result;
	}
	
	
	/**
	 * 将日期格式字符串转换成毫秒数
	 * @param pattern 要转换的日期格式
	 * @param timeStr 要转换的字符串
	 * @return
	 */
	public static long extractTime(String pattern,String timeStr) {
		long resultTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		if (!StringUtils.isEmpty(timeStr)) {
			Date datee = new Date(timeStr);
			format.format(datee);
			resultTime = datee.getTime();
		}
		return resultTime;
	}
	
	
	public static String getWeek(int num, String format) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int weekNum = calendar.get(Calendar.DAY_OF_WEEK) + num;
		if (weekNum > 7)
			weekNum = weekNum - 7;
		return format + WEEK[weekNum - 1];
	}
	
	
	public static int getIndexOfWeek(int num){
		final Calendar calendar = Calendar.getInstance();
		TimeZone timeZone = TimeZone.getDefault();
		String timeZoneStr = timeZone.getID();
		if(StringUtils.isEmpty(timeZoneStr)){
			timeZoneStr = "GMT+8:00";
		}
		calendar.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
		int weekNum = calendar.get(Calendar.DAY_OF_WEEK) + num;
		if (weekNum > 7){
			weekNum = weekNum - 7;
		}
		return weekNum;
	}
	
	public static String getSystemDate(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		String format = getDateFormat(contentResolver);
		if(!StringUtils.isEmpty(format)){
			DATE_FORMAT_DATE.applyPattern(format);
		}
		return DATE_FORMAT_DATE.format(new Date());
	}
	
	public static String getDateFormat(ContentResolver contentResolver) {
		return android.provider.Settings.System.getString(contentResolver,android.provider.Settings.System.DATE_FORMAT);
	}
	
	
}
