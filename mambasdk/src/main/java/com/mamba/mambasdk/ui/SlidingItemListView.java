package com.mamba.mambasdk.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * A ListView that left sliding its item will show a quick action buttons
 * section. Used for contact list.
 * 
 * Invoke {@link #setVisibleScrollItemPartId(int)} to set the quick action
 * buttons section.
 * 
 * Invoke {@link #setVisibleScrollItemPartId(int)} to set a view of the sliding
 * item to be invisible
 * 
 */
public class SlidingItemListView extends ListView {
    /*
     * x轴滑动速率
     */
    private static final int SNAP_VELOCITY = 600;
    private int mScreenWidth;
    /*
     * 系统默认的认为用户进行滑动操作的最小距离
     */
    private int mTouchSlop;
    private int mDownX, mDownY;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private OnScrollOverListener mOverListener;
    private View mVisibleScrollItemPart, mInvisibleScrollItemPart, mTempView;
    private int mVisibleScrollItemPartId, mInvisibleScrollItemPartId;
    private boolean mIsSliding;
    private ItemDirection mDirection = ItemDirection.NO_DIRECTION;
    private ItemDirection mScrollDirection = ItemDirection.NO_DIRECTION, mOldScrollDirection;
    private int mScrollPosition, mOldScrollPosition;
    private int mPositiveScrollX, mNegativeScrollX;
    private int mScrollOverReason;

    public SlidingItemListView(Context context) {
        this(context, null);
    }

    public SlidingItemListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingItemListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    /**
     * 
     * Set the resource id of the view to scrolled invisible when this ListView
     * item is left slid.
     * 
     * @param layoutId
     *            the the resource id of the view
     */
    public void setVisibleScrollItemPartId(int layoutId) {
        mVisibleScrollItemPartId = layoutId;
    }

    /**
     * Set the resource id of the view to be scrolled visible when this ListView
     * item is left slid.
     * 
     * @param layoutId
     *            the layoutId to set
     */
    public void setInvisibleScrollItemPartId(int layoutId) {
        mInvisibleScrollItemPartId = layoutId;
    }

    /**
     * Interface definition for a callback to be called when the item of this
     * ListView finished scrolling.
     * 
     * 
     */
    public interface OnScrollOverListener {
        /**
         * Called when the item of this ListView finished scrolling.
         * 
         * @param scrollReason
         *            the reason of the last scrolling.
         */
        void scrollToOriginOver(int scrollReason);
    }

    /**
     * Register a callback to be called when the item of this ListView finished
     * scrolling.
     * 
     * @param listener
     *            the callback will run.
     */
    public void setOnScrollOverListener(OnScrollOverListener listener) {
        mOverListener = listener;
    }

    /**
     * scroll the item of this ListView to its origin initial state.
     * 
     * @return true, if scroll successfully. false, otherwise.
     */
    public boolean scrollToOrigin() {
        if (null != mTempView && View.VISIBLE == mInvisibleScrollItemPart.getVisibility()) {
            // mScrollItem.scrollTo(0, 0);
            scrollToRight();
            mTempView = null;
            mOldScrollPosition = mScrollPosition;
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addVelocityTracker(ev);
                // 上一个滑动还没有结束，直接return，消费事件
                if (!mScroller.isFinished()) {
                    // return super.dispatchTouchEvent(ev);
                    return true;
                }
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                // 通过按下坐标获取ListView对应的Item position
                mScrollPosition = pointToPosition(mDownX, mDownY);
                if (AdapterView.INVALID_POSITION == mScrollPosition) {
                    return super.dispatchTouchEvent(ev);
                }
                // 判断是否有已经滑出的Item，有的话将已滑出的Item收回，并消费事件
                if (null != mInvisibleScrollItemPart && View.VISIBLE == mInvisibleScrollItemPart.getVisibility()
                        && mOldScrollPosition != mScrollPosition) {
                    scrollToRight();
                    mTempView = null;
                    mOldScrollPosition = mScrollPosition;
                    return true;
                }
                mOldScrollPosition = mScrollPosition;
                // mScrollItem = getChildAt(mScrollPosition
                // - getFirstVisiblePosition());
                // 获取item中需要滚动的view，getChildAt（int index）方法中的参数index是ListView
                // Item的相对position
                // 换个说法，就是这个index是从第一个可见的Item开始算
                View item = getChildAt(mScrollPosition - getFirstVisiblePosition());
                // 上层布局的view，正常情况下看见的姓名
                mVisibleScrollItemPart = item.findViewById(mVisibleScrollItemPartId);
                // 隐藏的布局view，滑动时显示出来的快速业务按钮布局
                mInvisibleScrollItemPart = item.findViewById(mInvisibleScrollItemPartId);
                mTempView = mVisibleScrollItemPart;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = ev.getX() - mDownX;
                // 判断是否满足滑动的条件
                // 被滑动view不为null && 起始滑动方向为无 && （滑动速率大于设定的值 || （X轴滑动距离大于系统默认滑动距离
                // && Y轴滑动距离小于系统默认滑动距离））
                if (null != mTempView
                        && !(deltaX > 0 && ItemDirection.NO_DIRECTION.equals(mDirection))
                        && (Math.abs(getScrollVelocity()) > SNAP_VELOCITY || (Math.abs(deltaX) > mTouchSlop && Math
                                .abs(ev.getY() - mDownY) < mTouchSlop))) {
                    mInvisibleScrollItemPart.setVisibility(View.VISIBLE);
                    mScrollDirection = ev.getX() - mDownX > 0 ? ItemDirection.SCROLL_RIGHT : ItemDirection.SCROLL_LEFT;
                    mIsSliding = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    // 注意：dispatchTouchEvent(MotionEvent ev)是事件分发的起点，所以在其中判定是否满足侧滑的条件mIsSliding
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mIsSliding || AdapterView.INVALID_POSITION == mScrollPosition) {
            return super.onTouchEvent(ev);
        }
        requestDisallowInterceptTouchEvent(true);
        addVelocityTracker(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                MotionEvent cancelEvent = MotionEvent.obtain(ev);
                cancelEvent.setAction(MotionEvent.ACTION_CANCEL
                        | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                onTouchEvent(cancelEvent);
                int x = (int) ev.getX();
                // deltaX 左滑正，右滑负
                int deltaX = mDownX - x;
                mDownX = x;
                if (!isNeedToForbidScrollRight(deltaX)) {
                    mVisibleScrollItemPart.scrollBy(deltaX, 0);
                }
                // 注意：此处已经处于侧滑状态，因此需要将事件消费掉，不再分发
                return true;
            case MotionEvent.ACTION_UP:
                // 释放时，判断Item应该处于何种状态
                int xVelocity = getScrollVelocity();
                if (xVelocity > SNAP_VELOCITY) {
                    scrollToRight();
                } else if (xVelocity < -SNAP_VELOCITY) {
                    scrollToLeft();
                } else {
                    scrollByDistanceX();
                }
                recycleVelocityTracker();
                mPositiveScrollX = 0;
                mNegativeScrollX = 0;
                mIsSliding = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 为啥一开始只能往左滑，不能往右滑？就是这个方法处理的，很简单吧？ 基本思想就是，记录左滑的距离与右滑的距离，简单判断一下就ok了 注意：正方向为左
     */
    private boolean isNeedToForbidScrollRight(int deltaX) {
        if (ItemDirection.NO_DIRECTION != mDirection) {
            return false;
        }
        if (deltaX > 0) {
            mPositiveScrollX += deltaX;
        } else {
            mNegativeScrollX += deltaX;
        }
        return Math.abs(mNegativeScrollX) > mPositiveScrollX;
    }

    // 每次scrolling结束，系统都会回调这个函数，这里能干什么自然很明了了
    @Override
    public void computeScroll() {
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        mVisibleScrollItemPart.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        postInvalidate();
        if (mScroller.isFinished()) {
            // 滚动结束，想搞什么搞什么
            if (null != mInvisibleScrollItemPart && ItemDirection.RIGHT == mDirection) {
                mInvisibleScrollItemPart.setVisibility(View.GONE);
                mDirection = ItemDirection.NO_DIRECTION;
            }
            if (null != mOverListener) {
                mOverListener.scrollToOriginOver(mScrollOverReason);
            }
        }
    }

    private void addVelocityTracker(MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int xVelocity = (int) mVelocityTracker.getXVelocity();
        return xVelocity;
    }

    private void scrollToRight() {
        scrollToRight(mVisibleScrollItemPart.getScrollX());
    }

    private void scrollToRight(int scrollX) {
        mDirection = ItemDirection.RIGHT;
        // final int delta = mScreenWidth + scrollX;
        final int delta = scrollX;
        mScroller.startScroll(scrollX, 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }

    private void scrollToLeft() {
        scrollToLeft(mVisibleScrollItemPart.getScrollX());
    }

    private void scrollToLeft(int scrollX) {
        mDirection = ItemDirection.LEFT;
        final int delta = mScreenWidth - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta));
        postInvalidate();
    }

    private void scrollByDistanceX() {
        int scrollX = mVisibleScrollItemPart.getScrollX();
        if (scrollX >= mScreenWidth / 10f) {
            if (!isScrollDirectionChanged() || ItemDirection.RIGHT.equals(mDirection)
                    || ItemDirection.NO_DIRECTION.equals(mDirection)) {
                scrollToLeft(scrollX);
            } else if (ItemDirection.LEFT.equals(mDirection) || isScrollDirectionChanged()) {
                scrollToRight(scrollX);
            }
        }
        // else if (scrollX <= -mScreenWidth / 5f) {
        // if (MoveDirection.LEFT.equals(mDirection)) {
        // scrollToRight(scrollX);
        // }else {
        // scrollToLeft(scrollX);
        // }
        // }
        else {
            mVisibleScrollItemPart.scrollTo(0, 0);
        }
    }

    private boolean isScrollDirectionChanged() {
        if (mScrollDirection.equals(mOldScrollDirection)) {
            return false;
        }
        mOldScrollDirection = mScrollDirection;
        return true;
    }

    private enum ItemDirection {
        LEFT, RIGHT, NO_DIRECTION, SCROLL_LEFT, SCROLL_RIGHT;
    }
}
