package com.casic.patrol.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/4/16.
 */
public class DateUtils
{
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat sdfFile = new SimpleDateFormat("yyyyMMddHHmmss");

    public static Calendar getCalendar(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static int getFistDayOfMonth(Calendar calendar)
    {
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return  calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getLastDayOfMonth(Calendar calendar)
    {
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.get(Calendar.DAY_OF_MONTH)+1;
    }

    public static int getYear(Calendar calendar)
    {
        return  calendar.get(Calendar.YEAR);
    }

    public static  int getMonth(Calendar calendar)
    {
        return  calendar.get(Calendar.MONTH);
    }

    /**
     * 判断日期是否为凌晨
     * @param date
     * @return
     */
    public static boolean isWeeHours(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);
        return hour + min + second + millisecond == 0;
    }

    /**
     * 获取第二天凌晨
     * @param date
     * @return
     */
    public static Date getWeeHoursNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }
}
