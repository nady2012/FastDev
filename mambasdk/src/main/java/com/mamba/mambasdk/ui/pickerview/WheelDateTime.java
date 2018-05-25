package com.mamba.mambasdk.ui.pickerview;

import android.util.Log;
import android.view.View;

import com.mamba.mambasdk.R;
import com.mamba.mambasdk.ui.pickerview.adapter.NumericWheelAdapter;
import com.mamba.mambasdk.ui.pickerview.lib.WheelView;
import com.mamba.mambasdk.ui.pickerview.listener.OnItemSelectedListener;
import com.mamba.mambasdk.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * The type Wheel date time.
 */
public class WheelDateTime {
    /**
    * The Text color out.
    */
    //文字的颜色和分割线的颜色
    int textColorOut;
    /**
    * The Text color center.
    */
    int textColorCenter;
    /**
    * The Divider color.
    */
    int dividerColor;
    /**
    * The Line spacing multiplier.
    */
    // 条目间距倍数
    float lineSpacingMultiplier = 1.6F;
    private View view;
    private WheelView wvDayOfYear;
    private WheelView wvHours;
    private WheelView wvMins;
    private WheelView wvSeconds;
    private int gravity;
    private boolean[] type;
    private CustomDate currentCalendar;
    private CustomDate startCalendar;
    private CustomDate endCalendar;
    private int textSize = 18;
    private WheelView.DividerType dividerType;

    /**
    * Instantiates a new Wheel date time.
    *
    * @param view the view
    */
    public WheelDateTime(View view) {
        super();
        this.view = view;
        type = new boolean[] { true, true, true, true };
        setView(view);
    }

    /**
    * Instantiates a new Wheel date time.
    *
    * @param view the view
    * @param type the type
    * @param gravity the gravity
    * @param textSize the text size
    */
    public WheelDateTime(View view, boolean[] type, int gravity, int textSize) {
        super();
        this.view = view;
        this.type = type;
        this.gravity = gravity;
        this.textSize = textSize;
        setView(view);
    }

