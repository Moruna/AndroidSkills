package com.moruna.intentservicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class IntentServiceActivity extends AppCompatActivity {

    private static final String TAG = "IntentServiceActivity";
    public static final String ACTION_DOWNLOAD_RESULT =
            "com.moruna.intentservice.result.action";
    private int i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DOWNLOAD_RESULT);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void addTask(View view) {
        Log.e(TAG, "addTask: ");
        Intent intent = new Intent(this, TaskService.class);
        intent.setAction(TaskService.ACITON_TASK);
        intent.putExtra(TaskService.EXTRA_IMG_URL, "图片"+(++i));
        startService(intent);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ACTION_DOWNLOAD_RESULT)) {
                Log.e(TAG, intent.getStringExtra(TaskService.EXTRA_IMG_URL) + "下载成功");
            }
        }
    };
}
