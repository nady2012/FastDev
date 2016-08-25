package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fastdev.fastdevlib.R;


/**
 * A subclass of {@link RelativeLayout}, used to show a bar with tips message or
 * progress message.
 * 
 * <p>
 * <strong>XML attributes</strong>
 * </p>
 * 
 * @attr ref {@link com.huawei.xs.widget.R.styleable#tips_bar}
 * @attr ref {@link com.huawei.xs.widget.R.styleable#tips_bar_icon_left}
 * @attr ref {@link com.huawei.xs.widget.R.styleable#tips_bar_show_progress}
 * @attr ref
 *       {@link com.huawei.xs.widget.R.styleable#tips_bar_text_message_detail}
 * @attr ref
 *       {@link com.huawei.xs.widget.R.styleable#tips_bar_text_message_title}
 * 
 */
public class TipsBarView extends RelativeLayout {

    private TextView txtMessageTitle;
    private TextView txtMessageDetail;
    private ProgressBar progressBar;
    private ImageView imgLeftIcon;

    private Drawable leftIconDrawable;
    private boolean isShowProgressBar;
    private boolean isExcuteDefaultAnim = true;
    private String tipsMessageTitle;
    private String tipsMessageDetail;

    private int defaultAnimDuration = 500;

    private Animation showTipsAnim;

    private Animation hideTipsAnim;

    private Context context;

    /**
     * 默认显示的时间
     */
    public static final int COMMON_TIPS_LAST_TIME = 2 * 1000;

    private OnTipsBarVisibilityChanged onTipsBarVisibilityChanged;

    public TipsBarView(Context context) {
        this(context, null);
    }

