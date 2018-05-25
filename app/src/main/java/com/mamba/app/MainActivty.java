package com.mamba.app;

import android.content.Intent;
import android.view.View;

import com.mamba.mambasdk.ui.BaseFragmentActivity;

/**
 * Created by shijunfeng on 2018/5/22.
 */
public class MainActivty extends BaseFragmentActivity{
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
        return R.layout.layout_main;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void getIntentData(Intent intent) {

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

    public void onMamaClick(View view) {
        startActivity(new Intent(this,VideoDemoActivity.class));
    }
}
