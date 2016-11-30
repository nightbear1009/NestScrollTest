package com.nestscrolltest;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by tedliang on 2016/11/6.
 */

public class MyLinearLayout extends LinearLayout {
    private View mView;
    private int mLayoutTop;
    private ViewDragHelper mViewDragHelper;
    private int sheetExpandedTop;
    private int sheetBottom;
    private boolean isNestScroll;
    private int mOffset;

    public MyLinearLayout(Context context) {
        super(context);
        init();
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mView = getChildAt(0);
        mLayoutTop = mView.getTop();
        mView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                sheetExpandedTop = top;
                sheetBottom = bottom;
            }
        });
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.d("Ted","onStartNestedScroll");
        return (nestedScrollAxes & View.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {
            final int upwardDragRange = mView.getTop() - sheetExpandedTop;
            if (upwardDragRange > 0) {
                Log.d("Ted","onNestedPreScroll");
                final int consume = Math.min(upwardDragRange, dy);
                mOffset += -consume;
                ViewCompat.offsetTopAndBottom(mView, mOffset - (mView.getTop() - mLayoutTop));
                consumed[1] = consume;
            }
        }
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed < 0) {
            Log.d("Ted","onNestedScroll");
            mOffset += -dyUnconsumed;
            ViewCompat.offsetTopAndBottom(mView, mOffset - (mView.getTop() - mLayoutTop));
        }
    }
}