    public TipsBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipsBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initCustomAttribute(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.xsp_tips_bar, this, true);
        bringToFront();
    }

    public void setOnTipsBarVisibilityChanged(OnTipsBarVisibilityChanged onTipsBarVisibilityChanged) {
        this.onTipsBarVisibilityChanged = onTipsBarVisibilityChanged;
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        imgLeftIcon.setVisibility(View.GONE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    public boolean isProgressShow() {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public void setLeftIconResource(int resId) {
        progressBar.setVisibility(View.GONE);

        imgLeftIcon.setImageResource(resId);
        imgLeftIcon.setVisibility(View.VISIBLE);
    }

    public void setLeftIconDrawable(Drawable drawable) {
        progressBar.setVisibility(View.GONE);

        imgLeftIcon.setImageDrawable(drawable);
        imgLeftIcon.setVisibility(View.VISIBLE);
    }

    public void setLeftIconBitmap(Bitmap bitmap) {
        progressBar.setVisibility(View.GONE);

        imgLeftIcon.setImageBitmap(bitmap);
        imgLeftIcon.setVisibility(View.VISIBLE);
    }

    public void setMessage(String title) {
        setMessage(title, R.color.black, null, 0);
    }

    public void setMessage(String title, String detail) {
        setMessage(title, R.color.black, detail, 0);
    }

    public void setMessage(String title, int tiltleColor, String detail, int detailColor) {
        if (!TextUtils.isEmpty(title)) {
            txtMessageTitle.setText(title);
        }

        if (tiltleColor != 0) {
            txtMessageTitle.setTextColor(context.getResources().getColor(tiltleColor));
        } else {
            txtMessageTitle.setTextColor(context.getResources().getColor(R.color.gray));
        }

        if (!TextUtils.isEmpty(detail)) {
            txtMessageDetail.setVisibility(View.VISIBLE);
            txtMessageDetail.setText(detail);
            if (detailColor != 0) {
                txtMessageTitle.setTextColor(context.getResources().getColor(detailColor));
            }
        } else {
            txtMessageDetail.setVisibility(View.GONE);

        }
    }

    public void setMessage(int titleId) {
        setMessage(titleId, R.color.black, 0, 0);
    }

    public void setMessage(int titleId, int detailId) {
        setMessage(titleId, R.color.black, 0, 0);
    }

    public void setMessage(int titleId, int tiltleColor, int detailId, int detailColor) {
        if (titleId != 0) {
            txtMessageTitle.setText(titleId);
        }

        if (tiltleColor != 0) {
            txtMessageTitle.setTextColor(context.getResources().getColor(tiltleColor));
        } else {
            txtMessageTitle.setTextColor(context.getResources().getColor(R.color.gray));
        }

        if (detailId != 0) {
            txtMessageDetail.setVisibility(View.VISIBLE);
            txtMessageDetail.setText(detailId);
            if (detailColor != 0) {
                txtMessageTitle.setTextColor(context.getResources().getColor(detailColor));
            }
        } else {
            txtMessageDetail.setVisibility(View.GONE);
        }
    }

    public TextView getMessageTitleTextView() {
        return txtMessageTitle;
    }

    public TextView getMessageDetailTextView() {
        return txtMessageDetail;
    }

    public void excuteDefalutAnimation(boolean isExcuteDefaultAnim) {
        this.isExcuteDefaultAnim = isExcuteDefaultAnim;
    }

    public void setDefaultAnimationDuration(int duration) {
        this.defaultAnimDuration = duration;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initProgressBar();
        initLeftIcon();
        initTipsMessage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#setVisibility(int)
     */
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            bringToFront();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    private void initCustomAttribute(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.tips_bar, defStyle, 0);
        isShowProgressBar = typedArray.getBoolean(R.styleable.tips_bar_show_progress, false);
        leftIconDrawable = typedArray.getDrawable(R.styleable.tips_bar_icon_left);
        tipsMessageTitle = typedArray.getString(R.styleable.tips_bar_text_message_title);
        tipsMessageDetail = typedArray.getString(R.styleable.tips_bar_text_message_detail);
        typedArray.recycle();
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.tips_ring_loading);
        progressBar.setVisibility(isShowProgressBar ? View.VISIBLE : View.GONE);
    }

    private void initLeftIcon() {
        imgLeftIcon = (ImageView) findViewById(R.id.tips_left_icon);
        if (null != leftIconDrawable) {
            imgLeftIcon.setImageDrawable(leftIconDrawable);
            imgLeftIcon.setVisibility(View.VISIBLE);
        }

    }

    private void initTipsMessage() {
        txtMessageTitle = (TextView) findViewById(R.id.tips_message_title);
        txtMessageDetail = (TextView) findViewById(R.id.tips_message_detail);
        if (!TextUtils.isEmpty(tipsMessageTitle)) {
            txtMessageTitle.setText(tipsMessageTitle);
        }

        if (!TextUtils.isEmpty(tipsMessageDetail)) {
            txtMessageDetail.setVisibility(View.VISIBLE);
            txtMessageDetail.setText(tipsMessageDetail);
            txtMessageTitle.setTextColor(context.getResources().getColor(R.color.hy_col_1));
        } else {
            txtMessageDetail.setVisibility(View.GONE);
            txtMessageTitle.setTextColor(context.getResources().getColor(R.color.hy_col_1));
        }
    }

    private void excuteDefalutInAnim() {
        // TranslateAnimation in = new TranslateAnimation(0, 0,
        // -this.getHeight(),
        // 0);
        // in.setDuration(defaultAnimDuration);
        // in.setFillAfter(true);

        if (null == showTipsAnim) {
            showTipsAnim = AnimationUtils.loadAnimation(getContext(), R.anim.tips_show);
            showTipsAnim.setAnimationListener(animationListener);
        }
        this.startAnimation(showTipsAnim);
    }

    private void excuteDefalutOutAnim() {
        // TranslateAnimation out = new TranslateAnimation(0, 0, 0,
        // -this.getHeight());
        // out.setDuration(defaultAnimDuration);

        if (null == hideTipsAnim) {
            hideTipsAnim = AnimationUtils.loadAnimation(getContext(), R.anim.tips_hide);
            hideTipsAnim.setAnimationListener(animationListener);
        }
        this.startAnimation(hideTipsAnim);
    }

    public boolean isTipsShowing() {
        return this.getVisibility() == View.VISIBLE;
    }

    public void showProgressBarTipsBar(String message) {
        setMessage(message);
        showProgress();
        showTipsBar();
    }

    public void showProgressBarTipsBar(int resId) {
        showProgress();
        setMessage(resId);
        showTipsBar();
    }

    public void showSucTipsBar(String message, boolean bLeftIcon) {
        hideProgress();
        setMessage(message);
        if (bLeftIcon){
            setLeftIconResource(R.drawable.common_tips_complete);
        }

        showTipsBar();
    }

    public void showSucTipsBarDelay(String message, int showTime, boolean bLeftIcon) {
        hideProgress();
        setMessage(message);
        if (bLeftIcon){
            setLeftIconResource(R.drawable.common_tips_complete);
        }

        showTipsBarDelay(showTime);
    }

    public void showSucTipsBarDelay(int messageId, int showTime, boolean bLeftIcon) {
        hideProgress();
        setMessage(messageId);
        if (bLeftIcon){
            setLeftIconResource(R.drawable.common_tips_complete);
        }

        showTipsBarDelay(showTime);
    }

    public void showFailTipsBar(String message, boolean bLeftIcon) {
        hideProgress();
        setMessage(message, R.color.hy_col_1, null, 0);
        if (bLeftIcon){
            setLeftIconResource(R.drawable.common_tips_error_icon);
        }

        showTipsBarDelay();
    }

    public void showFailTipsBarDelay(String message, int showTime, boolean bLeftIcon) {
        hideProgress();
        setMessage(message, R.color.hy_col_1, null, 0);
        if (bLeftIcon){
            setLeftIconResource(R.drawable.common_tips_error_icon);
        }
        showTipsBarDelay(showTime);
    }

    public void showFailTipsBar(int messageId, boolean bLeftIcon) {
        hideProgress();
        setMessage(0, R.color.hy_col_1, messageId, 0);
        if (bLeftIcon){
            setLeftIconResource(R.drawable.common_tips_error_icon);
        }
        showTipsBarDelay();
    }

    public void showFailTipsBarDelay(int messageId, int showTime,  boolean bLeftIcon) {
        hideProgress();
        setMessage(0, R.color.hy_col_1, messageId, 0);
        if (bLeftIcon){
            setLeftIconResource(R.drawable.common_tips_error_icon);
        }
        showTipsBarDelay(showTime);
    }

    public void showTipsBar() {
        if (isTipsShowing()) {
            mHandler.removeCallbacks(hideTipsBarRunnable);
            return;
        }
        mHandler.removeCallbacks(hideTipsBarRunnable);
        setVisibility(View.VISIBLE);
        if (isExcuteDefaultAnim) {
            excuteDefalutInAnim();
        }
        if (onTipsBarVisibilityChanged != null) {
            onTipsBarVisibilityChanged.onTipsBarVisibilityChanged(true);
        }
    }

    public void hideTipsBar() {
        if (!isTipsShowing()) {
            return;
        }
        if (isExcuteDefaultAnim) {
            excuteDefalutOutAnim();
        }
        mHandler.removeCallbacks(hideTipsBarRunnable);
        setVisibility(View.GONE);
        if (onTipsBarVisibilityChanged != null) {
            onTipsBarVisibilityChanged.onTipsBarVisibilityChanged(false);
        }
    }

    public void hideTipsBarDelay() {
        if (!isTipsShowing()) {
            return;
        }
        mHandler.removeCallbacks(hideTipsBarRunnable);
        mHandler.postDelayed(hideTipsBarRunnable, defaultAnimDuration);
    }

    private Handler mHandler = new Handler();

    public void showTipsBarDelay() {
        if (isTipsShowing()) {
            mHandler.removeCallbacks(hideTipsBarRunnable);
            mHandler.postDelayed(hideTipsBarRunnable, COMMON_TIPS_LAST_TIME);
            return;
        }
        setVisibility(View.VISIBLE);
        if (isExcuteDefaultAnim) {
            excuteDefalutInAnim();
        }
        mHandler.removeCallbacks(hideTipsBarRunnable);
        mHandler.postDelayed(hideTipsBarRunnable, COMMON_TIPS_LAST_TIME);
        if (onTipsBarVisibilityChanged != null) {
            onTipsBarVisibilityChanged.onTipsBarVisibilityChanged(true);
        }
    }

    public void showTipsBarDelay(int showTime) {
        if (isTipsShowing()) {
            mHandler.removeCallbacks(hideTipsBarRunnable);
            mHandler.postDelayed(hideTipsBarRunnable, showTime);
            return;
        }
        setVisibility(View.VISIBLE);
        if (isExcuteDefaultAnim) {
            excuteDefalutInAnim();
        }
        mHandler.removeCallbacks(hideTipsBarRunnable);
        mHandler.postDelayed(hideTipsBarRunnable, showTime);
        if (onTipsBarVisibilityChanged != null) {
            onTipsBarVisibilityChanged.onTipsBarVisibilityChanged(true);
        }
    }

    Runnable hideTipsBarRunnable = new Runnable() {

        @Override
        public void run() {
            if (isExcuteDefaultAnim) {
                excuteDefalutOutAnim();
            } else {
                setVisibility(View.GONE);
            }
            if (onTipsBarVisibilityChanged != null) {
                onTipsBarVisibilityChanged.onTipsBarVisibilityChanged(false);
            }
        }

    };

    AnimationListener animationListener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation arg0) {

        }

        @Override
        public void onAnimationRepeat(Animation arg0) {

        }

        @Override
        public void onAnimationEnd(Animation anim) {
            if (anim == showTipsAnim) {
                setVisibility(View.VISIBLE);
            } else if (anim == hideTipsAnim) {
                setVisibility(View.GONE);
            }
        }
    };

    public interface OnTipsBarVisibilityChanged {
        void onTipsBarVisibilityChanged(boolean isVisibility);
        // void onTipsBarProgressBarVisibilityChanged(boolean isVisibility);
    }
}
