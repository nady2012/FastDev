package com.mamba.mambasdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.mamba.mambasdk.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity implements TipsBarImpl {
    /**
     * 当前页面对象 方便调用
     */
    protected Activity mContext;
    /**
     * TipsBar 子类初始化后才可以使用
     */
    protected TipsBarView tipsBarView;

    @Override
    protected void onCreate(Bundle bundle) {
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
        super.onCreate(bundle);
        mContext = BaseActivity.this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        int layoutResID = getInflateLayoutId();
        if (layoutResID <= 0) {
            finish();
        }
        setContentView(layoutResID);

        initPresenter();

        Intent intent = getIntent();
        if (intent != null) {
            getIntentData(intent);
        }

        if (bundle != null) {
            getSavedBundleData(bundle);
        }

        initViews();
        bindEvents();
        initDatas();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (isApplyButterKnife()) {
            ButterKnife.bind(this);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }

    /**
     * toggle overridePendingTransition
     *
     * @return
     */
    protected abstract boolean toggleOverridePendingTransition();

    /**
     * get the overridePendingTransition mode
     */
    protected abstract TransitionMode getOverridePendingTransitionMode();

    protected abstract boolean isApplyButterKnife();

    /**
     * 获取layout Id
     *
     * @return layout Id
     */
    protected abstract int getInflateLayoutId();

    protected abstract void initPresenter();

    protected abstract void getIntentData(Intent intent);

    protected abstract void getSavedBundleData(Bundle bundle);

    /**
     * 所有初始化View的地方
     */
    protected abstract void initViews();

    /**
     * View绑定事件
     */
    protected abstract void bindEvents();

    /**
     * 初始化赋值操作
     */
    protected abstract void initDatas();

    @Override
    protected void onDestroy() {
        hideTips();
        super.onDestroy();
    }

    @Override
    public void showTips(boolean isSuc, int messageId) {
        if (0 == messageId) {
            return;
        }
        showTips(isSuc, mContext.getString(messageId));
    }

    @Override
    public void showTips(boolean isSuc, String message) {
        if (null == tipsBarView || null == message) {
            return;
        }
        if (isSuc) {
            tipsBarView.showSucTipsBarDelay(message, TipsBarView.COMMON_TIPS_LAST_TIME, false);
        } else {
            tipsBarView.showFailTipsBarDelay(message, TipsBarView.COMMON_TIPS_LAST_TIME, false);
        }
    }

    @Override
    public void showProgressTipsBar() {
        showProgressTipsBar(R.string.str_base_response_waiting);
    }

    @Override
    public void showProgressTipsBar(int messageId) {
        if (0 == messageId) {
            return;
        }
        showProgressTipsBar(mContext.getString(messageId));
    }

    @Override
    public void showProgressTipsBar(String message) {
        if (null == tipsBarView || null == message) {
            return;
        }
        tipsBarView.showProgressBarTipsBar(message);
    }

    @Override
    public void hideTips() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == tipsBarView) {
                    return;
                }
                tipsBarView.hideTipsBar();
            }
        });

    }

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }
}
