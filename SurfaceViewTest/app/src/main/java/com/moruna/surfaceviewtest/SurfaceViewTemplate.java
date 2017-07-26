package com.moruna.surfaceviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Author: Moruna
 * Date: 2017-07-26
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 * Desc:SurfaceView使用模板
 */
public class SurfaceViewTemplate extends SurfaceView implements Callback {
    private SurfaceHolder holder;
    private Canvas canvas;
    private boolean isDrawing;

    public SurfaceViewTemplate(Context context) {
        this(context, null);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isDrawing) {
                    draw();
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
            //draw something
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
