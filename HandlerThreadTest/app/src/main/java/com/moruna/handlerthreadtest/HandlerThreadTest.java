package com.moruna.handlerthreadtest;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class HandlerThreadTest extends AppCompatActivity {

    HandlerThread handlerThread;
    ConcreteHandler concreteHandler;
    String TAG = HandlerThreadTest.class.getSimpleName();
    int value = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread_test);
        handlerThread = new HandlerThread("TestHandlerThread");
        handlerThread.start();
        concreteHandler = new ConcreteHandler(handlerThread.getLooper());
        Log.d(TAG, Thread.currentThread() + "-- thread print --" + value);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, Thread.currentThread() + "-- thread print --" + value);
                Message msg = new Message();
                msg.what = value;
                concreteHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    private class ConcreteHandler extends Handler {
        private ConcreteHandler(Looper looper) {
            //如果不super looper，则还会运行在主线程
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //可执行耗时操作
            Log.d(TAG, Thread.currentThread() + "-- thread print --" + msg.what);
        }
    }
}
