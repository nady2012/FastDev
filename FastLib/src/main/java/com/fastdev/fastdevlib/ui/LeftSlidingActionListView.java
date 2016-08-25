package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.Scroller;

import com.fastdev.fastdevlib.R;

/**
 * A ListView that left sliding its item will show a quick action buttons
 * section. Used for fling an item of ListView to delete it.
 * 
 * Invoke {@link #setQuickActionItemId(int)} to set the quick action buttons
 * section.
 * 
 * Invoke {@link #setScrollItemId(int)} to set a view of the sliding item to be
 * invisible
 * 
 */
public class LeftSlidingActionListView extends SafeListView {

    // private final String TAG = "XSWLeftSlidingActionListView";

    private static final int SNAP_VELOCITY = 600;
    private int mScreenWidth;
    private int mTouchSlop;
    private int mDownX, mDownY;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private View mScrollItem, mTempView, mQuickWidget;
    private int mScrollLeftDistance;

    private boolean mIsSliding;
    private ItemDirection mDirection = ItemDirection.NO_DIRECTION;
    private ItemDirection mScrollDirection = ItemDirection.NO_DIRECTION, mOldScrollDirection;
    private int mScrollPosition, mOldScrollPosition;
    private int mPositiveScrollX, mNegativeScrollX;

    private int mScrollItemId;
    private int mQuickActionItemId;

    private OnQuickActionClickListenor listener;

    public LeftSlidingActionListView(Context context) {
        this(context, null, 0);
    }

