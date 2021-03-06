package com.mamba.mambasdk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * A subclass of ListView that catches the Exceptions occurs in
 * {@link android.widget.AbsListView#onTouchEvent(MotionEvent ev)} and
 * {@link ListView#dispatchDraw(Canvas canvas)}
 * 
 */
public class SafeListView extends ListView {

    public SafeListView(Context context) {
        super(context);
    }

    public SafeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SafeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (NullPointerException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
        }
    }
}
