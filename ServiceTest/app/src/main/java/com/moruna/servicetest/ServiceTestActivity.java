package com.moruna.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ServiceTestActivity extends AppCompatActivity {
    private static final String TAG = "ServiceTestActivity";
    private TestService.TestBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
        Log.e(TAG, "onCreate: process id = "+ Process.myPid() );
    }

    public void start_service(View view) {
        startService(new Intent(this, TestService.class));
    }

    public void stop_service(View view) {
        stopService(new Intent(this, TestService.class));
    }

    public void bind_service(View view) {
        bindService(new Intent(this, TestService.class), connection, BIND_AUTO_CREATE);
    }

    public void unbind_service(View view) {
        unbindService(connection);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (TestService.TestBinder) service;
            binder.startTask();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
