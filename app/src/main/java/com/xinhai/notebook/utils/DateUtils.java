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

}
