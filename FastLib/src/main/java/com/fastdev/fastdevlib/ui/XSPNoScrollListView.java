package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ScrollView;

public class XSPNoScrollListView extends XSWSafeListView {

    private ScrollView scrollView;

    private boolean isEnableScroll;

    public XSPNoScrollListView(Context context) {
        super(context);
    }

    public XSPNoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XSPNoScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    setParentScrollAble(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    // @Override
    // public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
    // MeasureSpec.AT_MOST);
    // super.onMeasure(widthMeasureSpec, expandSpec);
    // }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // setParentScrollAble(false);//
                // 当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview
                // 停住不能滚动
            case MotionEvent.ACTION_MOVE:
                // setParentScrollAble(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // setParentScrollAble(true);// 当手指松开时，让父ScrollView重新拿到onTouch权限
                break;
            default:
                break;

        }
        return super.onInterceptTouchEvent(ev);

    }

    private int lastScrollY;

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = getScrollY();

            // 此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }

        };

    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        setParentScrollAble(false);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    public void setEnableScroll(boolean isEnableScroll) {
        this.isEnableScroll = isEnableScroll;
    }

    /**
     * 是否把滚动事件交给父scrollview
     * 
     * @param flag
     */
    public void setParentScrollAble(boolean flag) {
        if (null == scrollView) {
            return;
        }
        scrollView.requestDisallowInterceptTouchEvent(isEnableScroll);
    }
}
