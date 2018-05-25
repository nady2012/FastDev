package com.mamba.mambasdk.ui.pickerview.lib;

import android.os.Handler;
import android.os.Message;

/**
 * The type Message handler.
 */
final class MessageHandler extends Handler {
    /**
     * The constant WHAT_INVALIDATE_LOOP_VIEW.
     */
    public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    /**
     * The constant WHAT_SMOOTH_SCROLL.
     */
    public static final int WHAT_SMOOTH_SCROLL = 2000;
    /**
     * The constant WHAT_ITEM_SELECTED.
     */
    public static final int WHAT_ITEM_SELECTED = 3000;

    /**
     * The Loopview.
     */
    final WheelView loopview;

    /**
     * Instantiates a new Message handler.
     *
     * @param loopview the loopview
     */
    MessageHandler(WheelView loopview) {
        this.loopview = loopview;
    }

    @Override
    public final void handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_INVALIDATE_LOOP_VIEW:
                loopview.invalidate();
                break;

            case WHAT_SMOOTH_SCROLL:
                loopview.smoothScroll(WheelView.ACTION.FLING);
                break;

            case WHAT_ITEM_SELECTED:
                loopview.onItemSelected();
                break;
            default:
                break;
        }
    }

}
