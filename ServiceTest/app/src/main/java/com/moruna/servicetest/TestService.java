package com.moruna.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Author: Moruna
 * Date: 2017-07-19
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class TestService extends Service {

    private static final String TAG = "TestService";
    private TestBinder binder = new TestBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        Log.e(TAG, "onCreate: process id = "+ Process.myPid() );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启执行后台任务
                Log.e(TAG, "run: onStartCommand");
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class TestBinder extends Binder {
        public void startTask() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //执行耗时任务
                    Log.e(TAG, "run: startTask");
                }
            }).start();
        }
    }
}

