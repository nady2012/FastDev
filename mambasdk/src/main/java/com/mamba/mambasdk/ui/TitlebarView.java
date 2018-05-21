package com.mamba.mambasdk.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mamba.mambasdk.R;

public class TitlebarView extends RelativeLayout {

    public static final String TAG_LEFT_BUTTON = "tag_left_button";
    public static final String TAG_TITLE_TEXTVIEW = "tag_title_textview";
    public static final String TAG_RIGHT_BUTTON = "tag_right_button";
    public static final String TAG_LEFT_TITLE_TEXTVIEW = "tag_left_title_textview";
    private Drawable backgroundDrawable, leftButtonDrawable, titleDrawable, rightButtonDrawable, leftTitleDrawable;
    private String leftButtonStr, titleStr, rightButtonStr, leftTitleStr;
    private int leftTitleColor;
    private TextView leftButtonTv, titleTv, rightButtonTv, leftTitleTv;
    private OnTitleBarClickEvent mTitleBarOnClickEvent;

    public TitlebarView(Context context) {
        this(context, null);
    }

    public TitlebarView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TitlebarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.title, defStyle, 0);

        backgroundDrawable = a.getDrawable(R.styleable.title_backGround);
        leftButtonDrawable = a.getDrawable(R.styleable.title_leftIcon);
        titleDrawable = a.getDrawable(R.styleable.title_titleIcon);
        leftTitleDrawable = a.getDrawable(R.styleable.title_leftTitleIcon);
        rightButtonDrawable = a.getDrawable(R.styleable.title_rightIcon);

        leftButtonStr = a.getString(R.styleable.title_leftLabel);
        titleStr = a.getString(R.styleable.title_leftTitleLabel);
        leftTitleStr = a.getString(R.styleable.title_leftTitleLabel);
        rightButtonStr = a.getString(R.styleable.title_rightLabel);

        leftTitleColor = a.getColor(R.styleable.title_leftTitleColor, getResources().getColor(R.color.hy_col_1));

        LayoutInflater.from(context).inflate(R.layout.xsp_view_title_bar, this, true);

        initBackground();

        initLeftButton();

        initTitle();

        initLeftTitle();

        initRightButton();
        a.recycle();
    }

    public void setOnTitleBarClickEvent(OnTitleBarClickEvent titleBarOnClickEvent) {
        this.mTitleBarOnClickEvent = titleBarOnClickEvent;
    }

    private void setBounds(Drawable icon) {
        icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
    }

    private void initBackground() {
        if (backgroundDrawable != null){
            this.setBackgroundDrawable(backgroundDrawable);
        }else {
            this.setBackgroundColor(getResources().getColor(R.color.transparent));
        }


    }

    private void initLeftTitle() {
        leftTitleTv = (TextView) findViewById(R.id.tv_left_title);
        leftTitleTv.setTag(TAG_LEFT_TITLE_TEXTVIEW);

        if (null != leftTitleDrawable) {
            setIcon(leftTitleTv, leftTitleDrawable);
            setOnClickEvent(leftTitleTv);
        }

        if (!TextUtils.isEmpty(leftTitleStr)) {
            leftTitleTv.setText(leftTitleStr);
        }

        leftTitleTv.setTextColor(leftTitleColor);
    }

    private void initLeftButton() {
        leftButtonTv = (TextView) findViewById(R.id.tv_left_button);
        leftButtonTv.setTag(TAG_LEFT_BUTTON);

        if (null != leftButtonDrawable) {
            setIcon(leftButtonTv, leftButtonDrawable);
        } else if (!TextUtils.isEmpty(leftButtonStr)) {
            leftButtonTv.setText(leftButtonStr);
        } else {
            leftButtonTv.setVisibility(View.GONE);
        }

        setOnClickEvent(leftButtonTv);
    }

    private void initTitle() {
        titleTv = (TextView) findViewById(R.id.tv_title);
        titleTv.setTag(TAG_TITLE_TEXTVIEW);

        if (null != titleDrawable) {
            setIcon(titleTv, titleDrawable);
            setOnClickEvent(titleTv);
        }

        if (!TextUtils.isEmpty(titleStr)) {
            titleTv.setText(titleStr);
        }
    }

    public void setTitleLongClick(OnLongClickListener onLongClickListener) {
        if (null == onLongClickListener) {
            return;
        }
        if (null == titleTv) {
            return;
        }
        titleTv.setLongClickable(true);
        titleTv.setOnLongClickListener(onLongClickListener);
    }

    public void setLeftTitleLongClick(OnLongClickListener onLongClickListener) {
        if (null == onLongClickListener) {
            return;
        }
        if (null == leftTitleTv) {
            return;
        }
        leftTitleTv.setLongClickable(true);
        leftTitleTv.setOnLongClickListener(onLongClickListener);
    }

    public void setLeftIcon(int rsid) {
        if (null == leftButtonTv) {
            return;
        }
        leftButtonTv.setVisibility(View.VISIBLE);
        Drawable drawable = getResources().getDrawable(rsid);
        setIcon(leftButtonTv, drawable);
    }

    public void setRightIcon(int rsid) {
        if (null == rightButtonTv) {
            return;
        }
        rightButtonTv.setVisibility(View.VISIBLE);
        Drawable drawable = getResources().getDrawable(rsid);
        setIcon(rightButtonTv, drawable);
    }

    public void setRightLabel(String label) {
        if (null == rightButtonTv) {
            return;
        }
        rightButtonTv.setVisibility(View.VISIBLE);
        rightButtonTv.setText(label);
    }

    public void setRightEnable(boolean isEnable) {
        if (null == rightButtonTv) {
            return;
        }
        rightButtonTv.setEnabled(isEnable);
    }

    public void setRightInvisible() {
        rightButtonTv.setVisibility(View.INVISIBLE);
    }

    public void setRightVisible() {
        rightButtonTv.setVisibility(View.VISIBLE);
    }

    public void setTitleIcon(int rsid) {
        if (null == titleTv) {
            return;
        }
        Drawable drawable = getResources().getDrawable(rsid);
        setIcon(titleTv, drawable);
        setOnClickEvent(titleTv);
    }

    public void setLeftTitleIcon(int rsid) {
        if (null == leftTitleTv) {
            return;
        }
        Drawable drawable = getResources().getDrawable(rsid);
        setIcon(leftTitleTv, drawable);
        setOnClickEvent(leftTitleTv);
    }

    public void setTitle(int titleRes) {
        if (null == titleTv) {
            return;
        }
        titleTv.setText(titleRes);
    }

    public void setLeftTitle(int leftTitleRes) {
        if (null == leftTitleTv) {
            return;
        }
        leftTitleTv.setText(leftTitleRes);
    }

    public void setTitle(String titleStr) {
        if (null == titleTv) {
            return;
        }
        titleTv.setText(titleStr);
    }

    public void setLeftTitle(String leftTitleStr) {
        if (null == leftTitleTv) {
            return;
        }
        leftTitleTv.setText(leftTitleStr);
    }

    public void setTitle(CharSequence titleChar) {
        if (null == titleChar) {
            return;
        }
        titleTv.setText(titleChar);
    }

    public void setLeftTitle(CharSequence leftTitleChar) {
        if (null == leftTitleChar) {
            return;
        }
        leftTitleTv.setText(leftTitleChar);
    }

    public void setTitleEllipsize(TruncateAt where) {
        if (null == titleTv) {
            return;
        }
        titleTv.setEllipsize(where);
    }

    public void setLeftTitleEllipsize(TruncateAt where) {
        if (null == leftTitleTv) {
            return;
        }
        leftTitleTv.setEllipsize(where);
    }

    private void initRightButton() {
        rightButtonTv = (TextView) findViewById(R.id.tv_right_button);
        rightButtonTv.setTag(TAG_RIGHT_BUTTON);

        if (null != rightButtonDrawable) {
            setIcon(rightButtonTv, rightButtonDrawable);
        } else if (!TextUtils.isEmpty(rightButtonStr)) {
            rightButtonTv.setText(rightButtonStr);
        } else {
            rightButtonTv.setVisibility(View.INVISIBLE);
        }

        setOnClickEvent(rightButtonTv);
    }

    private void setIcon(TextView tv, Drawable drawable) {
        setBounds(drawable);
        tv.setCompoundDrawables(null, null, drawable, null);
    }

    private void setOnClickEvent(TextView tv) {
        tv.setClickable(true);
        tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null == mTitleBarOnClickEvent) {
                    return;
                }

                String tag = (String) v.getTag();
                if (TAG_LEFT_BUTTON.equals(tag)) {// 左侧按钮
                    mTitleBarOnClickEvent.onLeftClick(v);

                } else if (TAG_LEFT_TITLE_TEXTVIEW.equals(tag)) {// left title
                    mTitleBarOnClickEvent.onLeftTitleClick(v);

                } else if (TAG_TITLE_TEXTVIEW.equals(tag)) {// 中间title
                    mTitleBarOnClickEvent.onTitleClick(v);

                } else {// 右侧按钮
                    mTitleBarOnClickEvent.onRightClick(v);

                }
            }
        });
    }

    public TextView getRightButtonTv() {
        return rightButtonTv;
    }

    public TextView getLeftButtonTv() {
        return leftButtonTv;
    }

    public interface OnTitleBarClickEvent {
        public void onLeftClick(View view);

        public void onLeftTitleClick(View view);

        public void onTitleClick(View view);

        public void onRightClick(View view);
    }

}
