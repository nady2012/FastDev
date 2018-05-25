package com.mamba.mambasdk.ui.pickerview.lib;

import android.view.MotionEvent;

/**
 * The type Loop view gesture listener.
 */
final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    /**
     * The Loop view.
     */
    final WheelView loopView;

    /**
     * Instantiates a new Loop view gesture listener.
     *
     * @param loopview the loopview
     */
    LoopViewGestureListener(WheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}
