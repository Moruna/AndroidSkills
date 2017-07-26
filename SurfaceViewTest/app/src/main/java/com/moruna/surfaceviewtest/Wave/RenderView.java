package com.moruna.surfaceviewtest.Wave;

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
 * Desc:模板基类
 */
public class RenderView extends SurfaceView implements Callback {
    public RenderView(Context context) {
        this(context, null);
    }

    public RenderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    /*回调线程*/

    private class RenderThread extends Thread {

        private static final long SLEEP_TIME = 16;

        private SurfaceHolder surfaceHolder;
        private boolean running = true;

        public RenderThread(SurfaceHolder holder) {
            super("RenderThread");
            surfaceHolder = holder;
        }

        @Override
        public void run() {
            long startAt = System.currentTimeMillis();
            while (true) {
                synchronized (surfaceLock) {
                    if (!running) {
                        return;
                    }
                    Canvas canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        render(canvas, System.currentTimeMillis() - startAt);  //这里做真正绘制的事情
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setRun(boolean isRun) {
            this.running = isRun;
        }
    }

    private final Object surfaceLock = new Object();
    private RenderThread renderThread;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        renderThread = new RenderThread(holder);
        renderThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //这里可以获取SurfaceView的宽高等信息
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (surfaceLock) {  //这里需要加锁，否则doDraw中有可能会crash
            renderThread.setRun(false);
        }
    }

    private void render(Canvas canvas, long millisPassed) {
        onRender(canvas, millisPassed);
    }

    /**
     * 渲染surfaceView的回调方法。
     *
     * @param canvas 画布
     */
    protected void onRender(Canvas canvas, long millisPassed) {
    }

}
