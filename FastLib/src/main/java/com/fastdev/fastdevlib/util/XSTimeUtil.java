package com.fastdev.fastdevlib.util;

import android.content.Context;
import android.text.TextUtils;

import com.fastdev.fastdevlib.R;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class XSTimeUtil {

    private static final String TAG = XSTimeUtil.class.getSimpleName();
    private static final long DAY_TO_MILLISECONDS = 24 * 60 * 60 * 1000;
    private static final String DEFAULT_TIME_00_00 = "00:00";
    private static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    private static final String PATTERN_DATE = "yyyy-MM-dd";
    private static final String PATTERN_DATE_1 = "yyyy年MM月dd日";
    private static final String PATTERN_TIME_1 = "HH:mm";
    // static public String CURRENT_BEFORE_DAY = "yesterday";
    static public String INPUT_WRONG_TIME = "00:00";

    public static String timeFormat(int i) {
        String s = i + "";
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * @param parseTime call time in long format.
     * @return call time in special format string.
     * @author m00223591
     */
    public static String getNewFormatTime(Context context, long parseTime) {
        if ((0 == parseTime) || (parseTime > System.currentTimeMillis())) {
            return INPUT_WRONG_TIME;
        }

        Calendar oldTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        oldTime.setTimeInMillis(parseTime);

        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // ��ȡ������ʱ��
        int curYear = currentTime.get(Calendar.YEAR); // ��ȡ��
        int curMonth = currentTime.get(Calendar.MONTH); // ��ȡ��
        int curDay = currentTime.get(Calendar.DAY_OF_MONTH); // ��ȡ��ǰ����

        currentTime.add(Calendar.DAY_OF_MONTH, -1);
        int curBeforeDay = currentTime.get(Calendar.DAY_OF_MONTH); // ��ȡ��ǰʱ���ǰһ��

        SimpleDateFormat isTodayFormat = new SimpleDateFormat("HH:mm", Locale.US);
        SimpleDateFormat isSameYearFormat = new SimpleDateFormat("MM-dd", Locale.US);
        SimpleDateFormat otherFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        boolean isToday = (curDay == oldTime.get(Calendar.DAY_OF_MONTH)) && (curMonth == oldTime.get(Calendar.MONTH))
                && (curYear == oldTime.get(Calendar.YEAR));
        boolean isBeforeDay = (curBeforeDay == oldTime.get(Calendar.DAY_OF_MONTH))
                && (curMonth == oldTime.get(Calendar.MONTH)) && (curYear == oldTime.get(Calendar.YEAR));
        boolean isSameYear = (curYear == oldTime.get(Calendar.YEAR));

        if (isToday) {
            return isTodayFormat.format(oldTime.getTime());
        } else if (isBeforeDay) {
            return context.getString(R.string.str_base_show_yesterday);
        } else if (isSameYear) {
            return isSameYearFormat.format(oldTime.getTime());
        } else {
            return otherFormat.format(oldTime.getTime());
        }
    }

    public static String convertTimeMillisToFormatDateTime(Context context, long millisTime) {
        if ((0 == millisTime) || (millisTime > System.currentTimeMillis())) {
            return DEFAULT_TIME_00_00;
        }

        // Calendar oldTime =
        // Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        Calendar oldTime = Calendar.getInstance(TimeZone.getDefault());
        oldTime.setTimeInMillis(millisTime);

        Calendar currentTime = Calendar.getInstance(TimeZone.getDefault());

        SimpleDateFormat isTodayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat isSameYearFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        SimpleDateFormat otherFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        int curYear = currentTime.get(Calendar.YEAR);
        boolean isSameYear = (curYear == oldTime.get(Calendar.YEAR));

        long oldHours = millisTime / DAY_TO_MILLISECONDS;
        long currenthours = currentTime.getTimeInMillis() / DAY_TO_MILLISECONDS;
        int daysAgo = (int) (currenthours - oldHours);
        if (daysAgo == 0) {// today
            return isTodayFormat.format(oldTime.getTime());
        }

        if (daysAgo == 1) {// yesterday
            return context.getString(R.string.str_base_show_yesterday);
        }

        if (daysAgo > 1 && daysAgo < 7) {// last week
            return ShowDayOfWeek(context, oldTime.get(Calendar.DAY_OF_WEEK));
        }

        if (isSameYear) {// this year
            return isSameYearFormat.format(oldTime.getTime());
        }

        return otherFormat.format(oldTime.getTime());// last year or older
    }

    public static String convertTimeMillisToFormatDateTime2(Context context, long millisTime) {
        if ((0 == millisTime) || (millisTime > System.currentTimeMillis())) {
            return DEFAULT_TIME_00_00;
        }

        // Calendar oldTime =
        // Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        Calendar oldTime = Calendar.getInstance(TimeZone.getDefault());
        oldTime.setTimeInMillis(millisTime);

        Calendar currentTime = Calendar.getInstance(TimeZone.getDefault());

        SimpleDateFormat isTodayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat isSameYearFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        SimpleDateFormat otherFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        int curYear = currentTime.get(Calendar.YEAR);
        boolean isSameYear = (curYear == oldTime.get(Calendar.YEAR));

        long oldHours = millisTime / DAY_TO_MILLISECONDS;
        long currenthours = currentTime.getTimeInMillis() / DAY_TO_MILLISECONDS;
        int daysAgo = (int) (currenthours - oldHours);
        if (daysAgo == 0) {// today
            return isTodayFormat.format(oldTime.getTime());
        }

        if (daysAgo == 1) {// yesterday
            return context.getString(R.string.str_base_show_yesterday);
        }
        if (isSameYear) {// this year
            return isSameYearFormat.format(oldTime.getTime());
        }

        return otherFormat.format(oldTime.getTime());// last year or older
    }

    /**
     * 转换格式--时：分
     *
     * @param context
     * @param millisTime
     * @return
     */
    public static String convertTimeMillisToFormatHourTime(Context context, long millisTime) {
        Calendar oldTime = Calendar.getInstance(TimeZone.getDefault());
        oldTime.setTimeInMillis(millisTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(oldTime.getTime());
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    public static String convertFormatDateTime(Context context, String formatDateTime) {

        Calendar oldTime = Calendar.getInstance(TimeZone.getDefault());
        oldTime.setTime(strToDate(formatDateTime));

        Calendar currentTime = Calendar.getInstance(TimeZone.getDefault());

        SimpleDateFormat isTodayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat isSameYearFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        SimpleDateFormat otherFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        boolean isSameYear = (currentTime.get(Calendar.YEAR) == oldTime.get(Calendar.YEAR));
        int daysAgo = (currentTime.get(Calendar.DAY_OF_MONTH) - oldTime.get(Calendar.DAY_OF_MONTH));
        if (daysAgo == 0) {// today
            return context.getString(R.string.str_base_show_today);
        }

        if (daysAgo == 1) {// yesterday
            return context.getString(R.string.str_base_show_yesterday);
        }

        if (isSameYear) {// this year
            return isSameYearFormat.format(oldTime.getTime());
        }

        return otherFormat.format(oldTime.getTime());// last year or older
    }

    public static String convert2FormatDateTime(Context context, long millisTime) {
        Calendar oldTime = Calendar.getInstance(TimeZone.getDefault());
        oldTime.setTimeInMillis(millisTime);

        SimpleDateFormat otherFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        return otherFormat.format(oldTime.getTime());
    }

    private static String ShowDayOfWeek(Context context, int i) {
        String dayOfWeek = null;
        switch (i) {
            case Calendar.SUNDAY:
                dayOfWeek = context.getResources().getString(R.string.str_base_week_sunday);
                break;
            case Calendar.MONDAY:
                dayOfWeek = context.getResources().getString(R.string.str_base_week_monday);
                break;
            case Calendar.TUESDAY:
                dayOfWeek = context.getResources().getString(R.string.str_base_week_tuesday);
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = context.getResources().getString(R.string.str_base_week_wednesday);
                break;
            case Calendar.THURSDAY:
                dayOfWeek = context.getResources().getString(R.string.str_base_week_thursday);
                break;
            case Calendar.FRIDAY:
                dayOfWeek = context.getResources().getString(R.string.str_base_week_friday);
                break;
            case Calendar.SATURDAY:
                dayOfWeek = context.getResources().getString(R.string.str_base_week_saturday);
                break;
            default:
                dayOfWeek = context.getResources().getString(R.string.str_base_week_sunday);
        }
        return dayOfWeek;
    }

    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE_TIME);
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }

    public static long getTimeMillisecond(String time, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            Date curDate = formatter.parse(time);
            return curDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean compareDate(String date1, String date2) {
        long ms1 = getTimeMillisecond(date1, PATTERN_DATE_TIME);
        long ms2 = getTimeMillisecond(date2, PATTERN_DATE_TIME);

        if (ms2 - ms1 > 1 * 24 * 60 * 60 * 100) {
            return true;
        }
        return false;
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss日期格式化为HH:mm形式
     *
     * @param time
     * @return
     */
    public static String formatHourMinute(String time) {
        if (TextUtils.isEmpty(time)) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DATE_TIME, Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat(PATTERN_TIME_1, Locale.getDefault());
            return timeFormat.format(dateFormat.parse(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String formatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE_TIME, Locale.getDefault());
        return format.format(new Date(time));
    }

    public static String formatTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE_TIME, Locale.getDefault());
        return format.format(new Date());
    }

    public static String formatDateTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE_1, Locale.getDefault());
        return format.format(new Date(time));
    }

    /**
     * 格式化时间的长度(HH:mm) 比如 2:5 显示为02：05
     *
     * @param time
     * @return
     */
    public static String formatTimeLength(String time) {
        if (TextUtils.isEmpty(time)) {
            return null;
        }
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat(PATTERN_TIME_1, Locale.getDefault());
            return timeFormat.format(timeFormat.parse(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 判断当前日期是否是本月最后一天
     *
     * @return
     */
    public static boolean isLastDay() {
        // 获取当前月最后一天
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE);
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastDay = format.format(ca.getTime());
        String nowDay = format.format(new Date());
        long lastDayMil = getTimeMillisecond(lastDay, PATTERN_DATE);
        long nowDayMil = getTimeMillisecond(nowDay, PATTERN_DATE);

        if (lastDayMil == nowDayMil) {
            return true;
        }
        return false;
    }

    /**
     * 将HH:mm格式的时间转换为yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatToDateTime(String time) {
        SimpleDateFormat format_date = new SimpleDateFormat(PATTERN_DATE);
        String currentData = format_date.format(new Date());
        return currentData + " " + time + ":00";
    }

    /**
     * 比较俩个HH:mm格式的时间大小
     *
     * @return true==time1 > time2
     */
    public static boolean compareTimeByHHmm(String time1, String time2) {
        if (TextUtils.isEmpty(time1)) {
            time1 = "00:00";

        }
        if (TextUtils.isEmpty(time2)) {
            time2 = "00:00";

        }
        long ms1 = getTimeMillisecond(time1, PATTERN_TIME_1);
        long ms2 = getTimeMillisecond(time2, PATTERN_TIME_1);

        if (ms1 - ms2 > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取UTC时间<br>
     * 上传服务器时间一律使用UTC时间
     *
     * @return UTC时间
     */
    public static String getUTCTime() {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE_TIME);
        try {
            Calendar cal = Calendar.getInstance();

            // 1、设置时间：
            cal.setTime(new Date());
            // 2、取得时间偏移量：
            int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
            // 3、取得夏令时差：
            int dstOffset = cal.get(Calendar.DST_OFFSET);
            // 4、从本地时间里减去这些差量，即可以取得UTC时间：
            cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

            String utcTime = format.format(new Date(cal.getTimeInMillis()));


            return utcTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取UTC时间<br>
     * 上传服务器时间一律使用UTC时间
     *
     * @param time 当前时区时间
     * @return
     */
    public static String getUTCTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE_TIME);
        try {
            Calendar cal = Calendar.getInstance();

            // 1、设置时间：
            cal.setTime(format.parse(time));
            // 2、取得时间偏移量：
            int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
            // 3、取得夏令时差：
            int dstOffset = cal.get(Calendar.DST_OFFSET);
            // 4、从本地时间里减去这些差量，即可以取得UTC时间：
            cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

            String utcTime = format.format(new Date(cal.getTimeInMillis()));


            return utcTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 将UTC时间转化为当地时区时间<br>
     * 上传服务器时间一律使用UTC时间
     *
     * @param utcTime UTC时间
     * @return 本地时区时间
     */
    public static String formatUTCTimeToCurrentTimezone(String utcTime) {
        if (TextUtils.isEmpty(utcTime)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE_TIME);
        try {

            Calendar cal = Calendar.getInstance();
            // 1、设置时间：
            cal.setTime(format.parse(utcTime));
            // 2、取得时间偏移量：
            int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
            // 3、取得夏令时差：
            int dstOffset = cal.get(Calendar.DST_OFFSET);
            // 4、从本地时间里添加这些差量，即可以取得当地时间：
            cal.add(Calendar.MILLISECOND, +(zoneOffset + dstOffset));

            String time = format.format(new Date(cal.getTimeInMillis()));


            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utcTime;
    }

    /**
     * 将UTC时间转化为当地时区时间<br>
     * <p>
     * 只转换年月日没有时分秒
     * </p>
     * 上传服务器时间一律使用UTC时间
     *
     * @return 本地时区时间
     */
    public static String formatUTCDateToCurrentTimezone(String utcDate) {
        if (TextUtils.isEmpty(utcDate)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE);

        try {

            Calendar cal = Calendar.getInstance();
            // 1、设置时间：
            cal.setTime(format.parse(utcDate));
            // 2、取得时间偏移量：
            int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
            // 3、取得夏令时差：
            int dstOffset = cal.get(Calendar.DST_OFFSET);
            // 4、从本地时间里添加这些差量，即可以取得当地时间：
            cal.add(Calendar.MILLISECOND, +(zoneOffset + dstOffset));

            String time = format.format(new Date(cal.getTimeInMillis()));


            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utcDate;
    }

    public static String formatCurrentTimeToUTCTime(String currentTime) {
        if (TextUtils.isEmpty(currentTime)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_TIME_1);
        try {
            Calendar cal = Calendar.getInstance();

            // 1、设置时间：
            cal.setTime(format.parse(currentTime));
            // 2、取得时间偏移量：
            int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
            // 3、取得夏令时差：
            int dstOffset = cal.get(Calendar.DST_OFFSET);
            // 4、从本地时间里减去这些差量，即可以取得UTC时间：
            cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

            String utcTime = format.format(new Date(cal.getTimeInMillis()));

            return utcTime;
        } catch (Exception e) {
        }
        return currentTime;

    }

    /**
     * Translate yyyy-MM-dd HH:mm:ss to Date
     */
    public static Date parseStrToDate(String formatStr) {
        if (TextUtils.isEmpty(formatStr)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_DATE_TIME);
        try {
            return simpleDateFormat.parse(formatStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Translate HH:mm to Date
     * @param formatStr
     * @return
     */
    public static Date parseStrToTime(String formatStr) {
        if (TextUtils.isEmpty(formatStr)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_TIME_1);
        try {
            return simpleDateFormat.parse(formatStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date转成yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDate2Str(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_DATE_TIME);
        return simpleDateFormat.format(date);
    }

    /**
     * Date转成HH:mm
     * @param date
     * @return
     */
    public static String formatDate2HourMinute(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_TIME_1);
        return simpleDateFormat.format(date);
    }
}
