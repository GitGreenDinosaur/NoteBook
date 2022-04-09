package com.xinhai.notebook.utils;


import com.xinhai.notebook.data.db.bean.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    /**
     * 获取时间的工具类
     * @return 时间字符对象
     */
    public static Time getTime(){
        Time time = new Time();
        //获取当前详细时间
        Date now = new Date();
//        time.setNowTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(now));
        time.setNowTime(new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss", Locale.ENGLISH).format(now));
        //获取某月某日某年
        Calendar cal = Calendar.getInstance();
        time.setDay(cal.get(Calendar.DATE));
        time.setMonth(cal.get(Calendar.MONTH) + 1);
        time.setYear(cal.get(Calendar.YEAR));
        return time;
    }

    /**
     * 根据年份 天数来获取日期
     * @param y 年份
     * @param n 天数
     * @return 返回一个数组 result[0]:年  result[1]:月  result[0]:日
     */
    public static int[] getDateByDayOfYear(int y, int n) {
        int[] result = new int[3];
        int dayOfYear = 365;
        int m = 1, d = 1; //m 代表月   d 代表 日
        int[] ms = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //平年的12个月的天数
        if ((y % 4) == 0 && (y % 100) != 0 || (y % 400) == 0){
            dayOfYear = 366; //闰年有366天
            ms[1] = 29; //如果是闰年二月29日
        }
        if (n > dayOfYear){
            y++;
            n = n - dayOfYear;
        }
        while (n > 0) {
            d = n; //记录减前的数 作为日期
            n -= ms[m-1]; //每月的减
            m++; //不断递增 月份是 m-1
        }
//        result = String.format("%d年%d月%d日",y,m-1,d);
        result[0] = y; result[1] = m-1; result[2] = d;
        return result;
    }

    /**
     * 获取七天之后的时间戳
     * @return result 时间戳
     */
    private static long getTaskTimeDiff(){
        long result;
        //获得日历对象
        Calendar calendar = Calendar.getInstance();
        //获取七天后的日期
        int[] YMD = getDateByDayOfYear(calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_YEAR) + 7);
        //根据获取结果进行日历校正
        if (YMD[0] != calendar.get(Calendar.YEAR)) calendar.set(Calendar.YEAR,YMD[0]);
        if (YMD[1] != (calendar.get(Calendar.MONTH) + 1)) calendar.set(Calendar.MONTH,YMD[1]);
        //设置删除日期 精确到秒
        calendar.set(Calendar.DAY_OF_MONTH,YMD[2]);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
        //得到task时间戳
        result = calendar.getTimeInMillis();
//        System.out.println("taskTimeInMillis: " + result);
        return result;
    }

}
