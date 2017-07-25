package com.moruna.viewdefinetest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Author: Moruna
 * Date: 2017-07-24
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 * Desc:以圆画波纹
 */
public class RingView extends View {
    private static final String TAG = "RingView";
    private Paint paint;
    private int[] startWidthArr = {50, 100, 150};
    private int startWidth;

    public RingView(Context context) {
        this(context, null);
        Log.e(TAG, "RingView: ---1");
    }

    public RingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.e(TAG, "RingView: ---2");
    }

    public RingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.e(TAG, "RingView: ---3");
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAlpha(80);
        paint.setARGB(255, 4, 74, 126);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(400,400);//按照自己想要的大小
        Log.e(TAG, "onMeasure: getMeasuredWidth="+getMeasuredWidth());
        Log.e(TAG, "onMeasure: getMeasuredHeight="+getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top,
                            int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "onLayout: getWidth=" + getWidth());
        Log.e(TAG, "onLayout: getHeight=" + getHeight());//450px = 300dp*1.5
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAnim(canvas);
    }

    private void drawAnim(Canvas canvas) {
        for (int i = 0; i < startWidthArr.length; i++) {
            startWidth = startWidthArr[i];
            canvas.drawCircle(getWidth()/2, getHeight()/2, startWidth, paint);
            if (startWidth <= 180) {
                startWidthArr[i] = startWidthArr[i] + 2;
            } else {
                startWidthArr[i] = 50;
            }
        }
        postInvalidateDelayed(40);
    }
}
