package com.fastdev.fastdevlib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fastdev.fastdevlib.R;

import butterknife.ButterKnife;

/**
 * 所有Fragment的基类<br>
 *
 * @author mWX271102
 * @ClassName: BaseFragment
 * @date 2014年11月20日 上午10:12:23
 */
public abstract class BaseFragment extends Fragment implements TipsBarImpl {

    protected LayoutInflater mInflater;
    /**
     * TipsBar 子类初始化后才可以使用
     */
    protected TipsBarView tipsBarView;
    private View mView;
    private int mLayoutId;

    protected BaseFragment() {
        this.mLayoutId = getLayoutId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mInflater = inflater;

        if (null == mView) {
            mView = inflater.inflate(mLayoutId, container, false);
            if (isApplyButterKnife()) {
                ButterKnife.bind(this, mView);
            }
            initViews(mView);
            initDatas();
            bindEvents();
        } else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract boolean isApplyButterKnife();

    /**
     * 获取layout Id
     *
     * @return layout Id
     */
    protected abstract int getLayoutId();

    /**
     * 所有初始化View的地方
     */
    protected abstract void initViews(View view);

    /**
     * View绑定事件
     */
    protected abstract void bindEvents();

    /**
     * 初始化赋值操作
     */
    protected abstract void initDatas();

    @Override
    public void showTips(boolean isSuc, int messageId) {
        if (0 == messageId) {
            return;
        }
        showTips(isSuc, getActivity().getString(messageId));
    }

    @Override
    public void showTips(boolean isSuc, String message) {
        if (null == tipsBarView || null == message) {
            return;
        }
        if (isSuc) {
            tipsBarView.showSucTipsBarDelay(message, TipsBarView.COMMON_TIPS_LAST_TIME, false);
        } else {
            tipsBarView.showFailTipsBarDelay(message, TipsBarView.COMMON_TIPS_LAST_TIME,false);
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
        showProgressTipsBar(getActivity().getString(messageId));
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
        if (null == tipsBarView) {
            return;
        }
        tipsBarView.hideTipsBar();
    }

}