    public LeftSlidingActionListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftSlidingActionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RightSlideList, defStyle, 0);
        mScrollItemId = typedArray.getResourceId(R.styleable.RightSlideList_scrollItem, 0);
        mQuickActionItemId = typedArray.getResourceId(R.styleable.RightSlideList_quickActionItem, 0);
        typedArray.recycle();
    }

    /**
     * 
     * Set the resource id of the view to scrolled when this ListView item is
     * left slid.
     * 
     * @param scrollItemId
     *            the the resource id of the view
     */
    public void setScrollItemId(int scrollItemId) {
        this.mScrollItemId = scrollItemId;
    }

    /**
     * Set the resource id of the view to be scrolled visible when this ListView
     * item is left slid.
     * 
     * @param quickActionItemId
     *            the the resource id of the view
     */
    public void setQuickActionItemId(int quickActionItemId) {
        this.mQuickActionItemId = quickActionItemId;
    }

    /**
     * Interface definition for a callback to be invoked when the quick action
     * section is clicked
     * 
     */
    public interface OnQuickActionClickListenor {

        /**
         * Called when the quick action section is clicked
         * 
         * @param position
         *            the position of the quick action section in this ListView
         */
        public void onClick(int position);
    };

    /**
     * Register a callback to be called when the quick action section is clicked
     * 
     * @param listener
     *            the callback will be run
     */
    public void setOnQuickActionClickListenor(OnQuickActionClickListenor listener) {
        this.listener = listener;
    }

    /**
     * scroll the item of this ListView to its origin initial state.
     * 
     * @return true, if scroll successfully. false, otherwise.
     */
    public boolean scrollToOrigin() {
        if (0 >= mQuickActionItemId || null == mQuickWidget || null == mScrollItem) {
            return false;
        }
        if (null != mTempView && View.VISIBLE == mQuickWidget.getVisibility()) {
            mScrollItem.scrollTo(0, 0);
            mTempView = null;
            mOldScrollPosition = mScrollPosition;
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (0 >= mQuickActionItemId) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addVelocityTracker(ev);
                // 上一个滑动还没有结束，直接return
                if (!mScroller.isFinished()) {
                    return super.dispatchTouchEvent(ev);
                }

                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                mScrollPosition = pointToPosition(mDownX, mDownY);
                if (AdapterView.INVALID_POSITION == mScrollPosition) {
                    if (isNeedScrollToRightOnDispatchTouchEvent()) {
                        scrollToRightOnDispatchTouchEvent();
                        return true;
                    }

                    return super.dispatchTouchEvent(ev);
                }
                if (isNeedScrollToRightOnDispatchTouchEvent()) {
                    scrollToRightOnDispatchTouchEvent();
                    return true;
                }

                mOldScrollPosition = mScrollPosition;
                // 获取item中需要滚动的view
                View item = getChildAt(mScrollPosition - getFirstVisiblePosition());
                if (0 >= mScrollItemId) {
                    mScrollItem = item;
                } else {
                    mScrollItem = item.findViewById(mScrollItemId);
                }

                mQuickWidget = item.findViewById(mQuickActionItemId);
                mTempView = mScrollItem;
                break;
            case MotionEvent.ACTION_MOVE:
                dispatchTouchEventMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isNeedScrollToRightOnDispatchTouchEvent() {
        return null != mTempView && null != mQuickWidget && mQuickWidget.isShown()
                && mScreenWidth - mDownX > mQuickWidget.getWidth();
    }

    private void scrollToRightOnDispatchTouchEvent() {
        scrollToRight();
        mTempView = null;
        mOldScrollPosition = mScrollPosition;
    }

    private void dispatchTouchEventMove(MotionEvent ev) {
        float deltaX = ev.getX() - mDownX;
        if (null != mTempView
                && !(deltaX > 0 && ItemDirection.NO_DIRECTION.equals(mDirection))
                && (Math.abs(getScrollVelocity()) > SNAP_VELOCITY || (Math.abs(deltaX) > mTouchSlop && Math.abs(ev
                        .getY() - mDownY) < mTouchSlop)) && null != mQuickWidget) {
            mScrollDirection = ev.getX() - mDownX > 0 ? ItemDirection.SCROLL_RIGHT : ItemDirection.SCROLL_LEFT;
            mQuickWidget.setVisibility(View.VISIBLE);
            mIsSliding = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (0 >= mQuickActionItemId) {
            return super.onTouchEvent(ev);
        }
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
                int deltaX = mDownX - x;
                mDownX = x;
                if (!isNeedToForbidScrollRight(deltaX) && !isNeedToForbidScrollLeft()) {
                    if (mScrollLeftDistance + deltaX > mQuickWidget.getWidth()) {
                        deltaX = mQuickWidget.getWidth() - mScrollLeftDistance - 1;
                    }
                    mScrollItem.scrollBy(deltaX, 0);
                    mScrollLeftDistance += deltaX;
                }
                // 已经是拖动，就拦截事件，禁止listView上下滑动
                return true;
            case MotionEvent.ACTION_UP:
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
                mScrollLeftDistance = 0;
                mIsSliding = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

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

    private boolean isNeedToForbidScrollLeft() {
        if (ItemDirection.LEFT == mDirection) {
            return true;
        }

        return Math.abs(mScrollLeftDistance + 1) >= mQuickWidget.getWidth();
    }

    @Override
    public void computeScroll() {
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        mScrollItem.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        postInvalidate();
        if (mScroller.isFinished()) {
            // 滚动结束，想搞什么搞什么
            if (null != mQuickWidget && ItemDirection.RIGHT == mDirection) {
                mQuickWidget.setVisibility(View.GONE);
                mDirection = ItemDirection.NO_DIRECTION;
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
        scrollToRight(mScrollItem.getScrollX());
    }

    private void scrollToRight(int scrollX) {
        mDirection = ItemDirection.RIGHT;
        final int delta = scrollX;
        mScroller.startScroll(scrollX, 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }

    private void scrollToLeft() {
        scrollToLeft(mScrollItem.getScrollX());
    }

    private void scrollToLeft(int scrollX) {
        mDirection = ItemDirection.LEFT;
        final int delta = (int) (mScreenWidth / 4.5f) - scrollX;
        if (delta > 0) {
            mScroller.startScroll(scrollX, 0, delta, 0, delta);
            postInvalidate();
        }

        if (0 >= mScrollItemId) {
            mQuickWidget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(mScrollPosition);
                    scrollToRight();
                }
            });
        }
    }

    private void scrollByDistanceX() {
        int scrollX = mScrollItem.getScrollX();
        if (scrollX >= mScreenWidth / 10f) {
            if (!isScrollDirectionChanged() || ItemDirection.RIGHT.equals(mDirection)
                    || ItemDirection.NO_DIRECTION.equals(mDirection)) {
                scrollToLeft(scrollX);
            } else if (ItemDirection.LEFT.equals(mDirection) || isScrollDirectionChanged()) {
                scrollToRight(scrollX);
            }
        } else {
            mScrollItem.scrollTo(0, 0);
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
