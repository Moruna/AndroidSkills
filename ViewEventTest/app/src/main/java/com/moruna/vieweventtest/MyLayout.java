package com.moruna.vieweventtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Author: Moruna
 * Date: 2017-07-21
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class MyLayout extends LinearLayout {
    public MyLayout(Context context) {
        this(context, null);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false; //一般返回false,即子view会把事件消费掉，事件不继续往下传递
        return true;//拦截到子view的事件，比如直接到Mylayout其onTouch事件
    }
}
