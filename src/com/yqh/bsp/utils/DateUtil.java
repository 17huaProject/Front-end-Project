package com.yqh.bsp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat();
	static final Calendar G_CALENDAR = new java.util.GregorianCalendar();

	public static String formatFullfmt(Date date) {
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String formatTranfmt(Date date) {
		sdf.applyPattern("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	public static Date parseDateFullfmt(String yyyyMMdd_HHmmss) {
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(yyyyMMdd_HHmmss);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	public static String getNowTimestamp() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
	
	public static String getNowFormat(String format) {
		Date now = new Date();
		sdf.applyPattern(format);
		String s = sdf.format(now);
		return s;
	}
	
	public static Date parseDate(String dateString,String formatString) {
		sdf.applyPattern(formatString);
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			return new Date();
		}
	}


	public static String formatDate(Date date, String formatString) {
		sdf.applyPattern(formatString);
		return sdf.format(date);
	}

	public static int getNumberDayInYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_YEAR);
	}

	public static int getWeekDay() { // 取当天是周几
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}

	public static int getTimeDiffer(Date date,Date nowDate) {// 返回相差的分钟数
		return (int) ((date.getTime() - nowDate.getTime()) / (1000 * 60));
	}

	public static boolean isTheSameDay(Date date) {
		Calendar c1 = Calendar.getInstance(), c2 = Calendar.getInstance();
		c1.setTime(date);
		c2.setTime(new Date());
		return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
	}

	public static boolean isTheSameDay(String date) {
		sdf.applyPattern("yyyy-MM-dd");
		try {
			Date datetime = sdf.parse(date);
			return isTheSameDay(datetime);
		} catch (ParseException e) {
		}
		return false;
	}
	
	/**
	 * 当前日期偏移运算(增、减几日）
	 * @param skipDate 日偏移量
	 * @return Date 偏移日期
	 */
	public static Date getSkipTime(int skipDay) {		
		return getSkipTime(new Date(), skipDay);
	}	

	/**
	 * 某一时间的偏移运算(增、减几日）
	 * @param Date 
	 * @param skipDate 日偏移量
	 * @return Date 偏移日期
	 */
	public static synchronized Date getSkipTime(Date date, int skipDay) {			
		G_CALENDAR.setTime(date);        
		G_CALENDAR.add(Calendar.DAY_OF_MONTH, skipDay);				
		
		return G_CALENDAR.getTime();	
	}
	
	public static String getWeekDay(Date date) { // 取当天是周几
		G_CALENDAR.setTime(date);
		int dayOfWeek = G_CALENDAR.get(Calendar.DAY_OF_WEEK);
		String week="";
		switch(dayOfWeek) {
		case 1:
			week="周日";
			break;
		case 2:
			week="周一";
			break;
		case 3:
			week="周二";
			break;
		case 4:
			week="周三";
			break;
		case 5:
			week="周四";
			break;
		case 6:
			week="周五";
			break;
		case 7:
			week="周六";
			break;
		}
		return week;
	}
	
	public static String formatYearMonth(Date date) {
		sdf.applyPattern("yyMM");
		return sdf.format(date);
	}
	
	
}