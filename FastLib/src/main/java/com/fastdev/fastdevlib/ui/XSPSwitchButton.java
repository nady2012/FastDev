package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.fastdev.fastdevlib.R;

public class XSPSwitchButton extends XSWSwitchWindow {

    public XSPSwitchButton(Context context) {
        this(context, null);
    }

    public XSPSwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public XSPSwitchButton(Context context, AttributeSet attrs, int defStyle) {
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
