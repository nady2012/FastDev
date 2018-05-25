package com.mamba.mambasdk.ui.pickerview;


import com.mamba.mambasdk.ui.pickerview.model.IPickerViewData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by shijunfeng on 2017/6/20.
 */
public class CustomDate implements IPickerViewData {
    private int dayIndex;

    /**
    * Instantiates a new Custom date.
    */
    public CustomDate() {
        date = new Date(System.currentTimeMillis());
    }

    private Date date;

    /**
    * Instantiates a new Custom date.
    *
    * @param date the date
    * @param dayIndex the day index
    */
    public CustomDate(Date date, int dayIndex) {
        this.date = date;
        this.dayIndex = dayIndex;
    }

    /**
    * Instantiates a new Custom date.
    *
    * @param date the date
    */
    public CustomDate(Date date) {

        this.date = date;
    }

    /**
    * Gets date.
    *
    * @return the date
    */
    public Date getDate() {

        return date;
    }

    /**
    * Sets date.
    *
    * @param date the date
    */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
    * Gets day index.
    *
    * @return the day index
    */
    public int getDayIndex() {
        return dayIndex;
    }

    /**
    * Sets day index.
    *
    * @param dayIndex the day index
    */
    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    /**
    * Gets picker view text.
    *
    * @return the picker view text
    */
    @Override
    public String getPickerViewText() {
        return getDateDes();
    }

    /**
    * Gets date des.
    *
    * @return  date des
    */
    public String getDateDes() {
        return convertFormatDateTime(this.date);
    }

    /**
    * Convert format date time string.
    *
    * @param date the date
    * @return  string
    */
    public static String convertFormatDateTime(Date date) {

        Calendar oldTime = Calendar.getInstance(TimeZone.getDefault());
        oldTime.setTime(date);

        Calendar currentTime = Calendar.getInstance(TimeZone.getDefault());

        SimpleDateFormat isSameYearFormat = new SimpleDateFormat("MM月dd日 EE", Locale.getDefault());
        SimpleDateFormat otherFormat = new SimpleDateFormat("yyyy年MM月dd日 EE", Locale.getDefault());

        boolean isSameYear = (currentTime.get(Calendar.YEAR) == oldTime.get(Calendar.YEAR));
        int daysAgo = (currentTime.get(Calendar.DAY_OF_YEAR) - oldTime.get(Calendar.DAY_OF_YEAR));
        if (isSameYear && daysAgo == 0) {// today
            return "今天";
        }

        if (daysAgo == -1) {
            return "明天";
        }

        if (daysAgo == -2) {
            return "后天";
        }

        if (isSameYear) {// this year
            return isSameYearFormat.format(oldTime.getTime());
        }

        return otherFormat.format(oldTime.getTime());// last year or older
    }
}
