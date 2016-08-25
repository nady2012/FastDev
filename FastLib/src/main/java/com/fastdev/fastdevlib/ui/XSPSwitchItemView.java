package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fastdev.fastdevlib.R;

public class XSPSwitchItemView extends LinearLayout {

    private Context context;
    private String switchlabel;
    private int switchBg;
    private boolean switchState;

    private TextView switchLabelText;
    private XSPSwitchButton switchButton;

    public XSPSwitchItemView(Context context) {
        this(context, null);
    }

    public XSPSwitchItemView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public XSPSwitchItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.switchButton_item, defStyle, 0);
        switchlabel = a.getString(R.styleable.switchButton_item_switchlabel);
        switchBg = a.getIndex(R.styleable.switchButton_item_switchBg);
        switchState = a.getBoolean(R.styleable.switchButton_item_switchCheck, false);

        LayoutInflater.from(context).inflate(R.layout.xsp_view_switch_item, this, true);

        a.recycle();
        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initSwitchItem();
        setBackground(switchBg);
        setSwitchItemText(switchlabel);
        setSwitchState(switchState);
        this.setOnClickListener(onclick);
    }

    private void initSwitchItem() {
        switchLabelText = (TextView) findViewById(R.id.xsp_view_switch_001_text);
        switchButton = (XSPSwitchButton) findViewById(R.id.xsp_view_switch_001_switch);
    }

    public void setSwitchItemText(int resId) {
        if (0 == resId) {
            return;
        }
        setSwitchItemText(context.getString(resId));
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
        if (switchButton != null) {
            switchButton.setSwitchState(switchState);
        }
    }

    public boolean getSwitchState() {
        return this.switchState;
    }

    public void setXSPSwitchStateChangedListenr(XSWSwitchWindow.OnXSPSwitchStateChangedListenr listener) {
        if (switchButton != null) {
            switchButton.setXSPSwitchStateChangedListenr(listener);
        }
    }

    public void setSwitchItemText(String label) {
        if (null == label || null == switchLabelText) {
            return;
        }
        switchLabelText.setText(label);
    }

    public XSPSwitchButton getSwitchButton() {
        return switchButton;
    }

    public void setBackground(int resId) {
        if (0 == resId) {
            return;
        }
        // setBackground(resId);
    }

    OnClickListener onclick = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (switchButton != null) {
                switchButton.performClick();
            }
        }

    };

}
