package com.moruna.servicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Author: Moruna
 * Date: 2017-07-19
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class TestForegroundService extends Service {
    private static final String TAG = "TestForegroundService";
    private TestBinder binder = new TestBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("前台server");
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Intent intent = new Intent(this, ServiceTestActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        startForeground(1, builder.build());
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