    /**
    * Sets picker.
    *
    * @param calendar the calendar
    * @param h the h
    * @param m the m
    * @param s the s
    */
    public void setPicker(CustomDate calendar, int h, int m, int s) {
        currentCalendar = calendar;
        wvDayOfYear = (WheelView) view.findViewById(R.id.day_of_year);
        wvDayOfYear.setAdapter(new DateWheelAdapter(startCalendar, endCalendar));
        wvDayOfYear.setCurrentItem(
                TimeUtil.daysBetween(startCalendar.getDate(), currentCalendar.getDate()));// 初始化时显示的数据
        wvDayOfYear.setGravity(gravity);
        //时
        wvHours = (WheelView) view.findViewById(R.id.hour);
        wvHours.setAdapter(new NumericWheelAdapter(0, 23));
        wvHours.setCurrentItem(h);
        wvHours.setGravity(gravity);
        //分
        wvMins = (WheelView) view.findViewById(R.id.min);
        wvMins.setAdapter(new NumericWheelAdapter(0, 59));
        wvMins.setCurrentItem(m);
        wvMins.setGravity(gravity);
        //秒
        wvSeconds = (WheelView) view.findViewById(R.id.second);
        wvSeconds.setAdapter(new NumericWheelAdapter(0, 59));
        wvSeconds.setCurrentItem(s);
        wvSeconds.setGravity(gravity);

        wvDayOfYear.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.d("kaven", "onItemSelected :" + index);
                long timeInMillis = startCalendar.getDate().getTime() + (1000L * 3600 * 24 * index);
                currentCalendar.getDate().setTime(timeInMillis);
                currentCalendar.setDayIndex(index);
            }
        });
        if (type.length != 4) {
            throw new RuntimeException("type[] length is not 4");
        }
        wvDayOfYear.setVisibility(type[0] ? View.VISIBLE : View.GONE);
        wvHours.setVisibility(type[1] ? View.VISIBLE : View.GONE);
        wvMins.setVisibility(type[2] ? View.VISIBLE : View.GONE);
        wvSeconds.setVisibility(type[3] ? View.VISIBLE : View.GONE);
        setContentTextSize();
    }

    private void setContentTextSize() {
        wvDayOfYear.setTextSize(textSize);
        wvHours.setTextSize(textSize);
        wvMins.setTextSize(textSize);
        wvSeconds.setTextSize(textSize);
    }

    private void setTextColorOut() {
        wvDayOfYear.setTextColorOut(textColorOut);
        wvHours.setTextColorOut(textColorOut);
        wvMins.setTextColorOut(textColorOut);
        wvSeconds.setTextColorOut(textColorOut);
    }

    private void setTextColorCenter() {
        wvDayOfYear.setTextColorCenter(textColorCenter);
        wvHours.setTextColorCenter(textColorCenter);
        wvMins.setTextColorCenter(textColorCenter);
        wvSeconds.setTextColorCenter(textColorCenter);
    }

    private void setDividerColor() {
        wvDayOfYear.setDividerColor(dividerColor);
        wvHours.setDividerColor(dividerColor);
        wvMins.setDividerColor(dividerColor);
        wvSeconds.setDividerColor(dividerColor);
    }

    private void setDividerType() {
        wvDayOfYear.setDividerType(dividerType);
        wvHours.setDividerType(dividerType);
        wvMins.setDividerType(dividerType);
        wvSeconds.setDividerType(dividerType);

    }

    private void setLineSpacingMultiplier() {
        wvDayOfYear.setLineSpacingMultiplier(lineSpacingMultiplier);
        wvHours.setLineSpacingMultiplier(lineSpacingMultiplier);
        wvMins.setLineSpacingMultiplier(lineSpacingMultiplier);
        wvSeconds.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    /**
    * Sets labels.
    *
    * @param labelHours the label hours
    * @param labelMins the label mins
    * @param labelSeconds the label seconds
    */
    public void setLabels(String labelHours, String labelMins, String labelSeconds) {
        if (labelHours != null) {
            wvHours.setLabel(labelHours);
        } else {
            wvHours.setLabel(view.getContext().getString(R.string.pickerview_hours));
        }
        if (labelMins != null) {
            wvMins.setLabel(labelMins);
        } else {
            wvMins.setLabel(view.getContext().getString(R.string.pickerview_minutes));
        }
        if (labelSeconds != null) {
            wvSeconds.setLabel(labelSeconds);
        } else {
            wvSeconds.setLabel(view.getContext().getString(R.string.pickerview_seconds));
        }

    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic the cyclic
    */
    public void setCyclic(boolean cyclic) {
        wvDayOfYear.setCyclic(cyclic);
        wvHours.setCyclic(cyclic);
        wvMins.setCyclic(cyclic);
        wvSeconds.setCyclic(cyclic);
    }

    /**
    * Gets time.
    *
    * @return the time
    */
    public String getTime() {
        StringBuilder sb = new StringBuilder();
        int currentItem = wvDayOfYear.getCurrentItem();
        CustomDate customDate = (CustomDate) wvDayOfYear.getAdapter().getItem(currentItem);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());

        sb.append(dateFormat.format(customDate.getDate())).append(wvHours.getCurrentItem())
                .append(":").append(wvMins.getCurrentItem()).append(":")
                .append(wvSeconds.getCurrentItem());

        return sb.toString();
    }

    /**
    * Gets view.
    *
    * @return the view
    */
    public View getView() {
        return view;
    }

    /**
    * Sets view.
    *
    * @param view the view
    */
    public void setView(View view) {
        this.view = view;
    }

    /**
    * Sets rang date.
    *
    * @param startDate the start date
    * @param endDate the end date
    */
    public void setRangDate(CustomDate startDate, CustomDate endDate) {
        this.startCalendar = startDate;
        this.endCalendar = endDate;
    }

    /**
     * 设置间距倍数,但是只能在1.0-2.0f之间
     *
     * @param lineSpacingMultiplier the line spacing multiplier
    */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor the divider color
    */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setDividerColor();
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType the divider type
    */
    public void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        setDividerType();
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter the text color center
    */
    public void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut the text color out
    */
    public void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        setTextColorOut();
    }

    /**
     * Label 是否只显示中间选中项的
     *
     * @param isCenterLabel the is center label
    */
    public void isCenterLabel(Boolean isCenterLabel) {

        wvDayOfYear.isCenterLabel(isCenterLabel);
        wvHours.isCenterLabel(isCenterLabel);
        wvMins.isCenterLabel(isCenterLabel);
        wvSeconds.isCenterLabel(isCenterLabel);
    }
}
