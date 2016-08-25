package com.fastdev.fastdevlib.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;

import com.fastdev.fastdevlib.R;


public class XSWSwitchWindow extends CompoundButton {

    private final String TAG = XSWSwitchWindow.class.getSimpleName();

    private boolean switchState;
    private Drawable onDrawable, offDrawable;

    private OnXSPSwitchStateChangedListenr mSwitchStateListener;

    public XSWSwitchWindow(Context context) {
        this(context, null);
    }

    public XSWSwitchWindow(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public XSWSwitchWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.switchWindow, defStyle, 0);
        switchState = a.getBoolean(R.styleable.switchWindow_switchstate, false);
        onDrawable = a.getDrawable(R.styleable.switchWindow_onDrawable);
        offDrawable = a.getDrawable(R.styleable.switchWindow_offDrawable);
        a.recycle();
        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setOnClickListener(mListener);
        initSwitchButton();
    }

    private void initSwitchButton() {
        if (switchState) {
            initOnDrawable();
        } else {
            initOffDrawable();
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void initOnDrawable() {
        if (null != onDrawable) {
            if (Build.VERSION.SDK_INT >= 16) {
                super.setBackground(onDrawable);
            } else {
                super.setBackgroundDrawable(onDrawable);
            }

        } else {
            super.setBackgroundResource(R.drawable.switch_button_001_on);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void initOffDrawable() {
        if (null != offDrawable) {
            if (Build.VERSION.SDK_INT >= 16) {
                super.setBackground(offDrawable);
            } else {
                super.setBackgroundDrawable(offDrawable);
            }

        } else {
            super.setBackgroundResource(R.drawable.switch_button_002_off);
        }
    }

    private OnClickListener mListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (switchState) {
                switchState = false;
                setSwitchState(switchState);
            } else {
                switchState = true;
                setSwitchState(switchState);
            }
            if (null == mSwitchStateListener) {
                return;
            }
            mSwitchStateListener.onSwitchStateChange(arg0, switchState);
        }
    };

    public interface OnXSPSwitchStateChangedListenr {

        public void onSwitchStateChange(View view, boolean isOn);
    }

    public void setXSPSwitchStateChangedListenr(OnXSPSwitchStateChangedListenr listener) {
        mSwitchStateListener = listener;
    }

    public boolean getSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean state) {
        switchState = state;
        initSwitchButton();
    }

    public void setOnDrawable(Drawable drawable) {
        onDrawable = drawable;
    }

    public void setOnDrawable(int resId) {
        onDrawable = getResources().getDrawable(resId);
    }

    public Drawable getOnDrawable() {
        return onDrawable;
    }

    public void setOffDrawable(Drawable drawable) {
        offDrawable = drawable;
    }

    public void setOffDrawable(int resId) {
        offDrawable = getResources().getDrawable(resId);
    }

    public Drawable getOffDrawable() {
        return offDrawable;
    }

}
