package com.moruna.aidlservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class AIDLServiceActivity extends AppCompatActivity {
    private static final String TAG = "AIDLServiceActivity";
    private IAIDLService aidlService;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlService = IAIDLService.Stub.asInterface(service);
            try {
                String str = aidlService.toUpperCase("aidl service test");
                Log.e(TAG, "onServiceConnected: str=" + str);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidlservice);
        Log.e(TAG, "onCreate: process id = " + Process.myPid());
    }

    public void bind_service(View view) {
        bindService(new Intent(this, TestService.class), connection, BIND_AUTO_CREATE);
    }

    public void unbind_service(View view) {
        //需要先判断是否已经bindService，不然程序会崩溃
        try {
            unbindService(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
