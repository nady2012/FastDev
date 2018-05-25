package com.mamba.mambasdk.ui.pickerview;


import com.mamba.mambasdk.ui.pickerview.adapter.WheelAdapter;

import java.util.Calendar;

/**
 * Created by shijunfeng on 2017/6/20.
 */
public class DateWheelAdapter implements WheelAdapter {
    // Values
    private CustomDate minValue;
    private CustomDate maxValue;

    /**
     * Constructor
     *
     * @param minValue the wheel min value
    * @param maxValue the wheel max value
    */
    public DateWheelAdapter(CustomDate minValue, CustomDate maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
    * Gets item.
    *
    * @param index the index
    * @return the item
    */
    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(minValue.getDate());
            calendar.add(Calendar.DAY_OF_YEAR, index);
            return new CustomDate(calendar.getTime(), minValue.getDayIndex() + index);
        }
        return minValue;
    }

    /**
    * Gets items count.
    *
    * @return the items count
    */
    @Override
    public int getItemsCount() {
        return maxValue.getDayIndex() - minValue.getDayIndex() + 1;
    }

    /**
    * Index of int.
    *
    * @param o the o
    * @return the int
    */
    @Override
    public int indexOf(Object o) {
        try {
            CustomDate customDate = (CustomDate) o;
            return customDate.getDayIndex() - minValue.getDayIndex();
        } catch (Exception e) {
            return -1;
        }

    }
}
