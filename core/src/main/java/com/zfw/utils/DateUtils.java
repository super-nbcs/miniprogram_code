/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.zfw.utils;

import com.zfw.core.exception.GlobalException;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author yzh
 * @version 2019-8-06
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static String[] parsePatterns = {
			"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
			"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd）
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTimeByDate(Date date) {
		return formatDate(date, "HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/*
	 * 将时间戳转换为时间(秒)
	 */
	public static Date stampToDate(int s){
		long a=s*1000L;
		Date date1 = new Date(a);
		return date1;
	}

	/*
	 * 将时间戳转换为时间(秒)
	 */
	public static Date timeToDate(Long s){
		Date date1 = new Date(s);
		return date1;
	}



	/**
	 * 将时间戳转换为时间(秒)
	 */
	public static String timeToDateString(int s){
		long a=s*1000L;
		Date date = new Date(a);
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}


	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}

	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
	}

	/**
	 * 获取两个日期之间的天数
	 *
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	public static int getDayOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		int a=(int)((afterTime - beforeTime) / (1000 * 60 * 60 * 24));
		return a;
	}
	public static int getHourOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		int a=(int)((afterTime - beforeTime) / (1000 * 60 * 60));
		return a;
	}

	/**
	 * @param source
	 * @throws ParseException
	 */
	//生成10位的时间戳
	public static int dateToStamp(String source) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = df.parse(source);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long timestamp = cal.getTimeInMillis();
		System.out.println(timestamp/1000);
		String s = Long.valueOf(timestamp/1000).toString();
		int integer = Integer.valueOf(s);
		return integer;
	}


	/**
	 * 生成时间戳
	 * @param date
	 * @return
	 */
	public static long dateToStampByDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long timestamp = cal.getTimeInMillis();
		return timestamp/1000;
	}



	public static int getStamp(int year) {
		int i = 0;
		try{
			Calendar cal = Calendar.getInstance();
			//int year1 =cal.get(Calendar.YEAR)+year;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)+year);
			Date date=cal.getTime();
			System.out.println(sdf.format(date));
			i = dateToStamp(sdf.format(date));
		}catch (Exception p){
			p.printStackTrace();
		}
		return i;
	}

	/**
	 * 根据当前时间获取多少天前的时间
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date getFrontDay(Date date, int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
		return cal.getTime();
	}

	/**
	 * 根据当前时间获取多少月前的时间
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date getFrontMonth(Date date, int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - i);
		return cal.getTime();
	}

	/**
	 * 获取昨天某个时间点的时间
	 * @param i 小时
	 * @return
	 */
	public static Date getYesterdayTime(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,i);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		calendar.add(Calendar.DAY_OF_YEAR,-1);
		return calendar.getTime();
	}

	/**
	 * 格式化Excel时间
	 * @param
	 * @return yyyy-MM-dd
	 */
	public String getPOIDate(boolean use1904windowing, double value) {
		int wholeDays = (int) Math.floor(value);
		int millisecondsInDay = (int) ((value - (double) wholeDays) * 8.64E7D + 0.5D);
		Calendar calendar = new GregorianCalendar();
		short startYear = 1900;
		byte dayAdjust = -1;
		if (use1904windowing) {
			startYear = 1904;
			dayAdjust = 1;
		} else if (wholeDays < 61) {
			dayAdjust = 0;
		}
		calendar.set(startYear, 0, wholeDays + dayAdjust, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, millisecondsInDay);
		if (calendar.get(Calendar.MILLISECOND) == 0) {
			calendar.clear(Calendar.MILLISECOND);
		}
		Date date = calendar.getTime();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return s.format(date);
	}

	/**
	 * 获取当前时间距离零点的秒数
	 * @return
	 */
	public static long getOvertime() {
		long now = System.currentTimeMillis();
		SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");
		long overTime = 0;
		try {
			overTime = (now - (sdfOne.parse(sdfOne.format(now)).getTime()))/1000;
		} catch (ParseException e) {
			e.printStackTrace();
			throw new GlobalException("获取距离0点的秒数时，时间转换异常");
		}
		return overTime;
	}

	/**
	 * 获取今日零点
	 * @return
	 */
	public static Date getTodayStart(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date zero = calendar.getTime();
		return zero;
	}


	/**
	 * 获取今日23:59:59
	 * @return
	 */
	public static Date getTodayEnd(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date end = calendar.getTime();
		return end;
	}
}


