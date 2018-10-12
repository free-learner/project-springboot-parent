package com.personal.springboot.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期格式工具类
 * 
 * @author Liubao
 * @2015年7月10日
 * 
 */
public class DatetimeUtils {

    private static final Logger logger = LoggerFactory.getLogger(DatetimeUtils.class);

    //日期时间格式常量
    public static final String DATETIME_PATTERN_FULL = "yyyy-MM-dd HH:mm:ss.S";
    public static final String DATETIME_PATTERN_STRING = "yyyyMMddHHmmss";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_CHN = "yyyy年MM月dd日";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String MINUTE_PATTERN = "HH:mm:ss";
    public static final String HOUR_PATTERN = "HH:mm";
    public static final String DATE_SEPARATOR = "-";
    public static final String DATE_PATTERN_IDCARD = "yyyyMMdd";

    private static final long ONE_DAY_IN_MILISECONDS = 24 * 60 * 60 * 1000;

    private static final String[] WEEK_NAMES = {"星期日", "星期一", "星期二",
            "星期三", "星期四", "星期五", "星期六" };

    public static final Timestamp TIMESTAMP_ZERO = parseTimestamp("1970-01-01 10:00:00");
    public static final Timestamp TIMESTAMP_MAX = parseTimestamp("2038-01-19 03:14:07");
    public static final Date DATETIME_ZERO = parseDatetime("1970-01-01 10:00:00");
    public static final Date DATETIME_MAX = parseTimestamp("2038-01-19 03:14:07");

    
    public static void main(String[] args) {
        Timestamp weekPlus = weekPlus(
                DatetimeUtils.createSpecialDateTime(
                        DatetimeUtils.currentTimestamp(), null, 23, 59, 59), 2);
        System.out.println(DatetimeUtils.formatDate(weekPlus));
        
        String firstDayStr = getNextWeekFirstDayStr(currentTimestamp(), DATETIME_PATTERN);
        System.out.println(firstDayStr);
        
        firstDayStr = getNextWeekFirstDayStr(dayPlus(currentTimestamp(),3), DATETIME_PATTERN);
        System.out.println(firstDayStr);
        
        System.out.println(currentTimestamp());
        System.out.println(millSecondPlus(currentTimestamp(), 2000));
        System.out.println(secondPlus(currentTimestamp(), 10));
        System.out.println(minitePlus(currentTimestamp(), 1));
        System.out.println(hourPlus(currentTimestamp(), -1));
        
    }
    
    /**
     * TODO 
     */
    public static long convertToSecond(long mss) {
        long seconds = (mss * (10000 * 6));
        return seconds;
    }
    
    public static boolean isTimestampZero(Timestamp timestamp) {
        return TIMESTAMP_ZERO.equals(timestamp);
    }

