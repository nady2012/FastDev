package com.mamba.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.mamba.mambasdk.ui.BaseActivity;
import com.mamba.mambasdk.ui.pickerview.CustomDate;
import com.mamba.mambasdk.ui.pickerview.DateTimePickerView;
import com.mamba.mambasdk.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shijunfeng on 2018/5/25.
 */
public class TimePickDemoActivity extends BaseActivity implements DateTimePickerView.OnTimeSelectListener {
    private DateTimePickerView timePicker;

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected boolean isApplyButterKnife() {
        return false;
    }

    @Override
    protected int getInflateLayoutId() {
        return R.layout.layout_timepicker_demo;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    @Override
    protected void getSavedBundleData(Bundle bundle) {

    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void bindEvents() {

    }

    @Override
    protected void initDatas() {

    }

    private void showTimePicker(int offset) {
        Date currentDate = new Date(System.currentTimeMillis());
        try {

            Calendar instance = Calendar.getInstance();
            instance.setTime(currentDate);
            if (offset > 0) {
                instance.add(Calendar.DATE, -offset);
            } else {
                instance.add(Calendar.DATE, -1);
            }

            Date endDate = instance.getTime();

            CustomDate startCustomDate = new CustomDate(endDate,
                TimeUtil.daysBetween(currentDate, endDate));

            CustomDate endCustomDate = new CustomDate(currentDate);

            timePicker = new DateTimePickerView.Builder(this, this)
                .setCancelText(getResources().getString(R.string.cancel))
                .setCancelColor(getResources().getColor(R.color.cor4))
                .setSubmitText(getResources().getString(R.string.ok)).setContentSize(18)
                .setSubmitColor(getResources().getColor(R.color.cor1)).setTitleSize(16)
                .setTitleText("").setTitleColor(getResources().getColor(R.color.cor6))
                .setDividerColor(getResources().getColor(R.color.lincor2))
                .setTextColorCenter(getResources().getColor(R.color.cor1))//设置选中项的颜色
                .setTextColorOut(getResources().getColor(R.color.cor3))
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(getResources().getColor(R.color.cor6))
                .setBgColor(getResources().getColor(R.color.cor6)).gravity(Gravity.CENTER)
                .setType(new boolean[]{true, true, true, true}).isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabel("点", "分", "秒").isCyclic(false)
                .setRangDate(startCustomDate, endCustomDate).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        timePicker.setInAnim(AnimationUtils.loadAnimation(this,
            R.anim.enter_down_to_up));
        timePicker.setOutAnim(AnimationUtils.loadAnimation(this,
            R.anim.exit_up_to_down));
        timePicker.show(true);

    }

    private void dismissTimePicker() {
        if (null != timePicker) {
            timePicker.dismiss();
            timePicker = null;
        }
    }

    public void onTimePickerShowClick(View view) {
        showTimePicker(7);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissTimePicker();
    }

    @Override
    public void onTimeSelect(Date date, View v) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
        Toast.makeText(this, "select time <" + format.format(date) + ">", Toast.LENGTH_SHORT).show();
        dismissTimePicker();
    }
}
