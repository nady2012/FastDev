package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fastdev.fastdevlib.R;

public class TimerView extends LinearLayout {

    private TextView timerText;

    private static final int MAX_SECONDS = 60;

    /** The rest time */
    private int seconds;
    /** When the timer is running, the text show */
    private String showStr;
    /** The text show when timer run over */
    private String finishedStr;

    public TimerView(Context context) {
        this(context, null);
    }

    public TimerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimerView, defStyle, 0);
        seconds = ta.getInteger(R.styleable.TimerView_max_seconds, MAX_SECONDS);
        showStr = ta.getString(R.styleable.TimerView_show_text);
        finishedStr = ta.getString(R.styleable.TimerView_finished_text);
        if (TextUtils.isEmpty(finishedStr)) {
            finishedStr = showStr;
        }
        ta.recycle();
        LayoutInflater.from(context).inflate(R.layout.xsp_view_timer, this, true);
        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        timerText = (TextView) findViewById(R.id.tv_timer);
        timerText.setEnabled(false);
        timerText.setText("");
    }

    public void startTimer() {
        if (seconds <= 0) {
            startTimer(MAX_SECONDS);
        } else {
            startTimer(seconds);
        }
    }

    public void startTimer(int seconds) {
        this.seconds = seconds;
        timerText.setEnabled(false);
        timerText.getPaint().setFlags(0);
        stopTimer(); // avoid handler conflict
        setTimerText();
    }

    /**
     * 重置定时器 重新回去
     */
    public void resetTimer() {
        stopTimer();
        this.seconds = 0;
        setTimerText();
    }

    public void stopTimer() {
        mHandler.removeMessages(0);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        if (null == timerText || null == onClickListener) {
            return;
        }
        timerText.setOnClickListener(onClickListener);
    }

    private void setTimerText() {
        if (seconds == 0) {
            timerText.setText(finishedStr);
            stopTimer();
            timerText.setEnabled(true);
            timerText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
            return;
        }
        timerText.setText(String.format(showStr, seconds));
        mHandler.sendEmptyMessageDelayed(0, 1000); // 每隔 1s 刷新一次界面
        seconds--;
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setTimerText();
                    break;
                default:
                    break;
            }
        }

    };
}