    public static boolean isDatetimeZero(Date datetime) {
        if (datetime != null && datetime instanceof Timestamp) {
            return isTimestampZero((Timestamp) datetime);
        }
        return DATETIME_ZERO.equals(datetime);
    }
    
    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * 指定日期的零时刻 TODO 
     */
    public static String dateZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return formatDate(calendar.getTime());
    }
    
    public static String dateZero() {
        return dateZero(new Date());
    }
    
    public static String dateTimeZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return formatTimestamp(new Timestamp(calendar.getTimeInMillis()));
    }
    
    public static String dateTimeZero() {
        return dateTimeZero(new Date());
    }
    
    /**
     * 指定日期的23:59:59
     */
    public static String dateEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return formatDate(calendar.getTime());
    }
    
    public static String dateEnd() {
        return dateEnd(new Date());
    }
    
    public static String dateTimeEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return formatTimestamp(new Timestamp(calendar.getTimeInMillis()));
    }
    
    public static String dateTimeEnd() {
        return dateTimeEnd(new Date());
    }
    
    /**
     * 获取时间戳
     */
    public static String getTimeStampString() {
        SimpleDateFormat df = new SimpleDateFormat(DATETIME_PATTERN_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }
    /**
     * 获取时间戳
     */
    public static String getTimeStampName() {
        SimpleDateFormat df = new SimpleDateFormat(DATETIME_PATTERN_STRING);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }
    
    public static String currentTimestampStr() {
        return formatTimestamp(new Timestamp(System.currentTimeMillis()),DATETIME_PATTERN_STRING);
    }
    
    public static String currentDateStr() {
        return formatDate(new Date(System.currentTimeMillis()));
    }

    public static Timestamp afterOneWeek(Timestamp datetime) {
        if (datetime == null) {
            throw new IllegalArgumentException("Argument datetime is null!!");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(datetime);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        return new Timestamp(c.getTime().getTime());
    }

    public static long daysBetween(Timestamp datetime1, Timestamp datetime2) {
        Timestamp currentTimestamp = currentTimestamp();
        datetime1 = (datetime1 == null) ? currentTimestamp : datetime1;
        datetime2 = (datetime2 == null) ? currentTimestamp : datetime2;
        
        long delta = Math.abs(datetime2.getTime() - datetime1.getTime());
        return (long) Math.ceil(delta * 1.0 / ONE_DAY_IN_MILISECONDS);
    }

    public static String getCurrentMinuteTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(MINUTE_PATTERN);
        return formatter.format(currentTime);
    }
    
    public static int getCurrentYear() {
        Calendar calendar=Calendar.getInstance();
        //calendar.setTime(DatetimeUtils.currentTimestamp());
        return calendar.get(Calendar.YEAR);
    }
    
    public static int getCurrentMonth() {
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.MONTH)+1;
    }
    
    public static int getCurrentDayOfMonth() {
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public static int getCurrentSecond() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.SECOND);
    }

    public static Timestamp parseTimestamp(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
        try {
            return new Timestamp(sdf.parse(timestamp).getTime());
        } catch (Exception ex) {
            logger.error("\"" + timestamp + "\" is invalid,"
                    + " it should be in pattern " + " \""
                    + DATETIME_PATTERN + "\"", ex);
        }
        return null;
    }
    
    public static Timestamp parseTimestamp(String timestamp, String parttern) {
        SimpleDateFormat sdf = new SimpleDateFormat(parttern);
        try {
            return new Timestamp(sdf.parse(timestamp).getTime());
        } catch (Exception ex) {
            logger.error("\"" + timestamp + "\" is invalid,"
                    + " it should be in pattern " + " \""
                    + parttern + "\"", ex);
        }
        return null;
    }

    public static Date parseDatetime(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            return sdf.parse(datetime);
        } catch (Exception ex) {
            logger.error("\"" + datetime + "\" is invalid,"
                    + " it should be in pattern " + " \""
                    + DATE_PATTERN + "\"", ex);
        }
        return null;
    }

    public static Date parseDatetime(String datetime, String parttern) {
        parttern = (StringUtils.isBlank(parttern)) ? DATE_PATTERN
                : parttern;
        SimpleDateFormat sdf = new SimpleDateFormat(parttern);
        try {
            return sdf.parse(datetime);
        } catch (Exception ex) {
            logger.error("\"" + datetime + "\" is invalid,"
                    + " it should be in pattern " + " \""
                    + parttern + "\"", ex);
        }
        return null;
    }
    
    public static String formatDate(Date timestamp) {
        return timestamp == null ? "" : new SimpleDateFormat(
                DATE_PATTERN).format(timestamp);
    }

    public static String formatTimestamp(Timestamp timestamp) {
        return timestamp == null ? "" : new SimpleDateFormat(
                DATETIME_PATTERN).format(timestamp);
    }

    public static String formatDate(Date date, String parttern) {
        parttern = (StringUtils.isBlank(parttern)) ? DATE_PATTERN
                : parttern;
        SimpleDateFormat sdf = new SimpleDateFormat(parttern);
        try {
            return sdf.format(date);
        } catch (Exception ex) {
            logger.error("\"" + date + "\" is invalid,"
                    + " it should be in pattern " + " \"" + parttern + "\"", ex);
        }
        return null;
    }
    
    public static String formatTimestamp(Timestamp timestamp, String parttern) {
        parttern = (StringUtils.isBlank(parttern)) ? DATETIME_PATTERN
                : parttern;
        SimpleDateFormat sdf = new SimpleDateFormat(parttern);
        try {
            return sdf.format(timestamp);
        } catch (Exception ex) {
            logger.error("\"" + timestamp + "\" is invalid,"
                    + " it should be in pattern " + " \"" + parttern + "\"", ex);
        }
        return null;
    }
    
    public static Timestamp dayStartDatetime(Timestamp timestamp) {
        String date = formatDate(timestamp, DATE_PATTERN);
        return parseTimestamp(date + " 00:00:00");
    }

    public static Timestamp dayEndDatetime(Timestamp timestamp) {
        String date = formatDate(timestamp,DATE_PATTERN);
        return parseTimestamp(date + " 23:59:59");
    }

    public static Timestamp nextDay() {
        return dayPlus(currentTimestamp(), 1);
    }

    public static Timestamp nextDay(Timestamp timestamp) {
        return dayPlus(timestamp, 1);
    }

    public static Timestamp millSecondPlus(Timestamp timestamp, int millSeconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.add(Calendar.MILLISECOND, millSeconds);
        return new Timestamp(c.getTimeInMillis());
    }
    
    public static Timestamp secondPlus(Timestamp timestamp, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.add(Calendar.SECOND, seconds);
        return new Timestamp(c.getTimeInMillis());
    }
    
    public static Timestamp minitePlus(Timestamp timestamp, int minites) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.add(Calendar.MINUTE, minites);
        return new Timestamp(c.getTimeInMillis());
    }
    
    public static Timestamp hourPlus(Timestamp timestamp, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return new Timestamp(c.getTimeInMillis());
    }

    public static Timestamp hourPlusSimple(Timestamp timestamp, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.add(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }
    
    public static Timestamp dayPlus(Timestamp timestamp, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.add(Calendar.DAY_OF_MONTH, days);
        return new Timestamp(c.getTimeInMillis());
    }
    
    public static Timestamp weekPlus(Timestamp timestamp, int weeks) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR) + weeks);
        return new Timestamp(c.getTimeInMillis());
    }

    public static Timestamp monthPlus(Timestamp timestamp, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);
        return new Timestamp(c.getTimeInMillis());
    }
    
    public static Date monthPlus(Date dateTime, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateTime);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);
        return new Date(c.getTimeInMillis());
    }
    
    public static Timestamp yearPlus(Timestamp timestamp, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + years);
        return new Timestamp(c.getTimeInMillis());
    }
    
    public static Date yearPlus(Date dateTime, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateTime);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + years);
        return new Timestamp(c.getTimeInMillis());
    }

    /**
     * 按默认格式的字符串距离今天的天数
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parseDatetime(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parseDatetime(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }
    
    /**
     * @return 增加的天数后的凌晨00:00:00
     */
    public static Timestamp getZeroTimeAfterDays(Timestamp timestamp,
            int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.add(Calendar.DAY_OF_MONTH, days);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }

    public static String getWeekDayInString(Timestamp timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        return WEEK_NAMES[c.get(Calendar.DAY_OF_WEEK)-1];
    }
    
    public static String getWeekDayInString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return WEEK_NAMES[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }
    
    /**
     * 根据传入的日期，获取对应的星期
     */
    public static String getWeekDayInStringSimple(Date date) {
        Map<Integer, String> weeks = new HashMap<Integer, String>();
        weeks.put(0, "周日");
        weeks.put(1, "周一");
        weeks.put(2, "周二");
        weeks.put(3, "周三");
        weeks.put(4, "周四");
        weeks.put(5, "周五");
        weeks.put(6, "周六");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weeks.get(intWeek);
    }

    /**
     * 获取下周的第一天时间
     */
    public static String getNextWeekFirstDayStr(Timestamp timeStamp,
            String parttern) {
        if (StringUtils.isBlank(parttern)) {
            parttern = DATETIME_PATTERN;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeStamp);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, 8-day_of_week);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return DatetimeUtils.formatDate(calendar.getTime());
    }

    public static Timestamp getNextWeekFirstDay(Timestamp nextDay,
            String parttern) {
        if (StringUtils.isBlank(parttern)) {
            parttern = DATETIME_PATTERN;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextDay);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 8);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return Timestamp.valueOf(formatDate(calendar.getTime()));
    }

    public static Timestamp getCurrentWeekFirstDay(Timestamp nextDay,
            String parttern) {
        if (StringUtils.isBlank(parttern)) {
            parttern = DATETIME_PATTERN;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextDay);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 1);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return Timestamp.valueOf(formatDate(calendar.getTime()));
    }

    public static String getCurrentWeekFirstDayStr(Timestamp nextDay,
            String parttern) {
        if (StringUtils.isBlank(parttern)) {
            parttern = DATETIME_PATTERN;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextDay);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 1);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return DatetimeUtils.formatDate(calendar.getTime());
    }

    public static String createSpecialDateTimeStr(Timestamp day, String parttern,
            int hour, int minutes, int seconds) {
        return DatetimeUtils.formatDate(createSpecialDateTime(day, parttern, hour,
                minutes, seconds));
    }

    public static Timestamp createSpecialDateTime(Timestamp day, String parttern,
            int hour, int minutes, int seconds) {
        if (StringUtils.isBlank(parttern)) {
            parttern = DATETIME_PATTERN;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        return new Timestamp(calendar.getTime().getTime());
    }

    public static long compareToMills(Timestamp datetime1, Timestamp datetime2) {
        Timestamp currentTimestamp = currentTimestamp();

        datetime1 = (datetime1 == null) ? currentTimestamp : datetime1;
        datetime2 = (datetime2 == null) ? currentTimestamp : datetime2;

        long delta = datetime2.getTime() - datetime1.getTime();
        return delta;
    }


}
