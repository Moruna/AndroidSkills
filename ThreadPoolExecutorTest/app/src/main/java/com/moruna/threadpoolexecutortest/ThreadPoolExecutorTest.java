package com.moruna.threadpoolexecutortest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest extends AppCompatActivity {
    private static final String TAG = "ThreadPoolExecutorTest";
    private static final String TASK_A = "TASK-A";
    private static final String TASK_B = "TASK-B";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_pool_executor_test);
    }

    public void startTaskA(View v) {
        Log.e(TAG, "执行任务A");
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + Thread.currentThread());
            }
        }, TASK_A, 1, TimeUnit.SECONDS);
    }

    public void startTaskB(View v) {
        Log.e(TAG, "执行任务B");
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "rate-run: " + Thread.currentThread());
            }
        }, TASK_B, 1, 4, TimeUnit.SECONDS);
    }

    public void cancelTaskA(View v) {
        Log.e(TAG, "取消任务A");
        ThreadPoolManager.getInstance().cancelTask(TASK_A, true);
    }

    public void cancelTaskB(View v) {
        Log.e(TAG, "取消任务B");
        ThreadPoolManager.getInstance().cancelTask(TASK_B, true);
    }

    public void cancelTaskAll(View v) {
        Log.e(TAG, "取消全部任务");
        ThreadPoolManager.getInstance().cancelAllTasks();
    }
}
