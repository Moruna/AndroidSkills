package com.moruna.intentservicetest;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Author: Moruna
 * Date: 2017-07-20
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class TaskService extends IntentService {
    private static final String TAG = "TaskService";
    public static final String ACITON_TASK = "com.moruna.intentservice.action";
    public static final String EXTRA_IMG_URL = "extra_url";

    public TaskService() {
        super("TaskService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getAction().equals(ACITON_TASK)) {
            downloadImg(intent.getStringExtra(EXTRA_IMG_URL));
        }
    }

    private void downloadImg(String strUrl) {
        try {
            Thread.sleep(3000);  //延时模拟下载操作
            Intent intent = new Intent(IntentServiceActivity.ACTION_DOWNLOAD_RESULT);
            intent.putExtra(EXTRA_IMG_URL, strUrl);
            sendBroadcast(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}
