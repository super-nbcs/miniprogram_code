package com.zfw.utils.holiday;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Holiday {
    private static Map<String,String> CHINAHOLIDAY =new HashMap<>();
    private static Map<String,String> HOLIDAY =new HashMap<>();
    static {
        CHINAHOLIDAY.put("正月初一","春节");
        CHINAHOLIDAY.put("正月十五","元宵节");
        CHINAHOLIDAY.put("五月初五","端午节");
        CHINAHOLIDAY.put("七月初七","七夕节");
        CHINAHOLIDAY.put("八月十五","中秋节");
        CHINAHOLIDAY.put("九月初九","重阳节");
        CHINAHOLIDAY.put("腊月初八","腊八节");
        CHINAHOLIDAY.put("腊月二十九","除夕");
        HOLIDAY.put("01-01","元旦");
        HOLIDAY.put("05-01","劳动节");
        HOLIDAY.put("10-01","国庆节");
    }

    /**
     * 获取中国节日
     * @param date 日期
     * @return 返回中国传统节日
     */
    public static String getChinaHoliday(Date date){
        String dateMMDD = getDateMMDD(date);
        return CHINAHOLIDAY.get(dateMMDD);
    }

    /**
     * 获取公历节日，如国庆，元旦
     * @param date 日期
     * @return 获取公历节日
     */
    public static String getHoliday(Date date){
        Calendar calendar = toCalendar(date);
        return HOLIDAY.get(String.format("%s-%s" , String.format("%02d" , calendar.get(Calendar.MONTH) + 1), String.format("%02d" , calendar.get(Calendar.DATE))));
    }
    /**
     * 获取24节气
     * @param date 日期
     * @return 返回中国传统二十四节气
     */
    public static String getChina24SolarTerms(Date date){
        Calendar calendar = toCalendar(date);
        String solatName = _24SolarTerms.getSolatName(calendar.get(Calendar.YEAR), String.format("%02d" , calendar.get(Calendar.MONTH) + 1)+calendar.get(Calendar.DATE));
        return solatName;
    }

    /**
     * 公历日期转农历日期,公历日期合法性经过检查. 推荐的调用方法
     *
     * @param date 公历日期
     * @return 农历日期
     */
    public static String getNongLi(Date date) {
        Calendar calendar = toCalendar(date);
        int year = calendar.get(Calendar.YEAR);
        String month = String.format("%02d",calendar.get(Calendar.MONTH) + 1);
        String day = String.format("%02d",calendar.get(Calendar.DATE));
        return NongLi.getDate(String.format("%s-%s-%s", year, month, day));
    }

    /**
     * date 转 Calendar
     * @param date
     * @return
     */
    private static Calendar toCalendar(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 公历日期转农历日期,公历日期合法性经过检查. 推荐的调用方法
     *
     * @param date 公历日期
     * @return 数组 {0} 年，{1} 月日
     */
    private static String[] getDateSplitYYAndMMDD(Date date){
        String date1 = getNongLi(date);
        String YY=date1.substring(0,5);
        String MMDD = date1.substring(5);
        return new String[]{YY,MMDD};
    }

    /**
     * 公历日期转农历日期,公历日期合法性经过检查. 推荐的调用方法
     *
     * @param date 公历日期
     * @return 返回农历年
     */
    private static String getDateYear(Date date){
        return getDateSplitYYAndMMDD(date)[0];
    }

    /**
     * 公历日期转农历日期,公历日期合法性经过检查. 推荐的调用方法
     *
     * @param date 公历日期
     * @return 返回农历日月
     */
    private static String getDateMMDD(Date date){
        return getDateSplitYYAndMMDD(date)[1];
    }
}
