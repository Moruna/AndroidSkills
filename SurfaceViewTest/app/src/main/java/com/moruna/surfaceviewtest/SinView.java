package com.moruna.surfaceviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Author: Moruna
 * Date: 2017-07-26
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 * Desc:画正弦
 */
public class SinView extends SurfaceView implements Callback {
    private static final String TAG = "SinView";
    private SurfaceHolder holder;
    private Canvas canvas;
    private boolean isDrawing;
    private int x = 0;
    private int y = 0;
    private Paint paint;
    private Path path;

    public SinView(Context context) {
        this(context, null);
    }

    public SinView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SinView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        path = new Path();
        paint = new Paint();
        paint.setARGB(255, 4, 74, 126);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);
        paint.setAntiAlias(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        path.moveTo(0, 170);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isDrawing) {
                    draw();
                    x += 3;
                    y = (int) (100*Math.sin(x * Math.PI / 180) + 170);
                    path.lineTo(x, y);
                }
            }
        }).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            //画背景
            canvas.drawColor(Color.WHITE);
            canvas.drawPath(path, paint);
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
