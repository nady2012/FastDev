package com.mamba.mambasdk.ui.pickerview.lib;

/**
 * The type On item selected runnable.
 */
final class OnItemSelectedRunnable implements Runnable {
    /**
     * The Loop view.
     */
    final WheelView loopView;

    /**
     * Instantiates a new On item selected runnable.
     *
     * @param loopview the loopview
     */
    OnItemSelectedRunnable(WheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        loopView.onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
    }
}
