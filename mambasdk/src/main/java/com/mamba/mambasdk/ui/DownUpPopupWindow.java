package com.mamba.mambasdk.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mamba.mambasdk.R;

/**
 * A subclass of {@link PopupWindow} to show a custom FDPopupWindow.
 * 
 */
@SuppressLint("ViewConstructor")
public class DownUpPopupWindow extends PopupWindow {
    private Context mContext;
    private View mMenuView;
    private LinearLayout mContentLayout;

    // 弹出框动画、背景
    private Animation showAnimation;
    private Animation hideAnimation;
    private Drawable background;
    private long duration;
    private int mMarginLeft, mMarginTop, mMarginRight, mMarginBottom;

    private Handler mHandle = new Handler();

    /**
     * Initialize the popup window.
     * 
     * @param context
     *            The context of Android operation system (OS).
     * @param txtResID
     *            The String resource ID of the FDPopupWindow title
     * @param btnResIDs
     *            The String resource ID of each button on this popup window.
     * @param btnTxtColorIDs
     *            The color resource ID of each button text.
     * @param btnBackgroundResIDs
     *            The drawable resource ID of the background for each button on
     *            this popup window.
     * @param itemsListener
     *            The OnClickListener of each button on this popup window.
     * @param keyListener
     *            The key listener to attach to this popup window
     */
    public DownUpPopupWindow(Activity context, int txtResID, int[] btnResIDs, int[] btnBackgroundResIDs,
                             int[] textColorResIDs, View.OnClickListener[] itemsListener, OnKeyListener keyListener) {
        super(context);
        this.mContext = context;

        initAnimation();

        initUI(txtResID, btnResIDs, btnBackgroundResIDs, textColorResIDs, itemsListener, keyListener);
    }

    /**
     * Initialize the popup window.
     * 
     * @param context
     *            The context of Android operation system (OS).
     * @param txtResID
     *            The String resource ID of the FDPopupWindow title
     * @param btnResIDs
     *            The String resource ID of each button on this popup window.
     * @param itemsListener
     *            The OnClickListener of each button on this popup window.
     * @param keyListener
     *            The key listener to attach to this popup window
     */
    public DownUpPopupWindow(Activity context, int txtResID, int[] btnResIDs, View.OnClickListener[] itemsListener,
                             OnKeyListener keyListener) {
        super(context);
        this.mContext = context;

        initAnimation();

        initUI(txtResID, btnResIDs, null, null, itemsListener, keyListener);
    }

    /**
     * Modify Animation
     */
    public void modifyAnimation(int styleRsId) {
        this.setAnimationStyle(styleRsId);
    }

    /*
     * Has't been used, private it
     */
    private void modifyAnimation(Animation showAnimation, Animation hideAnimation) {
        if (null != showAnimation) {
            this.showAnimation = showAnimation;
        }

        if (null != hideAnimation) {
            this.hideAnimation = hideAnimation;
            duration = hideAnimation.getDuration();
        }

    }

    /*
     * Has't been used, private it
     */
    private void cancelAnimation() {
        showAnimation = null;
        hideAnimation = null;
    }

    /*
     * Has't been used, private it
     */
    private void cancelAnimationByStyle() {
        this.setAnimationStyle(0);
    }

    /*
     * Has't been used, private it
     */
    private void modifyBackground(Drawable background) {
        if (null != background) {
            this.background = background;
            this.setBackgroundDrawable(background);
        }
    }

    @Override
    public void dismiss() {
        if (null != hideAnimation) {
            mMenuView.startAnimation(hideAnimation);

            mHandle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissPopupWindow();
                }
            }, duration);
        } else {
            dismissPopupWindow();
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (null != showAnimation) {
            // 设置弹出窗体动画效果
            mContentLayout.startAnimation(showAnimation);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (null != showAnimation) {
            // 设置弹出窗体动画效果
            mContentLayout.startAnimation(showAnimation);
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (null != showAnimation) {
            // 设置弹出窗体动画效果
            mContentLayout.startAnimation(showAnimation);
        }
        super.showAsDropDown(anchor);
    }

    /**
     * 初始化界面
     */
    private void initUI(int txtResID, int[] btnResID, int[] btnBackgroundResID, int[] textColorResID,
            View.OnClickListener[] itemsListener, OnKeyListener keyListener) {

        if (null == btnResID) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.xsp_popwindow_001_down_up, null);
        mContentLayout = (LinearLayout) mMenuView.findViewById(R.id.pop_layout);

        mMarginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, mContext.getResources()
                .getDisplayMetrics());
        mMarginRight = mMarginLeft;

        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        if (null != keyListener) {
            mMenuView.setFocusableInTouchMode(true);
            mMenuView.setOnKeyListener(keyListener);
        }

        int childViewCount = mContentLayout.getChildCount();
        // 第一个子View，固定为TextView
        if (0 != txtResID) {
            TextView titleText = (TextView) mContentLayout.getChildAt(0);
            titleText.setText(txtResID);
            titleText.setVisibility(View.VISIBLE);
        }

        if (btnResID != null && itemsListener != null && btnResID.length != 0
                && btnResID.length != itemsListener.length) {
            Toast.makeText(mContext, "resID and resID != null or length!=length!=0 ", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < btnResID.length && i < childViewCount; i++) {
            // 第一个子View，固定为TextView,button子view需要+1
            TextView btn_i = (TextView) mContentLayout.getChildAt(i + 1);
            btn_i.setText(btnResID[i]);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                mMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, mContext.getResources()
                        .getDisplayMetrics());
                mMarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext
                        .getResources().getDisplayMetrics());
                params.setMargins(mMarginLeft, mMarginTop, mMarginRight, mMarginBottom);
            } else if (i < btnResID.length - 1) {
                mMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources()
                        .getDisplayMetrics());
                mMarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext
                        .getResources().getDisplayMetrics());
                params.setMargins(mMarginLeft, mMarginTop, mMarginRight, mMarginBottom);
            } else {
                mMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources()
                        .getDisplayMetrics());
                mMarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, mContext
                        .getResources().getDisplayMetrics());
                params.setMargins(mMarginLeft, mMarginTop, mMarginRight, mMarginBottom);
            }
            btn_i.setLayoutParams(params);
            if (btnBackgroundResID != null) {
                btn_i.setBackgroundResource(btnBackgroundResID[i]);
            }
            if (textColorResID != null) {
                btn_i.setTextColor(mContext.getResources().getColor(textColorResID[i]));
            }
            btn_i.setOnClickListener(itemsListener[i]);
            btn_i.setVisibility(View.VISIBLE);
        }

        // 设置XSPDownUpPopupWindow的View
        this.setContentView(mMenuView);
        // 设置XSPDownUpPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置XSPDownUpPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置XSPDownUpPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置XSPDownUpPopupWindow背景动画
        // this.setAnimationStyle(R.style.AnimFade);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0x50000000);
        // 设置XSPDownUpPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initAnimation() {
        showAnimation = AnimationUtils.loadAnimation(mContext, R.anim.call_dial_keyboard_show);
        hideAnimation = AnimationUtils.loadAnimation(mContext, R.anim.call_dial_keyboard_hide);
        duration = hideAnimation.getDuration();
    }

    /**
     * 调用PopupWindow的dismiss方法
     */
    private void dismissPopupWindow() {
        try {
            super.dismiss();
        } catch (Exception e) {
        }
    }
}
