package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fastdev.fastdevlib.R;

import java.util.Arrays;

/**
 * A subclass of {@link View}. Used to show a letter index for contacts list.
 * 
 * <p>
 * Invoke
 * {@link #setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener)}
 * to register a callback to be invoked when the letter on touched in
 * XSWLetterWidget is changed.
 * 
 * <p>
 * Invoke {@link #moveToLetter(String)} to set a given letter highlight and as
 * selected.
 * 
 */
public class XSWLetterWidget extends View {

    protected static final String COUNTRY_RUSSIA = "RU";

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    private String[] lettersEN = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    private String[] lettersRU = { "#", "袗", "袘", "袙", "袚", "袛", "袝", "衼", "袞", "袟", "袠", "袡", "袣", "袥", "袦", "袧", "袨",
            "袩", "袪", "小", "孝", "校", "肖", "啸", "笑", "效", "楔", "些", "歇", "蝎", "鞋", "协", "挟", "携" };

    protected int choose = -1;
    protected Paint paintNormalLetter = new Paint();
    protected Paint paintChoosedLetter = new Paint();
    protected String[] letters;

    public XSWLetterWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public XSWLetterWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XSWLetterWidget(Context context) {
        super(context);
        init();
    }

    /**
     * Register a callback to be invoked when the letter on touched in
     * XSWLetterWidget is changed
     * 
     * @param onTouchingLetterChangedListener
     *            The callback will run.
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * Interface definition for a callback to be invoked when the letter on
     * touched in XSWLetterWidget is changed.
     * 
     */
    public interface OnTouchingLetterChangedListener {

        /**
         * Called when the letter on touched in XSWLetterWidget is changed.
         * 
         * @param s
         *            the letter on touched currently
         */
        public void onTouchingLetterChanged(String s);
    }

    /**
     * Set the given letter highlight and as selected.
     * 
     * @param s
     *            the letter to be highlight and selected.
     */
    public void moveToLetter(String s) {
        String sNew = s;
        if (!Arrays.asList(letters).contains(s)) {
            sNew = letters[0];
        }

        if (Character.isDigit(sNew.charAt(0))) {
            refresh(0);
        } else {
            for (int i = 0; i < letters.length; i++) {
                if (letters[i].equals(s)) {
                    refresh(i);
                    break;
                }
            }
        }
    }

    private void init() {
        if (getResources().getConfiguration().locale.getCountry().equalsIgnoreCase(COUNTRY_RUSSIA)) {
            letters = lettersRU;
        } else {
            letters = lettersEN;
        }
    }

    protected void refresh(int c) {
        choose = c;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        drawWidget(canvas, width, height);
    }

    protected void drawWidget(Canvas canvas, int width, int height) {
        int singleHeight = height / letters.length;
        for (int i = 0; i < letters.length; i++) {
            paintNormalLetter.setColor(getResources().getColor(R.color.gray));
            paintNormalLetter.setTextSize(getResources().getDimension(R.dimen.font_size1));
            paintNormalLetter.setAntiAlias(true);
            float xPos = width / 2 - paintNormalLetter.measureText(letters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            if (i == choose) {
                float y = singleHeight * i;
                paintChoosedLetter.setColor(getResources().getColor(R.color.rcs_content_color));
                // RectF rect = new RectF(x , y+singleHeight/4, 7*x,
                // y+5*singleHeight/4);
                RectF rect = new RectF(xPos / 4, y + singleHeight / 4, xPos + paintNormalLetter.measureText(letters[i])
                        + 3 * xPos / 4, y + 5 * singleHeight / 4);
                canvas.drawRoundRect(rect, 5, 5, paintChoosedLetter);
                paintNormalLetter.setColor(Color.WHITE);
                paintNormalLetter.setFakeBoldText(true);
            }
            canvas.drawText(letters[i], xPos, yPos, paintNormalLetter);
            paintNormalLetter.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = getChoosePosition(y);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                doLetterChangedAction(oldChoose, c, listener);
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundResource(Color.TRANSPARENT);
                choose = c;
                invalidate();
                break;
        }
        return true;
    }

    protected int getChoosePosition(float yPos) {
        return (int) (yPos / getHeight() * letters.length);
    }

    protected void doLetterChangedAction(int oldChoose, int newChoose, OnTouchingLetterChangedListener listener) {
        setBackgroundResource(Color.TRANSPARENT);
        if (oldChoose != newChoose && listener != null) {
            if (newChoose >= 0 && newChoose < letters.length) {
                listener.onTouchingLetterChanged(letters[newChoose]);
                choose = newChoose;
                invalidate();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if ((Math.abs(h - oldh) < 150)) {
            return;
        }
        if (h >= oldh) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.INVISIBLE);
        }
    }
}