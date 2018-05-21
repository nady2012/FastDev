package com.mamba.mambasdk.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.mamba.mambasdk.R;

public class SwitchButton extends SwitchWindow {

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setOnDrawable(R.drawable.switch_button_001_on);
        super.setOffDrawable(R.drawable.switch_button_002_off);
    }

}
